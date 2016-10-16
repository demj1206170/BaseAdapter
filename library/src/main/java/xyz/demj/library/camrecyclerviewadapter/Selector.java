package xyz.demj.library.camrecyclerviewadapter;

/**
 * Created by demj on 2016/10/15.
 */

public interface Selector<E> {
    boolean isSelected(E element);
}
