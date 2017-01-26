package appgenerator.zip

import groovy.transform.CompileStatic

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@CompileStatic
class ZipHelper {

    void zipFolder(String srcFolder, File destZipFile) {
        def fileWriter = new FileOutputStream(destZipFile)
        def zip = new ZipOutputStream(fileWriter)

        addFolderToZip "", srcFolder, zip
        zip.flush()
        zip.close()
    }

    protected void addFileToZip(String path, String srcFile, ZipOutputStream zip) {
        File folder = new File(srcFile)
        if (folder.isDirectory()) {
            addFolderToZip path, srcFile, zip
        } else {
            byte[] buf = new byte[1024]
            int len
            FileInputStream inputStream = new FileInputStream(srcFile)
            zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()))
            while ((len = inputStream.read(buf)) > 0) {
                zip.write(buf, 0, len)
            }
        }
    }

    protected void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) {
        File folder = new File(srcFolder)

        zip.putNextEntry(new ZipEntry(path + "/" + folder.name + "/"))

        for (String fileName : folder.list()) {
            if (path == '') {
                addFileToZip "${folder.name}", "${srcFolder}/${fileName}", zip
            } else {
                addFileToZip "${path}/${folder.name}", "${srcFolder}/${fileName}", zip
            }
        }
    }
}
