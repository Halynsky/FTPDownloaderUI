package App;

import java.io.File;


public class LocalFolderChecker {


    public void checkAndCreateFolder(String localPath) {

        File file = new File(localPath);
        if (file.getParent() != null && !file.exists()) {
            checkAndCreateFolder(file.getParent());
            File newFolder = new File (file.getPath());
            newFolder.mkdir();
            System.out.println("Directory: " + file.getPath() + " => is created");
        }
    }



}
