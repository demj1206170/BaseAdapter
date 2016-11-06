package xyz.demj.libs.valueholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by demj on 2016/10/23.
 */
@Keep
public abstract class TypedValueHolderSupportActivity<E> extends AppCompatActivity {

    protected E mTypedValue;

    private long mTypedValueKey = -1;


    protected static final String TYPED_VALUE_KEY = TypedValueHolderSupportActivity.class.getCanonicalName() + "typed_value_key";
    protected static final String TYPED_SAVE_VALUE_KEY = TypedValueHolderSupportActivity.class.getCanonicalName() + "typed_save_value_key";


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
        Log.e(getClass().getSimpleName(), "onrestore");
        restoreValue(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
     //   Log.e(getClass().getSimpleName(), "onrestore");
        restoreValue(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
      //  Log.e(getClass().getSimpleName(), "onsaveinstance");
        saveValue(outState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
       // Log.e(getClass().getSimpleName(), "onsaveinstance");
        saveValue(outState);
    }

    private void saveValue(Bundle outState) {
        mTypedValueKey = TypedValueHolder.save(mTypedValue,TYPED_SAVE_VALUE_KEY,getType(),outState);
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
            E e = TypedValueHolder.getAndRemoveValue(mTypedValueKey, getValueClass(),TYPED_SAVE_VALUE_KEY,getType(),savedInstanceState);
            if (e != null)
                mTypedValue = e;
        }
    }
    /**
     * get value type,which will used to determine call what fucntion when save or get value.
     * @see TypedValueHolder
     * */
    protected  abstract int getType();

    protected abstract Class<E> getValueClass();

    public static <E> Intent getInent(Activity pActivity, Class<? extends TypedValueHolderSupportActivity<E>> pClass, E pE) {
        return TypedValueHolder.getIntent(pActivity, pClass, TYPED_VALUE_KEY, pE);
    }

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


}
