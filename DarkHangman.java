import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.Box;
import javax.swing.Timer;

public class DarkHangman {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            StartScreen startScreen = new StartScreen();
            startScreen.setVisible(true);
        });
    }
}

class StartScreen extends JFrame {
    private BufferedImage backgroundImage;
    private JButton playButton, loadGameButton, highScoreButton, instructionsButton;
    private JPanel mainPanel;
    private static final Color GLOW_COLOR = new Color(255, 30, 30);
    private static final Color BUTTON_COLOR = new Color(20, 20, 20);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    
    public StartScreen() {
        setTitle("Dark Hangman");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        initializeBackground();
        initializeUI();
    }
    
    private void initializeBackground() {
        try {
            // Try to load background image from resources
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/resources/background.jpg"));
        } catch (Exception e) {
            System.out.println("Using fallback background");
            // Create a dark gradient background as fallback
            backgroundImage = createGradientBackground();
        }
    }
    
    private BufferedImage createGradientBackground() {
        BufferedImage image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(20, 20, 25),
            0, 700, new Color(40, 40, 45)
        );
        
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 1000, 700);
        g2d.dispose();
        
        return image;
    }
    
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setOpaque(false);
        
        // Title Panel
        JPanel titlePanel = createTitlePanel();
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Center Panel for main buttons
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Corner Panels
        JPanel cornerPanel = createCornerPanel();
        mainPanel.add(cornerPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("DARK HANGMAN", JLabel.CENTER);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 72));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        playButton = createGlowingButton("PLAY");
        loadGameButton = createGlowingButton("LOAD GAME");
        JButton loginButton = createGlowingButton("LOGIN");
        JButton signupButton = createGlowingButton("SIGN UP");
        JButton exitButton = createGlowingButton("EXIT");
        
        // Make buttons longer
        playButton.setPreferredSize(new Dimension(300, 60));
        loadGameButton.setPreferredSize(new Dimension(300, 60));
        loginButton.setPreferredSize(new Dimension(300, 60));
        signupButton.setPreferredSize(new Dimension(300, 60));
        exitButton.setPreferredSize(new Dimension(300, 60));
        
        playButton.addActionListener(e -> startNewGame());
        loadGameButton.addActionListener(e -> loadGame());
        loginButton.addActionListener(e -> showLoginDialog());
        signupButton.addActionListener(e -> showSignupDialog());
        exitButton.addActionListener(e -> System.exit(0));
        
        centerPanel.add(playButton, gbc);
        centerPanel.add(loadGameButton, gbc);
        centerPanel.add(loginButton, gbc);
        centerPanel.add(signupButton, gbc);
        centerPanel.add(exitButton, gbc);
        
        return centerPanel;
    }
    
    private JPanel createCornerPanel() {
        JPanel cornerPanel = new JPanel(new BorderLayout());
        cornerPanel.setOpaque(false);
        cornerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        highScoreButton = createGlowingButton("HIGH SCORES");
        instructionsButton = createGlowingButton("INSTRUCTIONS");
        
        // Make corner buttons more visible
        highScoreButton.setPreferredSize(new Dimension(200, 50));
        instructionsButton.setPreferredSize(new Dimension(200, 50));
        
        // Make text bolder
        highScoreButton.setFont(new Font("Arial", Font.BOLD, 20));
        instructionsButton.setFont(new Font("Arial", Font.BOLD, 20));
        
        highScoreButton.addActionListener(e -> showHighScores());
        instructionsButton.addActionListener(e -> showInstructions());
        
        // Use panels with FlowLayout for better visibility
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(instructionsButton);
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(highScoreButton);
        
        cornerPanel.add(leftPanel, BorderLayout.WEST);
        cornerPanel.add(rightPanel, BorderLayout.EAST);
        
        return cornerPanel;
    }
    
    private JButton createGlowingButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Enhanced glow effect
                if (getModel().isRollover()) {
                    // Outer glow
                    g2d.setColor(new Color(255, 0, 0, 50));
                    g2d.fillRoundRect(-5, -5, getWidth()+10, getHeight()+10, 25, 25);
                    // Inner glow
                    g2d.setColor(new Color(255, 0, 0, 100));
                    g2d.fillRoundRect(-2, -2, getWidth()+4, getHeight()+4, 25, 25);
                }
                
                // Button background
                g2d.setColor(new Color(20, 20, 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Button border
                g2d.setColor(new Color(255, 0, 0, getModel().isRollover() ? 255 : 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 25, 25);
                
                // Text
                g2d.setColor(new Color(255, 0, 0));
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                // Text glow
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 50, 50, 100));
                    g2d.drawString(getText(), x+1, y+1);
                }
                
                g2d.setColor(new Color(255, 0, 0));
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void startNewGame() {
        // Show loading screen
        JDialog loadingDialog = new JDialog(this, "Loading", false);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setUndecorated(true); // Remove window decorations
        
        JPanel loadingPanel = new JPanel(new BorderLayout(10, 10));
        loadingPanel.setBackground(new Color(20, 20, 20));
        loadingPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 2));
        
        JLabel loadingLabel = new JLabel("Loading game...", JLabel.CENTER);
        loadingLabel.setForeground(new Color(255, 0, 0));
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(new Color(255, 0, 0));
        progressBar.setBackground(new Color(40, 40, 40));
        progressBar.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 1));
        
        loadingPanel.add(loadingLabel, BorderLayout.NORTH);
        loadingPanel.add(progressBar, BorderLayout.CENTER);
        loadingPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        loadingDialog.add(loadingPanel);
        
        // Create and start loading animation
        Timer timer = new Timer(30, null);
        timer.addActionListener(e -> {
            int value = progressBar.getValue();
            if (value < 100) {
                progressBar.setValue(value + 2);
            } else {
                timer.stop();
                loadingDialog.dispose();
                GameFrame gameFrame = new GameFrame();
                gameFrame.setVisible(true);
                dispose();
            }
        });
        
        // Show loading dialog and start timer
        loadingDialog.setVisible(true);
        timer.start();
    }
    
    private void loadGame() {
        // Check if user is logged in
        if (!UserManager.isLoggedIn()) {
            int choice = JOptionPane.showConfirmDialog(this,
                "You need to log in to load a game.\nWould you like to log in now?",
                "Login Required",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
            if (choice == JOptionPane.YES_OPTION) {
                showLoginDialog();
            }
            return;
        }
        
        // Load saved game logic here
        // For now, just start a new game
        startNewGame();
    }
    
    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(400, 300);
        loginDialog.setLocationRelativeTo(this);
        loginDialog.getContentPane().setBackground(new Color(20, 20, 20));
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        
        JTextField username = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        JButton loginButton = createGlowingButton("Login");
        
        // Style text fields
        styleTextField(username);
        styleTextField(password);
        
        // Style labels
        JLabel userLabel = createStyledLabel("Username:");
        JLabel passLabel = createStyledLabel("Password:");
        
        panel.add(userLabel, gbc);
        panel.add(username, gbc);
        panel.add(passLabel, gbc);
        panel.add(password, gbc);
        panel.add(loginButton, gbc);
        
        loginButton.addActionListener(e -> {
            if (username.getText().length() > 0 && password.getPassword().length > 0) {
                UserManager.login(username.getText());
                JOptionPane.showMessageDialog(loginDialog,
                    "Login successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loginDialog.dispose();
            }
        });
        
        loginDialog.add(panel);
        loginDialog.setVisible(true);
    }
    
    private void showSignupDialog() {
        JDialog signupDialog = new JDialog(this, "Sign Up", true);
        signupDialog.setSize(400, 350);
        signupDialog.setLocationRelativeTo(this);
        signupDialog.getContentPane().setBackground(new Color(20, 20, 20));
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        
        JTextField username = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        JPasswordField confirmPassword = new JPasswordField(20);
        JButton signupButton = createGlowingButton("Sign Up");
        
        // Style text fields
        styleTextField(username);
        styleTextField(password);
        styleTextField(confirmPassword);
        
        // Style labels
        JLabel userLabel = createStyledLabel("Username:");
        JLabel passLabel = createStyledLabel("Password:");
        JLabel confirmLabel = createStyledLabel("Confirm Password:");
        
        panel.add(userLabel, gbc);
        panel.add(username, gbc);
        panel.add(passLabel, gbc);
        panel.add(password, gbc);
        panel.add(confirmLabel, gbc);
        panel.add(confirmPassword, gbc);
        panel.add(signupButton, gbc);
        
        signupButton.addActionListener(e -> {
            if (username.getText().length() > 0 && 
                password.getPassword().length > 0 &&
                Arrays.equals(password.getPassword(), confirmPassword.getPassword())) {
                UserManager.login(username.getText());
                JOptionPane.showMessageDialog(signupDialog,
                    "Sign up successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                signupDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(signupDialog,
                    "Passwords don't match or fields are empty!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        signupDialog.add(panel);
        signupDialog.setVisible(true);
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(255, 0, 0));
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }
    
    private void styleTextField(JTextField textField) {
        textField.setBackground(new Color(30, 30, 30));
        textField.setForeground(new Color(255, 0, 0));
        textField.setCaretColor(new Color(255, 0, 0));
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 0, 0), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void showHighScores() {
        // Create high scores dialog
        JDialog highScoresDialog = new JDialog(this, "High Scores", true);
        highScoresDialog.setSize(400, 500);
        highScoresDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BUTTON_COLOR);
        
        // Sample high scores (in real implementation, these would be loaded from a file/database)
        String[] scores = {
            "1. Player One - 1000",
            "2. Player Two - 850",
            "3. Player Three - 700",
            "4. Player Four - 550",
            "5. Player Five - 400"
        };
        
        JList<String> scoreList = new JList<>(scores);
        scoreList.setBackground(BUTTON_COLOR);
        scoreList.setForeground(TEXT_COLOR);
        scoreList.setFont(new Font("Arial", Font.BOLD, 16));
        
        panel.add(new JScrollPane(scoreList), BorderLayout.CENTER);
        
        JButton closeButton = createGlowingButton("Close");
        closeButton.addActionListener(e -> highScoresDialog.dispose());
        panel.add(closeButton, BorderLayout.SOUTH);
        
        highScoresDialog.add(panel);
        highScoresDialog.setVisible(true);
    }
    
    private void showInstructions() {
        JDialog instructionsDialog = new JDialog(this, "Instructions", true);
        instructionsDialog.setSize(600, 500);
        instructionsDialog.setLocationRelativeTo(this);
        instructionsDialog.getContentPane().setBackground(new Color(10, 10, 10));
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(10, 10, 10));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 0, 0), 2),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        String instructions = 
            "<html><body style='width: 500px; background-color: #0A0A0A; color: #FF0000; font-family: Arial;'>" +
            "<div style='text-align: center;'>" +
            "<h1 style='color: #FF0000; font-size: 32px; margin-bottom: 30px;'>How to Play Dark Hangman</h1>" +
            "</div>" +
            "<div style='font-size: 18px; line-height: 1.6;'>" +
            "<p style='margin: 15px 0;'><span style='color: #FF4444;'>1.</span> The game will choose a random word</p>" +
            "<p style='margin: 15px 0;'><span style='color: #FF4444;'>2.</span> Try to guess the word by selecting letters</p>" +
            "<p style='margin: 15px 0;'><span style='color: #FF4444;'>3.</span> You have 6 lives - each wrong guess loses a life</p>" +
            "<p style='margin: 15px 0;'><span style='color: #FF4444;'>4.</span> Correct guesses reveal the letter in the word</p>" +
            "<p style='margin: 15px 0;'><span style='color: #FF4444;'>5.</span> Win by guessing the complete word before losing all lives</p>" +
            "<div style='margin-top: 30px; border-top: 2px solid #FF0000; padding-top: 20px;'>" +
            "<h2 style='color: #FF4444; font-size: 24px; margin-bottom: 15px;'>Special Features:</h2>" +
            "<p style='margin: 10px 0;'>• Save your progress (requires login)</p>" +
            "<p style='margin: 10px 0;'>• Track your high scores</p>" +
            "<p style='margin: 10px 0;'>• Beautiful dark theme interface</p>" +
            "</div>" +
            "</div>" +
            "</body></html>";
        
        JLabel instructionsLabel = new JLabel(instructions);
        instructionsLabel.setForeground(new Color(255, 0, 0));
        
        // Create a custom scroll pane with red scrollbar
        JScrollPane scrollPane = new JScrollPane(instructionsLabel);
        scrollPane.setBackground(new Color(10, 10, 10));
        scrollPane.getViewport().setBackground(new Color(10, 10, 10));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Customize the scrollbar
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(255, 0, 0);
                this.trackColor = new Color(30, 30, 30);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Loading bar panel at the bottom
        JPanel loadingPanel = new JPanel(new BorderLayout(10, 10));
        loadingPanel.setBackground(new Color(10, 10, 10));
        loadingPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JProgressBar loadingBar = new JProgressBar(0, 100);
        loadingBar.setPreferredSize(new Dimension(0, 8));
        loadingBar.setForeground(new Color(255, 0, 0));
        loadingBar.setBackground(new Color(30, 30, 30));
        loadingBar.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0, 100), 1));
        loadingBar.setValue(0);
        
        loadingPanel.add(loadingBar, BorderLayout.CENTER);
        mainPanel.add(loadingPanel, BorderLayout.SOUTH);
        
        // Close button with enhanced styling
        JButton closeButton = createGlowingButton("Close");
        closeButton.setPreferredSize(new Dimension(200, 50));
        closeButton.addActionListener(e -> {
            Timer timer = new Timer(20, event -> {
                int value = loadingBar.getValue();
                if (value < 100) {
                    loadingBar.setValue(value + 4);
                } else {
                    ((Timer)event.getSource()).stop();
                    instructionsDialog.dispose();
                }
            });
            timer.start();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(10, 10, 10));
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        instructionsDialog.add(mainPanel);
        
        // Start loading animation when dialog opens
        Timer initialLoadTimer = new Timer(20, null);
        initialLoadTimer.addActionListener(e -> {
            int value = loadingBar.getValue();
            if (value < 100) {
                loadingBar.setValue(value + 4);
            } else {
                initialLoadTimer.stop();
            }
        });
        
        instructionsDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                initialLoadTimer.start();
            }
        });
        
        instructionsDialog.setVisible(true);
    }
}

class GameFrame extends JFrame {
    private static final Color BACKGROUND_COLOR = new Color(20, 20, 25);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color CORRECT_COLOR = new Color(0, 255, 0, 100);
    private static final Color WRONG_COLOR = new Color(255, 0, 0, 100);
    
    private String word;
    private char[] guessedWord;
    private Set<Character> guessedLetters;
    private int lives;
    private JLabel wordLabel;
    private JLabel livesLabel;
    private JPanel hangmanPanel;
    private JPanel keyboardPanel;
    private List<JButton> letterButtons;
    
    public GameFrame() {
        setTitle("Dark Hangman - Game");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        initializeGame();
        initializeUI();
    }
    
    private void initializeGame() {
        // Initialize game state
        word = getRandomWord().toUpperCase();
        guessedWord = new char[word.length()];
        Arrays.fill(guessedWord, '_');
        guessedLetters = new HashSet<>();
        lives = 6;
        letterButtons = new ArrayList<>();
    }
    
    private String getRandomWord() {
        String[] words = {
            "HANGMAN", "JAVA", "PROGRAMMING", "COMPUTER", "ALGORITHM",
            "DATABASE", "NETWORK", "SOFTWARE", "DEVELOPER", "INTERFACE",
            "KEYBOARD", "MONITOR", "SYSTEM", "VARIABLE", "FUNCTION"
        };
        return words[new Random().nextInt(words.length)];
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Word display with enhanced visibility
        wordLabel = new JLabel(String.valueOf(guessedWord), JLabel.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 60));
        wordLabel.setForeground(new Color(255, 0, 0));
        wordLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(wordLabel, BorderLayout.NORTH);
        
        // Lives display with boxes instead of hearts
        StringBuilder livesText = new StringBuilder("Lives: ");
        for (int i = 0; i < lives; i++) {
            livesText.append("□ ");
        }
        livesLabel = new JLabel(livesText.toString(), JLabel.CENTER);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 30));
        livesLabel.setForeground(new Color(255, 0, 0));
        
        // Hangman drawing panel with enhanced visibility
        hangmanPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawHangman(g);
            }
        };
        hangmanPanel.setOpaque(false);
        
        // Center panel containing hangman and lives
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.add(hangmanPanel, BorderLayout.CENTER);
        centerPanel.add(livesLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // Enhanced keyboard
        initializeKeyboard();
    }
    
    private void initializeKeyboard() {
        keyboardPanel = new JPanel(new GridLayout(3, 9, 10, 10));
        keyboardPanel.setOpaque(false);
        keyboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        String[] letters = {
            "Q", "W", "E", "R", "T", "Y", "U", "I", "O",
            "P", "A", "S", "D", "F", "G", "H", "J", "K",
            "L", "Z", "X", "C", "V", "B", "N", "M"
        };
        
        for (String letter : letters) {
            JButton button = createLetterButton(letter);
            letterButtons.add(button);
            keyboardPanel.add(button);
        }
        
        JPanel keyboardContainer = new JPanel(new BorderLayout());
        keyboardContainer.setOpaque(false);
        keyboardContainer.add(keyboardPanel, BorderLayout.CENTER);
        
        add(keyboardContainer, BorderLayout.SOUTH);
    }
    
    private JButton createLetterButton(String letter) {
        JButton button = new JButton(letter) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Enhanced glow effect
                if (getModel().isRollover() && isEnabled()) {
                    // Outer glow
                    g2d.setColor(new Color(255, 0, 0, 50));
                    g2d.fillRoundRect(-5, -5, getWidth()+10, getHeight()+10, 15, 15);
                    // Inner glow
                    g2d.setColor(new Color(255, 0, 0, 100));
                    g2d.fillRoundRect(-2, -2, getWidth()+4, getHeight()+4, 15, 15);
                }
                
                // Button background
                if (!isEnabled()) {
                    if (word.contains(getText())) {
                        g2d.setColor(new Color(0, 100, 0));
                    } else {
                        g2d.setColor(new Color(100, 0, 0));
                    }
                } else {
                    g2d.setColor(new Color(20, 20, 20));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Button border
                g2d.setColor(new Color(255, 0, 0, isEnabled() ? (getModel().isRollover() ? 255 : 150) : 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                
                // Text with glow
                if (isEnabled() && getModel().isRollover()) {
                    g2d.setColor(new Color(255, 50, 50, 100));
                    g2d.drawString(getText(), getWidth()/2-5, getHeight()/2+5);
                }
                
                g2d.setColor(isEnabled() ? new Color(255, 0, 0) : new Color(200, 200, 200));
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        
        button.setPreferredSize(new Dimension(60, 60));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> handleGuess(letter.charAt(0)));
        
        return button;
    }
    
    private void handleGuess(char letter) {
        guessedLetters.add(letter);
        boolean correct = false;
        
        // Update guessed word
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                guessedWord[i] = letter;
                correct = true;
            }
        }
        
        // Update UI
        wordLabel.setText(String.valueOf(guessedWord));
        
        // Disable button
        for (JButton button : letterButtons) {
            if (button.getText().charAt(0) == letter) {
                button.setEnabled(false);
                break;
            }
        }
        
        if (!correct) {
            lives--;
            updateLives();
            hangmanPanel.repaint();
            
            if (lives <= 0) {
                gameOver(false);
            }
        } else if (String.valueOf(guessedWord).equals(word)) {
            gameOver(true);
        }
    }
    
    private void updateLives() {
        StringBuilder boxes = new StringBuilder("Lives: ");
        for (int i = 0; i < lives; i++) {
            boxes.append("□ ");
        }
        livesLabel.setText(boxes.toString());
    }
    
    private void drawHangman(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(3));
        
        int centerX = getWidth() / 2;
        int bottomY = getHeight() - 50;
        
        // Enhanced gallows with glow effect
        drawGlowingLine(g2d, centerX - 100, bottomY, centerX + 100, bottomY); // Base
        
        if (6 - lives >= 1) {
            drawGlowingLine(g2d, centerX, bottomY, centerX, 50); // Vertical pole
        }
        if (6 - lives >= 2) {
            drawGlowingLine(g2d, centerX, 50, centerX + 100, 50); // Horizontal beam
        }
        if (6 - lives >= 3) {
            drawGlowingLine(g2d, centerX + 100, 50, centerX + 100, 100); // Rope
        }
        if (6 - lives >= 4) {
            // Head with glow
            drawGlowingCircle(g2d, centerX + 75, 100, 50); // Head
        }
        if (6 - lives >= 5) {
            // Body and arms with glow
            drawGlowingLine(g2d, centerX + 100, 150, centerX + 100, 250); // Body
            drawGlowingLine(g2d, centerX + 100, 180, centerX + 50, 200); // Left arm
            drawGlowingLine(g2d, centerX + 100, 180, centerX + 150, 200); // Right arm
        }
        if (6 - lives >= 6) {
            // Legs with glow
            drawGlowingLine(g2d, centerX + 100, 250, centerX + 50, 300); // Left leg
            drawGlowingLine(g2d, centerX + 100, 250, centerX + 150, 300); // Right leg
        }
    }
    
    private void drawGlowingLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        // Outer glow
        g2d.setColor(new Color(255, 0, 0, 30));
        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(x1, y1, x2, y2);
        
        // Inner glow
        g2d.setColor(new Color(255, 0, 0, 60));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(x1, y1, x2, y2);
        
        // Main line
        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(x1, y1, x2, y2);
    }
    
    private void drawGlowingCircle(Graphics2D g2d, int x, int y, int size) {
        // Outer glow
        g2d.setColor(new Color(255, 0, 0, 30));
        g2d.setStroke(new BasicStroke(8));
        g2d.drawOval(x, y, size, size);
        
        // Inner glow
        g2d.setColor(new Color(255, 0, 0, 60));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawOval(x, y, size, size);
        
        // Main circle
        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(x, y, size, size);
    }
    
    private void gameOver(boolean won) {
        for (JButton button : letterButtons) {
            button.setEnabled(false);
        }
        
        String message = won ? "Congratulations! You won!" : "Game Over! The word was: " + word;
        String title = won ? "Victory!" : "Defeat";
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            message + "\nWould you like to play again?",
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new GameFrame().setVisible(true);
        } else {
            dispose();
            new StartScreen().setVisible(true);
        }
    }
}

class UserManager {
    private static String currentUser = null;
    
    public static void login(String username) {
        currentUser = username;
    }
    
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public static String getCurrentUser() {
        return currentUser;
    }
    
    public static void logout() {
        currentUser = null;
    }
} 