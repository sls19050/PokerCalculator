package com.example.pokercalculator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.util.HashMap;

public class PokerCards {

    public static int getDrawableCard(Context myContext, String suit, String rank) {

        String resName;
        if (suit.equals("unknown") || rank.equals("unknown")) {
            resName = "back";
        } else if (rank.equals("J")) {
            resName = suit.substring(0, 1) + "_11";
        } else if (rank.equals("Q")) {
            resName = suit.substring(0, 1) + "_12";
        } else if (rank.equals("K")) {
            resName = suit.substring(0, 1) + "_13";
        } else{
            resName = suit.substring(0, 1) + "_" + rank;
        }

        Toast.makeText(myContext, resName, Toast.LENGTH_SHORT).show();

        int id = myContext.getResources().getIdentifier(resName, "drawable", myContext.getPackageName());

        return id;
    }
}
