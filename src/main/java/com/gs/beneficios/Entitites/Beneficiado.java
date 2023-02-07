package com.gs.beneficios.Entitites;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class Beneficiado {

    @JsonProperty("__metadata")
    private Object __metadata;
    
    @JsonProperty("IdEmpleado")
    private String IdEmpleado;
    
    @JsonProperty("Nombre")
    private String Nombre;
    
    @JsonProperty("ApellidoPaterno")
    private String ApellidoPaterno;
    
    @JsonProperty("ApellidoMaterno")
    private String ApellidoMaterno;
    
    @JsonProperty("FechaContratacion")
    private String FechaContratacion;
    
    @JsonProperty("FechaDeNacimiento")
    private String FechaDeNacimiento;
    
    @JsonProperty("Estatus")
    private String Estatus;

    
}
