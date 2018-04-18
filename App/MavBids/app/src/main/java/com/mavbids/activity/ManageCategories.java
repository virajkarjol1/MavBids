package com.mavbids.activity;

import android.os.Bundle;
import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mavbids.R;

public class ManageCategories extends Activity {

    String CategoryList[] = { "Automobiles", "Antique", "Electronics", "Furniture" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.categoriesLinearLayout);
        int length= CategoryList.length;
        final RadioButton[] radioButtons = new RadioButton[length];
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        for (int i = 0; i < length; i++)
        {
            radioButtons[i] = new RadioButton(this);
            radioGroup.addView(radioButtons[i]);
            radioButtons[i].setText(CategoryList[i]);
        }
        mLinearLayout.addView(radioGroup);
    }

}
