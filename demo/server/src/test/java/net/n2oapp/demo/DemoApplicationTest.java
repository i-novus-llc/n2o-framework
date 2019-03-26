package net.n2oapp.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTest {
    @LocalServerPort
    private int port;

    @Test
    public void config() {
        RestTemplate restTemplate = new RestTemplate();
        Map<?, ?> config = restTemplate.getForObject("http://localhost:" + port + "/n2o/config", Map.class);
        assertThat(config.get("menu"), notNullValue());
    }

    @Test
    public void pageWelcome() {
        RestTemplate restTemplate = new RestTemplate();
        Map<?, ?> page = restTemplate.getForObject("http://localhost:" + port + "/n2o/page/", Map.class);
        assertThat(page.get("layout"), notNullValue());

        Map<?, ?> result = restTemplate.getForObject("http://localhost:" + port + "/n2o/data/patients?size=10&page=1&sorting.birthday=ASC", Map.class);
        assertThat(result.get("list"), notNullValue());
        assertThat((Integer) result.get("count"), greaterThan(1));
        List<Map<?, ?>> list = (List<Map<?, ?>>) result.get("list");
        assertThat(list.size(), greaterThan(0));

        result = restTemplate.getForObject("http://localhost:" + port + "/n2o/data/patients/1/contacts?size=10&page=1&individualId=1", Map.class);
        assertThat(result.get("list"), notNullValue());
        assertThat((Integer) result.get("count"), greaterThan(1));
        list = (List<Map<?, ?>>) result.get("list");
        assertThat(list.size(), lessThan(10));
    }

    @Test
    public void pageProto() {
        RestTemplate restTemplate = new RestTemplate();
        Map<?, ?> page = restTemplate.getForObject("http://localhost:" + port + "/n2o/page/proto", Map.class);
        assertThat(page.get("layout"), notNullValue());

        Map<?, ?> result = restTemplate.getForObject("http://localhost:" + port + "/n2o/data/proto/patients", Map.class);
        assertThat(result.get("list"), notNullValue());
        List<Map<?, ?>> list = (List<Map<?, ?>>) result.get("list");
        assertThat(list.size(), greaterThan(0));
    }

    @Test
    public void create() {
        RestTemplate restTemplate = new RestTemplate();
        Map<?, ?> page = restTemplate.getForObject("http://localhost:" + port + "/n2o/page/patients/create", Map.class);
        assertThat(page.get("layout"), notNullValue());
        assertThat(((Map) ((Map) page.get("layout")).get("regions")).get("single"), notNullValue());
    }

    @Test
    public void update() {
        RestTemplate restTemplate = new RestTemplate();
        Map<?, ?> page = restTemplate.getForObject("http://localhost:" + port + "/n2o/page/patients/1/update", Map.class);
        assertThat(page.get("layout"), notNullValue());
        assertThat(((Map) ((Map) page.get("layout")).get("regions")).get("single"), notNullValue());

        Map<?, ?> data = restTemplate.getForObject("http://localhost:" + port + "/n2o/data/patients/1/update/main", Map.class);
        assertThat(((List) data.get("list")).size(), is(1));
    }
}
