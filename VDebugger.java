package com.videonetics.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;

public class VDebugger {
	
	private static final Logger LOGGER = Logger.getLogger(VDebugger.class.getSimpleName());

	private FileOutputStream debugFos = null;
	private boolean isDebugFolderExist = false;
	private String logFileName = null;
	private static final String ACTION_1 = "Debug/" ;

	private static final String ERROR_STRING = "error: {0}";

	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public VDebugger(String logFileName) {
		this.logFileName = logFileName;
		try {Thread.sleep(2);} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}
		
		boolean isDisabled = false;
		try {
			
			byte[] inputArray = loadFileContents("logDisabled.txt");
			if(inputArray != null) {
				String[] logDisabledArr = new String(inputArray).split("\n");
				String logFileParent = new File(logFileName).getParent();
				
				if (logFileParent ==  null) {
					logFileParent = FilenameUtils.removeExtension(logFileName);
				}
				
				for (String logDisabled : logDisabledArr) {
					if (!logDisabled.startsWith("#") && logFileParent.contains(logDisabled.trim())) {
						isDisabled = true;
						break;
					}
				}
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}
		
		try {isDebugFolderExist = new File("Debug").exists() && !isDisabled;} catch (Exception e)
		{LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());}
	}
	
	public static synchronized byte[] loadFileContents(String url) {

		byte[] dataBytes = null;

		try(FileInputStream fis = new FileInputStream(url)) {
			dataBytes = new byte[fis.available()];
			int n = fis.read(dataBytes);
			if (n > 0) {
				LOGGER.log(Level.INFO,"numberOfBytes :{0} ", n);
			}
		} catch (IOException ex) {
			dataBytes = null;
		} catch (Exception ex) {
			LOGGER.log(Level.WARNING, ERROR_STRING, ex.getMessage());
		} 

		return dataBytes;
	}

	public synchronized void writeToVTPLDebugFile(String debugStr) {

		if (isDebugFolderExist) {
			if (debugFos != null) {
				try {
					if (debugFos.getChannel().size() > 10 * 1024 * 1024) {
						debugFos.close();
						if (new File(ACTION_1+ logFileName).delete()) {
							debugFos = null;
						}
					}
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
				}
			}

			long currentTime = System.currentTimeMillis();
			
			if (debugFos == null) {
				try {
					if (!new File(ACTION_1 + logFileName).exists()) {
						createFile(ACTION_1+ logFileName);
					}
					debugFos = new FileOutputStream(ACTION_1 + logFileName, true);
				} catch (Exception e)
				{
				Object [] params = new Object[] { e.getMessage()};
				LOGGER.log(Level.WARNING, ERROR_STRING, params);}}
			if (debugFos != null) {try {debugFos.write(("\n" + df.format(new Date(currentTime)) + "(" + currentTime + ") " + debugStr).getBytes());debugFos.flush();} 
			catch (Exception e) {LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());}}
		}
	}

	public synchronized void closeLogFile() {
		if (debugFos != null) {
			try {debugFos.close();} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
			}
			debugFos = null;
		}
	}
	
	public static boolean createFile(String path) {
		boolean isSuccess = false;

		try {

			File out = new File(path);
			if (out.getParentFile() != null && !out.getParentFile().exists()) {
				out.getParentFile().mkdirs();
			}

			Boolean fileCreated;
			fileCreated = out.createNewFile();
			if(Boolean.FALSE.equals(fileCreated)) {
				LOGGER.log(Level.WARNING," Problem in creating file File: {0}", out.getName());
			}
			
			isSuccess = true;
		} catch (Exception e) {
			Object [] params = new Object[] { e.getMessage()};
			LOGGER.log(Level.WARNING, ERROR_STRING, params);}
		

		return isSuccess;
	}

}
