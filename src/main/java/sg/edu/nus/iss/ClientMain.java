package sg.edu.nus.iss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        String host = null;
        Integer port = null;

        if (args.length == 1) {
            String[] splitArgs = args[0].split(":");
            host = splitArgs[0];
            port = Integer.parseInt(splitArgs[1]);

            // open socket to connect with server
            Socket socket = new Socket(host, port);

            // open terminal for input
            Console console = System.console();
            String input = "";

            // send output once connected to server
            try (OutputStream os = socket.getOutputStream()) {
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                // get input once connected to server
                try (InputStream is = socket.getInputStream()) {
                    BufferedInputStream bis = new BufferedInputStream(is);
                    DataInputStream dis = new DataInputStream(bis);

                    while (!input.equals("close")) {
                        input = console.readLine();

                        dos.writeUTF(input);
                        dos.flush();

                        String msgReceived = dis.readUTF();
                        if (msgReceived.equals("cookie-text")) {
                            System.out.println(dis.readUTF());
                        }
                    }

                    dis.close();
                    bis.close();
                    is.close();
                } catch (EOFException e) {
                }

                dos.close();
                bos.close();
                os.close();
            } catch (EOFException e) {
            }

            socket.close();
        }
    }
}
