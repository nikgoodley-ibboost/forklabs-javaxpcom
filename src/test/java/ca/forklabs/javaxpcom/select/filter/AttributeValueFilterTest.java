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

package ca.forklabs.javaxpcom.select.filter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;

import static org.junit.Assert.assertEquals;

/**
 * Class {@code AttributeValueFilterTest} tests class
 * {@link AttributeValueFilter}.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.filter.AttributeValueFilterTest">Daniel Léonard</a>
 * @version $Revision$
 */
@SuppressWarnings({ "boxing", "nls" })
public class AttributeValueFilterTest {

//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    */
   public AttributeValueFilterTest() {
   // nothing
      }


//---------------------------
// Test methods
//---------------------------

   @Test
   public void testEval() {
      Mockery context = new Mockery();

   // AttributeValueFilter filter = new AttributeValueFilter("class", "title");
   // <a class="anchor"></a>         -> false
   // <h1 class="title active"></h1> -> true
   // <li id="123"></li>             -> false
   // <p></p>                        -> false
      final nsIDOMNode[] nodes = new nsIDOMNode[] {
         context.mock(nsIDOMNode.class, "a"),
         context.mock(nsIDOMNode.class, "h1"),
         context.mock(nsIDOMNode.class, "li"),
         context.mock(nsIDOMNode.class, "p"),
         };

      final nsIDOMNamedNodeMap[] nodemaps = new nsIDOMNamedNodeMap[] {
         context.mock(nsIDOMNamedNodeMap.class, "nnm1"),
         context.mock(nsIDOMNamedNodeMap.class, "nnm2"),
         context.mock(nsIDOMNamedNodeMap.class, "nnm3"),
         null,
         };

      final nsIDOMNode[] attributes = new nsIDOMNode[] {
         context.mock(nsIDOMNode.class, "class1"),
         context.mock(nsIDOMNode.class, "class2"),
         null,
         null,
         };

      final String[] values = new String[] {
         "anchor",
         "title",
         null,
         null,
         };

      context.checking(new Expectations() { {
      // <a class="anchor"></a>
         this.oneOf(nodes[0]).hasAttributes();
         this.will(returnValue(true));

         this.oneOf(nodes[0]).getAttributes();
         this.will(returnValue(nodemaps[0]));

         this.oneOf(nodemaps[0]).getNamedItem("class");
         this.will(returnValue(attributes[0]));

         this.oneOf(attributes[0]).getNodeValue();
         this.will(returnValue(values[0]));


      // <h1 class="title"></a>
         this.oneOf(nodes[1]).hasAttributes();
         this.will(returnValue(true));

         this.oneOf(nodes[1]).getAttributes();
         this.will(returnValue(nodemaps[1]));

         this.oneOf(nodemaps[1]).getNamedItem("class");
         this.will(returnValue(attributes[1]));

         this.oneOf(attributes[1]).getNodeValue();
         this.will(returnValue(values[1]));


      // <li id="123"></li>
         this.oneOf(nodes[2]).hasAttributes();
         this.will(returnValue(true));

         this.oneOf(nodes[2]).getAttributes();
         this.will(returnValue(nodemaps[2]));

         this.oneOf(nodemaps[2]).getNamedItem("class");
         this.will(returnValue(null));


         // <p></p>
         this.oneOf(nodes[3]).hasAttributes();
         this.will(returnValue(false));

         this.never(nodes[3]).getAttributes();
         }});

      boolean[] solutions = new boolean[] {
         false, true, false, false,
         };

      assertEquals(nodes.length, nodemaps.length);
      assertEquals(nodes.length, attributes.length);
      assertEquals(nodes.length, values.length);
      assertEquals(nodes.length, solutions.length);

      AttributeValueFilter filter = new AttributeValueFilter("class", "title");

      for (int i = 0, len = nodes.length; i < len; i++) {
         boolean answer = filter.eval(nodes[i]);
         assertEquals("node #" + (i+1), solutions[i], answer);
         }

      context.assertIsSatisfied();
      }

   }
