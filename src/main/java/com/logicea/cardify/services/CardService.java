package com.logicea.cardify.services;

import com.logicea.cardify.utils.Helper;
import com.logicea.cardify.modelpojos.CardPojo;
import com.logicea.cardify.models.Card;
import com.logicea.cardify.models.EStatus;
import com.logicea.cardify.models.User;
import com.logicea.cardify.repository.CardRepository;
import com.logicea.cardify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;


    @Autowired
    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository=userRepository;

    }

    public Card createCard(CardPojo cardPojo, String username) {
        boolean isColorFormatValid;
        User user = userRepository.findUserByUsername(username);
        isColorFormatValid=Helper.isColorFormatValid(cardPojo.getColor());



        if (!isColorFormatValid) {
            // Handle color validation error here
            throw new IllegalArgumentException("Invalid color format");
        }

        Card card = new Card();
        card.setName(cardPojo.getName());
        card.setDescription(cardPojo.getDescription());
        card.setColor(cardPojo.getColor());
        card.setStatus(EStatus.ToDo);
        card.setUser(user);
        card.setCreationDate(Helper.getRequestedDateTime());

        return cardRepository.save(card);
    }


}
