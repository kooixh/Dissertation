package com.kooixiuhong.rewritesystem.app.rewriter;

import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a rewrite step. It has a node represent the term at that
 * point and the rule is applied to the previous term.
 *
 * @author Kooi
 * @date 21 February 2019
 */
@Getter
@Setter
@AllArgsConstructor
public class RewriteStep {
    private Node termRoot;
    private String rule; //rule applied to previous step
}
