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
		else if(Pattern.matches("-?[1-9][0-9]*|0", stmt))
		{
			tokenlist.add("DIGIT"+"@");
			typelist.add(stmt);
		}
		else if(Pattern.matches("-?([1-9])([0-9])*.[0-9]*|0.([0-9])*", stmt))
		{
			tokenlist.add("DIGIT"+"@");
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
		else if(Pattern.matches("([a-z]|[A-Z])+([A-Z]|[a-z]|[0-9]|_)*", stmt))
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
				getType(temp);
			}
		}
	}
}
