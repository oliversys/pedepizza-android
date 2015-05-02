package br.com.oliverapps.pedepizza;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import br.com.oliverapps.pedepizza.pizzaria.PizzariasActivity;


public class MainActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button b = (Button) findViewById(R.id.pizzarias);
		b.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,PizzariasActivity.class);
				startActivity(i);
			}
		});
	}
}