package ca.forklabs.javaxpcom;

import java.io.Console;
import ca.forklabs.javaxpcom.Crawler;
import ca.forklabs.javaxpcom.util.XPCOMConverter;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.interfaces.nsIDOMHTMLAnchorElement;
import org.mozilla.interfaces.nsIDOMHTMLDivElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;

import static ca.forklabs.javaxpcom.util.XPCOMConverter.asDiv;
import static ca.forklabs.javaxpcom.util.XPCOMConverter.asAnchor;
import static ca.forklabs.javaxpcom.util.XPCOMInspector.inspect;

import java.io.IOException;

@SuppressWarnings("nls")
public class ProjectPageCrawler extends Crawler {

//---------------------------
// Constructors
//---------------------------

   public ProjectPageCrawler() {
   // nothing
      }


//---------------------------
// Overridden methods from ca.forklabs.javaxpcom.Crawler
//---------------------------

   @Override
   protected void configureNewShell(Shell shell) {
      shell.setSize(800, 600);
      shell.setText("Forklabs JavaXPCOM Example");
      }


//---------------------------
// Instance methods
//---------------------------

// <div id="mt" class="gtb">
//   <a href="/p/forklabs-baselib/" class="tab active">Project&nbsp;Home</a>
//   <a href="/p/forklabs-baselib/downloads/list" class="tab ">Downloads</a>
//   <a href="/p/forklabs-baselib/w/list" class="tab ">Wiki</a>
//   <a href="/p/forklabs-baselib/issues/list" class="tab ">Issues</a>
//   <a href="/p/forklabs-baselib/source/checkout" class="tab ">Source</a>
//   <a href="/p/forklabs-baselib/admin" class="tab inactive">Administer</a>
//   <div class="gtbc"></div>
// </div>
   protected String exploreTableCell(nsIDOMHTMLAnchorElement anchor) {
      String value = XPCOMConverter.asPlainText(anchor);
      return value;
      }

   protected void exploreMainMenu() {
      nsIDOMNode node = this.getElementById("mt");
      nsIDOMHTMLDivElement div = asDiv(node);

      System.out.println("=== Inspecting the div node ===");
      inspect(div);

      System.out.println("=== Listing the menu ===");
      nsIDOMNodeList anchors = div.getElementsByTagName("a");
      for (long i = 0, len = anchors.getLength(); i < len; i++) {
         nsIDOMHTMLAnchorElement anchor = asAnchor(anchors.item(i));
         String text = this.exploreTableCell(anchor);
         System.out.println("Menu #" + (i+1) + " is " + text);
         }
      }

   public void showOff() throws IOException, InterruptedException {
      String url = "http://code.google.com/p/forklabs-javaxpcom/";
      this.navigateTo(url);

      this.runOnSWTThread(new Runnable() {
         @Override
         public void run() {
            ProjectPageCrawler.this.exploreMainMenu();
            }
         });

      Thread.sleep(3000L);

      this.minimizeBrowser();

      Thread.sleep(3000L);

      this.restoreBrowser();
      }


//---------------------------
// main()
//---------------------------

   public static void main(String... args) throws IOException, InterruptedException {
   // Java needs to know where to find the XULRunner otherwise it doesn't work
      String gre_home = "./tools/xulrunner-1.9.0.13-sdk/bin";
      Crawler.setupXULRunner(gre_home);

      ProjectPageCrawler crawler = null;
      try {
         crawler = new ProjectPageCrawler();
         crawler.showOff();

         Console console = System.console();
         if (null != console) {
            console.printf("Press any key to end...");
            console.readLine();
            }
         else {
            Thread.sleep(5000L);
            }
         }
      finally {
         if (null != crawler) {
            crawler.teardown();
            }
         }
      }

   }
