package info.manhhdtop.cloud.gateway.support;

import java.net.ConnectException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpstreamConnectionFailuresTest {

    @Test
    void detectsConnectionRefused() {
        var ex = new RuntimeException("Connection refused: auth/172.20.0.8:8001");
        assertTrue(UpstreamConnectionFailures.isUpstreamUnavailable(ex));
        assertEquals("auth", UpstreamConnectionFailures.extractServiceName(ex));
    }

    @Test
    void detectsConnectException() {
        var ex = new ConnectException("Connection refused");
        assertTrue(UpstreamConnectionFailures.isUpstreamUnavailable(ex));
    }
}
