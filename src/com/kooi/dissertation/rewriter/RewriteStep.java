package com.kooi.dissertation.rewriter;

import com.kooi.dissertation.syntaxtree.Node;

/**
 * 
 * This class represents a rewrite step. It has a node represent the term at that 
 * point and the rule is applied to the previous term.
 * 
 * @author Kooi
 * @date 21 February 2019 
 *
 */
public class RewriteStep {
	
	private Node termRoot;
	private String rule; //rule applied to previous step
	
	
	public RewriteStep(Node root, String r) {
		this.termRoot = root;
		this.rule = r;
	}

	
	//getters
	
	public Node getTermRoot() {
		return termRoot;
	}


	public void setTermRoot(Node termRoot) {
		this.termRoot = termRoot;
	}


	public String getRule() {
		return rule;
	}


	public void setRule(String rule) {
		this.rule = rule;
	}
	
}
