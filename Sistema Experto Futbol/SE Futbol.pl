es_delantero(cristiano_ronaldo).
es_delantero(javier_hernandez).
es_delantero(karim_benzema).
es_centro_campista(james_rodriguez).
es_centro_campista(gareth_bale).
es_centro_campista(tony_kroos).
es_centro_campista(luka_modric).
es_defensa(sergio_ramos).
es_defensa(marcelo_vieira).
es_defensa(pepe_lima).
es_arquero(iker_casillas).

precio(cristiano_ronaldo,120).
precio(james_rodriguez,60).
precio(gareth_bale,80).
precio(karim_benzema,45).
precio(tony_kroos,45).
precio(luka_modric,55).
precio(sergio_ramos,45).
precio(marcelo_vieira,25).
precio(javier_hernandez,14).
precio(pepe_lima,15).
precio(iker_casillas,8).

edad(cristiano_ronaldo, 29).
edad(karim_benzema, 26).
edad(javier_hernandez, 25).
edad(james_rodriguez, 23).
edad(gareth_bale, 24).
edad(tony_kroos, 24).
edad(luka_modric, 28).
edad(sergio_ramos, 27).
edad(marcelo_vieira, 25).
edad(pepe_lima, 30).
edad(iker_casillas, 32).

partidos_seleccion(cristiano_ronaldo, 118).
partidos_seleccion(karim_benzema, 75).
partidos_seleccion(javier_hernandez, 68).
partidos_seleccion(james_rodriguez, 32).
partidos_seleccion(gareth_bale, 45).
partidos_seleccion(tony_kroos, 57).
partidos_seleccion(luka_modric, 78).
partidos_seleccion(sergio_ramos, 60).
partidos_seleccion(marcelo_vieira, 28).
partidos_seleccion(pepe_lima,  60).
partidos_seleccion(iker_casillas, 160).


estatura(cristiano_ronaldo, 186).
estatura(karim_benzema, 182).
estatura(javier_hernandez, 172).
estatura(james_rodriguez, 180).
estatura(gareth_bale, 183).
estatura(tony_kroos, 182).
estatura(luka_modric, 175).
estatura(sergio_ramos, 183).
estatura(marcelo_vieira, 174).
estatura(pepe_lima, 186).
estatura(iker_casillas, 185).

partidos_jugados(cristiano_ronaldo, 30).
partidos_jugados(karim_benzema, 35).
partidos_jugados(javier_hernandez, 24).
partidos_jugados(james_rodriguez, 34).
partidos_jugados(gareth_bale, 37).
partidos_jugados(tony_kroos, 2).
partidos_jugados(luka_modric, 34).
partidos_jugados(sergio_ramos, 32).
partidos_jugados(marcelo_vieira, 28).
partidos_jugados(pepe_lima, 30).
partidos_jugados(iker_casillas, 2).

goles(cristiano_ronaldo, 31).
goles(karim_benzema, 17).
goles(javier_hernandez, 4).
goles(james_rodriguez, 9).
goles(gareth_bale, 15).
goles(tony_kroos, 2).
goles(luka_modric, 1).
goles(sergio_ramos, 4).
goles(marcelo_vieira, 1).
goles(pepe_lima, 4).
goles(iker_casillas, 0).

asistencias(cristiano_ronaldo, 10).
asistencias(karim_benzema, 11).
asistencias(javier_hernandez, 3).
asistencias(james_rodriguez, 12).
asistencias(gareth_bale, 7).
asistencias(tony_kroos, 4).
asistencias(luka_modric, 5).
asistencias(sergio_ramos, 1).
asistencias(marcelo_vieira, 5).
asistencias(pepe_lima, 2).
asistencias(iker_casillas, 0).

pases(cristiano_ronaldo, 55).
pases(karim_benzema, 19).
pases(javier_hernandez, 20).
pases(james_rodriguez, 81).
pases(gareth_bale, 82).
pases(tony_kroos, 55).
pases(luka_modric, 79).
pases(sergio_ramos, 3).
pases(marcelo_vieira, 115).
pases(pepe_lima, 10).
pases(iker_casillas, 0).

tiros_al_arco(james_rodriguez,25).

tarjetas_amarillas(cristiano_ronaldo, 4).
tarjetas_amarillas(karim_benzema, 2).
tarjetas_amarillas(javier_hernandez, 2).
tarjetas_amarillas(james_rodriguez, 0).
tarjetas_amarillas(gareth_bale, 3).
tarjetas_amarillas(tony_kroos, 6).
tarjetas_amarillas(luka_modric, 6).
tarjetas_amarillas(sergio_ramos, 12).
tarjetas_amarillas(marcelo_vieira, 3).
tarjetas_amarillas(pepe_lima, 9).
tarjetas_amarillas(iker_casillas, 0).

tarjetas_rojas(cristiano_ronaldo, 0).
tarjetas_rojas(karim_benzema, 1).
tarjetas_rojas(javier_hernandez, 4).
tarjetas_rojas(james_rodriguez, 9).
tarjetas_rojas(gareth_bale, 15).
tarjetas_rojas(tony_kroos, 2).
tarjetas_rojas(luka_modric, 1).
tarjetas_rojas(sergio_ramos, 4).
tarjetas_rojas(marcelo_vieira, 1).
tarjetas_rojas(pepe_lima, 4).
tarjetas_rojas(iker_casillas, 0).

tiempo_sin_lesiones_graves(cristiano_ronaldo, 1200).
tiempo_sin_lesiones_graves(karim_benzema, 1200).
tiempo_sin_lesiones_graves(javier_hernandez, 1200).
tiempo_sin_lesiones_graves(james_rodriguez, 1200).
tiempo_sin_lesiones_graves(gareth_bale, 1200).
tiempo_sin_lesiones_graves(tony_kroos, 1200).
tiempo_sin_lesiones_graves(luka_modric, 0).
tiempo_sin_lesiones_graves(sergio_ramos, 1200).
tiempo_sin_lesiones_graves(marcelo_vieira, 1200).
tiempo_sin_lesiones_graves(pepe_lima, 60).
tiempo_sin_lesiones_graves(iker_casillas, 24).

atajadas(iker_casillas, 4).


esComprable(Jugador,Presupuesto):-
	precio(Jugador,X),
	X < Presupuesto.

estaturaMinima(Jugador,EstaturaMin):-
	estatura(Jugador,X),
	(   X >= EstaturaMin -> true;
	X < EstaturaMin -> fail).

edadMaxima(Jugador,EdadMax):-
	edad(Jugador,X),
	X =< EdadMax.

golesMinimos(Delantero,GolesMin):-
	goles(Delantero,G),
	G >= GolesMin.

asistenciasMinimas(Jugador,AsisMin):-
	asistencias(Jugador,A),
	A >= AsisMin.

partidosMinimos(Jugador,PartMin):-
	partidos_seleccion(Jugador,P),
	P >= PartMin.

tiempoMinimoSinLesiones(Jugador):-
	tiempo_sin_lesiones_graves(Jugador,T),
	T >= 6.




% Regla que determina si un jugador juega limpio dependiendo de su
% posición y las tarjetas recibidas en los partidos que ha jugado.
% Para cualquier posición: no puede tener alguna tarjeta roja.
% Para un delantero: en promedio 1 tarjeta amarilla cada 4 partidos
% Para un defensa: 1 tarjeta amarilla en 2 partidos
% Para un centro_campista: 1 tarjeta amarilla en 3 partidos
% Para un arquero: 1 tarjeta amarilla en 10 partidos
%
juegaLimpio(Jugador):-
	tarjetas_rojas(Jugador,TR),
	tarjetas_amarillas(Jugador,TA),
	partidos_jugados(Jugador,P),
	(   es_delantero(Jugador), TR == 0,TA/P =< 0.25);
	(   es_defensa(Jugador), TR == 0, TA/P =< 0.5);
	(   es_centro_campista(Jugador), TR == 0, TA/P =< 0.34);
	(   es_arquero(Jugador), TR == 0, TA/P =< 0.1).


comprarDelantero(Presupuesto,X):-
	es_delantero(X),
	esComprable(X, Presupuesto).

comprarDefensa(Presupuesto,X):-
	es_defensa(X),
	esComprable(X, Presupuesto).

comprarCentroCampista(Presupuesto,X):-
	es_centro_campista(X),
	esComprable(X, Presupuesto).

comprarArquero(Presupuesto,X):-
	es_arquero(X),
	esComprable(X, Presupuesto).

comprarJugador(Posicion, Presupuesto, X):-
	Posicion == delantero -> comprarDelantero(Presupuesto, X);
	Posicion == defensa -> comprarDefensa(Presupuesto, X);
	Posicion == centro_campista -> comprarCentroCampista(Presupuesto, X);
	Posicion == arquero -> comprarArquero(Presupuesto, X).

precision(Delantero,Precision):-
	goles(Delantero,GOLES),
	tiros_al_arco(Delantero,TIROS),
	Precision is (GOLES/TIROS).



go:-  write('Escriba la posicion del jugador'),
      nl,
      read(Posicion),
      write('Escriba su presupuesto'),
      nl,
      read(Presupuesto),
      write('Escriba la edad maxima del jugador'),
      nl,
      read(Edad),
      write('Escriba la estatura minima del jugador'),
      nl,
      read(Estatura),
      (Posicion == delantero ->
      write('Escriba el minimo numero de goles por temporada'),
      nl,
      read(Goles);
      Posicion \= delantero -> Goles = -1
      ),
      ((Posicion == delantero; Posicion == centro_campista) ->
      write('Escriba el minimo numero de asistencias por temporada'),
      nl,
      read(Asistencias);
      (Posicion \= delantero, Posicion \= centro_campista) -> Asistencias = -1
      ),
      write('Escriba el minimo de partidos que debe tener el jugador con la seleccion de su país'),
      nl,
      read(Partidos_seleccion),
      write('Le interesa que juegue limpio?'),
      nl,
      read(JuegoLimpio),
      (JuegoLimpio== no -> verifica_posicion(Posicion,Presupuesto, Edad, Estatura, Goles, Asistencias, Jugadores,Partidos_seleccion);
      JuegoLimpio== si -> verifica_posicion_con_juego_limpio(Posicion,Presupuesto, Edad, Estatura, Goles, Asistencias, Jugadores)),
      nl,
      length(Jugadores,X),
      (X == 1 -> write('El jugador recomendado y su precio es:');
      X > 1 -> write('Los jugadores recomendados con sus precios son:');
      X == 0 -> write('No existe ningun jugador recomendado')),
      write(Jugadores).


verifica_posicion(Posicion,Presupuesto, Edad, Estatura, Goles,  Asistencias, Jugadores,Partidos_seleccion) :-
	findall((X,Y),(precio(X,Y),comprarJugador(Posicion,Presupuesto,X), edadMaxima(X,Edad),estaturaMinima(X,Estatura), golesMinimos(X,Goles), asistenciasMinimas(X,Asistencias),partidosMinimos(X,Partidos_seleccion),tiempoMinimoSinLesiones(X)),Jugadores).



verifica_posicion_con_juego_limpio(Posicion,Presupuesto, Edad, Estatura, Goles, Asistencias, Jugadores,Partidos_seleccion)  :-
	findall((X,Y),(precio(X,Y),comprarJugador(Posicion,Presupuesto,X), edadMaxima(X,Edad), estaturaMinima(X,Estatura), golesMinimos(X, Goles), asistenciasMinimas(X,Asistencias),juegaLimpio(X),partidosMinimos(X,Partidos_seleccion),tiempoMinimoSinLesiones(X)),Jugadores).


























