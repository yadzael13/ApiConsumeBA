package com.gs.beneficios.Entitites;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class ResponsePaginado {

    @JsonProperty("Resultados")
    private int totalResults;

    @JsonProperty("Paginas")
    private int totalPages;

    @JsonProperty("Limite")
    private int limit;

    @JsonProperty("Actual")
    private int current_page;

    @JsonProperty("Lista de beneficiados")
    private List<Object> lista_beneficiarios;

  


}
