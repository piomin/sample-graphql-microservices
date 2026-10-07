package pl.piomin.services.department.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import pl.piomin.services.department.model.Department;
import pl.piomin.services.department.repository.DepartmentRepository;

@Controller
public class DepartmentMutations {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentMutations.class);

	@Autowired
	DepartmentRepository repository;

	@MutationMapping
	public Department newDepartment(@Argument Department department) {
		LOGGER.info("Department add: department={}", department);
		return repository.add(department);
	}

	@MutationMapping
	public boolean deleteDepartment(@Argument Long id) {
		LOGGER.info("Department delete: id={}", id);
		return repository.delete(id);
	}

	@MutationMapping
	public Department updateDepartment(@Argument Long id, @Argument Department department) {
		LOGGER.info("Department update: id={}, department={}", id, department);
		return repository.update(id, department);
	}

}
