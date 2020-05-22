
package alquileres.interfaz;

import java.io.IOException;

import alquileres.modelo.AgenciaAlquiler;
import alquileres.modelo.Coche;
import alquileres.modelo.Furgoneta;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuiAgenciaAlquiler extends Application
{

	private MenuItem itemLeer;
	private MenuItem itemGuardar;
	private MenuItem itemSalir;

	private TextArea area;

	private Button btnListarTodo;
	private TextField txtDias;
	private Button btnBuscar;

	private RadioButton rbtCoches;
	private RadioButton rbtFurgonetas;
	private Button btnMostrar;

	private AgenciaAlquiler agencia; // el modelo

	@Override
	public void start(Stage stage) {

		BorderPane root = crearGui();

		Scene scene = new Scene(root, 900, 500);
		stage.setScene(scene);
		stage.setTitle("- Alquiler de vehículos -");
		scene.getStylesheets().add(
		                getClass().getResource("application.css")
		                                .toExternalForm());
		stage.show();

		agencia = new AgenciaAlquiler("Rent-A-Car");
		cogerFoco();
	}

	private BorderPane crearGui() {

		BorderPane panel = new BorderPane();
		panel.setTop(crearBarraMenu());
		panel.setCenter(crearPanelPrincipal());
		return panel;
	}


	private BorderPane crearPanelPrincipal() {

		BorderPane panel = new BorderPane();
		area = new TextArea();
		panel.setCenter(area);

		VBox pnlBotones = crearPanelOperaciones();
		panel.setLeft(pnlBotones);

		return panel;
	}

	private VBox crearPanelOperaciones() {

		VBox panel = new VBox();
		panel.setPadding(new Insets(10));
		panel.setSpacing(10);
		HBox cajaTexto = new HBox();
		Label lblTxt = new Label("Dias de alquiler");
		lblTxt.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
		txtDias = new TextField();
		txtDias.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
		HBox.setHgrow(lblTxt, Priority.ALWAYS);
		HBox.setHgrow(lblTxt, Priority.ALWAYS);
		cajaTexto.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
		cajaTexto.getChildren().addAll(lblTxt,txtDias);
		cajaTexto.getStyleClass().add("hbox");
		btnBuscar = new Button("Buscar coches");
		btnBuscar.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
		btnBuscar.setOnAction(e -> buscar());
		btnListarTodo = new Button("Listar todo");
		btnListarTodo.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
		btnListarTodo.setOnAction(e -> listarTodo());
		Label botonesGrupo = new Label("Listados ordenados");
		botonesGrupo.setAlignment(Pos.CENTER);
		botonesGrupo.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
		ToggleGroup grupo = new ToggleGroup();
		rbtCoches = new RadioButton("Coches ASC matricula");
		rbtCoches.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
		rbtCoches.setToggleGroup(grupo);
		rbtCoches.setSelected(true);
		rbtFurgonetas = new RadioButton("Furgonetas DESC matricula");
		rbtFurgonetas.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
		rbtFurgonetas.setToggleGroup(grupo);
		btnMostrar = new Button("Mostrar");
		btnMostrar.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
		btnMostrar.setOnAction(e -> mostrar());
		panel.getChildren().addAll(cajaTexto,btnBuscar,btnListarTodo,botonesGrupo,rbtCoches,rbtFurgonetas,btnMostrar);
		panel.getStyleClass().add("vbox");
		return panel;
	}

	private MenuBar crearBarraMenu() {

		MenuBar barra = new MenuBar();
		Menu menuOperaciones = new Menu("Operaciones");
		itemLeer = new MenuItem("Leer de fichero");
		itemLeer.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
		itemLeer.setOnAction(e -> leerFichero());
		itemGuardar = new MenuItem("Guardar en fichero");
		itemGuardar.setAccelerator(KeyCombination.keyCombination("Ctrl+G"));
		itemGuardar.setOnAction(e -> {
			try {
				guardar();
			} catch (NullPointerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		itemSalir = new MenuItem("Salir");
		itemSalir.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		itemSalir.setOnAction(e -> salir());
		menuOperaciones.getItems().addAll(itemGuardar, itemLeer, new SeparatorMenuItem(), itemSalir);
		barra.getMenus().add(menuOperaciones);
		return barra;
	}

	private void buscar() {

		int numero = 0;
		String texto = agencia.toString();
		String dias = txtDias.getText();
		if(texto.isEmpty()) {
			area.setText("Lea fichero antes de mostrar");
		}
		else {
			if(dias.isEmpty()) {
				area.setText("Introduzca número");
			}
			else {
				try{
					numero = Integer.parseInt(dias);
					area.setText(agencia.buscarCoches(numero));
				}
				catch(NumberFormatException e) {
					area.setText("Introduzca número correcto");
				}
			}
		}
	}

	private void mostrar() {

		clear();
		String texto = agencia.toString();
		String coches = "";
		String furgonetas = "";
		if(texto.isEmpty()) {
			area.setText("Lea fichero antes de mostrar");
		}
		else {
			if(rbtCoches.isSelected()) {
				for (Coche coche : agencia.cochesOrdenadosMatricula()) {
					coches += coche.toString();
				}
				area.setText(coches);
			}
			else {
				for (Furgoneta furgoneta : agencia.furgonetasOrdenadasPorVolumen()) {
					furgonetas += furgoneta.toString();
				}
				area.setText(furgonetas);
			}
		}

	}

	private void listarTodo() {
		
		String texto = agencia.toString();
		if(texto.isEmpty()) {
			area.setText("Lea fichero antes de listar");
		}
		else {
			area.setText(texto);
		}
	}


	private void guardar() throws NullPointerException, IOException {

		clear();
		agencia.guardarMarcasModelos();
		area.setText("Guardado en el fichero");
	}

	private void leerFichero() {

		clear();
		agencia.cargarFlota();
		area.setText("Se ha leido el fichero");

	}

	private void salir() {
		
		Platform.exit();
	}


	private void clear() {

		area.clear();
	}

	private void cogerFoco() {

		txtDias.requestFocus();
		txtDias.selectAll();
	}

	public static void main(String[] args) {

		launch(args);
	}
}
