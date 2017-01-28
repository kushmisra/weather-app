
package panda.whats_the_weather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.R.id.message;
import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {


    EditText cityname;
    TextView details;

    public void findweather(View view){

        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityname.getWindowToken(),0);


        try {
            String encodedcity = URLEncoder.encode(cityname.getText().toString(),"UTF-8");
            DownloadTask Task=new DownloadTask();
            Task.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodedcity+"&APPID=50c9b0b7c0b35c54f29cff5d6595cee9");

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_SHORT).show();

        }


    }

    public class DownloadTask extends AsyncTask<String, Void ,String>{

        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlconnection=null;

            try {
                url=new URL(urls[0]);

                urlconnection=(HttpURLConnection)url.openConnection();

                InputStream in=urlconnection.getInputStream();

                InputStreamReader reader=new InputStreamReader(in);

                int data=reader.read();

                while(data!=-1){

                    char current=(char) data;
                    result+=current;
                    data=reader.read();
                }



            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "could not find weather", Toast.LENGTH_SHORT).show();
            }

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                String message="";
                JSONObject jobject=new JSONObject(result);
                String weather=jobject.getString("weather");
                Log.i("websitecontent",weather);

                JSONArray arr=new JSONArray(weather);

                for(int i=0;i<arr.length();i++){
                    JSONObject jsonpart=arr.getJSONObject(i);
                    String main="";
                    String description="";
                    main= jsonpart.getString("main");
                    description=jsonpart.getString("description");
                    if(main!="" && description!=""){
                        message = main + ":"+ description+"\r\n";
                    }
                }
            if(message!="")
                details.setText(message);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "could not find data", Toast.LENGTH_SHORT).show();
            }


        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname=(EditText)findViewById(R.id.editText);
        details=(TextView)findViewById(R.id.textView);
    }

}
