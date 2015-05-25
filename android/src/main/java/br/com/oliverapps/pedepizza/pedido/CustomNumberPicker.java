package br.com.oliverapps.pedepizza.pedido;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by William on 5/7/2015.
 */
public class CustomNumberPicker extends NumberPicker {

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setMinValue(0);
        this.setMaxValue(10);
        this.setWrapSelectorWheel(false);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        ((EditText) child).setTextSize(22);
    }
}