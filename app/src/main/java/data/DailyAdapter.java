package data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zenoyuki.flavorhythm.daily.R;

import java.util.List;

import model.Day;
import model.Week;
import model.WeekDay;
import model.WeekEnd;

/**
 * Created by ZYuki on 2/9/2016.
 */
public class DailyAdapter extends ArrayAdapter<Day> {
    private Activity activity;
    private int layoutResource;
    private List<Day> dayList;

    public DailyAdapter(Activity activity, int layoutResource, List<Day> dayList) {
        super(activity , layoutResource, dayList);

        this.activity = activity;
        this.layoutResource = layoutResource;
        this.dayList = dayList;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {return dayList.size();}

    @Override
    public Day getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(Day item) {
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

            viewHolder.dayOfWeek = (TextView)row.findViewById(R.id.daily_txt_dayOfWeek);
            viewHolder.lunchtime = (TextView)row.findViewById(R.id.daily_txt_lunchtime);
            viewHolder.dailyToDo = (TextView)row.findViewById(R.id.daily_txt_dailyToDo);
        } else {viewHolder = (ViewHolder)row.getTag();}

        switch(dayList.get(position).getName()) {
            case MON: case TUE: case WED: case THU: case FRI:
                WeekDay weekDay = (WeekDay)dayList.get(position);

                viewHolder.dayOfWeek.setText(weekDay.getName().toString());
                for(String lunchtime : weekDay.getLunchtime()) {
                    viewHolder.lunchtime.append(lunchtime);
                }

                for(String dailyToDo : weekDay.getDailyToDo()) {
                    viewHolder.dailyToDo.append(dailyToDo);
                }
                break;
            case SAT: case SUN:
                WeekEnd weekEnd = (WeekEnd)dayList.get(position);

                viewHolder.dayOfWeek.setText(weekEnd.getName().toString());

                for(String dailyToDo : weekEnd.getDailyToDo()) {
                    viewHolder.dailyToDo.append(dailyToDo);
                }
                break;
        }

        return row;
    }

    class ViewHolder {
        TextView dayOfWeek;
        TextView lunchtime;
        TextView dailyToDo;
    }
}
