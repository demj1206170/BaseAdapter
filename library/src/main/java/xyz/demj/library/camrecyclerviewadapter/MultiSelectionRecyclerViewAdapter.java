package xyz.demj.library.camrecyclerviewadapter;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by demj on 2016/10/15.
 */

public class MultiSelectionRecyclerViewAdapter<E> extends BaseRecyclerViewAdapter<E> {
    final ArrayMap<E, Boolean> mSelectedItems = new ArrayMap<>();

    private MultiSelectionOnOperateCallback<E> mMultiSelectionOnOperateCallback = new MultiSelectionOnOperateCallbackImpl<>();

    public MultiSelectionRecyclerViewAdapter(BaseRecyclerViewHolder.ViewHolderFactory<E> pFactory) {
        super(pFactory);
    }

    public void setMultiSelectionOnOperateCallback(MultiSelectionOnOperateCallback<E> pMultiSelectionOnOperateCallback) {
        if (pMultiSelectionOnOperateCallback != null)
            mMultiSelectionOnOperateCallback = pMultiSelectionOnOperateCallback;
        else mMultiSelectionOnOperateCallback = new MultiSelectionOnOperateCallbackImpl<>();
    }


    public interface OnItemSelectionChangedListener {
        void onItemSelectionChanged(int position, boolean isSelected);

        void onItemSelectionChanged(Map<Integer, Boolean> pMap);
    }

    private OnItemSelectionChangedListener mOnItemSelectionChangedListener;

    public void setOnItemSelectionChangedListener(OnItemSelectionChangedListener pOnItemSelectionChangedListener) {
        mOnItemSelectionChangedListener = pOnItemSelectionChangedListener;
    }

    @Override
    public void toggleSelection(int position) {
        setItemSelection(position, !isItemSelected(position));
    }

    @Override
    public void attachToContentActionMode(int triggerPosition) {
        mSelectedItems.clear();
        isInCAM = true;
        if (validPos(triggerPosition)) {
            setItemSelection(triggerPosition, true, true);
        }
    }

    @Override
    public void detachFromContentActionMode() {
        isInCAM = false;
        clearSelection(true);
    }

    public void setItemSelection(int position, boolean isSelected) {
        setItemSelection(position, isSelected, true);
    }

    @Override
    public boolean isItemSelected(int position) {
        if (!validPos(position))
            return false;
        return isItemSelected(mElements.get(position));
    }

    public boolean isItemSelected(E element) {
        if (element == null)
            return false;
        Boolean b = mSelectedItems.get(element);
        return b != null ? b : false;
    }

    @Override
    protected void doBeforeRemove(int pos) {
        super.doBeforeRemove(pos);
    }

    @Override
    protected void doAfterRemove(E pE, boolean notify) {
        super.doAfterRemove(pE, notify);
        internalSetItemSelection(pE, false, isInCAM && notify);

    }

    private boolean internalSetItemSelection(E e, boolean isSelected, boolean notify) {
        int position = mElements.indexOf(e);
        if (isSelected && position == -1)
            return false;
        doBeforeSetItemSelection(position, isSelected);
        if (isSelected)
            mSelectedItems.put(e, true);
        else mSelectedItems.remove(e);
        doAfterSetItemSelection(position, isSelected, notify);
        if (notify) {
            notifyItemChanged(position);
            if (mOnItemSelectionChangedListener != null)
                mOnItemSelectionChangedListener.onItemSelectionChanged(position, isSelected);
        }
        return true;
    }

    private void doBeforeSetItemSelection(int pPosition, boolean pIsSelected) {
        mMultiSelectionOnOperateCallback.doBeforeSetItemSelection(pPosition, pIsSelected);
    }

    private void doAfterSetItemSelection(int pPosition, boolean pIsSelected, boolean pNotify) {
        mMultiSelectionOnOperateCallback.doAfterSetItemSelection(pPosition, pIsSelected, pNotify);
    }

    @Override
    public void removeAll(boolean notify) {
        super.removeAll(notify);
        mSelectedItems.clear();
    }

    public boolean setItemSelection(int position, boolean selected, boolean notify) {
        if (!validPos(position))
            return false;
        return internalSetItemSelection(mElements.get(position), selected, notify);
    }

    private void notifyItemsSelectionChanged(Map<Integer, Boolean> pMap, boolean ifNull) {
        if (mOnItemSelectionChangedListener != null) {
            if (pMap == null) {
                int count = mElements.size();
                pMap = new ArrayMap<>(count);
                for (int i = 0; i < count; ++i) {
                    pMap.put(i, ifNull);
                }
            }
            mOnItemSelectionChangedListener.onItemSelectionChanged(pMap);
        }
    }

    public void setSelectedItems(int[] pSelectedItemPositions, boolean notify) {
        List<Integer> positions = new ArrayList<>(pSelectedItemPositions.length);
        for (int po : pSelectedItemPositions) {
            positions.add(po);
        }
        setSelectedItemsByPosition(positions, notify);
    }

    public void setSelectedItems(int[] pSelectedItemPositions) {
        setSelectedItems(pSelectedItemPositions, true);
    }

    public void setSelectedItems(Collection<? extends E> pSelectedItems, boolean notify) {
        Map<Integer, Boolean> map = new ArrayMap<Integer, Boolean>(pSelectedItems.size());
        for (E e : pSelectedItems) {
            int index = mElements.indexOf(e);
            if (index != -1) {
                doBeforeSetItemSelection(index, true);
                mSelectedItems.put(e, true);
                doAfterSetItemSelection(index, true, notify);
                map.put(index, true);
                if (notify)
                    notifyItemChanged(index);
            }
        }
        if (map.size() > 0 && mOnItemSelectionChangedListener != null) {
            mOnItemSelectionChangedListener.onItemSelectionChanged(map);
        }
    }

    public void setSelectedItemsByPosition(Collection<Integer> pPositions, boolean notify) {
        List<E> pSelectedItem = new ArrayList<>(pPositions.size());
        for (int position : pPositions) {
            if (validPos(position)) {
                pSelectedItem.add(mElements.get(position));
            }
        }
        setSelectedItems(pSelectedItem, notify);
    }

    public void setSelectedItemsByPosition(Collection<Integer> pPositions) {
        setSelectedItemsByPosition(pPositions, true);
    }

    public void clearSelection(boolean notify) {
        List<Integer> mSelected = getSelectedItemPositions();
        doBeforeClearItemSelection(notify);
        mSelectedItems.clear();
        doAfterClearItemSelection(notify);

        Map<Integer, Boolean> map = new ArrayMap<>(mSelected.size());
        if (notify) {
            for (Integer integer : mSelected) {
                map.put(integer, Boolean.FALSE);
                notifyItemChanged(integer);
            }
            if (mOnItemSelectionChangedListener != null)
                mOnItemSelectionChangedListener.onItemSelectionChanged(map);
        }
    }

    private void doAfterClearItemSelection(boolean notify) {
        mMultiSelectionOnOperateCallback.doAfterClearItemSelection(notify);
    }

    private void doBeforeClearItemSelection(boolean notify) {
        mMultiSelectionOnOperateCallback.doBeforeClearItemSelection(notify);
    }

    public void clearSelection() {
        clearSelection(true);
    }

    public List<Integer> getSelectedItemPositions(boolean sorted) {
        List<Integer> selectedPositions = new ArrayList<>(mSelectedItems.size());
        for (E e : mSelectedItems.keySet()) {
            int index = mElements.indexOf(e);
            if (validPos(index) && mSelectedItems.get(e)) {
                selectedPositions.add(index);
            } else mSelectedItems.remove(e);
        }
        if (sorted)
            Collections.sort(selectedPositions);
        return selectedPositions;
    }

    public List<Integer> getSelectedItemPositions() {
        return getSelectedItemPositions(true);
    }

    public List<E> getSelectedItems() {
        List<E> selected = new ArrayList<>(mSelectedItems.size());
        List<Integer> list = getSelectedItemPositions();
//        for (E e : mSelectedItems.keySet()) {
//            int index = mElements.indexOf(e);
//            if (index != -1 && mSelectedItems.get(e)) {
//                selected.add(e);
//            } else mSelectedItems.remove(e);
//        }
        for (Integer integer : list) {
            E e = mElements.get(integer);
            if (e != null) {
                selected.add(e);
            }
        }
        return selected;
    }

    @Override
    protected void doBeforeSet(int position, E newOne, boolean notify) {
        super.doBeforeSet(position, newOne, notify);
    }

    @Override
    protected void doAfterSet(E oldOne, E newOne, boolean notify) {
        super.doAfterSet(oldOne, newOne, notify);
        boolean is = isItemSelected(oldOne);
        if (is) {
            internalSetItemSelection(oldOne, false, false);
            internalSetItemSelection(newOne, true, false);
        }
    }

    protected interface MultiSelectionOnOperateCallback<E> {
        void doAfterSetItemSelection(int position, boolean isSelected, boolean pNotify);

        void doBeforeSetItemSelection(int positin, boolean isSelected);

        void doAfterClearItemSelection(boolean notify);

        void doBeforeClearItemSelection(boolean notify);
    }

    static class MultiSelectionOnOperateCallbackImpl<E> implements MultiSelectionOnOperateCallback<E> {

        @Override
        public void doAfterSetItemSelection(int position, boolean isSelected, boolean pNotify) {

        }

        @Override
        public void doBeforeSetItemSelection(int positin, boolean isSelected) {

        }

        @Override
        public void doAfterClearItemSelection(boolean notify) {

        }

        @Override
        public void doBeforeClearItemSelection(boolean notify) {

        }
    }


}

