/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animacion;

import graficas.Grafica;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class AnimationFrame extends javax.swing.JFrame implements Runnable {

    private Vector<Client> clients;
    private Vector<Client> service;
    private int width, height, limit, velX = 10, velY = 8;
    private Thread animationThread;
    private Client head;
    private int r = 30;
    private boolean threadDone = false;
    private JPanel jp;
    public boolean continuar = true;
    //Variables de menu
    private JMenu jMenu1;
    private JMenu mVer;
    private JMenuBar jMenuBar1;
    private JMenuItem miSalir;
    private JMenuItem miTerminarSim;
    private JMenuItem miGraficas;
    private JMenuItem miResultados;
    //Variables graficas
    ArrayList<String> tiempos;
    ArrayList<ArrayList<String>> colasGraficas;
    ArrayList<Float> resultadosSimulacion;
    
    public AnimationFrame(ArrayList<String> tiempos, ArrayList colasGraficas, ArrayList<Float> resultadosSimulacion) {
        initMenuComponents();
        init();
        this.tiempos = tiempos;
        this.colasGraficas = colasGraficas;
        this.resultadosSimulacion = resultadosSimulacion;
    }

    private void initMenuComponents() {
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miSalir = new javax.swing.JMenuItem();
        miTerminarSim = new javax.swing.JMenuItem();
        mVer = new javax.swing.JMenu();
        miGraficas = new javax.swing.JMenuItem();
        miResultados = new javax.swing.JMenuItem();

        jMenu1.setText("Archivo");
        miSalir.setText("Salir");
        miSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSalirActionPerformed(evt);
            }
        });
        jMenu1.add(miSalir);
        miTerminarSim.setText("Terminar Simulación");
        miTerminarSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miTerminarSimActionPerformed(evt);
            }
        });
        jMenu1.add(miTerminarSim);
        mVer.setBackground(new java.awt.Color(255, 255, 255));
        mVer.setText("Ver");
        miGraficas.setText("Gráficas");
        miGraficas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miGraficasSimActionPerformed(evt);
            }
        });
        mVer.add(miGraficas);
        miResultados.setText("Resultados");
        miResultados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miResultadosActionPerformed(evt);
            }
        });
        mVer.add(miResultados);
        jMenuBar1.add(jMenu1);
        jMenuBar1.add(mVer);
        setJMenuBar(jMenuBar1);
        
    }

    // Variables declaration - do not modify                     
    //Inicializa las variables al inicio de la animacion
    public void init() {
        jp = new JPanel();
        jp.setBackground(Color.white);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jp.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        add(jp);
        width = getSize().width;
        height = getSize().height;
        clients = new Vector();
        service = new Vector();
        head = null;
        animationThread = new Thread(this);
        animationThread.start(); //Se inicia el hilo, este ejecuta run() de inmediato una unica vez.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(jp.getSize());
        setBackground(jp.getBackground());
        setVisible(true);
        mVer.setEnabled(false);
    }

    public void actionPerformedd(String action) {
        //Simula la llegada de clientes
        if (action == "llegada") {
         
            if (clients.isEmpty()) {
                //Borra cualquier rectangulo de las corridas anteriores
                getGraphics().clearRect(0, 0, getSize().width, getSize().height);
                limit = width - width / 7 - (r + 20); //Limite inicial
            } else {
                limit = limit - r; //El limite se actualiza con cada nuevo circulo que se crea
                if (limit < r) { //Si la cola llega a ocupar todo el ancho de la pantalla
                    //Se dismunuye el radio de los circulos
                    reduceRatio();
                }
            }
            int x = limit - r;
            int y = height / 3;
            clients.add(new Client(x, y, r, velX, limit));
        }
        //Simula el avance de un cliente y el movimiento de la cola
        if (action == "entradaCaja") {
            
            if (!clients.isEmpty()) { //Si la cola no esta vacia y el servidor esta libre
                //Se remueve el primero de la cola
                head = clients.remove(0);
                head.setMoveDirection(0);
                //Aparece el combo
                head.setCombo(new Combo(width - width / 7 + 150, height / 3 - 20, 35, velY, height / 3 - 20));
                service.add(head);
                //Se mueve el primero en la cola
                head.setLimit(width - width / 7 + 100); //Se cambia el limite en x
                head.setVel(velX); //Se le da velocidad de nuevo
                //Se mueve la cola
                int size = clients.size();
                limit = width - width / 7 - (50 - r); //Limite inicial
                if (size >= 1) {
                    for (int i = 0; i < size; i++) {
                        limit = limit - r;
                        clients.get(i).setLimit(limit);
                        clients.get(i).setVel(velX);
                    }
                }
            } else {
                actionPerformedd("llegada");
                actionPerformedd("entradaCaja");
            }
        }

        //Simula la salida de un cliente y su combo
        if (action == "salidaCaja") {
           
            if (head != null && head.getCombo() != null) { //Condiciones para que un cliente salga del sistema
                head.setLimit(height);
                head.setVel(velY);
                head.setMoveDirection(1);
                head.getCombo().setLimit(height);
                head.getCombo().setVelY(velY);
            }
        }
        repaint();
    }

    @Override
    public void run() {
        while (!threadDone) {
            //System.out.println(rectangles.lastElement().getX());
            repaint();
            pause(100);
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    //Dibujo de las figuras
    public void paint(Graphics g) {
        //Dibujo de la linea divisoria
        g.setColor(Color.red);
        g.drawLine(width - width / 7, 80, width - width / 7, height - 65);
        jMenuBar1.repaint();
        //paint de la cola de espera
        Client waiting;
        for (int i = 0; i < clients.size(); i++) {
            waiting = clients.elementAt(i);
            g.setColor(this.getBackground());
            waiting.draw(g); //Vieja posicion
            waiting.move();
            g.setColor(this.getForeground());
            waiting.draw(g); //Nueva posicion
        }
        //paint de la cola de servicio
        Client attending;
        for (int i = 0; i < service.size(); i++) {
            attending = service.elementAt(i);
            g.setColor(this.getBackground());
            attending.draw(g); //Vieja posicion
            //Se dibuja el combo
            if (attending.getCombo() != null) {
                attending.getCombo().draw(g); //Vieja posicion
                attending.getCombo().move();
                g.setColor(Color.blue);
                attending.getCombo().draw(g); //Nueva posicion
            }
            //Se verifica el tipo de movimiento
            if (attending.getMoveDirection() == 0) { //Movimiento horizontal
                attending.move();
            } else { //Movimiento vertical
                //paint de la salida del cliente
                attending.moveY();
                //Cuando el cliente sale de la ventana se borra de la cola de atendidos
                if (attending.getY() > height) {
                    service.remove(attending);
                }
            }
            g.setColor(this.getForeground());
            attending.draw(g); //Nueva posicion
        }

    }

    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
        }
    }

    private void reduceRatio() {
        r = r / 2;
        threadDone = true;
        //Borra cualquier rectangulo de las corridas anteriores
        getGraphics().clearRect(0, 0, width, height);
        limit = width - width / 7 - (50 - r); //Limite inicial
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).setR(r);
            limit = limit - r;
            clients.get(i).setLimit(limit);
            clients.get(i).setVel(velX);
        }
        threadDone = false;
    }
    
   
    //Metodos del menu
    private void miSalirActionPerformed(java.awt.event.ActionEvent evt) {                                              
        System.exit(0);
    } 
    private void miTerminarSimActionPerformed(ActionEvent evt) {
        continuar = false; 
        mVer.setEnabled(true);
        miTerminarSim.setEnabled(false);
    }
    private void miGraficasSimActionPerformed(ActionEvent evt) {
        Grafica graficaColaEspera = new Grafica("Clientes en cola", "Minuto", "Clientes");
        graficaColaEspera.showGraphic(tiempos, colasGraficas.get(0),0);
        Grafica graficaColaServicio = new Grafica("Clientes en servicio", "Minuto", "Clientes");
        graficaColaServicio.showGraphic(tiempos, colasGraficas.get(1),615);
    }
    private void miResultadosActionPerformed(ActionEvent evt) {
        ResultsFrame rf = new ResultsFrame(resultadosSimulacion);
        rf.setLocationRelativeTo(this);
        rf.setVisible(true);               
    }
    
    public JPanel getJp() {
        return jp;
    }

    public JMenuItem getMiTerminarSim() {
        return miTerminarSim;
    }

    public JMenu getmVer() {
        return mVer;
    }
    
    
}
