package xyz.demj.library.valueholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * Created by demj on 2016/10/23.
 */

public abstract class TypedValueHolderActivity<E> extends Activity {

    protected E mTypedValue;

    private long mTypedValueKey = -1;
    protected static final String TYPED_VALUE_KEY = TypedValueHolderActivity.class.getCanonicalName() + "typed_value_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreValue(savedInstanceState);
        restoreIntentValue();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        restoreValue(savedInstanceState);
        restoreIntentValue();
    }

    private void restoreIntentValue() {
        Intent intent = getIntent();
        mTypedValueKey = intent.getLongExtra(TYPED_VALUE_KEY, -1);
        E e = TypedValueHolder.getAndRemoveValue(mTypedValueKey, getValueClass());
        if (e != null)
            mTypedValue = e;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreValue(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        restoreValue(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveValue(outState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        saveValue(outState);
    }

    private void saveValue(Bundle outState) {
        mTypedValueKey = TypedValueHolder.putValue(mTypedValue);
        outState.putLong(TYPED_VALUE_KEY, mTypedValueKey);
    }
    @Override
    protected void onResume() {
        super.onResume();
        E e = TypedValueHolder.getAndRemoveValue(mTypedValueKey, getValueClass());
        if (e != null)
            mTypedValue = e;
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

    public static void putValue(Intent pIntent, Object object) {
        long key = TypedValueHolder.putValue(object);
        pIntent.putExtra(TYPED_VALUE_KEY, key);
    }

    public static void setValueAndStartAty(Activity pActivity, Intent pIntent, Object object) {
        putValue(pIntent, object);
        pActivity.startActivity(pIntent);
    }


    public static <E> void setValueAndStartAty(Activity pActivity, Class<? extends TypedValueHolderSupportActivity<E>> pClass, E object) {
        Intent lvIntent = new Intent(pActivity, pClass);
        putValue(lvIntent, object);
        pActivity.startActivity(lvIntent);
    }
    public static <E> Intent getInent(Activity pActivity, Class<? extends TypedValueHolderActivity<E>> pClass, E pE) {
        return TypedValueHolder.getIntent(pActivity, pClass, TYPED_VALUE_KEY, pE);
    }

}
