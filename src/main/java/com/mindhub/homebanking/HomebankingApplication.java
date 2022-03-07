package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository){
	return (args) ->{



		//Payments to select
		var payments12_60  = List.of(12,24,36,48,60);
		var payments6_24  = List.of(6,12,24);
		var payments6_36  = List.of(6,12,24,36);

		//save costumers

		Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba1234"));
		Client client2 = new Client("Day", "Perez", "day@mindhub.com", passwordEncoder.encode("day1234"));
		Client client3 = new Client("Lautaro", "Mereles Britos", "lautaro@gmail.com", passwordEncoder.encode("Lautaro1234"));
		Client admin = new Client("admin", "", "admin@admin.com",passwordEncoder.encode("admin1234"));

		Account account1 = new Account("VIN001", LocalDateTime.now(),5000, client1, AccountType.CURRENT);
		Account account2 = new Account( "VIN002", LocalDateTime.now().plusDays(1),7500, client1, AccountType.SAVING);
		Account account3 = new Account( LocalDateTime.now().plusDays(1),800, client2, AccountType.CURRENT);
		Account account4 = new Account( LocalDateTime.now().plusDays(10),350, client3, AccountType.SAVING);

		Transaction transaction1 = new Transaction(TransactionType.DEBIT, "Fast Food Inc.", LocalDateTime.now(), 200.0, account1);
		Transaction transaction2 = new Transaction(TransactionType.CREDIT, "GameStore Refund", LocalDateTime.now(), 65.0, account2);
		Transaction transaction3 = new Transaction(TransactionType.CREDIT, "You sold 'Air Jordan 2'", LocalDateTime.now(), 550.99, account2);
		Transaction transaction4 = new Transaction(TransactionType.DEBIT, "Gas Station", LocalDateTime.now(), -5.30, account2);
		Transaction transaction5 = new Transaction(TransactionType.DEBIT, "Rent a Flat", LocalDateTime.now(), 850.0, account3);
		Transaction transaction6 = new Transaction(TransactionType.DEBIT, "Gas Station", LocalDateTime.now(), 5.30, account4);
		Transaction transaction7 = new Transaction(TransactionType.DEBIT, "Rent a Flat", LocalDateTime.now(), 850.0, account3);

		Loan loan1 = new Loan("Mortgage", 500000,  payments12_60, .10);
		Loan loan2 = new Loan("Personal", 100000,  payments6_24, .15);
		Loan loan3 = new Loan("Automotive", 300000,  payments6_36, .20);

		ClientLoan clientLoan1 = new ClientLoan(400000, payments12_60.get(4), client1, loan1 );
		ClientLoan clientLoan2 = new ClientLoan(50000, payments6_24.get(1), client1, loan2 );
		ClientLoan clientLoan3 = new ClientLoan(100000, payments6_24.get(2), client2, loan2 );
		ClientLoan clientLoan4 = new ClientLoan(200000, payments6_36.get(3), client2, loan3 );

		LocalDateTime today = LocalDateTime.now();
		LocalDateTime plus5years = today.plusYears(5);

		Card card1 = new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.DEBIT, CardColor.GOLD,"5858-1082-6128-1991",123, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1);
		Card card2 = new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.CREDIT, CardColor.TITANIUM, "4857-1000-6828-1921",103, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1);
		Card card3 = new Card(client2.getFirstName() + " " + client2.getLastName(), CardType.CREDIT, CardColor.SILVER,"3856-1082-6117-1921",134,  LocalDateTime.now(), LocalDateTime.now().plusYears(5), client2);
		Card card4 = new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.CREDIT, CardColor.GOLD,"2845-1082-6166-8921",196,  LocalDateTime.now(), LocalDateTime.now().plusYears(7), client1);
		Card card5 = new Card(client3.getFirstName() + " " + client3.getLastName(), CardType.CREDIT, CardColor.TITANIUM,"1825-1088-6728-1921",943, LocalDateTime.now(), LocalDateTime.now().plusYears(1), client3);
		Card card6 = new Card(client2.getFirstName() + " " + client2.getLastName(), CardType.CREDIT, CardColor.SILVER,"6853-1182-6120-1921",189,  LocalDateTime.now(), LocalDateTime.now().plusYears(3), client2);


		clientRepository.save(client1);
		clientRepository.save(client2);
		clientRepository.save(client3);
		clientRepository.save(admin);

		accountRepository.save(account1);
		accountRepository.save(account2);
		accountRepository.save(account3);
		accountRepository.save(account4);

		transactionRepository.save(transaction1);
		transactionRepository.save(transaction2);
		transactionRepository.save(transaction3);
		transactionRepository.save(transaction4);
		transactionRepository.save(transaction5);
		transactionRepository.save(transaction6);
		transactionRepository.save(transaction7);

		loanRepository.save(loan1);
		loanRepository.save(loan2);
		loanRepository.save(loan3);

		clientLoanRepository.save(clientLoan1);
		clientLoanRepository.save(clientLoan2);
		clientLoanRepository.save(clientLoan3);
		clientLoanRepository.save(clientLoan4);

		cardRepository.save(card1);
		cardRepository.save(card2);
		cardRepository.save(card3);
		cardRepository.save(card4);
		cardRepository.save(card5);
		cardRepository.save(card6);


		//Ver despues
		String cardThruDate = card1.getThruDate().toString().substring(5, 7) + '/' + card1.getThruDate().toString().substring(0,4);
		System.out.println("card " + cardThruDate);
	};
	}

}
