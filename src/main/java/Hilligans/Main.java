package Hilligans;


import Hilligans.Network.ServerNetworkInit;

public class Main {

    //time until token expires after being generated
    public static final int TOKEN_VALID_SECONDS = 14400;
    //time required after checking if a token is valid until the method can be called again
    public static final int TOKEN_VERIFY_DELAY = 5;

    public static String port = "25588";

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Server on port: " + port);
        ServerNetworkInit.startServer(port);
    }


}
