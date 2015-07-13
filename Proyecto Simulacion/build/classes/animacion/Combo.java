
package animacion;

import java.awt.Color;
import java.awt.Graphics;

public class Combo {
    private int x,y,l,velY,limit;

    public Combo(int x, int y, int l, int velY, int limit) {
        this.x = x;
        this.y = y;
        this.l = l;
        this.velY = velY;
        this.limit = limit;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    //Metodo para dibujar el circulo
    public void draw (Graphics g){
        g.fillRect(x, y, l, 2*l);
    }
    
    public void move(){
        setY(getY() + getVelY()); //Se realiza el movimiento
        checkLimit(limit); //Se verifica el limite
    }
    
    private void checkLimit(int limit){
        //Limite sobrepasado
        if(getY() > limit){
            setVelY(0); //Se detiene el movimiento.
            setY(limit); //Se ubica el circulo en el limite.
        }
            
    }
    
}
