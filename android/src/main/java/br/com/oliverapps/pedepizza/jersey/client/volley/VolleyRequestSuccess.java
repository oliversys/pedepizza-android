package br.com.oliverapps.pedepizza.jersey.client.volley;

public class VolleyRequestSuccess<T> {
    public T response;
    public int requestId;

    public VolleyRequestSuccess(int requestId, T response) {
        this.requestId = requestId;
        this.response = response;
    }
}
