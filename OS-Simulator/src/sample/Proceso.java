package sample;

public class Proceso {

    private String proceso;
    private String tamano;
    private Integer tiempo;
    private Integer duracion;

    public Proceso(String proceso, String tamano, Integer tiempo, Integer duracion) {
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

    public String getTamano() {
        return tamano;
    }

    public Integer getIntTamano() {
        tamano = tamano.replace("K", "");
        return Integer.valueOf(tamano);
    }

    public void setTamano(String tamano) {
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
