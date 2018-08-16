package pl.piomin.services.organization.resolver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import pl.piomin.services.organization.client.EmployeeClient;
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
	
	public Organization organizationByIdWithEmployees(Long id) throws InterruptedException {
		LOGGER.info("Organizations find: id={}", id);
		Organization organization = repository.findById(id);
		organization.setEmployees(employeeClient.findByOrganization(id));
		return organization;
	}
	
	public Organization organization(Long id) {
		LOGGER.info("Organization find: id={}", id);
		return repository.findById(id);
	}
	
}
