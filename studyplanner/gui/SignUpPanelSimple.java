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

public class SignUpPanelSimple extends JPanel {
    private static final String LOGIN_BG_PATH = "C:\\Users\\DELL\\OneDrive\\Desktop\\image.png";

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirm;
    private JButton btnSignUp;
    private JLabel lblError;
    private JLabel lblBack;

    private BufferedImage backgroundImage;
    private final Consumer<User> onSignUpSuccess;
    private final Runnable onBackToLogin;
    private final PlannerService service;

    public SignUpPanelSimple(Consumer<User> onSignUpSuccess, Runnable onBackToLogin) {
        this.onSignUpSuccess = onSignUpSuccess;
        this.onBackToLogin = onBackToLogin;
        this.service = new PlannerServiceImpl();
        loadBackgroundImage();
        initializeComponents();
    }

    private void loadBackgroundImage() {
        try {
            var stream = SignUpPanelSimple.class.getResourceAsStream("/image.png");
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
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(56, 189, 248), 2),
                BorderFactory.createEmptyBorder(22, 26, 22, 26)
        ));
        card.setPreferredSize(new Dimension(440, 470));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 0, 6, 0);

        JLabel title = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(236, 72, 153));
        c.gridy = 0;
        card.add(title, c);

        JLabel subtitle = new JLabel("Sign up to get started", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(148, 163, 184));
        c.gridy = 1;
        c.insets = new Insets(0, 0, 14, 0);
        card.add(subtitle, c);

        // Username
        c.gridy = 2;
        c.insets = new Insets(8, 0, 2, 0);
        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(new Color(226, 232, 240));
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(userLabel, c);

        c.gridy = 3;
        c.insets = new Insets(0, 0, 8, 0);
        txtUsername = styledTextField();
        card.add(txtUsername, c);

        // Password
        c.gridy = 4;
        c.insets = new Insets(8, 0, 2, 0);
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(new Color(226, 232, 240));
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(passLabel, c);

        c.gridy = 5;
        c.insets = new Insets(0, 0, 8, 0);
        txtPassword = styledPasswordField();
        card.add(txtPassword, c);

        // Confirm Password
        c.gridy = 6;
        c.insets = new Insets(8, 0, 2, 0);
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setForeground(new Color(226, 232, 240));
        confirmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(confirmLabel, c);

        c.gridy = 7;
        c.insets = new Insets(0, 0, 8, 0);
        txtConfirm = styledPasswordField();
        card.add(txtConfirm, c);

        // Error label
        c.gridy = 8;
        c.insets = new Insets(6, 0, 8, 0);
        lblError = new JLabel(" ");
        lblError.setForeground(new Color(239, 68, 68));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        card.add(lblError, c);

        // Sign Up button
        c.gridy = 9;
        c.insets = new Insets(12, 0, 0, 0);
        btnSignUp = new JButton("Sign Up");
        btnSignUp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSignUp.setBackground(new Color(56, 189, 248));
        btnSignUp.setForeground(Color.WHITE);
        btnSignUp.setBorderPainted(false);
        btnSignUp.setFocusPainted(false);
        btnSignUp.setContentAreaFilled(true);
        btnSignUp.setHorizontalAlignment(SwingConstants.CENTER);
        btnSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSignUp.setPreferredSize(new Dimension(0, 42));
        btnSignUp.addActionListener(e -> handleSignUp());
        card.add(btnSignUp, c);

        // Back to login link
        c.gridy = 10;
        c.insets = new Insets(14, 0, 0, 0);
        lblBack = new JLabel("<html>Already have an account? <font color='#38BDF8'><u>Log in</u></font></html>",
                SwingConstants.CENTER);
        lblBack.setForeground(new Color(148, 163, 184));
        lblBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBack.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onBackToLogin != null) onBackToLogin.run();
            }
        });
        card.add(lblBack, c);

        root.add(card, gbc);

        setLayout(new BorderLayout());
        add(root, BorderLayout.CENTER);

        // Submit on Enter from any field
        txtUsername.addActionListener(e -> handleSignUp());
        txtPassword.addActionListener(e -> handleSignUp());
        txtConfirm.addActionListener(e -> handleSignUp());
    }

    private JTextField styledTextField() {
        JTextField f = new JTextField(20);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBackground(new Color(30, 41, 59));
        f.setForeground(new Color(226, 232, 240));
        f.setCaretColor(new Color(226, 232, 240));
        f.setBorder(BorderFactory.createLineBorder(new Color(56, 189, 248), 1));
        f.setPreferredSize(new Dimension(0, 35));
        return f;
    }

    private JPasswordField styledPasswordField() {
        JPasswordField f = new JPasswordField(20);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBackground(new Color(30, 41, 59));
        f.setForeground(new Color(226, 232, 240));
        f.setCaretColor(new Color(226, 232, 240));
        f.setBorder(BorderFactory.createLineBorder(new Color(56, 189, 248), 1));
        f.setPreferredSize(new Dimension(0, 35));
        return f;
    }

    /** Clears inputs and error so the panel feels fresh when switched to. */
    public void reset() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirm.setText("");
        lblError.setText(" ");
    }

    private void handleSignUp() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            lblError.setText("All fields are required");
            return;
        }
        if (!password.equals(confirm)) {
            lblError.setText("Passwords do not match");
            txtConfirm.setText("");
            return;
        }

        try {
            User user = service.registerUser(username, password);
            lblError.setText(" ");
            txtPassword.setText("");
            txtConfirm.setText("");
            if (onSignUpSuccess != null) {
                onSignUpSuccess.accept(user);
            }
        } catch (Exception ex) {
            lblError.setText(ex.getMessage());
            txtPassword.setText("");
            txtConfirm.setText("");
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
