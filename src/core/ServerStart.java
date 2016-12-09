package core;

public class ServerStart {
    public static void main(String[] args) {
        Server alphaServer = new Server("alphaRegister.csv", 61616);
        Server betaServer = new Server("betaRegister.csv", 61615);

        alphaServer.startServer();
        betaServer.startServer();
    }
}
