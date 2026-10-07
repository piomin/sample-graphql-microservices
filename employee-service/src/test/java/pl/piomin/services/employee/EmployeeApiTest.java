package pl.piomin.services.employee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureGraphQlTester
@TestPropertySource(properties = {
    "eureka.client.enabled=false"
})
public class EmployeeApiTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@Test
	public void testEmployeesQuery() {
		graphQlTester.document("{ employees { id name position } }")
				.execute()
				.path("employees")
				.entityList(Object.class)
				.hasSizeGreaterThan(0);
	}

}
