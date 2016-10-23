package xyz.demj.library.filechooser;

import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.demj.library.camrecyclerviewadapter.BaseRecyclerViewAdapter;

/**
 * Created by demj on 2016/10/19.
 */

public class CoreFileChooser {

    private final FileShowStrategy mFileShowStrategy;
    private final RecyclerView mFileListView;

    private final FileAdapter mFileAdapter = new FileAdapter();

    private File mCurrentShowFolder = Environment.getExternalStorageDirectory();
    private File mInitRootFolder = mCurrentShowFolder;
    private Subscription mSubscription;
    private final long mChooserTransition;
    private boolean mIsMultiSelection;


    CoreFileChooser(RecyclerView view, FileShowStrategy pFileShowStrategy, long pChooserTransition) {
        mFileShowStrategy = pFileShowStrategy;
        mFileListView = view;
        mChooserTransition = pChooserTransition;
        initUI();
    }


    private void initUI() {
        mFileListView.setLayoutManager(new LinearLayoutManager(mFileListView.getContext()));
        mFileListView.setAdapter(mFileAdapter);
        mFileAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    private BaseRecyclerViewAdapter.OnItemClickListener mOnItemClickListener = new BaseRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onClick(View view, int position) {
            if (mFileAdapter.getSelectedItemCount() <= 0) {
                scanFile(mFileAdapter.get(position));
            }
        }
    };

    public void scanFile() {
        scanFile(mCurrentShowFolder);
    }

    private Comparator<File> mFileComparator = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            boolean lIsFile = o1.isFile();
            boolean rIsFile = o2.isFile();
            if (lIsFile && !rIsFile)
                return 1;
            if (!lIsFile && rIsFile)
                return -1;
            int ret = o1.getName().compareTo(o2.getName());
            return ret != 0 ? ret :
                    Long.valueOf(o1.lastModified()).compareTo(o2.lastModified());
        }
    };

    public final void scanFile(File pFile) {
        internalScanFile(pFile, true);
    }

    private void internalScanFile(File pFile, boolean saveFolderState) {
        if (pFile == null)
            return;
        if (pFile.isFile())
            return;
        if (saveFolderState)
            saveFileViewState(mCurrentShowFolder.getAbsolutePath());
        mCurrentShowFolder = pFile;
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
        mFileAdapter.removeAll();
        mSubscription = Observable.just(pFile)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File pFile) {
                        File[] files = pFile.listFiles();
                        if (files != null) {
                            List<File> list = new LinkedList<File>(Arrays.asList(files));
                            Collections.sort(list, mFileComparator);
                            return Observable.from(list);
                        }
                        return Observable.from(new File[0]);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        restoreFileViewState(mCurrentShowFolder.getAbsolutePath());
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File pFile) {
                        mFileAdapter.add(pFile);
                    }
                });
    }

    public final void scanFile(String path) {
        scanFile(new File(path));
    }


    public static final int MODE_SHOW_FOLDER = 1;
    public static final int MODE_SHOW_FILE = 2;
    public static final int MODE_SHOW_ALL = 3;

    public static CoreFileChooser create(RecyclerView pFileListView, int showMode, boolean isMultionSelection, long pChooserTransition) {
        FileShowStrategy fileShowStrategy = new FileShowStrategy.NoneShowStrategy(null);
        if (showMode == MODE_SHOW_FILE) {
            fileShowStrategy = new FileShowStrategy.ShowFileStrategy(fileShowStrategy);
        } else if (showMode == MODE_SHOW_FOLDER) {
            fileShowStrategy = new FileShowStrategy.ShowFolerStrategy(fileShowStrategy);
        } else {
            fileShowStrategy = new FileShowStrategy.ShowFolerStrategy(new FileShowStrategy.ShowFolerStrategy(fileShowStrategy));
        }
        CoreFileChooser coreFileChooser = new CoreFileChooser(pFileListView, fileShowStrategy, pChooserTransition);
        coreFileChooser.setIsMultiSelection(isMultionSelection);
        return coreFileChooser;
    }

    private static final String STATE_FOLDER_KEY = CoreFileChooser.class.getCanonicalName() + "state_folder_key";
    private static final String STATE_FOLDER_VALUE = CoreFileChooser.class.getCanonicalName() + "state_folder_value";
    private static final String STATE_CURRENT_SHOW_FOLDER_PATH = CoreFileChooser.class.getCanonicalName() + "state_current_show_folder_path";

    void saveInstanceState(Bundle pOutState) {
        String currentShowFolderPath = mCurrentShowFolder.getAbsolutePath();
        saveFileViewState(currentShowFolderPath);
        pOutState.putString(STATE_CURRENT_SHOW_FOLDER_PATH, currentShowFolderPath);
        int size = mFileFolderState.size();
        ArrayList<String> keyList = new ArrayList<>(size);
        ArrayList<SparseArray<Parcelable>> sparseArrayList = new ArrayList<>(size);
        Bundle bundle = new Bundle();
        for (int i = 0; i < size; i++) {
            String key = mFileFolderState.keyAt(i);
            SparseArray<Parcelable> sparseArray = mFileFolderState.valueAt(i);
            if (key != null || sparseArray != null) {
                keyList.add(key);
                bundle.putSparseParcelableArray(key, sparseArray);
            }
        }
        pOutState.putStringArrayList(STATE_FOLDER_KEY, keyList);
        pOutState.putBundle(STATE_FOLDER_VALUE, bundle);
    }

    void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle(STATE_FOLDER_VALUE);
            ArrayList<String> keyList = savedInstanceState.getStringArrayList(STATE_FOLDER_KEY);
            if (bundle != null && keyList != null) {
                for (String key : keyList) {
                    SparseArray<Parcelable> sparseArray = bundle.getSparseParcelableArray(key);
                    if (sparseArray != null) {
                        mFileFolderState.put(key, sparseArray);
                    }
                }
            }
            String currentShowFolderPath = savedInstanceState.getString(STATE_CURRENT_SHOW_FOLDER_PATH);
            if (currentShowFolderPath != null) {
                internalScanFile(new File(currentShowFolderPath), false);
            }
        }
    }

    private ArrayMap<String, SparseArray<Parcelable>> mFileFolderState = new ArrayMap<>();

    private void saveFileViewState(String key) {
        if (key == null)
            return;
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        mFileListView.saveHierarchyState(sparseArray);
        mFileFolderState.put(key, sparseArray);
    }

    private void restoreFileViewState(String key) {
        if (key == null)
            return;
        SparseArray<Parcelable> sparseArray = mFileFolderState.get(key);
        if (sparseArray != null) {
            mFileFolderState.remove(sparseArray);
            mFileListView.restoreHierarchyState(sparseArray);
        }
    }

    private void removeFolderState(String key) {
        if (key != null) {
            mFileFolderState.remove(key);
        }
    }

    public boolean onBackPressed() {
        if (!mCurrentShowFolder.equals(mInitRootFolder)) {
            internalScanFile(mCurrentShowFolder.getParentFile(), false);
            return true;
        }

        return false;
    }

    public void done() {
        FileChooserHelper.notifyFileSelectedResult(mChooserTransition, mFileAdapter.getSelectedItems(), true);
    }

    public void cancel() {
        FileChooserHelper.notifyFileSelectedResult(mChooserTransition, null, false);
    }

    public void setIsMultiSelection(boolean pIsMultiSelection) {
        mIsMultiSelection = pIsMultiSelection;
        mFileAdapter.setIsMultiSelection(mIsMultiSelection);
    }

    public void revertSelection() {
        mFileAdapter.revertSelection();
    }


    public void toggleAllSelection() {

        mFileAdapter.setAllItemSelection(!(mFileAdapter.getSelectedItemCount()>0));
    }
}
