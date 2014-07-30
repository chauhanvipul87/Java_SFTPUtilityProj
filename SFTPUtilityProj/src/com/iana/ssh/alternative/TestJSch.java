package com.iana.ssh.alternative;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class TestJSch {
    public static void main(String args[]) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession("vipul", "192.168.0.100", 23);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("vipul");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.get("vipul/remotefile.txt", "localfile.txt");
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();  
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }
}