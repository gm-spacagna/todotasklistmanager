package com.toptal.gmspacagna.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.toptal.gmspacagna.shared.EntryData;

/**
 * List of ToDoTask entries. It visualises the list of tasks and perform
 * operations on them.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class ToDoList implements EntryPoint {

	Label title = new Label();

	Label errorMessage = new Label();

	Panel panel = new VerticalPanel();
	Panel labels = new HorizontalPanel();
	Panel entriesPanel = new VerticalPanel();

	Label sort = new Label();
	Button sortById = new Button("ID");
	Button sortByDueToDate = new Button("DueTo Date");
	Button sortByPriority = new Button("Priority");

	SortingCriteria sortingCriteria = SortingCriteria.ID;

	Button newEntryButton = new Button("New Task entry");

	PopupPanel popUpPanel = new PopupPanel(true);
	NewTaskEntry newTaskEntry;

	private List<TaskEntry> entries = new ArrayList<TaskEntry>();

	private final EntriesServiceAsync service =
	    GWT.create(EntriesService.class);

	public void onModuleLoad() {
		/* Add pop-up panel for creating a new entry */
		newEntryButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				newTaskEntry = new NewTaskEntry();
				newTaskEntry.saveButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						addEntry(new TaskEntry(newTaskEntry.getData()));
						popUpPanel.hide();
					}
				});

				newTaskEntry.cancelButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						popUpPanel.hide();
					}
				});
				popUpPanel.setWidget(newTaskEntry);
				popUpPanel.showRelativeTo(newEntryButton);
			}
		});

		/* add the sorting functions to the buttons */

		sortById.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setSortingCriteria(SortingCriteria.ID);
			}
		});
		sortByDueToDate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setSortingCriteria(SortingCriteria.DUE_TO_DATE);
			}
		});
		sortByPriority.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setSortingCriteria(SortingCriteria.PRIORITY);
			}
		});

		/* set the labels */

		title.setText("ToDo List Manager");

		sort.setText("Sort by: ");

		labels.add(sort);
		labels.add(sortById);
		labels.add(sortByDueToDate);
		labels.add(sortByPriority);

		/* add widgets to main panel */

		panel.add(title);
		panel.add(errorMessage);
		panel.add(newEntryButton);
		panel.add(labels);
		panel.add(entriesPanel);
		RootPanel.get().add(panel);

		/* set the resource of the REST servlet */
		// ((RestServiceProxy) serviceREST).setResource(resource);

		/* load initial entries from the server and show them */

		// loadFromServer();
		loadFromServer();

	}

	protected void setSortingCriteria(SortingCriteria sortingCriteria) {
		this.sortingCriteria = sortingCriteria;
		showEntries();
	}

	private enum SortingCriteria {
		ID, PRIORITY, DUE_TO_DATE;
	}

	private class EntriesComparator implements Comparator<TaskEntry> {
		SortingCriteria sortingCriteria;

		EntriesComparator(SortingCriteria sortingCriteria) {
			this.sortingCriteria = sortingCriteria;
		}

		@Override
		public int compare(TaskEntry arg0, TaskEntry arg1) {
			switch (sortingCriteria) {
			case ID:
				/* older IDs first, chronological order */
				return (arg0.getData().getId().compareTo(arg1.getData().getId()));
			case PRIORITY:
				/* higher priorities first, reverse order */
				return (arg1.getData().getPriority().compareTo(arg0.getData().getPriority()));
			case DUE_TO_DATE:
				/* later data first, sort by upcoming deadlines */
				return arg0.getData().getDueToDate().compareTo(arg1.getData().getDueToDate());
			default:
				throw new RuntimeException("Sorting criteria not valid");
			}
		}
	}

	private void showEntries() {
		Comparator<TaskEntry> comparator = new EntriesComparator(sortingCriteria);

		Collections.sort(entries, comparator);
		entriesPanel.clear();
		for (TaskEntry entry : entries) {
			entriesPanel.add(entry);
		}
	}

	List<TaskEntry> pendingEntries = new ArrayList<TaskEntry>();

	private void addEntry(TaskEntry entry) {
		pendingEntries.add(entry);
		addHandlers(entry);
		storeEntry(entry);
	}

	/**
	 * Abstract handler of any button connected to operations on the entry,
	 * 
	 * @author Gianmario Spacagna (gmspacagna@gmail.com)
	 */
	private abstract class EntryHandler implements EventHandler {
		TaskEntry entry;

		public EntryHandler(TaskEntry entry) {
			super();
			this.entry = entry;
		}
	}

	/**
	 * Handler for remove entry button click.
	 * 
	 * @author Gianmario Spacagna (gmspacagna@gmail.com)
	 */
	private class RemoveEntryHandler extends EntryHandler implements ClickHandler {

		public RemoveEntryHandler(TaskEntry entry) {
			super(entry);
		}

		@Override
		public void onClick(ClickEvent event) {
			removeEntry(entry);
		}
	}

	/**
	 * Handler for save entry button click.
	 * 
	 * @author Gianmario Spacagna (gmspacagna@gmail.com)
	 */
	private class SaveEntryHandler extends EntryHandler implements ClickHandler {

		public SaveEntryHandler(TaskEntry entry) {
			super(entry);
		}

		@Override
		public void onClick(ClickEvent event) {
			saveEntry(entry);
		}
	}

	/**
	 * Handler for completed checkbox flagging.
	 * 
	 * @author Gianmario Spacagna (gmspacagna@gmail.com)
	 */
	private class CompletedEntryHandler extends EntryHandler
	    implements ValueChangeHandler<Boolean> {

		public CompletedEntryHandler(TaskEntry entry) {
			super(entry);
		}

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			saveEntry(entry);
		}
	}

	private void storeEntry(TaskEntry entry) {
		final EntryData data = entry.getData();
		service.saveEntry(data, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				errorMessage.setText("Error saving to server: " + caught.getMessage());
				entries.removeAll(pendingEntries);
				loadFromServer();
			}

			@Override
			public void onSuccess(Void arg1) {
				errorMessage.setText("Data saved: " + data);
				pendingEntries.clear();
				loadFromServer();
				showEntries();
			}

		});
	}

	private void removeEntry(final TaskEntry entry) {
		service.removeEntry(entry.getData().getId(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				showEntries();
			}

			@Override
			public void onSuccess(Void result) {
				entries.remove(entry);
				showEntries();
			}
		});
	}

	private void saveEntry(TaskEntry entry) {
		pendingEntries.add(entry);
		storeEntry(entry);
	}

	private void loadFromServer() {
		service.loadEntries(new AsyncCallback<Collection<EntryData>>() {

			@Override
			public void onSuccess(Collection<EntryData> result) {
				//errorMessage.setText("");
				entries.clear();
				if (result != null) {
					for (EntryData data : result) {
						TaskEntry entry = new TaskEntry(data);
						addHandlers(entry);
						entries.add(entry);
					}
					showEntries();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				errorMessage.setText("Error loading from server: " + caught.getMessage());
			}
		});
	}

	private void addHandlers(TaskEntry entry) {
		entry.deleteButton.addClickHandler(new RemoveEntryHandler(entry));
		entry.registerSave(new SaveEntryHandler(entry));
		entry.checkBox.addValueChangeHandler(new CompletedEntryHandler(entry));
	}
}
