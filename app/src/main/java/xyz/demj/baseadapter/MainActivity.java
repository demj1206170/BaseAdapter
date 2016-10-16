package xyz.demj.baseadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import xyz.demj.library.camrecyclerviewadapter.BaseRecyclerViewAdapter;
import xyz.demj.library.camrecyclerviewadapter.Selector;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;

    @Override
    public void onBackPressed() {
        if (mMyAdapter.isInContentActionMode())
            mMyAdapter.detachFromContentActionMode();
        else super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mMyAdapter = new MyAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mMyAdapter);
        for (int i = 0; i < 100; i++) {
            mMyAdapter.add(new A.B(i));
        }

        mMyAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e("dd", "click " + position);
            }
        });
        mMyAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onLongClick(View pView, int position) {
                mMyAdapter.attachToContentActionMode(position);
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.del:
             List<A> a=  mMyAdapter.getSelectedItems();
                a.size();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
