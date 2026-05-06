package com.studyplanner.gui;

import com.studyplanner.model.Task;
import com.studyplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerPanel extends JPanel {

    private final PlannerService plannerService;

    private JLabel lblCounts;
    private StyledButton btnClearCompleted;

    private JPanel pendingList;
    private JPanel completedList;

    public TaskManagerPanel(PlannerService plannerService) {
        this.plannerService = plannerService;

        setLayout(new BorderLayout(10, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initializeComponents();
        reload();
    }

    private void initializeComponents() {
        JPanel header = UITheme.createTransparentPanel(new BorderLayout(10, 0));

        JLabel title = UITheme.createSectionLabel("Tasks");
        header.add(title, BorderLayout.WEST);

        lblCounts = UITheme.createNormalLabel(" ");
        lblCounts.setForeground(UITheme.TEXT_MUTED);
        header.add(lblCounts, BorderLayout.CENTER);

        btnClearCompleted = UITheme.dangerButton("Clear Completed");
        btnClearCompleted.setPreferredSize(new Dimension(180, 44));
        btnClearCompleted.addActionListener(e -> clearCompletedTasks());
        header.add(btnClearCompleted, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        UITheme.styleTabbedPane(tabs);

        pendingList = createListPanel();
        completedList = createListPanel();

        tabs.addTab("Remaining", wrapScroll(pendingList));
        tabs.addTab("Completed", wrapScroll(completedList));

        JPanel card = UITheme.createCardPanel(new BorderLayout());
        card.add(tabs, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);
    }

    private JPanel createListPanel() {
        JPanel list = UITheme.createTransparentPanel(null);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return list;
    }

    private JScrollPane wrapScroll(JComponent content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        return scrollPane;
    }

    public final void reload() {
        pendingList.removeAll();
        completedList.removeAll();

        List<Task> allTasks;
        try {
            allTasks = plannerService.getAllTasks();
        } catch (Exception e) {
            pendingList.add(errorLabel("Error loading tasks: " + e.getMessage()));
            completedList.add(errorLabel("Error loading tasks: " + e.getMessage()));
            revalidate();
            repaint();
            return;
        }

        List<Task> pending = new ArrayList<>();
        List<Task> completed = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.isCompleted()) {
                completed.add(task);
            } else {
                pending.add(task);
            }
        }

        if (pending.isEmpty()) {
            pendingList.add(emptyLabel("No remaining tasks."));
        } else {
            for (Task task : pending) {
                pendingList.add(createTaskRow(task));
                pendingList.add(Box.createVerticalStrut(10));
            }
        }

        if (completed.isEmpty()) {
            completedList.add(emptyLabel("No completed tasks."));
        } else {
            for (Task task : completed) {
                completedList.add(createTaskRow(task));
                completedList.add(Box.createVerticalStrut(10));
            }
        }

        lblCounts.setText(pending.size() + " remaining • " + completed.size() + " completed");
        btnClearCompleted.setEnabled(!completed.isEmpty());

        revalidate();
        repaint();
    }

    private JComponent createTaskRow(Task task) {
        JPanel row = UITheme.createCardPanel(new BorderLayout(10, 0));
        row.setBackground(UITheme.PANEL_BACKGROUND_2);

        JCheckBox checkbox = new JCheckBox();
        checkbox.setSelected(task.isCompleted());
        checkbox.setOpaque(false);
        checkbox.setForeground(UITheme.TEXT_COLOR);
        checkbox.setFocusPainted(false);
        checkbox.addActionListener(e -> toggleTask(task.getId(), checkbox.isSelected()));

        JLabel label = UITheme.createNormalLabel(task.getTaskDescription());
        if (task.isCompleted()) {
            label.setText("<html><s>" + escapeHtml(task.getTaskDescription()) + "</s></html>");
            label.setForeground(UITheme.TEXT_MUTED);
        }

        JButton btnDelete = createTinyActionButton("Delete", UITheme.DANGER_BUTTON);
        btnDelete.addActionListener(e -> deleteTask(task.getId()));

        row.add(checkbox, BorderLayout.WEST);
        row.add(label, BorderLayout.CENTER);
        row.add(btnDelete, BorderLayout.EAST);
        return row;
    }

    private JButton createTinyActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BUTTON);
        btn.setForeground(color);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void toggleTask(int taskId, boolean completed) {
        try {
            Task task = plannerService.getTaskById(taskId);
            if (task == null) {
                return;
            }
            task.setCompleted(completed);
            plannerService.updateTask(task);
        } catch (Exception ignored) {
        }
        reload();
    }

    private void deleteTask(int taskId) {
        try {
            plannerService.deleteTask(taskId);
        } catch (Exception ignored) {
        }
        reload();
    }

    private void clearCompletedTasks() {
        try {
            List<Task> snapshot = List.copyOf(plannerService.getAllTasks());
            for (Task task : snapshot) {
                if (task.isCompleted()) {
                    plannerService.deleteTask(task.getId());
                }
            }
        } catch (Exception ignored) {
        }
        reload();
    }

    private JLabel emptyLabel(String text) {
        JLabel label = UITheme.createNormalLabel(text);
        label.setForeground(UITheme.TEXT_MUTED);
        return label;
    }

    private JLabel errorLabel(String text) {
        JLabel label = UITheme.createNormalLabel(text);
        label.setForeground(UITheme.DANGER_BUTTON);
        return label;
    }

    private static String escapeHtml(String input) {
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
