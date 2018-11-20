
from __future__ import print_function
import numpy as np
import keras
from keras.datasets import mnist
from keras.models import Sequential
from keras.layers.core import Dense, Activation
from keras.optimizers import SGD
from keras.utils import np_utils

np.random.seed(1671)  #重复性设置

#网络训练
NB_EPOCH = 200
BATCH_SIZE = 128
VERBOSE = 1
NB_CLASSES = 10 #输出个数等于数字个数
OPTIMIZER = SGD()   # SGD 优化器，
N_HIDDEN = 128
VALIDATION_SPLIT=0.2   # 训练集中用作验证集的数据比例

#数据：混合并划分训练集和测试集数据
#
(x_train,y_train),(x_test,y_test) = mnist.load_data()
#x_train是60000行28*28的数据，变形为60000*784
RESHAPED=784
x_train = x_train.reshape(60000,RESHAPED)
x_test = x_test.reshape(10000,RESHAPED)
x_train = x_train.astype('float32')
x_test = x_test.astype('float32')
#归一化
x_train /= 255
x_test /=255
print(x_train.shape[0],'train samples')
print(x_test.shape[0],'test samples')

#将类向量转换为二值类别矩阵
y_train = np_utils.to_categorical(y_train,NB_CLASSES)
y_test = np_utils.to_categorical(y_test,NB_CLASSES)
#10个输出
#最后是 softmax 激活函数
model = Sequential()
model.add(Dense(NB_CLASSES, input_shape=(RESHAPED,)))
model.add(Activation('softmax'))
model.summary()
model.compile(loss='categorical_crossentropy',optimizer=OPTIMIZER,metrics=['accuracy'])

history = model.fit(x_train,y_train, batch_size=BATCH_SIZE,epochs=NB_EPOCH,verbose=VERBOSE,validation_split=VALIDATION_SPLIT)
score = model.evaluate(x_test,y_test,verbose=VERBOSE)
print("Test score: ",score[0])
print("Test score: ",score[1])