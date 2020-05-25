package com.kooixiuhong.rewritesystem.app.rewriter;

import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a node used to build a search tree. The search tree can have multiple
 * children. The value held is the root of the term tree
 *
 * @author Kooi
 * @date 18th March 2019
 */
@Getter
public class SearchNode {


    private Node termNode;
    private SearchNode parentNode;
    private String prevRule;
    private List<SearchNode> childNodes;

    public SearchNode(Node node, SearchNode searchNode, String prevRule) {
        this.termNode = node;
        this.parentNode = searchNode;
        this.prevRule = prevRule;
        this.childNodes = new ArrayList<>();
    }


    /**
     * Add a new child
     *
     * @param n
     */
    public void addChild(SearchNode n) {
        this.childNodes.add(n);
    }

}
