/**
 * 
 */
package com.monitor.interfaces;

import com.monitor.model.Service;

/**
 * This class represents parent observer
 * @author akshayhiremath
 *
 */
public abstract class ServiceListener {
	
	/**
	 * service being observed by the clients.
	 * The subclasses will listen to the status change of service 
	 * and will implement the actions on these changes in update 
	 * method. 
	 */
	protected Service service;
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	
	/**
	 * Abstract method to be implemented by each listener subclass.
	 * Whenever service status changes this method will be called.
	 */
	public abstract void update();

}
