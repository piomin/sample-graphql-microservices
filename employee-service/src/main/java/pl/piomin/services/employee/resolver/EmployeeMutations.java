package pl.piomin.services.employee.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.repository.EmployeeRepository;

@Controller
public class EmployeeMutations {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeMutations.class);

	@Autowired
	EmployeeRepository repository;

	@MutationMapping
	public Employee newEmployee(@Argument Employee employee) {
		LOGGER.info("Employee add: employee={}", employee);
		return repository.add(employee);
	}

	@MutationMapping
	public boolean deleteEmployee(@Argument Long id) {
		LOGGER.info("Employee delete: id={}", id);
		return repository.delete(id);
	}

	@MutationMapping
	public Employee updateEmployee(@Argument Long id, @Argument Employee employee) {
		LOGGER.info("Employee update: id={}, employee={}", id, employee);
		return repository.update(id, employee);
	}

}
