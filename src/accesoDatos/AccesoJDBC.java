package accesoDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import auxiliares.LeeProperties;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class AccesoJDBC implements I_Acceso_Datos {

	private String driver, urlbd, user, password; // Datos de la conexion
	private Connection conn1;
	private ResultSet rs;
	private PreparedStatement ps;

	public AccesoJDBC() {
		System.out.println("ACCESO A DATOS - Acceso JDBC");

		try {
			HashMap<String, String> datosConexion;

			LeeProperties properties = new LeeProperties("Ficheros/config/accesoJDBC.properties");
			datosConexion = properties.getHash();

			driver = datosConexion.get("driver");
			urlbd = datosConexion.get("urlbd");
			user = datosConexion.get("user");
			password = datosConexion.get("password");
			conn1 = null;

			Class.forName(driver);
			conn1 = DriverManager.getConnection(urlbd, user, password);
			if (conn1 != null) {
				System.out.println("Conectado a la base de datos");
			}

		} catch (ClassNotFoundException e1) {
			System.out.println("ERROR: No Conectado a la base de datos. No se ha encontrado el driver de conexion");
			// e1.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		} catch (SQLException e) {
			System.out.println("ERROR: No se ha podido conectar con la base de datos");
			System.out.println(e.getMessage());
			// e.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		}
	}

	public int cerrarConexion() {
		try {
			conn1.close();
			System.out.println("Cerrada conexion");
			return 0;
		} catch (Exception e) {
			System.out.println("ERROR: No se ha cerrado corretamente");
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {
		HashMap<Integer, Deposito> depositos = new HashMap<Integer, Deposito>();
		String sql = "SELECT * FROM depositos";

		try {
			ps = conn1.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				String nombre = rs.getString(2);
				int valor = rs.getInt(3);
				int cantidad = rs.getInt(4);

				depositos.put(valor, new Deposito(nombre, valor, cantidad));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return depositos;
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> dispensadores = new HashMap<String, Dispensador>();
		String sql = "SELECT * FROM dispensadores";

		try {
			ps = conn1.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				String clave = rs.getString(2);
				String nombre = rs.getString(3);
				int precio = rs.getInt(4);
				int cantidad = rs.getInt(5);

				dispensadores.put(clave, new Dispensador(clave, nombre, precio, cantidad));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dispensadores;
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean todoOK = true;

		String sql = "UPDATE depositos SET cantidad = ? WHERE valor = ?";

		try {
			ps = conn1.prepareStatement(sql);

			for (Map.Entry<Integer, Deposito> entry : depositos.entrySet()) {
				Deposito value = entry.getValue();

				int valor = value.getValor();
				int cantidad = value.getCantidad();

				ps.setInt(1, cantidad);
				ps.setInt(2, valor);

				ps.executeUpdate();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return todoOK;
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean todoOK = true;

		String sql = "UPDATE dispensadores SET cantidad = ? WHERE clave = ? ";

		try {
			ps = conn1.prepareStatement(sql);

			for (Map.Entry<String, Dispensador> entry : dispensadores.entrySet()) {
				Dispensador value = entry.getValue();

				String clave = value.getClave();

				int cantidad = value.getCantidad();

				ps.setInt(1, cantidad);
				ps.setString(2, clave);

				ps.executeUpdate();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return todoOK;
	}

} // Fin de la clase