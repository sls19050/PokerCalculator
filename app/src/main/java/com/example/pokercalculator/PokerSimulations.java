package com.example.pokercalculator;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PokerSimulations {

    private int[][] allCardPairs;
    private Deck myDeck;
    private Card[] communityCards = new Card[5];

    private int numPlayer;
    private ArrayList<Card[]> playerCards = new ArrayList<>();

    public PokerSimulations(int numPlayer, int[][] allCardPairs) {
        this.allCardPairs = allCardPairs;
        this.numPlayer = numPlayer;

        myDeck = new Deck(allCardPairs);

    }

    public double[] getWinProbability() {
        // for loop for 1000 simulations
        int totalSimNum = 3;
        int numSamples = 500;

        //loop 3 sims

        double[] winRates = new double[totalSimNum];
        double myAveWinRate = 0;
        double stdev = 0;

        for (int i = 0; i < totalSimNum; i++) {

            int countMyWin = 0;
            double myWinRate = 0;

            // for the number of samples per sim
            for (int j = 0; j < numSamples; j++) {
                //including cards other players
                int[][] currentCardsDealt = distributeCards();
                //Log.d("simDeal", MainActivity.printTwoDArray(currentCardsDealt));
                Integer[] playerScores = new Integer[numPlayer];

                Card[] tempCards = new Card[7];

                // store 5 community cards first
                for (int m = 0; m < 5; m++)
                    tempCards[m] = new Card(currentCardsDealt[m][0], currentCardsDealt[m][1]);

                for (int k = 0; k < numPlayer; k++) {
                    // add two player cards
                    for (int n = 0; n < 2; n++) {
                        int index = 5 + 2 * k + n;
                        tempCards[n+5] = new Card(currentCardsDealt[index][0], currentCardsDealt[index][1]);
                        //Log.d("tempCardPairs", Arrays.toString(tempCardPairs[n]));
                    }

                    //tempCards = constructCards(tempCardPairs);

                    playerScores[k] = getMaxScore(tempCards, tempCards.length, 5);
                    //Log.d("playerScores", Arrays.toString(playerScores));
                }
                ArrayList<Integer> tempList = new ArrayList<>(Arrays.asList(playerScores));

                if (getMaxIndexFromIntArray(tempList) == 0)
                    countMyWin++;
            }
            winRates[i] = (double) countMyWin / numSamples;
        }

        // random draw from Deck for all players and community cards
        //check if I win the game
        // record win/loss
        // return averaged win/lose and stdev
        double[] result = new double[2];
        result[0] = getAverage(winRates);
        result[1] = getStdev(winRates);
        return result;
    }

    public Card[] constructCards(int[][] sevenCardPairs) {
        Card[] resultCards = new Card[7];

        for (int i = 0; i < 7; i++)
            resultCards[i] = new Card(sevenCardPairs[i][0], sevenCardPairs[i][1]);

        return resultCards;
    }

    public int[][] distributeCards() {

        //reset current card index and shuffle deck
        myDeck.randomize();

        int[][] result = new int[5 + 2 * numPlayer][2];

        // check Community Cards and My Cards if they are selected
        for (int i = 0; i < 7; i++) {
            int suit = allCardPairs[i][0];
            if (suit == 0) {
                result[i] = myDeck.getCardPair();
            } else {
                result[i] = allCardPairs[i];
            }
        }

        // distribute cards to other players
        for (int i = 7; i < result.length; i++) {
            result[i] = myDeck.getCardPair();
        }

        return result;
    }

    /*  arr[]       ---> Input Array
        data[]      ---> Temporary array to store current combination
        start & end ---> Staring and Ending indexes in arr[]
        index       ---> Current index in data[]
        r           ---> Size of a combination to be printed */
    public static void combinationUtil(Card[] arr, Card[] data, int start,
                                       int end, int index, int r, ArrayList<Integer> scores) {
        // Current combination is ready to be printed, print it
        if (index == r) {
            Log.d("data", ""+Arrays.toString(data));
            scores.add(PokerRules.valueHand(data));
            Log.d("scores_add", ""+PokerRules.valueHand(data));
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data[index] = arr[i];
            combinationUtil(arr, data, i + 1, end, index + 1, r, scores);
        }
    }

    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    public static int getMaxScore(Card[] arr, int n, int r) {
        //Log.d("arr", ""+arr.length);
        // A temporary array to store all combination one by one
        Card[] data = new Card[r];
        ArrayList<Integer> scores = new ArrayList<>();

        //Log.d("arr", ""+Arrays.toString(arr));
        // Print all combination using temprary array 'data[]'
        combinationUtil(arr, data, 0, n - 1, 0, r, scores);

        return getMaxFromIntArray(scores);
    }

    public static int getMaxFromIntArray(ArrayList<Integer> arr) {
        int index = 0;
        int max = arr.get(index);
        for (int i = 0; i < arr.size(); i++) {
            if (max < arr.get(i)) {
                index = i;
                max = arr.get(i);
                Log.d("max", ""+max);
            }
        }
        return max;
    }

    public static int getMaxIndexFromIntArray(ArrayList<Integer> arr) {
        int index = 0;
        int max = arr.get(index);
        for (int i = 0; i < arr.size(); i++) {
            if (max < arr.get(i)) {
                index = i;
                max = arr.get(i);
                Log.d("max", ""+max);
            }
        }
        return index;
    }

    public double getAverage(double[] arr) {
        double sum = 0;
        for (double a : arr)
            sum = sum + a;
        return sum / arr.length;
    }

    public double getStdev(double[] arr) {
        double sum = 0;
        double ave = getAverage(arr);
        for (double a : arr)
            sum = Math.pow(a - ave, 2) + sum;
        return Math.pow(sum / arr.length, 0.5);
    }

}


