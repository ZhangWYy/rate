package first.swufe.com.week6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class week6 extends AppCompatActivity {

    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float krwRate = 0.3f;

    EditText rmb;
    TextView showTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week6);

        rmb = (EditText) findViewById(R.id.rmb);
        showTxt = (TextView) findViewById(R.id.showTxt);
    }

    public void onClick(View btn){
        String str = rmb.getText().toString();
        float r = 0;
        if(str.length()>0) {
            r = Float.parseFloat(str);
        }else{
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
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
        }


        super.onActivityResult(requestCode,resultCode,data);
    }

}
