package com.example.draganddrop;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public class TouchListener implements View.OnTouchListener{
    private float xDelta; // początek widoku?
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
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                rect.bringToFront();
                break;


            case MotionEvent.ACTION_MOVE:
                //xDiff i yDiff mozna uzyc do obliczenia wspolrzednych klocka na planszy.
                int yDiff =  Math.round((motionEvent.getRawY() - yDelta)   / rect.grid ) * rect.grid; //ustawia zmienna odpowiedzialna za pozycje na iloraz szerokosci kratki(grid)
                int xDiff =  Math.round((motionEvent.getRawX() - xDelta)  / rect.grid ) * rect.grid;

                if( yDiff >= MainActivity.startBoard && yDiff <= MainActivity.startBoard + (MainActivity.BdimY-rect.dimY+2)*rect.grid
                        && xDiff >= MainActivity.topBoard + (rect.dimX)*rect.grid && xDiff <= MainActivity.topBoard + rect.grid*(MainActivity.BdimX) ){
                    lParams.topMargin = yDiff;
                    lParams.leftMargin = xDiff; //pozycja w którym jest palec
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

                if (((RelativeLayout.LayoutParams) view.getLayoutParams()).topMargin >= MainActivity.startBoard && lParams.topMargin <= MainActivity.startBoard + (MainActivity.BdimY-rect.dimY+2) * rect.grid //27 = PlanszaY-RozmiarKlocka+1 (rozmiar klocka nie wiem czy x czy y)
                        && ((RelativeLayout.LayoutParams) view.getLayoutParams()).leftMargin >= MainActivity.topBoard + (rect.dimX) * rect.grid && lParams.leftMargin <= MainActivity.topBoard + rect.grid * (MainActivity.BdimX)) {//5=rozmiarem prostokata+1, 21= PlanszaX+1
                    rect.canMove = false;
                    rect.setAlpha(1f);//zmienia przezroczystosc klocka na 100% podczas gdy sie nie rusza
                }

                rect.setLayoutParams(lParams);
                //sendViewToBack(rect);//nie działa, bo funkcja jest w main a nie tutaj

                break;
        }

        return true;
    }


}
