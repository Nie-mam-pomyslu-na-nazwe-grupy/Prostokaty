package com.example.draganddrop;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public class TouchListener implements View.OnTouchListener{
    private float xDelta;
    private float yDelta;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();

        Rectangle rect = (Rectangle) view;
        if(!rect.canMove){
            return true;
        }

        boolean moved=false;

        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

        //lParams.addRule(RelativeLayout.CENTER_HORIZONTAL,0);/
        //lParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,0);


        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //xDelta i yDelta to pozycje w ktorych znajduje sie palec podczas podnoszenia klocka.
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                rect.bringToFront();
                break;


            case MotionEvent.ACTION_MOVE:
                //ustalenie nowych parametrow
                int yDiff =  Math.round((motionEvent.getRawY() - yDelta)   / rect.grid ) * rect.grid; //ustawia zmienna odpowiedzialna za pozycje na iloraz szerokosci kratki(grid)
                int xDiff =  Math.round((motionEvent.getRawX() - xDelta)  / rect.grid ) * rect.grid;

                Log.d("",xDiff + " " + yDiff + " "+  rect.dimX );
                if( yDiff >= 80 && yDiff <= 1280 - ( rect.dimY * rect.grid)
                        && xDiff >= 160 && xDiff <= 960 - (rect.dimX * rect.grid)  ) {

                    lParams.topMargin = yDiff;
                    lParams.leftMargin = xDiff;
                }
                else //jesli ruszamy nim poza plansza to nie snapuje do siatki
                {
                    lParams.leftMargin = (int) (x - xDelta);
                    lParams.topMargin = (int) (y - yDelta);
                }

                rect.setAlpha(0.7f);//zmienia przezroczystosc klocka podczas ruszania nim
                view.setLayoutParams(lParams);
                break;
            case MotionEvent.ACTION_UP:

                if (((RelativeLayout.LayoutParams) view.getLayoutParams()).topMargin >= 80 && lParams.topMargin <= 1280 - ( rect.dimY * rect.grid)
                        && ((RelativeLayout.LayoutParams) view.getLayoutParams()).leftMargin >= 160 && lParams.leftMargin <=  960 - (rect.dimX * rect.grid)  ) {
                    rect.canMove = false;
                    rect.setAlpha(1f);//zmienia przezroczystosc klocka na 100% podczas gdy sie nie rusza
                }

                rect.setLayoutParams(lParams);


                break;
        }

        return true;
    }


}
