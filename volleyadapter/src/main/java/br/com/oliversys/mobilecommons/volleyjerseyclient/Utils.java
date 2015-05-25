package br.com.oliversys.mobilecommons.volleyjerseyclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Utils {

    public static List<IValueObject> fromGooglePlacesJsonToVO(IValueObject vo,JSONArray json){
        List<String> nomeProperties = new ArrayList<String>();
        List listaVO = new ArrayList();
        for(Field f:vo.getClass().getDeclaredFields())
        {
            if (f.getName().equals("reviews") || f.getName().equals("address_components")
                    || f.getName().equals("events") || f.getName().equals("formatted_address ")
                    || f.getName().equals("formatted_phone_number") || f.getName().equals("geometry")
                    || f.getName().equals("international_phone_number") || f.getName().equals("reference")
                    || f.getName().equals("types") || f.getName().equals("url") || f.getName().equals("utc_offset ")
                    || f.getName().equals("website "))
                continue;
            nomeProperties.add(f.getName());
        }

        for(int i =0; i < json.length(); i++) {
            IValueObject valueObject = getNovaInstanciaVO(vo);
            try {
                JSONObject jsonObj = json.getJSONObject(i);
                for(String p:nomeProperties){
                    if (!jsonObj.isNull("opening_hours")) {
                        setOpeningHours(jsonObj, valueObject);
                    }

                    if (!jsonObj.isNull("reviews"))
                        setComentarios(jsonObj, valueObject);

                    if (!jsonObj.isNull(p)) {
                        chamarSetterNoVO(valueObject, jsonObj, p);
                    }
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            listaVO.add(valueObject);
        }
        return listaVO;
    }

    private static void setComentarios(JSONObject jsonObj, IValueObject valueObject){
        JSONObject c = null;
        try {
            JSONArray jsonArray = jsonObj.getJSONArray("comments");
            for(int i =0; i < jsonArray.length(); i++) {
                c = jsonArray.getJSONObject(i); // CommentVO
                if (c != null) {
                    if (!c.isNull("aspects")) {
                        Object aspect = c.getJSONArray("aspects").get(0); // AspectVO
                        Method m1 = aspect.getClass().getDeclaredMethod("getType", String.class);
                        m1.invoke(aspect, c.getString("type"));
                        Method m2 = aspect.getClass().getDeclaredMethod("getRating", String.class);
                        m1.invoke(aspect, c.getString("rating"));
                    }
                    Method m3 = c.getClass().getDeclaredMethod("getAuthor", String.class);
                    m3.invoke(c,c.getString("author"));
                    Method m4 = c.getClass().getDeclaredMethod("getAuthorGplusUrl", String.class);
                    m4.invoke(c, c.getString("rating"));
                    Method m5 = c.getClass().getDeclaredMethod("getText", String.class);
                    m5.invoke(c,c.getString("text"));
                    Method m6 = c.getClass().getDeclaredMethod("getTime", String.class);
                    m6.invoke(c, c.getString("time"));
                    }
                }
            } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
    }

    private static void setOpeningHours(JSONObject jsonObj, IValueObject valueObject){
        JSONObject c = null;
        try {
            c = jsonObj.getJSONObject("opening_hours");
            if (c != null) {
                Method m1 = valueObject.getClass().getDeclaredMethod("setIsOpen", Boolean.class);
                m1.invoke(valueObject, c.get("open_now").toString());
            }
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

    public static List<IValueObject> fromJsonToVO(IValueObject vo,JSONArray json)   {
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

    /*  {
          "PROMOCIONAL":[{"id":1,"ingredientes":[],"categoriaCardapio":"PROMOCIONAL","maisPedida":true,
                    "descricao":"calabresa fatiada,cebola,azeitonas e oregano","pedido":null,"fotoURL":null,
                    "preco":23,"avaliacao":3.0,"nome":"CALABRESA"}],
           "DOCE":[{"id":3,"ingredientes":[],
                    "categoriaCardapio":"DOCE","maisPedida":false,"descricao":"chocolate, leite condensado e granulado",
                    "pedido":null,"fotoURL":null,"preco":20,"avaliacao":2.0,"nome":"BRIGADEIRO"}],
           "ESPECIAL":[{"id":2,"ingredientes":[],"categoriaCardapio":"ESPECIAL","maisPedida":false,
                    "descricao":"mussalera,pepperonni(salame condimentado) e oregano","pedido":null,"fotoURL":null,
                    "preco":28,"avaliacao":4.0,"nome":"PEPERONNI"}]}]
       }    */
    public static Map<String,List<IValueObject>> jsonToMapaVO(JSONObject json,IValueObject vo){
        Map<String, List<IValueObject>> pizzasPorCategoria = new HashMap<String, List<IValueObject>>();
        try {
            List<String> nomeProperties = new ArrayList<String>();
            for (Field f : vo.getClass().getDeclaredFields())
                nomeProperties.add(f.getName());

            List listaVO = new ArrayList();
            String categoria = null;
            JSONArray jsonArray = null;
            // itera os nomes dos arrays
            for (Iterator iterator = json.keys(); iterator.hasNext(); ) {
                String nomeArray = (String) iterator.next();
                // recupera o array c/ o nome iterado
                jsonArray = json.getJSONArray(nomeArray);
                // itera os objetos do array recuperado
                for (int j = 0; j < jsonArray.length(); j++) {
                    IValueObject valueObject = getNovaInstanciaVO(vo);
                    JSONObject jsonObj = jsonArray.getJSONObject(j);
                    // itera as propriedades do VO que sao as mesmas do obj iterado
                    for (String p : nomeProperties) {
                        if (!jsonObj.isNull(p)) {
                            // seta o valor da propriedade do obj na mesma propriedade do VO
                            chamarSetterNoVO(valueObject, jsonObj, p);
                            if (p.equals("categoriaCardapio")) {
                                // armazena o nome da categoria da pizza (obj iterado)
                                categoria = jsonObj.getString(p);
                            }
                        }
                    }
                    listaVO.add(valueObject);
                }
                pizzasPorCategoria.put(categoria, new ArrayList<IValueObject>(listaVO));
                listaVO.clear();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return pizzasPorCategoria;
    }

}
