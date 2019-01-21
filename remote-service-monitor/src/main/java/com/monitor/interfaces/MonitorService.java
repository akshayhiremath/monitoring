package com.monitor.interfaces;

import com.monitor.Exception.MonitorServiceException;
import com.monitor.model.Client;
import com.monitor.model.Service;
/**
 * Monitor service is used to monitor status of different services.
 * It provides APIs to add services to be monitored,
 * start, stop service monitoring and register interest of client 
 * to the existing service being monitored.
 * @author akshayhiremath
 *
 */
public interface MonitorService {
	
	/**
	 * Initiates the process of monitoring. 
	 * Creates one thread for monitoring each service in parallel.
	 * The polling interval is taken as smallest polling interval 
	 * out of the intervals specified by each client/caller interested 
	 * in monitoring the service
	 * @throws MonitorServiceException
	 */
	public void startServiceMonitor() throws MonitorServiceException;
	
	/**
	 * Stops all threads monitoring the services registered in ServiceMonitor
	 */
	public void stopServiceMonitoring();
	
	/**
	 * Register interest to know the status of the service.
	 * Status of service is checked by attempting a simple TCP
	 * connection to the Host and Port
	 * @param service Service to monitor
	 * @param client client interested in knowing the status of the service.
	 * @throws MonitorServiceException if any error occurs during monitoring
	 * 								   the exception message would illustrate the cause
	 */
	public void registerInterestInExistingService(Service service,Client client) throws MonitorServiceException;
	
	/**
	 * Add a new Service to the set of services being monitored by by MonitorService
	 * and starts monitoring for it in a separate thread
	 * @param service
	 * @throws MonitorServiceException 
	 */
	public void addAndMonitorNewService(Service service) throws MonitorServiceException;
	
	/**
	 * Updates grace period before notification
	 * for all services in the central configuration
	 * @param gracePeriod period to wait before notifying in milliseconds
	 */
	public void updateGracePeriod(int gracePeriod);
	
	
	
}
