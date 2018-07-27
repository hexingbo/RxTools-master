package com.vondear.rxui.view.popupwindows.tools;

import android.view.View;

/**
 * @author vondear
 */
public class RxCoordinates {

    int left;
    int top;
    int right;
    int bottom;

    public RxCoordinates(View view){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        left = location[0];
        right = left + view.getWidth();
        top = location[1];
        bottom = top + view.getHeight();
    }

}
