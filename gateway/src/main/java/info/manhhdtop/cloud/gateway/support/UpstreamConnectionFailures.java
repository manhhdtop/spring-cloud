package info.manhhdtop.cloud.gateway.support;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.nio.channels.ClosedChannelException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.web.reactive.function.client.WebClientRequestException;

public final class UpstreamConnectionFailures {

    private static final Pattern CONNECTION_REFUSED_SERVICE =
            Pattern.compile("Connection refused:\\s*([^/\\s]+)", Pattern.CASE_INSENSITIVE);

    private UpstreamConnectionFailures() {
    }

    public static boolean isUpstreamUnavailable(Throwable ex) {
        for (Throwable current = ex; current != null; current = current.getCause()) {
            if (current instanceof ConnectException
                    || current instanceof UnknownHostException
                    || current instanceof NoRouteToHostException
                    || current instanceof ClosedChannelException
                    || current instanceof WebClientRequestException) {
                return true;
            }
            if (current instanceof NotFoundException notFound
                    && containsConnectionFailureMessage(notFound.getReason())) {
                return true;
            }
            if (containsConnectionFailureMessage(current.getMessage())) {
                return true;
            }
        }
        return false;
    }

    public static String extractServiceName(Throwable ex) {
        for (Throwable current = ex; current != null; current = current.getCause()) {
            String fromMessage = extractFromMessage(current.getMessage());
            if (fromMessage != null) {
                return fromMessage;
            }
        }
        return "upstream";
    }

    private static boolean containsConnectionFailureMessage(String message) {
        if (message == null || message.isBlank()) {
            return false;
        }
        String lower = message.toLowerCase();
        return lower.contains("connection refused")
                || lower.contains("connection reset")
                || lower.contains("failed to connect")
                || lower.contains("no route to host")
                || lower.contains("unable to find instance")
                || lower.contains("503 service unavailable");
    }

    private static String extractFromMessage(String message) {
        if (message == null) {
            return null;
        }
        Matcher matcher = CONNECTION_REFUSED_SERVICE.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
