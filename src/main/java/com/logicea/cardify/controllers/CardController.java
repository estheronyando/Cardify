package com.logicea.cardify.controllers;

import com.logicea.cardify.modelapis.EntryResponse;
import com.logicea.cardify.modelapis.ModelApiResponse;
import com.logicea.cardify.modelpojos.CardPojo;
import com.logicea.cardify.modelpojos.UpdateCardPojo;
import com.logicea.cardify.models.Card;
import com.logicea.cardify.models.EStatus;
import com.logicea.cardify.services.CardService;
import com.logicea.cardify.utils.Validations;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponses;
//import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import  com.logicea.cardify.utils.GlobalVariables;


import java.awt.print.Book;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cards")
@Api(value = "Card Data Microservice", produces = MediaType.APPLICATION_JSON_VALUE)
public class CardController {
  private final CardService cardService;
  private final Validations validations;

  @Autowired
  public CardController(CardService cardService, Validations validations) {
    this.cardService = cardService;
    this.validations = validations;
  }

  @PostMapping("/add")
  @Operation(summary = "Add Card Data to Database")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "400", description = "Bad request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "401", description = "User unauthorized to access the resource", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "404", description = "Requested resource not found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "409", description = "Resource conflict", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "417", description = "Expectation failed", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) })
  })
  public ResponseEntity<ModelApiResponse> addCard(@RequestHeader HttpHeaders httpHeaders, @Valid @RequestBody CardPojo cardPojo, @Parameter(description = "Optional parameter for admins to use to modify other peoples cards")@RequestParam(required = false) String username) { // Use @PathVariable if username is part of the URL
    //Check if body is valid
    String transactionRequestRefId;
    transactionRequestRefId = UUID.randomUUID().toString();

    ResponseEntity<ModelApiResponse> validateRequestPayload = validations.validateRequestBody(httpHeaders, transactionRequestRefId,GlobalVariables.INSERT_CARD,cardPojo.getName(),cardPojo.getDescription(),cardPojo.getColor(),cardPojo.getUser());
    if(validateRequestPayload != null){
      return validateRequestPayload;
    }

    try{
      Card findCardWithName = cardService.getCardByNameWithAccessCheck(cardPojo.getName(),username);
      System.out.println(findCardWithName);
      if(findCardWithName==null){
        Card createdCard = cardService.createCard(cardPojo, username);
        return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_200, transactionRequestRefId,
                "Card created successfully", "Card Data inserted successfully",""),
                HttpStatus.OK);

      }else{
        return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_409, transactionRequestRefId,
                "Card Data with specified name already exists", "Card Data with specified name already exists",""),
                HttpStatus.CONFLICT);
      }
    }catch(Exception ex){
      System.out.println(ex.toString());
      return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_500, transactionRequestRefId,
              "Failed to insert Card Data", "Failed to insert Card data", ""),
              HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @GetMapping("/getCardByName")
  @Operation(summary="Retrieve card with specified Name as parameter")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Current Card with specified Name")})
  public ResponseEntity<ModelApiResponse> findCardByName(@RequestHeader HttpHeaders httpHeaders,@Parameter(description = "Card Name to be searched")@RequestParam("name") String cardName,@Parameter(description = "Optional parameter for admins to modify other peoples cards")@RequestParam(required = false) String username) { // Use @PathVariable if username is part of the URL
    //Check if body is valid
    String transactionRequestRefId;
    transactionRequestRefId = UUID.randomUUID().toString();

    try{
      List<Card> findCardsWithName = cardService.getCardByName(cardName);
      Long noOfCardsByName = findCardsWithName.stream().count();
      
      if(!findCardsWithName.isEmpty()){
        System.out.println(findCardsWithName.toString());
        return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_200, transactionRequestRefId,
                "Cards by name Found", String.valueOf(noOfCardsByName),findCardsWithName),
                HttpStatus.OK);

      }else{
        return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_409, transactionRequestRefId,
                "Card Data not found", "Card Data with specified name not found",""),
                HttpStatus.CONFLICT);
      }
    }catch(Exception ex){
      return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_500, transactionRequestRefId,
              "Failed to find card Data", "Failed to find Card data", ""),
              HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @GetMapping("/search")
  @Operation(summary="Retrieve cards with filters")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Retrieved cards")})
  public ResponseEntity<ModelApiResponse> searchCards(
          @Parameter(description = "Optional parameter to search cards by name ")@RequestParam(required = false) String name,
          @Parameter(description = "Optional parameter to sort cards by color")@RequestParam(required = false) String color,
          @Parameter(description = "Optional parameter to sort cards by status")@RequestParam(required = false) EStatus status,
          @Parameter(description = "Optional parameter to search cards by creationDate")@RequestParam(required = false) Date creationDate,
          @Parameter(description = "Optional parameter to return paginated results")@RequestParam(defaultValue = "0") int page,
          @Parameter(description = "Optional parameter to limit the number of results")@RequestParam(defaultValue = "10") int size,
          @Parameter(description = "Optional parameter to sort cards by name or creationDate")@RequestParam(required = false) String sortBy) {
    String transactionRequestRefId;
    transactionRequestRefId = UUID.randomUUID().toString();
    try{
      List<Card> filteredCards = cardService.searchCards(name, color, status, creationDate,page,size,sortBy);
      Long noOfFilteredCardsByName = filteredCards.stream().count();
      if(!filteredCards.isEmpty()){
        return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_200, transactionRequestRefId,
                "Cards retrieved successfully.", String.valueOf(noOfFilteredCardsByName),filteredCards),
                HttpStatus.OK);

      }else{
        return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_409, transactionRequestRefId,
                "No Card Data not found", "No Card Data not found",""),
                HttpStatus.CONFLICT);
      }
    }catch(Exception ex){
      return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_500, transactionRequestRefId,
              "An error occurred while retrieving cards.", "An error occurred while retrieving cards.", ""),
              HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }
  @GetMapping("/{cardName}")
  @Operation(summary="Retrieve one card with specified name")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Retrieved card")})
  public ResponseEntity<ModelApiResponse> getCardByName(@Parameter(description = "Card Name to be retrieved")@PathVariable String cardName, @Parameter(description = "Optional parameter for admins to modify other peoples cards")@RequestParam(required = false) String username) {
    String transactionRequestRefId;
    transactionRequestRefId = UUID.randomUUID().toString();
    try{
      Card retrievedCard = cardService.getCardByNameWithAccessCheck(cardName,username);
      if((retrievedCard!=null)){
        return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_200, transactionRequestRefId,
                "Card retrieved successfully.", "Card retrieved successfully.",retrievedCard),
                HttpStatus.OK);

      }else{
        return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_409, transactionRequestRefId,
                "No Card Data not found", "No Card Data not found",""),
                HttpStatus.CONFLICT);
      }
    }catch(Exception ex){
      return new ResponseEntity<>(EntryResponse.responseFormatter(GlobalVariables.RESPONSE_CODE_500, transactionRequestRefId,
              "An error occurred while retrieving card info.", "An error occurred while retrieving card info.", ""),
              HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @PutMapping("/updateCard")
  @Operation(summary = "Update Card data")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "400", description = "Bad request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "401", description = "User unauthorized to access the resource", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "404", description = "Requested resource not found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "409", description = "Resource conflict", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "417", description = "Expectation failed", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) })
  })
  public ResponseEntity<ModelApiResponse> updateCardByName(@RequestHeader HttpHeaders httpHeaders, @Valid @RequestBody UpdateCardPojo updateCardPojo,@Parameter(description = "Optional parameter for admins to modify other peoples cards")@RequestParam(required = false) String username){
    String transactionRequestRefId;
    transactionRequestRefId = UUID.randomUUID().toString();

    ResponseEntity<ModelApiResponse> validateRequestPayload = validations.validateUpdateRequestBody(httpHeaders, transactionRequestRefId,GlobalVariables.INSERT_CARD,updateCardPojo.getName(),updateCardPojo.getDescription(),updateCardPojo.getColor(),updateCardPojo.getUser(), updateCardPojo.getStatus());
    if(validateRequestPayload != null){
      return validateRequestPayload;
    }

   return cardService.updateCardData(transactionRequestRefId,username,updateCardPojo.getName(),updateCardPojo.getDescription(),updateCardPojo.getColor(),updateCardPojo.getStatus());
}

  @DeleteMapping("/deleteCard")
  @Operation(summary = "Delete Card data")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "400", description = "Bad request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "401", description = "User unauthorized to access the resource", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "404", description = "Requested resource not found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "409", description = "Resource conflict", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "417", description = "Expectation failed", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) }),
          @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ModelApiResponse.class)) })
  })
  public ResponseEntity<ModelApiResponse> deleteCardByName(@RequestHeader HttpHeaders httpHeaders, @Parameter(description = "Card Name to be deleted")@RequestParam("name") String name, @Parameter(description = "Optional parameter for admins to modify other peoples cards")@RequestParam(required = false) String username){

    String transactionRequestRefId;
    transactionRequestRefId = UUID.randomUUID().toString();

    return cardService.deleteCard(transactionRequestRefId,username,name);
  }


}
