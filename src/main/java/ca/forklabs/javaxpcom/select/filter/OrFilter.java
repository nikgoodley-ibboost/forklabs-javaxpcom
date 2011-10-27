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

import java.util.Iterator;
import java.util.List;
import org.mozilla.interfaces.nsIDOMNode;
import ca.forklabs.javaxpcom.select.Selector;

/**
 * Class {@code OrFilter} defines a composite filter that will evaluate
 * favourably a node if any filter in the composition evaluates favourably.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.filter.OrFilter">Daniel Léonard</a>
 * @version $Revision$
 */
public class OrFilter extends CompositeFilter {

//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    * @param   filters   the list of filters.
    */
   public OrFilter(Selector.Filter... filters) {
      super(filters);
      }


//---------------------------
// Implemented methods from ca.forklabs.javaxpcom.select.Selector.Filter
//---------------------------

   /**
    * Determines if the given node matches all the filters in the composition.
    * @param   node   the node under evaluation.
    * @return   {@code true} if the node matches all the filters,
    *           {@code false} otherwise.
    */
   @Override
   public boolean eval(nsIDOMNode node) {
      boolean evaluation = false;

      List<Selector.Filter> filters = this.getFilters();
      for (Iterator<Selector.Filter> iter = filters.iterator(); (false == evaluation) && iter.hasNext(); ) {
         Selector.Filter filter = iter.next();
         evaluation |= filter.eval(node);
         }

      return evaluation;
      }

   }
