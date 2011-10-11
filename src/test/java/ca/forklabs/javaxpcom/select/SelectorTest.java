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

import java.util.List;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Class {@code SelectorTest} tests class {@link Selector}.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.SelectorTest">Daniel Léonard</a>
 * @version $Revision$
 */
@SuppressWarnings({ "boxing", "nls" })
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

   @Test
   public void testGetAllChildren() {
      Mockery context = new Mockery();

   // <p>This is <em>a simple</em> test <strong>with many children</strong></p>
      final nsIDOMNode p = context.mock(nsIDOMNode.class, "p");
      final nsIDOMNode text1 = context.mock(nsIDOMNode.class, "text1");
      final nsIDOMNode em = context.mock(nsIDOMNode.class, "em");
      final nsIDOMNode text2 = context.mock(nsIDOMNode.class, "text2");
      final nsIDOMNode text3 = context.mock(nsIDOMNode.class, "text3");
      final nsIDOMNode strong = context.mock(nsIDOMNode.class, "strong");
      final nsIDOMNode text4 = context.mock(nsIDOMNode.class, "text4");

      final nsIDOMNodeList p_children = context.mock(nsIDOMNodeList.class, "p_children");
      final nsIDOMNodeList em_children = context.mock(nsIDOMNodeList.class, "em_children");
      final nsIDOMNodeList strong_children = context.mock(nsIDOMNodeList.class, "strong_children");

      context.checking(new Expectations() { {
      // p
         this.oneOf(p).hasChildNodes();
         this.will(returnValue(true));

         this.oneOf(p).getChildNodes();
         this.will(returnValue(p_children));

         this.oneOf(p_children).getLength();
         this.will(returnValue(4L));

         this.oneOf(p_children).item(0L);
         this.will(returnValue(text1));
         this.oneOf(p_children).item(1L);
         this.will(returnValue(em));
         this.oneOf(p_children).item(2L);
         this.will(returnValue(text3));
         this.oneOf(p_children).item(3L);
         this.will(returnValue(strong));

      // text1
         this.oneOf(text1).hasChildNodes();
         this.will(returnValue(false));

         this.never(text1).getChildNodes();

      // em
         this.oneOf(em).hasChildNodes();
         this.will(returnValue(true));

         this.oneOf(em).getChildNodes();
         this.will(returnValue(em_children));

         this.oneOf(em_children).getLength();
         this.will(returnValue(1L));

         this.oneOf(em_children).item(0L);
         this.will(returnValue(text2));

      // text2
         this.oneOf(text2).hasChildNodes();
         this.will(returnValue(false));

         this.never(text2).getChildNodes();

      // text3
         this.oneOf(text3).hasChildNodes();
         this.will(returnValue(false));

         this.never(text3).getChildNodes();

      // strong
         this.oneOf(strong).hasChildNodes();
         this.will(returnValue(true));

         this.oneOf(strong).getChildNodes();
         this.will(returnValue(strong_children));

         this.oneOf(strong_children).getLength();
         this.will(returnValue(1L));

         this.oneOf(strong_children).item(0L);
         this.will(returnValue(text4));

      // text4
         this.oneOf(text4).hasChildNodes();
         this.will(returnValue(false));

         this.never(text4).getChildNodes();
         }});

      Selector selector = new Selector(p);
      List<nsIDOMNode> nodes = selector.getAllChildren(p);

      assertEquals(7, nodes.size());
      assertSame(p, nodes.get(0));
      assertSame(text1, nodes.get(1));
      assertSame(em, nodes.get(2));
      assertSame(text2, nodes.get(3));
      assertSame(text3, nodes.get(4));
      assertSame(strong, nodes.get(5));
      assertSame(text4, nodes.get(6));

      context.assertIsSatisfied();
      }

   }
