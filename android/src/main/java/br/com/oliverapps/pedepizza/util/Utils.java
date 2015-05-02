package br.com.oliverapps.pedepizza.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.com.oliverapps.pedepizza.br.com.oliverapps.pedepizza.vo.IValueObject;

/**
 * Created by William on 4/21/2015.
 */
public class Utils {

    public static final String[] URL_LOGOS = {
        "http://1.bp.blogspot.com/-56NuqWT8row/TrekTZ860zI/AAAAAAAABII/orNikJDH-2I/s1600/Pizzarias_Osasco.bmp",
        "http://2.bp.blogspot.com/-gIgZyz9FyVE/ToEMJboW4_I/AAAAAAAADbU/uI2H5Aw0U7Q/s1600/PIZZARIAS_Na_aclimacao_SP.bmp",
        "http://2.bp.blogspot.com/-F1ulJMI7YCI/UW4ngKmhNSI/AAAAAAAAAMg/KOLEmL8FR-w/s1600/logo-pizzaria.jpg"
    };

    public static final String[] URL_FOTOS = {
        "http://perlbal.hi-pi.com/blog/1705472/mn/133605109542-minipizza.jpg",
        "http://3.kekantoimg.com/BEihcy6TlQh7krPcmxDuZlXNTB8=/50x50/s3.amazonaws.com/kekanto_pics/pics/411/661411.jpg",
        "http://2.kekantoimg.com/TRZ7uPit19RQrhBqoIH0zy5UGpM=/50x50/s3.amazonaws.com/kekanto_pics/pics/390/379390.jpg"
    };


    public static List<IValueObject> fromJsonToVO(IValueObject vo,@NotNull JSONArray json) throws JSONException {
        List<String> nomeProperties = new ArrayList<String>();
        List listaVO = new ArrayList();

        for(Field f:vo.getClass().getDeclaredFields())
        {
            if (!f.getName().equals("precoMinimo"))
            nomeProperties.add(f.getName());
        }

        for(int i =0; i < json.length(); i++) {
            JSONObject jsonObj = json.getJSONObject(i);
            IValueObject valueObject = null;
            try {
                valueObject = vo.getClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            for(String p:nomeProperties){
                if (!jsonObj.isNull(p)) {
                    try {
                        String methodName = "set" + p.substring(0, 1).toUpperCase() + p.substring(1);
                        Method m = valueObject.getClass().getDeclaredMethod(methodName,String.class);
                        String val = jsonObj.getString(p);
                        m.invoke(valueObject, val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!jsonObj.isNull("cardapios")) {
                if (jsonObj.getJSONArray("cardapios").length() != 0) {
                    setPrecoMinimoDoCardapio(jsonObj, valueObject);
                }
            }

            listaVO.add(valueObject);
        }
        return listaVO;
    }

    private static void setPrecoMinimoDoCardapio(JSONObject jsonObj, IValueObject valueObject) throws JSONException {
        JSONObject c = (JSONObject)jsonObj.getJSONArray("cardapios").get(0);
        if (c != null){
            try {
                Method m = valueObject.getClass().getDeclaredMethod("setPrecoMinimo",String.class);
                m.invoke(valueObject,c.getString("precoMinimo"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getLogoURL(int n){
        if (n >= 2) return URL_LOGOS[2];
        if (n < 0) return URL_LOGOS[0];
        return URL_LOGOS[n];
    }

    public static String getFotoPizzaURL(int n)
    {
        if (n >= 2) return URL_FOTOS[2];
        if (n < 0) return URL_FOTOS[0];
        return URL_FOTOS[n];
    }

    //    /*
//*  Converte a resposta json em lista de PizzariaRecord.
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
