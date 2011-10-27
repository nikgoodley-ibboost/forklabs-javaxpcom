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
 *                       .add(Filters.element("a"))
 *                       .add(Filters.attribute("class", "tab"))
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
   public Selector add(Filter filter) {
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
   public Selector add(Filter... filters) {
      Iterable<Filter> iterable = Iterators.asIterable(filters);
      this.add(iterable);
      return this;
      }

   /**
    * Adds filters to the list of filter.
    * @param   filters   new filters.
    * @return   this selector instance.
    */
   @SuppressWarnings("hiding")
   public Selector add(Iterable<Filter> filters) {
      for (Filter filter : filters) {
         this.add(filter);
         }
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
