package com.mavbids.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mavbids.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MyReviews extends Activity {

    EditText message;
    String m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reviews);
        message = (EditText)findViewById(R.id.Review);
        Button Submit=(Button)(findViewById(R.id.Submit));
        Submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View V)
            {
                m=message.getText().toString();
                try {
                    FileOutputStream fou = openFileOutput("Review.txt",MODE_WORLD_READABLE);
                    OutputStreamWriter osw = new OutputStreamWriter(fou);
                    osw.write(m);
                    osw.flush();
                    osw.close();
                    Toast.makeText(getBaseContext(),"Review Saved", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void select (View V) {
        boolean check = ((RadioButton) V).isChecked();

        switch (V.getId()) {
            case R.id.radioButton:
                if (check) {
                    Toast.makeText(this, "User selected", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.radioButton2:
                if (check) {
                    Toast.makeText(this, "User selected", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.radioButton3:
                if (check) {
                    Toast.makeText(this, "User selected", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.radioButton4:
                if (check) {
                    Toast.makeText(this, "User selected", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.radioButton5:
                if (check) {
                    Toast.makeText(this, "User selected", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
