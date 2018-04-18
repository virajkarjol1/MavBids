package com.mavbids.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mavbids.R;
import com.mavbids.app.SessionManager;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MyOrders extends Activity {

    ListView AdListView;
    JSONArray advList;

    private String searchStr;
    private DialogFragment mDialog;
    private EditText searchAdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        AdListView = (ListView) findViewById(R.id.myOrderslistView);
        mDialog =  ProgressDialogFragment.newInstance();


        AdListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MyOrders.this, Advertisement.class);
                try {
                    JSONObject advObj = advList.getJSONObject(position);

                    i.putExtra("fromScr", "MyOrders");
                    i.putExtra("auctionType", advObj.getString("auctionType"));
                    i.putExtra("itemName", advObj.getString("itemName"));
                    i.putExtra("currentBidPrice", advObj.getString("currentBidPrice"));
                    i.putExtra("startingPrice", advObj.getString("startingPrice"));
                    i.putExtra("expiryDate", advObj.getString("expiryDate"));
                    i.putExtra("description", advObj.getString("description"));
                    i.putExtra("categoryID", advObj.getString("categoryID"));
                    i.putExtra("advertisementId", advObj.getString("advertisementId"));
                    i.putExtra("sellerId", advObj.getString("sellerId"));
                    i.putExtra("status", advObj.getString("status"));

                } catch (Exception e) {
                    Log.i("JSON", "Error in setting data" + e.getMessage());
                }

                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!mDialog.isAdded())
            mDialog.show(getFragmentManager(), "Loading");

        AdListView.setAdapter(null);

        new HomeAdvItems().execute();
    }

    private class HomeAdvItems extends AsyncTask<String, Void , String> {

        @Override
        protected String doInBackground(String... params){

            try {
                Thread.sleep(500);
            } catch (Exception e){}

            try {
                OkHttpClient client = new OkHttpClient();

                HttpUrl url = new HttpUrl.Builder()
                        .scheme("http")
                        .host(SessionManager.getInstance().getMavBidsHost())
                        .port(80)
                        .addPathSegment("MavBids")
                        .addPathSegment("getMyOrders")
                        .build();

                String sessionId = SessionManager.getInstance().getSessionId();

                RequestBody formBody = new FormEncodingBuilder()
                        .add("userId", SessionManager.getInstance().getUserId())
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Cookie",sessionId)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();

                return response.body().string();
            } catch (Exception e){
                Log.e("HTTP Error", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            mDialog.dismiss();
            if(result != null) {
                try {
                    JSONObject obj = (JSONObject) new JSONTokener(result).nextValue();
                    advList = obj.getJSONArray("result");
                    AdListView.setAdapter(new AdItemsAdapter());
                } catch (Exception e) {
                    Log.e("JSON Parse", e.getMessage());
                }
            }
        }
    }

    public class AdItemsAdapter extends BaseAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.aditem, parent, false);
            TextView title, desc, itemCost, aucType;

            title = (TextView) row.findViewById(R.id.title);
            desc = (TextView) row.findViewById(R.id.desc);
            itemCost = (TextView) row.findViewById(R.id.itemCost);
            aucType = (TextView) row.findViewById(R.id.aucType);

            JSONObject obj = null;

            try {
                obj = advList.getJSONObject(position);
                title.setText(obj.getString("itemName"));
                desc.setText(obj.getString("description"));

                if(obj.getString("currentBidPrice").equalsIgnoreCase("null")){
                    itemCost.setText("$ " + obj.getString("startingPrice"));
                } else {
                    itemCost.setText("$ " + obj.getString("currentBidPrice"));
                }

                aucType.setText(obj.getString("auctionType"));
            } catch (Exception e){
            }

            return row;
        }

        public AdItemsAdapter(){
        }

        public int getCount() {
            try {
                return advList.length();
            } catch (Exception e){
                return 0;
            }
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }
    }

    public static class ProgressDialogFragment extends DialogFragment {

        public static ProgressDialogFragment newInstance() {
            return new ProgressDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading Advertisements...");
            dialog.setIndeterminate(true);

            return dialog;
        }
    }

}
