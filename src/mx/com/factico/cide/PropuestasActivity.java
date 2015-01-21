package mx.com.factico.cide;

import mx.com.factico.cide.adapters.PropuestasPagerAdapter;
import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.fragments.PropuestasCommentsPageFragment;
import mx.com.factico.cide.fragments.PropuestasVotesPageFragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class PropuestasActivity extends ActionBarActivity {
	public static final String TAG_CLASS = PropuestasActivity.class.getName();

	public static final String TAG_PROPUESTA = "propuesta";

	private Propuesta.Items propuesta = null;

	private TextView mTitle;

	private PropuestasPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;

	private TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_propuestas);

		setSupportActionBar();

		initUI();
	}

	private void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
		mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
		setSupportActionBar(mToolbar);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void setTabs() {
		mTabHost = (TabHost) findViewById(R.id.propuestas_menu_tabhost);
		mTabHost.setup();
		
		mTitle.setText(propuesta.getCategory());
		
		setupTab(new TextView(this), 0, "Vota");
		setupTab(new TextView(this), 1, "Comenta");
		
		changeImageDrawableToTab(0);
		
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				int position = Integer.parseInt(tabId);
				mViewPager.setCurrentItem(position, true);
				
				changeImageDrawableToTab(position);
			}
		});
		
		mViewPager = (ViewPager) findViewById(R.id.propuestas_menu_pager);
		
		// Defining a listener for pageChange
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                
                mTabHost.setCurrentTab(position);
                
                changeImageDrawableToTab(position);
            }
        };
		
        // Setting the pageChange listener to the viewPager
        mViewPager.setOnPageChangeListener(pageChangeListener);
        // mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setOffscreenPageLimit(2);
        
        // Creating an instance of PagerAdapter
		mPagerAdapter = new PropuestasPagerAdapter(getSupportFragmentManager());
		
		mPagerAdapter.addFragment(PropuestasVotesPageFragment.newInstance(0, propuesta));
		mPagerAdapter.addFragment(PropuestasCommentsPageFragment.newInstance(1, propuesta));
		
		// Setting the PagerAdapter object to the viewPager object
		mViewPager.setAdapter(mPagerAdapter);
	}
	
	private void initUI() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			propuesta = (Propuesta.Items) bundle.getSerializable(TAG_PROPUESTA);
			
			mTitle.setText(propuesta.getCategory());
			
			setTabs();
		}
	}

	private void setupTab(final View view, final int index, String tag) {
		View tabview = createTabView(mTabHost.getContext(), index);
		
		if (tabview != null) {
			TabSpec setContent = mTabHost.newTabSpec(String.valueOf(index)).setIndicator(tabview).setContent(new TabContentFactory() {
				@Override
				public View createTabContent(String tag) {
					return view;
				}
			});
			mTabHost.addTab(setContent);
		}
	}
	
	@SuppressLint("InflateParams")
	private View createTabView(Context context, int index) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_tabhost, null);
		ImageView ivIcon = (ImageView) view.findViewById(R.id.item_tabhost_iv_logo);
		setImageResourceToTabDefault(ivIcon, index);
		
		return view;
	}
	
	private void changeImageDrawableToTab(int index) {
		TabWidget mTabWidget = mTabHost.getTabWidget();
		
		for (int i = 0; i < mTabWidget.getChildCount(); i++) {
			View view = mTabWidget.getChildTabViewAt(i);
			ImageView ivIcon = (ImageView) view.findViewById(R.id.item_tabhost_iv_logo);
			
			if (index == i) {
				setImageResourceToTabSelected(ivIcon, index);
			} else {
				setImageResourceToTabDefault(ivIcon, i);
			}
		}
	}
	
	private void setImageResourceToTabDefault(ImageView ivIcon, int index) {
		switch (index) {
		case 0:
			ivIcon.setImageResource(R.drawable.tab_propuestas_vote_off);
			break;
		case 1:
			ivIcon.setImageResource(R.drawable.tab_propuestas_comments_off);
			break;

		default:
			break;
		}
	}
	
	private void setImageResourceToTabSelected(ImageView ivIcon, int index) {
		switch (index) {
		case 0:
			ivIcon.setImageResource(R.drawable.tab_propuestas_vote_on);
			break;
		case 1:
			ivIcon.setImageResource(R.drawable.tab_propuestas_comments_on);
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.home) {
			finish();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
