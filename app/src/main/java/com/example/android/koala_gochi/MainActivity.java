package com.example.android.koala_gochi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private String koala_name_entry;
    private EditText koala_name_edittext;
    private Button startButton;

    private CheckBox gamelenghtCheckBox;
    private CheckBox progressivCheckBox;

    private boolean isShortGame;
    private boolean isProgressive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        koala_name_edittext = (EditText) findViewById(R.id.koala_name);
        gamelenghtCheckBox = (CheckBox) findViewById(R.id.game_length);
        progressivCheckBox = (CheckBox) findViewById(R.id.game_rythm);
        startButton = (Button) findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent i = new Intent(getApplicationContext(), Game.class);

                koala_name_entry = koala_name_edittext.getText().toString();

                isProgressive = progressivCheckBox.isChecked();
                isShortGame = gamelenghtCheckBox.isChecked();

                i.putExtra("name", koala_name_entry);
                i.putExtra("length", isShortGame);
                i.putExtra("rythm", isProgressive);

                startActivity(i);
            }
        });
    }
}