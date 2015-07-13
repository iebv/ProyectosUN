from collections import OrderedDict
from math import exp, log
import random as r
import copy


auditorios = [] #Arreglo con todas las posiciones posibles de auditorios
salones = [] #Arreglo con todas las posiciones posibles de salones
pcs = [] #Arreglo con todas las posiciones posibles de salones de computadores


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
    file = open("horario_templado.txt", "w")
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
def asignacion_inicial():
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


#Calcula un puntaje para el horario de acuerdo a los cruces de materias que puedan presentarse
def funcion_energia(horario):
    puntaje = cruces(horario)
    return puntaje

#Calcula los cruces de un horario
def cruces(horario):
    puntaje = 0
    cursos_semana = []
    for dia in horario:
        cursos_dia = []
        for hora in horario[dia]:
            cursos_dia_hora_salon = horario[dia][hora].values()
            cursos_dia_hora = []
            for curso in cursos_dia_hora_salon:
                cursos_dia_hora.append(curso.numero)
            repetidos_dia_hora = len(cursos_dia_hora) - len(set(cursos_dia_hora)) #Se revisasn cuantos cursos pueden cruzarce a una misma hora en un dia determinado
            if(repetidos_dia_hora>0):
                #print "rep en la hora " + hora + " dia " + dia + " total: " + str(repetidos_dia_hora)
                puntaje += 5000*repetidos_dia_hora
            cursos_dia += set(cursos_dia_hora)
        repetidos_dia = len(cursos_dia) - len(set(cursos_dia))
        if(repetidos_dia>0): #Se revisan cuantos cursos pueden aparecer en mas de una hora por dia
            #print "rep en el dia " + dia  + " total: " + str(repetidos_dia)
            puntaje += (500 * repetidos_dia)
        cursos_semana.append(list(set(cursos_dia)))

    for i in range(len(cursos_semana)):
        if(i<len(cursos_semana)-1):
            dias_seguidos = cursos_semana[i] + cursos_semana [i+1]
            repetidos_dias_seguidos = len(dias_seguidos) - len(set(dias_seguidos))
            if(repetidos_dias_seguidos>0): #Se revisan cuantos cursos pueden estar en dos dia seguidos
                #print "rep en dias seguidos " + str(i) + " " + str(i+1)  + " total: " + str(repetidos_dias_seguidos)
                puntaje += (50 * repetidos_dias_seguidos)
    return puntaje


#Cambia un horario dado, modificando el horario de algunos cursos en forma aleatoria
def horario_modificado(horario):
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


#Algoritmo de templado simulado

#--Esta funcion calcula el promedio de una serie de diferencias (deltas) entre los valores de energia de estados vecinos
#--Se usa para determinar la temperatura inicial del algoritmo de templado
def max_delta_E():
    all_max = []
    max = 0
    actual = asignacion_inicial()
    for i in range(0,50):
        for j in range(0,50):
            siguiente = horario_modificado(actual)
            deltaE = funcion_energia(siguiente) - funcion_energia(actual)
            if(deltaE > max):
                max = deltaE
            actual = siguiente
        all_max.append(max)
    return sum(all_max)/len(all_max)

def templado_simulado():
    actual = asignacion_inicial() #Se encuentra un estado inicial factible
    T= int(-max_delta_E()/log(0.98)) #Se establece la temperatura inicial
    while(funcion_energia(actual)!=0): #El algoritmo itera mientras no se encuentre una solucion optima
        for i in range(1,100): #Numero de iteraciones para una temperatura determinada
            siguiente = horario_modificado(actual) #Se calcula un estado siguiente
            energiaAct = funcion_energia(actual) #Se calcula la funcion de energia del estado siguiente
            energiaSig = funcion_energia(siguiente) #Se calcula la funcion de energia del estado actual
            deltaE = energiaSig - energiaAct #Se calcula la diferencia de energia entre los estados
            print energiaAct, energiaSig
            print deltaE
            if (deltaE < 0): #Se acepta el nuevo estado si este es mejor que el actual
                actual = siguiente
            else:
                probabilidad = exp((-deltaE/float(T))) #Si no es mejor puede aceptarlo con una probabilidad dada
                print probabilidad
                r.seed()
                random = r.uniform(0,1)
                print random
                if(random < probabilidad): actual = siguiente
        T = 0.90*T #Se disminuye la temperatura en un 10%
    return actual #Se retorna la solucion optima




result = templado_simulado()
print funcion_energia(result)
imp_horario(result) #Se imprime el horario semanal de cada salon en un archivo de texto
