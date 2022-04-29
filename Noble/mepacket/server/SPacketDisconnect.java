package mepacket.server;

import mepacket.Packet;

public final class SPacketDisconnect implements Packet {
    private final String reason;

    public SPacketDisconnect(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
