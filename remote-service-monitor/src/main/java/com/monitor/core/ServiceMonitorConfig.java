package com.monitor.core;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.monitor.model.Service;
/**
 * Central configuration of services and their caller clients.
 * Warning: This is not a perfect singleton, the constructor of
 * the class in not private because I wanted to decouple this dependency.
 * For a Spring container to manage the dependency the constructor need to be
 * accessible from outside.
 * DO NOT INSTANTIATE another instance of this class anywhere in the application.
 * @author akshayhiremath
 *
 */
@Component
@Scope(value ="Singleton")
public class ServiceMonitorConfig {
	@Autowired
	private HashSet<Service> serviceRegister;
	/**
	 * Grace period to retry within before notifying
	 * the client of failure
	 */
	private volatile int gracePeriod=1000;

	public HashSet<Service> getServiceRegister() {
		return serviceRegister;
	}
	public int getGracePeriod() {
		return gracePeriod;
	}
	public synchronized void setGracePeriod(int gracePeriod) {
		this.gracePeriod = gracePeriod;
	}
}
