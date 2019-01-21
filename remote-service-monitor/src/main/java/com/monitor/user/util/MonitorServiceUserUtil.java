package com.monitor.user.util;

import java.time.LocalDateTime;

import com.monitor.model.Client;
import com.monitor.model.OutageWindow;
import com.monitor.model.Service;

public class MonitorServiceUserUtil {
	
	/**
	 * Initial configuration preparation assistant.
	 * Creates Service and Client based on the provided inputs  
	 * @param host hostname/IP Address of service
	 * @param port port number of service
	 * @param pollingInterval polling interval for client
	 * @param windowStart downtime window start time
	 * @param windowEnd downtime window end time
	 * @return Service with pre-registered client
	 */
	public static Service prepareServiceWithPreRegisteredClient(String host, int port, int pollingInterval, LocalDateTime windowStart, LocalDateTime windowEnd) {
		//Create service
		Service service = new Service();
		//Set host and port
		service.setHost(host);
		service.setPort(port);
		//Create client of the service
		Client c = new Client(service);
		//Set client parameters
		//Polling interval
		c.setPollingInterval(pollingInterval);
		//Outage window
		OutageWindow ow = new OutageWindow();
		//Start time - format YYYY,MM,DD,HH,mm,ss,ns
		ow.setStartTime(windowStart);
		//End time - format YYYY,MM,DD,HH,mm,ss,ns
		ow.setEndTime(windowEnd);
		c.setServiceOutage(ow);
		//Register client to the service
		service.registerClient(c);
		
		return service;
	}
	
}
