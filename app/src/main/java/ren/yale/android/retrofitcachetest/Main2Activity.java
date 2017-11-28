package ren.yale.android.retrofitcachetest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class Main2Activity extends Activity {

    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mTextView = (TextView) findViewById(R.id.tv_text);
        mHandler.sendEmptyMessageDelayed(0,5000);
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mTextView.setText(TestInstance.INSTANCE.count+"");
        }
    };
}
