package ru.iu3.fclient;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

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
        int res=initRng();
        Log.i("fclient", "Init Rng = " + res);

        byte[] v =randomBytes(10);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Example of a call to a native method
//        TextView tv = binding.sampleText;
//        TextView tv =findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key,byte[] data);
    public static native byte[] decrypt(byte[] key,byte[] data);

    public static byte[] StringToHex(String s){
        byte[] hex;
        try{
            hex= Hex.decodeHex(s.toCharArray());
        }
        catch(DecoderException ex){
            hex=null;
        }
        return hex;
    }

    public void onButtonClick(View view) {
//        byte[] key=StringToHex("0123456789ABCDEF0123456789ABCDEF");
//        byte[] enc=encrypt(key, StringToHex("000000000000000102"));
//        byte[] dec=decrypt(key,enc);
//        String s = new String (Hex.encodeHex(dec)).toUpperCase();
//        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        Intent it = new Intent(this, PinPadActivity.class);
        startActivityForResult(it,0);
    }
}