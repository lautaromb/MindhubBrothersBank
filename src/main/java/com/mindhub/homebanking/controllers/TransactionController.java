package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("clients/current/accounts")
    public Set<AccountDTO> getAll(Authentication authentication){
        ClientDTO client = new ClientDTO(clientRepository.findByEmail(authentication.getName())) ;
        Set<AccountDTO> accounts = client.getAccounts();
        return accounts;
    };

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam Double amount,
    @RequestParam String description, @RequestParam String numberSend, @RequestParam String numberReceiver){

        Account accountSender = accountRepository.findByNumber(numberSend);
        Account accountReceiver = accountRepository.findByNumber(numberReceiver);
        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Account> clientAccounts = client.getAccounts();

        double balanceAccountSender = accountSender.getBalance();
        double balanceAccountReceiver = accountReceiver.getBalance();




        if(accountSender.isActive() == false){
            return new ResponseEntity<>("Origin account is no longer active", HttpStatus.FORBIDDEN);
        }

        if(!accountReceiver.isActive()){
            return new ResponseEntity<>("Destiny account is no longer active", HttpStatus.FORBIDDEN);
        }

        if ( amount == null || description == null || numberSend == null || numberReceiver == null){
        return new ResponseEntity<>("One or many empty parameters", HttpStatus.FORBIDDEN);
            }

        if ((accountSender == null) || (accountReceiver == null)){
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(numberSend.equals(numberReceiver)){
        return new ResponseEntity<>("Can't make transactions to same account", HttpStatus.FORBIDDEN);
            }

        if(balanceAccountSender < amount){
            return new ResponseEntity<>("Don't have enough funds", HttpStatus.FORBIDDEN);
        }

        if( !clientAccounts.contains(accountSender)){
            return new ResponseEntity<>("Don't have permissions over this account", HttpStatus.FORBIDDEN);
        }


        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, description + accountSender.getNumber(), LocalDateTime.now(), amount, accountSender);
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, description + accountReceiver.getNumber(), LocalDateTime.now(), amount, accountReceiver);


        balanceAccountSender -= amount;
        balanceAccountReceiver += amount;

        accountSender.setBalance(balanceAccountSender);
        accountReceiver.setBalance(balanceAccountReceiver);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
