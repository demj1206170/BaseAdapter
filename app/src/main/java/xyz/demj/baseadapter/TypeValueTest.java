package xyz.demj.baseadapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xyz.demj.library.valueholder.TypedValueHolderActivity;
import xyz.demj.library.valueholder.TypedValueHolderFragment;

/**
 * Created by demj on 2016/10/23.
 */

public class TypeValueTest extends TypedValueHolderActivity<A> {
    @Override
    protected Class<A> getValueClass() {
        return A.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hhh);
        TextView textView = (TextView) findViewById(R.id.dd);
        textView.setText("" + mTypedValue.mInt);
        MyValuHoldrFrament myValuHoldrFrament = new MyValuHoldrFrament();
        TypedValueHolderFragment.putValue(myValuHoldrFrament, new A(mTypedValue.mInt * 2));
        getFragmentManager().beginTransaction().replace(R.id.cc,myValuHoldrFrament, "ssss")
                .show(myValuHoldrFrament)
                .commit();
    }

    public static class MyValuHoldrFrament extends TypedValueHolderFragment<A> {
        @Override
        protected Class<A> getValueClass() {
            return A.class;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            TextView view = new TextView(getActivity());
            view.setText("" + mTypedValue.mInt);
            return view;
        }
    }

}
