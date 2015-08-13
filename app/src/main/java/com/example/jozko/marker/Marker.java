package com.example.jozko.marker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Marker extends ActionBarActivity {


    ListView list;
    TextView name;
    TextView teacher;
    Button btnGetData;
    ArrayList<HashMap<String, String>> subjectlist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    private static String url = "http://marker.herokuapp/subjects.json";

    //JSON Node Names
    private static final String TAG_NAME = "name";
    private static final String TAG_TEACHER = "teacher";

    JSONArray subjects = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        new JSONParse().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_marker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JSONParse extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            name = (TextView)findViewById(R.id.subject_name);
            teacher = (TextView)findViewById(R.id.subject_teacher);
            pDialog = new ProgressDialog(Marker.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONArray doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONArray json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                subjects = json;
                subjectlist.clear();
                for(int i = 0; i < subjects.length(); i++){
                    JSONObject c = subjects.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String name = c.getString(TAG_NAME);
                    String teacher = c.getString(TAG_TEACHER);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_NAME, name);
                    map.put(TAG_TEACHER, teacher);

                    subjectlist.add(map);
                    list=(ListView)findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(Marker.this, subjectlist,
                            R.layout.subject_detail,
                            new String[] { TAG_NAME, TAG_TEACHER}, new int[] {
                            R.id.subject_name, R.id.subject_teacher});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(Marker.this, "You Clicked at " + subjectlist.get(+position).get("name"), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
