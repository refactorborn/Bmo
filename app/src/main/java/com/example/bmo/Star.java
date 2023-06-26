package com.example.bmo;

import android.content.Context;
import  android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Star {

    int starFrame =0;
    int starX , starY, starlocity;
    Random random;

    Bitmap star[]=new Bitmap[4];
    public Star(Context context){
        star[0] = BitmapFactory.decodeResource(context.getResources(),R.drawable.star1);
        star[1] = BitmapFactory.decodeResource(context.getResources(),R.drawable.star2);
        star[2] = BitmapFactory.decodeResource(context.getResources(),R.drawable.star3);
        star[3] = BitmapFactory.decodeResource(context.getResources(),R.drawable.star4);
        random = new Random();
        resetPosition();
    }

    // Hàm lấy kích thước vật thể
    public Bitmap getStar(int starFrame) {return star[starFrame];} // trả về bitmap star[0]
    public int getStarWidth(){
        return star[0].getWidth();
    } // trả về độ rộng của hình ảnh
    public int getStarHeight(){return star[0].getHeight();} // trả về độ cao của hình ảnh

    // hàm xác định tòa độ rơi ngẫu nhiên của vật thể
    // theo trục Ox/oy
    public void resetPosition() {
        starX = random.nextInt(GameView.dWidth - getStarWidth());
        starY = random.nextInt(GameView.dHeight)* -1;
        starlocity = 30 + random.nextInt(16);  // tăng vị trí rơi
    }
}
