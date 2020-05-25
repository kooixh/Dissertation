package com.kooixiuhong.rewritesystem.app.gui;

import com.kooixiuhong.rewritesystem.app.parser.ParseException;
import com.kooixiuhong.rewritesystem.app.rewriter.RewriteException;
import com.kooixiuhong.rewritesystem.app.rewriter.SearchEngine;
import com.kooixiuhong.rewritesystem.app.rewriter.SearchNode;
import com.kooixiuhong.rewritesystem.app.rewriter.SearchResult;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

public class SearchWindow extends JFrame {


    //components
    private JPanel contentPane;
    private JTextField initialTermField;
    private JTextField goalTermField;
    private JTextField boundField;
    private JButton searchBtn;
    private HomeScreen home;

    //engines
    private SearchEngine sEngine;


    /**
     * Create the frame.
     */
    public SearchWindow(InteractionPanel interactionPanel, HomeScreen homeScreen) {

        //init
        this.home = homeScreen;
        //reference to parent
        sEngine = new SearchEngine(home.getEngine());


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });


        //initial term panel
        JPanel panelInitial = new JPanel();
        initialTermField = new JTextField(20);
        initialTermField.setMaximumSize(initialTermField.getPreferredSize());
        panelInitial.setLayout(new BoxLayout(panelInitial, BoxLayout.X_AXIS));
        JLabel iniLab = new JLabel("Initial term");
        iniLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        panelInitial.add(iniLab);
        panelInitial.add(initialTermField);


        //goal panel
        JPanel panelGoal = new JPanel();
        goalTermField = new JTextField(20);
        panelGoal.setLayout(new BoxLayout(panelGoal, BoxLayout.X_AXIS));
        goalTermField.setMaximumSize(goalTermField.getPreferredSize());
        JLabel goalLab = new JLabel("Goal term");
        goalLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        panelGoal.add(goalLab);
        panelGoal.add(goalTermField);

        //bound panel
        JPanel boundPanel = new JPanel();
        boundField = new JTextField(10);
        boundPanel.setLayout(new BoxLayout(boundPanel, BoxLayout.X_AXIS));
        boundField.setMaximumSize(boundField.getPreferredSize());
        JLabel boundLab = new JLabel("Bound");
        boundLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        boundPanel.add(boundLab);
        boundPanel.add(boundField);


        //search button
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> {
            try {
                String initTermString = initialTermField.getText();
                String goalTermString = goalTermField.getText();
                String boundString = boundField.getText();

                if (initTermString.equals("") || goalTermString.equals("") || boundString.equals("")) {
                    JOptionPane.showMessageDialog(SearchWindow.this,
                            "Fields cannot be empty.", "Missing values", JOptionPane.ERROR_MESSAGE);

                } else {
                    Node initTermNode = home.getParser().parseAST(initTermString);
                    Node goalTermNode = home.getParser().parseAST(goalTermString);
                    int bound = Integer.parseInt(boundString);
                    if (bound <= 0) {
                        JOptionPane.showMessageDialog(SearchWindow.this, "Bound must be a number greater than 0.", "Invalid values", JOptionPane.ERROR_MESSAGE);

                    } else {
                        SearchResult result = sEngine.searchTerm(initTermNode, goalTermNode, bound);
                        if (result.getResult() == null) {
                            interactionPanel.setResultArea("Term is not reachable");
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Reachable!\n");
                            sb.append(initialTermField.getText()).append("\n");
                            sb.append(buildResultText(result));
                            interactionPanel.setResultArea(sb.toString());

                        }
                        //thread for building the visualiser
                        (new Thread(() -> {
                            SearchTreeVisualisationFrame stv = new SearchTreeVisualisationFrame(home, result.getSearchTree());
                            stv.setVisible(true);
                        })).start();
                        SearchWindow.this.dispose();
                    }
                }

            } catch (ParseException e1) {
                JOptionPane.showMessageDialog(SearchWindow.this,
                        "An error is encountered during parsing, check for mismatch parenthesis.",
                        "Parsing Exception", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (NumberFormatException e2) {
                JOptionPane.showMessageDialog(SearchWindow.this,
                        "Bound must be a number greater than 0.", "Invalid values", JOptionPane.ERROR_MESSAGE);

            } catch (RewriteException e1) {
                JOptionPane.showMessageDialog(SearchWindow.this,
                        "Error during rewriting, " + e1.getMessage(), "Rewrite Error", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });

        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(panelInitial);
        contentPane.add(panelGoal);
        contentPane.add(boundPanel);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.add(searchBtn);
        contentPane.add(btnPanel);

        setContentPane(contentPane);
    }


    //this method builds the trace of the search
    private String buildResultText(SearchResult result) throws ParseException {
        StringBuilder trace = new StringBuilder();

        Stack<SearchNode> nodes = new Stack<>();
        SearchNode res = result.getResult();

        while (res.getParentNode() != null) {
            nodes.push(res);
            res = res.getParentNode();
        }

        while (!nodes.isEmpty()) {
            SearchNode curr = nodes.pop();
            trace.append("\u2b91Apply ").append(curr.getPrevRule()).append(" to get ")
                    .append(home.getParser().toInfix(home.getParser().postOrderTreverse(curr.getTermNode())));
            trace.append("\n");
        }
        return trace.toString();

    }

}
