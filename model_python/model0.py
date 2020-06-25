import tensorflow as tf
import numpy as np
print(tf.__version__)
graph = tf.Graph()
builder = tf.saved_model.builder.SavedModelBuilder('./mod0')


camadas =8;
largura =18;
altura = 8
grupos =8


def weight_variable(shape):
   initial = tf.truncated_normal(shape, stddev=0.01)
   return tf.Variable(initial)

def bias_variable(shape):
  initial = tf.constant(0.1, shape=shape)
  return tf.Variable(initial)

def conv2d(x, W):
  return tf.nn.conv2d(x, W, strides=[1, 1, 1, 1], padding='SAME')

def conv_layer(input, shape):
   Wi = weight_variable(shape)
   b = bias_variable([shape[3]])
   return tf.nn.relu(conv2d(input, Wi) + b)

def full_layer(input, size):
  in_size = int(input.get_shape()[1])
  W = weight_variable([in_size, size])
  b = bias_variable([size])
  return tf.matmul(input, W) + b

def get_one_hot(my_input, size):
    aux = tf.constant([0,1,2,3,4,5,6,7,0],dtype=tf.float32)
    my_array = tf.ones((size,), dtype=tf.float32)
    sera = tf.greater_equal(aux,my_input)
    return sera



initializer = tf.contrib.layers.xavier_initializer()

entrada = tf.placeholder(tf.float32,[None,altura,largura,camadas],name='x')
anula = tf.placeholder(tf.float32,[None,altura*largura],name='x2')
anula2 = tf.reshape(anula,[-1,largura*altura,1])
y = tf.placeholder(tf.float32, [None,largura*altura,grupos+1],name='out')



num_grupo = tf.placeholder(tf.float32,[None,1],name='g')

my_tensor = tf.cast(get_one_hot(my_input = num_grupo, size = 8),tf.float32)
mult = tf.constant(-10000.0,dtype=tf.float32)
final = mult * my_tensor


conv1 = conv_layer(entrada, shape=[1, 1, camadas, 4])
conv2 = conv_layer(conv1, shape=[3, 3, 4, 8])
conv3 = conv_layer(conv2, shape=[1, 1, 8, 4])




flatten= tf.reshape( conv3,[-1,largura*altura*4])


full_2 = tf.nn.relu(full_layer(flatten, largura*altura*grupos))




y_pred=tf.reshape(full_2,[-1,largura*altura,grupos])

yy =tf.concat([y_pred,anula2],2)




limt= tf.reshape(tf.tile(final, [144,1]),[-1,144,9])


ppp=tf.math.add(yy,limt)


yyy= tf.nn.softmax(ppp,name="saida")

yyyy= tf.nn.softmax_cross_entropy_with_logits(    logits=yyy, labels=y,)

cross_entropy = tf.reduce_mean(yyyy)
gd_step = tf.train.GradientDescentOptimizer(3.0).minimize(cross_entropy,name="treina")


with tf.Session() as sess:# Train
    sess.run(tf.global_variables_initializer())
    builder.add_meta_graph_and_variables(sess, [tf.saved_model.tag_constants.SERVING])
    builder.save()

    tf.train.write_graph(sess.graph, "./",
                     'saved_model.pb', as_text=False)
