package com.toptal.gmspacagna.client;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.toptal.gmspacagna.shared.EntryData;

/**
 * Single entry of a ToDoTask list.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class TaskEntry extends Composite {

	final Panel widgetsPanel = new HorizontalPanel();

	final Label idLabel = new Label();
	final Label descriptionLabel = new Label();
	final Label dueToDateLabel = new Label();
	final Label priorityLabel = new Label();
	final CheckBox checkBox = new CheckBox();

	final Button editButton = new Button("edit");
	final Button deleteButton = new Button("delete");
	final Button saveButton = new Button("save");

	final Panel buttons = new HorizontalPanel();
	
	// edit mode boxes:
	final TextBox descriptionBox = new TextBox();
	final TextBox dueToDateBox = new TextBox();
	final TextBox priorityBox = new TextBox();

	private final EntryData data;

	public TaskEntry(final EntryData data) {
		super();
		this.data = data;

		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				data.setCompleted(event.getValue());
			}
		});

		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setEditMode();
			}
		});

		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveChanges();
				setViewMode();
				if (saveHandler != null) {
					saveHandler.onClick(event);
				}
			}
		});
		buttons.add(editButton);
		buttons.add(saveButton);
		buttons.add(deleteButton);
		
		updateLabels();
		
		setViewMode();
		
		initWidget(widgetsPanel);
	}

	public EntryData getData() {
		return data;
	}

	@SuppressWarnings("deprecation")
	private void saveChanges() {
		data.setDescription(descriptionBox.getValue());
		data.setDueToDate (new Date(dueToDateBox.getValue()));
		data.setPriority (Integer.valueOf(priorityBox.getValue()));
		data.setCompleted(checkBox.getValue());
		updateLabels();
	}
	
	@SuppressWarnings("deprecation")
  private void updateLabels() {
		idLabel.setText(String.valueOf("ID: " + data.getId()));
		descriptionLabel.setText("Description: " + data.getDescription());
		dueToDateLabel.setText("DueTo Date: " + data.getDueToDate().toLocaleString());
		priorityLabel.setText("Priority: " + String.valueOf(data.getPriority()));
		checkBox.setValue(data.isCompleted());
	}

	Widget[] viewModeWidgets = new Widget[] { idLabel, descriptionLabel, dueToDateLabel,
	    priorityLabel, checkBox };

	private void setViewMode() {
		setWidgets(viewModeWidgets);
	}

	Widget[] editModeWidgets = new Widget[] { idLabel, descriptionBox, dueToDateBox, priorityBox };

	@SuppressWarnings("deprecation")
  private void setEditMode() {
		descriptionBox.setValue(data.getDescription());
		dueToDateBox.setValue(data.getDueToDate().toLocaleString());
		priorityBox.setValue(String.valueOf(data.getPriority()));
		setWidgets(editModeWidgets);
	}

	private void setWidgets(Widget[] widgets) {
		widgetsPanel.clear();
		for (Widget widget : widgets) {
			widgetsPanel.add(widget);
		}
		widgetsPanel.add(buttons);
	}
	
	private ClickHandler saveHandler;
	
	public void registerSave(ClickHandler handler) {
		saveHandler = handler;
	}
}
