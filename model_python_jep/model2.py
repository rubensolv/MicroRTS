import tensorflow as tf
import numpy as np
print(tf.__version__)




camadas =8;
largura =18;
altura = 8
grupos =8

def get_one_hot(my_input, size):
    aux = tf.constant([-1,0,1,2,3,4,5,6],dtype=tf.float32)
    sera = tf.math.greater_equal(aux,my_input)
    return sera

def set_one_hot(my_input, size):
    aux = tf.constant([0,1,2,3,4,5,6,7],dtype=tf.float32)
    sera = tf.equal(aux,my_input)
    return sera


def cria_model():
    inp1= tf.keras.Input(shape=(8,18,8),dtype=float,name="input1")
    inp2=tf.keras.Input(shape=(8*18),dtype=float,name="input2")
    inp3=tf.keras.Input(shape=(1),dtype=float,name="input3")
    
    inp22=inp2*10000
    split = tf.keras.layers.Lambda( lambda x: tf.split(x,num_or_size_splits=8*18,axis=1))(inp22)

    
    
    conv1 = tf.keras.layers.Conv2D(4, (1, 1), activation='relu',kernel_regularizer=tf.keras.regularizers.l2(0.001), input_shape=(8, 18, 8))(inp1)
    d_conv1 = tf.keras.layers.Dropout(.2)(conv1)
    conv2 = tf.keras.layers.Conv2D(8, (3, 3), activation='relu',kernel_regularizer=tf.keras.regularizers.l2(0.001))(d_conv1)
    d_conv2 = tf.keras.layers.Dropout(.2)(conv2)
    conv3 = tf.keras.layers.Conv2D(4, (1, 1), activation='relu',kernel_regularizer=tf.keras.regularizers.l2(0.001))(d_conv2)
    d_conv3 = tf.keras.layers.Dropout(.2)(conv3)
    flatten = tf.keras.layers.Flatten()(d_conv3)
    cmd = tf.keras.layers.Dense(8*largura*altura,activation='relu',kernel_initializer='random_uniform')(flatten)
    d_cmd = tf.keras.layers.Dropout(.2)(cmd)
    
    g_conv1 = tf.keras.layers.Conv2D(1, (1, 1), activation='relu',kernel_regularizer=tf.keras.regularizers.l2(0.001),use_bias= True, input_shape=(8, 18, 8))(inp1)    
    g_flatten = tf.keras.layers.Flatten()(g_conv1)
    g_cmd1 = tf.keras.layers.Dense(4*largura*altura,activation='linear',kernel_initializer='random_uniform',use_bias= True,kernel_regularizer=tf.keras.regularizers.l2(0.0001))(g_flatten)
    g_d_cmd1 = tf.keras.layers.Dropout(.2)(g_cmd1)
    g_cmd = tf.keras.layers.Dense(4*largura*altura,activation='linear',kernel_initializer='random_uniform',use_bias= True,kernel_regularizer=tf.keras.regularizers.l2(0.0001))(g_d_cmd1)
    g_d_cmd = tf.keras.layers.Dropout(.2)(g_cmd)
    
    entrada_grupo = tf.cast(set_one_hot(my_input = inp3, size = 8),tf.float32)*tf.constant(1000.0,dtype=tf.float32)
    
    cmd_grupo = tf.keras.layers.Dense(8,activation='relu',kernel_initializer='random_uniform')(g_d_cmd)
    
    anula=cmd_grupo+entrada_grupo
    
    grupos = tf.keras.activations.softmax(anula)
    arg=tf.keras.layers.Reshape((1,))(tf.cast(tf.math.argmax(grupos,1),tf.float32))
    
    h = tf.cast(get_one_hot(my_input = arg, size = 8),tf.float32) * tf.constant(-10000.0,dtype=tf.float32)
    
    out = []
    for i in range(8*18):
      aux = tf.keras.layers.Dense(8,activation='linear',kernel_initializer='random_uniform')(d_cmd)
      aux = aux+h
      aux = tf.keras.activations.softmax(tf.keras.layers.concatenate([aux,split[i]]))
      out.append(aux)
      
      
    model = tf.keras.Model(inputs=[inp1,inp2,inp3],outputs=[grupos,out])
    model.compile(optimizer=tf.keras.optimizers.Adam(0.001),
              loss=tf.keras.losses.CategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])


    
    return model
        















