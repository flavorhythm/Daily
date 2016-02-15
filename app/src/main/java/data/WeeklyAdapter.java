package data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zenoyuki.flavorhythm.daily.R;

import java.util.List;
import java.util.zip.Inflater;

import model.Week;

/**
 * Created by ZYuki on 2/9/2016.
 */
public class WeeklyAdapter extends ArrayAdapter<Week> {
    private Activity activity;
    private int layoutResource;
    private List<Week> weekList;

    public WeeklyAdapter(Activity activity, int layoutResource, List<Week> weekList) {
        super(activity , layoutResource, weekList);

        this.activity = activity;
        this.layoutResource = layoutResource;
        this.weekList = weekList;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {return weekList.size();}

    @Override
    public Week getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(Week item) {
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

            viewHolder.weekNum = (TextView)row.findViewById(R.id.weekly_txt_weekNum);

            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)row.getTag();
        }

        Week week = weekList.get(position);

//        viewHolder.weekNum.setText(String.valueOf(week.getWeekNum()));

        return row;
    }

    class ViewHolder {
        TextView weekNum;
        ListView dailyList;
    }
}
