///-----------------------------------------------------------------
///
/// @file      Star Chess 7.0.cpp
/// @author    Juan Camilo Arias, Iván Ballesteros, Sebastian Díaz.
/// Created:   14/11/2012
/// @section   Juego de fichas virtual y de movimientos automáticos desarrollado usando clases, una lista doblemente encadenada, apuntadores y funciones.
///
///
///------------------------------------------------------------------
#include <iostream>
#include <string>
#include <time.h>
#include <conio.h>
#include <windows.h>

using namespace std;

struct tablero  //Declaración estructura tablero.
{
    unsigned char simbolo;  //Este miembro almacena el simbolo que contiene una casilla del tablero (cuadro begro, blanco o rey).
    string nombre;          //Este miembro almacena el nombre del rey en las casillas por donde este halla dejado su rastro.
    int ras;                //Este miembro entero posee la información del rastro numérico de cada casilla en el tablero.
    unsigned char direccion;//Este miembro entero posee la información de la direccion del movimiento.
    tablero *siguiente;     //Este miembro es un apuntador al siguiente nodo.
    tablero *anterior;      //Este miembro es un apuntador al nodo anterior.
};

class INTERFAZ  //Declaracion de la clase INTERFAZ con funciones virtuales.
{
public:
    virtual void crearTablero(int, int) {}
    virtual void colorCasilla(int, int) {}
    virtual void crearTablero() {}
    virtual void posicion(char) {}
    virtual void movimiento(int, char, int, int, int) {}
    virtual int freeHor() {}
    virtual int freeVer() {}
    virtual tablero get_ppos() {}
};

class LISTA: public INTERFAZ  //Declaración de la clase LISTA que hereda publicamente de INTERFAZ.
{
    tablero *cab; //Se declara la cabeza de la lista.
    tablero *ppos; //Se declara un apuntador que tendra la dirección de la posición del rey.
public:
    LISTA(int i=0)  //FUNCIÓN CONSTRUCTORA SOBRECARGADA IMPLICITAMENTE.
    {
        cab = new struct tablero; //Se inicializa cab con la direccion del primer nodo.
        cab->anterior=NULL;
    } //El miembro anterior del primer nodo se deja nulo.
    void crearTablero(int, int);
    void colorCasilla(int, int);
    void crearTablero();
    void posicion(char);
    void movimiento(int, char, int, int, int);
    int freeHor();
    int freeVer();
    //ESTA FUNCIÓN PUEDE SER DEFINIDA DENTRO DE LA CLASE GRACIAS A LA HERENCIA DE LA FUNCION VIRTUAL EN INTERFAZ.
    tablero get_ppos()
    {
        return *ppos;   //Esta funcón permite a la función principal acceder al atributo privado ppos de la clase lista.
    }
    ~LISTA(); //FUNCIÓN DESTRUCTORA
};

//VARIABLES GLOBALES
char rey1='r', rey2='a';
int pos, der, izq, up=0, down=0,rasj=0;
char tecla;

//PROTOTIPOS DE FUNCIONES EXTERNAS
void teclaAbajo();
int dados();
void mensaje(int);

//FUNCIÓN PRINCIPAL
int main()
{
    INTERFAZ *p; //Apuntador a INTERFAZ.
    LISTA lista1(1); //Instanciación de la clase.
    p = &lista1; //Incializacion del apuntador, Hace referencia al objeto lista1 de la clase LISTA.
    ///CONDICIONES INICIALES
    int seconds, h, v, free;
    srand(time(NULL)); //Función de números aleatorio para la posición inicial de los reyes.
    int a= rand()%64;
    int b= rand()%64;
    while(a==b)  //Evitando que la posición de los dos reyes sea la misma.
    {
        a= rand()%64;
        b= rand()%64;
    }
    ///CICLO DE JUEGO
    p->crearTablero(a,b);
    do
    {
        system("cls");
        seconds = dados();
        mensaje(seconds);
        p->crearTablero();
        p->posicion(rey1);
        h = p->freeHor();
        v = p->freeVer();
        free = h+v;
        if(free==0)
        {
            system("cls");
            cout<<"_________________ STAR CHESS __________________"<<endl<<endl;
            cout<<"El rey rojo se ha escapado en "<<rasj<<" jugadas"<<endl;
            p->crearTablero();
            system("pause");
            return 0;
        }
        p->movimiento(pos, rey1, seconds, h, v);
        rasj+=1; //Contador del número de jugada.
        teclaAbajo();
        mensaje(seconds);
        p->crearTablero();
        p->posicion(rey2);
        h = p->freeHor();
        v = p->freeVer();
        free = h+v;
        if(free==0)
        {
            system("cls");
            cout<<"_________________ STAR CHESS __________________"<<endl<<endl;
            cout<<"El rey azul no pudo atrapar al rojo"<<endl;
            p->crearTablero();
            system("pause");
            return 0;
        }
        p->movimiento(pos, rey2, (seconds/2), h, v);
        rasj+=1; //Contador del número de jugada.
        teclaAbajo();
        p->crearTablero();
        p->posicion(rey1);
        if(p->get_ppos().simbolo!=rey1)
        {
            system("cls");
            cout<<"_________________ STAR CHESS __________________"<<endl<<endl;
            cout<<"El rey rojo ha sido capturado en "<<rasj<<" jugadas"<<endl;
            p->crearTablero();
            system("pause");
            return 0;
        }
    }
    while(free!=0);
    system("pause");
    return 0;
}

//FUNCIONES DE LA CLASE
void LISTA::crearTablero(int a, int b)  //Esta función crea el tablero y ubica a los reyes aleatoriamente en el mismo.
{
    tablero *p=cab, *nuevo=NULL;

    for(int x=0; x<64; x++)
    {
        //Inicializando los miembros.
        p-> simbolo = 32;
        p-> nombre  = "    ";
        p-> direccion = 32;
        p-> ras = 0;

        // Ubicando de los reyes.
        if(x==a)
        {
            p-> simbolo = rey1;
        }  //Ubicación rey rojo.
        if(x==b)
        {
            p-> simbolo = rey2;
        } //Ubicación rey azul.

        //Creando un nuevo nodo.
        nuevo = new struct tablero;
        if(nuevo != NULL)   //Comprobando memoria para crear un nuevo nodo.
        {
            //Enlazando el nodo anterior con el siguiente.
            p->siguiente = nuevo;
            nuevo->anterior = p;
            p=nuevo;
            p->siguiente=NULL; //El miembro siguiente del ultimo nodo creado se deja nulo.
        }
        else
        {
            cout<<"Memoria insuficiente... Abortando programa...";    //Si no hay memoria para crear un nuevo nodo se aborta el programa.
            x=64;
        }
    }
}

void LISTA::colorCasilla(int x, int y)
{
    HANDLE hConsole;
    hConsole= GetStdHandle(STD_OUTPUT_HANDLE);
    tablero *p=cab;

    for(int i=0; i<(x+y); i++)
    {
        p=p->siguiente;
    }
    if((y+(x/8))%2==0)  //Se establece el color de las casillas blancas.
    {
        if (p-> nombre=="    ")
        {
            SetConsoleTextAttribute( hConsole,255);
        }
        if (p-> nombre=="rojo"||p-> simbolo == rey1)
        {
            SetConsoleTextAttribute( hConsole,252);   //Letra roja.
        }
        if (p-> nombre=="azul"||p-> simbolo == rey2)
        {
            SetConsoleTextAttribute( hConsole,249);   //Letra azul.
        }
    }
    else  //Se establece el color de las casillas negras.
    {
        if (p-> nombre=="    ")
        {
            SetConsoleTextAttribute( hConsole,0);
        }
        if (p-> nombre=="rojo"||p-> simbolo == rey1)
        {
            SetConsoleTextAttribute( hConsole,12);   //Letra roja
        }
        if (p-> nombre=="azul"||p-> simbolo == rey2)
        {
            SetConsoleTextAttribute( hConsole,9);   //Letra azul
        }
    }
}

void LISTA::crearTablero()  //Esta función imprime el tablero actual de juego.
{
    HANDLE hConsole;
    hConsole= GetStdHandle(STD_OUTPUT_HANDLE);
    cout<<endl;
    tablero *p=cab;

    for(int x=0; x<64; x=x+8)
    {
        p=cab;
        for(int y=0; y<8; y++)
        {
            colorCasilla(x,y); //Primera línea (dirección).
            for(int i=0; i<(x+y); i++)
            {
                p=p->siguiente;
            }
            cout<<"   "<<p-> direccion<<"  ";
            p=cab;
        }
        cout<<endl;

        for(int y=0; y<8; y++)
        {
            colorCasilla(x,y); //Línea básica.
            for(int i=0; i<(x+y); i++)
            {
                p=p->siguiente;
            }
            if(p-> simbolo!='*' )  //De esta forma se imprime un cuadro negro, blanco o el simbolo de rey en la casilla.
            {
                cout<<"   "<<p-> simbolo<<"  ";
            }

            if(p-> simbolo=='*' )  //De esta forma se mantiene el número de rastro anterior que hay en la casilla.
            {
                //Estas condiciones imprimen correctamente el rastro numérico.
                if(p-> ras==0)
                {
                    p-> ras = rasj;
                }
                if(p-> ras<10)
                {
                    cout<<"   "<<p-> ras<<"  ";   //En caso de ser un rastro de una cifra.
                }
                if(p-> ras>=10)
                {
                    cout<<"  "<<p-> ras<<"  ";   //En caso de ser un rastro de dos cifras.
                }
            }
            p=cab;
        }
        cout<<endl;

        for(int y=0; y<8; y++)
        {
            colorCasilla(x,y); //Tercera línea (nombre).
            for(int i=0; i<(x+y); i++)
            {
                p=p->siguiente;
            }
            cout<<" "<<p-> nombre<<" ";
            p=cab;
        }
        cout<<endl;
        for(int y=0; y<8; y++)
        {
            colorCasilla(x,y); //Cuarta linea.
            cout<<"   "<<" "<<"  ";
        }
        cout<<endl;
    }
    SetConsoleTextAttribute( hConsole,7);
    cout<<endl;
}

void LISTA::posicion(char rey)  //Con esta función se determina la posición actual de cualquiera de los reyes.
{
    tablero *p=cab;
    for(int x=0; x<64; x++)
    {
        if (p-> simbolo == rey)
        {
            ppos=p;
            pos = x;
        }
        p=p->siguiente;
    }
}

//FUNCIÓN DE MOVIMIENTO
void LISTA::movimiento(int pos, char rey, int m, int h, int v)  //requiere de la posición de un rey determinado y la directriz del movimiento
{
    tablero *p;

    while(m!=0)
    {
        //MOVIMIENTO EN HORIZONTAL
        if(m%2==0)
        {
            p=cab;
            m=m+1;
            if(h!=0)  //Se verifica que existan espacios libres para moverse a la derecha o a la izquierda del rey.
            {
                m=0;
                int y=0, n=0, m=0;
                der=-1;
                while (y<8)
                {
                    izq=0;
                    for(int x=0; x<8; x++)
                    {

                        // Recorriendo vector de izquierda a derecha.
                        if((p-> simbolo==32 || p-> simbolo==rey1) && der<0)
                        {
                            izq=izq+1; //Esta condición verifica si existen espacios libres a la izquierda del rey y los va sumando.
                            if(ppos-> simbolo==rey2 && p-> simbolo==rey1)
                            {
                                izq=1;   //Esta condición hace que el conteo se reinicie si el rey azul encuentra al rojo.
                            }
                        }
                        if((p-> simbolo=='*' || (ppos-> simbolo==rey1 && p-> simbolo==rey2)) && der<0)  //Si en algun punto del recorrido un espacio no llega a estar vacio el conteo se reinicia.
                        {
                            izq=0;
                        }
                        if(n==pos && p-> simbolo==rey1)  //Esta condición garantiza que la posición donde se encuentre el primer rey no sea contada como una casilla libre.
                        {
                            izq=izq-1;
                        }

                        //Punto medio.
                        if(p-> simbolo==rey) // De esta forma se detiene el conteo de casillas libres a la izquierda del rey, y empiezan a sumarse las de la derecha del rey.
                        {
                            y=8;
                            der=0;
                        }

                        //Recorriendo el vector de derecha a izquierda
                        if((p-> simbolo==32 || p-> simbolo==rey1) && der>=0)
                        {
                            der=der+1; //Esta condición verifica si existen espacios libres a la derecha del rey y los va sumando.
                            if(ppos-> simbolo==rey2 && p-> simbolo==rey1)
                            {
                                x=8;    //Esta condición hace que el conteo termine si el rey azul encuentra al rojo.
                                y=8;
                            }
                        }
                        if((p-> simbolo=='*' || (ppos-> simbolo==rey1 && p-> simbolo==rey2)) && der>=0)  //Esta condicion hace que al encontrar un obstaculo a la derecha del rey el ciclo de conteo termine.
                        {
                            x=8;
                            y=8;
                        }
                        if(n==pos && p-> simbolo==rey1)  //Esta condición garantiza que la posición donde se encuentre el primer rey no sea contada como una casilla libre.
                        {
                            der=der-1;
                        }//Esta condición garantiza que la posición donde se encuentre el primer rey no sea contada como una casilla libre.

                        n=n+1;
                        p=p->siguiente;
                    }
                    y=y+1;
                }

                //Eligiendo posición.
                p=ppos;
                if(der>izq)  //Se determina el camino más largo hacia los lados.
                {
                    for(int x=pos; x<(pos+der); x++)
                    {
                        p-> simbolo = '*'; //Se crea el rastro del movimiento anterior.
                        p-> direccion = 26; //se inicializa la dirección del movimiento.
                        if(rey=='r')
                        {
                            p-> nombre="rojo";   //Se inicializa el miembro nombre de acuerdo al rey que hace el movimimiento.
                        }
                        else
                        {
                            p-> nombre="azul";
                        }
                        p=p->siguiente;
                    }
                    p-> simbolo=rey; //Si el camino mas largo esta a la derecha el rey pasa a ubicarse hacia ese lado.
                }
                p=ppos;
                if(der<izq)
                {
                    for(int x=pos; x>(pos-izq); x--)
                    {
                        p-> simbolo= '*'; //Se crea el rastro del movimiento anterior.
                        p-> direccion = 27; //se inicializa la dirección del movimiento.
                        if(rey=='r')
                        {
                            p-> nombre="rojo";   //Se inicializa el miembro nombre de acuerdo al rey que hace el movimimiento.
                        }
                        else
                        {
                            p-> nombre="azul";
                        }
                        p=p->anterior;
                    }
                    p-> simbolo=rey; //Si el camino mas largo esta a la derecha el rey pasa a ubicarse hacia ese lado.
                }
                if(der==izq)  //Si el camino es igual a ambos lados se entra a este ciclo.
                {
                    p=ppos;
                    if(der%2==0)  //Puede moverse a la derecha.
                    {
                        for(int x=pos; x<(pos+der); x++)
                        {
                            p-> simbolo = '*'; //Se crea el rastro del movimiento anterior.
                            p-> direccion = 27; //se inicializa la dirección del movimiento.
                            if(rey=='r')
                            {
                                p-> nombre="rojo";   //Se inicializa el miembro nombre de acuerdo al rey que hace el movimimiento.
                            }
                            else
                            {
                                p-> nombre="azul";
                            }
                            p=p->siguiente;
                        }
                        p->simbolo=rey;
                    } //Si el camino mas largo esta a la derecha el rey pasa a ubicarse hacia ese lado.
                    p=ppos;
                    if(der%2!=0)  //Puede moverse a la izquierda
                    {
                        for(int x=pos; x>(pos-izq); x--)
                        {
                            p-> simbolo = '*'; //Se crea el rastro del movimiento anterior.
                            p-> direccion = 26; //se inicializa la dirección del movimiento.
                            if(rey=='r')
                            {
                                p-> nombre="rojo";   //Se inicializa el miembro nombre de acuerdo al rey que hace el movimimiento.
                            }
                            else
                            {
                                p-> nombre="azul";
                            }
                            p=p->anterior;
                        }
                        p-> simbolo=rey;
                    } //Si el camino mas largo esta a la derecha el rey pasa a ubicarse hacia ese lado.
                }
            }
        }//Se cierra el ciclo de movimiento horizontal.

        //MOVIMIENTO EN VERTICAL
        if(m%2!=0)
        {
            p=ppos;
            m=m-1;
            if(v!=0)  //Se verifica que existan espacios libres para moverse abajo o arriba del rey.
            {
                m=0;
                up=0;
                down=0;
                int n;
                int i=0;


                n=pos+8; //Una posición abajo del rey.
                while(p->siguiente != NULL && i<8)
                {
                    p=p->siguiente;
                    i+=1;
                }
                while (i==8 && n<64 && (p-> simbolo==32 || p-> simbolo== rey1))
                {
                    down=down+1; //Contador de las casillas libres debajo del rey.
                    if(p-> simbolo==rey1)
                    {
                        n=64;   //Esta condición hace que el conteo termine si el rey azul encuentra al rojo.
                    }
                    n=n+8;
                    for(int i=0; i<8; i++)
                    {
                        if(p->siguiente != NULL)
                        {
                            p=p->siguiente;
                        }
                    }
                }
                p=ppos;
                i=0;
                n=pos-8; //Una posición arriba del rey.
                while(p->anterior!= NULL && i<8)
                {
                    p=p->anterior;
                    i+=1;
                }
                while (i==8 && n>=0 && (p-> simbolo==32 || p-> simbolo== rey1))
                {
                    up=up+1; //Contador de las casillas libres encima del rey.
                    if(p-> simbolo==rey1)
                    {
                        n=-1;   //Esta condición hace que el conteo termine si el rey azul encuentra al rojo.
                    }
                    n=n-8;
                    for(int i=0; i<8; i++)
                    {
                        if(p->anterior != NULL)
                        {
                            p=p->anterior;
                        }
                    }
                }


                //Eligiendo posición.
                if(up>down)  //Si el camino mas largo esta arriba del rey.
                {
                    n=pos;
                    p=ppos;
                    for(int x=0; x<up; x++)
                    {
                        p-> simbolo= '*'; //Se crea el rastro del movimiento anterior.
                        p-> direccion = 24; //se inicializa la dirección del movimiento.
                        if(rey=='r')
                        {
                            p-> nombre="rojo";   //Se inicializa el miembro nombre de acuerdo al rey que hace el movimimiento.
                        }
                        else
                        {
                            p-> nombre="azul";
                        }
                        n=n-8;
                        for(int i=0; i<8; i++)
                        {
                            p=p->anterior;
                        }
                    }
                    p-> simbolo=rey;
                } //Si el camino mas largo esta arriba el rey pasa a ubicarse hacia arriba.

                if(up<down)  //Si el camino mas largo esta abajo del rey.
                {
                    n=pos;
                    p=ppos;
                    for(int x=0; x<down; x++)
                    {
                        p-> simbolo= '*'; //Se crea el rastro del movimiento anterior.
                        p-> direccion = 25; //se inicializa la dirección del movimiento.
                        if(rey=='r')
                        {
                            p-> nombre="rojo";   //Se inicializa el miembro nombre de acuerdo al rey que hace el movimimiento.
                        }
                        else
                        {
                            p-> nombre="azul";
                        }
                        n=n+8;
                        for(int i=0; i<8; i++)
                        {
                            p=p->siguiente;
                        }
                    }
                    p-> simbolo=rey;
                } //Si el camino mas largo esta abajo el rey pasa a ubicarse abajo.

                if(up==down)  //Si ambos caminos son iguales.
                {
                    if(up%2==0)  //Puede moverse hacia arriba.
                    {
                        n=pos;
                        p=ppos;
                        for(int x=0; x<up; x++)
                        {
                            p-> simbolo='*'; //Se crea el rastro del movimiento anterior.
                            p-> direccion = 25; //se inicializa la dirección del movimiento.
                            if(rey=='r')
                            {
                                p-> nombre="rojo";   //Se inicializa el miembro nombre de acuerdo al rey que hace el movimimiento.
                            }
                            else
                            {
                                p-> nombre="azul";
                            }
                            n=n-8;
                            for(int i=0; i<8; i++)
                            {
                                p=p->anterior;
                            }
                        }
                        p-> simbolo=rey;
                    }

                    if(up%2!=0)  //Puede moverse hacia abajo.
                    {
                        n=pos;
                        p=ppos;
                        for(int x=0; x<down; x++)
                        {
                            p-> simbolo= '*'; //Se crea el rastro del movimiento anterior.
                            p-> direccion = 24; //se inicializa la dirección del movimiento.
                            if(rey=='r')
                            {
                                p-> nombre="rojo";   //Se inicializa el miembro nombre de acuerdo al rey que hace el movimimiento.
                            }
                            else
                            {
                                p-> nombre="azul";
                            }
                            n=n+8;
                            for(int i=0; i<8; i++)
                            {
                                p=p->siguiente;
                            }
                        }
                        p-> simbolo=rey;
                    }
                }
            }
        } //Se cierra el ciclo del movimiento vertical.
    }
} //Se cierra la función movimiento.


int LISTA::freeHor()  //Esta función determina si hay espacios a los lados donde un rey pueda moverse.
{
    int h=0; //Cero espacios libres.
    tablero *p;

    p=ppos->siguiente;
    if(p!= NULL)
    {
        if ((p-> simbolo==32 || p-> simbolo==rey1) && ((pos+1)<64))
        {
            h=h+1;
            if((pos+1)%8==0)  //Excepción para las esquinas derechas.
            {
                h=h-1;
            }
        } //Espacio libre a la derecha.
    }

    p=ppos->anterior;
    if(p!= NULL)
    {
        if ((p-> simbolo==32 || p-> simbolo==rey1) && ((pos-1)>-1))
        {
            h=h+1;
            if(pos%8==0)  //Excepción para las esquinas izquierdas.
            {
                h=h-1;
            }
        } //Espacio libre a la izquierda.
    }
    return h;
}

int LISTA::freeVer()  //Esta función determina si hay espacios arriba o abajo donde un rey pueda moverse.
{
    int v=0; //Cero espacios libres.
    int i=0;
    tablero *p;

    p=ppos;
    while(p->siguiente != NULL && i<8)
    {
        p=p->siguiente;
        i+=1;
    }
    if (i==8 && (p-> simbolo==32 || p-> simbolo==rey1) && ((pos+8)<64))
    {
        v=v+1;
    } //Espacio libre abajo.

    p=ppos;
    i=0;
    while(p->anterior!= NULL && i<8)
    {
        p=p->anterior;
        i+=1;
    }
    if (i==8 && (p-> simbolo==32 || p-> simbolo==rey1) && ((pos-8)>-1))
    {
        v=v+1;
    } //Espacio libre arriba.

    return v;
}

LISTA::~LISTA()
{
    tablero *p=cab;
    while(p->siguiente != NULL)
    {
        cab=p->siguiente;
        p->siguiente=NULL; //Se borra la cadena delantera.
        p->anterior=NULL;  //Se borra la cadena trasera.
        p=NULL;            //Se borrar el apuntador al byte mas bajo de la estructura.
        p=cab;
    }
    p=NULL; //Se borra el ultimo nodo.
    cab=NULL;
    ppos=NULL; //Se borra el apuntador de posicion.
    cout<<"Lista destruida";
}

//FUNCIONES EXTERNAS
void teclaAbajo()  //Esta función garantiza que el ciclo de juego continúe unicamente al presionar tecla abajo.
{
    do
    {
        tecla = getch();
    }
    while (tecla!=80);
    system("cls");
}

int dados() //Esta función lanza los dados para determina el movimiento a realizar.
{
    time_t seconds; //Función de número aleatorio para los movimientos.
    seconds = time (NULL);
    return seconds;
}

void mensaje(int seconds)  //Esta función muestra el mensaje con el movimiento de cada rey en cada jugada.
{
    //MENSAJE INICIAL CON LA DIRECCIÓN DEL MOVIMIENTO DE CADA REY
    cout<<"_________________ STAR CHESS __________________"<<endl<<endl;
    if(seconds%2==0)  //Si el número aleatorio es par la dirección de movimiento es horizontal.
    {
        cout<<"Rey rojo movera horizontal si puede"<<"\n";
    }
    else
    {
        cout<<"Rey rojo movera vertical si puede"<<"\n";   //Si el número aleatorio es impar la dirección de movimiento es vertical.
    }

    if((seconds/2)%2==0)  //Si el número aleatorio es par la dirección de movimiento es horizontal.
    {
        cout<<"Rey azul movera horizontal si puede"<<"\n";
    }
    else
    {
        cout<<"Rey azul movera vertical si puede"<<"\n";   //Si el número aleatorio es impar la dirección de movimiento es vertical.
    }
}

