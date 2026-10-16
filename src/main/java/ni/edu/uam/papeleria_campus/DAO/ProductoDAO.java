package ni.edu.uam.papeleria_campus.DAO;

import ni.edu.uam.papeleria_campus.database.ConexionBD;
import ni.edu.uam.papeleria_campus.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    public List<Producto> cargarProductos() throws SQLException {
        List<Producto> listaProductos = new ArrayList<>();
        String sql = """
                SELECT
                    id_producto,
                    codigo,
                    nombre,
                    categoria,
                    precio,
                    stock
                FROM catalogo.producto
                ORDER BY codigo ASC
                """;
        try (
                Connection conexion = ConexionBD.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                listaProductos.add(crearProductoDesdeResultSet(rs));
            }
        }
        return listaProductos;
    }

    private Producto crearProductoDesdeResultSet(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId_producto(rs.getInt("id_producto"));
        producto.setCodigo(rs.getString("codigo"));
        producto.setNombre(rs.getString("nombre"));
        producto.setCategoria(rs.getString("categoria"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        producto.setStock(rs.getInt("stock"));
        return producto;
    }

    public int guardarProducto(Producto producto) throws SQLException {
        String sql = """
                INSERT INTO catalogo.producto
                (
                    codigo,
                    nombre,
                    categoria,
                    precio,
                    stock
                )
                VALUES (?, ?, ?, ?, ?)
                """;
        try (
                Connection conexion = ConexionBD.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setBigDecimal(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());
            return ps.executeUpdate();
        }
    }


    public int actualizarProducto(Producto producto) throws SQLException {
        String sql = """
                UPDATE catalogo.producto
                SET
                    codigo = ?,
                    nombre = ?,
                    categoria = ?,
                    precio = ?,
                    stock = ?
                WHERE id_producto = ?
                """;
        try (
                Connection conexion = ConexionBD.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setBigDecimal(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());
            ps.setInt(6, producto.getId_producto());
            return ps.executeUpdate();
        }
    }

    public int eliminarProducto(int idProducto) throws SQLException {
        String sql = """
                DELETE FROM catalogo.producto
                WHERE id_producto = ?
                """;
        try (
                Connection conexion = ConexionBD.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate();
        }
    }

    public boolean existeCodigo(String codigo, Integer idProductoExcluir) throws SQLException {
        String sql;
        if (idProductoExcluir == null) {
            sql = """
                    SELECT id_producto
                    FROM catalogo.producto
                    WHERE codigo = ?
                    """;
        } else {
            sql = """
                    SELECT id_producto
                    FROM catalogo.producto
                    WHERE codigo = ?
                    AND id_producto <> ?
                    """;
        }

        try (
                Connection conexion = ConexionBD.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {
            ps.setString(1, codigo);

            if (idProductoExcluir != null) {
                ps.setInt(2, idProductoExcluir);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Producto> buscarProductos(String texto, String categoria, boolean soloExistencias) throws SQLException {
        List<Producto> listaProductos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT
                    id_producto,
                    codigo,
                    nombre,
                    categoria,
                    precio,
                    stock
                FROM catalogo.producto
                WHERE 1 = 1
                """);
        List<Object> parametros = new ArrayList<>();

        if (texto != null && !texto.isBlank()) {
            sql.append("""
                    
                    AND (
                        codigo ILIKE ?
                        OR nombre ILIKE ?
                    )
                    """);
            parametros.add("%" + texto.trim() + "%");
            parametros.add("%" + texto.trim() + "%");
        }

        if (categoria != null && !categoria.equals("Todas")) {
            sql.append("""
                    
                    AND categoria = ?
                    """);
            parametros.add(categoria);
        }

        if (soloExistencias) {
            sql.append("""
                    
                    AND stock > 0
                    """);
        }
        sql.append("""
                
                ORDER BY codigo ASC
                """);

        try (
                Connection conexion = ConexionBD.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql.toString())
        ) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaProductos.add(crearProductoDesdeResultSet(rs));
                }
            }
        }
        return listaProductos;
    }
}
