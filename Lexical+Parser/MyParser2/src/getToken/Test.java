/**
 * 
 */
package getToken;
import java.io.*;
import java.util.regex.*;

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
	public static void API(String inputFilePath,String outputFilePath) throws IOException
	{
		getTokens(inputFilePath);//输入文件路径
		String line="";
		for(int i=0;i<=ChangeStringToToken.tokenlist.size()-1;i++)
		{
			//System.out.print(ChangeStringToToken.tokenlist.get(i));
			//System.out.println(ChangeStringToToken.typelist.get(i));
			line+=ChangeStringToToken.tokenlist.get(i)+ChangeStringToToken.typelist.get(i)+"\n";//一行结果

		}
		//System.out.println("文法分析结果：\n"+line);
		FileWriter fileWriter=new FileWriter(outputFilePath);
		fileWriter.write(line);
		fileWriter.close();

	}

	public static void main(String[] args) {

		try {
			API("src/sToken1.cpp","src/ownT.txt");
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
		
}
