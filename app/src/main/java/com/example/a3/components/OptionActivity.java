package com.example.a3.components;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.a3.R;
import com.example.a3.model.UserPreference;

public class OptionActivity extends AppCompatActivity {
    private String[] boardOptions = {"4 x 10", "5 x 12", "6 x 15"};
    private String[] mineOptions = {"10", "15", "20", "24", "30", "50", "60"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("Options");
        }

        setupBoardSizeSpinner();
        setupMineCountSpinner();
    }

    private void setupBoardSizeSpinner() {
        // set up drop down menu content
        Spinner spinner = (Spinner) findViewById(R.id.board_size_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, boardOptions);
        spinner.setAdapter(adapter);

        // set default position
        int default_position = 0;
        UserPreference preference = UserPreference.getInstance();
        String current = preference.getRow() + " x " + preference.getCol();
        for (String s : boardOptions) {
            if (current.equals(s)) {
                break;
            }
            default_position++;
        }
        spinner.setSelection(default_position);

        // setup item click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                String[] splited = item.split("x");
                UserPreference preference = UserPreference.getInstance();
                int row = Integer.parseInt(splited[0].replaceAll(" ", ""));
                int col = Integer.parseInt(splited[1].replaceAll(" ", ""));

                if (row * col < preference.getMine()) {
                    Toast.makeText(OptionActivity.this, "Table is too little", Toast.LENGTH_SHORT).show();
                } else {
                    preference.resetTable(row, col);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupMineCountSpinner() {
        // set up drop down menu content
        Spinner spinner = (Spinner) findViewById(R.id.mine_count_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mineOptions);
        spinner.setAdapter(adapter);

        // set default position
        int default_position = 0;
        UserPreference preference = UserPreference.getInstance();
        String current = String.valueOf(preference.getMine());
        for (String s : mineOptions) {
            if (current.equals(s)) {
                break;
            }
            default_position++;
        }
        spinner.setSelection(default_position);

        // setup item click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                UserPreference preference = UserPreference.getInstance();
                int mine_count = Integer.parseInt(item);
                if (preference.getRow() * preference.getCol() < mine_count) {
                    Toast.makeText(OptionActivity.this, "Too many mines", Toast.LENGTH_SHORT).show();
                } else {
                    preference.resetMine(mine_count);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}