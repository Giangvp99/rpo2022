package ru.iu3.fclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.iu3.fclient.databinding.ActivityMainBinding;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends Activity {

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("fclient");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btnClickMe);
        btn.setOnClickListener((View v) -> {
            onButtonClick(v);
        });


        Button btnTestHttp = findViewById(R.id.buttonTestHttp);
        btnTestHttp.setOnClickListener((View v) -> {
            onButtonTestHttpClick(v);
        });


        int res = initRng();
        Log.i("fclient", "Init Rng = " + res);

        // byte[] v = randomBytes(10);
        // binding = ActivityMainBinding.inflate(getLayoutInflater());
        // setContentView(binding.getRoot());
        //
        // // Example of a call to a native method
        // TextView tv = binding.sampleText;
        // TextView tv =findViewById(R.id.sample_text);
        // tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public static native int initRng();

    public static native byte[] randomBytes(int no);

    public static native byte[] encrypt(byte[] key, byte[] data);

    public static native byte[] decrypt(byte[] key, byte[] data);

    public static byte[] StringToHex(String s) {
        byte[] hex;
        try {
            hex = Hex.decodeHex(s.toCharArray());
        } catch (DecoderException ex) {
            hex = null;
        }
        return hex;
    }

    public void onButtonClick(View view) {
        // byte[] key=StringToHex("0123456789ABCDEF0123456789ABCDEF");
        // byte[] enc=encrypt(key, StringToHex("000000000000000102"));
        // byte[] dec=decrypt(key,enc);
        // String s = new String (Hex.encodeHex(dec)).toUpperCase();
        // Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        Intent it = new Intent(this, PinPadActivity.class);
        startActivityForResult(it, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0)
        {
            if (resultCode == RESULT_OK || data != null)
            {
                String pin = data.getStringExtra("pin");
                Toast.makeText(this, pin, Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected void onButtonTestHttpClick(View v) {
        TestHttpClient();
    }
    protected void TestHttpClient() {
        new Thread(() -> {
            try {
//              HttpURLConnection uc = (HttpURLConnection) (new URL("https://www.wikipedia.org").openConnection());
                HttpURLConnection uc = (HttpURLConnection) (new URL("http://10.0.2.2:8081/api/v1/title").openConnection());
                InputStream inputStream = uc.getInputStream();
                String html = IOUtils.toString(inputStream);
                String title = getPageTitle(html);
                runOnUiThread(() -> {
                    Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
                });
            } catch (Exception ex) {
                Log.e("fapptag", "Http client fails", ex);
            }
        }).start();
    }

    protected String getPageTitle(String html){
//        int pos = html.indexOf("<title");
//        String p = "not found";
//        if(pos >=0){
//            int pos2 = html.indexOf("<", pos+1);
//            if(pos2>=0){
//                p = html.substring(pos+7, pos2);
//            }
//        }
//        return p;
        Pattern partern = Pattern.compile("<title>(.+?)</title>", Pattern.DOTALL);
        Matcher matcher = partern.matcher(html);
        String p;
        if(matcher.find())
            p = matcher.group(1);
        else
            p = "Not found";
        return p;
    }
}