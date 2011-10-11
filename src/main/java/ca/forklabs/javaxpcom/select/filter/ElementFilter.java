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
 * Class {@code ElementFilter} filter nodes based on their name ignore case
 * issues. Node names are also known in HTML as tag
 * <p>
 * Filter {@code new ElementFilter("h1");} will match:
 * <pre>
 * <h1 class="title active"></h1>
 * </pre>
 * and will not match:
 * <pre>
 * <a class="anchor"></a> // wrong name
 * </pre>
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.filter.ElementFilter">Daniel Léonard</a>
 * @version $Revision$
 */
public class ElementFilter implements Selector.Filter {

//---------------------------
// Instance variables
//---------------------------

   /** The name of the element. */
   private String name;


//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    * @param   name   the name of the element.
    */
   public ElementFilter(String name) {
      this.setName(name);
      }


//---------------------------
// Accessors and mutators
//---------------------------

   /**
    * Changes the name of the attribute.
    * @param   name   the new name.
    */
   protected void setName(String name) {
      this.name = name;
      }

   /**
    * Gets the name of the attribute.
    * @return   the name.
    */
   protected String getName() {
      return this.name;
      }


//---------------------------
// Implemented methods from ca.forklabs.javaxpcom.select.Selector.Filter
//---------------------------

   /**
    * Determines if the given node has the correct tag name.
    * @param   node   the node under evaluation.
    * @return   {@code true} if the node matches, {@code false} otherwise.
    */
   @Override
   @SuppressWarnings("hiding")
   public boolean eval(nsIDOMNode node) {
      String name = this.getName();
      String tag = node.getNodeName();
      boolean evaluation = name.equalsIgnoreCase(tag);
      return evaluation;
      }

   }
