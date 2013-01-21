package com.toptal.gmspacagna.restserver;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.toptal.gmspacagna.shared.EntryData;
import com.toptal.gmspacagna.shared.TaskEntriesInMemoryDB;


/**
 * The server side implementation of the entries service.
 */
@Path("/entries")
public class TaskEntriesService {

	TaskEntriesInMemoryDB db = TaskEntriesInMemoryDB.getInstance();

	@POST
	@Path("save")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void saveEntries(List<EntryData> entries) {
		db.setEntries(entries);
	}

	@POST
	@Path("add")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void addEntry(EntryData entry) {
		EntryData entryToAdd = new EntryData(entry);
		db.addEntry(entryToAdd);
	}

	@GET
	@Path("loadFirst")
	@Produces("application/json")
	public EntryData loadFirst() {
		Iterator<EntryData> entries = db.getEntries().iterator();
		return (entries.hasNext()) ? entries.next() : null;
	}

	@GET
	@Path("load")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<EntryData> loadEntries() {
		return db.getEntries();
	}

	// This method is called if HTML is request
	@GET
	@Path("hello")
  @Produces(MediaType.APPLICATION_JSON)
	public String sayHtmlHello() {
		return "Hello Jersey";
	}
	
	@DELETE
	@Path("delete")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void deleteEntry(Long id) {
		db.removeEntry(id);
	}
}
