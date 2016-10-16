package xyz.demj.baseadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xyz.demj.library.camrecyclerviewadapter.BaseRecyclerViewAdapter;
import xyz.demj.library.camrecyclerviewadapter.BaseRecyclerViewHolder;
import xyz.demj.library.camrecyclerviewadapter.ConvertAdapter;

/**
 * Created by demj on 2016/10/15.
 */

public class MyAdapter extends ConvertAdapter<A> {
    public MyAdapter() {
        super(sHolderFactory);
    }

    private static BaseRecyclerViewHolder.ViewHolderFactory<A> sHolderFactory = new BaseRecyclerViewHolder.ViewHolderFactory<A>() {
        @Override
        public BaseRecyclerViewHolder<A> createViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_list, parent, false);
            return new ViewHolder(view);
        }
    };

    public static class ViewHolder extends BaseRecyclerViewHolder<A> {

        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void bindViewHolder(A element, BaseRecyclerViewAdapter<A> pAdapter, int position) {
            mTextView.setText(element.toString());
            itemView.setSelected(pAdapter.isItemSelected(position));
        }


    }
}
