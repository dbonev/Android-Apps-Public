package com.bubblelake.maia;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import java.util.List;

public class NewsFeedProviderListViewAdapter extends BaseAdapter implements ListAdapter {
	Greeting greeting;
	NewsFeedResolver resolver;
	List<NewsFeedItem> items;
	
	public NewsFeedProviderListViewAdapter(Greeting greeting, NewsFeedResolver resolver){
		this.resolver = resolver;
		this.greeting = greeting;
		this.items = this.resolver.getFeedItems();
	}
		

	@Override
	public int getCount() {
		return this.items.size();
	}

	@Override
	public Object getItem(int index) {		
		return this.items.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int index, View arg1, ViewGroup container) {		
		NewsFeedItem item = this.items.get(index);
		if (arg1 != null){
			NewsFeedDisplayControl current = (NewsFeedDisplayControl)arg1;
			if (current.getItem() == item){
				return current;
			}
		}
		
		NewsFeedDisplayControl view = get_display_control(container, item);
		view.inflate();
		return view;
	}

	public NewsFeedDisplayControl get_display_control(ViewGroup container,
			NewsFeedItem item) {		
		
		NewsFeedDisplayControl view = new NewsFeedDisplayControl(container.getContext(), item, greeting);		
		return view;
	}

}
