package com.kooi.dissertation.testing;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.kooi.dissertation.syntaxtree.ASTNode;
import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Node;
import com.kooi.dissertation.syntaxtree.Operator;
import com.kooi.dissertation.syntaxtree.OperatorNode;
import com.kooi.dissertation.syntaxtree.UnaryOperator;
import com.kooi.dissertation.syntaxtree.VariableNode;

import junit.framework.TestCase;

/**
 * 
 * This is a Junit test class for the ASTNode. 
 * 
 * @author Kooi
 * @date 29th January 2019
 *
 */
public class SyntaxTreeUnitTests extends TestCase {
	
	
	
	/**
	 * 
	 * The test suite below tests the hashcode and equals implementation for the ASTNode
	 * 
	 */
	@Test
	public void testHashCodeVariableNode() {
		
		//create node 1
		Node node1 = new VariableNode("x",DataType.INT);
		Node node1_left = new VariableNode("x left",DataType.INT);
		Node node1_right = new VariableNode("x right",DataType.INT);
		((VariableNode)node1).setLeft(node1_left);
		((VariableNode)node1).setRight(node1_right);
		
		
		//create node 2 with same values as node one but different objects
		Node node2 = new VariableNode("y",DataType.INT);
		Node node2_left = new VariableNode("y left",DataType.INT);
		Node node2_right = new VariableNode("y right",DataType.INT);
		((VariableNode)node2).setLeft(node2_left);
		((VariableNode)node2).setRight(node2_right);
		
		assertNotSame(node1,node2);
		assertEquals(node1.hashCode(),node2.hashCode());
	}
	
	
	@Test
	public void testHashCodeOperatorNode() {
		
		//create node 1
		Node node1 = new OperatorNode("+",DataType.INT);
		Node node1_left = new VariableNode("x",DataType.INT);
		ASTNode node1_right = new VariableNode("y",DataType.INT);
		((OperatorNode)node1).setLeft(node1_left);
		((OperatorNode)node1).setRight(node1_right);

		
		
		//create node 2 with same values as node one but different objects
		Node node2 = new OperatorNode("+",DataType.INT);
		Node node2_left = new VariableNode("x",DataType.INT);
		ASTNode node2_right = new VariableNode("y",DataType.INT);
		((OperatorNode)node2).setLeft(node2_left);
		((OperatorNode)node2).setRight(node2_right);
		
		assertNotSame(node1,node2);
		assertEquals(node1.hashCode(),node2.hashCode());
	}
	
	@Test
	public void testHashCodeDifferentTwoNodes() {
		
		//create node 1
		Node node1 = new OperatorNode("+",DataType.INT);
		Node node1_left = new VariableNode("x",DataType.INT);
		ASTNode node1_right = new VariableNode("y",DataType.INT);
		((OperatorNode)node1).setLeft(node1_left);
		((OperatorNode)node1).setRight(node1_right);

		
		//create node 2 with different values as node one but different objects
		Node node2 = new VariableNode("y",DataType.INT);
		Node node2_left = new VariableNode("y left",DataType.INT);
		Node node2_right = new VariableNode("y right",DataType.INT);
		((VariableNode)node2).setLeft(node2_left);
		((VariableNode)node2).setRight(node2_right);
		
		assertNotSame(node1,node2);
		assertFalse("Same hashcode", node1.hashCode()==node2.hashCode() );
	}
	
	@Test
	public void testEquals() {
		
		//create node 1
		Node node1 = new VariableNode("x",DataType.INT);
		Node node1_left = new VariableNode("x left",DataType.INT);
		Node node1_right = new VariableNode("x right",DataType.INT);
		((VariableNode)node1).setLeft(node1_left);
		((VariableNode)node1).setRight(node1_right);
		
		
		//create node 2 with same values as node one but different objects
		Node node2 = new VariableNode("y",DataType.INT);
		Node node2_left = new VariableNode("y left",DataType.INT);
		Node node2_right = new VariableNode("y right",DataType.INT);
		((VariableNode)node2).setLeft(node2_left);
		((VariableNode)node2).setRight(node2_right);
		
		assertNotSame(node1,node2);
		assertEquals(node1.hashCode(),node2.hashCode());
		
		assertNotSame(node1,node2);
		assertEquals(node1,node2);
	}
	
	@Test
	public void testNotEquals() {
		
		//create node 1
		Node node1 = new OperatorNode("+",DataType.INT);
		Node node1_left = new VariableNode("x",DataType.INT);
		ASTNode node1_right = new VariableNode("y",DataType.INT);
		((OperatorNode)node1).setLeft(node1_left);
		((OperatorNode)node1).setRight(node1_right);

		
		
		//create node 2 with same values as node one but different objects
		Node node2 = new VariableNode("y",DataType.INT);
		Node node2_left = new VariableNode("y left",DataType.INT);
		Node node2_right = new VariableNode("y right",DataType.INT);
		((VariableNode)node2).setLeft(node2_left);
		((VariableNode)node2).setRight(node2_right);
		
		assertNotSame(node1,node2);
		assertFalse(node1.equals(node2));
	}
	
	
	@Test
	public void testHashSetImplementability() {
		//create node 1
		Node node1 = new VariableNode("x",DataType.INT);
		Node node1_left = new VariableNode("x left",DataType.INT);
		Node node1_right = new VariableNode("x right",DataType.INT);
		((VariableNode)node1).setLeft(node1_left);
		((VariableNode)node1).setRight(node1_right);
		
		Set<Node> s = new HashSet<>();
		
		s.add(node1);
		
		//create node 2 with same values as node one but different objects
		Node node2 = new VariableNode("y",DataType.INT);
		Node node2_left = new VariableNode("y left",DataType.INT);
		Node node2_right = new VariableNode("y right",DataType.INT);
		((VariableNode)node2).setLeft(node2_left);
		((VariableNode)node2).setRight(node2_right);
		
		
		assert(s.contains(node2));
	}
	
	
	@Test
	public void testCompareBinaryBinary1() {
		
		Operator o1 = new BinaryOperator("*",3,DataType.INT,DataType.INT,DataType.INT);
		Operator o2 = new BinaryOperator("+",2,DataType.INT,DataType.INT,DataType.INT);
		
		int expected = 1;
		assertEquals(expected,o1.comparePrecedence(o2));
	}
	
	@Test
	public void testCompareBinaryBinary2() {
		
		Operator o1 = new BinaryOperator("*",3,DataType.INT,DataType.INT,DataType.INT);
		Operator o2 = new BinaryOperator("+",2,DataType.INT,DataType.INT,DataType.INT);
		
		int expected = -1;
		assertEquals(expected,o2.comparePrecedence(o1));
	}
	
	@Test
	public void testCompareBinaryBinary3() {
		
		Operator o1 = new BinaryOperator("-",2,DataType.INT,DataType.INT,DataType.INT);
		Operator o2 = new BinaryOperator("+",2,DataType.INT,DataType.INT,DataType.INT);
		
		int expected = 0;
		assertEquals(expected,o2.comparePrecedence(o1));
	}
	
	@Test
	public void testCompareBinaryBinary4() {
		
		Operator o1 = new BinaryOperator("*",3,DataType.INT,DataType.INT,DataType.INT);
		Operator o2 = new BinaryOperator("/",3,DataType.INT,DataType.INT,DataType.INT);
		
		int expected = 0;
		assertEquals(expected,o2.comparePrecedence(o1));
	}
	
	@Test
	public void testCompareUnaryBinary1() {
		
		Operator o1 = new BinaryOperator("*",3,DataType.INT,DataType.INT,DataType.INT);
		Operator o2 = new UnaryOperator("!",1,DataType.INT,DataType.INT);
		
		int expected = 1;
		assertEquals(expected,o2.comparePrecedence(o1));
	}
	
	@Test
	public void testCompareUnaryBinary2() {
		
		Operator o1 = new BinaryOperator("*",3,DataType.INT,DataType.INT,DataType.INT);
		Operator o2 = new UnaryOperator("!",1,DataType.INT,DataType.INT);
		
		int expected = -1;
		assertEquals(expected,o1.comparePrecedence(o2));
	}
	
	@Test
	public void testCompareUnaryUnary1() {
		
		Operator o1 = new UnaryOperator("-",1,DataType.INT,DataType.INT);
		Operator o2 = new UnaryOperator("!",1,DataType.INT,DataType.INT);
		
		int expected = 0;
		assertEquals(expected,o1.comparePrecedence(o2));
	}
	
	@Test
	public void testComparePrecedenceUnaryUnary2() {
		
		Operator o1 = new UnaryOperator("-",1,DataType.INT,DataType.INT);
		Operator o2 = new UnaryOperator("!",2,DataType.INT,DataType.INT);
		
		int expected = -1;
		assertEquals(expected,o1.comparePrecedence(o2));
	}
	
	@Test
	public void testComparePrecedenceUnaryUnary3() {
		
		Operator o1 = new UnaryOperator("-",1,DataType.INT,DataType.INT);
		Operator o2 = new UnaryOperator("!",2,DataType.INT,DataType.INT);
		
		int expected = 1;
		assertEquals(expected,o2.comparePrecedence(o1));
	}

}
