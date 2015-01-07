package mx.com.factico.cide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class AboutActivity extends ActionBarActivity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		setSupportActionBar();
		initUI();
	}

	public void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
		setSupportActionBar(mToolbar);
	}

	private void initUI() {
		VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);
		verticalViewPager.setAdapter(new DummyAdapter(getSupportFragmentManager()));
		verticalViewPager.setPageMargin(0);
		// verticalViewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_green_dark)));
		//verticalViewPager.setPageTransformer(true, new VerticalPageTransformer());
	}

	public class DummyAdapter extends FragmentPagerAdapter {

		public DummyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "";
			case 1:
				return "";
			case 2:
				return "";
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_about, container, false);
			ImageView ivPage = (ImageView) rootView.findViewById(R.id.fragment_about_iv_page);
			ivPage.setImageResource(R.drawable.ic_splash);
			
			LinearLayout vgContainer = (LinearLayout) rootView.findViewById(R.id.fragment_about_vg_container);
			
			int index = getArguments().getInt(ARG_SECTION_NUMBER);
			if (index == 1)
				vgContainer.setBackgroundResource(R.drawable.drawable_bgr_gradient_dark);
			if (index == 2)
				vgContainer.setBackgroundResource(R.drawable.drawable_bgr_gradient_middle);
			if (index == 3)
				vgContainer.setBackgroundResource(R.drawable.drawable_bgr_gradient_ligth);
			
			return rootView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.close_white, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_close) {
			finish();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
