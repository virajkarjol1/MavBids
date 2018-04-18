package com.mavbids.activity;

import android.os.Bundle;
import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mavbids.R;

public class ManageUsers extends Activity {
    String UserList[] = { "James Mathew", "Lionel Messi", "Adam Smith", "Jennifer Aniston","Sam Mathew","Alice John","Myra Saldanha",
            "Ram Khanna","Jessica Simson","Tom Bradley" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.usersListLinearLayout);
        int length= UserList.length;
        final RadioButton[] radioButtons = new RadioButton[length];
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        for (int i = 0; i < length; i++)
        {
            radioButtons[i] = new RadioButton(this);
            radioGroup.addView(radioButtons[i]);
            radioButtons[i].setText(UserList[i]);
        }
        mLinearLayout.addView(radioGroup);
    }

}
