package com.ihub.rangerapp.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihub.rangerapp.R;
import com.ihub.rangerapp.model.IndividualAnimalModel;
import com.ihub.rangerapp.model.Model;
import com.ihub.rangerapp.util.DateUtil;

public class IndividualAnimalAdapter extends AmazingAdapter {

	List<IndividualAnimalModel> models = new ArrayList<IndividualAnimalModel>();
	
	Activity activity;
	
	public IndividualAnimalAdapter(Activity activity) {
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return models.size();
	}

	@Override
	public Object getItem(int position) {
		
		return models.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	protected void onNextPageRequested(int page) {
	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) {
	}

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) {
		
		final IndividualAnimalModel model = (IndividualAnimalModel) this.getItem(position);
		
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.report_row_default, null);
		}
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
		
		try {
			Bitmap myBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(model.getImagePath()), 48, 48);
	    	imageView.setImageBitmap(myBitmap);
		} catch (Exception e) {}
    	
		TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
		nameView.setText(TextUtils.isEmpty(model.getAnimal()) ? "( Undefined )" : model.getAnimal());
		
		TextView dateView = (TextView) convertView.findViewById(R.id.dateView);
		
		Date date = null;
		
		try {
			date = DateUtil.parse(model.getDateCreated());
			dateView.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
		} catch (Exception e) {}
		
		TextView extraNotesView = (TextView) convertView.findViewById(R.id.extraNotesView);
		
		if(!TextUtils.isEmpty(model.getExtraNotes()))
			extraNotesView.setText(model.getExtraNotes());
		
		return convertView;
	}
	
	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
	}

	@Override
	public int getPositionForSection(int section) {
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public void add(Model model) {

		Boolean hasModel = false;
		
		for(Model m : models) {
			if(m.getId() == model.getId()) {
				hasModel = true;
				break;
			}
		}
		
		if(!hasModel)
			models.add((IndividualAnimalModel) model);
	}

	@Override
	public void clear() {
		models.clear();
	}
}