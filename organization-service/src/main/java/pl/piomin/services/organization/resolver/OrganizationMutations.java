package pl.piomin.services.organization.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.repository.OrganizationRepository;


@Component
public class OrganizationMutations implements GraphQLMutationResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationMutations.class);
	
	@Autowired
	OrganizationRepository repository;
	
	public Organization newOrganization(Organization department) {
		LOGGER.info("Organization add: department={}", department);
		return repository.add(department);
	}
	
	public boolean deleteOrganization(Long id) {
		LOGGER.info("Organization delete: id={}", id);
		return repository.delete(id);
	}
	
	public Organization updateOrganization(Long id, Organization department) {
		LOGGER.info("Organization update: id={}, department={}", id, department);
		return repository.update(id, department);
	}
	
}
