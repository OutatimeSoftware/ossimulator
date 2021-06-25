package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Integer contadorProceso = 0;
    private Integer contadorPrograma = 0;
    private Boolean inicioFlag = true;
    private ArrayList<Proceso> ordenProcesos = new ArrayList<>();
    private Boolean fragmentacionFlag = false;
    private Boolean insercionTerminada = false;
    private Integer ordenRetiro = 0;
    private Boolean first = true;
    private Double memoriaAux = 0.0;

    private Double memoriaOcupada = 0.0;
    private Integer memoriaKLibre = 54;

    //Variables particion
    private Integer numero = 0;
    private Integer localidad = 10;

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
    private TableColumn<Proceso, String> tTamano;

    @FXML
    private TableColumn<Proceso, Integer> tTiempo;

    @FXML
    private TableColumn<Proceso, Integer> tDuracion;

    //Tabla Areas
    @FXML
    private TableColumn<Area, Integer> taNo;

    @FXML
    private TableColumn<Area, String> taLocalidad;

    @FXML
    private TableColumn<Area, String> taTamano;

    @FXML
    private TableColumn<Area, String> taEstado;

    //Tabla Particiones
    @FXML
    private TableColumn<Particion, Integer> tpNo;

    @FXML
    private TableColumn<Particion, String> tpLocalidad;

    @FXML
    private TableColumn<Particion, String> tpTamano;

    @FXML
    private TableColumn<Particion, String> tpEstado;

    @FXML
    private TableColumn<Particion, String> tpProceso;

    //REPRESENTACION GRAFICA
    //Definicion de vbox
    @FXML
    private VBox repGrafica;

    //BOTON PASO N
    //Deficion de boton
    @FXML
    private Button botonPasoN;

    @FXML
    private Label labelIndicadorProceso;

    //Definicion de evento
    @FXML
    private void handleButtonAction(ActionEvent event) {
        //Obtenemos proceso actual y lo removemos de la lista
        contadorPrograma++;
        final ObservableList<Proceso> procesos = tableProceso.getItems();
        if (botonPasoN.getText() == "Finalizada") {
            Stage stage = (Stage) botonPasoN.getScene().getWindow();
            stage.close();
        }
        if (contadorProceso >= procesos.size() || insercionTerminada){
            //Proceso de retiro
            contadorProceso--;
            insercionTerminada = true;
            double height = repGrafica.getPrefHeight();
            if (ordenRetiro < ordenProcesos.size()){
                Proceso procesoOriginal = ordenProcesos.get(ordenRetiro);
                ordenRetiro++;
                botonPasoN.setText("Paso: " + contadorPrograma);
                labelIndicadorProceso.setText("Se retiró: " + procesoOriginal.getProceso());
                actualizacionAreaLibreRetiro(procesoOriginal);

                //Tabla areas
                ObservableList<Area> areasList = tablaArea.getItems();
                if (procesoOriginal.getProceso().equals("A")){
                    Area newArea = new Area(1, 10 + "K", 8 + "K", "Disponible");
                    areasList.add(0, newArea);
                    areasList.get(2).setNo(3);
                    areasList.get(1).setNo(2);
                    areasList.get(1).setLocalidad(46 + "K");
                    areasList.get(1).setTamano(4 + "K");
                } else if (procesoOriginal.getProceso().equals("B")) {
                    areasList.get(0).setTamano(22 + "K");
                }else if (procesoOriginal.getProceso().equals("D")) {
                    areasList.remove(2);
                    areasList.get(1).setTamano(18 + "K");
                }else if (procesoOriginal.getProceso().equals("E")) {
                    areasList.remove(1);
                    areasList.get(0).setTamano(54 + "K");
                }
                tablaArea.setItems(areasList);

                //Tabla particiones
                ObservableList<Particion> particiones = tablaParticiones.getItems();
                if (particiones.get(0).getProceso().equals("E") && particiones.size() != 1) {
                    particiones.remove(1);
                }else {
                    particiones.remove(0);
                }
                tablaParticiones.setItems(particiones);

                if (procesoOriginal.getProceso().equals("E")) {
                    reiniciarArea();
                }

            } else {
                fragmentacionFlag=false;
                inicioFlag=false;
                botonPasoN.setText("Finalizada");
                labelIndicadorProceso.setText("Simulacion finalizada");
            }
        } else {
            Proceso procesoActual = procesos.get(contadorProceso);

            final ObservableList<Area> areas = tablaArea.getItems();
            Area areaActual=areas.get(0);
            Area area1Actual=null;
            if (areas.size() == 2) {
                area1Actual = areas.get(1);
            }

            if(fragmentacionFlag){
                if (memoriaKLibre < procesoActual.getIntTamano()) {
                    Proceso procesoEliminado = eliminarProcesoDuracionMin();
                    botonPasoN.setText("Paso: " + contadorPrograma);
                    labelIndicadorProceso.setText("Se retiró: " + procesoEliminado.getProceso());

                    //Tabla de areas
                    Integer area2Localidad = 10;
                    for (Proceso proceso: ordenProcesos) {
                        area2Localidad += (proceso.getProceso() != "D") ? proceso.getIntTamano() : 0;
                    }
                    areaActual.setNo(2);
                    areaActual.setTamano(8 + "K");
                    Area newArea = areasHandler(procesoActual, areaActual);
                    newArea.setNo(1);
                    newArea.setLocalidad(area2Localidad + "K");
                    newArea.setTamano(18 + "K");
                    final ObservableList<Area> listAreas = FXCollections.observableArrayList(
                            newArea,
                            areaActual
                    );
                    tablaArea.setItems(listAreas);

                    //Tabla de particiones
                    ObservableList<Particion> particiones = tablaParticiones.getItems();
                    Particion eliminar = null;
                    for (Particion elemento : particiones) {
                        if (elemento.getProceso() == "C") {
                            eliminar = elemento;
                        }
                    }
                    particiones.remove(eliminar);
                    tablaParticiones.setItems(particiones);

                }else {
                    contadorProceso++;
                    insertarProcesoFragmentacion(procesoActual);

                    //Tabla areas
                    areaActual.setLocalidad(46 + "K");
                    areaActual.setTamano(4 + "K");
                    final ObservableList<Area> listAreas = FXCollections.observableArrayList(
                            areaActual,
                            area1Actual
                    );
                    tablaArea.setItems(listAreas);

                    //Actulizamos boton
                    botonPasoN.setText("Paso: " + contadorPrograma);
                    labelIndicadorProceso.setText("Se insertó: " + procesoActual.getProceso());

                    //Tabla particiones
                    ObservableList<Particion> particiones = tablaParticiones.getItems();
                    Particion nuevaParticion = new Particion(3, 32 + "K", 14 + "K", "Ocupado", "E");
                    Particion aux = particiones.get(2);
                    particiones.remove(2);
                    particiones.add(nuevaParticion);
                    particiones.add(aux);
                    tablaParticiones.setItems(particiones);
                }
            }else {
                if (memoriaKLibre < procesoActual.getIntTamano()) {
                    botonPasoN.setText("Paso: " + contadorPrograma);
                    labelIndicadorProceso.setText("No se pudo insertar: " + procesoActual.getProceso());
                    fragmentacionFlag = true;
                }else {
                    memoriaKLibre -= procesoActual.getIntTamano();
                    contadorProceso++;

                    //Actulizamos boton
                    botonPasoN.setText("Paso: " + contadorPrograma);
                    labelIndicadorProceso.setText("Se insertó: " + procesoActual.getProceso());

                    //Change on Areas table
                    Area newArea = areasHandler(procesoActual, areaActual);
                    final ObservableList<Area> listAreas = FXCollections.observableArrayList(
                            newArea
                    );
                    tablaArea.setItems(listAreas);

                    //Change on Particiones table
                    Particion newParticion = particionesHandler(procesoActual);
                    tablaParticiones.getItems().add(newParticion);

                    //Actualizamos representacion grafica
                    repGraficaHandler(procesoActual);

                    ordenProcesos.add(procesoActual);
                }
            }
        }

    }

    public Area areasHandler(Proceso proceso, Area area) {
        Integer pTamano = proceso.getIntTamano();

        Integer aNumero = area.getNo();

        Integer newLocalidad = localidad + pTamano;
        String newEstado = "Disponible";
        Area newArea = new Area(aNumero, newLocalidad + "K", memoriaKLibre + "K", newEstado);
        return newArea;
    }

    public Particion particionesHandler(Proceso proceso) {
        String pProceso = proceso.getProceso();
        Integer pTamano = proceso.getIntTamano();
        numero++;
        Particion particion = new Particion(numero, localidad + "K", pTamano + "K", "Ocupado", pProceso);
        localidad += pTamano;
        return particion;
    }

    public void repGraficaHandler(Proceso proceso){
        Pane nuevoProceso = crearProcesoGrafico(proceso);
        repGrafica.getChildren().add(nuevoProceso);
        Pane newFreePane = crearAreaLibre();
        repGrafica.getChildren().add(newFreePane);
    }

    public Pane crearProcesoGrafico(Proceso proceso) {
        String pProceso = proceso.getProceso();
        Integer pTamano = proceso.getIntTamano();

        Pane newPane = new Pane();
        Label label = new Label(pProceso);
        double width = repGrafica.getPrefWidth();
        double height = repGrafica.getPrefHeight();
        double newPaneWidth = width;
        double newPaneHeight = (pTamano * height) / 64 ;
        memoriaOcupada += newPaneHeight;
        newPane.getChildren().add(label);
        label.layoutXProperty().bind(newPane.widthProperty().subtract(label.widthProperty()).divide(2));
        label.layoutYProperty().bind(newPane.heightProperty().subtract(label.heightProperty()).divide(2));
        newPane.setStyle("-fx-background-color: silver");
        newPane.setPrefWidth(newPaneWidth);
        newPane.setPrefHeight(newPaneHeight);
        newPane.setId("proceso" + proceso.getProceso());
        return newPane;
    }

    public Pane crearAreaLibre() {
        //Actualizamos area libre
        Pane freePane = (Pane)repGrafica.lookup("#areaLibre");
        repGrafica.getChildren().remove(freePane);

        Pane newFreePane = new Pane();
        Label freeLabel = new Label("Area libre");
        double width = repGrafica.getPrefWidth();
        double height = repGrafica.getPrefHeight();
        double newFreeHeight = height - memoriaOcupada;
        newFreePane.setPrefWidth(width);
        newFreePane.setPrefHeight(newFreeHeight);
        newFreePane.getChildren().add(freeLabel);
        freeLabel.layoutXProperty().bind(newFreePane.widthProperty().subtract(freeLabel.widthProperty()).divide(2));
        freeLabel.layoutYProperty().bind(newFreePane.heightProperty().subtract(freeLabel.heightProperty()).divide(2));
        String backColor = (fragmentacionFlag) ? "-fx-background-color: red;" : "-fx-background-color: gray;";
        newFreePane.setStyle(backColor);
        newFreePane.setId("areaLibre");
        return newFreePane;
    }

    public Proceso eliminarProcesoDuracionMin() {
        Proceso procesoMinDuracion = ordenProcesos.get(0);
        for (Proceso proceso : ordenProcesos) {
            procesoMinDuracion =  procesoMinDuracion.getDuracion() < proceso.getDuracion() ? procesoMinDuracion : proceso;
        }

        Integer pTamano = procesoMinDuracion.getIntTamano();
        double width = repGrafica.getPrefWidth();
        double height = repGrafica.getPrefHeight();
        double oldPaneWidth = width;
        double oldPaneHeight = (pTamano * height) / 64 ;

        //Crear pane de area libre sustito
        Pane sustitoFreePane = new Pane();
        Label freeLabel = new Label("Area libre");
        sustitoFreePane.setPrefWidth(oldPaneWidth);
        sustitoFreePane.setPrefHeight(oldPaneHeight);
        sustitoFreePane.getChildren().add(freeLabel);
        freeLabel.layoutXProperty().bind(sustitoFreePane.widthProperty().subtract(freeLabel.widthProperty()).divide(2));
        freeLabel.layoutYProperty().bind(sustitoFreePane.heightProperty().subtract(freeLabel.heightProperty()).divide(2));
        sustitoFreePane.setStyle("-fx-background-color: red;");
        sustitoFreePane.setId("areaLibreFragmentada");

        Pane removePane = (Pane)repGrafica.lookup("#proceso" + procesoMinDuracion.getProceso());

        //Busqueda de procesos a reponer antes de remover
        ObservableList<Node> list = repGrafica.getChildren();
        ArrayList<Pane> reponerProcesos = new ArrayList<>();
        Boolean procesoRemovido = false;
        for (Node elemento : list) {
            if (procesoRemovido) {
                reponerProcesos.add((Pane) elemento);
            }
            if (elemento == removePane) {
                procesoRemovido = true;
            }
        }

        //Remover el proceso de menor duracion
        repGrafica.getChildren().remove(removePane);
        memoriaOcupada -= oldPaneHeight;
        memoriaKLibre += procesoMinDuracion.getIntTamano();
        ordenProcesos.remove(procesoMinDuracion);

        //Remover procesos por debajo
        for(Pane elemento: reponerProcesos) {
            repGrafica.getChildren().remove(elemento);
        }

        //Actualizar area libre (sustituto)
        repGrafica.getChildren().add(sustitoFreePane);

        //Agregar procesos removidos
        for(Pane elemento: reponerProcesos) {
            repGrafica.getChildren().add(elemento);
        }
        return procesoMinDuracion;
    }

    public void insertarProcesoFragmentacion(Proceso proceso) {
        double height = repGrafica.getPrefHeight();
        double width = repGrafica.getPrefWidth();
        double newProcesoHeight = (proceso.getIntTamano() * height) / 64;

        Pane sustitutoFreePane = (Pane)repGrafica.lookup("#areaLibreFragmentada");
        double memoriaFragHeight = sustitutoFreePane.getPrefHeight();

        //Crear pane de area libre sustito
        Pane sustitoFreePane = new Pane();
        Label freeLabel = new Label("Area libre");
        sustitoFreePane.setPrefWidth(width);
        sustitoFreePane.setPrefHeight(memoriaFragHeight-newProcesoHeight);
        sustitoFreePane.getChildren().add(freeLabel);
        freeLabel.layoutXProperty().bind(sustitoFreePane.widthProperty().subtract(freeLabel.widthProperty()).divide(2));
        freeLabel.layoutYProperty().bind(sustitoFreePane.heightProperty().subtract(freeLabel.heightProperty()).divide(2));
        sustitoFreePane.setStyle("-fx-background-color: red;");
        sustitoFreePane.setId("areaLibreFragmentada" + proceso.getProceso());

        //Creamos pane de proceso nuevo
        String pProceso = proceso.getProceso();
        Integer pTamano = proceso.getIntTamano();
        Pane newPane = new Pane();
        Label label = new Label(pProceso);
        double newPaneWidth = width;
        double newPaneHeight = (pTamano * height) / 64 ;
        memoriaOcupada += newPaneHeight;
        newPane.getChildren().add(label);
        label.layoutXProperty().bind(newPane.widthProperty().subtract(label.widthProperty()).divide(2));
        label.layoutYProperty().bind(newPane.heightProperty().subtract(label.heightProperty()).divide(2));
        newPane.setStyle("-fx-background-color: silver");
        newPane.setPrefWidth(newPaneWidth);
        newPane.setPrefHeight(newPaneHeight);
        newPane.setId("proceso" + proceso.getProceso());

        ObservableList<Node> list = repGrafica.getChildren();

        //Busqueda de procesos a reponer antes de remover
        ArrayList<Pane> reponerProcesos = new ArrayList<>();
        Boolean procesoRemovido = false;
        for (Node elemento : list) {
            if (procesoRemovido) {
                reponerProcesos.add((Pane) elemento);
            }
            if (elemento == sustitutoFreePane) {
                procesoRemovido = true;
            }
        }

        //Remover areaLibreFragmentada
        repGrafica.getChildren().remove(sustitutoFreePane);

        //Remover procesos por debajo
        for(Pane elemento: reponerProcesos) {
            repGrafica.getChildren().remove(elemento);
        }

        //Agregar proceso nuevo
        repGrafica.getChildren().add(newPane);
        memoriaKLibre -= proceso.getIntTamano();
        memoriaOcupada += (proceso.getIntTamano() * height) / 64;
        ordenProcesos.add(proceso);

        //Actualizar area libre (sustituto)
        repGrafica.getChildren().add(sustitoFreePane);

        //Agregar procesos removidos
        for(Pane elemento: reponerProcesos) {
            repGrafica.getChildren().add(elemento);
        }
    }

    public void actualizacionAreaLibreRetiro(Proceso proceso) {
        ObservableList<Node> panesList = repGrafica.getChildren();
        Integer index = 0;
        for (Node elemento: panesList) {
            if (elemento.getId().equals("proceso" + proceso.getProceso())) {
                index = panesList.indexOf(elemento);
                repGrafica.getChildren().remove(elemento);
                if (elemento.getId().equals("procesoB")){
                    juntarAreas1();
                }
                if (elemento.getId().equals("procesoD")){
                    juntarAreas2((Pane)elemento);
                }
                break;
            }
        }

        Pane newFreePane = new Pane();
        Label freeLabel = new Label("Area libre");
        double width = repGrafica.getPrefWidth();
        double height = repGrafica.getPrefHeight();
        double newFreeHeight = (proceso.getIntTamano() * height) / 64;
        newFreePane.setPrefWidth(width);
        newFreePane.setPrefHeight(newFreeHeight);
        newFreePane.getChildren().add(freeLabel);
        freeLabel.layoutXProperty().bind(newFreePane.widthProperty().subtract(freeLabel.widthProperty()).divide(2));
        freeLabel.layoutYProperty().bind(newFreePane.heightProperty().subtract(freeLabel.heightProperty()).divide(2));
        String backColor = (fragmentacionFlag) ? "-fx-background-color: red;" : "-fx-background-color: gray;";
        newFreePane.setStyle(backColor);
        newFreePane.setId("areaLibreFragmentada" + proceso.getProceso());
        if (proceso.getProceso().equals("A") ){
            repGrafica.getChildren().add(index, newFreePane);
        }
    }

    public void juntarAreas1() {
        ObservableList<Node> panes = repGrafica.getChildren();
        ArrayList<Pane> set1 = new ArrayList<>();
        for (Node pane : panes) {
            if (pane.getId().contains("areaLibre") && set1.size() <= 2){
                set1.add((Pane)pane);
            }
        }
        //Formamos set 1
        double totalHeight = 5.0;
        for (Pane pane: set1) {
            totalHeight += pane.getPrefHeight();
        }
        Pane set1Pane = new Pane();
        Label set1Label = new Label("Area libre");
        double set1Width = repGrafica.getPrefWidth();
        double set1Height = totalHeight;
        set1Pane.getChildren().add(set1Label);
        set1Label.layoutXProperty().bind(set1Pane.widthProperty().subtract(set1Label.widthProperty()).divide(2));
        set1Label.layoutYProperty().bind(set1Pane.heightProperty().subtract(set1Label.heightProperty()).divide(2));
        String back1Color = (fragmentacionFlag) ? "-fx-background-color: red;" : "-fx-background-color: gray;";
        set1Pane.setStyle(back1Color);
        set1Pane.setPrefWidth(set1Width);
        set1Pane.setPrefHeight(set1Height);
        set1Pane.setId("areaLibreFrag1");

        repGrafica.getChildren().remove(1);

        repGrafica.getChildren().add(1, set1Pane);
    }

    public void juntarAreas2(Pane paneProceso) {
        ObservableList<Node> panes = repGrafica.getChildren();
        ArrayList<Pane> set2 = new ArrayList<>();

        Pane element1 = (Pane)panes.get(3);
        Pane element2 = (Pane)panes.get(3);
        Pane element3 = (Pane)panes.get(3);

        set2.add(element1);
        set2.add(element2);
        set2.add(element3);

        //Formamos set 2
        double total2Height = 0.0;
        for (Pane pane: set2) {
            total2Height += pane.getPrefHeight();
        }

        Pane set2Pane = new Pane();
        Label set2Label = new Label("Area libre");
        double set2Width = repGrafica.getPrefWidth();
        double set2Height = total2Height + paneProceso.getPrefHeight();
        set2Pane.getChildren().add(set2Label);
        set2Label.layoutXProperty().bind(set2Pane.widthProperty().subtract(set2Label.widthProperty()).divide(2));
        set2Label.layoutYProperty().bind(set2Pane.heightProperty().subtract(set2Label.heightProperty()).divide(2));
        String back2Color = (fragmentacionFlag) ? "-fx-background-color: red;" : "-fx-background-color: gray;";
        set2Pane.setStyle(back2Color);
        set2Pane.setPrefWidth(set2Width);
        set2Pane.setPrefHeight(set2Height);
        set2Pane.setId("areaLibreFrag2");

        repGrafica.getChildren().remove(element1);
        repGrafica.getChildren().remove(element2);
        repGrafica.getChildren().remove(element3);

        repGrafica.getChildren().remove(repGrafica.getChildren().size()-1);

        repGrafica.getChildren().add(3, set2Pane);
    }

    public void reiniciarArea() {
        repGrafica.getChildren().clear();

        Pane newPane = new Pane();
        Label label = new Label("SO");
        label.setTextFill(Color.web("#fff"));
        double width = repGrafica.getPrefWidth();
        double height = repGrafica.getPrefHeight();
        double newPaneWidth = width;
        double newPaneHeight = (10 * height) / 64 ;
        memoriaOcupada += newPaneHeight;
        newPane.getChildren().add(label);
        label.layoutXProperty().bind(newPane.widthProperty().subtract(label.widthProperty()).divide(2));
        label.layoutYProperty().bind(newPane.heightProperty().subtract(label.heightProperty()).divide(2));
        newPane.setStyle("-fx-background-color: dodgerblue; -fx-text-inner-color: white;");
        newPane.setPrefWidth(newPaneWidth);
        newPane.setPrefHeight(newPaneHeight);
        newPane.setId("procesoSO");

        Pane newFreePane = new Pane();
        Label freeLabel = new Label("Area libre");
        double freePaneWidth = width;
        double freePaneHeight = height - newPaneHeight;
        newFreePane.getChildren().add(freeLabel);
        freeLabel.layoutXProperty().bind(newFreePane.widthProperty().subtract(freeLabel.widthProperty()).divide(2));
        freeLabel.layoutYProperty().bind(newFreePane.heightProperty().subtract(freeLabel.heightProperty()).divide(2));
        newFreePane.setStyle("-fx-background-color: gray;");
        newFreePane.setPrefWidth(freePaneWidth);
        newFreePane.setPrefHeight(freePaneHeight);
        newFreePane.setId("areaLibre");

        repGrafica.getChildren().add(newPane);
        repGrafica.getChildren().add(newFreePane);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Define data to add
        final ObservableList<Proceso> listProceso = FXCollections.observableArrayList(
                new Proceso("A", 8 + "K", 1, 7),
                new Proceso("B", 14 + "K", 2, 7),
                new Proceso("C", 18 + "K", 3, 4),
                new Proceso("D", 6 + "K", 4, 6),
                new Proceso("E", 14 + "K", 5, 5)
        );
        final ObservableList<Area> listAreas = FXCollections.observableArrayList(
                new Area(1, 10 + "K", 54 + "K", "Disponible")
        );

        //associate with data columns
        tProceso.setCellValueFactory(new PropertyValueFactory<Proceso, String>("proceso"));
        tTamano.setCellValueFactory(new PropertyValueFactory<Proceso, String>("tamano"));
        tTiempo.setCellValueFactory(new PropertyValueFactory<Proceso, Integer>("tiempo"));
        tDuracion.setCellValueFactory(new PropertyValueFactory<Proceso, Integer>("duracion"));

        taNo.setCellValueFactory(new PropertyValueFactory<Area, Integer>("no"));
        taLocalidad.setCellValueFactory(new PropertyValueFactory<Area, String>("localidad"));
        taTamano.setCellValueFactory(new PropertyValueFactory<Area, String>("tamano"));
        taEstado.setCellValueFactory(new PropertyValueFactory<Area, String>("estado"));

        tpNo.setCellValueFactory(new PropertyValueFactory<Particion, Integer>("no"));
        tpLocalidad.setCellValueFactory(new PropertyValueFactory<Particion, String>("localidad"));
        tpTamano.setCellValueFactory(new PropertyValueFactory<Particion, String>("tamano"));
        tpEstado.setCellValueFactory(new PropertyValueFactory<Particion, String>("estado"));
        tpProceso.setCellValueFactory(new PropertyValueFactory<Particion, String>("proceso"));

        //Anadir elemento a lista
        tableProceso.setItems(listProceso);
        tablaArea.setItems(listAreas);

        //Setear configuracion inicial de representacion grafica
        Pane newPane = new Pane();
        Label label = new Label("SO");
        label.setTextFill(Color.web("#fff"));
        double width = repGrafica.getPrefWidth();
        double height = repGrafica.getPrefHeight();
        double newPaneWidth = width;
        double newPaneHeight = (10 * height) / 64 ;
        memoriaOcupada += newPaneHeight;
        newPane.getChildren().add(label);
        label.layoutXProperty().bind(newPane.widthProperty().subtract(label.widthProperty()).divide(2));
        label.layoutYProperty().bind(newPane.heightProperty().subtract(label.heightProperty()).divide(2));
        newPane.setStyle("-fx-background-color: dodgerblue; -fx-text-inner-color: white;");
        newPane.setPrefWidth(newPaneWidth);
        newPane.setPrefHeight(newPaneHeight);
        newPane.setId("procesoSO");

        Pane newFreePane = new Pane();
        Label freeLabel = new Label("Area libre");
        double freePaneWidth = width;
        double freePaneHeight = height - newPaneHeight;
        newFreePane.getChildren().add(freeLabel);
        freeLabel.layoutXProperty().bind(newFreePane.widthProperty().subtract(freeLabel.widthProperty()).divide(2));
        freeLabel.layoutYProperty().bind(newFreePane.heightProperty().subtract(freeLabel.heightProperty()).divide(2));
        newFreePane.setStyle("-fx-background-color: gray;");
        newFreePane.setPrefWidth(freePaneWidth);
        newFreePane.setPrefHeight(freePaneHeight);
        newFreePane.setId("areaLibre");

        repGrafica.getChildren().add(newPane);
        repGrafica.getChildren().add(newFreePane);
    }
}
