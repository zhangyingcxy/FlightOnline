package com.example.flightonline;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {
    Http() throws Exception
    {
        //.openConnectio，,返回一个URLConnection实例表示由所引用的远程对象的连接URL
        //URLConnection的子类有HttpURLConnection和JarURLConnection
        URL url=new URL("https://www.mafengwo.cn/flight/#/list?departCity=%E4%B8%8A%E6%B5%B7&departCode=SHA&destCity=%E6%88%90%E9%83%BD&destCode=CTU&departDate=2021-06-26&status=0&withChild=0");
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");//模拟浏览器得get请求
        conn.setRequestProperty( "User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763");
        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
        String msg=null;
        while((msg=br.readLine())!=null)
        {

            Log.i("TAG", "main: msg="+msg);
        }
        br.close();
    }
}
