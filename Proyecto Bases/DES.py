#-------------------------------------------------------------------------------
# Name:        module1
# Purpose:
#
# Author:      Fabian
#
# Created:     11/09/2014
# Copyright:   (c) Fabian 2014
# Licence:     <your licence>
#-------------------------------------------------------------------------------


#FUNCIONES BASICAS
def bin_to_dec(list_bin):
    str1 = ''.join(str(e) for e in list_bin)
    return int(str1,2)

def dec_to_bin(dec):
    bina = list(bin(dec))[2:]
    bina = [int(i) for i in bina]
    aux =[0]*(4-len(bina))
    return aux + bina

def xor(list1, list2):
    list3 = []
    for i in range(len(list1)):
        list3.append(list1[i]^list2[i])

    return list3

#FUNCION DE CORRIMIENTO A LA IZQUIERDA
def l_shift(list):
    slist = list[1:]
    slist.append(list[0])
    return slist


#PERMUTACIONES
def PC_1(list):
    pc1 = [57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27,
     19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6,
     61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4]

    list2 = range(56)

    for i in range(len(pc1)):
        list2[i] = list[pc1[i]-1]

    return list2

def PC_2(list):
    pc2 = [14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13,
     2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32]

    list2 = range(48)

    for i in range(len(pc2)):
        list2[i] = list[pc2[i]-1]

    return list2

def IP(list):
    ip = [58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22,
     14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19,
     11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7]

    list2 = range(64)

    for i in range(len(ip)):
        list2[i] = list[ip[i]-1]

    return list2

def P(list):
    p = [16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9,
    19, 13, 30, 6, 22, 11, 4, 25]

    list2 = range(32)

    for i in range(len(p)):
        list2[i] = list[p[i]-1]

    return list2

def IP_1(list):
    ip_1=[40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62,
    30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59,
    27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25]

    list2 = range(64)

    for i in range(len(ip_1)):
        list2[i] = list[ip_1[i]-1]

    return list2


#KEY GENERATOR
def key_generator(key):
    k_prim = PC_1(key)
    c0 = k_prim[0:28]
    d0 = k_prim[28:56]
    print "C0:" + "".join(str(x) for x in c0)
    print "D0:" + "".join(str(x) for x in d0)
    keys = []
    c_i =[]
    d_i = []
    c_i.append(l_shift(c0))
    print "C1:" + "".join(str(x) for x in c_i[-1])
    d_i.append(l_shift(d0))
    print "D1:" + "".join(str(x) for x in d_i[-1])
    keys.append(PC_2(c_i[-1]+d_i[-1]))

    for i in range(2,17):
        if i==2 or i==9 or i==16:
           c_i.append(l_shift(c_i[-1]))
           d_i.append(l_shift(d_i[-1]))
           keys.append(PC_2(c_i[-1]+d_i[-1]))

        else:
           c_i.append(l_shift(l_shift(c_i[-1])))
           d_i.append(l_shift(l_shift(d_i[-1])))
           keys.append(PC_2(c_i[-1]+d_i[-1]))

        print "C"+ str(i) + ":" + "".join(str(x) for x in c_i[-1])
        print "D"+ str(i) + ":" + "".join(str(x) for x in d_i[-1])

    for i in range(0,len(keys)):
        print "k" + str(i+1) + ":" + "".join(str(x) for x in (keys[i]))

    return keys

#FUNCION DE EXPANSION
def E(list):
    e = [32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
    24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1]

    list2 = range(48)

    for i in range(len(e)):
        list2[i] = list[e[i]-1]

    return list2

#FUNCION f
def F(exp_ri_1,key_i):
    B= xor(exp_ri_1,key_i)
    B_list = S_BOX([B[0:6], B[6:12], B[12:18], B[18:24], B[24:30], B[30:36], B[36:42], B[42:48]] )
    return P(B_list)

#S-BOXES
def S_BOX(B_list):
    s1 = [[14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7],
    [0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8],
    [4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0],
    [15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13]]


    s2 = [[15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10],
    [3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5],
    [0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15],
    [13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9]]


    s3 = [[10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8],
    [13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1],
    [13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7],
    [1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12]]

    s4 = [[7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15],
    [13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9],
    [10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4],
    [3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14]]

    s5 = [[2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9],
    [14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6],
    [4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14],
    [11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3]]

    s6 = [[12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11],
    [10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8],
    [9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6],
    [4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13]]

    s7 = [[4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1],
    [13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6],
    [1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2],
    [6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12]]

    s8 = [[13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7],
    [1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2],
    [7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8],
    [2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11]]


    set_sbox = []
    set_sbox.append(s1)
    set_sbox.append(s2)
    set_sbox.append(s3)
    set_sbox.append(s4)
    set_sbox.append(s5)
    set_sbox.append(s6)
    set_sbox.append(s7)
    set_sbox.append(s8)


    sbox = []

    for i in range(len(B_list)):
        row = bin_to_dec([B_list[i][0], B_list[i][5]])
        col = bin_to_dec([B_list[i][1], B_list[i][2], B_list[i][3], B_list[i][4]])
        sbox = sbox + (dec_to_bin(set_sbox[i][row][col]))

    return sbox

#ENCRYPT FUNCTION
def message_cypher(m, keys):
    m_prim=IP(m)
    L0=m_prim[0:32]
    print "L0:" + "".join(str(x) for x in L0)
    R0=m_prim[32:64]
    print "R0:" + "".join(str(x) for x in R0)
    Li = []
    Ri = []
    Li.append(R0)
    print "L1:" + "".join(str(x) for x in Li[-1])
    Ri.append(xor(F(E(R0),keys[0]),L0))
    print "R1:" + "".join(str(x) for x in Ri[-1])

    for i in range(2,17):
        Li.append(Ri[-1])
        print "L" + str(i) + ":" + "".join(str(x) for x in Li[-1])
        Ri.append(xor(F(E(Ri[-1]),keys[i-1]),Li[-2]))
        print "R" + str(i) + ":" + "".join(str(x) for x in Ri[-1])

    return IP_1(Ri[-1]+Li[-1])

#DECRYPT FUNCTION
def message_descypher(c, keys):
    c_prim=IP(c)
    R16=c_prim[0:32]
    print "R16:" + "".join(str(x) for x in R16)
    L16=c_prim[32:64]
    print "L16:" + "".join(str(x) for x in L16)
    Li = []
    Ri = []
    Ri.append(L16)
    print "R15:" + "".join(str(x) for x in Ri[-1])
    Li.append(xor(F(E(L16),keys[-1]),R16))
    print "L15:" + "".join(str(x) for x in Li[-1])

    for i in range(2,17):
        Ri.append(Li[-1])
        print "R" + str(16-i) + ":" + "".join(str(x) for x in Ri[-1])
        Li.append(xor(F(E(Li[-1]),keys[16-i]),Ri[-2]))
        print "L" + str(16-i) + ":" + "".join(str(x) for x in Li[-1])

    return IP_1(Li[-1]+Ri[-1])










m = [0,1,0,0,0,0,1,1,0,1,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,1,0,1,0,0,0,0,0,1,0,1,0,1,0,0,0,1,0,0,1,1,1,1,0,1,0,1,0,1,0,0,0,1,0,0,0,1,0,1,0,1,
      0,0,0,1,0,0,0,1,0,0,1,0,0,1,0,1,0,1,0,1,1,0,0,1,0,0,1,0,0,1,0,1,0,0,0,1,0,1,0,1,0,1,0,0,1,0,0,1,0,1,0,1,0,0,0,1,0,0,0,1,0,1]

key= [0,1,0,0,0,0,1,1,0,1,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,1,0,1,0,0,0,0,0,1,0,1,0,1,0,0,0,1,0,0,1,1,1,1,0,1,0,1,0,1,0,1,0,1,0,0,1,1,1,0]
keys = key_generator(key)

cypher_m =   message_cypher(m[0:64],keys) + message_cypher(m[64:128],keys)
print hex(int("".join(str(x) for x in cypher_m),2))

'''
descypher_m =   "".join(str(x) for x in message_descypher(cypher_m[0:64],keys) + message_descypher(cypher_m[64:128],keys))
print "".join(str(x) for x in m)
print descypher_m'''




'''
cypher_m = [1,1,0,0,1,0,1,0,1,1,0,1,1,0,1,0,1,1,1,0,1,1,0,0,1,0,1,1,0,1,1,0,0,1,1,1,0,1,1,0,0,1,0,0,0,0,0,1,1,0,0,0,1,0,1,0,0,1,0,1,1,1,1,1,0,1,0,0,0,0,
1,0,0,1,0,0,1,1,0,0,0,1,1,0,1,0,0,1,1,1,0,0,1,0,1,0,0,0,0,0,1,0,0,0,0,1,1,1,0,0,1,0,1,0,0,1,0,1,0,0,0,0,0,0,0,1,1,1]

key=[0,1,0,0,1,0,0,0,0,1,0,0,1,1,1,1,0,1,0,0,1,1,0,0,0,1,0,0,0,0,0,1,0,1,0,0,1,0,1,1,0,1,0,0,0,1,0,1,0,1,0,1,1,0,0,1,0,1,0,1,1,0,0,1]
keys = key_generator(key)

descypher_m =   "".join(str(x) for x in message_descypher(cypher_m[0:64],keys) + message_descypher(cypher_m[64:128],keys))
result =  hex(int(descypher_m,2))
print result
print result[2:-1].decode("hex")'''







