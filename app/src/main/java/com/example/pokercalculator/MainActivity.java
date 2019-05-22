package com.example.pokercalculator;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static ImageButton currentCardTObeSet;
    static int numPlayers;

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

    public void setMyCard(View view){

        currentCardTObeSet = (ImageButton) view;

        myDialog.setContentView(R.layout.select_card);

        final Spinner spinnerRank = (Spinner) myDialog.findViewById(R.id.spinner_rank);
        final Spinner spinnerSuit = (Spinner) myDialog.findViewById(R.id.spinner_suit);

        ArrayAdapter<CharSequence> adapterRank = ArrayAdapter.createFromResource(MainActivity.this, R.array.ranks, android.R.layout.simple_spinner_item);
        adapterRank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterSuit = ArrayAdapter.createFromResource(MainActivity.this, R.array.suits, android.R.layout.simple_spinner_item);
        adapterSuit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRank.setAdapter(adapterRank);
        //spinnerRank.setOnItemSelectedListener(MainActivity.this);
        spinnerSuit.setAdapter(adapterSuit);
        //spinnerSuit.setOnItemSelectedListener(MainActivity.this);

        Button okButton = (Button) myDialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String suit = spinnerSuit.getSelectedItem().toString();
                String rank = spinnerRank.getSelectedItem().toString();
                String dummy = suit + rank;

                int cardDrawable = PokerCards.getDrawableCard(MainActivity.this, suit, rank);

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
        Toast.makeText(parent.getContext(), ""+numPlayers, Toast.LENGTH_SHORT).show();
        //currentCardTObeSet.setImageResource(R.drawable.c_1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void SelectCard(View v) {


    }

}
