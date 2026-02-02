package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CreadorExcel {
    
    public static void crearArchivosExcel() throws IOException {
        // Lista de lugares
        List<String[]> lugares = Arrays.asList(
            new String[]{"Nombre", "Tipo", "TiempoVisita"},
            new String[]{"Aeropuerto George Bush", "Aeropuerto", "0"},
            new String[]{"Hotel Marriott", "Hotel", "0"},
            new String[]{"Museo de Ciencias Naturales", "Museo", "3"},
            new String[]{"Zoológico de Houston", "Parque", "4"},
            new String[]{"Museo de Bellas Artes", "Museo", "2.5"},
            new String[]{"Parque Hermann", "Parque", "2"},
            new String[]{"Acuario de Houston", "Acuario", "3"},
            new String[]{"Estadio Minute Maid", "Estadio", "2"},
            new String[]{"Centro Espacial NASA", "Museo", "4"},
            new String[]{"Mercado de Agricultores", "Mercado", "1.5"}
        );

        // Crear archivo de lugares
        crearArchivoLugares(lugares);
        
        // Crear archivos de distancias y tiempos
        crearArchivoDistancias();
        crearArchivoTiempos();
    }

    private static void crearArchivoLugares(List<String[]> lugares) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Lugares");
            
            for (int i = 0; i < lugares.size(); i++) {
                Row row = sheet.createRow(i);
                String[] lugar = lugares.get(i);
                
                for (int j = 0; j < lugar.length; j++) {
                    Cell cell = row.createCell(j);
                    if (j == 2 && i > 0) { // Tiempo de visita es numérico
                        cell.setCellValue(Double.parseDouble(lugar[j]));
                    } else {
                        cell.setCellValue(lugar[j]);
                    }
                }
            }
            
            try (FileOutputStream fileOut = new FileOutputStream("lugares.xlsx")) {
                workbook.write(fileOut);
            }
        }
    }

    private static void crearArchivoDistancias() throws IOException {
        String[] lugares = {
            "Aeropuerto George Bush", "Hotel Marriott", "Museo de Ciencias Naturales",
            "Zoológico de Houston", "Museo de Bellas Artes", "Parque Hermann",
            "Acuario de Houston", "Estadio Minute Maid", "Centro Espacial NASA",
            "Mercado de Agricultores"
        };

        // Matriz de distancias en kilómetros
        double[][] distancias = {
            {0, 25, 30, 35, 28, 32, 27, 29, 40, 26},
            {25, 0, 5, 8, 3, 4, 6, 7, 15, 4},
            {30, 5, 0, 6, 4, 5, 7, 8, 20, 6},
            {35, 8, 6, 0, 7, 8, 10, 11, 25, 9},
            {28, 3, 4, 7, 0, 2, 4, 5, 18, 3},
            {32, 4, 5, 8, 2, 0, 3, 4, 19, 5},
            {27, 6, 7, 10, 4, 3, 0, 2, 22, 7},
            {29, 7, 8, 11, 5, 4, 2, 0, 23, 8},
            {40, 15, 20, 25, 18, 19, 22, 23, 0, 16},
            {26, 4, 6, 9, 3, 5, 7, 8, 16, 0}
        };

        crearArchivoMatriz("distancias.xlsx", lugares, distancias, "Distancias (km)");
    }

    private static void crearArchivoTiempos() throws IOException {
        String[] lugares = {
            "Aeropuerto George Bush", "Hotel Marriott", "Museo de Ciencias Naturales",
            "Zoológico de Houston", "Museo de Bellas Artes", "Parque Hermann",
            "Acuario de Houston", "Estadio Minute Maid", "Centro Espacial NASA",
            "Mercado de Agricultores"
        };

        // Matriz de tiempos en horas (considerando tráfico)
        double[][] tiempos = {
            {0, 0.5, 0.6, 0.7, 0.55, 0.65, 0.55, 0.6, 0.8, 0.5},
            {0.5, 0, 0.1, 0.15, 0.05, 0.08, 0.12, 0.15, 0.3, 0.08},
            {0.6, 0.1, 0, 0.12, 0.08, 0.1, 0.14, 0.16, 0.4, 0.12},
            {0.7, 0.15, 0.12, 0, 0.14, 0.16, 0.2, 0.22, 0.5, 0.18},
            {0.55, 0.05, 0.08, 0.14, 0, 0.04, 0.08, 0.1, 0.35, 0.06},
            {0.65, 0.08, 0.1, 0.16, 0.04, 0, 0.06, 0.08, 0.38, 0.1},
            {0.55, 0.12, 0.14, 0.2, 0.08, 0.06, 0, 0.04, 0.45, 0.14},
            {0.6, 0.15, 0.16, 0.22, 0.1, 0.08, 0.04, 0, 0.46, 0.16},
            {0.8, 0.3, 0.4, 0.5, 0.35, 0.38, 0.45, 0.46, 0, 0.32},
            {0.5, 0.08, 0.12, 0.18, 0.06, 0.1, 0.14, 0.16, 0.32, 0}
        };

        crearArchivoMatriz("tiempos.xlsx", lugares, tiempos, "Tiempos (horas)");
    }

    private static void crearArchivoMatriz(String nombreArchivo, String[] lugares, 
                                         double[][] matriz, String titulo) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Datos");
            
            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue(titulo);
            
            for (int i = 0; i < lugares.length; i++) {
                Cell cell = headerRow.createCell(i + 1);
                cell.setCellValue(lugares[i]);
            }
            
            // Crear datos
            for (int i = 0; i < lugares.length; i++) {
                Row row = sheet.createRow(i + 1);
                Cell cell = row.createCell(0);
                cell.setCellValue(lugares[i]);
                
                for (int j = 0; j < lugares.length; j++) {
                    cell = row.createCell(j + 1);
                    cell.setCellValue(matriz[i][j]);
                }
            }
            
            try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo)) {
                workbook.write(fileOut);
            }
        }
    }
} 