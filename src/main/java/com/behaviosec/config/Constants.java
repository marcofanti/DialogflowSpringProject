package com.behaviosec.config;

/**
 * Class for constants through the application
 */
public class Constants {

	//Default score settings
    public static final int SCORE_MULTIPLIER = 100;
    public static final double MIN_SCORE = 0.60*SCORE_MULTIPLIER;
    public static final double MIN_CONFIDENCE = 0.40*SCORE_MULTIPLIER;
    public static final double MAX_RISK = 0.60*SCORE_MULTIPLIER;

    public static final boolean ALLOW_BOT = false;
    public static final boolean ALLOW_REPLAY = false;
    public static final boolean ALLOW_IN_TRAINING = true;
    public static final boolean ALLOW_REMOTE_ACCESS = true;
    public static final boolean ALLOW_TAB_ANOMALY = true;
    public static final boolean ALLOW_NUMPAD_ANOMALY = true;
    public static final boolean ALLOW_DEVICE_CHANGE = true;
    public static final int ERROR_ID_POSITION = 1;
    public static final int ERROR_MESSAGE_POSITION = 2;
    
    public static final String MINIMUM_SSO_SCORE = "minimumssoscore";
    public static final String MAXIMUM_SSO_RISK = "maximumssorisk";
    
    public static final String MINIMUM_LOGIN_SCORE = "minimumloginscore";
    public static final String MAXIMUM_LOGIN_RISK = "maximumloginrisk";
    
    public static final String MINIMUM_STEP_UP_SCORE = "minimumstepupscore";
    public static final String MAXIMUM_STEP_UP_RISK = "maximumstepuprisk";
    
    public static final String MINIMUM_FORM_SCORE = "minimumformscore";
    public static final String MAXIMUM_FORM_RISK = "maximumformrisk";
    
    public static final String OTP = "otp";
    public static final String BEHAVIOSEC = "behaviosec";

    public static final String APP_NAME = "BehavioSecRegistration";
    public static final String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    
    // request header
    public static String ACCEPT_HEADER = "application/json";
    public static String CONTENT_TYPE = "application/json";


    // data collector field
    public static String DATA_FIELD = "application/json";

    // request body
    public static String USER_ID = "userId";
    public static String TIMING_DATA = "timing";
    public static String USER_AGENT = "userAgent";
    public static final String TENANT_ID = "tenantId";
    public static String IP = "ip";
    public static String TIMESTAMP = "timestamp";
    public static String NOTES = "notes";
    public static String REPORT_FLAGS = "reportflags";
    public static String OPERATOR_FLAGS = "operatorflags";
    public static String SESSION_ID = "sessionId";


    //request action
    private static String API_BASE_URL                  = "BehavioSenseAPI/";
    //API endpoint
    public static String API_GET_REPORT                 = API_BASE_URL + "GetReport";
    public static String API_FINALIZE_SESSION           = API_BASE_URL + "FinalizeSession";
    public static String API_FORCE_TRAINING             = API_BASE_URL + "ForceTrain";
    public static String API_GET_HEALTH_CHECK           = API_BASE_URL + "GetHealthCheck";
    public static String API_GET_INVESTIGATION_REPORT   = API_BASE_URL + "GetInvestigationReport";
    public static String API_GET_OBFUSCATED_JAVA_SCRIPT = API_BASE_URL + "GetObfuscatedJavaScript";
    public static String API_GET_REPORT_AND_INVESTIGATE = API_BASE_URL + "GetReportAndInvestigate";
    public static String API_GET_SESSION                = API_BASE_URL + "GetSession";
    public static String API_GET_USER                   = API_BASE_URL + "GetUser";
    public static String API_GET_VERSION                = API_BASE_URL + "GetVersion";
    public static String API_REMOVE_USER                = API_BASE_URL + "RemoveUser";
    public static String API_RESET_PROFILE              = API_BASE_URL + "ResetProfile";
    public static String API_SET_USER                   = API_BASE_URL + "SetUser";


    //Operator flags
    public final static int FINALIZE_DIRECTLY = 512;
    public final static int FLAG_GENERATE_TIMESTAMP = 2;
    
	public final static String INITIAL_CHAT_RETURN_STRING_1 = "{\r\n" + "  \"output\": {\r\n" + "    \"generic\": [\r\n"
			+ "      {\r\n" + "        \"response_type\": \"text\",\r\n" + "        \"text\": \"Hello ";

	public final static String INITIAL_CHAT_RETURN_STRING_2 = ", I am the ACME bank's Virtual Agent.\"\r\n"
			+ "      },\r\n" + "      {\r\n" + "        \"response_type\": \"option\",\r\n"
			+ "        \"title\": \"I can help you with a number of banking tasks:\",\r\n"
			+ "        \"options\": [\r\n" + "          {\r\n"
			+ "            \"label\": \"Making a credit card payment\",\r\n" + "            \"value\": {\r\n"
			+ "              \"input\": {\r\n"
			+ "                \"text\": \"I want to make a credit card payment\"\r\n" + "              }\r\n"
			+ "            }\r\n" + "          },\r\n" + "          {\r\n"
			+ "            \"label\": \"Transfer money\",\r\n" + "            \"value\": {\r\n"
			+ "              \"input\": {\r\n" + "                \"text\": \"I want transfer money\"\r\n"
			+ "              }\r\n" + "            }\r\n" + "          },\r\n" + "          {\r\n"
			+ "            \"label\": \"Check balance\",\r\n" + "            \"value\": {\r\n"
			+ "              \"input\": {\r\n" + "                \"text\": \"I want to check my balance\"\r\n"
			+ "              }\r\n" + "            }\r\n" + "          },\r\n" + "          {\r\n"
			+ "            \"label\": \"Open an account\",\r\n" + "            \"value\": {\r\n"
			+ "              \"input\": {\r\n" + "                \"text\": \"I want to open an account\"\r\n"
			+ "              }\r\n" + "            }\r\n" + "          }\r\n" + "        ]\r\n" + "      }\r\n"
			+ "    ],\r\n" + "    \"intents\": [],\r\n" + "    \"entities\": []\r\n" + "  }\r\n" + "}\r\n";
	
	public static final String VERIFY_MESSAGE_1 = ", I cannot verify you. Please enter your email address or username.";
	public static final String VERIFY_MESSAGE_2 = ", I cannot verify you (no input detected). Please enter your email address or username.";
	public static final String VERIFY_MESSAGE_3 = ", I cannot verify you (cut and paste detected). Please enter your email address or username.";
	public static final String VERIFY_MESSAGE_4 = ", I still cannot verify you. Please enter your email address or username, or your google authenticator code.";
	
	public static final String FEEDBACK_MODE_BOTH = "both";
	public static final String FEEDBACK_MODE_NONE = "none";
	public static final String FEEDBACK_MODE_SCORE = "score";
	public static final String FEEDBACK_MODE_PRIVILEGE = "privilege";

	public static final String TRAINING_MODE_TRUE = "true";
	public static final String TRAINING_MODE_FALSE = "false";
}
