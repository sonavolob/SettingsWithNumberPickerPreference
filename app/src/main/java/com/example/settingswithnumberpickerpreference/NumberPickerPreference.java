package com.example.settingswithnumberpickerpreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.example.settingswithnumberpickerpreference.R;

public class NumberPickerPreference extends DialogPreference {

    // allowed range
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;
    private int min_value = MIN_VALUE;
    private int max_value = MAX_VALUE;

    // enable or disable the 'circular behavior'
    public static final boolean WRAP_SELECTOR_WHEEL = true;

    private NumberPicker picker;
    private int value;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadStateFromAttrs(attrs);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadStateFromAttrs(attrs);
    }

    /**
     * Used for changing settings through XML
     * @param attributeSet AttributeSet
     */
    public void loadStateFromAttrs(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray a = null;
        try{
            a = getContext().obtainStyledAttributes(attributeSet, R.styleable
                    .NumberPickerPreference);
            min_value = a.getInt(R.styleable.NumberPickerPreference_charInputMinimum,
                    MIN_VALUE);
            max_value = a.getInt(R.styleable.NumberPickerPreference_charInputMaximum,
                    MAX_VALUE);
        }finally {
            if (a != null) {
                a.recycle(); // ensure this is always called
            }
        }
    }

    @Override
    protected View onCreateDialogView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        picker = new NumberPicker(getContext());
        picker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(picker);

        return dialogView;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setMinValue(min_value);
        picker.setMaxValue(max_value);
        picker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setValue(getValue());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int newValue = picker.getValue();
            if (callChangeListener(newValue)) {
                setValue(newValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, min_value);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(min_value) : (Integer) defaultValue);
    }

    public void setValue(int value) {
        this.value = value;
        persistInt(this.value);
    }

    public int getValue() {
        return this.value;
    }
}