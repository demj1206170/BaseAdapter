package xyz.demj.libs.camrecyclerviewadapter

import android.support.annotation.Keep

/**
 * Created by demj on 2016/10/16.
 */
@Keep
class TreeAdapter<E : TreeAdapter.TreeNode>(pFactory: BaseRecyclerViewHolder.ViewHolderFactory<E>) : ConvertAdapter<E, TreeAdapter.TT<E>>(pFactory, null) {

    interface TreeNode

    class TT<E> : ConvertAdapter.To<E> {

        override fun to(): E? {
            return null
        }
    }
}
