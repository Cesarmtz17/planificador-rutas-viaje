package util;

import model.Lugar;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {
    
    public static List<Lugar> leerLugares(String rutaArchivo) throws IOException {
        List<Lugar> lugares = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Saltar encabezados
                
                String nombre = row.getCell(0).getStringCellValue();
                String tipo = row.getCell(1).getStringCellValue();
                double tiempoVisita = row.getCell(2).getNumericCellValue();
                boolean esAeropuerto = nombre.toLowerCase().contains("aeropuerto");
                boolean esHotel = nombre.toLowerCase().contains("hotel");
                
                lugares.add(new Lugar(nombre, tipo, tiempoVisita, esAeropuerto, esHotel));
            }
        }
        
        return lugares;
    }
    
    public static Map<String, Map<String, Double>> leerDistancias(String rutaArchivo) throws IOException {
        Map<String, Map<String, Double>> distancias = new HashMap<>();
        
        try (FileInputStream fis = new FileInputStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String origen = row.getCell(0).getStringCellValue();
                Map<String, Double> distanciasOrigen = new HashMap<>();
                
                for (int j = 1; j < headerRow.getLastCellNum(); j++) {
                    String destino = headerRow.getCell(j).getStringCellValue();
                    double distancia = row.getCell(j).getNumericCellValue();
                    distanciasOrigen.put(destino, distancia);
                }
                
                distancias.put(origen, distanciasOrigen);
            }
        }
        
        return distancias;
    }
    
    public static Map<String, Map<String, Double>> leerTiempos(String rutaArchivo) throws IOException {
        return leerDistancias(rutaArchivo); // Misma estructura que las distancias
    }
} 