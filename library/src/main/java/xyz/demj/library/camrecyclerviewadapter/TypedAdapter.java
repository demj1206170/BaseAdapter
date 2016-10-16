package xyz.demj.library.camrecyclerviewadapter;

/**
 * Created by demj on 2016/10/16.
 */

public class TypedAdapter<E> extends ConvertAdapter<TypedAdapter.Type<E>> {

    public TypedAdapter(BaseRecyclerViewHolder.ViewHolderFactory<Type<E>> pFactory) {
        super(pFactory);
    }

    @Override
    public void add(Type<E> element, int position) {
        super.add(element, position);
    }

    public interface Type<E> {

    }
}
