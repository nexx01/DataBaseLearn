package com.example.batchinsertspringbootspringbatch.base.dto;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

@SuppressWarnings("restriction")
@XmlRootElement()
public class Transaction {
    private String username;
    private int userId;
    private int age;
    private String postCode;
    private LocalDateTime transactionDate;
    private double amount;

    @Override
    public String toString() {
        return "Transaction{" +
                "username='" + username + '\'' +
                ", userId=" + userId +
                ", transactionDate=" + transactionDate +
                ", amount=" + amount +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
