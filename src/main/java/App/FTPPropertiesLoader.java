package App;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class FTPPropertiesLoader {

    // Variables

    private String host;
    private String user;
    private String password;
    private String remoteFilePath;
    private String localFilePath;

    // Constructors

    public FTPPropertiesLoader() {
    }

    public FTPPropertiesLoader(String host, String user, String password, String remoteFilePath, String localFilePath) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.remoteFilePath = remoteFilePath;
        this.localFilePath = localFilePath;
    }

    // Getters and Setters

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public void setRemoteFilePath(String remoteFilePath) {
        this.remoteFilePath = remoteFilePath;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }


    // Methods

    public void loadProperties() {

        Properties properties = new Properties();
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/FTPConnectionProperties/FTPConnection");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            properties.load(new InputStreamReader(bufferedInputStream, "UTF8"));

            host = properties.getProperty("host");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            remoteFilePath = properties.getProperty("remoteFilePath");
            localFilePath = properties.getProperty("localFilePath");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
