package com.vondear.rxui.view.wheelhorizontal;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author vondear
 * Wheel items adapter interface
 */
public interface WheelViewAdapter {
    /**
     * Gets items count
     *
     * @return the count of spinnerwheel items
     */
    public int getItemsCount();

    /**
     * Get a View that displays the data at the specified position in the data set
     *
     * @param index       the item index
     * @param convertView the old view to reuse if possible
     * @param parent      the parent that this view will eventually be attached to
     * @return the spinnerwheel item View
     */
    public View getItem(int index, View convertView, ViewGroup parent);

    /**
     * Get a View that displays an empty spinnerwheel item placed before the first or after
     * the last spinnerwheel item.
     *
     * @param convertView the old view to reuse if possible
     * @param parent      the parent that this view will eventually be attached to
     * @return the empty item View
     */
    public View getEmptyItem(View convertView, ViewGroup parent);

    /**
     * Register an observer that is called when changes happen to the data used by this adapter.
     *
     * @param observer the observer to be registered
     */
    public void registerDataSetObserver(DataSetObserver observer);

    /**
     * Unregister an observer that has previously been registered
     *
     * @param observer the observer to be unregistered
     */
    void unregisterDataSetObserver(DataSetObserver observer);
}
