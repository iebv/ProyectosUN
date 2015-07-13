

package graficas;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class Grafica {
    JFreeChart grafica;
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String titulo;
    String tx;
    String ty;
    
    public Grafica(String titulo, String tx, String ty){
        this.titulo = titulo;
        this.tx = tx;
        this.ty = ty;
    }
    
    public JPanel createGraphic(ArrayList entradaX, ArrayList entradaY){
        fillData(entradaX, entradaY);
        grafica = ChartFactory.createBarChart(titulo, tx, ty, dataset, PlotOrientation.HORIZONTAL, true, true, true);
        return obtienePanel();
    }
    
    public void fillData(ArrayList entradaX, ArrayList entradaY){
        int n = entradaX.size();
        for(int i=0; i<n; i++){
            dataset.setValue((double)entradaY.get(i), "Clientes", entradaX.get(i).toString());
        }        
    }
    
    public JPanel obtienePanel(){
         return new ChartPanel(grafica);
    }
    
    public void showGraphic(ArrayList<String> tiempos, ArrayList<String> tamañoCola, int point){
        JFrame jf = new JFrame("Grafica cola");
        JPanel jp = this.createGraphic(tiempos, tamañoCola);
        jf.setSize(600, 600);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.add(jp);
        jf.setVisible(true);
        jf.setLocation(point, 0);
    }
    
}
