package translation;

import parser.entity.Grammar;
import parser.entity.MyObject;
import parser.util.MyUtil;

import java.util.List;

/**
 * Created by sss on 02/01/2018.
 */
public class SemanticAnalysis {


    private static SemanticAnalysis instance=null;



    public static SemanticAnalysis getInstance()
    {
        if (instance==null)
        {
            instance=new SemanticAnalysis();
        }
        return instance;
    }

    private SemanticAnalysis()
    {

    }
    


    public void distributeFunction(List<MyObject> objects)
    {
        String gra=SemanticUtil.generateGra(objects);
        System.out.println("+++++"+gra+"+++++");
        if (SemanticUtil.grammarCompare(objects,"program->stmts"))
        {
            SemanticUtil.func_program2stmts(objects);
        }else if (SemanticUtil.grammarCompare(objects,"stmts->stmtstmts"))
        {
            SemanticUtil.func_stmts2stmtstmts(objects);
        }else if (SemanticUtil.grammarCompare(objects,"stmt->identifieridentification~"))
        {
            SemanticUtil.func_stmt2identifieridentification(objects);
        }else if (SemanticUtil.grammarCompare(objects,"stmt->typeidentifieridentification~"))
        {
            SemanticUtil.func_stmt2typeidentifieridentification(objects);
        }else if (SemanticUtil.grammarCompare(objects,"stmt-><<expr~"))
        {
            SemanticUtil.func_stmt2llexpr(objects);
        }else if (SemanticUtil.grammarCompare(objects,"stmt->if(expr){stmts}if_right_part"))
        {
            SemanticUtil.func_stmt2if(objects);
        }else if (SemanticUtil.grammarCompare(objects,"stmt->break~"))
        {
            SemanticUtil.func_stmt2break(objects);
        }else if (SemanticUtil.grammarCompare(objects,"stmt->while(expr){stmts}"))
        {
            SemanticUtil.func_stmt2while(objects);
        }else if (SemanticUtil.grammarCompare(objects,"stmt->returnexpr~"))
        {
            SemanticUtil.func_stmt2returnexpr(objects);
        }else if (SemanticUtil.grammarCompare(objects,"stmt->defidentifier(def_args){stmts}"))
        {
            SemanticUtil.func_stmt2def(objects);
        }else if (SemanticUtil.grammarCompare(objects,"if_right_part->{stmts}else"))
        {
            SemanticUtil.func_ifraightpart2else(objects);
        }else if (SemanticUtil.grammarCompare(objects,"def_args->typeidentifierdef_arg"))
        {
            SemanticUtil.func_defargs2typeidentifierdefarg(objects);
        }else if (SemanticUtil.grammarCompare(objects,"def_arg->,def_args"))
        {
            SemanticUtil.func_defarg2defargs(objects);
        }else if (SemanticUtil.grammarCompare(objects,"F->(expr)"))
        {
            SemanticUtil.func_f2expr(objects);
        }else if (SemanticUtil.grammarCompare(objects,"F->identifierassignment_right"))
        {
            SemanticUtil.func_f2identifier(objects);
        }else if (SemanticUtil.grammarCompare(objects,"assignment_right->(call_args)"))
        {
            SemanticUtil.func_assignmentright2callargs(objects);
        }else if (SemanticUtil.grammarCompare(objects,"type->int"))
        {
            SemanticUtil.func_type2int(objects);
        }else if (SemanticUtil.grammarCompare(objects,"type->double"))
        {
            SemanticUtil.func_type2double(objects);
        }
//------------------------------------------------------------------------------------------
        else if (SemanticUtil.grammarCompare(objects,"call_args->exprcall_arg"))
        {
            SemanticUtil.call_args2exprcall_arg(objects);
        }
        else if (SemanticUtil.grammarCompare(objects,"call_arg->,call_args"))
        {
            SemanticUtil.call_arg2signcall_args(objects);
        }
        else if (SemanticUtil.grammarCompare(objects,"identification->=expr"))
        {
            SemanticUtil.identification2equalsexpr(objects);
        }
        else if (SemanticUtil.grammarCompare(objects,"identification->(call_args)"))
        {
            SemanticUtil.identification2equalscall_args(objects);
        }
        else if (SemanticUtil.grammarCompare(objects,"expr->TE'"))
        {
            SemanticUtil.expr2TE(objects);
        }
        else if (SemanticUtil.grammarCompare(objects,"E'->-TE'"))
        {
            SemanticUtil.E2deleteTE(objects);
        }
        else if (SemanticUtil.grammarCompare(objects,"E'->+TE'"))
        {
            SemanticUtil.E2addTE(objects);
        }
        else if (SemanticUtil.grammarCompare(objects,"T->FT'"))
        {
            SemanticUtil.T2FT(objects);
        }
        else if (SemanticUtil.grammarCompare(objects,"T'->*FT'"))
        {
            SemanticUtil.E2mulFT(objects);
        }
        else if (SemanticUtil.grammarCompare(objects,"T'->/FT'"))
        {
            SemanticUtil.E2divTE(objects);
        }
    }
}

/*

x=2.1+1.9+1.2~
if (x-5)
{
<<x~
}else {
x=x-1~
}
while (1-x)
{
<<x+1~
x=x+1~
if (x-1)
{
    x=x+2~
    <<x~
    x=2-x~
}else {
<<x-1~
break;
}
}
<<x~

 */

//SemanticUtil.grammarCompare(objects,"stmt->if(expr){stmts}if_right_part")"stmt->if_right_partexprstmtsif(){}")"stmt->stmtsexprif(){}if_right_part")"stmt->exprstmtsif(){}if_right_part")