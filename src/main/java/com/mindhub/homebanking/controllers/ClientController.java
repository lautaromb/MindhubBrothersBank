package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;



@RequestMapping("/api")
@RestController
public class ClientController {

//    @Autowired
//    private ClientRepository clientRepository;

//    @Autowired
//    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientServices clientServices;

    @Autowired
    private AccountServices accountServices;



    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientServices.getClients();
    };

    @RequestMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
//        ClientDTO client = new ClientDTO(clientRepository.findById(id).orElse(null));
//        return client;
        return clientServices.findClientById(id);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getClientByEmail(Authentication authentication) {

        //return clientRepository.findByEmail(authentication.ge);
        ClientDTO client = new ClientDTO(clientServices.findClientByEmail(authentication.getName()));
        return client;

    }

    //Task6 pag 8

    //

    //


    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (clientServices.findClientByEmail(email) !=  null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }



        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = new Account( LocalDateTime.now(), 0, client, AccountType.CURRENT);



        clientServices.saveClient(client);
        accountServices.saveAccount(account);


        return new ResponseEntity<>(HttpStatus.CREATED);

    }



}

