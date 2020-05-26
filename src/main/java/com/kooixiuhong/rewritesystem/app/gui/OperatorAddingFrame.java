package com.kooixiuhong.rewritesystem.app.gui;

import com.kooixiuhong.rewritesystem.app.syntaxtree.BinaryOperator;
import com.kooixiuhong.rewritesystem.app.syntaxtree.DataType;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Operator;
import com.kooixiuhong.rewritesystem.app.syntaxtree.UnaryOperator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class OperatorAddingFrame extends JFrame {

    private JPanel contentPane;
    private JTextField symbolField;
    private JTextField precedenceField;
    private JComboBox opType;
    private JComboBox opRetType;
    private JButton addBtn;
    private HomeScreen home;
    private Map<String, DataType> typeMap;


    /**
     * Create the frame.
     */
    public OperatorAddingFrame(HomeScreen homeScreen) {

        this.home = homeScreen;

        typeMap = new HashMap<>();
        typeMap.put("INT", DataType.INT);
        typeMap.put("NAT", DataType.NAT);
        typeMap.put("STRING", DataType.STRING);
        typeMap.put("BOOLEAN", DataType.BOOLEAN);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });


        addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            String symbol = symbolField.getText();
            String precedenceField = this.precedenceField.getText();
            if (symbol.equals("") || precedenceField.equals("")) {
                JOptionPane.showMessageDialog(OperatorAddingFrame.this, "Field cannot be empty.",
                        "Missing values", JOptionPane.ERROR_MESSAGE);
            } else if (symbol.equals("{") || symbol.equals("}") || symbol.equals("(") ||
                    symbol.equals(")") || symbol.equals("[") || symbol.equals("]")) {
                JOptionPane.showMessageDialog(OperatorAddingFrame.this,
                        "Your operator symbol is invalid, cannot be any sort of brackets.",
                        "Invalid values", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int precedence = Integer.parseInt(precedenceField);
                    if (precedence <= 0) {
                        JOptionPane.showMessageDialog(OperatorAddingFrame.this, "Precedence must be a number greater than 0.", "Invalid values", JOptionPane.ERROR_MESSAGE);
                    } else {
                        Operator operator;
                        if (opType.getSelectedIndex() == 0) {
                            operator = new UnaryOperator(symbol, precedence, typeMap.get(opRetType.getSelectedItem()));
                        } else {
                            operator = new BinaryOperator(symbol, precedence, typeMap.get(opRetType.getSelectedItem()));
                        }
                        home.getSignature().addOperator(operator);
                        home.updateUI();
                        OperatorAddingFrame.this.dispose();
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(OperatorAddingFrame.this,
                            "Precedence must be a number greater than 0.",
                            "Invalid values", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //setting up frame
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        //panel for symbol text field
        JPanel panelSymbol = new JPanel();
        panelSymbol.setLayout(new BoxLayout(panelSymbol, BoxLayout.X_AXIS));
        JLabel symLab = new JLabel("Operator Symbol");
        symLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        symbolField = new JTextField(10);
        symbolField.setMaximumSize(symbolField.getPreferredSize());

        panelSymbol.add(symLab);
        panelSymbol.add(symbolField);

        //panel for operator type
        JPanel panelType = new JPanel();
        panelType.setLayout(new BoxLayout(panelType, BoxLayout.X_AXIS));
        JLabel typeLab = new JLabel("Operator type");
        typeLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));

        String[] operators = { "Unary", "Binary" };
        opType = new JComboBox(operators);
        opType.setMaximumSize(opType.getPreferredSize());
        panelType.add(typeLab);
        panelType.add(opType);


        //panel for precedence
        JPanel precedencePanel = new JPanel();
        precedencePanel.setLayout(new BoxLayout(precedencePanel, BoxLayout.X_AXIS));
        JLabel precedenceLabel = new JLabel("Operator precedence");
        precedenceLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        precedenceField = new JTextField(10);
        precedenceField.setMaximumSize(precedenceField.getPreferredSize());
        precedencePanel.add(precedenceLabel);
        precedencePanel.add(precedenceField);


        //panel for return type
        JPanel returnTypePanel = new JPanel();
        returnTypePanel.setLayout(new BoxLayout(returnTypePanel, BoxLayout.X_AXIS));
        JLabel returnTypeLabel = new JLabel("Return type");
        returnTypeLabel.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        opRetType = new JComboBox(typeMap.keySet().toArray());
        opRetType.setMaximumSize(opRetType.getPreferredSize());
        returnTypePanel.add(returnTypeLabel);
        returnTypePanel.add(opRetType);

        contentPane.add(panelSymbol);
        contentPane.add(panelType);
        contentPane.add(precedencePanel);
        contentPane.add(returnTypePanel);
        contentPane.add(addBtn);

    }


    public void showWindow() {
        setVisible(true);
    }

}
