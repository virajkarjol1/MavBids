package com.mavbids.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mavbids.R;
import com.mavbids.app.SessionManager;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HomeScreen extends Activity {

    ListView AdListView;
    JSONArray advList;

    private String searchStr;
    private DialogFragment mDialog;
    private EditText searchAdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        AdListView = (ListView) findViewById(R.id.adlistView);
        searchAdEditText = (EditText) findViewById(R.id.searchAdEditText);

        mDialog =  ProgressDialogFragment.newInstance();

        searchAdEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchStr = searchAdEditText.getText().toString();
                    if (!searchStr.equalsIgnoreCase("")) {
                        mDialog.show(getFragmentManager(), "Loading");
                        AdListView.setAdapter(null);
                        new SearchAdItems().execute();
                    } else {
                        mDialog.show(getFragmentManager(), "Loading");
                        AdListView.setAdapter(null);
                        new HomeAdvItems().execute();
                    }
                    return true;
                }

                return false;
            }
        });
        AdListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HomeScreen.this, Advertisement.class);
                try {
                    JSONObject advObj = advList.getJSONObject(position);

                    i.putExtra("fromScr", "Home");
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
                        .addPathSegment("getAdvertisements")
                        .addQueryParameter("forUserId", SessionManager.getInstance().getUserId())
                        .addQueryParameter("status", "ON SALE")
                        .build();

                String sessionId = SessionManager.getInstance().getSessionId();

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Cookie",sessionId)
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

    private class SearchAdItems extends AsyncTask<String, Void , String> {

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
                        .addPathSegment("getAdvByName")
                        .addQueryParameter("itemName",searchStr)
                        .build();

                String sessionId = SessionManager.getInstance().getSessionId();

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Cookie",sessionId)
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

            try {
                JSONObject obj = (JSONObject) new JSONTokener(result).nextValue();
                advList = obj.getJSONArray("result");
            } catch (Exception e){
                Log.e("JSON Parse", e.getMessage());
            }

            AdListView.setAdapter(new AdItemsAdapter());
            mDialog.dismiss();
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
                    itemCost.setText("$" + obj.getString("startingPrice"));
                } else {
                    itemCost.setText("$" + obj.getString("currentBidPrice"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if(SessionManager.getInstance().getUserId().equalsIgnoreCase("1"))
            inflater.inflate(R.menu.topmenuadmin, menu);
        else
            inflater.inflate(R.menu.topmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.postAd:
                Intent i = new Intent(HomeScreen.this,PostAdvertisement.class);
                startActivity(i);
                return true;
            case R.id.dashboard:
                Intent j = new Intent(HomeScreen.this,Dashboard.class);
                startActivity(j);
                return true;
            case R.id.manage:
                Intent k = new Intent(HomeScreen.this,Manage.class);
                startActivity(k);
                return true;
            case R.id.logout:
                SessionManager.getInstance().clearSession();
                Intent intent = new Intent(this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
