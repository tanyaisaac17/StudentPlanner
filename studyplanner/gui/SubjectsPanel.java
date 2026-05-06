package com.studyplanner.gui;

import com.studyplanner.model.Subject;
import com.studyplanner.service.PlannerService;
import com.studyplanner.service.PlannerServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SubjectsPanel extends JPanel {
    private PlannerService plannerService;
    private JPanel subjectsListPanel;
    private JPanel addFormPanel;
    private JTextField txtSubjectName;
    private JSpinner spinnerCredits;
    
    private static final Color BG = new Color(15, 14, 46);
    private static final Color CARD_BG = new Color(30, 41, 59);
    private static final Color TEXT_COLOR = new Color(226, 232, 240);
    private static final Color INPUT_BG = new Color(20, 33, 61);
    private static final Color BTN_COLOR = new Color(56, 189, 248);
    
    public SubjectsPanel() {
        this.plannerService = new PlannerServiceImpl();
        initComponents();
        loadSubjects();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BG);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Manage Subjects");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(236, 72, 153));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton btnAdd = new JButton("Add Subject");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdd.setBackground(BTN_COLOR);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBorderPainted(false);
        btnAdd.setFocusPainted(false);
        btnAdd.setContentAreaFilled(true);
        btnAdd.setIcon(null);
        btnAdd.setHorizontalAlignment(SwingConstants.CENTER);
        btnAdd.setVerticalAlignment(SwingConstants.CENTER);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new Dimension(150, 35));
        btnAdd.addActionListener(e -> showAddForm());
        headerPanel.add(btnAdd, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content area
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Add form (hidden by default)
        addFormPanel = createAddForm();
        addFormPanel.setVisible(false);
        addFormPanel.setMaximumSize(new Dimension(500, 180));
        contentPanel.add(addFormPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Subjects list
        subjectsListPanel = new JPanel();
        subjectsListPanel.setLayout(new BoxLayout(subjectsListPanel, BoxLayout.Y_AXIS));
        subjectsListPanel.setBackground(BG);
        
        JScrollPane scrollPane = new JScrollPane(subjectsListPanel);
        scrollPane.setBackground(BG);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.add(scrollPane);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createAddForm() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BTN_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Subject name label
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Subject Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameLabel.setForeground(TEXT_COLOR);
        formPanel.add(nameLabel, gbc);
        
        // Subject name field
        gbc.gridx = 1;
        txtSubjectName = new JTextField(20);
        txtSubjectName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSubjectName.setBackground(INPUT_BG);
        txtSubjectName.setForeground(TEXT_COLOR);
        txtSubjectName.setCaretColor(TEXT_COLOR);
        txtSubjectName.setBorder(BorderFactory.createLineBorder(BTN_COLOR, 1));
        formPanel.add(txtSubjectName, gbc);
        
        // Credits label
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel creditsLabel = new JLabel("Credits (1-6):");
        creditsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        creditsLabel.setForeground(TEXT_COLOR);
        formPanel.add(creditsLabel, gbc);
        
        // Credits spinner
        gbc.gridx = 1;
        spinnerCredits = new JSpinner(new SpinnerNumberModel(1, 1, 6, 1));
        spinnerCredits.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        spinnerCredits.setBackground(INPUT_BG);
        spinnerCredits.setForeground(TEXT_COLOR);
        formPanel.add(spinnerCredits, gbc);
        
        // Button panel
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(CARD_BG);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton btnSave = new JButton("Save Subject");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setBackground(new Color(34, 197, 94));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        btnSave.setContentAreaFilled(true);
        btnSave.setIcon(null);
        btnSave.setHorizontalAlignment(SwingConstants.CENTER);
        btnSave.setVerticalAlignment(SwingConstants.CENTER);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setPreferredSize(new Dimension(120, 32));
        btnSave.addActionListener(e -> saveSubject());
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancel.setBackground(new Color(107, 114, 128));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.setContentAreaFilled(true);
        btnCancel.setIcon(null);
        btnCancel.setHorizontalAlignment(SwingConstants.CENTER);
        btnCancel.setVerticalAlignment(SwingConstants.CENTER);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setPreferredSize(new Dimension(120, 32));
        btnCancel.addActionListener(e -> hideAddForm());
        
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        
        formPanel.add(btnPanel, gbc);
        
        return formPanel;
    }
    
    private void showAddForm() {
        addFormPanel.setVisible(true);
        txtSubjectName.setText("");
        spinnerCredits.setValue(1);
        txtSubjectName.requestFocus();
    }
    
    private void hideAddForm() {
        addFormPanel.setVisible(false);
    }
    
    private void saveSubject() {
        String name = txtSubjectName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a subject name!");
            return;
        }
        
        try {
            Subject subject = new Subject(name);
            plannerService.addSubject(subject);
            
            JOptionPane.showMessageDialog(this, "Subject added successfully!");
            hideAddForm();
            loadSubjects();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void loadSubjects() {
        subjectsListPanel.removeAll();
        try {
            List<Subject> subjects = plannerService.getAllSubjects();
            if (subjects.isEmpty()) {
                JLabel noDataLabel = new JLabel("No subjects added yet. Click '+ Add Subject' to get started!");
                noDataLabel.setForeground(new Color(148, 163, 184));
                noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noDataLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                subjectsListPanel.add(noDataLabel);
            } else {
                for (Subject subject : subjects) {
                    subjectsListPanel.add(createSubjectCard(subject));
                    subjectsListPanel.add(Box.createVerticalStrut(12));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        subjectsListPanel.revalidate();
        subjectsListPanel.repaint();
    }
    
    private JPanel createSubjectCard(Subject subject) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(15, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(56, 189, 248), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JLabel nameLabel = new JLabel(subject.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(236, 72, 153));
        card.add(nameLabel, BorderLayout.WEST);
        
        JLabel creditsLabel = new JLabel("Credits: " + subject.getCredits());
        creditsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        creditsLabel.setForeground(new Color(148, 163, 184));
        card.add(creditsLabel, BorderLayout.EAST);
        
        return card;
    }
}
