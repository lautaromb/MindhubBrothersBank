package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/payment")
    public ResponseEntity<Object> paymentDTOList(@RequestBody PaymentDTO paymentDTO, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Card card = cardRepository.findByNumber(paymentDTO.getNumber());
        List<Account> accountList = client.getAccounts().stream().collect(Collectors.toList());
        List<Account> accountBalance = accountList.stream().filter(account -> account.getBalance() > paymentDTO.getAmount()).collect(Collectors.toList());
        Account account = accountBalance.stream().findFirst().orElse(null);

        //Account account = accountRepository.findById(card.getAccount().getId()).orElse(null);
        //List<Card> cardList = client.getCards().stream().filter(card1 -> card.getNumber() == paymentDTO.getNumber()).collect(Collectors.toList());




        if(paymentDTO == null ){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(paymentDTO.getNumber().isEmpty() || paymentDTO.getCvv() != card.getCvv() || paymentDTO.getAmount() < 0 || paymentDTO.getDescription().isEmpty()){
            return new ResponseEntity<>("Payment data is incorrect", HttpStatus.FORBIDDEN);
        }



        if(paymentDTO.getName() == client.getFirstName() + " " + client.getLastName()){
            return new ResponseEntity<>("Name doesn't match", HttpStatus.FORBIDDEN);
        }

        String cardThruDate = card.getThruDate().toString().substring(5, 7) + '/' + card.getThruDate().toString().substring(0,4);
        String paymentThruDate = paymentDTO.getThruDate().toString();
//        System.out.println("card" + cardThruDate);
//        System.out.println("payment" + paymentThruDate);
        System.out.println(cardThruDate.equals(paymentThruDate) );

        if(!cardThruDate.equals(paymentThruDate)){
            return new ResponseEntity<>("Thru date is incorrect", HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("Missing Account", HttpStatus.FORBIDDEN);
        }

        if(account.getBalance() < paymentDTO.getAmount()){
            return new ResponseEntity<>("Founds insufficient", HttpStatus.FORBIDDEN);
        }

        if(!card.isGood() && !account.isActive()){
            return new ResponseEntity<>("Account or card is no longer good", HttpStatus.FORBIDDEN);
        }

        //Card Number is an existing number
//        if(!cardList.contains(paymentDTO.getNumber())){
//           return new ResponseEntity<>("Card number is incorrect", HttpStatus.FORBIDDEN);
//       }


        if(card.getThruDate().isBefore(LocalDateTime.now())){
            return new ResponseEntity<>("Card expired", HttpStatus.FORBIDDEN);
        }


        Transaction transaction = new Transaction(TransactionType.DEBIT, paymentDTO.getDescription(), LocalDateTime.now(), paymentDTO.getAmount(), account);
         account.setBalance(account.getBalance() - paymentDTO.getAmount());
        transactionRepository.save(transaction);
        accountRepository.save(account);

        return new ResponseEntity<>("Transaction complete", HttpStatus.ACCEPTED);


    }


}
