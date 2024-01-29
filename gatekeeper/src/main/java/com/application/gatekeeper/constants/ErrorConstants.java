package com.application.gatekeeper.constants;

public class ErrorConstants {

    public static final String NO_SUCH_USER = "No such user, please check user ID";
    public static final String USER_ALREADY_APPROVED = "User is already approved";
    public static final String NO_SUCH_VISITOR_DETAIL = "No such visitor detail exists, please check the id";
    public static final String NO_SUCH_VISITOR = "No such visitor, please check Visitor ID";
    public static final String VISITOR_ALREADY_EXISTS = "Visitor with this mail already exists, please use different mail or schedule for this visitor";
    public static final String NO_SUCH_VISITOR_EXISTS = "No such Visitor Email, check the email or create a new visitor";
    public static final String VISITOR_BLOCKED = "This visitor is blacklisted by yourself or Gatekeeper, kindly check";
    public static final String VISITOR_BUSY = "Visitor busy during requested time";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists, try to login or use different email";
    public static final String APARTMENT_ALREADY_OCCUPIED = "Resident already exists in this apartment";
    public static final String INVALID_CREDS = "Invalid Credentials";
    public static final String ABOVE_18_AGE = "Should be age of above 18";
    public static final  String GENDER_ERROR = "Gender should be either M, F, O";
    public static final  String USER_NOT_APPROVED = "You have not been approved yet";
    public static final  String USER_DEACTIVATED = "You have been deactivated";
    public static final String TIME_OVER = "You are trying to schedule visitor in past, ensure date and time";
    public static final String JWT_TOKEN_MISSING = "JWT Token missing";
    public static final String TIME_MISMATCH = "You are allowed to enter only during the scheduled time";
    public static final String VISITOR_NOT_APPROVED = "You have not been approved yet";
    public static final String JWT_TOKEN_EXPIRED = "Jwt token expired";
    public static final String VISITOR_BLACKLISTED = "You have been blacklisted";
    public static final String GATEKEEPER_CANNOT_CREATE_VISITOR = "Only residents can create and schedule for visitors";
    public static final String ALREADY_BLACKLISTED = "Already blacklisted by you";

}
