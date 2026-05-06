package com.studyplanner.gui;

import com.studyplanner.model.StudySession;
import com.studyplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AddSessionForm extends JDialog implements ActionListener {
    
    // Modern Color Theme
    private static final Color BG_COLOR = new Color(244, 246, 248);      // #F4F6F8
    private static final Color SECONDARY_COLOR = new Color(33, 150, 243); // #2196F3
    private static final Color DANGER_COLOR = new Color(244, 67, 54);    // #F44336
    private static final Color TEXT_COLOR = new Color(51, 51, 51);       // #333333
    
    private PlannerService plannerService;
    
    // GUI Components
    private JComboBox<String> comboSubject;
    private JSpinner spinnerHours;
    private JButton btnAdd;
    private JButton btnCancel;
    private JLabel lblStatus;
    
    public AddSessionForm(PlannerService plannerService) {
        this.plannerService = plannerService;
        
        setTitle("⏱️ Add Study Session");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        
        initializeComponents();
        loadSubjects();
        
        setVisible(true);
    }
    

    private void initializeComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BG_COLOR);
        
        // ===== TITLE LABEL =====
        JLabel lblTitle = new JLabel("Add Study Session");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // ===== SUBJECT SELECTION =====
        JPanel subjectPanel = new JPanel();
        subjectPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        subjectPanel.setBackground(BG_COLOR);
        
        JLabel lblSubject = new JLabel("Select Subject:");
        lblSubject.setFont(new Font("Arial", Font.BOLD, 14));
        lblSubject.setForeground(TEXT_COLOR);
        lblSubject.setPreferredSize(new Dimension(130, 25));
        
        comboSubject = new JComboBox<>();
        comboSubject.setPreferredSize(new Dimension(240, 35));
        comboSubject.setFont(new Font("Arial", Font.PLAIN, 14));
        
        subjectPanel.add(lblSubject);
        subjectPanel.add(comboSubject);
        
        // ===== HOURS FIELD =====
        JPanel hoursPanel = new JPanel();
        hoursPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        hoursPanel.setBackground(BG_COLOR);
        
        JLabel lblHours = new JLabel("Study Hours (1-24):");
        lblHours.setFont(new Font("Arial", Font.BOLD, 14));
        lblHours.setForeground(TEXT_COLOR);
        lblHours.setPreferredSize(new Dimension(130, 25));
        
        spinnerHours = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1));
        spinnerHours.setPreferredSize(new Dimension(240, 35));
        spinnerHours.setFont(new Font("Arial", Font.PLAIN, 14));
        
        hoursPanel.add(lblHours);
        hoursPanel.add(spinnerHours);
        
        // ===== STATUS LABEL =====
        lblStatus = new JLabel("");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(100, 100, 100));
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(BG_COLOR);
        statusPanel.add(lblStatus);
        
        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(BG_COLOR);
        
        btnAdd = createStyledButton("Add Session", SECONDARY_COLOR);
        btnCancel = createStyledButton("Cancel", DANGER_COLOR);
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(subjectPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(hoursPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(statusPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    
    /**
     * Create styled button with modern design
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.CENTER);
        btn.addActionListener(this);
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            private Color originalColor = bgColor;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(darkenColor(originalColor, 0.8f));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(originalColor);
            }
        });
        
        return btn;
    }
    
    /**
     * Helper method to darken a color
     */
    private Color darkenColor(Color color, float factor) {
        return new Color(
            (int) (color.getRed() * factor),
            (int) (color.getGreen() * factor),
            (int) (color.getBlue() * factor)
        );
    }
    
    /**
     * Load subjects from database and populate combobox
     * Demonstrates dynamic population from database
     */
    private void loadSubjects() {
        try {
            var subjects = plannerService.getAllSubjects();
            comboSubject.removeAllItems();
            
            for (var subject : subjects) {
                comboSubject.addItem(subject.getName());
            }
            
            if (subjects.isEmpty()) {
                comboSubject.addItem("No subjects available");
            }
        } catch (Exception e) {
            showError("Failed to load subjects: " + e.getMessage());
        }
    }
    
    /**
     * ACTION LISTENER
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addSession();
        } else if (e.getSource() == btnCancel) {
            dispose();
        }
    }
    
    /**
     * Add study session to database
     */
    private void addSession() {
        String subject = (String) comboSubject.getSelectedItem();
        
        if (subject == null || subject.equals("No subjects available")) {
            showError("Please select a valid subject!");
            return;
        }
        
        int hours = (Integer) spinnerHours.getValue();
        
        try {
            StudySession session = new StudySession(subject, hours);
            
            if (plannerService.addStudySession(session)) {
                showSuccess("Study session added successfully!");
                spinnerHours.setValue(1);
            } else {
                showError("Failed to add study session!");
            }
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }
    
    private void showError(String message) {
        lblStatus.setForeground(new Color(231, 76, 60));
        lblStatus.setText("❌ " + message);
    }
    
    private void showSuccess(String message) {
        lblStatus.setForeground(new Color(46, 204, 113));
        lblStatus.setText("✓ " + message);
    }
}
