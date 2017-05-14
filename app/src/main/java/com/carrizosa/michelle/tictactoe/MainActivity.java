package com.carrizosa.michelle.tictactoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.NewGameButton:
                this.startGame();
                break;
            default:
                Button button = (Button) v;
                button.setText(this.currentPlayer.toString());
                this.newTurn();
                break;
        }
    }

    private enum Player {
        X,
        O;
    }

    private Button gameGrid[][];
    private Button newGameButton;
    private TextView announceDisplayer;
    private Player currentPlayer;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameGrid = new Button[3][3];
        gameGrid[0][0] = (Button) findViewById(R.id.square1);
        gameGrid[0][1] = (Button) findViewById(R.id.square2);
        gameGrid[0][2] = (Button) findViewById(R.id.square3);
        gameGrid[1][0] = (Button) findViewById(R.id.square4);
        gameGrid[1][1] = (Button) findViewById(R.id.square5);
        gameGrid[1][2] = (Button) findViewById(R.id.square6);
        gameGrid[2][0] = (Button) findViewById(R.id.square7);
        gameGrid[2][1] = (Button) findViewById(R.id.square8);
        gameGrid[2][2] = (Button) findViewById(R.id.square9);

        newGameButton = (Button) findViewById(R.id.NewGameButton);
        announceDisplayer = (TextView) findViewById(R.id.announceDisplayer);

        newGameButton.setOnClickListener(this);
        for(Button[] line: this.gameGrid){
            for(Button button: line){
                button.setOnClickListener(this);
            }
        }

        this.savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.currentPlayer = Player.valueOf(this.savedValues.getString("currentPlayer", "O"));
        this.displayCurrentPlayer();
    }

    @Override
    protected void onPause(){
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("currentPlayer", this.currentPlayer.toString());
        editor.commit();

        super.onPause();
    }

    private void clearGrid(){
        for(Button[] line: this.gameGrid){
            for(Button button: line){
                button.setText("");
            }
        }
    }

    private void startGame(){
        this.clearGrid();
        this.currentPlayer = Player.O;
        this.displayCurrentPlayer();
    }

    private void newTurn(){
        if(this.checkForGameOver()){
            this.announceDisplayer.setText("Player " + this.currentPlayer + " has won !");
            return;
        }
        if(this.currentPlayer == Player.O) {
            this.currentPlayer = Player.X;
        } else {
            this.currentPlayer = Player.O;
        }
        this.displayCurrentPlayer();
    }

    private void displayCurrentPlayer(){
        this.announceDisplayer.setText("Player " + this.currentPlayer + "'s turn");
    }

    private boolean checkForGameOver() {
        CharSequence current;
        // check lines
        for (Button[] line : this.gameGrid) {
            if (line[0].getText() != "" &&
                    line[0].getText() == line[1].getText() &&
                    line[1].getText() == line[2].getText())
                return true;
        }
        // check columns
        for(int j = 0; j < this.gameGrid[0].length; j++){
            if (this.gameGrid[0][j].getText() != "" &&
                    this.gameGrid[0][j].getText() == this.gameGrid[1][j].getText() &&
                    this.gameGrid[1][j].getText() == this.gameGrid[2][j].getText())
                return true;
        }
        // check diagonal
        if (this.gameGrid[0][0].getText() != "" &&
                this.gameGrid[0][0].getText() == this.gameGrid[1][1].getText() &&
                this.gameGrid[1][1].getText() == this.gameGrid[2][2].getText())
            return true;
        return false;
    }
}
