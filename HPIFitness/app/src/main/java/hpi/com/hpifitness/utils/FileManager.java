package hpi.com.hpifitness.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Georgey on 26-01-2017.
 */

public class FileManager {
    public static File getSDcard() {
        return Environment.getExternalStorageDirectory();
    }

    public static File getFolder(String folderName) {
        File folder = new File(getSDcard() + "/" + folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public static void sort(File[] filesInDir) {
        Comparator<File> comp = new Comparator<File>() {

            @Override
            public int compare(File f1, File f2) {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    // Directory before non-directory
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    // Non-directory after directory
                    return 1;
                } else {
                    // Alphabetic order otherwise
                    return f1.getName().compareTo(f2.getName());
                }
            }
        };
        Arrays.sort(filesInDir, comp);
    }

    public static boolean deleteFile(String folderName, String fileName) {
        return new File(getFolder(folderName), fileName).delete();
    }

    public static File[] getFilesInFolder(String folderName) {
        return getFolder(folderName).listFiles();
    }

    public static File[] getFilesInFolder(String folderName, FileFilter filter) {
        return getFolder(folderName).listFiles(filter);
    }

    public static void writeToFile(String folderName, String fileName, String data) throws IOException {
        writeToFile(new File(getFolder(folderName), fileName), data);
    }

    public static void writeToFile(File file, String data) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data.getBytes());
        outputStream.close();
    }

    public static String readFromFile(String folderName, String fileName) throws IOException {
        return readFromFile(new File(getFolder(folderName), fileName));
    }

    public static File getFileFromFolder(String folderName, String fileName) {
        return new File(getFolder(folderName), fileName);
    }

    public static boolean isFilePresentInFolder(String folderName, String fileName) {
        return new File(getFolder(folderName), fileName).exists();
    }

    public static String readFromFile(File file) throws IOException {

        String ret = "";

        InputStream inputStream = new FileInputStream(file);

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            ret = stringBuilder.toString();
        }

        return ret;
    }
}
