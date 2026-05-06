package com.studyplanner.gui;

import com.studyplanner.service.PlannerService;
import com.studyplanner.service.PlannerServiceImpl;

import javax.swing.*;
import java.awt.*;

public class SummaryPanel extends JPanel {
    private PlannerService plannerService;
    
    private static final Color BG = new Color(15, 14, 46);
    private static final Color CARD_BG = new Color(30, 41, 59);
    private static final Color TEXT_COLOR = new Color(226, 232, 240);
    private static final Color STAT_COLORS = new Color(56, 189, 248);
    
    public SummaryPanel() {
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
        JLabel titleLabel = new JLabel("Summary & Statistics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(236, 72, 153));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Stats grid
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(BG);
        statsPanel.setMaximumSize(new Dimension(600, 300));
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        statsPanel.add(createStatCard("Subjects", 0, new Color(236, 72, 153)));
        statsPanel.add(createStatCard("Study Hours", 0, new Color(56, 189, 248)));
        statsPanel.add(createStatCard("Tasks Completed", 0, new Color(34, 197, 94)));
        statsPanel.add(createStatCard("Total Tasks", 0, new Color(168, 85, 247)));
        
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(BG);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
        
        loadStatistics();
    }
    
    private JPanel createStatCard(String title, int value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(148, 163, 184));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        
        card.add(Box.createVerticalStrut(15));
        
        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(valueLabel);
        
        return card;
    }
    
    private void loadStatistics() {
        try {
            int subjects = plannerService.getAllSubjects().size();
            int tasks = plannerService.getAllTasks().size();
            long completedTasks = plannerService.getAllTasks().stream()
                .filter(t -> t.isCompleted()).count();
            
            // Update UI with real data
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
