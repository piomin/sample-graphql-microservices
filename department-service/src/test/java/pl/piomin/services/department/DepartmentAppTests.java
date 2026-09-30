package pl.piomin.services.department;

import com.apollographql.apollo3.ApolloClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.piomin.services.department.clientoperation.DepartmentsByOrganizationQuery;
import pl.piomin.services.department.clientoperation.DepartmentsQuery;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DepartmentAppTests {

    @LocalServerPort
    int port;

    @Test
    public void departments() {
        ApolloClient client = new ApolloClient.Builder()
                .serverUrl("http://localhost:" + port + "/graphql")
                .build();
        client.query(new DepartmentsQuery());
    }

    @Test
    public void departmentsByOrganization() {
        ApolloClient client = new ApolloClient.Builder()
                .serverUrl("http://localhost:" + port + "/graphql")
                .build();
        client.query(new DepartmentsByOrganizationQuery());
    }
}
