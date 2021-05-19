package com.example.game_connect3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //For animating the playAgainLayout dialog box
    Animation slideup;

    //0- blue, 1- red
    int activePlayer=0;

    //Even after the game has ended and the dialogue box has popped up, the user can still pay the game, so to prevent that we must declare another variable
    boolean gameIsActive = true;

    //We use gameState here since without it the user will be able to tap on an already occupied square and will be able to replace the current circle
    
    //2 means not-played
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};  //9 empty squares

    //Now, declare all the winning positions- all horizontal rows (0,1,2), all vertical columns (0,3,6) and all diagonal positions
    int [][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

    //We can use tags (extra piece of info that can be associated with a view) to update the gameState from empty to occupied

    //Drop in the blue circle on click
    public void dropIn (View view)
    {
        //Remove the initial src for the image to not be seen, all though the measurements and position of the image are still present
        ImageView counter = (ImageView) view; //we don't need R.id here as dropIn refers to that particular imageView itself!

        int tappedCounter = Integer.parseInt(counter.getTag().toString());  //To get the tag of the tapped counter/circle in the form of an integer

        //If the square is empty
        if (gameState[tappedCounter] == 2 && gameIsActive)  //tappedCounter identified by Tag
        {
            gameState[tappedCounter] = activePlayer; //assign the gameState as either 0 or 1 since it is not empty anymore.

            counter.setTranslationY(-1000f);  //To put the ImageView out of the screen initially to create the dropIn effect

            //Now we want to set an Image resource to be dropped in since we had removed the red circle from src

            //If active player is blue, then the next player will be red
            if (activePlayer==0)
            {
                counter.setImageResource(R.drawable.blue);
                activePlayer=1;
            }
            else
            {
                counter.setImageResource(R.drawable.red);
                activePlayer=0;
            }

            //Now to animate the dropIn (include rotation if u want)
            counter.animate().translationYBy(1000f).rotation(180).setDuration(300);

            //Loop through all the winning positions to check for the winner- for eg: if {0,1,2} winning positions have game state as 1,1,1
            //it means that the red player has won, if game state is 0,0,0 then blue player has won
            for (int [] winningPosition : winningPositions)
            {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]]
                        && gameState[winningPosition[0]]!=2)
                {
                    //Someone has won
                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0)
                    {
                        winner = "Blue";
                    }

                    TextView winnerMsg = (TextView)findViewById(R.id.winnerText);
                    winnerMsg.setText(winner + " has won!");

                    LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                    slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);

                    if(layout.getVisibility()==View.INVISIBLE) {
                        layout.startAnimation(slideup);
                        layout.setVisibility(View.VISIBLE);
                    }
                }
                //In case the game ends in a draw
                else
                {
                    boolean gameIsOver = true;

                    for (int counterState : gameState) {
                        if (counterState == 2) gameIsOver = false;
                    }

                    if (gameIsOver) {

                        TextView winnerMsg = (TextView)findViewById(R.id.winnerText);
                        winnerMsg.setText("It is a draw!");

                        LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                        slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);

                        if(layout.getVisibility()==View.INVISIBLE) {
                            layout.startAnimation(slideup);
                            layout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    public void playAgain(View view)
    {
        gameIsActive = true;

        LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
        layout.setVisibility(View.INVISIBLE);

        activePlayer = 0;

        //We cannot directly assign gameState = {2,2,2,2,2,2,2,2,2} since it is not an array
        //To reset the game state
        for (int i=0; i < gameState.length; i++)
        {
            gameState[i] = 2;
        }

        //To remove all the image resources from the grid layout
        GridLayout grid = (GridLayout)findViewById(R.id.gridLayout);

        for(int i = 0; i < grid.getChildCount(); i++) {
            //set image resource to 0 to remove it
            ((ImageView) grid.getChildAt(i)).setImageResource(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}