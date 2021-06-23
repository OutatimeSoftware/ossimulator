package sample;

public class Area {

    private Integer no;
    private Integer localidad;
    private Integer tamano;
    private String estado;

    public Area(Integer no, Integer localidad, Integer tamano, String estado) {
        this.no = no;
        this.localidad = localidad;
        this.tamano = tamano;
        this.estado = estado;
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

}
