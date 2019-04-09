package first.swufe.com.week6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {

    public final String TAG = "ConfigActivity";

    EditText dollarText;
    EditText euroText;
    EditText krwText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent = getIntent();
        float dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        float krw2 = intent.getFloatExtra("krw_rate_key",0.0f);

        Log.i(TAG,"onCreate:dollar2="+dollar2);
        Log.i(TAG,"onCreat:euro2="+euro2);
        Log.i(TAG,"onCreate:krw2="+krw2);

        dollarText = (EditText)findViewById(R.id.dollar_rate);
        euroText = (EditText)findViewById(R.id.euro_rate);
        krwText = (EditText)findViewById(R.id.krw_rate);

        //显示数据到控件
        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        krwText.setText(String.valueOf(krw2));

    }

    public void save(View btn){
        Log.i(TAG,"save:");
        //获取新的输入数据
        float newDollar = Float.parseFloat(dollarText.getText().toString());
        float newEuro = Float.parseFloat(euroText.getText().toString());
        float newKRW = Float.parseFloat(krwText.getText().toString());

        Log.i(TAG,"save:获取到新的值");
        Log.i(TAG,"save:newDollar="+newDollar);
        Log.i(TAG,"save:newEuro="+newEuro);
        Log.i(TAG,"save:newKRW="+newKRW);

        //把数据通过intent对象保存到Bundle或是放入到Extra
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_krw",newKRW);
        intent.putExtras(bdl);
        setResult(2,intent);

        //返回到调用界面
        finish();
    }
}
