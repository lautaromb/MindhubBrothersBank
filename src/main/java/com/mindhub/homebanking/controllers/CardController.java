package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RequestMapping("/api")
@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;


   @RequestMapping("clients/current/cards")
    List<CardDTO> getCards(){return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());}

    @RequestMapping("cards/{id}")
    public CardDTO getClient(@PathVariable Long id){
        CardDTO cardDTO = new CardDTO(cardRepository.findById(id).orElse(null));
        return cardDTO;
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(Authentication authentication,
    @RequestParam CardType cardType, @RequestParam CardColor cardColor ){
        Client client = clientRepository.findByEmail(authentication.getName());
        List<Card> cardList  = client.getCards().stream().filter(card -> { return card.getTypeCard() == cardType; }).collect(toList());
        List<Card> cardListGood  = cardList.stream().filter(card -> { return card.isGood() == true; }).collect(toList());

        if (cardListGood.size() >= 3) {
          return new ResponseEntity<>("Can't activate more cards", HttpStatus.FORBIDDEN);
       }

        String cardNumber = CardUtils.getCardNumber();
        int cardCVV = CardUtils.getCVV();


        Card card = new Card(client.getFirstName() + " "+ client.getLastName(), cardType, cardColor, cardNumber, cardCVV, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client);
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("clients/current/cards")
    public List<CardDTO> getAll(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    //@DeleteMapping("clients/current/cards/{id}")
    //public ResponseEntity<Object> deleteCard(@PathVariable Long id){
        //Card card = cardRepository.findById(id).orElse(null);
        // cardRepository.delete(card);
        // cardRepository.deleteById(id);  (this is the good one)
  //      return new ResponseEntity<>("Deleted", HttpStatus.CREATED);
//    }


//    @RequestMapping(path = "/clients/current/cards/delete/{id}", method = RequestMethod.PATCH)
//    public ResponseEntity<Object> deleteCard(@PathVariable Long id, boolean state ){
//        Card card = cardRepository.findById(id).orElse(null);
//        card.setGood(state);
//        return new ResponseEntity<>("Set Good: false", HttpStatus.CREATED);
//    }

    @PatchMapping("/clients/current/cards/delete/{id}")
    public ResponseEntity<Object> smartDelete(@PathVariable Long id){
       Card card = cardRepository.findById(id).orElse(null);
       card.setGood(false);
       cardRepository.save(card);
       return new ResponseEntity<>("Set Good: false", HttpStatus.CREATED);
    }

}
