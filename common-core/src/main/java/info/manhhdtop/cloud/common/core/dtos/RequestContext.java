package info.manhhdtop.cloud.common.core.dtos;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
public class RequestContext {
    private static final ThreadLocal<RequestContext> CTX = ThreadLocal.withInitial(RequestContext::new);

    private Long userId;
    private Long sessionId;
    private Set<String> roles = new HashSet<>();
    private Set<String> permissions = new HashSet<>();
    private String requestId;

    public static RequestContext get() {
        return CTX.get();
    }

    public static void clear() {
        CTX.remove();
    }

    public static Long userId() {
        return CTX.get().userId;
    }

    public static Long sessionId() {
        return CTX.get().sessionId;
    }

    public static Set<String> roles() {
        return CTX.get().roles;
    }

    public static Set<String> permissions() {
        return CTX.get().permissions;
    }

    public static String traceId() {
        return CTX.get().requestId;
    }
}
