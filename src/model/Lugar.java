package model;

public class Lugar {
    private String nombre;
    private String tipo; // museo, parque, estadio, etc.
    private double tiempoVisita; // en horas
    private boolean esAeropuerto;
    private boolean esHotel;

    public Lugar(String nombre, String tipo, double tiempoVisita, boolean esAeropuerto, boolean esHotel) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tiempoVisita = tiempoVisita;
        this.esAeropuerto = esAeropuerto;
        this.esHotel = esHotel;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getTiempoVisita() { return tiempoVisita; }
    public void setTiempoVisita(double tiempoVisita) { this.tiempoVisita = tiempoVisita; }
    public boolean isAeropuerto() { return esAeropuerto; }
    public void setAeropuerto(boolean esAeropuerto) { this.esAeropuerto = esAeropuerto; }
    public boolean isHotel() { return esHotel; }
    public void setHotel(boolean esHotel) { this.esHotel = esHotel; }

    @Override
    public String toString() {
        return nombre;
    }
}