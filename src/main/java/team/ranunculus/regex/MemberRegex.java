package team.ranunculus.regex;

public final class MemberRegex {
    public static final String USER_EMAIL = "^(?=.{7,50})([\\da-zA-Z_.]{4,})@([\\da-z\\-]{2,}\\.)?([\\da-z\\-]{2,})\\.([a-z]{2,10})(\\.[a-z]{2})?$";
    public static final String USER_PASSWORD = "^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{8,50})$";
    public static final String USER_NAME = "^([가-힣]{2,5})$";
    public static final String USER_CONTACT = "^(\\d{8,12})$";

    public static final String CONTACT_AUTH_CODE = "^(\\d{6})$";
    public static final String CONTACT_AUTH_SALT = "^([\\da-z]{128})$";

    public static final String EMAIL_AUTH_CODE = "^([\\da-z]{128})$";

    private MemberRegex() {
    }
}