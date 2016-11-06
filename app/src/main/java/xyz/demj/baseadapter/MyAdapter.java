package xyz.demj.baseadapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.demj.baseadapter.databinding.ItemSimpleListBinding;
import xyz.demj.libs.camrecyclerviewadapter.BaseRecyclerViewAdapter;
import xyz.demj.libs.camrecyclerviewadapter.BaseRecyclerViewHolder;
import xyz.demj.libs.camrecyclerviewadapter.ConvertAdapter;

/**
 * Created by demj on 2016/10/15.
 */

public class MyAdapter extends ConvertAdapter<A, EA<A>> {
    public MyAdapter() {
        super(sHolderFactory, new TFactory<A, EA<A>>() {
            @Override
            public EA<A> create(A pA) {
                return new EA<A>(pA);
            }
        });
    }

    private static BaseRecyclerViewHolder.ViewHolderFactory<A> sHolderFactory = new BaseRecyclerViewHolder.ViewHolderFactory<A>() {
        @Override
        public BaseRecyclerViewHolder<A> createViewHolder(BaseRecyclerViewAdapter<A> pAdapter, ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_list, parent, false);
            return new ViewHolder(view, pAdapter);
        }
    };

    @Override
    protected List<A> createNewList() {
        return new ObservableArrayList<>();
    }

    @Override
    protected List<EA<A>> createNewConvertList() {
        return new ObservableArrayList<>();
    }

    public static class ViewHolder extends BaseRecyclerViewHolder<A> {

        TextView mTextView;

        private ItemSimpleListBinding mItemSimpleListBinding;

        public ViewHolder(View itemView, BaseRecyclerViewAdapter<A> pABaseRecyclerViewAdapter) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
            mItemSimpleListBinding = DataBindingUtil.bind(itemView);
            mItemSimpleListBinding.setAdapter(pABaseRecyclerViewAdapter);
        }

        @Override
        public void bindViewHolder(A element, BaseRecyclerViewAdapter<A> pAdapter, int position) {

            //  mItemSimpleListBinding.setAdapter(pAdapter);
            mItemSimpleListBinding.setData(element);

        }
    }

//    @BindingAdapter("setSelected")
//    public static void setSelected(View pView, boolean selected) {
//        pView.setSelected(selected);
//    }
}
