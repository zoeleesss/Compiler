
```cpp




`PARSER`

## program ##
 - program -> stmts

 
 ## stmts ##
 - stmts -> stmtstmts
 - |    null

## stmt ##
 - stmt -> identifier=expr~stmts
 - |    type identifier~stmts
 - |    type identifier=expr~stmts
 - |    identifier=expr~stmts
 - |    if (expr){stmts}stmts
 - |    if (expr){stmts}else{stmts}stmts
 - |    while(expr){loop_stmts}stmts
 - |    def identifier(def_args){func_stmts}stmts
 - |    identifier(call_args)stmts
 - |    identifier=identifier(call_args)stmts
 - |    type identifier=identifier(call_args)stmts
 - |    <<identifier(call_args)stmts
 - |    <<expr~stmts
 - |    break~stmts
 - |    return expr~stmts
 
## space ##
 - |	space

 ## def_args ##
 
 - def_args -> type identifier def_arg
 - |    null
 
 ## def_arg ##
 - def_arg -> , def_agrs
 - |    null
 
 ## call_args ##
 
 - call_args -> expr call_arg
 - |    null
 
 ## call_arg ##
 - call_agr -> ,call_args
 - |    null
 
 ## expr ##
 - expr -> int_type
 - |    double_type 
 - |    expr + or - expr2
 - |    expr2
 - |    identifier
 - |	null

 ## expr2 ##
 - expr2 -> expr * or / expr3
 - |    expr3
 ## expr3 ##
 - expr3 -> (expr)
 - |    expr
 ## identifier ##
 - identifier -> x,y,z 等int or double 型变量
 
 ## type ##
 - type -> int | double
 
 ## 测试以上文法的正确性 ##
 
 Demo:
 
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


Test Demo:

program->stmts->def identifier(def_args){stmts}stmts
->def sub(int xdef_argdef_args){stmtstmts}
while(expr){stmts}stmts
->def sub(int x,int y){y=y-1~return expr~}
while(x-2){stmtstmts}null
->def sub(int x,int y){y=y-1~return (expr)*expr3}
while(x-2){if(expr){stmts}stmtstmts}
->def sub(int x,int y)
{y=y-1~return (identifier-identifier)*int_type~}
while(x-2){if(x+y/2){<<exprstmts}if(expr){stmts}stmts}
->def sub(int x,int y)
{y=y-1~return (x-y)*2~}
while(x-2){if(x+y/2){<<x-4*(y+1)<<identifier(call_args)~stmts}
if(x-10){identifier=expr~stmts}null}
->def sub(int x,int y)
{y=y-1~return (x-y)*2~}
while(x-2){if(x+y/2){<<x-4*(y+1)<<sub(exprcall_arg)~null}
if(x-10){x=x+y~break~stmts}null}
->def sub(int x,int y)
{y=y-1~return (x-y)*2~}
while(x-2){if(x+y/2){<<x-4*(y+1)<<sub(x,x+2null)~}
if(x-10){x=x+y~break~null}}
->
def sub(int x,int y)
{
    y=y-1~return (x-y)*2~
}
while(x-2)
{
    if(x+y/2){<<x-4*(y+1)~<<sub(x,x+2)~} 
    if(x-10){x=x+y~break~}
}


Done!!!



```