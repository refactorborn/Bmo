package com.example.bmo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Explosion {
    Bitmap expolosion[] = new Bitmap[4];
    int explosionFrame = 0;
    int explosionX, explositonY;

    public Explosion(Context context){
        expolosion[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.ex0);
        expolosion[1]= BitmapFactory.decodeResource(context.getResources(),R.drawable.ex1);
        expolosion[2]= BitmapFactory.decodeResource(context.getResources(),R.drawable.ex2);
        expolosion[3]= BitmapFactory.decodeResource(context.getResources(),R.drawable.ex3);
    }

    public Bitmap getExplosion(int explosionFrame){
        return expolosion[explosionFrame];
    }
}


