package com.example.english.minesweeper;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class CustomView extends View{

    private Paint black, white;
    private Case[] cases;
    private Random r = new Random();
    private int minesl = 20;

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
            cases[i] = new Case(i,RanMin(),new Rect(left,top,right,bottom));
            left += 60;
            right += 60;
        }
        int id = 0;
        while(minesl != 0){
            if(id == 99){
                id = 0;
            }
            if(!cases[id].getMine()){
                cases[id].setMine(RanMin());
            }
            id++;
        }
        SetNumb(cases);
    }

    public void SetNumb(Case[] cs){
        for(int i = 0; i < 100; i++){
            int res = 0;
            if(i %10 != 0 && cs[i-1].getMine()){
                res++;
            }
            if(i %10 !=0 && i > 9 && cs[i- 11].getMine()){
                res++;
            }
            if(((i+1) % 10 != 0 || i == 0) && cs[i+1].getMine()){
                res++;
            }
            if((i+1) % 10 !=0 && i > 9 &&cs[i-9].getMine()){
                res++;
            }
            if(i > 9 && cs[i - 10].getMine()){
                res++;
            }
            if(i < 90 && cs[i + 10].getMine()){
                res++;
            }
            if(i < 90 && i % 10 != 0 && cs[i + 9].getMine()){
                res++;
            }
            if(i < 90 && ((i+1) % 10 !=0 || i == 0) && cs[i + 11].getMine()){
                res++;
            }
            cs[i].setNumb(res);
        }
    }

    private boolean RanMin(){
        if(minesl == 0){
            return false;
        }else{
            if(r.nextBoolean()){
                minesl--;
                return true;
            }else{
                return false;
            }
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
                    c.exp();
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
    private int numb = 0;

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
        if(this.state == 2){
            c.drawRect(this.bounds, getPaint());
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(0xFF000000);
            p.setStyle(Paint.Style.FILL);
            p.setTextSize(p.getTextSize() * 2);
            c.drawText("M",this.bounds.centerX()-12,this.bounds.centerY()+10,p);
        }else if(this.state == 1){
            c.drawRect(this.bounds, getPaint());
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setStyle(Paint.Style.FILL);
            switch(numb){
                case 0:
                    break;
                case 1:
                    p.setColor(0xFF0000FF);
                    p.setTextSize(p.getTextSize() * 2);
                    c.drawText(Integer.toString(this.numb), this.bounds.centerX()-12, this.bounds.centerY()+10, p);
                    break;
                case 2:
                    p.setColor(0xFF00FF00);
                    p.setTextSize(p.getTextSize() * 2);
                    c.drawText(Integer.toString(this.numb), this.bounds.centerX()-12, this.bounds.centerY()+10, p);
                    break;
                case 3:
                    p.setColor(0xFFFFFF00);
                    p.setTextSize(p.getTextSize() * 2);
                    c.drawText(Integer.toString(this.numb), this.bounds.centerX()-12, this.bounds.centerY()+10, p);
                    break;
                default:
                    p.setColor(0xFFFF0000);
                    p.setTextSize(p.getTextSize() * 2);
                    c.drawText(Integer.toString(this.numb), this.bounds.centerX()-12, this.bounds.centerY()+10, p);
                    break;
            }

        }else{
            c.drawRect(this.bounds, getPaint());
        }
    }

    public void Uncover(){
        this.state = 1;
    }

    public void exp(){
        this.state = 2;
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
            case 2:
                Paint p3 = new Paint(Paint.ANTI_ALIAS_FLAG);
                p3.setColor(0xFFFF0000);
                p3.setStyle(Paint.Style.FILL);
                return p3;
        }
        return null;
    }

    public void setMine(boolean b){
        this.mine = b;
    }

    public void setNumb(int n){
        this.numb = n;
    }
}
