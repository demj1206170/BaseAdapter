package xyz.demj.libs.filechooser;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Keep;

import java.io.File;
import java.util.Collections;
import java.util.List;

import xyz.demj.libs.registerhelper.RegisterHelper;

import static xyz.demj.libs.filechooser.FileChooserActivity.CHOOSER_TRANSITION;
import static xyz.demj.libs.filechooser.FileChooserActivity.CHOOSE_MODE;
import static xyz.demj.libs.filechooser.FileChooserActivity.CHOOSE_SHOW_MODE;

/**
 * Created by demj on 2016/10/19.
 */
@Keep
public class FileChooserHelper {
    private static final class FileSelectResult {
        private List<File> mFileList;
        boolean isDone;
        long chooserTransition;
    }

    private static final RegisterHelper<OnFileSelectedListener, FileSelectResult> sRegisterHelper =
            RegisterHelper.get(new RegisterHelper.RegisterCallbackAdapter<OnFileSelectedListener, FileSelectResult>() {
                @Override
                public boolean isAllCallback() {
                    return false;
                }

                @Override
                public void call(OnFileSelectedListener registerSutff, FileSelectResult pData, int position, int count) {
                    if (pData.isDone)
                        registerSutff.onSelected(pData.chooserTransition, pData.mFileList);
                    else registerSutff.onCanceled(pData.chooserTransition);
                }
            });

    public static long chooseFileInActivity(Activity pCaller, int showMode, boolean multiSelection) {
        long time;
        synchronized (FileChooserHelper.class) {
            time = System.currentTimeMillis();
            try {
                FileChooserHelper.class.wait(1);
            } catch (InterruptedException pE) {
                pE.printStackTrace();
            }
        }

        Intent intent = new Intent(pCaller, FileChooserActivity.class);
        intent.putExtra(CHOOSER_TRANSITION, time);
        intent.putExtra(CHOOSE_MODE, multiSelection);
        intent.putExtra(CHOOSE_SHOW_MODE, showMode);
        pCaller.startActivity(intent);
        return time;
    }


    public static void registerFileSelectedListener(OnFileSelectedListener pOnFileSelectedListener) {
        sRegisterHelper.reigster(pOnFileSelectedListener);
    }

    public static void unregisterFileSelectedListener(OnFileSelectedListener pOnFileSelectedListener) {
        sRegisterHelper.unregister(pOnFileSelectedListener);
    }

    static void notifyFileSelectedResult(long pChooserTransition, List<File> pSelectedFiles, boolean isDone) {
        FileSelectResult fileSelectResult = new FileSelectResult();
        fileSelectResult.isDone = isDone;
        fileSelectResult.mFileList = Collections.unmodifiableList(pSelectedFiles == null ? Collections.<File>emptyList() : pSelectedFiles);
        fileSelectResult.chooserTransition = pChooserTransition;
        sRegisterHelper.notifyRegister(fileSelectResult);
    }
}
