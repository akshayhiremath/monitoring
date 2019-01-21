package com.monitor.configuration;

import static com.monitor.user.util.MonitorServiceUserUtil.prepareServiceWithPreRegisteredClient;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.monitor.core.ServiceMonitorConfig;
import com.monitor.interfaces.MonitorService;
import com.monitor.model.Service;
import com.monitor.service.MonitorServiceImpl;
import com.monitor.util.InputValidator;

/**
 * Initial Spring application configuration for the Monitor Service application
 * Dependency declarations
 * @author akshayhiremath
 *
 */
@Configuration
public class MonitorConfiguration {
	/**
	 * Service Register bean. 
	 * This maintains a central set of all services 
	 * that will be monitored by Monitor Service.
	 * The values initiated in this method will be 
	 * the initial set of services for application to start.
	 * @return set of Service objects. 
	 */
	@Bean
	public HashSet<Service> serviceRegister(){		
		HashSet<Service> register = new HashSet<>();
		register.add(prepareServiceWithPreRegisteredClient("localhost",8080,3000,LocalDateTime.of(2019,01,20,13,55,00,00),LocalDateTime.of(2019,01,20,20,55,00,00)));
		register.add(prepareServiceWithPreRegisteredClient("localhost",8081,4000,LocalDateTime.of(2019,01,20,13,55,00,00),LocalDateTime.of(2019,01,20,20,55,00,00)));
		register.add(prepareServiceWithPreRegisteredClient("localhost",8082,2000,LocalDateTime.of(2019,01,20,13,55,00,00),LocalDateTime.of(2019,01,20,20,55,00,00)));
		register.add(prepareServiceWithPreRegisteredClient("localhost",8083,5000,LocalDateTime.of(2019,01,20,13,55,00,00),LocalDateTime.of(2019,01,20,20,55,00,00)));
		return register;
	}
	
	@Bean
	public ServiceMonitorConfig serviceMonitorConfig() {

		ServiceMonitorConfig serviceMonitorConfig = new ServiceMonitorConfig();
		
		return serviceMonitorConfig;
		
	}
	
	@Bean
	public MonitorService monitorService() {
		return new MonitorServiceImpl();
	}
	
	/**
	 * Input validator bean.
	 * This provides input validation APIs to Monitor service.
	 * @return 
	 */
	@Bean
	public InputValidator inputValidator() {
		InputValidator inputValidator = new InputValidator();
		return inputValidator;
	}
	
	
}

