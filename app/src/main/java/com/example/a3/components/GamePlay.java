package com.example.a3.components;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.a3.R;
import com.example.a3.model.Cell;
import com.example.a3.model.UserPreference;

import java.util.HashSet;
import java.util.Set;

public class GamePlay extends AppCompatActivity {
    private int row_total;
    private int col_total;
    private int mine_total;
    private Button[][] btns;
    Set<Cell> mine_locations;
    Set<Cell> found_mines;
    Set<Cell> button_with_number_shown;
    TextView found_text;
    TextView scan_used_text;
    int scan_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("");
        }

        UserPreference preference = UserPreference.getInstance();
        row_total = preference.getRow();
        col_total = preference.getCol();
        mine_total = preference.getMine();

//        Toast.makeText(this, "row=" + row_total + " col=" + col_total + " mine=" + mine_total, Toast.LENGTH_SHORT).show();

        mine_locations = new HashSet<Cell>();
        found_mines = new HashSet<Cell>();
        button_with_number_shown = new HashSet<Cell>();

        scan_count = 0;

        setupMines();
        setupButtons();
        setupMineTexts();
        setupScanTexts();
    }

    private void setupScanTexts() {
        scan_used_text = (TextView) findViewById(R.id.scan_number_text);
        scan_used_text.setText("# Scans used: " + scan_count);
    }

    private void setupMineTexts() {
        found_text = (TextView) findViewById(R.id.found_text);
        found_text.setText("Found " + found_mines.size() + " of " + mine_total + " mines.");
    }

    private void setupMines() {
        int depolyedMine = 0;

        while (depolyedMine < mine_total) {
            int row = (int) (Math.random() * (row_total));
            int col = (int) (Math.random() * (col_total));
            int previous = mine_locations.size();
            mine_locations.add(new Cell(row, col));
            if (mine_locations.size() == previous + 1) {
                depolyedMine++;
            }
        }
    }

    private void setupButtons() {
        btns = new Button[row_total][col_total];
        TableLayout table = (TableLayout) findViewById(R.id.tableForButtons);

        for (int row = 0; row < row_total; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);

            for (int col = 0; col < col_total; col++){
                final int FINAL_COL = col;
                final int FINAL_ROW = row;

                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));


                // Make text not clip on small buttons
                button.setPadding(0, 0, 0, 0);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_COL, FINAL_ROW);
                    }
                });

                tableRow.addView(button);
                btns[row][col] = button;
            }
        }
    }

    private void gridButtonClicked(int col, int row) {
        System.out.println("Printing All Mines");
        show(mine_locations);
        System.out.println("These are discovered mines");
        show(found_mines);

        Button button = btns[row][col];
        Cell tapped = new Cell(row, col);

        if (mine_locations.contains(tapped)) {
            // "tapped" is a mine
            if (!(found_mines.contains(tapped))) {
                // if this is the first visit of the mine
                // 1. change its background
                lockButtonSizes();
                int newWidth = button.getWidth();
                int newHeight = button.getHeight();
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bad_apple);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                Resources resource = getResources();
                button.setBackground(new BitmapDrawable(resource, scaledBitmap));

                // 2. update found_mines set
                found_mines.add(tapped);

                // 3. update scan reported text on buttons sitting on the same row or column
                updateAppearedText(row, col);

                // 4. update found mine count
                setupMineTexts();

                // if the last mine is located, congratulation screen shows
                if (found_mines.size() == mine_total) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(GamePlay.this);
                    LayoutInflater factory = LayoutInflater.from(GamePlay.this);
                    View v = factory.inflate(R.layout.congrat, null);
                    alert.setView(v);
                    alert.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {
                            finish();
                        }
                    });
                    alert.show();
                }
            } else {
                // if the mine is found, but the user want to scan on that location as well
                int nearMineCount = searchMines(row, col);
                button.setText(String.valueOf(nearMineCount));

                // if no text on the mine
                if (!button_with_number_shown.contains(tapped)) {
                    scan_count++;
                    button_with_number_shown.add(tapped);
                }
                // update found mine and scan count
                setupMineTexts();
                setupScanTexts();
            }
        } else {
            // if "tapped" is not a mine, set the text (scan report) on that button
            int nearMineCount = searchMines(row, col);
            button.setText(String.valueOf(nearMineCount));

            if (!button_with_number_shown.contains(tapped)) {
                scan_count++;
                button_with_number_shown.add(tapped);
            }
            setupMineTexts();
            setupScanTexts();
        }
    }

    private void updateAppearedText(int row, int col) {
        // if later a mine at (row, col) is revealed, update buttons' text who sit on the same row or column
        for(Cell c : button_with_number_shown) {
            if (c.getX() == row || c.getY() == col) {
                Button btn = btns[c.getX()][c.getY()];
                btn.setText(String.valueOf(Integer.parseInt(String.valueOf(btn.getText())) - 1));
            }
        }
    }

    private int searchMines(int row, int col) {
        // return total number of mines in this row and column
        Set<Cell> notFoundMines = mine_locations;
        for (Cell x : found_mines) {
            if (notFoundMines.contains(x)) {
                notFoundMines.remove(x);
            }
        }

        int hiddenMinesCount = 0;
        for (Cell x : notFoundMines) {
            if (x.getX() == row || x.getY() == col) {
                hiddenMinesCount++;
            }
        }
        return hiddenMinesCount;
    }

    private void lockButtonSizes() {
        for (int row = 0; row < row_total; row++) {
            for (int col = 0; col < col_total; col++) {
                Button button = btns[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    private void show(Set<Cell> set) {
        // supporting function, for debugging usage
        for (Cell cell : set) {
            System.out.print("[" + cell.getX() + ", " + cell.getY() + "]" + ", ");
        }
    }
}