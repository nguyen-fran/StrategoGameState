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

        /**
         External Citation
         Date: 10 October 2020
         Problem: Needed to stop EditText from recieving user input
         Resource:
         https://stackoverflow.com/questions/9470171/edittext-non-editable
         Solution: I used the example code from this post.
         */
        gameInfoLog.setKeyListener(null);


    }

    /*
    *prints the current game info using the gameState's toString method
    * can either cast as charsequence and pass directly to edittext
    * or
    * make separate charsequence variable and pass
     */
    @Override
    public void onClick(View v) {
        //CharSequence update = gameState.toString();
        gameInfoLog.setText((CharSequence) gameState.toString());
    }

}