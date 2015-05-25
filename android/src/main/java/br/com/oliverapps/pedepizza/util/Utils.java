package br.com.oliverapps.pedepizza.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.com.oliverapps.pedepizza.Constants;
import br.com.oliverapps.pedepizza.R;
import br.com.oliversys.mobilecommons.volleyjerseyclient.IValueObject;

/**
 * Created by William on 4/21/2015.
 */
public class Utils {
    public static Dialog showProgressDialog(Context ctx){
        ProgressDialog progress = new ProgressDialog(ctx, ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setMessage(ctx.getString(R.string.aguarde));
        progress.show();

        return progress;
    }

    private static void chamarSetterNoVO(IValueObject valueObject, JSONObject jsonObj, String p) {
        String methodName = "set" + p.substring(0, 1).toUpperCase() + p.substring(1);
        Method m = null;
        try {
            m = valueObject.getClass().getDeclaredMethod(methodName, String.class);
            String val = jsonObj.getString(p);
            m.invoke(valueObject, val);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }

    }

    public static List<IValueObject> fromJsonToVO(IValueObject vo,@NotNull JSONArray json){
        List<String> nomeProperties = new ArrayList<String>();
        List listaVO = new ArrayList();

        for(Field f:vo.getClass().getDeclaredFields())
        {
            if (!f.getName().equals("precoMinimo"))
                nomeProperties.add(f.getName());
        }

        for(int i =0; i < json.length(); i++) {
            IValueObject valueObject = getNovaInstanciaVO(vo);
            try {
                JSONObject jsonObj = json.getJSONObject(i);
                for(String p:nomeProperties){
                    if (!jsonObj.isNull(p)) {
                       chamarSetterNoVO(valueObject, jsonObj, p);
                    }
                }
                if (!jsonObj.isNull("cardapios")) {
                    setPrecoMinimoDoCardapio(jsonObj, valueObject);

                }
                if (!jsonObj.isNull("ingredientes")) {
                    setIngredientesNoVO(jsonObj.getJSONObject("ingredientes"), valueObject);
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            listaVO.add(valueObject);
        }
        return listaVO;
    }

    private static IValueObject getNovaInstanciaVO(IValueObject vo) {
        IValueObject valueObject = null;
        try {
            valueObject = vo.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return valueObject;
    }

    private static void setIngredientesNoVO(JSONObject jsonObj, IValueObject valueObject) throws JSONException {
        if (jsonObj.getJSONArray("ingredientes").length() == 0) {
            return;
        }

        for(int i =0; i < jsonObj.length(); i++) {
            try {
                Method m = valueObject.getClass().getDeclaredMethod("setIngredientes", String.class);
                m.invoke(valueObject, jsonObj.getString("ingredientes"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setPrecoMinimoDoCardapio(JSONObject jsonObj, IValueObject valueObject){
        JSONObject c = null;
        try {
            if (jsonObj.getJSONArray("cardapios").length() == 0) {
                return;
            }
            c = (JSONObject)jsonObj.getJSONArray("cardapios").get(0);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (c != null){
            try {
                Method m1 = valueObject.getClass().getDeclaredMethod("setCardapioId",String.class);
                m1.invoke(valueObject,c.get("id").toString());
                Method m = valueObject.getClass().getDeclaredMethod("setPrecoMinimo",String.class);
                m.invoke(valueObject,c.getString("precoMinimo"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static String getLogoURL(int n){
        if (n >= 2) return Constants.URL_LOGOS[2];
        if (n < 0) return Constants.URL_LOGOS[0];
        return Constants.URL_LOGOS[n];
    }

    public static String getFotoPizzaURL(int n)
    {
        if (n >= 2) return Constants.URL_FOTOS[2];
        if (n < 0) return Constants.URL_FOTOS[0];
        return Constants.URL_FOTOS[n];
    }

        /*
*  Converte a resposta json em lista de PizzariaRecord.
//*/
//    public static List<PizzariaRow> toPizzariaRows(@NotNull JSONArray json) throws JSONException {
//        List<PizzariaRow> pizzarias = new ArrayList<PizzariaRow>();
//        JSONArray jsonPizzarias = json;
//
//        for(int i =0; i < jsonPizzarias.length(); i++) {
//            JSONObject jsonObj = jsonPizzarias.getJSONObject(i);
//
//            PizzariaRow row = new PizzariaRow();
//
//            if (!jsonObj.isNull("nome"))
//                row.setNome(jsonObj.getString("nome"));
//            if (!jsonObj.isNull("distanciaEmKM"))
//                row.setDistanciaEmKM(jsonObj.getString("distanciaEmKM"));
//            if (!jsonObj.isNull("cardapios")) {
//                if (jsonObj.getJSONArray("cardapios").length() != 0) {
//                    JSONObject c = (JSONObject)jsonObj.getJSONArray("cardapios").get(0);
//                    if (c != null)
//                        row.setPrecoMinimo(c.getString("precoMinimo"));
//                }
//            }
//            if (!jsonObj.isNull("taxaEntrega"))
//                row.setTaxaEntrega(jsonObj.getString("taxaEntrega"));
//            if (!jsonObj.isNull("tempoMedioEntregaEmMinutos"))
//                row.setTempoMedioEntregaEmMinutos(jsonObj.getString("tempoMedioEntregaEmMinutos"));
//            if (!jsonObj.isNull("logoURL"))
//                row.setLogoPizzaria(jsonObj.getString("logoURL"));
//            if (!jsonObj.isNull("avaliacao"))
//                row.setAvaliacao(jsonObj.getString("avaliacao"));
//
//            pizzarias.add(row);
//        }
//        return pizzarias;
//    }
}
