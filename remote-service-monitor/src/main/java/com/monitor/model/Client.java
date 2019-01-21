package com.monitor.model;

import com.monitor.interfaces.ServiceListener;

/**
 * This class implements Client/caller of the monitoring service.
 * Client specifies pollingInterval at what frequency he wants to poll
 * a service and service outage a time window during which client doesn't
 *  want notifications.
 *  The client extends ServiceListener which provides a notification 
 *  method if there is any message for client.
 * @author akshayhiremath
 *
 */
public class Client extends ServiceListener{
	
	/**
	 * Id to identify the client.
	 * At the moment, this id doesn't hold any significance other than 
	 * distinguishing messages from client.
	 * Id will be set by service when the client will be registered to get service status
	 */
	private int id;
	/**
	 * interval deciding polling frequency
	 */
	private int pollingInterval;
	/**
	 * Service outage window within which client doesnt want notification
	 */
	private OutageWindow serviceOutage;
	
	/**
	 * Default client constructor
	 */
	public Client() {
		
	}
	
	/**
	 * Constructs client with service it has subscribed to
	 * @param service
	 */
	public Client(Service service) {
		this.service = service;
	}
	
	/**
	 * Constructs Client with given polling interval and service outage window
	 * And also binds the service to the service passed
	 * @param service service to which this client wants to listen
	 * @param pollingInterval polling interval preferred by this client
	 * @param serviceOutage service outage window within which client doesnt want notification
	 */
	public Client(Service service, int pollingInterval, OutageWindow serviceOutage) {
		this.pollingInterval = pollingInterval;
		this.serviceOutage = serviceOutage;
		this.service= service;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPollingInterval() {
		return pollingInterval;
	}
	public void setPollingInterval(int pollingInterval) {
		this.pollingInterval = pollingInterval;
	}
	public OutageWindow getServiceOutage() {
		return serviceOutage;
	}
	public void setServiceOutage(OutageWindow serviceOutage) {
		this.serviceOutage = serviceOutage;
	}
	
	
	
	@Override
	public void update() {	
		System.out.println("\nClient "+id+": Got it. Service: "+service+" is down.");
	}
	
	

}
