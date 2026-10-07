package pl.piomin.services.department.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

import pl.piomin.services.department.model.Employee;

@Component
public class EmployeeClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeClient.class);
	private static final String SERVICE_NAME = "EMPLOYEE-SERVICE";
	private static final String SERVER_URL = "http://localhost:%d/graphql";

	Random r = new Random();

	@Autowired
	private EurekaClient discoveryClient;

	public List<Employee> findByDepartment(Long departmentId) {
		Application app = discoveryClient.getApplication(SERVICE_NAME);
		InstanceInfo ii = app.getInstances().get(r.nextInt(app.size()));
		String url = String.format(SERVER_URL, ii.getPort());
		String query = """
				{ "query": "{ employeesByDepartment(departmentId: %d) { id name position salary } }" }
				""".formatted(departmentId);
		RestClient restClient = RestClient.create();
		try {
			Map<String, Object> response = restClient.post()
					.uri(url)
					.contentType(MediaType.APPLICATION_JSON)
					.body(query)
					.retrieve()
					.body(new ParameterizedTypeReference<Map<String, Object>>() {});
			if (response != null && response.get("data") != null) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> employeesData = (List<Map<String, Object>>) ((Map<String, Object>) response.get("data")).get("employeesByDepartment");
				List<Employee> employees = new ArrayList<>();
				if (employeesData != null) {
					for (Map<String, Object> emp : employeesData) {
						Employee employee = new Employee(
								Long.valueOf(emp.get("id").toString()),
								(String) emp.get("name"),
								(String) emp.get("position"),
								emp.get("salary") instanceof Integer ? (Integer) emp.get("salary") : 0
						);
						employees.add(employee);
					}
				}
				return employees;
			}
		} catch (Exception e) {
			LOGGER.error("Error calling employee-service", e);
		}
		return new ArrayList<>();
	}

}
