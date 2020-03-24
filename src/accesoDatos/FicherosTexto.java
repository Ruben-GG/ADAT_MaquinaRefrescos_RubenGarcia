package accesoDatos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

/*
 * Todas los accesos a datos implementan la interfaz de Datos
 */

public class FicherosTexto implements I_Acceso_Datos {

	File fDis; // FicheroDispensadores
	File fDep; // FicheroDepositos

	public FicherosTexto() {
		System.out.println("ACCESO A DATOS - FICHEROS DE TEXTO");
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {

		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer, Deposito>();

		String cadena;

		try {

			fDep = new File("Ficheros/datos/depositos.txt");
			FileReader file = new FileReader(fDep);
			BufferedReader br = new BufferedReader(file);

			while ((cadena = br.readLine()) != null) {
				String[] values = cadena.split(";");

				String nombre = values[0];
				int valor = Integer.parseInt(values[1]);
				int cantidad = Integer.parseInt(values[2]);

				depositosCreados.put(valor, new Deposito(nombre, valor, cantidad));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return depositosCreados;
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {

		HashMap<String, Dispensador> dispensadoresCreados = new HashMap<String, Dispensador>();

		String cadena;

		try {
			fDis = new File("Ficheros/datos/dispensadores.txt");
			FileReader file = new FileReader(fDis);
			BufferedReader br = new BufferedReader(file);

			while ((cadena = br.readLine()) != null) {
				String[] values = cadena.split(";");

				String clave = values[0];
				String nombre = values[1];
				int precio = Integer.parseInt(values[2]);
				int cantidad = Integer.parseInt(values[3]);

				dispensadoresCreados.put(clave, new Dispensador(clave, nombre, precio, cantidad));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dispensadoresCreados;

	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {

		boolean todoOK = true;

		try {
			String fileData;
			fDep = new File("Ficheros/datos/depositos.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(fDep));

			for (Map.Entry<Integer, Deposito> entry : depositos.entrySet()) {
				Deposito value = entry.getValue();

				String nombre = value.getNombreMoneda();
				int valor = value.getValor();
				int cantidad = value.getCantidad();

				fileData = nombre + ";" + valor + ";" + cantidad;

				writer.write(fileData + "\n");
			}
			writer.close();

		} catch (

		IOException e) {
			e.printStackTrace();
			todoOK = false;
		}

		return todoOK;
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {

		boolean todoOK = true;

		try {
			String fileData;
			fDis = new File("Ficheros/datos/dispensadores.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(fDis));

			for (Map.Entry<String, Dispensador> entry : dispensadores.entrySet()) {
				Dispensador value = entry.getValue();

				String clave = value.getClave();
				String nombre = value.getNombreProducto();
				int precio = value.getPrecio();
				int cantidad = value.getCantidad();

				fileData = clave + ";" + nombre + ";" + precio + ";" + cantidad;

				writer.write(fileData + "\n");
			}
			writer.close();

		} catch (

		IOException e) {
			e.printStackTrace();
			todoOK = false;
		}

		return todoOK;
	}

} // Fin de la clase