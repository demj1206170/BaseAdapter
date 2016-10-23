package xyz.demj.baseadapter;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xyz.demj.library.camrecyclerviewadapter.BaseRecyclerViewAdapter;
import xyz.demj.library.camrecyclerviewadapter.BaseRecyclerViewHolder;
import xyz.demj.library.camrecyclerviewadapter.ConvertAdapter;
import xyz.demj.library.camrecyclerviewadapter.Selector;
import xyz.demj.library.filechooser.CoreFileChooser;
import xyz.demj.library.filechooser.FileChooserHelper;
import xyz.demj.library.filechooser.OnFileSelectedListener;
import xyz.demj.library.valueholder.TypedValueHolderActivity;
import xyz.demj.library.valueholder.TypedValueHolderSupportDialogFragment;

public class MainActivity extends AppCompatActivity implements OnFileSelectedListener {

    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;

//    @Override
//    public void onBackPressed() {
//        if (mMyAdapter.isInContentActionMode())
//            mMyAdapter.detachFromContentActionMode();
//        else super.onBackPressed();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  FileChooserHelper.registerFileSelectedListener(this);
//        mRecyclerView = (RecyclerView) findViewById(R.id.list);
//        mMyAdapter = new MyAdapter();
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(mCDConvertAdapter);
//        for (int i = 0; i < 100; i++) {
//            mCDConvertAdapter.add(new C());
//        }
//
//        mMyAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Log.e("dd", "click " + position);
//            }
//        });
//        mMyAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onLongClick(View pView, int position) {
//                mMyAdapter.attachToContentActionMode(position);
//                return true;
//            }
//        });
        //  FileChooserHelper.chooseFileInActivity(this, CoreFileChooser.MODE_SHOW_ALL, true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // FileChooserHelper.unregisterFileSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.del:
//                List<  A> ls=new ArrayList<>(2);
//                ls.add(mMyAdapter.get(0));
//                ls.add(mMyAdapter.get(1));
                mMyAdapter.set(new A.B(1234), 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelected(long pChooserTransiton, List<File> pSelectedFiles) {
        pSelectedFiles.size();
    }

    @Override
    public void onCanceled(long pChooserTransition) {
        int i = (int) pChooserTransition;
    }

    int i = 103;

    public void hh(View view) {
        //  startActivity(TypedValueHolderActivity.getInent(this, TypeValueTest.class, new A.B(i++)));

        MD md=new MD();
        MD.putValue(md,new A(100));
        md.show(getSupportFragmentManager(),"");
    }

    public class C {
    }

    public class D implements ConvertAdapter.To<C> {

        C mC;

        public D(C c) {
            mC = c;
        }

        @Override
        public C to() {
            return mC;
        }
    }

    public BaseRecyclerViewHolder.ViewHolderFactory<C> mViewHolderFactory = new BaseRecyclerViewHolder.ViewHolderFactory<C>() {
        @Override
        public BaseRecyclerViewHolder<C> createViewHolder(ViewGroup parent, int viewType) {
            return new V(new TextView(MainActivity.this));
        }

    };

    static class V extends BaseRecyclerViewHolder<C> {
        TextView mTextView;

        public V(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }

        @Override
        protected void bindViewHolder(C element, BaseRecyclerViewAdapter<C> pAdapter, int position) {
            mTextView.setText("" + position);
        }
    }

    ConvertAdapter<C, D> mCDConvertAdapter = new ConvertAdapter<>(mViewHolderFactory, new ConvertAdapter.TFactory<C, D>() {
        @Override
        public D create(C pC) {
            return new D(pC);
        }
    });

    public static class MD extends TypedValueHolderSupportDialogFragment<A> {
        @Override
        protected Class<A> getValueClass() {
            return A.class;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("" + mTypedValue.mInt)
                    .setMessage("" + mTypedValue.mInt)
                    .create();

        }
    }


}
