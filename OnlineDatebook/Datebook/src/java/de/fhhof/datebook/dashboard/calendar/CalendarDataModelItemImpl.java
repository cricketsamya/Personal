/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.dashboard.calendar;

import org.richfaces.model.CalendarDataModelItem;

/**
 *
 * @author Shamim
 */
public class CalendarDataModelItemImpl implements CalendarDataModelItem{
    private Object data;
	private String styleClass;
	private Object toolTip;
	private int day;
	private boolean enabled = true;
	private Boolean isEmpty;


	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.component.CalendarDataModelItem#getData()
	 */
	public Object getData() {
		return data;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.component.CalendarDataModelItem#getStyleClass()
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.component.CalendarDataModelItem#getToolTip()
	 */
	public Object getToolTip() {
		return toolTip;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.component.CalendarDataModelItem#hasToolTip()
	 */
	public boolean hasToolTip() {
		return getToolTip() != null;
	}

	/* (non-Javadoc)
	 * @see org.richfaces.component.CalendarDataModelItem#isEnabled()
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @param styleClass the styleClass to set
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/**
	 * @param toolTip the toolTip to set
	 */
	public void setToolTip(Object toolTip) {
		this.toolTip = toolTip;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getIsEmpty() {
		return isEmpty;
	}

	public void setIsEmpty(Boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

}
