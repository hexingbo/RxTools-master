package com.vondear.rxui.view.wheelhorizontal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @author vondear
 * Scroller class handles scrolling events and updates the spinnerwheel
 */
public abstract class WheelScroller {
    /**
     * Scrolling listener interface
     */
    public interface ScrollingListener {
        /**
         * Scrolling callback called when scrolling is performed.
         *
         * @param distance the distance to scroll
         */
        void onScroll(int distance);

        /**
         * This callback is invoked when scroller has been touched
         */
        void onTouch();

        /**
         * This callback is invoked when touch is up
         */
        void onTouchUp();

        /**
         * Starting callback called when scrolling is started
         */
        void onStarted();

        /**
         * Finishing callback called after justifying
         */
        void onFinished();

        /**
         * Justifying callback called to justify a view when scrolling is ended
         */
        void onJustify();
    }

    /**
     * Scrolling duration
     */
    private static final int SCROLLING_DURATION = 400;

    /**
     * Minimum delta for scrolling
     */
    public static final int MIN_DELTA_FOR_SCROLLING = 1;

    // Listener
    private ScrollingListener listener;

    // Context
    private Context context;

    // Scrolling
    private GestureDetector gestureDetector;
    protected Scroller scroller;
    private int lastScrollPosition;
    private float lastTouchedPosition;
    private boolean isScrollingPerformed;

    /**
     * Constructor
     *
     * @param context  the current context
     * @param listener the scrolling listener
     */
    public WheelScroller(Context context, ScrollingListener listener) {
        gestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // Do scrolling in onTouchEvent() since onScroll() are not call immediately
                //  when user touch and move the spinnerwheel
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                lastScrollPosition = 0;
                scrollerFling(lastScrollPosition, (int) velocityX, (int) velocityY);
                setNextMessage(MESSAGE_SCROLL);
                return true;
            }

            // public boolean onDown(MotionEvent motionEvent);

        });
        gestureDetector.setIsLongpressEnabled(false);

        scroller = new Scroller(context);

        this.listener = listener;
        this.context = context;
    }

    /**
     * Set the the specified scrolling interpolator
     *
     * @param interpolator the interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        scroller.forceFinished(true);
        scroller = new Scroller(context, interpolator);
    }

    /**
     * Scroll the spinnerwheel
     *
     * @param distance the scrolling distance
     * @param time     the scrolling duration
     */
    public void scroll(int distance, int time) {
        scroller.forceFinished(true);
        lastScrollPosition = 0;
        scrollerStartScroll(distance, time != 0 ? time : SCROLLING_DURATION);
        setNextMessage(MESSAGE_SCROLL);
        startScrolling();
    }

    /**
     * Stops scrolling
     */
    public void stopScrolling() {
        scroller.forceFinished(true);
    }

    /**
     * Handles Touch event
     *
     * @param event the motion event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastTouchedPosition = getMotionEventPosition(event);
                scroller.forceFinished(true);
                clearMessages();
                listener.onTouch();
                break;

            case MotionEvent.ACTION_UP:
                if (scroller.isFinished()) {
                    listener.onTouchUp();
                }
                break;


            case MotionEvent.ACTION_MOVE:
                // perform scrolling
                int distance = (int) (getMotionEventPosition(event) - lastTouchedPosition);
                if (distance != 0) {
                    startScrolling();
                    listener.onScroll(distance);
                    lastTouchedPosition = getMotionEventPosition(event);
                }
                break;
            default:
                break;
        }

        if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
            justify();
        }

        return true;
    }


    // Messages
    private final int MESSAGE_SCROLL = 0;
    private final int MESSAGE_JUSTIFY = 1;

    /**
     * Set next message to queue. Clears queue before.
     *
     * @param message the message to set
     */
    private void setNextMessage(int message) {
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }

    /**
     * Clears messages from queue
     */
    private void clearMessages() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }

    // animation handler
    @SuppressLint("HandlerLeak")
    private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            scroller.computeScrollOffset();
            int currPosition = getCurrentScrollerPosition();
            int delta = lastScrollPosition - currPosition;
            lastScrollPosition = currPosition;
            if (delta != 0) {
                listener.onScroll(delta);
            }

            // scrolling is not finished when it comes to final Y
            // so, finish it manually 
            if (Math.abs(currPosition - getFinalScrollerPosition()) < MIN_DELTA_FOR_SCROLLING) {
                // currPosition = getFinalScrollerPosition();
                scroller.forceFinished(true);
            }
            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == MESSAGE_SCROLL) {
                justify();
            } else {
                finishScrolling();
            }
        }
    };

    /**
     * Justifies spinnerwheel
     */
    private void justify() {
        listener.onJustify();
        setNextMessage(MESSAGE_JUSTIFY);
    }

    /**
     * Starts scrolling
     */
    private void startScrolling() {
        if (!isScrollingPerformed) {
            isScrollingPerformed = true;
            listener.onStarted();
        }
    }

    /**
     * Finishes scrolling
     */
    protected void finishScrolling() {
        if (isScrollingPerformed) {
            listener.onFinished();
            isScrollingPerformed = false;
        }
    }

    protected abstract int getCurrentScrollerPosition();

    protected abstract int getFinalScrollerPosition();

    protected abstract float getMotionEventPosition(MotionEvent event);

    protected abstract void scrollerStartScroll(int distance, int time);

    protected abstract void scrollerFling(int position, int velocityX, int velocityY);
}
