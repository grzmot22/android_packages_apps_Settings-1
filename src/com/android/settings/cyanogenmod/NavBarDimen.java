/*
* Copyright (C) 2012 Slimroms
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.settings.cyanogenmod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.util.Helpers;
import com.android.settings.widget.SeekBarPreference;

public class NavBarDimen extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "NavBarDimen";
    private static final String NAVIGATION_BAR_HEIGHT = "navigation_bar_height";
    private static final String NAVIGATION_BAR_HEIGHT_LANDSCAPE = "navigation_bar_height_landscape";
    private static final String NAVIGATION_BAR_WIDTH = "navigation_bar_width";
    private static final String KEY_DIMEN_OPTIONS = "navbar_dimen";

    private boolean mCheckPreferences;

    ListPreference mNavigationBarHeight;
    ListPreference mNavigationBarHeightLandscape;
    ListPreference mNavigationBarWidth;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshSettings();
    }

    public PreferenceScreen refreshSettings() {
    mCheckPreferences = false;
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.navbar_style_dimen_settings);

        prefs = getPreferenceScreen();

        mNavigationBarHeight = (ListPreference) findPreference("navigation_bar_height");
        mNavigationBarHeight.setOnPreferenceChangeListener(this);

        mNavigationBarHeightLandscape = (ListPreference) findPreference("navigation_bar_height_landscape");
        mNavigationBarHeightLandscape.setOnPreferenceChangeListener(this);

        mNavigationBarWidth = (ListPreference) findPreference("navigation_bar_width");
        mNavigationBarWidth.setOnPreferenceChangeListener(this);
        if (!Utils.isPhone(getActivity())) {
            PreferenceCategory dimenCategory = (PreferenceCategory) findPreference(KEY_DIMEN_OPTIONS);
            if (mNavigationBarWidth != null)
                dimenCategory.removePreference(mNavigationBarWidth);
        } else {
            mNavigationBarWidth.setOnPreferenceChangeListener(this);
        }

        setHasOptionsMenu(true);
        mCheckPreferences = true;
        return prefs;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.nav_bar_style_dimen, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_dimen:
                int height = mapChosenDpToPixels(48);
                Settings.System.putInt(getContentResolver(),
                        Settings.System.NAVIGATION_BAR_HEIGHT_LANDSCAPE,
                        height);
                height = mapChosenDpToPixels(48);
                Settings.System.putInt(getContentResolver(),
                        Settings.System.NAVIGATION_BAR_HEIGHT,
                        height);
                height = mapChosenDpToPixels(42);
                Settings.System.putInt(getContentResolver(),
                        Settings.System.NAVIGATION_BAR_WIDTH,
                        height);
                mNavigationBarHeight.setValue("48");
                mNavigationBarHeightLandscape.setValue("48");
                mNavigationBarWidth.setValue("42");

                refreshSettings();
                return true;
             default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
    if (!mCheckPreferences) {
            return false;
        }
        if (preference == mNavigationBarWidth) {
            String newVal = (String) newValue;
            int dp = Integer.parseInt(newVal);
            int width = mapChosenDpToPixels(dp);
            Settings.System.putInt(getContentResolver(), Settings.System.NAVIGATION_BAR_WIDTH,
                    width);
            return true;
        } else if (preference == mNavigationBarHeight) {
            String newVal = (String) newValue;
            int dp = Integer.parseInt(newVal);
            int height = mapChosenDpToPixels(dp);
            Settings.System.putInt(getContentResolver(), Settings.System.NAVIGATION_BAR_HEIGHT,
                    height);
            return true;
        } else if (preference == mNavigationBarHeightLandscape) {
            String newVal = (String) newValue;
            int dp = Integer.parseInt(newVal);
            int height = mapChosenDpToPixels(dp);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.NAVIGATION_BAR_HEIGHT_LANDSCAPE,
                    height);
            return true;
        }
        return false;
    }

    public int mapChosenDpToPixels(int dp) {
        switch (dp) {
            case 56:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_56);
            case 48:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_48);
            case 42:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_42);
            case 36:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_36);
            case 32:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_32);
            case 30:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_30);
            case 24:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_24);
	    case 0:
                return 0;
        }
        return -1;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
