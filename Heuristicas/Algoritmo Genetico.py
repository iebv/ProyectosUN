from collections import OrderedDict
from math import exp, log
from collections import Counter as count
import random as r
import copy


auditorios = [] #Arreglo con todas las posiciones posibles de auditorios
salones = [] #Arreglo con todas las posiciones posibles de salones
pcs = [] #Arreglo con todas las posiciones posibles de salones de computadores
size_poblacion = 20
total_cursos = 90
horas_semanales = 4
mejor_individuo = None


class curso:

    def __init__(self,num,est,tip):
        self.numero = num
        self.estudiantes = est
        self.tipo = tip # 1 si es curso con pc's, 0 en caso contraio

    def __repr__(self):
        return "Curso:" + str(self.numero) + "-" + str(self.estudiantes) + "-" + str(self.tipo)



def salon(): #Retorna un diccionario con todos los salones
    salon = OrderedDict()
    salon["A1"] = None
    salon["S1"] = None
    salon["PC1"] = None
    salon["A2"] = None
    salon["S2"] = None
    salon["PC2"] = None
    return salon


#Franjas horarias
def horas(): #Retorna un diccionario con todas las horas posibles y los salones por hora
    horas = OrderedDict()
    horas["7-9"] = salon()
    horas["9-11"] = salon()
    horas["11-1"] = salon()
    horas["2-4"] = salon()
    horas["4-6"] = salon()
    horas["6-8"] = salon()
    return horas

#Dias de la semana
def crear_horario(): #Retorna un horario con todos los dias de la semana, las horas y los salones
    horario = OrderedDict()
    horario["Lun"] = horas()
    horario["Mar"] = horas()
    horario["Mie"] = horas()
    horario["Jue"] = horas()
    horario["Vie"] = horas()
    return horario

#Mapas para acceder a las posiciones de los diccionarios
map_dias = {0:"Lun", 1:"Mar", 2:"Mie", 3:"Jue", 4:"Vie"}
map_horas = {0:"7-9", 1:"9-11", 2:"11-1", 3:"2-4", 4:"4-6", 5:"6-8"}
map_salon = {0:"A1", 1:"S1", 2:"PC1", 3:"A2", 4:"S2", 5:"PC2"}

def imp_horario(horario):
    file = open("horario_geneticos.txt", "w")
    file.write("Cursos salones normales: 1-30\n")
    file.write("Cursos salas pcs: 30-60\n")
    file.write("Cursos de auditorio: 60-90\n\n")
    for salon in map_salon.values():
        file.write( "                 ASIGNACION SEMANAL " + salon + "\n" )
        for dia in map_dias.values():
            file.write(dia + "\n")
            for hora in map_horas.values():
                file.write( "         " + hora + "  Curso " + str(horario[dia][hora][salon].numero) + "\n" )
        file.write("\n")
    file.close()

#Se llenan los arreglos con todas las posible posiciones para los salones del horario
for i in range(5):
        for j in range(6):
            auditorios.append([i,j,0])
            salones.append([i,j,1])
            pcs.append([i,j,2])
            auditorios.append([i,j,3])
            salones.append([i,j,4])
            pcs.append([i,j,5])


#Se asigna una primera configuracion de horario
def crear_individuo():
    horario = crear_horario()
    audi = auditorios[:]
    sal = salones[:]
    pc = pcs[:]
    #Cursos normales asignados aleatoriamente entre las posiciones que contiene el arreglo de salones
    for i in range(30):
        r.seed()
        pos = sal.pop(sal.index(r.choice(sal))) #primeras dos horas
        horario[map_dias[pos[0]]][map_horas[pos[1]]][map_salon[pos[2]]] = curso(i+1, int(r.gauss(35,5)), 0)
        r.seed()
        pos = sal.pop(sal.index(r.choice(sal))) #siguientes dos horas
        horario[map_dias[pos[0]]][map_horas[pos[1]]][map_salon[pos[2]]] = curso(i+1, int(r.gauss(35,5)), 0)

    #Cursos de computo asignados aleatoriamente entre las posiciones que contiene el arreglo pcs
    for i in range(30):
        r.seed()
        pos = pc.pop(pc.index(r.choice(pc)))
        horario[map_dias[pos[0]]][map_horas[pos[1]]][map_salon[pos[2]]] = curso(i+31, int(r.gauss(30,5)), 1)
        r.seed()
        pos = pc.pop(pc.index(r.choice(pc)))
        horario[map_dias[pos[0]]][map_horas[pos[1]]][map_salon[pos[2]]] = curso(i+31, int(r.gauss(30,5)), 1)

    #Cursos de auditorio asignados aleatoriamente
    for i in range(30):
        r.seed()
        pos = audi.pop(audi.index(r.choice(audi)))
        horario[map_dias[pos[0]]][map_horas[pos[1]]][map_salon[pos[2]]] = curso(i+61, int(r.gauss(80,15)), 0)
        r.seed()
        pos = audi.pop(audi.index(r.choice(audi)))
        horario[map_dias[pos[0]]][map_horas[pos[1]]][map_salon[pos[2]]] = curso(i+61, int(r.gauss(80,15)), 0)
    return horario

#Funcion para generar una poblacion inicial
def poblacion_inicial():
    poblacion = []
    for i in range(0, size_poblacion):
        poblacion.append(crear_individuo())

    return poblacion

#Seleccion por torneo
def seleccion(poblacion):
    seleccionados = [] #Lista de los individuos seleccionados
    mejores = {} #Este diccionario servira para poder encontrar el mejor de los individuos
    global mejor_individuo #Variable global que contiene el mejor individuo de una generacion
    for i in range (0,size_poblacion/2):
        fighter1 = poblacion.pop(poblacion.index(r.choice(poblacion))) #Se elige al azar el primer competidor
        fighter2 = poblacion.pop(poblacion.index(r.choice(poblacion))) #Se elige al azar el segundo competidor
        aptitud_f1 = funcion_aptitud(fighter1) #Se calculan las funciones de aptitud de cada contrincante
        aptitud_f2 = funcion_aptitud(fighter2)
        if(aptitud_f1 < aptitud_f2): #Se selecciona el ganador del torneo
            seleccionados.append(fighter1)
            mejores[aptitud_f1] = fighter1
        else:
            seleccionados.append(fighter2)
            mejores[aptitud_f2] = fighter2
    mejor_individuo = mejores[min(mejores.keys())] #Se guarda el mejor de todos los seleccionados
    return seleccionados

#Funcion de cruce en un punto
#El cromosoma esta reprentado por la secuencia de  dias Lunes- Viernes del horario
def cruce(padre1, padre2):
    r.seed()
    punto_corte = int(r.uniform(0,5)) #Se elige el punto de corte al azar (dia de la semana)
    hijo1 = crear_horario()
    hijo2 = crear_horario()
    for i in range(-1,punto_corte):
        hijo1[map_dias[i+1]] = padre1[map_dias[i+1]]
        hijo2[map_dias[i+1]] = padre2[map_dias[i+1]]
    for i in range(punto_corte + 1, len(map_dias)):
        hijo1[map_dias[i]] = padre2[map_dias[i]]
        hijo2[map_dias[i]] = padre1[map_dias[i]]
    return hijo1, hijo2

#Esta funcion genera una nueva poblacion
def nueva_generacion(seleccionados):
    nueva_gen = []
    prob_mutacion = 0.1 #Probabilidad de mutacion
    nueva_gen.append(mejor_individuo) #El mejor individuo de la generacion presente pasa a la generacion futura automaticamente
    while len(seleccionados) > 0:
        if len(seleccionados) == 2: #La ultima pareja de padres tiene 3 descendientes
            padre1 = seleccionados.pop(seleccionados.index(r.choice(seleccionados)))
            padre2 = seleccionados.pop(seleccionados.index(r.choice(seleccionados)))
            descendencia = cruce(padre1 , padre2)
            nueva_gen.append(descendencia[0])
            nueva_gen.append(descendencia[1])
            descendencia = cruce(padre2 , padre1)
            nueva_gen.append(descendencia[int(r.uniform(0,2))])
        else: #Las demas parejas tienen cuatro descendientes
            padre1 = seleccionados.pop(seleccionados.index(r.choice(seleccionados)))
            padre2 = seleccionados.pop(seleccionados.index(r.choice(seleccionados)))
            descendencia = cruce(padre1 , padre2)
            nueva_gen.append(descendencia[0])
            nueva_gen.append(descendencia[1])
            descendencia = cruce(padre2, padre1)
            nueva_gen.append(descendencia[0])
            nueva_gen.append(descendencia[1])

    #Mutacion
    for i in range (0,len(nueva_gen)-1):
        r.seed()
        if r.uniform(0,1) < prob_mutacion:
            indv_mutado = mutacion(nueva_gen[i]) #Se muta un individuo con la probabilidad dada
            nueva_gen[i] = indv_mutado

    return nueva_gen


#Muta un individuo, modificando el horario de algunos cursos en forma aleatoria
def mutacion(horario):
    horario_mod = copy.deepcopy(horario)
    for i in range(3):
        if(i==0) : posi = salones[:] #Se cambia aleatoriamente un curso asignado a un salon normal
        if(i==1) : posi = pcs[:] #Se cambia aleatoriamente un curso asignado a un salon de pcs
        if(i==2) : posi = auditorios[:] #Se cambia aleatoriamente un curso asignado a un auditorio
        p1 = posi.pop(posi.index(r.choice(posi)))
        p2 = posi.pop(posi.index(r.choice(posi)))
        aux = horario_mod[map_dias[p1[0]]][map_horas[p1[1]]][map_salon[p1[2]]]
        horario_mod[map_dias[p1[0]]][map_horas[p1[1]]][map_salon[p1[2]]] = horario_mod[map_dias[p2[0]]][map_horas[p2[1]]][map_salon[p2[2]]]
        horario_mod[map_dias[p2[0]]][map_horas[p2[1]]][map_salon[p2[2]]] = aux
    return horario_mod

#Calcula un puntaje para el horario de acuerdo a los cruces de materias que puedan presentarse
#Ademas verifica los que cada materia tenga exactamente dos bloques de dos horas
def funcion_aptitud(horario):
    puntaje = revisar_cruces(horario) + cantidad_cursos(horario)
    return puntaje

#Calcula los cruces de un horario
def revisar_cruces(horario):
    puntaje = 0
    cursos_semana = []
    for dia in horario:
        cursos_dia = []
        for hora in horario[dia]:
            cursos_dia_hora_salon = horario[dia][hora].values()
            cursos_dia_hora = []
            for curso in cursos_dia_hora_salon:
                cursos_dia_hora.append(curso.numero)
            repetidos_dia_hora = len(cursos_dia_hora) - len(set(cursos_dia_hora)) #Se revisan cuantos cursos pueden cruzarce a una misma hora en un dia determinado
            if(repetidos_dia_hora>0):
                puntaje += 5000*repetidos_dia_hora
            cursos_dia += set(cursos_dia_hora)
        repetidos_dia = len(cursos_dia) - len(set(cursos_dia))
        if(repetidos_dia>0): #Se revisan cuantos cursos pueden aparecer en mas de una hora por dia
            puntaje += (500 * repetidos_dia)
        cursos_semana.append(list(set(cursos_dia)))

    for i in range(len(cursos_semana)):
        if(i<len(cursos_semana)-1):
            dias_seguidos = cursos_semana[i] + cursos_semana [i+1]
            repetidos_dias_seguidos = len(dias_seguidos) - len(set(dias_seguidos))
            if(repetidos_dias_seguidos>0): #Se revisan cuantos cursos pueden estar en dos dia seguidos
                puntaje += (50 * repetidos_dias_seguidos)
    return puntaje

#Verifica la cantidad de cursos en el horario, penalizando los bloques faltantes o excesivos que tenga una materia
def cantidad_cursos(horario):
    puntaje = 0
    cursos = []
    for dia in horario:
        for hora in horario[dia]:
            for salon in horario[dia][hora]:
                cursos.append(horario[dia][hora][salon].numero)
    cursos = count(cursos) #Se usa el metodo Count el cual retorna un diccionario con los cursos que tenga el horario y la frecuencia en que aparecen los mismos
    cursos_faltantes = total_cursos - len(cursos.keys())
    if(cursos_faltantes > 0): puntaje += 5000*(cursos_faltantes) #Se penalizan los cursos faltantes
    bloques_cursos = cursos.values()
    for bloque in bloques_cursos:
        bloques_adicionales = abs(horas_semanales/2 - bloque)
        if(bloques_adicionales > 0): puntaje += 500*(bloques_adicionales) #Se penalizan los bloques de cursos faltantes o excesivos
    return puntaje

#Se evaluan los individuos de una generacion con la funcion de aptitud
def evaluar_generacion(generacion):
    for indv in generacion:
        punt = funcion_aptitud(indv)
        print punt
        if punt == 0: return indv #Si un individuo solucion es encontrado se retorna
    return None


def algoritmo_genetico():
    generacion = poblacion_inicial() #Se genera una poblacion inicial
    solucion = None
    cont = 0
    while solucion == None:
        print "Generacion : " + str(cont)
        selec = seleccion(generacion) #Se seleccionan los individuos mas aptos de la generacion
        generacion = nueva_generacion(selec) #Se genara una nueva poblacion
        solucion = evaluar_generacion(generacion) #Se evalua la nueva poblacion
        cont += 1
    return solucion #Se retorna una solucion cuando esta se encuentre

sol = algoritmo_genetico()
imp_horario(sol)


