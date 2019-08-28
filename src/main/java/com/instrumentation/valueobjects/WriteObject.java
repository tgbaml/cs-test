package com.instrumentation.valueobjects;


public class WriteObject {
    private String event;
    private long eventDuration;
    private String type;
    private boolean alert;

    public String getEvent() {
        return event;
    }

    public long getEventDuration() {
        return eventDuration;
    }

    public String getType() {
        return type;
    }

    public Boolean getAlert() {
        return alert;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setEventDuration(long eventDuration) {
        this.eventDuration = eventDuration;
    }

    public void  setType(String type) {
        this.type = type;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }
    
    @Override
    public String toString() {
    	return String.format("Event: %s, duration %d, type: %s, alert: %s" , this.event, this.eventDuration, this.type, this.alert);
    }

}
