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

import java.util.LinkedList;
import java.util.List;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import ca.forklabs.baselib.util.Algorithm;
import ca.forklabs.baselib.util.Iterators;
import ca.forklabs.baselib.util.Predicates;
import ca.forklabs.baselib.util.UnaryPredicate;
import ca.forklabs.javaxpcom.Crawler;
import ca.forklabs.javaxpcom.select.filter.AndFilter;
import ca.forklabs.javaxpcom.select.filter.AttributeValueFilter;
import ca.forklabs.javaxpcom.select.filter.ElementFilter;
import ca.forklabs.javaxpcom.select.filter.OrFilter;
import ca.forklabs.javaxpcom.util.XPCOMConverter;

/**
 * Class {@code Selector} matches a list of nodes in a document.
 * <p>
 * A selector is created from a root node, usually the document itself. An easy
 * way to get a selector is to use {@link Crawler#selector()} and work from that
 * instance. Shortcut methods are provided to create and add filters to narrow
 * the selection.
 * <p>
 * Once filters have been applied, the list matching nodes can be gotten by
 * calling {@link #list()}. Once the list has been gotten, the selector instance
 * should be discarded.
 * <p>
 * From {@link ca.forklabs.javaxpcom.ProjectPageCrawler#exploreMainMenu()}:
 * <pre>
 *    List<nsIDOMNode> anchors = this.selector()
 *                       .element("a")
 *                       .attribute("class", "tab")
 *                       .list();
 * </pre>
 * gives the list of the five menu items at the
 * <a href="http://code.google.com/p/forklabs-javaxpcom/">top of this project page</a>
 * <p>
 * The selector works a little bit like the {@code $()} variable from
 * <a href="http://api.jquery.com/category/selectors/">jQuery</a>.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.Selector">Daniel Léonard</a>
 * @version $Revision$
 */
public class Selector {

//---------------------------
// Inner classes
//---------------------------

   /**
    * Gives a type to the node filters.
    */
   public interface Filter extends UnaryPredicate<nsIDOMNode> { /* nothing */ }


//---------------------------
// Instance variables
//---------------------------

   /** The root node. */
   private nsIDOMNode root;

   /** The list of filters. */
   private List<UnaryPredicate<nsIDOMNode>> filters = new LinkedList<UnaryPredicate<nsIDOMNode>>();


//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    * @param   root   the root node for the selection.
    */
   public Selector(nsIDOMNode root) {
      this.setRoot(root);
      }


//---------------------------
// Accessors and mutators
//---------------------------

   /**
    * Gets the root node.
    * @return   the root node.
    */
   protected nsIDOMNode getRoot() {
      return this.root;
      }

   /**
    * Changes the root node.
    * @param   root   the new root node.
    */
   protected void setRoot(nsIDOMNode root) {
      this.root = root;
      }

   /**
    * Gets the list of filters.
    * @return   the list of filters.
    */
   protected List<UnaryPredicate<nsIDOMNode>> getFilters() {
      return this.filters;
      }

//---------------------------
// Utilitarian instance methods
//---------------------------

   /**
    * Makes a pre-order list of all the nodes under the given node. The first
    * node of the list is the given node.
    * @param   node   the root node.
    * @return   the list of the root with all its children nodes.
    */
   protected List<nsIDOMNode> getAllChildren(nsIDOMNode node) {
      List<nsIDOMNode> list = new LinkedList<nsIDOMNode>();

      list.add(node);

      boolean has_children = node.hasChildNodes();
      if (has_children) {
         nsIDOMNodeList child_nodes = node.getChildNodes();
         for (long l = 0, len = child_nodes.getLength(); l < len; l++) {
         // child will be added in the list of children
            nsIDOMNode child = child_nodes.item(l);
            List<nsIDOMNode> children = this.getAllChildren(child);
            list.addAll(children);
            }
         }

      return list;
      }


//---------------------------
// Instance methods
//---------------------------

   /**
    * Adds a filter to the list of filter.
    * @param   filter   a new filter.
    * @return   this selector instance.
    */
   @SuppressWarnings("hiding")
   public Selector addFilter(Filter filter) {
      List<UnaryPredicate<nsIDOMNode>> filters = this.getFilters();
      filters.add(filter);
      return this;
      }

   /**
    * Adds filters to the list of filter.
    * @param   filters   new filters.
    * @return   this selector instance.
    */
   @SuppressWarnings("hiding")
   public Selector addFilters(Filter... filters) {
      Iterable<Filter> iterable = Iterators.asIterable(filters);
      this.addFilters(iterable);
      return this;
      }

   /**
    * Adds filters to the list of filter.
    * @param   filters   new filters.
    * @return   this selector instance.
    */
   @SuppressWarnings("hiding")
   public Selector addFilters(Iterable<Filter> filters) {
      for (Filter filter : filters) {
         this.addFilter(filter);
         }
      return this;
      }


//---------------------------
// Generic filters
//---------------------------

   /**
    * Adds an {@link AttributeValueFilter} of the given name and given value.
    * @param   name   the name of the attribute.
    * @param   regex   the regular expression of the value of the attribute.
    * @return   this selector.
    */
   public Selector attribute(String name, String regex) {
// TODO : add methods for attribute selection: http://api.jquery.com/category/selectors/
      Filter filter = new AttributeValueFilter(name, regex);
      this.addFilter(filter);
      return this;
      }

   /**
    * Adds an {@link ElementFilter} of the given name.
    * @param   name   the name of the element.
    * @return   this selector.
    */
   public Selector element(String name) {
// TODO : add methods matching tags: http://api.jquery.com/category/selectors/
      Filter filter = new ElementFilter(name);
      this.addFilter(filter);
      return this;
      }


//---------------------------
// Specific filters
//---------------------------

   /**
    * Shortcut method to make a filter on {@code &lt;input type="$type"&gt;}.
    * @param   type   the type of input.
    * @return   the filter.
    */
   protected Filter newInputFilter(String type) {
      Filter input_filter = new ElementFilter("input"); //$NON-NLS-1$
      Filter type_filter = new AttributeValueFilter("type", type); //$NON-NLS-1$
      Filter filter = new AndFilter(input_filter, type_filter);
      return filter;
      }

   /**
    * Selects all button elements and input elements of type button
    * (e.g. {@code &lt;button&gt;} and {@code &lt;input type="button"&gt;}).
    * @return   this selector.
    */
   public Selector button() {
      Filter input_type_button = this.newInputFilter("button"); //$NON-NLS-1$
      Filter button = new ElementFilter("button"); //$NON-NLS-1$
      Filter filter = new OrFilter(input_type_button, button);
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all elements of type checkbox
    * (e.g. {@code &lt;input type="checkbox"&gt;}).
    * @return   this selector.
    */
   public Selector checkbox() {
      Filter filter = this.newInputFilter("checkbox"); //$NON-NLS-1$
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all elements of type file
    * (e.g. {@code &lt;input type="file"&gt;}).
    * @return   this selector.
    */
   public Selector file() {
      Filter filter = this.newInputFilter("file"); //$NON-NLS-1$
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all elements of type hidden
    * (e.g. {@code &lt;input type="hidden"&gt;}).
    * @return   this selector.
    */
   public Selector hidden() {
      Filter filter = this.newInputFilter("hidden"); //$NON-NLS-1$
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all elements of type image
    * (e.g. {@code &lt;input type="image"&gt;}).
    * @return   this selector.
    */
   public Selector image() {
      Filter filter = this.newInputFilter("image"); //$NON-NLS-1$
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all input elements of any type (e.g. {@code &lt;button&gt;},
    * {@code &lt;input&gt;}, {@code &lt;select&gt;} and
    * {@code &lt;textarea&gt;}).
    * @return   this selector.
    */
   public Selector input() {
      Filter button = new ElementFilter("button"); //$NON-NLS-1$
      Filter input = new ElementFilter("input"); //$NON-NLS-1$
      Filter select = new ElementFilter("select"); //$NON-NLS-1$
      Filter textarea = new ElementFilter("textarea"); //$NON-NLS-1$
      Filter filter = new OrFilter(button, input, select, textarea);
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all elements of type password
    * (e.g. {@code &lt;input type="password"&gt;}).
    * @return   this selector.
    */
   public Selector password() {
      Filter filter = this.newInputFilter("password"); //$NON-NLS-1$
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all elements of type radio
    * (e.g. {@code &lt;input type="radio"&gt;}).
    * @return   this selector.
    */
   public Selector radio() {
      Filter filter = this.newInputFilter("radio"); //$NON-NLS-1$
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all elements of type reset
    * (e.g. {@code &lt;input type="reset"&gt;}).
    * @return   this selector.
    */
   public Selector reset() {
      Filter filter = this.newInputFilter("reset"); //$NON-NLS-1$
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all input elements of type select
    * (e.g. {@code &lt;select&gt;}).
    * @return   this selector.
    */
   public Selector select() {
      this.element("select"); //$NON-NLS-1$
      return this;
      }

   /**
    * Selects all elements of type submit
    * (e.g. {@code &lt;input type="submit"&gt;}).
    * @return   this selector.
    */
   public Selector submit() {
      Filter filter = this.newInputFilter("submit"); //$NON-NLS-1$
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all elements of type text
    * (e.g. {@code &lt;input type="text"&gt;}).
    * @return   this selector.
    */
   public Selector text() {
      Filter input_type_text = this.newInputFilter("text"); //$NON-NLS-1$
      Filter input = new ElementFilter("input") { //$NON-NLS-1$
         @Override
         public boolean eval(nsIDOMNode node) {
            boolean evaluation = super.eval(node);

            if (evaluation) {
            // <input> without type are text input
               String value = XPCOMConverter.getAttributeValue(node, "type"); //$NON-NLS-1$
               evaluation = (null == value);
               }

            return evaluation;
            }
         };
      Filter filter = new OrFilter(input_type_text, input);
      this.addFilter(filter);
      return this;
      }

   /**
    * Selects all input elements of type textarea
    * (e.g. {@code &lt;textarea&gt;}).
    * @return   this selector.
    */
   public Selector textarea() {
      this.element("textarea"); //$NON-NLS-1$
      return this;
      }


//---------------------------
// list()
//---------------------------

   /**
    * Filters all the children nodes and returns the list of nodes that match
    * the filters.
    * @return   the list of nodes that match the filters.
    */
   @SuppressWarnings("hiding")
   public List<nsIDOMNode> list() {
      nsIDOMNode root = this.getRoot();
      List<nsIDOMNode> candidates = this.getAllChildren(root);

      List<UnaryPredicate<nsIDOMNode>> filters = this.getFilters();
      for (UnaryPredicate<nsIDOMNode> filter : filters) {
         Algorithm.removeIf(candidates, Predicates.not1(filter));
         }

      return candidates;
      }

   }

// From http://api.jquery.com/category/selectors/ the filters to make

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
