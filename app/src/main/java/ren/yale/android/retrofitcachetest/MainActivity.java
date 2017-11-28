package ren.yale.android.retrofitcachetest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import ren.yale.android.retrofitcachelib.RetrofitCache;
import ren.yale.android.retrofitcachetest.bean.GankAndroid;
import rx.Subscriber;
import rx.Subscription;

public class MainActivity extends Activity {

    OKHttpUtils mOKHttpUtils;
    String mDir="";
    private Subscription mSubscription;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OKHttpUtils.INSTANCE.init(this.getApplication());
        RetrofitCache.getInatance().init(this);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv_content);
        TestInstance.INSTANCE.count++;

    }

    private void test(){
        OKHttpUtils.INSTANCE.getApi().getGankAndroid("aaa")
                .compose(OKHttpUtils.<GankAndroid>IoMain())
                .subscribe(new Subscriber<GankAndroid>() {
                    @Override
                    public void onStart() {
                        mTextView.setText("");
                    }

                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(GankAndroid gankAndroid) {
                        mTextView.setText(JSON.toJSONString(gankAndroid));
                    }
                });
    }
    public void onClick(View v){

        test();
        //startActivity(new Intent(this,Main2Activity.class));
    }
}
