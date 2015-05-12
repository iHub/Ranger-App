package com.ihub.rangerapp.data.service;

import java.util.Map;

import com.ihub.rangerapp.data.sqlite.Schemas.Shift;

public interface ShiftService {

	public Map<String, Object> startShift(
			String station, 
			String ranch, 
			String leader, 
			String noOfMembers,
			String route,
			String mode,
			String weather,
			String lat,
			String lon,
			String purpose);
	
	public Boolean hasOpenShift();
	
	public Shift getOpenShift();
	
	public void endCurrentShift();
	
	public Long getCurrentShiftID();
}