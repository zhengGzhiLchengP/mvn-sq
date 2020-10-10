package com.feiqu.adminFramework.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.ssh.ChannelType;
import cn.hutool.extra.ssh.JschUtil;
import com.feiqu.system.model.deployData.SshModel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Service
public class SshService {

    /**
     * 执行命令
     *
     * @param sshModel ssh
     * @param command  命令
     * @return 结果
     * @throws IOException   io
     * @throws JSchException jsch
     */
    public String exec(SshModel sshModel, String command) throws IOException, JSchException {
        Session session = null;
        try {
            session = getSession(sshModel);
            return exec(session, sshModel.getCharsetT(), command);
        } finally {
            JschUtil.close(session);
        }
    }

    private String exec(Session session, Charset charset, String command) throws IOException, JSchException {
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) JschUtil.createChannel(session, ChannelType.EXEC);
            channel.setCommand(command);
            InputStream inputStream = channel.getInputStream();
            InputStream errStream = channel.getErrStream();
            channel.connect();
            // 读取结果
            String result = IoUtil.read(inputStream, charset);
            //
            String error = IoUtil.read(errStream, charset);
            return result + error;
        } finally {
            JschUtil.close(channel);
        }
    }

    public static Session getSession(SshModel sshModel) {
        return JschUtil.openSession(sshModel.getHost(), sshModel.getPort(), sshModel.getUser(), sshModel.getPassword());
    }
}
