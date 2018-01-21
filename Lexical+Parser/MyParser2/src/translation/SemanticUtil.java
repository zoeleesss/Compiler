package translation;

import parser.entity.*;
import parser.util.*;

import java.util.*;

/**
 * Created by sss on 02/01/2018.
 */
public class SemanticUtil {

    private static List<MyObject> leaves = MyUtil.leaves, allNodes = MyUtil.allNodes;
    private static MyObject root = MyUtil.root;
    public static String rawCode;

    public static void removeFromBoth(List<MyObject> objects) {
        removeFromAllNodes(objects);
        removeFromLeaves(objects);
    }

    public static void removeFromLeaves(List<MyObject> objects) {
        for (int i = 0; i < objects.size(); i++) {
            leaves.remove(objects.get(i));
        }
    }

    public static void removeFromAllNodes(List<MyObject> objects) {
        for (int i = 0; i < objects.size(); i++) {
            allNodes.remove(objects.get(i));
        }
    }
    public static int getIndexOfSign(List<MyObject> objects)
    {
    		if(getIndexByName(objects,"+")!=-1)
    		{
    			return getIndexByName(objects,"+");
    		}
    		else if(getIndexByName(objects,"-")!=-1)
    		{
    			return getIndexByName(objects,"-");
    		}
    		else if(getIndexByName(objects,"*")!=-1)
    		{
    			return getIndexByName(objects,"*");
    		}
    		else if(getIndexByName(objects,"/")!=-1)
    		{
    			return getIndexByName(objects,"/");
    		}
    	return -1;
    }
    public static String generateGra(List<MyObject> objects) {
        String s = "";
        int index=-1;
        if (objects.get(0).parent != null) {
            s += objects.get(0).parent.name + "->";
            if(getIndexOfSign(objects)!=-1)
            {
            	System.out.println(objects.get(getIndexOfSign(objects)).name);
            	s+= objects.get(getIndexOfSign(objects)).name;
            	index=getIndexOfSign(objects);
            }
            for (int i = 0; i < objects.size(); i++) {
            	if(i!=index)
                s += objects.get(i).name;
            }

        }
        return s;
    }
    
    public static double Count(double i,double j,String sign)
	{
		if(sign.equals("+"))
		{
			return i+j;
		}
		else if(sign.equals("-"))
		{
			return i-j;
		}
		else if(sign.equals("*"))
		{
			return i*j;
		}
		else if(sign.equals("/"))
		{
			return i/j;
		}
		return 0;
	}

    public static String getCode(List<MyObject> objects)
    {
        String code="";
        for (MyObject obj:objects)
            code+=obj.code+"";
        return code;
    }

    public static void permute(List<String> possibleGrammars,MyObject[] objects,int start)
    {

        if (start==objects.length)
        {
            //output
            String output="";
            String parent=objects[0].parent.name;
            String children="";
            for (MyObject obj:objects)
            {
                children+=obj.name;
            }
            output+=parent+"->"+children;
            possibleGrammars.add(output);
        }
        else {

            for (int i=start;i<objects.length;i++)
            {
                    swap(objects,start,i);      //swap
                    permute(possibleGrammars,objects,start+1);   // after swap, permute again
                    swap(objects,start,i);
            }
        }

    }

    private static void swap(MyObject []array,int s,int i)
    {

        MyObject t=array[s];
        array[s]=array[i];
        array[i]=t;
    }


    public static boolean grammarCompare(List<MyObject> objects,String str)
    {
        List<String> possibleGrammars=new ArrayList<>();
        MyObject []objs=new MyObject[objects.size()];
        for (int i=0;i<objs.length;i++)
        {
            objs[i]=objects.get(i);
        }
        permute(possibleGrammars,objs,0);

        for (int i=0;i<possibleGrammars.size();i++)
        {
            //System.out.println("-----"+possibleGrammars.get(i)+"-----");
            if (str.equals(possibleGrammars.get(i)))
            {
                return true;
            }
        }


        return false;
    }

    /**
     *
     * tool functions above  ^
     *
     * translation functions below V
     *
     * @param objects
     */




    // program->stmts
    public static void func_program2stmts(List<MyObject> objects)
    {
        // objects : stmts

        MyObject stmts=objects.get(0);
        root.code=stmts.code;
        allNodes.remove(stmts);
        leaves.remove(stmts);
        //leaves.add(root);
        //don't add any node to leaves, so now leaves.empty()==true . translation stopped!
        //output translation result here!
        System.out.println("Translation Started!\n");
        System.out.println(stmts.code);
        rawCode=stmts.code;
        System.out.println("\nTranslation Done!");

    }

    // stmts->stmtstmts
    public static void func_stmts2stmtstmts(List<MyObject> objects)//xsxsx
    {
        // objects : stmt stmts

        MyObject stmt=objects.get(getIndexByName(objects,"stmt"));
        MyObject stmts=objects.get(getIndexByName(objects,"stmts"));
        
        MyObject parent=stmt.parent;

        parent.code+=stmt.code+stmts.code;
        System.out.println("st "+stmt.type);
        System.out.println("str "+stmt.return_type);
        System.out.println("sts "+stmts.type);
        System.out.println("stsr "+stmts.return_type);
        parent.return_type=stmt.return_type;
        parent.type=stmt.type;
        removeFromBoth(objects);
        leaves.add(parent);

    }

    // stmt->identifieridentification~
    public static void func_stmt2identifieridentification(List<MyObject> objects)
    {
        // objects : identifier identification ~

        MyObject identifier=objects.get(getIndexByName(objects,"identifier"));
        MyObject identification=objects.get(getIndexByName(objects,"identification"));

        MyObject parent=identifier.parent;

        // ~ is converted to ;
        parent.code+=identifier.code+identification.code+";"+"\n";

        removeFromBoth(objects);
        leaves.add(parent);

    }
    // stmt->typeidentifieridentification~
    public static void func_stmt2typeidentifieridentification(List<MyObject> objects)
    {
        // objects : type identifier identification ~

        MyObject type=objects.get(getIndexByName(objects,"type"));
        MyObject identifier=objects.get(getIndexByName(objects,"identifier"));
        MyObject identification=objects.get(getIndexByName(objects,"identification"));

        MyObject parent=type.parent;

        parent.code+=type.code+" "+identifier.code+identification.code+";"+"\n";
        removeFromBoth(objects);
        leaves.add(parent);
    }

   // stmt-><<expr~
   public static void func_stmt2llexpr(List<MyObject> objects)
   {
       // objects : << expr ~


       MyObject expr=objects.get(getIndexByName(objects,"expr"));
       MyObject parent=expr.parent;

       parent.code+="cout<<"+expr.code+";"+"\n";
       removeFromBoth(objects);
       leaves.add(parent);
   }

   // stmt->if(expr){stmts}if_right_part
   public static void func_stmt2if(List<MyObject> objects)
   {
       // objects : if ( expr ) { stmts } if_right_part
       //           0   1 2   3 4    5   6   7

       MyObject expr=objects.get(2);
       MyObject parent=expr.parent;
       String result="";
       result+=objects.get(getIndexByName(objects,"if")).name;
       result+=objects.get(getIndexByName(objects,"(")).name;
       result+=objects.get(getIndexByName(objects,"expr")).code;
       result+=objects.get(getIndexByName(objects,")")).name;
       result+="\n{\n";
       result+=objects.get(getIndexByName(objects,"stmts")).code;
       result+=objects.get(getIndexByName(objects,"}")).name;
       result+=objects.get(getIndexByName(objects,"if_right_part")).code;
       MyObject stmts=objects.get(getIndexByName(objects,"stmts"));
       if (!stmts.type.equals(""))
       {
           if (parent.type.equals(""))
           parent.type=stmts.type;

       }


       if (!stmts.return_type.equals(""))
       {
           if (parent.return_type.equals(""))
           parent.return_type=stmts.return_type;
       }

       parent.code+=result+"\n";
       removeFromBoth(objects);
       leaves.add(parent);
   }

   // stmt->break~
   public static void func_stmt2break(List<MyObject> objects)
   {
       // objects : break ~

       MyObject parent=objects.get(0).parent;

       parent.code+="break;\n";
       removeFromBoth(objects);
       leaves.add(parent);
   }

   // stmt->while(expr){stmts}
   public static void func_stmt2while(List<MyObject> objects)
   {
       // objects : while ( expr ) { stmts }

       MyObject parent=objects.get(0).parent;
       String result="";
       result+=objects.get(getIndexByName(objects,"while")).name;
       result+=objects.get(getIndexByName(objects,"(")).name;
       result+=objects.get(getIndexByName(objects,"expr")).code;
       result+=objects.get(getIndexByName(objects,")")).name;
       result+="\n{\n";
       result+=objects.get(getIndexByName(objects,"stmts")).code;
       result+="\n}";
       parent.code+=result+"\n";
       removeFromBoth(objects);
       leaves.add(parent);
   }

   // stmt->returnexpr~
   public static void func_stmt2returnexpr(List<MyObject> objects)
   {
       // objects : return expr~
       MyObject expr=objects.get(getIndexByName(objects,"expr"));
       MyObject parent=expr.parent;
       parent.code+="return "+expr.code+";\n";
       if (!expr.type.equals("")) {
           System.out.println("expr "+parent.type +" 0 "+expr.type);
            if (parent.type.equals(""))
                parent.type = expr.type;
       }
       if (!expr.return_type.equals(""))
       {
           System.out.println("expr "+parent.return_type +" 1 "+expr.return_type);
           if (parent.return_type.equals(""))
                parent.return_type=expr.return_type;
       }
       removeFromBoth(objects);
       leaves.add(parent);
   }

   // stmt->defidentifier(def_args){stmts}
   public static void func_stmt2def(List<MyObject> objects)
   {
        // objects : def identifier ( def_args ) { stmts }
       //             0    1        2    3     4  5  6  7

        MyObject stmts=objects.get(getIndexByName(objects,"stmts"));
        MyObject parent=stmts.parent;
        String t="";


       System.out.println("sts "+stmts.type);
       System.out.println("stsr "+stmts.return_type);
       System.out.println("stp "+parent.type);
       System.out.println("stpr "+parent.return_type);

        t=stmts.type;
        //if (!t.equals("")) t=stmts.type;
        if (t.equals("")) t="void";

        parent.code+=t+" "+objects.get(getIndexByName(objects,"identifier")).code+"("+
                objects.get(getIndexByName(objects,"def_args")).code+")\n{\n"+stmts.code+"\n}\n";

        removeFromBoth(objects);
        leaves.add(parent);
   }

   // if_right_part->else{stmts}

    public static void func_ifraightpart2else(List<MyObject> objects)
    {
        // objects : else { stmts }
        //             0  1  2    3
        MyObject stmts=objects.get(getIndexByName(objects,"stmts"));
        MyObject parent=stmts.parent;
        parent.code+="\nelse \n{\n"+stmts.code+" \n}\n";
        if (!objects.get(getIndexByName(objects,"stmts")).type.equals("")) {
            parent.type = objects.get(getIndexByName(objects, "stmts")).type;
            parent.return_type = objects.get(getIndexByName(objects,"stmts")).return_type;
        }
        removeFromBoth(objects);
        leaves.add(parent);
    }

    // def_args->typeidentifierdef_arg

    public static void func_defargs2typeidentifierdefarg(List<MyObject> objects)
    {
        // objects : type identifier def_arg

        MyObject parent=objects.get(0).parent;
        parent.code+=objects.get(getIndexByName(objects,"type")).code+" "+objects.get(getIndexByName(objects,"identifier")).code+objects.get(getIndexByName(objects,"def_arg")).code;

        parent.type=objects.get(getIndexByName(objects,"def_arg")).type;
        removeFromBoth(objects);
        leaves.add(parent);

    }

    // def_arg->,def_args

    public static void func_defarg2defargs(List<MyObject> objects)
    {

        // objects : , def_args

        MyObject parent=objects.get(getIndexByName(objects,"def_args")).parent;
        parent.code+=", "+objects.get(getIndexByName(objects,"def_args")).code;
        parent.type=objects.get(getIndexByName(objects,"def_args")).type;
        removeFromBoth(objects);
        leaves.add(parent);

    }

    // F->(expr)

    public static void func_f2expr(List<MyObject> objects)
    {
        // objects : ( expr )
        MyObject expr=objects.get(1);
        MyObject parent=expr.parent;

        parent.value=expr.value;
        parent.type=expr.type;
        parent.code="("+expr.code+")";

        removeFromBoth(objects);
        leaves.add(parent);

    }

    // F->identifierassignment_right

    public static void func_f2identifier(List<MyObject> objects)
    {
        //objects : identifier  assignment_right

        MyObject identifier=objects.get(getIndexByName(objects,"identifier"));
        MyObject assignment_right=objects.get(getIndexByName(objects,"assignment_right"));
        MyObject parent=identifier.parent;

        parent.code+=identifier.code+assignment_right.code;
        parent.type="double";

        removeFromBoth(objects);
        leaves.add(parent);

    }

    // assignment_right->(call_args)

    public static void func_assignmentright2callargs(List<MyObject> objects)
    {
        //objects : ( call_args )

        MyObject call_args=objects.get(getIndexByName(objects,"call_args"));
        MyObject parent=call_args.parent;

        parent.code+="("+call_args.code+")";

        removeFromBoth(objects);
        leaves.add(parent);

    }

    // type->int

    public static void func_type2int(List<MyObject> objects)
    {
        // objects : int

        MyObject i=objects.get(0);
        MyObject parent=i.parent;

        parent.code+="int";
        parent.type="int";

        removeFromBoth(objects);
        leaves.add(parent);

    }

    // type->double

    public static void func_type2double(List<MyObject> objects)
    {
        //objects : double

        MyObject d=objects.get(0);
        MyObject parent=d.parent;

        parent.code+="double";
        parent.type="double";

        removeFromBoth(objects);
        leaves.add(parent);

    }
    //-----------------------------------------------------------------
    private static int getIndexByName(List<MyObject> objects,String name)//根据name获取index，仅对唯一index有效
    {
    	for(int i=0;i<=objects.size()-1;i++)
    	{
    		if(objects.get(i).name.equals(name))
    		{
    			return i;
    		}
    	}
    	return -1;
    }
    
    public static void call_args2exprcall_arg(List<MyObject> objects)//call_args->expr call_arg
    {
    	MyObject d=objects.get(0);
        MyObject parent=d.parent;
        parent.code+=objects.get(getIndexByName(objects,"expr")).code+objects.get(getIndexByName(objects,"call_arg")).code;
        parent.type=objects.get(getIndexByName(objects,"expr")).type;
        removeFromBoth(objects);
        leaves.add(parent);
    }
    public static void call_arg2signcall_args(List<MyObject> objects)//call_arg->, call_args
    {
    	MyObject d=objects.get(0);
        MyObject parent=d.parent;
        String result="";
        double value=0;
        result+=",";
        result+=objects.get(getIndexByName(objects,"call_args")).code;
        value+=objects.get(getIndexByName(objects,"call_args")).value;
        parent.code+=result;
        parent.value+=value;
        parent.type=objects.get(getIndexByName(objects,"call_args")).type;
        removeFromBoth(objects);
        leaves.add(parent);
    }
    public static void identification2equalsexpr(List<MyObject> objects)//identification->= expr
    {
    	MyObject d=objects.get(0);
        MyObject parent=d.parent;
        String result="";
        result+="=";
        result+=objects.get(getIndexByName(objects,"expr")).code;
        parent.code+=result;
        parent.type=objects.get(getIndexByName(objects,"expr")).type;
        removeFromBoth(objects);
        leaves.add(parent);
    }
    public static void identification2equalscall_args(List<MyObject> objects)//identification->=( call_args )
    {
    	MyObject d=objects.get(0);
        MyObject parent=d.parent;
        String result="(";
        result+=objects.get(getIndexByName(objects,"call_args")).code;
        result+=")";
        parent.code+=result;
        parent.type=objects.get(getIndexByName(objects,"call_args")).type;
        removeFromBoth(objects);
        leaves.add(parent);
    }
    public static void expr2TE(List<MyObject> objects)//expr->T E'
    {
    	MyObject d=objects.get(0);
        MyObject parent=d.parent;
        String result="";

        String t1="";
        String t2="";
        if(objects.get(getIndexByName(objects,"T")).isEnd==true)
        {
        	parent.code=objects.get(getIndexByName(objects,"F")).code;
        	t1=objects.get(getIndexByName(objects,"F")).type;
        }
        else 
        {
        	result+=objects.get(getIndexByName(objects,"T")).code+objects.get(getIndexByName(objects,"E'")).character+objects.get(getIndexByName(objects,"E'")).code;
        	parent.code=result;
            t2=objects.get(getIndexByName(objects,"T")).type;
        }
        if (!t1.equals("") || !t2.equals(""))
        {
            if (t1.equals("double") || t2.equals("double")) parent.type="double";
            else parent.type="int";
        }
        removeFromBoth(objects);
        leaves.add(parent);
    }
    public static void E2addTE(List<MyObject> objects)//E'->+ T E'
    {
    	MyObject d=objects.get(0);
        MyObject parent=d.parent;
        String result="";
        String t1="";
        String t2="";
        if(objects.get(getIndexByName(objects,"E'")).isEnd==true)
        {
        	parent.character="+";
        	parent.code=objects.get(getIndexByName(objects,"T")).code;
            t1=objects.get(getIndexByName(objects,"T")).type;
        }
        else 
        {
        	result+=objects.get(getIndexByName(objects,"T")).code+objects.get(getIndexByName(objects,"E'")).character+objects.get(getIndexByName(objects,"E'")).code;
        	parent.character="+";
        	parent.code=result;
            t1=objects.get(getIndexByName(objects,"E'")).type;
            t2=objects.get(getIndexByName(objects,"T")).type;
        }
        if (!t1.equals("") || !t2.equals(""))
        {
            if (t1.equals("double") || t2.equals("double")) parent.type="double";
            else parent.type="int";
        }
        removeFromBoth(objects);
        leaves.add(parent);
    }
    public static void E2deleteTE(List<MyObject> objects)//E'->- T E'
    {
    	MyObject d=objects.get(0);
        MyObject parent=d.parent;
        String result="";
        String t1="";
        String t2="";
        if(objects.get(getIndexByName(objects,"E'")).isEnd==true)
        {
        	parent.character="-";
        	parent.code=objects.get(getIndexByName(objects,"T")).code;
            t1=objects.get(getIndexByName(objects,"T")).type;
        }
        else 
        {
        	result+=objects.get(getIndexByName(objects,"T")).code+objects.get(getIndexByName(objects,"E'")).character+objects.get(getIndexByName(objects,"E'")).code;
        	parent.character="-";
        	parent.code=result;
            t1=objects.get(getIndexByName(objects,"E'")).type;
            t2=objects.get(getIndexByName(objects,"T")).type;
        }
        if (!t1.equals("") || !t2.equals(""))
        {
            if (t1.equals("double") || t2.equals("double")) parent.type="double";
            else parent.type="int";
        }
        removeFromBoth(objects);
        leaves.add(parent);
    }
    public static void T2FT(List<MyObject> objects)//T->F T'
    {
    	MyObject d=objects.get(0);
        MyObject parent=d.parent;
        String result="";
        String t1="";
        String t2="";
        if(objects.get(getIndexByName(objects,"T'")).isEnd==true)
        {
        	parent.code=objects.get(getIndexByName(objects,"F")).code;
            t1=objects.get(getIndexByName(objects,"F")).type;
        }
        else 
        {
        	result+=objects.get(getIndexByName(objects,"F")).code+objects.get(getIndexByName(objects,"T'")).character+objects.get(getIndexByName(objects,"T'")).code;
        	parent.code=result;
            t1=objects.get(getIndexByName(objects,"T'")).type;
            t2=objects.get(getIndexByName(objects,"F")).type;
        }
        if (!t1.equals("") || !t2.equals(""))
        {
            if (t1.equals("double") || t2.equals("double")) parent.type="double";
            else parent.type="int";
        }
        removeFromBoth(objects);
        leaves.add(parent);
    }
    public static void E2mulFT(List<MyObject> objects)//T'->* F T'
    {
    	MyObject d=objects.get(0);
        MyObject parent=d.parent;
        String result="";
        String t1="";
        String t2="";
        if(objects.get(getIndexByName(objects,"T'")).isEnd==true)
        {
        	parent.character="*";
        	parent.code=objects.get(getIndexByName(objects,"F")).code;
            t1=objects.get(getIndexByName(objects,"F")).type;
        }
        else 
        {
        	result+=objects.get(getIndexByName(objects,"F")).code+objects.get(getIndexByName(objects,"T'")).character+objects.get(getIndexByName(objects,"T'")).code;
        	parent.character="*";
        	parent.code=result;
            t1=objects.get(getIndexByName(objects,"T'")).type;
            t2=objects.get(getIndexByName(objects,"F")).type;
        }
        if (!t1.equals("") || !t2.equals(""))
        {
            if (t1.equals("double") || t2.equals("double")) parent.type="double";
            else parent.type="int";
        }
        removeFromBoth(objects);
        leaves.add(parent);
    }
    public static void E2divTE(List<MyObject> objects)//T'->/ F T'
    {
    	MyObject d=objects.get(0);
        MyObject parent=d.parent;
        String result="";
        String t1="";
        String t2="";
        if(objects.get(getIndexByName(objects,"T'")).isEnd==true)
        {
        	parent.character="/";
        	parent.code=objects.get(getIndexByName(objects,"F")).code;
            t1=objects.get(getIndexByName(objects,"F")).type;
        }
        else 
        {
        	result+=objects.get(getIndexByName(objects,"F")).code+objects.get(getIndexByName(objects,"T'")).character+objects.get(getIndexByName(objects,"T'")).code;
        	parent.character="/";
        	parent.code=result;
            t1=objects.get(getIndexByName(objects,"T'")).type;
            t2=objects.get(getIndexByName(objects,"F")).type;
        }
        if (!t1.equals("") || !t2.equals(""))
        {
            if (t1.equals("double") || t2.equals("double")) parent.type="double";
            else parent.type="int";
        }
        removeFromBoth(objects);
        leaves.add(parent);
    }
}









