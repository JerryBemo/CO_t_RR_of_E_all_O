/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coor;

import javax.mail.MessagingException;

/**
 *
 * @author Gerardo Becerril
 */
public class CorreoHotmail {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MessagingException {
            Hotmail h = new Hotmail();
            boolean i=h.envioDatos();
        // TODO code application logic here
    }
    
}
