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

package ca.forklabs.javaxpcom.util;

import java.util.Map;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;

import static org.junit.Assert.assertEquals;

/**
 * Class {@code XPCOMConverterTest} tests class {@link XPCOMConverter}.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.util.XPCOMConverterTest">Daniel Léonard</a>
 * @version $Revision$
 */
@SuppressWarnings({ "boxing", "nls" })
public class XPCOMConverterTest {

 //---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    */
   public XPCOMConverterTest() {
   // nothing
      }


//---------------------------
// Test methods
//---------------------------

   /**
    * Tests that the attribute map is well constructed.
    */
   @Test
   public void testAttributes() {
      Mockery context = new Mockery();

   // <table cellspacing="0"
   //        cellpadding="0"
   //        itemtype="http://schema.org/CreativeWork"
   //        itemscope=""
   //        style="padding:0px; margin: 0px 0px 10px 0px; width:100%">

      final nsIDOMNode table = context.mock(nsIDOMNode.class);
      final nsIDOMNamedNodeMap map = context.mock(nsIDOMNamedNodeMap.class);

      final nsIDOMNode[] nodes = new nsIDOMNode[] {
         context.mock(nsIDOMNode.class, "cellspacing"),
         context.mock(nsIDOMNode.class, "cellpadding"),
         context.mock(nsIDOMNode.class, "itemtype"),
         context.mock(nsIDOMNode.class, "itemscope"),
         context.mock(nsIDOMNode.class, "style"),
         };

      final String[] names = new String[] {
         "cellspacing", "cellpadding", "itemtype", "itemscope", "style",
         };

      final String[] values = new String[] {
         "0", "0", "http://schema.org/CreativeWork", "", "padding:0px; margin: 0px 0px 10px 0px; width:100%",
         };

      context.checking(new Expectations() { {
         this.oneOf(table).getAttributes();
         this.will(returnValue(map));

         this.oneOf(map).getLength();
         this.will(returnValue(5L));

         for (long l = 0L, len = 5; l < len; l++) {
            nsIDOMNode node = nodes[(int) l];
            String name = names[(int) l];
            String value = values[(int) l];

            this.oneOf(map).item(l);
            this.will(returnValue(node));

            this.oneOf(node).getNodeName();
            this.will(returnValue(name));

            this.oneOf(node).getNodeValue();
            this.will(returnValue(value));
            }
         }});

      Map<String, String> attributes = XPCOMConverter.attributes(table);

      assertEquals(5, attributes.size());

      assertEquals("0", attributes.get("cellspacing"));
      assertEquals("0", attributes.get("cellpadding"));
      assertEquals("http://schema.org/CreativeWork", attributes.get("itemtype"));
      assertEquals("", attributes.get("itemscope"));
      assertEquals("padding:0px; margin: 0px 0px 10px 0px; width:100%", attributes.get("style"));

      context.assertIsSatisfied();
      }

   }
