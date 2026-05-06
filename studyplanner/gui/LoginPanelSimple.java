package com.studyplanner.gui;

import com.studyplanner.model.User;
import com.studyplanner.service.PlannerService;
import com.studyplanner.service.PlannerServiceImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class LoginPanelSimple extends JPanel {
    private static final String LOGIN_BG_PATH = "C:\\Users\\DELL\\OneDrive\\Desktop\\image.png";

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;
    private JLabel lblSignUp;

    private BufferedImage backgroundImage;
    private final Consumer<User> onLoginSuccess;
    private final Runnable onSignUpRequested;
    private final PlannerService service;

    public LoginPanelSimple(Consumer<User> onLoginSuccess, Runnable onSignUpRequested) {
        this.onLoginSuccess = onLoginSuccess;
        this.onSignUpRequested = onSignUpRequested;
        this.service = new PlannerServiceImpl();
        loadBackgroundImage();
        initializeComponents();
    }
    
    private void loadBackgroundImage() {
        try {
            var stream = LoginPanelSimple.class.getResourceAsStream("/image.png");
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
        
        JPanel card = new JPanel();
        card.setBackground(new Color(20, 20, 30));
        card.setBorder(BorderFactory.createLineBorder(new Color(56, 189, 248), 2));
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(56, 189, 248), 2),
            BorderFactory.createEmptyBorder(22, 26, 22, 26)
        ));
        card.setPreferredSize(new Dimension(440, 390));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 0, 6, 0);
        
        JLabel title = new JLabel("STUDY PLANNER", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(236, 72, 153));
        c.gridy = 0;
        card.add(title, c);
        
        JLabel subtitle = new JLabel("Login to continue", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(148, 163, 184));
        c.gridy = 1;
        c.insets = new Insets(0, 0, 14, 0);
        card.add(subtitle, c);
        
        // Username field
        c.gridy = 2;
        c.insets = new Insets(8, 0, 2, 0);
        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(new Color(226, 232, 240));
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(userLabel, c);
        
        c.gridy = 3;
        c.insets = new Insets(0, 0, 8, 0);
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsername.setBackground(new Color(30, 41, 59));
        txtUsername.setForeground(new Color(226, 232, 240));
        txtUsername.setCaretColor(new Color(226, 232, 240));
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(56, 189, 248), 1));
        txtUsername.setPreferredSize(new Dimension(0, 35));
        card.add(txtUsername, c);
        
        // Password field
        c.gridy = 4;
        c.insets = new Insets(8, 0, 2, 0);
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(new Color(226, 232, 240));
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(passLabel, c);
        
        c.gridy = 5;
        c.insets = new Insets(0, 0, 8, 0);
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setBackground(new Color(30, 41, 59));
        txtPassword.setForeground(new Color(226, 232, 240));
        txtPassword.setCaretColor(new Color(226, 232, 240));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(56, 189, 248), 1));
        txtPassword.setPreferredSize(new Dimension(0, 35));
        card.add(txtPassword, c);
        
        // Error label
        c.gridy = 6;
        c.insets = new Insets(6, 0, 8, 0);
        lblError = new JLabel();
        lblError.setForeground(new Color(239, 68, 68));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        card.add(lblError, c);
        
        // Login button
        c.gridy = 7;
        c.insets = new Insets(12, 0, 0, 0);
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(56, 189, 248));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setContentAreaFilled(true);
        btnLogin.setIcon(null);
        btnLogin.setHorizontalAlignment(SwingConstants.CENTER);
        btnLogin.setVerticalAlignment(SwingConstants.CENTER);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(0, 42));
        btnLogin.addActionListener(e -> handleLogin());
        card.add(btnLogin, c);

        // Sign-up link
        c.gridy = 8;
        c.insets = new Insets(14, 0, 0, 0);
        lblSignUp = new JLabel("<html>Don't have an account? <font color='#38BDF8'><u>Sign up</u></font></html>",
                SwingConstants.CENTER);
        lblSignUp.setForeground(new Color(148, 163, 184));
        lblSignUp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblSignUp.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onSignUpRequested != null) onSignUpRequested.run();
            }
        });
        card.add(lblSignUp, c);

        root.add(card, gbc);

        setLayout(new BorderLayout());
        add(root, BorderLayout.CENTER);

        // Submit on Enter
        getRootPane();
        txtPassword.addActionListener(e -> handleLogin());
        txtUsername.addActionListener(e -> handleLogin());
    }

    /** Clears inputs and error so the panel feels fresh when switched to. */
    public void reset() {
        txtUsername.setText("");
        txtPassword.setText("");
        lblError.setText(" ");
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Username and password are required");
            return;
        }

        try {
            User user = service.authenticateUser(username, password);
            if (user != null) {
                lblError.setText(" ");
                txtPassword.setText("");
                if (onLoginSuccess != null) {
                    onLoginSuccess.accept(user);
                }
            } else {
                lblError.setText("Invalid username or password");
                txtPassword.setText("");
            }
        } catch (Exception ex) {
            lblError.setText("Login failed: " + ex.getMessage());
            txtPassword.setText("");
        }
    }
    
    private static class BackgroundImagePanel extends JPanel {
        private final BufferedImage image;
        
        private BackgroundImagePanel(BufferedImage image) {
            this.image = image;
            setOpaque(true);
            setBackground(new Color(0, 0, 0));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int w = getWidth();
            int h = getHeight();

            if (image != null) {
                g2.drawImage(image, 0, 0, w, h, this);

                // Subtle dark overlay to keep the login card readable (no white wash-out)
                g2.setColor(new Color(15, 14, 46, 150));
                g2.fillRect(0, 0, w, h);
            } else {
                g2.setColor(getBackground());
                g2.fillRect(0, 0, w, h);
            }

            g2.dispose();
        }
    }
}
