package com.videonetics.util;

import java.io.File;
import java.io.IOException;


public class BuildCopy {
	
	private String targetDirectory = "../JARS BUILD";

	public BuildCopy() {
		
		File fTargetDirectory = new File(targetDirectory);
		targetDirectory = fTargetDirectory.getAbsolutePath();
		System.err.println("Target Directory is " + targetDirectory);
		if (fTargetDirectory.exists()) {
			VUtilities.deleteDirectoryContents(targetDirectory);
		}
		
		File[] files = new File("../").listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				String inputPath = files[i].getPath() + "/target/" + files[i].getName() + ".jar";
				String outputPath = null;

				if (files[i].getName().endsWith("V-Archive-Streamer-Proxy")) {
					
					outputPath = "../JARS BUILD/MasterServer/" + "ArchiveProxy" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");
					
					outputPath = "../JARS BUILD/MasterServerMediaServerBoth/" + "ArchiveProxy" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				} else if (files[i].getName().endsWith("V-Smart-Server")) {
					
					outputPath = "../JARS BUILD/MasterServer/" + "VMS" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");
					
					outputPath = "../JARS BUILD/MasterServerMediaServerBoth/" + "VMS" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				} else if (files[i].getName().endsWith("V-File-Streamer")) {
					
					outputPath = "../JARS BUILD/MasterServer/" + "VFileStreamer" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");
					
					outputPath = "../JARS BUILD/MasterServerMediaServerBoth/" + "VFileStreamer" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				} else if (files[i].getName().endsWith("V-Archive-Streamer-Main")) {
					
					outputPath = "../JARS BUILD/MediaServer/" + "ArchiveServer" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");
					
					outputPath = "../JARS BUILD/MasterServerMediaServerBoth/" + "ArchiveServer" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				} else if (files[i].getName().endsWith("V-Superb-Media-Server")) {
					
					outputPath = "../JARS BUILD/MediaServer/" + "MediaServer" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");
					
					outputPath = "../JARS BUILD/MasterServerMediaServerBoth/" + "MediaServer" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				} else if (files[i].getName().endsWith("V-NAS-Uploader")) {
					
					outputPath = "../JARS BUILD/MediaServer/" + "NasUploader" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");
					
					outputPath = "../JARS BUILD/MasterServerMediaServerBoth/" + "NasUploader" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				}  else if (files[i].getName().endsWith("V-NAS-Uploader-Event")) {
					
					outputPath = "../JARS BUILD/MasterServer/" + "NasUploaderEvent" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");
					
					outputPath = "../JARS BUILD/MasterServerMediaServerBoth/" + "NasUploaderEvent" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				} else if (files[i].getName().endsWith("V-Superb-Edge-Uploader")) {
					
					outputPath = "../JARS BUILD/MediaServer/" + "EdgeUploader" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");
					
					outputPath = "../JARS BUILD/MasterServerMediaServerBoth/" + "EdgeUploader" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				} else if (files[i].getName().endsWith("V-Smart-Alert-Client")) {
					
					outputPath = "../JARS BUILD/IVMSClient" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				} else if (files[i].getName().endsWith("IVMSClient")) {
					
					outputPath = "../JARS BUILD/IVMSClient" + "/lib/" + "V-Smart-Alert-Client" + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				} else if (files[i].getName().endsWith("V-DC-DR-Data-Replicator")) {
					
					outputPath = "../JARS BUILD/V-DC-DR-Data-Replicator" + "/lib/" + files[i].getName() + ".jar";
					copyMe(inputPath, outputPath);
					copyMe("../vtpl_cnf/ivms-keystore.p12", outputPath.substring(0, outputPath.lastIndexOf("/") + 1) + "ivms-keystore.p12");

				}
			}
		}
	}
	
	private void copyMe(String inputPath, String outputPath) {
		if (new File(inputPath).exists() && outputPath != null) {
			try {
				VUtilities.copyFile(new File(inputPath), new File(outputPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new BuildCopy();
	}
}
