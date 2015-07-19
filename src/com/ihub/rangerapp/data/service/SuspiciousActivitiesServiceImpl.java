package com.ihub.rangerapp.data.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.ihub.rangerapp.RangerApp;
import com.ihub.rangerapp.data.sqlite.Schemas;
import com.ihub.rangerapp.util.DateUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SuspiciousActivitiesServiceImpl extends DatabaseService implements SuspiciousActivitiesService {

	@Override
	public Map<String, Object> save(Integer id, String actionTaken, String extraNotes, String imagePath, String lat, String lon) {
		
		Map<String, Object> result = new HashMap<String, Object>();
	
		SQLiteDatabase db = getWritableDatabase(RangerApp.get());
		
		ShiftService service = new ShiftServiceImpl();
		Long shiftID = service.getCurrentShiftID();
		
 		ContentValues values = new ContentValues();
 		if(id == -1)
 			values.put(Schemas.SHIFT_ID, shiftID);
 		
		values.put(Schemas.SuspiciousActivities.ACTION_TAKEN, actionTaken);
		values.put(Schemas.SuspiciousActivities.EXTRA_NOTES, extraNotes);
		values.put(Schemas.SuspiciousActivities.IMAGE_PATH, imagePath);
		values.put(Schemas.SuspiciousActivities.LAT, lat);
		values.put(Schemas.SuspiciousActivities.LON, lon);
		
		try {
			
			if(id == -1) {
 				db.insert(Schemas.SUSPICIOUS_ACTIVITIES_TABLE, null, values);
 	 			result.put("status", "success");
 			} else {
 				db.update(Schemas.SUSPICIOUS_ACTIVITIES_TABLE, values, BaseColumns._ID + "=" + id, null);
 				result.put("status", "success");
 			}
			
		} catch (Exception e) {
			result.put("status", "error");
			result.put("message", e.getMessage());
		}
			
		return result;
	}

	@Override
	public void sync(Integer id, AsyncHttpResponseHandler handler) {

		String url = UrlUtils.SUSPICIOUS_ACTIVITIES_URL;
		
		Cursor cursor = null;
		
		RequestParams params = new RequestParams();

		try{
        	
        	SQLiteDatabase db = getWritableDatabase(RangerApp.get());
            cursor = db.rawQuery("SELECT * FROM " + Schemas.SUSPICIOUS_ACTIVITIES_TABLE +" WHERE " + BaseColumns._ID +"=?", new String[] {id + ""});
            
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();

                
                params.put("device_record_id", cursor.getInt(0));
				String actionTaken = cursor.getString(1);
				String extraNotes = cursor.getString(2);
				String latitude = cursor.getString(3);
				String longitude = cursor.getString(4);
				String imagePath = cursor.getString(5);
				String dateCreated = cursor.getString(6);
				Integer shiftID = cursor.getInt(7);
				
				
				params.put("lat", latitude);
				params.put("lon", longitude);
				params.put("action_taken", actionTaken);
				params.put("extra_notes", extraNotes);
				
				try {
					File myFile = new File(imagePath);
				    params.put("image", myFile);
				    
				} catch(FileNotFoundException e) {}
				
				                
				ShiftService service = new ShiftServiceImpl();
				params.put("shift_unique_record_id", service.getShiftUniqueRecordID(shiftID));
				
				try {
        			params.put("record_date_created", DateUtil.parse(dateCreated).getTime() + "");
        			params.put("unique_record_id", RangerApp.getUniqueDeviceID() + "-" + DateUtil.parse(dateCreated).getTime());
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
            }
        }finally {
            cursor.close();
        }
		
		AsyncHttpClient client = new AsyncHttpClient();
		
		client.post(url, params, handler);
	}

}
