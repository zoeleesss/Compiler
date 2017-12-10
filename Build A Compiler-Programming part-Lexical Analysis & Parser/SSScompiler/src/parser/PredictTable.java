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

        /*** for each nonTerminal : derive many generators like S :  S-> A , S-> B ***/
        while(it.hasNext()){
            Map.Entry<String,List<String>> entry=it.next();

            // Non-terminals             A
            String nt=entry.getKey();
            List<String> nt_generators=entry.getValue();



            /*** for each non-terminal , get the generators like A->if(B){A}A  ***/
            while (nt_generators.iterator().hasNext())
            {
                // get A->if(B){A}~
                String t=nt_generators.iterator().next();

                // get if(B){A}~
                String right_hand_token=t.split("->")[1];

                //get terminal 'if' or nonTerminal  maybe ?
                ParserUtil util=ParserUtil.getInstance();
                Vector<String> vec=util.getTerminalOrNonTerminalOfFirstCharacters(right_hand_token,terminals,nonTerminals);

                String str="",type="";

                try {
                    str = vec.get(0);
                    type = vec.get(1);
                }catch (NullPointerException e)
                {
                    //e.printStackTrace();

                    /**   it couldnt find either terminal or nonTerminal
                     *
                     *  Situation 1: wrong input
                     *  Situation 2: A->null
                     *
                     * **/

                    if (right_hand_token.equals("null"))
                    {
                        /** Add Follow Set **/
                        // get terminals of FollowSet of T
                        List<String> follow_terminals = followSet.get(nt);

                        // for each terminal t in Follow(A) add to table[A,t] with generator A->null
                        for (int i = 0; i < follow_terminals.size(); i++) {
                            String temp_follow_terminal = follow_terminals.get(i);
                            if (!temp_follow_terminal.equals("null"))
                                util.fillAContentInTable(table, nt, temp_follow_terminal, t, terminals, nonTerminals);

                        }
                    }
                    else {
                        System.err.println("ERROR! wrong input: "+right_hand_token);

                    }

                }


             /**   Consider First Set             **/

                /**     e.g. A->+FD            **/
                //the first part of the right part of generator like if, +    see below
                if (type.equals("terminal")) {

                    // e.g. add if    table[A][if]  =  A->if{B}{A}
                    // or e.g. add +  table[A][+] = A->+FD
                    //table = table[][] ,nt = nonTerminal (A) , str = Terminal(if),t = generator(A->if(B){A}~), the rest are same.
                     util.fillAContentInTable(table,nt,str,t,terminals,nonTerminals);


                     /*** e.g  T->FD ***/
                } else if (type.equals("nonTerminal")) {

                    /*** for each terminal t in First(FD) or First(right_hand_token), add Table[T,t] = generator  ***/

                    boolean isContainsNullInFirst = false;
                    // e.g F
                    String per_non_terminal = right_hand_token.charAt(0) + "";
                    int index_of_r = 0;
                    while (!per_non_terminal.isEmpty()) {

                        // F is nonTerminal
                        if (util.isNonTerminal(per_non_terminal, nonTerminals)) {

                            // First(F)={+,),null}
                            List<String> firsts = firstSet.get(per_non_terminal);

                            for (int i = 0; i < firsts.size(); i++) {
                                // if it is null, set flag
                                if (firsts.get(i).equals("null")) {
                                    isContainsNullInFirst = true;
                                }
                                // if it is not null, add to table
                                else {
                                    util.fillAContentInTable(table, nt, firsts.get(i), t, terminals, nonTerminals);

                                }

                            }

                            // a nonTerminal is done ,go on e.g.  A->FD , F is done , now deal with D
                            try {
                                index_of_r++;
                                per_non_terminal = right_hand_token.charAt(index_of_r) + "";
                            } catch (NullPointerException e) {
                                //it is already the end, so break
                                per_non_terminal = "";
                                break;
                            }


                            // if is terminal
                        } else if (util.isTerminal(per_non_terminal, terminals))
                        //e.g. A->F+D
                        {
                            if (isContainsNullInFirst)
                            //null is in F , so add +
                            {
                                util.fillAContentInTable(table, nt, per_non_terminal, t, terminals, nonTerminals);
                                break;
                            } else {
                                // A->if(B) wont happen here because see above , we've already dealt with it(added into the table)
                                // null is not in F , so do nothing further

                                break;
                            }

                        } else {
                            // not a terminal or either a nonTerminal like 'i' still needs 'f' to become 'if'
                            index_of_r++;
                            per_non_terminal += right_hand_token.charAt(index_of_r);

                        }
                    }


                    /**   Consider Follow Set             **/

                    /*** if (null is in First(FD)) , for each terminals t in Follow(T) , add Table[T,t] = generator ***/

                    // FD can derive null, not directly equals to null
                    if (isContainsNullInFirst) {

                        // get terminals of FollowSet of T
                        List<String> follow_terminals = followSet.get(nt);

                        for (int i = 0; i < follow_terminals.size(); i++) {
                            String temp_follow_terminal = follow_terminals.get(i);
                            if (!temp_follow_terminal.equals("null"))
                                util.fillAContentInTable(table, nt, temp_follow_terminal, t, terminals, nonTerminals);

                        }

                    }
                }

            }


            //System.out.println("key="+entry.getKey()+","+"value="+entry.getValue());
        }
        printTable(table,nonTerminals.size(),terminals.size());
        return table;
    }

    public void printTable(String [][]table,int x,int y)
    {
        System.out.println("\n ");
        for (int i=0;i<x;i++)
        {

            for (int j=0;j<y;j++)
            {
                System.out.print(table[x][y]+"\t");

            }
            System.out.println(" ");

        }
        System.out.println("\n ");

    }



}
