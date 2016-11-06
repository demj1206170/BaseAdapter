package xyz.demj.libs.filechooser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by demj on 2016/10/22.
 */

public final class FileUtil {
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        }
        float fsize = size * 1.0f / 1024;
        if (fsize < 1024)
            return String.format(Locale.CHINA, "%.2fK", fsize);
        fsize = fsize / 1024;
        if (fsize < 1024)
            return String.format(Locale.CHINA, "%.2fM", fsize);
        fsize = fsize / 1024;
        return String.format(Locale.CHINA, "%.2fG", fsize);
    }

    public static String formatFileSize(File pFile) {
        if (pFile.isDirectory())
            return "";
        return formatFileSize(pFile.length());
    }

    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);

    public static String formatFileDate(long date) {
        return sDateFormat.format(date);
    }

    public static String formatFileDate(File pFile) {
        return sDateFormat.format(pFile.lastModified());
    }
}
