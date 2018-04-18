package com.mavbids.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.mavbids.R;
import com.mavbids.app.SessionManager;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.app.DialogFragment;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Login extends Activity {

    final Context context = this;
    private EditText username;
    private EditText password;

    private DialogFragment mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button)findViewById(R.id.signInButton);

        button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
            username = (EditText) findViewById(R.id.emailId);
            password = (EditText) findViewById(R.id.password);

            if (!username.getText().toString().equalsIgnoreCase("")
                    && !password.getText().toString().equalsIgnoreCase("")) {

                mDialog =  ProgressDialogFragment.newInstance();
                mDialog.show(getFragmentManager(), "Loading");
                new MavBidsLogin().execute(username.getText().toString(),password.getText().toString());

            } else {

                AlertDialog.Builder errorAlert = new AlertDialog.Builder(context);

                errorAlert.setMessage("Please enter password or username");
                errorAlert.setTitle("Error");
                errorAlert.setPositiveButton("OK", null);
                errorAlert.setCancelable(true);
                errorAlert.create().show();
            }
            }
        });

        Button registerButton = (Button)findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), Register.class);
            startActivity(intent);
            }
        });
    }

    private class MavBidsLogin extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params){

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
                        .addPathSegment("login")
                        .addQueryParameter("emailId", params[0])
                        .addQueryParameter("password", params[1])
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();

                String result = response.body().string();

                JSONObject obj = (JSONObject) new JSONTokener(result).nextValue();
                String resultStr = obj.getString("type");

                if(!resultStr.equalsIgnoreCase("failed")) {
                    String setCookie = response.header("Set-Cookie");
                    String sessionId = setCookie.split(";")[0];

                    SessionManager.getInstance().setSessionId(sessionId);

                    JSONObject rObj = obj.getJSONObject("result");
                    SessionManager.getInstance().setUserId(rObj.getString("userId"));
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e){
                Log.e("HTTP Error", e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {

            mDialog.dismiss();

            if(result == true){
                Intent intent = new Intent(Login.this, HomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid username or password.",
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
            dialog.setMessage("Signing in...");
            dialog.setIndeterminate(true);

            return dialog;
        }
    }
}
