package br.com.oliverapps.pedepizza;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import br.com.oliverapps.pedepizza.pizzaria.PizzariasActivity;

public class MainActivity extends FragmentActivity
		implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private boolean isResolvendoErro = false;
	// Request code to use when launching the resolution activity
	private static final int REQUEST_RESOLVE_ERROR = 1001;
	private static final String STATE_RESOLVING_ERROR = "ESTADO_RESOLUCAO_ERRO";
	// Unique tag for the error dialog fragment
	private static final String DIALOG_ERROR = "dialog_error";

	private Location mLastLocation;
	private static final String LOCATION_KEY = "LAST_KNOWN_LOCATION";
	private AddressResultReceiver mResultReceiver;
	private ArrayList<String> enderecosRecuperados = new ArrayList<String>();
	private boolean isRecuperarEnderecoPendente = true;

	private static String enderecoSelecionado;

	private GoogleApiClient mGoogleApiClient;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState != null) {
			isResolvendoErro = (savedInstanceState != null
					&& savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false));
			mLastLocation = savedInstanceState.getParcelable(LOCATION_KEY);
		}

		mResultReceiver = new AddressResultReceiver(new Handler());

		Button pizzariasBtn = (Button) findViewById(R.id.pizzarias);
		pizzariasBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chamarPizzariasActivity();
			}
		});

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	public void chamarPizzariasActivity() {
		Intent i = new Intent(MainActivity.this, PizzariasActivity.class);
		startActivity(i);
	}

	public void onDialogConfirmed(String numEnd) {
		enderecoSelecionado += "," + numEnd;
		armazenarEnderecoNoSharedPreferences(enderecoSelecionado);
	}

	private void armazenarEnderecoNoSharedPreferences(String enderecoEntrega)
	{
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("ENDERECO_ENTREGA", enderecoEntrega);
		editor.commit();
	}


	// Tenta conectar ao Google Pay Services no inicio da atividade
	@Override
	public void onStart() {
		super.onStart();
		if (!isResolvendoErro) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	public void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	// Apos conexao bem-sucedida, chama o svc de "conversao de coordenadas"(geocode) em endereco
	@Override
	public void onConnected(Bundle bundle) {
		Toast.makeText(this, "Conectado ao Google Pay Services com sucesso", Toast.LENGTH_LONG).show();

		// usa o FusionLocationProvider para recuperar as coordenadas da ultima localizacao do dispositivo
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (mLastLocation != null) {
			// Determine whether a Geocoder is available.
			if (!Geocoder.isPresent()) {
				Toast.makeText(this, "Problemas na recuperacao do GeoCoder. " +
						" Verifique sua conexao internet", Toast.LENGTH_LONG).show();
				return;
			}
			if (isRecuperarEnderecoPendente) {
				startFetchAddressService();
			}
		}
	}

	// Chama o svc de recuperacao de endereco baseado no obj Location recuperado da GoogleAPI Location
	protected void startFetchAddressService() {
		if (mGoogleApiClient.isConnected() && mLastLocation != null) {
			Intent intent = new Intent(this, FetchAddressIntentService.class);
			intent.putExtra(Constants.RECEIVER, mResultReceiver);
			intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
			startService(intent);
			isRecuperarEnderecoPendente = false;
		}
		else{
			isRecuperarEnderecoPendente = true;
		}
	}

	// Chamado pelo GoogleApiClient apos algum erro de conexao ser resolvido
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_RESOLVE_ERROR) {
			isResolvendoErro = false;
			if (resultCode == MainActivity.RESULT_OK) {
				// Make sure the app is not already connected or attempting to connect
				if (!mGoogleApiClient.isConnecting() &&
						!mGoogleApiClient.isConnected()) {
					mGoogleApiClient.connect();
				}
			}
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		tentarResolverErro(result);
	}

	/**
	 * Se o erro tem solucao (ex: erro de autenticacao G+),
	 * o Android exibe dialogos para o usuario (ex: tela de sign in).
	 * Esse metodo evita multiplas chamadas ao metodo "connect" do GoogleAPIClient
	 * caso o app seja suspenso ou essa activity seja recriada.
	 *
	 * @param result resultado da conexao a GoogleAPI.
	 */
	private void tentarResolverErro(ConnectionResult result) {
		if (isResolvendoErro) {
			// Already attempting to resolve an error.
			return;
		} else if (result.hasResolution()) {
			try {
				isResolvendoErro = true;
				result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
			} catch (IntentSender.SendIntentException e) {
				// There was an error with the resolution intent. Try again.
				mGoogleApiClient.connect();
			}
		} else {
			// Show dialog using GooglePlayServicesUtil.getErrorDialog()
			showErrorDialog(result.getErrorCode());
			isResolvendoErro = true;
		}
	}

	/* Creates a dialog for an error message */
	private void showErrorDialog(int errorCode) {
		// Create a fragment for the error dialog
		ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
		// Pass the error that should be displayed
		Bundle args = new Bundle();
		args.putInt(DIALOG_ERROR, errorCode);
		dialogFragment.setArguments(args);
		dialogFragment.show(getSupportFragmentManager(), "errordialog");
	}

	/* Called from ErrorDialogFragment when the dialog is dismissed. */
	public void onDialogConfirmed() {
		isResolvendoErro = false;
	}

	/* A fragment to display an error dialog */
	public static class ErrorDialogFragment extends DialogFragment {
		public ErrorDialogFragment() { }

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Get the error code and retrieve the appropriate dialog
			int errorCode = this.getArguments().getInt(DIALOG_ERROR);
			return GooglePlayServicesUtil.getErrorDialog(errorCode,
					this.getActivity(), REQUEST_RESOLVE_ERROR);
		}
	}

	class AddressResultReceiver extends ResultReceiver {
		public AddressResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			enderecosRecuperados = resultData.getStringArrayList(Constants.RESULT_DATA_KEY);
			if (enderecosRecuperados != null && !enderecosRecuperados.isEmpty())
				exibirDialogoEndereco();
			else
				Log.e("exibirDialogoEndereco", "Enderecos nao recuperados da API Location");
		}
	}

	private void exibirDialogoEndereco(){
		CharSequence[] ends = enderecosRecuperados.toArray(new CharSequence[enderecosRecuperados.size()]);

		new MaterialDialog.Builder(this)
				.title("Endereco de Entrega")
				.listSelector(R.drawable.selector)
				.titleGravity(GravityEnum.CENTER)
				.contentGravity(GravityEnum.CENTER)
				.itemsGravity(GravityEnum.CENTER)
				.buttonsGravity(GravityEnum.CENTER)
				.items(ends)
				.itemsCallback(new MaterialDialog.ListCallback() {
					@Override
					public void onSelection(MaterialDialog dialog, View view, int posItemSelecionado, CharSequence text)
					{
						new MaterialDialog.Builder(MainActivity.this)
								.title(R.string.title_dialog_numero)
								.inputMaxLength(6)
								//.inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
								.input(null, null, new MaterialDialog.InputCallback() {
									@Override
									public void onInput(MaterialDialog dialog, CharSequence input) {
										enderecoSelecionado = input.toString();
									}
								}).show();
					}
				})
				.show();
	}

	public static class NumEnderecoDialogFragment extends DialogFragment {

		private static String numeroInserido;

		public static String getNumeroInserido(){
			return numeroInserido;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			LayoutInflater inflater = getActivity().getLayoutInflater();
			final View tela = inflater.inflate(R.layout.dlg_numero_endereco, null);
			builder.setView(tela)
					.setTitle("Informe o Numero")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							EditText e = (EditText) tela.findViewById(R.id.numero);
							numeroInserido = e.getText().toString();
							((MainActivity)getActivity()).onDialogConfirmed(numeroInserido);
						}
					})
					.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							NumEnderecoDialogFragment.this.getDialog().cancel();
						}
					});

			return builder.create();
		}
	}

}