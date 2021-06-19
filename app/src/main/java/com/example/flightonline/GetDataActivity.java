package com.example.flightonline;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;

public class GetDataActivity extends AppCompatActivity implements Runnable{
    private static final String msg = "消息传递";
    private static final String TAG = "GetDataActivity";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);
//开启子线程
        Thread t = new Thread(this);
        t.start();

        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: getMessage msg = " + str);

                }
                super.handleMessage(msg);
            }
        };
    }

    @Override

    public void run() {
        Document doc =null;
//        URL url = null;
        try {
            String url = "https://flights.ctrip.com/international/search/oneway-ctu-sha?depdate=2021-06-17&cabin=Y_S_C_F";
            doc = Jsoup.connect(url).get();
            Log.i(TAG, "run: "+doc);
            if(doc!= null) {
                Elements es = doc.getElementsByAttributeValue("class","airline-name");
//                Elements es = doc.getElementsByClass("airline-name");
                Log.i(TAG, "run:es= "+es);
                for (Element element:es){
                    Element ele  = element.getElementsByAttributeValue("class","s5").first();
                    String dtime = element.text();
                    Log.i(TAG, "run: dtime"+dtime);

                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
//msg.what = 5;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}