package com.toptal.gmspacagna.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.toptal.gmspacagna.client.EntriesService;
import com.toptal.gmspacagna.shared.EntryData;
import com.toptal.gmspacagna.shared.TaskEntriesInMemoryDB;

/**
 * The server side implementation of the entries service.
 */
public class EntriesServiceImpl extends RemoteServiceServlet implements EntriesService {

  private static final long serialVersionUID = -306410844283472813L;

  TaskEntriesInMemoryDB db = TaskEntriesInMemoryDB.getInstance();
  
  @Override
	public Collection<EntryData> loadEntries() {
  	Collection<EntryData> entries = new ArrayList<EntryData>(db.getEntries());
  	return entries;
	}

	@Override
  public void saveEntry(EntryData entry) {
		db.addEntry(entry);
  }

	@Override
  public void removeEntry(Long entryId) {
		db.removeEntry(entryId);
  }
}
