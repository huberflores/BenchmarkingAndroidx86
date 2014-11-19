Benchmarking Android-x86 through direct Dalvik-x86 invocation
======================

How to compile Android AOSP for x86? or at least a similar Android-based system.

The idea of benchmarking an Amazon instance with Dalvik x86 is to find out the level of concurrency-based QoS that can be achieved in computational offloading operations. 


Concept
==========


![alt text](https://raw.githubusercontent.com/huberflores/BenchmarkingAndroidx86/master/CodeOffloadingMultiTenancy.png "Multi-tenancy Code Offloading")



Dalvik x86 can be created by following these scripts, we encourage to use AOSP code [script 1](https://gist.github.com/huberflores/4687766), [script 2](https://gist.github.com/huberflores/4714824)

Alternatively, we can provide you with an Amazon EC2 Image (AMI) with a running Dalvik x86
Please contact at huber AT ut DOT ee for granting access


[(./rund.sh) - script wrapper](https://gist.github.com/huberflores/9444950) 


![alt text](https://raw.githubusercontent.com/huberflores/BenchmarkingAndroidx86/master/Dalvik-x86.png "Android x86, Dalvik interface")


Dalvik bytecode execution
=========================


![alt text](https://raw.githubusercontent.com/huberflores/BenchmarkingAndroidx86/master/Dalvik-x86-example.png "Android x86, Dalvik bytecode example")


Notice that foo.jar can be foo.apk


Before asking for help, check more detail instructions here [Dalvik bytecode](https://gist.github.com/huberflores/9886339)

Moreover, you can also follow discussion here [Discussion](http://stackoverflow.com/questions/22579661/is-it-possible-to-get-an-estimation-of-the-number-of-instructions-executed-by-da)


Installation
===========

Project are in mavenized for automatic compilation.


```xml
$ cd ./Simulator
````

```xml
$ mvn clean install
````

```xml
$ cd target/
````

```xml
$ java -jar MobileOffloadSimulator-0.0.1-SNAPSHOT.jar cs.mc.ut.ee.simulator.Controller
Remote result: 6822.2117842 (client 1)
Remote result: 5922.5678763 (client 2)
and so on...

````

```xml
$ cd ./Manager
````

```xml
$ mvn clean install
````

```xml
$ cd target/
````

```xml
$ java -jar CodeOffload-0.0.1-SNAPSHOT.jar
Starting server...
Handling a code offload request (client 1)
Handling a code offload request (client 2)
````




How to cite
===========
 
If you are using the tool for your research, please do not forget to cite. Thanks!

- Flores, Huber, and Satish Srirama. ["Mobile code offloading: should it be a local decision or global inference?"](http://dl.acm.org/citation.cfm?id=2465722), the 11th International Conference on Mobile Systems, Applications and Services (MobiSys 2013).


