package xyz.demj.library.camrecyclerviewadapter;

import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by demj on 2016/10/16.
 */

public class ConvertAdapter<E, T extends ConvertAdapter.To<E>> extends MultiSelectionRecyclerViewAdapter<E> {
    private final MultiSelectionRecyclerViewAdapter<T> mToMultiSelectionRecyclerViewAdapter;

    private final ArrayMap<E, To<E>> mElementMap = new ArrayMap<>();


    private ArrayMap<E, T> mEToArrayMap = new ArrayMap<>();

    @Override
    public int getItemViewType(int position) {
        return mToMultiSelectionRecyclerViewAdapter.getItemViewType(position);
    }

    final TFactory<E, T> mETTFactory;

    public ConvertAdapter(BaseRecyclerViewHolder.ViewHolderFactory<E> pFactory, TFactory<E, T> pETTFactory) {
        super(pFactory);
        mETTFactory = pETTFactory;
        mToMultiSelectionRecyclerViewAdapter = new MultiSelectionRecyclerViewAdapter<>(new BaseRecyclerViewHolder.ViewHolderFactory<T>() {
            @Override
            public BaseRecyclerViewHolder<T> createViewHolder(ViewGroup parent, int viewType) {
                BaseRecyclerViewHolder<E> viewHolder = mViewHolderFactory.createViewHolder(parent, viewType);
                viewHolder.mAdapter = ConvertAdapter.this;
                return new ToViewHolder<>(viewHolder);
            }
        });
        mToMultiSelectionRecyclerViewAdapter.setMultiSelectionOnOperateCallback(mMultiSelectionOnOperateCallback);
        mToMultiSelectionRecyclerViewAdapter.setOnOperateCallback(msOnOperateCallback);
    }

    @Override
    public void add(E element, int position) {
        add(element, position, true);
    }

    @Override
    public void add(E element, int position, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.add(get(element), position, notify);
    }

    @Override
    public void setOnItemSelectionChangedListener(OnItemSelectionChangedListener pOnItemSelectionChangedListener) {
        mToMultiSelectionRecyclerViewAdapter.setOnItemSelectionChangedListener(pOnItemSelectionChangedListener);
    }

    @Override
    public void toggleSelection(int position) {
        mToMultiSelectionRecyclerViewAdapter.toggleSelection(position);
    }

    @Override
    public void attachToContentActionMode(int triggerPosition) {
        super.attachToContentActionMode(triggerPosition);
        mToMultiSelectionRecyclerViewAdapter.attachToContentActionMode(triggerPosition);
    }

    @Override
    public void detachFromContentActionMode() {
        super.detachFromContentActionMode();
        mToMultiSelectionRecyclerViewAdapter.detachFromContentActionMode();
    }

    @Override
    public void setItemSelection(int position, boolean isSelected) {
        mToMultiSelectionRecyclerViewAdapter.setItemSelection(position, isSelected);
    }

    @Override
    public boolean isItemSelected(int position) {
        return mToMultiSelectionRecyclerViewAdapter.isItemSelected(position);
    }

    @Override
    public boolean isItemSelected(E element) {
        return mToMultiSelectionRecyclerViewAdapter.isItemSelected(findTo(element));
    }

    private T findTo(E key) {
        if (key == null)
            return null;
        return mEToArrayMap.get(key);
    }

    @Override
    public void removeAll(boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.removeAll(notify);
    }

    @Override
    public boolean setItemSelection(int position, boolean selected, boolean notify) {
        return mToMultiSelectionRecyclerViewAdapter.setItemSelection(position, selected, notify);
    }

    @Override
    public void setSelectedItems(int[] pSelectedItemPositions, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.setSelectedItems(pSelectedItemPositions, notify);
    }

    @Override
    public void setSelectedItems(int[] pSelectedItemPositions) {
        mToMultiSelectionRecyclerViewAdapter.setSelectedItems(pSelectedItemPositions);
    }

    @Override
    public void setSelectedItems(Collection<? extends E> pSelectedItems, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.setSelectedItems(convertTo(pSelectedItems, false), notify);
    }

    private Collection<T> convertTo(Collection<? extends E> pECollection, boolean create) {
        List<T> list = new ArrayList<>(pECollection.size());
        if (create) {
            for (E e : pECollection) {
                list.add(get((e)));
            }
        } else {
            for (E e : pECollection) {
                T to = findTo(e);
                if (to != null)
                    list.add(to);
            }
        }
        return list;
    }

    @Override
    public void setSelectedItemsByPosition(Collection<Integer> pPositions, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.setSelectedItemsByPosition(pPositions, notify);
    }

    @Override
    public void setSelectedItemsByPosition(Collection<Integer> pPositions) {
        mToMultiSelectionRecyclerViewAdapter.setSelectedItemsByPosition(pPositions);
    }

    @Override
    public void clearSelection(boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.clearSelection(notify);
    }

    @Override
    public void clearSelection() {
        mToMultiSelectionRecyclerViewAdapter.clearSelection();
    }

    @Override
    public List<Integer> getSelectedItemPositions() {
        return mToMultiSelectionRecyclerViewAdapter.getSelectedItemPositions();
    }

    @Override
    public List<E> getSelectedItems() {
        List<T> list = mToMultiSelectionRecyclerViewAdapter.getSelectedItems();
        return revert(list);
    }

    @Override
    public List<Integer> getSelectedItemPositions(boolean sorted) {
        return mToMultiSelectionRecyclerViewAdapter.getSelectedItemPositions(sorted);
    }

    private List<E> revert(Collection<T> pToCollection) {
        List<E> list = new ArrayList<>(pToCollection.size());
        for (To<? extends E> to : pToCollection) {
            E e = to.to();
            if (e != null)
                list.add(e);
        }
        return list;
    }


    @Override
    public void setOnItemClickListener(OnItemClickListener pOnItemClickListener) {
        super.setOnItemClickListener(pOnItemClickListener);
        mToMultiSelectionRecyclerViewAdapter.setOnItemClickListener(pOnItemClickListener);

    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener pOnItemLongClickListener) {
        super.setOnItemLongClickListener(pOnItemLongClickListener);
        mToMultiSelectionRecyclerViewAdapter.setOnItemLongClickListener(pOnItemLongClickListener);

    }

    @Override
    public void setOnHandleClickListener(OnHandleClickListener pOnHandleClickListener) {
        mToMultiSelectionRecyclerViewAdapter.setOnHandleClickListener(pOnHandleClickListener);
    }

    @Override
    protected List<E> createNewList() {
        return super.createNewList();
    }
//
//
//    @Override
//    public void onBindViewHolder(BaseRecyclerViewHolder<E> holder, int position) {
//        holder.bindViewHolder(mToMultiSelectionRecyclerViewAdapter.get(position).to(), this, position);
//        super.onBindViewHolder(holder, position);
//    }

    @Override
    public int getItemCount() {
        return mToMultiSelectionRecyclerViewAdapter.getItemCount();
    }


    private boolean validPos(int pos) {
        return pos >= 0 && pos < mToMultiSelectionRecyclerViewAdapter.mElements.size();
        // return mToMultiSelectionRecyclerViewAdapter.validPos(pos);
    }

    @Override
    public E set(E pNewOne, int position, boolean notify) {
        if (pNewOne == null)
            return null;
        T to = get(pNewOne);
        to = mToMultiSelectionRecyclerViewAdapter.set(to, position, notify);
        return to == null ? null : to.to();
    }

    @Override
    public E set(E pNewOne, int position) {
        return set(pNewOne, position, true);
    }

    @Override
    public E set(E pOldOne, E pNewOne, boolean notify) {
        if (pOldOne == null || pNewOne == null)
            return null;
        T old = findTo(pOldOne);
        if (old == null)
            return null;
        T newOne = get(pNewOne);
        old = mToMultiSelectionRecyclerViewAdapter.set(old, newOne, notify);
        return old == null ? null : old.to();
    }

    @Override
    public E set(E pOldOne, E pNewOne) {
        return set(pOldOne, pNewOne, true);
    }

    @Override
    public E set(E pNewOne, final Selector<? super E> pSelector) {
        return set(pNewOne, pSelector, true);
    }

    @Override
    public E set(E pNewOne, final Selector<? super E> pSelector, boolean notify) {
        if (pNewOne == null)
            return null;
        T to = get(pNewOne);
        to = mToMultiSelectionRecyclerViewAdapter.set(to, new Selector<T>() {
            @Override
            public boolean isSelected(T element) {
                return pSelector.isSelected(element.to());
            }
        }, notify);
        return to == null ? null : to.to();
    }

    @Override
    public E setFirst(E pNewOne, final Selector<? super E> pSelector, boolean notify) {
        if (pNewOne == null)
            return null;
        T to = get(pNewOne);
        to = mToMultiSelectionRecyclerViewAdapter.setFirst(to, new Selector<T>() {
            @Override
            public boolean isSelected(T element) {
                return pSelector.isSelected(element.to());
            }
        }, notify);
        return to == null ? null : to.to();
    }

    @Override
    public E setFirst(E pNewOne, Selector<? super E> pSelector) {
        return setFirst(pNewOne, pSelector, true);
    }

    @Override
    public E setLast(E pNewOne, final Selector<? super E> pSelector, boolean notify) {
        if (pNewOne == null)
            return null;
        T to = get(pNewOne);
        to = mToMultiSelectionRecyclerViewAdapter.setLast(to, new Selector<T>() {
            @Override
            public boolean isSelected(T element) {
                return pSelector.isSelected(element.to());
            }
        }, notify);
        return to(to);
    }

    public static <E> E to(To<E> pETo) {
        return pETo == null ? null : pETo.to();
    }

    public static <E> List<E> to(Collection<? extends To<E>> pTos) {
        List<E> list = new ArrayList<>(pTos.size());
        for (To<E> to : pTos) {
            E e = to.to();
            if (e != null)
                list.add(e);
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public E setLast(E pNewOne, Selector<? super E> pSelector) {
        return setLast(pNewOne, pSelector, true);
    }

    @Override
    public E get(int position) {
        To<E> to = mToMultiSelectionRecyclerViewAdapter.get(position);
        return to(to);
    }

    @Override
    public E getFirst(final Selector<? super E> pSelector) {
        return to(mToMultiSelectionRecyclerViewAdapter.getFirst(new Selector<To<E>>() {
            @Override
            public boolean isSelected(To<E> element) {
                return pSelector.isSelected(element.to());
            }
        }));
    }

    @Override
    public E getLast(final Selector<? super E> pSelector) {
        return to(mToMultiSelectionRecyclerViewAdapter.getLast(new Selector<To<E>>() {
            @Override
            public boolean isSelected(To<E> element) {
                return pSelector.isSelected(element.to());
            }
        }));
    }

    @Override
    public void add(E element, E beforeWhich) {
        mToMultiSelectionRecyclerViewAdapter.add(get(element), findTo(beforeWhich));
    }

    @Override
    public void add(E element, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.add(get(element), notify);
    }

    @Override
    public void add(E element, E beforeWhichOne, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.add(get(element), findTo(beforeWhichOne), notify);
    }

    @Override
    public void add(E element, final Selector<? super E> pSelector) {
        add(element, pSelector, true);
    }

    @Override
    public void add(E element, final Selector<? super E> pSelector, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.add(get(element), new Selector<To<E>>() {
            @Override
            public boolean isSelected(To<E> element) {
                return pSelector.isSelected(element.to());
            }
        }, notify);
    }

    @Override
    public void addAll(Collection<? extends E> elements, int position, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.addAll(convertTo(elements, true), position, notify);
    }

    @Override
    public void addAll(Collection<? extends E> elements, int position) {
        addAll(elements, position, true);
    }

    @Override
    public void addAll(Collection<? extends E> elements, boolean notify) {
        addAll(elements, -1, notify);
    }

    @Override
    public void addAll(Collection<? extends E> elements) {
        addAll(elements, -1, true);
    }


    @Override
    public void addAll(E[] elements, int postion, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.addAll(convertTo(Arrays.asList(elements), true), postion, notify);
    }

    @Override
    public void addAll(E[] elements, int postion) {
        mToMultiSelectionRecyclerViewAdapter.addAll(convertTo(Arrays.asList(elements), true), postion);
    }

    @Override
    public void addAll(E[] elements, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.addAll(convertTo(Arrays.asList(elements), true), notify);
    }

    @Override
    public void addAll(E[] elements) {
        addAll(elements, true);
    }


    @Override
    public E remove(int position, boolean notify) {
        return to(mToMultiSelectionRecyclerViewAdapter.remove(position, notify));
    }

    @Override
    public E remove(int position) {
        return remove(position, true);
    }

    @Override
    public E remove(E comparator, boolean notify) {
        return to(mToMultiSelectionRecyclerViewAdapter.remove(findTo(comparator), notify));
    }

    @Override
    public E remove(E comparator) {
        return remove(comparator, true);
    }

    @Override
    public E removeBy(final Selector<? super E> pSelector) {
        return removeBy(pSelector, true);
    }

    @Override
    public E removeBy(final Selector<? super E> pSelector, boolean notify) {
        return to(mToMultiSelectionRecyclerViewAdapter.removeBy(new Selector<To<E>>() {
            @Override
            public boolean isSelected(To<E> element) {
                return pSelector.isSelected(element.to());
            }
        }, notify));
    }

    @Override
    public E removeFirst(final Selector<? super E> pSelector, boolean notify) {
        return to(mToMultiSelectionRecyclerViewAdapter.removeFirst(new Selector<To<E>>() {
            @Override
            public boolean isSelected(To<E> element) {
                return pSelector.isSelected(element.to());
            }
        }, notify));
    }

    @Override
    public E removeFirst(Selector<? super E> pSelector) {
        return removeFirst(pSelector, true);
    }

    @Override
    public E removeLast(Selector<? super E> pSelector) {
        return removeLast(pSelector, true);
    }

    @Override
    public E removeLast(final Selector<? super E> pSelector, boolean notify) {
        return to(mToMultiSelectionRecyclerViewAdapter.removeLast(new Selector<To<E>>() {
            @Override
            public boolean isSelected(To<E> element) {
                return pSelector.isSelected(element.to());
            }
        }, true));
    }

    @Override
    public void remove(Collection<? extends E> elements, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.remove(convertTo(elements, false), notify);
    }

    @Override
    public void remove(Collection<? extends E> elements) {
        remove(elements, true);
    }

    @Override
    public void remove(E[] elements) {
        mToMultiSelectionRecyclerViewAdapter.remove(convertTo(Arrays.asList(elements), false), true);
    }

    @Override
    public void remove(E[] elements, boolean notify) {

        mToMultiSelectionRecyclerViewAdapter.remove(convertTo(Arrays.asList(elements), false), notify);
    }

    @Override
    public void removeByPositions(Collection<Integer> positions, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.removeByPositions(positions, notify);
    }

    @Override
    public void removeByPositions(Collection<Integer> positions) {
        mToMultiSelectionRecyclerViewAdapter.removeByPositions(positions, true);
    }

    @Override
    public void removeByPositions(int[] positions) {
        mToMultiSelectionRecyclerViewAdapter.removeByPositions(positions, true);
    }

    @Override
    public void removeByPositions(int[] positions, boolean notify) {
        mToMultiSelectionRecyclerViewAdapter.removeByPositions(positions, notify);
    }

    @Override
    public void removeAll() {
        mToMultiSelectionRecyclerViewAdapter.removeAll();
    }

    @Override
    protected boolean canHandleClick(View pView) {
        return mToMultiSelectionRecyclerViewAdapter.canHandleClick(pView);
    }

    @Override
    public boolean isInContentActionMode() {
        return mToMultiSelectionRecyclerViewAdapter.isInContentActionMode();
    }

    @Override
    public List<E> get(final Selector<? super E> pSelector) {
        return to(mToMultiSelectionRecyclerViewAdapter.get(new Selector<T>() {
            @Override
            public boolean isSelected(T element) {
                return pSelector.isSelected(element.to());
            }
        }));
    }

    public interface To<T> {
        T to();
    }

    public static class ToViewHolder<E, T> extends BaseRecyclerViewHolder<T> {
        private final BaseRecyclerViewHolder<E> mTarget;

        public ToViewHolder(BaseRecyclerViewHolder<E> target) {
            super(target.itemView);
            mTarget = target;
        }


        @Override
        protected void bindViewHolder(T element, BaseRecyclerViewAdapter<T> pAdapter, int position) {

        }
    }

    public interface TFactory<E, T> {
        T create(E pE);
    }


    @Override
    public void add(E element) {
        To<T> to = new ToImpl<>(mETTFactory.create(element));
        T t = mETTFactory.create(element);
        mToMultiSelectionRecyclerViewAdapter.add(t);
    }


    private static class ToImpl<E, T> implements To<T> {
        private T mT;

        public ToImpl(T t) {
            mT = t;
        }


        @Override
        public T to() {
            return mT;
        }
    }

    private T get(E el) {
        return el == null ? null : mETTFactory.create(el);
    }

    private OnOperateCallback<T> msOnOperateCallback = new OnOperateCallback<T>() {
        @Override
        public void doBeforeRemove(int pos) {

        }

        @Override
        public void doBeforeSet(int position, T newOne, boolean notify) {

        }

        @Override
        public void doAfterSet(T oldOne, T newOne, boolean notify) {
            mEToArrayMap.put(to(oldOne), newOne);
            ConvertAdapter.super.set(to(oldOne), to(newOne), notify);
        }

        @Override
        public void doAfterRemove(T pETo, boolean notify) {
            mEToArrayMap.remove(to(pETo));
            ConvertAdapter.super.remove(to(pETo), notify);
        }

        @Override
        public void doAfterAdded(int pPosition, T newOne, boolean notify) {
            mEToArrayMap.put(to(newOne), newOne);
            ConvertAdapter.super.add(to(newOne), pPosition, notify);
        }

        @Override
        public void doBeforeAdd(int pos, T pWill, boolean notify) {

        }

        @Override
        public void doBeforeAddAll(int pPosition, List<T> pList) {

        }

        @Override
        public void doAfterAddAll(int pPosition, List<T> pList, boolean notify) {
            Map<E, T> map = new ArrayMap<>(pList.size());
            for (T t : pList) {
                map.put(to(t), t);
            }
            mEToArrayMap.putAll(map);
            ConvertAdapter.super.addAll(revert(pList), pPosition, notify);
        }

        @Override
        public void doAfterRemoveAll(boolean notify) {
            mEToArrayMap.clear();
            ConvertAdapter.super.removeAll(notify);
        }

        @Override
        public void doBeforeRemoveAll(boolean pNotify) {
            mEToArrayMap.clear();
            ConvertAdapter.super.removeAll(pNotify);
        }
    };
    private MultiSelectionOnOperateCallback<T> mMultiSelectionOnOperateCallback = new MultiSelectionOnOperateCallback<T>() {
        @Override
        public void doAfterSetItemSelection(int position, boolean isSelected, boolean pNotify) {
            ConvertAdapter.super.setItemSelection(position, isSelected, pNotify);
        }

        @Override
        public void doBeforeSetItemSelection(int positin, boolean isSelected) {

        }

        @Override
        public void doAfterClearItemSelection(boolean notify) {
            ConvertAdapter.super.clearSelection(notify);
        }

        @Override
        public void doBeforeClearItemSelection(boolean notify) {
            ConvertAdapter.super.clearSelection(notify);
        }
    };
}
