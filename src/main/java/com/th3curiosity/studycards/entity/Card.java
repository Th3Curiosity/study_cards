package com.th3curiosity.studycards.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    private Deck deck;
    private String front;
    private String back;


    public Card() {
    }

    public Card(Deck deck, String front, String back) {
        this.deck = deck;
        this.front = front;
        this.back = back;
    }


    public Long getId() {
        return id;
    }

    public Deck getDeck() {
        return deck;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public void setBack(String back) {
        this.back = back;
    }

}
