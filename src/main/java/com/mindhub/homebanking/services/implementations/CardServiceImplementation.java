package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class CardServiceImplementation implements CardServices {

    @Autowired
    CardRepository cardRepository;

    @Override
    public List<CardDTO> getCards() {
        return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());
    }

    @Override
    public CardDTO getCardDTOById(long id) {
        CardDTO cardDTO = new CardDTO(cardRepository.findById(id).orElse(null));
        return cardDTO;
    }

    @Override
    public Card getCardById(long id) {
       // Card card = new Card(cardRepository.findById(id).orElse(null));
        //return card;
        return  cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card findCardByNumber() {
        return null;
    }

    @Override
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

}
