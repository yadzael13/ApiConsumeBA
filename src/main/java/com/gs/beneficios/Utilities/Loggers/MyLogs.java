package com.gs.beneficios.Utilities.Loggers;


import java.util.logging.Logger;

import org.springframework.stereotype.Service;




@Service
public class MyLogs {
    private static Logger logger = Logger.getLogger(" MY LOGGERS ------------ * * *");
    private static String sep1 = "\n-----------------------------";
    private static String sep2 = "--------------------------- * * *\n";

    public static void LoggerInfo(String path, String issue){
        String Message = sep1+" INFO "+sep2+"Actividad: "+path+"\n"+"Informacion: "+issue+"\n\n";
        logger.info(Message);
    }

    public static void LoggerError(String ubication, String info, String issue){
        String Message = sep1+" ERROR "+sep2+"Ubicacion: "+ubication+"\n"+"Info: "+info+"\n"+"Problema: "+issue+"\n\n";
        logger.warning(Message);

    }
    
}
