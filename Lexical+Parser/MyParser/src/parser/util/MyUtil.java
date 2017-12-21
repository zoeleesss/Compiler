package parser.util;

import parser.entity.Grammar;
import parser.entity.NonTerminalSymbol;
import parser.entity.TerminalSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class MyUtil {

	public static HashMap<String, TerminalSymbol> terminalSymbolMap = new HashMap<String, TerminalSymbol>();
	public static HashMap<String, NonTerminalSymbol> nonTerminalSymbolMap = new HashMap<String, NonTerminalSymbol>();
	public static HashMap<String, HashMap<String, List<String>>> analysisTable = new HashMap<String, HashMap<String, List<String>>>();
	public static List<Grammar> grammarList = new ArrayList<Grammar>();
	public static String ERROR = "error";
	public static String SYN = "synch";
	public static boolean whetherChanged = true;

	private static MyUtil instance=null;

	public static MyUtil getInstance()
	{
		if (instance==null)
		{
			instance=new MyUtil();
		}
		return instance;
	}

	private MyUtil()
	{

	}

	/**
	 * 求非终结符的FIRST集
	 * 
	 * @param nts
	 *            非终结符
	 * @return FIRST集
	 */
	public  List<String> FIRST(NonTerminalSymbol nts) {
		List<Grammar> expList = getMyExp(nts.getValue());
		List<String> mFirst = new ArrayList<String>();
		List<String> right = null;

		mFirst = initFirst(expList, mFirst);// 初始化工作

		Runtime r=Runtime.getRuntime();
		r.gc();
		for (int i = 0; i < expList.size(); i++) {
			right = expList.get(i).getRightPart();
			if (right.size() > 0) {
				if (nonTerminalSymbolMap.containsKey(right.get(0))) {// 若右部表达式的第一位为非终结符则递归
					combineList(mFirst,
							FIRST(new NonTerminalSymbol(right.get(0))));

					int j = 0;
					while (j < right.size() - 1 && isEmptyExp(right.get(j))) { // 若X→Y1…Yn∈P，且Y1...Yi-1⇒*ε，
						combineList(mFirst,
								FIRST(new NonTerminalSymbol(right.get(j + 1))));
						j++;
					}
				}
			}
		}
		if (mFirst.contains("$"))
			mFirst.remove("$");
		combineList(nts.getFirst(), mFirst);
		return mFirst;
	}

	/**
	 * 初始化First集
	 * 
	 * @param expList
	 * @param mFirst
	 */
	public  List<String> initFirst(List<Grammar> expList,
			List<String> mFirst) {
		List<String> right = null;
		for (int i = 0; i < expList.size(); i++) { // 初始化FIRST集
			right = expList.get(i).getRightPart();
			if (right.size() > 0) {
				if (terminalSymbolMap.containsKey(right.get(0))) { // 若右部表达式的第一位为终结符则加入nts的FIRST集
					mFirst.add(right.get(0));
				}
			}
		}
		return mFirst;
	}

	/**
	 * 求一个文法串的FIRST集
	 * 
	 * @param right
	 *            文法产生式的右部
	 * @return FIRST集
	 */
	public List<String> getNTSStringFirst(List<String> right) {
		List<String> mFirst = new ArrayList<String>();

		if (right.size() > 0) { // 初始化,FIRST(α)= FIRST(X1);
			String curString = right.get(0);
			if (terminalSymbolMap.get(curString) != null)
				combineList(mFirst, terminalSymbolMap.get(right.get(0))
						.getFirst());
			else {
				combineList(mFirst, FIRST(new NonTerminalSymbol(curString)));
			}
		}

		if (right.size() > 0) {
			if (nonTerminalSymbolMap.get(right.get(0)) != null) { // 第一个符号为非终结符
				if (right.size() == 1) { // 直接将这个非终结符的first集加入right的first集
					combineList(mFirst, nonTerminalSymbolMap.get(right.get(0))
							.getFirst());
				} else if (right.size() > 1) {
					int j = 0;
					while (j < right.size() - 1 && isEmptyExp(right.get(j))) { // 若Xk→ε,FIRST(α)=FIRST(α)∪FIRST(Xk+1)
						String tString = right.get(j + 1);
						if (nonTerminalSymbolMap.get(tString) != null) {
							combineList(mFirst, FIRST(new NonTerminalSymbol(
									tString)));
						} else {
							combineList(mFirst, terminalSymbolMap.get(tString)
									.getFirst());
						}
						j++;
					}
				}
			}
		}
		return mFirst;
	}

	/**
	 * 求非终结符的FOLLOW集
	 * 
	 *
	 * 非终结符
	 * FOLLOW集
	 */
	public  void FOLLOW() {
		for (int i = 0; i < grammarList.size(); i++) {
			String left = grammarList.get(i).getLeftPart();
			List<String> right = grammarList.get(i).getRightPart();

			for (int j = 0; j < right.size(); j++) {
				String cur = right.get(j);

				if (terminalSymbolMap.containsKey(cur)) // 若是终结符则忽略
					continue;

				if (j < right.size() - 1) {
					List<String> nList = new ArrayList<String>();
					for (int m = j + 1; m < right.size(); m++)
						nList.add(right.get(m));
					List<String> mfirst = getNTSStringFirst(nList);
					for (String str : mfirst) {
						if (!nonTerminalSymbolMap.get(cur).getFollowList()
								.contains(str)) {
							combineList(nonTerminalSymbolMap.get(cur)
									.getFollowList(), mfirst);
							whetherChanged = true;
							break;
						}
					}

					boolean isNull = true; // 判断A→αBβ，且β⇒*ε，其中β能否为空
					for (int k = 0; k < nList.size(); k++) {
						if (!isEmptyExp(nList.get(k))) {
							isNull = false;
							break;
						}
					}
					if (isNull == true) {
						for (String str : nonTerminalSymbolMap.get(left)
								.getFollowList()) {
							if (!nonTerminalSymbolMap.get(cur).getFollowList()
									.contains(str)) {
								combineList(nonTerminalSymbolMap.get(cur)
										.getFollowList(), nonTerminalSymbolMap
										.get(left).getFollowList());
								whetherChanged = true;
								break;
							}
						}
					}
				} else if (j == right.size() - 1) { // 若该非终结符位于右部表达式最后一个字符
					List<String> mfollow = nonTerminalSymbolMap.get(left)
							.getFollowList();
					for (String str : mfollow) {
						if (!nonTerminalSymbolMap.get(cur).getFollowList()
								.contains(str)) {
							combineList(nonTerminalSymbolMap.get(cur)
									.getFollowList(), mfollow);
							whetherChanged = true;
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * 求一个文法表达式的SELECT集
	 * 
	 * @param g
	 *            一个文法表达式
	 * @return SELECT集
	 */
	public List<String> SELECT(Grammar g) {
		List<String> mselect = new ArrayList<String>();
		if (g.getRightPart().size() > 0) {
			if (g.getRightPart().get(0).equals("$")) { // 含有空表达式
				mselect = nonTerminalSymbolMap.get(g.getLeftPart())
						.getFollowList();
				return mselect;
			} else {
				mselect = getNTSStringFirst(g.getRightPart());
				boolean isNull = true;
				for (int i = 0; i < g.getRightPart().size(); i++) { // 如果右部可以推出空
					if (!isEmptyExp(g.getRightPart().get(i))) {
						isNull = false;
						break;
					}
				}
				if (isNull) {
					combineList(mselect,
							nonTerminalSymbolMap.get(g.getLeftPart())
									.getFollowList());
				}
				return mselect;
			}
		}
		return null;
	}

	/**
	 * 获取指定非终结符开头的文法表达式
	 * 
	 * @param nts
	 *            指定非终结符
	 * @return 特定的文法表达式
	 */
	public List<Grammar> getMyExp(String nts) {
		List<Grammar> myExp = new ArrayList<Grammar>();
		for (int i = 0; i < grammarList.size(); i++) {
			Grammar mGrammar = grammarList.get(i);
			if (nts.equals(mGrammar.getLeftPart()))
				myExp.add(mGrammar);
		}
		return myExp;
	}

	/**
	 * 将两个List合并 List1 U List2
	 * 
	 * @param mfirst1
	 *            List1
	 * @param mfirst2
	 *            List2
	 * @return List1
	 */
	public List<String> combineList(List<String> mfirst1,
			List<String> mfirst2) {
		for (int i = 0; i < mfirst2.size(); i++) {
			if (!mfirst1.contains(mfirst2.get(i)))
				mfirst1.add(mfirst2.get(i));
		}
		return mfirst1;
	}

	/**
	 * 将两个List合并 List1 U List2
	 * 
	 * @param
	 *            list1
	 * @param
	 *            list2
	 * @return List1
	 */
	public List<NonTerminalSymbol> combine(
            List<NonTerminalSymbol> list1, List<NonTerminalSymbol> list2) {
		for (int i = 0; i < list2.size(); i++) {
			if (!list1.contains(list2.get(i)))
				list1.add(list2.get(i));
		}
		return list1;
	}

	/**
	 * 判断一个文法符号能否通过N步推导出$，X->$
	 * 
	 * @param nts
	 *            文法符号
	 * @return 是否
	 */
	public boolean isEmptyExp(String nts) {
		if (nonTerminalSymbolMap.get(nts) != null) {
			List<Grammar> expList = getMyExp(nts);
			for (int i = 0; i < expList.size(); i++) {
				Grammar mGrammar = expList.get(i);
				if (mGrammar.getmGrammar().equals(nts + "->$")) // 是否包含X->$表达式
					return true;
				else {
					boolean flag = false;
					List<String> right = mGrammar.getRightPart();
					for (int j = 0; j < right.size(); j++) {
						if (!isEmptyExp(right.get(j))) {
							flag = true;
							break;
						}
					}
					if (flag == false) {
						return true;
					}
					flag = false;
				}
			}
			return false;
		} else { // 终结符不能推出空，直接返回false
			return false;
		}
	}

	/**
	 * 构造预测分析表
	 */
	public void getAnalysisTable() {
		List<String> synString = new ArrayList<String>();
		synString.add(SYN);

		for (int i = 0; i < grammarList.size(); i++) {
			Grammar grammar = grammarList.get(i);
			if (analysisTable.get(grammar.getLeftPart()) != null) { // 如果多个文法表达式的左部相同，则将其合并
				for (String sel : grammar.getSelect()) {
					analysisTable.get(grammar.getLeftPart()).put(sel,
							grammar.getRightPart());
				}
			} else {
				HashMap<String, List<String>> inMap = new HashMap<String, List<String>>();
				for (String sel : grammar.getSelect())
					inMap.put(sel, grammar.getRightPart());
				analysisTable.put(grammar.getLeftPart(), inMap);
			}
			NonTerminalSymbol nts = nonTerminalSymbolMap.get(grammar
					.getLeftPart());
			List<String> msynList = nts.getSynList();
			for (int j = 0; j < msynList.size(); j++) {
				analysisTable.get(grammar.getLeftPart()).put(msynList.get(j), // 将同步记号集加入分析表中
						synString);
			}
		}
	}

	/**
	 * LL（1）语法分析控制程序，输出分析树
	 * 
	 * @param sentence
	 *            分析的句子
	 */
	public void myParser(List<String> sentence) {
		Stack<String> stack = new Stack<String>();
		int index = 0;
		String curCharacter = null;
		stack.push("#");
		stack.push("program");

		while (true) {

			if ((stack.peek().equals("#")))
			{
				System.out.println("Parser Done! Success!");
				break;
			}

			if (index < sentence.size()) // 注意下标，否则越界
				curCharacter = sentence.get(index);
			if (terminalSymbolMap.containsKey(stack.peek())
					|| stack.peek().equals("#")) {
				if (stack.peek().equals(curCharacter)) {
					if (!stack.peek().equals("#")) {
						stack.pop();
						index++;
					}
				} else {
					System.out.println("当前栈顶符号" + stack.pop() + "与"
							+ curCharacter + "不匹配");
				}
			} else {
				List<String> exp = analysisTable.get(stack.peek()).get(
						curCharacter);
				if (exp != null) {
					if (exp.get(0).equals(SYN)) {
						System.out.println("遇到SYNCH，从栈顶弹出非终结符" + stack.pop());
					} else {
						System.out.println(stack.peek() + "->"
								+ ListToString(exp));
						stack.pop();

						for (int j = exp.size() - 1; j > -1; j--) {
							if (!exp.get(j).equals("$")
									&& !exp.get(j).equals(SYN))
								stack.push(exp.get(j));
						}
					}
				} else {
					if (index < sentence.size()-1){
						System.out.println("exp: " + stack.peek() +"  "+analysisTable.get(stack.peek())+"  "+
								curCharacter+"  "+sentence.size());
						System.out.println("忽略" + curCharacter);
						index++;
					}else {
						System.out.println("exp: " + stack.peek()+"   "+analysisTable.get(stack.peek())+"   "+
								curCharacter);
						System.out.println("语法错误，缺少 '}' ！");
						break;
					}
				}
			}


		}
	}

	/**
	 * 判断一个文法是否为LL（1）文法
	 * 
	 * @return 是否
	 */
	public boolean isLL1() {
		for (String keyString : nonTerminalSymbolMap.keySet()) {
			List<Grammar> glist = getMyExp(keyString);
			if (glist.size() > 1) {
				for (int i = 0; i < glist.size(); i++) { // 文法相同左部的SELECT集不相交即为LL(1)文法
					for (int j = 1; j < glist.size(); j++) {
						if (i != j) {
							if (glist.get(i).getSelect()
									.equals(glist.get(j).getSelect())){
								System.out.println(keyString+"为左部的select集相交！");
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 计算同步记号集
	 */
	public void SYNCH(NonTerminalSymbol nts) {
		combineList(nts.getSynList(), nts.getFollowList()); // 把FOLLOW(A)的元素放入A的同步记号集合
		combineList(nts.getSynList(), nts.getFirst()); // 把FIRST(A)的元素放入A的同步记号集合
		List<NonTerminalSymbol> higher = nts.getHigherNTS();
		for (int j = 0; j < higher.size(); j++) { // 把高层结构的First集加入A的同步记号集
			combineList(nts.getSynList(), higher.get(j).getFirst());
		}

		List<String> msynList = nts.getSynList();

		for (int i = 0; i < grammarList.size(); i++) { // 将select集中的元素从A的同步记号集中除去
			Grammar grammar = grammarList.get(i);
			if (grammar.getLeftPart().equals(nts.getValue())) {
				List<String> mselect = grammar.getSelect();
				for (int j = 0; j < mselect.size(); j++) {
					if (msynList.contains(mselect.get(j)))
						msynList.remove(mselect.get(j));
				}
			}
		}
	}

	/**
	 * 将数组转换为ArrayList<String>
	 * 
	 * @param str
	 *            数组
	 * @return list
	 */
	public List<String> toList(String[] str) {
		List<String> myList = new ArrayList<String>();
		for (String s : str)
			myList.add(s);
		return myList;
	}

	/**
	 * 将List的每项合并为字符串
	 * 
	 * @param strings
	 *            list
	 * @return string
	 */
	public String ListToString(List<String> strings) {
		String mystr = "";
		for (int i = 0; i < strings.size(); i++) {
			mystr += strings.get(i) + " ";
		}
		return mystr;
	}

	/**
	 * 判断两个非终结符哪个是更高层的结构
	 * 
	 * @param nts1
	 * @param nts2
	 * @return
	 */
	public boolean isHigher(NonTerminalSymbol nts1,
			NonTerminalSymbol nts2) {
		List<Grammar> mexp = getMyExp(nts1.getValue());
		for (int i = 0; i < mexp.size(); i++) {
			List<String> mright = mexp.get(i).getRightPart();
			for (int j = 0; j < mright.size(); j++) {
				if (mright.get(j).equals(nts2.getValue()))
					return true;
			}
		}
		return false;
	}

	/**
	 * 计算非终结符的高层结构集
	 */
	public void calcHigherGroup() {
		for (String s : nonTerminalSymbolMap.keySet()) {
			NonTerminalSymbol sNonTerminalSymbol = nonTerminalSymbolMap.get(s);
			for (String r : nonTerminalSymbolMap.keySet()) {
				NonTerminalSymbol rNonTerminalSymbol = nonTerminalSymbolMap
						.get(r);
				if (isHigher(sNonTerminalSymbol, rNonTerminalSymbol)) {
					if (!rNonTerminalSymbol.getHigherNTS().contains(
							sNonTerminalSymbol)) {
						rNonTerminalSymbol.getHigherNTS().add(
								sNonTerminalSymbol);
						combine(rNonTerminalSymbol.getHigherNTS(),
								sNonTerminalSymbol.getHigherNTS());
						whetherChanged = true;
					}
				}
			}
		}
	}
}
