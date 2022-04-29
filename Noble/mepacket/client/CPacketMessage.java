package mepacket.client;

import mepacket.Packet;

public final class CPacketMessage implements Packet {
    private final String message;

    public CPacketMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
