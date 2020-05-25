package com.kooixiuhong.rewritesystem.app.gui;

import com.kooixiuhong.rewritesystem.app.rewriter.RewriteRule;
import com.kooixiuhong.rewritesystem.app.rewriter.RewriteRuleFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RuleAddingPanel extends JFrame {

    private JPanel contentPane;
    private JTextField lhsTextField;
    private JTextField rhsTextField;
    private JTextField nameField;
    private JButton addBtn;
    private HomeScreen home;
    private RewriteRuleFactory rewriteRuleFactory;

    /**
     * Create the frame.
     */
    public RuleAddingPanel(HomeScreen homeScreen) {

        this.home = homeScreen;
        this.rewriteRuleFactory = new RewriteRuleFactory(homeScreen.getParser());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));


        //add rule button
        addBtn = new JButton("Add");
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (lhsTextField.getText().equals("") || rhsTextField.getText().equals("")
                        || nameField.getText().equals("")) {
                    JOptionPane.showMessageDialog(RuleAddingPanel.this,
                            "Fields cannot be empty.", "Missing values", JOptionPane.ERROR_MESSAGE);
                } else {
                    RewriteRule rule = rewriteRuleFactory.getRewriteRule(lhsTextField.getText(),
                            rhsTextField.getText(), nameField.getText());
                    if (rule == null) {
                        JOptionPane.showMessageDialog(RuleAddingPanel.this,
                                "An error is encountered during parsing, check for mismatch parenthesis.",
                                "Parsing Exception", JOptionPane.ERROR_MESSAGE);
                    } else {
                        home.getEngine().addRule(rule);
                        home.updateUI();
                        RuleAddingPanel.this.dispose();
                    }
                }
            }
        });


        //lhs field
        JPanel panelLhs = new JPanel();
        lhsTextField = new JTextField(10);
        panelLhs.setLayout(new BoxLayout(panelLhs, BoxLayout.X_AXIS));
        JLabel lhsLab = new JLabel("Left-Hand Side");
        lhsLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        panelLhs.add(lhsLab);
        panelLhs.add(lhsTextField);

        //rhs field
        JPanel panelRhs = new JPanel();
        rhsTextField = new JTextField(10);
        panelRhs.setLayout(new BoxLayout(panelRhs, BoxLayout.X_AXIS));
        JLabel rhsLab = new JLabel("Right-Hand Side");
        rhsLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        panelRhs.add(rhsLab);
        panelRhs.add(rhsTextField);


        //rule name

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        JLabel nameLab = new JLabel("Rule name");
        nameLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        namePanel.add(nameLab);
        nameField = new JTextField(10);
        namePanel.add(nameField);

        lhsTextField.setMaximumSize(lhsTextField.getPreferredSize());
        rhsTextField.setMaximumSize(rhsTextField.getPreferredSize());
        nameField.setMaximumSize(nameField.getPreferredSize());
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));

        btnPanel.add(addBtn);
        contentPane.add(panelLhs);
        contentPane.add(panelRhs);
        contentPane.add(namePanel);
        contentPane.add(btnPanel);
        setContentPane(contentPane);

    }


    public void showWindow() {
        setVisible(true);
    }

}
