package com.gs.beneficios.Utilities.ApiConsume;

import java.util.Arrays;

import java.util.HashMap;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gs.beneficios.Utilities.Loggers.MyLogs;


@Service
//Cosumir la api para token, obtener el token y consumir la api de forma segura
public class ApiConsumeOdata {
    private static int totalResults;
    private static ResponseEntity<?> apiconsume;
    
    /**
     * @return -- Respuesta de la api consumida
     */
    public ResponseEntity<?> getApiconsume() {
        return apiconsume;
    }

    /**
     * @return -- Longitud de la lista -- Total de empleados
     */
    public static int getTotalResults(){
        totalResults = getApiList().size();
        return totalResults;
    }

    /**
     * @return -- Lista completa de Empleados
     */
    public static List<Object> getApiList(){
        try {
            HashMap<?,?> aux =(HashMap<?,?>) apiconsume.getBody();
            aux = (HashMap<?,?>) aux.get("d");
            return (List<Object>) aux.get("results");
        } catch (Exception e) {
           MyLogs.LoggerError("getApiList", "cannot get results from api", e.toString());
           return null;
        }
       
    }
  
    //Objeto restTemplate
    static RestTemplate rest = new RestTemplate();

    /**Consume la API Odata de URL_PATH
     * Se inicializa el consumo de la API cuando se inicia el proyecto
     */
    public static void consumoApiOdata(){
        /*-------------------- API EN AMBIENTE DE CALIDAD, MARCO, (consume la funcion para API de token) --------------
         *            Para consumir desde ambiente de calidad sin desacitvar la verificacion SSL, descomentar estas líneas
         *                  y comentar los bloques de código del otro try - catch
        */

        try {
           String URL_PATH = "https://e5735-iflmap.hcisbt.us2.hana.ondemand.com/http/BW_to_GS/requestGeneric";
              //Armado de headers para consumo
            HttpHeaders httpHeaders = new HttpHeaders();
           // httpHeaders.add("Authorization", "Basic " + encodedCredentials);
            httpHeaders.add("Authorization", "Bearer " + getTokenFromApi());
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            //Apagar la verificacion SSL
            //SSLUtil.turnOffSslChecking();
        
            HttpEntity<?> entity = new HttpEntity(httpHeaders);
            MyLogs.LoggerInfo("Consumo de Api ODATA", "Comenzando consumo..."+"\nURL----: "+URL_PATH);
            
            //Consumo con Exchange
            apiconsume = rest.exchange(URL_PATH, HttpMethod.GET, entity, HashMap.class);
            //Encender la verificacion SSL
            //SSLUtil.turnOnSslChecking();
            MyLogs.LoggerInfo("Consumo de Api ODATA", "Consumo de API correcto!");
            
           
        } catch (Exception e) {
            MyLogs.LoggerError("ApiConsumeOdata","Error al consumir servicio", e.toString());
            apiconsume = null;
        }  

        /* ---------------------------- API ORIGINAL Beneficios echa por Gustavo Vela, sin filtro ---------------------------------*/

        // try {
        //     String URL_PATH = "https://qabw.sistemasbo.corp:8443/sap/opu/odata/sap/ZCB_PLAHODA_CDS/ZCB_PLAHODA?$format=json";
        //     String adminuserCredentials = "ZEUSODATA:Qu5ServOdaZ";
        //     String encodedCredentials =
        //             new String(Base64.encodeBase64(adminuserCredentials.getBytes(),false));
        //     HttpHeaders httpHeaders = new HttpHeaders();
        //     httpHeaders.add("Authorization", "Basic " + encodedCredentials);
        //     httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        //      //Apagar la verificacion SSL
        //      SSLUtil.turnOffSslChecking();
        //      HttpEntity<?> entity = new HttpEntity(httpHeaders);
        //      MyLogs.LoggerInfo("Consumo de Api", "Comenzando consumo..."+"\nURL----: "+URL_PATH);
             
        //      //Consumo con Exchange
        //      apiconsume = rest.exchange(URL_PATH, HttpMethod.GET, entity, HashMap.class);
        //      //Encender la verificacion SSL
        //      SSLUtil.turnOnSslChecking();
        //      MyLogs.LoggerInfo("Consumo de Api", "Consumo de API correcto!");
    
        // } catch (Exception e) {
        //     MyLogs.LoggerError("ApiConsumeOdata","Error al consumir servicio", e.toString());
        //     apiconsume =  null;
        // }
    }

    /**Consume el servcio para Token de URL_TOKEN, se consume con API en ambiente de calidad
     * @return String -- token de servicio
     */
    private static String getTokenFromApi(){
        
        try {
            String URL_TOKEN = "https://oauthasservices-d4006e1b2.us2.hana.ondemand.com/oauth2/api/v1/token?grant_type=client_credentials";
            //Armado de headers para api token 
            String adminuserCredentials = "appbeneficiosfi:R*R;Q9Yw\"(";
            String encodedCredentials =
                    new String(Base64.encodeBase64(adminuserCredentials.getBytes(),false));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Basic " + encodedCredentials);
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));


            //consumo de api para token
            HttpEntity<?> entity = new HttpEntity(httpHeaders);
            MyLogs.LoggerInfo("Consumo de Api para token", "Comenzando consumo y obteniendo token..."+"\nURL----: "+URL_TOKEN);
            
            //Consumo con Exchange
            ResponseEntity<?> res = rest.exchange(URL_TOKEN, HttpMethod.POST, entity, HashMap.class);
            
            int status = res.getStatusCode().value();
            if(status!=200) throw new Exception("Failed consume to token api");
            HashMap<String, Object> respuesta_token = (HashMap<String, Object>) res.getBody();

            String token = (String) respuesta_token.get("access_token");

            MyLogs.LoggerInfo("Token obtenido", "Consumo de API para token correcto! --- TOKEN : "+token);
            return token;
        } catch (Exception e) {
            MyLogs.LoggerError("getTokenFromApi -- ApiConsumeOdata", "Error al obtener el token", e.toString());
            return "";
        }
    }
}
