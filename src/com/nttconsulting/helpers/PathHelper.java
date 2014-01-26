package com.nttconsulting.helpers;

import com.nttconsulting.data.Chapter;
import com.nttconsulting.data.Manga;

import java.io.File;

import android.os.Environment;

public class PathHelper {
	public static String getStorageDir(Manga manga, Chapter chapter) {
		File sdcard = Environment.getExternalStorageDirectory();
    	String path = sdcard.getAbsolutePath();
    	String dir = String.format("%s/mangavn/%s/%s/", path, manga.name, chapter.title);
		return dir;
	}
}
