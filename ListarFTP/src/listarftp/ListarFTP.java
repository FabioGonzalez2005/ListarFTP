package listarftp;

import java.io.IOException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class ListarFTP {

    public static void main(String[] args) {
        String hostFTP = "ftp.rediris.es"; 
        String nombreUsuario = "miUsuarioFTP";
        String clave = "";
        
        FTPClient clienteFtp = new FTPClient(); 
        try {
            // Intentar conexión con el servidor FTP
            clienteFtp.connect(hostFTP); 
            int codigoRespuestaServidor = clienteFtp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(codigoRespuestaServidor)) { 
                System.out.printf("No se pudo establecer la conexión. Código de respuesta: %d.\n", 
                    codigoRespuestaServidor); 
                System.exit(2); 
            }
            
            // Iniciar sesión si los parámetros son válidos
            if (nombreUsuario != null && clave != null) {
                boolean inicioSesionExitoso = clienteFtp.login(nombreUsuario, clave); 
                if (inicioSesionExitoso) {
                    System.out.printf("Conexión exitosa con el usuario %s.\n", 
                        nombreUsuario); 
                }
                else { 
                    System.out.printf("Fallo en el inicio de sesión con el usuario %s.\n", 
                        nombreUsuario); 
                    return; 
                } 
            } 

            // Preparar el modo pasivo y establecer el tipo de archivo
            clienteFtp.enterLocalPassiveMode(); 
            clienteFtp.setFileType(FTP.BINARY_FILE_TYPE);
            
            // Mostrar el mensaje de bienvenida
            System.out.printf("Conexión establecida correctamente. Mensaje del servidor: %s", clienteFtp.getReplyString()); 
            
            // Obtener y mostrar el directorio de trabajo
            System.out.printf("Actualmente en el directorio %s. Archivos disponibles:\n", clienteFtp.printWorkingDirectory()); 
            
            // Listar los archivos del servidor
            FTPFile[] archivosEnServidor = clienteFtp.listFiles(); 
            for (FTPFile archivo : archivosEnServidor) {
                String informacionAdicional = ""; 
                if(archivo.getType() == FTPFile.DIRECTORY_TYPE) { 
                    informacionAdicional = "/";
                }
                else if(archivo.getType() == FTPFile.SYMBOLIC_LINK_TYPE) {
                    informacionAdicional = " -> " + archivo.getLink();
                }
                System.out.printf("%s%s\n", archivo.getName(), informacionAdicional);
            } 
        } catch (IOException e){
            // Capturar y mostrar errores de conexión
            System.out.println("No se pudo conectar al servidor.");
            e.printStackTrace();
            return;
        } finally {
            // Cerrar la conexión al servidor
            if (clienteFtp != null) {
                try {
                    clienteFtp.disconnect();
                    System.out.println("Conexión terminada exitosamente.");
                } catch (IOException e) {
                    System.out.println("No se pudo cerrar la conexión.");
                }
            }
        }
    }
}