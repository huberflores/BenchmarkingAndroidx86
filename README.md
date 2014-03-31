Benchmarking Android-x86
======================

How to compile Android AOSP for x86? or at least a similar Android-based system.

The idea of benchmarking an Amazon instance with Dalvik x86 is to find out the level of concurrency-based QoS that can be achieved in computational offloading operations. 

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
