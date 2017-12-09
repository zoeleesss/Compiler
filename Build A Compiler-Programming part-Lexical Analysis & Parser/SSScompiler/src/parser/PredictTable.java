package parser;

/**
 * LL1 predict table
 * author @ZoeLee
 * Created by sss on 04/12/2017.
 * copyright ©️ sss 2017-2018
 */


import java.util.*;

public class PredictTable {






    /**
     *
     *
     * GENERATORS : A -> B w t
     *
     *
     *
     *
     */


    //tool functions


    private boolean isTerminal(String src,List<String> terminals)
    {

        return terminals.contains(src);
    }

    private boolean isNonTerminal(String src,List<String> nonTerminals)
    {

        return nonTerminals.contains(src);
    }

    /**
     *
     * vector.get(0)    ->  a string like + or if
     * vector.get(1)    ->  a string = terminal or nonTerminal
     *
     * @param src
     * @param terminals
     * @param nonTerminals
     * @return
     */

    private Vector<String> getTerminalOrNonTerminal(String src,List<String> terminals,List<String> nonTerminals)
    {
        String str=src.charAt(0)+"";
        int i=1;
        while (i<src.length())
        {
            str+=src.charAt(i)+"";
            i++;


            if (isTerminal(str,terminals))
            {
                Vector<String> v=new Vector<>();
                v.add(str);
                v.add("terminal");
                return v;
            }else if(isNonTerminal(str,nonTerminals))
            {
                Vector<String> v=new Vector<>();
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
     * @param table
     * @param nonTerminal
     * @param terminal
     * @param generator
     * @param terminals
     * @param nonTerminals
     */

    private void fillAContentInTable(String [][]table,String nonTerminal,String terminal,String generator,List<String> terminals,List<String> nonTerminals)
    {

        int non_terminal_index=nonTerminals.indexOf(nonTerminal);
        int terminal_index=terminals.indexOf(terminal);
        table[non_terminal_index][terminal_index]=generator;

    }


    /**
     *
     *  @param firstSet
     *  @param followSet
     *  @param generators
     *
     *
     *  for each rule A -> w , for each terminal t
     *  in FIRST(w) set T[A,t] = w   (excluding null)
     *
     *  for each rule A -> w with null in FIRST(w),
     *  for each t in FOLLOW(A), set T[A,t] = w
     *
     *          Ts
     *  Nts   +       -       if      ,
     *
     *  S    S->T           S->DE
     *  T           T->null
     *  E                           E->T+S
     *  D   D->ifET
     *
     *
     */


    public String[][] createTable(Map<String,List<String>> firstSet,Map<String,List<String>> followSet,Map<String,List<String>> generators,List<String> terminals,List<String> nonTerminals){


        /**
         *
         * initialization of table
         *
         */
        String[][] table;

        table= new String[nonTerminals.size()][terminals.size()];




        Iterator<Map.Entry<String,List<String>>> it=generators.entrySet().iterator();


        // for each nonTerminal : derive many generators like S :  S-> A , S-> B
        while(it.hasNext()){
            Map.Entry<String,List<String>> entry=it.next();

            // Non-terminals             A
            String nt=entry.getKey();
            List<String> nt_generators=entry.getValue();



            // for each non-terminal , get the generators like A->if(B){A}~
            while (nt_generators.iterator().hasNext())
            {
                // get A->if(B){A}~
                String t=nt_generators.iterator().next();

                // get if(B){A}~
                String right_hand_token=t.split("->")[1];

                //get terminal 'if' or nonterminal  maybe ?
                Vector<String> vec=getTerminalOrNonTerminal(right_hand_token,terminals,nonTerminals);

                String str="",type="";

                try {
                    str = vec.get(0);
                    type = vec.get(1);
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }


                if (type.equals("terminal")) {

                    // add if    table[A][if]  =  A->if{B}{A}~
                     fillAContentInTable(table,nt,str,t,terminals,nonTerminals);


                     /*** e.g  T->FD ***/
                } else if (type.equals("nonTerminal")) {

                    /*** for each terminal t in First(FD) , add Table[T,t] = generator
                     *
                     *
                     *  First 集合 你写一下吧？然后给我一个 function 调用的接口？？？
                     *  辛苦辛苦！
                     *
                     *
                     * ***/







                    /*** if (null is in First(FD)) , for each terminals t in Follow(T) , add Table[T,t] = generator ***/


                    // get terminals of FollowSet of T
                    List<String> follow_terminals=followSet.get(nt);

                    for(int i=0;i<follow_terminals.size();i++)
                    {
                       String temp_follow_terminal=follow_terminals.get(i);
                       fillAContentInTable(table,nt,temp_follow_terminal,t,terminals,nonTerminals);

                    }



                }
















            }









            System.out.println("key="+entry.getKey()+","+"value="+entry.getValue());
        }












        return table;
    }





    public static void main(String[] args) {



    }

}
