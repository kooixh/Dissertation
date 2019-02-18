package com.kooi.dissertation.rewriter;

import com.kooi.dissertation.syntaxtree.Node;

/**
 * 
 * This class represents a rewrite rule. A rule is made up of two terms. Here the 
 * terms are fields with the root of their respective syntax tree.
 * 
 * 
 * @author Kooi
 * @date 13 February 2019 
 *
 */
public class RewriteRule {
	
	
	private Node lhs;
	private Node rhs;
	private String name;
	
	
	public RewriteRule(Node l, Node r, String name) {
		this.lhs = l;
		this.rhs = r;
		this.name = name;
	}

	//getters
	public Node getLhs() {
		return lhs;
	}
	public Node getRhs() {
		return rhs;
	}
	public String getName() {
		return name;
	}
	
	
	

}
