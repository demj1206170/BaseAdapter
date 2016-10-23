package xyz.demj.library.valueholder;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.ArrayMap;

/**
 * Created by demj on 2016/10/23.
 */

public class TypedValueHolder {

    private static ArrayMap<Long, Object> sHolder = new ArrayMap<>();

    public static long putValue(Object object) {
        long time = -1;
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
        Object object = sHolder.remove(key);
        if (pEClass.isInstance(object))
            return (E) object;
        return null;
    }

    static Intent getIntent(Activity pActivity, Class<? extends Activity> pClass, String keyStr, Object pE) {
        Intent intent = new Intent(pActivity, pClass);
        long key =  putValue(pE);
        intent.putExtra(keyStr, key);
        return intent;
    }

}
