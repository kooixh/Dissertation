package com.kooixiuhong.rewritesystem.app.rewriter;

import com.kooixiuhong.rewritesystem.app.syntaxtree.ASTNode;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores a rewrite result. It stores a list of rewrite state.
 * This class is immutable
 *
 * @author Kooi
 * @date 21 February 2019
 */
@Getter
public final class RewriteResult {

    private final String initialTerm;
    private final List<RewriteStep> listOfSteps;
    private final Node finalTerm;

    public RewriteResult(String initial, List<RewriteStep> steps, Node finalTerm) {
        this.initialTerm = initial;
        this.listOfSteps = new ArrayList<>(steps);
        this.finalTerm = copy(finalTerm);
    }

    private Node copy(Node n) {

        if (n == null)
            return null;
        Node newNode = new ASTNode(n.getValue(), copy(n.getLeft()), copy(n.getRight()), n.getType(), n.getNodeType());
        return newNode;

    }

}
