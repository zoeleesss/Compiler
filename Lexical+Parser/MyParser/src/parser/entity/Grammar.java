package parser.entity;
import java.util.ArrayList;
import java.util.List;

public class Grammar {

	private String mGrammar;
	private String leftPart;
	private List<String> rightPart;
	private List<String> select;

	public Grammar(String mGrammer) {
		this.mGrammar = mGrammer;
		this.select = new ArrayList<String>();
		this.rightPart = new ArrayList<String>();
	}

	public String getmGrammar() {
		return mGrammar;
	}

	public void setmGrammar(String mGrammar) {
		this.mGrammar = mGrammar;
	}

	public String getLeftPart() {
		return leftPart;
	}

	public void setLeftPart(String leftPart) {
		this.leftPart = leftPart;
	}

	public List<String> getRightPart() {
		return rightPart;
	}

	public void setRightPart(List<String> rightPart) {
		this.rightPart = rightPart;
	}

	public List<String> getSelect() {
		return select;
	}

	public void setSelect(List<String> select) {
		this.select = select;
	}

}
