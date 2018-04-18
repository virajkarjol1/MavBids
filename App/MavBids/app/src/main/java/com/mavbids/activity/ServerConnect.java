package com.mavbids.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mavbids.R;
import com.mavbids.app.SessionManager;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;
import org.json.JSONTokener;

public class ServerConnect extends Activity {

    Button connectButton;
    EditText hostNameEditText;
    private DialogFragment mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_connect);

        connectButton = (Button) findViewById(R.id.connectButton);
        hostNameEditText = (EditText) findViewById(R.id.hostNameEditText);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog =  ProgressDialogFragment.newInstance();
                mDialog.show(getFragmentManager(), "Loading");
                String hName = hostNameEditText.getText().toString();

                if(!hName.equalsIgnoreCase("")){

                    hName = hName.concat(".ngrok.io");
                    SessionManager.getInstance().setMavBidsHost(hName);
                    new PingMavBidsServer().execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a hostname",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class PingMavBidsServer extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params){

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
                        .addPathSegment("ping")
                        .build();

                Request request = new Request.Builder()
                        .url(url)
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
                mDialog.dismiss();

                if(result != null){

                    JSONObject obj = (JSONObject) new JSONTokener(result).nextValue();
                    String resultStr = obj.getString("result");

                    if(resultStr.equalsIgnoreCase("Hello")) {
                        Intent intent = new Intent(ServerConnect.this, Login.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Host did not reply",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't connect to the Server. Try again",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Log.e("Session error", e.getMessage());
                Toast.makeText(getApplicationContext(), "Couldn't connect to the Server. Try again",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class ProgressDialogFragment extends DialogFragment {

        public static ProgressDialogFragment newInstance() {
            return new ProgressDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Connecting...");
            dialog.setIndeterminate(true);

            return dialog;
        }
    }
}
