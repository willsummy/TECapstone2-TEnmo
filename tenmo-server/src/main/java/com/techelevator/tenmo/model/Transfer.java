package com.techelevator.tenmo.model;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Transfer {
    // many transfers for every account

    private Long transfer_id; // primary key
    @NotNull(message = "Type ID is mandatory")
    private Long transfer_type_id; // foreign key
    @NotNull(message = "Status ID is mandatory")
    private Long transfer_status_id; // foreign key
    @NotNull(message = "Sender ID is mandatory")
    private Long sender_account; // account_id fk
    private Long sender_id;
    private String sendername;
    @NotNull(message = "Receiver ID is mandatory")
    private Long receiver_account; // account_id fk
    private Long receiver_id;
    private String receivername;
    @NotNull(message = "Amount is mandatory")
    private BigDecimal amount;

    public Transfer() {}

    public Transfer(Long transfer_type_id, Long transfer_status_id, Long account_from, Long account_to, BigDecimal amount) {
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.sender_account = account_from;
        this.receiver_account = account_to;
        this.amount = amount;
    }

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
