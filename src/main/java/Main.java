import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;
import com.thoughtworks.xstream.XStream; //Guardado en XML
import com.thoughtworks.xstream.security.WildcardTypePermission;

public class Main {
	
	/*
 	Crear un programa que muestre datos de contacto (Nombre, Teléfono y e-mail) de un fichero.
	También se deben poder añadir contactos al fichero de agenda.
	Usar serialización de XML
	*/

	public static void main(String[] args) {
		
		File archivo = new File("agenda.xml");
		Agenda miAgenda;
		if (archivo.exists()) {
			miAgenda = leeXML(archivo);
		}else {
			miAgenda = new Agenda();
		}
		int opcion;
		boolean power = true;
		while (power) {
			
			muestraMenu();
			Scanner lee = new Scanner(System.in);
			opcion = lee.nextInt();
			
			if (opcion == 0) { // Cieerra la app
				power = !power;
			}
			
			if (opcion == 1) { // Muestra toda la agenda
				for (Persona contacto : miAgenda.getAgenda()) {
					System.out.println();
					System.out.println("Nombre: " + contacto.getNombre());
					System.out.println("Telf.: " + contacto.getTelefono());
					System.out.println("mail: " + contacto.getMail());
				}
			}
			
			if (opcion == 2) { // Agrega persona
				agregarPersonaA(miAgenda);
				guardaXML(miAgenda, archivo);
			}
		}
	}
	
	
	
	private static void muestraMenu() {
		System.out.println();
		System.out.println("Selecciona la opcion que desees:");
		System.out.println();
		System.out.println("1) Leer agenda completa");
		System.out.println("2) Agregar persona");
		System.out.println();
		System.out.println("0) Salir");
		System.out.print(":");
	}
	
	
	private static void agregarPersonaA(Agenda miAgenda) {
		String nombre, telf, mail;
		Scanner lee = new Scanner(System.in);
		
		System.out.print("Nombre: ");
		nombre = lee.nextLine();
		System.out.print("Telf.: ");
		telf = lee.nextLine();
		System.out.print("mail: ");
		mail = lee.nextLine();
		
		Persona nuevoContacto = new Persona(nombre,telf, mail);
		miAgenda.add(nuevoContacto);
	}
	
	
	private static void guardaXML(Agenda miAgenda, File archivo) {
		// Crea XStream e indica tipos a usar
		XStream flujox = new XStream();
		flujox.allowTypes(new Class[] {Persona.class, Agenda.class});
		
		//Serialize to XML
		try {
			flujox.toXML(miAgenda, new FileOutputStream(archivo));
		} catch (FileNotFoundException e) {
			System.out.println("Excepcion: "+e);
		}
	}
	

	private static Agenda leeXML(File archivoALeer) {
		// Crea XStream
		XStream flujox = new XStream();
		// Configurar el alias para la clase (nombre de la etiqueta XML)
        flujox.alias("miAgenda", Agenda.class);
        // Permitir deserializar la clase Agenda
        flujox.addPermission(new WildcardTypePermission(new String[] { "**" }));
        // deserializar archivo xml
        Agenda miAgenda = (Agenda) flujox.fromXML(archivoALeer);
		return miAgenda;
	}
	
}
