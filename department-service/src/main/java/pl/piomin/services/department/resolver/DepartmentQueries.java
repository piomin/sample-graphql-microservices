package pl.piomin.services.department.resolver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import pl.piomin.services.department.client.EmployeeClient;
import pl.piomin.services.department.model.Department;
import pl.piomin.services.department.repository.DepartmentRepository;

@Controller
public class DepartmentQueries {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentQueries.class);

	@Autowired
	EmployeeClient employeeClient;
	@Autowired
	DepartmentRepository repository;

	@QueryMapping
	public List<Department> departments() {
		LOGGER.info("Departments find");
		return repository.findAll();
	}

	@QueryMapping
	public List<Department> departmentsByOrganization(@Argument Long organizationId) {
		LOGGER.info("Departments find: organizationId={}", organizationId);
		return repository.findByOrganization(organizationId);
	}

	@QueryMapping
	public List<Department> departmentsByOrganizationWithEmployees(@Argument Long organizationId) {
		LOGGER.info("Departments find: organizationId={}", organizationId);
		List<Department> departments = repository.findByOrganization(organizationId);
		for (int i = 0; i < departments.size(); i++) {
			departments.get(i).setEmployees(employeeClient.findByDepartment(departments.get(i).getId()));
		}
		return departments;
	}

	@QueryMapping
	public Department department(@Argument Long id) {
		LOGGER.info("Department find: id={}", id);
		return repository.findById(id);
	}

}
