package com.studyplanner.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * LOGIN FRAME - First screen users see.
 */
public class LoginFrame extends JFrame implements ActionListener {

    // Requested background image path (absolute)
    private static final String LOGIN_BG_PATH = "C:\\Users\\DELL\\OneDrive\\Desktop\\image.png";

    // Demo credentials (can be replaced with real auth later)
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "1234";

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private StyledButton btnLogin;
    private JLabel lblError;

    private BufferedImage backgroundImage;

    public LoginFrame() {
        UITheme.configureFrame(
                this,
                "Study Planner - Login",
                new Dimension(740, 520),
                new Dimension(700, 480),
                JFrame.EXIT_ON_CLOSE
        );

        loadBackgroundImage();
        initializeComponents();
        setVisible(true);
    }

    private void loadBackgroundImage() {
        // Tries: (1) classpath "/image.png" (recommended), then (2) absolute path requested by you.
        try {
            var stream = LoginFrame.class.getResourceAsStream("/image.png");
            if (stream != null) {
                backgroundImage = ImageIO.read(stream);
                return;
            }
        } catch (Exception ignored) {
        }

        try {
            File file = new File(LOGIN_BG_PATH);
            if (file.exists()) {
                backgroundImage = ImageIO.read(file);
            }
        } catch (Exception ignored) {
        }
    }

    private void initializeComponents() {
        JPanel root = new BackgroundImagePanel(backgroundImage);
        root.setLayout(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel card = new RoundedGlassPanel();
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));
        card.setPreferredSize(new Dimension(440, 340));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 0, 6, 0);

        JLabel title = new JLabel("STUDY PLANNER", SwingConstants.CENTER);
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_COLOR);
        c.gridy = 0;
        card.add(title, c);

        JLabel subtitle = new JLabel("Login to continue", SwingConstants.CENTER);
        subtitle.setFont(UITheme.FONT_NORMAL);
        subtitle.setForeground(UITheme.TEXT_MUTED);
        c.gridy = 1;
        c.insets = new Insets(0, 0, 14, 0);
        card.add(subtitle, c);

        c.insets = new Insets(6, 0, 6, 0);
        c.gridy = 2;
        JLabel userLabel = UITheme.createNormalLabel("Username");
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(userLabel, c);

        c.gridy = 3;
        txtUsername = new JTextField();
        UITheme.styleTextField(txtUsername);
        txtUsername.setPreferredSize(new Dimension(320, 42));
        card.add(wrapCentered(txtUsername, 340), c);

        c.gridy = 4;
        JLabel passLabel = UITheme.createNormalLabel("Password");
        passLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(passLabel, c);

        c.gridy = 5;
        txtPassword = new JPasswordField();
        UITheme.styleTextComponent(txtPassword);
        txtPassword.setPreferredSize(new Dimension(320, 42));
        card.add(wrapCentered(txtPassword, 340), c);

        c.gridy = 6;
        c.insets = new Insets(10, 0, 6, 0);
        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setFont(UITheme.FONT_NORMAL);
        lblError.setForeground(UITheme.DANGER_BUTTON);
        card.add(lblError, c);

        c.gridy = 7;
        c.insets = new Insets(6, 0, 6, 0);
        btnLogin = UITheme.primaryButton("Login");
        btnLogin.setPreferredSize(new Dimension(240, 48));
        btnLogin.addActionListener(this);
        card.add(wrapCentered(btnLogin, 260), c);

        c.gridy = 8;
        c.insets = new Insets(8, 0, 0, 0);
        JLabel demo = new JLabel("Demo: admin / 1234", SwingConstants.CENTER);
        demo.setFont(UITheme.FONT_NORMAL);
        demo.setForeground(UITheme.TEXT_MUTED);
        card.add(demo, c);

        root.add(card, gbc);
        add(root);
    }

    private static JComponent wrapCentered(JComponent component, int maxWidth) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);
        component.setMaximumSize(new Dimension(maxWidth, component.getPreferredSize().height));
        panel.add(component);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            handleLogin();
        }
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            lblError.setText(" ");
            new DashboardFrame();
            dispose();
            return;
        }

        lblError.setText("Invalid credentials.");
        txtPassword.setText("");
        txtUsername.requestFocus();
    }

    private static final class BackgroundImagePanel extends JPanel {
        private final BufferedImage image;

        private BackgroundImagePanel(BufferedImage image) {
            this.image = image;
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            if (image != null) {
                int w = getWidth();
                int h = getHeight();
                Image scaled = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                g2.drawImage(scaled, 0, 0, null);
                g2.setColor(new Color(UITheme.PRIMARY_BACKGROUND.getRed(), UITheme.PRIMARY_BACKGROUND.getGreen(), UITheme.PRIMARY_BACKGROUND.getBlue(), 170));
                g2.fillRect(0, 0, w, h);
            } else {
                g2.setColor(UITheme.PRIMARY_BACKGROUND);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            g2.dispose();
        }
    }

    private static final class RoundedGlassPanel extends JPanel {
        private static final int ARC = 20;

        private RoundedGlassPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(new Color(UITheme.PANEL_BACKGROUND.getRed(), UITheme.PANEL_BACKGROUND.getGreen(), UITheme.PANEL_BACKGROUND.getBlue(), 210));
            g2.fillRoundRect(0, 0, w - 1, h - 1, ARC, ARC);

            g2.setColor(new Color(UITheme.FIELD_BORDER.getRed(), UITheme.FIELD_BORDER.getGreen(), UITheme.FIELD_BORDER.getBlue(), 180));
            g2.drawRoundRect(0, 0, w - 1, h - 1, ARC, ARC);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
