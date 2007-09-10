package org.crank.controller;

public class ControllerBean {

	private boolean showForm = false;
	private boolean showListing = true;
	
    public boolean isShowForm() {
		return showForm;
	}

	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}

	public synchronized String toggleShowForm() {
    	showForm = !showForm;
    	return null;
    }

	public synchronized String toggleShowListing() {
		showListing = !showListing;
    	return null;
    }

	public boolean isShowListing() {
		return showListing;
	}

	public void setShowListing(boolean showListing) {
		this.showListing = showListing;
	}
}
