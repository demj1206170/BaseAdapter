package xyz.demj.library.camrecyclerviewadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by demj on 2016/10/15.
 */

public abstract class BaseRecyclerViewAdapter<E> extends RecyclerView.Adapter<BaseRecyclerViewHolder<E>> implements IBaseAdapter<E> {

    protected final List<E> mElements;
    protected final BaseRecyclerViewHolder.ViewHolderFactory<E> mViewHolderFactory;
    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;
    OnHandleClickListener mOnHandleClickListener;
    boolean isInCAM = false;
    private OnOperateCallback<E> mOnOperateCallback = new OnOperateCallbackImpl<>();

    public BaseRecyclerViewAdapter(BaseRecyclerViewHolder.ViewHolderFactory<E> pFactory) {
        if (pFactory == null)
            throw new IllegalStateException("ViewHolderFactory must not null.");
        mElements = createNewList();
        mViewHolderFactory = pFactory;
    }

    protected List<E> createNewList() {
        return new LinkedList<>();
    }

    protected void setOnOperateCallback(OnOperateCallback<E> pOnOperateCallback) {
        if (pOnOperateCallback == null)
            mOnOperateCallback = new OnOperateCallbackImpl<>();
        else mOnOperateCallback = pOnOperateCallback;
    }

    public void setOnItemClickListener(OnItemClickListener pOnItemClickListener) {
        mOnItemClickListener = pOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener pOnItemLongClickListener) {
        mOnItemLongClickListener = pOnItemLongClickListener;
    }

    public void setOnHandleClickListener(OnHandleClickListener pOnHandleClickListener) {
        mOnHandleClickListener = pOnHandleClickListener;
    }

    @Override
    public final BaseRecyclerViewHolder<E> onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder<E> viewHolder = mViewHolderFactory.createViewHolder(parent, viewType);
        viewHolder.mAdapter = this;
        doAfterCreateViewHolder(viewHolder);
        return viewHolder;
    }

    protected void doAfterCreateViewHolder(BaseRecyclerViewHolder<E> pCraetedViewHolder) {

    }

    @Override
    public final void onBindViewHolder(BaseRecyclerViewHolder<E> holder, int position) {
        holder.bindViewHolder(get(position), this, position);
    }

    @Override
    public int getItemCount() {
        return mElements.size();
    }

    @Override
    public void add(E element) {
        internalAdd(element, -1, true);
    }

    @Override
    public void add(E element, int position) {
        internalAdd(element, position, true);
    }

    @Override
    public void add(E element, E beforeWhich) {
        internalAdd(element, beforeWhich, true, true);
    }

    @Override
    public void add(E element, boolean notify) {
        internalAdd(element, mElements.size(), notify);
    }

    @Override
    public void add(E element, int position, boolean notify) {
        internalAdd(element, position, notify);
    }

    @Override
    public void add(E element, E beforeWhichOne, boolean notify) {
        internalAdd(element, beforeWhichOne, notify, true);
    }

    @Override
    public void add(E element, Selector<? super E> pSelector) {
        internalAdd(element, pSelector, true);
    }

    @Override
    public void add(E element, Selector<? super E> pSelector, boolean notify) {
        internalAdd(element, pSelector, notify);
    }

    @Override
    public void addAll(Collection<? extends E> elements, int position, boolean notify) {
        internalAddAll(elements, position, notify);
    }

    private void internalAddAll(Collection<? extends E> elements, int position, boolean notify) {
        List<E> list = Collections.unmodifiableList(filterNoneNull(elements));
        int count = list.size();
        if (position <= -1 || position > mElements.size())
            position = mElements.size();
        if (count != 0) {
            doBeforeAddAll(position, list);
            mElements.addAll(position, list);
            doAfterAddAll(position, list, notify);
            if (notify)
                notifyItemRangeInserted(position, count);
        }
    }

    private static <E> List<E> filterNoneNull(Collection<? extends E> pCollection) {
        List<E> list = new ArrayList<>(pCollection.size());
        for (E e : pCollection) {
            if (e != null)
                list.add(e);
        }
        return list;
    }

    protected void doBeforeAddAll(int pPosition, List<E> pList) {
        mOnOperateCallback.doBeforeAddAll(pPosition, pList);
    }

    protected void doAfterAddAll(int pPosition, List<E> pList, boolean notify) {
        mOnOperateCallback.doAfterAddAll(pPosition, pList, notify);
    }

    @Override
    public void addAll(Collection<? extends E> elements, int position) {
        internalAddAll(elements, position, true);
    }

    @Override
    public void addAll(Collection<? extends E> elements, boolean notify) {
        internalAddAll(elements, -1, notify);
    }

    @Override
    public void addAll(Collection<? extends E> elements) {
        internalAddAll(elements, -1, true);
    }

    @Override
    public void addAll(E[] elements, int postion, boolean notify) {
        internalAddAll(elements, postion, notify);
    }

    private void internalAddAll(E[] elements, int postion, boolean notify) {
        int length = elements.length;
        int count = 0;
        if (postion <= -1 || postion > mElements.size())
            postion = mElements.size();
        for (int i = length - 1; i >= 0; i--) {
            E e = elements[i];
            if (e != null) {
                doBeforeAdd(postion, e, notify);
                mElements.add(postion, e);
                doAfterAdd(postion, e, notify);
                ++count;
            }
        }
        if (notify && count > 0)
            notifyItemRangeInserted(postion, count);
    }

    @Override
    public void addAll(E[] elements, int postion) {
        internalAddAll(elements, postion, true);
    }

    @Override
    public void addAll(E[] elements, boolean notify) {
        internalAddAll(elements, -1, notify);
    }

    @Override
    public void addAll(E[] elements) {
        internalAddAll(elements, -1, true);
    }

    @Override
    public E remove(int position, boolean notify) {
        return internalRemove(position, notify);
    }

    private E internalRemove(int position, boolean notify) {
        if (!validPos(position))
            return null;
        doBeforeRemove(position);
        E removed = mElements.remove(position);
        if (notify)
            notifyItemRemoved(position);
        doAfterRemove(removed, notify);
        return removed;
    }

    private boolean validPos(int pos) {
        return pos >= 0 && pos < mElements.size();
    }

    protected void doBeforeRemove(int pos) {
        mOnOperateCallback.doBeforeRemove(pos);
    }

    protected void doAfterRemove(E e, boolean notify) {
        mOnOperateCallback.doAfterRemove(e, notify);
    }

    @Override
    public E remove(int position) {
        return internalRemove(position, true);
    }

    @Override
    public E remove(E comparator, boolean notify) {
        return internalRemove(comparator, notify);
        //return remove(index, notify);
    }

    private E internalRemove(E comparator, boolean notify) {
        if (comparator == null)
            return null;
        E removed = comparator;
        int index = mElements.indexOf(comparator);
        if (index != -1) {
            doBeforeRemove(index);
            removed = mElements.remove(index);
            doAfterRemove(removed, notify);
            if (notify)
                notifyItemRemoved(index);
        }
        return removed;
    }

    @Override
    public E remove(E comparator) {
        return internalRemove(comparator, true);
    }

    @Override
    public E removeBy(Selector<? super E> pSelector) {
        return internalRemoveBy(pSelector, true, false, false);
    }

    private E internalRemoveBy(Selector<? super E> pSelector, boolean notify, boolean justOne, boolean reversed) {
        E removed = null;
        for (E e : copyList(mElements, reversed)) {
            if (pSelector.isSelected(e)) {
                int index = mElements.indexOf(e);
                if (index != -1) {
                    doBeforeRemove(index);
                    removed = mElements.remove(index);
                    if (notify)
                        notifyItemRemoved(index);
                    doAfterRemove(removed, notify);
                    if (justOne)
                        break;
                }
            }
        }
        return removed;
    }

    private static <E> List<E> copyList(List<E> list, boolean isReversed) {
        List<E> ls = new ArrayList<>(list.size());
        ls.addAll(list);
        if (isReversed)
            Collections.reverse(ls);
        return ls;
    }

    @Override
    public E removeBy(Selector<? super E> pSelector, boolean notify) {
        return internalRemoveBy(pSelector, notify, false, false);
    }

    @Override
    public E removeFirst(Selector<? super E> pSelector, boolean notify) {
        return internalRemoveBy(pSelector, notify, true, false);
    }

    @Override
    public E removeFirst(Selector<? super E> pSelector) {
        return internalRemoveBy(pSelector, true, true, false);
    }

    @Override
    public E removeLast(Selector<? super E> pSelector) {
        return internalRemoveBy(pSelector, true, true, true);
    }

    @Override
    public E removeLast(Selector<? super E> pSelector, boolean notify) {
        return internalRemoveBy(pSelector, notify, true, true);
    }

    @Override
    public void removeAll(boolean notify) {
        internalRemoveAll(notify);
    }

    private void internalRemoveAll(boolean notify) {
        int size = mElements.size();
        doBeforeRemoveAll(notify);
        mElements.clear();
        doAfterRemoveAll(notify);
        if (notify)
            notifyItemRangeRemoved(0, size);
    }

    protected void doBeforeRemoveAll(boolean notify) {
        mOnOperateCallback.doBeforeRemoveAll(notify);
    }

    protected void doAfterRemoveAll(boolean notify) {
        mOnOperateCallback.doAfterRemoveAll(notify);
    }

    @Override
    public void remove(Collection<? extends E> elements, boolean notify) {
//        mElements.removeAll(elements);
//        if (notify)
        internalRemove(elements, notify);
    }

    private void internalRemove(Collection<? extends E> elements, boolean notify) {
        for (E e : elements) {
            int index = mElements.indexOf(e);
            if (index != -1) {
                doBeforeRemove(index);
                mElements.remove(index);
                if (notify)
                    notifyItemRemoved(index);
                doAfterRemove(e, notify);
            }
        }
    }

    @Override
    public void remove(Collection<? extends E> elements) {
        internalRemove(elements, true);
    }

    @Override
    public void remove(E[] elements) {
        internalRemove(Arrays.asList(elements), true);
    }

    @Override
    public void remove(E[] elements, boolean notify) {
        internalRemove(Arrays.asList(elements), notify);
    }

    @Override
    public void removeByPositions(Collection<Integer> positions, boolean notify) {
        internalRemoveByPositions(positions, notify);
    }

    private void internalRemoveByPositions(Collection<Integer> positions, boolean notify) {
        List<E> list = new ArrayList<>(positions.size());
        for (int pos : positions) {
            E e = mElements.get(pos);
            if (e != null)
                list.add(e);
        }
        internalRemove(list, notify);
    }

    @Override
    public void removeByPositions(Collection<Integer> positions) {
        internalRemoveByPositions(positions, true);
    }

    @Override
    public void removeByPositions(int[] positions) {
        removeByPositions(positions, true);
    }

    @Override
    public void removeByPositions(int[] positions, boolean notify) {
        internalRemoveByPositions(positions, notify);
    }

    @Override
    public void removeAll() {
        internalRemoveAll(true);
    }

    @Override
    public E set(E pNewOne, int position, boolean notify) {
        return internalSet(pNewOne, position, notify);
    }

    @Override
    public E set(E pNewOne, int position) {
        return internalSet(pNewOne, position, true);
    }

    private E internalSet(E pNewOne, int position, boolean notify) {

        if (pNewOne == null)
            return null;
        if (!validPos(position))
            return null;
        doBeforeSet(position, pNewOne, notify);
        E old = mElements.set(position, pNewOne);
        doAfterSet(old, pNewOne, notify);
        if (notify)
            notifyItemChanged(position);
        return old;
    }

    protected void doBeforeSet(int position, E newOne, boolean notify) {
        mOnOperateCallback.doBeforeSet(position, newOne, notify);
    }

    protected void doAfterSet(E oldOne, E newOne, boolean notify) {
        mOnOperateCallback.doAfterSet(oldOne, newOne, notify);
    }

    @Override
    public E set(E pOldOne, E pNewOne, boolean notify) {
        if (pOldOne == null || pNewOne == null)
            return null;
        int index = mElements.indexOf(pOldOne);
        return internalSet(pNewOne, index, notify);
    }

    @Override
    public E set(E pOldOne, E pNewOne) {
        return internalSet(pOldOne, pNewOne, true);
    }

    private E internalSet(E pOldOne, E pNewOne, boolean notify) {
        if (pOldOne == null || pNewOne == null)
            return null;
        int index = mElements.indexOf(pOldOne);
        return internalSet(pNewOne, index, notify);

    }

    @Override
    public E set(E pNewOne, Selector<? super E> pSelector) {
        return internalSet(pNewOne, pSelector, true, false, false);
    }

    private E internalSet(E pNewOne, Selector<? super E> pSelector, boolean notify, boolean justOne, boolean isReversed) {
        if (pNewOne == null)
            return null;
        E old = null;

        for (E e : copyList(mElements, isReversed)) {
            if (pSelector.isSelected(e)) {
                int index = mElements.indexOf(e);
                old = internalSet(pNewOne, index, notify);
                if (notify)
                    notifyItemChanged(index);
                if (justOne)
                    break;
            }
        }
        return old;
    }

    @Override
    public E set(E pNewOne, Selector<? super E> pSelector, boolean notify) {
        return internalSet(pNewOne, pSelector, notify, false, false);
    }

    @Override
    public E setFirst(E pNewOne, Selector<? super E> pSelector, boolean notify) {
        return internalSet(pNewOne, pSelector, notify, true, false);
    }

    @Override
    public E setFirst(E pNewOne, Selector<? super E> pSelector) {
        return internalSet(pNewOne, pSelector, true, true, false);
    }

    @Override
    public E setLast(E pNewOne, Selector<? super E> pSelector, boolean notify) {
        return internalSet(pNewOne, pSelector, notify, true, true);
    }

    @Override
    public E setLast(E pNewOne, Selector<? super E> pSelector) {
        return internalSet(pNewOne, pSelector, true, true, true);
    }

    @Override
    public E get(int position) {
        return mElements.get(position);
    }

    @Override
    public List<E> get(Selector<? super E> pSelector) {
        List<E> ls = new LinkedList<>();
        for (E e : mElements) {
            if (pSelector.isSelected(e)) {
                ls.add(e);
            }
        }
        return ls;
    }

    @Override
    public E getFirst(Selector<? super E> pSelector) {
        return internalGet(pSelector, false);
    }

    private E internalGet(Selector<? super E> pSelector, boolean reversed) {
        E target = null;
        for (E e : copyList(mElements, reversed)) {
            if (pSelector.isSelected(e)) {
                int index = mElements.indexOf(e);
                if (index != -1) {
                    target = e;
                    break;
                }
            }
        }
        return target;
    }

    @Override
    public E getLast(Selector<? super E> pSelector) {
        return internalGet(pSelector, true);
    }

    private void internalRemoveByPositions(int[] positions, boolean notify) {
        List<Integer> posss = new ArrayList<Integer>(positions.length);
        for (int integer : positions) {
            posss.add(integer);
        }
        internalRemoveByPositions(posss, notify);
    }

    private void internalAdd(E element, Selector<? super E> pSelector, boolean notify) {
        if (pSelector == null)
            return;
        for (E e : mElements) {
            if (pSelector.isSelected(e)) {
                int index = mElements.indexOf(e);
                internalAdd(element, index, notify);
                break;
            }
        }
    }

    private void internalAdd(E element, E beforeWhichOne, boolean notify, boolean before) {
        if (beforeWhichOne == null)
            return;
        int index = mElements.indexOf(beforeWhichOne);
        if (!before || index != -1)
            internalAdd(element, index, notify);
    }

    private void internalAdd(E element, int position, boolean notify) {
        if (element == null)
            return;
        if (position <= -1 || position > mElements.size())
            position = mElements.size();
//        if (position > mElements.size())
//            position = mElements.size();
        doBeforeAdd(position, element, notify);
        mElements.add(position, element);
        doAfterAdd(position, element, notify);
        if (notify)
            notifyItemInserted(position);
    }

    protected void doBeforeAdd(int pos, E pNewOne, boolean notify) {
        mOnOperateCallback.doBeforeAdd(pos, pNewOne, notify);
    }

    protected void doAfterAdd(int pPosition, E pElement, boolean notify) {
        mOnOperateCallback.doAfterAdded(pPosition, pElement, notify);
    }

    public void addAfter(E element, E afterWhichOne, boolean notify) {
        internalAdd(element, afterWhichOne, notify, false);
    }

    public void addAfter(E element, E afterWhichOne) {
        internalAdd(element, afterWhichOne, true, false);
    }

    protected boolean canHandleClick(View pView) {
        return false;
    }

    public void toggleSelection(int position) {
    }

    public boolean isInContentActionMode() {
        return isInCAM;
    }

    public void attachToContentActionMode(int triggerPosition) {
    }

    public void detachFromContentActionMode() {
    }

    public boolean isItemSelected(int position) {
        return false;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onLongClick(View pView, int position);
    }

    public interface OnHandleClickListener {
        void handleClick(View pView, int position);
    }

    protected interface OnOperateCallback<E> {
        void doBeforeRemove(int pos);

        void doBeforeSet(int position, E newOne, boolean notify);

        void doAfterSet(E oldOne, E newOne, boolean notify);

        void doAfterRemove(E e, boolean notify);

        void doAfterAdded(int pPosition, E newOne, boolean notify);

        void doBeforeAdd(int pos, E pWill, boolean notify);

        void doBeforeAddAll(int pPosition, List<E> pList);

        void doAfterAddAll(int pPosition, List<E> pList, boolean notify);

        void doAfterRemoveAll(boolean notify);

        void doBeforeRemoveAll(boolean pNotify);

    }

    static class OnOperateCallbackImpl<E> implements OnOperateCallback<E> {
        @Override
        public void doBeforeRemove(int pos) {

        }

        @Override
        public void doBeforeSet(int position, E newOne, boolean notify) {

        }

        @Override
        public void doAfterSet(E oldOne, E newOne, boolean notify) {

        }

        @Override
        public void doAfterRemove(E pE, boolean notify) {

        }

        @Override
        public void doAfterAdded(int pPosition, E newOne, boolean notify) {

        }

        @Override
        public void doBeforeAdd(int pos, E pWill, boolean notify) {

        }

        @Override
        public void doBeforeAddAll(int pPosition, List<E> pList) {

        }

        @Override
        public void doAfterAddAll(int pPosition, List<E> pList, boolean notify) {

        }

        @Override
        public void doAfterRemoveAll(boolean notify) {

        }

        @Override
        public void doBeforeRemoveAll(boolean pNotify) {

        }
    }

}
