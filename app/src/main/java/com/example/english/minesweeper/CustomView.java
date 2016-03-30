package com.example.english.minesweeper;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View{

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

    }
}
