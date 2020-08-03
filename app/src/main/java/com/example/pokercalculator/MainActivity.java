package com.example.pokercalculator;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static ImageButton currentCardTObeSet;
    static int currentCardIndex;
    static int numPlayers = 2;
    static double potSize = 100;

    static int[][] allCards = new int[7][2]; // unknown cards have value 0

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //myCard1 = (ImageButton) findViewById(R.id.myCard1);

        myDialog = new Dialog(this);

        Spinner spinnerNumPlayers = (Spinner) findViewById(R.id.spinner_num_players);

        ArrayAdapter<CharSequence> adapterNumPlayers = ArrayAdapter.createFromResource(MainActivity.this, R.array.num_players, android.R.layout.simple_spinner_item);
        adapterNumPlayers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerNumPlayers.setAdapter(adapterNumPlayers);
        spinnerNumPlayers.setOnItemSelectedListener(MainActivity.this);

    }

    public void computeOdds(View view) {

        PokerSimulations mySim = new PokerSimulations(numPlayers, allCards);
        double[] simResult = mySim.getWinProbability();

        double win = simResult[0] * 100;
        double sigma = simResult[1] * 100;
        double villanWin = (100 - win) / (numPlayers - 1);

        String winPercent = String.format("%.1f", win) + "%";
        String uncertainty = String.format("%.1f", sigma) + "%";

        String villanWinPercent = String.format("%.1f", villanWin) + "%";
        String villanUncertainty = String.format("%.1f", sigma / (numPlayers - 1)) + "%";


        TextView myPotSize = (TextView) findViewById(R.id.myPotSize);
        String stringPotSize = myPotSize.getText().toString();
        if (stringPotSize.matches("-?\\d+(\\.\\d+)?")) {
            potSize = Double.parseDouble(stringPotSize);
            myPotSize.setHint(stringPotSize);
        } else {
            potSize = 100;
            myPotSize.setHint("100");
        }

        TextView myWinPercentView = (TextView) findViewById(R.id.myWinPercent);
        TextView villanWinPercentView = (TextView) findViewById(R.id.villanWinPercent);
        TextView betLimitView = (TextView) findViewById(R.id.betLimit);
        TextView winTextView = (TextView) findViewById(R.id.winText);

        myWinPercentView.setText("Me = " + winPercent + " +/- " + uncertainty);
        villanWinPercentView.setText("Villain = " + villanWinPercent + " +/- " + villanUncertainty);
        betLimitView.setText("Limit = " + (int) (potSize * win / 100));

        if (win - villanWin > 2 * sigma) {
            winTextView.setTextColor(getResources().getColor(R.color.green));
        } else if (villanWin - win > 2 * sigma) {
            winTextView.setTextColor(getResources().getColor(R.color.red));
        } else {
            winTextView.setTextColor(getResources().getColor(R.color.white));
        }

    }

    public void setMyCard(View view) {

        switch (view.getId()) {

            case R.id.flop1:
                currentCardIndex = 0;
                break;
            case R.id.flop2:
                currentCardIndex = 1;
                break;
            case R.id.flop3:
                currentCardIndex = 2;
                break;
            case R.id.turn:
                currentCardIndex = 3;
                break;
            case R.id.river:
                currentCardIndex = 4;
                break;
            case R.id.myCard1:
                currentCardIndex = 5;
                break;
            case R.id.myCard2:
                currentCardIndex = 6;
                break;
        }

        currentCardTObeSet = (ImageButton) view;

        myDialog.setContentView(R.layout.select_card);

        final Spinner spinnerRank = (Spinner) myDialog.findViewById(R.id.spinner_rank);
        ArrayAdapter<CharSequence> adapterRank = ArrayAdapter.createFromResource(MainActivity.this, R.array.ranks, android.R.layout.simple_spinner_item);
        adapterRank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRank.setAdapter(adapterRank);
        //spinnerRank.setOnItemSelectedListener(MainActivity.this);

        final Spinner spinnerSuit = (Spinner) myDialog.findViewById(R.id.spinner_suit);
        ArrayAdapter<CharSequence> adapterSuit = ArrayAdapter.createFromResource(MainActivity.this, R.array.suits, android.R.layout.simple_spinner_item);
        adapterSuit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSuit.setAdapter(adapterSuit);
        //spinnerSuit.setOnItemSelectedListener(MainActivity.this);

        Button okButton = (Button) myDialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String suit = spinnerSuit.getSelectedItem().toString();
                String rank = spinnerRank.getSelectedItem().toString();
                String dummy = suit + rank;

                int cardDrawable = getDrawableCard(MainActivity.this, suit, rank);

                currentCardTObeSet.setImageResource(cardDrawable);
                myDialog.dismiss();
            }

        });


        TextView txtclose;
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        numPlayers = Integer.parseInt(text);
        //Toast.makeText(parent.getContext(), "" + numPlayers, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static int getDrawableCard(Context myContext, String suit, String rank) {

        allCards[currentCardIndex][0] = 0; // suit as unknown
        allCards[currentCardIndex][1] = 0; // rank as unknown

        String resName;

        if (rank.equals("unknown") || suit.equals("unknown")) {
            resName = "back";
        } else if (rank.equals("J")) {
            allCards[currentCardIndex][1] = 11; // rank
            resName = suit.substring(0, 1) + "_11";
        } else if (rank.equals("Q")) {
            allCards[currentCardIndex][1] = 12; // rank
            resName = suit.substring(0, 1) + "_12";
        } else if (rank.equals("K")) {
            allCards[currentCardIndex][1] = 13; // rank
            resName = suit.substring(0, 1) + "_13";
        } else {
            allCards[currentCardIndex][1] = Integer.parseInt(rank); // rank
            resName = suit.substring(0, 1) + "_" + rank;
        }

        String suitLetter = suit.substring(0, 1);

        if (!rank.equals("unknown") && !suit.equals("unknown")) {
            switch (suitLetter) {
                case "d":
                    allCards[currentCardIndex][0] = 1; // suit as diamonds
                    break;
                case "c":
                    allCards[currentCardIndex][0] = 2; // suit as clubs
                    break;
                case "h":
                    allCards[currentCardIndex][0] = 3; // suit as hearts
                    break;
                case "s":
                    allCards[currentCardIndex][0] = 4; // suit as spades
                    break;
            }
        }

        Toast.makeText(myContext, printTwoDArray(allCards), Toast.LENGTH_SHORT).show();

        int id = myContext.getResources().getIdentifier(resName, "drawable", myContext.getPackageName());

        return id;
    }

    public static String printTwoDArray(int[][] myArray) {
        String result = "";

        for (int i = 0; i < myArray.length; i++) {
            result = result + "[";
            for (int j = 0; j < myArray[i].length; j++) {
                result = result + myArray[i][j] + ",";
            }
            result = result + "] ";
        }
        return result;
    }


}
