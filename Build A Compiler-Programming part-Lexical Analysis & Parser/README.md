


<h1>PARSER</h1>

<h2> program </h2>

 - program -> stmts

 
<h2> stmts </h2>

 - stmts -> stmtstmts  

 - |    	null

<h2> stmt </h2>

 - stmt -> identifier=expr~stmts

 - |	type identifier~stmts

 - |	type identifier=expr~stmts

 - |	identifier=expr~stmts

 - |	if (expr){stmts}stmts

 - |	if (expr){stmts}else{stmts}stmts

 - |	while(expr){loop_stmts}stmts

 - |	def identifier(def_args){func_stmts}stmts

 - |	identifier(call_args)stmts

 - |	identifier=identifier(call_args)stmts

 - |    type identifier=identifier(call_args)stmts

 - |    <<identifier(call_args)stmts

 - |    <<expr~stmts

 - |    break~stmts

 - |    return expr~stmts

 

 <h2> def_args </h2>
 
 - def_args -> type identifier def_arg

 - |    null
 
 <h2> def_arg </h2>

 - def_arg -> , def_args

 - |    null
 
 <h2> call_args </h2>

 - call_args -> expr call_arg

 - |    null
 
 <h2> call_arg </h2>

 - call_arg -> ,call_args

 - |    null
 
 <h2> expr </h2>

 - expr -> int_type

 - |    double_type 

 - |    expr + or - expr2

 - |    expr2

 - |    identifier

 - |	null

 <h2> expr2 </h2>

 - expr2 -> expr * or / expr3

 - |    expr3

 <h2> expr3 </h2>

 - expr3 -> (expr)

 - |    expr

 <h2> identifier </h2>

 - identifier -> x,y,z.. variables belong to int or double
 
 <h2> type </h2>

 - type -> int | double

 <h2> int_type , double_type </h2>

 - int_type -> integer number like 2 , -4 ,  21

 - double_type -> double number like -3.1 , 0.32 , 21.002
 
 <h2> Test if any program qualifies the grammar </h2>
 
 <h3>Demo:</h3>

```c++
 
 def sub(int x,int y)
 {
    y=y-1~
    return (x-y)*2~
 }
 
 int x=5~
 int y=-8~
 
 while(x-2)
 {
    if (x+y/2) 
    {
        <<x-4*(y+1)~
        <<sub(x,x+2)~
    }
    if (x-10)
    {
        x=x+y~
        break~
    }
}
```



<h3>Test Demo:</h3>

```c++

program->stmts->def identifier(def_args){stmts}stmts

->def sub(int xdef_argdef_args){stmtstmts}
while(expr){stmts}stmts

->def sub(int x,int y){y=y-1~return expr~}
while(x-2){stmtstmts}null

->def sub(int x,int y){y=y-1~return (expr)*expr3}
while(x-2){if(expr){stmts}stmtstmts}

->def sub(int x,int y)
{y=y-1~return (identifier-identifier)*int_type~}
while(x-2){if(x+y/2){<<expr~stmts}if(expr){stmts}stmts}

->def sub(int x,int y)
{y=y-1~return (x-y)*2~}
while(x-2){if(x+y/2){<<x-4*(y+1)~<<identifier(call_args)~stmts}
if(x-10){identifier=expr~stmts}null}

->def sub(int x,int y)
{y=y-1~return (x-y)*2~}
while(x-2){if(x+y/2){<<x-4*(y+1)~<<sub(exprcall_arg)~null}
if(x-10){x=x+y~break~stmts}null}

->def sub(int x,int y)
{y=y-1~return (x-y)*2~}
while(x-2){if(x+y/2){<<x-4*(y+1)~<<sub(x,x+2null)~}
if(x-10){x=x+y~break~null}}

->def sub(int x,int y)
{
    y=y-1~return (x-y)*2~
}
while(x-2)
{
    if(x+y/2){<<x-4*(y+1)~<<sub(x,x+2)~} 
    if(x-10){x=x+y~break~}
}

```

<h4>Done!!!</h4>

