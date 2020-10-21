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

        //gameInfoLog.setEnabled(false);


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


        String tempSecondString = secondInstance.toString();
        String tempFourthString = fourthInstance.toString();

        if(tempSecondString.equals(tempFourthString)){
            gameInfoLog.setText("# The separate deep copies match!\n\n\n");
        }

        gameInfoLog.setText(gameInfoLog.getText().toString().concat(tempSecondString + "\n" + tempFourthString));

        //check undo
        if(firstInstance.undo(firstInstance)){
            gameInfoLog.setText(gameInfoLog.getText().toString().concat("\n\n# The undo was successful"));
        }

        //check reset
        if(firstInstance.reset(firstInstance)){
            gameInfoLog.setText(gameInfoLog.getText().toString().concat("\n\n# The reset was successful\n\n"));
        }

        //TODO fix move() to actually move piece (can be tested with below)
        //move spy to square 1 above piece
        //attack with other piece
        firstInstance.move(firstInstance.getBoardSquares()[8][6], 8, 4, false );
        gameInfoLog.setText(gameInfoLog.getText().toString().concat(firstInstance.toString()));


    }

}