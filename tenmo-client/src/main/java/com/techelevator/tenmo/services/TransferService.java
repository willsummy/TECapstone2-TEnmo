package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class TransferService {
    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;

    public TransferService(String url, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        API_BASE_URL = url;
    }

    public boolean sendBucks(TransferModel transfer) {
        return restTemplate.exchange(API_BASE_URL + "transfer/send", HttpMethod.POST, makeTransferEntity(transfer), Boolean.class).getBody();
    }

    public boolean requestBucks(TransferModel transfer) {
        return restTemplate.exchange(API_BASE_URL + "transfer/request", HttpMethod.POST, makeTransferEntity(transfer), Boolean.class).getBody();
    }

    // send transfer method, http get method to API
        return restTemplate.exchange(API_BASE_URL + "transfers/list", HttpMethod.GET, makeAuthEntity(), TransferModel[].class).getBody();
    }


    private HttpEntity<TransferModel> makeTransferEntity(TransferModel transferModel) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<TransferModel> entity = new HttpEntity<>(transferModel, headers);
        return entity;
    }



    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}



