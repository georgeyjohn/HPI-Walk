package hpi.com.hpifitness.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Georgey on 26-01-2017.
 */

public class FileOpener {
    public static void openFile(Context context, File file) {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(fileExt(file));
        newIntent.setDataAndType(Uri.fromFile(file), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Apps found to open this file!", Toast.LENGTH_LONG).show();
        }
    }

    private static String fileExt(File file) {
        String name = file.getName();
        if (name.contains(".")) {
            name = name.substring(name.lastIndexOf("."));
            name = name.replace(".", "");
        }
        return name;
    }
}
