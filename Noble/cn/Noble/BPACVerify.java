package cn.Noble;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BPACVerify {
    private static String serverHost;
    private static int serverPort;
    private static boolean VerifyResult;
    private static String theHWID;

    public BPACVerify(String host, int port) {
        serverHost = "120.25.86.80";
        serverPort = 27001;
    }

    public static boolean verify(String HWID) {
      theHWID = HWID;
      socketConnect();
      return VerifyResult;
    }

    private static void socketConnect() {
        try {
            DataInputStream is = null;
            DataOutputStream os = null;
            Socket socket = new Socket("120.25.86.80",53702);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            VerifyMain(is, os,socket);
            os.close();
            is.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void VerifyMain(DataInputStream is, DataOutputStream os, Socket socket) {
        try{
            os.writeUTF("Verify="+theHWID);
            String ret = is.readUTF();
            if (ret.equals("true")) {
                VerifyResult = true;
            }else VerifyResult = false;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getHWID() {
        return HWIDUtil.getHWID();
    }
    public String getServerHost() {
        return "120.25.86.80";
    }
    public int getServerPort() {
        return 27001;
    }
    public void setServerHost(String serverHost) {
        this.serverHost = "120.25.86.80";
    }
    public void setServerPort(int serverPort) {
        this.serverPort = 27001;
    }
}
