package com.logicea.cardify.repository;

import com.logicea.cardify.models.Card;
import com.logicea.cardify.models.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface CardRepository extends JpaRepository<Card,Long> {

    List<Card> findAllByUserUsername(String username);

    List<Card> findAllByUserUsernameAndNameContainingAndColorContainingAndStatus(String username, String name, String color, EStatus status);

    List<Card> findAllByUserUsernameAndCreationDateAfter(String username, Date date);
}
