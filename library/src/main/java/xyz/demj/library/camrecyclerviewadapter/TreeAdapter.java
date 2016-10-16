package xyz.demj.library.camrecyclerviewadapter;

/**
 * Created by demj on 2016/10/16.
 */

public class TreeAdapter<E extends TreeAdapter.TreeNode> extends ConvertAdapter<E> {
    public TreeAdapter(BaseRecyclerViewHolder.ViewHolderFactory<E> pFactory) {
        super(pFactory);
    }

    public interface TreeNode{

    }
}
