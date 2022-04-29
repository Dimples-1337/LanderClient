package mepacket.client;

import mepacket.Packet;

public final class CPacketJoin implements Packet {
    private final String username;
    private final String hwid;

    public CPacketJoin(String username, String hwid) {
        this.username = username;
        this.hwid = hwid;
    }

    public String getUserName() {
        return username;
    }

    public String getHWID() {
        return hwid;
    }
}
