package com.gionee.eighteenmonth.progress;

import android.content.Context;
import android.os.AsyncTask;

import com.gionee.eighteenmonth.R;
import com.gionee.eighteenmonth.util.Constants;
import com.gionee.eighteenmonth.util.ExeclUtil;
import com.gionee.gnutils.ContextUtil;
import com.gionee.gnutils.Preference;
import com.gionee.gnutils.ResourceUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class LogReaderAsyncTask
        extends AsyncTask<Void, String, Boolean>
{

    private String     mFileName   = null;
    private FileHelper mfileHelper = null;
    private String[]     mApp;
    private List<String> mLineList;

    public LogReaderAsyncTask(Context ctx) {
        mFileName = Constants.LOG_PATH + Preference.getString(ctx,
                                                              Constants.START_DATE) + "_log.txt";
        mfileHelper = new FileHelper();
        mApp = ResourceUtil.getStringArrayResource(R.array.arr_pkg);
        mLineList = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        // Log.i("enter doInBackground");
        Process        process = null;
        BufferedReader reader  = null;
        boolean        ok      = true;

        try {
            // clear buffer first
            Util.clearLogcatBuffer();
            Util.clearLogcatBufferForEvents();

            process = LogcatHelper.getLogcatProcess(LogcatHelper.BUFFER_EVENTS);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()), 8192);
            while (!isCancelled()) {
                final String line = reader.readLine();
                if (isContainNeed(line)) {
                    mfileHelper.write(mFileName, line, true);
                    publishProgress(line);
                    mLineList.add(line);
                }
                if (line.contains("com.example.cta_testload") && line.contains(
                        "am_activity_launch_time"))
                {
                    analysisLog();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            ok = false;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                RuntimeHelper.destroy(process);
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ok;
    }

    private boolean isContainNeed(String s) {
        if (s == null || s.equals("")) {
            return false;
        } else if (s.contains("am_create_activity") && !getLogPkg(s).isEmpty()) {
            return true;
        } else if (s.contains("am_activity_launch_time") && !getLogPkg(s).isEmpty()) {
            return true;
        }
        return false;
    }

    private void analysisLog()
            throws ParseException
    {
        List<String> list = new ArrayList<>();
        for (String s : mApp) {
            list.add(getAppStartTime(s));
        }
        mLineList.clear();
        ExeclUtil.insertData(new File(Constants.EXCEL_PATH + Preference.getString(ContextUtil.getInstance(),
                                                                                  Constants.START_DATE) + ".xls"),
                             list);
    }

    private String getAppStartTime(String pkg)
            throws ParseException
    {
        String create = "";
        for (String s : mLineList) {
            if (s.contains(pkg) && s.contains("am_create_activity")) {
                create = s;
            } else if (s.contains(pkg) && s.contains("am_activity_launch_time")) {
                return String.valueOf(getTDOA(create, s));
            }
        }
        return "-";
    }

    private String getLogPkg(String line) {
        for (String s : mApp) {
            if (line.contains(s)) {
                return s;
            }
        }
        return "";
    }

    /**
     * 时间差
     */
    private long getTDOA(String start, String end)
            throws ParseException
    {
        //14:45:55.915
        long startTimeHms = getTimeHms(start.split(" ")[1]);
        long endTimeHms   = getTimeHms(end.split(" ")[1]);
        return endTimeHms - startTimeHms;

    }

    /**
     * 获得时间
     */
    private long getTimeHms(String data)
            throws ParseException
    {
        String[] strs = data.split("\\.");

        String[] strings = strs[0].split(":");

        return Long.parseLong(strings[2]) * 1000 + Long.parseLong(strings[1]) * 1000 * 60 + Long.parseLong(
                strings[0]) * 1000 * 60 * 60 + Long.parseLong(strs[1]);
    }
}
