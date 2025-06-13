
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Osmin Tovar
 */
public class Conexion {

    public static Connection conectorcito() {
        Properties props = new Properties();
        File configFile;

        try {
            String jarDir = new File(
                    Conexion.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            ).getParent();

            configFile = new File(jarDir + File.separator + "config.properties");
            System.out.println("🔍 Buscando config.properties en: " + configFile.getAbsolutePath());

            // Si no existe el archivo, lo crea y SIGUE intentando conectarse
            if (!configFile.exists()) {
                try (FileOutputStream out = new FileOutputStream(configFile)) {
                    props.setProperty("db.server", "localhost");
                    props.setProperty("db.port", "1433");
                    props.setProperty("db.name", "NombreDeTuBase");
                    props.setProperty("db.user", "sa");
                    props.setProperty("db.password", "1234");
                    props.store(out, "Configuración de conexión a SQL Server");
                    JOptionPane.showMessageDialog(null,
                            "⚠️ Se creó el archivo de configuración. Modifícalo con los datos correctos y vuelve a ejecutar.",
                            "Archivo creado", JOptionPane.INFORMATION_MESSAGE);
                    return null; // Aquí puedes dejar return null para forzar la edición del config
                }
            }

            // Cargar configuración y conectar
            try (FileInputStream in = new FileInputStream(configFile)) {
                props.load(in);
                String servidor = props.getProperty("db.server");
                String puerto = props.getProperty("db.port");
                String nombreBase = props.getProperty("db.name");
                String USER = props.getProperty("db.user");
                String PASSWORD = props.getProperty("db.password");

                String URL = "jdbc:sqlserver://" + servidor + ":" + puerto + ";databaseName=" + nombreBase + ";encrypt=false";

                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                
                return conn;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Error al conectar o cargar configuración:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

}
