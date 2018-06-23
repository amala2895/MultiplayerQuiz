package multiplayerquiz;
import java.io.*;
import static java.lang.System.out;
import java.util.*;
import java.net.*;

public class  Server {
  Vector<String> users = new Vector<String>();
  Vector<HandleClient> clients = new Vector<HandleClient>();

  public void process() throws Exception  {
      ServerSocket server = new ServerSocket(9999,10);
      out.println("Server Started...");
      while( true) {
 		 Socket client = server.accept();
 		 HandleClient c = new HandleClient(client);
  		 clients.add(c);
     }  // end of while
  }
  public static void main(String ... args) throws Exception {
      new Server().process();
  } // end of main

  public void boradcast(String user, String message)  {
	    // send message to all connected users
	    for ( HandleClient c : clients )
	       if ( ! c.getUserName().equals(user) )
	          c.sendMessage(user,message);
  }

  class  HandleClient extends Thread {
        String name = "";
	BufferedReader input;
	PrintWriter output;

	public HandleClient(Socket  client) throws Exception {
         // get input and output streams
	 input = new BufferedReader( new InputStreamReader( client.getInputStream())) ;
	 output = new PrintWriter ( client.getOutputStream(),true);
	 // read name
	 name  = input.readLine();
	 users.add(name); // add to vector
         System.out.println(name+" is online now");
	 start();
        }

        public void sendMessage(String uname,String  msg)  {
	    output.println( uname + ":" + msg);
	}
		
        public String getUserName() {  
            return name; 
        }
        public void run()  {
    	     String line;
             String[] questions = new String[]{"Q1: I m lovin it", "Q2: Think Different","Q3: Taste the Thunder","Q4: Taste bhi, Health bhi","Q5: Share moments, share life","Have it your way","Finger Lickin' Good","Buy it. Sell it. Love it","The taste of India","The Complete Man","The mint with a hole","Tyres With Muscle","The King of Good Times","No one can eat just one","Earth's Biggest BookStore","High Performance. Delivered","Beyond the Obvious","The Joy of Flying","Always low prices. Always."," Shop. Eat. Celebrate.","Solutions for a small planet","Connecting People","The choice of new generation","Eat fresh","Drivers Wanted"};
            String[] answers = new String[]{"McDonalds","Apple","Thums up","Maggi","Kodak","Burger King","KFC","ebay","Amul","Raymond","Polo","MRF","Kingfisher","Lays","Amazon","Accenture","TCS","Jet Airways","Walmart","Central","IBM","Nokia","Pepsi","Subway","Volkswagen"};
            int score=0,i=0;
            
	     try    {
                 output.println("Enter ready to begin your quiz");
                 line=input.readLine();
                 if(line.equalsIgnoreCase("ready")){
                     output.println("Name the brands corresponding to the following taglines");
                while(i!=5)   {
                   output.println(questions[i]);//send question 
		 line = input.readLine();//receive answer
		 if ( line.equals("end") ) {
		   clients.remove(this);
		   users.remove(name);
		   break;
                 }
                 if(line.equalsIgnoreCase(answers[i]))
                 {
                     score++;
                     output.println("correct");
                 }
                 else
                     output.println("wrong");
                 i++;
		 //boradcast(name,line); // method  of outer class - send messages to all
	       } // end of while
                output.println("Quiz has ended");
                output.println("Your score is "+score);
                clients.remove(this);
                users.remove(name);}
	     } // try
	     catch(Exception ex) {
	       System.out.println(ex.getMessage());
	     }
        } // end of run()
   } // end of inner class

} // end of Server
