package com.nttconsulting.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.nttconsulting.data.Chapter;
import com.nttconsulting.data.Manga;
import com.nttconsulting.data.MangaPage;

public class DownloadTask extends AsyncTask<String, Integer, String> {

    private Activity activity;
	private Chapter chapter;
	private Manga manga;
	private String dir;

    public DownloadTask(Activity context, Manga manga, Chapter chapter) {
        this.activity = context;
        this.chapter = chapter;
        this.manga = manga;
        this.dir = PathHelper.getStorageDir(manga, chapter);
    }

    @Override
    protected String doInBackground(String... sUrl) {
        
    	//chapter url
    	String url = chapter.path;
    	
    	//do it 1000 times
    	//count the number of failed times
    	//if the number of failed is 5 then quit
    	//the format can be:
    	//http://www.mangareader.net/103-55461-4/one-piece/chapter-592.html
    	//or http://www.mangareader.net/last-game/19/3
    	ArrayList<MangaPage> pages_to_download = new ArrayList<MangaPage>();
    	//sometimes it start with page 0
    	int failed_count = 0;
    	for (int i = 1; i < 1000; i ++) {
    		MangaPage page = pageExists(url, i);
    		if (page != null) {
    			pages_to_download.add(page);
    			failed_count = 0;
    		} else {
    			failed_count++;
    		}
    		
    		if (failed_count >= 5)
    			break;
    	}
    	PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
             getClass().getName());
        wl.acquire();

        try {
        	
        	if (makeDir()) {
		    	for(final MangaPage page : pages_to_download) {
		    		activity.runOnUiThread(new Runnable() {
		    			  public void run() {
		    				 Toast.makeText(activity, "downloading the page: " + page.path, 2000).show();
		    			  }
		    		});
		    		performDownload(dir, page);
		    		activity.runOnUiThread(new Runnable() {
		  			  public void run() {
		  				Toast.makeText(activity, "finished the page: " + page.path, 2000).show();
		  			  }
		    		});
		    		
		    	}
        	}
        } finally {
        	wl.release();
        }
    	return "done";
    }
    
    private boolean makeDir() {
		// create the storage dir
    	File file = new File(dir);
    	file.mkdirs();
		return true;
	}
    

	private MangaPage pageExists(String url, int i) {
		// TODO Auto-generated method stub
    	String pattern1 = "([\\w:/\\.]+)(/\\d+-\\d+-)(\\d+)(.+)";
    	//http://www.mangareader.net/11-eyes/6
    	String pattern2 = "([\\w:/\\.]+)(/.*)(/\\d+)(/\\d+)?";
    	//http://www.mangareader.net/1703-54124-1/11-eyes/chapter-1.html
    	if (url.matches(pattern1)) {
    		return new MangaPage(i, url.replaceAll(pattern1, "$1$2" + i + "$4"), pattern1);
    	} else if (url.matches(pattern2)) {
    		return new MangaPage(i, url.replaceAll(pattern2, "$1$2$3/" + i), pattern2);
    	}
		return null;
	}

	private String performDownload(String dir, MangaPage page) {
    	// take CPU lock to prevent CPU from going off if the user 
        // presses the power button during download
		
		String response = HttpHelper.connect(page.path);
		if (response != null) {
			Document doc = Jsoup.parse(response);
			Element element = doc.getElementById("img");
			String url = element.attributes().get("src");
			Log.d("downlod", url);
			if (url != null) {
		        InputStream input = null;
		        OutputStream output = null;
		        HttpURLConnection connection = null;
		        try {
		            connection = (HttpURLConnection) new URL(url).openConnection();
		            connection.connect();
		
		            // expect HTTP 200 OK, so we don't mistakenly save error report 
		            // instead of the file
		            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		                 return "Server returned HTTP " + connection.getResponseCode() 
		                     + " " + connection.getResponseMessage();
		
		            // this will be useful to display download percentage
		            // might be -1: server did not report the length
		            int fileLength = connection.getContentLength();
		
		            // download the file
		            input = connection.getInputStream();
		            output = new FileOutputStream(String.format("%s/%d.jpg", dir, page.position));
		
		            byte data[] = new byte[4096];
		            long total = 0;
		            int count;
		            while ((count = input.read(data)) != -1) {
		                // allow canceling with back button
		                if (isCancelled())
		                    return null;
		                total += count;
		                // publishing the progress....
		                if (fileLength > 0) // only if total length is known
		                    publishProgress((int) (total * 100 / fileLength));
		                output.write(data, 0, count);
		            }
		        } catch (Exception e) {
		            return e.toString();
		        } finally {
		            try {
		                if (output != null)
		                    output.close();
		                if (input != null)
		                    input.close();
		            } 
		            catch (IOException ignored) { }
		
		            if (connection != null)
		                connection.disconnect();
		        }
			}
		}
        return null;
    }
}