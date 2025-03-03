package com.ravindercodes.constant;

public class MessagesConstants {

    /**
     * In below constant is used for success message
     **/
    public static final String VERIFICATION_EMAIL_SENT = "A verification email has been sent to your registered email address.";
    public static final String LOGIN_SUCCESSFULLY = "Login successfully!";
    public static final String TOKEN_VALIDATE_SUCCESSFULLY = "Token validate successfully!";
    public static final String RECORD_SAVED = "Record saved successfully!";
    public static final String EMAIL_VERIFIED_SUCCESSFULLY = "Email verified successfully!";
    public static final String USER_DISABLED_VERIFICATION_EMAIL_SENT = "Your account has been disabled. A verification email has been sent to your registered email address.";
    public static final String RESET_PASSWORD_EMAIL_SENT = "Password reset link sent to your email.";
    public static final String RESET_PASSWORD_SUCCESSFULLY = "Your password has been reset successfully.";
    public static final String FETCH_RECORD_SUCCESSFULLY = "Record fetch successfully!";
    public static final String USER_LOGOUT_FROM_DEVICE = "Logout from device successfully!";
    public static final String USER_LOGOUT_FROM_ALL_DEVICE = "Logout from all device successfully!";
    public static final String REFRESH_TOKEN_GENERATED_SUCCESSFULLY = "Refresh token generated successfully!";
    public static final String REFRESH_TOKEN_GENERATED_FAILED = "Refresh token generated failed";

    /**
     * In below constant is used for exception message
     **/
    public static final String JWT_GENERATION_EX = "JWT generation failed for user: {}";
    public static final String USERNAME_ALREADY_EXIT = "Username already exit: {}";
    public static final String EMAIL_ALREADY_EXIT = "Email already exit: {}";
    public static final String ROLE_NOT_FOUND = "Role not found with ID: {}";
    public static final String EMAIL_VERIFICATION_FAILED = "Email verification failed: {}";
    public static final String USER_NOT_FOUND = "User not found : {}";
    public static final String INVALID_RESET_TOKEN = "Invalid or expired reset token. Please request a new password reset link.";
    public static final String INVALID_TOKEN = "Invalid or expired token. Please request a new one: {}";
    public static final String RECORD_NOT_FOUND = "Record not found: {}";
    public static final String IP_ADDRESS_BLOCKED = "IP address blocked. Please try after %d minutes.";

    /**
     * Email subject
     */
    public static final String SUBJECT_VERIFICATION_EMAIL = "Springforge email verification";
    public static final String SUBJECT_RESET_PASSWORD = "Springforge reset password";


}
