package com.example.pokercalculator;
/* -----------------------------------------------------
   New implementation of a Card:

     cardValue =  10*Rank + Suit
   ----------------------------------------------------- */

public class Card {
    public static final int SPADE = 4;
    public static final int HEART = 3;
    public static final int CLUB = 2;
    public static final int DIAMOND = 1;

    private static final String[] Suit = {"*", "d", "c", "h", "s"};
    private static final String[] Rank = {"*", "A", "2", "3", "4",
            "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

    private int cardValue;

    public Card(int suit, int rank) {
        if (rank == 1)
            cardValue = 14 * 10 + suit;      // Give Ace the rank 14
        else
            cardValue = rank * 10 + suit;
    }

    public int suit() {
        return (cardValue % 10);
    }

    public String suitStr() {
        return (Suit[cardValue % 10]);
    }

    public int rank() {
        return (cardValue / 10);
    }

    public String rankStr() {
        return (Rank[cardValue / 10]);
    }

    public String toString() {
        return (Rank[cardValue / 10] + Suit[cardValue % 10]);
    }
}