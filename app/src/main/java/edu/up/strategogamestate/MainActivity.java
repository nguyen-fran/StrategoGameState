package edu.up.strategogamestate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText gameInfoLog;
    private StrategoGameState gameState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameState = new StrategoGameState();

        Button runTestBtn = findViewById(R.id.runTestButton);
        runTestBtn.setOnClickListener(this);

        gameInfoLog = findViewById(R.id.gameInfoLog);


    }

    /*
    *prints the current game info using the gameState's toString method
    * can either cast as charsequence and pass directly to edittext
    * or
    * make separate charsequence variable and pass
     */
    @Override
    public void onClick(View v) {
        //clear out any previous text from data space
        gameInfoLog.setText("");

        StrategoGameState firstInstance = new StrategoGameState();

        //creating deep copy of firstInstance
        StrategoGameState secondInstance = new StrategoGameState(firstInstance);

        //call each method of StrategoGameState

        StrategoGameState thirdInstance = new StrategoGameState();
        StrategoGameState fourthInstance = new StrategoGameState(thirdInstance);

        //TODO make sure new instances of the game state have the same board layout

        String tempSecondString = secondInstance.toString();
        String tempFourthString = fourthInstance.toString();

        if(tempSecondString.equals(tempFourthString)){
            gameInfoLog.setText("The seperate deep copies match!");
        }

        gameInfoLog.setText(gameInfoLog.getText().toString().concat(tempSecondString + "\n" + tempFourthString));
        gameInfoLog.setText(gameState.toString());

    }

}