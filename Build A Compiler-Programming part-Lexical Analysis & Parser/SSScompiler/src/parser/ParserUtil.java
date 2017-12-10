package parser;


import java.util.*;

/**
 * LL1 Utilities / Tool Functions
 * author @ZoeLee
 * Created by sss on 05/12/2017.
 * copyright ©️ sss 2017-2018
 */
public class ParserUtil {
    private static ParserUtil ourInstance = new ParserUtil();

    public static ParserUtil getInstance() {
        return ourInstance;
    }



        public boolean isTerminal(String src,List<String> terminals)
        {

            return terminals.contains(src);
        }

        public boolean isNonTerminal(String src,List<String> nonTerminals)
        {

            return nonTerminals.contains(src);
        }

        /**
         *
         * this generator doesn't have "A->"
         * it only has the right hand part "typeidentifier=expr"
         *
         * vector.get(0)    ->  a string like A or if
         * vector.get(1)    ->  a string = terminal or nonTerminal
         *
         * @param src               String
         * @param terminals         List<String>
         * @param nonTerminals      List<String>
         * @return                  Vector<String>
         */

        public Vector<String> getTerminalOrNonTerminalOfFirstCharacters(String src, List<String> terminals, List<String> nonTerminals)
        {
            String str=src.charAt(0)+"";
            int i=1;
            Vector<String> v=new Vector<>();
            if (isTerminal(str,terminals))
            {

                v.add(str);
                v.add("terminal");
                return v;

            }
            while (i<src.length())
            {
                str+=src.charAt(i)+"";
                i++;


                if (isTerminal(str,terminals))
                {

                    v.add(str);
                    v.add("terminal");
                    return v;
                }else if(isNonTerminal(str,nonTerminals))
                {
                    v.add(str);
                    v.add("nonTerminal");
                    return v;
                }

            }
            return null;

        }

        /**
         *
         *  add a generator into table :  Table[A,+]=generator
         *  generator e.g.  "A->if(B){A}~A"
         *  its generator has full parts including "A->"
         *
         * @param table             String [][]
         * @param nonTerminal       String
         * @param terminal          String
         * @param generator         String
         * @param terminals         List<String>
         * @param nonTerminals      List<String>
         *
         */

        public void fillAContentInTable(String [][]table, String nonTerminal, String terminal, String generator, List<String> terminals, List<String> nonTerminals)
        {

            int non_terminal_index=nonTerminals.indexOf(nonTerminal);
            int terminal_index=terminals.indexOf(terminal);
            if (table[non_terminal_index][terminal_index].equals(""))
            table[non_terminal_index][terminal_index]=generator;
            else System.err.println("ERROR! table [ "+nonTerminal+" ] ,[  "+terminal+" ] is not empty");

        }


    /**
     *
     *  get the generator from the predict table by using nonTerminal+terminal Strings
     *  e.g. "A->if(B){A}A" get a list that contains if,(,B,),{,A,},A
     *  only the right hand part
     *
     * @param table                     String [][]
     * @param parseStackPointer         String
     * @param tokenStreamStackPointer   String
     * @param terminals                 List<String>
     * @param nonTerminals              List<String>
     * @return                          List<String>  that contains terminals+nonTerminals
     */

        public List<String> findGeneratorInTable(String [][]table,String parseStackPointer,String tokenStreamStackPointer,List<String>terminals, List<String> nonTerminals)
        {

            int non_terminal_index=nonTerminals.indexOf(parseStackPointer);
            int terminal_index=terminals.indexOf(tokenStreamStackPointer);
            String generator=table[non_terminal_index][terminal_index];
            generator=generator.split("->")[1];
            return splitGenerator(generator,terminals,nonTerminals);
        }


    /**
     *
     * split generator
     * this generator doesn't have "stmt->"
     * it only has the right hand part "typeidentifier=expr"
     *
     *  e.g. type identifier = expr
     *  vec[0]="type"
     *  vec[1]="identifier"
     *  vec[2]="="
     *  vec[3]="expr"
     *
     * @param generator         String
     * @param terminals         List<String>
     * @param nonTerminals      List<String>
     * @return                  Vector<String>  a vec that contains terminals+nonTerminals
     */

    public List<String> splitGenerator(String generator,List<String> terminals,List<String> nonTerminals)
    {

        String str=generator.charAt(0)+"";
        int i=1;
        List<String> v=new ArrayList<>();
        if (isTerminal(str,terminals))
        {

            v.add(str);
            v.add("terminal");

        }
        while (i<generator.length())
        {
            str+=generator.charAt(i)+"";
            i++;

            if (isTerminal(str,terminals))
            {

                v.add(str);
                v.add("terminal");

            }else if(isNonTerminal(str,nonTerminals))
            {

                v.add(str);
                v.add("nonTerminal");

            }

        }

        return v;

    }


    /**
     *
     * e.g. src=[1,2,3] dst=[]
     * after processing,
     * dst[0]=1,dst[1]=2,dst[2]=3
     *
     * @param src       List<String>
     * @param dst       Stack<String>
     */

    public void reverseAddAll(List<String> src,Stack<String> dst)
    {
        for (int i=src.size()-1;i>=0;i--)
        {
            dst.push(src.get(i));
        }
    }





    private ParserUtil() {
    }
}
