
package connectionmysql;
import java.sql.*;
import javax.swing.JOptionPane;

public class ConnectionMySql {
    
    String host =  "jdbc:mysql://localhost/";
    //String host = "jdbc:mysql://sql4.freesqldatabase.com:3306/";
    String db = "matriculas";
    //String db = "sql427417";
    
    public Statement sentencia;
    public PreparedStatement presentencia;
    public Connection con;
    public boolean connectionEstablished; //Varibale para saber si se ha establecido una conexion exitosa a la BD
    
 
    public ConnectionMySql (String userName, String password, int intent) {
        try{
            int i =0;
            System.out.println("Entrando a la base");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(host + db , userName, password);
            //con = DriverManager.getConnection(host + db, "sql427417", "rF7%eL9*");
            System.out.println("Conexión Exitosa");
            i = 1;
            //Muestra el mensaje de conexión exitosa solo en la primera conexión
            if(intent != 1) JOptionPane.showMessageDialog(null, "Conexión Exitosa");
            sentencia = con.createStatement();
            connectionEstablished = true;
        }
        catch (SQLException e){
            System.out.println("Error de MySQL");
            JOptionPane.showMessageDialog(null, "Error en la conexión a MySql");
            connectionEstablished = false;
        }
        catch (Exception e){
            System.out.println("Se ha encontrado el siguiente error: " + e.getMessage());
        }
    }
    
}

