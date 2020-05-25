package com.kooixiuhong.rewritesystem.app.gui;

import com.kooixiuhong.rewritesystem.app.syntaxtree.DataType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class AddVariable extends JFrame {

    private JPanel contentPane;
    private JTextField symbolField;
    private JButton addBtn;
    private JComboBox type;
    private Map<String, DataType> typeMap;
    private HomeScreen home;


    /**
     * Create the frame.
     */
    public AddVariable(HomeScreen homeScreen) {

        home = homeScreen;

        typeMap = new HashMap<>();
        typeMap.put("INT", DataType.INT);
        typeMap.put("NAT", DataType.NAT);
        typeMap.put("STRING", DataType.STRING);
        typeMap.put("BOOLEAN", DataType.BOOLEAN);
        typeMap.put("CONSTANT", DataType.CONST);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        //setting up frame
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        addBtn = new JButton("Add");
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (symbolField.getText().equals("")) {
                    JOptionPane.showMessageDialog(AddVariable.this,
                            "Field cannot be empty.", "Missing values", JOptionPane.ERROR_MESSAGE);
                } else {
                    home.getSignature().addVariable(symbolField.getText(), typeMap.get(type.getSelectedItem()));
                    home.updateUI();
                    AddVariable.this.dispose();
                }
            }
        });


        //panel for symbol text field
        JPanel panelSymbol = new JPanel();
        panelSymbol.setLayout(new BoxLayout(panelSymbol, BoxLayout.X_AXIS));
        JLabel symLab = new JLabel("Variable name");
        symLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        symbolField = new JTextField(10);
        symbolField.setMaximumSize(symbolField.getPreferredSize());

        panelSymbol.add(symLab);
        panelSymbol.add(symbolField);


        //panel for return type
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.X_AXIS));
        JLabel tLab = new JLabel("Type");
        tLab.setFont(new Font("Century Gothic", Font.PLAIN, 14));

        type = new JComboBox(typeMap.keySet().toArray());
        type.setMaximumSize(type.getPreferredSize());
        typePanel.add(tLab);
        typePanel.add(type);

        contentPane.add(panelSymbol);
        contentPane.add(typePanel);
        contentPane.add(addBtn);

    }


    public void showWindow() {
        setVisible(true);
    }

}
