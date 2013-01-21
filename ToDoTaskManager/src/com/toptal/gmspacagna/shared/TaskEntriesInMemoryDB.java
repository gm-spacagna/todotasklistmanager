package com.toptal.gmspacagna.shared;


import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;



/**
 * In-Memory database of entries.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class TaskEntriesInMemoryDB {
	Map<Long, EntryData> entries = new Hashtable<Long, EntryData>();

	private static final TaskEntriesInMemoryDB instance = new TaskEntriesInMemoryDB();
	
	public static TaskEntriesInMemoryDB getInstance() {
		return instance;
	}

	@SuppressWarnings("deprecation")
  public TaskEntriesInMemoryDB() {
	  super();
	  addEntry(new EntryData("Ciao1", new Date(2012, 11, 19), 4));
//	  addEntry(new EntryData("Ciao2", new Date(2012, 7, 20), 2));
  }

	public Collection<EntryData> getEntries() {
		return entries.values();
	}

	public void setEntries(Iterable<EntryData> entries) {
		for (EntryData entry : entries) {
			addEntry(entry);
		}
	}
	
	public void addEntry(EntryData entry) {
		entries.put(entry.getId(), entry);
	}
	
	public void removeEntry(Long id) {
		entries.remove(id);
	}
}
