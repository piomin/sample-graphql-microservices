package pl.piomin.services.organization.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pl.piomin.services.organization.model.Organization;

public class OrganizationRepository {

	private List<Organization> organizations = new ArrayList<>();
	
	public Organization add(Organization organization) {
		organization.setId((long) (organizations.size()+1));
		organizations.add(organization);
		return organization;
	}
	
	public Organization findById(Long id) {
		Optional<Organization> organization = organizations.stream().filter(a -> a.getId().equals(id)).findFirst();
		if (organization.isPresent())
			return organization.get();
		else
			return null;
	}
	
	public List<Organization> findAll() {
		return organizations;
	}
	
	public boolean delete(Long id) {
		return organizations.removeIf(it -> it.getId() == id.longValue());
	}
	
	public Organization update(Long id, Organization organization) {
		organization.setId(id);
		int index = organizations.indexOf(organization);
		organizations.set(index, organization);
		return organization;
	}
	
	
}
