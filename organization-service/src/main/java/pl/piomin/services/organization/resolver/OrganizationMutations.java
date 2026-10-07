package pl.piomin.services.organization.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import pl.piomin.services.organization.model.Organization;
import pl.piomin.services.organization.repository.OrganizationRepository;


@Controller
public class OrganizationMutations {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationMutations.class);

	@Autowired
	OrganizationRepository repository;

	@MutationMapping
	public Organization newOrganization(@Argument Organization organization) {
		LOGGER.info("Organization add: organization={}", organization);
		return repository.add(organization);
	}

	@MutationMapping
	public boolean deleteOrganization(@Argument Long id) {
		LOGGER.info("Organization delete: id={}", id);
		return repository.delete(id);
	}

	@MutationMapping
	public Organization updateOrganization(@Argument Long id, @Argument Organization organization) {
		LOGGER.info("Organization update: id={}, organization={}", id, organization);
		return repository.update(id, organization);
	}

}
