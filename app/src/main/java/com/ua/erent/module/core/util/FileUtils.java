package com.ua.erent.module.core.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by Максим on 10/30/2016.
 */

public final class FileUtils {

    private FileUtils() {
        throw new RuntimeException();
    }

    public static void delete(@NotNull File file, boolean recursively) {

        if(file.exists()) {
            if(recursively) {
                deleteRecursively(file);
            } else {
                file.delete();
            }
        }
    }

    private static void deleteRecursively(File file) {

        if (!file.exists()) return;

        if (file.isFile()) {
            file.delete();
            return;
        }

        for (final File f : file.listFiles()) {

            if (f.isDirectory()) {
                deleteRecursively(file);
            } else {
                f.delete();
            }
        }
    }

}
