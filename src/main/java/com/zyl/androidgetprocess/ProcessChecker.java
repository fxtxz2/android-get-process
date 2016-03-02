package com.zyl.androidgetprocess;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Android上面获取指定包名的进程是否运行
 * 参考了https://github.com/jaredrummler/AndroidProcesses
 * Created by zyl on 16/3/1.
 */
public class ProcessChecker {
    /**
     *  Android上面获取指定包名的进程是否运行
     * @param packName 需要检查的应用主包名
     * @return true 表示进程在运行; false 表示进程没有运行
     */
    public static boolean isCheckProcess(String packName, Context context) {
        boolean checkProcess = false;
        // Android4.4.4进程监控(普通方法)
        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo process: runningAppProcessInfos) {
            if (packName.equals(process.processName)) {
                Log.v("app_run:", process.processName);
                checkProcess = true;
                break;
            }
        }
        if (!checkProcess){
            // Android5.1进程监控写法
            List<AndroidAppProcess> processes = ProcessManager.getRunningAppProcesses();
            for (AndroidAppProcess process : processes){
                if (packName.equals(process.getPackageName())){
                    Log.v("app_run:", process.getPackageName());
                    checkProcess = true;
                    break;
                }
            }
        }
        // 自己写shell运行
        if (!checkProcess){
            // Android5.0.2监控
            Process process = null;
            try {
                String shell = "ps | grep " + packName;
                String[] command = { "sh", "-c", shell };
                process = Runtime
                        .getRuntime()
                        .exec(command);
                InputStream in = process.getInputStream();
                OutputStream out = process.getOutputStream();
                String str = IOUtils.toString(in);
                if(Pattern.compile(Pattern.quote(packName), Pattern.CASE_INSENSITIVE).matcher(str).find()){
                    Log.v("app_run:", str);
                    checkProcess = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (process != null){
                    process.destroy();
                }
            }
        }
        return checkProcess;
    }
}
