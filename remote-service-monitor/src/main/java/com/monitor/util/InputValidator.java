package com.monitor.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import com.monitor.Exception.InputValidationException;
import com.monitor.model.Client;
import com.monitor.model.Service;
/**
 * This class provides utilities to validate input passed to the 
 * ServiceMonitor.
 * @author akshayhiremath
 *
 */
public class InputValidator {
	
	final static String VALUE_NOT_VALID = " value is not valid: ";
	final static String SERVICE_PREFIX = "Service ";
	final static String CLIENT_PREFIX = "Client ";
	final static String NEXT_LINE = "\n";
	
	/**
	 * Validates service and pre-registered client
	 * @param service service with pre-registred client
	 * @throws InputValidationException
	 */
	public void validateInput(Service service) throws InputValidationException {
		StringBuilder message=new StringBuilder();
		
		validateService(service, message);
		
		Vector<Client> serviceClients = service.getClients();
		serviceClients.forEach(client -> validateClient(client,message));
		
		if(message.length() > 0) {
			throw new InputValidationException(message.toString()); 
		}
		
	}
	
	/**
	 * Validates a new client to be added to the service.
	 * This is useful while registering a new client to existing service being monitored by ServiceMonitor
	 * Use this validator when you want to validate only client and basics of service.
	 * 
	 * @param service
	 * @param client
	 * @throws InputValidationException
	 */
	public void validateInput(Service service, Client client) throws InputValidationException {
		StringBuilder message=new StringBuilder();
		
		validateService(service, message);
		validateClient(client,message);
		
		if(message.length() > 0) {
			throw new InputValidationException(message.toString()); 
		}
		
	}
	
	/**
	 * Validates service object passed to the ServiceMonitor
	 * @param service service object
	 * @param message message specifying validation error
	 */
	private void validateService(Service service, StringBuilder message) {
		//Service validation
				if(service==null) {
					message.append(SERVICE_PREFIX+VALUE_NOT_VALID+service+NEXT_LINE);
				}else {
					//Host validation
					if(service.getHost()==null) {
						message.append(SERVICE_PREFIX+"Host"+VALUE_NOT_VALID+service.getHost()+NEXT_LINE);
					}else {
						try {
							InetAddress.getByName(service.getHost());
						} catch (UnknownHostException e) {
							message.append(SERVICE_PREFIX+"Host"+VALUE_NOT_VALID+service.getHost()+NEXT_LINE);
						}
					}
					
					//Port validation
					if(service.getPort() < 0 && service.getPort() > 0xFFFF) {
						message.append(SERVICE_PREFIX+"Port"+VALUE_NOT_VALID+service.getPort()+NEXT_LINE);
					}
				}
	}
	
	/**
	 * Validates client object passed to the ServiceMonitor
	 * @param client client object to validate
	 * @param message message specifying validation error
	 */
	private void validateClient(Client client,StringBuilder message) {
		//Client validation
		if(client==null) {
			message.append(CLIENT_PREFIX+VALUE_NOT_VALID+client);
		}else {
			//Polling period validation
			if(client.getPollingInterval() < 1000 && client.getPollingInterval() > Integer.MAX_VALUE) {
				message.append(CLIENT_PREFIX+"Polling Interval"+VALUE_NOT_VALID+client.getPollingInterval()+NEXT_LINE);
			}
			//Outage Window valiation
			if(client.getServiceOutage()==null) {
				message.append(CLIENT_PREFIX+"Service Outage window"+VALUE_NOT_VALID+client.getServiceOutage()+NEXT_LINE);
			}else {
				if(client.getServiceOutage().getStartTime()==null || client.getServiceOutage().getEndTime()==null) {
					message.append(CLIENT_PREFIX+"Start or End time in outage window"+VALUE_NOT_VALID
							+"Start: "+client.getServiceOutage().getStartTime()+",End: "+client.getServiceOutage().getEndTime()+NEXT_LINE);
				}
			}
		}
	}
	
	/**
	 * Validate grace period
	 * @param gracePeriod grace period to wait after failure detection and before notifying the client
	 * @param message message specifying validation error
	 */
	public boolean validateGracePeriod(int gracePeriodValue) {
		return (gracePeriodValue >= 1000 && gracePeriodValue < Integer.MAX_VALUE);
	}

}
