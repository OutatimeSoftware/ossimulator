package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //Variables particion
    private Integer numero = 0;
    private Integer localidad = 0;
    private Integer tamano = 0;
    private String estado = '';
    private String proceso = '';

    //Definicion de tablas
    @FXML
    private TableView<Proceso> tableProceso;

    @FXML
    private TableView<Area> tablaArea;

    @FXML TableView<Particion> tablaParticiones;

    //Definicion de columnas
    //Tabla proceso
    @FXML
    private TableColumn<Proceso, String> tProceso;

    @FXML
    private TableColumn<Proceso, Integer> tTamano;

    @FXML
    private TableColumn<Proceso, Integer> tTiempo;

    @FXML
    private TableColumn<Proceso, Integer> tDuracion;

    //Tabla Areas
    @FXML
    private TableColumn<Area, Integer> taNo;

    @FXML
    private TableColumn<Area, Integer> taLocalidad;

    @FXML
    private TableColumn<Area, Integer> taTamano;

    @FXML
    private TableColumn<Area, String> taEstado;

    //Tabla Particiones
    @FXML
    private TableColumn<Particion, Integer> tpNo;

    @FXML
    private TableColumn<Particion, Integer> tpLocalidad;

    @FXML
    private TableColumn<Particion, Integer> tpTamano;

    @FXML
    private TableColumn<Particion, String> tpEstado;

    @FXML
    private TableColumn<Particion, String> tpProceso;

    //Definicion de boton Paso n
    @FXML
    private void handleButtonAction(ActionEvent event) {
        //Change on Areas table
        areasHandler(proceso, area);

        //Change on Particiones table
        particionesHandler(proceso)
    }

    public Area areasHandler(Proceso proceso, Area area) {
        String pProceso = proceso.getProceso();
        Integer pTamano = proceso.getTamano();
        Integer pTiempo = proceso.getTiempo();
        Integer pDuracion = proceso.getDuracion();

        Integer aNumero = area.getNo();
        Integer aLocalidad = area.getLocalidad();
        Integer aTamano = area.getTamano();
        String aEstado = area.getEstado();

        aNumero++;
        aLocalidad += pTamano;
        if (aLocalidad >= 64){
            aEstado = "Lleno";
        }
        Area area = new Area(aNumero, aLocalidad, aTamano, aEstado);
        return area;
    }

    public Particion particionesHandler(Proceso proceso) {
        String pProceso = proceso.getProceso();
        Integer pTamano = proceso.getTamano();
        numero++;
        Particion particion = new Particion(numero, localidad, pTamano, "Ocupado", pProceso);
        return particion;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Define data to add
        final ObservableList<Proceso> listProceso = FXCollections.observableArrayList(
                new Proceso("A", 8, 1, 7),
                new Proceso("B", 14, 2, 7),
                new Proceso("C", 18, 3, 4),
                new Proceso("D", 6, 4, 6),
                new Proceso("E", 14, 5, 5)
        );

        final ObservableList<Area> listAreas = FXCollections.observableArrayList(
                new Area(1, 10, 54, "D")
        );
        final ObservableList<Particion> listParticiones = FXCollections.observableArrayList(
                new Particion(1, 10, 8, "Ocupado", "A")
        );

        //associate with data columns
        tProceso.setCellValueFactory(new PropertyValueFactory<Proceso, String>("proceso"));
        tTamano.setCellValueFactory(new PropertyValueFactory<Proceso, Integer>("tamano"));
        tTiempo.setCellValueFactory(new PropertyValueFactory<Proceso, Integer>("tiempo"));
        tDuracion.setCellValueFactory(new PropertyValueFactory<Proceso, Integer>("duracion"));

        taNo.setCellValueFactory(new PropertyValueFactory<Area, Integer>("no"));
        taLocalidad.setCellValueFactory(new PropertyValueFactory<Area, Integer>("localidad"));
        taTamano.setCellValueFactory(new PropertyValueFactory<Area, Integer>("tamano"));
        taEstado.setCellValueFactory(new PropertyValueFactory<Area, String>("estado"));

        tpNo.setCellValueFactory(new PropertyValueFactory<Particion, Integer>("no"));
        tpLocalidad.setCellValueFactory(new PropertyValueFactory<Particion, Integer>("localidad"));
        tpTamano.setCellValueFactory(new PropertyValueFactory<Particion, Integer>("tamano"));
        tpEstado.setCellValueFactory(new PropertyValueFactory<Particion, String>("estado"));
        tpProceso.setCellValueFactory(new PropertyValueFactory<Particion, String>("proceso"));

        //Anadir elemento a lista
        tableProceso.setItems(listProceso);
        tablaArea.setItems(listAreas);

    }
}
