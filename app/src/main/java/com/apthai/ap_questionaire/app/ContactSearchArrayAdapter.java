package com.apthai.ap_questionaire.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cloud9worldwide.questionnaire.data.ContactSearchData;

import java.util.ArrayList;

/**
 * Created by koy on 02/05/2014.
 */
public class ContactSearchArrayAdapter extends ArrayAdapter<ContactSearchData> {
    private Context context;
    private ArrayList<ContactSearchData> objects;
    private questionniare_delegate delegate;

    public ContactSearchArrayAdapter(Context context, int resource, ArrayList<ContactSearchData> objects) {
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

        String s = objects.get(position).getFname() + " " + objects.get(position).getLname();
        textView.setText(s);
        return rowView;
    }
}
