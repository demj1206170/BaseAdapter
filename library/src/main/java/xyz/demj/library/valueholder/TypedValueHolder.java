package xyz.demj.library.valueholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by demj on 2016/10/23.
 */

public class TypedValueHolder {

    public static final int TYPED_PARCELABLE_ARRAYLIST = 0;
    public static final int TYPED_PARCELABLE = 1;
    public static final int TYPED_PARCELABLE_ARRAY = 2;
    public static final int TYPED_SPARSE_PARCELABLE_ARRAY = 3;

    public static final int TYPED_STRING = 6;
    public static final int TYPED_STRING_ARRAYLIST = 7;
    public static final int TYPED_STRING_ARRAY = 8;
    public static final int TYPED_BINDER = 9;
    public static final int TYPED_BUNDLE = 10;

    public static final int TYPED_CHARARRAY = 13;
    public static final int TYPED_CHAR_SEQUENCE = 14;
    public static final int TYPED_CHAR_SEQUENCEARRAY = 15;
    public static final int TYPED_CHAR_SEQUENCE_ARRAYLIST = 16;

    public static final int TYPED_FLOATARRAY = 18;
    public static final int TYPED_INTEGER_ARRAYLIST = 19;

    public static final int TYPED_SHORTARRAY = 21;
    public static final int TYPED_SIZE = 22;
    public static final int TYPED_SIZEF = 23;
    public static final int TYPED_SERIALIZABLE = 24;

    public static final int TYPED_BOOLEANARRAY = 26;

    public static final int TYPED_DOUBLEARRAY = 28;
    public static final int TYPED_INTARRAY = 29;
    public static final int TYPED_LONGARRAY = 30;
    public static final int TYPED_OTHER = 31;
    public static final int TYPED_BYTE_ARRAY = 32;


    private static ArrayMap<Long, Object> sHolder = new ArrayMap<>();

    public static long putValue(Object object) {
        long time = -1;
        if (object == null)
            throw new IllegalStateException("value must not be null");
        synchronized (TypedValueHolder.class) {
            time = System.currentTimeMillis();
            if (sHolder.containsKey(time)) {
                try {
                    TypedValueHolder.class.wait(1);
                } catch (InterruptedException pE) {
                    pE.printStackTrace();
                }
                time = System.currentTimeMillis();
            }
            sHolder.put(time, object);
        }
        return time;
    }

    @SuppressWarnings("unchecked")
    public static <E> E getAndRemoveValue(long key, Class<E> pEClass) {
        int index = sHolder.indexOfKey(key);

        if (index < 0)
            return null;

        Object object = sHolder.valueAt(index);
        if (pEClass.isInstance(object)) {
            sHolder.remove(key);
            return (E) object;
        }

        return null;
    }

    static Intent getIntent(Activity pActivity, Class<? extends Activity> pClass, String keyStr, Object pE) {
        Intent intent = new Intent(pActivity, pClass);
        long key = putValue(pE);
        intent.putExtra(keyStr, key);
        return intent;
    }


    @SuppressWarnings("unchecked")
    public static <E> long save(E pE, String saveKey, int type, Bundle outstate) {
        checkType(type);
        long key = 0;
        try {
            switch (type) {
                case TYPED_PARCELABLE_ARRAYLIST:
                    outstate.putParcelableArrayList(saveKey, (ArrayList<? extends Parcelable>) pE);
                    break;
                case TYPED_PARCELABLE:
                    outstate.putParcelable(saveKey, (Parcelable) pE);
                    break;
                case TYPED_PARCELABLE_ARRAY:
                    outstate.putParcelableArray(saveKey, (Parcelable[]) pE);
                    break;
                case TYPED_SPARSE_PARCELABLE_ARRAY:
                    outstate.putSparseParcelableArray(saveKey, (SparseArray<? extends Parcelable>) pE);
                    break;

                case TYPED_STRING:
                    outstate.putString(saveKey, (String) pE);
                    break;
                case TYPED_STRING_ARRAYLIST:
                    outstate.putStringArrayList(saveKey, (ArrayList<String>) pE);
                    break;
                case TYPED_STRING_ARRAY:
                    outstate.putStringArray(saveKey, (String[]) pE);
                    break;
                case TYPED_BINDER:
                    outstate.putBinder(saveKey, (IBinder) pE);
                    break;
                case TYPED_BUNDLE:
                    outstate.putBundle(saveKey, (Bundle) pE);
                    break;

                case TYPED_BYTE_ARRAY:
                    outstate.putByteArray(saveKey, (byte[]) pE);
                    break;

                case TYPED_CHARARRAY:
                    outstate.putCharArray(saveKey, (char[]) pE);
                    break;
                case TYPED_CHAR_SEQUENCE:
                    outstate.putCharSequence(saveKey, (CharSequence) pE);
                    break;
                case TYPED_CHAR_SEQUENCEARRAY:
                    outstate.putCharSequenceArray(saveKey, (CharSequence[]) pE);
                    break;
                case TYPED_CHAR_SEQUENCE_ARRAYLIST:
                    outstate.putCharSequenceArrayList(saveKey, (ArrayList<CharSequence>) pE);
                    break;

                case TYPED_FLOATARRAY:
                    outstate.putFloatArray(saveKey, (float[]) pE);
                    break;
                case TYPED_INTEGER_ARRAYLIST:
                    outstate.putIntegerArrayList(saveKey, (ArrayList<Integer>) pE);
                    break;

                case TYPED_SHORTARRAY:
                    outstate.putShortArray(saveKey, (short[]) pE);
                    break;
                case TYPED_SIZE:
                    outstate.putSize(saveKey, (Size) pE);
                    break;
                case TYPED_SIZEF:
                    outstate.putSizeF(saveKey, (SizeF) pE);
                    break;
                case TYPED_SERIALIZABLE:
                    outstate.putSerializable(saveKey, (Serializable) pE);
                    break;

                case TYPED_BOOLEANARRAY:
                    outstate.putBooleanArray(saveKey, (boolean[]) pE);
                    break;

                case TYPED_DOUBLEARRAY:
                    outstate.putDoubleArray(saveKey, (double[]) pE);
                    break;
                case TYPED_INTARRAY:
                    outstate.putIntArray(saveKey, (int[]) pE);
                    break;
                case TYPED_LONGARRAY:
                    outstate.putLongArray(saveKey, (long[]) pE);
                    break;
                case TYPED_OTHER:
                    key = putValue(pE);
                    break;
            }
        } catch (Exception e) {
            key = putValue(pE);
            Log.e("TypedValueHolder", e.getMessage() + "\nwill put into static map,\nwhich might lose value when app kill by system when some situtation.");
        }
        return key;
    }

    private static void checkType(int type) {
        if (type < 0 || type > 31)
            throw new IllegalStateException("TypedValueHolder: save type is illegal, is " + type);
    }

    @SuppressWarnings("unchecked")
    public static <E> E getAndRemoveValue(long key, Class<E> pE, String saveKey, int type, Bundle outstate) {
        checkType(type);
        E e = null;
        try {
            switch (type) {
                case TYPED_PARCELABLE_ARRAYLIST:
                    e = (E) outstate.getParcelableArrayList(saveKey);
                    break;
                case TYPED_PARCELABLE:
                    e = (E) outstate.getParcelable(saveKey);
                    break;
                case TYPED_PARCELABLE_ARRAY:
                    e = (E) outstate.getParcelableArray(saveKey);
                    break;
                case TYPED_SPARSE_PARCELABLE_ARRAY:
                    e = (E) outstate.getSparseParcelableArray(saveKey);
                    break;
                case TYPED_STRING:
                    e = (E) outstate.getString(saveKey);
                    break;
                case TYPED_STRING_ARRAYLIST:
                    e = (E) outstate.getStringArrayList(saveKey);
                    break;
                case TYPED_STRING_ARRAY:
                    e = (E) outstate.getStringArray(saveKey);
                    break;
                case TYPED_BINDER:
                    e = (E) outstate.getBinder(saveKey);
                    break;
                case TYPED_BUNDLE:
                    e = (E) outstate.getBundle(saveKey);
                    break;

                case TYPED_BYTE_ARRAY:
                    e = (E) outstate.getByteArray(saveKey);
                    break;

                case TYPED_CHARARRAY:
                    e = (E) outstate.getCharArray(saveKey);
                    break;
                case TYPED_CHAR_SEQUENCE:
                    e = (E) outstate.getCharSequence(saveKey);
                    break;
                case TYPED_CHAR_SEQUENCEARRAY:
                    e = (E) outstate.getCharSequenceArray(saveKey);
                    break;
                case TYPED_CHAR_SEQUENCE_ARRAYLIST:
                    e = (E) outstate.getCharSequenceArrayList(saveKey);
                    break;

                case TYPED_FLOATARRAY:
                    e = (E) outstate.getFloatArray(saveKey);
                    break;
                case TYPED_INTEGER_ARRAYLIST:
                    e = (E) outstate.getIntegerArrayList(saveKey);
                    break;
                case TYPED_SHORTARRAY:
                    e = (E) outstate.getShortArray(saveKey);
                    break;
                case TYPED_SIZE:
                    e = (E) outstate.getSize(saveKey);
                    break;
                case TYPED_SIZEF:
                    e = (E) outstate.getSizeF(saveKey);
                    break;
                case TYPED_SERIALIZABLE:
                    e = (E) outstate.getSerializable(saveKey);
                    break;

                case TYPED_BOOLEANARRAY:
                    e = (E) outstate.getBooleanArray(saveKey);
                    break;

                case TYPED_DOUBLEARRAY:
                    e = (E) outstate.getDoubleArray(saveKey);
                    break;
                case TYPED_INTARRAY:
                    e = (E) outstate.getIntArray(saveKey);
                    break;
                case TYPED_LONGARRAY:
                    e = (E) outstate.getLongArray(saveKey);
                    break;
                case TYPED_OTHER:
                    e = getAndRemoveValue(key, pE);
                    break;
            }
        } catch (ClassCastException pE1) {
            Log.e("TypedVauleHolder", "get exception" + pE1.getMessage() + "\n attempt get value from satatic,which might null when some situtation");
            e = getAndRemoveValue(key, pE);
        }
        return e;
    }

}
