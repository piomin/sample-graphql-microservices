package pl.piomin.services.employee.resolver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.repository.EmployeeRepository;

@Controller
public class EmployeeQueries {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeQueries.class);

	@Autowired
	EmployeeRepository repository;

	@QueryMapping
	public List<Employee> employees() {
		LOGGER.info("Employees find");
		return repository.findAll();
	}

	@QueryMapping
	public List<Employee> employeesByOrganization(@Argument Long organizationId) {
		LOGGER.info("Employees find: organizationId={}", organizationId);
		return repository.findByOrganization(organizationId);
	}

	@QueryMapping
	public List<Employee> employeesByDepartment(@Argument Long departmentId) {
		LOGGER.info("Employees find: departmentId={}", departmentId);
		return repository.findByDepartment(departmentId);
	}

	@QueryMapping
	public Employee employee(@Argument Long id) {
		LOGGER.info("Employee find: id={}", id);
		return repository.findById(id);
	}

}
