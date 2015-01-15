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
			if (position == 3)
				ivScroll.setImageResource(R.drawable.ic_about_scroll_4);
			if (position == 4)
				ivScroll.setImageResource(R.drawable.ic_about_scroll_5);
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
		
		@Override
		public void onPageScrollStateChanged(int state) {}
	};
	
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
			return 5;
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
			
			int index = getArguments().getInt(ARG_SECTION_NUMBER);
			if (index == 1) {
				ivPage.setImageResource(R.drawable.justicia_cotidiana_1);
			} if (index == 2) {
				ivPage.setImageResource(R.drawable.justicia_cotidiana_2);
			} if (index == 3) {
				ivPage.setImageResource(R.drawable.justicia_cotidiana_3);
			} if (index == 4) {
				ivPage.setImageResource(R.drawable.justicia_cotidiana_4);
			} if (index == 5) {
				ivPage.setImageResource(R.drawable.justicia_cotidiana_5);
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
