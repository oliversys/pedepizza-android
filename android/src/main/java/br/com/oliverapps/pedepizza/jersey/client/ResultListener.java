package br.com.oliverapps.pedepizza.jersey.client;

public interface ResultListener<T> {
	public void onSuccess(T result);
    public void onFailure(Throwable e);
}
