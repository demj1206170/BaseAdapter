package xyz.demj.libs.camrecyclerviewadapter;

import android.support.annotation.Keep;

/**
 * Created by demj on 2016/10/15.
 */
@Keep
public interface Selector<E> {
    boolean isSelected(E element);
}
