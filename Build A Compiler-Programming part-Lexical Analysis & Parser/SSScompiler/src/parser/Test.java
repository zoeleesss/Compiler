package parser;

import java.util.*;

/**
 * LL1 Test
 * author @ZoeLee
 * Created by sss on 05/12/2017.
 * copyright ©️ sss 2017-2018
 */
public class Test {

    private static Map<String,List<String>> generators;
    private static List<String> terminals;
    private static List<String> nonTerminals;
    private static Map<String,List<String>> followSet;
    private static Map<String,List<String>> firstSet;
    private static List<String> tokenStream;

    public static String [][]table;



    public static void initAttributes()
    {

        /**
         *  initialize all private attributes (except table)
         *
         */


        

    }


    public static void test() {


        initAttributes();

        PredictTable predictTable=new PredictTable();
        LL1Parser ll1Parser=new LL1Parser();

        // use PredictTable

        table=predictTable.createTable(firstSet,followSet,generators,terminals,nonTerminals);

        // use LL1Parser

        boolean flag=ll1Parser.parse(table,generators,terminals,nonTerminals,tokenStream);

        //successful
        if (flag)
        System.out.println("Parser Done! No Grammar mistakes!");
        else System.out.println("Parser Failed!");



    }

    public static void main(String[] args) {
        test();
    }


}
