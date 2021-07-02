package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.view.ConsoleService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    //private final ConsoleService console = new ConsoleService();
    //public static String AUTH_TOKEN = "";
    private String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;

    public AccountService(String url, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        API_BASE_URL = url;
    }

    public BigDecimal getBalance() {
        BigDecimal balance = new BigDecimal(0);
        try {
            balance = restTemplate.exchange(API_BASE_URL + "balance" + authenticatedUser.getUser().getId(),
                    HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
            System.out.println("Your current account balance is : $" + balance);
        } catch (RestClientResponseException e) {
            System.out.println("Cannot get balance.");

        }
        return balance;

    }





    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
