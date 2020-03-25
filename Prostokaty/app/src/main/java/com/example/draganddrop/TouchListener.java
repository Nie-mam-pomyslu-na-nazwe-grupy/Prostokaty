package com.example.draganddrop;

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

        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                rect.bringToFront();
                break;
            case MotionEvent.ACTION_MOVE:
                lParams.leftMargin = (int) (x - xDelta);
                lParams.topMargin = (int) (y - yDelta);
                view.setLayoutParams(lParams);
                //todo: przeniesc snap to grid z action up do move, niech klocek bedzie szary podczas action move
                break;
            case MotionEvent.ACTION_UP:
                //dodać if(jest w zakresie planszy)
                int yDiff =  Math.round((motionEvent.getRawY() - yDelta)   / rect.grid ) * rect.grid; //ustawia zmienna odpowiedzialna za pozycje na iloraz szerokosci kratki(grid)
                int xDiff =  Math.round((motionEvent.getRawX() - xDelta)  / rect.grid ) * rect.grid;

                //todo: zrobić z if'a bardziej uniwersalny if(szczegoly w komentarzach)
                if( yDiff >= MainActivity.startBoard && yDiff <= MainActivity.startBoard + 27*rect.grid //27 = PlanszaY-RozmiarKlocka+1 (rozmiar klocka nie wiem czy x czy y)
                && xDiff >= MainActivity.topBoard + 5*rect.grid && xDiff <= MainActivity.topBoard + rect.grid*21 ){//5=rozmiarem prostokata+1, 21= PlanszaX+1
                    lParams.topMargin = yDiff;
                    lParams.leftMargin = xDiff;
                }
                rect.setLayoutParams(lParams);
                    //rect.canMove = false;//odkomentowac jak wszystko bedzie dzialalo = blokuje dalsze poruszanie
                    //sendViewToBack(rect);//nie działa, bo funkcja jest w main a nie tutaj

                break;
        }

        return true;
    }


}
