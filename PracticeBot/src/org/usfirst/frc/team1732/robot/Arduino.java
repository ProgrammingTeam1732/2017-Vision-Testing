

package org.usfirst.frc.team1732.robot;
import gnu.io.*;
import edu.wpi.first.wpilibj.SerialPort;
public class Arduino {
    /*
     * Hopefully, i will be able to have an arduino be hooked up to the robot
     * and communicate with the serial port.  I can use this to do many things,
     * such as maintain a LCD screen and display data on it to help the electrical
     * and mechanical teams figure out what went wrong.
     *
     * In all realism, it will most likely just be a fun cool thing for me to
     * play with :D
     */
    private SerialPort serial;

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

    public String getData() {
        try {
            return this.serial.readString();
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

    public int requestData(String request) {
        try {
        	System.out.println(request);
            return Integer.parseInt(this.getData());
        } catch (Exception e) {
            System.out.println("something went wrong, " + e.getMessage());
            return 0;
        }
    }
    public static void main(String[] args){
    	new Arduino();
    }
}