package com.example.tictactoe;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];

    private Button btPlayAgain;

    private boolean p1Turn = true;

    private int turnCount;

    private int p1Points;
    private int p2Points;

    private TextView tvP1Score;
    private TextView tvP2Score;

    private TextView tvGameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btPlayAgain = findViewById(R.id.bt_play_again);

        tvP1Score = findViewById(R.id.tv_p1_score);
        tvP2Score = findViewById(R.id.tv_p2_score);

        tvGameStatus = findViewById(R.id.tv_game_status);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String btID = "bt_" + i + j;
                int resID = getResources().getIdentifier(btID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button btResetRound = findViewById(R.id.bt_reset_round);
        btResetRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGrid();
            }
        });

        btPlayAgain.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                resetGrid();

                btPlayAgain.setClickable(false);
                btPlayAgain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btUnclickable)));
            }
        });

        Button btResetGame = findViewById(R.id.bt_reset_game);
        btResetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        // if the text of the space clicked on within the 3x3 grid isn't empty, that means it is either 'X' or 'O', and hence the clicking activity should be ignored
        if (!(((Button) v).getText().toString().equals(""))) {
            return;
        }

        // otherwise, the text of the space clicked on within the 3x3 grid is empty, and so the space must be occupied with either 'X' or 'O', depending on which player's turn it is
        // if Player 1 clicked on the empty space, occupy the empty space with 'X' and make the text color blue as per convention
        if (p1Turn) {
            ((Button) v).setText("X");
            ((Button) v).setTextColor(getResources().getColor(R.color.p1Color));
        }

        // if Player 1 didn't click on the empty space, then it must have been Player 2, so occupy the empty space with 'O' and make the text color red as per convention
        else {
            ((Button) v).setText("O");
            ((Button) v).setTextColor(getResources().getColor(R.color.p2Color));
        }

        // at this point, since a valid turn has taken place, increment the turn count
        turnCount++;

        // before ending the turn, check to see if there has been a win
        if (checkForWin()) {
            // since it is only possible for a player to win on their own turn, just check whose turn it was to determine which player won
            if (p1Turn) {
                p1Wins();
            }

            // if it wasn't Player 1's turn, then it must have been Player 2's turn
            else {
                p2Wins();
            }
        }

        // if there hasn't been a win this turn, check if there has been a draw
        // if there have been 9 valid turns including this turn, that means the 3x3 grid is fully occupied and if a win still hasn't occurred, then it is a draw
        else if (turnCount == 9) {
            draw();
        }

        // otherwise, end turn, invert boolean value of 'p1Turn' and change game status text to indicate that is now the other player's turn
        else {
            p1Turn = !p1Turn;

            if (p1Turn) {
                tvGameStatus.setText("Player 1's turn");
            }

            else {
                tvGameStatus.setText("Player 2's turn");
            }
        }
    }

    private boolean checkForWin() {
        String[][] grid = new String[3][3];

        // convert buttons' text to String and store in 2D array 'grid'
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = buttons[i][j].getText().toString();
            }
        }

        // check horizontal win case row-by-row for each of the 3 rows in the 3x3 grid
        for (int i = 0; i < 3; i++) {
            // if the text of all 3 buttons in the row is identical and the identical text isn't empty, that means it is either 'X' or 'O', so it's a win case
            if ((grid[i][0].equals(grid[i][1])) && (grid[i][1].equals(grid[i][2])) && !(grid[i][0].equals(""))) {
                return true;
            }
        }

        // check vertical win case column-by-column for each of the 3 columns in the 3x3 grid
        for (int i = 0; i < 3; i++) {
            // if the text of all 3 buttons in the column is identical and the identical text isn't empty, that means it is either 'X' or 'O', so it's a win case
            if ((grid[0][i].equals(grid[1][i])) && (grid[1][i].equals(grid[2][i])) && !(grid[0][i].equals(""))) {
                return true;
            }
        }

        // there are only two distinct diagonal win cases for a 3x3 grid, so check them one-by-one

        // check first diagonal win case that goes from top-left corner to bottom-right corner
        // if the text of all 3 buttons in the diagonal is identical and the identical text isn't empty, that means it is either 'X' or 'O', so it's a win case
        if ((grid[0][0].equals(grid[1][1])) && (grid[1][1].equals(grid[2][2])) && !(grid[0][0].equals(""))) {
            return true;
        }

        // check second diagonal win case that goes from bottom-left corner to top-right corner
        // if the text of all 3 buttons in the diagonal is identical and the identical text isn't empty, that means it is either 'X' or 'O', so it's a win case
        if ((grid[2][0].equals(grid[1][1])) && (grid[1][1].equals(grid[0][2])) && !(grid[2][0].equals(""))) {
            return true;
        }

        // if none of the above win cases apply, then a win has not occurred
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void p1Wins() {
        p1Points++;
        // Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        tvGameStatus.setText("Player 1 wins!");
        updateScore();

        btPlayAgain.setClickable(true);
        btPlayAgain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btClickable)));

        // resetGrid();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void p2Wins() {
        p2Points++;
        // Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        tvGameStatus.setText("Player 2 wins!");
        updateScore();

        btPlayAgain.setClickable(true);
        btPlayAgain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btClickable)));

        // resetGrid();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void draw() {
        // Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        tvGameStatus.setText("Draw!");

        btPlayAgain.setClickable(true);
        btPlayAgain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btClickable)));

        // resetGrid();
    }

    private void updateScore() {
        // updates the score with the up-to-date player points every time a win occurs
        tvP1Score.setText(Integer.toString(p1Points));
        tvP2Score.setText(Integer.toString(p2Points));
    }

    private void resetGrid() {
        // traverse through the rows and columns of the 3x3 grid, reverting each space to be unoccupied
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }

        // revert turn count back to 0 and make it Player 1's turn, since Player 1 ('X') always starts the round
        turnCount = 0;
        p1Turn = true;
        tvGameStatus.setText("Player 1's turn");
    }

    private void resetGame() {
        // resetting the game reverts the score to 0 - 0 in addition to resetting the grid/round.
        resetGrid();
        p1Points = 0;
        p2Points = 0;
        updateScore();
    }
}
