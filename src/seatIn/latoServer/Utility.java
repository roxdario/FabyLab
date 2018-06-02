package seatIn.latoServer;

import java.io.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utility {

    public static void zip(ArrayList<String> fnames, String fout) {

        try {
            FileOutputStream fos = new FileOutputStream(fout);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (String filename : fnames){
                addToZipFile(filename, zos);
            }
            zos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
        }

    }


    private static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }


        public static boolean createDirectory(String serverpath){
            File serverpathdir = new File(serverpath);
            return serverpathdir.mkdir();

        }
        public static boolean removeDirectoryOrFile(String serverpath) {
            File serverpathdir = new File(serverpath);
            return serverpathdir.delete();

        }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();

    }
}
