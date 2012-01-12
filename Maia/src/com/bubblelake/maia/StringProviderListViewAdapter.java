package com.bubblelake.maia;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class StringProviderListViewAdapter extends BaseAdapter implements ListAdapter {
	Greeting greeting;
	ArrayList<StringProvider> providers;
	boolean addMode; 
	
	public StringProviderListViewAdapter(Greeting greeting, boolean addMode){
		this.addMode = addMode;
		this.greeting = greeting;
		this.providers = (ArrayList)greeting.getProviders().clone();
		getNewProvidersList();
	}
	
	private void getNewProvidersList() {
		if (this.providers == null){
			this.providers = new ArrayList<StringProvider>();
		}
		addIfPossible(new TimeOfDayGreetingProvider());
		addIfPossible(new SalutationProvider());
		addIfPossible(new CurrentTimeProvider());		
		addIfPossible(new WeatherProvider());
		addIfPossible(new CalendarAppointmentsProvider());
		addIfPossible(new LocalNewsProvider());
		addIfPossible(new GenericRSSFeedProvider(PredefinedNewsConfigureActivity.NEWS_TYPE_TECH));
		addIfPossible(new GenericRSSFeedProvider(PredefinedNewsConfigureActivity.NEWS_TYPE_TOP));
		addIfPossible(new GenericRSSFeedProvider(PredefinedNewsConfigureActivity.NEWS_TYPE_SPORT));
		addIfPossible(new StockQuoteProvider());
		addIfPossible(new DailyHoroscopeProvider());
		addIfPossible(new JokeOfTheDayProvider());
	}
	
	private void addIfPossible(StringProvider newProvider){
		if (canAddNewProvider(newProvider)){
			this.providers.add(newProvider);
		}
	}
	
	private boolean canAddNewProvider(StringProvider provider){
		if (provider.canDuplicate()){
			return true;
		}
		
		boolean result = hasGreetingSuchProvider(provider.getClass());
		return !result;
	}

	private boolean hasGreetingSuchProvider(Class providerClass) {
		for (StringProvider p : greeting.getProviders()){
			if (p.getClass() == providerClass){
				return true;
			}
		}		
		return false;
	}

	@Override
	public int getCount() {
		return this.providers.size();
	}

	@Override
	public Object getItem(int index) {		
		return this.providers.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int index, View arg1, ViewGroup container) {		
		StringProvider provider = this.providers.get(index);
		if (arg1 != null){
			ProviderViewItem current = (ProviderViewItem)arg1;
			if (current.getProvider() == provider){
				return current;
			}
		}
		
		ProviderViewItem view = get_provider_view_item(container, provider);
		view.inflate();
		return view;
	}

	public ProviderViewItem get_provider_view_item(ViewGroup container,
			StringProvider provider) {
		ProviderViewItem view;
		
		if (greeting.getProviders().indexOf(provider) < 0){		
			view = new ProviderViewItemAdd(container.getContext(), provider, greeting);
		} else {
			view = new ProviderViewItem(container.getContext(), provider, greeting);
		}
		return view;
	}

}
