package data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zenoyuki.flavorhythm.daily.R;

import java.util.List;

import model.Day;
import model.WeekDay;
import model.WeekEnd;

/**
 * Created by ZYuki on 2/9/2016.
 */
public class DailyAdapter extends ArrayAdapter<Day> implements View.OnClickListener {
    private Activity activity;
    private int layoutResource;
    private List<Day> dayList;

    private ArrayAdapter<String> weekDayLunch, weekDayToDo, weekEndToDo;

    public DailyAdapter(Activity activity, int layoutResource, List<Day> dayList) {
        super(activity , layoutResource, dayList);

        this.activity = activity;
        this.layoutResource = layoutResource;
        this.dayList = dayList;
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

            viewHolder.dayOfWeek = (TextView)row.findViewById(R.id.dailyRow_text_dayOfWeek);
            viewHolder.hideLunchtime = (LinearLayout)row.findViewById(R.id.dailyRow_linear_hideLunchtime);

            viewHolder.lunchtime = (ListView)row.findViewById(R.id.dailyRow_list_lunchtime);
            viewHolder.dailyToDo = (ListView)row.findViewById(R.id.dailyRow_list_afterWork);

            viewHolder.addLunchtime = (Button)row.findViewById(R.id.dailyRow_butn_addLunchtime);
            viewHolder.addDailyToDo = (Button)row.findViewById(R.id.dailyRow_butn_addDailyToDo);

        } else {viewHolder = (ViewHolder)row.getTag();}

        //TODO: figure out how to set the todos to dynamic textviews
        switch(dayList.get(position).getName()) {
            case MON: case TUE: case WED: case THU: case FRI:
                WeekDay weekDay = (WeekDay)dayList.get(position);

                viewHolder.dayOfWeek.setText(weekDay.getName().toString());

                weekDayLunch = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, weekDay.getLunchtime());
                weekDayToDo = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, weekDay.getDailyToDo());

                viewHolder.lunchtime.setAdapter(weekDayLunch);
                viewHolder.dailyToDo.setAdapter(weekDayToDo);

                break;
            case SAT: case SUN:
                WeekEnd weekEnd = (WeekEnd)dayList.get(position);

                viewHolder.dayOfWeek.setText(weekEnd.getName().toString());

                viewHolder.hideLunchtime.setVisibility(View.GONE);

                /*weekEndToDo = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, weekEnd.getDailyToDo());

                viewHolder.dailyToDo.setAdapter(weekEndToDo);*/

                break;
        }

        viewHolder.addLunchtime.setOnClickListener(this);
        viewHolder.addDailyToDo.setOnClickListener(this);

        return row;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.dailyRow_butn_addLunchtime:
                //TODO: add input to arrayadapter
                break;
            case R.id.dailyRow_butn_addDailyToDo:
                break;
        }
    }

    class ViewHolder {
        TextView dayOfWeek;
        LinearLayout hideLunchtime;
        ListView lunchtime;
        ListView dailyToDo;
        Button addLunchtime;
        Button addDailyToDo;
    }
}
