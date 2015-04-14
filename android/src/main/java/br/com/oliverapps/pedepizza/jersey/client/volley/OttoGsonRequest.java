package br.com.oliverapps.pedepizza.jersey.client.volley;

import br.com.oliverapps.pedepizza.jersey.client.GsonRequest;
import br.com.oliverapps.pedepizza.jersey.client.volley.requestlib.ottolib.OttoErrorListener;
import br.com.oliverapps.pedepizza.jersey.client.volley.requestlib.ottolib.OttoSuccessListener;

import com.squareup.otto.Bus;

/** GsonRequest which passes the result on to an Otto Message Bus */
public class OttoGsonRequest<T> extends GsonRequest<T> {
    /** Request ID counter for this session */
    private static int _idCounter = 1;

    /** A ID for this request, unique for the lifetime of the process (given that you do < 2BN requests) */
    public int requestId;

    public OttoGsonRequest(Bus eventBus, String url, Class<T> classType) {
        super(url,
              classType,
              new OttoSuccessListener<T>(eventBus, _idCounter),
              new OttoErrorListener(eventBus, _idCounter));
        
        requestId = _idCounter;
        _idCounter++;
    }
}
