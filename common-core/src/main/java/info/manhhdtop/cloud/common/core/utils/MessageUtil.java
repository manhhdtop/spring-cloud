package info.manhhdtop.cloud.common.core.utils;

public final class MessageUtil extends AbstractMessageUtil {
    private static final String BASE_NAME = "messages";

    static {
        init();
    }

    private MessageUtil() {
        super(BASE_NAME);
    }

    public static void init() {
        if (BASE_NAME == null) {
            new MessageUtil();
        }
    }
}
