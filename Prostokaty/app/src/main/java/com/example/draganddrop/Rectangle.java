package com.example.draganddrop;

import android.content.Context;
import android.widget.ImageView;

public class Rectangle extends ImageView {
    public int xCoord;
    public int yCoord;
    public int dimX;
    public int dimY;
    public int grid;
    public boolean canMove = true;


    public Rectangle(Context context){
        super(context);
    }
}
