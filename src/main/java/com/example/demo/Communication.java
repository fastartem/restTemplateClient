package com.example.demo;

import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Communication {
    @Autowired
    private RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers = new HttpHeaders();
    private final String URL = "http://91.241.64.178:7081/api/users";
    private final String DELETE_URL = "http://91.241.64.178:7081/api/users/3";

    public String concatAllFragments() {
        //first request
        getAllUsersAndSetCookie();

        //first fragment
        User user = new User(3L, "James", "Brown", (byte) 20);
        String first = getFirstFragment(user);

        //second fragment
        user.setName("Thomas");
        user.setLastName("Shelby");
        String second = first.concat(getSecondFragment(user));

        //third fragment
        return second.concat(getThirdFragment(user));
    }

    private void getAllUsersAndSetCookie() {
        ResponseEntity<String> entity = restTemplate.getForEntity(URL, String.class);
        String cookie = entity.getHeaders().getFirst("Set-Cookie");
        headers.set("Cookie", cookie);

        System.out.println(entity.getBody());
    }

    private String getThirdFragment(User user) {
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(DELETE_URL, HttpMethod.DELETE, entity, String.class);

        return response.getBody();
    }

    private String getSecondFragment(User user) {
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);

        return response.getBody();
    }

    private String getFirstFragment(User user) {
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
