/*I had help from multiple sites for this homework. http://www.tutorialspoint.com/java/lang/throwable_printstacktrace_printstream.htm, Pipedwriter from https://docs.oracle.com/javase/7/docs/api/java/io/PipedWriter.html PipedReader from http://docs.oracle.com/javase/7/docs/api/java/io/PipedReader.html
I used http://tutorials.jenkov.com/java-io/pipes.html to understand pipes. I learned how to mimic a chat server from site http://www.dreamincode.net/forums/topic/346137-java-clientserver-chat-room-with-p2p/ I wrote the class for Client B but for some reason it wouldn't work and it would only show the text from Client A*/

import java.io.*;


public class Chat 
{
    
    public static void main(String[] args) throws Throwable 
    {
        
        PipedWriter pipewriter_a = new PipedWriter();//Client A writer
        PipedReader pipereader_a = new PipedReader(pipewriter_a);//Client A reader
        PipedWriter pipewriter_sa = new PipedWriter();//Server A Writer
        PipedReader pipereader_as = new PipedReader(pipewriter_sa);//Server A reader
        
        PipedWriter pipewriter_b = new PipedWriter();//Client B writer
        PipedReader pipereader_b = new PipedReader(pipewriter_b);//Client B reader
        PipedWriter pipewriter_sb = new PipedWriter();//Server B writer
        PipedReader pipereader_bs = new PipedReader(pipewriter_sb);//Server B reader
        
        
        ClientsA a = new ClientsA(); // Sets a new Client A object
        ClientsB b = new ClientsB(); // Sets a new Client B object
        Server s = new Server(); 	// Sets a new server
        
        a.setWriter_A(pipewriter_a);//Client A writer sends string to the pipe server
        a.setReader_A(pipereader_as);//Client A gets string from pipe server
        a.setReader_A(pipereader_as);//Client A gets string from pipe server
        
        b.setWriter_B(pipewriter_b);//Client B writer sends string to the pipe server
        b.setReader_B(pipereader_bs);//Client B gets string from pipe server
        b.setReader_B(pipereader_bs);//Client B gets string from pipe server
        
        s.setWriterA(pipewriter_sb);//Sever gets string from B to write into pipe to A
        s.setWriterB(pipewriter_sa);//Server gets string from A to write into pipe to B
        s.setReaderA(pipereader_a);//Server gets string from Client A to read for B
        s.setReaderB(pipereader_b);//Server gets string from Client B to read for A
        
        Thread threada = new Thread(a); //Creates a new thread for Client A
        threada.start();
        threada.join();
        Thread threadb = new Thread(b); //Creates a new thread for Client B
        threadb.start();
        threadb.join();
        Thread threads = new Thread(s); //Creates a new thread for the server
        threads.start();
        threads.join();
        
    }
    
}

class Server implements Runnable // Server class
{ 
    PipedWriter pipewriter_serverb;
    PipedReader pipereader_servera;
    PipedWriter pipewriter_servera;
    PipedReader pipereader_serverb;
    
    char a; // Char variable to convert from string serverA for message
    char b;// Char variable to convert from string serverB for message
    String serverA;
    String serverB;
    
    public void setWriterA(PipedWriter pipewriter_a) 
    {
        pipewriter_serverb = pipewriter_a;
    }
    
    public void setReaderA(PipedReader pipereader_a) 
    {
        pipereader_servera = pipereader_a;
    }
    
    public void setWriterB(PipedWriter pipewriter_b) 
    {
        pipewriter_servera = pipewriter_b;
    }
    
    public void setReaderB(PipedReader pipereader_b) 
    {
        pipereader_serverb = pipereader_b;
    }
    
    
    public void run()  // Runs thread
    {
        try 
        {
            while (true) 
            {
                
                if (pipereader_servera.ready())  // Checks if thread a is ready
                {
                    pipewriter_serverb.write(a = (char) pipereader_servera.read()); //Writes message from Client A to Client B
                }
                
                if (pipereader_serverb.ready()) 
                {
                    pipewriter_servera.write(b = (char) pipereader_serverb.read());//Writes message from Client B to Client A
                    
                }
                
            }
        } catch (Throwable e) // Catch block for errors
        { 
            e.printStackTrace();
        }
        
    }
}

class ClientsA implements Runnable  // Client A class
	{
    PipedReader pipereader;
    PipedWriter pipewriter;
    char c;
    String s = "Blah";
    String x;
    
    public void setWriter_A(PipedWriter pipewriter_a) 
    {
        
        pipewriter = pipewriter_a;
    }
    
    public void setReader_A(PipedReader pipereader_a) 
    {
        pipereader = pipereader_a;
    }
    
    
    
    
    public void run()  // Runs the thread and writes the string to pipe from Client B
    {
        String x = "";
        while (true) {
            try {
                int j = 0;
                if (pipereader.ready()) 
                {
                    while (pipereader.ready() && (j = pipereader.read()) != -1) {
                        if (pipereader.ready()) {
                            c = (char) (j);
                            x += c;
                        } else {
                            System.out.println("Client A received: " + x);
                            x = "";
                        }
                    }
                } else 
                {
                    pipewriter.write(s);
                    System.out.println("Client A said:" + s);
                    Thread.sleep((long) (Math.random() * 1000));//sleep function waits for random time
                }
            } catch (Throwable e) 
            {
                e.printStackTrace();
            }
        }
        
    }
    
}

class ClientsB implements Runnable 
{ // Client B class
    PipedReader pr;
    PipedWriter pipewriter;
    char c;
    String s = "How's it going, eh? ";
    String x = "";
    
    public void setWriter_B(PipedWriter pipewriter_b) 
    {
        pipewriter = pipewriter_b;
    }
    
    public void setReader_B(PipedReader pipereader_b) 
    {
        pr = pipereader_b;
        
    }
    
    public void run() { /// Runs the thread and writes the string to pipe from Client A
        while (true) 
        {
            try 
            {
                int j = 0;
                if (pr.ready()) 
                {
                    while (pr.ready() && (j = pr.read()) != -1) 
                    {
                        if (pr.ready()) 
                        {
                            c = (char) (j);
                            x += c;
                        } else {
                            System.out.println ("Client B received: " + x);
                            x = "";
                        }
                    }
                } else
                {
                    pipewriter.write(s);
                    System.out.println("Client B said:" + s);
                    Thread.sleep((long) (Math.random() * 1000));//sleep function waits for random time
                }
            } catch (Throwable e)  // Catch block for errors
            {
                e.printStackTrace();
            }
        }
        
    }
}