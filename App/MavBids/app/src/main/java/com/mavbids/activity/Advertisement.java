package com.mavbids.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Advertisement extends Activity {

    private ImageView advImageViewer;
    private TextView advTextView;
    private Button advActionButton;
    private EditText bidAmtEditText;

    private AlertDialogFragment saveDialog;
    private ProgressDialogFragment mDialog;

    private String advertisementId, currentBidPrice = "NA",startingPrice, bidAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        Bundle extras = getIntent().getExtras();

        advImageViewer = (ImageView) findViewById(R.id.advImageView);
        advTextView = (TextView) findViewById(R.id.advTextView);
        advActionButton = (Button) findViewById(R.id.advActionbutton);
        bidAmtEditText = (EditText) findViewById(R.id.bidAmtEditText);
        mDialog =  ProgressDialogFragment.newInstance();

        advertisementId = extras.getString("advertisementId");
        new ImageDownloader().execute("http://" + SessionManager.getInstance().getMavBidsHost()
                + "/MavBids/getAdvImage?advId=" + advertisementId);

        String auctionType = extras.getString("auctionType");
        String fromScr = extras.getString("fromScr");

        if(!extras.getString("currentBidPrice").equalsIgnoreCase("null")){
            currentBidPrice = extras.getString("currentBidPrice");
        }

        startingPrice = extras.getString("startingPrice");
        String itemName = extras.getString("itemName");
        String description = extras.getString("description");
        String expiryDate = extras.getString("expiryDate");

        StringBuffer sBuffer = new StringBuffer();

        if(auctionType.equalsIgnoreCase("Bid")) {
            sBuffer.append("<b>Current Bid Price :</b> ");
            if(!extras.getString("currentBidPrice").equalsIgnoreCase("null")){
                sBuffer.append("$");
            }

            sBuffer.append(currentBidPrice + "<br>");
            sBuffer.append("<b>Starting Bid Price :</b> $" + startingPrice + "<br>");
        }
        else { // Buy
            sBuffer.append("<b>Price :</b> $" + startingPrice + "<br>");
        }

        sBuffer.append("<b>Name :</b> " + itemName + "<br>");

        String categoryID = extras.getString("categoryID").split("\\.")[0];
        int cId = Integer.parseInt(categoryID);
        List<String> cArray = Arrays.asList(getResources().getStringArray(R.array.category_array));

        sBuffer.append("<b>Category :</b> " + cArray.get(cId) + "<br>");
        sBuffer.append("<b>Description :</b> " + description + "<br>");
        sBuffer.append("<b>Expires on :</b> " + expiryDate + "<br>");

        advTextView.setText(Html.fromHtml(sBuffer.toString()));

        if(fromScr.equalsIgnoreCase("Home")){
            String sellerId = extras.getString("sellerId").split("\\.")[0];
            if(SessionManager.getInstance().getUserId().equalsIgnoreCase(sellerId)){
                bidAmtEditText.setVisibility(View.GONE);
                advActionButton.setVisibility(View.GONE);
            } else if(auctionType.equalsIgnoreCase("Bid")){
                advActionButton.setText("Bid");
                mDialog.setMsg("Submitting your bid...");

                advActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bidAmount = bidAmtEditText.getText().toString();

                        if(bidAmount.equalsIgnoreCase("")){
                            Toast.makeText(getApplicationContext(), "Enter a bid",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        double bidAmt = toDoubleWithRoundOff(bidAmount);

                        if (!currentBidPrice.equalsIgnoreCase("NA")) {

                            double cPrice = toDoubleWithRoundOff(currentBidPrice);

                            if (bidAmt > cPrice) {
                                mDialog.show(getFragmentManager(), "Processing");
                                new BidItem().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Your bid amount should be higher than current bid",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            double cPrice = toDoubleWithRoundOff(startingPrice);

                            if (bidAmt > cPrice) {
                                mDialog.show(getFragmentManager(), "Processing");
                                new BidItem().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Your bid amount should be higher than starting price",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            } else { // Buy
                mDialog.setMsg("Processing this Item...");

                bidAmtEditText.setVisibility(View.GONE);
                advActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.show(getFragmentManager(), "Processing");
                        new DirectBuy().execute();
                    }
                });
            }
        }

        if(fromScr.equalsIgnoreCase("MyAdv")){
            if(auctionType.equalsIgnoreCase("Bid")){
                bidAmtEditText.setVisibility(View.GONE);
                advActionButton.setText("Close deal");
                advActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Closed the deal",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else { // Buy
                bidAmtEditText.setVisibility(View.GONE);
                String status = extras.getString("status");
                if(status.equalsIgnoreCase("SOLD")) {
                    advActionButton.setText("Contact Buyer");
                    advActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "Contacting the buyer...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else{
                    advActionButton.setVisibility(View.GONE);
                }
            }
        }

        if(fromScr.equalsIgnoreCase("MyOrders")){
            if(auctionType.equalsIgnoreCase("Bid")){
                bidAmtEditText.setVisibility(View.GONE);
                advActionButton.setVisibility(View.GONE);
            } else if(auctionType.equalsIgnoreCase("Direct Buy")){
                bidAmtEditText.setVisibility(View.GONE);
                advActionButton.setText("Contact Seller");
                advActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Contacting the seller...",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private class DirectBuy extends AsyncTask<String, Void , String> {

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
                        .addPathSegment("directBuy")
                        .build();

                RequestBody formBody = new FormEncodingBuilder()
                        .add("buyerId", SessionManager.getInstance().getUserId())
                        .add("adId", advertisementId)
                        .build();

                String sessionId = SessionManager.getInstance().getSessionId();

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Cookie", sessionId)
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
            try {
                JSONObject obj = (JSONObject) new JSONTokener(result).nextValue();
                String resultStr = obj.getString("result");

                if(resultStr.equalsIgnoreCase("true")){
                    saveDialog = AlertDialogFragment.newInstance();
                    saveDialog.setMsg("You have purchased this item.");
                    saveDialog.show(getFragmentManager(), "Info");
                }
            } catch (Exception e){
                Log.e("JSON Parse", e.getMessage());
            }

            mDialog.dismiss();
        }
    }

    private class BidItem extends AsyncTask<String, Void , String> {

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
                        .addPathSegment("bidAdvertisement")
                        .build();

                RequestBody formBody = new FormEncodingBuilder()
                        .add("bidderId", SessionManager.getInstance().getUserId())
                        .add("adId", advertisementId)
                        .add("bidAmount",roundedOffDoubleString(bidAmount))
                        .build();

                String sessionId = SessionManager.getInstance().getSessionId();

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Cookie", sessionId)
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

            try {
                JSONObject obj = (JSONObject) new JSONTokener(result).nextValue();
                String resultStr = obj.getString("result");

                if(resultStr.equalsIgnoreCase("true")){
                    saveDialog = AlertDialogFragment.newInstance();
                    saveDialog.setMsg("Your bid is placed");
                    saveDialog.show(getFragmentManager(), "Info");
                }
            } catch (Exception e){
                Log.e("JSON Parse", e.getMessage());
            }


            mDialog.dismiss();
        }
    }

    private class ImageDownloader extends AsyncTask<String, String, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params){
            try {
                URL u = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
                urlConnection.setRequestProperty("Cookie",SessionManager.getInstance().getSessionId());
                return BitmapFactory.decodeStream(urlConnection.getInputStream());
            } catch (Exception e){
                Log.e("HTTP Error", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null)
                advImageViewer.setImageBitmap(result);
        }
    }

    double toDoubleWithRoundOff(String s){
        double d = Double.parseDouble(s);
        double rounded = (double) Math.round(d * 100) / 100;
        return rounded;
    }

    String roundedOffDoubleString(String s){
        double d = Double.parseDouble(s);
        double rounded = (double) Math.round(d * 100) / 100;
        return "" + rounded;
    }

    private void navigateToHomeScreen(){
        finish();
    }

    public static class AlertDialogFragment extends DialogFragment {

        String msg;
        public static AlertDialogFragment newInstance() {
            return new AlertDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage(this.msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    ((Advertisement) getActivity()).navigateToHomeScreen();
                                }
                            }).create();
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class ProgressDialogFragment extends DialogFragment {

        String msg;
        public static ProgressDialogFragment newInstance() {
            return new ProgressDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(msg);
            dialog.setIndeterminate(true);

            return dialog;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
