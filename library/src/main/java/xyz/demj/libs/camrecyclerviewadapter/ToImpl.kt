package xyz.demj.libs.camrecyclerviewadapter

/**
 * Created by demj on 2016/11/6 0006.
 */

class ToImpl<E>(private val mE: E) : ConvertAdapter.To<E> {

    override fun to(): E {
        return mE
    }
}
