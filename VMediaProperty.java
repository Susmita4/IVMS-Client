package com.videonetics.values;

public interface VMediaProperty {

	int MAX_ALLOWED_FRAME_LENGTH = 50 * 1024 * 1024;

	int H_FRAME = 0;
	int I_FRAME = 1;
	int P_FRAME = 2;   
	int CONNECT_HEADER = 3;
	int AUDIO_FRAME = 16;
	int V_FRAME = 100;

	int INVALID = -1;
	int MJPG 	= 0;
	int MPEG 	= 1;
	int H264 	= 2;
	int PCMU 	= 3;
	int PCMA 	= 4;
	int L16  	= 5;
	int ACC  	= 6;
	int UNKNOWN = 7;
	int H265 	= 8;
	

	byte[] MAGIC_BYTES = "00dc".getBytes();
	String MAGIC_STRING = "00dc";
	byte[] EXTENDED_MAGIC_BYTES = "00dd".getBytes();
	String EXTENDED_MAGIC_STRING = "00dd";
	long MAX_CLIP_TIME = 5 * 60 * 1000L; // for haldia 5 * 60 * 1000 //SHIFT TO CONFIG
	long MAX_MOTION_EXTENTION_TIME = 15 * 60 * 1000L;



	int FORWARD_PLAY = 0;
	int BACKWARD_PLAY = 1;
	int IP_PLAY = 0;
	int I_PLAY = 1; 
	int SCHEDULED_PLAY = 0;
	int EVENT_PLAY = 1;
	int MOTION_PLAY = 2;
	
	
	// critical Video
	int NORMAL_PLAY = 0;
	int CRITICAL_PLAY = 1;
	int INCIDENT_BOOKMARK_PLAY = 2;
			


	// STREAMER ACTIONS
	int STREAMER_END_PLAYER = 10;
	int STREAMER_SET_INTERVAL = 11;
	int STREAMER_SET_IP_FILTER = 12;
	int STREAMER_SET_MOVEMENT = 13;
	int STREAMER_SEEK_TO_TIMESTAMP = 14;
	int STREAMER_PAUSE = 15;
	int STREAMER_RESUME = 16;


	int COLOR_SPACE_UNKNOWN = -1;
	int COLOR_SPACE_RGB  = 0;
	int COLOR_SPACE_ARGB = 1;
	int COLOR_SPACE_BGR  = 2;
	int COLOR_SPACE_BGRA = 3;
	int COLOR_SPACE_ABGR = 4;
	int COLOR_SPACE_RGBA = 5;

}
