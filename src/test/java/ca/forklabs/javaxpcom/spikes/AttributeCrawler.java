/*
 * @(#) $Header$
 *
 * Copyright (C) 2011  Forklabs Daniel Léonard
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

package ca.forklabs.javaxpcom.spikes;

import java.util.List;
import java.util.Map;
import org.mozilla.interfaces.nsIDOMNode;
import ca.forklabs.javaxpcom.Crawler;
import ca.forklabs.javaxpcom.select.Filters;
import ca.forklabs.javaxpcom.util.XPCOMConverter;

/**
 * Spike class {@code AttributeCrawler} explores the behaviour of the XPCOM
 * library about node attributes.
 */
@SuppressWarnings("nls")
public class AttributeCrawler extends Crawler {

   /**
    * Explore the behaviour on tbody nodes.
    */
   protected void tbodies() {
   // there at least a table in the header header of the project page
   // usually tbodies don't have attributes
      List<nsIDOMNode> tbodies = this.selector()
                                     .add(Filters.element("tbody"))
                                     .list();

      System.out.println("There are " + tbodies.size() + " <tbody>");

      for (nsIDOMNode tbody : tbodies) {
         Map<String, String> attributes = XPCOMConverter.attributes(tbody);

         System.out.println("Node " + tbody.getLocalName() + " has " + attributes.size() + " attributes");
         for (String attribute : attributes.keySet()) {
            String value = attributes.get(attribute);
            System.out.println("  - attribute = " + attribute + ", value = " + value);
            }

         System.out.println("  - attribute foo gives " + XPCOMConverter.getAttributeValue(tbody, "foo"));
         }
      }

   /**
    * Explore the behaviour on anchor nodes.
    */
   protected void anchors() {
   // anchors usually have one or more attributes
      List<nsIDOMNode> anchors = this.selector()
                                     .add(Filters.element("a"))
                                     .list();

      System.out.println("There are " + anchors.size() + " <a>");

      for (nsIDOMNode anchor : anchors) {
         Map<String, String> attributes = XPCOMConverter.attributes(anchor);

         System.out.println("Node " + anchor.getLocalName() + " has " + attributes.size() + " attributes");
         for (String attribute : attributes.keySet()) {
            String value = attributes.get(attribute);
            System.out.println("  - attribute = " + attribute + ", value = " + value);
            }

         System.out.println("  - attribute href gives " + XPCOMConverter.getAttributeValue(anchor, "href"));
         }
      }

   /**
    * Entry point.
    * @param   args   ignored.
    * @throws   Exception   if anything goes wrong.
    */
   public static void main(String... args) throws Exception {
      String gre_home = "./tools/xulrunner-1.9.0.13-sdk/bin";
      Crawler.setupXULRunner(gre_home);

      AttributeCrawler crawler = null;
      try {
         crawler = new AttributeCrawler();

         String url = "http://code.google.com/p/forklabs-javaxpcom/";
         crawler.navigateTo(url);

         crawler.tbodies();
         crawler.anchors();
         }
      finally {
         if (null != crawler) {
            crawler.teardown();
            }
         }
      }

   }
