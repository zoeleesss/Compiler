package parser.entity;

import java.util.ArrayList;
import java.util.List;

public class NonTerminalSymbol {

	private String value;
	private List<String> first;
	private List<String> followList;
	private List<String> synList;
	private List<NonTerminalSymbol> higherNTS;

	public NonTerminalSymbol(String value) {
		this.value = value;

		this.first = new ArrayList<String>();
		this.followList = new ArrayList<String>();
		this.synList = new ArrayList<String>();
		this.higherNTS = new ArrayList<NonTerminalSymbol>();
	}

	public List<NonTerminalSymbol> getHigherNTS() {
		return higherNTS;
	}

	public void setHigherNTS(List<NonTerminalSymbol> higherNTS) {
		this.higherNTS = higherNTS;
	}

	public List<String> getSynList() {
		return synList;
	}

	public void setSynList(List<String> synList) {
		this.synList = synList;
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

	public List<String> getFollowList() {
		return followList;
	}

	public void setFollowList(List<String> followList) {
		this.followList = followList;
	}

}
