package com.mavbids.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mavbids.R;

public class Dashboard extends Activity {

    ListView dashboardLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashboardLV = (ListView) findViewById(R.id.dashboardlistView);

        dashboardLV.setAdapter(new DashboadItemsAdapter());

        dashboardLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = null;
                switch (position){
                    case 0:
                        i = new Intent(Dashboard.this,MyAdvertisments.class);
                        break;
                    case 1:
                        i = new Intent(Dashboard.this,MyOrders.class);
                        break;
                    case 2:
                        i = new Intent(Dashboard.this,MyReviews.class);
                        break;
                }
                startActivity(i);
            }
        });
    }

    public class DashboadItemsAdapter extends BaseAdapter {

        private String[] dItems = {"My Advertisements", "My Orders", "My Reviews"};
        private Integer[] mImageIds = { R.drawable.adv,R.drawable.orders,R.drawable.review};

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.dashboarditems, parent, false);
            TextView title;
            ImageView dImage;

            title = (TextView) row.findViewById(R.id.dItemName);
            dImage = (ImageView) row.findViewById(R.id.dimageView);


            try {
                title.setText(dItems[position]);
                dImage.setImageResource(mImageIds[position]);
            } catch (Exception e){
            }

            return row;
        }

        public DashboadItemsAdapter(){
        }

        public int getCount() {
            return dItems.length;
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }
    }
}
