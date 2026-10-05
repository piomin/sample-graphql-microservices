package pl.piomin.services.department.client;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

import pl.piomin.services.department.model.Employee;

@Component
public class EmployeeClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeClient.class);
	private static final String SERVICE_NAME = "EMPLOYEE-SERVICE";
	private static final String SERVER_URL = "http://localhost:%d/graphql";

	private static final String QUERY_BY_DEPARTMENT = """
			query EmployeesByDepartment($departmentId: Int!) {
			  employeesByDepartment(departmentId: $departmentId) {
			    id
			    name
			    position
			    salary
			  }
			}
			""";

	Random r = new Random();

	@Autowired
	private EurekaClient discoveryClient;

	public List<Employee> findByDepartment(Long departmentId) {
		Application app = discoveryClient.getApplication(SERVICE_NAME);
		InstanceInfo ii = app.getInstances().get(r.nextInt(app.size()));
		String url = String.format(SERVER_URL, ii.getPort());

		HttpGraphQlClient client = HttpGraphQlClient.builder(
				WebClient.builder().baseUrl(url).build())
				.build();

		List<Employee> employees = client.document(QUERY_BY_DEPARTMENT)
				.variable("departmentId", departmentId.intValue())
				.retrieve("employeesByDepartment")
				.toEntityList(Employee.class)
				.doOnNext(list -> LOGGER.info("Res: {}", list))
				.onErrorResume(ex -> {
					LOGGER.error("Err: {}", ex.getMessage());
					return reactor.core.publisher.Mono.just(Collections.emptyList());
				})
				.block();

		return employees != null ? employees : Collections.emptyList();
	}

}
