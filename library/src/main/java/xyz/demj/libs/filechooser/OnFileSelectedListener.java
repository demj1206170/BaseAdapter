package xyz.demj.libs.filechooser;

import android.support.annotation.Keep;

import java.io.File;
import java.util.List;

/**
 * Created by demj on 2016/10/19.
 */
@Keep
public interface OnFileSelectedListener {
    public void onSelected(long pChooserTransiton, List<File> pSelectedFiles);

    public void onCanceled(long pChooserTransition);
}
