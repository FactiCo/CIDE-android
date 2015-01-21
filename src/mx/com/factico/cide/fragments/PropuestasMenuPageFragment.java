package mx.com.factico.cide.fragments;

import mx.com.factico.cide.PropuestasActivity;
import mx.com.factico.cide.R;
import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.beans.Propuesta.Items;
import mx.com.factico.cide.facebook.FacebookUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
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
		View view = getActivity().getLayoutInflater().inflate(R.layout.item_menu_propuestas, null, false);
		
		View container = view.findViewById(R.id.item_propuestas_menu_vg_container);
		container.setTag(item);
		container.setOnClickListener(PropuestasOnClickListener);
		
		TextView tvTitle = (TextView) view.findViewById(R.id.item_propuestas_menu_tv_title);
		tvTitle.setText(item.getTitle());
		
		ImageView ivUser = (ImageView) view.findViewById(R.id.item_propuestas_menu_iv_user);
		FacebookUtils.loadImageProfileToImageView(ivUser, getResources().getString(R.string.facebook_userid));
		
		TextView tvCurrentVotes = (TextView) view.findViewById(R.id.item_propuestas_menu_tv_currentvotes);
		
		int currentVotes = 0;
		if (item.getVotes() != null && item.getVotes().getFavor() != null 
				&& item.getVotes().getContra() != null 
				&& item.getVotes().getAbtencion() != null)
			currentVotes = item.getVotes().getFavor().getParticipantes().size() +
					item.getVotes().getContra().getParticipantes().size() +
					item.getVotes().getAbtencion().getParticipantes().size();
		tvCurrentVotes.setText(getResources().getString(R.string.propuestas_menu_current_votes, currentVotes));
		
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