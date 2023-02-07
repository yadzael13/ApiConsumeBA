package com.gs.beneficios.Controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.gs.beneficios.Entitites.Response;
import com.gs.beneficios.Entitites.ResponsePaginado;
import com.gs.beneficios.Logic.PaginadoLogic;
import com.gs.beneficios.Logic.SaveToFile;
import com.gs.beneficios.Utilities.ApiConsume.ApiConsumeOdata;
import com.gs.beneficios.Utilities.Loggers.MyLogs;

@RestController

@RequestMapping("/v1")
public class Controller {


    @Autowired
    ApiConsumeOdata api;

    @Autowired
    SaveToFile save;

    @Autowired
    PaginadoLogic pageLog;

   

    /**Servicio para obtener el consumo de API odata, retorna la información transparente
     * @return ResponseEntity con el consumo de API en memoria
     */
    @GetMapping("/ApiBeneficios")
    public ResponseEntity<?> getBeneficios(){
        try {
            return api.getApiconsume();
        } catch (Exception e) {
            MyLogs.LoggerError("getBeneficios() -- Controller", "Error en Servicio", e.toString());
            Response resp = new Response();
            resp.setCode("500");
            resp.setResult("Error en el servicio");
            resp.setResultDescription("Ha ocurrido un error al consumir api Odata");
        }
        return null;
    }

    /**Servicio para guardar en archivo de texto los datos de la API Odata -Beneficios en el formato requerido
     * @return ResponseEntity -- Resultado del flujo, en objeto tipo Response
     */
    @PostMapping("/saveTxt")
    public ResponseEntity<?> saveTxt(){
        Response resp = new Response();
        int status;
        try {
            ResponseEntity<?> r =api.getApiconsume();
            HashMap<String,Object> h1 = (HashMap<String, Object>) r.getBody();
            h1 = (HashMap<String, Object>) h1.get("d");

            List<Object> li_em = (List<Object>) h1.get("results");  
            boolean saving = save.saveText(li_em);
            if(!saving) throw new Exception("No se ha guardado el archivo de texto");
            resp.setCode("200");
            resp.setResult("Servicio consumido correctamente");
            resp.setResultDescription("Se ha creado el archivo de texto exitosamente");
            status = 200;
            
        } catch (Exception e) {
            resp.setCode("500");
            resp.setResult("Error interno en el servicio");
            resp.setResultDescription("Error al crear documento de texto");
            status = 500;
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(status)).body(resp);
    }

    /**Servicio para consumir la API en memoria local con paginado
     * @param limit -- limite de resultados por response
     * @param current_page -- pagína a consultar
     * @return - ResponseEntity tipo -ResponsePaginado
     */
    @PostMapping("/Beneficios_paginado")
    public ResponseEntity<?> beneficios_page(@RequestParam int limit, @RequestParam int current_page){
        try {
            ResponsePaginado resp_pag = pageLog.paginado(limit, current_page);
            if(current_page > resp_pag.getTotalPages()){
                Response resp_if = new Response();
                resp_if.setCode("400");
                resp_if.setResult("No se ha consumido el servicio");
                resp_if.setResultDescription("Current Page fuera de rango");
                return ResponseEntity.status(400).body(resp_if);
            }
            return ResponseEntity.status(200).body(resp_pag);
        } catch (Exception e) {
            Response resp = new Response();
            resp.setCode("500");
            resp.setResult("Error interno en el servicio");
            resp.setResultDescription("Error al crear documento de texto");
            return ResponseEntity.status(500).body(resp);
        }
    }
}
