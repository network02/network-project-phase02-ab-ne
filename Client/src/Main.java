import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;



public class Main {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 21;
    private static final int DATA_PORT = 20; // Port for data connection

    private static ServerSocket welcomeSocket;
    private static Socket controlSocket;
    private static BufferedReader controlIn;

    private static PrintWriter controlOutWriter;

    private static boolean useDataSocket = false;
    private static Socket dataSocket;

    public static void main(String[] args) {


        try {
            // اتصال به سرور
            controlSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the FTP server");

            // دریافت و ارسال داده‌ها از و به سرور

            //DataOutputStream dataOutputStream = new DataOutputStream(controlSocket.getOutputStream());
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));


            controlIn = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
            controlOutWriter = new PrintWriter(controlSocket.getOutputStream(), true);




            try {

                // دریافت خوشامد و راهنما
                String serverResponse;
                while (true){
                    serverResponse = controlIn.readLine();
                    if (serverResponse.equals(".") || serverResponse==null)
                        break;
                    System.out.println("Server: " + serverResponse);
                }

            } catch (IOException e) {
                e.printStackTrace();
                // مدیریت خطاهای ورودی-خروجی در اینجا
            }


            // خواندن و ارسال دستورات به سرور
            while (true) {
                System.out.print("Enter a command (QUIT to exit): ");
                String command = consoleInput.readLine();
                controlOutWriter.println(command);


                // چک کردن برای خاتمه اجرای برنامه
                if (command.equalsIgnoreCase("QUIT")) {
                    System.out.println("Closing connection to the FTP server.");
                    break;
                }

                // دریافت و چاپ پاسخ از سرور


                if (command.equalsIgnoreCase("LIST")) {
                    dataSocket=ListenToServer();
//                    dataSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    receiveData(dataSocket);
                    //printAllMsg(1,controlIn);
                    printAllByWhile(controlIn);
                }
                else {
                    printAllByWhile(controlIn);
                }


//
//                String response = controlIn.readLine();
//                System.out.println("Server: " + response);

            }

            // بستن اتصالات
            controlIn.close();
            controlOutWriter.close();
            consoleInput.close();
            controlSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private static void printAllMsg(int n,BufferedReader controlIn) throws IOException {

        for (int i = 0; i < n; i++) {
            String response = controlIn.readLine();
            System.out.println("Server: " + response);
        }
    }
    private static void printAllByWhile(BufferedReader controlIn) throws IOException {
        boolean continueReading = true;

        while (continueReading) {
            // خواندن پیام از سرور
            String response = controlIn.readLine();

            // چاپ پیام در کنسول
            System.out.println("Server: " + response);

            // اگر پیام "exit" باشد، حلقه خاتمه یافته و برنامه خاتمه می‌یابد
            if ("226 Transfer complete.".equalsIgnoreCase(response.trim())) {
                continueReading = false;
            }
        }

    }

    private static Socket ListenToServer(){

        try {
            welcomeSocket = new ServerSocket(DATA_PORT);


        } catch (IOException e) {
            System.out.println("Could not create server socket");
            System.exit(-1);
        }

        System.out.println("FTP DATA connection started listening on port " + DATA_PORT);

        while (true) {

            try {

                Socket DataConnectionSocket = welcomeSocket.accept();
                System.out.println("New Data ConnectionSocket received. Data ConnectionSocket was created.");
                if (DataConnectionSocket!=null)
                        return DataConnectionSocket;


            } catch (IOException e) {
                System.out.println("Exception encountered on accept");
                e.printStackTrace();
            }



        }



    }
    private static void receiveData(Socket dataSocket) throws IOException {

        try {
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

            while (true) {
                String dataResponse = dataIn.readLine();
                if (dataResponse == null || dataResponse.equals(".")) {
                    break;
                }
                System.out.println("Data Server: " + dataResponse);
            }

            // بستن اتصال دیتا
            dataIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

