package com.vondear.rxui.view.popupwindows.tools;

import java.util.Locale;

/**
 * @author Vondear
 */
class RxPopupViewTool {

    static boolean isRtl() {
        Locale defLocal = Locale.getDefault();
        return Character.getDirectionality(defLocal.getDisplayName(defLocal).charAt(0))
                == Character.DIRECTIONALITY_RIGHT_TO_LEFT;
    }
}
