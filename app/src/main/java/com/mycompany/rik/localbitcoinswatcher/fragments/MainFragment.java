package com.mycompany.rik.localbitcoinswatcher.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mycompany.rik.localbitcoinswatcher.MainActivity;
import com.mycompany.rik.localbitcoinswatcher.R;
import com.mycompany.rik.localbitcoinswatcher.localbitcoinsapi.LocalbitcoinsApiConfig;
import com.mycompany.rik.localbitcoinswatcher.localbitcoinsapi.LocalbitcoinsApiListener;
import com.mycompany.rik.localbitcoinswatcher.localbitcoinsapi.LocalbitcoinsApiRequest;
import com.mycompany.rik.localbitcoinswatcher.localbitcoinsapi.LocalbitcoinsApiRequestTask;

import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Rik on 6-1-2016.
 */
public class MainFragment extends Fragment {

    SeekBar thresholdSlider;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public MainFragment(){ }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setUpSlider(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void setUpSlider(View rootView){
        thresholdSlider = (SeekBar)rootView.findViewById(R.id.slider_threshold);
        TextView labelThreshold = (TextView)rootView.findViewById(R.id.label_current_threshold);
        thresholdSlider.setOnSeekBarChangeListener(createSeekBarChangeListener(labelThreshold));

    }

    private SeekBar.OnSeekBarChangeListener createSeekBarChangeListener(final TextView tv){
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv.setText("€ " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

}
