package com.studyplanner.gui;

import com.studyplanner.model.StudySession;
import com.studyplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ADD SESSION PANEL - Converted from AddSessionForm to CardLayout panel with TIMER FEATURE
 * 
 * KEY FEATURES:
 * - Uses CardLayout for navigation
 * - Top navigation bar with back button
 * - AUTO-START TIMER (1-hour demo, customizable)
 * - "Mark Session Completed" button
 * - Shows countdown timer display
 * - Auto-complete when timer reaches 0:00
 * - Auto-close after session completion
 * 
 * THREADING NOTE:
 * - Uses javax.swing.Timer for thread-safe UI updates
 * - All timer updates happen on EDT (Event Dispatch Thread)
 * - No need for SwingUtilities.invokeLater() with Timer
 */
public class AddSessionPanel extends JPanel implements ActionListener {
    
    private NavigationListener navigationListener;
    private PlannerService plannerService;
    
    private JComboBox<String> comboSubject;
    private JSpinner spinnerHours;
    private JButton btnMinusHour;
    private JButton btnPlusHour;
    private JButton btnMarkCompleted;
    private JButton btnCancel;
    private JLabel lblStatus;
    private JLabel lblTimer;
    
    private Timer sessionTimer;
    private int remainingSeconds;
    private int plannedHours = 1;
    private int elapsedSeconds = 0;
    private boolean sessionStarted = false;
    private boolean sessionCompleted = false;
    private String selectedSubject;
    private int selectedHours;
    
    /**
     * CONSTRUCTOR
     */
    public AddSessionPanel(NavigationListener navigationListener, PlannerService plannerService) {
        this.navigationListener = navigationListener;
        this.plannerService = plannerService;
        this.remainingSeconds = plannedHours * 3600;
        
        initializeComponents();
        loadSubjects();
        updateTimerDisplay();
        startSessionTimer();
    }
    
    /**
     * Initialize UI components
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(UITheme.PRIMARY_BACKGROUND);
        
        // Top navigation bar
        TopNavigationBar navBar = new TopNavigationBar(navigationListener, "Start Study Session");
        add(navBar, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = UITheme.createRootPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Form panel (centered)
        JPanel formPanel = UITheme.createCardPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        formPanel.setMaximumSize(new Dimension(500, 500));
        formPanel.setPreferredSize(new Dimension(500, 500));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Subject selection
        gbc.gridy = 0;
        JLabel lblSubject = UITheme.createNormalLabel("Select Subject:");
        formPanel.add(lblSubject, gbc);
        
        gbc.gridy = 1;
        comboSubject = new JComboBox<>();
        comboSubject.setFont(UITheme.FONT_NORMAL);
        comboSubject.setPreferredSize(new Dimension(250, 40));
        formPanel.add(comboSubject, gbc);
        
        // Hours field
        gbc.gridy = 2;
        JLabel lblHours = UITheme.createNormalLabel("Study Hours (1-24):");
        formPanel.add(lblHours, gbc);
        
        gbc.gridy = 3;
        spinnerHours = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1));
        spinnerHours.setFont(UITheme.FONT_NORMAL);
        spinnerHours.setPreferredSize(new Dimension(250, 40));
        spinnerHours.addChangeListener(e -> onHoursChanged());

        JPanel hoursControl = UITheme.createTransparentPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        hoursControl.add(spinnerHours);

        btnMinusHour = createTinyButton("−", UITheme.SECONDARY_BUTTON);
        btnMinusHour.addActionListener(e -> adjustHours(-1));
        hoursControl.add(btnMinusHour);

        btnPlusHour = createTinyButton("+", UITheme.SECONDARY_BUTTON);
        btnPlusHour.addActionListener(e -> adjustHours(1));
        hoursControl.add(btnPlusHour);

        formPanel.add(hoursControl, gbc);
        
        // Separator
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 0, 15, 0);
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setBackground(new Color(203, 213, 225));
        formPanel.add(sep, gbc);
        
        // Timer display (large, prominent)
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 10, 0);
        lblTimer = new JLabel("1:00:00", SwingConstants.CENTER);
        lblTimer.setFont(new Font("Consolas", Font.BOLD, 48));
        lblTimer.setForeground(UITheme.PRIMARY_BUTTON);
        formPanel.add(lblTimer, gbc);
        
        // Timer label
        gbc.gridy = 6;
        JLabel timerLabel = new JLabel("Session Duration", SwingConstants.CENTER);
        timerLabel.setFont(UITheme.FONT_NORMAL);
        timerLabel.setForeground(UITheme.TEXT_MUTED);
        formPanel.add(timerLabel, gbc);
        
        // Status label
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 0, 10, 0);
        lblStatus = new JLabel(" ");
        lblStatus.setFont(UITheme.FONT_NORMAL);
        lblStatus.setForeground(new Color(100, 116, 139));
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(lblStatus, gbc);
        
        // Center form in main panel
        JPanel centerWrapper = UITheme.createTransparentPanel(new GridBagLayout());
        centerWrapper.add(formPanel, new GridBagConstraints());
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = UITheme.createTransparentPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnMarkCompleted = UITheme.primaryButton("Mark Session Completed");
        btnMarkCompleted.setPreferredSize(new Dimension(220, 45));
        btnMarkCompleted.addActionListener(this);
        
        btnCancel = UITheme.secondaryButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(130, 45));
        btnCancel.addActionListener(this);
        
        buttonPanel.add(btnMarkCompleted);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Load subjects from database
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
     * START SESSION TIMER
     * 
     * TIMER MECHANISM:
     * - Uses javax.swing.Timer (thread-safe, EDT updates)
     * - Fires every 1000ms (1 second)
     * - Updates countdown display
     * - Auto-completes when reaching 0:00
     * 
     * THREADING: All UI updates happen on EDT, no explicit SwingUtilities needed
     */
    private void startSessionTimer() {
        sessionTimer = new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingSeconds > 0) {
                    remainingSeconds--;
                    elapsedSeconds++;
                    updateTimerDisplay();
                    
                    // Auto-complete when timer reaches 0
                    if (remainingSeconds == 0) {
                        ((Timer) e.getSource()).stop();
                        autoCompleteSession();
                    }
                }
            }
        });
        
        sessionTimer.setRepeats(true);
        sessionTimer.start();
    }
    
    /**
     * Update timer display (HH:MM:SS format)
     */
    private void updateTimerDisplay() {
        int hours = remainingSeconds / 3600;
        int minutes = (remainingSeconds % 3600) / 60;
        int seconds = remainingSeconds % 60;
        
        lblTimer.setText(String.format("%d:%02d:%02d", hours, minutes, seconds));
        
        // Change color to orange at 5 minutes remaining
        if (remainingSeconds <= 300) {
            lblTimer.setForeground(new Color(249, 115, 22)); // Orange
        }
        // Change color to red at 1 minute remaining
        if (remainingSeconds <= 60) {
            lblTimer.setForeground(new Color(239, 68, 68)); // Red
        }
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

        remainingSeconds = Math.max(0, remainingSeconds + (deltaHours * 3600));
        updateTimerDisplay();
    }

    private JButton createTinyButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BUTTON);
        btn.setBackground(color);
        btn.setForeground(UITheme.BUTTON_TEXT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(48, 40));
        return btn;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMarkCompleted) {
            markSessionCompleted();
        } else if (e.getSource() == btnCancel) {
            // Stop timer before navigating
            if (sessionTimer != null) {
                sessionTimer.stop();
            }
            navigationListener.navigateTo(MainFrame.DASHBOARD_SCREEN);
        }
    }
    
    /**
     * Mark session as completed
     * 
     * FLOW:
     * 1. Validate subject selection
     * 2. Stop timer
     * 3. Save session to database
     * 4. Show success dialog
     * 5. Auto-close (navigate to DASHBOARD)
     */
    private void markSessionCompleted() {
        if (sessionCompleted) {
            showError("Session already completed!");
            return;
        }
        
        String subject = (String) comboSubject.getSelectedItem();
        
        if (subject == null || subject.equals("No subjects available")) {
            showError("❌ Please select a valid subject!");
            return;
        }
        
        int hours = (Integer) spinnerHours.getValue();
        
        try {
            selectedSubject = subject;
            selectedHours = hours;
            
            // Stop timer
            if (sessionTimer != null) {
                sessionTimer.stop();
            }
            
            // Create and save session
            StudySession session = new StudySession(subject, hours);
            
            if (plannerService.addStudySession(session)) {
                sessionCompleted = true;
                
                // Show success dialog
                int minutesSpent = Math.max(0, elapsedSeconds / 60);
                
                String message = String.format(
                    "Study session completed!\n\n" +
                    "Subject: %s\n" +
                    "Duration: %d hours\n" +
                    "Time Spent: ~%d minutes\n\n" +
                    "Great work! Returning to dashboard...",
                    subject, hours, minutesSpent
                );
                
                JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Session Completed",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Auto-close: navigate back to dashboard
                SwingUtilities.invokeLater(() -> {
                    navigationListener.navigateTo(MainFrame.DASHBOARD_SCREEN);
                });
            } else {
                showError("❌ Failed to save session!");
                // Restart timer if save failed
                startSessionTimer();
            }
        } catch (Exception ex) {
            showError("❌ Error: " + ex.getMessage());
        }
    }
    
    /**
     * Auto-complete session when timer reaches 0:00
     * Called automatically by timer
     */
    private void autoCompleteSession() {
        lblStatus.setText("Time's up! Session auto-completing...");
        lblStatus.setForeground(UITheme.SECONDARY_BUTTON);
        
        // Automatically mark as completed
        SwingUtilities.invokeLater(this::markSessionCompleted);
    }
    
    /**
     * Show error message
     */
    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.setForeground(UITheme.DANGER_BUTTON);
    }
    
    /**
     * Cleanup when panel is removed
     */
    @Override
    public void removeNotify() {
        if (sessionTimer != null) {
            sessionTimer.stop();
        }
        super.removeNotify();
    }
}
