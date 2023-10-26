package com.company;

import org.json.simple.JSONValue;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;



// Server class
public class EnhancedEchoServer {



    public static int totalRequests;
    public static List<Message> messages = new ArrayList<>();
    public static List<Message> messagesClassList= new ArrayList<Message>();
    public static List<User> userList = new ArrayList<User>();
    public static void main(String[] args)
    {

        ServerSocket server = null;
        try {
            server = new ServerSocket(12345);
            server.setReuseAddress(true);
            while (true) {
                Socket client = server.accept();
                System.out.println("New client connected"
                                + client.getInetAddress()
                                        .getHostAddress());
                ClientHandler clientSocket = new ClientHandler(client);
                new Thread(clientSocket).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static class ClientHandler implements Runnable{
        private final Socket clientSocket;
        private PrintWriter out;
        private int id;
        private int intRead=0;
        private String loginName;
        private int read;
        private int requests=0;
        boolean login=false;
        private User userLogged;
        boolean ignore;
        private List<String> subscribed = new ArrayList<>();
        private BufferedReader in;
        // Constructor
        public ClientHandler(Socket socket) throws IOException
        {
                //this.id = idCreator.getInstance().getID();
                this.userLogged=null;
                this.clientSocket = socket;
                out= new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        }

        public void run(){
            try{
                messagesClassList.clear();
                String inputLine;
                final File folder = new File("channels");
                Message oldMessage;
                List<List<String>> reading;
                List<String> fileNames =reader.listFilesForFolder(folder);

                for(String s:fileNames){
                    reading = (reader.reader("channels//" + s));

                    for(List<String> li:reading) {
                        for (String str : li) {
                            String author, message, time, channel,data;
                            String[] splitColon = (str.split(":"));
                            author = splitColon[0];

                            data = (splitColon[1] + ":" + splitColon[2]);
                            time = data.substring(data.indexOf("(") + 1, data.indexOf(")"));

                            message = data.replace("(" + time + ")", "");
                            message = message.substring(1, message.length() - 1);

                            channel = s.substring(0, s.length() - 4);
                            /*System.out.println(author);
                            System.out.println(message);
                            System.out.println(time);
                            System.out.println(channel);*/

                            oldMessage = new Message(message,author,time,channel);
                            messagesClassList.add(oldMessage);
                        }
                    }
                }

                List<List<String>> users = reader.reader("users.csv");
                //System.out.println(users.get(0).get(0));
                //System.out.println(users);
                for(List<String> u:users){
                    String username = null;
                    String password = null;
                    List<String> subscribedChannels = new ArrayList<>();
                    for(String str:u){

                        if(username==null){
                            username=str;
                        }else if(password==null){
                            password = str;
                        }
                        else{
                            subscribedChannels.add(str);
                        }
                    }
                    System.out.println(username);
                    System.out.println(password);
                    System.out.println(subscribedChannels);
                    User user = new User(username,password,subscribedChannels);
                    userList.add(user);



                }
                while ((inputLine = in.readLine()) != null) {

                    System.out.println(inputLine);

                            // This loop body is solution to exercise 2, parts 1-4.
                    String command = "";
                    //String message = "";
                    //String channel="";

                    //C:\Users\spike\OneDrive\Desktop\Year2Documents\NSDLabWeek10
                    Object json = JSONValue.parse(inputLine);
                    Request req;
                    /*
                    try {
                        command = sc.next();
                        message = sc.skip(" ").nextLine();
                        channel = sc.skip(" ").nextLine();
                    } catch (NoSuchElementException e) {}
                    */


                    System.out.println(login);
                    System.out.println(LoginRequest.fromJSON(json) != null);
                    System.out.println(FindRequest.fromJSON(json)!=null);
                    System.out.println(PostRequest.fromJSON(json) != null);
                    System.out.println(ReadRequest.fromJSON(json) != null);
                    System.out.println(SubscribeRequest.fromJSON(json) != null);
                    System.out.println(UnsubscribeRequest.fromJSON(json) != null);
                    if((req = LoginRequest.fromJSON(json)) == null&&(!login)){
                        out.println(new ErrorResponse("Not logged in"));
                    }
                    else if((req = LoginRequest.fromJSON(json)) != null&&(!login)){
                        loginName = ((LoginRequest)req).getName();
                        String password;

                        int delimIndex = loginName.lastIndexOf(" ");
                        if (delimIndex == -1) {
                            System.out.println("");
                        }
                        password = loginName.substring(delimIndex+1,loginName.length());
                        loginName = loginName.substring(0,delimIndex);
                        System.out.println(loginName);
                        System.out.println(password);

                        User user = null;
                        for(User u:userList){

                            if(u.getUsername().equals(loginName)){
                                user=u;

                                if(u.getPassword().equals(password)){

                                    subscribed=u.getSubscribed();
                                    System.out.println(subscribed);
                                    this.userLogged=user;
                                    login = true;
                                }


                            }
                            if(user!=null){
                                break;
                            }
                        }
                        if(user==null){
                            subscribed.add("default");
                            user = new User(loginName,password,subscribed);
                            userList.add(user);
                            this.userLogged=user;
                            write.write(user,"users.csv",true);
                            login = true;
                        }


                        out.println(new SuccessResponse());
                    } else if ((req = PostRequest.fromJSON(json)) != null &&login) {


                        String message = ((PostRequest)req).getMessage();


                        int delimIndex = message.lastIndexOf(" ");
                        if (delimIndex == -1) {
                            System.out.println("");
                        }
                        String channel = message.substring(delimIndex+1,message.length());
                        message = message.substring(0,delimIndex);

                        System.out.print("Message " + message+"\n");
                        System.out.println("Channel " + channel+"\n");
                        Instant time = Instant.now();
                        String times = time.toString();

                        System.out.println(time);
                        String timestamp =times.substring(0,10)+" "+ times.substring(11,16);
                        messagesClassList.add( new Message(message,userLogged.getUsername(), timestamp,channel));
                        System.out.println(messagesClassList.toString());

                        write.write(messagesClassList.get(messagesClassList.size()-1),"channels\\" + channel +".csv",true);

                        //messages.add(message);
                        //names.add(loginName);
                        out.println(new SuccessResponse());
                        requests+=1;
                        totalRequests+=1;
                    } else if ((req = SubscribeRequest.fromJSON(json)) != null || (req = UnsubscribeRequest.fromJSON(json)) != null&&login) {
                        if(req instanceof SubscribeRequest) {
                            if (!subscribed.contains((((SubscribeRequest) req).getChannel()))) {
                                String channel = ((SubscribeRequest) req).getChannel();

                                subscribed.add(channel);
                                userLogged.setSub(subscribed);
                            }
                        }else{
                            if (subscribed.contains((((UnsubscribeRequest) req).getChannel()))) {
                                String channel = ((UnsubscribeRequest) req).getChannel();

                                subscribed.remove(channel);
                                userLogged.setSub(subscribed);
                            }
                        }
                        for(User u:userList){
                            if(u.getUsername().equals(userLogged.getUsername())){
                                u=userLogged;
                            }
                        }
                        System.out.println(userList);
                        write.write(userList,"users.csv",false);
                        out.println(new SuccessResponse());
                        requests+=1;
                        totalRequests+=1;

                    }else if (ReadRequest.fromJSON(json) != null&&login) {
                        System.out.println(subscribed.toString());
                        /*while(intRead < messages.size()){
                            message = message + messages.get(intRead) + " from: "+ names.get(intRead) + " " ;
                            System.out.println(message);
                            intRead++;
                                    
                        }
                        out.println(message);*/
                        List<Message> msgs;
                        // synchronized access to the shared message board
                        msgs = messagesClassList;
                        System.out.println(msgs.toString());

                        // adjust read counter
                        for(int i = msgs.size()-1;i>=0;i--){
                            if(!subscribed.contains((msgs.get(i)).getChannel() )){
                                msgs.remove(i);
                            }
                            //System.out.println((msgs.get(i)).getChannel());
                            //System.out.println(subscribed.contains((msgs.get(i)).getChannel() ));
                        }

                        // response: list of unread messages
                        //System.out.println(msgs.toString());
                        System.out.println(msgs);

                        //out.println(new SuccessResponse());
                        out.println(new MessageListResponse(msgs));



                        requests+=1;
                        totalRequests+=1;
                        continue;
                    } else if (QuitRequest.fromJSON(json) != null&&login) {
                        in.close();
                        out.close();
                        requests+=1;
                        totalRequests+=1;
                        break;
                    }else if((req = FindRequest.fromJSON(json)) != null &&login){
                        messages.clear();
                        String message = ((FindRequest)req).getMessage();


                        /*int delimIndex = message.lastIndexOf(" ");
                        if (delimIndex == -1) {
                            System.out.println("");
                        }
                        String channel = message.substring(delimIndex+1,message.length());
                        message = message.substring(0,delimIndex);*/

                        for(Message M: messagesClassList){
                            System.out.println(M);
                            System.out.println(M.getBody());
                            if(message.equals(M.getBody())){
                                System.out.println("Found");
                                messages.add(M);
                                System.out.println(M.getChannel());

                                out.println(new MessageListResponse(messages));
                            }
                            else{
                                out.println(new ErrorResponse("No message found"));
                            }
                        }
                        requests+=1;
                        totalRequests+=1;
                    } else {
                        out.println(new ErrorResponse("ILLEGAL REQUEST"));
                    }
                    if(login){
                        subscribed=userLogged.getSubscribed();
                        System.out.println(messagesClassList.toString());
                        System.out.println("Amount of valid requests from " + userLogged.getUsername() + " is " + requests + " requests\n");
                        System.out.println("Amount of valid requests from all clients: " + totalRequests+ "\n");
                    }




                    }

            }catch(IOException io){
                System.out.println(io);
            }


        }
    }
    public static class idCreator {
        private static idCreator instance = null;
        public static idCreator getInstance(){
            if(instance ==null){
                instance = new idCreator();
            }
            return instance;
        }

        private static int nextID=0;
        public static int getID(){
            if(nextID < 0){
                throw new IllegalStateException("Out of IDs");
            }
            int uniqueID = nextID;
            nextID++;
            return uniqueID;


        }
    }



}