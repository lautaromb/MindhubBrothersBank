package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardServices;
import com.mindhub.homebanking.services.ClientServices;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RequestMapping("/api")
@RestController
public class CardController {

    @Autowired
    private ClientServices clientServices;
    @Autowired
    private CardServices cardServices;


   @RequestMapping("clients/current/cards")
    List<CardDTO> getCards(){return cardServices.getCards();}

    @RequestMapping("cards/{id}")
    public CardDTO getClientById(@PathVariable Long id){
//        CardDTO cardDTO = new CardDTO(cardRepository.findById(id).orElse(null));
//        return cardDTO;
        return cardServices.getCardDTOById(id);
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(Authentication authentication,
    @RequestParam CardType cardType, @RequestParam CardColor cardColor ){
        Client client = clientServices.findClientByEmail(authentication.getName());
        List<Card> cardList  = client.getCards().stream().filter(card -> { return card.getTypeCard() == cardType; }).collect(toList());
        List<Card> cardListGood  = cardList.stream().filter(card -> { return card.isGood() == true; }).collect(toList());

        if (cardListGood.size() >= 3) {
          return new ResponseEntity<>("Can't activate more cards", HttpStatus.FORBIDDEN);
       }

        String cardNumber = CardUtils.getCardNumber();
        int cardCVV = CardUtils.getCVV();


        Card card = new Card(client.getFirstName() + " "+ client.getLastName(), cardType, cardColor, cardNumber, cardCVV, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client);
        //cardRepository.save(card);
        cardServices.saveCard(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("clients/current/cards")
    public List<CardDTO> getAll(Authentication authentication) {
//        Client client = clientRepository.findByEmail(authentication.getName());
        Client client = clientServices.findClientByEmail(authentication.getName());
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
//       Card card = cardRepository.findById(id).orElse(null);
        Card card = cardServices.getCardById(id

        );
       card.setGood(false);
       cardServices.saveCard(card);
       return new ResponseEntity<>("Set Good: false", HttpStatus.CREATED);
    }

}
