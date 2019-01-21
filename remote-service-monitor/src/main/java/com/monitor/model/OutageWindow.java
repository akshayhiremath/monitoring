package com.monitor.model;

import java.time.LocalDateTime;

/**
 * @author akshayhiremath
 *
 */
public class OutageWindow {
	
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public OutageWindow(LocalDateTime startTime, LocalDateTime endTime) {

		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * Default constructor
	 */
	public OutageWindow() {
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	
	
}
