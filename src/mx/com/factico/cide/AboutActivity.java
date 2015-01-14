package mx.com.factico.cide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class AboutActivity extends ActionBarActivity {
	private ImageView ivScroll;
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		setSupportActionBar();
		initUI();
	}

	public void setSupportActionBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(255);
		setSupportActionBar(mToolbar);
	}

	private void initUI() {
		VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.about_verticlaviewpager);
		verticalViewPager.setAdapter(new DummyAdapter(getSupportFragmentManager()));
		verticalViewPager.setPageMargin(0);
		// verticalViewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_green_dark)));
		//verticalViewPager.setPageTransformer(true, new VerticalPageTransformer());
		
		ivScroll = (ImageView) findViewById(R.id.about_iv_scroll);
		
		verticalViewPager.setOnPageChangeListener(VerticalOnPageChangeListener);
	}

	OnPageChangeListener VerticalOnPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			if (position == 0)
				ivScroll.setImageResource(R.drawable.ic_about_scroll_1);
			if (position == 1)
				ivScroll.setImageResource(R.drawable.ic_about_scroll_2);
			if (position == 2)
				ivScroll.setImageResource(R.drawable.ic_about_scroll_3);
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			/*Point point = ScreenUtils.getScreenSize(getBaseContext());
			
			int headerHeight = (point.y * 3) - getSupportActionBar().getHeight();
            float ratio = (float) Math.min(Math.max(positionOffsetPixels, 0), headerHeight) / headerHeight;
            int newAlpha = (int) (ratio * 255);
            mToolbar.setAlpha(newAlpha);*/
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {}
	};
	
	/*private float getAlphaForView(int position) {
		int diff = 0;
		float minAlpha = 0.4f, maxAlpha = 1.f;
		float alpha = minAlpha; // min alpha
		if (position > screenHeight)
			alpha = minAlpha;
		else if (position + locationImageHeight < screenHeight)
			alpha = maxAlpha;
		else {
			diff = screenHeight - position;
			alpha += ((diff * 1f) / locationImageHeight) * (maxAlpha - minAlpha); // 1f and 0.4f are maximum and min
			// alpha this will return a number betn 0f and 0.6f
		}
		// System.out.println(alpha+" "+screenHeight +" "+locationImageInitialLocation+" "+position+" "+diff);
		return alpha;
	}*/
	
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
			//ivPage.setImageResource(R.drawable.ic_splash);
			
			//LinearLayout vgContainer = (LinearLayout) rootView.findViewById(R.id.fragment_about_vg_container);
			
			int index = getArguments().getInt(ARG_SECTION_NUMBER);
			if (index == 1) {
				//vgContainer.setBackgroundResource(R.drawable.drawable_bgr_gradient_dark);
				ivPage.setImageResource(R.drawable.justicia_cotidiana_1);
			} if (index == 2) {
				//vgContainer.setBackgroundResource(R.drawable.drawable_bgr_gradient_middle);
				ivPage.setImageResource(R.drawable.justicia_cotidiana_2);
			} if (index == 3) {
				//vgContainer.setBackgroundResource(R.drawable.drawable_bgr_gradient_ligth);
				ivPage.setImageResource(R.drawable.justicia_cotidiana_3);
			}
			
			return rootView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.close_green, menu);
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
