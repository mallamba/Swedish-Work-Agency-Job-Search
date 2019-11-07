package com.example.afm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ListArrayAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    private  List<String> objects, objects2, sideList1, sideList2, sideList3, sideList4;


    public ListArrayAdapter(Context context, int textViewResourceId,
                              List<String> objects, List<String> objects2) {
        super(context,-1, objects);
        this.objects = objects;
        this.objects2 = objects2;
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    public void setSideLists(List<String> sideList1, List<String> sideList2, List<String> sideList3, List<String> sideList4  ){
        this.sideList1 = sideList1;
        this.sideList2 = sideList2;
        this.sideList3 = sideList3;
        this.sideList4 = sideList4;
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.job_list, parent, false);

        TextView textView1 = rowView.findViewById(R.id.firstLine);
        TextView textView2 = rowView.findViewById(R.id.secondLine);

        TextView textView3 = rowView.findViewById(R.id.icon_up);
        TextView textView4 = rowView.findViewById(R.id.icon_mid1);
        TextView textView5 = rowView.findViewById(R.id.icon_mid2);
        TextView textView6 = rowView.findViewById(R.id.icon_down);

        textView1.setText( objects.get(position) );
        textView2.setText( objects2.get(position) );

        textView3.setText( sideList1.get(position) );
        textView4.setText( sideList2.get(position) );
        textView5.setText( sideList3.get(position) );
        textView6.setText( sideList4.get(position) );

        return rowView;
    }

}