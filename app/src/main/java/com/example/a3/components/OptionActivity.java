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
    private String[] boardOptions = {"4 x 8", "5 x 10", "6 x 15"};
    private String[] mineOptions = {"6", "10", "15", "20", "24", "30"};

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
        Spinner spinner = (Spinner) findViewById(R.id.board_size_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, boardOptions);
        spinner.setAdapter(adapter);

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
//                Toast.makeText(OptionActivity.this, "row=" + row + " col=" + col, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupMineCountSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.mine_count_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mineOptions);
        spinner.setAdapter(adapter);

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
//                Toast.makeText(OptionActivity.this, "mine count=" + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}