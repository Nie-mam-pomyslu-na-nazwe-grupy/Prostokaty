package com.example.draganddrop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Bitmap> rectangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ConstraintLayout layout = findViewById(R.id.layout); //zmienna layout, reprezentuje ona miejsce gdzie wrzucamy rzeczy z kodu na widok
        ImageView imageSquare = findViewById(R.id.imageViewSquare);//zmienna imageSquare, typu ImageView. Reprezentuje ona kwadracik w formie graficznej


        imageSquare.post(new Runnable() {
            @Override
            public void run() {

                ImageView imageRectangle = new ImageView(getApplicationContext()); //tworze nowa zmienna typu ImageView
                imageRectangle.setImageBitmap(createRectangle(4,4)); //ustawiam imageRectangle jako wynik funkcji createRectangle() o wymiarach 2x2 (przykladowe wartosci prostokata)
                layout.addView(imageRectangle);//do layout dodaje zmienna imageRectangle
            }

        });

    }

    //postaram sie stworzyc funkcje, ktora tworzy prostokaty z malych kwadratow i zwraca prostakat jako bitmapa
    private Bitmap createRectangle( int x, int y) {
        //x i y oraz sizeX i sizeY to wymiar prostokata ktory chcemy stworzyc z malych kwadratow
        int sizeX = x;
        int sizeY = y;


        ImageView squareImage = findViewById(R.id.imageViewSquare); //pobieram z activity_main obrazek z id imageViewSquare
        BitmapDrawable drawable = (BitmapDrawable) squareImage.getDrawable(); //pobiera obraz kwadratu do zmiennej typu BitmapDrawable

        //tworzymy dwie zmienne typu bitmapa, jedna "merged", druga "toMerge", kazda z nich zawiera obrazek square, co zostalo ustalone wczesniej
        Bitmap merged = drawable.getBitmap(); //tworzy typ Bitmap z typu BitmapDrawable powyzej
        Bitmap toMerge = merged.copy(merged.getConfig(), true);//kopiuje Bitmape merged do bitmapy toMerge



        //sizeX-1 razy laczymy bitmapy sposobem funkcja ktora trzeba napisac ponizej
        for(int i=0; i<sizeX-1; i++){
            merged = mergeHorizontally( merged, toMerge);
        }

        //po wykonaniu tej petli powinnismy miec rzadek o wymiarze x
        toMerge = merged.copy(merged.getConfig(), true); //kopiujemy ponownie merged do "toMerge"

        //sizeY-1 razy laczymy bitmapy
        for(int i=0; i<sizeY-1; i++){
            merged = mergeVertically(merged, toMerge);
        }

        //otrzymujemy bitmape zlozona z kwadratow o wymiarze x na y ktora mozemy zwrocic w return :)
        return merged;


    }

    //funkcja zaklada, ze obie bitmapy maja te sama wysokosc
    public Bitmap mergeHorizontally( Bitmap a, Bitmap b){
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

    public Bitmap mergeVertically( Bitmap a, Bitmap b){
        Bitmap ab = null;

        int width, height = 0;

        width = a.getWidth() ;
        height = a.getHeight() + b.getHeight();

        ab = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas mergedImages = new Canvas(ab);

        mergedImages.drawBitmap(a, 0f, 0f, null);
        mergedImages.drawBitmap(b, 0f, a.getHeight(), null);

        return ab;
    }




}

