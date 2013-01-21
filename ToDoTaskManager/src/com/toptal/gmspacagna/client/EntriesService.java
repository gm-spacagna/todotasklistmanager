package com.toptal.gmspacagna.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.toptal.gmspacagna.shared.EntryData;

@RemoteServiceRelativePath("entries")
public interface EntriesService extends RemoteService {

	public void saveEntry(EntryData entry);
	
	public void removeEntry(Long entryId);

	public Collection<EntryData> loadEntries();

}
