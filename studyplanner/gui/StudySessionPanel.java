package com.studyplanner.gui;

import com.studyplanner.service.PlannerService;
import com.studyplanner.service.PlannerServiceImpl;

import javax.swing.*;
import java.awt.*;

public class StudySessionPanel extends JPanel {
    private PlannerService plannerService;
    private JLabel timerLabel;
    private JLabel statusLabel;
    private JButton btnStartSession;
    private JButton btnDoneStudying;
    private JComboBox<String> subjectCombo;
    private Thread timerThread;
    private JSpinner spinnerHours;
    private JButton btnMinusHour;
    private JButton btnPlusHour;

    private volatile boolean isSessionActive = false;
    private volatile int totalSeconds = 3600;
    private int plannedHours = 1;
    
    private static final Color BG = new Color(15, 14, 46);
    private static final Color CARD_BG = new Color(30, 41, 59);
    private static final Color TEXT_COLOR = new Color(226, 232, 240);
    private static final Color INPUT_BG = new Color(20, 33, 61);
    private static final Color BTN_START = new Color(56, 189, 248);
    private static final Color BTN_DONE = new Color(34, 197, 94);
    
    public StudySessionPanel() {
        this.plannerService = new PlannerServiceImpl();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BG);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        JLabel titleLabel = new JLabel("Study Session");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(236, 72, 153));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Session card
        JPanel sessionCard = new JPanel();
        sessionCard.setLayout(new GridBagLayout());
        sessionCard.setBackground(CARD_BG);
        sessionCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(56, 189, 248), 2),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        sessionCard.setMaximumSize(new Dimension(500, 400));
        sessionCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridwidth = 2;
        
        // Subject label
        gbc.gridy = 0;
        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        subjectLabel.setForeground(TEXT_COLOR);
        sessionCard.add(subjectLabel, gbc);
        
        // Subject combo
        gbc.gridy = 1;
        subjectCombo = new JComboBox<>();
        subjectCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subjectCombo.setBackground(INPUT_BG);
        subjectCombo.setForeground(TEXT_COLOR);
        loadSubjects();
        sessionCard.add(subjectCombo, gbc);

        // Duration controls (plus/minus)
        gbc.gridy = 2;
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        durationPanel.setBackground(CARD_BG);

        JLabel durationLabel = new JLabel("Study Hours:");
        durationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        durationLabel.setForeground(TEXT_COLOR);

        spinnerHours = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1));
        spinnerHours.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (spinnerHours.getEditor() instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) spinnerHours.getEditor()).getTextField().setColumns(3);
        }

        btnMinusHour = createTinyButton("−", new Color(107, 114, 128));
        btnMinusHour.addActionListener(e -> adjustHours(-1));

        btnPlusHour = createTinyButton("+", BTN_START);
        btnPlusHour.addActionListener(e -> adjustHours(1));

        spinnerHours.addChangeListener(e -> onHoursChanged());

        durationPanel.add(durationLabel);
        durationPanel.add(spinnerHours);
        durationPanel.add(btnMinusHour);
        durationPanel.add(btnPlusHour);
        sessionCard.add(durationPanel, gbc);
        
        // Timer display
        gbc.gridy = 3;
        timerLabel = new JLabel("⏱️ 01:00:00");
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 48));
        timerLabel.setForeground(new Color(236, 72, 153));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        sessionCard.add(timerLabel, gbc);
        
        // Status label
        gbc.gridy = 4;
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(148, 163, 184));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        sessionCard.add(statusLabel, gbc);
        
        // Button panel
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        btnStartSession = createButton("Start Session", BTN_START);
        btnStartSession.addActionListener(e -> startSession());
        sessionCard.add(btnStartSession, gbc);
        
        gbc.gridx = 1;
        btnDoneStudying = createButton("Done Studying", BTN_DONE);
        btnDoneStudying.setEnabled(false);
        btnDoneStudying.addActionListener(e -> completeSession());
        sessionCard.add(btnDoneStudying, gbc);
        
        contentPanel.add(sessionCard);
        contentPanel.add(Box.createVerticalGlue());
        
        add(contentPanel, BorderLayout.CENTER);

        onHoursChanged();
    }
    
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setIcon(null);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 45));
        return btn;
    }

    private JButton createTinyButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(44, 36));
        return btn;
    }

    private void adjustHours(int delta) {
        int current = (Integer) spinnerHours.getValue();
        int next = Math.max(1, Math.min(24, current + delta));
        if (next != current) {
            spinnerHours.setValue(next);
        }
    }

    private void onHoursChanged() {
        int newHours = (Integer) spinnerHours.getValue();
        int deltaHours = newHours - plannedHours;
        plannedHours = newHours;

        if (isSessionActive) {
            totalSeconds = Math.max(0, totalSeconds + (deltaHours * 3600));
        } else {
            totalSeconds = plannedHours * 3600;
        }

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        timerLabel.setText(String.format("⏱️ %02d:%02d:%02d", hours, minutes, seconds));

        statusLabel.setText(plannedHours == 1
            ? "Ready to start your 1-hour study session"
            : "Ready to start your " + plannedHours + "-hour study session");
    }
    
    private void loadSubjects() {
        try {
            subjectCombo.removeAllItems();
            plannerService.getAllSubjects().forEach(s -> subjectCombo.addItem(s.getName()));
            if (subjectCombo.getItemCount() == 0) {
                subjectCombo.addItem("No subjects available");
                subjectCombo.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void startSession() {
        if (subjectCombo.getItemCount() == 0 || "No subjects available".equals(subjectCombo.getSelectedItem())) {
            JOptionPane.showMessageDialog(this, "Add a subject first!");
            return;
        }
        
        isSessionActive = true;
        plannedHours = (Integer) spinnerHours.getValue();
        totalSeconds = plannedHours * 3600;
        btnStartSession.setEnabled(false);
        btnDoneStudying.setEnabled(true);
        subjectCombo.setEnabled(false);
        spinnerHours.setEnabled(false);
        
        timerThread = new Thread(this::runTimer);
        timerThread.start();
    }
    
    private void runTimer() {
        while (totalSeconds > 0 && isSessionActive) {
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;
            
            SwingUtilities.invokeLater(() -> timerLabel.setText(String.format("⏱️ %02d:%02d:%02d", hours, minutes, seconds)));
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            
            totalSeconds--;
        }
        
        if (isSessionActive && totalSeconds == 0) {
            completeSession();
        }
    }
    
    private void completeSession() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::completeSession);
            return;
        }

        isSessionActive = false;
        
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
        
        JOptionPane.showMessageDialog(this, 
            "🎉 Great job! " + plannedHours + " hour(s) completed!\nSession saved successfully.",
            "Session Complete",
            JOptionPane.INFORMATION_MESSAGE);
        
        resetUI();
    }
    
    private void resetUI() {
        plannedHours = (Integer) spinnerHours.getValue();
        totalSeconds = plannedHours * 3600;
        timerLabel.setText("⏱️ 01:00:00");
        btnStartSession.setEnabled(true);
        btnDoneStudying.setEnabled(false);
        subjectCombo.setEnabled(true);
        spinnerHours.setEnabled(true);
        onHoursChanged();
    }
}
