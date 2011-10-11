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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import ca.forklabs.javaxpcom.select.Selector;

/**
 * Class {@code AttributeValueFilter} filters nodes based on the value of their
 * attributes. To be selected, a node must have said attribute and its value
 * must match the regular expression.
 * <p>
 * The <a href="http://download.oracle.com/javase/tutorial/essential/regex/">regular expression rules</a>
 * are described on Oracle's Java website.
 * <p>
 * Filter {@code new AttributeValueFilter("class", "title");} will match:
 * <pre>
 * <h1 class="title active"></h1>
 * </pre>
 * and will not match:
 * <pre>
 * <a class="anchor"></a> // wrong attribute value
 * <li id="123">          // wrong attribute
 * <p></p>                // wrong attribute - no attribute
 * </pre>
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.filter.AttributeValueFilter">Daniel Léonard</a>
 * @version $Revision$
 */
public class AttributeValueFilter implements Selector.Filter {

//---------------------------
// Instance variables
//---------------------------

   /** The name of the attribute. */
   private String name;

   /** The pattern of the value of the attribute. */
   private Pattern pattern;


//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    * @param   name   the name of the attribute.
    * @param   pattern   the pattern of the value of the attribute.
    */
   public AttributeValueFilter(String name, String regex) {
      this.setName(name);
      this.setPattern(Pattern.compile(regex));
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

   /**
    * Changes the pattern of the value of the attribute.
    * @param   pattern   the new pattern.
    */
   protected void setPattern(Pattern pattern) {
      this.pattern = pattern;
      }

   /**
    * Gets the pattern of the value of the attribute.
    * @return   the pattern.
    */
   protected Pattern getPattern() {
      return this.pattern;
      }


//---------------------------
// Implemented methods from ca.forklabs.javaxpcom.select.Selector.Filter
//---------------------------

   /**
    * Determines if the given node has an attribute with the correct name and
    * if the value of that attribute is matched by the pattern.
    * @param   node   the node under evaluation.
    * @return   {@code true} if the node matches, {@code false} otherwise.
    */
   @Override
   @SuppressWarnings("hiding")
   public boolean eval(nsIDOMNode node) {
      boolean evaluation = false;

      boolean has_attribute = node.hasAttributes();
      if (has_attribute) {
         nsIDOMNamedNodeMap attributes = node.getAttributes();

         String name = this.getName();
         nsIDOMNode attribute = attributes.getNamedItem(name);

         if (null != attribute) {
            Pattern pattern = this.getPattern();
            String value = attribute.getNodeValue();
            Matcher matcher = pattern.matcher(value);
            evaluation = matcher.find();
            }
         }

      return evaluation;
      }

   }
