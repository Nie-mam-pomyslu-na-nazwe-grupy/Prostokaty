package com.example.draganddrop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
    public static int topBoard; //field którego użyjemy w touch listener, poczatek planszy
    public static int startBoard; //field ktorego uzyjemy w touch listener, koniec planszy
    public static int BdimX = 20;
    public static int BdimY = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final RelativeLayout layout = findViewById(R.id.layout); //zmienna layout, reprezentuje ona miejsce gdzie wrzucamy rzeczy z kodu na widok
        final ImageView imageSquare = findViewById(R.id.imageViewSquare);//zmienna imageSquare, typu ImageView. Reprezentuje ona kwadracik w formie graficznej


        imageSquare.post(new Runnable() {
            @Override
            public void run() {
                //imageSquare.setVisibility(View.invisible);
               TouchListener touchListener = new TouchListener();

               RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                //params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                //przygotowanie imageBoard

                //pobieranie obrazu z ImageView
                ImageView imageBoard = findViewById(R.id.imageBoard);
                getBitmapPositionInsideImageView(imageBoard);
                sendViewToBack(imageBoard);

                //ustalanie zmiennych ImageBoard
                int[] dimensions = getBitmapPositionInsideImageView(imageBoard);
                int startOfBoard = dimensions[0];
                startBoard = startOfBoard;
                int topOfBoard = dimensions[1];
                topBoard = topOfBoard;
                int gridSize = dimensions[2] / BdimX;//zakładam, że kratki są idealnie kwadratowe


                //przygotowanie pierwszego (jednego, przykładowego) prostokąta:
                Rectangle r = new Rectangle(getApplicationContext());
                int SDimx = 4;
                int SDimy = 4;
                r.setImageBitmap( createRectangle(SDimx, SDimy, gridSize));

                r.dimX = SDimx;
                r.dimY = SDimy;
                r.grid = gridSize;
                //todo: ustawić wymiary prostokąta xCord i yCord i ja zaimplementowac
                //todo: ustawic ladna pozycje startowa prostokata
                rects.add(r);

                //pętla dodająca do widoku kazdy prostokat w tablicy prostokatow
                for(Rectangle piece : rects){
                    piece.setOnTouchListener(touchListener);
                    layout.addView(piece);
                    piece.setLayoutParams(params);

                }

                //todo: stworzyc funkcje round()


            }

        });

    }

    //funkcja, ktora tworzy bitmape prostokata z malych kwadratow i zwraca prostakat jako bitmapa.
    private Bitmap createRectangle(int x, int y, int gridSize) {
        //x i y oraz sizeX i sizeY to wymiar prostokata ktory chcemy stworzyc z malych kwadratow
        int sizeX = x;
        int sizeY = y;


        ImageView squareImage = findViewById(R.id.imageViewSquare); //pobieram z activity_main obrazek z id imageViewSquare
        BitmapDrawable drawable = (BitmapDrawable) squareImage.getDrawable(); //pobiera obraz kwadratu do zmiennej typu BitmapDrawable

        //tworzymy dwie zmienne typu bitmapa, jedna "merged", druga "toMerge", kazda z nich zawiera obrazek square, co zostalo ustalone wczesniej
        Bitmap merged = drawable.getBitmap(); //tworzy typ Bitmap z typu BitmapDrawable powyzej
        Bitmap resized = Bitmap.createScaledBitmap(merged, gridSize, gridSize, true);
        merged = resized;

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
}

