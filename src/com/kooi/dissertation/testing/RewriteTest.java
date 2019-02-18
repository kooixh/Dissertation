package com.kooi.dissertation.testing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.Context;
import com.kooi.dissertation.rewriter.RewriteEngine;
import com.kooi.dissertation.rewriter.RewriteRule;
import com.kooi.dissertation.rewriter.RewriteRuleFactory;
import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Operator;
import com.kooi.dissertation.syntaxtree.UnaryOperator;

public class RewriteTest {
	
	
	static RewriteEngine r;
	static RewriteEngine rB;
	static ASTParser p;
	static ASTParser boolP;
	@BeforeClass
	public static void initialise() {
		
		//two rewrite engine
		Set<Operator> ops = new HashSet<>();
		ops.add(new BinaryOperator("+",2,DataType.INT));
		ops.add(new BinaryOperator("-",2,DataType.INT));
		ops.add(new BinaryOperator("*",3,DataType.INT));
		ops.add(new BinaryOperator("/",3,DataType.INT));
		ops.add(new UnaryOperator("!",1,DataType.INT));
		ops.add(new UnaryOperator("succ",2,DataType.INT));
		
		HashMap<String,DataType> variables = new HashMap<>();
		variables.put("x",DataType.INT);
		variables.put("y",DataType.INT);
		
		Set<RewriteRule> rules = new HashSet<>();
		
		Context c = new Context(ops,variables);
		p = new ASTParser(c);
		
		RewriteRuleFactory f = new RewriteRuleFactory(p);
		rules.add(f.getRewriteRule("x+0", "x", "adding zero"));
		rules.add(f.getRewriteRule("0+x", "x", "adding zero"));
		rules.add(f.getRewriteRule("succ(x)+y", "succ(x+y)", "succ rule"));
		
		
		
		
		//boolean engine starts here
		Set<Operator> opsB = new HashSet<>();
		opsB.add(new BinaryOperator("AND",2,DataType.BOOLEAN));
		opsB.add(new BinaryOperator("OR",2,DataType.BOOLEAN));
		opsB.add(new UnaryOperator("NOT",1,DataType.BOOLEAN));
		
		HashMap<String,DataType> variablesB = new HashMap<>();
		variablesB.put("T",DataType.BOOLEAN);
		variablesB.put("F",DataType.BOOLEAN);
		variablesB.put("B",DataType.BOOLEAN);
		
		Context cBool = new Context(opsB,variablesB);
		boolP = new ASTParser(cBool);
		
		RewriteRuleFactory fB = new RewriteRuleFactory(boolP);
		
		Set<RewriteRule> rulesB = new HashSet<>();
		
		rulesB.add(fB.getRewriteRule("NOT NOT B", "B", "double negation"));
		rulesB.add(fB.getRewriteRule("B AND B", "B", "idempotent"));
		rulesB.add(fB.getRewriteRule("True OR B", "True", "identity"));
		rulesB.add(fB.getRewriteRule("B OR True", "True", "identity"));
		rulesB.add(fB.getRewriteRule("False OR False", "False", "identity"));
		rulesB.add(fB.getRewriteRule("B AND False", "False", "AND-identity"));
		rulesB.add(fB.getRewriteRule("False AND B", "False", "AND- identity"));
		
		
		//System.out.println(f.getRewriteRule("x+x", "x", "idempotent").getLhs().getValue());
		
		r = new RewriteEngine(rules,p);
		rB = new RewriteEngine(rulesB,boolP);
	}
	

	@Test
	public void testRewrite1() {
		
		String input = "True AND True";
		String output = rB.rewritePostfix(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite2() {
		
		String input = "NOT NOT True";
		String output = rB.rewritePostfix(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite3() {
		
		String input = "True AND False OR (True AND True)";
		String output = rB.rewritePostfix(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite4() {
		
		String input = "True AND False OR (True AND (False OR False)) AND True AND True";
		String output = rB.rewritePostfix(input);
		String expected = "False";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite5() {
		
		String input = "succ(succ(0)) +x";
		String output = r.rewritePostfix(input);
		String expected = "x succ succ";
		
		assertEquals(expected,output);
	}
	
	@Test
	public void testRewrite6() {
		
		String input = "succ(succ(0)) + succ(0)";
		String output = r.rewritePostfix(input);
		String expected = "0 succ succ succ";
		
		assertEquals(expected,output);
	}
	@Test 
	public void testRewriteNone1() {
		String input = "True AND False";
		String output = rB.rewritePostfix(input);
		String expected = "False";
		
		assertEquals(expected,output);
	}
	@Test 
	public void testRewriteNone2() {
		String input = "True AND 1";
		String output = rB.rewritePostfix(input);
		String expected = "True 1 AND";
		
		assertEquals(expected,output);
	}
	@Test 
	public void testRewriteNone3() {
		String input = "True";
		String output = rB.rewritePostfix(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}

}
