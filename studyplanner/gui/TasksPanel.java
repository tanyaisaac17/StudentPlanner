package com.studyplanner.gui;

import com.studyplanner.model.Task;
import com.studyplanner.service.PlannerService;
import com.studyplanner.service.PlannerServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TasksPanel extends JPanel {
    private PlannerService plannerService;
    private JPanel tasksListPanel;
    private JPanel addFormPanel;
    private JTextArea txtTaskDescription;
    private JButton btnClearCompleted;
    
    private static final Color BG = new Color(15, 14, 46);
    private static final Color CARD_BG = new Color(30, 41, 59);
    private static final Color TEXT_COLOR = new Color(226, 232, 240);
    private static final Color INPUT_BG = new Color(20, 33, 61);
    private static final Color BTN_COLOR = new Color(236, 72, 153);
    
    public TasksPanel() {
        this.plannerService = new PlannerServiceImpl();
        initComponents();
        loadTasks();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BG);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Task Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(236, 72, 153));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerActions.setBackground(BG);

        JButton btnAdd = new JButton("Add Task");
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
        btnAdd.setPreferredSize(new Dimension(130, 35));
        btnAdd.addActionListener(e -> showAddForm());

        btnClearCompleted = new JButton("Clear Done");
        btnClearCompleted.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnClearCompleted.setBackground(new Color(239, 68, 68));
        btnClearCompleted.setForeground(Color.WHITE);
        btnClearCompleted.setBorderPainted(false);
        btnClearCompleted.setFocusPainted(false);
        btnClearCompleted.setContentAreaFilled(true);
        btnClearCompleted.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClearCompleted.setPreferredSize(new Dimension(130, 35));
        btnClearCompleted.addActionListener(e -> clearCompletedTasks());

        headerActions.add(btnAdd);
        headerActions.add(btnClearCompleted);
        headerPanel.add(headerActions, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Add form (hidden by default)
        addFormPanel = createAddForm();
        addFormPanel.setVisible(false);
        addFormPanel.setMaximumSize(new Dimension(500, 200));
        contentPanel.add(addFormPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Tasks list
        tasksListPanel = new JPanel();
        tasksListPanel.setLayout(new BoxLayout(tasksListPanel, BoxLayout.Y_AXIS));
        tasksListPanel.setBackground(BG);
        
        JScrollPane scrollPane = new JScrollPane(tasksListPanel);
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
        gbc.fill = GridBagConstraints.BOTH;
        
        // Task label
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel taskLabel = new JLabel("Task Description:");
        taskLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        taskLabel.setForeground(TEXT_COLOR);
        formPanel.add(taskLabel, gbc);
        
        // Task text area
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weighty = 1.0;
        txtTaskDescription = new JTextArea(4, 30);
        txtTaskDescription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTaskDescription.setBackground(INPUT_BG);
        txtTaskDescription.setForeground(TEXT_COLOR);
        txtTaskDescription.setCaretColor(TEXT_COLOR);
        txtTaskDescription.setLineWrap(true);
        txtTaskDescription.setWrapStyleWord(true);
        txtTaskDescription.setBorder(BorderFactory.createLineBorder(BTN_COLOR, 1));
        formPanel.add(txtTaskDescription, gbc);
        
        // Button panel
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weighty = 0;
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(CARD_BG);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton btnSave = new JButton("Save Task");
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
        btnSave.addActionListener(e -> saveTask());
        
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
        txtTaskDescription.setText("");
        txtTaskDescription.requestFocus();
    }
    
    private void hideAddForm() {
        addFormPanel.setVisible(false);
    }
    
    private void saveTask() {
        String description = txtTaskDescription.getText().trim();
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a task description!");
            return;
        }
        
        try {
            Task task = new Task(description);
            plannerService.addTask(task);
            
            JOptionPane.showMessageDialog(this, "Task added successfully!");
            hideAddForm();
            loadTasks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void loadTasks() {
        tasksListPanel.removeAll();
        try {
            List<Task> tasks = plannerService.getAllTasks();
            if (btnClearCompleted != null) {
                btnClearCompleted.setEnabled(tasks.stream().anyMatch(Task::isCompleted));
            }
            if (tasks.isEmpty()) {
                JLabel noDataLabel = new JLabel("No tasks yet. Click '+ Add Task' to create one!");
                noDataLabel.setForeground(new Color(148, 163, 184));
                noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                noDataLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                tasksListPanel.add(noDataLabel);
            } else {
                for (Task task : tasks) {
                    tasksListPanel.add(createTaskCard(task));
                    tasksListPanel.add(Box.createVerticalStrut(12));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tasksListPanel.revalidate();
        tasksListPanel.repaint();
    }
    
    private JPanel createTaskCard(Task task) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(15, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(task.isCompleted() ? new Color(34, 197, 94) : BTN_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JCheckBox checkbox = new JCheckBox(task.getTaskDescription());
        checkbox.setSelected(task.isCompleted());
        checkbox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        checkbox.setForeground(TEXT_COLOR);
        checkbox.setBackground(CARD_BG);
        checkbox.addActionListener(e -> markTaskComplete(task, checkbox.isSelected()));
        card.add(checkbox, BorderLayout.WEST);

        JButton btnDelete = new JButton("Remove");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDelete.setForeground(new Color(239, 68, 68));
        btnDelete.setOpaque(false);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> deleteTask(task));
        card.add(btnDelete, BorderLayout.EAST);
        
        return card;
    }
    
    private void markTaskComplete(Task task, boolean completed) {
        try {
            task.setCompleted(completed);
            plannerService.updateTask(task);
            loadTasks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTask(Task task) {
        try {
            plannerService.deleteTask(task.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadTasks();
    }

    private void clearCompletedTasks() {
        try {
            List<Task> snapshot = List.copyOf(plannerService.getAllTasks());
            for (Task task : snapshot) {
                if (task.isCompleted()) {
                    plannerService.deleteTask(task.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadTasks();
    }
}
