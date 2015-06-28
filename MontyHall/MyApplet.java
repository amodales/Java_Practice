//MyApplet.java -- @CopyLeft by tsaiwn@csie.nctu.edu.tw
// provide some good utility functions for Applets
// as long as some static utility functions for Applications
//All functions begins with "new" are for Application only
///
import java.applet.*; 
import java.awt.*;  import java.awt.image.*;
import java.io.*; import java.net.*;
import javax.swing.*;

public class MyApplet extends Applet {

   public MyApplet( ) {   // nothing to do
   }

   public void start( ) {
       Class o = getClass( );
       printf("MyApplet_start: my class name from getclass( ) = "+ o +"\n");
       super.start( );
   }

 /// newCodeBase() --- get the user's current working Directory
   static public URL newCodeBase( ) {    // for Application
       URL codeBase = null;
       try {
          codeBase = new URL("file:" + System.getProperty("user.dir") + "/");
       } catch ( Exception e ) {
          // maybe it is an Applet, use getCodeBase() or getDocumentBase()
         /// but they only works in an Applet hosted in a Browser
       }
       return codeBase;
   }
 /// same as newCodeBase() --- get the user's current working Directory
   static public URL getMyCodeBase( ) {    // for Application
       return newCodeBase( );
   }
 /// same as newCodeBase() --- get the user's current working Directory
   static public URL getCWD( ) {    // get Current Working Directory
       return newCodeBase( );
   }

 /// getPath, only works for Applet
   public URL getPath(String filename) {
      URL url = null;
          try {
             Toolkit tkt = Toolkit.getDefaultToolkit( );
             Class me = getClass( );   // get The class is running
             url = me.getResource(filename); // for Applet
          } catch ( Exception e2) {;} // ignore
      return url;
   }
 /// newGetPath, static version of getPath
   static public URL newGetPath(String filename) {
      URL url = null;
          try {
             Toolkit tkt = Toolkit.getDefaultToolkit( );
             url = new URL( newCodeBase( ) + filename );
          } catch ( Exception e2) {;} // ignore
      return url;
   }

 /// getAudioBoth(filename) --- get AudioClip from Application or Applet
   public AudioClip getAudioBoth(String filename) {
       AudioClip audio = null;
       try { // try Application first
          audio = newAudio(newCodeBase( ), filename);
       } catch (Exception e) { // then try Applet
       }
       if(audio == null) {   // try Applet
          try{ audio = getAudioClip(getCodeBase( ), filename);
          } catch ( Exception e ) {;}
       }
       return audio;   // send it back anyway
   } // getAudioBoth

 /// newAudio(filename) --- get AudioClip from an Application (not Applet)
   static public AudioClip newAudioStatic(String filename) {
       AudioClip audio = null;
       try { 
          audio = newAudio(newCodeBase( ), filename);
       } catch (Exception e) { ; }
       return audio;
   } // newAudio(filename)
///
   public AudioClip newAudioBoth(String filename) {   /// NOT static
       AudioClip audio = null;
       try {   // 2004/06/08
          Toolkit tkt = Toolkit.getDefaultToolkit( );
          Class me = getClass( );   // get The class is running
          URL url = me.getResource(filename);
          audio = Applet.newAudioClip( url );
       } catch (Exception e) { ; }
       return audio;
   } // newAudioBoth(filename)
 /// newAudio(url, filename)
   static public AudioClip newAudio(URL base, String filename) {
       AudioClip audio = null;
       try{ audio = Applet.newAudioClip( new URL(base+ filename) );
       } catch ( Exception e ) {
          // maybe it is an Applet, you should use getAudioClip(URL url) 
          // .. or getAudioClip(URL url, String filename)  in Applet
       }
       return audio;
   } // newAudio(url, filename)
 /// newAudio(url)  --- get AudioClip from an Application (not Applet)
   static public AudioClip newAudio(URL url) {
       AudioClip audio = null;
       try{ audio = Applet.newAudioClip( url );   // JDK1.2 static method
       } catch ( Exception e ) {;}
       return audio;
   } // newAudio(url)

  /// get Image from a local file --- for Application (not Applet)
   // Note that this is a static methods
   // usage: MyApplet.newImage("your_image_filename_path");
   static public Image newImage(String filename) {
       Image image = null;
       try{
          Toolkit tkt = Toolkit.getDefaultToolkit( );
          URL url = null;
          try {
             image = tkt.getImage(filename);  //for application only
          }catch(Exception e3) {;}
          if(image == null) {
             url = newGetPath(filename);
             image = tkt.getImage(url); 
          }//if
          // image = getImage(getCodeBase( ), filename); //for Applet
       } catch ( Exception e ) { image = null;}
       return image;
   } // newImage(filename)
   static public Image newGetImage(String filename) {
      return newImage(filename);
   } // newGetImage
   static public Image getMyImage(String filename) {
       return newImage(filename);
   } // getMyImage(filename)

 /// get Image from a URL
   static public Image newImage(URL url) {
       Image image = null;
       try{
            Toolkit tkt = Toolkit.getDefaultToolkit( );
            image = tkt.getImage(url);  //for application only
       } catch ( Exception e ) {;}
       return image;
   } // newImage(url)
   static public Image newGetImage(URL url) {
      return newImage(url);
   } // newGetImage(url)

///
///
   public  Image  newImageBoth(String filename) {   /// NOT static
       Image image = null;
       try {   // 2011/04/02  good in Jar file
          Toolkit tkt = Toolkit.getDefaultToolkit( );
          Class me = getClass( );   // get The class is running
          URL url = me.getResource(filename);
          image = tkt.getImage(url); 
       } catch (Exception e) { ; }
       return image;
   } // newImageBoth(String 
//////

   /// @@@ Œg­Ó function to createMyCursor €è«KšÏ¥Î@@@
  /// Application please uses Cursor mycsr = newMyCursor(filename);
  /// Applet please uses Cursor mycsr = createMyCursor(filename);
  ///
   /// now, this static method is for Application only
   static public java.awt.Cursor newMyCursor(String filename) throws
                                   IndexOutOfBoundsException {
       java.awt.Cursor cursor = null;
       try{
          Toolkit tkt = Toolkit.getDefaultToolkit( );
          Image csrImg = tkt.getImage(filename);  //for application
          cursor = tkt.createCustomCursor(csrImg, new Point(1,1), filename);
       } catch ( Exception e ) { // if error, give him an Hand Cursor
          cursor = new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR);
       }
       return cursor;
   } // createMyCursor

  // now, the following is for Applet, not a static method
   public java.awt.Cursor createMyCursor(String filename) throws
                                   IndexOutOfBoundsException {
       java.awt.Cursor cursor = null;
       try{
          Toolkit tkt = Toolkit.getDefaultToolkit( );
          //Image csrImg = tkt.getImage(filename);  //for application
          Image csrImg = getImage(getCodeBase( ), filename); //for Applet
          cursor = tkt.createCustomCursor(csrImg, new Point(1,1), filename);
       } catch ( Exception e ) {
          cursor = new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR);
       }
       return cursor;
   } // createMyCursor

   public java.awt.Cursor createMyCursorBoth(String filename) throws
                                   IndexOutOfBoundsException {
       java.awt.Cursor cursor = null;
       try{
          Toolkit tkt = Toolkit.getDefaultToolkit( );
          Class me = getClass( );   // get The class is running
          URL url = me.getResource(filename);
          Image csrImg = tkt.getImage( url ); // works for Application+Applet
          cursor = tkt.createCustomCursor(csrImg, new Point(1,1), filename);
       } catch ( Exception e ) {
          cursor = new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR);
       }
       return cursor;
   } // createMyCursor

  // for both Application and Applet
   static public java.awt.Cursor createMyCursor(Image image) throws
                                   IndexOutOfBoundsException {
       java.awt.Cursor cursor = null;
       try{
          Toolkit tkt = Toolkit.getDefaultToolkit( );
          cursor = tkt.createCustomCursor(image, new Point(1,1), "cursor");
       } catch ( Exception e ) {
          cursor = new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR);
       }
       return cursor;
   } // createMyCursor

   JLabel createLabelWithPicture(String filename) {
       return createLabelWithPicture(filename, "", 
                            javax.swing.SwingConstants.TRAILING);
   }
   JLabel createLabelWithPicture(String filename, String caption) {
       return createLabelWithPicture(filename, caption, 
                            javax.swing.SwingConstants.TRAILING);
   }
   JLabel createLabelWithPicture(String filename, String caption,
                                                  int alignment ) {
      JLabel ans = null;
      ImageIcon icon = null;
      try {
         Image image = myGetImageBoth(filename);
         icon = new ImageIcon( image ); 
         ans = new JLabel(caption, icon, alignment);
         ans.setVerticalTextPosition(
                            javax.swing.SwingConstants.BOTTOM);
         ans.setHorizontalTextPosition(
                            javax.swing.SwingConstants.CENTER);
      } catch (Exception e) {
         try { if(ans==null) ans = new JLabel(caption);
         } catch (Exception e2) {;}
      }
      return ans;
   }

   /// some int constants used in the following functions
     final static int TEXT_VP = javax.swing.SwingConstants.BOTTOM;
     final static int TEXT_HP = javax.swing.SwingConstants.CENTER;
     final static int IMG_V_ALIGN = javax.swing.SwingConstants.TOP;
     final static int IMG_H_ALIGN = javax.swing.SwingConstants.LEFT;
   JButton createButtonWithPicture(String filename) {
      return createButtonWithPicture(filename, "",
                 TEXT_VP, TEXT_HP, IMG_V_ALIGN, IMG_H_ALIGN );
   }
   JButton createButtonWithPicture(String filename, String caption) {
      return createButtonWithPicture(filename, caption,
                 TEXT_VP, TEXT_HP, IMG_V_ALIGN, IMG_H_ALIGN );
   }
   JButton createButtonWithPicture(String filename, String caption,
               int textVP) {
      return createButtonWithPicture(filename, caption,
                 textVP, TEXT_HP, IMG_V_ALIGN, IMG_H_ALIGN );
   }
   JButton createButtonWithPicture(String filename, String caption,
               int textVP, int textHP ) {
      return createButtonWithPicture(filename, caption,
                 textVP, textHP, IMG_V_ALIGN, IMG_H_ALIGN );
   }
   JButton createButtonWithPicture(String filename, String caption,
               int textVP, int textHP, int imgVAlign ) {
      return createButtonWithPicture(filename, caption,
                 textVP, textHP, imgVAlign, IMG_H_ALIGN );
   }
   JButton createButtonWithPicture(String filename, String caption,
               int textVP, int textHP, int imgVAlign, int imgHAlign ) {
      JButton ans = null;
      ImageIcon icon = null;
      try {
         Image image = myGetImageBoth(filename);
         icon = new ImageIcon( image ); 
         ans = new JButton(caption, icon);
           ans.setVerticalTextPosition(textVP);
           ans.setHorizontalTextPosition(textHP);
           ans.setVerticalAlignment(imgVAlign);
           ans.setHorizontalAlignment(imgHAlign);
      } catch (Exception e) {
         try { if(ans==null) ans = new JButton(caption);
         } catch (Exception e2) {;}
      }
      return ans;
   }

   Image getImageBoth(String filename) {
       return myGetImageBoth( filename);
   }
   Image myGetImageBoth(String filename) {
      Image image = null;
      try {       // Application €€Åªšú Image €èªk»P Applet €€€£ŠP
         image = newImage(filename);   //try Application first
         //println("MyApplet got image=" + image);
      } catch (Exception e) { image=null; // then, try Applet
      } // try .. catch
      if(image==null) {
         try {       // try Applet, Applet use different method
            image = getImage(new URL( getCodeBase()+ filename));
         } catch (Exception e) { image = null;}
      } //if
      try { // ok, now the last way I can try
         Toolkit tkt = Toolkit.getDefaultToolkit( );
         Class me = getClass( );   // get The class which is running
         URL url = me.getResource(filename);
         image = tkt.getImage( url ); // works for both Application+Applet
      } catch (Exception e) { image = null; } // sorry, I have no way
      return image; 
   } // myGetImageBoth

 /// play an audio file (for Application only)
   static AudioClip newPlayFile(String filename) {
       AudioClip song = null;
       try {
             URL base = newCodeBase();
             song = Applet.newAudioClip(new URL(base+filename)); 
          song.play( );
       } catch (Exception e) { return song; } // ignore any error
       try { Thread.sleep(358); } catch (Exception e) {;}
       return song;
   }

   public AudioClip playFile(String filename) {
       AudioClip song = null;
       try {
           //song = getAudioClip(getCodeBase(), filename);  // Applet's
           song = newAudioBoth(filename);  // both OK
           song.play( );
       } catch (Exception e) { return song; } // Let the caller know
       try { Thread.sleep(358); } catch (Exception e) {;}
       return song;
   }

   ///
  /// a utility to invoke the Task Manager (taskmgr) of Windows system
  // usage: MyApplet.runTaskManager( );   
   static void runTaskManager( ) {    // only works for Application only
       printf("Try to run the Task Manager (taskmgr ) \n" );
       try {
             try {              // ª`·N®æŠ¡, Š]ŠrŠê€€§tŠ³ " Âù€Þž¹ "
                Runtime.getRuntime().exec(
                  "cmd /c \"taskmgr"  + "\" " );
             } catch (Exception e95){
                // to do : how to do on Win95/98 ?
             }  // Win95/98 ?
       } catch (Exception e) {; }
   } // run taskmgr


 ///
/// some println/print utilities for MyApplet, note that this is not static
/// some println/print utilities for convinent
   static public void println(String s) {
      try {  System.out.println(s);
      } catch (Exception e) {;} // ignore any error
   }
   static public void print(String s) {     // not static
      try {  System.out.print(s);
      } catch (Exception e) {;} // ignore any error
   }
/// some printErr utilities
   static public void printErr(String s) {
      try {  System.err.println(s); System.err.flush( );
      } catch (Exception e) {;} // ignore any error
   }
   static public void printf(String s) {
      try {  System.out.print(s);
      } catch (Exception e) {;} // ignore any error
   }
   public static void printf(long n) throws IOException {
      System.out.print(""+n);
   }
   public static void printf(int n) throws IOException {
      System.out.print(""+n);
   }
  /// print n as w columns width
   public static void printf(short n, int w) {
      printf((long)n, w);   // print n as w columns
   }
   public static void printf(int n, int w) {
      printf((long)n, w);
   }
   public static void printf(long n, int w) {
      long tmp = 1;   // print n as w columns
      long nabs = n; if(n<0) {nabs = -nabs; --w; }  // ensure positive
      for(;;) {
          tmp *= 10;
          if(tmp > nabs) break;
          --w;    // w is the expected Width (number of digits)
      }
      try {
         for(;;) {
             if(--w <= 0) break;
             System.out.print(" ");   // necessary leading space
         }
         System.out.print(""+n);
      } catch (Exception e) {;} // ignore any error
   }

   public void flush( ) {
	   try { System.out.flush( );
      } catch (Exception e) {;} // ignore any error
   }

   static public void fflush( ) {
      try { System.out.flush( );
      } catch (Exception e) {;} // ignore any error
   }

   static public void beep( ) {
     java.awt.Toolkit.getDefaultToolkit( ).beep( );
   }

  /// a main program to test my printf functions
   public static void main(String[]p) throws IOException {
       printf("= This MyApplet.java provides many utility functions =\n");
       printf("=="); printf(1, 1); printf("==\n");
       printf("=="); printf(2, 2); printf("==\n");
       printf("=="); printf(3, 3); printf("==\n");
       printf("=="); printf(11, 1); printf("==\n");
       printf("=="); printf(12, 2); printf("==\n");
       printf("=="); printf(13, 3); printf("==\n");
       printf("=="); printf(111, 1); printf("==\n");
       printf("=="); printf(113, 3); printf("==\n");
       printf("=="); printf(115, 5); printf("==\n");
       printf("=="); printf(-11, 1); printf("==\n");
       printf("=="); printf(-12, 2); printf("==\n");
       printf("=="); printf(-13, 3); printf("==\n");
       printf("=="); printf(-15, 5); printf("==\n");
       printf("=="); printf(-116, 6); printf("==\n");
       try {
          AudioClip sound = null; sound = newAudioStatic("laugh.au");
          if(sound != null) { sound.play( ); Thread.sleep(2588); }
       } catch(Exception e) {;}
       printf("Thank you!\n"); 
       for(int i=1; i <= 3; ++i) {
          java.awt.Toolkit.getDefaultToolkit( ).beep( );
          try{ Thread.sleep(258);}catch(Exception e){;}  // 0.258 second
       }
       System.exit(0);
   }
} // class MyApplet

class PictureCanvas extends Canvas implements ImageObserver {
   String filename = null;
   String caption = null;
   Image image = null;
   public PictureCanvas(String filename) {
      this(filename, "");   // call other Constructor
   }
   public PictureCanvas(String filename, String caption) {
      this.filename = filename;
      this.caption = caption;
      try {
         Toolkit tkt = Toolkit.getDefaultToolkit( );
         Class me = getClass( );   // get The class is running
         URL url = me.getResource(filename);
         image = tkt.getImage( url ); // works for both Application+Applet
      } catch (Exception e) { ; }
      repaint( );
   }
   public void update(Graphics g) {
       paint(g);   // call paint directly without clear the panel
   }
   public void paint(Graphics g) {
      int xpos = 12, ypos = 12;
      if(image!=null) {
         g.drawImage(image, 2, 2, this );     // Align left-upper corner
         try {
            xpos = image.getWidth(this)/4;
            ypos = image.getHeight(this) - 24;
         }catch(Exception e) {;}
      }
      if(caption != null) {
          g.setFont(new Font("細明體", Font.PLAIN, 18) );
          g.setColor(Color.red);
          g.drawString( caption, xpos, ypos );
      }
   } // paint
} // PictureCanvas

class PicturePanel extends Panel {
   String filename = null;
   String caption = null;
   Image image = null;
   boolean wantResize = false;
   public PicturePanel(String filename, boolean wantResize) {
      this(filename, "", wantResize);   // call other Constructor
   }
   public PicturePanel(String filename) {
      this(filename, "", false);   // call other Constructor
   }
   public PicturePanel(String filename, String caption) {
      this(filename, caption, false);   // call other Constructor
   }
   public PicturePanel(String filename, String caption, boolean w) {
      this.wantResize = w;
      this.filename = filename;
      this.caption = caption;
      try {
         Toolkit tkt = Toolkit.getDefaultToolkit( );
         Class me = getClass( );   // get The class is running
         //MyApplet. printf(" getClass() = " + me +"\n");
         URL url = me.getResource(filename);
         image = tkt.getImage( url ); // works for both Application+Applet
        // Only those Applets hosted in a Browser can obtain CodeBase
        /// .. and DocumentBase using getCodeBase( ) and getDocumentBase( )
        /// .. Since this PicturePanel is not an Applet, the
        /// method getCodeBase() doesn't work even you call it from an Applet
      } catch (Exception e) { ; }
      repaint( );
   }
   public void update(Graphics g) {
       paint(g);   // call paint directly without clear the panel
   }
   public void paint(Graphics g) {
      int xpos = 12, ypos = 12;
      if(image!=null) {
         int width = 0;
         int height = 38;
         try {
            width = image.getWidth(this);   // image's width
            height = image.getHeight(this);
         } catch (Exception e) {;}
         if(wantResize)
         try {
             width = getSize().width;   // current Panel's width
             height = getSize().height;
         } catch (Exception e) {;}
         if(width == 0)
            g.drawImage(image, 2, 2, this );     // Align left-upper corner
         else
            g.drawImage(image, 2, 2, width, height, this );  
      // now paint the caption
         try {
            xpos = image.getWidth(this)/4;
            ypos = image.getHeight(this) - 24;
         }catch(Exception e) {;}
      }
      if(caption != null) {
          g.setFont(new Font("細明體", Font.PLAIN, 18) );
          g.setColor(Color.red);
          g.drawString( caption, xpos, ypos );
      }
   } // paint
} // PicturePanel
