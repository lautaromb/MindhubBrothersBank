package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.utils.CardUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String number;
    LocalDateTime creationDate = LocalDateTime.now();
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    //transaction
    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    private boolean active = true;
    private AccountType accountType;

//    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
//    private Set<Card> cardsSet = new HashSet<>();


    public Account() {
    }

    public Account( LocalDateTime creationDate, double balance, Client client, AccountType accountType) {
        this.number = "VIN" + CardUtils.getRandomNumber(00000000,99999999);
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.accountType = accountType;
    }

    public Account(String number, LocalDateTime creationDate, double balance, Client client, AccountType accountType) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.accountType = accountType;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    //set & get

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    //One to Many


    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransactions(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }

//    public Set<Card> getCardsSet() {
//        return cardsSet;
//    }
//
//    public void setCardsSet(Set<Card> cardsSet) {
//        this.cardsSet = cardsSet;
//    }


}
