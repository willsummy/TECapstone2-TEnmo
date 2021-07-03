package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.model.TransferModel;

import java.util.List;

public class TransferService {
    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;

    public TransferService(String url, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        BASE_URL = url;
    }

//    public void requestBucks() {
//        User[] users = null;
//        TransferService transfer = new TransferService();
//        try {
//            users = restTemplate.exchange(BASE_URL + "LIST---USERS", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
//            for (User user : users) {
//                if (user.getId() != authenticatedUser.getUser().getId()) {
//                    System.out.println(user.getId() + "    " + user.getUsername());
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // send transfer method, http get method to API
    public TransferModel[] listTransfers() throws RestClientException {
        return restTemplate.exchange(BASE_URL + "list_transfers", HttpMethod.GET, makeAuthEntity(), TransferModel[].class).getBody();
    }

        private HttpEntity makeAuthEntity() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authenticatedUser.getToken());
            HttpEntity entity = new HttpEntity<>(headers);
            return entity;
        }
    }



