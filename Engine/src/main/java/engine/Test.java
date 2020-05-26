
package engine;

public class Test {
    //static void show();

    public static void main(String[] args) {
        
        
        /// /// /// Początek gry: /// /// ///
        
        int iloscGraczy = 4;    // 2/4
        int x = 10;
        int y = 10;
        
        Engine E = new Engine(x, y, iloscGraczy);
        
        
        
        //System.out.println("test");
        
        
        show(x, y, E);
        
        /// /// /// Kolejka: /// /// ///
        
        
        //deklaracja klocka
        Brick B = new Brick(2, 3, E.player[0]);
        
        /*if(E.canPlace(12, 4, B))System.out.println("sprawdzanie1 pozytywne");
        else System.out.println("sprawdzanie1 negatywne");
        
        if(E.canPlace(9, 11, B))System.out.println("sprawdzanie2 pozytywne");
        else System.out.println("sprawdzanie2 negatywne");
        
        if(E.canPlace(9, 4, B))System.out.println("sprawdzanie3 pozytywne");
        else System.out.println("sprawdzanie3 negatywne");
        
        if(E.canPlace(2, 0, B))System.out.println("sprawdzanie4 pozytywne");
        else System.out.println("sprawdzanie4 negatywne");
        
        // kolizja
        if(E.canPlace(0, 0, B))System.out.println("sprawdzanieK pozytywne");
        else System.out.println("sprawdzanieK negatywne");
        if(E.canPlace(8, 7, B))System.out.println("sprawdzanieK pozytywne");
        else System.out.println("sprawdzanieK negatywne");
        
        // sojusznicy
        if(E.canPlace(1, 1, B))System.out.println("sprawdzanieS pozytywne");
        else System.out.println("sprawdzanieS negatywne");*/
        if(E.canPlace(0, 1, B))System.out.println("sprawdzanieS pozytywne");
        else System.out.println("sprawdzanieS negatywne");
        if(E.canPlace(1, 0, B))System.out.println("sprawdzanieS pozytywne");
        else System.out.println("sprawdzanieS negatywne");
        
        if(E.canPlace(1, 0, B))
        {
            E.placeBrick(1, 0, B);
        }else
        {
            System.out.println("nie można umieścić");
        }
        
        show(x, y, E);
        
        Brick B2 = new Brick(4, 2, E.player[1]);
        
        if(E.canPlace(9, 8, B2))
        {
            E.placeBrick(9, 8, B2);
        }else
        {
            System.out.println("nie można umieścić");
        }
        
        
        show(x, y, E);
        
        
    }
    
    
    
    
    
    
    
    
    static void show(int x, int y, Engine E)
    {
        //System.out.println("test");
        for(int i = 0; i<y; i++)
        {
            for(int j = 0; j<x; j++)
            {
                System.out.print(" "+ E.boardMain[j][i] +" ");
            }
            System.out.println();
        }
    }
    
}


