package com.studyplanner.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class DashboardHomePanel extends JPanel {
    private static final String BG_IMAGE_PATH = "C:\\Users\\DELL\\OneDrive\\Desktop\\image.png";
    private static final Color NEON_BLUE = new Color(0, 195, 255);
    private static final Color PINK = new Color(236, 72, 153);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);
    private static final Color TEXT_LIGHT = new Color(226, 232, 240);
    
    private BufferedImage backgroundImage;
    
    public DashboardHomePanel() {
        loadBackgroundImage();
        initComponents();
    }
    
    private void loadBackgroundImage() {
        try {
            var stream = DashboardHomePanel.class.getResourceAsStream("/image.png");
            if (stream != null) {
                backgroundImage = ImageIO.read(stream);
                return;
            }
        } catch (Exception ignored) {
        }
        
        try {
            File file = new File(BG_IMAGE_PATH);
            if (file.exists()) {
                backgroundImage = ImageIO.read(file);
            }
        } catch (Exception ignored) {
        }
    }
    
    private void initComponents() {
        JPanel root = new BackgroundImagePanel(backgroundImage);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Welcome to Study Planner");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(PINK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        root.add(titleLabel);
        
        root.add(Box.createVerticalStrut(10));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Organize your studies efficiently");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        root.add(subtitleLabel);
        
        root.add(Box.createVerticalStrut(50));
        
        // Cards container
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new GridLayout(2, 2, 25, 25));
        cardsContainer.setOpaque(false);
        cardsContainer.setMaximumSize(new Dimension(700, 350));
        cardsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cardsContainer.add(createGlassCard("Subjects", "Manage study subjects and credits"));
        cardsContainer.add(createGlassCard("Tasks", "Track your to-do list"));
        cardsContainer.add(createGlassCard("Study Session", "Log study hours with timer"));
        cardsContainer.add(createGlassCard("Summary", "View statistics and progress"));
        
        root.add(cardsContainer);
        root.add(Box.createVerticalGlue());
        
        setLayout(new BorderLayout());
        add(root, BorderLayout.CENTER);
    }
    
    private JPanel createGlassCard(String title, String description) {
        GlassCard card = new GlassCard(title, description);
        return card;
    }
    
    private static class GlassCard extends JPanel {
        private boolean isHovering = false;
        private final String title;
        private final String description;
        
        public GlassCard(String title, String description) {
            this.title = title;
            this.description = description;
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(new Color(236, 72, 153));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(titleLabel);
            
            add(Box.createVerticalStrut(10));
            
            JLabel descLabel = new JLabel(description);
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            descLabel.setForeground(new Color(148, 163, 184));
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(descLabel);
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovering = true;
                    repaint();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovering = false;
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            // Draw glass background
            Color bgColor = new Color(20, 30, 60, 180);
            g2d.setColor(bgColor);
            g2d.fillRoundRect(0, 0, width, height, 20, 20);
            
            // Draw border with glow effect on hover
            if (isHovering) {
                // Glow effect
                for (int i = 3; i > 0; i--) {
                    g2d.setColor(new Color(0, 195, 255, (int) (50 / i)));
                    g2d.setStroke(new BasicStroke(i));
                    g2d.drawRoundRect(0, 0, width - 1, height - 1, 20, 20);
                }
            }
            
            // Main border
            g2d.setColor(new Color(0, 195, 255, isHovering ? 255 : 200));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(0, 0, width - 1, height - 1, 20, 20);
            
            g2d.dispose();
            
            // Paint child components (labels)
            super.paintComponent(g);
        }
    }
    
    private static class BackgroundImagePanel extends JPanel {
        private final BufferedImage image;
        
        private BackgroundImagePanel(BufferedImage image) {
            this.image = image;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (image != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                
                // Dark semi-transparent overlay for readability
                g2d.setColor(new Color(15, 14, 46, 200));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.dispose();
            } else {
                // Fallback color if image not loaded
                g.setColor(new Color(15, 14, 46));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
