package xyz.demj.library.camrecyclerviewadapter;

/**
 * Created by demj on 2016/10/16.
 */

public class TreeAdapter<E extends TreeAdapter.TreeNode> extends ConvertAdapter<E,TreeAdapter.TT<E>> {
    public TreeAdapter(BaseRecyclerViewHolder.ViewHolderFactory<E> pFactory) {
        super(pFactory,null);
    }

    public interface TreeNode{

    }

    public static class TT<E>implements ConvertAdapter.To<E>
    {

        @Override
        public E to() {
            return null;
        }
    }
}
