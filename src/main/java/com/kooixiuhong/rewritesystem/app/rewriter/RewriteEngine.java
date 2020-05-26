package com.kooixiuhong.rewritesystem.app.rewriter;

import com.kooixiuhong.rewritesystem.app.parser.ASTParser;
import com.kooixiuhong.rewritesystem.app.parser.ParseException;
import com.kooixiuhong.rewritesystem.app.syntaxtree.ASTNode;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;
import com.kooixiuhong.rewritesystem.app.syntaxtree.NodeType;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * This class is the main rewrite engine. It performs parsing of input and rules. It also
 * invokes the parsing algorithm.
 *
 * @author Kooi
 * @date 13 February 2019
 */
public class RewriteEngine implements Serializable {

    @Getter private Set<RewriteRule> rules;
    private Map<String, List<RewriteRule>> ruleMap;
    private ASTParser parser;
    private static final int MAX_ITERATION = 100;

    public RewriteEngine(Set<RewriteRule> rules, ASTParser parser) {
        this.parser = parser;
        this.rules = rules;
        this.ruleMap = new HashMap<>();
        for (RewriteRule rule : this.rules) {
            if (!ruleMap.containsKey(rule.getLhs().getValue())) {
                ruleMap.put(rule.getLhs().getValue(), new ArrayList<>());
            }
            ruleMap.get(rule.getLhs().getValue()).add(rule);
        }
    }

    public RewriteEngine(ASTParser parser) {
        this.parser = parser;
        this.rules = new HashSet<>();
        ruleMap = new HashMap<>();
    }

    public void addRule(RewriteRule rule) {
        rules.add(rule);
        if (!ruleMap.containsKey(rule.getLhs().getValue())) {
            ruleMap.put(rule.getLhs().getValue(), new ArrayList<>());
        }
        ruleMap.get(rule.getLhs().getValue()).add(rule);
    }

    public void deleteRule(RewriteRule rule) {
        rules.remove(rule);
        List<RewriteRule> list = ruleMap.get(rule.getLhs().getValue());
        list.remove(rule);
    }

    /**
     * Rewrite function to rewrite an infix expression string into it's normal form.
     * Output returned in post fix notation
     *
     * @param input Infix expression string
     * @return the normal form of input in post fix notation
     * @throws ParseException
     * @throws RewriteException
     */
    public String rewritePostfix(String input) throws ParseException, RewriteException {
        return parser.postOrderTraversal(rewriteNode(input));
    }

    /**
     * This is the main rewrite function. It takes a string in infix form and return the
     * syntax tree node of the result.
     *
     * @param infix expression to rewrite in infix form
     * @return root Node of the syntax tree of the result
     * @throws ParseException
     * @throws RewriteException
     */
    public Node rewriteNode(String infix) throws ParseException, RewriteException {

        int count = 0; //total iteration, prevents infinite
        Node root = parser.parseAST(infix);
        if (!checkTerm(root))
            throw new RewriteException("Term to rewrite cannot contain declared variable.");
        boolean flag;  //if flag is false then cannot be rewritten any further
        do {
            flag = search(root, null);
            count++;
        } while (flag && count < MAX_ITERATION);

        if (count == MAX_ITERATION)
            throw new RewriteException("Max Iteration reached, possible infinite rewrite");

        return root;

    }

    /**
     * @param infix String of expression to rewrite in infix form
     * @return A RewriteResult containing the result of the rewrite
     * @throws ParseException
     * @throws RewriteException
     */
    public RewriteResult rewrite(String infix) throws ParseException, RewriteException {

        int count = 0; //total iteration, prevents infinite

        Node root = parser.parseAST(infix);
        if (!checkTerm(root))
            throw new RewriteException("Term to rewrite cannot contain declared variable.");
        boolean flag;  //if flag is false then cannot be rewritten any further
        StringBuilder lastRule;
        List<RewriteStep> steps = new ArrayList<>();

        do {
            lastRule = new StringBuilder();
            flag = search(root, lastRule);
            //skips the final step
            if (flag) {
                RewriteStep step = new RewriteStep(copy(root), lastRule.toString());
                steps.add(step);
            }
            count++;
        } while (flag && count < MAX_ITERATION);
        if (count == MAX_ITERATION)
            throw new RewriteException("Max Iteration reached, possible infinite rewrite");
        return new RewriteResult(infix, steps, root);

    }

    public boolean singleSearch(Node node, RewriteRule r) throws RewriteException {
        if (!checkTerm(node))
            throw new RewriteException("Term to rewrite cannot contain declared variable.");
        return singleRewrite(node, r);
    }


    /**
     * This method implements the algorithm to perform a single rewrite
     *
     * @param node
     * @param rule
     * @return
     * @throws RewriteException
     */
    public boolean singleRewrite(Node node, RewriteRule rule) throws RewriteException {
        if (node == null)
            return false;
        if (singleRewrite(node.getLeft(), rule))
            return true;
        if (singleRewrite(node.getRight(), rule))
            return true;
        Map<String, Node> vars = new HashMap<>();
        if (match(node, rule.getLhs(), vars)) {
            substitute(node, rule.getRhs(), vars);
            return true;
        }
        return false;
    }

    /**
     * Apply a rule to a term just once.
     *
     * @param term Term to rewrite
     * @param rule Rewrite Rule to apply
     * @return the node after applying the rule
     * @throws RewriteException
     */
    public Node applyRule(Node term, RewriteRule rule) throws RewriteException {
        if (!checkTerm(term))
            throw new RewriteException("Term to rewrite cannot contain declared variable.");
        Map<String, Node> vars = new HashMap<>();
        if (match(term, rule.getLhs(), vars)) {
            substitute(term, rule.getRhs(), vars);
            return term;
        }
        return term;
    }


    /**
     * Call the rewrite method and return the result in infix form
     *
     * @param infix expression to rewrite in infix string
     * @return result after rewrite in infix form
     * @throws RewriteException
     */
    public String rewriteInfix(String infix) throws ParseException, RewriteException {
        return parser.toInfix(rewritePostfix(infix));
    }


    //private functions for algorithm


    //matching function to determine if term and rule match, vars map to keep track of
    //the variables
    private boolean match(Node term, Node rule, Map<String, Node> vars) throws RewriteException {
        if (term == null || rule == null) {
            return term == null && rule == null; //they match if both are null
        }
        //if is variable then just need to match type and add to var list.
        if (rule.getNodeType().equals(NodeType.VARIABLE)) {
            //if variable appeared before, check if current match prev
            if (vars.containsKey(rule.getValue())) {
                return term.equals(vars.get(rule.getValue()));
            } else {
                //if variable type is compatible with operator return type
                if (term.getNodeType().equals(NodeType.OPERATOR) && term.getType().equals(rule.getType())) {
                    vars.put(rule.getValue(), term);
                    return true;
                } else if (term.getNodeType().equals(NodeType.CONSTANT)) {
                    vars.put(rule.getValue(), term);
                    return true;
                } else if (term.getNodeType().equals(NodeType.VARIABLE) && term.getType().equals(rule.getType())) {
                    vars.put(rule.getValue(), term);
                    return true;
                }
                return false;
            }
        } else { //if rule is not a variable then must match symbol
            if (term.getNodeType().equals(NodeType.VARIABLE)) {
                throw new RewriteException("Term to rewrite cannot contain declared variable.");
            }
            if (rule.getValue().equals(term.getValue())) //recursively match the sub-expression
                return (match(term.getLeft(), rule.getLeft(), vars) && match(term.getRight(), rule.getRight(), vars));
            return false;
        }

    }

    //substitute function in rewrite algorithm. Swap the term node with rule and fill the
    //variables
    private void substitute(Node term, Node rule, Map<String, Node> vars) {
        //swap the term to be the rule
        ((ASTNode) term).setValue(rule.getValue());
        ((ASTNode) term).setNodeType(rule.getNodeType());
        ((ASTNode) term).setType(rule.getType());
        ((ASTNode) term).setRight(copy(rule.getRight()));
        ((ASTNode) term).setLeft(copy(rule.getLeft()));
        //fill in variable
        swap(term, vars);
    }

    //function to swap the node with the variable node
    private void swap(Node node, Map<String, Node> vars) {
        if (node == null)
            return;
        //check node value is a variable
        if (vars.containsKey(node.getValue())) {
            String initialValue = node.getValue();
            //set the respective node
            ((ASTNode) node).setValue(vars.get(initialValue).getValue());
            ((ASTNode) node).setNodeType(vars.get(initialValue).getNodeType());
            ((ASTNode) node).setType(vars.get(initialValue).getType());
            ((ASTNode) node).setRight(copy(vars.get(initialValue).getRight()));
            ((ASTNode) node).setLeft(copy(vars.get(initialValue).getLeft()));
        }
        swap(node.getLeft(), vars);
        swap(node.getRight(), vars);
    }

    //copy function to copy one node into a new instance
    public Node copy(Node node) {
        if (node == null)
            return null;
        return new ASTNode(node.getValue(), copy(node.getLeft()), copy(node.getRight()), node.getType(), node.getNodeType());

    }

    //this method checks that a term is well formed
    private boolean checkTerm(Node node) {
        if (node == null)
            return true;
        if (node.getNodeType().equals(NodeType.VARIABLE))
            return false;
        return (checkTerm(node.getLeft())) && checkTerm(node.getRight());
    }

    //use a post-order traversal to search the syntax tree for matching rule
    //root keeps a reference to the original root so we can swap when we make a copy
    private boolean search(Node node, StringBuilder ruleName) throws RewriteException {
        if (node == null)
            return false;
        //if something is rewritten don't rewrite further
        if (search(node.getLeft(), ruleName))
            return true;
        if (search(node.getRight(), ruleName))
            return true;
        //try all the possible rule that can be match
        if (ruleMap.get(node.getValue()) == null)
            return false;
        for (RewriteRule rule : ruleMap.get(node.getValue())) {
            Map<String, Node> vars = new HashMap<>();
            if (match(node, rule.getLhs(), vars)) {
                substitute(node, rule.getRhs(), vars);
                if (ruleName != null)
                    ruleName.append(rule.getName());
                return true;
            }
        }
        return false;
    }

}
