package parser.entity;

import java.util.*;
/**
 * Created by sss on 30/12/2017.
 */
public class MyObject {


    public MyObject parent;
    public String name;            //E'
    public String code;            // if (..) {..}      |  x y (identifier)
    public String character;       // + , - , * , /
    public double value;           // 12 | 22.3
    public boolean isLeaf;         // true -> leaf node
    public boolean isEnd;          // true -> it could derive $ ,but also a leaf


    public String return_type;     // return expr~  expr: 2.0 -> return_type=double
    public String type;            // type : int / double

    public MyObject() {
        parent=null;
        name="";
        code="";
        character="";
        value=0;
        isLeaf=false;
        isEnd=false;
        return_type="";
        type="";
    }

    public MyObject(MyObject parent, String name, String code, String character, double value, boolean isLeaf, boolean isEnd) {
        this.parent = parent;
        this.name = name;
        this.code = code;
        this.character = character;
        this.value = value;
        this.isLeaf = isLeaf;
        this.isEnd = isEnd;
        this.return_type="";
        type="";
    }

    public static List<MyObject> getChildren(MyObject obj,List<MyObject> objects)
    {
        List<MyObject> children=new ArrayList<>();

        for (MyObject myObject:objects)
        {
            if (myObject.parent==obj)
            {
                children.add(myObject);
            }
        }
        return children;
    }


    public static List<MyObject> reverseList(List<MyObject> objs)
    {
        List<MyObject> reservedList =new ArrayList<>();
        for (int i = objs.size()-1; i >=0 ; i--) {
            reservedList.add(objs.get(i));
        }
        return reservedList;
    }

    public static List<MyObject> findCommonParentNodes(MyObject node,List<MyObject> leaves,List<MyObject> allNodes)
    {
        List<MyObject> commonLeaves=new ArrayList<>();
        List<MyObject> children;
        MyObject parent=node.parent;
        //System.out.println("==================================================");
        //System.out.println("<<<"+node.name+">>>");
        children=getChildren(parent,allNodes);
        for (MyObject leaf:leaves)
        {
            if (leaf.parent==parent)
            {
                commonLeaves.add(leaf);
                //System.out.println("<"+leaf.name+">");
            }
        }
        if (children.size()==commonLeaves.size()) {
            //System.out.println("ok");
            //System.out.println("==================================================");
            return reverseList(commonLeaves);
        }
        else {
            //System.out.println("null");
            //System.out.println("==================================================");
            return null;
        }
    }
    public static void printList(List<MyObject> objects)
    {
        for (int i = 0; i <objects.size() ; i++) {
            System.out.print(objects.get(i).name+"\t");
        }
        System.out.println();
    }



    //  program -> stmts ,stmts-> stmt stmts

    /**
     *
     *
     *
     *
     * -program
     *      -stmts
     *              -stmt
     *                      -type
     *                      -identifier
     *              -stmts
     *
     *
     */

    public static void print(MyObject root,List<MyObject> allNodes)
    {

        List<MyObject> needs=new ArrayList<>();
        needs.add(root);
        printTree(needs,allNodes,0);

    }


    public static void printTree(List<MyObject> needToProcessNodes,List<MyObject> nodes,int level) {

        for (int index = needToProcessNodes.size()-1; index >=0 ; index--) {

            MyObject curr_obj = needToProcessNodes.get(index);
            List<MyObject> children = getChildren(curr_obj, nodes);
            if (children != null)      //it is a parent
            {
                for (int j = 0; j < level; j++) {
                    System.out.print("\t");
                }
                System.out.println("- "+curr_obj.name);
                printTree(children, nodes, level + 1);
            } else if (level != 0) {
                for (int j = 0; j < level; j++) {
                    System.out.print("\t");
                }
                System.out.println("- "+curr_obj.name);

            }


        }


    }




}






