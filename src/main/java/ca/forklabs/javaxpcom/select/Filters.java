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

import ca.forklabs.javaxpcom.select.Selector.Filter;
import ca.forklabs.javaxpcom.select.filter.AndFilter;
import ca.forklabs.javaxpcom.select.filter.AttributeValueFilter;
import ca.forklabs.javaxpcom.select.filter.ElementFilter;
import ca.forklabs.javaxpcom.select.filter.NotFilter;
import ca.forklabs.javaxpcom.select.filter.OrFilter;

/**
 * Class {@code Filters} facilitates the creation of filters for
 * {@link Selector}s.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.Filters">Daniel Léonard</a>
 * @version $Revision$
 */
public class Filters {

//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    */
   protected Filters() {
   // nothing
      }


//---------------------------
// Generic filters
//---------------------------

   /**
    * Selects all nodes that have the given attribute with the given value.
    * @param   name   the name of the attribute.
    * @param   regex   the regular expression of the value of the attribute.
    * @return   this selector.
    */
   public static Filter attribute(String name, String regex) {
      Filter filter = new AttributeValueFilter(name, regex);
      return filter;
      }

   /**
    * Selects all node of the given tag name.
    * @param   name   the name of the element.
    * @return   this selector.
    */
   public static Filter element(String name) {
      Filter filter = new ElementFilter(name);
      return filter;
      }

   /**
    * Creates a composite filter that selects nodes if and only if all the
    * filters select it.
    * @param   filters   the filters
    * @return   {@code true} if and only if all the filters return {@code true},
    *           {@code false} otherwise.
    */
   public static Filter and(Filter... filters) {
      Filter and = new AndFilter(filters);
      return and;
      }

   /**
    * Creates a composite filter that selects nodes if at least one of the
    * filters select it.
    * @param   filters   the filters
    * @return   {@code true} if at least one the filters return {@code true},
    *           {@code false} otherwise.
    */
   public static Filter or(Filter... filters) {
      Filter or = new OrFilter(filters);
      return or;
      }

   /**
    * Creates a filter that selects what the given filter rejects and rejects
    * what the given filter selects.
    * @param   filter   the filter.
    * @return   {@code true} if the filter returns {@code false},
    *           {@code false} if the filter returns {@code true}.
    */
   public static Filter not(Filter filter) {
      Filter not = new NotFilter(filter);
      return not;
      }

//---------------------------
// Form-related filters
//---------------------------

   /**
    * Shortcut method to make a filter on {@code &lt;input type="$type"&gt;}.
    * @param   type   the type of input.
    * @return   the filter.
    */
   protected static Filter inputFilter(String type) {
      Filter filter = and(element("input"), //$NON-NLS-1$
                          attribute("type", type) //$NON-NLS-1$
                          );
      return filter;
      }

   /**
    * Selects all button elements and input elements of type button
    * (e.g. {@code &lt;button&gt;} and {@code &lt;input type="button"&gt;}).
    * @return   this selector.
    */
   public static Filter button() {
      Filter filter = or(inputFilter("button"), //$NON-NLS-1$
                         element("button") //$NON-NLS-1$
                         );
      return filter;
      }

   /**
    * Selects all elements of type checkbox
    * (e.g. {@code &lt;input type="checkbox"&gt;}).
    * @return   this selector.
    */
   public static Filter checkbox() {
      Filter filter = inputFilter("checkbox"); //$NON-NLS-1$
      return filter;
      }

   /**
    * Selects all elements of type file
    * (e.g. {@code &lt;input type="file"&gt;}).
    * @return   this selector.
    */
   public static Filter file() {
      Filter filter = inputFilter("file"); //$NON-NLS-1$
      return filter;
      }

   /**
    * Selects all elements of type hidden
    * (e.g. {@code &lt;input type="hidden"&gt;}).
    * @return   this selector.
    */
   public static Filter hidden() {
      Filter filter = inputFilter("hidden"); //$NON-NLS-1$
      return filter;
      }

   /**
    * Selects all elements of type image
    * (e.g. {@code &lt;input type="image"&gt;}).
    * @return   this selector.
    */
   public static Filter image() {
      Filter filter = inputFilter("image"); //$NON-NLS-1$
      return filter;
      }

   /**
    * Selects all input elements of any type (e.g. {@code &lt;button&gt;},
    * {@code &lt;input&gt;}, {@code &lt;select&gt;} and
    * {@code &lt;textarea&gt;}).
    * @return   this selector.
    */
   public static Filter input() {
      Filter filter = or(element("button"), //$NON-NLS-1$
                         element("input"), //$NON-NLS-1$
                         element("select"), //$NON-NLS-1$
                         element("textarea") //$NON-NLS-1$
                         );
      return filter;
      }

   /**
    * Selects all elements of type password
    * (e.g. {@code &lt;input type="password"&gt;}).
    * @return   this selector.
    */
   public static Filter password() {
      Filter filter = inputFilter("password"); //$NON-NLS-1$
      return filter;
      }

   /**
    * Selects all elements of type radio
    * (e.g. {@code &lt;input type="radio"&gt;}).
    * @return   this selector.
    */
   public static Filter radio() {
      Filter filter = inputFilter("radio"); //$NON-NLS-1$
      return filter;
      }

   /**
    * Selects all elements of type reset
    * (e.g. {@code &lt;input type="reset"&gt;}).
    * @return   this selector.
    */
   public static Filter reset() {
      Filter filter = inputFilter("reset"); //$NON-NLS-1$
      return filter;
      }

   /**
    * Selects all input elements of type select
    * (e.g. {@code &lt;select&gt;}).
    * @return   this selector.
    */
   public static Filter select() {
      Filter filter = element("select"); //$NON-NLS-1$
      return filter;
      }

   /**
    * Selects all elements of type submit
    * (e.g. {@code &lt;input type="submit"&gt;}).
    * @return   this selector.
    */
   public static Filter submit() {
      Filter filter = inputFilter("submit"); //$NON-NLS-1$
      return filter;
      }

   /**
    * Selects all elements of type text (e.g. {@code &lt;input type="text"&gt;})
    * or all input element without type.
    * @return   this selector.
    */
   public static Filter text() {
      Filter filter = or(inputFilter("text"), //$NON-NLS-1$
                         and(element("input"), //$NON-NLS-1$
                             not(attribute("type", ".*")) //$NON-NLS-1$ //$NON-NLS-2$
                            )
                         );

      return filter;
      }

   /**
    * Selects all input elements of type textarea
    * (e.g. {@code &lt;textarea&gt;}).
    * @return   this selector.
    */
   public static Filter textarea() {
      Filter filter = element("textarea"); //$NON-NLS-1$
      return filter;
      }

   }

//From http://api.jquery.com/category/selectors/ the filters to make

//:animated Selector
//Basic Filter, jQuery Extensions
//Select all elements that are in the progress of an animation at the time the selector is run.

//Attribute Contains Prefix Selector [name|="value"]
//Attribute
//Selects elements that have the specified attribute with a value either equal to a given string or starting with that string followed by a hyphen (-).

//Attribute Contains Selector [name*="value"]
//Attribute
//Selects elements that have the specified attribute with a value containing the a given substring.

//Attribute Contains Word Selector [name~="value"]
//Attribute
//Selects elements that have the specified attribute with a value containing a given word, delimited by spaces.

//Attribute Ends With Selector [name$="value"]
//Attribute
//Selects elements that have the specified attribute with a value ending exactly with a given string. The comparison is case sensitive.

//Attribute Equals Selector [name="value"]
//Attribute
//Selects elements that have the specified attribute with a value exactly equal to a certain value.

//Attribute Not Equal Selector [name!="value"]
//Attribute, jQuery Extensions
//Select elements that either don't have the specified attribute, or do have the specified attribute but not with a certain value.

//Attribute Starts With Selector [name^="value"]
//Attribute
//Selects elements that have the specified attribute with a value beginning exactly with a given string.

//:checked Selector
//Form
//Matches all elements that are checked.

//Child Selector (“parent > child”)
//Hierarchy
//Selects all direct child elements specified by "child" of elements specified by "parent".

//Class Selector (“.class”)
//Basic
//Selects all elements with the given class.

//:contains() Selector
//Content Filter
//Select all elements that contain the specified text.

//Descendant Selector (“ancestor descendant”)
//Hierarchy
//Selects all elements that are descendants of a given ancestor.

//:disabled Selector
//Form
//Selects all elements that are disabled.

//Element Selector (“element”)
//Basic
//Selects all elements with the given tag name.

//:empty Selector
//Content Filter
//Select all elements that have no children (including text nodes).

//:enabled Selector
//Form
//Selects all elements that are enabled.

//:eq() Selector
//Basic Filter, jQuery Extensions
//Select the element at index n within the matched set.

//:even Selector
//Basic Filter, jQuery Extensions
//Selects even elements, zero-indexed. See also odd.

//:file Selector
//Form, jQuery Extensions
//Selects all elements of type file.

//:first-child Selector
//Child Filter
//Selects all elements that are the first child of their parent.

//:first Selector
//Basic Filter, jQuery Extensions
//Selects the first matched element.

//:focus selector
//Basic Filter, Form
//Selects element if it is currently focused.

//:gt() Selector
//Basic Filter, jQuery Extensions
//Select all elements at an index greater than index within the matched set.

//Has Attribute Selector [name]
//Attribute
//Selects elements that have the specified attribute, with any value.

//:has() Selector
//Content Filter, jQuery Extensions
//Selects elements which contain at least one element that matches the specified selector.

//:header Selector
//Basic Filter, jQuery Extensions
//Selects all elements that are headers, like h1, h2, h3 and so on.

//ID Selector (“#id”)
//Basic
//Selects a single element with the given id attribute.

//:last-child Selector
//Child Filter
//Selects all elements that are the last child of their parent.

//:last Selector
//Basic Filter, jQuery Extensions
//Selects the last matched element.

//:lt() Selector
//Basic Filter, jQuery Extensions
//Select all elements at an index less than index within the matched set.

//Multiple Attribute Selector [name="value"][name2="value2"]
//Attribute
//Matches elements that match all of the specified attribute filters.

//Multiple Selector (“selector1, selector2, selectorN”)
//Basic
//Selects the combined results of all the specified selectors.

//Next Adjacent Selector (“prev + next”)
//Hierarchy
//Selects all next elements matching "next" that are immediately preceded by a sibling "prev".

//Next Siblings Selector (“prev ~ siblings”)
//Hierarchy
//Selects all sibling elements that follow after the "prev" element, have the same parent, and match the filtering "siblings" selector.

//:not() Selector
//Basic Filter
//Selects all elements that do not match the given selector.

//:nth-child() Selector
//Child Filter
//Selects all elements that are the nth-child of their parent.

//:odd Selector
//Basic Filter, jQuery Extensions
//Selects odd elements, zero-indexed. See also even.

//:only-child Selector
//Child Filter
//Selects all elements that are the only child of their parent.

//:parent Selector
//Content Filter, jQuery Extensions
//Select all elements that are the parent of another element, including text nodes.

//:selected Selector
//Form, jQuery Extensions
//Selects all elements that are selected.

//:visible Selector
//jQuery Extensions, Visibility Filter
//Selects all elements that are visible.
