package xyz.demj.library.valueholder;

import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by demj on 2016/10/23.
 */

public abstract class TypedValueHolderDialogFragment<E> extends DialogFragment {
    protected E mTypedValue;
    private long mTypedValueKey = -1;
    protected static final String TYPED_VALUE_KEY = TypedValueHolderDialogFragment.class.getCanonicalName() + "typed_value_key";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreValue(savedInstanceState);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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
        saveValue(outState);
    }

    private void saveValue(Bundle outState) {
        mTypedValueKey = TypedValueHolder.putValue(mTypedValue);
        outState.putLong(TYPED_VALUE_KEY, mTypedValueKey);
    }

    private void restoreValue(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTypedValueKey = savedInstanceState.getLong(TYPED_VALUE_KEY, -1);
            E e = TypedValueHolder.getAndRemoveValue(mTypedValueKey, getValueClass());
            if (e != null)
                mTypedValue = e;
        }
    }

    protected abstract Class<E> getValueClass();

    public static <E> void putValue(TypedValueHolderDialogFragment<E> pFragment, E pE) {
        pFragment.mTypedValue = pE;
    }
}
