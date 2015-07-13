import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;


public class  EntradaAudio {
    
    
    public static EntradaAudio INSTANCE;
    public static Minim minim1;
    public static AudioInput player1;
 
    // El constructor privado no permite que se genere un constructor por defecto.
    // (con mismo modificador de acceso que la definición de la clase) 
    public EntradaAudio(){

            minim1 =new Minim(this);
            player1 = minim1.getLineIn(Minim.MONO, 8192, 44100);    

    }
 
    public static EntradaAudio getInstance() {
        if(INSTANCE == null){
            INSTANCE = new EntradaAudio();
        }
        
        return INSTANCE;
    }

    public static AudioInput getPlayer1() {
        return player1;
    }
    
    



    
    
    
    
}
