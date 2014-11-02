package com.example.rfood;

//chechgit
//master change1

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity  {

	private String addressStr;
	private Button inputButton;
	private Button browseButton;
	private Button editButton;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    
    	inputButton = (Button)findViewById(R.id.button1);
    	browseButton = (Button)findViewById(R.id.button2);
    	editButton = (Button)findViewById(R.id.button3);
    	inputButton.setOnClickListener(toInput);  
    	browseButton.setOnClickListener(toMap); 
    	editButton.setOnClickListener(toEdit);
    }
	

	private Button.OnClickListener toInput = new Button.OnClickListener()
	{
		public void onClick(View v)
		{		
			Intent intent = new Intent();
			intent.setClass(MainActivity.this,MealInputActivity.class);
		    startActivity(intent);				
		}
		
	};
	private Button.OnClickListener toMap = new Button.OnClickListener()
	{
		public void onClick(View v)
		{		
			Intent intent = new Intent();
			intent.setClass(MainActivity.this,Map_activity.class);
		    startActivity(intent);		
		}
		
	};
	private Button.OnClickListener toEdit = new Button.OnClickListener()
	{
		public void onClick(View v)
		{		
			Intent intent = new Intent();
			intent.setClass(MainActivity.this,AllProductsActivity.class);
		    startActivity(intent);		
		}
		
	};
	
	
	
}
