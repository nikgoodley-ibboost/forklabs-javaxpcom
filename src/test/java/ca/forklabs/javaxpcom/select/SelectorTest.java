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

package ca.forklabs.javaxpcom.select;

import java.net.URL;
import java.util.List;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMNode;
import ca.forklabs.javaxpcom.Crawler;
import ca.forklabs.javaxpcom.util.XPCOMConverter;
//import ca.forklabs.javaxpcom.util.XPCOMInspector;

import static org.junit.Assert.assertEquals;

/**
 * Class {@code SelectorTest} tests class {@link Selector}.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.SelectorTest">Daniel Léonard</a>
 * @version $Revision$
 */
@SuppressWarnings("nls")
public class SelectorTest {

//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    */
   public SelectorTest() {
   // nothing
      }


//---------------------------
// Test methods
//---------------------------

   /**
    * Tests that {@code Selector#getAllChildren(nsIDOMNode)} selects all the
    * children.
    * @throws   Exception   if anything goes wrong.
    */
   @Test
   public void testGetAllChildren() throws Exception {
      String gre_home = "./tools/xulrunner-1.9.0.13-sdk/bin";
      Crawler.setupXULRunner(gre_home);

      Crawler crawler = null;
      try {
         crawler = new Crawler() { /* nothing */ };

         URL url = SelectorTest.class.getResource("/select.all.children.html");
         crawler.navigateTo(url);

         nsIDOMNode document = crawler.getDocument();

         Selector selector = new Selector(document);
         List<nsIDOMNode> children = selector.getAllChildren(document);

//         for (nsIDOMNode child : children) {
//            XPCOMInspector.inspect(child, false);
//            }

         assertEquals(14, children.size());

      // the document node, has one child <html>
         assertEquals(9, children.get(0).getNodeType());
         assertEquals(1, children.get(0).getChildNodes().getLength());

      // the html node, has two children <head>, <body>
         assertEquals("HTML", children.get(1).getNodeName());
         assertEquals(2, children.get(1).getChildNodes().getLength());

      // the head node, has one empty child #text
         assertEquals("HEAD", children.get(2).getNodeName());
         assertEquals(1, children.get(2).getChildNodes().getLength());

         assertEquals(3, children.get(3).getNodeType());

      // the body node, has three children, #text, <p>, #text
         assertEquals("BODY", children.get(4).getNodeName());
         assertEquals(3, children.get(4).getChildNodes().getLength());

      // body -> #text
         assertEquals(3, children.get(5).getNodeType());

      // body -> p, has four children, #text, <em>, #text, <strong>
         assertEquals("P", children.get(6).getNodeName());
         assertEquals(4, children.get(6).getChildNodes().getLength());

      // body -> p -> #text
         assertEquals(3, children.get(7).getNodeType());

      // body -> p -> em, has one child, #text
         assertEquals("EM", children.get(8).getNodeName());
         assertEquals(1, children.get(8).getChildNodes().getLength());

      // body -> p -> em -> #text
         assertEquals(3, children.get(9).getNodeType());

      // body -> p -> #text
         assertEquals(3, children.get(10).getNodeType());

      // body -> p -> strong, has one child, #text
         assertEquals("STRONG", children.get(11).getNodeName());
         assertEquals(1, children.get(11).getChildNodes().getLength());

      // body -> p -> strong -> #text
         assertEquals(3, children.get(12).getNodeType());
         }
      finally {
         if (null != crawler) {
            crawler.teardown();
            }
         }
      }

   /**
    * Tests that the input selectors select the correct nodes.
    * @throws   Exception   if anything goes wrong.
    */
   @Test
   public void testInputSelectors() throws Exception {
      String gre_home = "./tools/xulrunner-1.9.0.13-sdk/bin";
      Crawler.setupXULRunner(gre_home);

      Crawler crawler = null;
      try {
         crawler = new Crawler() { /* nothing */ };

         URL url = SelectorTest.class.getResource("/selector.input.html");
         crawler.navigateTo(url);

         List<nsIDOMNode> buttons = crawler.selector().add(Filters.button()).list();
         assertEquals(2, buttons.size());
         assertEquals("I am a button", XPCOMConverter.getAttributeValue(buttons.get(0), "value"));
         assertEquals("I am also a button", XPCOMConverter.asPlainText(buttons.get(1)));

         List<nsIDOMNode> checkboxes = crawler.selector().add(Filters.checkbox()).list();
         assertEquals(1, checkboxes.size());
         assertEquals("I am a checkbox", XPCOMConverter.getAttributeValue(checkboxes.get(0), "value"));

         List<nsIDOMNode> files = crawler.selector().add(Filters.file()).list();
         assertEquals(1, files.size());

         List<nsIDOMNode> hiddens = crawler.selector().add(Filters.hidden()).list();
         assertEquals(2, hiddens.size());
         assertEquals("I am hidden", XPCOMConverter.getAttributeValue(hiddens.get(0), "value"));
         assertEquals("I am hidden too", XPCOMConverter.getAttributeValue(hiddens.get(1), "value"));

         List<nsIDOMNode> images = crawler.selector().add(Filters.image()).list();
         assertEquals(1, images.size());

         List<nsIDOMNode> inputs = crawler.selector().add(Filters.input()).list();
         assertEquals(15, inputs.size());

         List<nsIDOMNode> passwords = crawler.selector().add(Filters.password()).list();
         assertEquals(1, passwords.size());
         assertEquals("Guess me!", XPCOMConverter.getAttributeValue(passwords.get(0), "value"));

         List<nsIDOMNode> selects = crawler.selector().add(Filters.select()).list();
         assertEquals(1, selects.size());
         assertEquals(3L, selects.get(0).getChildNodes().getLength());

         List<nsIDOMNode> submits = crawler.selector().add(Filters.submit()).list();
         assertEquals(1, submits.size());
         assertEquals("Send this!", XPCOMConverter.getAttributeValue(submits.get(0), "value"));

         List<nsIDOMNode> texts = crawler.selector().add(Filters.text()).list();
         assertEquals(2, texts.size());
         assertEquals("This is text", XPCOMConverter.getAttributeValue(texts.get(0), "value"));
         assertEquals("This is text too!", XPCOMConverter.getAttributeValue(texts.get(1), "value"));

         List<nsIDOMNode> textareas = crawler.selector().add(Filters.textarea()).list();
         assertEquals(1, textareas.size());
         assertEquals("This is even more text", XPCOMConverter.asPlainText(textareas.get(0)));
         }
      finally {
         if (null != crawler) {
            crawler.teardown();
            }
         }
      }

   /**
    * Tests that the basic selectors select the correct nodes.
    * @throws   Exception   if anything goes wrong.
    */
   @Test
   public void testBasicFilters() throws Exception {
      String gre_home = "./tools/xulrunner-1.9.0.13-sdk/bin";
      Crawler.setupXULRunner(gre_home);

      Crawler crawler = null;
      try {
         crawler = new Crawler() { /* nothing */ };

         URL url = SelectorTest.class.getResource("/basic.html");
         crawler.navigateTo(url);

         List<nsIDOMNode> css = crawler.selector().add(Filters.css("target")).list();
         assertEquals(2, css.size());
         assertEquals("H3", css.get(0).getNodeName());
         assertEquals("H6", css.get(1).getNodeName());

         List<nsIDOMNode> headers = crawler.selector().add(Filters.headers()).list();
         assertEquals("H1", headers.get(0).getNodeName());
         assertEquals("H2", headers.get(1).getNodeName());
         assertEquals("H2", headers.get(2).getNodeName());
         assertEquals("H3", headers.get(3).getNodeName());
         assertEquals("H4", headers.get(4).getNodeName());
         assertEquals("H5", headers.get(5).getNodeName());
         assertEquals("H6", headers.get(6).getNodeName());

         List<nsIDOMNode> ids = crawler.selector().add(Filters.id("h2")).list();
         assertEquals(1, ids.size());
         assertEquals("H2", ids.get(0).getNodeName());
         }
      finally {
         if (null != crawler) {
            crawler.teardown();
            }
         }
      }

   /**
    * Tests that get works.
    * @throws   Exception   if anything goes wrong.
    */
   @Test
   public void testGet() throws Exception {
      String gre_home = "./tools/xulrunner-1.9.0.13-sdk/bin";
      Crawler.setupXULRunner(gre_home);

      Crawler crawler = null;
      try {
         crawler = new Crawler() { /* nothing */ };

         URL url = SelectorTest.class.getResource("/basic.html");
         crawler.navigateTo(url);

         List<nsIDOMNode> css = crawler.selector().add(Filters.css("target")).list();
         assertEquals(2, css.size());
         assertEquals("H3", css.get(0).getNodeName());
         assertEquals("H6", css.get(1).getNodeName());

         nsIDOMNode node = crawler.selector().add(Filters.css("target")).get();
         assertEquals("H3", node.getNodeName());
         }
      finally {
         if (null != crawler) {
            crawler.teardown();
            }
         }
      }

   }
