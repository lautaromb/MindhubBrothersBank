package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;

import java.util.List;

public interface CardServices {
    public List<CardDTO> getCards();
    public CardDTO getCardDTOById(long id);
    public Card getCardById(long id);
    public Card findCardByNumber();
    public Card saveCard( Card card);
}
