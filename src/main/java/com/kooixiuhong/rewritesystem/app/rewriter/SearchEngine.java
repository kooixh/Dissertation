package com.kooixiuhong.rewritesystem.app.rewriter;

import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;
import lombok.AllArgsConstructor;

/**
 * This class provides analysis functionalities to search for if a term is reachable.
 *
 * @author Kooi
 * @date 18th March 2019
 */
@AllArgsConstructor
public class SearchEngine {

    private RewriteEngine engine;

    /**
     * Build a search tree
     *
     * @param initialTerm root of the tree
     * @param bound       maximum depth
     * @return root node of the search tree
     * @throws RewriteException
     */
    public SearchNode buildSearchTree(Node initialTerm, int bound) throws RewriteException {

        SearchNode root = new SearchNode(engine.copy(initialTerm), null, "");

        buildUtil(root, bound, 0);

        return root;
    }

    /**
     * This method uses a bounded depth-first search to find if a goal term is reachable.
     * Return the search tree and node of result, null if not reachable.
     *
     * @param initialTerm
     * @param goalTerm
     * @param bound
     * @return a SearchResult object
     * @throws RewriteException
     */
    public SearchResult searchTerm(Node initialTerm, Node goalTerm, int bound) throws RewriteException {
        SearchNode searchRoot = buildSearchTree(initialTerm, bound);
        SearchNode resNode = searchUtil(searchRoot, goalTerm, bound, 0);
        return new SearchResult(searchRoot, resNode);
    }

    //use a bounded dfs to search for a match
    private SearchNode searchUtil(SearchNode searchRoot, Node root, int bound, int curB) {
        //out of bounds
        if (curB > bound)
            return null;
        if (searchRoot.getTermNode().equals(root))
            return searchRoot;
        //continue
        for (SearchNode s : searchRoot.getChildNodes()) {
            SearchNode next = searchUtil(s, root, bound, curB + 1);
            if (next != null)
                return next;
        }
        return null;
    }

    //use a bounded dfs to build a search tree
    private void buildUtil(SearchNode searchRoot, int bound, int curB) throws RewriteException {
        if (curB > bound)
            return;
        //apply all possible rule on current node and add as child
        for (RewriteRule r : engine.getRules()) {
            Node copyOfTerm = engine.copy(searchRoot.getTermNode());
            if (engine.singleSearch(copyOfTerm, r))
                searchRoot.addChild(new SearchNode(copyOfTerm, searchRoot, r.getName()));
        }
        //dfs children
        for (SearchNode c : searchRoot.getChildNodes()) {
            buildUtil(c, bound, curB + 1);
        }

    }

}
