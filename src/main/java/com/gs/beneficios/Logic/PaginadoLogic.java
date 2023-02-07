package com.gs.beneficios.Logic;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import com.gs.beneficios.Entitites.ResponsePaginado;
import com.gs.beneficios.Utilities.ApiConsume.ApiConsumeOdata;
import com.gs.beneficios.Utilities.Loggers.MyLogs;


@Service
public class PaginadoLogic {

    /** Lógica de paginado
     * @param limit
     * @param current
     * @return Objeto tipo ResponsePaginado 
     */
    public ResponsePaginado paginado(int limit, int current){

        ResponsePaginado resp = new ResponsePaginado();
        try{
            List<Object> apiList = ApiConsumeOdata.getApiList();
            List<Object> pagination_list = new ArrayList<>();
            resp.setCurrent_page(current);
            resp.setLimit(limit);
            resp.setTotalResults(ApiConsumeOdata.getTotalResults());
            //Si la division no tiene residuo, se resta 1 al resultado, si tiene se respeta
            int totalResults_aux = (resp.getTotalResults()%limit==0)?resp.getTotalResults()/limit-1:resp.getTotalResults()/limit;
            resp.setTotalPages(totalResults_aux);
           
            int start_on_list = current * limit;
            int i = 0;
            while (i<limit) {
                try {
                    pagination_list.add(apiList.get(start_on_list));
                } catch (Exception ignored) { break; }
                start_on_list++;
                i++;
            } 
            resp.setLista_beneficiarios(pagination_list);
        }catch(Exception e){
            MyLogs.LoggerError("paginado -- PaginadoLogic", "Error al obtener lista paginada", e.toString());
            resp=null;
        }
        return resp;
    }
}
