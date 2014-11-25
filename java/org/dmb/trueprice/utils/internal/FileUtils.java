/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.utils.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


/**
 *
 * @author Guiitch
 */
public abstract class FileUtils {
    
         private static final Logger log 
            = InitContextListener.getLogger( FileUtils.class) ;
    
    /**
     * Créer les dossiers nécessaires à la FID pour chaque langue
     * 
     * @throws IOException - Si un des dossiers ou fichiers est inaccessible
     * @throws FileExistsException - Si le dossier temp existe déjà => Need admin intervention
     */
    public static void createFile(String folder, String fullFilename) throws IOException {

        // Attributs sur lesquels travailler
        File f = null;
        Path p = null;
        String folderWorked = "";

        // Pour chaque langue
//        for (String lang : fidLanguages) {

            folderWorked = folder;

            // Si la langue doit être modifier dans le chemin de la FID
//            if (lang != fidLang) {
//                folderWorked = fidFolder.replace(fidLang, lang);
//            }
            // Get the folder
            p = Paths.get(folderWorked);

            // Si le dossier n'existe pas déjà
            if (!Files.exists(p)) {
                // Creer le dossier
                    Files.createDirectory(p);
                    log.info("Created > [dir=" + p + "]");
            }
                try {                    
                    // Creer le fichier de la FID
                    f = new File(p + File.separator + fullFilename);
                    f.createNewFile();
                    log.info("Created > [file=" + f.getAbsolutePath() + "]");

                } catch (IOException e) {
                    throw new IOException("Error creating file [" + p + File.separator + fullFilename + "]\n >>> Cause  : "
                            + e.getMessage());
                }
            
            // Si le dossier existe déjà => Une erreur est survenue dans une
            // requête précédente
//            else {
                // TODO: Advertise to admin >> 'temp' folder already exist
//                throw new FileExistsException("Directory already exists ["
//                        + folderWorked + "]");
//            }
//        }
    }
    
/**
     * Renommer un fichier
     * 
     * @param oldName - Fichier a renommer
     * @param newName - Nouveau nom du fichier
     * @throws FileExistsException 
     * @throws FileNotFoundException 
     */
    public static void renameFile(String oldName, String newName) throws FileExistsException, FileNotFoundException, IOException {
        // Dossier sur lesquels travailler
        String oldFolderWorked = "";
        String newFolderWorked = "";
        // pour chaque langue
//        for (String lang : fidLanguages) {
            oldFolderWorked = oldName;
            newFolderWorked = newName;
            // Si la langue doit être modifiée dans le chemin des dossiers
//            if (!oldFolder.contains(lang)) {
//                oldFolderWorked = oldFolder.replace(fid_lang, lang);
//            }
//            if (!newFolder.contains(lang)) {
//                newFolderWorked = newFolder.replace(fid_lang, lang);
//            }
            // Si l'ancien fichier existe
            if (Files.exists(Paths.get(oldFolderWorked))) {
                // Si le nouveau fichier existe 
                if (!Files.exists(Paths.get(newFolderWorked))) {
                    // Supprimer l'ancien fichier
                    File f_Old = new File(oldFolderWorked);
                    File f_New = new File(newFolderWorked);
                    // Renommer l'ancien dossier avec le nouveau
                    f_Old.renameTo(f_New);
                    log.info("Renamed > [" + f_Old.getAbsolutePath()
                            + "] to [" + f_New.getAbsolutePath() + "]");
                }
                // Si le nouveau fichier existe déjà
                else {
                    throw new FileExistsException(
                            "The (new)file already exists [" + newFolderWorked
                                    + "]");
                }
            }
            
            
            // Si l'ancien fichier n'est pas accessible
            else {
                // TODO: Log admin intervention needed
                throw new FileNotFoundException(
                        "The (old)file does not exists [" + oldFolderWorked
                                + "]");
            }
//        } // END for lang
    }    
/**
     * Renommer la structure temporaire vers la definitive
     * 
     * @param oldFolder - Dossier a renommer
     * @param newFolder - Nom du nouveau dossier
     * @param fid_lang - Langue de la FID pour pouvoir renommer les 4 langues
     * @throws FileExistsException 
     * @throws FileNotFoundException 
     */
    public static void renameFolders(String oldFolder, String newFolder) throws FileExistsException, FileNotFoundException {
        // Dossier sur lesquels travailler
        String oldFolderWorked = "";
        String newFolderWorked = "";
        // pour chaque langue
//        for (String lang : fidLanguages) {
            oldFolderWorked = oldFolder;
            newFolderWorked = newFolder;
            // Si la langue doit être modifiée dans le chemin des dossiers
//            if (!oldFolder.contains(lang)) {
//                oldFolderWorked = oldFolder.replace(fid_lang, lang);
//            }
//            if (!newFolder.contains(lang)) {
//                newFolderWorked = newFolder.replace(fid_lang, lang);
//            }
            // Si l'ancien dossier est accessible
            if (Paths.get(oldFolderWorked).isAbsolute()) {
                // Si le nouveau dossier n'existe pas encore
                if (!Files.exists(Paths.get(newFolderWorked))) {
                    // Recuperer les dossiers en tant que 'File'
                    File f_Old = new File(oldFolderWorked);
                    File f_New = new File(newFolderWorked);
                    // Renommer l'ancien dossier avec le nouveau
                    f_Old.renameTo(f_New);
                    log.info("Renamed > [" + f_Old.getAbsolutePath()
                            + "] to [" + f_New.getAbsolutePath() + "]");
                }
                // Si le nouveau dossier existe déjà
                else {
                    throw new FileExistsException(
                            "The (new)path already exists [" + newFolderWorked
                                    + "]");
                }
            }
            
            
            // Si l'ancien dossier n'est pas accessible
            else {
                // TODO: Log admin intervention needed
                throw new FileNotFoundException(
                        "The (old)path is not absolute [" + oldFolderWorked
                                + "]");
            }
//        } // END for lang
    }    
    
/**
     * Write FID's attachements on server
     * 
     * @param fileName - Nom du fichier joint
     * @param fileContent - InputStream contenant le coprs du fichier
     * @param targetFolder - Dossier où écrire le fichier
     */
//    public static void writeFile(String fileName,InputStream fileContent, String targetFolder) throws IOException{
    public static void writeFile(String fileName, byte[] bytes, String targetFolder) throws IOException{

//        try {
            
            Files.write(
                Paths.get(targetFolder + File.separator + fileName),
                bytes, StandardOpenOption.WRITE
            );

//        } catch ( e) {
//            logger.error("IOException writing attachment on disk : " + e.getMessage());
//        }
    }    
    
    public static void deleteTempFiles (String folder){
        
        File dir = new File(folder);
        File files [] = dir.listFiles();
        
        for(int index = 0; index < files.length; index++){ 
            
            dir = files[index];
            
           if (dir.isFile() && dir.getName().endsWith(".tmp")) {
            
                boolean wasDeleted = files[index].delete();

                if (!wasDeleted)
                {
                  log.warn("Could not delete " + files[index]);
                } else {
                  log.info("Deleted " + files[index]);
                }
           }
        }
        
    }    
    public static void deleteFile (String AbsolutePath){
        
        File f = new File(AbsolutePath);
            
           if (f.isFile()) {
            
                boolean wasDeleted = f.delete();

                if (!wasDeleted)
                {
                  log.warn("Could not delete " + f);
                }
           }
        
    }    
}
