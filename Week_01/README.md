# 学习笔记
***
*前部分是作业，后部分是学习笔记*

## 作业一
### 作业要求：
&ensp;&ensp;&ensp;&ensp;自己写一个简单的Hello.java，里面需要涉及基本类型，四则运行，if和for，然后自己分析一下对应的字节码，有问题群里讨论。

&ensp;&ensp;&ensp;&ensp;学习中搜索到的相应参考链接：

- [字节码指令集oracle文档:https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5)

### 思路
- 1.首先在当前周下week_01下建立代码工程目录：code，并新建工程
- 2.编写代码文件：MyHello.java(src目录下)
- 3.编译生成：MyHello.class(build目录下)
- 4.字节码解释

#### MyHello.java

```java
import java.util.ArrayList;
import java.util.List;

public class MyHello {
    public static void main(String[] args) {
        int num1 = 1;
        int num2 = 130;
        int num3 = num1 + num2;
        int num4 = num2 - num1;
        int num5 = num1 * num2;
        int num6 = num2 / num1;

        final int num7 = 5;
        Integer num88 = 6;

        // 仅作为试验，看装箱指令
        if (num88 == 0) {
            System.out.println(num1);
        }

        List<Integer> nums = new ArrayList<>();
        nums.add(1);
        nums.add(2);

        for (int num : nums) {
            System.out.println(num);
        }

        if (nums.size() == num2) {
            System.out.println(num2);
        }
    }
}
```

### 字节码展示及相关说明

```shell script
PS C:\Users\12439\Documents\Code\Java\JAVA-000\Week_01\code\build\classes\java\main> javap -c .\MyHello.class
Compiled from "MyHello.java"
public class MyHello {
  public MyHello();
    // 调用父类Object的初始化函数
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       // 初始化变量num1
       0: iconst_1
       1: istore_1

       // 初始化化变量num2，这里的指令有些变化，这里查资料得到不同数据类型和访问值会用到不同的指令，可以看下面的int数据类型指令表
       2: sipush        130
       5: istore_2

       // 计算num3
       6: iload_1
       7: iload_2
       8: iadd
       9: istore_3
       
      // 计算num4
      10: iload_2
      11: iload_1
      12: isub
      13: istore        4

      // 计算num5
      15: iload_1
      16: iload_2
      17: imul
      18: istore        5

      // 计算num6
      20: iload_2
      21: iload_1
      22: idiv
      23: istore        6

      // 初始化化变量num7
      25: iconst_5
      26: istore        7

      // 初始化化变量num88,这里可以看到触发了Integer的自动装箱；想到写程序时还是尽量用基础类型，这样快点，不然会触发自动装箱/拆箱，多一些指令
      28: bipush        6
      30: invokestatic  #2                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
      33: astore        8

      // 判断，调用静态方法打印
      35: aload         8
      37: invokevirtual #3                  // Method java/lang/Integer.intValue:()I
      40: ifne          50
      43: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
      46: iload_1
      47: invokevirtual #5                  // Method java/io/PrintStream.println:(I)V

      // 这里初始化list容器对象
      50: new           #6                  // class java/util/ArrayList
      53: dup
      54: invokespecial #7                  // Method java/util/ArrayList."<init>":()V
      57: astore        9

      // 这里获取list，生成Integer，进行添加
      59: aload         9
      61: iconst_1
      62: invokestatic  #2                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
      65: invokeinterface #8,  2            // InterfaceMethod java/util/List.add:(Ljava/lang/Object;)Z
      70: pop

      71: aload         9
      73: iconst_2
      74: invokestatic  #2                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
      77: invokeinterface #8,  2            // InterfaceMethod java/util/List.add:(Ljava/lang/Object;)Z
      82: pop

      // 获取迭代器
      83: aload         9
      85: invokeinterface #9,  1            // InterfaceMethod java/util/List.iterator:()Ljava/util/Iterator;
      90: astore        10
      // 遍历容器打印
      92: aload         10
      94: invokeinterface #10,  1           // InterfaceMethod java/util/Iterator.hasNext:()Z
      99: ifeq          128
     102: aload         10
     104: invokeinterface #11,  1           // InterfaceMethod java/util/Iterator.next:()Ljava/lang/Object;
     109: checkcast     #12                 // class java/lang/Integer
     112: invokevirtual #3                  // Method java/lang/Integer.intValue:()I
     115: istore        11
     117: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
     120: iload         11
     122: invokevirtual #5                  // Method java/io/PrintStream.println:(I)V
     125: goto          92

     // 容器大小判断
     128: aload         9
     130: invokeinterface #13,  1           // InterfaceMethod java/util/List.size:()I
     135: iload_2
     136: if_icmpne     146
     139: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
     142: iload_2
     143: invokevirtual #5                  // Method java/io/PrintStream.println:(I)V

     146: return
}
```

### int数据类型指令表
&ensp;&ensp;&ensp;&ensp;int(bool,byte,char,short)常用指令及范围如下：

- iconst: [-1, 5]
- bipush: [-128, 127]
- sipush: [-32768, 32767]
- idc: any int value

&ensp;&ensp;&ensp;&ensp;其他的就不展示了，搜索相关资料也能查到。字节码这部分内容感觉有个大概了解就行了，不必太深入。

## 作业二
### 作业要求：
&ensp;&ensp;&ensp;&ensp;自定义一个Classloader，加载一个Hello.xlass文件，执行hello方法，此文件内容是一个Hello.class文件所有字节(x=255-x)处理后的文件。文件群里提供。

### 思路
- 1.首先了解类加载的相关机制，为啥需要类加载器
- 2.自定义类加载器编写要点：
    - 1.继承类：ClassLoader，并重写方法：findClass
    - 2.核心加载函数：defineClass，其需要字节码文件的字节流及类名；则思路为读取字节码文件成字节流，传入字节流及函数名称即可
    - 3.字节流读取的特殊处理：应被相应加密处理过了，读取的时候进行相应的解密
    - 4.加载class文件，实例化，调用函数
    
 &ensp;&ensp;&ensp;&ensp;这里使用文件绝对路径
 
 ### 完整代码
 
 ```java
import java.io.*;
import java.lang.reflect.InvocationTargetException;

/**
 * 自定义类加载
 * 关键点是defineClass,读取字节码的字节流，生成class，思路就是转化class文件为字节流，传入defineClass中
 * 思路：
 * 1.继承ClassLoader，重写findClass方法
 * 2.从文件中读取转化成字节流
 * 3.传入defineClass进行加载
 * 4.生成实例，调用方法
 */
public class HelloClassLoader extends ClassLoader {
    @Override
    public Class findClass(String name) {
        byte[] b = new byte[0];
        try {
            b = loadClassFromFile(name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // name 就是加载的类名称，这里注意要填写正确
        return defineClass("Hello", b, 0, b.length);
    }

    /**
     * 读取class文件，转化内容为字节流
     * @param fileName 文件路径
     * @return
     * @throws FileNotFoundException
     */
    private byte[] loadClassFromFile(String fileName) throws FileNotFoundException {
        System.out.println(fileName);
        File file = new File(fileName);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ( (nextValue = inputStream.read()) != -1 ) {
                // 注意字节还原
                byteStream.write(255 - nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        // 文件读取，字节流转换，加载
        String path = "C:\\Users\\12439\\Documents\\Code\\Java\\JAVA-000\\Week_01\\code\\src\\main\\resources\\Hello.xlass".replace("\\", "/");
        HelloClassLoader loader = new HelloClassLoader();
        Class hello = loader.loadClass(path);
        System.out.println(hello.getName());
        // 生成实例，调用方法
        Object instance = hello.newInstance();
        System.out.println(hello.getMethod("hello").invoke(instance));
    }
}
```

## 作业三
### 作业要求
&ensp;&ensp;&ensp;&ensp;画一张图，展示Xmx 、 Xms 、 Xmn 、 Meta 、 DirectMemory 、 Xss 这些内存参数的关系。

### 思路
&ensp;&ensp;&ensp;&ensp;首先要了解上面各个参数的含义：

- Xmx：指定最大堆内存
- Xms：指定堆内存空间的初始大小
- Xmn：设置新生代初始和最大大小；设置过小会导致频繁GC，过大会导致GC一次时间过长；建议1/2~1/4
- Meta(-XX:MaxMetaspaceSize=size)：设置Meta空间大小
- DirectMemory(-XX:MaxDirectMemorySize=size)：设置系统可使用的最大堆外内存
- Xss：设置每个线程栈的字节数

### 内存模型及相应参数对应图

![内存模型](./memory.png)

## 作业四、五（一起做了）
### 作业要求 
&ensp;&ensp;&ensp;&ensp;作业4：检查一下自己维护的业务系统的JVM参数配置，用jstat和jstack、jmap查看一下详情，并且自己独立分析一下大概情况，思考有没有不合理的地方，如何改进。

&ensp;&ensp;&ensp;&ensp;作业5：本机使用G1 GC启动一个程序，仿照课上案例分析一下JVM情况

&ensp;&ensp;&ensp;&ensp;这里使用秦老师的GC日志分析的代码进行运行，然后使用命令查看下情况（Java11，G1 GC），完整的代码如下：不断生成新的对象

```java
package com.company;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * GC日志生成演示与解读
 */
public class GCLogAnalysis {

    private static Random random = new Random();

    public static void main(String[] args) {
        // 当前毫秒时间戳
        long startMillis = System.currentTimeMillis();
        // 持续运行毫秒数，可根据需要进行修改
        long timeoutMillis = TimeUnit.SECONDS.toMillis(1);
        // 结束时间戳
        long endMillis = startMillis + timeoutMillis;
        LongAdder counter = new LongAdder();
        System.out.println("正在执行");

        // 缓存一部分对象，进入老年代
        int cacheSize = 2000;
        Object[] cacheGarbege = new Object[cacheSize];

        // 在此时间范围内，持续循环
        while (true) {
//        while (System.currentTimeMillis() < endMillis) {
            // 生成垃圾对象
            Object garbage = generateGarbage(100 * 1024);
            counter.increment();
            int randomIndex = random.nextInt(2 * cacheSize);
            if (randomIndex < cacheSize) {
                cacheGarbege[randomIndex] = garbage;
            }
            System.out.println("执行中！ 共生成对象次数：" + counter.longValue());
        }

//        System.out.println("执行结束！ 共生成对象次数：" + counter.longValue());
    }

    /**
     * 生成对象
     * @param maxSize
     * @return
     */
    private static Object generateGarbage(int maxSize) {
        int randomSize = random.nextInt(maxSize);
        int type = randomSize % 4;
        Object result = null;
        switch (type) {
            case 0:
                result = new int[randomSize];
                break;
            case 1:
                result = new byte[randomSize];
                break;
            case 2:
                result = new double[randomSize];
                break;
            default:
                StringBuilder builder = new StringBuilder();
                String randomString = "randomString-Anything";
                while (builder.length() < randomSize) {
                    builder.append(randomString);
                    builder.append(maxSize);
                    builder.append(randomSize);
                }
                result = builder.toString();
                break;
        }
        return result;
    }
}
```

&ensp;&ensp;&ensp;&ensp;GC的相关统计信息如下：

```bash
❯ .\jstat.exe -gc 24172 100 100
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT    CGC    CGCT     GCT
 0.0   399360.0  0.0   399360.0 4485120.0 614400.0 2867200.0   943206.2  7936.0 7742.8 768.0  687.5     849   28.181   0      0.000   4      0.005   28.185
 0.0   399360.0  0.0   399360.0 4485120.0 1325056.0 2867200.0   943206.2  7936.0 7742.8 768.0  687.5     849   28.181   0      0.000   4      0.005   28.185
 0.0   399360.0  0.0   399360.0 4485120.0 2004992.0 2867200.0   943206.2  7936.0 7742.8 768.0  687.5     849   28.181   0      0.000   4      0.005   28.185
 0.0   399360.0  0.0   399360.0 4485120.0 2662400.0 2867200.0   943206.2  7936.0 7742.8 768.0  687.5     849   28.181   0      0.000   4      0.005   28.185
 0.0   399360.0  0.0   399360.0 4485120.0 3334144.0 2867200.0   943206.2  7936.0 7742.8 768.0  687.5     849   28.181   0      0.000   4      0.005   28.185
 0.0   399360.0  0.0   399360.0 4485120.0 4059136.0 2867200.0   943206.2  7936.0 7742.8 768.0  687.5     849   28.181   0      0.000   4      0.005   28.185
 0.0   409600.0  0.0   409600.0 4474880.0 272384.0 2867200.0   945619.9  7936.0 7742.8 768.0  687.5     850   28.217   0      0.000   4      0.005   28.222
 0.0   409600.0  0.0   409600.0 4474880.0 1030144.0 2867200.0   945619.9  7936.0 7742.8 768.0  687.5     850   28.217   0      0.000   4      0.005   28.222
 0.0   409600.0  0.0   409600.0 4474880.0 1808384.0 2867200.0   945619.9  7936.0 7742.8 768.0  687.5     850   28.217   0      0.000   4      0.005   28.222
 0.0   409600.0  0.0   409600.0 4474880.0 2510848.0 2867200.0   945619.9  7936.0 7742.8 768.0  687.5     850   28.217   0      0.000   4      0.005   28.222
 0.0   409600.0  0.0   409600.0 4474880.0 3180544.0 2867200.0   945619.9  7936.0 7742.8 768.0  687.5     850   28.217   0      0.000   4      0.005   28.222
 0.0   409600.0  0.0   409600.0 4474880.0 3952640.0 2867200.0   945619.9  7936.0 7742.8 768.0  687.5     850   28.217   0      0.000   4      0.005   28.222
 0.0   389120.0  0.0   389120.0 4495360.0 83968.0  2867200.0   949227.6  7936.0 7742.8 768.0  687.5     851   28.259   0      0.000   4      0.005   28.264
 0.0   389120.0  0.0   389120.0 4495360.0 757760.0 2867200.0   949227.6  7936.0 7742.8 768.0  687.5     851   28.259   0      0.000   4      0.005   28.264
 0.0   389120.0  0.0   389120.0 4495360.0 1492992.0 2867200.0   949227.6  7936.0 7742.8 768.0  687.5     851   28.259   0      0.000   4      0.005   28.264
 0.0   389120.0  0.0   389120.0 4495360.0 2256896.0 2867200.0   949227.6  7936.0 7742.8 768.0  687.5     851   28.259   0      0.000   4      0.005   28.264
 0.0   389120.0  0.0   389120.0 4495360.0 3045376.0 2867200.0   949227.6  7936.0 7742.8 768.0  687.5     851   28.259   0      0.000   4      0.005   28.264
 0.0   389120.0  0.0   389120.0 4495360.0 3825664.0 2867200.0   949227.6  7936.0 7742.8 768.0  687.5     851   28.259   0      0.000   4      0.005   28.264
 0.0   370688.0  0.0   370688.0 4513792.0 24576.0  2867200.0   953274.5  7936.0 7742.8 768.0  687.5     852   28.298   0      0.000   4      0.005   28.303
 0.0   370688.0  0.0   370688.0 4513792.0 708608.0 2867200.0   953274.5  7936.0 7742.8 768.0  687.5     852   28.298   0      0.000   4      0.005   28.303
 0.0   370688.0  0.0   370688.0 4513792.0 1413120.0 2867200.0   953274.5  7936.0 7742.8 768.0  687.5     852   28.298   0      0.000   4      0.005   28.303
 0.0   370688.0  0.0   370688.0 4513792.0 2168832.0 2867200.0   953274.5  7936.0 7742.8 768.0  687.5     852   28.298   0      0.000   4      0.005   28.303
 0.0   370688.0  0.0   370688.0 4513792.0 2867200.0 2867200.0   953274.5  7936.0 7742.8 768.0  687.5     852   28.298   0      0.000   4      0.005   28.303
 0.0   370688.0  0.0   370688.0 4513792.0 3461120.0 2867200.0   953274.5  7936.0 7742.8 768.0  687.5     852   28.298   0      0.000   4      0.005   28.303
 0.0   370688.0  0.0   370688.0 4513792.0 4155392.0 2867200.0   953274.5  7936.0 7742.8 768.0  687.5     852   28.298   0      0.000   4      0.005   28.303
 0.0   370688.0  0.0   370688.0 4513792.0 315392.0 2867200.0   955841.3  7936.0 7742.8 768.0  687.5     853   28.338   0      0.000   4      0.005   28.342
 0.0   370688.0  0.0   370688.0 4513792.0 1071104.0 2867200.0   955841.3  7936.0 7742.8 768.0  687.5     853   28.338   0      0.000   4      0.005   28.342
 0.0   370688.0  0.0   370688.0 4513792.0 1847296.0 2867200.0   955841.3  7936.0 7742.8 768.0  687.5     853   28.338   0      0.000   4      0.005   28.342
 0.0   370688.0  0.0   370688.0 4513792.0 2570240.0 2867200.0   955841.3  7936.0 7742.8 768.0  687.5     853   28.338   0      0.000   4      0.005   28.342
 0.0   370688.0  0.0   370688.0 4513792.0 3303424.0 2867200.0   955841.3  7936.0 7742.8 768.0  687.5     853   28.338   0      0.000   4      0.005   28.342
 0.0   370688.0  0.0   370688.0 4513792.0 4059136.0 2867200.0   955841.3  7936.0 7742.8 768.0  687.5     853   28.338   0      0.000   4      0.005   28.342
 0.0   382976.0  0.0   382976.0 4501504.0 182272.0 2867200.0   957260.3  7936.0 7742.8 768.0  687.5     854   28.381   0      0.000   4      0.005   28.386
 0.0   382976.0  0.0   382976.0 4501504.0 870400.0 2867200.0   957260.3  7936.0 7742.8 768.0  687.5     854   28.381   0      0.000   4      0.005   28.386
 0.0   382976.0  0.0   382976.0 4501504.0 1554432.0 2867200.0   957260.3  7936.0 7742.8 768.0  687.5     854   28.381   0      0.000   4      0.005   28.386
 0.0   382976.0  0.0   382976.0 4501504.0 2260992.0 2867200.0   957260.3  7936.0 7742.8 768.0  687.5     854   28.381   0      0.000   4      0.005   28.386
 0.0   382976.0  0.0   382976.0 4501504.0 2973696.0 2867200.0   957260.3  7936.0 7742.8 768.0  687.5     854   28.381   0      0.000   4      0.005   28.386
 0.0   382976.0  0.0   382976.0 4501504.0 3731456.0 2867200.0   957260.3  7936.0 7742.8 768.0  687.5     854   28.381   0      0.000   4      0.005   28.386
 ```

 &ensp;&ensp;&ensp;&ensp;可以看到在较短的时间内（时间较短所有没有年轻代向老年代的晋升），新生代不断的增加，Eden不断的增加新对象，发生Young GC后，对象复制到S1中，S1不断增加，行为符合预期

 &ensp;&ensp;&ensp;&ensp;下面是相关的线程信息：

 ```sh
 ❯ .\jstack.exe 24172
2020-10-21 16:22:15
Full thread dump Java HotSpot(TM) 64-Bit Server VM (11.0.8+10-LTS mixed mode):

Threads class SMR info:
_java_thread_list=0x00000148b2840a10, length=11, elements={
0x0000014884865800, 0x00000148b244c000, 0x00000148b244d800, 0x00000148b24e3000,
0x00000148b24e4000, 0x00000148b24e6000, 0x00000148b24ed000, 0x00000148b2501000,
0x00000148b268d800, 0x00000148b2806800, 0x00000148b2807000
}

"main" #1 prio=5 os_prio=0 cpu=542625.00ms elapsed=581.92s tid=0x0000014884865800 nid=0x4f24 runnable  [0x000000316b3fe000]
   java.lang.Thread.State: RUNNABLE
        at com.company.GCLogAnalysis.generateGarbage(GCLogAnalysis.java:69)
        at com.company.GCLogAnalysis.main(GCLogAnalysis.java:32)

"Reference Handler" #2 daemon prio=10 os_prio=2 cpu=15.63ms elapsed=581.88s tid=0x00000148b244c000 nid=0x5850 waiting on condition  [0x000000316bafe000]
   java.lang.Thread.State: RUNNABLE
        at java.lang.ref.Reference.waitForReferencePendingList(java.base@11.0.8/Native Method)
        at java.lang.ref.Reference.processPendingReferences(java.base@11.0.8/Reference.java:241)
        at java.lang.ref.Reference$ReferenceHandler.run(java.base@11.0.8/Reference.java:213)

"Finalizer" #3 daemon prio=8 os_prio=1 cpu=0.00ms elapsed=581.88s tid=0x00000148b244d800 nid=0x4550 in Object.wait()  [0x000000316bbff000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(java.base@11.0.8/Native Method)
        - waiting on <0x000000060ea7b828> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(java.base@11.0.8/ReferenceQueue.java:155)
        - waiting to re-lock in wait() <0x000000060ea7b828> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(java.base@11.0.8/ReferenceQueue.java:176)
        at java.lang.ref.Finalizer$FinalizerThread.run(java.base@11.0.8/Finalizer.java:170)

"Signal Dispatcher" #4 daemon prio=9 os_prio=2 cpu=0.00ms elapsed=581.85s tid=0x00000148b24e3000 nid=0x3858 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Attach Listener" #5 daemon prio=5 os_prio=2 cpu=31.25ms elapsed=581.85s tid=0x00000148b24e4000 nid=0x5328 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #6 daemon prio=9 os_prio=2 cpu=156.25ms elapsed=581.85s tid=0x00000148b24e6000 nid=0x44d8 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"C1 CompilerThread0" #9 daemon prio=9 os_prio=2 cpu=156.25ms elapsed=581.85s tid=0x00000148b24ed000 nid=0x59dc waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

"Sweeper thread" #10 daemon prio=9 os_prio=2 cpu=0.00ms elapsed=581.85s tid=0x00000148b2501000 nid=0x504c runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Common-Cleaner" #11 daemon prio=8 os_prio=1 cpu=0.00ms elapsed=581.81s tid=0x00000148b268d800 nid=0x58e0 in Object.wait()  [0x000000316c1ff000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
        at java.lang.Object.wait(java.base@11.0.8/Native Method)
        - waiting on <0x000000060ea768c8> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(java.base@11.0.8/ReferenceQueue.java:155)
        - waiting to re-lock in wait() <0x000000060ea768c8> (a java.lang.ref.ReferenceQueue$Lock)
        at jdk.internal.ref.CleanerImpl.run(java.base@11.0.8/CleanerImpl.java:148)
        at java.lang.Thread.run(java.base@11.0.8/Thread.java:834)
        at jdk.internal.misc.InnocuousThread.run(java.base@11.0.8/InnocuousThread.java:134)

"Monitor Ctrl-Break" #12 daemon prio=5 os_prio=0 cpu=15.63ms elapsed=581.74s tid=0x00000148b2806800 nid=0x46c4 runnable  [0x000000316c3fe000]
   java.lang.Thread.State: RUNNABLE
        at java.net.SocketInputStream.socketRead0(java.base@11.0.8/Native Method)
        at java.net.SocketInputStream.socketRead(java.base@11.0.8/SocketInputStream.java:115)
        at java.net.SocketInputStream.read(java.base@11.0.8/SocketInputStream.java:168)
        at java.net.SocketInputStream.read(java.base@11.0.8/SocketInputStream.java:140)
        at sun.nio.cs.StreamDecoder.readBytes(java.base@11.0.8/StreamDecoder.java:284)
        at sun.nio.cs.StreamDecoder.implRead(java.base@11.0.8/StreamDecoder.java:326)
        at sun.nio.cs.StreamDecoder.read(java.base@11.0.8/StreamDecoder.java:178)
        - locked <0x000000060ea7a9d0> (a java.io.InputStreamReader)
        at java.io.InputStreamReader.read(java.base@11.0.8/InputStreamReader.java:185)
        at java.io.BufferedReader.fill(java.base@11.0.8/BufferedReader.java:161)
        at java.io.BufferedReader.readLine(java.base@11.0.8/BufferedReader.java:326)
        - locked <0x000000060ea7a9d0> (a java.io.InputStreamReader)
        at java.io.BufferedReader.readLine(java.base@11.0.8/BufferedReader.java:392)
        at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)

"Service Thread" #13 daemon prio=9 os_prio=0 cpu=0.00ms elapsed=581.74s tid=0x00000148b2807000 nid=0x50f4 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"VM Thread" os_prio=2 cpu=1234.38ms elapsed=581.88s tid=0x00000148b244a800 nid=0x3a68 runnable

"GC Thread#0" os_prio=2 cpu=29218.75ms elapsed=581.92s tid=0x000001488487b800 nid=0x33bc runnable

"GC Thread#1" os_prio=2 cpu=31515.63ms elapsed=581.70s tid=0x00000148b2840000 nid=0x4e08 runnable

"GC Thread#2" os_prio=2 cpu=29718.75ms elapsed=581.70s tid=0x00000148b2aec000 nid=0x5134 runnable

"GC Thread#3" os_prio=2 cpu=30156.25ms elapsed=581.70s tid=0x00000148b2aed000 nid=0x4d28 runnable

"GC Thread#4" os_prio=2 cpu=31406.25ms elapsed=581.70s tid=0x00000148b2aed800 nid=0x5360 runnable

"GC Thread#5" os_prio=2 cpu=31062.50ms elapsed=581.70s tid=0x00000148b2aee800 nid=0x31f8 runnable

"GC Thread#6" os_prio=2 cpu=29984.38ms elapsed=581.70s tid=0x00000148b2aef800 nid=0x4bcc runnable

"GC Thread#7" os_prio=2 cpu=29953.13ms elapsed=581.70s tid=0x00000148b2af2800 nid=0x54ec runnable

"G1 Main Marker" os_prio=2 cpu=0.00ms elapsed=581.92s tid=0x00000148848f8800 nid=0x1a30 runnable

"G1 Conc#0" os_prio=2 cpu=62.50ms elapsed=581.92s tid=0x00000148848fa800 nid=0x2f8c runnable

"G1 Conc#1" os_prio=2 cpu=62.50ms elapsed=552.07s tid=0x00000148b3663000 nid=0x5690 runnable

"G1 Refine#0" os_prio=2 cpu=0.00ms elapsed=581.90s tid=0x00000148b1aae000 nid=0x4ba0 runnable

"G1 Young RemSet Sampling" os_prio=2 cpu=453.13ms elapsed=581.90s tid=0x00000148b1aaf000 nid=0x2b78 runnable
"VM Periodic Task Thread" os_prio=2 cpu=109.38ms elapsed=581.74s tid=0x00000148b2809000 nid=0x4f10 waiting on condition

JNI global refs: 16, weak refs: 0
```

&ensp;&ensp;&ensp;&ensp;可以看到有主线程、Java相关一些配置线程、G1 GC相关的线程。在所有线程中涉及变量：counter的并发修改，应该使用普通的变量就行，可以不使用原子类操作。

&ensp;&ensp;&ensp;&ensp;下面是堆的相关信息：可以看到其中double,int,byte占据了前三的位置，符合程序预期。

```sh
❯ .\jmap.exe -histo 24172
 num     #instances         #bytes  class name (module)
-------------------------------------------------------
   1:          6574     2697801648  [D (java.base@11.0.8)
   2:         12418     1579103664  [I (java.base@11.0.8)
   3:         80700     1227504048  [B (java.base@11.0.8)
   4:         28915        1387920  java.nio.HeapCharBuffer (java.base@11.0.8)
   5:         28078         673872  java.lang.String (java.base@11.0.8)
   6:           142         159448  [C (java.base@11.0.8)
   7:          1112         135768  java.lang.Class (java.base@11.0.8)
   8:          3574         114368  java.util.HashMap$Node (java.base@11.0.8)
   9:          1429         114168  [Ljava.lang.Object; (java.base@11.0.8)
  10:          3609          86616  java.lang.StringBuilder (java.base@11.0.8)
  11:           384          53432  [Ljava.util.HashMap$Node; (java.base@11.0.8)
  12:          1300          41600  java.util.concurrent.ConcurrentHashMap$Node (java.base@11.0.8)
  13:           624          24960  java.util.LinkedHashMap$Entry (java.base@11.0.8)
  14:           399          19152  java.util.HashMap (java.base@11.0.8)
  15:            50          17888  [Ljava.util.concurrent.ConcurrentHashMap$Node; (java.base@11.0.8)
  16:           293          14824  [Ljava.lang.String; (java.base@11.0.8)
  17:           303          14544  java.lang.invoke.MemberName (java.base@11.0.8)
  18:           399          12072  [Ljava.lang.Class; (java.base@11.0.8)
  19:           154          10472  [Ljava.lang.ref.SoftReference; (java.base@11.0.8)
  20:           397           9528  java.lang.module.ModuleDescriptor$Exports (java.base@11.0.8)
  21:            95           8360  java.lang.reflect.Method (java.base@11.0.8)
  22:           260           8320  java.lang.invoke.LambdaForm$Name (java.base@11.0.8)
  23:           207           8280  java.lang.invoke.MethodType (java.base@11.0.8)
  24:           211           6752  java.lang.invoke.MethodType$ConcurrentWeakInternSet$WeakEntry (java.base@11.0.8)
  25:           264           6336  java.util.ImmutableCollections$Set12 (java.base@11.0.8)
  26:           188           6016  java.lang.module.ModuleDescriptor$Requires (java.base@11.0.8)
  27:           250           6000  java.lang.invoke.ResolvedMethodName (java.base@11.0.8)
  28:            72           5760  java.net.URI (java.base@11.0.8)
  29:            92           5152  java.lang.invoke.MethodTypeForm (java.base@11.0.8)
  30:            72           4608  java.lang.module.ModuleDescriptor (java.base@11.0.8)
  31:            67           4288  java.util.concurrent.ConcurrentHashMap (java.base@11.0.8)
  32:           107           4280  java.lang.ref.SoftReference (java.base@11.0.8)
  33:           262           4192  java.lang.Integer (java.base@11.0.8)
  34:           260           4160  java.util.HashSet (java.base@11.0.8)
  35:           256           4096  java.lang.Byte (java.base@11.0.8)
  36:            64           4096  java.net.URL (java.base@11.0.8)
  37:            46           4072  [Ljava.lang.invoke.MethodHandle; (java.base@11.0.8)
  38:           169           4056  java.util.ImmutableCollections$SetN (java.base@11.0.8)
  39:            72           4032  jdk.internal.module.ModuleReferenceImpl (java.base@11.0.8)
  40:            68           3808  java.lang.Module (java.base@11.0.8)
  41:             9           3384  java.lang.Thread (java.base@11.0.8)
  42:            68           3264  [Ljava.lang.invoke.LambdaForm$Name; (java.base@11.0.8)
  43:           101           3232  java.lang.invoke.LambdaForm$NamedFunction (java.base@11.0.8)
  44:            63           3024  java.lang.invoke.LambdaForm (java.base@11.0.8)
  45:            32           2560  java.lang.reflect.Constructor (java.base@11.0.8)
  46:            50           2400  sun.util.locale.LocaleObjectCache$CacheEntry (java.base@11.0.8)
  47:            36           2304  java.lang.Class$ReflectionData (java.base@11.0.8)
  48:            66           2112  java.lang.invoke.LambdaForm$Kind (java.base@11.0.8)
  49:           132           2112  java.util.Collections$UnmodifiableSet (java.base@11.0.8)
  50:            77           1848  java.util.ImmutableCollections$List12 (java.base@11.0.8)
```

&ensp;&ensp;&ensp;&ensp;下面是visualVM的分析图，在图中有个注意的点是，老年代有一个回归下跌，这里因为是代码中是缓存一定数量，后面新生成的对象会替换原来对象的位置，原来对象就没有被引用了，就要被回收。它要到一定容量才进行回收，我猜测是和G1的回收阈值有关，但目前对G1还没深入了解，暂时就看出这么多。

 ![](./visualVM.png)

## 一.Java语义概览（需要掌握程度：了解）
### 知识概览
&ensp;&ensp;&ensp;&ensp;Java是一种面向对象、静态类型、编译运行，有VM/GC和运行时的、跨平台的高级语言

&ensp;&ensp;&ensp;&ensp;和一些看到的专栏描述有点冲突，专栏《Java核心面试知识点》关于Java是解释执行还是编译执行有不同的解读：

&ensp;&ensp;&ensp;&ensp;代码首先编译成字节码，jvm在编译成机器码后执行。但动态编译器（JIT）会将热点代码编译成机器码，这种属于编译执行。则Java有解释执行，也有编译执行。

## 二.字节码（需要掌握程度：了解）
### 知识概览
&ensp;&ensp;&ensp;&ensp;这个知识点好像在工作中用的不多，但了解这些知识点，在未来如果在底层方面遇到问题，起码有方向和思路。

- 四种指令类型
- 基于栈的计算机器
- 方法调用的指令

### 相关扩展
&ensp;&ensp;&ensp;&ensp;下面是助教提到的一些应用：

- 现有的开源应用有asm,cglib, aop的代理，  监控的agent实现，都需要动态修改字节码

## 3.类加载器（需要掌握程度：熟练）
### 知识概览
&ensp;&ensp;&ensp;&ensp;该部分知识在工作中有实际应用，掌握其原理，助于在工作中正确使用

- 类的7个生命周期（步骤）：加载、链接（效验、准备、解析）、初始化、使用、卸载
- 类的8个加载时机：main、new、遭到调用静态方法的类、遭到访问静态字段的类、子类触发父类、default、发射、MethodHandle
- 不会触发类初始化：6种
- 三类加载器：启动类加载器、扩展类加载器、应用类加载器
- 加载器特点：双亲委托、负责依赖、缓存加载
- 显示当前Classloader加载了哪些Jar
- 自定义Classloader
- 添加引用类的几种方式

#### 加载重名冲突包

#### 类加载的一些技巧

## 4.Java内存模型（需要掌握程度：精通）
### 知识概览
&ensp;&ensp;&ensp;&ensp;这部分知识应该是基础，涉及到后面的GC，多线程编程、JVM调优等重要知识点

- JVM内存整体结构
    - 进程
    - 栈、堆、非堆、JVM自身
        - 堆：
            - 年轻代
                - 新生代
                - S0
                - S1
            - 老年代
        - 非堆
            - 元数据区：常量池方法区
    - 线程栈
    - 帧

- CPU与内存行为
    - CPU乱序执行
    - volatile关键字
    - 原子性操作
    - 内存屏障

## 5.JVM启动参数（需要掌握程度：熟练）
&ensp;&ensp;&ensp;&ensp;介绍参数相关约定，这块也相对重要，JVM调优之类的需要

- -：标准参数
- -D：设置系统属性
- -X：非标准参数
- -XX：非稳定参数，控制JVM行为
- -XX：+-Flags：+-是对布尔值进行开关
- -XX：key=value 指定某个选项的值

- JVM启动参数
    - 1.系统属性参数
    - 2.运行模式参数:server/client/Xint/Xcomp/Xmixed
    - 3.堆内存设置参数:Xmx/Xms/Xmn/Xx/XX/Xss
    - 4.GC设置参数:-XX:+UseG1GC/-XX:+UseConcMarkSweepGC/-XX:+UseSerialGC/-XX:+UseParallelGC/-XX:+UnlockExperimentalVMOptions -XX:+UseZGC/-XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC
    - 5.分析诊断参数
    - 6.JavaAgent参数

### 各个JVM版本的默认GC
1.8之前是并行GC，11开始是G1 GC

## 6.JDK命令行工具(需要掌握程度：熟练)
- 常用命令行工具：jps,jinfo,jstat,jmap,jstack,jcmd,jrunscript,jjs
- 一个神奇的计算公式：64位*线程数*系数13 / 10
- 大部分都是可以本地和远程的

### 命令详解与尝试
*一些小提示：使用命令的时候，直接一个 -help 就能对命令用途和用法有个大概的了解*

#### [jps](https://docs.oracle.com/en/java/javase/13/docs/specs/man/jps.html)
命令用途：查看当前系统中的Java进程列表

常用的命令有下面两种：

```bash
# 查看当前系统中Java进程，显示进程号和名称
jps

# 查看当前系统中Java进程，显示进程号和名称，还有更相信的启动信息
jps -lvm
```

#### [jinfo](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jinfo.html)
命令用途：查看指定Java进程的详细信息。能看到当前系统的信息，如显示win10；java路径；运行程序的用户；Java版本信息；VM信息

常用命令示例：

```bash
jinfo <pid>
```

#### [jstack](https://docs.oracle.com/javase/7/docs/technotes/tools/share/jstack.html)
命令用途：打印Java线程中的信息，类名，线程名（线程尽量要有一个标识名称，便于调试查看）。常用语查看当前进程是否有死锁

下面是一个死锁的程序示例代码：

```java
public class DeadLockSample extends Thread {
    private String first;
    private String second;

    public DeadLockSample(String name, String first, String second) {
        super(name);
        this.first = first;
        this.second = second;
    }

    @Override
    public void run() {
        synchronized (first) {
            System.out.println(this.getName() + " get lock: " + first);
            try {
                Thread.sleep(1000);
                synchronized (second) {
                    System.out.println(this.getName() + " get lock: " + second);
                }
            } catch (InterruptedException e) {

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String lockA = "LockA";
        String lockB = "LockB";

        DeadLockSample t1 = new DeadLockSample("Thread1", lockA, lockB);
        DeadLockSample t2 = new DeadLockSample("Thread2", lockB, lockA);

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
```

使用就stack查看进程就可以发现在最后打印了死锁信息

```bash
Found one Java-level deadlock:
=============================
"Thread1":
  waiting to lock monitor 0x000002a2e47ecb80 (object 0x0000000621291fe0, a java.lang.String),
  which is held by "Thread2"
"Thread2":
  waiting to lock monitor 0x000002a2e47ee980 (object 0x0000000621291fb0, a java.lang.String),
  which is held by "Thread1"

Java stack information for the threads listed above:
===================================================
"Thread1":
        at com.company.DeadLockSample.run(DeadLockSample.java:20)
        - waiting to lock <0x0000000621291fe0> (a java.lang.String)
        - locked <0x0000000621291fb0> (a java.lang.String)
"Thread2":
        at com.company.DeadLockSample.run(DeadLockSample.java:20)
        - waiting to lock <0x0000000621291fb0> (a java.lang.String)
        - locked <0x0000000621291fe0> (a java.lang.String)

Found 1 deadlock.
```

使用示例：

```
jstack pid
```

#### [jstat](https://docs.oracle.com/en/java/javase/14/docs/specs/man/jstat.html)
 命令用途：用于查看虚拟机中的性能统计信息，非常有用，

 使用示例：

 ```bash
 jstat -gc pid 100 100
 jstat -class pid 100 100
 ```

#### [jmap](https://docs.oracle.com/javase/7/docs/technotes/tools/share/jmap.html)
命令用途：查看堆信息

使用示例：

```bash
jmap -histo pid
```

#### 【jcmd】(https://docs.oracle.com/en/java/javase/13/docs/specs/man/jcmd.html)
命令用途：这个命令有点类似基础命令，上面那些的命令可以看做在这个命令之上的封装使用

## 7.JDK内置图形化工具（需要掌握程度：了解）

- jconsole:显示线程、内存、CPU等等
- jvisualvm：
- VisualGC：IDEA插件
- jmc

## 8.GC的背景与一般原理（需要掌握程度：熟练）
Java是自动内存管理，在程序中无法人工管理，在生成新对象时需要消耗内存，如果不对无用的对象进行回收，就会导致内存占满，无法为新对象分配内存，程序崩溃。

什么是无用对象：利用可达性分析，不可达的对象就是无用对象，可进行回收

可以作为GC Roots的对象标准：
- 当前正在执行的方法里的局部变量和输入参数
- 活动线程
- 所有类的静态字段
- JNI引用

分代假设：分为年轻代代和老年代，年轻代中又分新生代、S0、S1
- 新对象生成时在新生代中；大对象之间放到老年代中
- 新生代用满了，触发年轻代 Minor GC，将新生代和S0中存活的对象放到S1中，并标记其存活次数，最后S0和S1互换
- 当存活次数达到要求（这个可以进行设置：-XX:+MaxTenuringThreshold），则这个对象就会晋升到老年代
- 当单个S去被占用50%（这个可以进行设置：-XX:TargetSurvivorRatio），较高存活次数的对象也晋升到老年代

Minor GC中需要注意的点：
- 老年代中引用了新生代中的对象：记录被老年代中引用的新生代中的对象，并将其作为GC Roots，复制后进行引用更新，技术名叫：卡表
- 为啥使用复制，而不是移动：大部分对象已经死亡，复制的数据少

回收算法：
- 标记-清除
- 标记-复制
- 标记-清除-整理

并行GC和CMS可以处理循环依赖，只扫描部分对象（只需扫描可达的对象），这部分可以并行，不影响业务运行，但在清楚和压缩阶段，必须停止业务代码（STW）

## 9.串行GC/并行GC（Serial GC / Parallel GC)（需要掌握程度：熟练）
### 串行 GC（Serial GC）/ParNewGC
配置：
- -XX:+UseSerialGC 配置串行 GC
- -XX:+USeParNewGC 配置ParNewGC

年轻代使用标记-复制算法，老年代使用标记-清除-整理算法

都是单线程，不能并行进行梳理，进行垃圾回收时触发STW，老式电脑容易卡死

适合几百MB堆内存的JVM，单核CPU：几百MB本身较小，回收也快，在单核中时间切片就只能一个线程运行

### 并行 GC（Parallel GC）
配置：
-XX：+UseParallelGC
-XX：+UseParallelOldGC
-XX：+UseParallelGC -XX:+UseParallelOldGC
-XX：ParallelGCThreads=N 来指定 GC 线程数， 其默认值为 CPU 核心数。

年轻代使用标记-复制算法，老年代使用标记-清除-整理算法

多线程，年轻代和老年代GC都会触发STW，只是串行GC的改进，利用GC多线程，加快GC速度

## 10.CMS GC / G1 GC（需要掌握程度：熟练）
### CMS GC
配置：
- -XX：+UseConcMarkSweepGC

年轻代使用并行STW标记-复制，老年代使用并发标记-清除

设计目标是避免在老年代GC出现长时间的卡顿，主要通过下面两种手段达成：
- 1.不对老年代进行整理，使用空闲列表来管理内存空间的回收
- 2.在标记-清除阶段的大部分工作和应用线程一起并发执行

CMS GC六阶段
- 1. Initial Mark（初始标记）：STW，标记GC Roots
- 2.Concurrent Mark（并发标记）：并行标记
- 3. Concurrent Preclean(并发预清理)：卡片标记
- 4.阶段 4: Final Remark（最终标记）：STW，最终标记存活对象
- 5: Concurrent Sweep（并发清除）：删除回收对象
- 6.Concurrent Reset（并发重置）：重置算法内部数据

CMS优缺点：
- 优点：利用GC分阶段并行降低了GC STW时间
- 缺点：老年代内存碎片问题，堆内存较大会造成不可预测的STW

### G1 GC
配置：
- -XX:+UseG1GC
- -XX:MaxGCPauseMillis=50
- -XX:G1NewSizePercent：初始年轻代占整个Java Heap的大小，默认值为5%
- -XX:G1MaxNewSizePercent：最大年轻代占整个Java Heap的大小，默认值为60%；
- -XX:G1HeapRegionSize：设置每个Region的大小，单位MB，需要为1，2，4，8，16，32中的某个值，默认是 堆内存的1/2000。如果这个值设置比较大，那么大对象就可以进入Region了
- -XX:ConcGCThreads：与Java应用一起执行的GC线程数量，默认是Java线程的1/4，减少这个参数的数值可能会 提升并行回收的效率，提高系统内部吞吐量。如果这个数值过低，参与回收垃圾的线程不足，也会导致并行回收机制耗时加长
- -XX:+InitiatingHeapOccupancyPercent（简称IHOP）：G1内部并行回收循环启动的阈值，默认为Java Heap的45%。这个可以理解为老年代使用大于等于45%的时候，JVM会启动垃圾回收。这个值非常重要，它决定了在什么 时间启动老年代的并行回收
- -XX:G1HeapWastePercent：G1停止回收的最小内存大小，默认是堆大小的5%。GC会收集所有的Region中的对 象，但是如果下降到了5%，就会停下来不再收集了。就是说，不必每次回收就把所有的垃圾都处理完，可以遗留 少量的下次处理，这样也降低了单次消耗的时间。
- -XX:G1MixedGCCountTarget：设置并行循环之后需要有多少个混合GC启动，默认值是8个。老年代Regions的回 收时间通常比年轻代的收集时间要长一些。所以如果混合收集器比较多，可以允许G1延长老年代的收集时间。
- -XX:+G1PrintRegionLivenessInfo：这个参数需要和 -XX:+UnlockDiagnosticVMOptions 配合启动，打印JVM的调试信息，每个Region里的对象存活信息
- -XX:G1ReservePercent：G1为了保留一些空间用于年代之间的提升，默认值是堆空间的10%。因为大量执行回收的地方在年轻代（存活时间较短），所以如果你的应用里面有比较大的堆内存空间、比较多的大对象存活，这里需要保留一些内存。
- -XX:+G1SummarizeRSetStats：这也是一个VM的调试信息。如果启用，会在VM退出的时候打印出RSets的详细总结信息。如果启用-XX:G1SummaryRSetStatsPeriod参数，就会阶段性地打印RSets信息
- -XX:+GCTimeRatio：这个参数就是计算花在Java应用线程上和花在GC线程上的时间比率，默认是9，跟新生代内存的分配比例一致。这个参数主要的目的是让用户可以控制花在应用上的时间，G1的计算公式是100/（1+GCTimeRatio）。这样如果参数设置为9，则最多10%的时间会花在GC工作上面。Parallel GC的默认值是99，表示1%的时间被用在GC上面，这是因为Parallel GC贯穿整个GC，而G1则根据Region来进行划分，不需要全局性扫描整个内存堆。
- -XX:+UseStringDeduplication：手动开启Java String对象的去重工作，这个是JDK8u20版本之后新增的参数，主要用于相同String避免重复申请内存，节约Region的使用。
- -XX:MaxGCPauseMills：预期G1每次执行GC操作的暂停时间，单位是毫秒，默认值是200毫秒，G1会尽量保证控制在这个范围内

G1的全称是Garbage-First，意为垃圾优先，哪一块的垃圾最多就优先清理它。

设计目标：将STW时间和分布，变成可预期且可配置的

G1 GC 处理步骤
- 1.年轻代模式转移暂停：年轻代空间满了以后，回收好的存活对象拷贝到存活区
- 2.并发标记：过程与CMS基本一样，构建每个堆块的存活状态，用于后面执行老年代区域的垃圾收集；当堆内存总体使用比例达到一定数值，就会触发并发标记（默认45%，设置参数：InitiatingHeapOccupancyPercent）
    - 阶段1：Initial Mark(初始标记)：标记所有从GC根对象直接可达的对象
    - 阶段 2: Root Region Scan(Root区扫描)：标记所有从“根区域”可达的存活对象；根区域：非空的区域，以及在标记过重不得不收集的区域
    - 阶段 3: Concurrent Mark(并发标记)：与CMS并发标记类似，遍历对象图，在一个特殊的位图中标记能访问到的对象
    - 阶段 4: Remark(再次标记)：SWT，标记所有在并发标记开始是未被标记的存活对象
    - 阶段 5: Cleanup(清理)：也需要短暂的STW，统计小堆块中所有存活的对象，并将小堆块进行排序，以提升GC效率，维护并发标记的内部状态；不含存活对象的小堆块直接回收
- 3.转移暂停（混合模式）：并发标记完成后，G1执行一次混合收集，包含年轻代和老年代

G1 GC注意事项
特别需要注意的是，某些情况下G1触发了Full GC，这时G1会退化使用Serial收集器来完成垃圾的清理工作，它仅仅使用单线程来完成GC工作，GC暂停时间将达到秒级别的。
- 1.并发模式失败：G1启动标记周期，但在Mix GC之前，老年代就被填满，这时候G1会放弃标记周期。
- 解决办法：增加堆大小，或者 调整周期（例如增加线程数-XX:ConcGCThreads等）

- 2.晋升失败：没有足够的内存供存活对象或晋升对象使用，由此触发了Full GC(to-space exhausted/to-space overflow）。 
- 解决办法：
    - a)增加 -XX:G1ReservePercent 选项的值（并相应增加总的堆大小）增加预留内存量。
    - b)通过减少 -XX:InitiatingHeapOccupancyPercent 提前启动标记周期。
    - c)也可以通过增加 -XX:ConcGCThreads 选项的值来增加并行标记线程的数目。

- 3.巨型对象分配失败:当巨型对象找不到合适的空间进行分配时，就会启动Full GC，来释放空间。
- 解决办法：增加内存或者增大-XX:G1HeapRegionSize

## 11.ZGC / Shenandoah GC（需要掌握程度：熟练ZGC）
### ZGC
配置
- -XX:+UnlockExperimentalVMOptions 
- -XX:+UseZGC 
- -Xmx16g

ZGC最主要的特点包括:
- 1. GC 最大停顿时间不超过 10ms
- 2. 堆内存支持范围广，小至几百 MB 的堆空间，大至4TB 的超大堆内存（JDK13升至16TB）
- 3. 与 G1 相比，应用吞吐量下降不超过15%
- 4. 当前只支持 Linux/x64 位平台，JDK15后支持MacOS和Windows系统

### ShennandoahGC
配置：
- -XX:+UnlockExperimentalVMOptions 
- -XX:+UseShenandoahGC 
- -Xmx16g

Shenandoah GC立项比ZGC更早，设计为GC线程与应用线程并发执行的方式，通过实现垃圾回收过程的并发处理，改善停顿时间，使得GC执行线程能够在业务处理线程运行过程中进行堆压缩、标记和整理，从而消除了绝大部分的暂停时间。
Shenandoah 团队对外宣称ShenandoahGC的暂停时间与堆大小无关，无论是200MB 还是 200 GB的堆内存，都可以保障具有很低的暂停时间（注意:并不像ZGC那样保证暂停时间在10ms以内）。

## GC 如何选择
选择正确的GC算法，唯一可行的方式就是去尝试，一般性的指导原则：
- 1. 如果系统考虑吞吐优先，CPU资源都用来最大程度处理业务，用Parallel GC；
- 2. 如果系统考虑低延迟有限，每次GC时间尽量短，用CMS GC；
- 3. 如果系统内存堆较大，同时希望整体来看平均GC时间可控，使用G1 GC。

对于内存大小的考量：
- 1. 一般4G以上，算是比较大，用G1的性价比较高。
- 2. 一般超过8G，比如16G-64G内存，非常推荐使用G1 GC。

## GC总结
到目前为止，我们一共了解了Java目前支持的所有GC算法，一共有7类:
- 1. 串行GC（Serial GC）: 单线程执行，应用需要暂停；
- 2. 并行GC（ParNew、Parallel Scavenge、Parallel Old）: 多线程并行地执行垃圾回收，关注与高吞吐；
- 3. CMS（Concurrent Mark-Sweep）: 多线程并发标记和清除，关注与降低延迟；
- 4. G1（G First）: 通过划分多个内存区域做增量整理和回收，进一步降低延迟；
- 5. ZGC（Z Garbage Collector）: 通过着色指针和读屏障，实现几乎全部的并发执行，几毫秒级别的延迟，线性可扩展；
- 6. Epsilon: 实验性的GC，供性能分析使用；
- 7. Shenandoah: G1的改进版本，跟ZGC类似。

可以看出GC算法和实现的演进路线:
1. 串行 -> 并行: 重复利用多核CPU的优势，大幅降低GC暂停时间，提升吞吐量。
2. 并行 -> 并发: 不只开多个GC线程并行回收，还将GC操作拆分为多个步骤，让很多繁重的任务和应用线程 一起并发执行，减少了单次GC暂停持续的时间，这能有效降低业务系统的延迟。
3. CMS -> G1: G1可以说是在CMS基础上进行迭代和优化开发出来的，划分为多个小堆块进行增量回收，这 样就更进一步地降低了单次GC暂停的时间
4. G1 -> ZGC: ZGC号称无停顿垃圾收集器，这又是一次极大的改进。ZGC和G1有一些相似的地方，但是底层 的算法和思想又有了全新的突破。

## 其他
助教-kris的平常JVM参数记录

```sh
-Xms4096m  //初始堆大小

-Xmx4096m  //最大堆大小

-Xmn1536m //新生代大小 eden + from + to

-Xss512K  //线程大小

-XX:NewRatio=2  //新生代和老年代的比例

-XX:MaxPermSize=64m   //持久代最大值

-XX:PermSize=16m  //持久代初始值

-XX:SurvivorRatio=8  // eden 区和survivor区的比例

-verbose:gc  

-Xloggc:gc.log  //输出gc日志文件

-XX:+UseGCLogFileRotation  //使用log文件循环输出

-XX:NumberOfGCLogFiles=1  //循环输出文件数量

-XX:GCLogFileSize=8k //日志文件大小限制

-XX:+PrintGCDateStamps //gc日志打印时间

-XX:+PrintTenuringDistribution            //查看每次minor GC后新的存活周期的阈值

-XX:+PrintGCDetails //输出gc明细

-XX:+PrintGCApplicationStoppedTime //输出gc造成应用停顿的时间

-XX:+PrintReferenceGC //输出堆内对象引用收集时间

-XX:+PrintHeapAtGC //输出gc前后堆占用情况



-XX:+UseParallelGC  //年轻代并行GC，标记-清除

-XX:+UseParallelOldGC //老年代并行GC，标记-清除

-XX:ParallelGCThreads=23 //并行GC线程数， cpu<=8?cpu:5*cpu/8+3

-XX:+UseAdaptiveSizePolicy //默认，自动调整年轻代各区大小及晋升年龄

-XX:MaxGCPauseMillis=15 //每次GC最大停顿时间,单位为毫秒

-XX:+UseParNewGC  //Serial多线程版

-XX:+UseConcMarkSweepGC  //CMS old gc

-XX:+UseCMSCompactAtFullCollection  //FullGC后进行内存碎片整理压缩

-XX:CMSFullGCsBeforeCompaction=n  //n次FullGC后执行内存整理

-XX:+CMSParallelRemarkEnabled  //启用并行重新标记,只适用ParNewGC

-XX:CMSInitiatingOccupancyFraction=80             //cms作为垃圾回收是，回收比例80%

-XX:ParallelGCThreads=23 //并行GC线程数，cpu<=8?cpu:5*cpu/8+3

-XX:-UseSerialGC //默认不启用，client使用时启用

-XX:+UseG1GC //启用G1收集器

-XX:-UseAdaptiveSizePolicy //默认，不自动调整各区大小及晋升年龄

-XX:PretenureSizeThreshold=2097152 //直接晋升到老年代的对象大小

-XX:MaxTenuringThreshold=15(default) //晋升到老年代的对象年龄，PSGen无效



-XX:-DisableExplicitGC //禁止在运行期显式地调用?System.gc() 

-XX:+HeapDumpOnOutOfMemoryError  //在OOM时输出堆内存快照

-XX:HeapDumpPath=./java_pid<pid>.hprof  //堆内存快照的存储路径 

-XX:+CMSScavengeBeforeRemark //执行CMS重新标记之前，尝试执行一此MinorGC

-XX:+CMSPermGenSweepingEnabled //开启永久代的并发垃圾收集
```

秦金卫老师提到的注意点：

```
-XX:ParallelGCThreads=23 //并行GC线程数，cpu<=8?cpu:5*cpu/8+3;这个很有意思，8core以下是CPU核心数，超过8core比较多的话，只用一大半。

32核心时， 5*cpu/8+3 = 23
64核心时， 5*cpu/8+3 = 43

前天看到群里有同学是13，那反过来推算 16核心
```

1、-XX:+HeapDumpOnOutOfMemoryError参数，让虚拟机在出现内存溢出时Dump出当前的内存堆转储快照，这个参数建议在生产环境开启吗？还是在出现问题后，再添加上？
2、网上看到，“JDK7的hotspot就已经把原本放在永久代的字符串常量池、静态变量移出，到JDK8将永久代改为Metaspace，并将JDK7中永久代剩余内容移到Metaspace”，这意味着字符串常量池和静态变量并不在Metaspace，这对吗？若不在，它们是存放在哪里的？

@助教-铁锚
1. 生产环境可以配置 HeapDumpOnOutOfMemoryError, 原因是既然堆内存溢出了，一般会停止服务，所以Dump对系统无影响。
2. 常量池就在 Meta区， 静态变量本身的存储区域在 非堆 中，当然也可能是指针，指向其他区域。 Meta区这些概念是Hotspot具体实现的，学习时注意规范与具体实现的区别， JVM规范并没有规定说你必须实现哪个区。


## 参考资料记录
- Java训练营课程及相关课件
- [第23讲 | 请介绍类加载过程，什么是双亲委派模型？](https://time.geekbang.org/column/article/9946)
- [Class Loaders in Java](https://www.baeldung.com/java-classloaders)
- [Java8 JVM 参数官方说明文档](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html)
- [class反编译：JD-GUI 1.6.6](https://github.com/java-decompiler/jd-gui/releases/tag/v1.6.6)
- [6 The Parallel Collector](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/parallel.html#default_heap_size)
