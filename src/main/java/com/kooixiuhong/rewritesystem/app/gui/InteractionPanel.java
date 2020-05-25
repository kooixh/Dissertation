package com.kooixiuhong.rewritesystem.app.gui;

import com.kooixiuhong.rewritesystem.app.parser.ParseException;
import com.kooixiuhong.rewritesystem.app.rewriter.RewriteEngine;
import com.kooixiuhong.rewritesystem.app.rewriter.RewriteException;
import com.kooixiuhong.rewritesystem.app.rewriter.RewriteResult;
import com.kooixiuhong.rewritesystem.app.rewriter.RewriteRule;
import com.kooixiuhong.rewritesystem.app.rewriter.RewriteStep;
import com.kooixiuhong.rewritesystem.app.rewriter.SearchEngine;
import com.kooixiuhong.rewritesystem.app.rewriter.SearchNode;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;

import javax.swing.*;
import java.awt.*;

public class InteractionPanel extends JPanel {

    //components
    private JButton addRuleBtn;
    private JButton addOpBtn;
    private JButton addVarBtn;
    private JTextField rewriteField;
    private JTextArea resultArea;
    private JButton rewriteButton;
    private JLabel intStat;

    //fields
    private RewriteEngine rewriteEngine;
    private StringBuilder currentResult;
    private Node interactiveRoot;

    //parent frame
    private HomeScreen home;

    //constant
    private final int BOUND = 100;


    /**
     * Create the panel.
     */
    public InteractionPanel(HomeScreen homeScreen, RewriteEngine rewriteEngine) {

        home = homeScreen;
        this.rewriteEngine = rewriteEngine;

        //button to show the various windows
        addRuleBtn = new JButton("Add Rule");
        addRuleBtn.addActionListener(e -> {

            RuleAddingPanel ar = new RuleAddingPanel(home);
            ar.showWindow();
        });


        addOpBtn = new JButton("Add Operator");
        addOpBtn.addActionListener(e -> {
            OperatorAddingFrame ao = new OperatorAddingFrame(home);
            ao.showWindow();
        });


        addVarBtn = new JButton("Add Variable");
        addVarBtn.addActionListener(e -> {
            VariableAddingPanel av = new VariableAddingPanel(home);
            av.showWindow();
        });


        //setting up the panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel innerPane = new JPanel();
        innerPane.setLayout(new BoxLayout(innerPane, BoxLayout.X_AXIS));

        innerPane.add(addOpBtn);
        innerPane.add(addVarBtn);
        innerPane.add(addRuleBtn);

        this.add(innerPane);

        JPanel txtPane = new JPanel();
        txtPane.setLayout(new BoxLayout(txtPane, BoxLayout.X_AXIS));
        txtPane.setPreferredSize(new Dimension(200, 50));


        //input for string to rewrite

        rewriteField = new JTextField(20);
        rewriteField.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        rewriteField.setMaximumSize(rewriteField.getPreferredSize());
        txtPane.add(rewriteField);


        rewriteButton = new JButton("Rewrite");

        rewriteButton.addActionListener(e -> {
            try {
                //reset interactive mode
                if (interactiveRoot != null) {
                    intStat.setText("OFF");
                    intStat.setForeground(Color.RED);
                    interactiveRoot = null;
                }


                if (rewriteField.getText().equals("")) {
                    JOptionPane.showMessageDialog(InteractionPanel.this, "Field cannot be empty, enter a term to be rewritten.", "Missing values", JOptionPane.ERROR_MESSAGE);

                } else {
                    RewriteResult o = InteractionPanel.this.rewriteEngine.rewrite(rewriteField.getText());
                    currentResult = new StringBuilder();
                    currentResult.append("Initial term: ").append(rewriteField.getText()).append("\n");


                    for (RewriteStep step : o.getListOfSteps()) {
                        currentResult.append("\u2b91 ").append(home.getParser().toInfix(home.getParser().postOrderTreverse(step.getTermRoot())));
                        currentResult.append(" By ").append(step.getRule());
                        currentResult.append("\n");
                    }
                    currentResult.append("\nFinal term: ")
                            .append(home.getParser().toInfix(home.getParser().postOrderTreverse(o.getFinalTerm())));
                    resultArea.setText(currentResult.toString());
                }
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(InteractionPanel.this, "An error is encountered during parsing, check for mismatch parenthesis.", "Parsing Exception", JOptionPane.ERROR_MESSAGE);
                pe.printStackTrace();
            } catch (RewriteException re) {
                JOptionPane.showMessageDialog(InteractionPanel.this, "An error is encountered during rewriting, " + re.getMessage(), "Rewrite Exception", JOptionPane.ERROR_MESSAGE);
                re.printStackTrace();
            }
        });
        txtPane.add(rewriteButton);
        this.add(txtPane);
        JPanel resultPanelFull = new JPanel();
        resultPanelFull.setLayout(new BoxLayout(resultPanelFull, BoxLayout.Y_AXIS));
        resultPanelFull.setBorder(BorderFactory.createTitledBorder("Result"));

        //analysis capabilities panel
        JPanel analysisPane = new JPanel();
        analysisPane.setLayout(new BoxLayout(analysisPane, BoxLayout.X_AXIS));

        JButton intButton = new JButton("Interactive");

        intStat = new JLabel("OFF");
        intStat.setForeground(Color.RED);
        intStat.setFont(new Font("Century Gothic", Font.PLAIN, 12));

        intButton.addActionListener(e -> {

            //create a selector of all possible rules
            Object[] rules = InteractionPanel.this.rewriteEngine.getRules().toArray();
            RewriteRule r = (RewriteRule) JOptionPane.showInputDialog(
                    home,
                    "Choose a rule to apply\n",
                    "Choose rule to apply",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    rules,
                    "");

            if (r != null) {

                //if first interactive run, clear resArea and set on
                if (interactiveRoot == null) {
                    try {
                        interactiveRoot = home.getParser().parseAST(rewriteField.getText());
                        intStat.setText("ON");
                        intStat.setForeground(Color.GREEN);
                        resultArea.setText("");
                    } catch (ParseException e1) {
                        JOptionPane.showMessageDialog(InteractionPanel.this, "An error is encountered during parsing, check for syntax errors.", "Parsing Exception", JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                    }
                }

                try {
                    if (InteractionPanel.this.rewriteEngine.singleSearch(interactiveRoot, r))
                        resultArea.append("\u2b91 " + home.getParser().toInfix(home.getParser().postOrderTreverse(interactiveRoot)) + " By " + r.getName() + "\n");
                    else
                        resultArea.append("\u2b91 " + home.getParser().toInfix(home.getParser().postOrderTreverse(interactiveRoot)) + " Rule does not apply!!" + "\n");
                } catch (ParseException e1) {
                    JOptionPane.showMessageDialog(InteractionPanel.this, "An error is encountered during parsing, check for syntax errors.", "Parsing Exception", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                } catch (RewriteException e1) {
                    JOptionPane.showMessageDialog(InteractionPanel.this, "Error during rewriting, " + e1.getMessage(), "Rewrite Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }

        });

        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> {

            SearchWindow sw = new SearchWindow(InteractionPanel.this, home);
            sw.setVisible(true);
        });

        JButton visButton = new JButton("Visualise");

        visButton.addActionListener(e -> {
            SearchEngine se = new SearchEngine(home.getEngine());
            try {
                Node n = home.getParser().parseAST(rewriteField.getText());
                SearchNode searchRoot = se.buildSearchTree(n, BOUND);

                (new Thread(() -> {
                    SearchTreeVisualisationFrame stv = new SearchTreeVisualisationFrame(home, searchRoot);
                    stv.setVisible(true);
                })).start();
            } catch (ParseException e1) {
                JOptionPane.showMessageDialog(InteractionPanel.this, "An error is encountered during parsing, check for syntax errors.", "Parsing Exception", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (RewriteException e1) {
                JOptionPane.showMessageDialog(InteractionPanel.this, "An error is encountered during rewriting, " + e1.getMessage(), "Rewrite Exception", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        });

        analysisPane.add(intButton);
        analysisPane.add(intStat);
        analysisPane.add(searchButton);
        analysisPane.add(visButton);

        this.add(analysisPane);
        //text area for result
        JPanel resPane = new JPanel();
        resPane.setLayout(new BoxLayout(resPane, BoxLayout.Y_AXIS));

        resultArea = new JTextArea();
        resultArea.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resPane.add(resultArea);

        JScrollPane resScroll = new JScrollPane(resPane);
        resultPanelFull.add(resScroll);

        this.add(resultPanelFull);
    }


    /**
     * Set the result are in the panel
     *
     * @param text String to set in the Text area
     */
    public void setResultArea(String text) {
        this.resultArea.setText(text);
    }


}
