package App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FileDownloadRunner implements Runnable {

    private FTPClient ftpClient = null;
    private String host = "";
    private String user = "";
    private String password = "";
    private String localFilePath = null;
    private String remoteFilePath = null;

    public FileDownloadRunner(String host, String user, String password, String remoteFilePath, String localFilePath) throws Exception {

        this.host = host;
        this.user = user;
        this.password = password;
        this.localFilePath = localFilePath;
        this.remoteFilePath = remoteFilePath;

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

        if (ftpClient.isConnected()) {
            System.out.println("FTPClient: connected to " + host);
        } else {
            System.out.println("FTPClient: fail to connect" + host);
        }
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public void setRemoteFilePath(String remoteFilePath) {
        this.remoteFilePath = remoteFilePath;
    }

    public void downloadFile() throws IOException {

        FileOutputStream fos = new FileOutputStream(new File(localFilePath));

        boolean sucsses = ftpClient.retrieveFile(remoteFilePath, fos);
        if (sucsses) {
            System.out.println("File " + remoteFilePath + " | => downloaded successfully");
        } else {
            System.out.println("File " + remoteFilePath + " | => downloading fail");
        }

        fos.flush();
        fos.close();

        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }

    }

    @Override
    public void run() {
        try {
            this.downloadFile();
        } catch (IOException e) {
            e.printStackTrace();
            FTPFileDownloader.getMainGUI().showErrorMassage("Something went wrong! Delete all files in a folder and try again!");
        }
    }
}
