import socket
import numpy as np
import model


camadas = linhas = colunas = 8;
colunas =18
grupos=8
m,entrada,anula,num_grupo,y,yyy,gd_step = model.cria_model()
saver = m.train.Saver()
        
sess = m.Session()
sess.run(m.global_variables_initializer())
#saver.restore(sess, "/rna/model.ckpt")
saver.save(sess, "./rna/model.ckpt")
saver.save(sess, "./rna/model1.ckpt")
buffer=3
epoca=30
n=0

entradas1=[]
entradas2=[]
entradas3=[]
saidas = []

for i in range(buffer):
    entradas1.append(np.zeros((0,linhas,colunas,camadas)))
    entradas2.append(np.zeros((0,linhas*colunas)))
    entradas3.append(np.zeros((0,1)))
    saidas.append(np.zeros((0,linhas*colunas,grupos+1)))
    



def ler():
    estado=""
    while True:
        msg = con.recv(128)
        msg =msg.decode()
        estado+=msg
        if msg.find("FIM") != -1:
            break
    return estado

def entrada1(estado):     
    estado = estado.split('#')
    input = np.zeros((linhas,colunas,camadas))
    for i in range(1,camadas+1):
        aux=estado[i].split(',')
        for k in range(linhas):
            for j in range(colunas):
                input[k][j][i-1]= float(aux[k*colunas+j])
        
   
    return input

def entrada2(estado):
 
    input = np.ones((linhas*colunas))
    input = input*10000
    aux=estado.split(',')
    unit = {}
    for i in range(len(aux)-1):
        id = aux[i] 
        aux2 = id.split(':')
        input[int(aux2[1])]= -10000
        unit[aux2[0]]=int(aux2[1])
    return unit,input

def montar_saida(saida):
    out= np.zeros([linhas*colunas,grupos+1])
    out[:,grupos]=1
    aux=saida.split(',')
    for i in range(len(aux)-1):
        id = aux[i] 
        aux2 = id.split(':')
        out[aux2[0]][grupos]=0
        out[aux2[0]][aux2[1]]=1
    return out



def rna_amostra(in1,in2,in3,unit):
    msg = ""
       
    #for i in range(2):
     #   sess.run(gd_step,feed_dict={entrada : x, anula : x2,y:out})
    #sess.run()
    saida = sess.run(yyy,feed_dict={entrada : in1, anula : in2,num_grupo : in3})
    for u in unit:
        msg+=u
        msg+=":"
        for i in saida[0][unit[u]]:
            msg+=str(i)
            msg+=","
        msg+=";"
    return msg 



def amostra():  
    estado = ler().split("*");
    input1 = entrada1(estado[0]).reshape((1,linhas,colunas,camadas))
    unit, input2 = entrada2(estado[1])
    input2 = input2.reshape((1,linhas*colunas))
    input3 = np.zeros((1,1))
    input3[0][0] = int(estado[2])
    msg =rna_amostra(input1, input2, input3,unit)
    con.send(msg.encode())
    return 

def treina():

    for i in range(buffer):
        aux = entradas1[i].shape
        if(aux[0]!=0):
            for j in range(epoca):
                sess.run(gd_step,feed_dict={entrada : entradas1[i], anula : entradas2[i],
                                        num_grupo : entradas3[i], y : saidas[i]})
                

   
    
    


def grava():  
    estado = ler().split("*");
    input1 = entrada1(estado[0]).reshape((1,linhas,colunas,camadas))
    unit, input2 = entrada2(estado[1])
    input2 = input2.reshape((1,linhas*colunas))
    input3 = np.zeros((1,1))
    input3[0][0] = int(estado[2])
    saida = montar_saida(estado[2]).reshape((1,linhas*colunas,grupos+1))
    entradas1[n]=np.concatenate((entradas1[n],input1))
    entradas2[n]=np.concatenate((entradas2[n],input2))
    entradas3[n]=np.concatenate((entradas3[n],input3))
    saidas[n]=np.concatenate((saidas[n],saida))
    msg="ok"
    con.send(msg.encode())
    return 



HOST = ''              # Endereco IP do Servidor
PORT = 12345            # Porta que o Servidor esta
tcp = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
orig = (HOST, PORT)
tcp.bind(orig)
tcp.listen(1)

while True:
    #print("esperando")
    con, cliente = tcp.accept()
    #print ('Concetado por', cliente)
    
    msg = con.recv(128).decode()
    
    #print("servico "+msg)
    if msg == "amostragem\r\n":
        amostra()
        
    elif msg == "gravar\r\n":
        grava()
    elif msg == "proximo\r\n":
        n=(n+1)%buffer
        entradas1[n]=np.zeros((0,linhas,colunas,camadas))
        entradas2[n]=np.zeros((0,linhas*colunas))
        entradas3[n]=np.zeros((0,1))
        saidas[n]=np.zeros((0,linhas*colunas,grupos+1))
        msg2="ok"
        con.send(msg2.encode())
        
    elif msg == "carregar\r\n":
        saver.restore(sess, "/rna/model.ckpt")
        msg2="ok"
        con.send(msg2.encode())
        
    elif msg == "treinar\r\n":
        saver.save(sess, "/rna/model.ckpt")
        treina()
        msg2="ok"
        con.send(msg2.encode())
        
        
    #print("Concluido")
    #print ("------------------------------------------")
    #print (cliente, input1)
    #print ("------------------------------------------")
    #print (cliente, input2)
    #print (cliente, msg)
    #print ('Finalizando conexao do cliente', cliente)
    con.close()
    
