package com.example.a3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.a3.components.GamePlay;
import com.example.a3.components.HelpActivity;
import com.example.a3.components.OptionActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Bad Apple Finder");
        }

        setupPlayButton();
        setupOptionButton();
        setupHelpButton();
    }

    private void setupPlayButton() {
        Button btn = (Button) findViewById(R.id.play);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, GamePlay.class);
                startActivity(i);
            }
        });
    }

    private void setupOptionButton() {
        Button btn = (Button) findViewById(R.id.options);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, OptionActivity.class);
                startActivity(i);
            }
        });
    }

    private void setupHelpButton() {
        Button btn = (Button) findViewById(R.id.help);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, HelpActivity.class);
                startActivity(i);
            }
        });
    }
}