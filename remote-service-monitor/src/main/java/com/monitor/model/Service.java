/**
 * 
 */
package com.monitor.model;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Vector;

/**
 * This class implements Service.
 * Service represents any service hosted on 
 * a domain or IP address and listening to socket 
 * bound to a particular TCP port.
 * 
 * @author akshayhiremath
 *
 */
public class Service {
	/**
	 * Hostname or IP address of the service
	 */
	private String host;
	/**
	 * TCP port on which service is listening
	 */
	private int port;
	/**
	 * Status of the service true means UP and false means DOWN
	 */
	private boolean status=true;
	
	/**
	 * Polling interval to check status of the service.
	 */
	private volatile int pollingInterval;
	/**
	 * Do we want to continue polling?
	 */
	private boolean continuePolling=true;
	
	private Vector<Client> clients = new Vector<>();
	
	private int lastClientIdTracker = 0;
	
	public Service() {
		
	}
	/**
	 * Creates service with host and port
	 * @param host domain name/ip address of service
	 * @param port listening port
	 */
	public Service(String host, int port) {
		this.host = host;
		this.port = port;
		this.pollingInterval = findSmallestPollingInterval();
	}
	
	/**
	 * Returns a collection of clients interested in status of this service 
	 * @return a collection of clients
	 */
	public Vector<Client> getClients() {
		return clients;
	}

	/**
	 * Returns host of the service
	 * @return domain name/IP address of the service 
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * Sets host of the service
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPollingInterval() {
		return pollingInterval;
	}
	public synchronized void setPollingInterval(int smallestPollingInterval) {
		this.pollingInterval = smallestPollingInterval;
	}
	public boolean isContinuePolling() {
		return continuePolling;
	}
	public void setContinuePolling(boolean continuePolling) {
		this.continuePolling = continuePolling;
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
		notifyAllClients();
	}
	
	public int getLastClientIdTracker() {
		return lastClientIdTracker;
	}
	private void setLastClientIdTracker(int lastClientIdTracker) {
		this.lastClientIdTracker = lastClientIdTracker;
	}
	/**
	 * Notifies all clients that service is down
	 */
	private void notifyAllClients() {
		
		if(!status) {
			this.clients.forEach(c -> {
				//Check outage window, 
				//if current time is in window 
				//then don't notify the caller
				if(!isOutageWindowOn(c)){
					c.update();
				}
			});
		}
		
	}
	
	/**
	 * Registers a client in service's subscribers/callers list
	 * @param client
	 */
	public void registerClient(Client client) {
		
		//If the polling interval of the new client is smaller than  
		//existing service's polling interval then that will become 
		//new polling interval of the service
		if((client.getPollingInterval() >= 1000 && client.getPollingInterval() < this.getPollingInterval())
				|| this.clients.size() == 0) {
			this.setPollingInterval(client.getPollingInterval());
		}
		//Set client id
		client.setId(this.getLastClientIdTracker());
		this.setLastClientIdTracker(this.getLastClientIdTracker()+1); 
		//Add client to the list of clients
		this.clients.add(client);
	}
	/**
	 * Checks if the known down time/maintenance of this service is in progress
	 * @param c client of the service
	 * @return if current time is in outage window
	 */
	public static boolean isOutageWindowOn(Client c) {
		LocalDateTime timeNow = LocalDateTime.now();
		OutageWindow serviceOutage = c.getServiceOutage();
		  return !timeNow.isBefore(serviceOutage.getStartTime()) && !timeNow.isAfter(serviceOutage.getEndTime());
	}
	
	/**
	 * Finds smallest polling interval out of the clients/callers interested in the service
	 * @param clientList list of registered clients/callers
	 * @return the smallest polling interval value 
	 */
	private int findSmallestPollingInterval(){
		int smallestPollingInterval=1000;
		Optional<Integer> opMin= Optional.empty();
		if(this.clients.size() > 0) {
			//If there is just one client, this could be a first client
			//so set this interval. e.g. if first client starts with interval of 3000ms
			//then we shouldn't find the minimum by comparing with 1000
			smallestPollingInterval=this.clients.get(0).getPollingInterval();
			opMin = this.clients.stream().map(c -> { 
				//Stamp clients with ids
				c.setId(lastClientIdTracker);lastClientIdTracker++;
				//take polling interval value to find min
				return c.getPollingInterval();
			}).min(Integer::compare);
				
		}
			
		if(opMin.isPresent()) {
			smallestPollingInterval = opMin.get();
		}
		
		return smallestPollingInterval;
	}
	
	@Override
	public int hashCode() {
		return this.port;
	}
	
	@Override
	public boolean equals(Object s) {	
		Service service = (Service)s;
		return service.getHost().equalsIgnoreCase(this.getHost()) && service.getPort()==this.getPort();
	}
	
	@Override
	public String toString() {
		return this.getHost()+":"+this.getPort();
	}

}
