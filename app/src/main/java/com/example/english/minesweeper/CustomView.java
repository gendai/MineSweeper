package com.example.english.minesweeper;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View{

    private Paint black, white;
    private Case[] cases;

    public CustomView(Context c) {
        super(c);
        init();
    }

    public CustomView(Context c, AttributeSet attributeSet) {
        super(c, attributeSet);
        init();
    }

    public CustomView(Context c, AttributeSet attributeSet, int style) {
        super(c, attributeSet, style);
        init();
    }


    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int widthM = getMeasuredWidth();
        int heightM = getMeasuredHeight();
        if (widthM > heightM) {
            setMeasuredDimension(heightM, heightM);
        } else {
            setMeasuredDimension(widthM, widthM);
        }
    }

    private void init()
    {
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        white = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(0xFF000000);
        white.setColor(0xFFFFFFFF);
        cases = new Case[100];
        int left = 0;
        int top = -60;
        int right = 60;
        int bottom = 0;
        for (int i = 0; i < 100; i++){
            if(i % 10 == 0){
                left = 0;
                top += 60;
                right = 60;
                bottom += 60;
            }
            cases[i] = new Case(i,false,new Rect(left,top,right,bottom));
            left += 60;
            right += 60;
        }
    }

    @Override
    public void onDraw(Canvas c) {
        for (Case cs : cases) {
            cs.Draw(c);
        }
        int startx = 60;
        int starty = 0;
        int stopx = 60;
        int stopy = 600;
        for(int j = 0; j < 9; j++){
            c.drawLine(startx,starty,stopx,stopy,white);
            startx += 60;
            stopx = startx;
        }
        startx = 0;
        starty = 60;
        stopx = 600;
        stopy = 60;
        for (int j = 0; j < 9; j++){
            c.drawLine(startx,starty,stopx,stopy,white);
            starty += 60;
            stopy = starty;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        for (Case c : cases) {
            if (c.Touched((int) x, (int) y)) {
                if (c.getMine()) {
                    Lose();
                } else {
                    c.Uncover();
                }
                invalidate();
            }
        }
        return true;
    }




    private void Lose(){

    }
}

class Case {
    private int id;
    private boolean mine;
    private Rect bounds;
    private int state;

    public Case(int id, boolean m, Rect bd){
        this.id = id;
        this.mine = m;
        this.bounds = bd;
        this.state = 0;
    }

    public int getId(){
        return this.id;
    }

    public boolean getMine(){
        return this.mine;
    }


    public boolean Touched(int x, int y) {
        return this.bounds.contains(x,y);
    }

    public void Draw(Canvas c){
        c.drawRect(this.bounds, getPaint());
    }

    public void Uncover(){
        this.state = 1;
    }

    public Paint getPaint()
    {
        switch(this.state){
            case 0:
                Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setColor(0xFF000000);
                p.setStyle(Paint.Style.FILL);
                return p;
            case 1:
                Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
                p2.setColor(0xFF808080);
                p2.setStyle(Paint.Style.FILL);
                return p2;
        }
        return null;
    }
}
