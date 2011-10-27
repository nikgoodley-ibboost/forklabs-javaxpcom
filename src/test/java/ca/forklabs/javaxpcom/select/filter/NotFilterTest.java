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

import org.junit.Test;
import org.mozilla.interfaces.nsIDOMNode;
import ca.forklabs.javaxpcom.select.Selector;

import static org.junit.Assert.assertEquals;

/**
 * Class {@code NotFilterTest} tests class {@link NotFilter}.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.filter.NotFilterTest">Daniel Léonard</a>
 * @version $Revision$
 */
@SuppressWarnings({ "boxing", "nls" })
public class NotFilterTest {

//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    */
   public NotFilterTest() {
   // nothing
      }


//---------------------------
// Test methods
//---------------------------

   /**
    * Tests that the evaluation function works.
    */
   @Test
   public void testEval() {
      Selector.Filter yes = new Selector.Filter() { @Override public boolean eval(nsIDOMNode node) { return true; } };
      Selector.Filter no = new Selector.Filter() { @Override public boolean eval(nsIDOMNode node) { return false; } };

      Selector.Filter[] candidates = new Selector.Filter[] {
         new NotFilter(yes),
         new NotFilter(no),
         };

      boolean[] solutions = new boolean[] {
         false, true,
         };

      assertEquals(candidates.length, solutions.length);

      for (int i = 0, len = candidates.length; i < len; i++) {
         Selector.Filter filter = candidates[i];
         boolean answer = filter.eval(null);
         assertEquals("filter #" + (i+1), solutions[i], answer);
         }
      }

   }
