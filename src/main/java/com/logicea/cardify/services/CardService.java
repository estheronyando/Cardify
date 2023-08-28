package com.logicea.cardify.services;

import com.logicea.cardify.modelapis.EntryResponse;
import com.logicea.cardify.modelapis.ModelApiResponse;
import com.logicea.cardify.models.*;
import com.logicea.cardify.utils.GlobalVariables;
import com.logicea.cardify.utils.Helper;
import com.logicea.cardify.modelpojos.CardPojo;
import com.logicea.cardify.repository.CardRepository;
import com.logicea.cardify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
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
    public User getCurrentLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();
        User user = userRepository.findUserByUsername(loggedInUsername);
        return user;
    }



    public Card createCard(CardPojo cardPojo, String user_name) {
        User loggedInUser=getCurrentLoggedInUser();
        String username=loggedInUser.getUsername();
        boolean isColorFormatValid;
        User user = userRepository.findUserByUsername(username);
        isColorFormatValid=Helper.isColorFormatValid(cardPojo.getColor());
        if (user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_ADMIN"))) {
            if(user_name != null && !user_name.isEmpty() ){
                user=userRepository.findUserByUsername(user_name);
                if(user==null){
                    return null;
                }
            }
        }
        if (!isColorFormatValid) {
            throw new IllegalArgumentException("Invalid color format");
        }

        Card card = new Card();
        card.setName(cardPojo.getName());
        card.setDescription(cardPojo.getDescription());
        card.setColor(cardPojo.getColor());
        card.setStatus(EStatus.ToDo.toString());
        card.setUser(user);
        card.setCreationDate(Helper.getRequestedDateTime());

        return cardRepository.save(card);
    }

    public List<Card> getCardByName(String cardName){
        User user=getCurrentLoggedInUser();
        List<Card> rcaData = cardRepository.findCardByname(cardName);
        return rcaData;
    }

    public List<Card> searchCards(String name, String color, EStatus status, Date creationDate,int page, int size,String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        User user = getCurrentLoggedInUser();
        if (user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_ADMIN"))){
            return cardRepository.searchCards(name, color, status, creationDate,pageable);
        }
        return cardRepository.searchCardsWithAccessCheck(name, color, status, creationDate,user.getId(),pageable);
    }

    public Card getCardByNameWithAccessCheck(String cardName, String user_name) {
        User user;
        user = getCurrentLoggedInUser();
        if (user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_ADMIN"))){
            if(user_name != null && !user_name.isEmpty()){
                user=userRepository.findUserByUsername(user_name);
                if(user==null){
                    return null;
                }
            }
            return cardRepository.findCardByNameWithAccessCheck(cardName,user.getId());
        }

        return cardRepository.findCardByNameWithAccessCheck(cardName, user.getId());
    }

    public ResponseEntity<ModelApiResponse> deleteCard(String requestRefId,String user_name,String cardName){

        try {
            User loggedInuser=getCurrentLoggedInUser();
            String username=loggedInuser.getUsername();
            User user=userRepository.findUserByUsername(username);
            if(user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_ADMIN"))){
                if(user_name!=null && !user_name.isEmpty()){
                    user=userRepository.findUserByUsername(user_name);
                    if (user==null){
                        return new ResponseEntity<>(EntryResponse
                                .responseFormatter(GlobalVariables.RESPONSE_CODE_404, requestRefId, "Username not found",
                                        "Username not found. Please submit a valid username", ""), HttpStatus.OK);
                    }
                }
            }

            Card cardToDelete=cardRepository.findCardByNameWithAccessCheck(cardName, user.getId());
            if (cardToDelete != null) {
                cardRepository.deleteCardByName(cardName,user.getId());
                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_200, requestRefId, "Card deleted successfully",
                                "Card deleted successfully", cardToDelete), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_404, requestRefId, "Card not found",
                                "Card Details not found", ""), HttpStatus.NOT_FOUND);

            }
        } catch (Exception ex) {
            return new ResponseEntity<>(EntryResponse
                    .responseFormatter(GlobalVariables.RESPONSE_CODE_500, requestRefId, "Card deletion failed",
                            "Card deletion failed", ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    public ResponseEntity<ModelApiResponse> updateCardData(String requestRefId,String username, String name,String description, String color, String status) {
        try {
            User user=getCurrentLoggedInUser();
            if(user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_ADMIN"))){
                if(username != null && !username.isEmpty()){
                    user=userRepository.findUserByUsername(username);
                }
            }
            Card updateCard_Data = cardRepository.findCardByNameWithAccessCheck(name,user.getId());
            if (updateCard_Data != null) {
                Card updateCard = cardRepository.findCardByNameWithAccessCheck(name,user.getId());
                updateCard.setName(name);
                updateCard.setDescription(description);
                updateCard.setColor(color);
                updateCard.setStatus(status);
                try {
                    cardRepository.save(updateCard);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_200, requestRefId, "Card data updated successfully",
                                "Card data updated successfully", updateCard), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(EntryResponse
                        .responseFormatter(GlobalVariables.RESPONSE_CODE_404, requestRefId, "Card not found",
                                "Card Data Details not found", ""), HttpStatus.NOT_FOUND);

            }
        } catch (Exception ex) {
            return new ResponseEntity<>(EntryResponse
                    .responseFormatter(GlobalVariables.RESPONSE_CODE_500, requestRefId, "Card data update failed",
                            "Card data update failed", ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
