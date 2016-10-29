package xyz.demj.library.valueholder;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by demj on 2016/10/23.
 */

public abstract class TypedValueHolderFragment<E> extends Fragment {

    protected E mTypedValue;
    private long mTypedValueKey = -1;
    protected static final String TYPED_VALUE_KEY = TypedValueHolderFragment.class.getCanonicalName() + "typed_value_key";
    protected static final String TYPED_SAVE_VALUE_KEY = TypedValueHolderFragment.class.getCanonicalName() + "typed_save_value_key";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreValue(savedInstanceState);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //   Log.e(getClass().getSimpleName(), "onrestorefragment");
        restoreValue(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        E e = TypedValueHolder.getAndRemoveValue(mTypedValueKey, getValueClass());
        if (e != null)
            mTypedValue = e;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Log.e(getClass().getSimpleName(), "onsavefragment");
        saveValue(outState);
    }

    private void saveValue(Bundle outState) {
        mTypedValueKey = TypedValueHolder.save(mTypedValue, TYPED_SAVE_VALUE_KEY, getType(), outState);
        outState.putLong(TYPED_VALUE_KEY, mTypedValueKey);
    }

    private void restoreValue(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTypedValueKey = savedInstanceState.getLong(TYPED_VALUE_KEY, -1);
            E e = TypedValueHolder.getAndRemoveValue(mTypedValueKey, getValueClass(), TYPED_SAVE_VALUE_KEY, getType(), savedInstanceState);
            if (e != null)
                mTypedValue = e;
        }
    }

    protected abstract int getType();
    protected abstract Class<E> getValueClass();

    public static <E> void putValue(TypedValueHolderFragment<E> pFragment, E pE) {
        pFragment.mTypedValue = pE;
    }

}
