package com.toptal.gmspacagna.client;

import java.util.Date;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.toptal.gmspacagna.shared.EntryData;

/**
 * Widget for the creation of new {@code TaskEntry}.
 * 
 * @author Gianmario Spacagna (gmspacagna@gmail.com)
 */
public class NewTaskEntry extends Composite {
	
	Panel widget = new VerticalPanel();
	
	TextBox description = new TextBox();
	TextBox dueToDate = new TextBox();
	TextBox priority = new TextBox();
	Button saveButton = new Button("save");
	Button cancelButton = new Button("cancel");
	
	Panel buttons = new HorizontalPanel();
	
	@SuppressWarnings("deprecation")
  public NewTaskEntry() {
	  super();
	  
	  description.setValue("Task description");
	  dueToDate.setValue(new Date(System.currentTimeMillis()).toLocaleString());
	  priority.setValue("0");
	  
	  buttons.add(saveButton);
	  buttons.add(cancelButton);
	  
	  widget.add(description);
	  widget.add(dueToDate);
	  widget.add(priority);
	  widget.add(buttons);
	  
	  initWidget(widget);
  }

	public String getDescription() {
		return description.getValue();
	}
	
	@SuppressWarnings("deprecation")
  public Date getDueToDate() {
		return new Date(dueToDate.getValue());
	}
	
	public int getPriority() {
		return Integer.valueOf(priority.getValue());
	}
	
	public EntryData getData() {
		return new EntryData(getDescription(), getDueToDate(), getPriority());
	}
}
