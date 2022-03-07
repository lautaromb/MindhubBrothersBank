package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;


@RequestMapping("/api")
@RestController
public class LoanController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }

    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<String> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findByName(loanApplicationDTO.getLoanName());
        Account account = accountRepository.findByNumber(loanApplicationDTO.getNumberAccount());
        //ClientLoan clientLoan = clientLoanRepository.getById(client.getId());


        if(loanApplicationDTO == null ){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

//       if(!loanApplicationDTO.getLoanName().equals("Mortgage")||!loanApplicationDTO.getLoanName().equals("Personal")||!loanApplicationDTO.getLoanName().equals("Automotive")){
//           return new ResponseEntity<>("No loan match", HttpStatus.FORBIDDEN);
//      }

        //if(loanRespository)

        if (loanApplicationDTO.getAmount() == 0 || Objects.equals(loanApplicationDTO.getNumberAccount(), "") || loanApplicationDTO.getPayments() == 0){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(loan == null){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("Loan exceeded max amount", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.getPayments()) ){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        double loanInterest = (loanApplicationDTO.getAmount() * 0.20) + loanApplicationDTO.getAmount();
        double loanPayments = Math.floor(loanInterest / loanApplicationDTO.getPayments());

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments(), client, loan);
        Transaction transactionLoanCredit = new Transaction(TransactionType.CREDIT, "Loan Approved: " + loan.getName(), LocalDateTime.now(), loanApplicationDTO.getAmount(), account);

        double balance = account.getBalance() + loanApplicationDTO.getAmount();
        account.setBalance(balance);

        //Transaction transactionLoanDebit = new Transaction(TransactionType.DEBIT, loan.getName(), LocalDateTime.now(), loanApplicationDTO.getAmount(), account);

        transactionRepository.save(transactionLoanCredit);
        clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
