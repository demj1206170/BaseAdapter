package xyz.demj.library.filechooser;

import java.io.File;
import java.util.List;

/**
 * Created by demj on 2016/10/19.
 */

public interface OnFileSelectedListener {
    public void onSelected(long pChooserTransiton, List<File> pSelectedFiles);

    public void onCanceled(long pChooserTransition);
}
