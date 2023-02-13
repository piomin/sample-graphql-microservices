package pl.piomin.services.employee;

import com.apollographql.apollo.ApolloCall.Callback;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import pl.piomin.services.employee.model.EmployeesQuery;
import pl.piomin.services.employee.model.EmployeesQuery.Data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class EmployeeApiTest {

	@LocalServerPort
	int port;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeApiTest.class);
	
	private CountDownLatch lock = new CountDownLatch(1);
	
	@Test
	public void testClient() throws InterruptedException {
		ApolloClient client = ApolloClient.builder().serverUrl("http://localhost:" + port + "/graphql").build();
		client.query(EmployeesQuery.builder().build()).enqueue(new Callback<EmployeesQuery.Data>() {

			@Override
			public void onFailure(ApolloException arg0) {
				LOGGER.error("Error", arg0);
				lock.countDown();
			}

			@Override
			public void onResponse(Response<Data> res) {
				LOGGER.info("Res: {}", res.data().employees());
				lock.countDown();
			}
		});
		lock.await(10000, TimeUnit.MILLISECONDS);
	}
	
}
