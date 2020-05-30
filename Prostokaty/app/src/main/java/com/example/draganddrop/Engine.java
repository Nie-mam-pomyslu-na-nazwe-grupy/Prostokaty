package com.example.draganddrop;

public class Engine {
    private int boardX;
    private int boardY;
    public int[][] boardMain;
    private int numberOfGamers;
    public Player[] player;


    public Engine(int boardX, int boardY, int numberOfGamers)
    {
        this.boardX = boardX;
        this.boardY = boardY;
        boardMain = new int[boardX][boardY];

        //wypełnienie tablic zerami
        for(int i=0; i<this.boardX; i++)
        {
            for(int j=0; j<this.boardY; j++)
            {
                boardMain[i][j] = 0;
            }
        }

        this.numberOfGamers = numberOfGamers;
        player = new Player[this.numberOfGamers];
        for(int i = 0; i<this.numberOfGamers; i++)
            player[i] = new Player();


        // przypisanie rogu i koloru do gracza
        for(int i=0; i<this.numberOfGamers; i++)
        {
            player[i].setPlayerColor(i+1);
            switch(i)
            {
                case 0:
                {
                    player[i].corner[0] = 0;
                    player[i].corner[1] = 0;
                    break;
                }
                case 1:
                {
                    player[i].corner[0] = boardX-1;
                    player[i].corner[1] = boardY-1;
                    break;
                }
                case 2:
                {
                    player[i].corner[0] = 0;
                    player[i].corner[1] = boardY-1;
                    break;
                }
                case 3:
                {
                    player[i].corner[0] = boardX-1;
                    player[i].corner[1] = 0;
                    break;
                }
            }
            boardMain[player[i].corner[0]][player[i].corner[1]] = player[i].getPlayerColor();
        }

        // koniec konstruktora
    }


    public boolean canPlace(int x, int y, Brick B)
    {
        //czy się mieści
        if(!cFit(x,y,B))return false;
        //czy nie ma nic pod klockiem
        if(!cColide(x,y,B))return false;
        //czy się styka z polami gracza
        if(!cAllies(x,y,B))return false;
        return true;
    }
    private boolean cFit(int x, int y, Brick B)
    {
        if(x < 0 || x >= boardX) return false;
        if(y < 0 || y >= boardY) return false;
        if(x+B.getBrickX()+1 < 0 || x+B.getBrickX()-1 >= boardX) return false;
        if(y+B.getBrickY()+1 < 0 || y+B.getBrickY()-1 >= boardY) return false;
        return true;
    }
    private boolean cColide(int x, int y, Brick B)
    {
        for(int i = 0; i<B.getBrickX(); i++)
        {
            for(int j = 0; j<B.getBrickY(); j++)
            {
                if(this.boardMain[x+i][y+j] != 0)return false;
            }
        }
        return true;
    }

    private boolean cAllies(int x, int y, Brick B)
    {
        for(int i = 0; i<B.getBrickX(); i++)
        {
            for(int j = 0; j<B.getBrickY(); j++)
            {
                for(int k = 0; k<4; k++)
                {
                    switch(k)
                    {
                        case 0:
                        {
                            if(x+i+1 < 0 || x+i+1 >= boardX)break;
                            if(this.boardMain[x+i+1][y+j] == B.getBrickColor())return true;
                            break;
                        }
                        case 1:
                        {
                            if(x+i-1 < 0 || x+i-1 >= boardX)break;
                            if(this.boardMain[x+i-1][y+j] == B.getBrickColor())return true;
                            break;
                        }
                        case 2:
                        {
                            if(y+j+1 < 0 || y+j+1 >= boardY)break;
                            if(this.boardMain[x+i][y+j+1] == B.getBrickColor())return true;
                            break;
                        }
                        case 3:
                        {
                            if(y+j-1 < 0 || y+j-1 >= boardY)break;
                            if(this.boardMain[x+i][y+j-1] == B.getBrickColor())return true;
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void placeBrick(int x, int y, Brick B)
    {
        for(int i = 0; i<B.getBrickX(); i++)
        {
            for(int j = 0; j<B.getBrickY(); j++)
            {
                this.boardMain[x+i][y+j] = B.getBrickColor();
            }
        }
    }

    public int getNumberOfGamers()
    {
        return numberOfGamers;
    }
}
