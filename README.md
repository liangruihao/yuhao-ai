# yuhao-ai



环境：MAC,PYTHON3.6.7

1、安装IDLE.
https://www.python.org/ftp/python/3.6.7/python-3.6.7-macosx10.9.pkg

2、安装tensorflow
$ pip3 install --upgrade https://storage.googleapis.com/tensorflow/mac/cpu/tensorflow-1.0.1-py3-none-any.whl
$ pip3 install --upgrade https://storage.googleapis.com/tensorflow/mac/cpu/tensorflow-1.2.1-py3-none-any.whl
 
$ pip3 install --upgrade https://download.tensorflow.google.cn/mac/cpu/tensorflow-1.8.0-py3-none-any.whl

如果网络连接失败则将URL的网址部分替换为清华镜像

$ pip3 install --upgrade https:// https://mirrors.tuna.tsinghua.edu.cn/mac/cpu/tensorflow-1.3.0-py3-none-any.whl

--------------------- 


3、安装keras
$ pip3 install keras

$ pip3 install keras==2.1



>>> import keras
Using TensorFlow backend.
>>> keras.__version__
'2.1.0'
>>> import tensorflow as tf
>>> tf.__version__
'1.0.1'
>>> 
