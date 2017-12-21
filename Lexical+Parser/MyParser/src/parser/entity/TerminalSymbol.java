package parser.entity;
import java.util.ArrayList;
import java.util.List;

public class TerminalSymbol {

	private String value;
	private List<String> first;

	public TerminalSymbol(String value) {
		this.value = value;
		this.first = new ArrayList<String>();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getFirst() {
		return first;
	}

	public void setFirst(List<String> first) {
		this.first = first;
	}
}
