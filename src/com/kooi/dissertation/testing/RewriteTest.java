package com.kooi.dissertation.testing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.Signature;
import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.rewriter.RewriteEngine;
import com.kooi.dissertation.rewriter.RewriteException;
import com.kooi.dissertation.rewriter.RewriteRule;
import com.kooi.dissertation.rewriter.RewriteRuleFactory;
import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Operator;
import com.kooi.dissertation.syntaxtree.UnaryOperator;

public class RewriteTest {
	
	
	static RewriteEngine r;
	static RewriteEngine rB;
	static RewriteEngine rI;
	static ASTParser p;
	static ASTParser boolP;
	static ASTParser infP;
	@BeforeClass
	public static void initialise() {
		
		//two rewrite engines
		Set<Operator> ops = new HashSet<>();
		ops.add(new BinaryOperator("+",2,DataType.INT));
		ops.add(new BinaryOperator("-",2,DataType.INT));
		ops.add(new BinaryOperator("*",3,DataType.INT));
		ops.add(new BinaryOperator("/",3,DataType.INT));
		ops.add(new UnaryOperator("succ",2,DataType.INT));
		
		HashMap<String,DataType> variables = new HashMap<>();
		variables.put("x",DataType.INT);
		variables.put("y",DataType.INT);
		
		Set<RewriteRule> rules = new HashSet<>();
		
		Signature c = new Signature(ops,variables);
		p = new ASTParser(c);
		
		RewriteRuleFactory f = new RewriteRuleFactory(p);
		rules.add(f.getRewriteRule("x+0", "x", "adding zero"));
		rules.add(f.getRewriteRule("0+x", "x", "adding zero"));
		rules.add(f.getRewriteRule("succ(x)+y", "succ(x+y)", "succ add rule"));
		rules.add(f.getRewriteRule("succ(x)*succ(y)", "x*y", "succ multi rule"));
		rules.add(f.getRewriteRule("x*0", "0", "multi zero rule"));
		rules.add(f.getRewriteRule("0*x", "0", "multi zero rule"));
		rules.add(f.getRewriteRule("x*1", "x", "multi one rule"));
		rules.add(f.getRewriteRule("1*x", "x", "multi one rule"));
		
		
		
		//boolean engine starts here
		Set<Operator> opsB = new HashSet<>();
		opsB.add(new BinaryOperator("AND",2,DataType.BOOLEAN));
		opsB.add(new BinaryOperator("OR",2,DataType.BOOLEAN));
		opsB.add(new UnaryOperator("NOT",1,DataType.BOOLEAN));
		
		HashMap<String,DataType> variablesB = new HashMap<>();
		variablesB.put("T",DataType.BOOLEAN);
		variablesB.put("F",DataType.BOOLEAN);
		variablesB.put("B",DataType.BOOLEAN);
		
		Signature cBool = new Signature(opsB,variablesB);
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
		
		//Infinite engine starts
		Set<Operator> opsInfinite = new HashSet<>();
		opsInfinite.add(new BinaryOperator("+",2,DataType.INT));
		opsInfinite.add(new BinaryOperator("-",2,DataType.INT));
		
		Set<RewriteRule> rulesInfinite = new HashSet<>();
		
		Signature cInf = new Signature(ops,variables);
		infP = new ASTParser(cInf);
		RewriteRuleFactory fI = new RewriteRuleFactory(infP);
		rulesInfinite.add(fI.getRewriteRule("x+y", "y+x", "inf1"));
		rulesInfinite.add(fI.getRewriteRule("y+x", "x+y", "inf2"));
			
		rI = new RewriteEngine(rulesInfinite,infP);
		r = new RewriteEngine(rules,p);
		rB = new RewriteEngine(rulesB,boolP);
	}
	

	@Test
	public void testRewrite1() throws ParseException, RewriteException{
		
		String input = "True AND True";
		String output = rB.rewritePostfix(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite2() throws ParseException, RewriteException{
		
		String input = "NOT NOT True";
		String output = rB.rewritePostfix(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite3() throws ParseException, RewriteException{
		
		String input = "True AND False OR (True AND True)";
		String output = rB.rewritePostfix(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite4() throws ParseException, RewriteException{
		
		String input = "True AND False OR (True AND (False OR False)) AND True AND True";
		String output = rB.rewritePostfix(input);
		String expected = "False";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite5()throws ParseException, RewriteException {
		
		String input = "succ(succ(0)) +x";
		String output = r.rewritePostfix(input);
		String expected = "x succ succ";
		
		assertEquals(expected,output);
	}
	
	@Test
	public void testRewrite6() throws ParseException, RewriteException{
		
		String input = "succ(succ(0)) + succ(0)";
		String output = r.rewritePostfix(input);
		String expected = "0 succ succ succ";
		
		assertEquals(expected,output);
	}

	@Test
	public void testRewrite7() throws ParseException, RewriteException{
		
		String input = "True AND False OR ((True AND TRUE)OR TRUE AND False) OR False";
		String output = rB.rewritePostfix(input);
		String expected = "False";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite8()throws ParseException, RewriteException{
		
		String input = "True AND ((NOT NOT True) AND (False OR True AND True)) AND (False OR (True AND True OR False))";
		String output = rB.rewritePostfix(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite9() throws ParseException, RewriteException{
		
		String input = "0+succ(0)*succ(0)+x";
		String output = r.rewritePostfix(input);
		String expected = "x";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite10()throws ParseException, RewriteException {
		
		String input = "succ(0)+(succ(4)+(succ(0)*succ(0)))";
		String output = r.rewritePostfix(input);
		String expected = "4 succ succ";
		
		assertEquals(expected,output);
	}
	
	@Test
	public void testRewrite11()throws ParseException, RewriteException {
		
		String input = "succ(1)*(succ(10)+ (( (succ(0)+0) * succ(1) )))";
		String output = r.rewritePostfix(input);
		String expected = "10";
		
		assertEquals(expected,output);
	}

	@Test 
	public void testRewriteNone1()throws ParseException , RewriteException{
		String input = "True AND False";
		String output = rB.rewritePostfix(input);
		String expected = "False";
		
		assertEquals(expected,output);
	}
	@Test 
	public void testRewriteNone2()throws ParseException, RewriteException{
		String input = "True AND 1";
		String output = rB.rewritePostfix(input);
		String expected = "True 1 AND";
		
		assertEquals(expected,output);
	}
	@Test 
	public void testRewriteNone3() throws ParseException, RewriteException{
		String input = "True";
		String output = rB.rewritePostfix(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	
	@Test(expected=RewriteException.class)
	public void testRewriteInfite1() throws ParseException, RewriteException{
		String input = "1+2";
		String output = rI.rewritePostfix(input);
		String expected = "2+1";
		
		assertEquals(expected,output);
	}
	 

}
