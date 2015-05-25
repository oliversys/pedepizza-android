package br.com.oliversys.mobilecommons.volleyjerseyclient;


import com.android.volley.VolleyError;

public class VolleyRequestFailed {
    public int requestId;
    public VolleyError error;

    public VolleyRequestFailed(int requestId, VolleyError error) {
        this.requestId = requestId;
        this.error = error;
    }
}
