


import animacion.*;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JPanel;

public class Animation extends JPanel implements Runnable/*, ActionListener */ {

    private Vector<Client> clients;
    private Vector<Client> service;
    private int width, height, limit, velX = 12, velY = 8;
    private Button startClient, advance, departClient;
    private Thread animationThread;
    private Client head;
    private int r = 30;
    private boolean threadDone = false;
   
    //Inicializa las variabñes al inicio de la animacion
    public void init() {
        setBackground(Color.white);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - 60);
        width = getSize().width;
        height = getSize().height;
        clients = new Vector();
        service = new Vector();
        head = null;
        animationThread = new Thread(this);
        animationThread.start(); //Se inicia el hilo, este ejecuta run() de inmediato una unica vez.
        startClient = new Button("Llegada");
        add(startClient);
        advance = new Button("Avance");
        add(advance);
        departClient = new Button("Salida");
        add(departClient);
        
    }

    public void actionPerformedd(String action) {
        //Simula la llegada de clientes
        if (action == "llegada") {
            startClient.setBackground(Color.yellow);
            advance.setBackground(null);
            departClient.setBackground(null);
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
            advance.setBackground(Color.yellow);
            startClient.setBackground(null);
            departClient.setBackground(null);
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
            departClient.setBackground(Color.yellow);
            advance.setBackground(null);
            startClient.setBackground(null);
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
        g.drawLine(width - width / 7, 30, width - width / 7, height - 65);

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

}
