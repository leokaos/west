package org.west.utilitarios;

import java.io.File;
import java.io.FileFilter;

public class FileFilterFotos implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".jpg") && pathname.length() < (150 * 1024);
    }
}
