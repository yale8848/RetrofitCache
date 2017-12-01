package ren.yale.android.retrofitcachetest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import ren.yale.android.retrofitcachelib.RetrofitCache;
import ren.yale.android.retrofitcachetest.bean.GankAndroid;
import ren.yale.android.retrofitcachetest.rx1.OKHttpUtilsRx1;
import ren.yale.android.retrofitcachetest.rx2.OKHttpUtilsRx2;
import rx.Subscriber;

public class MainActivity extends Activity {

    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OKHttpUtilsRx1.INSTANCE.init(this.getApplication());
        OKHttpUtilsRx2.INSTANCE.init(this.getApplication());
        RetrofitCache.getInstance().init(this).enableMock(false);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv_content);
    }

    private void testRx1(){
        OKHttpUtilsRx1.INSTANCE.getApi().getGankAndroid("bb")
                .compose(OKHttpUtilsRx1.<GankAndroid>IoMain())
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

                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(GankAndroid gankAndroid) {
                        mTextView.setText(JSON.toJSONString(gankAndroid));
                    }
                });
    }
    private void testRx1RamMock(){
        OKHttpUtilsRx1.INSTANCE.getApi().getRamMockGankAndroid()
                .compose(OKHttpUtilsRx1.<GankAndroid>IoMain())
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

                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(GankAndroid gankAndroid) {
                        mTextView.setText(JSON.toJSONString(gankAndroid));
                    }
                });
    }
    private void testRx1UrlMock(){
        OKHttpUtilsRx1.INSTANCE.getApi().getUrlMockGankAndroid()
                .compose(OKHttpUtilsRx1.<GankAndroid>IoMain())
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

                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(GankAndroid gankAndroid) {
                        mTextView.setText(JSON.toJSONString(gankAndroid));
                    }
                });
    }
    private void testRx2(){
        OKHttpUtilsRx2.INSTANCE.getApi().getGankAndroid()
                .compose(OKHttpUtilsRx2.INSTANCE.<GankAndroid>IoMain())
                .subscribe(new Subject<GankAndroid>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super GankAndroid> observer) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GankAndroid gankAndroid) {
                        mTextView.setText(JSON.toJSONString(gankAndroid));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void testRx1AsssetslMock(){
        OKHttpUtilsRx1.INSTANCE.getApi().getAssetsMockGankAndroid()
                .compose(OKHttpUtilsRx1.<GankAndroid>IoMain())
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

                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(GankAndroid gankAndroid) {
                        mTextView.setText(JSON.toJSONString(gankAndroid));
                    }
                });
    }
    public void onClickRx1AssetsMock(View v){
        testRx1AsssetslMock();
    }
    public void onClickRx1(View v){

        testRx1();
    }
    public void onClickRx1RamMock(View v){
        testRx1RamMock();
    }
    public void onClickRx1UrlMock(View v){
        testRx1UrlMock();
    }
    public void onClickRx2(View v){
        mTextView.setText("");
        testRx2();
    }
}
