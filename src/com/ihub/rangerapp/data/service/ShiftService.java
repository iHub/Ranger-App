package com.ihub.rangerapp.data.service;

import java.util.Map;

import com.ihub.rangerapp.data.sqlite.Schemas.Shift;
import com.loopj.android.http.AsyncHttpResponseHandler;

public interface ShiftService {

	public Map<String, Object> startShift(
			String station, 
			String ranch, 
			String leader, 
			String noOfMembers,
			String route,
			String mode,
			String weather,
			String waypoint,
			String purpose);
	
	public Boolean hasOpenShift();
	
	public Shift getOpenShift();
	
	public void endCurrentShift(String waypoint);
	
	public Long getCurrentShiftID();
	
	public void syncShift(Integer shiftID, AsyncHttpResponseHandler handler);
	
	public String getShiftUniqueRecordID(Integer shiftID);
}