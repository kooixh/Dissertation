package com.kooi.dissertation.syntaxtree;

/**
 * 
 * This is a class to represent a constant node. A constant node has is matched using the 
 * value.
 * 
 * @author Kooi
 * @date 12 February 2019
 *
 */
public class ConstantNode extends ASTNode {

	
	//constructor
	public ConstantNode(String value) {
		super(value);
	}
	public ConstantNode(String value,Node left,Node right) {
		super(value,left,right);
	}


	
	@Override
	public String toString() {
		return this.getValue()+"(const)";
	}

}
