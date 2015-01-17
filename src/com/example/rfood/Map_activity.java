package com.example.rfood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.example.android.location.LocationUtils;
//import com.example.android.location.R;
//import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
//import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

;

public class Map_activity extends Activity 
{
	private GoogleMap mMap;
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productsList;

	// url to get all products list
	private static String url_all_products = "http://54.68.92.191/android_connect_rfood/get_all_products.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_MEALNAME = "name";
	private static final String TAG_NUMBER = "number";

	// products JSONArray
	JSONArray products = null;
	
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_map);
	                
	       // geoCoder = new Geocoder(Map_activity.this);
	        
	        // Hashmap for ListView
			productsList = new ArrayList<HashMap<String, String>>();

			// Loading products in Background Thread
			new LoadAllProducts().execute();
	        	        	           	 
	   	 mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	   	 mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(25.00189710,121.52623110) , 8.0f) );
	   	     
	    }
	
	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllProducts extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Map_activity.this);
			pDialog.setMessage("Loading products. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All Products: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					products = json.getJSONArray(TAG_PRODUCTS);

					// looping through All Products
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);

						// Storing each json item in variable
						String latitude = c.getString(TAG_LATITUDE);
						String longitude = c.getString(TAG_LONGITUDE);
						String name = c.getString(TAG_MEALNAME);
						String number = c.getString(TAG_NUMBER);
						//String name = c.getString(TAG_NAME);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_LATITUDE, latitude);
						map.put(TAG_LONGITUDE, longitude);
						map.put(TAG_MEALNAME, name);
						map.put(TAG_NUMBER, number);

						// adding HashList to ArrayList
						productsList.add(map);
					}
				} else {
					// no products found
					// Launch Add New product Activity
					Intent i = new Intent(getApplicationContext(),
							MealInputActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
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
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					
					for(HashMap<String, String> item:productsList)
					{
						double lat=Double.valueOf(item.get("latitude"));
						double longi = Double.valueOf(item.get("longitude"));
					
				 	 mMap.addMarker(new MarkerOptions()
		   	         .position(new LatLng(lat,longi))
			   	     .title(item.get("name")+"*"+item.get("number")));
					}
					
				}
			});

		}

	}

	 

}


