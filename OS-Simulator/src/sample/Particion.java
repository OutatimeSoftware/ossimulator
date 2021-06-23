package sample;

public class Particion {

    private Integer no;
    private Integer localidad;
    private Integer tamano;
    private String estado;
    private String proceso;

    public Particion(Integer no, Integer localidad, Integer tamano, String estado, String proceso) {
        this.no = no;
        this.localidad = localidad;
        this.tamano = tamano;
        this.estado = estado;
        this.proceso = proceso;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Integer getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Integer localidad) {
        this.localidad = localidad;
    }

    public Integer getTamano() {
        return tamano;
    }

    public void setTamano(Integer tamano) {
        this.tamano = tamano;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }
}
