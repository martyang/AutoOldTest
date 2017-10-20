package com.gionee.myapplication.newpkg;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by viking on 10/14/16.
 */

public class ShellUtil {

    private final static Uri    CONTENT_URI      = Uri.parse("content://com.amigo.settings.RosterProvider/rosters");
    public static final  String COMMAND_SU       = "amigosu";
    public static final  String COMMAND_SH       = "sh";
    public static final  String COMMAND_EXIT     = "exit\n";
    public static final  String COMMAND_LINE_END = "\n";

    private ShellUtil() {
        throw new AssertionError();
    }

    /**
     * kill process with the given process id .
     *@param
     *@author Viking Den
     *@version 1.0
     *@exception
     *@return null
     *@date 2016/6/22 0021 11:35
     */
    public static void killProcess(int pid){
        ShellUtil.CommandResult result = ShellUtil.execCommand("kill -9 " + pid , true) ;
    }

    /**
     * kill uiautomator process with the given process id .
     *@param
     *@author Viking Den
     *@version 1.0
     *@exception
     *@return null
     *@date 2016/6/21 0021 17:35
     */
    public static void killUiAutomator() {
        int pUiautomatorId = dumpUiautomatorId() ;
        if (pUiautomatorId == -1){
            return ;
        }
        killProcess(pUiautomatorId);
    }

    /**
     * Get uiautomator's process id if it's running, or return -1
     *@param
     *@author Viking Den
     *@version 1.0
     *@exception
     *@return null
     *@date 2016/6/21 0021 17:37
     */
    private static int dumpUiautomatorId() {
        int pId = -1 ;
        try{
            //root      3815  485   1884972 51760 futex_wait 0000000000 S uiautomator
            ShellUtil.CommandResult result = ShellUtil.execCommand("ps |grep uiautomator" , false) ;
            if (result.result == 0 && result.successMsg != null && !result.successMsg.equals("")){
                String   line  = result.successMsg ;
                String[] args  = line.split(" ") ;
                int      arg_2 = 1 ;
                for (String arg : args){
                    if (!arg.equals("") && arg_2 == 2){
                        return Integer.parseInt(arg) ;
                    }
                    if (!arg.equals("")){
                        arg_2 ++ ;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pId ;
    }

    /**
     * require system get current device as a root application
     * @param context
     */
    public static void requireRoot(Context context){
        ContentValues values = new ContentValues();
        values.put("usertype", "root");
        values.put("packagename", context.getPackageName());
        values.put("status", 1);
        context.getContentResolver().insert(CONTENT_URI, values);
    }

    /**
     * check whether has root permission
     *
     * @return
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    /**
     * execute shell command, default return result msg
     *
     * @param command command
     * @param isRoot whether need to run with root
     * @return
     * @see ShellUtil#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[] {command}, isRoot, true);
    }

    /**
     * execute shell commands, default return result msg
     *
     * @param commands command list
     * @param isRoot whether need to run with root
     * @return
     * @see ShellUtil#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, true);
    }

    /**
     * execute shell commands, default return result msg
     *
     * @param commands command array
     * @param isRoot whether need to run with root
     * @return
     * @see ShellUtil#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    /**
     * execute shell command
     *
     * @param command command
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtil#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[] {command}, isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     *
     * @param commands command list
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtil#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     *
     * @param commands command array
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return <ul>
     *         <li>if isNeedResultMsg is false, {@link CommandResult#successMsg} is null and
     *         {@link CommandResult#errorMsg} is null.</li>
     *         <li>if {@link CommandResult#result} is -1, there maybe some excepiton.</li>
     *         </ul>
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process        process       = null;
        BufferedReader successResult = null;
        BufferedReader errorResult   = null;
        StringBuilder  successMsg    = null;
        StringBuilder  errorMsg      = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }

                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            // get command result
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    /**
     * result of command
     * <ul>
     * <li>{@link CommandResult#result} means result of command, 0 means normal, else means error, same to excute in
     * linux shell</li>
     * <li>{@link CommandResult#successMsg} means success message of command result</li>
     * <li>{@link CommandResult#errorMsg} means error message of command result</li>
     * </ul>
     *
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
     */
    public static class CommandResult {

        /** result of command **/
        public int    result;
        /** success message of command result **/
        public String successMsg;
        /** error message of command result **/
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
    /**
     * 停止正在执行的ui脚本
     */
    public static void stopUiCase(String tag) {
        CommandResult commandResult = execCommand("ps |grep "+tag, false);
        String msg = commandResult.successMsg;
        if (!"".equals(msg)) {
            String proString = msg.replace("shell", " ").trim()
                                  .split(" ")[0].trim();
            execCommand("kill -9 " + proString, false);
        }
    }
}
