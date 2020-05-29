package com.example.draganddrop;

public class Player {
    int[] corner;
    int[] direction;
    int color;    // 1-4

    Player()
    {
        corner = new int[2];
        direction = new int[2];
    }

    public void setPlayerColor(int color)
    {
        this.color = color;
    }

    public int getPlayerColor()
    {
        return this.color;
    }

    public int getPlayerCornerX()
    {
        return this.corner[0];
    }

    public int getPlayerCornerY()
    {
        return this.corner[1];
    }

    public int getPlayerDirectionX()
    {
        return this.direction[0];
    }

    public int getPlayerDirectionY()
    {
        return this.direction[1];
    }
}
