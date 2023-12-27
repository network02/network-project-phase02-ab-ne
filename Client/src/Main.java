import java.io.*;

import java.net.Socket;



public class Main {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1025;
    private static final int DATA_PORT = 1026; // Port for data connection

    private static BufferedReader controlIn;

    private static PrintWriter controlOutWriter;

    private static boolean useDataSocket = false;//j
    private static Socket dataSocket;

    public static void main(String[] args) {


        try {
            // اتصال به سرور
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the FTP server");

            // دریافت و ارسال داده‌ها از و به سرور

            //DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));


            controlIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            controlOutWriter = new PrintWriter(socket.getOutputStream(), true);




            try {

                // دریافت خوشامد
                System.out.println("Server: " + controlIn.readLine());

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
                String response = controlIn.readLine();
                System.out.println("Server: " + response);


                // اگر دستور LIST اجرا شده باشد، پاسخ‌های بیشتر را بخوانید
                if (command.equalsIgnoreCase("LIST")) {
                    useDataSocket = true;
//
//                    while (true) {
//                        String dataResponse = controlIn.readLine();
//                        if (dataResponse == null || dataResponse.equals(".")) {
//                            break;
//                        }
//                        System.out.println("Data Server: " + dataResponse);
//                    }
                }
                else {
                    useDataSocket = false;
                }
                if (useDataSocket) {
                    receiveData();
                }
            }

            // بستن اتصالات
            controlIn.close();
            controlOutWriter.close();
            consoleInput.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void receiveData() throws IOException {
        dataSocket = new Socket(SERVER_ADDRESS, DATA_PORT);
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

