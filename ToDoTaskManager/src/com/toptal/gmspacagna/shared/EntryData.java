package com.toptal.gmspacagna.shared;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gwt.user.client.rpc.IsSerializable;

@XmlRootElement
public class EntryData implements IsSerializable {

	protected Long id;
	protected String description;
	protected Date dueToDate;
	protected Integer priority;
	protected boolean completed;

	public EntryData(String description, Date dueToDate, int priority) {
		this.id = System.currentTimeMillis();
		this.description = description;
		this.dueToDate = dueToDate;
		this.priority = priority;
		this.completed = false;
	}

	public EntryData(EntryData entry) {
		this(entry.getDescription(), entry.getDueToDate(), entry.getPriority());
	}

	public EntryData() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueToDate() {
		return dueToDate;
	}

	public void setDueToDate(Date dueToDate) {
		this.dueToDate = dueToDate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "id: " + id + " , Description: " + description + " , date: " + dueToDate
		    + " , priority : " + priority + " , completed: " + completed;
	}
}
