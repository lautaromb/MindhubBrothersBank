package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;


@RequestMapping("/api")
@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientServices clientServices;
    @Autowired
    private AccountServices accountServices;

//    @GetMapping("/clients/current/accounts")
//    public List<AccountDTO> getAccountsDTO(){
//        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
//    };



    @GetMapping("/clients/current/accounts/{id}")
    public List<AccountDTO>  getAccountDTO(@PathVariable Long id, Authentication authentication){
//        Client client = clientRepository.findByEmail(authentication.getName());
//        Account account = accountRepository.findById(id).orElse(null);
        Client client = clientServices.findClientByEmail(authentication.getName());
        Account account = accountServices.findById(id);

        List<Account> filterAccount = client.getAccounts().stream().filter(account1 -> account1.getId() == id).collect(toList());

        List<AccountDTO> accountDTOS = filterAccount.stream().map(AccountDTO::new).collect(toList());
        List<AccountDTO> accountsDTO  = accountDTOS.stream().filter(account1 ->  account1.isActive()).collect(toList());
        //AccountDTO accountDTO = new AccountDTO(accountRepository.findById(id).orElse(null));

        return accountsDTO;
    }

//    @GetMapping("/clients/current/accounts")
//    public AccountDTO getAccountDTO(Authentication authentication){
//        Client client = clientRepository.findByEmail(authentication.getName());
//        AccountDTO accountDTO = new AccountDTO(accountRepository.findById(id).orElse(null));
//
//        return accountDTO;
//    }

    @GetMapping("/clients/current/accounts/")
    public List<AccountDTO>  getAccounts(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        List<AccountDTO> account = client.getAccounts().stream().map(AccountDTO::new).collect(toList());
        List<AccountDTO> accountsDTO  = account.stream().filter(account1 ->  account1.isActive()).collect(toList());
        //AccountDTO accountDTO = new AccountDTO(accountRepository.findById(id).orElse(null));
        return accountsDTO;
    }


    //----

    int min = 10000000;
    int max = 99999999;
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public String getStringRandomNumber(){
        int randomNumber = getRandomNumber(min,max);
        return  String.valueOf(randomNumber);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {

    Client client = clientRepository.findByEmail(authentication.getName());
//        List<Card> cardList  = client.getCards().stream().filter(card -> { return card.getTypeCard() == cardType; }).collect(toList());
        List<Account> accountList  = client.getAccounts().stream().filter(account -> { return account.isActive(); }).collect(toList());

        if (accountList.size() >= 3) {
            return new ResponseEntity<>("Can't create more accounts", HttpStatus.FORBIDDEN);
        }

        //String number = getStringRandomNumber();

        Account account = new Account( LocalDateTime.now(), 0, client, AccountType.SAVING);

        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PatchMapping("/clients/current/accounts/delete/{id}")
    public ResponseEntity<Object> smartDelete(@PathVariable Long id){
        Account account = accountRepository.findById(id).orElse(null);

        if(account.getBalance() > 0){
            return new ResponseEntity<>("Can't delete an account with positive balance", HttpStatus.FORBIDDEN);
        }


        account.setActive(false);
        accountRepository.save(account);

        return new ResponseEntity<>("Account deleted", HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/accounts/change/{id}")
    public ResponseEntity<Object> smartChange(@PathVariable Long id){
        Account account = accountRepository.findById(id).orElse(null);

        if(account.getAccountType() == AccountType.CURRENT){
            account.setAccountType(AccountType.SAVING);
        }else{
            account.setAccountType(AccountType.CURRENT);
        }

        accountRepository.save(account);

        return new ResponseEntity<>("Account STATUS MODIFIED", HttpStatus.CREATED);
    }

}
