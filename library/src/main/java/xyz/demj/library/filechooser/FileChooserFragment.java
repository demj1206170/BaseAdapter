package xyz.demj.library.filechooser;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import xyz.demj.library.R;

/**
 * Created by demj on 2016/10/19.
 */

public class FileChooserFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private CoreFileChooser mCoreFileChooser;

    private int showMode = CoreFileChooser.MODE_SHOW_ALL;
    private boolean isMultiSelection = false;
    private long mChooserTransition = -1;

    private static final String sNeededPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String[] sNeededPermissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int PERMISSION_REQUEST_CODE = 503;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        setHasOptionsMenu(true);
        if (activity instanceof FileChooserActivity) {
            ((FileChooserActivity) activity).setOnBackPressedCallback(mOnBackPressedCallback);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_chooser, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.file_list);
        initData(savedInstanceState);
        mCoreFileChooser = CoreFileChooser.create(mRecyclerView, showMode, isMultiSelection, mChooserTransition);
        checkPermissionAndScanFolder();
        return view;
    }

    private void checkPermissionAndScanFolder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), sNeededPermission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(sNeededPermissions, PERMISSION_REQUEST_CODE);
            } else {
                scanFolder();
            }
        } else {
            scanFolder();
        }
    }

    private void scanFolder() {
        mCoreFileChooser.scanFile();
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mChooserTransition = savedInstanceState.getLong(FileChooserActivity.CHOOSER_TRANSITION, -1);
            showMode = savedInstanceState.getInt(FileChooserActivity.CHOOSE_SHOW_MODE, CoreFileChooser.MODE_SHOW_ALL);
            isMultiSelection = savedInstanceState.getBoolean(FileChooserActivity.CHOOSE_MODE, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanFolder();
            } else {
                Toast.makeText(getActivity(), R.string.cannot_scan_folder_by_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCoreFileChooser.saveInstanceState(outState);
    }

    public static FileChooserFragment create(long chooserTransition, int showMode, boolean pIsMultiSelection) {
        FileChooserFragment fileChooserFragment = new FileChooserFragment();
        fileChooserFragment.isMultiSelection = pIsMultiSelection;
        fileChooserFragment.showMode = showMode;
        fileChooserFragment.mChooserTransition = chooserTransition;
        return fileChooserFragment;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mCoreFileChooser.restoreInstanceState(savedInstanceState);
    }

    private FileChooserActivity.OnBackPressedCallback mOnBackPressedCallback = new FileChooserActivity.OnBackPressedCallback() {
        @Override
        public boolean onBackPressed() {
            return FileChooserFragment.this.onBackPressed();
        }
    };

    public boolean onBackPressed() {
        return mCoreFileChooser.onBackPressed();
    }

    private Menu mMenu;
    MenuItem done;
    MenuItem cancel;
    MenuItem revert;
    MenuItem toggleSelectAll;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_file_chooser, menu);
        mMenu = menu;
        done= mMenu.findItem(R.id.done);
        cancel= mMenu.findItem(R.id.cancel);
        revert= mMenu.findItem(R.id.revert_selection);
        toggleSelectAll= mMenu.findItem(R.id.toggle_all_selection);


    }

    private boolean mDismissAfterSelectedOverFinished = true;

    public void setDismissAfterSelectedOverFinished(boolean dismiss) {
        mDismissAfterSelectedOverFinished = dismiss;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == done) {
            doneSelected(true);
        } else if(item==cancel){
            doneSelected(false);
        }else if(item==revert)
        {
            mCoreFileChooser.revertSelection();
        } else if (item == toggleSelectAll) {
            mCoreFileChooser.toggleAllSelection();
        }
        return super.onOptionsItemSelected(item);
    }

    private void doneSelected(boolean isDone) {
        if (isDone)
            mCoreFileChooser.done();
        else mCoreFileChooser.cancel();

        if (mDismissAfterSelectedOverFinished)
            getActivity().finish();
    }

}
