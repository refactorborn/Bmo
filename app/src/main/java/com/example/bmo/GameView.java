package com.example.bmo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;
import android.view.WindowManager;

import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    Bitmap background , ground , jake1;
    Rect rectBackground , rectGround;
    Context context;
    Handler handler;
    final long UPFATE_MILLIS = 20;   // trả về milis (mili giây)
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static  int dWidth, dHeight;
    Random random;
    float jakeX, jakeY;
    float oldX;
    float oldjakeX;
    ArrayList<Star> stars;
    ArrayList<Explosion> exceptions;

    public GameView(Context context) {
        super(context);

        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background3);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        jake1 = BitmapFactory.decodeResource(getResources(), R.drawable.bmo12);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background3);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        jake1 = BitmapFactory.decodeResource(getResources(), R.drawable.bmo12);
        textPaint.setColor(Color.rgb(255,165,0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context,R.font.kenny_blocks));
        healthPaint.setColor(Color.GREEN);
        random = new Random();
        jakeX = dWidth/2 - jake1.getWidth()/2;
        jakeY = dHeight - ground.getHeight() - jake1.getHeight();
        stars = new ArrayList<>();
        exceptions = new ArrayList<Explosion>();
        for (int i=0; i<4 ; i++){
            Star star = new Star(context);
            stars.add(star);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // vẽ giao diện hiển thị trò chơi
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground,null);
        canvas.drawBitmap(ground,null,rectGround,null);
        canvas.drawBitmap(jake1,jakeX,jakeY,null);
        for (int i=0; i<stars.size(); i++)
        {
            canvas.drawBitmap( stars.get(i).getStar( stars.get(i).starFrame ) ,
                    stars.get(i).starX,
                    stars.get(i).starY,null);
            stars.get(i).starFrame++;

            // quay từ star[0]-star[4] thì lại quay về star[0] cứ lặp lại như thế
            if(stars.get(i).starFrame >3 ){
                stars.get(i).starFrame = 0;
            }
            // starlocity = 30 + random.nextInt(16);
            // cứ mỗi lần rơi và quay thì tọa độ Y của vật thể tăng lên
            stars.get(i).starY += stars.get(i).starlocity;

            // xử lí khi vật thể ko trúng BMO
            if(stars.get(i).starY + stars.get(i).getStarHeight() >= dHeight - ground.getHeight()){
                points += 10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = stars.get(i).starX;
                explosion.explositonY = stars.get(i).starY;
                exceptions.add(explosion);
                stars.get(i).resetPosition();
            }
        }

        // hàm xử lí khi vật thể trúng BMO
        for( int i=0;i<stars.size();i++){
            if(stars.get(i).starX + stars.get(i).getStarWidth() >= jakeX
            && stars.get(i).starX <= jakeX + jake1.getWidth()
            && stars.get(i).starY + stars.get(i).getStarWidth() >= jakeY
            && stars.get(i).starY + stars.get(i).getStarWidth() <= jakeY + jake1.getHeight()){
                life --;
                stars.get(i).resetPosition();
                if(life == 0){
                    Intent intent = new Intent(context,GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity( intent );
                    ((Activity) context).finish();
                }
            }
        }
        for (int i=0 ; i < exceptions.size(); i++){
            canvas.drawBitmap(exceptions.get(i).getExplosion(exceptions.get(i).explosionFrame),
                    exceptions.get(i).explosionX,
                    exceptions.get(i).explositonY,null);
            exceptions.get(i).explosionFrame++;
            if(exceptions.get(i).explosionFrame > 3){
                exceptions.remove(i);
            }
        }
        // check life
        // Life = 2 thành máu sẽ chuyển sang màu vàng
        if(life == 2){
            healthPaint.setColor(Color.YELLOW);
        }
        // Life = 1 thành máu sẽ chuyển sang màu vàng
        else if(life == 1){
            healthPaint.setColor(Color.RED);
        }
        canvas.drawRect(dWidth-200, 30, dWidth-200+60*life,80,healthPaint);
        canvas.drawText("" +points, 20 , TEXT_SIZE, textPaint);
        handler.postDelayed(runnable,UPFATE_MILLIS);
    }

    // chạm và di chuyển BMO sang hai bên
    @Override
    public  boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= jakeY){
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oldjakeX = jakeX;
            }
            if (action == MotionEvent.ACTION_MOVE){
                float shift = oldX - touchX;
                float newjakeX = oldjakeX - shift;
                if( newjakeX <=0)
                    jakeX =0;
                else if (newjakeX >= dWidth - jake1.getWidth())
                    jakeX = dWidth - jake1.getWidth();
                else
                    jakeX = newjakeX ;
            }
        }
        return true;
    }
}
