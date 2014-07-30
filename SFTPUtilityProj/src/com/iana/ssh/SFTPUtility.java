package com.iana.ssh;

import java.io.File;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import com.iana.ssh.exception.WrongConfigurationException;

/**
 * The class SFTPUtil containing uploading, downloading, checking if file exists
 * and deleting functionality using Apache Commons VFS (Virtual File System)
 * Library
 * 
 * @author Vipul Chauhan
 * 
 */
public class SFTPUtility implements SFTPServices{
	
	private String hostName;
	private String username; 
	private String password;
	
	public SFTPUtility(String hostName, String username, String password){
		this.hostName = hostName;
		this.username = username;
		this.password = password;
	}
	
	/**
     * Method to upload a file in Remote server
     * 
     * @param localFilePath
     *            LocalFilePath. Should contain the entire local file path -
     *            Directory and Filename with \\ as separator
     * @param remoteFilePath
     *            remoteFilePath. Should contain the entire remote file path -
     *            Directory and Filename with / as separator
     *            
      * @return Return UPLOADED if file is locally available & at proper location..            
     *            
     */
	@Override
    public String upload( String localFilePath, String remoteFilePath) throws WrongConfigurationException {

        File file = new File(localFilePath);
        if (!file.exists())
            throw new WrongConfigurationException(LOCAL_FILE_NOT_FOUND);

        StandardFileSystemManager manager = new StandardFileSystemManager();
        try {
            manager.init();

            // Create local file object
            FileObject localFile = manager.resolveFile(file.getAbsolutePath());

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(createConnectionString(remoteFilePath), createDefaultOptions());
            /*
             * use createDefaultOptions() in place of fsOptions for all default
             * options - vipul.
             */

            // Copy local file to sftp server
            remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
            System.out.println("File upload success");
            return FILE_UPLOADED;
        } catch (Exception e) {
            throw new WrongConfigurationException(e);
        } finally {
            manager.close();
        }
    }
	
	
    /**
     * Method to move file from remote system to other location on remove system.
     * 
     * @param remoteSrcFilePath
     *            RemoteSrcFilePath. Should contain the entire remote file path -
     *            Directory and Filename with / as separator, from where you want to move file.
     * @param remoteDestFilePath
     *            RemoteDestFilePath. Should contain the entire remote file path on same remote system-
     *            Directory and Filename with / as separator,from where you want to store file.
     *            
     * @return Return MOVED if file has been moved successfully       
     */
	
	@Override
    public String move( String remoteSrcFilePath, String remoteDestFilePath) throws WrongConfigurationException{
        StandardFileSystemManager manager = new StandardFileSystemManager();
        try {
            manager.init();

            // Create remote object
            FileObject remoteFile = manager.resolveFile(createConnectionString(remoteSrcFilePath), createDefaultOptions());
            FileObject remoteDestFile = manager.resolveFile(createConnectionString(remoteDestFilePath), createDefaultOptions());

            if (remoteFile.exists()) {
                remoteFile.moveTo(remoteDestFile);;
                System.out.println("Move remote file success");
                return FILE_MOVED;
            }
            else{
                System.out.println("Source file doesn't exist");
                return SOURCE_FILE_NOT_FOUND;
            }
        } catch (Exception e) {
            throw new WrongConfigurationException(e);
        } finally {
            manager.close();
        }
    }

    /**
     * Method to download the file from remote server location
     * 
     * @param localFilePath
     *            LocalFilePath. Should contain the entire local file path -
     *            Directory and Filename with \\ as separator
     * @param remoteFilePath
     *            remoteFilePath. Should contain the entire remote file path -
     *            Directory and Filename with / as separator
     * @return Return DOWNLOADED if file available to download.    
     */
	@Override
    public String download( String localFilePath, String remoteFilePath) throws WrongConfigurationException{

        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Append _downlaod_from_sftp to the given file name.
            //String downloadFilePath = localFilePath.substring(0, localFilePath.lastIndexOf(".")) + "_downlaod_from_sftp" + localFilePath.substring(localFilePath.lastIndexOf("."), localFilePath.length());

            // Create local file object. Change location if necessary for new downloadFilePath
            FileObject localFile = manager.resolveFile(localFilePath);

            // Create remote file object
            FileObject remoteFile = manager.resolveFile(createConnectionString(remoteFilePath), createDefaultOptions());

            // Copy local file to sftp server
            localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
            
            System.out.println("File download success");
            return FILE_DOWNLOADED;
        } catch (Exception e) {
            throw new WrongConfigurationException(e);
        } finally {
            manager.close();
        }
    }

    /**
     * Method to delete the specified file from the remote system
     * 
     * @param localFilePath
     *            LocalFilePath. Should contain the entire local file path -
     *            Directory and Filename with \\ as separator
     * @param remoteFilePath
     *            remoteFilePath. Should contain the entire remote file path -
     *            Directory and Filename with / as separator
     *            
     * @return Return DELETED if delete successfully       
     */
	@Override
    public String delete( String remoteFilePath) throws WrongConfigurationException {
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create remote object
            FileObject remoteFile = manager.resolveFile(createConnectionString(remoteFilePath), createDefaultOptions());

            if (remoteFile.exists()) {
                remoteFile.delete();
                System.out.println("Delete remote file success");
                return FILE_DELETED;
            }else{
            	return FAILED_TO_DELETE;
            }
        } catch (Exception e) {
            throw new WrongConfigurationException(e);
        } finally {
            manager.close();
        }
    }

    // Check remote file is exist function:
    /**
     * Method to check if the remote file exists in the specified remote
     * location
     * 
     * @param remoteFilePath
     *            remoteFilePath. Should contain the entire remote file path -
     *            Directory and Filename with / as separator
     * @return Returns if the file exists in the specified remote location
     */
	@Override
    public boolean exist( String remoteFilePath) {
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {
            manager.init();

            // Create remote object
            FileObject remoteFile = manager.resolveFile(createConnectionString(remoteFilePath), createDefaultOptions());
            System.out.println("File exist: " + remoteFile.exists());
            return remoteFile.exists();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            manager.close();
        }
    }

    /**
     * Generates SFTP URL connection String
     * 
     * @param remoteFilePath
     *            remoteFilePath. Should contain the entire remote file path -
     *            Directory and Filename with / as separator
     * @return concatenated SFTP URL string
     */
    private String createConnectionString(String remoteFilePath) {
        return "sftp://" + this.username + ":" + this.password + "@" + this.hostName + "/" + remoteFilePath;
    }

    /**
     * Method to setup default SFTP config
     * 
     * @return the FileSystemOptions object containing the specified
     *         configuration options
     * @throws FileSystemException
     */
    
    private FileSystemOptions createDefaultOptions() throws FileSystemException {
        // Create SFTP options
        FileSystemOptions opts = new FileSystemOptions();

        // SSH Key checking
        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");

        /*
         * Using the following line will cause VFS to choose File System's Root
         * as VFS's root. If I wanted to use User's home as VFS's root then set
         * 2nd method parameter to "true"
         */
        // Root directory set to user home
        SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);

        // Timeout is count by Milliseconds
        SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);
        
        return opts;
    }
}