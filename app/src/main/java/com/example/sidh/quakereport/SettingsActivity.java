package com.example.sidh.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sidh on 2/14/2017.
 */
public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
    }
    public static class InformationPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener
    {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String value=newValue.toString();
            if(preference instanceof ListPreference)
            {
                ListPreference listPreference=(ListPreference) preference;
                int prefernceIndex=listPreference.findIndexOfValue(value);
                if(prefernceIndex>=0)
                {
                    CharSequence[] labels=listPreference.getEntries();
                    preference.setSummary(labels[prefernceIndex]);
                }
            }
            else
                preference.setSummary(value);
            return true;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_main);
            Preference minMagnitudePreference=findPreference(getString(R.string.setting_min_mag_key));
            bindPreferenceSummaryToValue(minMagnitudePreference);
            Preference maxMagnitudePrefernce=findPreference(getString(R.string.setting_max_mag_key));
            bindPreferenceSummaryToValue(maxMagnitudePrefernce);
            Preference orderByPreference=findPreference(getString(R.string.setting_order_by_key));
            bindPreferenceSummaryToValue(orderByPreference);
            Preference limitPreference=findPreference(getString(R.string.setting_limit_key));
            bindPreferenceSummaryToValue(limitPreference);
            Preference startprefernce=findPreference(getString(R.string.setting_start_time_key));
            bindPreferenceSummaryToValue(startprefernce);
            Preference endprefernce=findPreference(getString(R.string.setting_end_time_key));
            bindPreferenceSummaryToValue(endprefernce);

        }
        private void bindPreferenceSummaryToValue(Preference preference)
        {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString=sharedPreferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,preferenceString);
        }
    }
}
