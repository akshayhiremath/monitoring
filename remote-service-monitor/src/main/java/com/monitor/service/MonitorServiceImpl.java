package com.monitor.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.monitor.Exception.InputValidationException;
import com.monitor.Exception.MonitorServiceException;
import com.monitor.core.ConnectAndCheck;
import com.monitor.core.ServiceMonitorConfig;
import com.monitor.interfaces.MonitorService;
import com.monitor.model.Client;
import com.monitor.model.Service;
import com.monitor.util.InputValidator;
/**
 * Implementation of MonitorService.
 * All the configurable time periods are in milliseconds
 * GracePeriod and PollingInterval 
 * @author akshayhiremath
 *
 */
@Component
public class MonitorServiceImpl implements MonitorService{
	
	/**
	 * The monitor service Input validator.
	 * 
	 */
	@Autowired
	private InputValidator inputValidator;
	
	/**
	 * Central configuration store.
	 * It contains 
	 * 1. register of services being monitered,
	 * 2. Grace period applicable to all services
	 * 
	 */
	@Autowired
	private ServiceMonitorConfig serviceMonitorConfig;
	
	
	/**
	 * Register specified client's interest to know the status of the service being monitored by 
	 * ServiceMonitor.
	 * Status of service is checked by attempting a simple TCP
	 * connection to the Host and Port.
	 * Here the Service passed in the argument just need to have correct host and port, other attributes are ignored.
	 * The service instance passed is used to match and find the existing service instance and to register the passed
	 * client to it.
	 * @param service Service to monitor
	 * @param client client interested in knowing the status of the service.
	 * @throws MonitorServiceException if any error occurs during monitoring
	 * 								   the exception message would illustrate the cause
	 */
	public void registerInterestInExistingService(Service service,Client client) throws MonitorServiceException {
		//validate inputs
		try {
			inputValidator.validateInput(service);
		} catch (InputValidationException e) {
			throw new MonitorServiceException(e.getMessage());
		}
		
		HashSet<Service> services = serviceMonitorConfig.getServiceRegister();
		if(services.contains(service)) {
				services.forEach(s -> {if(s.equals(service)) {
								client.setService(s);
								s.registerClient(client);
							}
						});
		}
	}
	
	/**
	 * Add a new Service to the set of services being monitored by by MonitorService
	 * and starts monitoring for it in a separate thread.
	 * Service passed to this method will have client/clients pre-registered to it.
	 * The called has to create Client and Service instances, then set the service instance 
	 * to Client, register each client to the service with service.registerClient() and 
	 * then pass the service instance to this API.
	 * @param service
	 * @throws MonitorServiceException 
	 */
	public void addAndMonitorNewService(Service service) throws MonitorServiceException {
		//validate inputs
		try {
				inputValidator.validateInput(service);
		} catch (InputValidationException e) {
				throw new MonitorServiceException(e.getMessage());
		}
		
		if(serviceMonitorConfig.getServiceRegister().contains(service)) {
			throw new MonitorServiceException("Service is already being monitored. Try to register with API InterestInExistingService(service, client)");
		}
		
		//Add new service to the register of services being monitored by MonitorService
		serviceMonitorConfig.getServiceRegister().add(service);
		//TODO Executor could be used to do better thread management
		//Start monitoring for this new service
		Thread t = new Thread(new ConnectAndCheck(service,serviceMonitorConfig));
		t.start();
		System.out.println("Service "+service+" set for monitoring.");
		
	}

	/**
	 * Initiates the process of monitoring with the Pre-Configured Service and Client configuration.
	 * The initial configuration could be provided in MonitorConfiguration class.
	 * The bean serviceRegister contains the central repository of all the Services that
	 * MonitorService will deal with.
	 * 
	 * Creates one thread for monitoring each service and executes in parallel.
	 * The polling interval is taken as smallest polling interval 
	 * out of the intervals specified by each client/caller interested 
	 * in monitoring the service
	 * @throws MonitorServiceException
	 */
	public void startServiceMonitor() throws MonitorServiceException {
		HashSet<Service> servicesToMonitor = serviceMonitorConfig.getServiceRegister();	
		for(Service s:servicesToMonitor) {
			//TODO Executor could be used to do better thread management
			Thread t = new Thread(new ConnectAndCheck(s,serviceMonitorConfig));
			t.start();
			System.out.println("Service "+s+" set for monitoring.");
		}
	}
	
	/**
	 * Updates grace period before notification
	 * for all services in the central configuration
	 * @param gracePeriod period to wait before notifying in milliseconds
	 */
	public void updateGracePeriod(int gracePeriod) {
		
		if(inputValidator.validateGracePeriod(gracePeriod)) {
			serviceMonitorConfig.setGracePeriod(gracePeriod);
		}
		
	}
	
	/**
	 * Stops all threads monitoring the services registered in ServiceMonitor.
	 * The caller/user of the ServiceMonitor could stop the all the monitoring threads gracefully
	 * with a call to this API.
	 * This sets the polling flags checked by each thread that causes the monitoring thread to stop
	 * at next iteration.
	 */
	public void stopServiceMonitoring() {
		System.out.println("Stopping MonitorService...\nGracefully taking all the monitoring threads down.");
		HashSet<Service> servicesToMonitor = serviceMonitorConfig.getServiceRegister();
		if(servicesToMonitor.size() > 0) {
			servicesToMonitor.forEach(s -> s.setContinuePolling(false));
		}
		System.out.println("Service Monitor Stopped.");
	}
	
}
