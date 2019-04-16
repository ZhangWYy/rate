package first.swufe.com.week6;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class week6 extends AppCompatActivity implements Runnable {

    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float krwRate = 0.3f;

    EditText rmb;
    TextView showTxt;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week6);

        rmb = (EditText) findViewById(R.id.rmb);
        showTxt = (TextView) findViewById(R.id.showTxt);

        //获取SP里保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        krwRate = sharedPreferences.getFloat("krw_rate",0.0f);

        Log.i(TAG,"onCreate: sp dollarRate=" + dollarRate);
        Log.i(TAG,"onCreate: sp euroRate="+euroRate);
        Log.i(TAG,"onCreate: sp krwRate="+krwRate);

        //开启子线程
        Thread t = new Thread(this);//当前对象已经实现了接口，可以作为线程运行的载体
        t.start();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    String str = (String)msg.obj;
                    Log.i(TAG, "handleMessage: getMesseage msg ="+str);
                    showTxt.setText(str);
                }
                super.handleMessage(msg);
            }
        };


    }

    public void onClick(View btn){
        String str = rmb.getText().toString();
        float r = 0;
        if(str.length()>0) {
            r = Float.parseFloat(str);
        }else{
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG,"onClick:r="+r);

        if(btn.getId()==R.id.dollar){
            showTxt.setText(String.format("%.2f",r*dollarRate));
        }else if(btn.getId()==R.id.euro){
            showTxt.setText(String.format("%.2f",r*euroRate));
        }else{
            showTxt.setText(String.format("%.2f",r*krwRate));
        }
    }

    public void openOne(View btn){
        openConfig();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_set){
            openConfig();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openConfig() {
        Intent config = new Intent(this,ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("krw_rate_key",krwRate);

        Log.i(TAG,"openOne:dollar_rate_key="+dollarRate);
        Log.i(TAG,"openOne:euro_rate_key="+euroRate);
        Log.i(TAG,"openOne:krw_rate_key="+krwRate);

        startActivityForResult(config,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //通过requestCode区分到底是谁返回的数据
        //通过resultCode判断返回的到底是什么样的数据，返回的数据按什么规则去拆分

        if(requestCode==1 && resultCode==2){
            /*bdl.putFloat("key_dollar",newDollar);
            bdl.putFloat("key_euro",newEuro);
            bdl.putFloat("key_KRW",newKRW);*/
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            krwRate = bundle.getFloat("key_krw",0.1f);
            Log.i(TAG,"onActivityResult:dollarRate="+dollarRate);
            Log.i(TAG,"onActivityResult:euroRate="+euroRate);
            Log.i(TAG,"onActivityResult:krwRate="+krwRate);

            //将新设置的汇率写到SP里
            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("krw_rate",krwRate);
            //保存
            editor.commit();
            Log.i(TAG,"onActivityResult: 数据已保存到sharedPreferences");

        }


        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void run() {
        Log.i(TAG, "run: run()......");
        for(int i=1;i<6;i++){
            Log.i(TAG, "run: i="+i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage();
        msg.what = 5;
        //Message msg = handler.obtainMessage(5);
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);

        //获取网络数据
        URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();//获得输入流
            //将in转换为字符串
            String html = inputStream2String(in);
            Log.i(TAG, "run: html="+html);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //将输入流转换成字符串输出
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        for(;;){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}
