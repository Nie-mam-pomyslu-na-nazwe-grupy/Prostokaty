package com.example.draganddrop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
    private Button rollButton;
    private ImageButton imageButton;
    private TextView rollText;
    

    int turaGracza = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewDie1 = (ImageView) findViewById(R.id.image_view_die_1);
        imageViewDie2 = (ImageView) findViewById(R.id.image_view_die_2);

        //todo
        //spytac sie o ilosc graczy przed gra
        final Engine engine = new Engine(20, 30, 2);

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


                //runda gry:
                rollText = (TextView) findViewById(R.id.rollText);
                rollButton = (Button) findViewById(R.id.rollBbutton);
                rollButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if sprawdza czy poprzedni klocek zostal polozony na plansze
                        if(  rects.size() == 0 || rects.get(rects.size()-1).canMove == false) {
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

                            Brick b = new Brick(SDimx, SDimy, engine.player[turaGracza-1]);
                            bricks.add(b);
                            r.setImageBitmap(createRectangle(SDimx, SDimy, gridSize, turaGracza));

                            rects.add(r);//dodawanie prostokata do tablicy prostokatow

                            touchListener.takeEngine(engine);

                            r.setOnTouchListener(touchListener);
                            layout.addView(r);
                            params = (RelativeLayout.LayoutParams) r.getLayoutParams();

                            params.leftMargin = imageViewDie2.getRight() + 30;
                            params.topMargin = rollText.getBottom() + 1;

                            r.setLayoutParams(params);

                            turaGracza++;
                            if(turaGracza > engine.getNumberOfGamers())
                            {
                                turaGracza = 1;
                            }
                        }//if( rects.get(rects.size()).canMove == false )
                        //przy probubie wygenerowania klocka jesli jest aktywny klocek, wyswietla sie komunikat
                        else {rollText.setText(("There already is rectangle to be placed."));}
                    }//OnClick
                });//OnClickListener

            }//public void run

        });//imagesquare post

    }//void oncreate


    private int roll()
    {//losowanie 1 kosci
        return RANDOM.nextInt(6) + 1;
    }

    //funkcja, ktora tworzy bitmape prostokata z malych kwadratow i zwraca prostakat jako bitmapa.
    private Bitmap createRectangle(int x, int y, int gridSize, int gracz) {
        //x i y oraz sizeX i sizeY to wymiar prostokata ktory chcemy stworzyc z malych kwadratow
        int sizeX = x;
        int sizeY = y;

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
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}

