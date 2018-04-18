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

public class Manage extends Activity {

    ListView ManageLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        ManageLV = (ListView) findViewById(R.id.managelistView);

        ManageLV.setAdapter(new ManageItemsAdapter());

        ManageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = null;
                switch (position) {
                    case 0:
                        i = new Intent(Manage.this, ManageUsers.class);
                        break;
                    case 1:
                        i = new Intent(Manage.this, ManageCategories.class);
                        break;
                    case 2:
                        i = new Intent(Manage.this, ManageAdvertisements.class);
                        break;
                }
                startActivity(i);
            }
        });
    }


    public class ManageItemsAdapter extends BaseAdapter {

        private String[] dItems = {"Manage Users","Manage Categories","Manage Advertisements"};
        private Integer[] mImageIds = { R.drawable.users,R.drawable.category,R.drawable.adv};

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.manageitems, parent, false);
            TextView title;
            ImageView dImage;

            title = (TextView) row.findViewById(R.id.mItemName);
            dImage = (ImageView) row.findViewById(R.id.mimageView);


            try {
                title.setText(dItems[position]);
                dImage.setImageResource(mImageIds[position]);
            } catch (Exception e){
            }

            return row;
        }

        public ManageItemsAdapter(){
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
