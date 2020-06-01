package com.example.draganddrop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
    ArrayList<Brick> bricks = new ArrayList<Brick>();
    private static final Random RANDOM = new Random();
    private ImageView imageViewDie1, imageViewDie2;
    public static int startBoard; //field ktorego uzyjemy w touch listener, koniec planszy
    public static int topBoard;
    public static int BdimX = 20;
    public static int BdimY = 30;
    private ImageButton rollButton, passButton, turnButton;
    private ImageButton imageButton, gameOverButton;
    private TextView rollText, score1Text, score2Text, score3Text, score4Text, passText1, passText2, passText3, passText4, gameOverText;
    private ImageView popUpView;
    //zmienne ktore chce uzyc w klasie wewnetrznej, wiec musza byc zadeklarowane przed OnCreate
    int turaGracza = 0;
    boolean firstPlaced = false;
    boolean justDeleted = false;
    int[] passes = {0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewDie1 = (ImageView) findViewById(R.id.image_view_die_1);
        imageViewDie2 = (ImageView) findViewById(R.id.image_view_die_2);

        Intent intent = getIntent();
        int NoPlayers= intent.getIntExtra(PlayersNumber.PLAYERS,2);//pobiera liczbę graczy z PlayersNumber.java
        final int loseCondition = 3;

        final Engine engine = new Engine(20, 30, NoPlayers);

        imageButton=(ImageButton) findViewById(R.id.imageButton);//dodaje guzik pauzy

        final RelativeLayout layout = findViewById(R.id.layout); //zmienna layout, reprezentuje ona miejsce gdzie wrzucamy rzeczy z kodu na widok
        final ImageView imageSquare = findViewById(R.id.imageViewSquare);//zmienna imageSquare, typu ImageView. Reprezentuje ona kwadracik w formie graficznej
        imageSquare.setImageResource(R.drawable.square1);

        //pozwala przejść do innej karty
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });

        imageSquare.post(new Runnable() {
            @Override
            public void run() {
               final TouchListener touchListener = new TouchListener();

                //przygotowanie imageBoard
                //pobieranie obrazu z ImageView
                ImageView imageBoard = findViewById(R.id.imageBoard);
                getBitmapPositionInsideImageView(imageBoard);
                sendViewToBack(imageBoard);

                //ustalanie zmiennych ImageBoard
                int[] dimensions = getBitmapPositionInsideImageView(imageBoard);
                startBoard =imageBoard.getLeft();;
                topBoard = imageBoard.getTop();
                final int gridSize =( dimensions[2] / BdimX) +1;//zakładam, że kratki są idealnie kwadratowe

                //Ustawianie pojedynczego klocka w rogu, w zaleznosci od ilosci graczy:
                Rectangle[] rectsStart;
                rectsStart = new Rectangle[engine.getNumberOfGamers()];

                for(int i = 0; i<engine.getNumberOfGamers(); i++)
                    rectsStart[i] = new Rectangle((getApplicationContext()));

                for(int i=0; i<engine.getNumberOfGamers(); i++)
                {

                    rectsStart[i] = new Rectangle(getApplicationContext());
                    rectsStart[i].setImageBitmap(createRectangle(1, 1, gridSize, i));

                    RelativeLayout.LayoutParams params;
                    layout.addView(rectsStart[i]);
                    params = (RelativeLayout.LayoutParams) rectsStart[i].getLayoutParams();

                    switch(i){
                        case 0:
                            params.leftMargin = startBoard +4;
                            params.topMargin = topBoard;
                            break;
                        case 1:
                            params.leftMargin = startBoard + ( (BdimX - 1) * gridSize ) ;
                            params.topMargin = topBoard+ ( (BdimY - 1) * gridSize );
                            break;
                        case 2:
                            params.leftMargin = startBoard +2;
                            params.topMargin = topBoard+ ( (BdimY - 1) * gridSize ) -4;
                            break;
                        case 3:
                            params.leftMargin = startBoard + ( (BdimX - 1) * gridSize ) ;
                            params.topMargin = topBoard;
                            break;

                    }

                    rectsStart[i].setLayoutParams(params);
                }

                //ustawienie TextView z punktami graczy

                score1Text = (TextView) findViewById(R.id.score1);
                score2Text = (TextView) findViewById(R.id.score2);

                if(engine.getNumberOfGamers() > 2 )
                {
                    switch(engine.getNumberOfGamers()){
                        case 3:
                            score3Text = (TextView) findViewById(R.id.score3);
                            score3Text.setText("G3 punkty: 0");
                            break;
                        case 4:
                            score3Text = (TextView) findViewById(R.id.score3);
                            score3Text.setText("G3 punkty: 0");
                            score4Text = (TextView) findViewById(R.id.score4);
                            score4Text.setText("G4 punkty: 0");
                            break;
                    }
                }

                //Ustawienie TextView z pasami graczy

                passText1 = (TextView) findViewById(R.id.passText1);
                passText2 = (TextView) findViewById(R.id.passText2);
                if(engine.getNumberOfGamers() > 2 )
                {
                    switch(engine.getNumberOfGamers()){
                        case 3:
                            passText3 = (TextView) findViewById(R.id.passText3);
                            passText3.setText("G3 pasy: 0/3");
                            break;
                        case 4:
                            passText3 = (TextView) findViewById(R.id.passText3);
                            passText3.setText("G3 pasy: 0/3");
                            passText4 = (TextView) findViewById(R.id.passText4);
                            passText4.setText("G4 pasy: 0/3");
                            break;
                    }
                }

                //runda gry:
                rollText = (TextView) findViewById(R.id.rollText);
                rollButton = (ImageButton) findViewById(R.id.rollButton);
                gameOverText = (TextView) findViewById(R.id.gameOverText);

                rollButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if sprawdza czy poprzedni klocek zostal polozony na plansze
                        if(  rects.size() == 0 || justDeleted || !rects.get(rects.size()-1).canMove ) {
                            justDeleted = false;
                            rollText.setText((" "));

                            RelativeLayout.LayoutParams params;

                            Rectangle r = new Rectangle(getApplicationContext());
                            //losowanie wymiaru prostokąta
                            int SDimx = roll();
                            int SDimy = roll();

                            //na podstawie wylosowanej liczby ustawia obraz kostki
                            int res1 = getResources().getIdentifier("dice" + SDimx, "drawable", "com.example.draganddrop");
                            int res2 = getResources().getIdentifier("dice" + SDimy, "drawable", "com.example.draganddrop");

                            imageViewDie1.setImageResource(res1);
                            imageViewDie2.setImageResource(res2);

                            //ustawia pola prostokata na jego dane
                            r.dimX = SDimx;
                            r.dimY = SDimy;
                            r.grid = gridSize;

                            //dodawanie nowego prostokata do tablicy prostokatkow
                            Brick b = new Brick(SDimx, SDimy, engine.player[turaGracza]);
                            bricks.add(b);
                            r.setImageBitmap(createRectangle(SDimx, SDimy, gridSize, turaGracza));
                            rects.add(r);

                            touchListener.takeEngine(engine, turaGracza);//umozliwia operowanie na engine w TouchListenerze
                            r.setOnTouchListener(touchListener);

                            //ustawienie pozycji nowostworzonego klocka
                            layout.addView(r);
                            params = (RelativeLayout.LayoutParams) r.getLayoutParams();

                            params.leftMargin = imageViewDie2.getRight() + 30;
                            params.topMargin = rollText.getBottom() + 1;

                            r.setLayoutParams(params);

                            //sprawdzanie czy ktos wygral gre

                            if(!checkWinner(engine, passes, loseCondition))
                            {
                                turaGracza++;
                                if(turaGracza >= engine.getNumberOfGamers()) {
                                    turaGracza = 0;
                                }
                                while( passes[turaGracza] >= loseCondition)
                                {
                                    turaGracza++;
                                    if(turaGracza >= engine.getNumberOfGamers()) {
                                        turaGracza = 0;
                                    }
                                }
                            }

                            if( turaGracza > 0 ) firstPlaced = true;//flaga dajaca znac ze pierwszy klocek zostal postawiony

                            //ustawienie punktacji graczy w TextView

                            score1Text.setText("G1 punkty: " + engine.player[0].getScore());
                            score2Text.setText("G2 punkty: " + engine.player[1].getScore());

                            if(engine.getNumberOfGamers() > 2)
                            score3Text.setText("G3 punkty: " + engine.player[2].getScore());

                            if(engine.getNumberOfGamers() > 3)
                            score4Text.setText("G4 punkty: " + engine.player[3].getScore());

                        }//if( rects.get(rects.size()).canMove == false )
                        //przy probubie wygenerowania klocka jesli jest aktywny klocek, wyswietla sie komunikat
                        else {rollText.setText(("Umieść istniejący prostokąt."));}
                    }//OnClick rollButton
                });//OnClickListener rollButton

                //Rezygnowanie z klocka/warunek przegranej
                passButton = (ImageButton) findViewById(R.id.passButton);
                passButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(firstPlaced && !justDeleted && rects.size() > 0 && rects.get(rects.size()-1).canMove )//sprawdzanie czy jest klocek z ktorego mozna zrezygnowac
                        {
                            //faktyczne usuwanie klocka z widoku
                            int x = rects.size()-1;
                            layout.removeView( rects.get(x) ) ;
                            rects.remove( x );

                            justDeleted = true;//flaga dajaca znac ze usunelismy przed chwila klocek

                            //manipulowanie tablica ktora informuje ktory gracz ile razy spasowal
                            int nGracz = activePlayer(turaGracza, passes, engine, loseCondition);
                            passes[nGracz]++;

                            //ustawianie tekstu informujacego o pasach
                            switch(nGracz)
                            {
                                case 0:
                                    passText1.setText("G1 pasy: " + passes[nGracz] +"/3");
                                    if(passes[nGracz] >= loseCondition ) passText1.setTextColor(Color.RED);
                                    break;
                                case 1:
                                    passText2.setText("G2 pasy: " + passes[nGracz] +"/3");
                                    if(passes[nGracz] >= loseCondition ) passText2.setTextColor(Color.RED);
                                    break;
                                case 2:
                                    passText3.setText("G3 pasy: " + passes[nGracz] +"/3");
                                    if(passes[nGracz] >= loseCondition ) passText3.setTextColor(Color.RED);
                                    break;
                                case 3:
                                    passText4.setText("G4 pasy: " + passes[nGracz] +"/3");
                                    if(passes[nGracz] >= loseCondition ) passText4.setTextColor(Color.RED);
                                    break;
                            }

                        }
                        else rollText.setText("Brak prostokąta do odrzucenia");

                    }//OnClick passButton

                    });//OnClickListener passButton

                //obracanie klocka
                turnButton = (ImageButton) findViewById(R.id.turnButton);
                turnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(firstPlaced && !justDeleted && rects.size() > 0 && rects.get(rects.size()-1).canMove )
                        {
                            bricks.get(bricks.size()-1).rollBrick();

                            int x = rects.size()-1;
                            RelativeLayout.LayoutParams parameters;
                            parameters = (RelativeLayout.LayoutParams) rects.get(x).getLayoutParams();
                            layout.removeView( rects.get(x) ) ;
                            rects.remove( x );

                            Rectangle r = new Rectangle(getApplicationContext());
                            r.dimX = bricks.get(bricks.size()-1).getBrickX();
                            r.dimY = bricks.get(bricks.size()-1).getBrickY();
                            r.grid = gridSize;
                            r.setImageBitmap(createRectangle(bricks.get(bricks.size()-1).getBrickX(), bricks.get(bricks.size()-1).getBrickY(), gridSize, bricks.get(bricks.size()-1).getBrickColor()-1));
                            r.setLayoutParams(parameters);
                            rects.add(r);

                            int gracz = activePlayer(turaGracza, passes, engine, loseCondition);

                            touchListener.takeEngine(engine, gracz);//umozliwia operowanie na engine w TouchListenerze
                            r.setOnTouchListener(touchListener);
                            layout.addView(r);
                        }
                        else rollText.setText("Brak prostokąta do obrócenia");

                    }//onclick
                });//onclicklistener

            }//public void run

        });//imagesquare post


    }//void oncreate


    private int roll()
    {//losowanie 1 kosci
        return RANDOM.nextInt(6) + 1;
    }

    //funkcja uzywana do obliczenia aktywnego gracza dla guzików turn i pass
    private int activePlayer( int turaGracza, int []passes, Engine engine, int loseCondition)
    {
        int nGracz;
        nGracz = turaGracza-1;
        if(turaGracza - 1 < 0 ) nGracz = engine.getNumberOfGamers() -1 ;

        while(passes[nGracz] == loseCondition)
        {
            nGracz--;
            if(nGracz < 0)
            {
                nGracz = engine.getNumberOfGamers()-1;
            }
        }

        return nGracz;
    }

    //funkcja, ktora tworzy bitmape prostokata z malych kwadratow i zwraca prostakat jako bitmapa.
    private Bitmap createRectangle(int x, int y, int gridSize, int gracz) {
        //x i y oraz sizeX i sizeY to wymiar prostokata ktory chcemy stworzyc z malych kwadratow
        int sizeX = x;
        int sizeY = y;

        gracz+=1;

        //ustawia odpowiedni kolor kwadrata w zaleznosci od aktywnego gracza
        ImageView squareImage = findViewById(R.id.imageViewSquare);
        int res = getResources().getIdentifier("square"+gracz, "drawable", "com.example.draganddrop");
        squareImage.setImageResource(res);

         //pobieram z activity_main obrazek z id imageViewSquare
        BitmapDrawable drawable = (BitmapDrawable) squareImage.getDrawable(); //pobiera obraz kwadratu do zmiennej typu BitmapDrawable


        //tworzymy dwie zmienne typu bitmapa, jedna "merged", druga "toMerge", kazda z nich zawiera obrazek square, co zostalo ustalone wczesniej
        Bitmap merged = drawable.getBitmap(); //tworzy typ Bitmap z typu BitmapDrawable powyzej
        Bitmap resized = Bitmap.createScaledBitmap(merged, gridSize, gridSize, true);
        merged = resized;

        if( sizeX == 1 && sizeY == 1)
        {
            return Bitmap.createScaledBitmap(merged, 17, 17 , true);
        }

        Bitmap toMerge = merged.copy(merged.getConfig(), true);//kopiuje Bitmape merged do bitmapy toMerge


        //sizeX-1 razy laczymy bitmapy sposobem funkcja ktora trzeba napisac ponizej
        for (int i = 0; i < sizeX - 1; i++) {
            merged = mergeHorizontally(merged, toMerge);
        }

        //po wykonaniu tej petli powinnismy miec rzadek o wymiarze x
        toMerge = merged.copy(merged.getConfig(), true); //kopiujemy ponownie merged do "toMerge"

        //sizeY-1 razy laczymy bitmapy
        for (int i = 0; i < sizeY - 1; i++) {
            merged = mergeVertically(merged, toMerge);
        }

        //otrzymujemy bitmape zlozona z kwadratow o wymiarze x na y ktora mozemy zwrocic w return :)
        return merged;


    }

    private boolean checkWinner(Engine engine, int[]passes, int loseCondition)
    {
        boolean winner = false;
        int counter = 0;
        int whoWon = 10;

        for(int i=0; i< engine.getNumberOfGamers(); i++)
        {
            if(passes[i] >= loseCondition)
            {
                counter++;
            }

            if(counter >= engine.getNumberOfGamers() - 1)
            {
                winner = true;
            }
        }

        if (winner)
        {
            //obliczanie zwyciezcy
            for(int i=0; i<engine.getNumberOfGamers(); i++)
            {
                if(passes[i] < loseCondition)
                {
                    whoWon = i+1;
                }
            }


            int mostPoints=0;
            for(int i=1;  i<engine.getNumberOfGamers(); i++)
            {
                if(engine.player[i].getScore() > engine.player[mostPoints].getScore())
                    mostPoints = i;
            }
            mostPoints++;


            popUpView = (ImageView) findViewById(R.id.popUpImage);
            gameOverButton = (ImageButton) findViewById(R.id.gameOverButton);
            popUpView.setVisibility(View.VISIBLE);
            popUpView.bringToFront();
            gameOverButton.bringToFront();


            gameOverText.setText("Koniec Gry! \nGracz " + whoWon + " jest ostatnim ocalałym \nGracz "+ mostPoints + " ma najwięcej punktów");
            gameOverText.bringToFront();
            gameOverButton.setVisibility(View.VISIBLE);
            gameOverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMenu();
                }
            });

        }



        if(winner) return true;
        else return false;
    }

    //funkcja zaklada, ze obie bitmapy maja te sama wysokosc
    public Bitmap mergeHorizontally(Bitmap a, Bitmap b) {
        Bitmap ab = null;

        int width, height = 0;

        width = a.getWidth() + b.getWidth();
        height = a.getHeight();

        ab = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas mergedImages = new Canvas(ab);

        mergedImages.drawBitmap(a, 0f, 0f, null);
        mergedImages.drawBitmap(b, a.getWidth(), 0f, null);

        return ab;
    }

    public Bitmap mergeVertically(Bitmap a, Bitmap b) {
        Bitmap ab = null;

        int width, height = 0;

        width = a.getWidth();
        //wysokosc wynikowej bitmapy = suma wysokosci dwoch sklejanych bitmap
        height = a.getHeight() + b.getHeight();


        ab = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas mergedImages = new Canvas(ab);

        mergedImages.drawBitmap(a, 0f, 0f, null);
        mergedImages.drawBitmap(b, 0f, a.getHeight(), null);

        return ab;
    }

    //funkcja wczytujaca wymiary i pozycje obrazy ImageView
    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    //funkcja ustawiająca ImageView na sam dół
    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    public void openMenu() { //przechodzi do menu
        Intent intent = new Intent(this, StartMenu.class);
        startActivity(intent);
    }
}

