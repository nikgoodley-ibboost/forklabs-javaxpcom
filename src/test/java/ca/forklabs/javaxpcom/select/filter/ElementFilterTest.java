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
import org.mozilla.interfaces.nsIDOMNode;

import static org.junit.Assert.assertEquals;

/**
 * Class {@code ElementFilterTest} tests class {@link ElementFilter}.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.filter.ElementFilterTest">Daniel Léonard</a>
 * @version $Revision$
 */
@SuppressWarnings({ "boxing", "nls" })
public class ElementFilterTest {

//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    */
   public ElementFilterTest() {
   // nothing
      }


//---------------------------
// Test methods
//---------------------------

   @Test
   public void testEval() {
      Mockery context = new Mockery();

   // ElementFilter filter = new ElementFilter("h1");
   // <a></a>  -> false
   // <h1></h1>  -> true
      final nsIDOMNode[] nodes = new nsIDOMNode[] {
         context.mock(nsIDOMNode.class, "a"),
         context.mock(nsIDOMNode.class, "h1"),
         };

      final String[] names = new String[] {
         "a",
         "h1",
         };

      context.checking(new Expectations() { {
         this.oneOf(nodes[0]).getNodeName();
         this.will(returnValue(names[0]));

         this.oneOf(nodes[1]).getNodeName();
         this.will(returnValue(names[1]));
         }});

      boolean[] solutions = new boolean[] {
         false, true,
         };

      assertEquals(nodes.length, names.length);
      assertEquals(nodes.length, solutions.length);

      ElementFilter filter = new ElementFilter("h1");

      for (int i = 0, len = nodes.length; i < len; i++) {
         boolean answer = filter.eval(nodes[i]);
         assertEquals("node #" + (i+1), solutions[i], answer);
         }

      context.assertIsSatisfied();
      }

   }
