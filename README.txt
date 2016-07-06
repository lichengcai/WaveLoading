一个水波形的loading

在XML文件中可以通过paintColor属性配置颜色
                   speed     属性配置加载速度
                   bgBitmap  属性配置背景图
另外这些属性也可通过代码控制
                   setPaintColor(int resource);
                   setSpeed(double speed);
                   setBgBitmap(int resource);
可以通过setFullListener(FullListener fullListener)设置加载完成监听。