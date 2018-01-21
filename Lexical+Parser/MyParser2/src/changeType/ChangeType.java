package changeType;

import translation.SemanticUtil;

public class ChangeType {
	static String finalFile="";
	public static void change(){
		String function = "";
		String notFunction="";
		int flagL=0;
		int flagR=0;
		String raw=SemanticUtil.rawCode;
		System.out.println("======ChangeType======");
		//头文件
		 if(raw.contains("cout")){
			 finalFile+="#include <iostream>\n";
		 }
		 finalFile+="#include <stdlib.h>\nusing namespace std;\n";
		 for (int i = 0; i < raw.length(); i++){
			 char temp = raw.charAt(i);
			 function+=String.valueOf(temp);
			 //识别函数并打印
			 if(function.contains(" ")){
				 if(function.contains("void")||function.contains("int")||function.contains("double")){
					 if(function.contains("(")){
						 if(function.contains(")")){
							 if(function.contains("{")){
								 if(function.contains("}")){
									 flagL=count(function,"{");
									 flagR=count(function,"}");
									 if(flagL==flagR){
										 finalFile+=function+"\n";
										 function="";
									 }
								 }
							 }
						 }
					 }
				 }
				 else{
					 System.out.println(function);
					 function="";
					 System.out.println("只发现空格");
				 }
			 }
		 }
		 for (int i = 0; i < raw.length(); i++){
			 char temp = raw.charAt(i);
			 notFunction+=String.valueOf(temp);
			 if(notFunction.contains("}")){
				 notFunction="";
			 } 
		 }
		 finalFile+="int main()\n{"+notFunction+"}";
		 System.out.println(finalFile);
	}

	private static int count(String function,String match){
		int num = 0;
		for(int i=0;i<function.length();i++){
			char temp = function.charAt(i);
			if(String.valueOf(temp).equals(match)) num++;
		}
		return num;
	}
	
	public static String getFile(){
		return finalFile;
	}
}
