package com.enigmastation.ml.bayes.persistence;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class FileUtil {
    public void findFile(FileCallback callback, FilenameFilter filter) {
        final String pathSep = System.getProperty("path.separator");
        final String list = System.getProperty("java.class.path");
        for (final String path : list.split(pathSep)) {
        }
    }

    public static void main(String... args) {
        new FileUtil().findFile("cassandra\\.yaml", new FileCallback() {
            @Override
            public void found(String filename) {
                try {
                    System.out.println("found " + new File(filename).toURI().toURL().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void findFile(String regex, FileCallback callback) {
        findFile(Pattern.compile(regex), callback);
    }

    static final String pathSep = System.getProperty("path.separator");

    private void findFile(final Pattern pattern, final FileCallback callback) {
        findFile(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        }, callback);
    }

    private void findFile(FilenameFilter filenameFilter, FileCallback callback) {
        String list = System.getProperty("java.class.path");
        for (String path : list.split(pathSep)) {
            //System.out.println("Scanning "+path);
            File file = new File(path);
            if (file.isDirectory()) {
                walkDirectory(file, filenameFilter, callback);
            } else {
                walkIndex(file, filenameFilter, callback);
            }
        }
    }

    private static void walkIndex(File file, FilenameFilter filenameFilter, FileCallback callback) {
        try {
            JarFile jarFile = new JarFile(file);
            Enumeration<JarEntry> entryEnumeration = jarFile.entries();
            while (entryEnumeration.hasMoreElements()) {
                JarEntry element = entryEnumeration.nextElement();
                if (filenameFilter.accept(null, element.getName())) {
                    callback.found(element.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void walkDirectory(File f, FilenameFilter filenameFilter, FileCallback callback) {
        if (f.isFile()) {
            if (filenameFilter.accept(f, f.getName())) {
                callback.found(f.getAbsolutePath());
            }
        } else {
            for (File file : f.listFiles()) {
                walkDirectory(file, filenameFilter, callback);
            }
        }
    }
}
