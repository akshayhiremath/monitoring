package com.monitor.core;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import com.monitor.model.Service;

/**
 * Core logic to perform service status check.
 * This class implements Runnable and the logic will be executed 
 * in separate thread for each service in the ServiceRegister
 * in ServiceMonitorConfig.
 * @author akshayhiremath
 *
 */

public class ConnectAndCheck implements Runnable{
	
	/**
	 * Service to monitor
	 */
	private Service service;
	
	/**
	 * Central ServiceMonitor configuration 
	 */
	private ServiceMonitorConfig serviceMonitorConfig;

	public ConnectAndCheck(Service s,ServiceMonitorConfig serviceMonitorConfig) {
		this.service=s;
		this.serviceMonitorConfig=serviceMonitorConfig;
	}
	
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	@Override
	public void run() {
		Service s = getService();
		Socket socket=null;
		if(s!=null) {
			//Whether to continue polling or to stop
			while(s.isContinuePolling()) {
					try {	
							System.out.println("Checking connection to service: "+service);
							//socket initialization creates a socket and connects it to 
							//the address with HOST:PORT
							socket = new Socket(s.getHost(),s.getPort());
							System.out.println("connection to "+service+" successful.");
							//Close socket and connection after check is successful
							socket.close();
							//Wait for polling interval
							Thread.sleep(s.getPollingInterval());
					} catch (IOException e) {
						if(e instanceof ConnectException && e.getMessage().contains("Connection refused")) {
							//Connection refused, indicates service is down
							System.err.println("Failed to connect to service."+s.getHost()+":"+s.getPort());
							//To wait for gracePeriod. If grace Period is shorter than polling interval 
							//then the loop will continue and next attempt will be made earlier.
								try {
									Thread.sleep(serviceMonitorConfig.getGracePeriod());
								} catch (InterruptedException e1) {
									handleThreadInterruption(e1,s);
								}
								try {
									//After GracePeriod check
									socket = new Socket(s.getHost(),s.getPort());
									socket.close();
								} catch (IOException e2) {
									if(e2.getMessage().contains("Connection refused")) {
										System.err.println("After Grace Period: Failed to connect to service."+s.getHost()+":"+s.getPort());
										s.setStatus(false);
									}
								}
						}
					} catch (InterruptedException e) {
						handleThreadInterruption(e,s);
					}
				}
			}	
	}
	
	/**
	 * Handles and reports any abnormal thread interruption
	 * @param e exception thrown during interruption
	 * @param s service for which monitoring flow got interrupted
	 */
	public void handleThreadInterruption(Exception e,Service s) {
		System.err.println("Monitoring flow interuppted for service: "+s+" Error details: "+e.getMessage());
	}

}
