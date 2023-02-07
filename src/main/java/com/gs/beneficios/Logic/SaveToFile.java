package com.gs.beneficios.Logic;


import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gs.beneficios.Entitites.Beneficiado;
import com.gs.beneficios.Utilities.Loggers.MyLogs;

//import java.io.FileWriter;
import java.io.OutputStreamWriter;

import java.util.List;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

@Service
public class SaveToFile {
    
    /**Guarda en un documento de texto los datos obtenidos de la API, Usando BufferedWritter
     * @param lis_emp -- Lista extraida del consumo
     * @return -- true si se ha realizado el proceso correctamente, false si ha ocurrido un error
     * @throws FileNotFoundException
     */
    public boolean saveText(List<Object> lis_emp) throws FileNotFoundException, IOException{
        boolean status;
        
        try {
            OutputStreamWriter outputStream = new OutputStreamWriter(
                            new FileOutputStream("beneficios/Registros/Beneficiados.txt"),
                            Charset.forName("UTF-8").newEncoder());
             
            int i = 0;
            int lis_size =lis_emp.size();
            BufferedWriter bfwriter = new BufferedWriter(outputStream);
            while(i<lis_size){
                
                Beneficiado em = convert_to_empleado(lis_emp.get(i));
                String string_employee_line = (i!=lis_size-1)?getEmployeeLine(em)+"\n":getEmployeeLine(em);
                bfwriter.write(string_employee_line);
                i++;
            }
            bfwriter.close();
            status = true;
        } catch (Exception e) {
            MyLogs.LoggerError("saveText() -- Logic", "Error al crear txt", e.toString());
            status = false;
        }
        return status; 
    }

    /**Convierte un objeto en tipo Beneficiado para trabajar en plantilla, usando ObjectMapper
     * @param ob -- Objeto
     * @return Empleado -- Objeto tipo Beneficiado
     */
    private Beneficiado convert_to_empleado(Object ob) throws Exception{
        Beneficiado emp = new Beneficiado();
            ObjectMapper mapper = new ObjectMapper();
            emp = mapper.convertValue(ob, Beneficiado.class);
        return emp;
     
    }

    /**Extrae de un objeto tipo Empleado y retorna un String con el formato soicitado
     * @param emp -- Objeto para extraer datos
     * @return String -- Cadena en formato solicitado
     */
    private String getEmployeeLine(Beneficiado emp){
        return emp.getIdEmpleado()+"|"+emp.getNombre()+"|"+emp.getApellidoPaterno()+"|"
                +emp.getApellidoMaterno() +"|"+emp.getFechaContratacion()+"|"
                +emp.getFechaDeNacimiento()+"|"+emp.getEstatus();
    }
    
}
