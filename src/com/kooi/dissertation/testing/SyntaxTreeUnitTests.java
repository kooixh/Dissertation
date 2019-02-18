package com.kooi.dissertation.testing;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.kooi.dissertation.syntaxtree.ASTNode;
import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Node;
import com.kooi.dissertation.syntaxtree.NodeType;
import com.kooi.dissertation.syntaxtree.Operator;
import com.kooi.dissertation.syntaxtree.UnaryOperator;

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
	public void testEquals() {
		
		//create node 1
		Node node1 = new ASTNode("+",DataType.INT,NodeType.OPERATOR);
		Node node1_left = new ASTNode("x",DataType.INT,NodeType.VARIABLE);
		ASTNode node1_right = new ASTNode("y",DataType.INT,NodeType.VARIABLE);
		((ASTNode)node1).setLeft(node1_left);
		((ASTNode)node1).setRight(node1_right);

		
		
		//create node 2 with same values as node one but different objects
		Node node2 = new ASTNode("+",DataType.INT,NodeType.OPERATOR);
		Node node2_left =  new ASTNode("x",DataType.INT,NodeType.VARIABLE);
		ASTNode node2_right = new ASTNode("y",DataType.INT,NodeType.VARIABLE);
		((ASTNode)node2).setLeft(node2_left);
		((ASTNode)node2).setRight(node2_right);
		
		assertNotSame(node1,node2);
		assertEquals(node1.hashCode(),node2.hashCode());
		
		assertNotSame(node1,node2);
		assertEquals(node1,node2);
	}
	
	
	@Test
	public void testEquals2() {
		
		
		//create node 1
		Node node1 = new ASTNode("+",DataType.INT,NodeType.OPERATOR);
		Node node1_left = new ASTNode("x",DataType.INT,NodeType.VARIABLE);
		ASTNode node1_right = new ASTNode("y",DataType.INT,NodeType.VARIABLE);
		((ASTNode)node1).setLeft(node1_left);
		((ASTNode)node1).setRight(node1_right);
		
		Node node1_root = new ASTNode("+_root",null,node1,DataType.INT,NodeType.OPERATOR);
		
		//create node 2 with same values as node one but different objects
		Node node2 = new ASTNode("+",DataType.INT,NodeType.OPERATOR);
		Node node2_left = new ASTNode("x",DataType.INT,NodeType.VARIABLE);
		Node node2_right = new ASTNode("y",DataType.INT,NodeType.VARIABLE);
		((ASTNode)node2).setLeft(node2_left);
		((ASTNode)node2).setRight(node2_right);

		assertEquals(node1_root.getRight(),node2);
	}
	
	@Test
	public void testNotEquals() {
		
		//create node 1
		Node node1 = new ASTNode("+",DataType.INT,NodeType.OPERATOR);
		Node node1_left = new ASTNode("x",DataType.INT,NodeType.VARIABLE);
		ASTNode node1_right = new ASTNode("y",DataType.INT,NodeType.VARIABLE);
		((ASTNode)node1).setLeft(node1_left);
		((ASTNode)node1).setRight(node1_right);
		
		Node node1_root = new ASTNode("+_root",null,node1,DataType.INT,NodeType.OPERATOR);
		
		//create node 2 with same values as node one but different objects
		Node node2 = new ASTNode("+",DataType.INT,NodeType.OPERATOR);
		Node node2_left = new ASTNode("x",DataType.INT,NodeType.VARIABLE);
		Node node2_right = new ASTNode("z",DataType.INT,NodeType.VARIABLE);
		((ASTNode)node2).setLeft(node2_left);
		((ASTNode)node2).setRight(node2_right);
;
		
		assertNotSame(node1,node2);
		assertFalse(node1.equals(node2));
	}
	
	@Test
	public void testNotEquals2() {
		
		
		
		//create node 1
		Node node1 = new ASTNode("+",DataType.INT,NodeType.OPERATOR);
		Node node1_left = new ASTNode("x",DataType.INT,NodeType.VARIABLE);
		ASTNode node1_right = new ASTNode("y",DataType.INT,NodeType.VARIABLE);
		((ASTNode)node1).setLeft(node1_left);
		((ASTNode)node1).setRight(node1_right);
		
		Node node1_root = new ASTNode("+_root",null,node1,DataType.INT,NodeType.OPERATOR);
		
		//create node 2 with same values as node one but different objects
		Node node2 = new ASTNode("+",DataType.INT,NodeType.OPERATOR);
		Node node2_left = new ASTNode("x",DataType.INT,NodeType.VARIABLE);
		Node node2_right = new ASTNode("z",DataType.INT,NodeType.VARIABLE);
		((ASTNode)node2).setLeft(node2_left);
		((ASTNode)node2).setRight(node2_right);
;
		
		assertNotSame(node1,node2);
		assertFalse(node1_root.equals(node2));
	}
	
		
	@Test
	public void testCompareBinaryBinary1() {
		
		Operator o1 = new BinaryOperator("*",3,DataType.INT);
		Operator o2 = new BinaryOperator("+",2,DataType.INT);
		
		int expected = 1;
		assertEquals(expected,o1.comparePrecedence(o2));
	}
	
	@Test
	public void testCompareBinaryBinary2() {
		
		Operator o1 = new BinaryOperator("*",3,DataType.INT);
		Operator o2 = new BinaryOperator("+",2,DataType.INT);
		
		int expected = -1;
		assertEquals(expected,o2.comparePrecedence(o1));
	}
	
	@Test
	public void testCompareBinaryBinary3() {
		
		Operator o1 = new BinaryOperator("-",2,DataType.INT);
		Operator o2 = new BinaryOperator("+",2,DataType.INT);
		
		int expected = 0;
		assertEquals(expected,o2.comparePrecedence(o1));
	}
	
	@Test
	public void testCompareBinaryBinary4() {
		
		Operator o1 = new BinaryOperator("*",3,DataType.INT);
		Operator o2 = new BinaryOperator("/",3,DataType.INT);
		
		int expected = 0;
		assertEquals(expected,o2.comparePrecedence(o1));
	}
	
	@Test
	public void testCompareUnaryBinary1() {
		
		Operator o1 = new BinaryOperator("*",3,DataType.INT);
		Operator o2 = new UnaryOperator("!",1,DataType.INT);
		
		int expected = 1;
		assertEquals(expected,o2.comparePrecedence(o1));
	}
	
	@Test
	public void testCompareUnaryBinary2() {
		
		Operator o1 = new BinaryOperator("*",3,DataType.INT);
		Operator o2 = new UnaryOperator("!",1,DataType.INT);
		
		int expected = -1;
		assertEquals(expected,o1.comparePrecedence(o2));
	}
	
	@Test
	public void testCompareUnaryUnary1() {
		
		Operator o1 = new UnaryOperator("-",1,DataType.INT);
		Operator o2 = new UnaryOperator("!",1,DataType.INT);
		
		int expected = 0;
		assertEquals(expected,o1.comparePrecedence(o2));
	}
	
	@Test
	public void testComparePrecedenceUnaryUnary2() {
		
		Operator o1 = new UnaryOperator("-",1,DataType.INT);
		Operator o2 = new UnaryOperator("!",2,DataType.INT);
		
		int expected = -1;
		assertEquals(expected,o1.comparePrecedence(o2));
	}
	
	@Test
	public void testComparePrecedenceUnaryUnary3() {
		
		Operator o1 = new UnaryOperator("-",1,DataType.INT);
		Operator o2 = new UnaryOperator("!",2,DataType.INT);
		
		int expected = 1;
		assertEquals(expected,o2.comparePrecedence(o1));
	}

}
