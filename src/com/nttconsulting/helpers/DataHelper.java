package com.nttconsulting.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class DataHelper {
	public static final String MANGA_LIST = "manga-list";
	public static String get(Context context, String url, boolean fromCache, String cacheFile) throws IOException {
		String response = getFromFile(context, cacheFile);
		if (response == null || response.length() == 0) {
			response = HttpHelper.connect(url);
			saveToFile(context, cacheFile, response);
		}
		
		return response;
	}

	private static void saveToFile(Context context, String url, String response) throws IOException {
		FileOutputStream fos = null;
		
		fos = context.openFileOutput(url, Context.MODE_PRIVATE);
		fos.write(response.getBytes());
		fos.close();
	}

	private static String getFromFile(Context context, String url) throws IOException {
		// TODO Auto-generated method stub
		String response=  null;
		FileInputStream fis = null;
		
		try {
			fis = context.openFileInput(url);
			response = HttpHelper.convertStreamToString(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("cache", e.getMessage());
		}
		finally {
			if (fis != null)
				fis.close();
		}
		return response;
	}
	
	

}
