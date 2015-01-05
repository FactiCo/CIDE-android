package mx.com.factico.cide.adapters;

import java.util.List;

import mx.com.factico.cide.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {
	private List<String> data;
	private Context context;

	public SpinnerAdapter(Context context, int resourceId, List<String> data) {
		super(context, resourceId, data);
		this.data = data;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.item_spinner, parent, false);
		TextView label = (TextView) row.findViewById(R.id.item_spinner_tv_text);
		label.setText(data.get(position));

		if (position == 0) { // Special style for drop down header
			label.setTextColor(context.getResources().getColor(R.color.hololigth));
		}

		return row;
	}
}
