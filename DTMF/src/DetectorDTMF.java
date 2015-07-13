import processing.core.PFont;
import processing.core.PImage;
import processing.data.FloatList;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;


public class DetectorDTMF extends processing.core.PApplet {

	  
	private static final TonosFrecuencias tonosFrecuencias = new TonosFrecuencias();
	private AudioInput player; 
	FFT fftLin;
	PImage img0, img1, img2, img3, img4, img5, img6, img7, img8, img9, imgast, imgnum, imgbl;
	PFont f; 
	int largo=600;
    int ancho=800;
    String marcacion = " ";
    



	public void setup(){
		
		int titleBarHt = getBounds().y;
		size(ancho, largo-titleBarHt);
		
		
		img0 = loadImage("Imagenes/0.png");
		img1 = loadImage("Imagenes/1.png");
		img2 = loadImage("Imagenes/2.png");
		img3 = loadImage("Imagenes/3.png");
		img4 = loadImage("Imagenes/4.png");
		img5 = loadImage("Imagenes/5.png");
		img6 = loadImage("Imagenes/6.png");
		img7 = loadImage("Imagenes/7.png");
		img8 = loadImage("Imagenes/8.png");
		img9 = loadImage("Imagenes/9.png");
		imgast = loadImage("Imagenes/asterisco.png");
		imgnum = loadImage("Imagenes/numeral.png");
		imgbl = loadImage("Imagenes/blanco.png");
		
		EntradaAudio x = EntradaAudio.getInstance();
    	player = x.getPlayer1();
        f = createFont("Arial",16,true); // STEP 3 Create Font
              
		
	}
        
       

	public void draw(){
		
		fill(0);
		rect(0,0,ancho,largo);
		fill(255);
		textSize(30);
		text(marcacion,ancho-20*marcacion.length(),50);
		
		image(imgbl,0,100,ancho,largo-100);
	
		fftLin = new FFT(player.mix.size(), 44100);
		fftLin.forward(player.mix);
		float a[] = fftLin.getSpectrumReal();
		float maxBaja = a[floor(600 / fftLin.getBandWidth())];
		int posicionMaxBaja = floor(600 / fftLin.getBandWidth());

		for (int i = floor(600 / fftLin.getBandWidth()); i < floor(1000 / fftLin
				.getBandWidth()); i++) {
			if (a[i] > maxBaja) {
				maxBaja = a[i];
				posicionMaxBaja = i;
			}

		}
		

		float freqBaja = fftLin.getBandWidth() * posicionMaxBaja;

		float maxAlta = a[floor(1100 / fftLin.getBandWidth())];
		int posicionMaxAlta = floor(1100 / fftLin.getBandWidth());

		for (int i = floor(1100 / fftLin.getBandWidth()); i < floor(1700 / fftLin
				.getBandWidth()); i++) {
			if (a[i] > maxAlta) {
				maxAlta = a[i]; 
				posicionMaxAlta = i;
			}

		}
		
		float freqAlta = fftLin.getBandWidth() * posicionMaxAlta;

		/*for (int i = 0; i < fftLin.specSize(); i++) {
			// draw the line for frequency band i, scaling it by 4 so we can see
			// it a bit better
			line(i, height, i, height - fftLin.getBand(i) * 4);

		}*/

		// println("frecuenciaBaja: "+freqBaja + "frecuenciaAlta: "+freqAlta);

		String[] numero = tonosFrecuencias.detectarTono(freqAlta, freqBaja, maxAlta);
		marcacion += numero[0];
		drawKeypad(numero[1].charAt(numero[1].length()-1));
		print(numero[0]);
		
	}
                
     
        public void drawKeypad(char numero){
        	switch(numero){
        	case '1':
        		image(img1,0,100,ancho,largo-100);
        		break;
        	case '2':	
        		image(img2,0,100,ancho,largo-100);
        		break;
        	case '3':	
        		image(img3,0,100,ancho,largo-100);
        		break;
        	case '4':	
        		image(img4,0,100,ancho,largo-100);
        		break;
        	case '5':	
        		image(img5,0,100,ancho,largo-100);
        		break;
        	case '6':	
        		image(img6,0,100,ancho,largo-100);
        		break;
        	case '7':	
        		image(img7,0,100,ancho,largo-100);
        		break;
        	case '8':	
        		image(img8,0,100,ancho,largo-100);
        		break;
        	case '9':	
        		image(img9,0,100,ancho,largo-100);
        		break;
        	case '0':	
        		image(img0,0,100,ancho,largo-100);
        		break;
        	case '*':	
        		image(imgast,0,100,ancho,largo-100);
        		break;
        	case '#':	
        		image(imgnum,0,100,ancho,largo-100);
        		break;
        	case '/':
        		image(imgbl,0,100,ancho,largo-100);
        		break;
        	}
        }
     
}

