

package org.usfirst.frc.team1732.robot;
import java.io.IOException;

//import gnu.io.*;
import edu.wpi.first.wpilibj.SerialPort;
public class Arduino {
    private SerialPort serial;
    public volatile int sig;
    public volatile int x;
    public volatile int y;
    public volatile int width;
    public volatile int height;
    public Arduino() {
        try {
            serial = new SerialPort(9600, SerialPort.Port.kUSB1);
            this.serial.disableTermination();
            System.out.println("Arduino Starting, waiting 0.5 seconds to get data");
            String e = this.getData();
            edu.wpi.first.wpilibj.Timer.delay(0.125);

            if(e.equals("h")) {
                System.out.println("Arduino communications locked in");
            }
        }
        catch (Exception e) {
            System.out.println("something went wrong, " + e.getMessage());
        }
    }
    private String buffer = "";
    //private String total = "";
	//private boolean dollar = false;
    public String getData() {
        try {
        	//serial.enableTermination();
        	String s = this.serial.readString();
        	while(!s.contains("\n"))// || s.length() < 35 || !s.contains("Detected")) 
        		s += this.serial.readString();
        	return s;
        	//return this.serial.readString();
        	/*if(s.contains("$") && s.contains("^"))
        		return s.substring(s.indexOf('$'), s.indexOf('^'));
        	else if(s.contains("$"))
        		return s.substring(s.indexOf('$'));
        	else if(s.contains("^"))
        		return s.substring(0, s.indexOf('^'));
        	else{
        		return s;
        	}*/
        	//boolean carrot = false;
        	/*for(int i = 0; i < s.length(); i++){
        		char c = s.charAt(i);
        		if(dollar){
        			total += c;
        		}
        		if(c == '$'){ 
        			dollar = true;
        		}else if(c == '^'){
        			dollar = false;
        			return total;
        		}
        	}
        	return total;*/
        } catch (Exception e) {
            System.out.println("something went wrong, " + e.getMessage());
            return null;
        }
    }

	public boolean sendData(byte[] buffer) throws Exception {
        try {
            int count = buffer.length;
            this.serial.write(buffer, count);
            return true;
        } catch (Exception e) {
            System.out.println("something went wrong, " + e.getMessage());
            return false;
        }
    }
    
    public boolean printf(String data) {
        try {
        	System.out.println(data);
            return true;
        } catch (Exception e) {
            System.out.println("something went wrong, " + e.getMessage());
            return false;
        }
    }

    public String requestData() {
        try {
        	System.out.println("request");
            return this.serial.readString();
        } catch (Exception e) {
            System.out.println("something went wrong, " + e.getMessage());
            return null;
        }
    }

//    public int requestData(String request) {
//        try {
//        	System.out.println(request);
//            return Integer.parseInt(this.getData());
//        } catch (Exception e) {
//            System.out.println("something went wrong, " + e.getMessage());
//            return 0;
//        }
//    }
    //public static void main(String[] args){
    //	new Arduino();
    //}
}