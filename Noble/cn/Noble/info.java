package cn.Noble;

public class info {
    public static void main(String[] args) {
        BPACVerify BPACVerify = new BPACVerify("120.25.86.80",53702);
        System.out.println(BPACVerify.verify(BPACVerify.getHWID()));
    }
}
