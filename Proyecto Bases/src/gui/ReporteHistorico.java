

package gui;

import connectionmysql.ConnectionMySql;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ReporteHistorico extends javax.swing.JFrame {
    
    ConnectionMySql conexionMySql;
    private String year, grade, code;
    
    public ReporteHistorico(ConnectionMySql conexionMySql, String year, String grade, String code) throws SQLException {
        initComponents();
        this.conexionMySql = conexionMySql;
        if(!"Todos".equals(year)){
            this.year = "\"" + year + "\"";
        }else this.year = year;
        if(!"Todos".equals(grade)){
            this.grade = "\"" + grade + "\"";
        }else this.grade = grade;
        if(code != null){
            this.code = "\"" + code + "\"";
        }else this.code = code;
        fillTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tHistorial = new javax.swing.JTable();
        bReporte = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Historial Matrícula");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        tHistorial.setAutoCreateRowSorter(true);
        tHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código Estudiante", "Cédula Acudiente", "Grado", "Fecha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tHistorial.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tHistorial);

        bReporte.setText("Ver Reporte");
        bReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bReporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(124, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bReporte)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(bReporte)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bReporteActionPerformed
        int selectedRows [] =  tHistorial.getSelectedRows(); 
        if(selectedRows.length != 0){
            String cedula = tHistorial.getValueAt(selectedRows[0], 1).toString();
            String codigo = tHistorial.getValueAt(selectedRows[0], 0).toString();
            PreLoadForm plf = new PreLoadForm(conexionMySql, cedula, codigo);
            plf.setLocationRelativeTo(this);
            plf.setVisible(true);
            plf.blockFields();
            plf.getNewButton().setVisible(false);
            plf.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //Hace que al cerrar el reporte solo se cierre este y no toda la aplicacion
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }//GEN-LAST:event_bReporteActionPerformed

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tHistorial;
    // End of variables declaration//GEN-END:variables

    private void fillTable() throws SQLException {
        DefaultTableModel dtm = new DefaultTableModel();
        //ArrayList <String> labels = new ArrayList ();
        for(int i=0; i< tHistorial.getColumnCount(); i++)
            dtm.addColumn(tHistorial.getColumnName(i));
        ResultSet rs = null;
        //Se determian el tipo de selet que debe hacerse sobre reporte_matricula
        if(year.equals("Todos") && grade.equals("Todos") && code == null) //000
            rs = conexionMySql.sentencia.executeQuery("select estudiante_codigo, acudiente_cedula, grado, fecha from reporte_view");
        if(year.equals("Todos") && grade.equals("Todos") && code != null) //001
            rs = conexionMySql.sentencia.executeQuery("select estudiante_codigo, acudiente_cedula, grado, fecha from reporte_view where"
                    + " estudiante_codigo = " + code );
        if(year.equals("Todos") && !grade.equals("Todos") && code == null) //010
            rs = conexionMySql.sentencia.executeQuery("select estudiante_codigo, acudiente_cedula, grado, fecha from reporte_view where"
                    + " grado = " + grade);
        if(year.equals("Todos") && !grade.equals("Todos") && code != null) //011
            rs = conexionMySql.sentencia.executeQuery("select estudiante_codigo, acudiente_cedula, grado, fecha from reporte_view where"
                    + " grado = " + grade + " and estudiante_codigo = " + code );
        if(!year.equals("Todos") && grade.equals("Todos") && code == null) //100
            rs = conexionMySql.sentencia.executeQuery("select estudiante_codigo, acudiente_cedula, grado, fecha from reporte_view where"
                    + " año_historico = " + year);
        if(!year.equals("Todos") && grade.equals("Todos") && code != null) //101
            rs = conexionMySql.sentencia.executeQuery("select estudiante_codigo, acudiente_cedula, grado, fecha from reporte_view where"
                    + " año_historico = " + year + " and estudiante_codigo = " + code );
        if(!year.equals("Todos") && !grade.equals("Todos") && code == null) //110
            rs = conexionMySql.sentencia.executeQuery("select estudiante_codigo, acudiente_cedula, grado, fecha from reporte_view where"
                    + " grado = " + grade + " and año_historico = " + year );
        if(!year.equals("Todos") && !grade.equals("Todos") && code != null) //111
            rs = conexionMySql.sentencia.executeQuery("select estudiante_codigo, acudiente_cedula, grado, fecha from reporte_view where"
                    +" año_historico = " + year + " and grado = " + grade + " and estudiante_codigo = " + code );
        
        //Se agregan los campos de cada registro resultante del select al modelo de la tabla
        while(rs.next()){
            Vector v = new Vector();
            for(int i=1; i<= tHistorial.getColumnCount(); i++)
                v.add(rs.getString(i));
            dtm.addRow(v);
        }
        //Se le asigna a la tabla el modelo que contiene los resultados
        tHistorial.setModel(dtm);
        
    }
}
