import model.Lugar;
import model.PlanificadorRutas;
import util.CreadorExcel;
import util.ExcelReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            // Crear archivos Excel de ejemplo
            System.out.println("Creando archivos Excel de ejemplo...");
            CreadorExcel.crearArchivosExcel();
            System.out.println("Archivos Excel creados exitosamente.\n");

            // Leer datos de los archivos Excel
            List<Lugar> lugares = ExcelReader.leerLugares("lugares.xlsx");
            Map<String, Map<String, Double>> distancias = ExcelReader.leerDistancias("distancias.xlsx");
            Map<String, Map<String, Double>> tiempos = ExcelReader.leerTiempos("tiempos.xlsx");

            // Crear planificador
            PlanificadorRutas planificador = new PlanificadorRutas(lugares, distancias, tiempos);

            // Obtener rutas con ambos algoritmos
            List<List<Lugar>> rutasOptimizadas = planificador.planificarRutas();
            List<List<Lugar>> rutasVecinoMasCercano = planificador.planificarRutasVecinoMasCercano();

            // Imprimir resultados del algoritmo optimizado
            System.out.println("Plan de viaje optimizado (tiempo + visita):");
            System.out.println("----------------------------------------");
            imprimirRutas(rutasOptimizadas, distancias, tiempos);

            System.out.println("\n\nPlan de viaje con vecino más cercano:");
            System.out.println("----------------------------------");
            imprimirRutas(rutasVecinoMasCercano, distancias, tiempos);

        } catch (IOException e) {
            System.err.println("Error al leer los archivos Excel: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void imprimirRutas(List<List<Lugar>> rutas, 
                                    Map<String, Map<String, Double>> distancias,
                                    Map<String, Map<String, Double>> tiempos) {
        double distanciaTotal = 0;
        double tiempoTotal = 0;

        for (int i = 0; i < rutas.size(); i++) {
            System.out.println("\nDía " + (i + 1) + ":");
            List<Lugar> ruta = rutas.get(i);
            
            double distanciaDia = 0;
            double tiempoDia = 0;
            
            for (int j = 0; j < ruta.size(); j++) {
                Lugar lugar = ruta.get(j);
                System.out.print(lugar.getNombre());
                
                if (j < ruta.size() - 1) {
                    Lugar siguiente = ruta.get(j + 1);
                    double distancia = distancias.get(lugar.getNombre()).get(siguiente.getNombre());
                    double tiempo = tiempos.get(lugar.getNombre()).get(siguiente.getNombre());
                    
                    distanciaDia += distancia;
                    tiempoDia += tiempo + lugar.getTiempoVisita();
                    
                    System.out.print(" -> ");
                }
            }
            
            distanciaTotal += distanciaDia;
            tiempoTotal += tiempoDia;
            
            System.out.println("\nDistancia del día: " + String.format("%.2f", distanciaDia) + " km");
            System.out.println("Tiempo del día: " + String.format("%.2f", tiempoDia) + " horas");
        }
        
        System.out.println("\nTotales:");
        System.out.println("Distancia total: " + String.format("%.2f", distanciaTotal) + " km");
        System.out.println("Tiempo total: " + String.format("%.2f", tiempoTotal) + " horas");
    }
} 