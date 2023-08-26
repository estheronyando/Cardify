package com.logicea.cardify.controllers;

import com.logicea.cardify.modelpojos.CardPojo;
import com.logicea.cardify.models.Card;
import com.logicea.cardify.services.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cards")
public class CardController {
  private final CardService cardService;

  @Autowired
  public CardController(CardService cardService) {
    this.cardService = cardService;
  }

  @PostMapping("/add")
  public ResponseEntity<Card> addCard(@Valid @RequestBody CardPojo cardPojo , @RequestParam("username") String username) { // Use @PathVariable if username is part of the URL
    Card createdCard = cardService.createCard(cardPojo,username);
    return ResponseEntity.ok(createdCard);
  }
}
