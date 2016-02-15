package data;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zenoyuki.flavorhythm.daily.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZYuki on 2/10/2016.
 */
public class OverviewAdapter extends ArrayAdapter<OverviewAdapter.WeeksInYear> {
    public static final int WEEKS_IN_YEAR = 52;

    private Activity activity;
    private int layoutResource;
    private List<WeeksInYear> listOfWeeks;

    public OverviewAdapter(Activity activity, int layoutResource, int year) {
        super(activity, layoutResource);

        Log.v("Date", String.valueOf(year));
        this.activity = activity;
        this.layoutResource = layoutResource;
        listOfWeeks = new ArrayList<>();
        generateWeeksList(year);
    }

    private void generateWeeksList(int year) {
        final int firstMonth = 0;
        final int firstDay = 1;
        final int dateOffset = 2;
        final int daysInWeek = 7;

        Calendar cal = Calendar.getInstance();
        cal.set(year, firstMonth, firstDay);

        int startOfWeek = cal.get(Calendar.DAY_OF_WEEK) - dateOffset;
        cal.add(Calendar.DAY_OF_MONTH, -1 * startOfWeek);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        for(int i = 0; i < WEEKS_IN_YEAR; i++) {
            WeeksInYear week = new WeeksInYear();

            week.weekNum = String.valueOf(i + 1);
            week.dateString = dateFormat.format(cal.getTime());

            listOfWeeks.add(i, week);
            cal.add(Calendar.DAY_OF_MONTH, daysInWeek);
        }
    }

    @Override
    public int getCount() {
        return WEEKS_IN_YEAR;
    }

    @Override
    public WeeksInYear getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(WeeksInYear item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ViewHolder viewHolder;

        if(row == null || row.getTag() == null) {
            row = LayoutInflater.from(activity).inflate(layoutResource, null);
            viewHolder = new ViewHolder();

            viewHolder.weekNum = (TextView)row.findViewById(R.id.overviewRow_text_weekNum);
            viewHolder.startingDate = (TextView)row.findViewById(R.id.overviewRow_text_date);
        } else {
            viewHolder = (ViewHolder)row.getTag();
        }

        WeeksInYear week = listOfWeeks.get(position);

        viewHolder.weekNum.setText(week.weekNum);
        viewHolder.startingDate.setText(week.dateString);

        return row;
    }

    class ViewHolder {
        TextView weekNum;
        TextView startingDate;
    }

    class WeeksInYear {
        String weekNum;
        String dateString;
    }
}
