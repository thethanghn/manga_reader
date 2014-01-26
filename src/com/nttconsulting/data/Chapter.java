package com.nttconsulting.data;

import java.io.IOException;
import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.nttconsulting.helpers.DataHelper;

public class Chapter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String title;
	public String path;
	
	private static final String api = "/mobile/mangas/";
	
	public static Chapter[] getChapterList(Context context, int manga_id) throws JSONException, IOException {
		String jsonResponse = DataHelper.get(context, Config.SERVER_URL + api + manga_id + "/chapters", true, DataHelper.CHAPTER_LIST + manga_id);
		JSONArray json= new JSONArray(jsonResponse);
		
		
		Chapter[] list = new Chapter[json.length()];
		for(int i =0; i < json.length();i++) {
			Chapter m = new Chapter();
			JSONObject o = (JSONObject) json.get(i);
			m.title = o.getString("title");
			m.path = o.getString("path");
			list[i] = m;
		}
		
		return list;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;
	}
}
