package model;

import java.util.*;

public class PlanificadorRutas {
    private List<Lugar> lugares;
    private Map<String, Map<String, Double>> distancias;
    private Map<String, Map<String, Double>> tiempos;
    private static final double HORAS_DISPONIBLES = 12.0;

    public PlanificadorRutas(List<Lugar> lugares, Map<String, Map<String, Double>> distancias,
                           Map<String, Map<String, Double>> tiempos) {
        this.lugares = lugares;
        this.distancias = distancias;
        this.tiempos = tiempos;
    }

    public List<List<Lugar>> planificarRutas() {
        List<List<Lugar>> rutas = new ArrayList<>();
        
        // Día 1: Aeropuerto -> Hotel
        List<Lugar> rutaDia1 = planificarDia1();
        rutas.add(rutaDia1);
        
        // Día 2: Hotel -> Hotel
        List<Lugar> rutaDia2 = planificarDia2();
        rutas.add(rutaDia2);
        
        // Día 3: Hotel -> Aeropuerto
        List<Lugar> rutaDia3 = planificarDia3();
        rutas.add(rutaDia3);
        
        return rutas;
    }

    private List<Lugar> planificarDia1() {
        Lugar aeropuerto = encontrarLugar(true, false);
        Lugar hotel = encontrarLugar(false, true);
        List<Lugar> lugaresNoVisitados = new ArrayList<>(lugares);
        lugaresNoVisitados.remove(aeropuerto);
        lugaresNoVisitados.remove(hotel);
        
        return optimizarRuta(aeropuerto, hotel, lugaresNoVisitados);
    }

    private List<Lugar> planificarDia2() {
        Lugar hotel = encontrarLugar(false, true);
        List<Lugar> lugaresNoVisitados = new ArrayList<>(lugares);
        lugaresNoVisitados.remove(hotel);
        
        return optimizarRuta(hotel, hotel, lugaresNoVisitados);
    }

    private List<Lugar> planificarDia3() {
        Lugar hotel = encontrarLugar(false, true);
        Lugar aeropuerto = encontrarLugar(true, false);
        List<Lugar> lugaresNoVisitados = new ArrayList<>(lugares);
        lugaresNoVisitados.remove(hotel);
        lugaresNoVisitados.remove(aeropuerto);
        
        return optimizarRuta(hotel, aeropuerto, lugaresNoVisitados);
    }

    private List<Lugar> optimizarRuta(Lugar inicio, Lugar fin, List<Lugar> lugaresDisponibles) {
        List<Lugar> ruta = new ArrayList<>();
        ruta.add(inicio);
        
        double tiempoTotal = 0;
        Lugar actual = inicio;
        
        while (!lugaresDisponibles.isEmpty() && tiempoTotal < HORAS_DISPONIBLES) {
            Lugar siguiente = encontrarMejorSiguiente(actual, lugaresDisponibles, tiempoTotal);
            if (siguiente == null) break;
            
            tiempoTotal += tiempos.get(actual.getNombre()).get(siguiente.getNombre());
            tiempoTotal += siguiente.getTiempoVisita();
            
            if (tiempoTotal > HORAS_DISPONIBLES) break;
            
            ruta.add(siguiente);
            lugaresDisponibles.remove(siguiente);
            actual = siguiente;
        }
        
        ruta.add(fin);
        return ruta;
    }

    private Lugar encontrarMejorSiguiente(Lugar actual, List<Lugar> disponibles, double tiempoAcumulado) {
        Lugar mejor = null;
        double mejorPuntuacion = Double.MAX_VALUE;
        
        for (Lugar lugar : disponibles) {
            double tiempoViaje = tiempos.get(actual.getNombre()).get(lugar.getNombre());
            double tiempoTotal = tiempoAcumulado + tiempoViaje + lugar.getTiempoVisita();
            
            if (tiempoTotal <= HORAS_DISPONIBLES) {
                double puntuacion = tiempoViaje + lugar.getTiempoVisita();
                if (puntuacion < mejorPuntuacion) {
                    mejorPuntuacion = puntuacion;
                    mejor = lugar;
                }
            }
        }
        
        return mejor;
    }

    private Lugar encontrarLugar(boolean esAeropuerto, boolean esHotel) {
        return lugares.stream()
                .filter(l -> l.isAeropuerto() == esAeropuerto && l.isHotel() == esHotel)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró el lugar requerido"));
    }

    // Método para planificar usando vecino más cercano
    public List<List<Lugar>> planificarRutasVecinoMasCercano() {
        List<List<Lugar>> rutas = new ArrayList<>();
        
        // Día 1: Aeropuerto -> Hotel
        List<Lugar> rutaDia1 = planificarDia1VecinoMasCercano();
        rutas.add(rutaDia1);
        
        // Día 2: Hotel -> Hotel
        List<Lugar> rutaDia2 = planificarDia2VecinoMasCercano();
        rutas.add(rutaDia2);
        
        // Día 3: Hotel -> Aeropuerto
        List<Lugar> rutaDia3 = planificarDia3VecinoMasCercano();
        rutas.add(rutaDia3);
        
        return rutas;
    }

    private List<Lugar> planificarDia1VecinoMasCercano() {
        Lugar aeropuerto = encontrarLugar(true, false);
        Lugar hotel = encontrarLugar(false, true);
        List<Lugar> lugaresNoVisitados = new ArrayList<>(lugares);
        lugaresNoVisitados.remove(aeropuerto);
        lugaresNoVisitados.remove(hotel);
        
        return optimizarRutaVecinoMasCercano(aeropuerto, hotel, lugaresNoVisitados);
    }

    private List<Lugar> planificarDia2VecinoMasCercano() {
        Lugar hotel = encontrarLugar(false, true);
        List<Lugar> lugaresNoVisitados = new ArrayList<>(lugares);
        lugaresNoVisitados.remove(hotel);
        
        return optimizarRutaVecinoMasCercano(hotel, hotel, lugaresNoVisitados);
    }

    private List<Lugar> planificarDia3VecinoMasCercano() {
        Lugar hotel = encontrarLugar(false, true);
        Lugar aeropuerto = encontrarLugar(true, false);
        List<Lugar> lugaresNoVisitados = new ArrayList<>(lugares);
        lugaresNoVisitados.remove(hotel);
        lugaresNoVisitados.remove(aeropuerto);
        
        return optimizarRutaVecinoMasCercano(hotel, aeropuerto, lugaresNoVisitados);
    }

    private List<Lugar> optimizarRutaVecinoMasCercano(Lugar inicio, Lugar fin, List<Lugar> lugaresDisponibles) {
        List<Lugar> ruta = new ArrayList<>();
        ruta.add(inicio);
        
        double tiempoTotal = 0;
        Lugar actual = inicio;
        
        while (!lugaresDisponibles.isEmpty() && tiempoTotal < HORAS_DISPONIBLES) {
            Lugar siguiente = encontrarVecinoMasCercano(actual, lugaresDisponibles, tiempoTotal);
            if (siguiente == null) break;
            
            tiempoTotal += tiempos.get(actual.getNombre()).get(siguiente.getNombre());
            tiempoTotal += siguiente.getTiempoVisita();
            
            if (tiempoTotal > HORAS_DISPONIBLES) break;
            
            ruta.add(siguiente);
            lugaresDisponibles.remove(siguiente);
            actual = siguiente;
        }
        
        ruta.add(fin);
        return ruta;
    }

    private Lugar encontrarVecinoMasCercano(Lugar actual, List<Lugar> disponibles, double tiempoAcumulado) {
        Lugar masCercano = null;
        double menorDistancia = Double.MAX_VALUE;
        
        for (Lugar lugar : disponibles) {
            double distancia = distancias.get(actual.getNombre()).get(lugar.getNombre());
            double tiempoTotal = tiempoAcumulado + 
                               tiempos.get(actual.getNombre()).get(lugar.getNombre()) + 
                               lugar.getTiempoVisita();
            
            if (tiempoTotal <= HORAS_DISPONIBLES && distancia < menorDistancia) {
                menorDistancia = distancia;
                masCercano = lugar;
            }
        }
        
        return masCercano;
    }
} 