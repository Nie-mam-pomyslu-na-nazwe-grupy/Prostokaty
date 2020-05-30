package com.example.draganddrop;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import android.widget.RelativeLayout;

import static com.example.draganddrop.MainActivity.BdimX;
import static com.example.draganddrop.MainActivity.BdimY;
import static com.example.draganddrop.MainActivity.startBoard;
import static com.example.draganddrop.MainActivity.topBoard;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public class TouchListener implements View.OnTouchListener{
    private float xDelta; // początek widoku
    private float yDelta;

    public int gracz;
    public Engine engine;

    public void takeEngine(Engine e, int g)
    {
        engine = e;
        gracz = g;
    }

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

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //xDelta i yDelta to pozycje w ktorych znajduje sie palec podczas podnoszenia klocka.
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                rect.bringToFront();
                break;


            case MotionEvent.ACTION_MOVE:
                //ustalenie nowych parametrow
                int yDiff =  ( Math.round((motionEvent.getRawY() - yDelta)   / rect.grid ) * rect.grid  ) + topBoard%rect.grid ;; //ustawia zmienna odpowiedzialna za pozycje na iloraz szerokosci kratki(grid)
                int xDiff = ( Math.round((motionEvent.getRawX() - xDelta)  / rect.grid ) * rect.grid ) + startBoard%rect.grid +10;

                if( yDiff >= topBoard && yDiff <= topBoard + ( (BdimY - rect.dimY  ) * rect.grid)
                        && xDiff >= startBoard  && xDiff <= startBoard + ( (BdimX - rect.dimX + 1 ) * rect.grid)  ) {

                    lParams.topMargin = yDiff;
                    lParams.leftMargin = xDiff; //pozycja w którym jest palec

                    rect.xCoord = (xDiff - startBoard) / rect.grid; //wspolrzedne na planszy
                    rect.yCoord = (yDiff - topBoard) / rect.grid;


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
                //postawienie klocka

                if (((RelativeLayout.LayoutParams) view.getLayoutParams()).topMargin >= topBoard && lParams.topMargin <= topBoard + ( (BdimY - rect.dimY  ) * rect.grid)
                        && ((RelativeLayout.LayoutParams) view.getLayoutParams()).leftMargin >= startBoard && lParams.leftMargin <=  startBoard + ( (BdimX - rect.dimX + 1 ) * rect.grid)  )   {

                    Brick B = new Brick(rect.dimX, rect.dimY, engine.player[gracz]);//todo

                    //sprawdzanie czy mozemy postawic klocek w danym miejscu w relacji do reszty klockow w grze
                    if (engine.canPlace(rect.xCoord, rect.yCoord, B)){
                        engine.placeBrick(rect.xCoord, rect.yCoord, B );
                        rect.canMove = false;
                        rect.setAlpha(1f);//zmienia przezroczystosc klocka na 100% podczas gdy sie nie rusza
                        engine.player[gracz].setScore( rect.dimX * rect.dimY );
                    }

                    }//duży if


                }//motion event

                rect.setLayoutParams(lParams);

                break;
        }

        return true;
    }


}
