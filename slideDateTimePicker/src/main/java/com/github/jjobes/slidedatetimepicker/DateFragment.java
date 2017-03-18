package com.github.jjobes.slidedatetimepicker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import java.util.Date;

/**
 * The fragment for the first page in the ViewPager that holds
 * the {@link DatePicker}.
 *
 * @author jjobes
 */
public class DateFragment extends Fragment {
    /**
     * Used to communicate back to the parent fragment as the user
     * is changing the date spinners so we can dynamically update
     * the tab text.
     */
    public interface DateChangedListener {
        void onDateChanged(int year, int month, int day);
    }

    private DateChangedListener mCallback;

    public DateFragment() {
        // Required empty public constructor for fragment.
    }

    /**
     * Cast the reference to {@link SlideDateTimeDialogFragment}
     * to a {@link DateChangedListener}.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mCallback = (DateChangedListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DateFragment.DateChangedListener interface");
        }
    }

    /**
     * Return an instance of DateFragment with its bundle filled with the
     * constructor arguments. The values in the bundle are retrieved in
     * {@link #onCreateView} below to properly initialize the DatePicker.
     *
     * @param year
     * @param month
     * @param day
     * @param minDate
     * @param maxDate
     * @return an instance of DateFragment
     */
    public static final DateFragment newInstance(int year, int month, int day, Date minDate, Date maxDate) {
        final DateFragment f = new DateFragment();

        final Bundle b = new Bundle();
        b.putInt("year", year);
        b.putInt("month", month);
        b.putInt("day", day);
        b.putSerializable("minDate", minDate);
        b.putSerializable("maxDate", maxDate);
        f.setArguments(b);

        return f;
    }

    /**
     * Create and return the user interface view for this fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int initialYear = getArguments().getInt("year");
        final int initialMonth = getArguments().getInt("month");
        final int initialDay = getArguments().getInt("day");
        final Date minDate = (Date) getArguments().getSerializable("minDate");
        final Date maxDate = (Date) getArguments().getSerializable("maxDate");

        final View v = inflater.inflate(R.layout.fragment_date, container, false);

        final DatePicker picker = (DatePicker) v.findViewById(R.id.datePicker);
        // block keyboard popping up on touch
        picker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        picker.init(
                initialYear,
                initialMonth,
                initialDay,
                new OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        mCallback.onDateChanged(year, monthOfYear, dayOfMonth);
                    }
                });

        if (minDate != null)
            picker.setMinDate(minDate.getTime());

        if (maxDate != null)
            picker.setMaxDate(maxDate.getTime());

        return v;
    }
}
