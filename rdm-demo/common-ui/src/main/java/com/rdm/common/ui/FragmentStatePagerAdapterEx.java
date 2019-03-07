package com.rdm.common.ui;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

public abstract class FragmentStatePagerAdapterEx extends FragmentStatePagerAdapter
{

	private static final boolean DEBUG = false;

	private Fragment mPrimary;

	public FragmentStatePagerAdapterEx(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Object instantiateItem (ViewGroup container, int position)
	{
		if (DEBUG)
		{
			Log.d(tag(), "instantiateItem " + container + "," + position);
		}

		return super.instantiateItem(container, position);
	}

	@Override
	public Parcelable saveState ()
	{
		if (DEBUG)
		{
			Log.d(tag(), "saveState ");
		}

		return super.saveState();
	}

	@Override
	public void restoreState (Parcelable state, ClassLoader loader)
	{
		if (DEBUG)
		{
			Log.d(tag(), "restoreState ");
		}

		try
		{
			super.restoreState(state, loader);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void destroyItem (ViewGroup container, int position, Object object)
	{
		if (DEBUG)
		{
			Log.v(tag(), "destroyItem position = " + position);
		}

		try
		{
			super.destroyItem(container, position, object);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
		mPrimary = (Fragment) object;
	}

	/**
	 * 这个方法被废弃，并且没有替代方法，完全没有用。有疑惑的联系asherchen
	 */
	@Deprecated
	public void setUserVisibleHint(boolean isVisible) {
		if (mPrimary != null){
			mPrimary.setUserVisibleHint(isVisible);
		}
	}

	private String tag ()
	{
		return getClass().getSimpleName();
	}

}
