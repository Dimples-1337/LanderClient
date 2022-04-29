package mepacket.server;

import mepacket.Packet;

public final class SPacketMessage implements Packet {
    private final String message;

    public SPacketMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
