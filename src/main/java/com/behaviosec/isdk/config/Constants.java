package com.behaviosec.isdk.config;

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
    public static final String API_WHITELIST_USER       = API_BASE_URL + "WhiteListUser";
    public static final String API_SET_FRAUD_STATUS       = API_BASE_URL + "SetFraudStatus";




    //Operator flags
    public final static int FINALIZE_DIRECTLY = 512;
    public final static int FLAG_GENERATE_TIMESTAMP = 2;

}
