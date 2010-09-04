package ca.forklabs.javaxpcom;

import java.io.Console;
import ca.forklabs.javaxpcom.Crawler;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.interfaces.nsIDOMHTMLCollection;
import org.mozilla.interfaces.nsIDOMHTMLDivElement;
import org.mozilla.interfaces.nsIDOMHTMLTableCellElement;
import org.mozilla.interfaces.nsIDOMHTMLTableElement;
import org.mozilla.interfaces.nsIDOMHTMLTableRowElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;

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
// Overriden methods from ca.forklabs.javaxpcom.Crawler
//---------------------------

   @Override
   protected void configureNewShell(Shell shell) {
      shell.setSize(800, 600);
      shell.setText("Forklabs JavaXPCOM Example");
      }


//---------------------------
// Instance methods
//---------------------------

   protected nsIDOMHTMLTableElement asTable(nsIDOMNode node) {
      nsIDOMHTMLTableElement table = (nsIDOMHTMLTableElement) node.queryInterface(nsIDOMHTMLTableElement.NS_IDOMHTMLTABLEELEMENT_IID);
      return table;
      }

   protected nsIDOMHTMLTableRowElement asTableRow(nsIDOMNode node) {
      nsIDOMHTMLTableRowElement row = (nsIDOMHTMLTableRowElement) node.queryInterface(nsIDOMHTMLTableRowElement.NS_IDOMHTMLTABLEROWELEMENT_IID);
      return row;
      }

   protected nsIDOMHTMLTableCellElement asTableCell(nsIDOMNode node) {
      nsIDOMHTMLTableCellElement cell = (nsIDOMHTMLTableCellElement) node.queryInterface(nsIDOMHTMLTableCellElement.NS_IDOMHTMLTABLECELLELEMENT_IID);
      return cell;
      }

   protected nsIDOMHTMLDivElement asDiv(nsIDOMNode node) {
      nsIDOMHTMLDivElement div = (nsIDOMHTMLDivElement) node.queryInterface(nsIDOMHTMLDivElement.NS_IDOMHTMLDIVELEMENT_IID);
      return div;
      }

//  <th onclick="if (!cancelBubble) _go('/p/forklabs-javaxpcom/');">
//    <div class="tab active">
//      <div class="round4"></div>
//      <div class="round2"></div>
//      <div class="round1"></div>
//      <div class="box-inner">
//        <a href="/p/forklabs-javaxpcom/" onclick="cancelBubble=true;">Project&nbsp;Home</a>
//      </div>
//    </div>
//  </th>
   protected String exploreTableCell(nsIDOMHTMLTableCellElement cell) {
      nsIDOMNode anchor = cell.getElementsByTagName("a").item(0L);
      nsIDOMNode text = anchor.getFirstChild();
      String value = text.getNodeValue();
      return value;
      }

   protected void exploreMainMenu() {
      nsIDOMNode node = this.getElementById("mt");
      nsIDOMHTMLTableElement table = this.asTable(node);

      System.out.println("=== Inspecting the table node ===");
      inspect(table);

   // at the time of writing there was only one row
      nsIDOMHTMLCollection rows = table.getRows();
      nsIDOMHTMLTableRowElement row = this.asTableRow(rows.item(0L));

      System.out.println("=== Inspecting the table row node ===");
      inspect(row);

      System.out.println("=== Listing the menu ===");
      nsIDOMNodeList cells = row.getElementsByTagName("th");
      for (long i = 0, len = cells.getLength(); i < len; i++) {
         nsIDOMHTMLTableCellElement cell = this.asTableCell(cells.item(i));
         String text = this.exploreTableCell(cell);
         System.out.println("Menu #" + (i+1) + " is " + text);
         }
      }

   public void showOff() throws IOException {
      String url = "http://code.google.com/p/forklabs-javaxpcom/";
      this.navigateTo(url);

      this.runOnSWTThread(new Runnable() {
         @Override
         public void run() {
            ProjectPageCrawler.this.exploreMainMenu();
            }
         });
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
