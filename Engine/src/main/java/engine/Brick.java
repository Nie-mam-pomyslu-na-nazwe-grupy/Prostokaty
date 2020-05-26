
package engine;

public class Brick {
    int color;
    int[] brickSize;
    Brick(int sizeX, int sizeY, Player player)
    {
        brickSize = new int[2];
        
        if(player.direction[0] == 0)
            brickSize[0] = -sizeX;
        else brickSize[0] = sizeX;
        
        if(player.direction[1] == 0)
            brickSize[1] = -sizeY;
        else brickSize[1] = sizeY;
        
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
