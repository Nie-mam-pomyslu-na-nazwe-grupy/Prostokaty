package com.example.draganddrop;

public class Brick {
    int color;
    int[] brickSize;
    Brick(int sizeX, int sizeY, Player player)
    {
        brickSize = new int[2];
        brickSize[0] = sizeX;
        brickSize[1] = sizeY;

        this.color = player.getPlayerColor();
    }


    public int getBrickX()
    {
        return brickSize[0];
    }

    public int getBrickY()
    {
        return brickSize[1];
    }

    public int getBrickColor()
    {
        return this.color;
    }

    public void rollBrick()
    {
        int tmp = brickSize[0];
        brickSize[0] = brickSize[1];
        brickSize[1] = tmp;
    }
}
