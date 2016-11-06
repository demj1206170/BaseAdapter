package xyz.demj.libs.filechooser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xyz.demj.libs.R;

public class FileChooserActivity extends AppCompatActivity {


    public static final String CHOOSER_TRANSITION = FileChooserActivity.class.getCanonicalName() + "chooseFileTransition";
    public static final String CHOOSE_MODE = FileChooserActivity.class.getCanonicalName() + "chooseFileMode";
    public static final String CHOOSE_SHOW_MODE = FileChooserActivity.class.getCanonicalName() + "showMode";

    private OnBackPressedCallback mOnBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);
        Intent intent = getIntent();
        long transition = intent.getLongExtra(CHOOSER_TRANSITION, -1);
        int showMode = intent.getIntExtra(CHOOSE_SHOW_MODE, CoreFileChooser.MODE_SHOW_ALL);
        boolean isMultiSelection = intent.getBooleanExtra(CHOOSE_MODE, false);
        if (savedInstanceState == null) {
            FileChooserFragment fileChooserFragment = FileChooserFragment.create(transition, showMode, isMultiSelection);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fileChooserFragment)
                    .show(fileChooserFragment)
                    .commit();
        }
    }

    public void setOnBackPressedCallback(OnBackPressedCallback pOnBackPressedCallback) {
        mOnBackPressedCallback = pOnBackPressedCallback;
    }

    interface OnBackPressedCallback {
        boolean onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedCallback == null || !mOnBackPressedCallback.onBackPressed())
            super.onBackPressed();
    }
}
