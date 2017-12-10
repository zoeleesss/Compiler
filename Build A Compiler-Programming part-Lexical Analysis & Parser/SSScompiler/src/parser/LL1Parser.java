package parser;


import java.util.*;

/**
 * LL1 Parser
 * author @ZoeLee
 * Created by sss on 05/12/2017.
 * copyright ©️ sss 2017-2018
 */
public class LL1Parser {


    public boolean parse(String[][]table,Map<String,List<String>> generators,List<String> terminals, List<String> nonTerminals,List<String> tokenStream)
    {
        /**
         *  initialize 2 stacks
         *  parseStack
         *  tokenStreamStack
         */

        Stack<String> parseStack=new Stack<>();
        Stack<String> tokenStreamStack= new Stack<>();

        // generators.get("program").get(0)     =       program->stmts/whatever     program is the Start symbol

        /*** init parseStack***/
        String generator=generators.get("program").get(0);
        ParserUtil util=ParserUtil.getInstance();
        String right_part_generator=generator.split("->")[1];
        // only typeidentifier=expr  without stmt->
        List<String>generatorList= util.splitGenerator(right_part_generator,terminals,nonTerminals);
        util.reverseAddAll(generatorList,parseStack);
        util.reverseAddAll(tokenStream,tokenStreamStack);

        /*** start repeating util one stack is empty***/

        while(!parseStack.isEmpty())
        {
            String parseStackPointer=parseStack.peek();
            String tokenStreamStackPointer=tokenStreamStack.peek();

            /**  parseStackPointer is a terminal   **/
            if (util.isTerminal(parseStackPointer,terminals))
            {
                // if == then pop an element from each stack
                if (parseStackPointer.equals(tokenStreamStackPointer))
                {
                    System.out.println("parseStack and tokenStreamStack popped "+parseStackPointer);
                    parseStack.pop();
                    tokenStreamStack.pop();
                }
                else{
                    System.err.println("ERROR! terminal in parseStack: "+parseStackPointer+" != terminal in tokenStreamStack"+tokenStreamStackPointer);

                    return false;
                }
            }
            /**  parseStackPointer is a nonTerminal   **/
            else
            {
                List<String> foundGenerator=util.findGeneratorInTable(table,parseStackPointer,tokenStreamStackPointer,terminals,nonTerminals);
                // find generator from the table . e.g. "typeidentifier=expr~" without "stmt->"
                if (!foundGenerator.isEmpty())
                {
                    parseStack.pop();
                    System.out.println("parseStack popped "+parseStackPointer);
                    util.reverseAddAll(foundGenerator,parseStack);
                    System.out.println("use generator "+foundGenerator.toString());


                }else{

                    System.err.println("ERROR! cannot find a generator during parsing! ");
                    return false;
                }

            }
            if (tokenStreamStack.isEmpty())
            {
                if (!parseStack.isEmpty())
                {
                    System.err.println("ERROR! tokenStreamStack is empty but parseStack is NOT empty! ");
                    return false;
                }
            }




        }



        return true;
    }




}
