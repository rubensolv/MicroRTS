import numpy as np
import imp



class Modelo:



	def __init__(self,id,camada,linha,coluna,num_grupos,epoca,buffer,estrutura):
		arquivo, caminho, descricao = imp.find_module('model'+estrutura, ['C:/Users/david/Desktop/MicroRTS/model_python_jep'])
		model = imp.load_module('model'+estrutura, arquivo, caminho, descricao)
		self.id=id
		self.camadas=camada
		self.linhas=linha
		self.colunas=coluna
		self.grupos=num_grupos

		self.n=0
		self.epoca = epoca
		self.buffer =buffer

		self.entradas1=np.zeros((0,self.linhas,self.colunas,self.camadas),dtype=np.float16)
		self.entradas2=np.zeros((0,self.linhas*self.colunas),dtype=np.int8)
		self.entradas3=np.zeros((0,1))
		
		
		#self.saidas_grupo =np.zeros((0,self.grupos))
		
		
		self.saidas = []
		self.saidas.append(np.zeros((0,self.grupos)))
		for j in  range(self.linhas*self.colunas):
			self.saidas.append(np.zeros((0,self.grupos+1)))
			
		self.deleta = []	

		for j in  range(10):
			self.deleta.append(0)

		self.m  = model.cria_model()
		



	def entrada1(self,estado):     
		estado = estado.split('#')
		input = np.zeros((self.linhas,self.colunas,self.camadas))
		for i in range(1,self.camadas+1):
			aux=estado[i].split(',')
			for k in range(self.linhas):
				for j in range(self.colunas):
					input[k][j][i-1]= float(aux[k*self.colunas+j])
		return input

	def entrada2(self,estado):
	
		input = np.ones((self.linhas*self.colunas),np.int8)
		input = input*1
		aux=estado.split(',')
		unit = {}
		for i in range(len(aux)-1):
			id_unit = aux[i] 
			aux2 = id_unit.split(':')
			input[int(aux2[1])]= -1
			unit[aux2[0]]=int(aux2[1])
		return unit,input

	def montar_saida(self,saida):
		
		
		out1= []
		for i in range(8*18):
			aux2=np.zeros([1,8+1],np.int8)
			aux2[0][8]=1
			out1.append(aux2)
		aux=saida.split(',')
		for i in range(len(aux)-1):
			id_unit = aux[i] 
			aux2 =id_unit.split(':')
			out1[int(aux2[0])][0][8]=0
			out1[int(aux2[0])][0][int(aux2[1])]=1
		return out1



	def amostra(self,msg):  
		estado = msg.split("*");
		input1 = self.entrada1(estado[0]).reshape((1,self.linhas,self.colunas,self.camadas))
		unit, input2 = self.entrada2(estado[1])
		input2 = input2.reshape((1,self.linhas*self.colunas))
		input3 = np.zeros((1,1))
		input3[0][0] = int(estado[2])
		return self.rna_amostra(input1, input2, input3,unit)
		
		
	
	def amostra_grupo(self,msg):  
		estado = msg.split("*");
		input1 = self.entrada1(estado[0]).reshape((1,self.linhas,self.colunas,self.camadas))
		unit, input2 = self.entrada2(estado[1])
		input2 = input2.reshape((1,self.linhas*self.colunas))
		input3 = np.zeros((1,1))
		input3[0][0] = int(estado[2])
		return self.rna_amostra_num_grupo(input1, input2, input3,unit)	
	

	def rna_amostra_num_grupo(self,in1,in2,in3,unit):
		msg = ""
		#for i in range(2):
		#   sess.run(gd_step,feed_dict={entrada : x, anula : x2,y:out})
		#sess.run()
		saida = self.m.predict([in1,in2,in3])
		for g in saida[0][0]:
			msg+=str(g)
			msg+=","
		
		return msg 

	def teste(self,in1,in2,in3):
			saida = self.m.predict([in1,in2,in3])
			print(len(saida))

	def rna_amostra(self,in1,in2,in3,unit):
		msg = ""
		#for i in range(2):
		#   sess.run(gd_step,feed_dict={entrada : x, anula : x2,y:out})
		#sess.run()
		saida = self.m.predict([in1,in2,in3])
		for u in unit:
			msg+=u
			msg+=":"
			for i in range(self.grupos):
				msg+=str(saida[unit[u]+1][0][i])
				msg+=","
			msg+=";"
		return msg 



	def treina(self):
		aux = self.entradas1.shape[0]
		num = 50
		if aux < 50:
			
			num=aux
		idx = np.random.randint(aux, size=num)
		inp1 = self.entradas1[idx]
		inp2 = self.entradas2[idx]
		inp3 = self.entradas3[idx]
		
		out = []
		for i in range(self.linhas*self.colunas+1):
			out.append(self.saidas[i][idx])
		
		self.m.fit([inp1,inp2,inp3],out,batch_size=30,epochs=50,verbose=0)
			





	def grava(self,msg):  
		estado = msg.split("*");
		input1 = self.entrada1(estado[0]).reshape((1,self.linhas,self.colunas,self.camadas))
		unit, input2 = self.entrada2(estado[1])
		input2 = input2.reshape((1,self.linhas*self.colunas))
		input3 = np.zeros((1,1))
		input3[0][0] = -1
		
		s_grupo =np.zeros((1,self.grupos))
		s_grupo[0][int(estado[2])]=1
		
		saida = self.montar_saida(estado[3])
		self.entradas1=np.concatenate((self.entradas1,input1))
		self.entradas2=np.concatenate((self.entradas2,input2))
		self.entradas3=np.concatenate((self.entradas3,input3))
		self.saidas[0]=np.concatenate((self.saidas[0],s_grupo))
		for i in range(1,self.colunas*self.linhas+1):
			self.saidas[i]=np.concatenate((self.saidas[i],saida[i-1]))
		self.deleta[self.n]+=1
		return 

	def proximo(self):
		#deleta n+1
		self.n=(self.n+1)%10
		self.entradas1 = self.entradas1[self.n:]
		self.entradas2 = self.entradas2[self.n:]
		self.entradas3 = self.entradas3[self.n:]
		for i in range(self.colunas*self.linhas):
			self.saidas[i]=self.saidas[i][self.n:]

		self.deleta[self.n]=0


	def carregar(self,complemento):
		self.m.load_weights("/rna/model"+self.id+"_"+complemento+".h5")

	
	def salvar(self,complemento):
		self.m.save_weights("/rna/model"+self.id+"_"+complemento+".h5")
