package parser.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	public static List<String> myGrammarStringList = new ArrayList<String>();
	public static List<String> myTokenList = new ArrayList<String>(); 

	/**
	 * 从文件中读取文法
	 */
	public static List<String> getGrammarFromFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"src/ourGrammar"));
			String mGrammar = br.readLine();

			while (mGrammar != null) {
				if (mGrammar.equals("\n"))
					continue;
				myGrammarStringList.add(mGrammar);
				mGrammar = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myGrammarStringList;
	}
	
	/**
	 * 从文件中读取token
	 * @return token list
	 */
	public static List<String> getTokenFormFile(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"src/ownT.txt"));
			String mToken = br.readLine();

			while (mToken != null) {
				myTokenList.add(mToken);
				mToken = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myTokenList;
	}
}
