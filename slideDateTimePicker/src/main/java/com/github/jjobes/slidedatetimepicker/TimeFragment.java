package com.github.jjobes.slidedatetimepicker;

import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * The fragment for the second page in the ViewPager that holds
 * the {@link TimePicker}.
 *
 * @author jjobes
 */
public class TimeFragment extends Fragment {
    /**
     * Used to communicate back to the parent fragment as the user
     * is changing the time spinners so we can dynamically update
     * the tab text.
     */
    public interface TimeChangedListener {
        void onTimeChanged(int hour, int minute);
    }

    private TimeChangedListener mCallback;

    public TimeFragment() {
        // Required empty public constructor for fragment.
    }

    /**
     * Cast the reference to {@link SlideDateTimeDialogFragment} to a
     * {@link TimeChangedListener}.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mCallback = (TimeChangedListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement TimeFragment.TimeChangedListener interface");
        }
    }

    /**
     * Return an instance of TimeFragment with its bundle filled with the
     * constructor arguments. The values in the bundle are retrieved in
     * {@link #onCreateView} below to properly initialize the TimePicker.
     *
     * @param hour
     * @param minute
     * @param isClientSpecified24HourTime
     * @param is24HourTime
     * @return
     */
    public static final TimeFragment newInstance(int hour, int minute, boolean isClientSpecified24HourTime, boolean is24HourTime) {
        final TimeFragment f = new TimeFragment();

        final Bundle b = new Bundle();
        b.putInt("hour", hour);
        b.putInt("minute", minute);
        b.putBoolean("isClientSpecified24HourTime", isClientSpecified24HourTime);
        b.putBoolean("is24HourTime", is24HourTime);
        f.setArguments(b);

        return f;
    }

    /**
     * Create and return the user interface view for this fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int initialHour = getArguments().getInt("hour");
        final int initialMinute = getArguments().getInt("minute");
        final boolean isClientSpecified24HourTime = getArguments().getBoolean("isClientSpecified24HourTime");
        final boolean is24HourTime = getArguments().getBoolean("is24HourTime");

        final View v = inflater.inflate(R.layout.fragment_time, container, false);

        final TimePicker timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        // block keyboard popping up on touch
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mCallback.onTimeChanged(hourOfDay, minute);
            }
        });

        // If the client specifies a 24-hour time format, set it on
        // the TimePicker.
        if (isClientSpecified24HourTime) {
            timePicker.setIs24HourView(is24HourTime);
        } else {
            // If the client does not specify a 24-hour time format, use the
            // device default.
            timePicker.setIs24HourView(DateFormat.is24HourFormat(
                    getTargetFragment().getActivity()));
        }

        timePicker.setCurrentHour(initialHour);
        timePicker.setCurrentMinute(initialMinute);

        return v;
    }
}
