package mx.com.factico.cide.adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PropuestasPagerAdapter extends FragmentStatePagerAdapter {
	private List<Fragment> listFragments;
	
	public PropuestasPagerAdapter(FragmentManager fm) {
		super(fm);
		this.listFragments = new ArrayList<Fragment>();
	}

	/**
	 * Add a new fragment in the list.
	 * 
	 * @param fragment a new fragment
	 */
	public void addFragment(Fragment fragment) {
		this.listFragments.add(fragment);
	}

	@Override
	public Fragment getItem(int position) {
		return this.listFragments.get(position);
	}

	@Override
	public int getCount() {
		return this.listFragments.size();
	}
}
