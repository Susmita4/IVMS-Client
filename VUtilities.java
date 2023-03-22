package com.videonetics.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.PixelGrabber;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.PasswordAuthentication;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.videonetics.media.event.EventDataInImage;
import com.videonetics.model.output.VFile;
import com.videonetics.model.output.VRectangle;
import com.videonetics.values.VMediaProperty;
import com.videonetics.values.VReturn;

public class VUtilities {
	
	private static final Logger LOGGER = Logger.getLogger(VUtilities.class.getSimpleName());
	
	 private VUtilities() {
		 
	 }



		public static final String LOOPBACK_IP = "127.0.0.1";

		public static final long SUPER_ADMIN_SESSION_ID = Long.MIN_VALUE;

		public static final short CENTRAL_ADMIN_PROFILE = 7;
		public static final short CENTRAL_OPERATOR_PROFILE = 8;

		public static final long LICENSE_REFRESH_INTERVAL = 12 * 60 * 60 * 1000L;
		//private static final long LICENSE_REFRESH_INTERVAL = 20 * 1000;

		public static final long LICENSE_SERVER_CONNECT_RETRY_INTERVAL = 10 * 1000L;
		public static final long LICENSE_SERVER_CONNECT_RETRY_MAX_ATTEMPT = 6;

		public static final String MAIN_SERVER_HEART_BEAT_FILE_NAME = "MainServerHearbeat";
		public static final String PROXY_SERVER_HEART_BEAT_FILE_NAME = "ProxyServerHearbeat";
		public static final String NAS_UPLOADER_HEART_BEAT_FILE_NAME = "NasUploaderHearBeat";
		public static final String EDGE_SERVER_HEART_BEAT_FILE_NAME = "EdgeServerHearBeat";
		public static final long GENERIC_SERVER_HEART_BEAT_INTERVAL = 3 * 1000L;
		public static final long GENERIC_SERVER_HEART_BEAT_CHECKER_INTERVAL = 10 * 1000L;

		/** Only for Alphanumeric & space */
		public static final String DEFAULT_PATTERN_MATCHER = "[\\w\\s]*";
		/** Only for Alphanumeric, space & (@.) */
		public static final String USER_ID_PATTERN_MATCHER = "[\\w@.\\s]*";
		/** Only for Alphanumeric, space & (@#.!$) */
		public static final String SECURITY_ANSWER_PATTERN_MATCHER = "[\\w@#.!$\\s]*";
		/** Only for Alphanumeric, space & (.<+|&!*-%_>?:=) */
		public static final String PD_PATTERN_MATCHER = "[\\w.<+|&!*-%_>?:=\\s]*";
		
		private static final String EMAILADDRESS_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		private static Pattern EMAILADDRESS_PATTERN = Pattern
				.compile(EMAILADDRESS_REGEX);
		/*
		 * Notes on date format strings:
		 * 1.  Use kk (24hr) instead of HH (12hr) for hours; if you use HH you'll 
		 * also need to include am/pm. 
		 * 2.  Avoid 'z' for the timezone.  As so much of our work is historical 
		 * and involving data from different timezones, this is confusing.  If you 
		 * need to display the timezone use the DisplayTimeZoneID string
		 * to indicate location.
		 */
		protected static String FileName_DateTime = "yyyyMMdd_kkmmss";
		protected static String Log_TimeOnly = "kk:mm:ss.SSS";
		protected static String DateTime_With_Millis = "yyyy-MM-dd kk:mm:ss.SSS";
		protected static String DateTime_Packed = "yyyymmddkkmmssSS";
		protected static String Long_DateTime = "yyyy.MM.dd 'at' HH:mm:ss.SSS";
		protected static String pSQL_DateTime = "yyyy-MM-dd kk:mm:ss";
		protected static String Log_DateOnly = "yyyy-MM-dd";
		protected static String FileName_DateTime2 = "yyyyMMddkkmmss";
		protected static String DateTime_With_ZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		protected static String vTimeLine_DateTime = "E, dd MMM yyyy";
		protected static String fft_DateTime = "yyyyMMdd'T'HHmmss";

		//	"yyyy.MM.dd_kk.mm.ss"
		protected static String Log_DateTime = "dd/MM/yyyy HH:mm:ss";
		protected static String Log_DateShort = "MM-dd-yy";
		protected static String Log_TimeShort = "kk:mm:ss";
		protected static String Log_TimeProFormat = "MM/dd/yyyy HH:mm:ss";

		public static final String IMPULSE_CAMERA_MOTION_DETECTION = "event.motion_detection.enable";
		public static final String IMPULSE_CAMERA_TAMPER_DETECTION = "event.tamper_alarm.enable";
		public static final String IMPULSE_CAMERA_FACE_DETECTION = "event.face_detection.enable";
		public static final String IMPULSE_CAMERA_OBJECT_DETECTION = "event.object_detection.enable";

		public static final String[] MEDIA_SERVER_RECORDING_STOARGE_TYPE_STRING = {"Local Storage Only", "Network Storage Only", "Record Locally First, Then Upload to Network Storage"};

		public static final int SERVER_RECORDING_IN_LOCAL_STOARGE_ONLY = 0, SERVER_RECORDING_IN_NETWORK_STOARGE_ONLY = 1, SERVER_RECORDING_IN_LOCAL_FIRST_THEN_UPLOAD_TO_NETWORK_STOARGE = 2;

		public static final int VMS_CAMERA_LOAD_BALANCE_TYPE_INVALID = 0, VMS_CAMERA_LOAD_BALANCE_TYPE_AUTO = 1, VMS_CAMERA_LOAD_BALANCE_TYPE_FIXED = 2;
		public static final int VMS_INSTALLATION_TYPE_INVALID = 0, VMS_INSTALLATION_TYPE_DC = 1, VMS_INSTALLATION_TYPE_DR = 2;

		public static final int VIVOTEK_CAMERA_MOTION_SUBSCRIBED = 1, VIVOTEK_CAMERA_MOTION_UNSUBSCRIBED = 0;

		public static final int RCS_SERVER_HEARTBEAT_STATUS = 10;

		
		private static final Gson GSON = new Gson();
		
		private static final Gson HTML_ESCAPE_DISABLED_GSON = new GsonBuilder().disableHtmlEscaping().create();

		private static final String ERROR_STRING = "Error: {0}";
		
		public static String getJsonHtmlEscaped(Object source) {
			return HTML_ESCAPE_DISABLED_GSON.toJson(source);
		}
		
		public static <T> T getObject(String json, Class<T> classOfT) {
			return HTML_ESCAPE_DISABLED_GSON.fromJson(json, classOfT);
		}

		public static <T> T fromJson(JsonElement jsonElement, Class<T> classOfT) {
			return GSON.fromJson(jsonElement, classOfT);
		}
		
		public static <T> T getObject(Reader reader, Class<T> classOfT) {
			return GSON.fromJson(reader, classOfT);
		}
		

	

		

		/**
		 * Used for endian conversion.
		 * @param v an integer.
		 * @return the integer in reverse form ie., the bytes are swapped.
		 */
		public static int swap(int v) {

			return  (v >>> 24) | (v << 24) | ((v << 8) & 0x00FF0000) 
					| ((v >> 8) & 0x0000FF00);

		}

		/**
		 * Used for endian conversion.
		 * @param v the short.
		 * @return the short in reverse form ie., the bytes are swapped.
		 */
		public static short swap(short v) {

			return  (short)(((v << 8) & 0xFF00) | ((v >> 8) & 0x00FF));

		}

		/**
		 * Used for endian conversion.
		 * @param v an long.
		 * @return the long in reverse form ie., the bytes are swapped.
		 */
		public static long swap(long v) {

			long b1 = (v >>  0) & 0xff;
			long b2 = (v >>  8) & 0xff;
			long b3 = (v >> 16) & 0xff;
			long b4 = (v >> 24) & 0xff;
			long b5 = (v >> 32) & 0xff;
			long b6 = (v >> 40) & 0xff;
			long b7 = (v >> 48) & 0xff;
			long b8 = (v >> 56) & 0xff;

			return b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 |
					b5 << 24 | b6 << 16 | b7 <<  8 | b8;

		}

		/**
		 * Return true if the provided class or any of its super classes
		 * implements the specified interface, otherwise false.
		 * 
		 * @param c the class.
		 * @param i the interface.
		 * @return true if the class implements the interface, otherwise false.
		 */
		public static boolean implementsInterface(Class c, Class i) {
			boolean ret = false;
			Class s = c;

			do {
				Class[] arr = s.getInterfaces();
				for(int j = 0; j < arr.length; j++) {
					if(arr[j].equals(i)) {
						ret = true;
						break;
					}
				}
				s = s.getSuperclass();
			} while(!ret && s != null);

			return ret;
		}

		/**
		 * Return true if the provided class is inherited from the specified
		 * super class. Note that this could happen anywhere in the chain. 
		 * 
		 * @param the class
		 * @param the super class
		 * @return true if the class inherits the super
		 */
		public static boolean extendsClass(Class c, Class e) {
			boolean ret = false;
			Class s = c;

			do {
				s = s.getSuperclass();
				if(s != null && s.equals(e)) {
					ret = true;
					break;
				}
			} while(s != null);

			return ret;
		}

		
		/**
		 * This method is used to parse a url and return the ip address
		 * from the given url.
		 * @author Tutai  Dalal Feb 2008
		 * @param url
		 * @return ipAddress
		 */
		public static String getIPAddress(String url){
			String ipAddress = "";
			char lastChar = 0;
			char presentChar;
			int startIndex = 0;
			int endIndex = 15;
			for (int i = 0; i < url.length(); i++) {
				presentChar = url.charAt(i);
				if(presentChar == '/' && lastChar == '/'){
					startIndex = i+1;
					break;
				}
				lastChar = presentChar;
			}

			for(int i = startIndex; i < url.length(); i++){
				if(url.charAt(i) == '/'){
					endIndex = i;
					break;
				}
			}
			ipAddress = url.substring(startIndex, endIndex);
			return ipAddress;
		}

		/**
		 * A method to truncate a double value to two decimal places.
		 * @param val The double value to be formatted.
		 * @return String value of the input double value.
		 */
		public static String format(double val) {
			val *= 1000;
			val = Math.round(val);
			val /= 1000;
			return String.valueOf(val);
		}
		
		

		synchronized public static int[] convertRGBBytestToRGBInt(byte rgbInBytes[], int outputImageW, int outputImageH) {

			int[] intsArray = new int[outputImageW * outputImageH];
			for (int j =0, index = 0; index < outputImageW * outputImageH; index ++) {
				intsArray[index] = (int)(0xFF000000 + (rgbInBytes[j] << 16) + (rgbInBytes[j + 1] << 8) + (rgbInBytes[j + 2]));
				j += 3;
			}

			return intsArray;
		}

		synchronized public static byte[] convertRGBIntToByteArray(int[] rgb, int inputImageW2, int inputImageH2) {
			byte[] outData = null;
			if (rgb != null) {
				outData = new byte[rgb.length * 3];
				int count = 0;
				for (int height = 0; height < inputImageH2 ; height++) {
					for (int width = 0; width < inputImageW2; width++) {
						int i = height*inputImageW2 + width; 
						byte alpha = (byte) (rgb[i] >> 24);
						//if((height > 10) && (width > 11) && (height + 10 < inputImageH2 ) && (width + 11 < inputImageW2))
						//outData[count++] = (byte)(((rgb[i] << 8) >> 24) | 0xF0);
						//else						
						outData[count++] = (byte)((rgb[i] << 8) >> 24);
						outData[count++] = (byte) ((rgb[i] << 16) >> 24);
						outData[count++] = (byte) ((rgb[i] << 24) >> 24);

					}
				}
			}

			return outData;
		}
		
		
		synchronized public static byte [] convertIntArrayToByteArray(int [] array) {
	        if (array == null || array.length <= 0) {
	            return new byte[0];
	        }

	        return writeInts(array);
	    }

		synchronized public static int [] convertByteArrayToIntArray(byte [] array) {
	        if (array == null || array.length <= 0) {
	            return new int[0];
	        }

	        return readInts(array);
	    }

	    private static byte[] writeInts(int [] array) {
	    	byte[] bytes = null;
	    	ByteArrayOutputStream bos = null;
	    	DataOutputStream dos = null;
	        try {
	            bos = new ByteArrayOutputStream(array.length * 4);
	            dos = new DataOutputStream(bos);
	            for (int i = 0; i < array.length; i++) {
	                dos.writeInt(array[i]);
	            }
	            bytes = bos.toByteArray();
	        } catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
	        } finally {
	        	if (dos != null) {try {dos.close();} catch (Exception e2) {}}
	        	if (bos != null) {try {bos.close();} catch (Exception e2) {}}
	        }
	        
	        return bytes;
	    }

	    private static int[] readInts(byte [] array) {
	    	int[] ints = null;
	    	ByteArrayInputStream bis = null;
	    	DataInputStream dis = null;
	        try {
	            bis = new ByteArrayInputStream(array);
	            dis = new DataInputStream(bis);
	            int size = array.length / 4;
	            ints = new int[size];
	            for (int i = 0; i < size; i++) {
	            	ints[i] = dis.readInt();
	            }
	        } catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
	        } finally {
	        	if (dis != null) {try {dis.close();} catch (Exception e2) {}}
	        	if (bis != null) {try {bis.close();} catch (Exception e2) {}}
	        }
	        
	        return ints;
	    }
	    
	    synchronized public static String storageSizeInGBToString(double sizeInGB) {
	    	return storageSizeInBytesToString((long) (sizeInGB * 1024 * 1024 * 1024));
	    }
	    
	    synchronized public static String storageSizeInBytesToStringWithoutDecimal(long sizeInBytes) {
	    	return storageSizeInBytesToString(sizeInBytes, new DecimalFormat("0"));
	    }
	    
	    synchronized public static String storageSizeInBytesToString(long sizeInBytes) {
	    	return storageSizeInBytesToString(sizeInBytes, new DecimalFormat("0.00"));
	    }
	    
	    synchronized public static String storageSizeInBytesToString(long sizeInBytes, DecimalFormat dec) {
	    	
	    	String storageSizeInStr = null;

	        double b = sizeInBytes;
	        double k = sizeInBytes/1024.0;
	        double m = ((sizeInBytes/1024.0)/1024.0);
	        double g = (((sizeInBytes/1024.0)/1024.0)/1024.0);
	        double t = ((((sizeInBytes/1024.0)/1024.0)/1024.0)/1024.0);
	        double p = (((((sizeInBytes/1024.0)/1024.0)/1024.0)/1024.0)/1024.0);

	        if (p > 1) {
	        	storageSizeInStr = dec.format(t).concat(" PB");
	        	
	        } else if (t > 1) {
	        	storageSizeInStr = dec.format(t).concat(" TB");
	        	
	        } else if (g > 1) {
	        	storageSizeInStr = dec.format(g).concat(" GB");
	        	
	        } else if (m > 1){
	        	storageSizeInStr = dec.format(m).concat(" MB");
	            
	        } else if (k > 1) {
	        	storageSizeInStr = dec.format(k).concat(" KB");
	            
	        } else {
	        	storageSizeInStr = dec.format(b).concat(" Bytes");
	            
	        }
	        
	        if (storageSizeInStr != null && storageSizeInStr.contains(".00")) {
	            storageSizeInStr = storageSizeInStr.replace(".00", "");
	        }

	        return storageSizeInStr;
		}

		synchronized public static long parseDateStringToLong(String startTime) {
			Date date = null;
			long millis = -1L;
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				date = (Date)formatter.parse(startTime);
				millis = date.getTime();
			} catch(Exception e) {}
			return millis;
		}

		synchronized public static String parseDateLongToString(long timeStamp) {
			String dateTime = null;
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd't'HHmmss");
				dateTime = formatter.format(new Date(timeStamp));
			} catch(Exception e) {}
			return dateTime;
		}

		
		
		synchronized public static String getFrameTypeStr(int frameType) {
			String frameTypeStr = "";

			switch (frameType) {
			case VMediaProperty.H_FRAME:
				frameTypeStr = "H_FRAME";
				break;

			case VMediaProperty.I_FRAME:
				frameTypeStr = "I_FRAME";
				break;

			case VMediaProperty.P_FRAME:
				frameTypeStr = "P_FRAME";
				break;

			case VMediaProperty.CONNECT_HEADER:
				frameTypeStr = "CONNECT_HEADER";
				break;

			case VMediaProperty.V_FRAME:
				frameTypeStr = "V_FRAME";
				break;

			case VMediaProperty.AUDIO_FRAME:
				frameTypeStr = "AUDIO_FRAME";
				break;

			default:
				frameTypeStr = "Unknown";
				break;
			}

			return frameTypeStr;
		}

		synchronized public static String getMediaTypeStr(int mediaType) {
			String mediaTypeStr = "";

			switch (mediaType) {
			case VMediaProperty.MJPG:
				mediaTypeStr = "MJPG";
				break;

			case VMediaProperty.MPEG:
				mediaTypeStr = "MPEG";
				break;

			case VMediaProperty.H264:
				mediaTypeStr = "H264";
				break;

			case VMediaProperty.PCMU:
				mediaTypeStr = "AUDIO_PCMU";
				break;

			case VMediaProperty.PCMA:
				mediaTypeStr = "AUDIO_PCMA";
				break;

			case VMediaProperty.L16:
				mediaTypeStr = "AUDIO_L16";
				break;

			case VMediaProperty.ACC:
				mediaTypeStr = "AUDIO_ACC";
				break;

			case VMediaProperty.UNKNOWN:
				mediaTypeStr = "UNKNOWN";
				break;

			case VMediaProperty.H265:
				mediaTypeStr = "H265";
				break;

			default:
				mediaTypeStr = "Unknown";
				break;
			}

			return mediaTypeStr;
		}

		synchronized public static int getMediaTypeInt(String mediaTypeStr) {
			int mediaType = VMediaProperty.UNKNOWN;

			if (mediaTypeStr.toUpperCase().contains("H264")) {
				mediaType = VMediaProperty.H264;

			} else if (mediaTypeStr.toUpperCase().contains("H265")) {
				mediaType = VMediaProperty.H265;

			} else if (mediaTypeStr.toUpperCase().contains("MPEG")) {
				mediaType = VMediaProperty.MPEG;

			} else if (mediaTypeStr.toUpperCase().contains("MJPG")) {
				mediaType = VMediaProperty.MJPG;

			} else if (mediaTypeStr.toUpperCase().contains("PCMU")) {
				mediaType = VMediaProperty.PCMU;

			} else if (mediaTypeStr.toUpperCase().contains("PCMA")) {
				mediaType = VMediaProperty.PCMA;

			} else if (mediaTypeStr.toUpperCase().contains("L16")) {
				mediaType = VMediaProperty.L16;

			} else if (mediaTypeStr.toUpperCase().contains("ACC")) {
				mediaType = VMediaProperty.ACC;

			} else if (mediaTypeStr.toUpperCase().contains("UNKNOWN")) {
				mediaType = VMediaProperty.UNKNOWN;

			}

			return mediaType;
		}

		synchronized public static boolean isValidMediaType(int mediaType) {
			return (mediaType == VMediaProperty.MJPG || mediaType == VMediaProperty.MPEG || mediaType == VMediaProperty.H264 || mediaType == VMediaProperty.PCMU
					|| mediaType == VMediaProperty.PCMA || mediaType == VMediaProperty.L16
					|| mediaType == VMediaProperty.ACC || mediaType == VMediaProperty.UNKNOWN
					|| mediaType == VMediaProperty.H265);
		}

		synchronized public static boolean isValidFrameType(int frameType) {
			return (frameType == VMediaProperty.H_FRAME || frameType == VMediaProperty.I_FRAME || frameType == VMediaProperty.P_FRAME
					|| frameType == VMediaProperty.CONNECT_HEADER || frameType == VMediaProperty.V_FRAME);
		}

		

		synchronized public static boolean isAudioFrame(int mediaType) {
			return (mediaType == VMediaProperty.PCMU || mediaType == VMediaProperty.PCMA || mediaType == VMediaProperty.L16 || mediaType == VMediaProperty.ACC
					|| mediaType == VMediaProperty.UNKNOWN);
		}

		

		synchronized public static String generateUUID() {
			String uuid = "";
			try {
				uuid = UUID.randomUUID().toString().toUpperCase();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
			}

			return uuid;
		}

		synchronized public static ArrayList<InetAddress> getAllAddressForThisMachine() {
			ArrayList<InetAddress> addressList = new ArrayList<InetAddress>();

			try {
				Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
				while (interfaces.hasMoreElements()) {
					NetworkInterface networkInterface = interfaces.nextElement();
					if (!networkInterface.isLoopback() && networkInterface.isUp()) {
						List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
						for (InterfaceAddress address : interfaceAddresses) {
							addressList.add(address.getAddress());
						}
					}
				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
			}

			return addressList;
		}

		
		
		public static long getStartedAt(String path) {

			long startedAt = -1;

			File pathFile = new File(path);

			if (pathFile.exists() && pathFile.canRead()) {
				DataInputStream dis = null;
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(pathFile);
					dis = new DataInputStream(fis);
					String dataStr = dis.readLine();
					while(dataStr != null){
						if(!dataStr.contains("#")){
							if(dataStr.contains("startedAt=")){
								String[] pathStrArray = dataStr.split("=");
								if (pathStrArray.length > 1) {
									startedAt = Long.parseLong(pathStrArray[1].trim());
								}
							}
						}
						dataStr = dis.readLine();
					}    

				} catch (Exception e) {

				} finally {
					if (dis != null) {try {dis.close();} catch (Exception e) {}}
					if (fis != null) {try {fis.close();} catch (Exception e) {}}
				}
			} else {
				LOGGER.log(Level.WARNING, "getStartedAt: {0} doesn't exist", pathFile.getAbsolutePath());
			}

			return startedAt;
		}

		public static long getUpdatedAt(String path) {

			long updatedAt = -1;

			File pathFile = new File(path);

			if (pathFile.exists() && pathFile.canRead()) {
				DataInputStream dis = null;
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(pathFile);
					dis = new DataInputStream(fis);
					String dataStr = dis.readLine();
					while(dataStr != null){
						if(!dataStr.contains("#")){
							if(dataStr.contains("updatedAt=")){
								String[] pathStrArray = dataStr.split("=");
								if (pathStrArray.length > 1) {
									updatedAt = Long.parseLong(pathStrArray[1].trim());
								}
							}
						}
						dataStr = dis.readLine();
					}    

				} catch (Exception e) {

				} finally {
					if (dis != null) {try {dis.close();} catch (Exception e) {}}
					if (fis != null) {try {fis.close();} catch (Exception e) {}}
				}
			} else {
				LOGGER.log(Level.WARNING, "getUpdatedAt: {0} doesn't exist", pathFile.getAbsolutePath());
			}

			return updatedAt;
		}

		public static long getStoppedAt(String path) {

			long stoppedAt = -1;

			File pathFile = new File(path);

			if (pathFile.exists() && pathFile.canRead()) {
				DataInputStream dis = null;
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(pathFile);
					dis = new DataInputStream(fis);
					String dataStr = dis.readLine();
					while(dataStr != null){
						if(!dataStr.contains("#")){
							if(dataStr.contains("stoppedAt=")){
								String[] pathStrArray = dataStr.split("=");
								if (pathStrArray.length > 1) {
									stoppedAt = Long.parseLong(pathStrArray[1].trim());
								}
							}
						}
						dataStr = dis.readLine();
					}    

				} catch (Exception e) {

				} finally {
					if (dis != null) {try {dis.close();} catch (Exception e) {}}
					if (fis != null) {try {fis.close();} catch (Exception e) {}}
				}
			} else {
				LOGGER.log(Level.WARNING, "getStoppedAt: {0} doesn't exist", pathFile.getAbsolutePath());
			}

			return stoppedAt;
		}

		public static String getDetails(String path) {

			String details = null;

			File pathFile = new File(path);

			if (pathFile.exists() && pathFile.canRead()) {
				DataInputStream dis = null;
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(pathFile);
					dis = new DataInputStream(fis);
					String dataStr = dis.readLine();
					while(dataStr != null){
						if(!dataStr.contains("#")){
							if(dataStr.contains("details=")){
								String[] pathStrArray = dataStr.split("=");
								if (pathStrArray.length > 1) {
									String temp = dataStr.substring(dataStr.indexOf("=") + 1).trim();
									details = temp;
								}
							}
						}
						dataStr = dis.readLine();
					}    

				} catch (Exception e) {
					LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
				} finally {
					if (dis != null) {try {dis.close();} catch (Exception e) {}}
					if (fis != null) {try {fis.close();} catch (Exception e) {}}
				}
			} else {
				LOGGER.log(Level.WARNING, "{0} doesn't exist", pathFile.getAbsolutePath());
			}

			return details;
		}

		public static VReturn updateProcessTimeInMs(String path, boolean justGotStarted) {
			VReturn vReturn = new VReturn();

			FileOutputStream fos = null;

			try {

				String startedAtStr = "startedAt=" + "-1";
				String updatedAtStr = "updatedAt=" + "-1";
				String stoppedAtStr = "stoppedAt=" + "-1";

				if (justGotStarted) {
					startedAtStr = "startedAt=" + System.currentTimeMillis();
					stoppedAtStr = "stoppedAt=" + getUpdatedAt(path);
					updatedAtStr = "updatedAt=" + System.currentTimeMillis();
					LOGGER.log(Level.INFO, "justGot started {0}", getUpdatedAt(path));
				} else {
					startedAtStr = "startedAt=" + getStartedAt(path);
					stoppedAtStr = "stoppedAt=" + getStoppedAt(path);
					updatedAtStr = "updatedAt=" + System.currentTimeMillis();
				}

				fos = new FileOutputStream(path);
				fos.write((startedAtStr + "\n" + updatedAtStr + "\n" + stoppedAtStr).getBytes());
				vReturn.setReturnValue(VReturn.SUCCESS);
			} catch (Exception ex) {
				LOGGER.log(Level.WARNING, "updateProcessTimeInMs: Error {0}", ex.toString());
				vReturn.setReturnValue(VReturn.SSC_FILE_NOT_FOUND);
			} finally {
				if (fos != null) {
					try {fos.close();} catch (IOException e) {}
				}
			}

			return vReturn;
		}

		

		synchronized public static VReturn rebootAllProcessBeingHandeledByWatchDog(String source) {

			VReturn vReturn = new VReturn();

			//		Socket socket = null;
			//		DataInputStream dis = null;
			//		DataOutputStream dos = null;
			//		boolean success = true;
			//
			//		if (success) { 
			//			try {
			//				socket = new Socket("localhost", VPorts.LICENSE_SERVER_PORT);
			//				socket.setSoTimeout(10000);
			//				dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			//				dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			//			} catch (Exception e) {
			//				vReturn.setReturnValue(VReturn.WATCHDOG_CONNECTION_ERROR);
			//				vReturn.setMessage(e.toString());
			//				success = false;
			//			}
			//		}
			//
			//		if (success) { 
			//			try {
			//				dos.writeShort(9);//rebootAllProcess callId = 9, getLicence callId = 2, getFileExchnagerDetails callId = 10;
			//				dos.writeInt(0);
			//				dos.flush();
			//			} catch (IOException e) {
			//				vReturn.setReturnValue(VReturn.WATCHDOG_CONNECTION_ERROR);
			//				vReturn.setMessage(e.toString());
			//				success = false;
			//			}
			//		}
			//
			//		if (success) { 
			//			vReturn.setReturnValue(VReturn.SUCCESS);
			//		}
			//
			//		try {
			//			dis.close();
			//		} catch (Exception e) {}
			//
			//		try {
			//			dos.close();
			//		} catch (Exception e) {}
			//
			//		try {
			//			socket.close();
			//		} catch (Exception e) {}

			vReturn.setReturnValue(VReturn.SUCCESS);
			Object [] params = new Object[] {source , new Date(), vReturn.getReturnValue()};
			LOGGER.log(Level.WARNING, "***********rebootAllProcessBeingHandeledByWatchDog called from {0} at {1} vReturn {2}", params);
			System.exit(0);
			return vReturn;
		}

		synchronized public static final ArrayList<File> listFilesAndDirectory(String path, ArrayList<File> files) {

			if (files == null) {
				files = new ArrayList<File>();
			}

			File root = new File(path);
			if (root.isDirectory()) {
				File[] list = root.listFiles();
				if (list != null) {
					for (File file : list) {
						if (file.isDirectory()) {
							if (!files.contains(file)) {files.add(file);}
							listFilesAndDirectory(file.getAbsolutePath(), files);
						} else {
							files.add(file);
						}
					}
				}
			} else if (root.exists()) {
				files.add(root);
			}

			return files;
		}

		synchronized public static final ArrayList<VFile> listVFilesAndDirectory(String path, ArrayList<VFile> files) {

			if (files == null) {
				files = new ArrayList<VFile>();
			}

			File root = new File(path);
			if (root.isDirectory()) {
				File[] list = root.listFiles();
				if (list != null) {
					for (File file : list) {
						if (file.isDirectory()) {
							if (!files.contains(new VFile(file.getPath()))) {
								files.add(new VFile(file.getPath(), file.lastModified(), file.lastModified(),
										file.lastModified(), file.isDirectory(), file.length(), ""));
								}
							listVFilesAndDirectory(file.getAbsolutePath(), files);
						} else {
							files.add(new VFile(file.getPath(), file.lastModified(), file.lastModified()
									, file.lastModified(), file.isDirectory(), file.length(), ""));
						}
					}
				}
			} else if (root.exists()) {
				files.add(new VFile(root.getPath(), root.lastModified(), root.lastModified()
						, root.lastModified(), root.isDirectory(), root.length(), ""));
			}

			return files;
		}

		synchronized public static final ArrayList<File> listVideoFolders(String path, ArrayList<File> videoFolders) {

			if (videoFolders == null) {
				videoFolders = new ArrayList<File>();
			}

			File root = new File(path);
			if (root.isDirectory()) {
				File[] list = root.listFiles();
				if (list != null) {
					for (File file : list) {
						if (file.isDirectory()) {

							boolean isItAVideoFolder = false;
							File[] files = file.listFiles();
							if (files != null) {
								for (int i = 0; i < files.length; i++) {
									File f = files[i];
									isItAVideoFolder = f.getName().toUpperCase().endsWith(".AVF") || 
											f.getName().toUpperCase().endsWith(".AVI") ||
											f.getName().toUpperCase().endsWith(".MJPG") ||
											f.getName().toUpperCase().endsWith(".MP4") ||
											f.getName().toUpperCase().endsWith(".JPEG") ||
											f.getName().toUpperCase().endsWith(".JPG");

									if (!isItAVideoFolder) {
										break;
									}
								}
							}

							if (isItAVideoFolder) {
								//							System.err.println("file " + file);
								videoFolders.add(file);
							}

							listVideoFolders(file.getAbsolutePath(), videoFolders);
						}
					}
				}
			}

			return videoFolders;
		}

		synchronized public static VReturn sendHttpCommand(String httpUrl, String userName, String password) {

			VReturn vReturn = new VReturn();

			HttpURLConnection huc = null;
			DataInputStream in = null;


			try {
				Object [] params = new Object[] {httpUrl , userName, password};
				LOGGER.log(Level.WARNING, "httpUrl {0} userName {1} password {2}", params);
				URL url = new URL(httpUrl);			
				huc = (HttpURLConnection)url.openConnection();
				if (huc != null) {
					if (userName != null && password != null) {
						Authenticator.setDefault(new MyAuthenticator(userName, password));
					}
					huc.setDoInput(true);
					huc.connect();
					in  = new DataInputStream(new BufferedInputStream(huc.getInputStream()));

					vReturn.setReturnValue(VReturn.SUCCESS);
				} else {
					LOGGER.log(Level.WARNING, "Unable to open url= {0}", httpUrl);
				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
				vReturn.setReturnValue(VReturn.INVALID_USER_PASSWORD);
				vReturn.setResult(e.toString());
			} finally { 
				if (in != null) {
					try {in.close();} catch (Exception e) {}
				}

				if (huc != null) {
					try {huc.disconnect();} catch (Exception e) {}
				}
			}

			return vReturn;
		}

		private static class MyAuthenticator extends Authenticator {
			private String vtplUn = "INFINOVA";
			private String vtplPk = "INFINOVA";

			public MyAuthenticator(String username, String password) {
				this.vtplUn = username;
				this.vtplPk = password;
			}

			// This method is called when a password-protected URL is accessed
			protected
			PasswordAuthentication getPasswordAuthentication() {
				// Get information about the request
				String promptString = getRequestingPrompt();
				String hostname = getRequestingHost();
				InetAddress ipaddr = getRequestingSite();
				int port = getRequestingPort();

				// Return the information
				return new PasswordAuthentication(vtplUn, vtplPk.toCharArray());
			}
		}



		synchronized public static boolean isReachable(String host, int timeOutInMs) {
			try {
				InetAddress addr = InetAddress.getByName(host);
				if (VUtilities.isValidIP(addr.getHostAddress())) {
					return addr.isReachable(timeOutInMs);
				}
				
			} catch (Exception e) {}

			return false;
		}

		synchronized public static boolean isIt64BitSystem() {

			boolean is64bit = false;
			try {
				if (System.getProperty("os.name").contains("Windows")) {
					is64bit = (System.getenv("ProgramFiles(x86)") != null);
				}else if(System.getProperty("os.name").contains("Linux")){
					is64bit = false;
				}else {

					is64bit = (System.getProperty("os.arch").indexOf("64") != -1);
				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
			}

			return is64bit; 
		}

		

		private static void saveImageFromByte(byte[] imageInByte, String imagePath){
			InputStream in = null;
			try {
				in = new ByteArrayInputStream(imageInByte);
				BufferedImage bImageFromConvert = ImageIO.read(in);
				ImageIO.write(bImageFromConvert, "jpg", new File(imagePath));
			} catch (Exception e) {}
			finally {try {if (in != null) { in.close(); }} catch (Exception e2) {}
			}
		}

		private static byte[] getByteArrayFromImage(String imagePath) {
			byte[] imageInByte = null;
			ByteArrayOutputStream baos = null;
			try {
				BufferedImage originalImage = ImageIO.read(new File(imagePath));
				baos = new ByteArrayOutputStream();
				ImageIO.write(originalImage, "jpg", baos);
				baos.flush();
				imageInByte = baos.toByteArray();
			} catch (Exception e) {}
			finally {
				try {if (baos != null) {baos.close();}} catch (Exception e2) {}
			}
			return imageInByte;
		}

		

		synchronized public static boolean isProcessAlive(Process process) {

			boolean isAlive = false;

			try {
				process.exitValue();
			} catch (IllegalThreadStateException e) {
				isAlive = true;
			} catch (Exception e) {
				isAlive = false;
			}

			return isAlive; 
		}

		

		synchronized public static void zipFolder(String srcFolder, String destZipFile) throws Exception {
			ZipOutputStream zip = null;
			FileOutputStream fileWriter = null;

			fileWriter = new FileOutputStream(destZipFile);
			zip = new ZipOutputStream(fileWriter);

			addFolderToZip("", srcFolder, zip);
			zip.flush();
			zip.close();
			fileWriter.close();
			Object [] params = new Object[] {srcFolder , destZipFile};
			LOGGER.log(Level.INFO, "{0}  is zipped to archive {1}", params);
		}

		 public static synchronized void zipFile(String inputFilePath, String zipFilePath) throws Exception {

			 try(FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)
					 ;FileInputStream fileInputStream = new FileInputStream(inputFilePath);) {
				 
				 	ZipEntry zipEntry = new ZipEntry(inputFilePath);
					zipOutputStream.putNextEntry(zipEntry);
					byte[] buf = new byte[1024];
					int bytesRead;

					while ((bytesRead = fileInputStream.read(buf)) > 0) {
						zipOutputStream.write(buf, 0, bytesRead);
					}

					zipOutputStream.closeEntry();
				
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Error: {0}", e.getMessage());
			}
			
			
		}

		private static synchronized void addFileToZip(String path, String srcFile, ZipOutputStream zip)
				throws Exception {

			File folder = new File(srcFile);
			if (folder.isDirectory()) {
				addFolderToZip(path, srcFile, zip);
			} else {
				byte[] buf = new byte[1024];
				int len;
				try(FileInputStream in = new FileInputStream(srcFile)) {
					zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
					while ((len = in.read(buf)) > 0) {
						zip.write(buf, 0, len);
					}
				}
				
				
			}
		}

		synchronized private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
				throws Exception {
			File folder = new File(srcFolder);

			for (String fileName : folder.list()) {
				if (path.equals("")) {
					addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
				} else {
					addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
				}
			}
		}

		

		 public static synchronized BufferedImage generateQRCode(String qrCodeText, int size) {
			BufferedImage qrImage = null;

			try {/*
				Hashtable hintMap = new Hashtable();
				hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
				QRCodeWriter qrCodeWriter = new QRCodeWriter();
				BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
						BarcodeFormat.QR_CODE, size, size, hintMap);
				// Make the BufferedImage that are to hold the QRCode
				int matrixWidth = byteMatrix.getWidth();
				qrImage = new BufferedImage(matrixWidth, matrixWidth,
						BufferedImage.TYPE_INT_RGB);
				qrImage.createGraphics();

				Graphics2D graphics = (Graphics2D) qrImage.getGraphics();
				graphics.setColor(Color.WHITE);
				graphics.fillRect(0, 0, matrixWidth, matrixWidth);
				// Paint and save the image using the ByteMatrix
				graphics.setColor(Color.BLACK);

				for (int i = 0; i < matrixWidth; i++) {
					for (int j = 0; j < matrixWidth; j++) {
						if (byteMatrix.get(i, j)) {
							graphics.fillRect(i, j, 1, 1);
						}
					}
				}
				graphics.dispose();
			 */} catch (Exception e) {}

			return qrImage;
		}

		 public static synchronized String readQRCode(BufferedImage qrImage) {
			String qrCodeText = null;

			try {/*
				Map hintMap = new HashMap();
				hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
				BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
						new BufferedImageLuminanceSource(qrImage)));
				Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
						hintMap);
				qrCodeText = qrCodeResult.getText();
			 */} catch (Exception e) {}

			return qrCodeText;
		}

		 public static synchronized BufferedImage rotateImage(BufferedImage image, double angle) {
			BufferedImage result = null;
			try {
				int w = image.getWidth(), h = image.getHeight();
				GraphicsConfiguration gc = getDefaultConfiguration();
				result = gc.createCompatibleImage(w, h);
				Graphics2D g = result.createGraphics();
				g.rotate(Math.toRadians(angle), (double) w / 2, (double) h / 2);
				g.drawRenderedImage(image, null);
				g.dispose();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
			}
			return result;
		}

		 private static synchronized GraphicsConfiguration getDefaultConfiguration() {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			return gd.getDefaultConfiguration();
		}

		 private static synchronized AffineTransform findTranslation(AffineTransform at, BufferedImage bi) {
			Point2D p2din, p2dout;

			p2din = new Point2D.Double(0.0, 0.0);
			p2dout = at.transform(p2din, null);
			double ytrans = p2dout.getY();

			p2din = new Point2D.Double(0, bi.getHeight());
			p2dout = at.transform(p2din, null);
			double xtrans = p2dout.getX();

			AffineTransform tat = new AffineTransform();
			tat.translate(-xtrans, -ytrans);
			return tat;
		}

		 public static synchronized String macIdToPlainText(String macId) {
			if (macId != null) {
				macId = macId.replace(":", "").toUpperCase();
			}

			return macId;
		}

		 public static synchronized boolean deleteDirectory(String dirPath) {
			try {
				File dir = new File(dirPath);
				if (!dir.exists()) {
					return true;
				}

				deleteFile(dir);
				return true;

			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
			}

			return false;
		}

		 public static synchronized boolean deleteFile(File file) throws IOException {

			if (!file.exists())
				return false;

			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					deleteFile(f);
				}
			}

			boolean rs = file.delete();
			rs = file.delete();

			return true;
			//		System.out.println("Deleted file/folder: " + file.getAbsolutePath());
		}

		synchronized public static boolean deleteDirectoryContents(String dirPath) {

			try {
				File file = new File(dirPath);
				if (!file.exists())
					return false;

				if (file.isDirectory()) {
					for (File f : file.listFiles()) {
						deleteFile(f);
					}
				}
				return true;
			} catch (Exception e) {
				return false;
			}
			//		System.out.println("Deleted file/folder: " + file.getAbsolutePath());
		}

		synchronized public static String getRunningFolderName() {
			String runningFolder = null;

			try {
				String runningJarFolderName = new File(".").getAbsolutePath().replace("\\", "/");
				runningJarFolderName = runningJarFolderName.replace("/.", "");
				runningJarFolderName = runningJarFolderName.substring(runningJarFolderName.lastIndexOf("/") + 1);
				runningFolder = runningJarFolderName;
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
			}

			return runningFolder;
		}

		synchronized public static String getRunningFolderPath() {
			String runningFolderPath = null;

			try {
				String runningJarFolderName = new File(".").getAbsolutePath().replace("\\", "/");
				runningJarFolderName = runningJarFolderName.replace("/.", "");
				runningFolderPath = runningJarFolderName;
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
			}

			return runningFolderPath;
		}

		

		synchronized public static void rebootMachine() {

			if (System.getProperty("os.name").contains("Windows")) {

				if (!rebootMachine("shutdown -r -t 0"))
					if (!rebootMachine("shutdown /r /f"))
						if (!rebootMachine("shutdown /r /f /t 0"))
							if (!rebootMachine("cmd /c shutdown /r /f"))
								if (!rebootMachine("cmd /c shutdown /r /f /t 0"))
									if (!rebootMachine("cmd /C shutdown /r /f"))
										if (!rebootMachine("cmd /C shutdown /r /f /t 0"))
											if (!rebootMachine("cmd /d shutdown /r /f"))
												if (!rebootMachine("cmd /d shutdown /r /f /t 0"))
													if (!rebootMachine("cmd /D shutdown /r /f"))
														if (!rebootMachine("cmd /D shutdown /r /f /t 0"))
															if (!rebootMachine("cmd /e shutdown /r /f"))
																if (!rebootMachine("cmd /e shutdown /r /f /t 0"))
																	if (!rebootMachine("cmd /E shutdown /r /f"))
																		if (!rebootMachine("cmd /E shutdown /r /f /t 0")) {}

			} else if(System.getProperty("os.name").contains("Linux")){
				if (!rebootMachine("reboot"))
					if (!rebootMachine("sudo reboot"))
						rebootMachine("sudo su reboot");

			}
		}

		synchronized private static boolean rebootMachine(String rebootCommand) {
			try {
				ProcessBuilder pb = new ProcessBuilder(rebootCommand);
				final Process process = pb.start();
				return true;
			} catch (IOException e) {
				try {
					Runtime r = Runtime.getRuntime();
					final Process process = r.exec(rebootCommand);
					return true;
				} catch (Exception e2) {
				}
			} finally {
			}

			return false;
		}

		synchronized public static boolean isValidIP(String ip) {

			boolean isValidIP = false;

			try {
				ip = ip.trim();
				String IPADDRESS_PATTERN =
						"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
								"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

				Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
				Matcher matcher = pattern.matcher(ip);
				isValidIP = matcher.matches();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
			}

			return isValidIP;
		}

		synchronized public static boolean isValidPort(String port) {

			boolean isValidPort = false;

			try {
				port = port.trim();
				int portInt = Integer.parseInt(port);
				if(portInt <= 0 || portInt > 65535){  
					isValidPort = false;
				} else {
					isValidPort = true;
				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
			}

			return isValidPort;
		}

		public static String convertHexToBinary(String hex) {
			try {
				return Integer.toBinaryString(Integer.parseInt(hex, 16));
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Failed to convert hex to binary");
			}
			return null;
		}

		public static String convertBinaryToHex(String binary) {
			try {
				String hexStr = Integer.toString(Integer.parseInt(binary, 2),16);
				String hexStrTemp = hexStr;
				for (int i = 0; i < 2 - hexStr.length(); i++) {
					hexStrTemp = "0" + hexStrTemp;
				}
				return hexStrTemp.toUpperCase();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Failed to convert hex to binary");
			}

			return null;
		}

		
		
		public static final ArrayList<Long> getSpeedEvidanceTimeStampFromImage(byte[] jpegBytes) {
			ArrayList<Long> speedTimeStampList =  new ArrayList<Long>();

			EventDataInImage eventDataInImage = getEventDataInImage(jpegBytes);
			if (eventDataInImage != null) {
				if (eventDataInImage.getEvidenceTimeStamp_1() > 0) {
					speedTimeStampList.add(eventDataInImage.getEvidenceTimeStamp_1());
				}

				if (eventDataInImage.getEvidenceTimeStamp_2() > 0) {
					speedTimeStampList.add(eventDataInImage.getEvidenceTimeStamp_2());
				}

				if (eventDataInImage.getEvidenceTimeStamp_3() > 0) {
					speedTimeStampList.add(eventDataInImage.getEvidenceTimeStamp_3());
				}
			}

			return speedTimeStampList;
		}

		public static final byte[] setSpeedEvidanceTimeStampToImage(byte[] jpegBytes, ArrayList<Long> speedTimeStampList) {

			EventDataInImage eventDataInImage = new EventDataInImage();

			for (int i = 0; i < speedTimeStampList.size(); i++) {
				if (i == 0) {
					eventDataInImage.setEvidenceTimeStamp_1(speedTimeStampList.get(i));
				} else if (i == 1) {
					eventDataInImage.setEvidenceTimeStamp_2(speedTimeStampList.get(i));
				} else if (i == 2) {
					eventDataInImage.setEvidenceTimeStamp_3(speedTimeStampList.get(i));
				}
			}

			return setEventDataInImageToImage(jpegBytes, eventDataInImage);
		}

		public static final byte[] setEventDataInImageToImage(byte[] jpegBytes, EventDataInImage eventDataInImage) {

			int extraBytesSize = 64;//total 64 extra bytes in image after FFD9

			try {
				if (jpegBytes != null && eventDataInImage != null) {
					java.nio.ByteBuffer rectBufferTemp = ByteBuffer.allocate(jpegBytes.length + extraBytesSize).order(ByteOrder.BIG_ENDIAN);
					rectBufferTemp.position(0);
					rectBufferTemp.put(jpegBytes);

					rectBufferTemp.putShort(eventDataInImage.getTopLeftCol());
					rectBufferTemp.putShort(eventDataInImage.getTopLeftRow());
					rectBufferTemp.putShort(eventDataInImage.getButtomRightCol());
					rectBufferTemp.putShort(eventDataInImage.getButtomRightRow());

					rectBufferTemp.putLong(eventDataInImage.getEvidenceTimeStamp_1());
					rectBufferTemp.putShort(eventDataInImage.getTopLeftCol_1());
					rectBufferTemp.putShort(eventDataInImage.getTopLeftRow_1());
					rectBufferTemp.putShort(eventDataInImage.getButtomRightCol_1());
					rectBufferTemp.putShort(eventDataInImage.getButtomRightRow_1());

					rectBufferTemp.putLong(eventDataInImage.getEvidenceTimeStamp_2());
					rectBufferTemp.putShort(eventDataInImage.getTopLeftCol_2());
					rectBufferTemp.putShort(eventDataInImage.getTopLeftRow_2());
					rectBufferTemp.putShort(eventDataInImage.getButtomRightCol_2());
					rectBufferTemp.putShort(eventDataInImage.getButtomRightRow_2());

					rectBufferTemp.putLong(eventDataInImage.getEvidenceTimeStamp_3());
					rectBufferTemp.putShort(eventDataInImage.getTopLeftCol_3());
					rectBufferTemp.putShort(eventDataInImage.getTopLeftRow_3());
					rectBufferTemp.putShort(eventDataInImage.getButtomRightCol_3());
					rectBufferTemp.putShort(eventDataInImage.getButtomRightRow_3());

					rectBufferTemp.putInt(eventDataInImage.getRgbWidth());
					rectBufferTemp.putInt(eventDataInImage.getRgbHeight());

					rectBufferTemp.position(0);
					jpegBytes = new byte[rectBufferTemp.capacity()];
					rectBufferTemp.get(jpegBytes);
				}
			} catch (Exception e1) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e1.toString());
			}

			return jpegBytes;
		}

		public static final EventDataInImage getEventDataInImage(byte[] jpegBytes) {

			EventDataInImage eventDataInImage = null;

			int extraBytesSize = 64;//total 64 extra bytes in image after FFD9
			int sizeWithFFD9 = extraBytesSize + 2;//2 is for FF and D9

			try {
				if (jpegBytes != null && jpegBytes.length > sizeWithFFD9) {
					int ffIndex = jpegBytes.length - sizeWithFFD9;
					if (jpegBytes[ffIndex] == (byte)0xFF && jpegBytes[ffIndex + 1] == (byte)0xD9) {

					} else {
						for (int i = jpegBytes.length - 1; i >= 1; i--) {
							if (jpegBytes[i] == (byte)0xD9 && jpegBytes[i - 1] == (byte)0xFF) {
								ffIndex = i - 1;
								break;
							}
						}
					}

					if(jpegBytes[ffIndex] == (byte)0xFF && jpegBytes[ffIndex + 1] == (byte)0xD9) { 
						java.nio.ByteBuffer rectBuffer = ByteBuffer.allocate(extraBytesSize).order(ByteOrder.BIG_ENDIAN);
						rectBuffer.position(0);
						rectBuffer.put(jpegBytes, ffIndex + 2, rectBuffer.capacity());
						rectBuffer.position(0);

						eventDataInImage = new EventDataInImage();
						eventDataInImage.setTopLeftCol(rectBuffer.getShort());
						eventDataInImage.setTopLeftRow(rectBuffer.getShort());
						eventDataInImage.setButtomRightCol(rectBuffer.getShort());
						eventDataInImage.setButtomRightRow(rectBuffer.getShort());

						eventDataInImage.setEvidenceTimeStamp_1(rectBuffer.getLong());
						eventDataInImage.setTopLeftCol_1(rectBuffer.getShort());
						eventDataInImage.setTopLeftRow_1(rectBuffer.getShort());
						eventDataInImage.setButtomRightCol_1(rectBuffer.getShort());
						eventDataInImage.setButtomRightRow_1(rectBuffer.getShort());

						eventDataInImage.setEvidenceTimeStamp_2(rectBuffer.getLong());
						eventDataInImage.setTopLeftCol_2(rectBuffer.getShort());
						eventDataInImage.setTopLeftRow_2(rectBuffer.getShort());
						eventDataInImage.setButtomRightCol_2(rectBuffer.getShort());
						eventDataInImage.setButtomRightRow_2(rectBuffer.getShort());

						eventDataInImage.setEvidenceTimeStamp_3(rectBuffer.getLong());
						eventDataInImage.setTopLeftCol_3(rectBuffer.getShort());
						eventDataInImage.setTopLeftRow_3(rectBuffer.getShort());
						eventDataInImage.setButtomRightCol_3(rectBuffer.getShort());
						eventDataInImage.setButtomRightRow_3(rectBuffer.getShort());

						eventDataInImage.setRgbWidth(rectBuffer.getInt());
						eventDataInImage.setRgbHeight(rectBuffer.getInt());

						//					System.out.println(eventDataInImage);

					}
				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "exception in getEventDataInImage() {0}", e.toString());
			}

			return eventDataInImage;
		}

		public static final byte[] trimJPEG(byte[] jpegBytes) {

			byte[] trimedJPEGBytes = jpegBytes;
			try {
				int from = -1;
				int to = 0;
				for (int i = 0; i < jpegBytes.length; i++) {

					if(from == -1 && jpegBytes[i] == (byte)0xFF && jpegBytes[i + 1] == (byte)0xD8) { 
						from = i;
					}

					if(jpegBytes[i] == (byte)0xFF && jpegBytes[i + 1] == (byte)0xD9) { 
						to = (i + 2);
					}
				}

				trimedJPEGBytes = Arrays.copyOfRange(jpegBytes, from, to);
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "trimJPEG " + ERROR_STRING, e.toString());
			}

			return trimedJPEGBytes;
		}

		synchronized public static void VTPL_EXIT(String className) {
			VTPL_EXIT(className, 0);
		}


		synchronized public static void exitAfterSleep(final String className, final long interval) {
			Thread td = new Thread() {
				@Override
				public void run() {
					try { Thread.sleep(interval); } catch (InterruptedException e) { Thread.currentThread().interrupt();}
					VTPL_EXIT(className, 0);
				}
			};
			td.start();

		}


		synchronized public static void VTPL_EXIT(String className, int exitStatus) {
			Object [] params = new Object[] {className, exitStatus};
			LOGGER.log(Level.WARNING, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! VTPL_EXIT called from {0} exitStatus {1}", params);
			System.exit(exitStatus);
		}



		static Cipher ecipher;
		static Cipher dcipher;
		static // 8-byte Salt
		byte[] salt = {
				(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
				(byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
		};
		// Iteration count
		static int iterationCount = 19;

		
		

		 public static synchronized boolean executeJAR(String executablePath) {

			Process runtimeProcess = null;

			String javaPath = "java";

			try {
				String[] command = {javaPath, "-jar", executablePath};
				if(command[0].contains("java")) {
					runtimeProcess = Runtime.getRuntime().exec(command);
				}
			} catch (IOException e1) {
				try {

					if (SystemUtils.IS_OS_WINDOWS) {
						javaPath = "../jre/bin/javavideonetics.exe";

					} else if (SystemUtils.IS_OS_LINUX) {
						javaPath = "../jre/bin/javavideonetics";

					}

					Object [] params = new Object[] {javaPath, new File(javaPath).exists()};
					LOGGER.log(Level.WARNING, "Exist {0} {1}", params);

					String[] command = {javaPath, "-jar", executablePath};
					if(command[0].contains("../jre")) {
					runtimeProcess = Runtime.getRuntime().exec(command);
					}
					
					LOGGER.log(Level.WARNING, "runtimeProcess {0}", runtimeProcess);
					
				} catch (IOException e2) {

					try {

						if (SystemUtils.IS_OS_WINDOWS) {
							javaPath = "./jre/bin/javavideonetics.exe";

						} else if (SystemUtils.IS_OS_LINUX) {
							javaPath = "./jre/bin/javavideonetics";

						}

						Object [] params = new Object[] {javaPath, new File(javaPath).exists()};
						LOGGER.log(Level.WARNING, "Exist {0} {1}", params);

						String[] command = {javaPath, "-jar", executablePath};
						if(command[0].contains("../jre")) {
						runtimeProcess = Runtime.getRuntime().exec(command);
						}
						LOGGER.log(Level.WARNING, "runtimeprocess {0}", runtimeProcess);
					} catch (IOException e3) {
						LOGGER.log(Level.WARNING, ERROR_STRING, e3.toString());
					}
				}
			}

			if (runtimeProcess != null) {
				LOGGER.log(Level.INFO, "Executed VJAR successfully. path: {0}", executablePath);
				return true;
			} else {
				LOGGER.log(Level.INFO, "Failed to execute VJAR. path: {0}", executablePath);
				return false;
			}
		}

		public static synchronized boolean executeSystemCommand(String systemCommand) {

			
			Process runtimeProcess = null;

			try {
				String[] command = {systemCommand};
				runtimeProcess = Runtime.getRuntime().exec(command);
			} catch (IOException e1) {
				try {

					String[] command = {"sudo", systemCommand};
					if(command[0].contains("sudo")) {
						runtimeProcess = Runtime.getRuntime().exec(command);
					}
				} catch (IOException e2) {

					try {
						String[] command = {"sudo su", systemCommand};
						if(command[0].contains("sudo su")) {
							runtimeProcess = Runtime.getRuntime().exec(command);
						}
					} catch (IOException e3) {
	    				LOGGER.log(Level.WARNING, ERROR_STRING, e3.toString());
					}
				}
			}
			
			boolean rs = false;

			if (runtimeProcess != null) {
				rs = true;
			}
			
			
			return rs;
		}


		public static String getTimeZoneOffsetForMySQL(String timezoneoffset) {

			if(timezoneoffset == null || timezoneoffset.length() == 0 || !StringUtils.contains(timezoneoffset, "GMT"))
				return null;

			timezoneoffset = StringUtils.remove(timezoneoffset, "GMT");
			String newString = ""; int index = 2;
			for (int i = 0; i < timezoneoffset.length(); i++) {
				newString += timezoneoffset.charAt(i);
				if (i == index) {  
					newString += ":"; 
				} 
			}
			return StringUtils.length(newString) == 6 ? (StringUtils.indexOf(newString, ":") == 3 ? newString : null) : null;
		}

		
		synchronized public static boolean isStrongPassword(String password) {
			try {
				return password.length() >= 8 && password.matches("^(?=.*([A-Z]){1,})(?=.*[!@#$&*]{1,})(?=.*[0-9]{1,})(?=.*[a-z]{1,}).{8,100}$");
			} catch (Exception e) {
				return false;
			}
		}
		
	

		synchronized public static Date getUTCdatetimeAsDate(String dateFormate) {
		    // note: doesn't check for null
		    return stringDateToDate(getUTCdatetimeAsString(dateFormate));
		}

		synchronized public static String getUTCdatetimeAsString(String dateFormate) {
		    final SimpleDateFormat sdf = new SimpleDateFormat(dateFormate);
		    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		    final String utcTime = sdf.format(new Date());

		    return utcTime;
		}

		synchronized public static Date stringDateToDate(String StrDate) {
		    Date dateToReturn = null;
		    SimpleDateFormat dateFormat = new SimpleDateFormat(StrDate);

		    try {
		        dateToReturn = (Date)dateFormat.parse(StrDate);
		    }
		    catch (ParseException e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
		    }

		    return dateToReturn;
		}

		
		synchronized public static boolean isOverlapping(VRectangle activityRectangle, VRectangle zoneRectangle, int sensitivity) {
		    
			boolean isOverlapping = false;
			
			boolean doesTopLeftLiesInZone = activityRectangle.getX1() >= zoneRectangle.getX1() && activityRectangle.getX1() <= zoneRectangle.getX2()
					&& activityRectangle.getY1() >= zoneRectangle.getY1() && activityRectangle.getY1() <= zoneRectangle.getY3();
					
			boolean doesTopRightLiesInZone = activityRectangle.getX2() >= zoneRectangle.getX1() && activityRectangle.getX2() <= zoneRectangle.getX2()
							&& activityRectangle.getY2() >= zoneRectangle.getY1() && activityRectangle.getY2() <= zoneRectangle.getY3();
					
			boolean doesBottomLeftLiesInZone = activityRectangle.getX4() >= zoneRectangle.getX4() && activityRectangle.getX4() <= zoneRectangle.getX3()
									&& activityRectangle.getY4() >= zoneRectangle.getY1() && activityRectangle.getY4() <= zoneRectangle.getY4();
									
			boolean doesBottomRightLiesInZone = activityRectangle.getX3() >= zoneRectangle.getX4() && activityRectangle.getX3() <= zoneRectangle.getX3()
							&& activityRectangle.getY3() >= zoneRectangle.getY2() && activityRectangle.getY3() <= zoneRectangle.getY3();
							
			int totalOverlappingCoordinateCount = 0;
			if (doesTopLeftLiesInZone) {
				totalOverlappingCoordinateCount++;
			}
			
			if (doesTopRightLiesInZone) {
				totalOverlappingCoordinateCount++;
			}
			
			if (doesBottomLeftLiesInZone) {
				totalOverlappingCoordinateCount++;
			}
			
			if (doesBottomRightLiesInZone) {
				totalOverlappingCoordinateCount++;
			}
			
							
							switch (sensitivity) {
							case 0://Low
								isOverlapping = totalOverlappingCoordinateCount >= 4;
								break;

							case 1://Medium
								isOverlapping = totalOverlappingCoordinateCount >= 3;
								break;

							case 2://High
								isOverlapping = totalOverlappingCoordinateCount >= 2;
								break;

							case 3://Higher
								isOverlapping = totalOverlappingCoordinateCount >= 1;
								break;

							default:
								break;
							}				
		    
							
		    return isOverlapping;
		}
		
		private static final String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		public static synchronized String getMonthName(int month) {
			try {
				return monthNames[month];
			} catch (Exception e) {
				return null;
			}
		}
		
		 public static synchronized String uploadFileHttp(String srcFilePath, String destFilePath, String baseUrl) throws Exception {

			String uploadedFilePath = null;
			
			try(CloseableHttpClient client = HttpClients.createDefault()) {
				
				
				HttpPost httpPost = new HttpPost(baseUrl);
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				builder.addBinaryBody(destFilePath, new File(srcFilePath), ContentType.DEFAULT_BINARY, destFilePath);
				// 
				HttpEntity entity = builder.build();
				httpPost.setEntity(entity);

				HttpResponse response = client.execute(httpPost);
				
				int rs = response.getStatusLine().getStatusCode();
				if (rs == HttpStatus.SC_OK || rs == HttpStatus.SC_CREATED) {
					
					JSONObject outputJson = new JSONObject(EntityUtils.toString(response.getEntity()).trim());
					
					JSONArray jsonArray = outputJson.getJSONArray("items");
					
					if (jsonArray != null && jsonArray.length() > 0) {
						JSONObject locationJson = jsonArray.getJSONObject(0);
						uploadedFilePath = locationJson.getString("Location");
					}
					
					
					
				} else {
					LOGGER.log(Level.WARNING, "Failed to upload file" + " \n rs {0}", rs);
				}
				
				client.close();
				
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
			}
			
			return uploadedFilePath;
		}
		
		synchronized public static boolean deleteFileHttp(String filePathToBeDeleted, String baseUrl) throws Exception {

			boolean deleteFile = false;
			
			try(CloseableHttpClient client = HttpClients.createDefault()) {
				
				
				HttpDelete httpDelete = new HttpDelete(baseUrl + "/" + filePathToBeDeleted);

				

				HttpResponse response = client.execute(httpDelete);
				
				int rs = response.getStatusLine().getStatusCode();
				if (rs == HttpStatus.SC_OK || rs == HttpStatus.SC_NO_CONTENT) {
					deleteFile = true;
				} else {
    				LOGGER.log(Level.WARNING, "Failed to delete file" + " \n rs {0}", rs);
				}
				
				client.close();
				
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
			}
			
			return deleteFile;
		}
		
		synchronized public static File downloadFileHttp(String filePathToBeDownloaded, File destFile, String baseUrl) throws Exception {

			File downloaded = null;
			
			try(CloseableHttpClient client = HttpClients.createDefault()) {
				
				
				HttpGet httpGet = new HttpGet(baseUrl + "/" + filePathToBeDownloaded);

				downloaded = client.execute(httpGet, new FileDownloadResponseHandler(destFile));
				
				
				client.close();
				
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
			}
			
			return downloaded;
		}
		
		static class FileDownloadResponseHandler implements ResponseHandler<File> {

			private final File destFile;

			public FileDownloadResponseHandler(File destFile) {
				this.destFile = destFile;
			}

			@Override
			public File handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				InputStream source = response.getEntity().getContent();
				FileUtils.copyInputStreamToFile(source, this.destFile);
				return this.destFile;
			}
		}
		
		
		public static String getFaceRegistrationDetailsByImage(String baseUrl,  String filename, String contentType, byte[] probeFileByte) {

			String restUrl =  baseUrl + "/internal/faceenrollment/featurevector";
			String newLine = "\r\n";
			String boundaryPrefix = "--";
			String boundary = "----" + UUID.randomUUID().toString();
			HttpURLConnection conn = null;
			String investigateRestResponse = null;
			try {

				URL postUrl = new URL(restUrl);
				if (postUrl.getProtocol().toLowerCase().equalsIgnoreCase("https")) {
					HttpsURLConnection https = (HttpsURLConnection) postUrl.openConnection();
					conn = https;
				} else {
					conn = (HttpURLConnection) postUrl.openConnection();
				}

				conn.setRequestMethod("POST");
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setConnectTimeout(15000);
				conn.setReadTimeout(2 * 60 * 1000);

				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("Charset", StandardCharsets.UTF_8.name());
				conn.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + boundary);
				OutputStream out = conn.getOutputStream();

				StringBuilder sb = new StringBuilder();

				sb.append(boundaryPrefix);
				sb.append(boundary);
				sb.append(newLine);

				sb.append("Content-Disposition: form-data; name=\"probe\"; filename=\""
						+ filename + "\"");
				sb.append(newLine);
				sb.append("Content-Type:" + URLConnection.guessContentTypeFromName(filename));
				sb.append(newLine);
				sb.append(newLine);

				out.write(sb.toString().getBytes());
				out.flush();
				out.write(probeFileByte);
				out.write(newLine.getBytes());
				byte[] endData = (newLine + boundaryPrefix + boundary + boundaryPrefix + newLine)
						.getBytes();
				out.write(endData);
				out.flush();
				out.close();
				int statusCode = conn.getResponseCode();
				InputStream inputStream;
				if (200 <= statusCode && statusCode <= 299) {
					inputStream = conn.getInputStream();
				} else {
					inputStream = conn.getErrorStream();
				}

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

				StringBuilder response = new StringBuilder();
				String currentLine;

				while ((currentLine = bufferedReader.readLine()) != null) 
					response.append(currentLine);
				
				if(statusCode == HttpStatus.SC_OK) {
					investigateRestResponse = response.toString();
				}
				

			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Failed to get face event details by Image. reason: {0}", e.toString());
			}

			return investigateRestResponse;
		}
		
		public static String enrolFaceRegistrationDetails(String baseUrl, String body) {

			String restUrl =  baseUrl + "/internal/faceenrollment/enroll";
			
			HttpURLConnection conn = null;
			String investigateRestResponse = null;
			try {

				URL postUrl = new URL(restUrl);
				if (postUrl.getProtocol().toLowerCase().equalsIgnoreCase("https")) {
					HttpsURLConnection https = (HttpsURLConnection) postUrl.openConnection();
					conn = https;
				} else {
					conn = (HttpURLConnection) postUrl.openConnection();
				}

				conn.setRequestMethod("POST");
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setConnectTimeout(15000);
				conn.setReadTimeout(2 * 60 * 1000);

				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("Charset", StandardCharsets.UTF_8.name());
				conn.setRequestProperty("Content-Type", "application/json");
				OutputStream out = conn.getOutputStream();

				out.write(body.getBytes());
				out.close();
				int statusCode = conn.getResponseCode();
				
				InputStream inputStream;
				if (200 <= statusCode && statusCode <= 299) {
					inputStream = conn.getInputStream();
				} else {
					inputStream = conn.getErrorStream();
				}

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

				StringBuilder response = new StringBuilder();
				String currentLine;

				while ((currentLine = bufferedReader.readLine()) != null) 
					response.append(currentLine);
				
				if(statusCode == HttpStatus.SC_OK) {
					investigateRestResponse = response.toString();
					
					LOGGER.log(Level.WARNING, "jsonObject= {0}", investigateRestResponse);

				}else {
					LOGGER.log(Level.WARNING, "1. Failed Register face. status code: {0}", statusCode);
					LOGGER.log(Level.WARNING, "2. Response: {0}", response.toString());
				}
				

			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Failed to register face, reason: {0}", e.toString());
			}

			return investigateRestResponse;
		}
		
	
		

	

	
	public static final int FILENAME_DATETIME = 1;
	/**
	 * Used for "kk:mm:ss.SSS" date format.
	 */
	public static final int LOG_TIMEONLY = 2;
	/**
	 * Used for "yyyy-MM-dd kk:mm:ss.SSS" date format.
	 */
	public static final int DATETIME_WITH_MILLIS = 3;
	/**
	 * Used for "yyyymmddkkmmssSS" date format.
	 */
	public static final int DATETIME_PACKED = 4;
	/**
	 * Used for "yyyy.MM.dd 'at' kk:mm:ss.SSS" date format.
	 */
	public static final int LONG_DATETIME = 5;

	/**
	 * Used for "yyyy-MM-dd" date format.
	 */
	public static final int LOG_DATEONLY = 6;
	/**
	 * Used for "yyyy.MM.dd_kk.mm.ss" date format.
	 */
	public static final int LOG_DATETIME = 7;
	/**
	 * Used for "MM-dd-yy" date format.
	 */
	public static final int LOG_DATESHORT = 8;
	/**
	 * Used for "kk:mm:ss" date format.
	 */
	public static final int LOG_TIMESHORT = 9;
	/**
	 * Used for "MM/dd/yyyy HH:mm:ss" date format.
	 */
	public static final int LOG_PRO_DATETIME = 10;
	/**
	 * Used for "yyyyMMddkkmmss" date format.
	 */
	public static final int FILENAME_DATETIME2 = 11;
	/**
	 * Used for "yyyy-MM-dd'T'HH:mm:ss'Z'" date format.
	 */
	public static final int DATETIME_WITH_ZONE = 12;

	/**
	 * Used for "E, dd MMM yyyy" date format.
	 */
	public static final int DATETIME_V_TIME_LINE = 13;

	/**
	 * Used for "YYYYMM-DDThh:mm:ss.sssZ" date format.
	 */
	public static final int DATETIME_FFT_FORMAT = 14;

	/**
	 * Used for "yyyy-MM-dd kk:mm:ss" date format.
	 */
	public static final int PSQL_TIMESTAMP = 15;

	/*
	 * Notes on date format strings: 1. Use kk (24hr) instead of HH (12hr) for
	 * hours; if you use HH you'll also need to include am/pm. 2. Avoid 'z' for the
	 * timezone. As so much of our work is historical and involving data from
	 * different timezones, this is confusing. If you need to display the timezone
	 * use the DisplayTimeZoneID string to indicate location.
	 */
	protected static String FileName_DateTime_Give = "yyyyMMdd_kkmmss";
	protected static String Log_TimeOnly_Give = "kk:mm:ss.SSS";
	protected static String DateTime_With_Millis_Give = "yyyy-MM-dd kk:mm:ss.SSS";
	protected static String DateTime_Packed_Give = "yyyymmddkkmmssSS";
	protected static String Long_DateTime_Give = "yyyy.MM.dd 'at' HH:mm:ss.SSS";
	protected static String pSQL_DateTime_Give = "yyyy-MM-dd kk:mm:ss";
	protected static String Log_DateOnly_Give = "yyyy-MM-dd";
	protected static String FileName_DateTime2_Give = "yyyyMMddkkmmss";
	protected static String DateTime_With_ZONE_Give = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	protected static String vTimeLine_DateTime_Give = "E, dd MMM yyyy";
	protected static String fft_DateTime_Give = "yyyyMMdd'T'HHmmss";

	// "yyyy.MM.dd_kk.mm.ss"
	protected static String Log_DateTime_Give = "dd/MM/yyyy HH:mm:ss";
	protected static String Log_DateShort_Give = "MM-dd-yy";
	protected static String Log_TimeShort_Give = "kk:mm:ss";
	protected static String Log_TimeProFormat_Give = "MM/dd/yyyy HH:mm:ss";
	private static final String ACTION_1 = "Error";
	private static final String ACTION_2 = "getMacAddress: ";
	private static final String ACTION_3 = "Failed to set hostname... Error ";
	private static final String OS_NAME = "os.name";
	private static final String OS_WINDOWS =  "Windows";
	private static final String CMD_NETSH ="netsh";
	private static final String ACTION_7 ="interface";
	private static final String ACTION_8 ="docker";
	private static final String ACTION_9 ="Failed to get nic name... Error ";
	private static final String ACTION_10 = "Linux";
	private static final String ACTION_11 = "address";
	private static final String ACTION_12 ="netmask";
	private static final String ACTION_13="gateway";
	private static final String ACTION_14="iface";
	private static final String ACTION_15=" dev ";
	private static final String ACTION_16=" unknown os";
	private static final String ACTION_17="name=";
	private static final String ACTION_18= "Failed to get NICConfiguration... Error ";
	private static final String ACTION_19 =  " src ";
	private static final String ACTION_20 = " via ";
	private static final String ACTION_21 = " proto ";
	private static final String ACTION_22 =  "process return ";
	//private static final String ACTION_23 =
			
	
	
	
	/**
	 * The timezone id that will be displayed.
	 */
	protected static String DisplayTimeZoneID = null;
	/**
	 * The local timezone.
	 */
	private static TimeZone local = TimeZone.getDefault();
	/**
	 * The target timezone.
	 */
	private static TimeZone target = TimeZone.getDefault();
	/**
	 * A static reference of TutaiorianCalendar.
	 */

	/**
	 * Adjusts timestamp to source timezone time and returns a String formatted per
	 * the specified style.
	 * 
	 * @param ts    Calendar object containing the timestamp.
	 * @param style Format style; see constants in Utilities.
	 * @return the formatted string.
	 */
	public static String dateFormat(Calendar ts, int style) {
		GregorianCalendar _cal = new GregorianCalendar();
		SimpleDateFormat df = null;
		long tim = ts.getTimeInMillis();

		switch (style) {
		case FILENAME_DATETIME:
			df = new SimpleDateFormat(FileName_DateTime_Give);
			break;
		case LOG_TIMEONLY:
			df = new SimpleDateFormat(Log_TimeOnly_Give);
			break;

		case DATETIME_WITH_MILLIS:
			df = new SimpleDateFormat(DateTime_With_Millis_Give);
			break;
		case DATETIME_PACKED:
			df = new SimpleDateFormat(DateTime_Packed_Give);
			break;
		case LONG_DATETIME:
			df = new SimpleDateFormat(Long_DateTime_Give);
			break;
		case PSQL_TIMESTAMP:
			df = new SimpleDateFormat(pSQL_DateTime_Give);
			break;
		case LOG_DATEONLY:
			df = new SimpleDateFormat(Log_DateOnly_Give);
			break;
		case LOG_DATETIME:
			df = new SimpleDateFormat(Log_DateTime_Give);
			break;
		case LOG_DATESHORT:
			df = new SimpleDateFormat(Log_DateOnly_Give);
			break;

		case LOG_TIMESHORT:
			df = new SimpleDateFormat(Log_TimeShort_Give);
			break;
		case LOG_PRO_DATETIME:
			df = new SimpleDateFormat(DateTime_With_Millis_Give);
			break;
		case FILENAME_DATETIME2:
			df = new SimpleDateFormat(FileName_DateTime2_Give);
			break;
		case DATETIME_WITH_ZONE:
			df = new SimpleDateFormat(DateTime_With_ZONE_Give);
			break;
		case DATETIME_V_TIME_LINE:
			df = new SimpleDateFormat(vTimeLine_DateTime_Give);
			break;
		case DATETIME_FFT_FORMAT:
			df = new SimpleDateFormat(fft_DateTime_Give);
			break;

		default:
			df = new SimpleDateFormat(Log_DateOnly_Give);
			break;
		}

		if (df != null) {
			if (DisplayTimeZoneID == null) {
				DisplayTimeZoneID = local.getID();
			}
			_cal.setTimeInMillis(tim + target.getOffset(tim) - local.getOffset(tim));
			return df.format(_cal.getTime());
		}
		return null;
	}

	public static long dateFormat(String timeStr, int style, String timeZoneId) {
		SimpleDateFormat df = null;

		switch (style) {
		case FILENAME_DATETIME:
			df = new SimpleDateFormat(FileName_DateTime_Give);
			break;
		case LOG_TIMEONLY:
			df = new SimpleDateFormat(Log_TimeOnly_Give);
			break;

		case DATETIME_WITH_MILLIS:
			df = new SimpleDateFormat(DateTime_With_Millis_Give);
			break;
		case DATETIME_PACKED:
			df = new SimpleDateFormat(DateTime_Packed_Give);
			break;
		case LONG_DATETIME:
			df = new SimpleDateFormat(Long_DateTime_Give);
			break;
		case PSQL_TIMESTAMP:
			df = new SimpleDateFormat(pSQL_DateTime_Give);
			break;
		case LOG_DATEONLY:
			df = new SimpleDateFormat(Log_DateOnly_Give);
			break;
		case LOG_DATETIME:
			df = new SimpleDateFormat(Log_DateTime_Give);
			break;
		case LOG_DATESHORT:
			df = new SimpleDateFormat(Log_DateOnly_Give);
			break;

		case LOG_TIMESHORT:
			df = new SimpleDateFormat(Log_TimeShort_Give);
			break;
		case LOG_PRO_DATETIME:
			df = new SimpleDateFormat(DateTime_With_Millis_Give);
			break;
		case FILENAME_DATETIME2:
			df = new SimpleDateFormat(FileName_DateTime2_Give);
			break;
		case DATETIME_WITH_ZONE:
			df = new SimpleDateFormat(DateTime_With_ZONE_Give);
			break;
		case DATETIME_V_TIME_LINE:
			df = new SimpleDateFormat(vTimeLine_DateTime_Give);
			break;
		case DATETIME_FFT_FORMAT:
			df = new SimpleDateFormat(fft_DateTime_Give);
			break;

		default:
			df = new SimpleDateFormat(Log_DateOnly_Give);
			break;
		}

		if (df != null) {
			try {
				if (timeZoneId != null) {
					df.setTimeZone(TimeZone.getTimeZone(timeZoneId));
				} else {
					df.setTimeZone(TimeZone.getDefault());
				}
				return df.parse(timeStr).getTime();
			} catch (ParseException e){
				LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
			}
		}
		return -1;
	}

	/**
	 * Get the timezone id.
	 * 
	 * @return the timezone id.
	 */
	public static String getDisplayTimeZoneID() {
		if (DisplayTimeZoneID == null) {
			DisplayTimeZoneID = local.getID();
		}
		return DisplayTimeZoneID;
	}

	/**
	 * Set the timezone id.
	 * 
	 * @param string the timezone id.
	 */
	public static void setDisplayTimeZoneID(String string) {
		DisplayTimeZoneID = string;
		target = TimeZone.getTimeZone(DisplayTimeZoneID);
	}

	 public static synchronized boolean isValidTimestamp(long timeStamp) {
		Calendar cal = new GregorianCalendar();
		
		cal.setTimeInMillis(System.currentTimeMillis());
		int yearOfCurrentSystem = cal.get(Calendar.YEAR);

		cal.setTimeInMillis(timeStamp);
		int yearOfInput = cal.get(Calendar.YEAR);

		return (yearOfInput >= 1900 && yearOfInput <= yearOfCurrentSystem);
	}

	 public static synchronized String timeToString(long timeStamp) {
		return "timeStamp = " + timeStamp + "(" + new Date(timeStamp) + ")";
	}

	 public static synchronized String timeToStringWithoutTimestamp(long timeStamp) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timeStamp);
		return "" + dateFormat(cal, FILENAME_DATETIME);
	}

	public static String convertMillisecondsToHoursMinutesAndSeconds(long milliseconds) {
		String str = "";
		try {
			int seconds = (int) (milliseconds / 1000) % 60;
			int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
			int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
			str = str + hours + (hours > 1 ? " hours, " : " hour, ");
			str = str + minutes + (minutes > 1 ? " minutes, " : " minute, ");
			str = str + seconds + (seconds > 1 ? " seconds " : " second ");
		} catch (Exception e) {
			str = str + ACTION_1 + e;
		}

		return str;
	}

	public static String convertMillisecondsToHoursMinutesAndSecondsNonZeroOnly(long milliseconds) {
		String str = "";
		try {
			int seconds = (int) (milliseconds / 1000) % 60;
			int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
			int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
			if (hours > 0) {
				str = str + hours + (hours > 1 ? " hours, " : " hour, ");
			}

			if (minutes > 0) {
				str = str + minutes + (minutes > 1 ? " minutes, " : " minute, ");
			}

			if (seconds > 0) {
				str = str + seconds + (seconds > 1 ? " seconds " : " second ");
			}

		} catch (Exception e) {
			str = str + ACTION_1 + e;
		}

		return str;
	}

	public static String convertMillisecondsToDaysHoursMinutesSecondsMillis(long milliseconds) {
		String str = "";
		try {

			int seconds = (int) (milliseconds / 1000) % 60;
			int minutes = (int) ((milliseconds / (60 * 1000)) % 60);
			int hours = (int) ((milliseconds / (60 * 60 * 1000)) % 24);
			int days = (int) ((milliseconds / (24 * 60 * 60 * 1000)) % 365);
			int millis = (int) (milliseconds % 1000);

			if (days > 0) {
				if (!str.equals("")) {
					str = str + ", ";
				}
				str = str + days + (days > 1 ? " days" : " day");
			}

			if (hours > 0) {
				if (!str.equals("")) {
					str = str + ", ";
				}
				str = str + hours + (hours > 1 ? " hours" : " hour");
			}

			if (minutes > 0) {
				if (!str.equals("")) {
					str = str + ", ";
				}
				str = str + minutes + (minutes > 1 ? " minutes" : " minute");
			}

			if (seconds > 0) {
				if (!str.equals("")) {
					str = str + ", ";
				}
				str = str + seconds + (seconds > 1 ? " seconds" : " second");
			}

			if (millis > 0) {
				if (!str.equals("")) {
					str = str + ", ";
				}
				str = str + millis + (millis > 1 ? " milliseconds" : " millisecond");
			}

		} catch (Exception e) {
			str = str + ACTION_1 + e;
		}

		return str;
	}

	 public static synchronized int convertMillisecondsToSeconds(long timestamp) {
		 GregorianCalendar _cal = new GregorianCalendar();
		_cal.setTimeInMillis(timestamp);
		return (_cal.get(Calendar.HOUR_OF_DAY) * 3600) + (_cal.get(Calendar.MINUTE) * 60) + _cal.get(Calendar.SECOND);
	}

	 public static synchronized long convertSecondsToMilliseconds(int seconds, long referenceMilliseconds) {
		 GregorianCalendar _cal = new GregorianCalendar();
		try {
			
			int p1 = seconds % 60;
			int p2 = seconds / 60;
			int p3 = p2 % 60;
			p2 = p2 / 60;

			int hr = p2;
			int minute = p3;
			int second = p1;

			_cal.setTimeInMillis(referenceMilliseconds);
			_cal.set(Calendar.HOUR_OF_DAY, hr);
			_cal.set(Calendar.MINUTE, minute);
			_cal.set(Calendar.SECOND, second);

			return _cal.getTimeInMillis();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
			return -1L;
		}
	}

	 public static synchronized int[] convertBufferedImageToRGBIntArray(BufferedImage bufferedImage) {
		int rgbWidth = bufferedImage.getWidth();
		int rgbHeight = bufferedImage.getHeight();

		int[] rgb = new int[rgbWidth * rgbHeight];
		PixelGrabber pg = new PixelGrabber(bufferedImage, 0, 0, rgbWidth, rgbHeight, rgb, 0, rgbWidth);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LOGGER.log(Level.WARNING, "Interrupted..... Waiting for pixels! : {0}" , e.toString());
			Thread.currentThread().interrupt();
		}

		return rgb;
	}

	 public static synchronized BufferedImage convertRGBIntArrayToBufferedImage(int[] rgb, int rgbWidth, int rgbHeight) {
		BufferedImage image = null;

		try {
			image = new BufferedImage(rgbWidth, rgbHeight, BufferedImage.TYPE_INT_RGB);
			final int[] a = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
			System.arraycopy(rgb, 0, a, 0, rgb.length);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		return image;
	}

	 public static synchronized BufferedImage convertARGBIntArrayToBufferedImage(int[] rgb, int rgbWidth,
			int rgbHeight) {
		BufferedImage image = null;

		try {
			image = new BufferedImage(rgbWidth, rgbHeight, BufferedImage.TYPE_INT_ARGB);
			final int[] a = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
			System.arraycopy(rgb, 0, a, 0, rgb.length);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		return image;
	}

	 public static synchronized byte[] encodeToJpeg(BufferedImage img) {
		
		byte[] result = null;
		
		try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			

			if (img.getColorModel().hasAlpha()) {
				img = dropAlphaChannel(img);
			}

			ImageIO.write(img, "jpeg", os);
			result = os.toByteArray();
			
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}
		
		return result;

	}

	 public static synchronized BufferedImage dropAlphaChannel(BufferedImage src) {
		BufferedImage convertedImg = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		convertedImg.getGraphics().drawImage(src, 0, 0, null);

		return convertedImg;
	}

	 public static synchronized BufferedImage decodeImage(byte[] encodedBytes) {
		
		try(ByteArrayInputStream bis = new ByteArrayInputStream(encodedBytes)) {
			
			return ImageIO.read(bis);
		} catch (Exception e) {
			return null;
		} 
	}

	 public static synchronized BufferedImage copyImage(BufferedImage source) {
		BufferedImage output = null;
		Graphics g = null;
		try {
			output = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
			g = output.getGraphics();
			g.drawImage(source, 0, 0, null);
		} catch (Exception e) {
			return output;
		} 

		return output;
	}

	 public static synchronized boolean dumpPPMFile(BufferedImage image, String url, String targetDir) {

		boolean rs = false;
		File file = new File(url.replace("\\", "/"));
		String canonicalDestinationPath;
		try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
			canonicalDestinationPath = file.getCanonicalPath();
			if(targetDir == null || canonicalDestinationPath.contains(targetDir)) {
				if (file.getParentFile() != null && !file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					boolean filecreated = file.createNewFile();
					if(!filecreated) {
						LOGGER.log(Level.WARNING, "Problem in creating file ");
					}
				}
				
				int rgbWidth = image.getWidth();
				int rgbHeight = image.getHeight();

				int[] rgb = new int[rgbWidth * rgbHeight];
				PixelGrabber pg = new PixelGrabber(image, 0, 0, rgbWidth, rgbHeight, rgb, 0, rgbWidth);
				pg.grabPixels();

				if (rgb != null) {

						dos.write("P6\n".getBytes());
						dos.write((image.getWidth() + " " + image.getHeight() + "\n").getBytes());

						dos.write("255\n".getBytes());
						byte[] outData = new byte[rgb.length * 3];
						int count = 0;
						for (int i = 0; i < rgb.length; i++) {
							byte alpha = (byte) (rgb[i] >> 24);
							outData[count++] = (byte) ((rgb[i] << 8) >> 24);
							outData[count++] = (byte) ((rgb[i] << 16) >> 24);
							outData[count++] = (byte) ((rgb[i] << 24) >> 24);
						}

						dos.write(outData);
						dos.flush();

						rs = true;
				}
				rs = true;
			}
			
		}catch (IOException e1) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e1.getCause().getMessage());
		} catch (InterruptedException e1) {
			Thread.currentThread().interrupt();
			LOGGER.log(Level.WARNING, ERROR_STRING, e1.getCause().getMessage());
			Thread.currentThread().interrupt();
		}
		return rs;
		
	

		
	}

	 public static synchronized boolean dumpPPMFile(int[] rgb, int rgbW, int rgbH, String url,  String targetDir) {

		boolean rs = false;
		File file = new File(url.replace("\\", "/"));
		String canonicalDestinationPath;
		try(FileOutputStream fos = new FileOutputStream(file); XMLEncoder enc = new XMLEncoder(fos)) {
			canonicalDestinationPath = file.getCanonicalPath();
			if(targetDir == null || canonicalDestinationPath.contains(targetDir)) {
				if (file.getParentFile() != null && !file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					boolean filecreated = file.createNewFile();
					if(!filecreated) {
						LOGGER.log(Level.WARNING, "Problem in creating file ");
					}
				}
				
				if (rgb != null) {
					try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))){
						dos.write("P6\n".getBytes());
						dos.write((rgbW + " " + rgbH + "\n").getBytes());

						dos.write("255\n".getBytes());
						byte[] outData = new byte[rgb.length * 3];
						int count = 0;
						for (int i = 0; i < rgb.length; i++) {
							byte alpha = (byte) (rgb[i] >> 24);
							outData[count++] = (byte) ((rgb[i] << 8) >> 24);
							outData[count++] = (byte) ((rgb[i] << 16) >> 24);
							outData[count++] = (byte) ((rgb[i] << 24) >> 24);
						}

						dos.write(outData);
						dos.flush();

						rs = true;
					} catch (Exception e) {
						LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
					}
				}
				rs = true;
				return rs;
				
			}
			
		}catch (IOException e1) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e1.getCause().getMessage());
		}
		return rs;
		
	
		
	}

	 public static synchronized boolean dumpJPEGFile(byte[] encodedBytes, String url, String targetDir) {

			boolean rs = false;
			File file = new File(url.replace("\\", "/"));
			if (!(url.toUpperCase().endsWith(".JPEG") || url.toUpperCase().endsWith(".JPG"))) {
				url = url + ".jpeg";
			}
			String canonicalDestinationPath;
			try(FileOutputStream fos = new FileOutputStream(file)) {
				canonicalDestinationPath = file.getCanonicalPath();
				if(targetDir == null || canonicalDestinationPath.contains(targetDir)) {
					if (file.getParentFile() != null && !file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					if (!file.exists()) {
						boolean filecreated = file.createNewFile();
						if(!filecreated) {
							LOGGER.log(Level.WARNING," Problem in creating file File: {0}", file.getName());
						}
					}
					
					if (encodedBytes != null) {

						fos.write(encodedBytes);
						fos.flush();
						rs = true;
					}
				}
				
			}catch (IOException e1) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e1.getCause().getMessage());
			}
			return rs;
			

	}

	 public static synchronized boolean dumpFileContents(byte[] dataBytes, String url, String targetDir) {

		boolean rs = false;
	
		File file = new File(url.replace("\\", "/"));
		String canonicalDestinationPath;
		try(FileOutputStream fos = new FileOutputStream(file)) {
			canonicalDestinationPath = file.getCanonicalPath();
			if(targetDir == null || canonicalDestinationPath.contains(targetDir)) {
				if (file.getParentFile() != null && !file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				if (!file.exists()) {
					boolean filecreated = file.createNewFile();
					if(!filecreated) {
						LOGGER.log(Level.WARNING, "Problem in creating file ");
					}
				}

				if (dataBytes != null) {

						fos.write(dataBytes);
						fos.flush();
						rs = true;
				}
			}else {
				LOGGER.log(Level.WARNING, "Entry is outside of the target directory ");
			}
		} catch (IOException e1) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e1.getCause().getMessage());
		}
		
		return rs;
	}

	 public static synchronized byte[] loadFileContents(String url) {

		byte[] dataBytes = null;

		

		try(FileInputStream fis =  new FileInputStream(url);) {
			
			dataBytes = new byte[fis.available()];
			long n = fis.read(dataBytes);
			LOGGER.log(Level.INFO, "Read databytes: {0}", n);
		} catch (IOException ex) {
			dataBytes = null;
		} catch (Exception ex) {
			LOGGER.log(Level.WARNING, ERROR_STRING, ex.getMessage());
		} 

		return dataBytes;
	}

	 public static synchronized int findJpegEOIOffset(byte[] jpegData) {
		long stTime = System.currentTimeMillis();
		int offset = -1;
		try {
			for (int i = jpegData.length - 2; i >= 0; i--) {
				if (jpegData[i] == (byte) 0xFF && jpegData[i + 1] == (byte) 0xD9) {
					offset = i;
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		return offset;
	}

	 public static synchronized Properties loadConfigurationProperties(String confFileName) {
		Properties properties = new Properties();

		
		try (FileInputStream fis = new FileInputStream(confFileName);
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"))){
			
			properties.load(isr);
		} catch (IOException ex) {
			properties = null;

			Object [] params = new Object[] {confFileName , ACTION_1 , ex.toString()};
			LOGGER.log(Level.WARNING, "Unable to load File, filename: {0}, action: {1}, error: {2}", params);
			
		} catch (Exception ex) {
			LOGGER.log(Level.WARNING, ERROR_STRING, ex.getMessage());
		}

		return properties;
	}

	 public static synchronized boolean saveConfigurationProperties(Properties properties, String confFileName,
			String comments, String targetDir) {
		boolean rs = false;
		
		File file = new File(confFileName.replace("\\", "/"));
		String canonicalDestinationPath;
		try(FileOutputStream fos = new FileOutputStream(file)) {
			canonicalDestinationPath = file.getCanonicalPath();
			if(targetDir == null || canonicalDestinationPath.contains(targetDir)) {
				if (file.getParentFile() != null && !file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					boolean filecreated = file.createNewFile();
					if(!filecreated) {
						LOGGER.log(Level.WARNING, "Problem in creating file ");
					}
				}
				properties.store(fos, comments);
				rs = true;
			}
			
		}catch (IOException e1) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e1.getCause().getMessage());
		}
		return rs;
	}

	 public static synchronized Object loadConfigurationXml(String confFileName) {
		Object object = null;

		File xml = new File(confFileName);
		if (xml.exists()) {
			
			try(FileInputStream	fis = new FileInputStream(xml);
				XMLDecoder dec = new XMLDecoder(fis)) {
				
				object = dec.readObject();
			} catch (IOException ex) {
				Object [] params = new Object[] {confFileName , ex.toString()};
				LOGGER.log(Level.WARNING, "Unable to load File: {0} " + ACTION_1 + " {1}", params);
				object = null;
			} catch (Exception ex) {
				LOGGER.log(Level.WARNING, ERROR_STRING, ex.getMessage());
			} 
		}

		return object;
	}

	 public static synchronized boolean saveConfigurationXml(Object object, String confFileName, String targetDir) {

		boolean rs = false;
		File file = new File(confFileName.replace("\\", "/"));
		String canonicalDestinationPath;
		try(FileOutputStream fos = new FileOutputStream(file); XMLEncoder enc = new XMLEncoder(fos)) {
			canonicalDestinationPath = file.getCanonicalPath();
			if(targetDir == null || canonicalDestinationPath.contains(targetDir)) {
				if (file.getParentFile() != null && !file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					boolean filecreated = file.createNewFile();
					if(!filecreated) {
						LOGGER.log(Level.WARNING, " Problem in creating file ");
					}
				}
				enc.writeObject(object);
				rs = true;
			}
			
		}catch (IOException e1) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e1.getCause().getMessage());
		}
		return rs;
		
	}

	 public static synchronized boolean isExecutable(File file, String targetDir) {
		byte[] firstBytes = new byte[4];
		String canonicalDestinationPath;
		try(FileInputStream input = new FileInputStream(file)) {
			canonicalDestinationPath = file.getCanonicalPath();
			if(targetDir == null || canonicalDestinationPath.contains(targetDir)) {
				int readVal = input.read(firstBytes);
				if (readVal > 0  && firstBytes[0] == 0x4d && firstBytes[1] == 0x5a) {
					return true;
				}
				return false;
			}else {
				return false;
			}
			
		}catch (Exception e) {
			return false;
		}
	}

	 public static synchronized boolean isAudioFile(String selectedFilePath) {
		boolean isAudioFile = false;
		try {
			String[] extensions = { "3gp", "aa", "aac", "aiff", "alac", "m4a", "m4b", "m4p", "mp3", "mpc", "msv", "ogg",
					"raw", "voc", "vox", "wav", "wma", "wv", "webm", "8svx", "cda" };
			for (String extension : extensions) {
				if (FilenameUtils.getExtension(selectedFilePath).equals(extension)) {
					return true;
				} else {
					isAudioFile = false;
				}
			}
		} catch (Exception e) {
			isAudioFile = false;
		}
		return isAudioFile;
	}

	 public static synchronized boolean isMultipleExtension(String selectedFilePath) {
		try {
			String[] extensions = { "3gp", "aa", "aac", "adt", "adts", "accdb", "accde", "accdr", "accdt", "aif",
					"aifc", "aiff", "alac", "aspx", "avi", "bat", "bin", "bmp", "cab", "cda", "csv", "dif", "dll",
					"doc", "docm", "docx", "dot", "dotx", "eml", "eps", "exe", "flv", "gif", "htm", "html", "ini",
					"iso", "jar", "jpg", "jpeg", "m4a", "m4b", "m4p", "mdb", "mid", "midi", "mov", "mp3", "mpc", "mp4",
					"mpeg", "mpg", "msv", "msi", "mui", "pdf", "png", "pot", "potm", "potx", "ppam", "pps", "ppsm",
					"ppsx", "ppt", "pptm", "pptx", "psd", "pst", "pub", "rar", "raw", "rtf", "sldm", "sldx", "swf",
					"sys", "tif", "tiff", "tmp", "txt", "vob", "voc", "vsd", "vsdm", "vsdx", "vss", "vssm", "vst",
					"vstm", "vstx", "wav", "wbk", "wv", "webm", "wks", "wma", "wmd", "wmv", "wmz", "wms", "wpd", "wp5",
					"xla", "8svx", "xlam", "xll", "xlm", "xls", "xlsm", "xlsx", "xlt", "xltm", "xltx", "xps", "zip" };

			String selectedPath[] = selectedFilePath.split("\\.");

			int count = 0;

			for (int i = 0; i < selectedPath.length; i++) {
				for (String extension : extensions) {
					if (extension.equalsIgnoreCase(selectedPath[i])) {
						count++;
					}
				}
			}
			if (count > 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	 public static synchronized boolean isExeFile(String selectedFilePath) {
		try {
			String selectedPath[] = selectedFilePath.split("\\.");
			if (FilenameUtils.getExtension(selectedFilePath).equals("exe")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	 public static synchronized String maliciousFileCheck(String filePath) {

		String maliciousFileError = "";

		int numberOfExtension = 0;
		int fileNameLength = 0;
		long fileContentLength = 0;
		boolean isExecutable = false;
		boolean isDirectory = false;
		boolean isExist = false;

		try {
			if (filePath.contains("\\.")) {
				numberOfExtension = filePath.split("\\.").length;
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		try {
			fileNameLength = new File(filePath).getName().length();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		try {
			fileContentLength = new File(filePath).length();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		try {
			isExecutable = Files.isExecutable(FileSystems.getDefault().getPath(filePath));
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		try {
			isDirectory = new File(filePath).isDirectory();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		try {
			isExist = new File(filePath).exists();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		if (isExist) {
			if (!isDirectory && numberOfExtension != 1 ) {
				
				LOGGER.log(Level.INFO, "numberOfExtension: {0} " , numberOfExtension);
					maliciousFileError = maliciousFileError.equals("") ? "Multiple file extensions are not allowed"
							: maliciousFileError + "\n" + "Multiple file extensions are not allowed";
				}
			

			if (fileNameLength > 512) {
				maliciousFileError = maliciousFileError.equals("") ? "More than 256 characters file name not allowed"
						: maliciousFileError + "\n" + "More than 256 characters file name not allowed";
			}

			if (!isDirectory) {
				if (fileContentLength <= 0) {
					maliciousFileError = maliciousFileError.equals("") ? "Empty file not allowed"
							: maliciousFileError + "\n" + "Empty file not allowed";
				}
			}

			if (!isDirectory) {
				if (fileContentLength > 512 * 1024 * 1024) {
					maliciousFileError = maliciousFileError.equals("") ? "More than 512 MB file not allowed"
							: maliciousFileError + "\n" + "More than 512 MB file not allowed";
				}
			}

			if (!isDirectory) {
				if (isExecutable) {
					LOGGER.log(Level.WARNING, "isExecutable: {0} ", isExecutable);
					maliciousFileError = maliciousFileError.equals("") ? "Executable file not allowed"
							: maliciousFileError + "\n" + "Executable file not allowed";
				}
			}
		}

		maliciousFileError = "";

		return maliciousFileError;
	}

	public static synchronized int copyFile(File in, File out) throws IOException {

		int byteCount = 0;
		
		if (out.getParentFile() != null && !out.getParentFile().exists()) {
			out.getParentFile().mkdirs();
		}

		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(in));
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out));) {
			byteCount = copy(bis, bos);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}
		
		return byteCount;
		
	}

	public static synchronized void copyFolder(File src, File dest) {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdirs();
				Object [] params = new Object[] {src, dest};
				LOGGER.log(Level.WARNING, "Directory copied from : {0} to {1}", params);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			try (InputStream in = new FileInputStream(src);
					OutputStream out = new FileOutputStream(dest);) {

				byte[] buffer = new byte[1024];

				int length;
				// copy the file content in bytes
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}

			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
			}
		}
	}

	public static boolean createFile(String path) {
		boolean isSuccess = false;

		try {

			File out = new File(path);
			if (out.getParentFile() != null && !out.getParentFile().exists()) {
				out.getParentFile().mkdirs();
			}

			
			boolean fileCreated = out.createNewFile();
			if (Boolean.FALSE.equals(fileCreated)) {
				LOGGER.log(Level.WARNING, "Problem in creating File Error: {0}", out.getName());
			}

			isSuccess = true;
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		return isSuccess;
	}

	public static boolean createFolder(String path) {
		boolean isSuccess = false;

		try {
			File out = new File(path);
			out.mkdirs();

			isSuccess = true;
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		return isSuccess;
	}

	public static int copy(InputStream in, OutputStream out) throws IOException {
		try {
			return copyTemp(in, out);
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				LOGGER.log(Level.WARNING, ERROR_STRING, ex.getMessage());
			}
			try {
				out.close();
			} catch (IOException ex) {
				LOGGER.log(Level.WARNING, ERROR_STRING, ex.getMessage());
			}
		}
	}

	private static int copyTemp(InputStream in, OutputStream out) throws IOException {
		int BUFFER_SIZE = 4096;
		int byteCount = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}
		out.flush();
		return byteCount;
	}

	public static final BufferedImage cropImage(byte[] jpegBytes, Rectangle rectangle) {
		return cropImage(decodeImage(jpegBytes), rectangle, -1, -1);
	}

	public static final BufferedImage cropImage(byte[] jpegBytes, Rectangle rectangle, int scaledW, int scaledH) {
		return cropImage(decodeImage(jpegBytes), rectangle, scaledW, scaledH);
	}

	public static final BufferedImage cropImage(BufferedImage image, Rectangle rectangle) {
		return cropImage(image, rectangle, -1, -1);
	}

	public static final BufferedImage cropImage(BufferedImage image, Rectangle rectangle, int scaledW, int scaledH) {

		BufferedImage subImage = image;

		if (rectangle != null) {
			try {
				if (rectangle.x >= 0 && rectangle.y >= 0 && rectangle.width > 0 && rectangle.height > 0
						&& rectangle.width < image.getWidth() && rectangle.height < image.getHeight()) {
					subImage = image.getSubimage(rectangle.x, rectangle.y,
							Math.min(rectangle.width, image.getWidth() - rectangle.x),
							Math.min(rectangle.height, image.getHeight() - rectangle.y));
				}
			} catch (Exception e1) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e1.toString());
			}
		}

		if (scaledW > 0 && scaledH > 0) {
			subImage = getScaledImage(subImage, scaledW, scaledH, false);
		}

		return subImage;
	}

	public static synchronized BufferedImage getScaledImage(BufferedImage image, int scaledW, int scaledH,
			boolean maintainAspectRatio) {

		if (scaledW <= 0 || scaledH <= 0)
			return image;

		BufferedImage scaledImage = null;
		try {

			int imageType = image.getType();
			if (imageType == 0)
				imageType = BufferedImage.TYPE_INT_ARGB;

			if (maintainAspectRatio) {

				if (scaledW >= image.getWidth() && scaledH >= image.getHeight()) {
					scaledImage = image;

				} else {

					double aspectRatioOrig = (image.getWidth() * 1.0) / image.getHeight();
					aspectRatioOrig = aspectRatioOrig == 0 ? 1 : aspectRatioOrig;

					int wMin = Math.min(scaledW, image.getWidth());
					int hMin = Math.min(scaledH, image.getHeight());

					int scaledWTemp = scaledW;
					int scaledHTemp = scaledH;

					if (wMin <= hMin) {
						scaledHTemp = (int) (wMin / aspectRatioOrig);
						if (scaledHTemp > scaledH) {
							scaledHTemp = scaledH;
							scaledWTemp = (int) (hMin * aspectRatioOrig);
						}
					} else {
						scaledWTemp = (int) (hMin * aspectRatioOrig);
						if (scaledWTemp > scaledW) {
							scaledWTemp = scaledW;
							scaledHTemp = (int) (wMin / aspectRatioOrig);
						}
					}

					scaledW = Math.min(scaledWTemp, scaledW);
					scaledH = Math.min(scaledHTemp, scaledH);

					
					

					scaledImage = new BufferedImage(scaledW, scaledH, imageType);
					Graphics2D g = scaledImage.createGraphics();
					g.drawImage(image, 0, 0, scaledW, scaledH, null);
					g.dispose();
				}

			} else {

				scaledImage = new BufferedImage(scaledW, scaledH, imageType);
				Graphics2D g = scaledImage.createGraphics();
				g.drawImage(image, 0, 0, scaledW, scaledH, null);
				g.dispose();
			}

		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}

		return scaledImage;
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight)
			throws IOException {
		Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
		BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
		return outputImage;
	}

	public static synchronized VNICConfiguration getNICConfiguration() {

		VNICConfiguration nicConfiguration = new VNICConfiguration();



		// active nic name
		{
			if (System.getProperty(OS_NAME).contains(OS_WINDOWS)) {

				String nicName = null;
				String[] command = { CMD_NETSH, ACTION_7, "show", ACTION_7 };
				Process process = null;
				try {
					process = Runtime.getRuntime().exec(command);
				} catch (IOException e1) {
					LOGGER.log(Level.WARNING, ERROR_STRING, e1.toString());
				}
				
				if(process != null) {
					try (InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream()); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
						
						String line = "";
						while (line != null) {
							line = bufferedReader.readLine();
							if (line != null && !line.equals("")) {

								LOGGER.log(Level.INFO,"getNicName line :{0} ", line);

								if (line.contains(" Connected ") && !line.toLowerCase().contains(ACTION_8 )) {
									String[] nicNameLine = line.split("    ");
									nicName = nicNameLine[nicNameLine.length - 1].trim();
									if (nicName != null ) {
										if (nicName.toUpperCase().contains("SERVICE")
												|| nicName.toUpperCase().contains("WI-FI")
												|| nicName.toUpperCase().contains("WIFI")
												|| nicName.toUpperCase().contains("ETHERNET")
												|| nicName.toUpperCase().contains("SERVER")
												|| nicName.toUpperCase().contains("BOND0")) {// APFSL data port,
																								// management port
											break;
										}
									}
								}
							}
						}
					} catch (Exception e) {
						LOGGER.log(Level.WARNING, ERROR_STRING, e.getCause().getMessage());
					}
				}
				

				if (nicName == null || nicName.equals("")) {
					nicName = "Local Area Connection";
				}

				nicConfiguration.setNicName(nicName);

			} else if (System.getProperty(OS_NAME).contains(ACTION_10)) {

				String nicName = null;

				File interfacesFile = new File("/etc/network/interfaces");
				try (FileInputStream fis = new FileInputStream(interfacesFile) ){
					
					byte[] dataBytes = new byte[fis.available()];
					
					long n = fis.read(dataBytes);
					LOGGER.log(Level.INFO, "Read databytes {0}", n);

					String[] dataStrArr = new String(dataBytes).split("\n");
					for (int i = 0; i < dataStrArr.length; i++) {
						if (!dataStrArr[i].contains("#") && !dataStrArr[i].toLowerCase().contains(ACTION_8 )) {
							if (dataStrArr[i].contains(ACTION_14) && dataStrArr[i].contains("inet")) {
								nicName = dataStrArr[i].substring(dataStrArr[i].indexOf(ACTION_14) + ACTION_14.length(),
										dataStrArr[i].indexOf("inet")).trim();

							} else if (dataStrArr[i].contains(ACTION_11)) {
								nicConfiguration.setIp(dataStrArr[i]
										.substring(dataStrArr[i].indexOf(ACTION_11) + ACTION_11.length()).trim());

							} else if (dataStrArr[i].contains(ACTION_12)) {
								nicConfiguration.setSubnetMask(dataStrArr[i]
										.substring(dataStrArr[i].indexOf(ACTION_12) + ACTION_12.length()).trim());

							} else if (dataStrArr[i].contains(ACTION_13)) {
								nicConfiguration.setGateway(dataStrArr[i]
										.substring(dataStrArr[i].indexOf(ACTION_13) + ACTION_13.length()).trim());

							}
						}

						if (nicName != null) {
							if (nicName.toUpperCase().contains("SERVICE") || nicName.toUpperCase().contains("WI-FI")
									|| nicName.toUpperCase().contains("WIFI")
									|| nicName.toUpperCase().contains("ETHERNET")
									|| nicName.toUpperCase().contains("SERVER")
									|| nicName.toUpperCase().contains("BOND0")) {// APFSL data port, management port
								break;
							}
						}
					}
				} catch (Exception e) {
					
					LOGGER.log(Level.WARNING, ACTION_9 , e.getCause().getMessage());
					
				} 
				// retry another way
				if (nicName == null || nicName.equals("") || nicConfiguration.getIp() == null
						|| nicConfiguration.getIp().equals("")) {
					String[] command = { "ip", "route" };

					Process process = null;
					try {
						if(command[0].contains("ip")) {
							process = Runtime.getRuntime().exec(command);
						}
					} catch (IOException e1) {
						LOGGER.log(Level.WARNING, ERROR_STRING, e1.toString());
					}
					if(process != null) {
					try (InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
						BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
					
						
						String line = "";
						while (line != null) {
							line = bufferedReader.readLine();
							if (line != null && !line.equals("")) {
								if (line.contains(ACTION_15) && !line.toLowerCase().contains(ACTION_8 )) {
									int nicNameStartIndex = line.indexOf(ACTION_15) + ACTION_15.length();
									nicName = line
											.substring(nicNameStartIndex, line.indexOf(" ", nicNameStartIndex))
											.trim();
								}
							}
						}
					} catch (Exception e) {
						LOGGER.log(Level.WARNING, ACTION_9 + "{0}",  e.getCause().getMessage());
					} 
				}
				}

				if (nicName == null || nicName.equals("")) {
					nicName = "eth0";
				}

				nicConfiguration.setNicName(nicName);

			} else {
				LOGGER.log(Level.WARNING, ERROR_STRING, ACTION_9 + ACTION_16);
			}
		}

		{
			if (System.getProperty(OS_NAME).contains(OS_WINDOWS)) {

				String[] command = { CMD_NETSH, ACTION_7, "ip", "show", "config", ACTION_17,
						nicConfiguration.getNicName() };

				Process process = null;
				try {
					process = Runtime.getRuntime().exec(command);
				} catch (IOException e1) {
					LOGGER.log(Level.WARNING, ERROR_STRING, e1.toString());
				}
				if(process != null) {
				
				try(InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
						BufferedReader	bufferedReader = new BufferedReader(inputStreamReader)) {
					
					
					String line = "";
					while (line != null) {
						line = bufferedReader.readLine();
						if (line != null && !line.equals("")) {

							

							if (line.contains("DHCP enabled:")) {
								nicConfiguration.setSourceType(line.split(":")[1].trim().toLowerCase().equals("yes")
										? VNICConfiguration.SOURCE_TYPE_DHCP
										: VNICConfiguration.SOURCE_TYPE_STATIC);

							}

							if (line.contains("IP Address:")) {
								nicConfiguration.setIp(line.split(":")[1].trim());

							}

							if (line.contains("Subnet Prefix:")) {
								String subnetStr = line.split(":")[1].trim();
								subnetStr = subnetStr.substring(subnetStr.indexOf("(mask") + "(mask".length() + 1,
										subnetStr.lastIndexOf(")")).trim();
								nicConfiguration.setSubnetMask(subnetStr);

							}

							if (line.contains("Default Gateway:")) {
								nicConfiguration.setGateway(line.split(":")[1].trim());

							}
						}
					}
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, ERROR_STRING, ACTION_18 + e.toString());
				} 
				}

			} else if (System.getProperty(OS_NAME).contains(ACTION_10)) {

				 
				File interfacesFile = new File("/etc/network/interfaces");
				try(FileInputStream	fis = new FileInputStream(interfacesFile)){
					
					
					byte[] dataBytes = new byte[fis.available()];
					
					long n = fis.read(dataBytes);
					LOGGER.log(Level.INFO, "Read databytes {0}", n);
					
					String[] dataStrArr = new String(dataBytes).split("\n");
					for (int i = 0; i < dataStrArr.length; i++) {
						if (!dataStrArr[i].contains("#")) {
							if (dataStrArr[i].contains(ACTION_14)
									&& dataStrArr[i].contains(nicConfiguration.getNicName())
									&& dataStrArr[i].contains("inet")) {
								nicConfiguration.setSourceType(
										dataStrArr[i].substring(dataStrArr[i].indexOf("inet") + "inet".length())
												.trim().equals("dhcp") ? VNICConfiguration.SOURCE_TYPE_DHCP
														: VNICConfiguration.SOURCE_TYPE_STATIC);

							} else if (dataStrArr[i].contains(ACTION_11)) {
								nicConfiguration.setIp(dataStrArr[i]
										.substring(dataStrArr[i].indexOf(ACTION_11) + ACTION_11.length()).trim());

							} else if (dataStrArr[i].contains(ACTION_12)) {
								nicConfiguration.setSubnetMask(dataStrArr[i]
										.substring(dataStrArr[i].indexOf(ACTION_12) + ACTION_12.length()).trim());

							} else if (dataStrArr[i].contains(ACTION_13)) {
								nicConfiguration.setGateway(dataStrArr[i]
										.substring(dataStrArr[i].indexOf(ACTION_13) + ACTION_13.length()).trim());

							}
						}
					}
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, ERROR_STRING + ACTION_18,  e.toString());
				}

				if (nicConfiguration.getSourceType() == VNICConfiguration.SOURCE_TYPE_INVALID
						|| (nicConfiguration.getSourceType() == VNICConfiguration.SOURCE_TYPE_STATIC
								&& (nicConfiguration.getIp() == null || nicConfiguration.getIp().equals("")))) {
					String[] command = { "ip", "route" };

					
					Process process = null;
					try {
						process = Runtime.getRuntime().exec(command);
					} catch (IOException e1) {
						LOGGER.log(Level.WARNING, ERROR_STRING, e1.toString());
					}
					
					if (process != null) {
						try (InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
								BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {

							String line = "";
							while (line != null) {
								line = bufferedReader.readLine();
								if (line != null && !line.equals("") && line.contains(nicConfiguration.getNicName())) {

								

									

										if (line.contains(ACTION_19)) {
											int ipStartIndex = line.indexOf(ACTION_19) + ACTION_19.length();
											String ip = line.substring(ipStartIndex, line.indexOf(" ", ipStartIndex))
													.trim();
											nicConfiguration.setIp(ip);

											String subnetStr = line.split(" ")[0].trim();
											subnetStr = new SubnetUtils(subnetStr).getInfo().getNetmask();
											nicConfiguration.setSubnetMask(subnetStr);
										}

										if (line.contains(ACTION_20)) {
											int gatewayStartIndex = line.indexOf(ACTION_20) + ACTION_20.length();
											String gateway = line
													.substring(gatewayStartIndex, line.indexOf(" ", gatewayStartIndex))
													.trim();
											nicConfiguration.setGateway(gateway);

											if (line.contains(ACTION_21)) {
												int sourceTypeStartIndex = line.indexOf(ACTION_21) + ACTION_21.length();
												String sourceTypeStr = line.substring(sourceTypeStartIndex,
														line.indexOf(" ", sourceTypeStartIndex)).trim();
												nicConfiguration
														.setSourceType((sourceTypeStr.toLowerCase().equals("dhcp")
																|| sourceTypeStr.toLowerCase().equals("kernel"))
																		? VNICConfiguration.SOURCE_TYPE_DHCP
																		: VNICConfiguration.SOURCE_TYPE_STATIC);
											}
										} else {
											if (line.contains(ACTION_21)) {
												int sourceTypeStartIndex = line.indexOf(ACTION_21) + ACTION_21.length();
												String sourceTypeStr = line.substring(sourceTypeStartIndex,
														line.indexOf(" ", sourceTypeStartIndex)).trim();
												nicConfiguration
														.setSourceType((sourceTypeStr.toLowerCase().equals("dhcp")
																|| sourceTypeStr.toLowerCase().equals("kernel"))
																		? VNICConfiguration.SOURCE_TYPE_DHCP
																		: VNICConfiguration.SOURCE_TYPE_STATIC);
											}
										}
									
								}
							}
						} catch (Exception e) {

							LOGGER.log(Level.WARNING, ERROR_STRING + ACTION_18, e.toString());
						}
					}
					
				}

			} else {
				LOGGER.log(Level.WARNING, ERROR_STRING + ACTION_18, ACTION_16);
			}
		}

		// hostname
		{
			String hostname = "";
			String[] command = { "hostname" };
			
			Process process = null;
			try {
				if(command[0].contains("hostname")) {
					process = Runtime.getRuntime().exec(command);
				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
			}
			
			if(process != null) {
				try (InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
						BufferedReader bufferedReader = new BufferedReader(inputStreamReader);){
					
					hostname = bufferedReader.readLine();
					nicConfiguration.setHostname(hostname);
				} catch (Exception e) {
					LOGGER.log(Level.WARNING,"Failed to get hostname... " + ERROR_STRING, e.toString());
				} 
			}
		}

		// mac address
		{
			String hardwareAdress = "";
			try {
				InetAddress addr = InetAddress.getByName(nicConfiguration.getIp());
				if (VUtilities.isValidIP(addr.getHostAddress())) {
					NetworkInterface network = NetworkInterface
							.getByInetAddress(addr);
					hardwareAdress = byteArrayToHexString(network.getHardwareAddress());
				}
				
			} catch (Exception e) {
				LOGGER.log(Level.WARNING,"Failed to get macAddress... " + ERROR_STRING, e.toString());
			}
			nicConfiguration.setHardwareAdress(hardwareAdress);
		}

	

		return nicConfiguration;
	}

	public static synchronized boolean setNICConfiguration(VNICConfiguration nicConfiguration) {

		boolean isSuccess = false;
		boolean hostnameToBeChanged = nicConfiguration.getHostname() != null
				&& !nicConfiguration.getHostname().trim().equals("");
		boolean ipToBeChanged = nicConfiguration.getSourceType() == VNICConfiguration.SOURCE_TYPE_DHCP
				|| (nicConfiguration.getSourceType() == VNICConfiguration.SOURCE_TYPE_STATIC
						&& nicConfiguration.getIp() != null && !nicConfiguration.getIp().trim().equals(""));

		VNICConfiguration nicConfigurationTemp = getNICConfiguration();
		nicConfiguration.setNicName(nicConfigurationTemp.getNicName());

		if (hostnameToBeChanged) {


			if (System.getProperty(OS_NAME).contains(OS_WINDOWS)) {

				String[] command = { "wmic", "computersystem", "where", "caption='CURRENT-PC-NAME'", "rename",
						"'" + nicConfiguration.getHostname() + "'" };

				Process process = null;
				try {
					if(command[0].contains("wmic")) {
						process = Runtime.getRuntime().exec(command);
					}
				} catch (Exception e) {
				}

				if (process != null) {
					try (InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
							BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {

						LOGGER.log(Level.WARNING, ACTION_22 + "{0}", bufferedReader.readLine());
					} catch (Exception e) {
						Object [] params = new Object[] {ACTION_3 ,  e.getMessage()};
						LOGGER.log(Level.WARNING, " Action: {0}, Error: {1}", params);
					}
				}

			} else if (System.getProperty(OS_NAME).contains(ACTION_10)) {

				File hostsFile = new File("hosts");
				try {
					
					FileOutputStream fos = new FileOutputStream(hostsFile);
					fos.write(("127.0.0.1" + '\t' + "localhost").getBytes());
					fos.write(("\n" + nicConfiguration.getIp() + '\t' + nicConfiguration.getHostname()).getBytes());
					fos.close();

					File hostnameFile = new File("hostname");
					fos = new FileOutputStream(hostnameFile);
					fos.write((nicConfiguration.getHostname()).getBytes());
					fos.close();

					copyFile(hostsFile, new File("/etc/" + hostsFile.getName()));
					
					Files.delete(Paths.get(hostsFile.getAbsolutePath()));

					copyFile(hostnameFile, new File("/etc/" + hostnameFile.getName()));
					
					Files.delete(Paths.get(hostsFile.getAbsolutePath()));
					
				} catch (IOException e) {

					LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage()); 

				}
				

			} else {
				LOGGER.log(Level.WARNING, ERROR_STRING + ACTION_3 + ACTION_16);
			}
		
		}

		if (ipToBeChanged) {



			if (System.getProperty(OS_NAME).contains(OS_WINDOWS)) {

				if (nicConfiguration.getSourceType() == VNICConfiguration.SOURCE_TYPE_DHCP) {
					String[] command = { CMD_NETSH, ACTION_7, "ip", "set", ACTION_11, ACTION_17,
							nicConfiguration.getNicName(), "source=dhcp" };
					Process process = null;
					try {
						process = Runtime.getRuntime().exec(command);
					} catch (IOException e1) {
						LOGGER.log(Level.WARNING, ERROR_STRING, e1.toString());
					}
					if (process != null) {

						try (InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
								BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
							LOGGER.log(Level.WARNING, ACTION_22 + "{0}", bufferedReader.readLine());
						} catch (Exception e) {

							LOGGER.log(Level.WARNING, ERROR_STRING + ACTION_3,  e.toString());
						

							Object [] params = new Object[] { e.getMessage()};
							LOGGER.log(Level.WARNING, ERROR_STRING, params);
						}
					}

				} else if (nicConfiguration.getSourceType() == VNICConfiguration.SOURCE_TYPE_STATIC) {

					String[] command = { CMD_NETSH, ACTION_7, "ip", "set", ACTION_11, ACTION_17,
							nicConfiguration.getNicName(), "source=static", "addr=", nicConfiguration.getIp(),
							"mask=", nicConfiguration.getSubnetMask(), nicConfiguration.getGateway() };
					
					Process process = null;
					try {
						process = Runtime.getRuntime().exec(command);
					} catch (IOException e1) {
						LOGGER.log(Level.WARNING, ERROR_STRING, e1.toString());
					}

					if (process != null) {
						try (InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
								BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {

							LOGGER.log(Level.WARNING, ACTION_22 + "{0}", bufferedReader.readLine());

						} catch (Exception e) {

							LOGGER.log(Level.WARNING, ERROR_STRING + ACTION_3, e.toString());

							Object[] params = new Object[] { e.getMessage() };
							LOGGER.log(Level.WARNING, ERROR_STRING, params);
						}
					}
				} else {
					LOGGER.log(Level.WARNING, "Failed to set IP and Gateway...unknown nicConfiguration.getSourceType");
				}

			} else if (System.getProperty(OS_NAME).contains(ACTION_10)) {

				File interfacesFile = new File("interfaces");
				
				try (FileOutputStream fos = new FileOutputStream(interfacesFile);){
					fos.write(("# The loopback network interface").getBytes());
					fos.write(("\n" + "auto lo").getBytes());
					fos.write(("\n" + "iface lo inet loopback").getBytes());
					fos.write(("\n" + "# The primary network interface").getBytes());
					fos.write(("\n" + "auto " + nicConfiguration.getNicName()).getBytes());
					fos.write(("\n" + "iface " + nicConfiguration.getNicName() + " inet "
							+ (nicConfiguration.getSourceType() == VNICConfiguration.SOURCE_TYPE_DHCP ? "dhcp"
									: "static"))
							.getBytes());

					if (nicConfiguration.getSourceType() == VNICConfiguration.SOURCE_TYPE_STATIC) {
						fos.write(("\n" + ACTION_11 + '\t' + nicConfiguration.getIp()).getBytes());
						fos.write(("\n" + ACTION_12 + '\t' + nicConfiguration.getSubnetMask()).getBytes());
						fos.write(("\n" + ACTION_13 + '\t' + nicConfiguration.getGateway()).getBytes());
					}

					copyFile(interfacesFile, new File("/etc/network/" + interfacesFile.getName()));
					Files.delete(Paths.get(interfacesFile.getAbsolutePath()));
					
				} catch (IOException e) {
					

					Object [] params = new Object[] { e.getMessage()};
					LOGGER.log(Level.WARNING, ERROR_STRING, params);
				}
				
			} else {
				LOGGER.log(Level.WARNING, "Failed to set IP and Gateway... Error{0}", ACTION_16);
			}
		}

		try {
			Thread.sleep(10 * 1000L);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LOGGER.log(Level.WARNING, ERROR_STRING, e.toString());
        }

		VNICConfiguration nicConfigurationCurrent = getNICConfiguration();
		LOGGER.log(Level.WARNING, "nicConfigurationCurrent {0}" , nicConfigurationCurrent);

		if (hostnameToBeChanged) {
			if (nicConfigurationCurrent.getHostname() != null
					&& nicConfigurationCurrent.getHostname().equals(nicConfiguration.getHostname())) {
				isSuccess = true;
			} else {
				isSuccess = false;
			}

			if (isSuccess) {
				Object [] params = new Object[] { nicConfigurationCurrent.getHostname(), nicConfiguration.getHostname()};
				LOGGER.log(Level.WARNING, "Success to set hostname. current hostname {0}, hostNameRequested {1}", params);
			} else {
				Object [] params = new Object[] { nicConfigurationCurrent.getHostname(), nicConfiguration.getHostname()};
				LOGGER.log(Level.WARNING, "Failed to set hostname. current hostname  {0}, hostNameRequested {1}", params);
			}
		}

		if (ipToBeChanged) {

			if (nicConfiguration.getSourceType() == nicConfigurationCurrent.getSourceType()) {
				isSuccess = true;
				// this code has got no meaning without restart
				try {
					if (nicConfiguration.getSourceType() == VNICConfiguration.SOURCE_TYPE_STATIC) {
						if (nicConfiguration.getIp().equals(nicConfigurationCurrent.getIp())
								&& nicConfiguration.getSubnetMask().equals(nicConfigurationCurrent.getSubnetMask())
								&& nicConfiguration.getGateway().equals(nicConfigurationCurrent.getGateway())) {
							isSuccess = true;
						} else {
							isSuccess = false;
						}
					}
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
				}
			} else {
				isSuccess = false;
			}

			if (isSuccess) {
				Object [] params = new Object[] { nicConfigurationCurrent, nicConfiguration};
				LOGGER.log(Level.WARNING, "Success to set ip,subnetmask,gateway. current nicConfiguration {0}, nicConfigurationRequested {1}", params);
			} else {
				Object [] params = new Object[] { nicConfigurationCurrent, nicConfiguration};
				LOGGER.log(Level.WARNING, "Failed to set ip,subnetmask,gateway. current nicConfiguration {0}, nicConfigurationRequested {1}", params);
			}
		}

		return isSuccess;
	}

	public static synchronized NetworkInterface getNetworkInterface() {
		NetworkInterface networkInterface = null;

		ArrayList<NetworkInterface> allNetworkInterfaceForGivenNicName = new ArrayList<NetworkInterface>();
		try {
			VNICConfiguration nicConfiguration = getNICConfiguration();
			Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
			while (enumeration.hasMoreElements()) {
				NetworkInterface networkInterfaceTemp = enumeration.nextElement();
				if (nicConfiguration != null && nicConfiguration.getNicName() != null
						&& networkInterfaceTemp.getName().equals(nicConfiguration.getNicName())) {
					allNetworkInterfaceForGivenNicName.add(networkInterfaceTemp);
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to getNetworkInterface..." + ERROR_STRING , e.toString());

			Object [] params = new Object[] { e.getMessage()};
			LOGGER.log(Level.WARNING, ERROR_STRING, params);
		}

		if (allNetworkInterfaceForGivenNicName.size() > 0) {
			NetworkInterface networkInterfaceActive = null;
			for (int i = 0; i < allNetworkInterfaceForGivenNicName.size(); i++) {
				try {
					if (allNetworkInterfaceForGivenNicName.get(i).isUp()) {
						networkInterfaceActive = allNetworkInterfaceForGivenNicName.get(i);
					}
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Failed to getNetworkInterface..." + ERROR_STRING , e.toString());
					

					Object [] params = new Object[] { e.getMessage()};
					LOGGER.log(Level.WARNING, ERROR_STRING, params);
				}
			}

			if (networkInterfaceActive == null) {
				networkInterfaceActive = allNetworkInterfaceForGivenNicName
						.get(allNetworkInterfaceForGivenNicName.size() - 1);
			}

			networkInterface = networkInterfaceActive;
		}

		return networkInterface;
	}
	
	public static synchronized String getMacAddress() {

		
		String macAddress = "";
		try {
			InetAddress ip = InetAddress.getLocalHost();
			LOGGER.log(Level.WARNING, "ACTION_2 Current IP address : {0}", ip.getHostAddress());
			if (VUtilities.isValidIP(ip.getHostAddress())) {
				Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
				while (networks.hasMoreElements()) {
					NetworkInterface network = networks.nextElement();
					if (network.isUp()) {
						byte[] mac = network.getHardwareAddress();

						if (mac != null) {
							LOGGER.log(Level.INFO,"Current MAC address : ");
							macAddress = byteArrayToHexString(mac);
							
						}
					}
				}
			}

			
		} catch (UnknownHostException e) {
			LOGGER.log(Level.WARNING, ERROR_STRING + ACTION_2, e.getMessage());
		} catch (SocketException e2) {
			LOGGER.log(Level.WARNING, ERROR_STRING + ACTION_2, e2.getMessage());
		} catch (Exception e3) {
			LOGGER.log(Level.WARNING, ERROR_STRING + ACTION_2, e3.getMessage());
		}

		return macAddress;
	}

	public static String getMacAddressByIP(String ip) {

		String macAddress = "";

		// mac address
		{
			try {
				InetAddress addr = InetAddress.getByName(ip);
				if (VUtilities.isValidIP(addr.getHostAddress())) {
					NetworkInterface network = NetworkInterface.getByInetAddress(addr);
					macAddress = byteArrayToHexString(network.getHardwareAddress());
				}
				
			} catch (Exception e) {
				Object [] params = new Object[] { ip, e.toString()};
				LOGGER.log(Level.WARNING,"Failed to get macAddress... for ip: {0} " +  ACTION_1 + "{1}", params);
			}
		}

		return macAddress;
	}

	 public static synchronized String getLocalMachineIpAddress() {
		String ipAddress = null;
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface ntwInterface = networkInterfaces.nextElement();
				if (ntwInterface != null && ntwInterface.isUp() && ntwInterface.getHardwareAddress() != null
						&& ntwInterface.getHardwareAddress().length > 0) {
					Enumeration<InetAddress> inetenumeration = ntwInterface.getInetAddresses();
					while (inetenumeration.hasMoreElements()) {
						InetAddress inetAddress = (InetAddress) inetenumeration.nextElement();
						String address = inetAddress.getHostAddress();
						if (!address.contains(":")) {
							ipAddress = inetAddress.getHostAddress();
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, ERROR_STRING, e.getMessage());
		}
		return ipAddress;
	}

	public static synchronized String byteArrayToHexString(Byte in[]) {
		if (in == null || in.length <= 0)
			return "";

		byte[] inTemp = new byte[in.length];
		for (int i = 0; i < in.length; i++) {
			inTemp[i] = in[i];
		}

		return byteArrayToHexString(inTemp);
	}

	 public static synchronized String byteArrayToHexString(byte in[]) {
		byte ch = 0x00;
		int i = 0;
		if (in == null || in.length <= 0)
			return "";

		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		StringBuffer out = new StringBuffer(in.length * 2);
		while (i < in.length) {
			ch = (byte) (in[i] & 0xF0); // Strip off
			ch = (byte) (ch >>> 4);
			ch = (byte) (ch & 0x0F);
			out.append(pseudo[(int) ch]);
			ch = (byte) (in[i] & 0x0F);
			out.append(pseudo[(int) ch]);
			i++;
		}
		String rslt = new String(out);
		return rslt;
	}
	
	public static synchronized boolean isSamePassword(String passwordPlainText, String passwordEncrypted) {
		try {
			return SHAUtility.encrypt512(passwordPlainText).equals(passwordEncrypted);
		} catch (Exception e) {
			return false;
		}
	}
	
	
	public static synchronized OSInformation getOSInformation() {


		OSInformation osInformation = null;

		try {
			String name = System.getProperty("os.name");
			String architecture = System.getProperty("os.arch");
			String version = System.getProperty("os.version");
			String description = null;

			try {
				if (name.contains("Windows")) {
					//method 1
					{
						String[] command = {"systeminfo"};

						BufferedReader bufferedReader = null;
						InputStreamReader inputStreamReader = null;
						try {
							Process p = Runtime.getRuntime().exec(command);
							inputStreamReader = new InputStreamReader(p.getInputStream());
							bufferedReader = new BufferedReader(inputStreamReader);
							String line = "";	
							while (line != null) {
								line = bufferedReader.readLine();
								if (line != null && !line.equals("")) {
									if (line.startsWith("OS Name:")) {
										String[] dataLine = line.split("OS Name:");
										description = dataLine[1].trim();

									} else if (line.startsWith("OS Version:")) {
										String[] dataLine = line.split("OS Version:");
										version = dataLine[1].trim();

									}
								}
							}
						} catch (Exception e) {
							Object[] params = new Object[] {e.getMessage()};LOGGER.log(Level.WARNING, "Method1: Failed to get OS name... Error: {0}", params);
						} finally {
							if (bufferedReader != null) {try {bufferedReader.close();} catch (Exception e2) {}}
							if (inputStreamReader != null) {try {inputStreamReader.close();} catch (Exception e2) {}}
						}

					}

				} else if (name.contains("Linux")) {

					//method 1
					{
						String[] command = {"lsb_release", "-d"};

						BufferedReader bufferedReader = null;
						InputStreamReader inputStreamReader = null;
						try {
							Process p = Runtime.getRuntime().exec(command);
							inputStreamReader = new InputStreamReader(p.getInputStream());
							bufferedReader = new BufferedReader(inputStreamReader);
							String line = "";	
							while (line != null) {
								line = bufferedReader.readLine();
								if (line != null && !line.equals("") && line.contains("Description:")) {
									String[] descriptionLine = line.split("Description:");
									description = descriptionLine[1].trim();
								}
							}
						} catch (Exception e) {
							Object[] params = new Object[] {e.getMessage()};LOGGER.log(Level.WARNING, "Method1: Failed to get OS name... Error: {0}", params);
						} finally {
							if (bufferedReader != null) {try {bufferedReader.close();} catch (Exception e2) {}}
							if (inputStreamReader != null) {try {inputStreamReader.close();} catch (Exception e2) {}}
						}

					}

					if (description == null || description.equals("")) {
						//method 2
						{
							String[] command = {"uname", "-v"};

							BufferedReader bufferedReader = null;
							InputStreamReader inputStreamReader = null;
							try {
								Process p = Runtime.getRuntime().exec(command);
								inputStreamReader = new InputStreamReader(p.getInputStream());
								bufferedReader = new BufferedReader(inputStreamReader);
								String line = "";	
								while (line != null) {
									line = bufferedReader.readLine();
									if (line != null && !line.equals("")) {
										String[] descriptionLine = line.split(" ");
										description = descriptionLine[0].trim();
									}
								}
							} catch (Exception e) {
								Object[] params = new Object[] {e.getMessage()};LOGGER.log(Level.WARNING, "Method2: Failed to get OS name... Error: {0}", params);
							} finally {
								if (bufferedReader != null) {try {bufferedReader.close();} catch (Exception e2) {}}
								if (inputStreamReader != null) {try {inputStreamReader.close();} catch (Exception e2) {}}
							}

						}
					}

				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, " Failed to get OSInformation... Error: {0}", e.getMessage());
			}

			osInformation = new OSInformation();
			osInformation.setName(name);
			osInformation.setArchitecture(architecture);
			osInformation.setVersion(version);
			osInformation.setDescription(description);
			
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, " Failed to get OSInformation... Error: {0}", e.getMessage());
		}

		return osInformation;

	}

	public static String getSanitizedInput(String patternMatcher, String input) {

		String result = null;
		if (input != null && !input.equals("")) {
			try {
				String matchingInput = Jsoup.clean(input.trim(), Safelist.none());

				patternMatcher = patternMatcher != null ? patternMatcher : DEFAULT_PATTERN_MATCHER;

				if (matchingInput.matches(patternMatcher)) {
					result = matchingInput;
				} else {
					Object[] param = new Object[] { input, patternMatcher };
					LOGGER.log(Level.WARNING, "Input is not matching with pattern, input: {0}, pattern: {1}", param);
				}

			} catch (Exception e) {
				LOGGER.log(Level.WARNING, ERROR_STRING, e.getCause().getMessage());
			}
		}

		
		return result;

	}
	
	public static boolean isValidPath(String path) {
		
	    try {
	        Paths.get(path);
	    } catch (InvalidPathException | NullPointerException ex) {
	        return false;
	    }
	    return true;
	}
	public static boolean isValidURL(String url) {
		boolean result = false;
		if (url != null) {
			try {
				UrlValidator validator = new UrlValidator();
				result = validator.isValid(url);
			} catch (Exception e) {

			}
		}
		return result;
	}
	
	public static boolean isValidEmail(String email) {
		return email != null && email.length() > 0 ? EMAILADDRESS_PATTERN.matcher(email).matches() : false;
	}

}
