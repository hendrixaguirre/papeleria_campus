package ni.edu.uam.papeleria_campus.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ni.edu.uam.papeleria_campus.DAO.ProductoDAO;
import ni.edu.uam.papeleria_campus.model.Producto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoController {
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<String> cmbCategoria;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtCodigoNombre;
    @FXML
    private ComboBox<String> cmbCategoriaBusqueda;
    @FXML
    private CheckBox chkSoloExistencias;
    @FXML
    private TableView<Producto> tblProductos;
    @FXML
    private TableColumn<Producto, Integer> colID;
    @FXML
    private TableColumn<Producto, String> colCodigo;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, String> colCategoria;
    @FXML
    private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML
    private TableColumn<Producto, Integer> colStock;
    @FXML
    private Label lblContador;

    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private final ProductoDAO productoDAO = new ProductoDAO();

    @FXML
    private void initialize() {
        configurarTabla();
        configurarSeleccionTableView();
        configurarCategorias();
        cargarProductos();
    }

    private void configurarTabla() {
        colID.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId_producto()).asObject());
        colCodigo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodigo()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colCategoria.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategoria()));
        colPrecio.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrecio()));
        colStock.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStock()).asObject());
        tblProductos.setItems(listaProductos);
    }


    private void configurarSeleccionTableView() {
        tblProductos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        cargarProductoSeleccionado(newValue);
                    }
                });
    }

    private void cargarProductoSeleccionado(Producto producto) {
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        cmbCategoria.setValue(producto.getCategoria());
        txtPrecio.setText(producto.getPrecio().toString());
        txtStock.setText(String.valueOf(producto.getStock()));
    }

    private void configurarCategorias() {
        cmbCategoria.setItems(FXCollections.observableArrayList("Papelería", "Tecnología", "Accesorios"));
        cmbCategoriaBusqueda.setItems(FXCollections.observableArrayList("Todas", "Papelería", "Tecnología", "Accesorios"));
        cmbCategoriaBusqueda.setValue("Todas");
    }

    private void cargarProductos() {
        listaProductos.clear();
        try {
            listaProductos.addAll(productoDAO.cargarProductos());
            tblProductos.setItems(listaProductos);
            actualizarContador();
        } catch (SQLException e) {
            mostrarErrorBaseDatos("No se pudieron cargar los productos.", e);
        }
    }

    @FXML
    private void guardarProducto() {
        if (!validarCampos()) {
            return;
        }
        String codigo = txtCodigo.getText().trim().toUpperCase();
        try {
            if (productoDAO.existeCodigo(codigo, null)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado", "Código duplicado", "El código ingresado ya pertenece a otro producto.");
                txtCodigo.requestFocus();
                return;
            }
            Producto producto = new Producto();
            producto.setCodigo(codigo);
            producto.setNombre(txtNombre.getText().trim());
            producto.setCategoria(cmbCategoria.getValue());
            producto.setPrecio(new BigDecimal(txtPrecio.getText().trim()));
            producto.setStock(Integer.parseInt(txtStock.getText().trim()));
            int filasAfectadas = productoDAO.guardarProducto(producto);
            if (filasAfectadas > 0) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Registro guardado", "Producto registrado", "El producto fue registrado correctamente.");
                crearProducto();
                recargarBusqueda();
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado", "Código duplicado", "El código ingresado ya existe.");
            } else {
                mostrarErrorBaseDatos("No se pudo guardar el producto.", e);
            }
        }
    }

    @FXML
    private void actualizarProducto() {
        Producto productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Producto no seleccionado", "Producto no seleccionado", "Seleccione un producto del TableView antes de actualizar.");
            return;
        }
        if (!validarCampos()) {
            return;
        }
        String codigo = txtCodigo.getText().trim().toUpperCase();
        try {
            if (productoDAO.existeCodigo(codigo, productoSeleccionado.getId_producto())) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado", "Código duplicado", "El código ingresado pertenece a otro producto.");
                txtCodigo.requestFocus();
                return;
            }
            Producto producto = new Producto();
            producto.setId_producto(productoSeleccionado.getId_producto());
            producto.setCodigo(codigo);
            producto.setNombre(txtNombre.getText().trim());
            producto.setCategoria(cmbCategoria.getValue());
            producto.setPrecio(new BigDecimal(txtPrecio.getText().trim()));
            producto.setStock(Integer.parseInt(txtStock.getText().trim()));
            int filasAfectadas = productoDAO.actualizarProducto(producto);

            if (filasAfectadas > 0) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Registro actualizado", "Actualización completada", "El producto fue actualizado correctamente.");
                crearProducto();
                recargarBusqueda();
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Producto no encontrado", "No se pudo actualizar", "El producto seleccionado ya no existe.");
                cargarProductos();
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado", "Código duplicado", "El código ingresado ya está siendo utilizado.");
            } else {
                mostrarErrorBaseDatos("No se pudo actualizar el producto.", e);
            }
        }
    }

    @FXML
    private void eliminarProducto() {
        Producto productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Producto no seleccionado", "Producto no seleccionado", "Seleccione un producto antes de eliminarlo.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar producto");
        confirmacion.setContentText("¿Desea eliminar el producto " + productoSeleccionado.getNombre() + "?");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) {
            return;
        }

        try {
            int filasAfectadas = productoDAO.eliminarProducto(productoSeleccionado.getId_producto());
            if (filasAfectadas > 0) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Producto eliminado", "Eliminación completada", "El producto fue eliminado correctamente.");
                crearProducto();
                recargarBusqueda();
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Producto no encontrado", "No se pudo eliminar", "El producto seleccionado ya no existe.");
                cargarProductos();
            }
        } catch (SQLException e) {
            mostrarErrorBaseDatos("No se pudo eliminar el producto.", e);
        }
    }


    @FXML
    private void crearProducto() {
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        tblProductos.getSelectionModel().clearSelection();
        txtCodigo.requestFocus();
    }

    @FXML
    private void buscarProducto() {
        listaProductos.clear();
        String texto = txtCodigoNombre.getText().trim();
        String categoria = cmbCategoriaBusqueda.getValue();
        boolean soloExistencias = chkSoloExistencias.isSelected();

        try {
            List<Producto> productos = productoDAO.buscarProductos(texto, categoria, soloExistencias);
            listaProductos.addAll(productos);
            tblProductos.setItems(listaProductos);
            actualizarContador();

            if (listaProductos.isEmpty()) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sin coincidencias", "Búsqueda completada", "No existen productos que coincidan con los filtros aplicados.");
            }
        } catch (SQLException e) {
            mostrarErrorBaseDatos("No se pudo realizar la búsqueda.", e);
        }
    }

    @FXML
    private void limpiarFiltros() {
        txtCodigoNombre.clear();
        cmbCategoriaBusqueda.setValue("Todas");
        chkSoloExistencias.setSelected(false);
        cargarProductos();
    }

    @FXML
    private void recargarBusqueda() {
        buscarProducto();
    }

    private boolean validarCampos() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || txtPrecio.getText().trim().isEmpty() || txtStock.getText().trim().isEmpty() || cmbCategoria.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos requeridos", "Datos incompletos", "Complete todos los campos obligatorios.");
            return false;
        }

        if (codigo.length() > 10) {
            mostrarAlerta(Alert.AlertType.WARNING, "Código incorrecto", "Código no válido", "El código debe tener entre 1 y 10 caracteres.");
            txtCodigo.requestFocus();
            return false;
        }

        if (nombre.length() < 3 || nombre.length() > 80) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nombre incorrecto", "Nombre no válido", "El nombre debe tener entre 3 y 80 caracteres.");
            txtNombre.requestFocus();
            return false;
        }

        try {
            BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());

            if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Precio incorrecto", "Precio no válido", "El precio debe ser mayor que cero.");
                return false;
            }
            if (precio.scale() > 2) {
                mostrarAlerta(Alert.AlertType.WARNING, "Precio incorrecto", "Precio no válido", "El precio no puede tener más de dos decimales.");
                return false;
            }
            if (precio.compareTo(new BigDecimal("99999999.99")
            ) > 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Precio incorrecto", "Precio fuera de rango", "El precio máximo permitido es 99999999.99.");
                return false;
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "Precio incorrecto", "Precio no válido", "Ingrese un número válido en el precio.");
            return false;
        }

        try {
            int stock = Integer.parseInt(txtStock.getText().trim());
            if (stock < 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Stock incorrecto", "Stock no válido", "El stock no puede ser negativo."
                );
                return false;
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "Stock incorrecto", "Stock no válido", "Ingrese un número entero válido en el stock.");
            return false;
        }
        return true;
    }

    private void actualizarContador() {
        if (lblContador != null) {
            lblContador.setText("Registros visibles: " + listaProductos.size());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarErrorBaseDatos(String mensaje, SQLException e) {
        System.err.println("Error de base de datos. SQLState: " + e.getSQLState());
        mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos", "Ocurrió un error", mensaje);
    }
}
