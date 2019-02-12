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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		
		ConstantNode other = (ConstantNode) obj;
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
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
