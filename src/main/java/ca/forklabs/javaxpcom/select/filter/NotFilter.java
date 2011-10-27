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

import org.mozilla.interfaces.nsIDOMNode;
import ca.forklabs.javaxpcom.select.Selector;

/**
 * Class {@code NotFilter} evaluates to the opposite of its adapted filter.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.filter.NotFilter">Daniel Léonard</a>
 * @version $Revision$
 */
public class NotFilter implements Selector.Filter {

//---------------------------
// Instance variables
//---------------------------

   /** The adapted filter. */
   private Selector.Filter filter;


//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    * @param   filter   the filter.
    */
   public NotFilter(Selector.Filter filter) {
      this.setFilter(filter);
      }


//---------------------------
// Accessors and mutators
//---------------------------

   /**
    * Changes the filter.
    * @param   filter   the new filter.
    */
   protected void setFilter(Selector.Filter filter) {
      this.filter = filter;
      }

   /**
    * Gets the filter.
    * @return   the filter.
    */
   protected Selector.Filter getFilter() {
      return this.filter;
      }


//---------------------------
// Implemented methods from ca.forklabs.javaxpcom.select.Selector.Filter
//---------------------------

   /**
    * Evaluates to the opposite of the adapted filter.
    * @param   node   the node under evaluation.
    * @return   {@code true} if evaluated to {@code false},
    *           {@code false} if evaluated to {@code true}.
    */
   @Override
   @SuppressWarnings("hiding")
   public boolean eval(nsIDOMNode node) {
      Selector.Filter filter = this.getFilter();

      boolean evaluation = filter.eval(node);
      evaluation = !evaluation;

      return evaluation;
      }

   }
