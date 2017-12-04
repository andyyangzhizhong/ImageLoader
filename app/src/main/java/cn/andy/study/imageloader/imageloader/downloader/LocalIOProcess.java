//$_FILEHEADER_BEGIN ***************************
//版权声明: 贵阳朗玛信息技术股份有限公司版权所有
//Copyright (C) 2007 Longmaster Corporation. All Rights Reserved
//文件名称: GroupAvatarDisplay.java
//创建日期: 2012/12/05
//创 建 人: zdx
//文件说明: 群消息处理
//$_FILEHEADER_END *****************************
package cn.andy.study.imageloader.imageloader.downloader;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


/**
 * 本地IO线程，专门用于读取本地IO
 */
public class LocalIOProcess {
    private static LocalIOProcess g_PPThreadProcess;

    private Executor m_Process;

    private LocalIOProcess() {
        m_Process = new ThreadPool(30, TimeUnit.SECONDS, Thread.NORM_PRIORITY - 1);
    }

    public static LocalIOProcess getInstance() {
        if (g_PPThreadProcess == null) {
            synchronized (LocalIOProcess.class) {
                if (g_PPThreadProcess == null) {
                    g_PPThreadProcess = new LocalIOProcess();
                }
            }
        }
        return g_PPThreadProcess;
    }

    public void execute(Runnable a_doInBackground) {
        m_Process.execute(a_doInBackground);
    }
}
