package com.kooi.dissertation.syntaxtree;


/**
 * 
 * This class represents an operator. For an operator to match, the return type and 
 * the value must match.
 * 
 * @author Kooi
 * @date 12 February 2019
 *
 */
public class OperatorNode extends ASTNode {
	
	private DataType returnType;
	
	public OperatorNode(String value, DataType type) {
		super(value);
		this.returnType = type;
	}
	public OperatorNode(String value,Node left,Node right, DataType type) {
		super(value,left,right);
		this.returnType = type;
	}

	public DataType getReturnType() {
		return returnType;
	}
	@Override
	public String toString() {
		return this.getValue()+"(o):"+this.returnType;
	}

}
