package com.logicea.cardify.repository;

import com.logicea.cardify.models.Card;
import com.logicea.cardify.models.EStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Repository
public interface CardRepository extends JpaRepository<Card,Long> {

    List<Card> findAllByUserUsername(String username);

    List<Card> findAllByUserUsernameAndNameContainingAndColorContainingAndStatus(String username, String name, String color, EStatus status);

    List<Card> findAllByUserUsernameAndCreationDateAfter(String username, Date date);

    @Query(value= "SELECT * FROM cards where name = ?1",nativeQuery = true)
    List<Card> findAllCardsByname(String name);
    @Query(value= "SELECT c FROM Card c WHERE c.name = :cardName AND c.user.id = :userId")
    List<Card> findAllCardsBynameforuser(@Param("cardName") String cardName, @Param("userId") Long userId);

    @Query("SELECT c FROM Card c WHERE " +
            "(:name IS NULL OR c.name LIKE %:name%) " +
            "AND (:color IS NULL OR c.color = :color) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:creationDate IS NULL OR c.creationDate = :creationDate)")
    List<Card> searchCards(
            @Param("name") String name,
            @Param("color") String color,
            @Param("status") EStatus status,
            @Param("creationDate") Date creationDate,
            Pageable pageable);
    @Query("SELECT c FROM Card c WHERE " +
            "(:name IS NULL OR c.name LIKE %:name%) " +
            "AND (:color IS NULL OR c.color = :color) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:creationDate IS NULL OR c.creationDate = :creationDate)"+
            "AND (c.user.id = :userId)"
    )
    List<Card> searchCardsWithAccessCheck(
            @Param("name") String name,
            @Param("color") String color,
            @Param("status") EStatus status,
            @Param("creationDate") Date creationDate,
            @Param("userId") Long userId,
            Pageable pageable);

    @Query("SELECT c FROM Card c WHERE c.name = :cardName AND c.user.id = :userId")
    Card findCardByNameWithAccessCheck(@Param("cardName") String cardName, @Param("userId") Long userId);

    @Query("SELECT c FROM Card c WHERE c.name = :cardName")
    Card findCardByName(@Param("cardName") String cardName);

    @Transactional
    @Modifying
    @Query("DELETE FROM Card c WHERE c.name = :cardName AND c.user.id = :userId")
    void deleteCardByName(@Param("cardName") String cardName, @Param("userId") Long userId);

}
