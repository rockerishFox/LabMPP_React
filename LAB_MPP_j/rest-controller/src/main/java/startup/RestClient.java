package startup;

import domain.Meci;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class RestClient {
    public static final String URL = "http://localhost:8080/rest";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Meci[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Meci[].class));
    }

    public Meci getById(String id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Meci.class));
    }

    public Meci create(Meci meci) {
        return execute(() -> restTemplate.postForObject(URL, meci, Meci.class));
    }

    public void update(Meci meci, Integer id) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, id), meci);
            return null;
        });
    }

    public void delete(Integer id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}
