package com.apthai.ap_questionaire.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.ValTextData;

import java.util.ArrayList;

/**
 * Created by koy on 02/05/2014.
 */
public class provinceAdapter extends ArrayAdapter<ValTextData> {

    private Context context;
    private ArrayList<ValTextData> objects;
    private questionniare_delegate delegate;

    public provinceAdapter(Context context, int resource, ArrayList<ValTextData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        delegate = (questionniare_delegate) context.getApplicationContext();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.dropdownlist, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.list_title);

        String s = objects.get(position).getText() ;
        textView.setText(s);
        return rowView;
    }

}
