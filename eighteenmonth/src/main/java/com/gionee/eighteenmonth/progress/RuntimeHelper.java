package com.gionee.eighteenmonth.progress;


import android.os.Build;

import java.io.IOException;
import java.util.List;

/**
 * Helper functions for running processes.
 * @author nolan
 *
 */
public class RuntimeHelper {
    
	/**
	 * Exec the arguments, using root if necessary.
	 * @param args
	 */
	public static Process exec(List<String> args) throws IOException {
		// since JellyBean, sudo is required to read other apps' logs
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
				//&& !SuperUserHelper.isFailedToObtainRoot()
                ) {
			Process process = Runtime.getRuntime().exec("su");
			
			PrintStream outputStream = null;
			try {
				outputStream = new PrintStream(new BufferedOutputStream(process.getOutputStream(), 8192));
				outputStream.println(TextUtils.join(" ", args));
				outputStream.flush();
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}
			

			return process;
		}*/
		return Runtime.getRuntime().exec(Util.toArray(args, String.class));
	}
	
	public static void destroy(Process process) {
	    // if we're in JellyBean, then we need to kill the process as root, which requires all this
	    // extra UnixProcess logic
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
	            && !SuperUserHelper.isFailedToObtainRoot()) {
	       SuperUserHelper.destroy(process);
	    } else {
	        process.destroy();
	    }
	}
	
}