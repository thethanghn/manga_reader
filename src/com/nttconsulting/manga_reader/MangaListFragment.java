package com.nttconsulting.manga_reader;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.nttconsulting.manga_reader.R;
import com.nttconsulting.data.Manga;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class MangaListFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";
    
    

    public MangaListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	final MainActivity activity = (MainActivity) getActivity();
        TabHost tabHost = (TabHost) inflater.inflate(R.layout.all_manga, container, false);
        ListView mangaList = (ListView) tabHost.findViewById(R.id.manga_list);
        
        tabHost.setup();
        TabSpec tab1 = tabHost.newTabSpec("tab1");
        tab1.setIndicator(activity.getResources().getString(R.string.main_tab_first));
        tab1.setContent(R.id.manga_list);
        
        TabSpec tab2 = tabHost.newTabSpec("tab2");
        tab2.setIndicator(activity.getResources().getString(R.string.main_tab_second));
        tab2.setContent(R.id.manga_filter);
        
        
        Manga[] mangas = null;
		try {
			mangas = Manga.getMangaList(activity.getApplicationContext());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(activity, "Unable to get data", 2000).show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			String m = e.getMessage();
			Toast.makeText(activity, "Unable to contact to server" + m, 2000).show();
		}
    
        mangaList.setAdapter(new ArrayAdapter<Manga>(activity, R.layout.manga_list_item, mangas));
        
        //setup listener
        mangaList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				ListView lv = ((ListView)arg0); 
				activity.selectedManga = (Manga) lv.getItemAtPosition(position);
				displaySelectedManga();
			}
        	
        });
        
        activity.setTitle(R.string.manga_list);
        
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        return tabHost;
    }
    
    public void displaySelectedManga() {
    	final MainActivity activity = (MainActivity) getActivity();
		MangaDetailFragment fragment = new MangaDetailFragment(activity.selectedManga);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
		ft.replace(R.id.content_frame, fragment).commit();
	}
}