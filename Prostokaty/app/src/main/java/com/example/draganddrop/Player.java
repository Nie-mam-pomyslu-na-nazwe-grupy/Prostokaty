package com.example.draganddrop;

public class Player {
    int[] corner;
    int color;    // 1-4
    int score;

    Player()
    {
        corner = new int[2];
        score = 0;
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

    public void setScore(int s){ score += s; }
    public int getScore(){return score;}

}
