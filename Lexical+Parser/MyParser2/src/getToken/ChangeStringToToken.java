/**
 * 
 */
package getToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.*;
/**
 * @author lw
 *
 */
public class ChangeStringToToken {
	public static List<String> typelist =new ArrayList();
	public static List<String> tokenlist =new ArrayList();
	private static final String myInt="-?[1-9][0-9]*|0";
	private static final String myDouble="-?[1-9][0-9]*\\.[0-9]*|-?0\\.[0-9]*";
	private static final String myIdentifier="([a-z]|[A-Z])+([A-Z]|[a-z]|[0-9]|_)*";
	private static String headToken="";
	@SuppressWarnings("unchecked")
	public static void getType(String stmt)
	{ 
		if(Pattern.matches("int", stmt))
		{
			tokenlist.add("int"+"@");
			typelist.add("int");
		}
		else if(Pattern.matches("double", stmt))
		{
			tokenlist.add("double"+"@");
			typelist.add("double");
		}
		else if(Pattern.matches(",", stmt))
		{
			tokenlist.add(","+"@");
			typelist.add(",");
		}
		else if(Pattern.matches("while", stmt))
		{
			tokenlist.add("while"+"@");
			typelist.add("while");
		}
		else if(Pattern.matches("\\(", stmt))
		{
			tokenlist.add("("+"@");
			typelist.add("(");
		}
		else if(Pattern.matches("\\)", stmt))
		{
			tokenlist.add(")"+"@");
			typelist.add(")");
		}
		else if(Pattern.matches("\\{", stmt))
		{
			tokenlist.add("{"+"@");
			typelist.add("{");
		}
		else if(Pattern.matches("\\}", stmt))
		{
			tokenlist.add("}"+"@");
			typelist.add("}");
		}
		else if(Pattern.matches("\\+", stmt))
		{
			tokenlist.add("+"+"@");
			typelist.add("+");
		}
		else if(Pattern.matches("\\-", stmt))
		{
			tokenlist.add("-"+"@");
			typelist.add("-");
		}
		else if(Pattern.matches("\\*", stmt))
		{
			tokenlist.add("*"+"@");
			typelist.add("*");
		}
		else if(Pattern.matches("\\/", stmt))
		{
			tokenlist.add("/"+"@");
			typelist.add("/");
		}
		else if(Pattern.matches("~", stmt))
		{
			tokenlist.add("~"+"@");
			typelist.add("~");
		}
		else if(Pattern.matches("=", stmt))
		{
			tokenlist.add("="+"@");
			typelist.add("=");
		}
		else if(Pattern.matches("if", stmt))
		{
			tokenlist.add("if"+"@");
			typelist.add("if");
		}
		else if(Pattern.matches("else", stmt))
		{
			tokenlist.add("else"+"@");
			typelist.add("else");
		}
		else if(Pattern.matches(myInt, stmt))	//INT DIGIT
		{
			tokenlist.add("IDIGIT"+"@");
			typelist.add(stmt);
		}
		else if(Pattern.matches(myDouble, stmt))
		{
			tokenlist.add("DDIGIT"+"@");
			typelist.add(stmt);
		}
		else if(Pattern.matches("def", stmt))
		{
			tokenlist.add("def"+"@");
			typelist.add("def");
		}
		else if(Pattern.matches("break", stmt))
		{
			tokenlist.add("break"+"@");
			typelist.add("break");
		}
		else if(Pattern.matches("return", stmt))
		{
			tokenlist.add("return"+"@");
			typelist.add("return");
		}
		else if(Pattern.matches(myIdentifier, stmt))
		{
			tokenlist.add("identifier"+"@");
			typelist.add(stmt);
		}
		else if(Pattern.matches("<<", stmt))
		{
			tokenlist.add("<<"+"@");
			typelist.add("<<");
		}
		else if(stmt.contains("+")||stmt.contains("-")||stmt.contains("*")||stmt.contains("/")||stmt.contains("=")||(stmt.contains("(")||stmt.contains(")"))||stmt.contains("~")||(stmt.contains("{")||stmt.contains("}")||stmt.contains("<<")||stmt.contains(",")))
		{
			String temp="";
			StringTokenizer st = new StringTokenizer(stmt, "\\+|~|\\-|\\*|\\/|\\=|\\(|\\)|\\{|\\}|<<|,", true);
			while(st.hasMoreElements())
			{
				temp=st.nextToken();
				if(temp.equals("<")&&temp.length()<=2)
				{
					temp+=st.nextToken();
				}
				else if(temp.equals("-")&& !(Pattern.matches(myInt,headToken)||Pattern.matches(myDouble,headToken)||Pattern.matches(myIdentifier,headToken)||Pattern.matches("\\)",headToken)))
				{
					String next=st.nextToken();
					if(Pattern.matches(myIdentifier,next)||Pattern.matches("\\(",next))
					{
						temp="0-"+next;
					}
					else
					{
						temp+=next;
					}
				}
				//System.out.println(temp);
				getType(temp);
				headToken=temp;
			}
		}
	}
}
