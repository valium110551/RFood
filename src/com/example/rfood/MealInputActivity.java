package com.example.rfood;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MealInputActivity extends Activity implements android.location.LocationListener{
		
	// Progress Dialog
		private ProgressDialog pDialog;
		private LocationManager locationMgr;

		JSONParser jsonParser = new JSONParser();
		EditText inputMealName;
		EditText inputMealNumber;
		double loclatitude;
		double loclongitude;

		// url to create new product
		private static String url_create_product = "http://54.68.92.191/android_connect_rfood/create_product.php";

		// JSON Node names
		private static final String TAG_SUCCESS = "success";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_meal_input);

			// Edit Text
			inputMealName = (EditText) findViewById(R.id.mealName);
			inputMealNumber = (EditText) findViewById(R.id.mealNumber);

			this.locationMgr = (LocationManager) this.getSystemService(LOCATION_SERVICE);
			findPosition();
			
			// Create button
			Button btnCreateProduct = (Button) findViewById(R.id.sendButton);

			// button click event
			btnCreateProduct.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// creating new product in background thread
					new CreateNewProduct().execute();
				}
			});
		}

		/**
		 * Background Async Task to Create new product
		 * */
		class CreateNewProduct extends AsyncTask<String, String, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(MealInputActivity.this);
				pDialog.setMessage("Creating Product..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}

			/**
			 * Creating product
			 * */
			protected String doInBackground(String... args) {
				String name = inputMealName.getText().toString();
				String number = inputMealNumber.getText().toString();
                String latitude = String.valueOf(loclatitude);
                String longtitude = String.valueOf(loclongitude);
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", name));
				params.add(new BasicNameValuePair("number", number));
				params.add(new BasicNameValuePair("latitude", latitude));
				params.add(new BasicNameValuePair("longitude", longtitude));
			

				// getting JSON Object
				// Note that create product url accepts POST method
				JSONObject json = jsonParser.makeHttpRequest(url_create_product,
						"POST", params);
				
				// check log cat fro response
				Log.d("Create Response", json.toString());

				// check for success tag
				try {
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// successfully created product
						Intent i = new Intent();
						i.setClass(MealInputActivity.this, Map_activity.class);
						startActivity(i);
						
						// closing this screen
						finish();
					} else {
						// failed to create product
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done
				pDialog.dismiss();
			}

		}

		//@Override
	    protected void findPosition() {
	        //super.onResume();
	        // 取得位置提供者，不下條件，讓系統決定最適用者，true 表示生效的 provider
	        String provider = this.locationMgr.getBestProvider(new Criteria(), true);
	        if (provider == null) {
	           // this.insert2tv("沒有 location provider 可以使用");
	            return;
	        }
	      
	        this.locationMgr.requestLocationUpdates(provider, 0, 0, (android.location.LocationListener) this);

	        Location location = this.locationMgr.getLastKnownLocation(provider);//LocationManager.NETWORK_PROVIDER);
	        
	        loclatitude = location.getLatitude();
	        loclongitude = location.getLongitude();
	        
	        this.onLocationChanged(location);
	    }
		
		@Override
	    public void onLocationChanged(Location location) {
	        //this.insert2tv("onLocationChanged...");
	        String msg = "經度: " + location.getLongitude() + ", 緯度: "
	                + location.getLatitude();
	        //this.insert2tv(msg);
	    }
		

	    @Override
	    protected void onPause() {
	        super.onPause();
	        //this.insert2tv("removeUpdates...");
	        this.locationMgr.removeUpdates((android.location.LocationListener) this);
	    }
	    
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	        //this.insert2tv("onStatusChanged...");
	        // 當 location provider 改變時
	    }

	    @Override
	    public void onProviderEnabled(String provider) {
	        //this.insert2tv("onProviderEnabled...");
	        // 當 location provider 有效時
	    }
	    
	    @Override
	    public void onProviderDisabled(String provider) {
	        //this.insert2tv("onProviderDisabled...");
	        // 當 location provider 無效時
	    }
		

}
