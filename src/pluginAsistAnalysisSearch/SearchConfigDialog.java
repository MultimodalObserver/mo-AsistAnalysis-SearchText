/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pluginAsistAnalysisSearch;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import mo.core.ui.GridBConstraints;
import mo.core.ui.Utils;
import mo.organization.ProjectOrganization;

/**
 *
 * @author Lathy
 */
public class SearchConfigDialog extends JDialog implements DocumentListener {
    
    JLabel errorLabel;
    JTextField nameField;
    JButton accept;

    ProjectOrganization org;

    boolean accepted = false;

    ResourceBundle dialogBundle = java.util.ResourceBundle.getBundle("properties/principal");
    
    public SearchConfigDialog() {
        super(null, "Asist Analysis Configuration", Dialog.ModalityType.APPLICATION_MODAL);
    }

    SearchConfigDialog(ProjectOrganization organization) {
        super(null, "Asist Analysis Configuration", Dialog.ModalityType.APPLICATION_MODAL);
        org = organization;

    }
    
    public boolean showDialog() {

        setLayout(new GridBagLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                accepted = false;
                super.windowClosing(e);
            }
        });

        setLayout(new GridBagLayout());
        GridBConstraints gbc = new GridBConstraints();

        JLabel label = new JLabel(dialogBundle.getString("configuration_n"));
        nameField = new JTextField();
        nameField.getDocument().addDocumentListener(this);

        gbc.gx(0).gy(0).f(GridBConstraints.HORIZONTAL)
                .a(GridBConstraints.FIRST_LINE_START)
                .i(new Insets(5, 5, 5, 5));

        add(label, gbc);
        add(nameField, gbc.gx(2).wx(1));

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.red);
        add(errorLabel, gbc.gx(0).gy(2).gw(3).a(GridBConstraints.LAST_LINE_START).wy(1));

        accept = new JButton(dialogBundle.getString("accept"));
        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accepted = true;
                setVisible(false);
                dispose();
            }
        });

        gbc.gx(0).gy(3).a(GridBConstraints.LAST_LINE_END).gw(3).wy(1).f(GridBConstraints.NONE);
        add(accept, gbc);

        setMinimumSize(new Dimension(400, 150));
        setPreferredSize(new Dimension(400, 300));
        pack();
        Utils.centerOnScreen(this);
        updateState();
        setVisible(true);

        return accepted;
    }
    

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateState();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateState();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateState();
    }
    
    private void updateState() {
        if (nameField.getText().isEmpty()) {
            errorLabel.setText(dialogBundle.getString("name"));
            accept.setEnabled(false);
        } else {
            errorLabel.setText("");
            accept.setEnabled(true);
        }
    }
    
    public String getConfigurationName() {
        return nameField.getText();
    }
    
}
