package com.example.sophie.newsappupdate.data;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.sophie.newsappupdate.R;

public class SettingsActivity extends PreferenceActivity {

    private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String updatedValue = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(updatedValue);
                preference.setSummary(index > 0
                        ? listPreference.getEntries()[index]
                        : null
                );
            }
            return true;
        }
    };
    ImageView backArrow;

    private static void changeSummaryValue(Preference preference) {
        preference.setOnPreferenceChangeListener(listener);
        listener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsPreferenceFragment()).commit();
        //find my views
        //backArrow = findViewById(R.id.backArrowSettings);

        //set on click listener
        /** backArrow.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        onBackPressed();
        }
        });*/
    }

    public static class SettingsPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            changeSummaryValue(findPreference("section"));
            changeSummaryValue(findPreference("order"));

            /**SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
             SharedPreferences.Editor editor = sharedPreferences.edit();
             editor.putString("section", );
             editor.commit();*/
        }
    }
}
