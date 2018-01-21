package changeType;

public class CompileFile {
	public static void compileCpp(){
		Runtime run = Runtime.getRuntime();
		try{
			Process process = run.exec("g++ /Users/njy97/Desktop/a.cpp -o result");
			process.waitFor();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("======File Compiled======");
	}
}
