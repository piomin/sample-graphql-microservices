package pl.piomin.services.employee;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;
import pl.piomin.services.employee.model.Employee;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeApiTest {

	@LocalServerPort
	int port;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeApiTest.class);

	private static final String QUERY_EMPLOYEES = """
			query EmployeesQuery {
			  employees {
			    name
			    age
			  }
			}
			""";

	@Test
	public void testClient() {
		HttpGraphQlClient client = HttpGraphQlClient.builder(
				WebClient.builder()
						.baseUrl("http://localhost:" + port + "/graphql")
						.build())
				.build();

		List<Employee> employees = client.document(QUERY_EMPLOYEES)
				.retrieve("employees")
				.toEntityList(Employee.class)
				.doOnNext(list -> LOGGER.info("Res: {}", list))
				.block();

		assertThat(employees).isNotNull();
		LOGGER.info("Employees count: {}", employees.size());
	}

}
