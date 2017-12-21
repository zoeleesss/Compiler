/**
 * 
 */
package getToken;
import java.util.regex.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lw
 *
 */
public class Test {
	public static void getTokens(String file) throws IOException
	{
		String pathname = file;
	    File filename = new File(pathname);
	    InputStreamReader reader;
		try {
			ChangeStringToToken gettoken=new ChangeStringToToken();
			reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			String line ="";
			line = br.readLine();
		    try {
		    	 while (line != null)
		    	 {  
	                if(line != null)
	                {
		                String[] arr =line.trim().split(" ");
						for(int i=0;i<=arr.length-1;i++)
						{
							gettoken.getType(arr[i]);
						}
	                }
	                line = br.readLine(); 
	             }  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String args[]) throws IOException
	{
		getTokens("src/sToken1.cpp");//输入文件路径
		String line="";
		for(int i=0;i<=ChangeStringToToken.tokenlist.size()-1;i++)
		{
			System.out.print(ChangeStringToToken.tokenlist.get(i));
			System.out.println(ChangeStringToToken.typelist.get(i));
			line=ChangeStringToToken.tokenlist.get(i)+ChangeStringToToken.typelist.get(i);//一行结果
		}
	}
		
}
