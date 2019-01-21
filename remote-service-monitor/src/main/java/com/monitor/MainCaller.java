package com.monitor;


import static com.monitor.user.util.MonitorServiceUserUtil.prepareServiceWithPreRegisteredClient;
import java.time.LocalDateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.monitor.Exception.MonitorServiceException;
import com.monitor.configuration.MonitorConfiguration;
import com.monitor.interfaces.MonitorService;
import com.monitor.model.Client;
import com.monitor.model.OutageWindow;
import com.monitor.model.Service;

/**
 * An example caller of the MonitorService.
 * @author akshayhiremath
 *
 */
public class MainCaller {

	public static void main(String[] args) {
		
		ApplicationContext ctx = 
		         new AnnotationConfigApplicationContext(MonitorConfiguration.class);
		MonitorService ms = (MonitorService)ctx.getBean("monitorService");
		
		try {
				//1. Start the service with initial configuration in MonitorConfiguration class
				ms.startServiceMonitor();
				
				//Add a new Service to monitor in the set of Services monitored by ServiceMonitor
				
				//Create service, client and register client to the service status notifications
				Service service = prepareServiceWithPreRegisteredClient("localhost",8084,5000,LocalDateTime.of(2019,01,20,13,55,00,00),LocalDateTime.of(2019,01,20,14,10,00,00));
				
				//2. Add service with Pre-Registered client to ServiceMonitor
				ms.addAndMonitorNewService(service);
				
				//3. Register client interest to service currently being monitored by MonitorService.
				//Service object to identify the target service
				Service s = new Service("localhost",8080);
				//Client object with details
				Client c1 = new Client(service,2000,new OutageWindow(LocalDateTime.of(2019,01,20,15,15,00,00),LocalDateTime.of(2019,01,20,15,20,00,00)));
				ms.registerInterestInExistingService(s,c1);
				
				//4. update grace period to 10 seconds.
				ms.updateGracePeriod(10000);
				
				//Let the Caller sleep for some time 
				Thread.sleep(60000);
				
				//5. Stop the MonitorService gracefully
				ms.stopServiceMonitoring();
			
				((AnnotationConfigApplicationContext)ctx).close();
		} catch (MonitorServiceException e) {
			System.err.println(e.getMessage()+"\n"+e.getCause());
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
