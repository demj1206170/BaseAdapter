package xyz.demj.libs.camrecyclerviewadapter

/**
 * Created by demj on 2016/11/6 0006.
 */

class TTT<E>(pFactory: BaseRecyclerViewHolder.ViewHolderFactory<E>)
: ConvertAdapter<E,
        ConvertAdapter.To<E>>(pFactory, TFactory<E, xyz.demj.libs.camrecyclerviewadapter.ConvertAdapter.To<E>> { pE -> ToImpl(pE) })
{

}
