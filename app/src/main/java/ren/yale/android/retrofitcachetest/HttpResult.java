package ren.yale.android.retrofitcachetest;

/**
 * Created by Yale on 2017/6/13.
 */

public class HttpResult {
    private String message="";
    private boolean success = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
