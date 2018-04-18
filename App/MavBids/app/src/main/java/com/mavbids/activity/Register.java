package com.mavbids.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mavbids.R;
import com.mavbids.app.SessionManager;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Register extends Activity {

    EditText fullName, phoneno, email, address, password;
    Button registerButton, cancelButton, uploadButton;
    String fName, pno, emailStr, addressStr, passwordStr;

    private DialogFragment mDialog;
    private DialogFragment saveDialog;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        fullName = (EditText) findViewById(R.id.nameText);
        phoneno = (EditText) findViewById(R.id.phoneText);
        email = (EditText) findViewById(R.id.emailIdText);
        address = (EditText) findViewById(R.id.postalText);
        password = (EditText) findViewById(R.id.passwordText);

        registerButton = (Button) findViewById(R.id.registerButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        uploadButton = (Button) findViewById(R.id.uploadButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fName = fullName.getText().toString().trim();
                pno = phoneno.getText().toString().trim();
                emailStr = email.getText().toString().trim();
                addressStr = address.getText().toString().trim();
                passwordStr = password.getText().toString().trim();

                if(fName.length() == 0 ||
                        pno.length() ==0  ||
                        emailStr.length() == 0 ||
                        addressStr.length() == 0 ||
                        passwordStr.length() == 0){
                    Toast.makeText(getApplicationContext(), "All fields are mandatory.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    saveDialog =  ProgressDialogFragment.newInstance();
                    saveDialog.show(getFragmentManager(), "Saving");
                    new RegisterUser().execute();
                }
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class RegisterUser extends AsyncTask<Void, Void , String> {

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
                        .addPathSegment("registerUser")
                        .build();

                RequestBody formBody = new FormEncodingBuilder()
                        .add("name", fName)
                        .add("contact", pno)
                        .add("email", emailStr)
                        .add("address", addressStr)
                        .add("password", passwordStr)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
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

            saveDialog.dismiss();

            try {
                Thread.sleep(500);
            } catch (Exception e){}


            if(result != null) {
                try {
                    JSONObject obj = (JSONObject) new JSONTokener(result).nextValue();
                    String resultStr = obj.getString("result");

                    if(resultStr.equalsIgnoreCase("true")){
                        mDialog = AlertDialogFragment.newInstance();
                        mDialog.show(getFragmentManager(), "Info");
                    }
                } catch (Exception e) {
                    Log.e("JSON Parse", e.getMessage());
                }

            }

        }
    }

    private void navigateToLogin(){
        Intent intent = new Intent(Register.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public static class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance() {
            return new AlertDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                .setMessage("Successfully registered. You will need to Login now.")
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    final DialogInterface dialog, int id) {
                                ((Register) getActivity()).navigateToLogin();
                            }
                        }).create();
        }
    }

    public static class ProgressDialogFragment extends DialogFragment {

        public static ProgressDialogFragment newInstance() {
            return new ProgressDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Saving details...");
            dialog.setIndeterminate(true);

            return dialog;
        }
    }
}
