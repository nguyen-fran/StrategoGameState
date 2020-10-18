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
        gameInfoLog.setText((CharSequence) gameState.toString());
    }

}