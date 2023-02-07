package com.gs.beneficios;
/*
 * author : Hiram Yadzael Vargas Chalico
 * Fecha : Enero 2023
 * Version : 1
 * Java version : funcional en java 15 y java 17
 * Aplication server :  Tomcat
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gs.beneficios.Utilities.ApiConsume.ApiConsumeOdata;

@SpringBootApplication
public class BeneficiosApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeneficiosApplication.class, args);

		//Se consume la API Odata al iniciar el proyecto, con la siguiente función se ejecuta--
		ApiConsumeOdata.consumoApiOdata();
		
		System.out.println("-------------------- Proyecto Beneficios correctamente iniciado ----------------------");
	}

}
