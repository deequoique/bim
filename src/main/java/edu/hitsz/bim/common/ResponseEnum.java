package edu.hitsz.bim.common;

/**
 * @desc Response enum
 */
public enum ResponseEnum {

    /********************************************** Define Message ****************************************************/

    SUCCESS(200, "success"),
    ERROR(101, "error"),
    PARAM_ERROR(102, "parameter error"),
    UNAUTHORIZED(103, "unauthorized"),
    NOT_FOUND(104, "not found"),
    CUSTOM(105, null),
    WRONG_STATUS(106,"Current Operate Object in Unexpected Status "),
    UIV_RANGE_INVALID(107,"range of UIV invalid"),
    DB_ERROR(108,"DB error"),

    SIGN_INVALID(109,"Invalid signature"),


    POOL_REACH_MAX(107,"obey the limitation a pool has no asset"),

    ACCESS_FAIL(111, "Fail to pass accessibility"),

    DELETE_FAIL(112, "Fail when deleting"),

    DUPLICATE(113, "Existing duplicate data"),
    /**   USER    **/
    USER_NOT_REGISTER(1000,"The public key has not registered"),
    USER_HAS_BEEN_REGISTERED(1001,"The public key has already been registered"),
    TOKEN_EXPIRED(401,"The token was expired"),


    /**   Tx Record    **/
    TX_ROLLBACK(1, "The tx should roll back"),
    OUTPUT_NEGATIVE(2100, "Occurred negative value when calculate formula"),


    /**   SUI    **/
    SUI_ADDRESS_EMPTY(2000, "Get Empty AddressList from KeyStore"),
    SUI_ADDRESS_NOT_EXIST(2001, "Not Get Address from KeyStore"),


    /**   Rule Create Validate    **/
    REPEATED_UIV_ID(3001,"id of UIV repeated"),
    INVALID_UIV_COLLECTION_RANGE(3002, "uiv collection range invalid"),
    INVALID_BEFORE_AFTER(3003, "beforeAfter cannot be null for outer data"),
    INVALID_NFT_OPERATION(3004, "nft operation is invalid"),
    INVALID_SCENARIO_LAST(3005, "last cannot be 0 for all scenarios"),
    INVALID_RESTRICTION_TYPE(3006, "restriction type is invalid"),
    RESTRICTION_NOT_FOUND(3007, "restriction is null"),
    INVALID_FORMULA(3008, "formula cannot parse"),
    ACCESSIBILITY_NOT_FOUND(3009, "accessibility is null"),
    INVALID_ACC_TYPE(3010, "accessibility type is invalid"),
    INVALID_RESTRI_COLLECTION_RANGE(3011, "data restriction collection range invalid"),
    INVALID_RESTRI_TIME_RANGE(3012, "time range is invalid"),
    INVALID_RESTRI_TIME_PERIODIC(3013, "time periodic is invalid"),
    INVALID_RESTRI_TIME_EVENT(3014, "time event is invalid"),
    INVALID_ACCESS_COLLECTION_RANGE(3015, "data accessibility collection range invalid"),
    INVALID_ACCESS_TIME_RANGE(3016, "time range accessibility is invalid"),
    INVALID_ACCESS_TIME_PERIODIC(3017, "time periodic accessibility is invalid"),
    INVALID_ACCESS_TIME_EVENT(3018, "time event accessibility is invalid")
    ;


    /********************************************** Define format *****************************************************/

    private Integer code;
    private String  msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg  = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
