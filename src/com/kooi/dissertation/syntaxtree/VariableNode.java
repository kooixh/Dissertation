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

	public VariableNode(String value,DataType type) {
		super(value);
		this.type = type;
	}
	
	public VariableNode(String value,Node left,Node right,DataType type) {
		super(value,left,right);
		this.type = type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		VariableNode other = (VariableNode) obj;
		if (left == null) {
			if (other.getLeft() != null)
				return false;
		} else if (!left.equals(other.getLeft()))
			return false;
		if (right == null) {
			if (other.getRight() != null)
				return false;
		} else if (!right.equals(other.getRight()))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


}
