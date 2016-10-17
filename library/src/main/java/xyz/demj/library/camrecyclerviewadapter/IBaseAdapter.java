package xyz.demj.library.camrecyclerviewadapter;

import java.util.Collection;
import java.util.List;

/**
 * Created by demj on 2016/10/15.
 */

public interface IBaseAdapter<E> {

    void add(E element);

    void add(E element, int position);

    void add(E element, E beforeWhich);

    void add(E element, boolean notify);

    void add(E element, int position, boolean notify);

    void add(E element, E beforeWhichOne, boolean notify);

    void add(E element, Selector<? super E> pSelector);

    void add(E element, Selector<? super E> pSelector, boolean notify);

    void addAll(Collection<? extends E> elements, int position, boolean notify);

    void addAll(Collection<? extends E> elements, int position);

    void addAll(Collection<? extends E> elements, boolean notify);

    void addAll(Collection<? extends E> elements);

    void addAll(E[] elements, int postion, boolean notify);

    void addAll(E[] elements, int postion);

    void addAll(E[] elements, boolean notify);

    void addAll(E[] elements);

    E remove(int position, boolean notify);

    E remove(int position);

    E remove(E comparator, boolean notify);

    E remove(E comparator);

    E removeBy(Selector<? super E> pSelector);

    E removeBy(Selector<? super E> pSelector, boolean notify);

    E removeFirst(Selector<? super E> pSelector, boolean notify);

    E removeFirst(Selector<? super E> pSelector);

    E removeLast(Selector<? super E> pSelector);

    E removeLast(Selector<? super E> pSelector, boolean notify);

    void removeAll(boolean notofy);

    void remove(Collection<? extends E> elements, boolean notify);

    void remove(Collection<? extends E> elements);

    void remove(E[] elements);

    void remove(E[] elements, boolean notofi);

    void removeByPositions(Collection<Integer> positions, boolean notify);

    void removeByPositions(Collection<Integer> positions);

    void removeByPositions(int[] positions);

    void removeByPositions(int[] positions, boolean notify);

    void removeAll();

    int getItemCount();

    E set(E pNewOne, int position, boolean notify);

    E set(E pNewOne, int position);

    E set(E pOldOne, E pNewOne, boolean notify);

    E set(E pOldOne, E pNewOne);

    E set(E pNewOne, Selector<? super E> pSelector);

    E set(E pNewOne, Selector<? super E> pSelector, boolean notify);

    E setFirst(E pNewOne, Selector<? super E> pSelector, boolean notify);

    E setFirst(E pNewOne, Selector<? super E> pSelector);

    E setLast(E pNewOne, Selector<? super E> pSelector, boolean notify);

    E setLast(E pNewOne, Selector<? super E> pSelector);

    E get(int position);

    public abstract List<E> get(Selector<? super E> pSelector);

    E getFirst(Selector<? super E> pSelector);

    E getLast(Selector<? super E> pSelector);


}
