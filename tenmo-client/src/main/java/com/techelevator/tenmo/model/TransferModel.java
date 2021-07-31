package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferModel {
    private Long transfer_id;
    private Long transfer_type_id;
    private Long transfer_status_id;
    private Long sender_account;
    private Long sender_id;
    private String sendername;
    private Long receiver_account;
    private Long receiver_id;
    private String receivername;
    private BigDecimal amount;

    public Long getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(Long transfer_id) {
        this.transfer_id = transfer_id;
    }

    public Long getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(Long transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public Long getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(Long transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public Long getSender_account() {
        return sender_account;
    }

    public void setSender_account(Long sender_account) {
        this.sender_account = sender_account;
    }

    public Long getSender_id() {
        return sender_id;
    }

    public void setSender_id(Long sender_id) {
        this.sender_id = sender_id;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public Long getReceiver_account() {
        return receiver_account;
    }

    public void setReceiver_account(Long receiver_account) {
        this.receiver_account = receiver_account;
    }

    public Long getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(Long receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getReceivername() {
        return receivername;
    }

    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}



