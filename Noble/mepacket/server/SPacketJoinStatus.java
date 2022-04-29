package mepacket.server;

import mepacket.Packet;

public final class SPacketJoinStatus implements Packet {
    private final int code;

    public SPacketJoinStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
