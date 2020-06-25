import tensorflow as tf
import numpy as np
print(tf.__version__)




camadas =8;
largura =18;
altura = 8
grupos =8

def get_one_hot(my_input, size):
    aux = tf.constant([0,1,2,3,4,5,6,7],dtype=tf.float32)
    my_array = tf.ones((size,), dtype=tf.float32)
    sera = tf.greater_equal(aux,my_input)
    return sera


def cria_model():
    inp1= tf.keras.Input(shape=(8,18,8),dtype=float,name="input1")
    inp2=tf.keras.Input(shape=(8*18),dtype=float,name="input2")
    inp3=tf.keras.Input(shape=(1),dtype=float,name="input3")
    
    split = tf.keras.layers.Lambda( lambda x: tf.split(x,num_or_size_splits=8*18,axis=1))(inp2)

    h = tf.cast(get_one_hot(my_input = inp3, size = 8),tf.float32) * tf.constant(-10000.0,dtype=tf.float32)
    
    flatten = tf.keras.layers.Flatten()(inp1)
    cmd = tf.keras.layers.Dense(124,activation='relu',kernel_initializer='random_uniform')(flatten)
    
    out = []
    for i in range(8*18):
      aux = tf.keras.layers.Dense(8,activation='linear',kernel_initializer='random_uniform')(cmd)
      aux = aux+h
      aux = tf.keras.activations.softmax(tf.keras.layers.concatenate([aux,split[i]]))
      out.append(aux)
      
      
    model = tf.keras.Model(inputs=[inp1,inp2,inp3],outputs=out)
    model.compile(optimizer=tf.keras.optimizers.Adam(0.01),
              loss=tf.keras.losses.CategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

    
    return model
        















