package com.kooi.dissertation.rewriter;

import java.util.ArrayList;
import java.util.List;

import com.kooi.dissertation.syntaxtree.Node;

/**
 * 
 * This class is a node used to build a search tree. The search tree can have multiple
 * children. The value held is the root of the term tree
 * 
 * @author Kooi
 * @date 18th March 2019 
 *
 */
public class SearchNode {

	
	private Node termNode; 
	private SearchNode parentNode;
	private String prevRule;
	private List<SearchNode> childNodes;
	
	public SearchNode(Node n,SearchNode par,String prevRule) {
		this.termNode = n;
		this.parentNode = par;
		this.prevRule = prevRule;
		childNodes = new ArrayList<>();
	}
	
	
	/**
	 * Add a new child
	 * 
	 * @param n
	 */
	public void addChild(SearchNode n) {
		this.childNodes.add(n);
	}
	
	//getters
	
	public Node getTermNode() {
		return this.termNode;
	}
	
	public String getPrevRule() {
		return this.prevRule;
	}
	public SearchNode getParentNode() {
		return this.parentNode;
	}
	public List<SearchNode> getChildNodes(){
		return this.childNodes;
	}
	
}
