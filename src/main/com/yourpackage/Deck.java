package com.yourpackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<com.yourpackage.Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
        String[] suits = {"CLUBS", "DIAMONDS", "HEARTS", "SPADES"};
        String[] ranks = {"TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING", "ACE"};
        int power = 2;
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new com.yourpackage.Card(suit, rank, power));
                power++;
            }
            power = 2;
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public com.yourpackage.Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    public List<com.yourpackage.Card> drawCards(int count) {
        List<com.yourpackage.Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            com.yourpackage.Card card = drawCard();
            if (card != null) {
                drawnCards.add(card);
            } else {
                break;
            }
        }
        return drawnCards;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
