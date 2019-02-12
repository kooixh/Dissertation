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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
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
		
		OperatorNode other = (OperatorNode) obj;
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
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
