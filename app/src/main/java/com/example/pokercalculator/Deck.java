package com.example.pokercalculator;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Deck {

    static int currentDeckIndex = 0;
    static ArrayList<int[]> deckCardsPairs;

    public Deck(int[][] allCardPairs) {
        initializeDeckPairs(allCardPairs);
    }

    public static void initializeDeckPairs(int[][] allCardPairs) {
        // use an int[] size 2 to indicate a card
        Set<int[]> mySelectedCards = new HashSet<>();
        for (int i = 0; i < allCardPairs.length; i++) {
            int suit = allCardPairs[i][0];
            int rank = allCardPairs[i][1];

            if (suit != 0) {
                int[] temp = new int[]{suit, rank};
                mySelectedCards.add(temp);
            } else {
                continue;
            }
        }

        deckCardsPairs = filterDeck(mySelectedCards);

        randomize();
    }

    public static ArrayList<int[]> filterDeck(Set<int[]> mySelectedCards) {
        ArrayList<int[]> result = new ArrayList<>();

        // add cards to deck by suit first
        for (int i = 1; i <= 4; i++) {
            // add cards to deck by rank
            for (int j = 1; j <= 13; j++) {
                int[] temp = new int[]{i, j};

                if (mySelectedCards.contains(temp)) {
                    break;
                }
                result.add(temp);
            }
        }
        return result;
    }

    public static void randomize(){
        currentDeckIndex = 0;
        Collections.shuffle(deckCardsPairs);
    }

    public static int[] getCardPair(){

        return deckCardsPairs.get(currentDeckIndex++);
    }
}
