package com.studyplanner.gui;

import com.studyplanner.model.Subject;
import com.studyplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ADD SUBJECT PANEL - Converted from AddSubjectForm (JDialog) to JPanel for CardLayout
 * 
 * KEY IMPROVEMENTS:
 * - Uses CardLayout for navigation (no separate windows)
 * - Top navigation bar with back button
 * - Auto-close after successful save:
 *   1. Show success dialog
 *   2. Clear fields
 *   3. Navigate to DASHBOARD automatically
 * - Disable save button if fields are empty
 * - Shows confirmation dialogs
 * - Cleaner UX
 */
public class AddSubjectPanel extends JPanel implements ActionListener {
    
    private NavigationListener navigationListener;
    private PlannerService plannerService;
    
    private JTextField txtSubjectName;
    private JSpinner spinnerCredits;
    private JButton btnAdd;
    private JButton btnCancel;
    private JLabel lblStatus;
    
    /**
     * CONSTRUCTOR
     */
    public AddSubjectPanel(NavigationListener navigationListener, PlannerService plannerService) {
        this.navigationListener = navigationListener;
        this.plannerService = plannerService;
        
        initializeComponents();
    }
    
    /**
     * Initialize UI components
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(UITheme.PRIMARY_BACKGROUND);
        
        // Top navigation bar
        TopNavigationBar navBar = new TopNavigationBar(navigationListener, "Add New Subject");
        add(navBar, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = UITheme.createRootPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Form panel (centered)
        JPanel formPanel = UITheme.createCardPanel(new BorderLayout(15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        formPanel.setMaximumSize(new Dimension(500, 400));
        formPanel.setPreferredSize(new Dimension(500, 400));
        
        // Subject name row
        JPanel nameRow = createFormRow("Subject Name:", UITheme.FONT_NORMAL);
        txtSubjectName = new JTextField();
        UITheme.styleTextField(txtSubjectName);
        txtSubjectName.setFont(UITheme.FONT_NORMAL);
        txtSubjectName.setPreferredSize(new Dimension(250, 40));
        // Add listener to enable/disable save button
        txtSubjectName.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateSaveButton(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateSaveButton(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateSaveButton(); }
        });
        nameRow.add(txtSubjectName);
        formPanel.add(nameRow, BorderLayout.NORTH);
        
        // Credits row
        JPanel creditsRow = createFormRow("Credits (1-6):", UITheme.FONT_NORMAL);
        spinnerCredits = new JSpinner(new SpinnerNumberModel(1, 1, 6, 1));
        spinnerCredits.setFont(UITheme.FONT_NORMAL);
        spinnerCredits.setPreferredSize(new Dimension(250, 40));
        creditsRow.add(spinnerCredits);
        formPanel.add(creditsRow, BorderLayout.CENTER);
        
        // Status label
        lblStatus = new JLabel(" ");
        lblStatus.setFont(UITheme.FONT_NORMAL);
        lblStatus.setForeground(new Color(100, 116, 139));
        JPanel statusPanel = new JPanel();
        statusPanel.setOpaque(false);
        statusPanel.add(lblStatus);
        formPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Button panel
        JPanel buttonPanel = UITheme.createTransparentPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnAdd = UITheme.primaryButton("Add Subject");
        btnAdd.setPreferredSize(new Dimension(140, 45));
        btnAdd.addActionListener(this);
        
        btnCancel = UITheme.secondaryButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(140, 45));
        btnCancel.addActionListener(this);
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);
        
        // Center form in main panel
        JPanel centerWrapper = UITheme.createTransparentPanel(new GridBagLayout());
        centerWrapper.add(formPanel, new GridBagConstraints());
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        
        // Bottom button panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Initialize save button state
        updateSaveButton();
    }
    
    /**
     * Create a form row with label and input field
     */
    private JPanel createFormRow(String label, Font font) {
        JPanel row = UITheme.createTransparentPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        JLabel lbl = UITheme.createNormalLabel(label);
        lbl.setFont(font);
        lbl.setPreferredSize(new Dimension(130, 35));
        
        row.add(lbl);
        return row;
    }
    
    /**
     * Update save button state based on field validity
     * DISABLED if: subject name is empty
     */
    private void updateSaveButton() {
        String subjectName = txtSubjectName.getText().trim();
        btnAdd.setEnabled(!subjectName.isEmpty());
        if (subjectName.isEmpty()) {
            btnAdd.setOpaque(true);
            btnAdd.setBackground(new Color(148, 163, 184));
        } else {
            btnAdd.setBackground(UITheme.PRIMARY_BUTTON);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addSubject();
        } else if (e.getSource() == btnCancel) {
            // Navigate back to dashboard
            navigationListener.navigateTo(MainFrame.DASHBOARD_SCREEN);
        }
    }
    
    /**
     * Add subject to database with auto-close
     * 
     * FLOW:
     * 1. Validate input
     * 2. Insert to database
     * 3. Show success dialog
     * 4. Clear fields
     * 5. Navigate to DASHBOARD (auto-close)
     */
    private void addSubject() {
        String subjectName = txtSubjectName.getText().trim();
        
        // Validation
        if (subjectName.isEmpty()) {
            showError("❌ Please enter subject name!");
            return;
        }
        
        if (subjectName.length() > 50) {
            showError("❌ Subject name too long (max 50 characters)!");
            return;
        }
        
        int credits = (Integer) spinnerCredits.getValue();
        
        try {
            // Create and save subject
            Subject subject = new Subject(subjectName);
            subject.setCredits(credits);
            
            if (plannerService.addSubject(subject)) {
                // Show success dialog
                JOptionPane.showMessageDialog(
                    this,
                    "✓ Subject '" + subjectName + "' added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Clear fields
                txtSubjectName.setText("");
                spinnerCredits.setValue(1);
                lblStatus.setText(" ");
                
                // Auto-close: navigate back to dashboard
                SwingUtilities.invokeLater(() -> {
                    navigationListener.navigateTo(MainFrame.DASHBOARD_SCREEN);
                });
            } else {
                showError("❌ Failed to add subject. Try again.");
            }
        } catch (Exception ex) {
            showError("❌ Error: " + ex.getMessage());
        }
    }
    
    /**
     * Show error message
     */
    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.setForeground(UITheme.DANGER_BUTTON);
    }
}
