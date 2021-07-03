package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.view.ConsoleService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AccountService {

    //private final ConsoleService console = new ConsoleService();
    //public static String AUTH_TOKEN = "";
    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;

    public AccountService(String url, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        API_BASE_URL = url;
    }

    public BigDecimal getBalance() throws RestClientException {
        BigDecimal balance = new BigDecimal(0);
        //try {
        balance = restTemplate.exchange(API_BASE_URL + "balance/", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();

//        } catch (RestClientResponseException e) {
//
//
//        }
        return balance;

    }
    public User[] getUsers() {
        User[] user = null;
        try {
            user = restTemplate.exchange(API_BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (RestClientException e) {
            System.out.println("Cannot retrieve users");
        }
        return user;
    }

    public String findUserNameByAccountID(Long account_id) throws RestClientException {
        return restTemplate.exchange(API_BASE_URL + "user/account/" + account_id, HttpMethod.GET, makeAuthEntity(), String.class ).getBody();
    }

    public Map<Long, String> getMapOfUsersWithIds() {
        return restTemplate.exchange(API_BASE_URL + "usernames", HttpMethod.GET, makeAuthEntity(), Map.class).getBody();
    }

    public Long getAccountIdFromUserId(Long user_id) {
        return restTemplate.exchange(API_BASE_URL + "/account/user/" + user_id, HttpMethod.GET, makeAuthEntity(), Long.class).getBody();
    }


    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
