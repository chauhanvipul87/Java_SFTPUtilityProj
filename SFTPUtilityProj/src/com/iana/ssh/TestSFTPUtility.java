package com.iana.ssh;

import com.iana.ssh.exception.WrongConfigurationException;

/**
 * @author Vipul Chauhan
 * 
 */
public class TestSFTPUtility {
	
	public static void main(String[] args) {
		
    	//Provides access to the files on an SFTP server (that is, an SSH or SCP server). 
    	//sftp://[ username[: password]@] hostname[: port][ relative-path]
		
		String hostName = "192.168.0.100:23";
		String username = "vipul";
		String password = "vipul";

		String localFilePath = "G:\\var\\remotefile.txt";
		String remoteFilePath = "/vipul/remotefile.txt";
		String remoteTempFilePath = "/vipul/FakeRemoteTempFile.txt";
		
		SFTPServices  obj =  new SFTPUtility(hostName, username, password);
    	try {
    		
    		obj.upload(localFilePath, remoteFilePath);
    		obj.exist(remoteFilePath);
    		obj.download( localFilePath,remoteFilePath);
    		obj.move( remoteFilePath, remoteTempFilePath);
    		obj.delete( remoteFilePath);
			
		} catch (WrongConfigurationException e) {
			e.printStackTrace();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
    }

}
