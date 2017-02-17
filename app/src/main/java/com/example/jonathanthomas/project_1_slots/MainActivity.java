/**
 * Author: Jonathan Thomas
 * Version: 1.0
 * Date: 2/9/17
 * Description: mainActivity.java contains the logic behind the flower slots app.
 *              It is responsible for the flower animations, keeping track of the money,
 *              animates the flower rotation, and is responsible for the logic behind the
 *              go and reset buttons
 */
package com.example.jonathanthomas.project_1_slots;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/**
 * Logic behind the flower slot app.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gamePlaying();
    }

    /**
     * Main game loop. Contains listeners for go and reset buttons.
     * Also calculates new money values.
     */
    public void gamePlaying() {
        final ImageView goButton = (ImageView) findViewById(R.id.goBut);
        final ImageView resetButton = (ImageView) findViewById(R.id.reset);
        final ImageView flower1 = (ImageView) findViewById(R.id.f1);
        final ImageView flower2 = (ImageView) findViewById(R.id.f2);
        final ImageView flower3 = (ImageView) findViewById(R.id.f3);

        final ImageView[] flowers = {flower1, flower2, flower3};

        final TextView dolAmnt = (TextView) findViewById(R.id.dolAmnt);

        final Random randomNum = new Random();

        //reset game
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dolAmnt.setText("$5"); //reset money to $5
                goButton.setEnabled(true); //re-enable go button

                for (ImageView imV : flowers)//redraw original flowers
                    imV.setImageResource(R.drawable.f1);

                resetButton.setVisibility(View.INVISIBLE);//set reset button to invisible again
                resetButton.setEnabled(false);
            }
        });

        //spin flowers, calculate new dollar amount
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goButton pressed

                final Animation an = new RotateAnimation(0, 3600,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);//v.getWidth()/4, v.getHeight()/4);
                an.setDuration(1000);

                if (resetButton.getVisibility() == View.INVISIBLE)//not visible
                {
                    resetButton.setVisibility(View.VISIBLE);//make it visible
                    resetButton.setEnabled(true);
                }

                //spin flowers
                for (int i = 0; i < 3; i++) {
                    flowers[i].setImageResource(R.drawable.tmp);
                    flowers[i].startAnimation(an);

                    an.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            //prevents user from rapidly clicking
                            goButton.setEnabled(false);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            goButton.setEnabled(true); //reenable button to be clicked again
                            for (int j = 0; j < 3; j++) {
                                int randomInt = randomNum.nextInt(3);
                                switch (randomInt) {
                                    case 0:
                                        flowers[j].setImageResource((R.drawable.f1));
                                        break;
                                    case 1:
                                        flowers[j].setImageResource((R.drawable.f2));
                                        break;
                                    default:
                                        flowers[j].setImageResource((R.drawable.f3));
                                        break;
                                }
                            }

                            //calculate new dollar amount
                            if (imagesMatch(flower1, flower2) && imagesMatch(flower2, flower3)) {
                                //all images match
                                dolAmnt.setText("$" + Integer.toString((Integer.parseInt(dolAmnt.getText().toString().substring(1)) + 3)));
                            } else if (imagesMatch(flower1, flower2) || imagesMatch(flower1, flower3) || imagesMatch(flower2, flower3)) {
                                //two match
                                dolAmnt.setText("$" + Integer.toString((Integer.parseInt(dolAmnt.getText().toString().substring(1)) + 1)));
                            } else {
                                //none match
                                dolAmnt.setText("$" + Integer.toString((Integer.parseInt(dolAmnt.getText().toString().substring(1)) - 1)));
                            }

                            if (dolAmnt.getText().charAt(1) == '0')//no money left, disable go button
                                goButton.setEnabled(false);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });
    }

    /**
     * imagesMatch compares two of the flower images to determine if they match.
     * If they match, it returns true. If not, it returns false
     * @param image1 - first image to compare
     * @param image2 - second image to compare
     * @return true if the images match, false if they don't
     */
    private boolean imagesMatch(ImageView image1, ImageView image2) {
        if (image1.getDrawable().getConstantState()
                .equals(image2.getDrawable().getConstantState()))
            return true; //images match
        return false;//images dont match
    }
}