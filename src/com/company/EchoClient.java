package com.company;

import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        int portNumber = 12345;
        String hostName = "localhost";

        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
            String userInput;

            while ((userInput = stdIn.readLine()) != null) {

                Request req;
                Scanner sc = new Scanner(userInput);


                try {
                    switch (sc.next()) {
                        case "login" -> req = new LoginRequest(sc.skip(" ").nextLine());
                        case "post" -> req = new PostRequest(sc.skip(" ").nextLine());
                        case "find" -> req = new FindRequest(sc.skip(" ").nextLine());
                        case "read" -> req = new ReadRequest();
                        case "quit" -> req = new QuitRequest();
                        case "unsub" -> req = new UnsubscribeRequest(sc.skip(" ").nextLine());
                        case "sub" -> req = new SubscribeRequest(sc.skip(" ").nextLine());
                        default -> {
                            System.out.println("ILLEGAL COMMAND");
                            continue;
                        }
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("ILLEGAL COMMAND");
                    continue;
                }

                out.println(req);

                String serverResponse;
                if ((serverResponse= in.readLine())  == null) {
                    break;
                }
                Object json = JSONValue.parse(serverResponse);

                Response clientResponse;

                if ((clientResponse=SuccessResponse.fromJSON(json)) != null){
                    continue;
                } else if ((clientResponse = MessageListResponse.fromJSON(json)) != null) {

                    for (Message m : ((MessageListResponse)clientResponse).getMessages())
                        System.out.println(m);

                }else if ((clientResponse = ErrorResponse.fromJSON(json)) != null){
                    System.out.println((((ErrorResponse)clientResponse).getError()));



                }else{
                    break;
                }

                /*out.println(userInput);
                System.out.println("echo: " + in.readLine());*/



            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}