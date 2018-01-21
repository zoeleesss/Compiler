package parser.main;

import parser.entity.*;
import parser.util.*;
import getToken.*;
import translation.SemanticAnalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import changeType.ChangeType;
import changeType.CompileFile;
import changeType.SaveFile;

public class SyntacticAnalysis {

	public static List<Token> tokenList = new ArrayList<Token>();
	public static List<String> valueList=new ArrayList<>();


	/**
	 * 将文件中的文法可分解为多个子式的文法分解
	 *
	 * @param list
	 *            文法列表
	 */
	private void getGrammarList(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			String replaceString = list.get(i).replace('|', '@'); // replace函数使用正则表达式，'|'在正则表达式中为特殊字符，故替换为'@'

			if (replaceString.indexOf("@") != -1) {
				String[] arrStrings = replaceString.split("@");
				MyUtil.grammarList.add(new Grammar(arrStrings[0]));
				String[] expLeft = arrStrings[0].split("->");
				for (int j = 1; j < arrStrings.length; j++) {
					MyUtil.grammarList.add(new Grammar(expLeft[0] + "->"
							+ arrStrings[j]));
				}
			} else {
				MyUtil.grammarList.add(new Grammar(replaceString));
			}
		}
		System.out.println("文法表达式：");
		for (int k = 0; k < MyUtil.grammarList.size(); k++) {
			System.out.println(MyUtil.grammarList.get(k).getmGrammar());
		}
		System.out.println();
	}

	/**
	 * 获取文法中的非终结符,保存到nonTerminalSymbolMap
	 */
	private void splitNonTerminalSymbol() {
		for (int i = 0; i < MyUtil.grammarList.size(); i++) {
			String singleGrammar = MyUtil.grammarList.get(i).getmGrammar();
			String[] part = singleGrammar.split("->");
			if (part.length == 2) {
				MyUtil.grammarList.get(i).setLeftPart(part[0]);
				String[] right = part[1].split(" ");
				MyUtil.grammarList.get(i).setRightPart(MyUtil.getInstance().toList(right)); // 将数组转换为List
				if (!MyUtil.nonTerminalSymbolMap.containsKey(part[0]))
					MyUtil.nonTerminalSymbolMap.put(part[0],
							new NonTerminalSymbol(part[0]));
			}
		}
		System.out.println("非终结符：");
		for (String string : MyUtil.nonTerminalSymbolMap.keySet()) {
			System.out.print(string + "\t");
		}
		System.out.println();
	}

	/**
	 * 分隔出文法中的终结符，保存到terminalSymbolMap
	 */
	private void splitTerminalSymbol() {
		for (int i = 0; i < MyUtil.grammarList.size(); i++) {
			List<String> rightPart = new ArrayList<String>();
			MyUtil.getInstance().combineList(rightPart, MyUtil.grammarList.get(i)
					.getRightPart());
			for (String keyString : MyUtil.nonTerminalSymbolMap.keySet()) {
				// 文法中非终结符默认为一个字符
				if (rightPart.contains(keyString))
					rightPart.remove(keyString);
			}
			for (int k = 0; k < rightPart.size(); k++) { // 终结符
				String mtermial = rightPart.get(k);
				if (!MyUtil.terminalSymbolMap.containsKey(mtermial)) // 空仍存在于终结符集中
					MyUtil.terminalSymbolMap.put(mtermial, new TerminalSymbol(
							mtermial));
			}
		}
		System.out.println("终结符：");
		for (String string : MyUtil.terminalSymbolMap.keySet()) {
			System.out.print(string + "\t");
		}
		System.out.println();
	}

	/**
	 * 完成预测分析法的准备工作，构造非终结符的FIRST集、FOLLOW集、SELECT集
	 */
	public void preparationWork() {
		SyntacticAnalysis sAnalysis = new SyntacticAnalysis();
		sAnalysis.getGrammarList(FileUtil.getGrammarFromFile());
		sAnalysis.splitNonTerminalSymbol();
		sAnalysis.splitTerminalSymbol();
	}

	/**
	 * 获取非终结符Select集
	 */
	public void getSelect() {
		System.out.println("文法表达式的SELECT集：");
		for (int i = 0; i < MyUtil.grammarList.size(); i++) {
			Grammar grammar = MyUtil.grammarList.get(i);
			List<String> mselect = MyUtil.getInstance().SELECT(grammar);
			if (mselect != null) {
				for (String str : mselect) {
					grammar.getSelect().add(str);
				}
			}
			System.out.println("SELECT(" + grammar.getmGrammar() + ") = "
					+ grammar.getSelect());
		}
		System.out.println();
	}

	/**
	 * 获取非终结符的Follow集
	 */
	public void getFollow() {
		System.out.println("非终结符的FOLLOW集：");
		MyUtil.nonTerminalSymbolMap
				.get(MyUtil.grammarList.get(0).getLeftPart()).getFollowList()
				.add("#");
		MyUtil.whetherChanged = true;
		while (MyUtil.whetherChanged) {
			MyUtil.whetherChanged = false;
			MyUtil.getInstance().FOLLOW();
		}
		for (String key : MyUtil.nonTerminalSymbolMap.keySet()) {
			List<String> mfollow = MyUtil.nonTerminalSymbolMap.get(key)
					.getFollowList();
			System.out.println("FOLLOW(" + key + ") = " + mfollow);
		}
		System.out.println();
	}

	/**
	 * 获取终结符、非终结符的First集
	 */
	public void getFrist() {
		System.out.println();
		System.out.println("终结符的FIRST集：");
		for (String key : MyUtil.terminalSymbolMap.keySet()) {
			MyUtil.terminalSymbolMap.get(key).getFirst().add(key);
			List<String> mfirst = MyUtil.terminalSymbolMap.get(key).getFirst();
			System.out.println("FIRST(" + key + ") = " + mfirst);
		}
		System.out.println();

		System.out.println("非终结符的FIRST集：");
		for (String key : MyUtil.nonTerminalSymbolMap.keySet()) {
			List<String> mfrist = MyUtil.getInstance().FIRST(MyUtil.nonTerminalSymbolMap
					.get(key));
			MyUtil.nonTerminalSymbolMap.get(key).setFirst(mfrist);
			System.out.println("FIRST(" + key + ") = " + mfrist);
		}
		System.out.println();
	}

	/**
	 * 构造预测分析表
	 */
	public void getAnalysisTable() {


		getHigherNTS();
		for(String aString : MyUtil.nonTerminalSymbolMap.keySet()){
			List<NonTerminalSymbol> list = MyUtil.nonTerminalSymbolMap.get(aString).getHigherNTS();
			System.out.print(aString+":\t\t");
			for(int i = 0;i < list.size();i++)
				System.out.print(list.get(i).getValue()+"\t");
			//System.out.println();
		}
		
		for (String keyString : MyUtil.nonTerminalSymbolMap.keySet()) {
			MyUtil.getInstance().SYNCH(MyUtil.nonTerminalSymbolMap.get(keyString)); // 计算同步记号集
		}

		System.out.println("预测分析表M");
		MyUtil.getInstance().getAnalysisTable();
		for (String nts : MyUtil.analysisTable.keySet()) {
			System.out.print(nts + "\t");
			for (String ts : MyUtil.analysisTable.get(nts).keySet()) {
				System.out.print(ts
						+ "->"
						+ MyUtil.getInstance().ListToString(MyUtil.analysisTable.get(nts)
								.get(ts)) + "  ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * 获取token串，并进行相应的处理
	 */
	public void getToken() {
		List<String> mtoken = FileUtil.getTokenFormFile();
		for (int i = 0; i < mtoken.size(); i++) {
			String[] splitString = mtoken.get(i).split("@");
			Token token = new Token();
			if (splitString.length == 2) {
				token.setSyn(splitString[0]);
				token.setValue(splitString[1]);
				valueList.add(splitString[1]);
				tokenList.add(token);
			}
		}
	}




	/**
	 * 由token序列构造待分析的句子
	 */
	public List<String> getSentence() {
		List<String> sentence = new ArrayList<String>();
		for (int i = 0; i < tokenList.size(); i++) {
			sentence.add(tokenList.get(i).getSyn());
		}
		sentence.add("#");
		return sentence;
	}
	
	/**
	 * 获取高层结构的集合
	 */
	public void getHigherNTS(){
		MyUtil.whetherChanged = true;
		while (MyUtil.whetherChanged) {
			MyUtil.whetherChanged = false;
			MyUtil.getInstance().calcHigherGroup();

		}
	}

	static List<MyObject> leaves=MyUtil.leaves;
	static List<MyObject> allNodes=MyUtil.allNodes;
	static MyObject root=MyUtil.root;



	public static List<MyObject> findCommonParentNode(MyObject obj)//寻找公共父节点
	{
		System.out.println("==================================================");
		System.out.println("<<<"+obj.name+">>>");
		List<MyObject> CommonParentNode=new  ArrayList<>();
		MyObject parentObject=obj.parent;
		int temp=0;
		int contr=0;
		for(int i=0;i<=allNodes.size()-1;i++)
		{
			if(allNodes.get(i).parent==parentObject)
			{
				System.out.println("<"+allNodes.get(i).name+">");
				CommonParentNode.add(allNodes.get(i));
				for(int j=0;j<=leaves.size()-1;j++)
				{
					if(leaves.get(j)==allNodes.get(i))
					{
						temp=1;
						break;
					}
				}
				if(temp!=1)
				{
					contr=1;
					break;
				}
			}
			temp=0;
		}
		System.out.println("==================================================");
		if(contr==1)
		{
			System.out.println("null");
			return null;
		}
		else
		{
			System.out.println("ok");
			return CommonParentNode;
		}
	}

	public static void main(String[] args) {

		// get Tokens first!
		try {
			Test.API("src/sToken1.cpp", "src/ownT.txt");
		}catch (IOException e)
		{
			e.printStackTrace();
		}

		// Parser below!

		SyntacticAnalysis syntacticAnalysis=new SyntacticAnalysis();
		MyUtil myUtil=MyUtil.getInstance();


		syntacticAnalysis.preparationWork();
		syntacticAnalysis.getFrist();
		syntacticAnalysis.getFollow();
		syntacticAnalysis.getSelect();
		syntacticAnalysis.getToken();

		List<String> sentence = syntacticAnalysis.getSentence();

		// String sentence = "+i+i*+i#"; // 待分析的句子
		if (myUtil.isLL1()) { // 判断文法是否为LL(1)文法
			syntacticAnalysis.getAnalysisTable();
			System.out.println("待分析的句子：" + myUtil.ListToString(sentence) + " : " + myUtil.ListToString(valueList) + "\n");
			System.out.println("语法分析结果：");
			myUtil.myParser(sentence,valueList);
		} else {
			System.out.println("该文法不是LL(1)文法，本编译器不能识别!");
		}

		SemanticAnalysis translation= SemanticAnalysis.getInstance();

		MyObject.print(root,allNodes);
		System.out.println(leaves.size());
		int s=leaves.size();
		for(int i=0;i<=s-1;i++)
		{
			MyObject curr=leaves.get(i);
			if(curr.name.equals("IDIGIT") || curr.name.equals("DDIGIT"))//将IDIGIT值传给父节点
			{
				if (curr.name.equals("IDIGIT")) {
					curr.parent.code += (int) curr.value + "";
					curr.parent.type="int";
				}
				else {
					curr.parent.code += curr.value;
					curr.parent.type = "double";
				}
				leaves.add(curr.parent);
				allNodes.remove(curr);
				leaves.remove(curr);
			}
		}
		MyObject.print(root,allNodes);
		// translation part here!
		while (allNodes.size()!=1) {
			System.out.println(allNodes.size());
			for (MyObject object : leaves) {
				List<MyObject> objects;
				objects = MyObject.findCommonParentNodes(object, leaves, allNodes);
				if (objects != null) {
					System.out.print(object.name + ":\t");

					MyObject.printList(objects);
					translation.distributeFunction(objects);
					break;
				}
			}
		}
		System.out.println("****"+allNodes.get(0).code+"****");
		ChangeType.change();
		SaveFile.saveCpp();
		CompileFile.compileCpp();
	}
}


