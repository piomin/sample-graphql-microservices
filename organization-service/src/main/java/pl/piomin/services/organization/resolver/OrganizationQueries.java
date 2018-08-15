package pl.piomin.services.organization.resolver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import pl.piomin.services.department.client.EmployeeClient;
import pl.piomin.services.department.model.Department;
import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.repository.OrganizationRepository;

@Component
public class OrganizationQueries implements GraphQLQueryResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationQueries.class);
	
	@Autowired
	EmployeeClient employeeClient;
	@Autowired
	OrganizationRepository repository;
	
	public List<Organization> organizations() {
		LOGGER.info("Organization find");
		return repository.findAll();
	}
	
	public List<Department> departmentsByOrganization(Long organizationId) {
		LOGGER.info("Departments find: organizationId={}", organizationId);
		return repository.findByOrganization(organizationId);
	}

	public List<Department> departmentsByOrganizationWithEmployees(Long organizationId) {
		LOGGER.info("Departments find: organizationId={}", organizationId);
		List<Department> departments = repository.findByOrganization(organizationId);
		for (int i = 0; i < departments.size(); i++) {
			try {
				departments.get(i).setEmployees(employeeClient.findByDepartment(departments.get(i).getId()));
			} catch (InterruptedException e) {
				LOGGER.error("Error calling employee-service", e);
			}
		}
		return departments;
	}
	
	public Organization organization(Long id) {
		LOGGER.info("Organization find: id={}", id);
		return repository.findById(id);
	}
	
}
