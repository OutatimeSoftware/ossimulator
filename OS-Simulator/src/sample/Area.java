package sample;

public class Area {

    private Integer no;
    private String localidad;
    private String tamano;
    private String estado;

    public Area(Integer no, String localidad, String tamano, String estado) {
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

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
