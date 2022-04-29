package cn.Noble.Util;

import mepacket.Packet;
import mepacket.client.CPacketAlive;
import mepacket.client.CPacketExit;
import mepacket.client.CPacketJoin;
import mepacket.client.CPacketMessage;
import mepacket.server.SPacketDisconnect;
import mepacket.server.SPacketJoinStatus;
import mepacket.server.SPacketMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import javax.swing.*;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NobleIO extends Thread implements Closeable {
    public static NobleIO Instance;

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private final KeepAliveSendThread keepAliveSendThread = new KeepAliveSendThread();

    private volatile boolean closed = false;

    private final String username;

    public NobleIO(String username) {
        this.username = username;
        Instance = this;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        try {
            socket = new Socket("127.0.0.1", 1314);
            oos = new ObjectOutputStream(socket.getOutputStream());
            sendPacket(new CPacketJoin(username,"123"));

            ois = new ObjectInputStream(socket.getInputStream());

            Object o;
            while ((o = ois.readObject()) != null) {
                if (o instanceof SPacketJoinStatus) {
                    final SPacketJoinStatus packet = (SPacketJoinStatus) o;

                    if (packet.getCode() == -1) {
                        System.exit(0);
                        return;
                    }

                    break;
                }
            }

            keepAliveSendThread.start();
        } catch (Throwable e) {
            e.printStackTrace();

            close();

            System.exit(0);
        }

        try {
            Object o;

            while (!isInterrupted()) {
                if (closed) break;
                o =  ois.readObject();

                if (o instanceof SPacketDisconnect) {
                    final SPacketDisconnect packet = (SPacketDisconnect) o;

                    System.out.println(packet.getReason());

                    close();
                    break;
                } else if (o instanceof SPacketMessage) {
                    final SPacketMessage packet = (SPacketMessage) o;

                    if (Minecraft.getMinecraft() != null) {
                        if (Minecraft.getMinecraft().player != null) {
                            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(packet.getMessage()));
                        }
                    }
                }

                try {
                    sleep(1);
                } catch (InterruptedException ignored) {}
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void sendPacket(Packet packet) {
        try {
            oos.writeObject(packet);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (closed) return;

        closed = true;

        keepAliveSendThread.interrupt();

        try {
            try {
                oos.writeObject(new CPacketExit());
            } catch (Throwable e) {
                e.printStackTrace();
            }

            ois.close();
            oos.close();
            socket.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        interrupt();
    }

    private final class KeepAliveSendThread extends Thread {
        public KeepAliveSendThread() {
            super("KeepAliveSendThread");
            setDaemon(true);
        }

        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            while (!isInterrupted()) {
                sendPacket(new CPacketAlive());

                try {
                    sleep(5000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
