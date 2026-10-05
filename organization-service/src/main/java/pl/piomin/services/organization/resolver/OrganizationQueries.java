package pl.piomin.services.organization.resolver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import pl.piomin.services.organization.client.EmployeeClient;
import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.repository.OrganizationRepository;

@Controller
public class OrganizationQueries {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationQueries.class);

	@Autowired
	EmployeeClient employeeClient;
	@Autowired
	OrganizationRepository repository;

	@QueryMapping
	public List<Organization> organizations() {
		LOGGER.info("Organization find");
		return repository.findAll();
	}

	@QueryMapping
	public Organization organizationByIdWithEmployees(@Argument Long id) throws InterruptedException {
		LOGGER.info("Organizations find: id={}", id);
		Organization organization = repository.findById(id);
		organization.setEmployees(employeeClient.findByOrganization(id));
		return organization;
	}

	@QueryMapping
	public Organization organization(@Argument Long id) {
		LOGGER.info("Organization find: id={}", id);
		return repository.findById(id);
	}

	@QueryMapping
	public Organization organizationByIdWithDepartments(@Argument Long id) {
		LOGGER.info("Organization find with departments: id={}", id);
		return repository.findById(id);
	}

	@QueryMapping
	public Organization organizationByIdWithDepartmentsAndEmployees(@Argument Long id) throws InterruptedException {
		LOGGER.info("Organization find with departments and employees: id={}", id);
		Organization organization = repository.findById(id);
		organization.setEmployees(employeeClient.findByOrganization(id));
		return organization;
	}

}
