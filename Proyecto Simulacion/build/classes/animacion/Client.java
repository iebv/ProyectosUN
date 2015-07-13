

package animacion;

import java.awt.Graphics;


public class Client {
    
    private int x,y,r,vel,limit,moveDir;
    private Combo combo;

    
            
    public Client(int x, int y, int r, int vel, int limit){
        this.x = x;
        this.y = y;
        this.r = r;
        this.vel = vel;
        this.limit = limit;
    }

    public int getMoveDir() {
        return moveDir;
    }

    public void setMoveDir(int moveDir) {
        this.moveDir = moveDir;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getX() {
        return x;
    }

    public int getVel() {
        return vel;
    }

    public void setVel(int vel) {
        this.vel = vel;
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

    public int getR() {
        return r;
    }

    public void setR(int l) {
        this.r = l;
    }
    
    public int getMoveDirection() {
        return moveDir;
    }

    public void setMoveDirection(int moveDir) {
        this.moveDir = moveDir;
    }
    //Metodo para dibujar el circulo
    public void draw (Graphics g){
        g.fillOval(x, y, r, r);
    }
    
    public void move(){
        setX(getX() + getVel()); //Se realiza el movimiento
        checkLimit(limit); //Se verifica el limite
    }
    
    private void checkLimit(int limit){
        //Limite sobrepasado
        if(getX() > limit){
            setVel(0); //Se detiene el movimiento.
            setX(limit); //Se ubica el circulo en el limite.
        }      
    }
     
    public void moveY(){
        setY(getY() + vel);
        checkLimitY(limit);
    } 
    
    private void checkLimitY(int limit){
        //Limite sobrepasado en la salida del cliente
        if(getY() > limit){
            vel = 0; //Se detiene el movimiento.
        }     
    }
    
}
