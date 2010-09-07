/*
 * @(#) $Header$
 *
 * Copyright (C) 2010  Daniel L�onard
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package ca.forklabs.javaxpcom;

import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIWebBrowser;
import es.ladyr.ladyrbrowser.impl.DisplayManager;

import java.io.IOException;

/**
 * Class {@code Crawler} provides a skeleton to build web crawlers using
 * Mozilla and SWT.
 *
 * This crawler gives access to a {@link Browser} object inside a simple
 * {@link Shell} that can be interacted with to navigate the web and access the
 * underlying {@link nsIDOMDocument}.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.Crawler">Daniel L�onard</a>
 * @version $Revision$
 */
public abstract class Crawler {

//---------------------------
// Class variables
//---------------------------

   /** The default timeout we wait for a page to load, one minute. */
   public static final long DEFAULT_TIMEOUT = TimeUnit.MINUTES.toMillis(1);


//---------------------------
// Instance variables
//---------------------------

   /** The mozilla browser. */
   private Browser browser;
   /** The browser's display. */
   private Display display;

   /** Semaphore to monitor the loading of webpages. */
   private CountDownLatch latch;

   /** The timeout between saying that the page cannot be loaded. */
   private long timeout = DEFAULT_TIMEOUT;


//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    */
   protected Crawler() {
      this(null);
      }

   /**
    * Constructor.
    * @param   display   the display on which to open the browser's shell.
    */
   protected Crawler(Display display) {
      this.setupCrawler(display);
      }


//---------------------------
// Accessors and mutators
//---------------------------

   /**
    * Gets the browser's display.
    * @return   the display.
    */
   protected Display getDisplay() {
      return this.display;
      }

   /**
    * Changes the browser's display.
    * @param   display   the new display.
    */
   protected void setDisplay(Display display) {
      this.display = display;
      }

   /**
    * Gets the browser.
    * @return   the browser.
    */
   protected Browser getBrowser() {
      return this.browser;
      }

   /**
    * Changes the browser.
    * @param   browser   the new browser.
    */
   protected void setBrowser(Browser browser) {
      this.browser = browser;
      }

   /**
    * Gets the shell containing the browser.
    * @return  the shell.
    */
   @SuppressWarnings("hiding")
   protected Shell getShell() {
      Browser browser = this.getBrowser();
      Shell shell = browser.getShell();
      return shell;
      }

   /**
    * Changes the time to wait for a page to load.
    * @param   millis   the new time out.
    */
   public void setTimeOut(long millis) {
      this.timeout = millis;
      }

   /**
    * Gets the time to wait for a page to load.
    * @return   the time out.
    */
   public long getTimeOut() {
      return this.timeout;
      }


//---------------------------
// Useful methods
//---------------------------

   /**
    * Runs a snipet of code on the SWT event thread.
    * @param   runnable   the snipet of code.
    */
   @SuppressWarnings("hiding")
   protected void runOnSWTThread(Runnable runnable) {
      Display display = this.getDisplay();
      display.syncExec(runnable);
      }


//---------------------------
// Navigation methods
//---------------------------

   /**
    * Resets the latch to have a quantity of one.
    */
   protected void newCountDownLatch() {
      this.latch = new CountDownLatch(1);
      }

   /**
    * Makes the latch count down once.
    */
   protected void countDownLatch() {
      this.latch.countDown();
      }

   /**
    * Makes the current thread wait on the latch.
    * @param   timeout   the maximum time to wait.
    * @param   unit   the time unit of the {@code timeout} argument.
    * @return   {@code true} if the count reached zero, {@code false} if the
    *           waiting time elapsed before the count reached zero.
    * @see   CountDownLatch#await(long, TimeUnit)
    */
   @SuppressWarnings("hiding")
   protected boolean waitOnLatch(long timeout, TimeUnit unit) throws InterruptedException {
      boolean reached_zero = this.latch.await(timeout, unit);
      return reached_zero;
      }

   /**
    * Blocks the thread until the web page has been loaded.
    * @throws   IOException   if the web page fails to load before the timeout
    *                         expires.
    */
   @SuppressWarnings({ "hiding", "boxing" })
   protected void waitForPageToLoad() throws IOException {
      boolean has_timed_out = true;

      long timeout = this.getTimeOut();
      TimeUnit unit = TimeUnit.MILLISECONDS;

      try {
         info(getLocalizedString(WAIT_FOR_LOADING, timeout, unit));

         has_timed_out = !this.waitOnLatch(timeout, unit);
         if (has_timed_out) {
            info(getLocalizedString(TOO_LONG));
            this.runOnSWTThread(new Runnable() {
               public void run() {
                  Browser browser = Crawler.this.getBrowser();
                  browser.stop();
                  info(getLocalizedString(HAS_STOPPED));
                  }
               });
            this.waitOnLatch(timeout, unit);
            }
         }
      catch (InterruptedException ie) {
// BUG : better error handling, convert to IOException???
// BUG : use class InterruptedIOException everywhere
         throw new RuntimeException(ie);
         }

      if (has_timed_out) {
         String message = getLocalizedString(HAS_TIMED_OUT, timeout / 1000L);
         throw new IOException(message);
         }
      }

   /**
    * Navigates to the specified URL and waits for the page to load.
    * @param   url   the url.
    * @throws   IOException   if the page fails to load.
    */
   @SuppressWarnings("hiding")
   protected void navigateTo(String url) throws IOException {
      this.newCountDownLatch();

      final String destination = url;
      this.runOnSWTThread(new Runnable() {
         public void run() {
            info(getLocalizedString(GOING_TO));
            Browser browser = Crawler.this.getBrowser();
            browser.setUrl(destination);
            }
         });

      this.waitForPageToLoad();
      }


//---------------------------
// DOM methods
//---------------------------

   /**
    * Gets the DOM document object.
    * @return   the DOM document.
    */
   @SuppressWarnings("hiding")
   protected nsIDOMDocument getDocument() {
      Browser browser = this.getBrowser();
      nsIWebBrowser web_browser = (nsIWebBrowser) browser.getWebBrowser();
      nsIDOMWindow window = web_browser.getContentDOMWindow();
      nsIDOMDocument document = window.getDocument();
      return document;
      }

   /**
    * Gets a DOM element by its id.
    * @param   id   the id of the element.
    * @return   the element or {@code null} if the id does not map to an
    *           element.
    */
   protected nsIDOMElement getElementById(String id) {
      nsIDOMDocument document = this.getDocument();
      nsIDOMElement element = document.getElementById(id);
      return element;
      }


//---------------------------
// Setup instance methods
//---------------------------

   /**
    * Configures the newly created shell.
    * @param   shell   the new shell.
    */
   protected abstract void configureNewShell(Shell shell);

   /**
    * Creates a new {@link Shell} to contain a {@link Browser}.
    * @return   the shell.
    * @see   #configureNewShell(Shell)
    */
   @SuppressWarnings("hiding")
   protected final Shell createNewShell() {
      Display display = this.getDisplay();
      Shell shell = new Shell(display);
      this.configureNewShell(shell);
      return shell;
      }

   /**
    * Creates a new {@link Browser} and embeds it into the given {@link Shell}.
    * @return   the browser.
    */
   @SuppressWarnings("hiding")
   protected final Browser createNewBrowser() {
      Shell shell = this.createNewShell();

      Browser browser = new Browser(shell, SWT.MOZILLA);
      browser.setBounds(shell.getClientArea());
   // each time a page is being loaded, this listener will
   // count down the latch when the page has been loaded
      browser.addProgressListener(new ProgressListener() {
         public void changed(ProgressEvent event) { /* nothing */ }

         public void completed(ProgressEvent event) {
            Crawler.this.countDownLatch();
            info(getLocalizedString(PAGE_LOADED));
            }
         });

      shell.open();

      return browser;
      }

   /**
    * Sets up the crawler by opening a browser's shell.
    * @param   display   the display on which to open the browser's shell.
    */
   @SuppressWarnings("hiding")
   protected final void setupCrawler(Display display) {
   // first things first, create and save the display
   // because the run on swt thread needs it and
   // so does the creation of the shells
      if (null == display) {
         DisplayManager display_manager = DisplayManager.getInstance();
         display = display_manager.getDisplay();
         }
      this.setDisplay(display);

      this.runOnSWTThread(new Runnable() {
         public void run() {
            Browser browser = Crawler.this.createNewBrowser();
            Crawler.this.setBrowser(browser);
            }
         });
      }

   /**
    * Tears down this search engine.
    */
   public void teardown() {
// BUG : fix the teardown
//      this.runOnSWTThread(new Runnable() {
//         @Override
//         public void run() {
//            Browser browser = Crawler.this.getBrowser();
//            if (null != browser) {
//               Shell shell = browser.getShell();
//               shell.dispose();
//               }

//            Display display = Crawler.this.getDisplay();
//            if (null != display) {
//               display.close();
//               }
//            }
//         });
      }


//---------------------------
// Class methods
//---------------------------

   /**
    * Sets up the XULRunner so that crawlers can be used.
    * @param   the location of the XULRunner binaries.
    * @exception   IllegalStateException   if the location does not exist.
    */
   public static void setupXULRunner(String gre_home) throws IllegalStateException {
      try {
         gre_home = new File(gre_home).getCanonicalPath();
         System.setProperty("org.eclipse.swt.browser.XULRunnerPath", gre_home); //$NON-NLS-1$
         }
      catch (IOException ioe) {
         String message = getLocalizedString(BAD_XULRUNNER_PATH, gre_home);
         throw new IllegalStateException(message, ioe);
         }
      }


 //---------------------------
// Logger methods
//---------------------------

   /** The name of the logger of this crawler. */
   protected static final String LOGGER_NAME = "forklabs-crawler";              //$NON-NLS-1$

   /** The logger object. */
   protected static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

   /**
    * Logs a string message at the config level.
    * @param   message   the message.
    */
   protected static void config(String message) {
      LOGGER.config(message);
      }

   /**
    * Logs a string message at the fine level.
    * @param   message   the message.
    */
   protected static void fine(String message) {
      LOGGER.fine(message);
      }

   /**
    * Logs a string message at the info level.
    * @param   message   the message.
    */
   protected static void info(String message) {
      LOGGER.info(message);
      }

   /**
    * Logs a string message at the severe level.
    * @param   message   the message.
    */
   protected static void severe(String message) {
      LOGGER.severe(message);
      }

   /**
    * Logs an exception.
    * @param   t   the exception.
    */
   @SuppressWarnings("nls")
   protected static void log(Throwable t) {
      LOGGER.log(Level.SEVERE, "", t);
      }

   /**
    * Adds a new handler to this logger.
    * @param   handler   the new handler.
    */
   protected static void addHandler(Handler handler) {
      LOGGER.addHandler(handler);
      }

   /**
    * Removes a handler from this logger.
    * @param   handler   the handler.
    */
   protected static void removeHandler(Handler handler) {
      LOGGER.removeHandler(handler);
      }


//---------------------------
// Localization
//---------------------------

   /** The key for the log message saying that a page is loading. */
   protected static final String WAIT_FOR_LOADING = "wait.for.loading";         //$NON-NLS-1$
   /** The key for the log message saying that a page has taken too long to load. */
   protected static final String TOO_LONG = "too.long";                         //$NON-NLS-1$
   /** The key for the log message saying that a page has stopped loading. */
   protected static final String HAS_STOPPED = "has.stopped";                   //$NON-NLS-1$
   /** The key for the log message saying that a page loading has timed out. */
   protected static final String HAS_TIMED_OUT = "has.timed.out";               //$NON-NLS-1$

   /** The key for the log message saying that the browser is now going to a new page. */
   protected static final String GOING_TO = "going.to";                         //$NON-NLS-1$
   /** The key for the log message saying that a page has loaded. */
   protected static final String PAGE_LOADED = "page.loaded";                   //$NON-NLS-1$

   /** The key for the log message saying that the XULRunner installation path is wrong. */
   protected static final String BAD_XULRUNNER_PATH = "bad.xulrunner.path";     //$NON-NLS-1$

   /**
    * Gets and formats the specified localized string from the resource bundle.
    * @param   key   the key.
    * @param   arguments   the arguments to format the string.
    * @return   the value.
    */
   protected static String getLocalizedString(String key, Object... arguments) {
      String name = Crawler.class.getName();
      ResourceBundle bundle = ResourceBundle.getBundle(name);
      String pattern = bundle.getString(key);
      String message = MessageFormat.format(pattern, arguments);
      return message;
      }

   }
