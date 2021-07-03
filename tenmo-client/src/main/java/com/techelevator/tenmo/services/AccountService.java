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

    // method listing users
    /*
        this needs to call the resttemplate for list users path
        use get, makeAuthEntity, and User.class
        returns list of Users
     */

    public List<User> listUserAccounts() throws RestClientException {
        List<User> users = restTemplate.exchange(API_BASE_URL + "list_users", HttpMethod.GET, makeAuthEntity(), List.class).getBody();
        return users;
    }




    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
