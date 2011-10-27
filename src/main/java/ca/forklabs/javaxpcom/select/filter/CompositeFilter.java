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

import java.util.LinkedList;
import java.util.List;
import ca.forklabs.javaxpcom.select.Selector;

/**
 * Class {@code CompositeFilter} lays down the basics to make composite filters.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.filter.CompositeFilter">Daniel Léonard</a>
 * @version $Revision$
 */
public abstract class CompositeFilter implements Selector.Filter {

//---------------------------
// Instance variables
//---------------------------

   /** The list of filters. */
   private List<Selector.Filter> filters = new LinkedList<Selector.Filter>();


//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    * @param   filters   the list of filters.
    */
   protected CompositeFilter(Selector.Filter... filters) {
      this.addFilters(filters);
      }


//---------------------------
// Accessors and mutators
//---------------------------

   /**
    * Gets the list of filters.
    * @return   the list of filters.
    */
   protected List<Selector.Filter> getFilters() {
      return this.filters;
      }


//---------------------------
// Instance methods
//---------------------------

   /**
    * Adds a filter to the composition.
    * @param   filter   the new filter.
    * @return   this composite filter.
    */
   @SuppressWarnings("hiding")
   public CompositeFilter addFilter(Selector.Filter filter) {
      List<Selector.Filter> filters = this.getFilters();
      filters.add(filter);
      return this;
      }

   /**
    * Adds many filters to the composition.
    * @param   filters   the new filters.
    * @return   this composite filter.
    */
   @SuppressWarnings("hiding")
   public CompositeFilter addFilters(Selector.Filter... filters) {
      for (Selector.Filter filter : filters) {
         this.addFilter(filter);
         }
      return this;
      }

   }
