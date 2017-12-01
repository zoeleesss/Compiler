# Compiler	Chapter 1

#How to run compiler examples of LLVM & install LLVM v5.0 on MAC OSX 


Firstly, Let’s see how to install and configure LLVM (latest version) on your Mac.

.Step	1	download LLVM 

open your terminal, and copy code1.1 below to your terminal.
	
code1.1
`svn co http://llvm.org/svn/llvm-project/llvm/trunk llvm`

If everything goes smooth here, you’ll see a directory called llvm in your /usr/local/

.Step	2	download Clang 

open your terminal, and copy code1.2 below to your terminal.

code1.2
`cd /usr/local/llvm/tools`
`svn co http://llvm.org/svn/llvm-project/cfe/trunk clang`

.Step	3	download cmake
You can use this or find it yourself (latest version preferred)
https://cmake.org/files/v3.9/cmake-3.9.1-Darwin-x86_64.dmg
https://cmake.org/download/

.Step	4	configure PATH of cmake

open your terminal, and copy code1.4.1 below to your terminal.

code1.4.1
`vi ~/.bash_profile`	

now you’ll see you can edit a file, add this line: 
`export PATH=/Applications/CMake.app/Contents/bin:$PATH`

(then press `i` to insert/edit, and press `esc` button to exit edit mode and press `:` and press `wq` and press `enter`). Ok, now you have  successfully mastered the techniques of vi.

copy code1.4.2 below to your terminal. 
code1.4.2 
`source ~/.bash_profile`

to test if’s configured, use code1.4.3

code1.4.3
`echo $PATH`

if it prints content that contains `/Applications/CMake.app/Contents/bin` then it’s right.

.Step	5	configure LLVM with cmake

if you have done the first 4 steps mentioned above, you should be able to use `cmake` command in your terminal. 

open your terminal, and copy code1.5.1 below to your terminal.

code1.5.1
`cd /usr/local/llvm`
`mkdir build`
`cd build`


now you have entered a directory called ‘build’ you just created. Then u need to use cmake to configure your llvm. but  we need to use ‘ROOT’ authority to do that.

copy code1.5.3 below to your terminal.

code1.5.2
`sudo su`

then enter your password of the current user of Mac OS. NOTE: password cannot be seen. So make sure you enter it right.

copy code1.5.3 below to your terminal.

code1.5.3
`cmake ../`

you’ll see it loads very fast. less than a minute, it will load to 100%. Then you need to further configure it.

copy code1.5.4 below to your terminal.

code1.5.4
`make`

WELL… …you ‘ll see it starts to load from 1%, but unfortunately, this process is very low, several hours perhaps. So just take your time, you are making A Huge process right now.

After hours of waiting, it will be good 100% and consumes nearly 20 GB storage in my MacBookAir. (But if you mistakenly close the terminal while it’s loading, don’t be afraid, just enter `make` again, and it will continue.)

now, configure the PATH like the way we did it before.
  
Do it in your terminal with code 1.5.5

code 1.5.5
`vi ~/.bash_profile`
`export PATH=/usr/local/llvm/build/bin:$PATH`
`source ~/.bash_profile`

.Step	6	enjoy a little.

now you can use clang++ and llvm tools which can be really helpful. But if your program includes the header/.h files from llvm tools. You need to do things like this 

code 1.6.1
`clang++ -c main.cpp \`llvm-config --cflags --ldflags --libs -o ./main\``

to compile your file, otherwise it will throw errors. 
then you can simple enter `main` in your terminal to run main.cpp.
But i am not gonna say many things about clang++ . I will focus on how to use the compiler examples that LLVM has to offer us.


.Step	7	Try BrainF (BrainFuck actually) and Kaleidoscpoe compiler. 

BTW, other examples that LLVM contained can actually run the same way.



As for BrainF:

a. go to the BrainF folder:

code1.7.1
`cd /usr/local/llvm/build/examples/BrainF`

b. use makefile to correctly configure BrainF

code1.7.2
`make BrainF`

just wait for a while. And you’ll see a file created in the folder:
`/usr/local/llvm/build/bin`

code1.7.3 
`BrainF [+filename]`

then you can run this executable file by using code 1.7.3.


As for Kaleidoscope, it is really interesting and a very powerful language which supports if else statement, loop , function definition ,using function , define ,use and prints variables.	AND most importantly, it can even show you how LLVM processed this to an assemble language, which should be really helpful to those who want to know how LLVM works.

Enough for talking, here we go. 

a. go to the Kaleidoscope folder:

code1.7.4
`cd /usr/local/llvm/build/examples/Kaleidoscope`

b. use makefile to correctly configure BrainF

code1.7.5
`make Kaleidoscope`

just wait for a while (it will take 1 GB roughly). And you’ll see several files created in the folder.

then you can see many executable files relevant to Kaleidoscope like Kaleidoscope-Ch2.
For me, i’d suggest you to use Kaleidoscope-Ch07 which supports more features.

You can enter a simple program like fibonacci.
Okay, here comes the code:

code1.7.6
`def fib(x)`
`if x<3 then`
`1`
`else`
`fib(x-1)+fib(x-2)`
`;`
`fib(40);`


then you can see it prints some lines code of assemble language which refer to the original fibonacci program.
And you can see it also prints  the result of fib(40).
And if u are interested in how they did Kaleidoscope . Check it out here: 
llvm.org/docs/tutorial/Langlmpl01.html


If you have seen here, it means you have already known the basics of LLVM and how to configure and run things correctly. Due to the limited knowledge of mine, I may make some mistakes and if you find any, feel free to PULL REQUESTS on Github. If you still have problem that you may not understand, feel free to contact me:	1157395373@qq.com	ZoeLee

Thx for your reading, hope you could like this and support me by stars, thx again.
I will continue to update this repository. (I will show you how to create a simple assembler on your own with real programming code).














 
 


















	
