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

public class AddOperator extends JFrame {

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
    public AddOperator(HomeScreen homeScreen) {

        typeMap = new HashMap<>();

        typeMap.put("INT", DataType.INT);
        typeMap.put("NAT", DataType.NAT);
        typeMap.put("STRING", DataType.STRING);
        typeMap.put("BOOLEAN", DataType.BOOLEAN);

        home = homeScreen;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });


        //Add operator button
        addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {


            String symbol = symbolField.getText();
            String precedenceField = this.precedenceField.getText();

            if (symbol.equals("") || precedenceField.equals("")) {
                JOptionPane.showMessageDialog(AddOperator.this, "Field cannot be empty.",
                        "Missing values", JOptionPane.ERROR_MESSAGE);
            } else if (symbol.equals("{") || symbol.equals("}") || symbol.equals("(") ||
                    symbol.equals(")") || symbol.equals("[") || symbol.equals("]")) {
                JOptionPane.showMessageDialog(AddOperator.this,
                        "Your operator symbol is invalid, cannot be any sort of brackets.",
                        "Invalid values", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int precedence = Integer.parseInt(precedenceField);
                    if (precedence <= 0) {
                        JOptionPane.showMessageDialog(AddOperator.this, "Precedence must be a number greater than 0.", "Invalid values", JOptionPane.ERROR_MESSAGE);

                    } else {
                        Operator operator;
                        if (opType.getSelectedIndex() == 0) {
                            operator = new UnaryOperator(symbol, precedence, typeMap.get(opRetType.getSelectedItem()));
                        } else {
                            operator = new BinaryOperator(symbol, precedence, typeMap.get(opRetType.getSelectedItem()));
                        }

                        home.getSignature().addOperator(operator);
                        home.updateUI();
                        AddOperator.this.dispose();
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(AddOperator.this,
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

        String[] ops = { "Unary", "Binary" };
        opType = new JComboBox(ops);
        opType.setMaximumSize(opType.getPreferredSize());
        panelType.add(typeLab);
        panelType.add(opType);


        //panel for precedence

        JPanel precedencePanel = new JPanel();
        precedencePanel.setLayout(new BoxLayout(precedencePanel, BoxLayout.X_AXIS));
        JLabel precLab = new JLabel("Operator precedence");
        precLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        precedenceField = new JTextField(10);
        precedenceField.setMaximumSize(precedenceField.getPreferredSize());
        precedencePanel.add(precLab);
        precedencePanel.add(precedenceField);


        //panel for return type

        JPanel returnTypePanel = new JPanel();
        returnTypePanel.setLayout(new BoxLayout(returnTypePanel, BoxLayout.X_AXIS));
        JLabel rtLab = new JLabel("Return type");
        rtLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        opRetType = new JComboBox(typeMap.keySet().toArray());
        opRetType.setMaximumSize(opRetType.getPreferredSize());
        returnTypePanel.add(rtLab);
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
