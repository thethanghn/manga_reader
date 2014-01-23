package com.nttconsulting.data;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.nttconsulting.helpers.DataHelper;
import com.nttconsulting.helpers.HttpHelper;

public class Manga {
	private static final String api = "/mobile/mangas/";
	public String title;
	public String name;
	public String path;
	public static Manga[] getMangaList(Context context) throws JSONException, IOException {
		String jsonResponse = DataHelper.get(context, Config.SERVER_URL + api, true, DataHelper.MANGA_LIST);
		JSONArray json= new JSONArray(jsonResponse);
		
		
		Manga[] mangas = new Manga[json.length()];
		for(int i =0; i < json.length();i++) {
			Manga m = new Manga();
			JSONObject o = (JSONObject) json.get(i);
			m.name = o.getString("name");
			m.title = o.getString("title");
			m.path = o.getString("path");
			mangas[i] = m;
		}
		
		return mangas;
	}
}
