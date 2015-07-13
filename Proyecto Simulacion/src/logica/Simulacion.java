package logica;

//La simulación de eventos discretos usa la libreria SIMLIB en su versión para JAVA
//Esta libreria es una versión orientada a objetos análoga a la versión estructurada creada para FORTRAN Y C++
//Para más informacipon visitar http://archive.ite.journal.informs.org/Vol2No1/Huffman/Huffman.php
import animacion.AnimationFrame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Simulacion {

    static File file = new File("Report.txt"); //Archivo donde se generará el reporte
    static PrintWriter pw; //Variabla para escribir lineas en el archivo
    static Date var = new Date(); //Semilla para las corridas de cada dia de la simulacion

    //Contadores estadisticos
    static DiscreteStat clientesAtendidos;
    static ContinStat clientesCola;
    static DiscreteStat durCaja;
    static DiscreteStat durServ;
    static DiscreteStat durEspera;
    static ContinStat usoCaja;
    static final int YES = 1; //caja en uso
    static final int NO = 0; //caja libre
    static boolean cajaLibre;

    //Otras variables
    static Timer clock = new Timer(); //Reloj de la simulacion
    static float currentTime;
    static List event_list = new List(); //Lista de eventos
    static LinkedList<Cliente> colaEsp; //Cola de espera
    static LinkedList<Cliente> colaServ;
    static Cliente[] servidor;
    static final int total_cajas = 1; //Numero total de cajas
    static final float MEANEXP = 1 / (float) 1.64527; //Media para la dist.exponencial de llegada en minutos
    static final float MEANERL = 1 / (float) 0.735; //Media para la dist.erlang de duracion de servicio en minutos
    static final int K = 3; //k para la dist.erlang de duración del servicio
    static final int simTime = 60; //Tiempo de la simulación (2 horas)
    static boolean go;
    static int totalRuns, selectedRun, currentRun;
    static ArrayList tiempos = new ArrayList();
    static ArrayList tamañoCola = new ArrayList();
    static ArrayList tamañoColaServicio = new ArrayList();
    static ArrayList<String> eventosAnimacion = new ArrayList();
    static ArrayList<Float> resultadosSimulacion = new ArrayList();
 
    //Rutina de inicialización.
    public static void inic() {
        //Condiciones iniciales
        clock.setTime(0); //Reloj en 0.0
        colaEsp = new LinkedList<>();
        colaServ = new LinkedList();
        clientesAtendidos = new DiscreteStat();
        clientesCola = new ContinStat(colaEsp.size(), clock.getTime());
        usoCaja = new ContinStat(NO, clock.getTime());
        durEspera = new DiscreteStat();
        durCaja = new DiscreteStat();
        durServ = new DiscreteStat();
        servidor = new Cliente[1];
        go = true;
        //Se programa la llegada del primer cliente a la cafeteria
        SimObject event = new SimObject();
        event.setName("llegada");
        float time = clock.getTime() + Random.expon(MEANEXP, var.getSeconds());
        event.setTime(clock.getTime() + time);
        event_list.insertInOrder(event, event.getTime());

        //Se llama a la rutina temporizadora
        temp();
    }

    public static void temp() {
        SimObject removed = event_list.removeFromFront(); //Se remueve el primer evento en la lista de eventos
        clock.setTime(removed.getTime()); //Se actualiza el reloj de la simulacion
        currentTime = clock.getTime();
        DecimalFormat df = new DecimalFormat("0");
        if (currentTime < simTime) { //Se ejecutan las rutinas mientras se este dentro del tiempo de simulacion	
            //System.out.println(currentTime);
            if(currentRun == selectedRun){ //Se guardan lso datos de la corrida a animar
                System.out.println(removed.getName());
                eventosAnimacion.add(removed.getName());
                //System.out.println(df.format(currentTime));
                //Se guardan los datos principales del sistema cada minuto para generar las graficas
                if (tiempos.contains(df.format(currentTime)) == false) {
                    tiempos.add(df.format(currentTime));
                    tamañoCola.add((double) colaEsp.size());
                    tamañoColaServicio.add((double) colaServ.size());
                }  
            }
        
            switch (removed.getName()) {

                case "llegada": //Evento llegada de un cliente
                case "entradaCaja":

                    if (removed.getName().equals("llegada")) {
                        //Se ingresa el cliente a la cola
                        Cliente nuevo = new Cliente();
                        nuevo.setEntCola(currentTime);
                        colaEsp.add(nuevo);
                        clientesCola.recordContin(colaEsp.size(), currentTime);
                        //Se programa la siguiente llegada
                        SimObject event = new SimObject();
                        event.setName("llegada");
                        event.setTime(currentTime + Random.expon(MEANEXP, var.getSeconds())); //Tiempo de la llegada
                        event_list.insertInOrder(event, event.getTime()); //Insercion en la lista de eventos
                    }
                    //Si la caja esta libre...
                    if (servidor[0] == null) {
                        //Se programa la salida de la caja
                        SimObject event2 = new SimObject();
                        event2.setName("salidaCaja");
                        event2.setTime(currentTime + Random.unifrm((float) 0.50, (float) 1.2, var.getSeconds()));
                        if(currentRun == selectedRun) eventosAnimacion.remove(eventosAnimacion.size() - 1);
                        //Un cliente es atendido (si hay tiempo para su atencion en la caja)
                        if (event2.getTime() < simTime) {
                            clientesAtendidos.recordDiscrete(1);
                            Cliente atendido = colaEsp.removeFirst();
                            clientesCola.recordContin(colaEsp.size(), currentTime);
                            //Se toman tiempos del cliente
                            atendido.setSalCola(currentTime); //Salida de la cola
                            atendido.setEntCaja(currentTime); //Entrada a la caja
                            //Se actualiza el contador de la espera en la cola
                            durEspera.recordDiscrete(atendido.getSalCola() - atendido.getEntCola());
                            //System.out.println("salida a los " + atendido.getSalCola());
                            //System.out.println("Tiempo de espera " + (atendido.getSalCola() - atendido.getEntCola()));
                            //Se ocupa la caja
                            servidor[0] = atendido;
                            usoCaja.recordContin(YES, currentTime);
                            //Se añade a la lista el evento de salida
                            event_list.insertInOrder(event2, event2.getTime());
                            //eventosAnimacion.add("llegada*");
                            if(currentRun == selectedRun) eventosAnimacion.add("entradaCaja");
                        } else {
                            finSimulacion(removed); //Si no hay tiempo para atenderlo en la caja se acaba la simulación
                            break;
                        }
                    } //Si la caja no esta libre el cliente ocupa su lugar en la cola...
                    else {
                        clientesCola.recordContin(colaEsp.size(), currentTime);
                    }
                    break;

                case "salidaCaja": //Evento salida del cliente de la caja

                    //Se toman los tiempos del cliente que sale de la caja
                    Cliente despachado = servidor[0];
                    despachado.setSalCaja(currentTime);
                    //Se actualiza contador de tiempo en caja
                    durCaja.recordDiscrete(despachado.getSalCaja() - despachado.getEntCaja());
                    //La caja queda libre
                    servidor[0] = null;
                    usoCaja.recordContin(NO, currentTime);
                    //Se programa el momento en que saldrá el cliente
                    float timeSalida = currentTime + Random.erlang(K, MEANERL, var.getSeconds());
                    SimObject event3 = new SimObject();
                    event3.setName("salidaCafeteria");
                    event3.setTime(timeSalida);
                    event_list.insertInOrder(event3, timeSalida);
                    despachado.setSalServ(timeSalida);
                    //Se añade a la cola de servicio
                    colaServ.add(despachado);
                    //Se hace pasar al primer cliente en la cola si lo hay
                    if (colaEsp.size() > 0) {
                        SimObject event4 = new SimObject();
                        event4.setName("entradaCaja");
                        event4.setTime(currentTime);
                        event_list.insertInOrder(event4, currentTime);
                    }
                    break;

                case "salidaCafeteria": //Evento salida del cliente del sistema
                    //Se toma el tiempo de salida del servicio
                    Cliente terminado = colaServ.removeFirst();
                    durServ.recordDiscrete(terminado.getSalServ() - terminado.getSalCaja());
                    break;
            }
        } else {
            finSimulacion(removed);
        }
   
    }

    public static void finSimulacion(SimObject removed) {
        //Si llega el fin de la simulacion se cierra la caja y no se admiten mas llegadas
        //pero se termina de atender a los clientes que hagan falta
        go = false;
        //Se revisa el evento que fue removido al inicio de la rutina
        if (removed.getName().equals("salidaCafeteria")) {
            if(currentRun == selectedRun) System.out.println(removed.getName() + " extemporal " + currentTime);
            //Se toma el tiempo de salida del servicio
            Cliente terminado = colaServ.removeFirst();
            durServ.recordDiscrete(terminado.getSalServ() - terminado.getSalCaja());
        }
        //Se revisan el resto de eventos en la lista de eventos
        while (!event_list.isEmpty()) {
            removed = event_list.removeFromFront();
            if (removed.getName().equals("salidaCafeteria")) {
                if(currentRun == selectedRun) System.out.println(removed.getName() + " extemporal " + currentTime);
                //Se toma el tiempo de salida del servicio
                Cliente terminado = colaServ.removeFirst();
                durServ.recordDiscrete(terminado.getSalServ() - terminado.getSalCaja());
            }
        }
    }

    //Función que escribe el reporte en el archivo
    public static void report() {
        //Reporte corrida animada
        if(currentRun == selectedRun){
            System.out.println("Clientes por atender a las 2pm");
            System.out.println(colaEsp.size());
            System.out.println("clientes atendidos");
            System.out.println(clientesAtendidos.getDiscreteSum());
            resultadosSimulacion.add(clientesAtendidos.getDiscreteSum());
            System.out.println("maximo de clientes en la cola");
            System.out.println(clientesCola.getContinMax());
            resultadosSimulacion.add(clientesCola.getContinMax());
            System.out.println("Duracion promedio de la caja");
            System.out.println(durCaja.getDiscreteAverage());
            resultadosSimulacion.add(durCaja.getDiscreteAverage());
            System.out.println("Duracion promedio del servicio de bandejas");
            System.out.println(durServ.getDiscreteAverage());
            resultadosSimulacion.add(durServ.getDiscreteAverage());
            System.out.println("Duracion promedio total del servicio");
            System.out.println(durCaja.getDiscreteAverage() + durServ.getDiscreteAverage());
            resultadosSimulacion.add(durCaja.getDiscreteAverage() + durServ.getDiscreteAverage());
            System.out.println("Espera máxima en la cola");
            System.out.println(durEspera.getDiscreteMax());
            resultadosSimulacion.add(durEspera.getDiscreteMax());
            System.out.println("Uso de la caja");
            System.out.println(usoCaja.getContinAve(currentTime) * 100);
            resultadosSimulacion.add(usoCaja.getContinAve(currentTime) * 100);
        }
        //Reporte archivo CSV
        pw.print(clientesAtendidos.getDiscreteSum() + ",");
        pw.print(clientesCola.getContinMax() + ",");
        pw.print(durCaja.getDiscreteAverage() + ",");
        pw.print(durServ.getDiscreteAverage() + ",");
        pw.print(durCaja.getDiscreteAverage() + durServ.getDiscreteAverage() + ",");
        pw.print(durEspera.getDiscreteMax()+ ",");
        pw.println(usoCaja.getContinAve(currentTime) * 100);
    }

    //Funcion principal
    public static void main(String[] args) {
        
        totalRuns = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite el número de corridas \n" + "Solo se anima una corrida elegida al azar \n\n"));
        selectedRun = 1 + (int)(Math.random() * ((totalRuns - 1) + 1));
        System.out.println("Corrida seleccionada " + selectedRun);
        //Se prepara el archio de salida para ser escrito
        try {
            pw = new PrintWriter(file);
        } catch (FileNotFoundException e) {
        }
       
        currentRun = 1;
        do {
            inic();
            while (go) {
                temp(); //Se ejecutan eventos mientras se este en el tiempo de la simulación
            }
            report(); //Al finalizar cada dia se genera el reporte del mismo
            currentRun++;
        } while (currentRun <= totalRuns); //La simulación se ejecuta n veces (dias)
        pw.close(); //Se cierra el archivo del reporte
        ArrayList colasGraficas = new ArrayList();
        colasGraficas.add(tamañoCola);
        colasGraficas.add(tamañoColaServicio);
        AnimationFrame af = new AnimationFrame(tiempos, colasGraficas, resultadosSimulacion);
        af.init();
        int numEventos = eventosAnimacion.size();
        for (int j = 0; j < numEventos; j++) {
            String evento = eventosAnimacion.get(j);
            int pausa;
            System.out.println(evento);
            if (af.continuar) {
                if (evento != "salidaCafeteria") {
                    af.actionPerformedd(eventosAnimacion.get(j));
                    if (evento == "entradaCaja") {
                        pausa = 1500;
                    } else {
                        pausa = 1000;
                    }
                    try {
                        Thread.currentThread().sleep(pausa);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Simulacion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }else j = numEventos + 1;
        }
        af.getMiTerminarSim().setEnabled(false);
        af.getmVer().setEnabled(true);
    }
}
