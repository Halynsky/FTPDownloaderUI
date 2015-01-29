package GUI;
import App.FTPFileDownloader;
import App.LocalFolderChecker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainGUI extends JFrame implements ActionListener {

    static private JPanel mainPanel, loadingPanel;
    static private JLabel loginLabel, passwordLabel, ftpHostLabel, remotePathLabel, localPathLabel, loadingMassageLabel, imageLabel;
    static private JTextField loginField, passwordField, ftpHostField, remotePathField, localPathField;
    static private JButton downloadButton, browseButton, cancelButton;
    static private JFileChooser chooser;
    Image loadingIcon = new ImageIcon(this.getClass().getResource("/img/loadingAnimation.gif")).getImage();

    public static JPanel getLoadingPanel() {
        return loadingPanel;
    }

    public static void setLoadingPanel(JPanel loadingPanel) {
        MainGUI.loadingPanel = loadingPanel;
    }

    public static JPanel getMainPanel() {
        return mainPanel;
    }

    public static void setMainPanel(JPanel mainPanel) {
        MainGUI.mainPanel = mainPanel;
    }

    public MainGUI() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("FTPDownloader");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // mainPanel and its elements

        mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, 700, 400);
        mainPanel.setLayout(null);
        mainPanel.setVisible(true);

        loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        loginLabel.setSize(200, 35);
        loginLabel.setLocation(20, 10);

        loginField = new JTextField(30);
        loginField.setFont(new Font("Verdana", Font.BOLD, 14));
        loginField.setSize(240, 35);
        loginField.setLocation(230, 10);
        loginField.setText("anonymous");

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        passwordLabel.setSize(200, 35);
        passwordLabel.setLocation(20, 55);

        passwordField = new JTextField(10);
        passwordField.setFont(new Font("Verdana", Font.BOLD, 14));
        passwordField.setSize(240, 35);
        passwordField.setLocation(230, 55);
        passwordField.setText("anonymous@com");

        ftpHostLabel = new JLabel("FTP");
        ftpHostLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        ftpHostLabel.setSize(200, 35);
        ftpHostLabel.setLocation(20, 100);

        ftpHostField = new JTextField(10);
        ftpHostField.setFont(new Font("Verdana", Font.BOLD, 14));
        ftpHostField.setSize(240, 35);
        ftpHostField.setLocation(230, 100);
//        ftpHostField.setText("ftp.mozilla.org"); // Test dates

        remotePathLabel = new JLabel("Remote Path");
        remotePathLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        remotePathLabel.setSize(200, 35);
        remotePathLabel.setLocation(20, 145);

        remotePathField = new JTextField(10);
        remotePathField.setFont(new Font("Verdana", Font.BOLD, 14));
        remotePathField.setSize(240, 35);
        remotePathField.setLocation(230, 145);
//        remotePathField.setText("/");
//        remotePathField.setText("/pub/mozilla.org/extensions/"); // Test dates

        localPathLabel = new JLabel("Local Folder");
        localPathLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        localPathLabel.setSize(200, 35);
        localPathLabel.setLocation(20, 190);

        localPathField = new JTextField(10);
        localPathField.setFont(new Font("Verdana", Font.BOLD, 14));
        localPathField.setSize(240, 35);
        localPathField.setLocation(230, 190);
//        localPathField.setText("D:\\Test"); // Test dates

        downloadButton = new JButton("DOWNLOAD");
        downloadButton.setFont(new Font("Verdana", Font.BOLD, 16));
        downloadButton.setSize(200, 40);
        downloadButton.setLocation(250, 290);
        downloadButton.setHorizontalTextPosition(SwingConstants.LEFT);

        browseButton = new JButton("Browse");
        browseButton.setFont(new Font("Verdana", Font.BOLD, 12));
        browseButton.setSize(100, 25);
        browseButton.setLocation(510, 195);
        browseButton.setHorizontalTextPosition(SwingConstants.LEFT);

        chooser = new JFileChooser();
        chooser.setVisible(true);
        chooser.setCurrentDirectory(new File("/."));
        chooser.setDialogTitle("Choose Local Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        mainPanel.add(loginLabel);
        mainPanel.add(passwordLabel);
        mainPanel.add(ftpHostLabel);
        mainPanel.add(remotePathLabel);
        mainPanel.add(localPathLabel);

        mainPanel.add(loginField);
        mainPanel.add(passwordField);
        mainPanel.add(ftpHostField);
        mainPanel.add(remotePathField);
        mainPanel.add(localPathField);

        mainPanel.add(downloadButton);
        mainPanel.add(browseButton);

        // loadingPanel and its elements

        loadingPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (loadingIcon != null) {
                    g.drawImage(loadingIcon, 150, 200, this);
                }
            }
        };

        loadingPanel.setBounds(0, 0, 700, 400);
        loadingPanel.setLayout(null);
        loadingPanel.setVisible(false);

        loadingMassageLabel = new JLabel("DOWNLOADING...");
        loadingMassageLabel.setFont(new Font("Verdana", Font.BOLD, 40));
        loadingMassageLabel.setSize(700, 35);
        loadingMassageLabel.setLocation(0, 70);
        loadingMassageLabel.setHorizontalAlignment(JTextField.CENTER);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Verdana", Font.BOLD, 20));
        cancelButton.setSize(150, 50);
        cancelButton.setLocation(280, 270);
        cancelButton.setHorizontalTextPosition(SwingConstants.LEFT);

        loadingPanel.add(loadingMassageLabel);
        loadingPanel.add(cancelButton);

        // Main JFrame

        add(mainPanel);
        add(loadingPanel);

        downloadButton.addActionListener(this);
        browseButton.addActionListener(this);
        cancelButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == browseButton) {
            int ret = chooser.showDialog(null, "choose");
            if (ret == JFileChooser.APPROVE_OPTION) {
                System.out.println("Local folder selected => " + chooser.getSelectedFile().getAbsoluteFile());
                File selectedFolder = chooser.getSelectedFile();
                localPathField.setText(selectedFolder.getAbsolutePath());
            }
        }



        if (event.getSource() == downloadButton) {

            final String host = ftpHostField.getText();
            final String user = loginField.getText();
            final String password = passwordField.getText();
            final String remoteFilePath = remotePathField.getText();
            final String localFilePath = localPathField.getText();

            LocalFolderChecker localFolderChecker = new LocalFolderChecker();
            localFolderChecker.checkAndCreateFolder(localFilePath);

            Thread bufferThread = new Thread() {
                @Override
                public void run() {
                    super.run();

                    try {
                        FTPFileDownloader ftpFileDownloader = new FTPFileDownloader(host, user, password, remoteFilePath, localFilePath);

                        Thread initTread = new Thread(ftpFileDownloader);

                        initTread.start();

                        if (initTread.isAlive()) {

                            Thread setVisibleLoading = new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    mainPanel.setVisible(false);
                                    loadingPanel.setVisible(true);
                                }
                            };

                            setVisibleLoading.start();
                            JOptionPane.showMessageDialog(null, "Downloading started!", "STARTED!", JOptionPane.INFORMATION_MESSAGE);

                        }

                        initTread.join();

                        System.out.println("DOWNLOADING FINISHED!");
                        JOptionPane.showMessageDialog(null, "Downloading finished!", "FINISHED!", JOptionPane.INFORMATION_MESSAGE);
                        mainPanel.setVisible(true);
                        loadingPanel.setVisible(false);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "ERROR!", "ERROR!", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            };

            bufferThread.start();
        }

        if (event.getSource() == cancelButton){

            System.exit(0);
        }

    }

    public void showErrorMassage(final String text) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, text, "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        });

    }
}

