package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.mindhub.homebanking.methods.RandomNum;
import com.mindhub.homebanking.utils.CardUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String cardholder;
    private CardType typeCard;
    private CardColor typeColor;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    private boolean good = true;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="account_ID")
//    private Account account;

    public Card() {
    }

    public Card(String cardholder, CardType typeCard, CardColor typeColor , String number, int cvv,LocalDateTime fromDate, LocalDateTime thruDate, Client client) {
        this.cardholder = cardholder;
        this.typeCard = typeCard;
        this.typeColor = typeColor;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.client = client;
        //this.account = account;
    }


    public long getId() {
        return id;
    }

    @JsonIgnore

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public CardType getTypeCard() {
        return typeCard;
    }

    public void setTypeCard(CardType typeCard) {
        this.typeCard = typeCard;
    }

    public CardColor getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(CardColor typeColor) {
        this.typeColor = typeColor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }



    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }
//
//    public Account getAccount() {
//        return account;
//    }
//
//    public void setAccount(Account account) {
//        this.account = account;
//    }
}
