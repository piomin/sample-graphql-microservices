package pl.piomin.services.department;

import com.netflix.discovery.EurekaClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "eureka.client.enabled=false",
    "spring.cloud.config.enabled=false"
})
public class DepartmentAppTests {

    @MockBean
    private EurekaClient eurekaClient;

    @Test
    public void contextLoads() {
    }
}
