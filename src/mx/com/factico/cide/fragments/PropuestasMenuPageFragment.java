package mx.com.factico.cide.fragments;

import java.io.IOException;

import mx.com.factico.cide.PropuestasActivity;
import mx.com.factico.cide.R;
import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.beans.Propuesta.Items;
import mx.com.factico.cide.parser.URLImageParser;
import mx.com.factico.cide.utils.AssetsUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PropuestasMenuPageFragment extends Fragment {

	/**
	 * Key to insert the background color into the mapping of a Bundle.
	 */
	private static final String PROPUESTA = "propuesta";

	/**
	 * Key to insert the index page into the mapping of a Bundle.
	 */
	private static final String INDEX = "index";
	
	/**
	 * Key to insert the category page into the mapping of a Bundle.
	 */
	private static final String CATEGORY = "category";

	private Propuesta propuesta;
	private int index;
	private String category;

	/**
	 * Instances a new fragment with a background color and an index page.
	 * 
	 * @param propuesta
	 *            list of items
	 * @param index
	 *            index page
	 * @return a new page
	 */
	public static Fragment newInstance(int index, Propuesta propuesta, String category) {

		// Instantiate a new fragment
		PropuestasMenuPageFragment fragment = new PropuestasMenuPageFragment();

		// Save the parameters
		Bundle bundle = new Bundle();
		bundle.putInt(INDEX, index);
		bundle.putSerializable(PROPUESTA, propuesta);
		bundle.putString(CATEGORY, category);
		fragment.setArguments(bundle);
		fragment.setRetainInstance(true);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Load parameters when the initial creation of the fragment is done
		this.propuesta = (Propuesta) ((getArguments() != null) ? getArguments().getSerializable(PROPUESTA) : null);
		this.index = (getArguments() != null) ? getArguments().getInt(INDEX) : -1;
		this.category = (getArguments() != null) ? getArguments().getString(CATEGORY) : "";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_propuestas_menu, container, false);
		
		// Change the info
		LinearLayout vgContainer = (LinearLayout) rootView.findViewById(R.id.fragment_propuestas_vg_container);

		for (Propuesta.Items item : propuesta.getItems()) {
			if (item.getCategory().equals(category)) {
				View view = createItemPropuestasView(item);
				vgContainer.addView(view);
			}
		}
		
		return rootView;
	}
	
	@SuppressLint("InflateParams")
	private View createItemPropuestasView(Propuesta.Items item) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.item_propuestas, null, false);
		view.setTag(item);
		view.setOnClickListener(PropuestasOnClickListener);
		
		TextView tvTitle = (TextView) view.findViewById(R.id.item_propuestas_tv_title);
		TextView tvDescription = (TextView) view.findViewById(R.id.item_propuestas_tv_description);
		
		// String html = "<html><body><p>Propuesta para <strong>juntas</strong> <strong>vecinales<\/strong>, puedes ver un ejemplo en el siguiente video:<\/p><p><iframe src=\"http:\/\/www.youtube.com\/embed\/il7WTHIKvq8\" width=\"100%\" height=\"228\" frameborder=\"0\" allowfullscreen=\"allowfullscreen\"><\/iframe><\/p><\/body><\/html>";
		String html;
		try {
			html = AssetsUtils.getStringFromAssets(getActivity().getBaseContext(), "prueba.html");
			
			URLImageParser p = new URLImageParser(tvDescription, getActivity().getBaseContext());
			Spanned htmlSpan = Html.fromHtml(html, p, null);
			tvDescription.setText(htmlSpan);
			
			tvTitle.setText(item.getTitle());
			//tvDescription.setText(Html.fromHtml(item.getDescription()).toString());
			//tvDescription.setText(Html.fromHtml(html));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return view;
	}
	
	View.OnClickListener PropuestasOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Propuesta.Items item = (Items) v.getTag();
			startPropuestaIntent(item);
		}
	};
	
	private void startPropuestaIntent(Propuesta.Items item) {
		Intent intent = new Intent(getActivity().getBaseContext(), PropuestasActivity.class);
		intent.putExtra(PropuestasActivity.TAG_PROPUESTA, item);
		startActivity(intent);
	}
}