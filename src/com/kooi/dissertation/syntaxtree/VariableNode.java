package com.kooi.dissertation.syntaxtree;


/**
 * 
 * A class to represent a variable node. A variable node does not need the name value to match,
 * just same type. 
 * 
 * 
 * @author Kooi
 * @date 12 February 2019
 *
 */
public class VariableNode extends ASTNode {
	
	
	private DataType type;

	public DataType getType() {
		return type;
	}

	public VariableNode(String value,DataType type) {
		super(value);
		this.type = type;
	}
	
	public VariableNode(String value,Node left,Node right,DataType type) {
		super(value,left,right);
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.getValue()+"(v):"+this.type;
	}


}
