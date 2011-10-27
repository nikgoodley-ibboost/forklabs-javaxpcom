/*
 * @(#) $Header$
 *
 * Copyright (C) 2010  Forklabs Daniel Léonard
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

import java.io.Console;
import java.util.List;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.interfaces.nsIDOMHTMLAnchorElement;
//import org.mozilla.interfaces.nsIDOMHTMLDivElement;
import org.mozilla.interfaces.nsIDOMNode;
//import org.mozilla.interfaces.nsIDOMNodeList;
import ca.forklabs.javaxpcom.Crawler;

import java.io.IOException;

//import static ca.forklabs.javaxpcom.util.XPCOMConverter.asDiv;
import static ca.forklabs.javaxpcom.util.XPCOMConverter.asAnchor;
import static ca.forklabs.javaxpcom.util.XPCOMConverter.asPlainText;
//import static ca.forklabs.javaxpcom.util.XPCOMInspector.inspect;

/**
 * Class {@code ProjectPageCrawler} shows off how to make a basic crawler.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.ProjectPageCrawler">Daniel Léonard</a>
 * @version $Revision$
 */
@SuppressWarnings("nls")
public class ProjectPageCrawler extends Crawler {

//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    */
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

   protected String exploreAnchor(nsIDOMHTMLAnchorElement anchor) {
      String value = asPlainText(anchor);
      return value;
      }

// <div id="mt" class="gtb">
//   <a href="/p/forklabs-baselib/" class="tab active">Project&nbsp;Home</a>
//   <a href="/p/forklabs-baselib/downloads/list" class="tab ">Downloads</a>
//   <a href="/p/forklabs-baselib/w/list" class="tab ">Wiki</a>
//   <a href="/p/forklabs-baselib/issues/list" class="tab ">Issues</a>
//   <a href="/p/forklabs-baselib/source/checkout" class="tab ">Source</a>
//   <a href="/p/forklabs-baselib/admin" class="tab inactive">Administer</a>
//   <div class="gtbc"></div>
// </div>

   protected void exploreMainMenu() {
// This is the old way
//
//      nsIDOMNode node = this.getElementById("mt");
//      nsIDOMHTMLDivElement div = asDiv(node);
//
//      System.out.println("=== Inspecting the div node ===");
//      inspect(div);
//
//      System.out.println("=== Listing the menu ===");
//      nsIDOMNodeList anchors = div.getElementsByTagName("a");
//      for (long i = 0, len = anchors.getLength(); i < len; i++) {
//         nsIDOMHTMLAnchorElement anchor = asAnchor(anchors.item(i));
//         String text = this.exploreAnchor(anchor);
//         System.out.println("Menu #" + (i+1) + " is " + text);
//         }

// This is the new way using the selector API
      System.out.println("=== Listing the menu using the selector mechanism ===");
      List<nsIDOMNode> anchors = this.selector()
                                     .element("a")
                                     .attribute("class", "tab")
                                     .list();
      for (nsIDOMNode node : anchors) {
         nsIDOMHTMLAnchorElement anchor = asAnchor(node);
         String text = this.exploreAnchor(anchor);
         System.out.println("* Menu is " + text);
         }
      }

   protected void showOff() throws IOException, InterruptedException {
      String url = "http://code.google.com/p/forklabs-javaxpcom/";
      this.navigateTo(url);

      this.exploreMainMenu();

      Thread.sleep(3000L);

      this.minimizeBrowser();

      Thread.sleep(3000L);

      this.restoreBrowser();
      }


//---------------------------
// main()
//---------------------------

   /**
    * Entry point.
    * @param   args   ignored
    * @exception   Exception   if anything goes wrong.
    */
   public static void main(String... args) throws Exception {
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
