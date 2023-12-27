import java.io.*;

import java.net.Socket;



public class Main {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1025;
    private static BufferedReader controlIn;

    private static PrintWriter controlOutWriter;


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
}

