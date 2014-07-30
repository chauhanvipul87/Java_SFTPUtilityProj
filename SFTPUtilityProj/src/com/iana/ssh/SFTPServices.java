package com.iana.ssh;

import com.iana.ssh.exception.WrongConfigurationException;

public interface SFTPServices extends Status{

	public String upload(String localFilePath, String remoteFilePath)throws WrongConfigurationException ;
	public boolean exist(String remoteFilePath)throws WrongConfigurationException ;
	public String download(String localFilePath, String remoteFilePath)throws WrongConfigurationException ;
	public String delete(String remoteFilePath)throws WrongConfigurationException ;
	public String move( String remoteSrcFilePath, String remoteDestFilePath) throws WrongConfigurationException;
	

}
