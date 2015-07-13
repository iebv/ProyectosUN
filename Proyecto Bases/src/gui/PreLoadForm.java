package gui;

import connectionmysql.ConnectionMySql;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PreLoadForm extends EmptyForm {

    private ConnectionMySql conexionMysql;
    private String cedulaAcudiente;
    private String codigo;
    private boolean cedulaNoExistente = false;
    private JButton newButton;

    public PreLoadForm(ConnectionMySql conexionMysql, String cedula, String codigo) {
        super(conexionMysql, cedula, codigo);
        this.conexionMysql = conexionMysql;
        this.cedulaAcudiente = cedula;
        this.codigo = codigo;
        preloadDataEstudiante();
        preLoadDataAcudiente();
        //Se reemplaza el botón Enviar Formulario por uno nuevo (con nueva funcionalidad)
        newButton = new JButton("Enviar Formulario");
        newButton.setLocation(this.getbEnviarFormulario().getLocation());
        newButton.setSize(this.getbEnviarFormulario().getSize());
        remove(this.getbEnviarFormulario());
        add(newButton);
        newButton.setVisible(true);
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    newButtonActionPerformed(evt);
                } catch (SQLException ex) {
                    Logger.getLogger(PreLoadForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    //En esta funcion se precarga los datos en el formulario
    private void preloadDataEstudiante() {
        try {
            //DATOS ESTUDIANTE
            ResultSet rs = conexionMysql.sentencia.executeQuery("select apellido, nombre, direccion, documento, eps, telefono, celular "
                    + "from estudiante where codigo = " + codigo);

            //Se guardan los resultados del select
            ResultSetMetaData rsmd = rs.getMetaData();
            ArrayList<String> datosEstudiante = new ArrayList();
            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    datosEstudiante.add(rs.getString(i));
                }
            }
            //Se agregan los resultados a los campos de texto correspondientes
            Component[] componentsEstudiante;
            componentsEstudiante = getjPanel2().getComponents();
            for (int i = 0; i < componentsEstudiante.length; i++) {
                if (componentsEstudiante[i].getClass().getName().toString().equals("javax.swing.JTextField")) {
                    JTextField jtf = (JTextField) componentsEstudiante[i];
                    jtf.setText(datosEstudiante.remove(0));
                }
            }
            //Se agregan los datos de la matricula
            rs = conexionMysql.sentencia.executeQuery("select grado, monto_matricula, fecha_ingreso from estudiante where codigo = "
                    + codigo);
            if (rs.next()) {
                getCbGrado().setSelectedItem(rs.getString(1));
                getCbMonto().setSelectedItem(rs.getString(2));
                getCbAño().setSelectedItem(rs.getString(3));
            }

        } catch (SQLException ex) {
            Logger.getLogger(PreLoadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void preLoadDataAcudiente() {
        try {
            //Se comprueba que exista la cedula del acudiente
            ResultSet rs = conexionMysql.sentencia.executeQuery("select count(cedula) as cedulacount from acudiente where"
                    + " cedula = " + "\"" + cedulaAcudiente + "\"");
            if (rs.next()) {
                if (rs.getInt("cedulacount") == 1) {
                    //DATOS ACUDIENTE
                    ResultSet rs2 = conexionMysql.sentencia.executeQuery("select nombre, apellido, direccion, telefono, celular from acudiente"
                            + " where cedula = " + cedulaAcudiente);
                    //Se guardan los resultados del select
                    ResultSetMetaData rsmd2 = rs2.getMetaData();
                    ArrayList<String> datosAcudiente = new ArrayList();
                    while (rs2.next()) {
                        for (int i = 1; i <= rsmd2.getColumnCount(); i++) {
                            datosAcudiente.add(rs2.getString(i));
                        }
                    }
                    //Se agregan los resultados a los campos de texto correspondientes
                    Component[] componentsAcudiente;
                    componentsAcudiente = getjPanel3().getComponents();
                    for (int i = 0; i < componentsAcudiente.length; i++) {
                        if (componentsAcudiente[i].getClass().getName().toString().equals("javax.swing.JTextField")) {
                            JTextField jtf = (JTextField) componentsAcudiente[i];
                            jtf.setText(datosAcudiente.remove(0));
                        }
                    }

                    //Se agregan los datos de empleado
                    ResultSet rs3 = conexionMysql.sentencia.executeQuery("select empresa from acudiente where cedula = "
                            + cedulaAcudiente);
                    if (rs3.next()) {
                        if (rs3.getString(1).equals("UN")) {
                            this.getRbSi().setSelected(true);
                            this.getPanelEmpleadoUN().setVisible(true);
                            rs3 = conexionMysql.sentencia.executeQuery("select area_trabajo, extension from acudiente where cedula = "
                                    + cedulaAcudiente);
                            if (rs3.next()) {
                                this.getCbAreaTrabajo().setSelectedItem(rs3.getString(1));
                                this.getTfExtEmpUN().setText(String.valueOf(rs3.getInt(2)));
                            }
                            System.out.println(rs3.getString(1));
                        } else {
                            this.getRbNo().setSelected(true);
                            this.getPanelEmpleado().setVisible(true);
                            rs3 = conexionMysql.sentencia.executeQuery("select empresa, direccion_empresa, telefono_empresa, extension "
                                    + "from acudiente where cedula = " + cedulaAcudiente);
                            if (rs3.next()) {
                                this.getTfEmpresa().setText(rs3.getString(1));
                                this.getTfDirEmpresa().setText(rs3.getString(2));
                                this.getTfTelEmpresa().setText(rs3.getString(3));
                                this.getTfExtension().setText(String.valueOf(rs3.getInt(4)));
                            }
                        }
                    }
                } else {
                    cedulaNoExistente = true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PreLoadForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void newButtonActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
        Component[] componentsEstudiante = this.getjPanel2().getComponents();//Lista de los componentes del panel con los datos del estudiante
        Component[] componentsAcudiente = this.getjPanel3().getComponents();//Lista de los componentes del panel con los datos del acudiente
        if (this.checkForm(componentsEstudiante, componentsAcudiente) == false) {
            JOptionPane.showMessageDialog(null, "Imposible enviar el formulario, campos faltantes");
        } else {
            JOptionPane.showConfirmDialog(null, "¿Esta seguro de enviar el formulario?");
            if (updateDatos()) { //Se verifica que el ingreso de datos en ambas tablas halla sido correcto
                JOptionPane.showMessageDialog(null, "Matricula realizada correctamente");
                crearReporteMatricula();//Se crea el reporte de matricula 
                this.dispose(); //Se cierra la ventana de formulario
            }
        }
    }

    private boolean updateDatos() throws SQLException {
        try {
            //ACTUALIZACION DATOS ESTUDIANTE
            conexionMysql.con.setAutoCommit(false);
            //Se prepara la sentencia
            PreparedStatement ps = conexionMysql.con.prepareStatement("update estudiante set apellido = ?, nombre = ?, direccion = ?, documento = ?, eps = ?, telefono = ?, celular = ?, "
                    + "genero = ?, grupo_sanguineo = ?, monto_matricula = ?, fecha_ingreso = ?, grado = ?, acudiente_matricula = ? where codigo = " + codigo);
            //Se ingresan los datos al prepated statement
            for (int i = 0; i < this.getDatosEstudiante().size(); i++) {
                ps.setString(i + 1, this.getDatosEstudiante().get(i));
            }
            ps.setString(8, this.getCbGenero().getSelectedItem().toString());
            ps.setString(9, this.getCbGrupoSang().getSelectedItem().toString());
            ps.setInt(10, Integer.parseInt(this.getCbMonto().getSelectedItem().toString()));
            ps.setString(11, this.getCbAño().getSelectedItem().toString());
            ps.setString(12, this.getCbGrado().getSelectedItem().toString());
            ps.setString(13, cedulaAcudiente);
            //Se ejecuta el prepared statement
            ps.executeUpdate();

            //ACTUALIZACION ACUDIENTE
            if (cedulaNoExistente) { //En caso de que la cedula no exista debe insertarse un nuevo acudiente
                //DATOS ACUDIENTE
                if (getRbSi().isSelected()) {
                    //Se prepara la sentencia
                    PreparedStatement ps1 = conexionMysql.con.prepareStatement("insert into acudiente (cedula, nombre, apellido, direccion, telefono, celular, extension,"
                            + "area_trabajo) values (?,?,?,?,?,?,?,?)");
                    //Se ingresan los datos al prepated statement
                    ps1.setString(1, cedulaAcudiente);
                    for (int i = 0; i < getDatosAcudiente().size(); i++) {
                        ps1.setString(i + 2, getDatosAcudiente().get(i));
                    }
                    ps1.setString(8, getCbAreaTrabajo().getSelectedItem().toString());
                    //Se ejecuta el prepared statement
                    ps1.executeUpdate();
                }
                if (getRbNo().isSelected()) {
                    //Se prepara la sentencia
                    PreparedStatement ps2 = conexionMysql.con.prepareStatement("insert into acudiente (cedula, nombre, apellido, direccion, telefono, celular,"
                            + "empresa, telefono_empresa, direccion_empresa, extension) values (?,?,?,?,?,?,?,?,?,?)");
                    ps2.setString(1, cedulaAcudiente);
                    for (int i = 0; i < getDatosAcudiente().size(); i++) {
                        ps2.setString(i + 2, getDatosAcudiente().get(i));
                    }
                    //Se ejecuta el prepared statement
                    ps2.executeUpdate();
                }
            } else { //Si la cedula existe solo debe actualizarse la informacion del acudiente
                if (this.getRbSi().isSelected()) {
                    //Se prepara la sentencia
                    PreparedStatement ps1 = conexionMysql.con.prepareStatement("update acudiente set nombre = ?, apellido = ?, direccion = ?, telefono = ?, celular = ?, extension = ?,"
                            + " area_trabajo = ? where cedula = " + "\"" + cedulaAcudiente + "\"");

                    //Se ingresan los datos al prepated statement
                    for (int i = 0; i < this.getDatosAcudiente().size(); i++) {
                        ps1.setString(i + 1, this.getDatosAcudiente().get(i));
                    }
                    ps1.setString(7, this.getCbAreaTrabajo().getSelectedItem().toString());
                    //Se ejecuta el prepared statement
                    ps1.executeUpdate();
                }
                if (this.getRbNo().isSelected()) {
                    //Se prepara la sentencia
                    PreparedStatement ps2 = conexionMysql.con.prepareStatement("update acudiente set nombre = ?, apellido = ?, direccion = ?, telefono = ?, celular = ?,"
                            + " empresa= ?, telefono_empresa = ?, direccion_empresa = ?, extension = ? where cedula = " + "\"" + cedulaAcudiente + "\"");

                    //Se ingresan los datos al prepated statement
                    for (int i = 0; i < this.getDatosAcudiente().size(); i++) {
                        ps2.setString(i + 1, this.getDatosAcudiente().get(i));
                    }
                    //Se ejecuta el prepared statement
                    ps2.executeUpdate();
                }
            }
            conexionMysql.con.commit();
            return true;
        } catch (SQLException ex) {
            //Logger.getLogger(EmptyForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error en dato de estudiante");
            conexionMysql.con.rollback();
            return false;
        } finally {
            conexionMysql.con.setAutoCommit(true);
        }
    }

    //Bloquea los campos de un formulario 
    public void blockFields() {
        Component[] componentsEstudiante = this.getjPanel2().getComponents();
        Component[] componentsAcudiente = this.getjPanel3().getComponents();
        for (int i = 0; i < componentsEstudiante.length; i++) {
            componentsEstudiante[i].setEnabled(false);
        }
        for (int i = 0; i < componentsAcudiente.length; i++) {
            //Bloquea los campos del panel de empleado
            if (componentsAcudiente[i].getName() == "panelEmpleadoUN" || componentsAcudiente[i].getName() == "panelEmpleado") {
                JPanel jp = (JPanel) componentsAcudiente[i];
                Component[] componentsEmpleado = jp.getComponents();
                for (int j = 0; j < componentsEmpleado.length; j++) {
                    componentsEmpleado[j].setEnabled(false);
                }
            }
            componentsAcudiente[i].setEnabled(false);
        }
    }

    @Override
    public void crearReporteMatricula() {
        try {
            Date fecha = new Date();
            int count = 0;
            //Se verifica si se trata de una actualización o una inserción
            ResultSet res = conexionMysql.sentencia.executeQuery("select count(*) from reporte_matricula where estudiante_codigo = " + "\"" + codigo + "\""
                    + " and año_historico = " + Calendar.getInstance().get(Calendar.YEAR));
            if (res.next()) {
                count = res.getInt(1);
            }
            if (count == 0) { //Si no existe el reporte previamente se inserta uno nuevo
                PreparedStatement ps = conexionMysql.con.prepareStatement("insert into reporte_matricula values (?,?,?,?,?)");
                //Se ingresan los datos al prepared statement
                if (codigo.equals("0")) { //Se obtiene el codigo en caso de tratarse de una nueva matricula
                    ResultSet rs = conexionMysql.sentencia.executeQuery("select LAST_INSERT_ID()");
                    if (rs.next()) {
                        codigo = Integer.toString(rs.getInt(1));
                    }
                }
                ps.setString(1, codigo);
                ps.setString(2, cedulaAcudiente);
                ps.setString(3, getCbAño().getSelectedItem().toString());
                //Se configura la fecha para pasarla como parametro en la insercion
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                ps.setString(4, sdf.format(fecha)); //Se agrega la fecha
                ps.setString(5, getCbGrado().getSelectedItem().toString());
                //Se ejecuta la sentencia
                ps.executeUpdate();
            }
            if(count == 1){ //Si el reporte ya existe se debe actualizar el mismo
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                PreparedStatement ps = conexionMysql.con.prepareStatement("update reporte_matricula set fecha = ?, grado = ?, acudiente_cedula = ? where estudiante_codigo = " + codigo +
                                        " and año_historico = " + Calendar.getInstance().get(Calendar.YEAR));
                ps.setString(1, sdf.format(fecha));
                ps.setString(2, getCbGrado().getSelectedItem().toString());
                ps.setString(3, cedulaAcudiente);
                ps.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(EmptyForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JButton getNewButton() {

        return newButton;
    }

}
