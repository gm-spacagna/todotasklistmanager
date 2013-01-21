package com.toptal.gmspacagna.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.toptal.gmspacagna.shared.EntryData;

public interface EntriesServiceAsync {

	public void saveEntry(EntryData entry, AsyncCallback<Void> callback);
	
	public void removeEntry(Long entryId, AsyncCallback<Void> callback);

	public void loadEntries(AsyncCallback<Collection<EntryData>> callback);

}
