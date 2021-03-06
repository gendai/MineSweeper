package com.example.english.minesweeper;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class CustomView extends View {

    private Paint black, white;
    private Case[] cases;
    private Random r = new Random();
    private int minesl = 20;
    private boolean touchable = true;
    private boolean Uncmod = true;
    private TextView MarkedMined;
    private int MrkM = 0;

    public void setText(TextView tv){
        this.MarkedMined = tv;
    }

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

    public void Restart() {
        touchable = true;
        this.invalidate();
        cases = null;
        minesl = 20;
        MrkM = 0;
        MarkedMined.setText("Mines Marked : "+MrkM);
        init();

    }

    public void SetMode() {
        Uncmod = !Uncmod;
    }

    private void init() {
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        white = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(0xFF000000);
        white.setColor(0xFFFFFFFF);
        cases = new Case[100];
        int left = 0;
        int top = -60;
        int right = 60;
        int bottom = 0;
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                left = 0;
                top += 60;
                right = 60;
                bottom += 60;
            }
            cases[i] = new Case(i, false, new Rect(left, top, right, bottom));
            left += 60;
            right += 60;
        }
        int id = 0;
        while (minesl != 0) {
            id = RanCase();
            if (!cases[id].getMine()) {
                cases[id].setMine(RanMin());
            }
            //Log.d("debug","DEBUG "+Integer.toString(minesl)+" id "+Integer.toString(id));
        }
        SetNumb(cases);
    }

    public int RanCase(){

        return r.nextInt(100);
    }

    public void SetNumb(Case[] cs) {
        for (int i = 0; i < 100; i++) {
            int res = 0;
            if (i % 10 != 0 && cs[i - 1].getMine()) {
                res++;
            }
            if (i % 10 != 0 && i > 9 && cs[i - 11].getMine()) {
                res++;
            }
            if (((i + 1) % 10 != 0 || i == 0) && cs[i + 1].getMine()) {
                res++;
            }
            if ((i + 1) % 10 != 0 && i > 9 && cs[i - 9].getMine()) {
                res++;
            }
            if (i > 9 && cs[i - 10].getMine()) {
                res++;
            }
            if (i < 90 && cs[i + 10].getMine()) {
                res++;
            }
            if (i < 90 && i % 10 != 0 && cs[i + 9].getMine()) {
                res++;
            }
            if (i < 90 && ((i + 1) % 10 != 0 || i == 0) && cs[i + 11].getMine()) {
                res++;
            }
            cs[i].setNumb(res);
        }
    }

    private boolean RanMin() {
        if (minesl == 0) {
            return false;
        } else {
            if (r.nextBoolean()) {
                minesl--;
                return true;
            } else {
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
        for (int j = 0; j < 9; j++) {
            c.drawLine(startx, starty, stopx, stopy, white);
            startx += 60;
            stopx = startx;
        }
        startx = 0;
        starty = 60;
        stopx = 600;
        stopy = 60;
        for (int j = 0; j < 9; j++) {
            c.drawLine(startx, starty, stopx, stopy, white);
            starty += 60;
            stopy = starty;
        }
        if(MrkM == 20 && AllUnc()){
            Toast.makeText(this.getContext(), "You Win", Toast.LENGTH_LONG).show();
            touchable = false;
        }
    }

    private boolean AllUnc(){
        for(Case c : cases){
            if(!c.getTouched() && !c.getMine()){
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (touchable) {
            float x = e.getX();
            float y = e.getY();
            switch(e.getAction()){
                case MotionEvent.ACTION_UP:
                    for (Case c : cases) {
                        if (this.Uncmod) {
                            if (c.Touched((int) x, (int) y) && !c.getMark()) {
                                if (c.getMine()) {
                                    c.exp();
                                    Lose();
                                } else {
                                    if(c.getNumb() == 0){
                                        RevealAround(c.getId());
                                    }else{
                                        c.Uncover();
                                    }
                                }
                                invalidate();
                            }
                        } else {
                            if (c.Touched((int) x, (int) y)) {
                                if(c.getMark()){
                                    MrkM--;
                                }else{
                                    MrkM++;
                                }
                                MarkedMined.setText("Mines Marked : "+MrkM);
                                c.Mark();
                                invalidate();
                            }
                        }
                    }
                    return true;
            }
        }
        return true;
    }

    private void RevealAround(int i){
        if(!cases[i].getRevealed()){
            cases[i].Uncover();
        }else{
            return;
        }
        if(cases[i].getNumb() == 0) {
            if (i % 10 != 0) {
                RevealAround(cases[i - 1].getId());
            }
            if (i % 10 != 0 && i > 9) {
                    RevealAround(cases[i - 11].getId());
            }
            if (((i + 1) % 10 != 0 || i == 0)) {
                    RevealAround(cases[i + 1].getId());
            }
            if ((i + 1) % 10 != 0 && i > 9) {
                    RevealAround(cases[i - 9].getId());
            }
            if (i > 9) {
                    RevealAround(cases[i - 10].getId());
            }
            if (i < 90) {
                    RevealAround(cases[i + 10].getId());
            }
            if (i < 90 && i % 10 != 0) {
                    RevealAround(cases[i + 9].getId());
            }
            if (i < 90 && ((i + 1) % 10 != 0 || i == 0)) {
                    RevealAround(cases[i + 11].getId());
            }
        }
    }

    private void Lose(){
        //Toast.makeText(this.getContext(), "You Loose", Toast.LENGTH_LONG).show();
        touchable = false;
        RevealAll();
    }

    private void RevealAll(){
            for(Case c : cases){
                if(!c.getRevealed()){
                    if(c.getMine()){
                        c.exp();
                    }
                }
                invalidate();
            }
    }
}

class Case {
    private int id;
    private boolean mine;
    private Rect bounds;
    private int state;
    private int numb = 0;
    private boolean mark;
    private boolean touched = false;
    private boolean revealed = false;

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


    public int getNumb()
    {
        return numb;
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
            touched = true;
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

        }else if(this.state == 3){
            c.drawRect(this.bounds, getPaint());
        }else{
            c.drawRect(this.bounds, getPaint());
        }
    }

    public void Uncover(){
        this.state = 1;
        this.revealed = true;
    }

    public boolean getTouched(){
        return touched;
    }

    public void exp(){
        this.state = 2;
    }

    public void Mark(){
        if(this.state == 3){
            this.state = 0;
            mark = false;
        }else if(this.state == 0){
            mark = true;
            this.state = 3;
        }
    }

    public boolean getMark(){
        return mark;
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
            case 3:
                Paint p4 = new Paint(Paint.ANTI_ALIAS_FLAG);
                p4.setColor(0xFFFFFF00);
                p4.setStyle(Paint.Style.FILL);
                return p4;
        }
        return null;
    }

    public boolean getRevealed(){
        return this.revealed;
    }

    public void setMine(boolean b){
        this.mine = b;
    }

    public void setNumb(int n){
        this.numb = n;
    }
}
