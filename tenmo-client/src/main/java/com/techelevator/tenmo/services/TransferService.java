package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferModel;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class TransferService {
    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;

    public TransferService(String url, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        API_BASE_URL = url;
    }

    public void sendBucks() {
        User[] users = null;
        TransferModel transfer = new TransferModel();
        try {
            Scanner scanner = new Scanner(System.in);
            users = restTemplate.exchange(API_BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            System.out.println("-------------------------------------------");
            System.out.println("Users");
            System.out.println("ID    Name");
            System.out.println("-------------------------------------------");
            for (User user : users) {
                if (user.getId() != authenticatedUser.getUser().getId()) {
                    System.out.println(user.getId() + "    " + user.getUsername());
                }
            }
            System.out.println("Enter ID of user you are sending to (0 to cancel): ");
            transfer.setAccount_to(Long.parseLong(scanner.nextLine()));
            transfer.setAccount_from(authenticatedUser.getUser().getId());
            if (transfer.getAccount_to() != 0) {
                System.out.print("Enter amount: ");
                try {
                    transfer.setAmount(new BigDecimal(Double.parseDouble(scanner.nextLine())));
                } catch (Exception e) {
                    System.out.println("Please enter proper format for amount for transfer");
                }
                String transfers = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
                System.out.println(transfers);
            }
        } catch (Exception e) {
            System.out.println("Error in system");
        }
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



