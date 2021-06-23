package sample;

public class Proceso {

    private String proceso;
    private Integer tamano;
    private Integer tiempo;
    private Integer duracion;

    public Proceso(String proceso, Integer tamano, Integer tiempo, Integer duracion) {
        this.proceso = proceso;
        this.tamano = tamano;
        this.tiempo = tiempo;
        this.duracion = duracion;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public Integer getTamano() {
        return tamano;
    }

    public void setTamano(Integer tamano) {
        this.tamano = tamano;
    }

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
}
