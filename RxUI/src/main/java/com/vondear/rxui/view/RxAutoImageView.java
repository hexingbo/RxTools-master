package com.vondear.rxui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vondear.rxui.R;


/**
 * @author vondear
 * 自动左右平滑移动的ImageView
 */
public class RxAutoImageView extends FrameLayout {

    private ImageView mImageView;

    int resId;

    public RxAutoImageView(Context context) {
        super(context);
    }

    public RxAutoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //导入布局
        initView(context, attrs);
    }

    private void initView(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_auto_imageview, this);

        mImageView = findViewById(R.id.img_backgroud);

        //获得这个控件对应的属性。
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RxAutoImageView);

        try {
            //获得属性值
            resId = a.getResourceId(R.styleable.RxAutoImageView_ImageSrc, 0);
        } finally {
            //回收这个对象
            a.recycle();
        }

        if (resId != 0) {
            mImageView.setImageResource(resId);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
                mImageView.startAnimation(animation);
            }
        }, 200);
    }
}
