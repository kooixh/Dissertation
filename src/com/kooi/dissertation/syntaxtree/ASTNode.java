package com.kooi.dissertation.syntaxtree;

/**
 * This class represents a node for an abstract syntax tree. the AST is in the form 
 * of a binary tree with each node having a string value.
 * 
 * 
 * 
 * @author Kooi
 * @date 30th January 2019
 *
 */
public abstract class ASTNode implements Node{
	
	
	//fields
	protected String value;
	protected Node left;
	protected Node right;
	
	
	//constructors
	public ASTNode(String value) {
		this.value = value;
		left = null;
		right = null;
	}
	public ASTNode(String value,Node left,Node right) {
		this.value = value;
		this.left = left;
		this.right = right;
	}
	

	
	//getters and setters
	public void setRight(Node right) {
		this.right = right;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Node getLeft() {
		return left;
	}
	public void setLeft(Node left) {
		this.left = left;
	}
	public Node getRight() {
		return right;
	}
	
	//overriding hashcode and equals. These will be used for 
	@Override
	public abstract int hashCode();
	@Override
	public abstract boolean equals(Object obj);

}
