package xyz.demj.libs.camrecyclerviewadapter;

import android.support.annotation.Keep;
import android.view.ViewGroup;

/**
 * Created by demj on 2016/10/17.
 */
@Keep
public class CAdapter<E, T extends ConvertAdapter.To<E>> extends MultiSelectionRecyclerViewAdapter<E> {
    private MultiSelectionRecyclerViewAdapter<T> mAdapter;

    public CAdapter(BaseRecyclerViewHolder.ViewHolderFactory<E> pFactory) {
        super(pFactory);
        mAdapter = new MultiSelectionRecyclerViewAdapter<>(new BaseRecyclerViewHolder.ViewHolderFactory<T>() {
            @Override
            public BaseRecyclerViewHolder<T> createViewHolder(BaseRecyclerViewAdapter<T> pAdapter, ViewGroup parent, int viewType) {
                BaseRecyclerViewHolder<E> viewholder = mViewHolderFactory.createViewHolder(CAdapter.this, parent, viewType);
                viewholder.mAdapter = CAdapter.this;
                return new ToViewHolder<>(viewholder);
            }
        });
    }

    static class ToViewHolder<E, T extends ConvertAdapter.To<E>> extends BaseRecyclerViewHolder<T> {
        private final BaseRecyclerViewHolder<E> mTarget;

        public ToViewHolder(BaseRecyclerViewHolder<E> target) {
            super(target.itemView);
            mTarget = target;
        }

        @Override
        protected void bindViewHolder(T element, BaseRecyclerViewAdapter<T> pAdapter, int position) {

        }
    }
}
