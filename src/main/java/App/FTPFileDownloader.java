package App;
import GUI.MainGUI;

import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPFileDownloader implements Runnable {

    private static MainGUI mainGUI = new MainGUI();
    private FTPClient ftpClient = null;
    private String host = "";
    private String user = "";
    private String password = "";
    private String remoteFilePath = "";
    private String localFilePath = "";
    private boolean isDirectory = true;

    public FTPFileDownloader(String host, String user, String password, String remoteFilePath, String localFilePath) throws Exception {

        this.host = host;
        this.user = user;
        this.password = password;
        this.remoteFilePath = remoteFilePath;
        this.localFilePath = localFilePath;

        ftpClient = new FTPClient();
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftpClient.connect(host);
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftpClient.login(user, password);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();

    }

    public static MainGUI getMainGUI() {
        return mainGUI;
    }

    public static void setMainGUI(MainGUI mainGUI) {
        FTPFileDownloader.mainGUI = mainGUI;
    }

    public void downloadFile() throws Exception {

        File remoteFile = new File(remoteFilePath);
        localFilePath += "\\\\" + remoteFile.getName();
        File localFile = new File(localFilePath);

            FTPFile ftpFiles[] = ftpClient.listFiles(remoteFilePath);
            FTPFile[] listDirectories = ftpClient.listDirectories(remoteFilePath);

            if (ftpClient.listNames(remoteFilePath).length == 1) {
                if (ftpClient.listNames(remoteFilePath)[0].equals(remoteFilePath)) {
                    isDirectory = false;
                }
            }

            if (!isDirectory) {
                FileDownloadRunner fileDownloadRunner = new FileDownloadRunner(host, user, password, remoteFilePath, localFilePath);
                Thread fileDownloadThread = new Thread(fileDownloadRunner);
                fileDownloadThread.start();
                fileDownloadThread.join();
            }

            if (isDirectory) {

                if (!Character.toString(remoteFilePath.charAt(remoteFilePath.length() - 1)).equals("/")){
                    remoteFilePath += "/";
                }
                localFile.mkdir();

                for (FTPFile ftpFile : ftpFiles) {

                    if (ftpFile.isFile()) {

                        FileDownloadRunner fileDownloadRunner = new FileDownloadRunner(host, user, password, remoteFilePath + ftpFile.getName(), localFilePath + "\\\\" + ftpFile.getName());
                        Thread fileDownloadThread = new Thread(fileDownloadRunner);
                        fileDownloadThread.start();
                        fileDownloadThread.join();

                    }
                }

                for (FTPFile ftpDirectory : listDirectories) {

                    if (ftpDirectory.isDirectory()) {

                        FTPFileDownloader ftpDirDownloader = new FTPFileDownloader(host, user, password, remoteFilePath + ftpDirectory.getName(), localFilePath);
                        Thread dirDownloadThread = new Thread(ftpDirDownloader);
                        dirDownloadThread.start();
                        dirDownloadThread.join();

                    }
                }
            }

            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
    }

    @Override
    public void run() {
        try {
            this.downloadFile();
        } catch (Exception e) {
            e.printStackTrace();
            FTPFileDownloader.getMainGUI().showErrorMassage("Something went wrong! Delete all files in a folder and try again!");
        }
    }
}