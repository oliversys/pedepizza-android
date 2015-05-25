package br.com.oliverapps.pedepizza;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by William on 5/7/2015.
 */
public class FetchAddressIntentService extends IntentService{

    private ResultReceiver mReceiver;

    public FetchAddressIntentService(String name) {
        super(name);
    }

    public FetchAddressIntentService(){
        super(null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Address> enderecos = null;
        String errorMessage = null;

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf("RECUPERACAO DO RESULT RECEIVER", "No receiver received. There is nowhere to send the results.");
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        try {
            enderecos = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 3);
        } catch (IOException e) {
            e.printStackTrace();
            deliverResultToReceiver(Constants.FAILURE_RESULT, null);
            Toast.makeText(this,"Erro de conexao ao Google Play Services",Toast.LENGTH_LONG).show();
            return;
        }

        // Handle case where no address was found.
        if (enderecos == null || enderecos.size()  == 0) {
            if (errorMessage != null && errorMessage.isEmpty()) {
                errorMessage = "NENHUM ENDERECO ENCONTRADO PARA A SUA LOCALIZACAO";
                Log.e("FETCH_ADDRESS_SVC", errorMessage);
                deliverResultToReceiver(Constants.SUCCESS_RESULT, null);
            }
            ArrayList<String> erro = new ArrayList<>();
            erro.add(errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT, erro);
        } else {
            ArrayList<String> addressFragments = new ArrayList<String>();
            ArrayList<String> enderecosStr = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(Address a:enderecos){
                for(int i = 0; i < a.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(a.getAddressLine(i));
                }
                String endCompleto = TextUtils.join(System.getProperty("line.separator"), addressFragments);
                Log.i("FETCH_ADDRESS_SVC", "Endereco encontrado: " + endCompleto);
                enderecosStr.add(endCompleto);
            }
            deliverResultToReceiver(Constants.SUCCESS_RESULT,enderecosStr);
        }
    }

    private void deliverResultToReceiver(int resultCode, ArrayList<String> enderecos) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constants.RESULT_DATA_KEY, enderecos);
        try{
            mReceiver.send(resultCode, bundle);
        }
        catch(Exception e){
            e.printStackTrace();
            Log.e(getClass().getSimpleName(),Log.getStackTraceString(e));
        }

    }
}
