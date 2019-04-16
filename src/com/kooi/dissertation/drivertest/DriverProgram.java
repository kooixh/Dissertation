package com.kooi.dissertation.drivertest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.Signature;
import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.rewriter.RewriteEngine;
import com.kooi.dissertation.rewriter.RewriteException;
import com.kooi.dissertation.rewriter.RewriteResult;
import com.kooi.dissertation.rewriter.RewriteRule;
import com.kooi.dissertation.rewriter.RewriteRuleFactory;
import com.kooi.dissertation.rewriter.RewriteStep;
import com.kooi.dissertation.rewriter.SearchEngine;
import com.kooi.dissertation.rewriter.SearchNode;
import com.kooi.dissertation.rewriter.SearchResult;
import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Node;
import com.kooi.dissertation.syntaxtree.Operator;
import com.kooi.dissertation.syntaxtree.UnaryOperator;

public class DriverProgram {
	
	
	
	static void parsingTest() {
		Set<Operator> opsB = new HashSet<>();
		opsB.add(new BinaryOperator("AND",2,DataType.BOOLEAN));
		opsB.add(new BinaryOperator("OR",2,DataType.BOOLEAN));
		opsB.add(new UnaryOperator("NOT",1,DataType.BOOLEAN));

		HashMap<String,DataType> variablesB = new HashMap<>();
		variablesB.put("T",DataType.BOOLEAN);
		variablesB.put("F",DataType.BOOLEAN);
		variablesB.put("B",DataType.BOOLEAN);
		
		Signature cBool = new Signature(opsB,variablesB);
		ASTParser boolP = new ASTParser(cBool);
		

		try {
			Node n = boolP.parseAST("False AND NOT True");
			prettyPrint(n);

		} catch (ParseException e) {
			e.printStackTrace();
		}	
	}
	
	static void trigoTest() {
		Set<Operator> ops = new HashSet<>();
		ops.add(new BinaryOperator("+",2,DataType.INT));
		ops.add(new BinaryOperator("-",2,DataType.INT));
		ops.add(new BinaryOperator("*",3,DataType.INT));
		ops.add(new BinaryOperator("/",3,DataType.INT));
		ops.add(new UnaryOperator("sin",2,DataType.INT));
		ops.add(new UnaryOperator("cos",2,DataType.INT));
		ops.add(new UnaryOperator("tan",2,DataType.INT));


		
		HashMap<String,DataType> variables = new HashMap<>();
		variables.put("x",DataType.INT);
		variables.put("y",DataType.INT);
		
		Set<RewriteRule> rules = new HashSet<>();
		
		Signature c = new Signature(ops,variables);
		ASTParser p = new ASTParser(c);
		
		RewriteRuleFactory f = new RewriteRuleFactory(p);
		rules.add(f.getRewriteRule("x+0", "x", "adding zero rule"));
		rules.add(f.getRewriteRule("0+x", "x", "adding zero rule"));
		rules.add(f.getRewriteRule("sin(90)", "1", "sin(90) rule"));
		rules.add(f.getRewriteRule("sin(30)", "1/2", "sin(30) rule"));
		rules.add(f.getRewriteRule("sin(0)", "0", "sin(0) rule"));
		rules.add(f.getRewriteRule("cos(90)", "0", "cos(90) rule"));
		rules.add(f.getRewriteRule("tan(45)", "1", "tan(45) rule"));
		rules.add(f.getRewriteRule("x*0", "0", "multiply zero rule"));
		rules.add(f.getRewriteRule("0*x", "0", "multiply zero rule"));
		rules.add(f.getRewriteRule("x*1", "x", "multiply one rule"));
		rules.add(f.getRewriteRule("1*x", "x", "multiply one rule"));
		
		RewriteEngine r = new RewriteEngine(rules,p);
		try {
			
			
			System.out.println("Rules: ");
			for(RewriteRule rule : rules) {
				System.out.println(p.toInfix(p.postOrderTreverse(rule.getLhs()))+" -> "+p.toInfix(p.postOrderTreverse(rule.getRhs()))+" : "+rule.getName());
			}
			System.out.println("\n\n");
			
			String s = "sin(sin(90*1)+sin(tan(45)*cos(90)))+sin(0)";
			RewriteResult res = r.rewrite(s);
			
			System.out.println("Initial term: "+res.getInitialTerm());
			System.out.println("Steps: "+res.getListOfSteps().size());
			for(RewriteStep step : res.getListOfSteps()) {
				System.out.println("=>"+p.toInfix(p.postOrderTreverse(step.getTermRoot()))+" using "+step.getRule());
			}
			System.out.println("Final Term: "+p.toInfix(p.postOrderTreverse(res.getFinalTerm())));
			
			
		}catch(Exception e) {
			
		}
	}
	
	
	static void booleanTest() {
		Set<Operator> opsB = new HashSet<>();
		opsB.add(new BinaryOperator("AND",2,DataType.BOOLEAN));
		opsB.add(new BinaryOperator("OR",2,DataType.BOOLEAN));
		opsB.add(new UnaryOperator("NOT",1,DataType.BOOLEAN));

		
		HashMap<String,DataType> variablesB = new HashMap<>();
		variablesB.put("T",DataType.BOOLEAN);
		variablesB.put("F",DataType.BOOLEAN);
		variablesB.put("B",DataType.BOOLEAN);
		
		Signature cBool = new Signature(opsB,variablesB);
		ASTParser boolP = new ASTParser(cBool);
		
		RewriteRuleFactory fB = new RewriteRuleFactory(boolP);
		
		Set<RewriteRule> rulesB = new HashSet<>();
		
		rulesB.add(fB.getRewriteRule("NOT NOT B", "B", "double negation"));
		rulesB.add(fB.getRewriteRule("B AND B", "B", "idempotent"));
		rulesB.add(fB.getRewriteRule("True OR B", "True", "OR-identity"));
		rulesB.add(fB.getRewriteRule("B OR True", "True", "OR-identity"));
		rulesB.add(fB.getRewriteRule("False OR False", "False", "idempotent"));
		rulesB.add(fB.getRewriteRule("B AND False", "False", "AND-identity"));
		rulesB.add(fB.getRewriteRule("False AND B", "False", "AND-identity"));

		RewriteEngine r = new RewriteEngine(rulesB,boolP);
		try {
			
			System.out.println("Rules: ");
			for(RewriteRule rule : rulesB) {
				System.out.println(boolP.toInfix(boolP.postOrderTreverse(rule.getLhs()))+" -> "+boolP.toInfix(boolP.postOrderTreverse(rule.getRhs()))+" : "+rule.getName());
			}
			System.out.println("\n\n");
			

			RewriteResult res = r.rewrite("True AND (NOT NOT False OR True) AND (False OR True)");
			
			System.out.println("Initial term: "+res.getInitialTerm());
			System.out.println("Steps: "+res.getListOfSteps().size());
			for(RewriteStep step : res.getListOfSteps()) {
				System.out.println("=>"+boolP.toInfix(boolP.postOrderTreverse(step.getTermRoot()))+" using "+step.getRule());
			}
			System.out.println("Final Term: "+boolP.toInfix(boolP.postOrderTreverse(res.getFinalTerm())));

		} catch (ParseException | RewriteException e) {
			e.printStackTrace();
		}
	}
	
	
	static void treeTest() {
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
		ASTParser p = new ASTParser(c);
		
		RewriteRuleFactory f = new RewriteRuleFactory(p);
		rules.add(f.getRewriteRule("x+0", "x", "adding zero rule"));
		rules.add(f.getRewriteRule("0+x", "x", "adding zero rule"));
		rules.add(f.getRewriteRule("succ(x)+y", "succ(x+y)", "succ add rule"));
		rules.add(f.getRewriteRule("succ(x)*succ(y)", "x*y", "succ multiply rule"));
		rules.add(f.getRewriteRule("x*0", "0", "multiply zero rule"));
		rules.add(f.getRewriteRule("0*x", "0", "multiply zero rule"));
		rules.add(f.getRewriteRule("x*1", "x", "multiply one rule"));
		rules.add(f.getRewriteRule("1*x", "x", "multiply one rule"));
		
		RewriteEngine r = new RewriteEngine(rules,p);
		
		
		try {
				
			String s = "(succ(10)+ ((succ(0) * succ(1))))";
			
			prettyPrint(p.parseAST(s));

			
			
		}catch(Exception e) {
			
		}
	}
	
	
	
	static void mathTest() {
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
		ASTParser p = new ASTParser(c);
		
		RewriteRuleFactory f = new RewriteRuleFactory(p);
		rules.add(f.getRewriteRule("x+0", "x", "adding zero rule"));
		rules.add(f.getRewriteRule("0+x", "x", "adding zero rule"));
		rules.add(f.getRewriteRule("succ(x)+y", "succ(x+y)", "succ add rule"));
		rules.add(f.getRewriteRule("succ(x)*succ(y)", "x*y", "succ multiply rule"));
		rules.add(f.getRewriteRule("x*0", "0", "multiply zero rule"));
		rules.add(f.getRewriteRule("0*x", "0", "multiply zero rule"));
		rules.add(f.getRewriteRule("x*1", "x", "multiply one rule"));
		rules.add(f.getRewriteRule("1*x", "x", "multiply one rule"));
		
		RewriteEngine r = new RewriteEngine(rules,p);
		
		
		try {
			
			
			System.out.println("Rules: ");
			for(RewriteRule rule : rules) {
				System.out.println(p.toInfix(p.postOrderTreverse(rule.getLhs()))+" -> "+p.toInfix(p.postOrderTreverse(rule.getRhs()))+" : "+rule.getName());
			}
			System.out.println("\n\n");
			
			RewriteResult res = r.rewrite("succ(1) * (succ(10) + ((succ(0 + 0) * succ(1))) )");
			
			System.out.println("Initial term: "+res.getInitialTerm());
			System.out.println("Steps: "+res.getListOfSteps().size());
			for(RewriteStep step : res.getListOfSteps()) {
				System.out.println("=>"+p.toInfix(p.postOrderTreverse(step.getTermRoot()))+" using "+step.getRule());
			}
			System.out.println("Final Term: "+p.toInfix(p.postOrderTreverse(res.getFinalTerm())));
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void indvRewriteTest() {
		Set<Operator> opsB = new HashSet<>();
		opsB.add(new BinaryOperator("AND",2,DataType.BOOLEAN));
		opsB.add(new BinaryOperator("OR",2,DataType.BOOLEAN));
		opsB.add(new UnaryOperator("NOT",1,DataType.BOOLEAN));

		
		HashMap<String,DataType> variablesB = new HashMap<>();
		variablesB.put("T",DataType.BOOLEAN);
		variablesB.put("F",DataType.BOOLEAN);
		variablesB.put("B",DataType.BOOLEAN);
		
		Signature cBool = new Signature(opsB,variablesB);
		ASTParser boolP = new ASTParser(cBool);
		
		RewriteRuleFactory fB = new RewriteRuleFactory(boolP);
		
		Set<RewriteRule> rulesB = new HashSet<>();
		
		rulesB.add(fB.getRewriteRule("NOT NOT B", "B", "double negation"));
		rulesB.add(fB.getRewriteRule("B AND B", "B", "idempotent"));
		rulesB.add(fB.getRewriteRule("True OR B", "True", "OR-identity"));
		rulesB.add(fB.getRewriteRule("B OR True", "True", "OR-identity"));
		rulesB.add(fB.getRewriteRule("False OR False", "False", "idempotent"));
		rulesB.add(fB.getRewriteRule("B AND False", "False", "AND-identity"));
		rulesB.add(fB.getRewriteRule("False AND B", "False", "AND-identity"));

		RewriteEngine r = new RewriteEngine(rulesB,boolP);
		try {
			
			System.out.println("Rules: ");
			for(RewriteRule rule : rulesB) {
				System.out.println(boolP.toInfix(boolP.postOrderTreverse(rule.getLhs()))+" -> "+boolP.toInfix(boolP.postOrderTreverse(rule.getRhs()))+" : "+rule.getName());
			}
			System.out.println("\n\n");
			
			String s = "True AND ((NOT NOT True) AND (False OR True AND True)) AND (False OR (True AND True OR False))";

			Node root = boolP.parseAST(s);
			
			System.out.println(boolP.toInfix(boolP.postOrderTreverse(root)));
			System.out.println("");

			
			System.out.println("Apply OR-identity");
			try {
				r.singleRewrite(root, fB.getRewriteRule("B OR True", "True", "OR-identity"));
				System.out.println(boolP.toInfix(boolP.postOrderTreverse(root)));
				System.out.println("Apply idempotent");
				r.singleRewrite(root, fB.getRewriteRule("B AND B", "B", "idempotent"));
				System.out.println(boolP.toInfix(boolP.postOrderTreverse(root)));
				System.out.println("Apply idempotent");
				r.singleRewrite(root, fB.getRewriteRule("B AND B", "B", "idempotent"));
				System.out.println(boolP.toInfix(boolP.postOrderTreverse(root)));
				System.out.println("Apply double negation");
				r.singleRewrite(root, fB.getRewriteRule("NOT NOT B", "B", "double negation"));
				System.out.println(boolP.toInfix(boolP.postOrderTreverse(root)));
			} catch (RewriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void searchTest() {
		
		Set<Operator> opsB = new HashSet<>();
		opsB.add(new BinaryOperator("AND",2,DataType.BOOLEAN));
		opsB.add(new BinaryOperator("OR",2,DataType.BOOLEAN));
		opsB.add(new UnaryOperator("NOT",1,DataType.BOOLEAN));

		
		HashMap<String,DataType> variablesB = new HashMap<>();
		variablesB.put("T",DataType.BOOLEAN);
		variablesB.put("F",DataType.BOOLEAN);
		variablesB.put("B",DataType.BOOLEAN);
		
		Signature cBool = new Signature(opsB,variablesB);
		ASTParser boolP = new ASTParser(cBool);
		
		RewriteRuleFactory fB = new RewriteRuleFactory(boolP);
		
		Set<RewriteRule> rulesB = new HashSet<>();
		
		rulesB.add(fB.getRewriteRule("NOT NOT B", "B", "double negation"));
		rulesB.add(fB.getRewriteRule("B AND B", "B", "idempotent"));
		rulesB.add(fB.getRewriteRule("True OR B", "True", "OR-identity"));
		rulesB.add(fB.getRewriteRule("B OR True", "True", "OR-identity"));
		rulesB.add(fB.getRewriteRule("False OR False", "False", "idempotent"));
		rulesB.add(fB.getRewriteRule("B AND False", "False", "AND-identity"));
		rulesB.add(fB.getRewriteRule("False AND B", "False", "AND-identity"));

		RewriteEngine r = new RewriteEngine(rulesB,boolP);
		
		
		SearchEngine s = new SearchEngine(r);
		
		
		try {
			
			Node initialTerm = boolP.parseAST("True AND (False OR (True AND (NOT NOT False OR True)))");
			Node goalTerm = boolP.parseAST("True AND (False OR True)");
			
			SearchResult sr = s.searchTerm(initialTerm, goalTerm, 4);
			
			if(sr == null )
				System.out.println("Not found");
			else {
				
				System.out.println("traversal");
				printSearch(sr.getSearchTree(),boolP);
				System.out.println("");
				
				System.out.println("Result");
				Stack<SearchNode> nodes = new Stack<>();
				
				SearchNode res = sr.getResult();
				

				
				while(res.getParentNode() != null) {
					nodes.push(res);
					res = res.getParentNode();
				}
				
				while(!nodes.isEmpty()) {
					SearchNode curr = nodes.pop();
					System.out.println("Apply "+curr.getPrevRule()+" to get "+boolP.toInfix(boolP.postOrderTreverse(curr.getTermNode())));
				}
				
				
				
			}
			
			
		}catch(Exception e) {
			
		}
	}
	
	
	public static void printSearch(SearchNode root,ASTParser p) throws Exception{
		
		System.out.println(p.toInfix(p.postOrderTreverse(root.getTermNode())));
		
		for(SearchNode s : root.getChildNodes())
			printSearch(s,p);
	}
	
	

	public static void main(String args[]) {
		
		
		
		
		//treeTest();
		//trigoTest();
		//mathTest();
		booleanTest();
		//searchTest();
		//parsingTest();
		//indvRewriteTest();
		
	}
	
	
	public static void prettyPrint(Node root)
	    {
	        List<List<String>> lines = new ArrayList<List<String>>();

	        List<Node> level = new ArrayList<Node>();
	        List<Node> next = new ArrayList<Node>();

	        level.add(root);
	        int nn = 1;

	        int widest = 0;

	        while (nn != 0) {
	            List<String> line = new ArrayList<String>();

	            nn = 0;

	            for (Node n : level) {
	                if (n == null) {
	                    line.add(null);

	                    next.add(null);
	                    next.add(null);
	                } else {
	                    String aa = n.toString();
	                    line.add(aa);
	                    if (aa.length() > widest) widest = aa.length();

	                    next.add(n.getLeft());
	                    next.add(n.getRight());

	                    if (n.getLeft() != null) nn++;
	                    if (n.getRight() != null) nn++;
	                }
	            }

	            if (widest % 2 == 1) widest++;

	            lines.add(line);

	            List<Node> tmp = level;
	            level = next;
	            next = tmp;
	            next.clear();
	        }

	        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
	        for (int i = 0; i < lines.size(); i++) {
	            List<String> line = lines.get(i);
	            int hpw = (int) Math.floor(perpiece / 2f) - 1;

	            if (i > 0) {
	                for (int j = 0; j < line.size(); j++) {

	                    // split node
	                    char c = ' ';
	                    if (j % 2 == 1) {
	                        if (line.get(j - 1) != null) {
	                            c = (line.get(j) != null) ? '┴' : '┘';
	                        } else {
	                            if (j < line.size() && line.get(j) != null) c = '└';
	                        }
	                    }
	                    System.out.print(c);

	                    // lines and spaces
	                    if (line.get(j) == null) {
	                        for (int k = 0; k < perpiece - 1; k++) {
	                            System.out.print(" ");
	                        }
	                    } else {

	                        for (int k = 0; k < hpw; k++) {
	                            System.out.print(j % 2 == 0 ? " " : "─");
	                        }
	                        System.out.print(j % 2 == 0 ? "┌" : "┐");
	                        for (int k = 0; k < hpw; k++) {
	                            System.out.print(j % 2 == 0 ? "─" : " ");
	                        }
	                    }
	                }
	                System.out.println();
	            }

	            // print line of numbers
	            for (int j = 0; j < line.size(); j++) {

	                String f = line.get(j);
	                if (f == null) f = "";
	                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
	                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

	                // a number
	                for (int k = 0; k < gap1; k++) {
	                    System.out.print(" ");
	                }
	                System.out.print(f);
	                for (int k = 0; k < gap2; k++) {
	                    System.out.print(" ");
	                }
	            }
	            System.out.println();

	            perpiece /= 2;
	        }
	    }
	
	
	public static void printPreOrder(Node n) {
		if(n == null) {
			System.out.println("null");
			return;
		}
			
		
		System.out.println(n.getValue());
		System.out.print("left ");
		printPreOrder(n.getLeft());
		System.out.print("right ");
		printPreOrder(n.getRight());
	}
}
