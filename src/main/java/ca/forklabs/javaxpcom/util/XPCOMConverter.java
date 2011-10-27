/*
 * @(#) $Header$
 *
 * Copyright (C) 2010  Forklabs Daniel Léonard
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

package ca.forklabs.javaxpcom.util;

import java.util.HashMap;
import java.util.Map;
import org.mozilla.interfaces.nsIDOMHTMLAnchorElement;
import org.mozilla.interfaces.nsIDOMHTMLDivElement;
import org.mozilla.interfaces.nsIDOMHTMLElement;
import org.mozilla.interfaces.nsIDOMHTMLFormElement;
import org.mozilla.interfaces.nsIDOMHTMLHeadingElement;
import org.mozilla.interfaces.nsIDOMHTMLInputElement;
import org.mozilla.interfaces.nsIDOMHTMLTableCellElement;
import org.mozilla.interfaces.nsIDOMHTMLTableElement;
import org.mozilla.interfaces.nsIDOMHTMLTableRowElement;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.mozilla.interfaces.nsIDOMText;

import org.mozilla.xpcom.XPCOMException;

/**
 * Class {@code XPCOMConverterTest} is a rudimentary set of methods to convert from
 * {@link nsIDOMNode}s to specialized nodes.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.util.XPCOMConverterTest">Daniel Léonard</a>
 * @version $Revision$
 */
public class XPCOMConverter {

//---------------------------
// Constructors
//---------------------------

   /**
    * Open constructor for converter subclasses.
    */
   protected XPCOMConverter() {
   // nothing
      }


//---------------------------
// Conversion class methods
//---------------------------

   /**
    * Extract the plain text of this node and all its children. Not to be
    * confused with {@link #asTextNode(nsIDOMNode)}.
    * @param   node   the node from which to extract the text.
    * @return   the inner plain text.
    * @see   #asTextNode(nsIDOMNode)
    */
   public static String asPlainText(nsIDOMNode node) {
      StringBuilder sb = new StringBuilder();

      int type = node.getNodeType();
      if (nsIDOMNode.TEXT_NODE == type) {
         String text = node.getNodeValue();
         sb.append(text);
         }

      nsIDOMNodeList children = node.getChildNodes();
      for (long i = 0, len = children.getLength(); i < len; i++) {
         nsIDOMNode child = children.item(i);
         String text = asPlainText(child);
         sb.append(text);
         }

      String text = sb.toString();
      return text;
      }

   /**
    * Creates a map of all the attributes of the node
    * @param   node   the node.
    * @return   the map of attributes.
    */
   public static Map<String, String> attributes(nsIDOMNode node) {
      Map<String, String> attributes = new HashMap<String, String>();

      nsIDOMNamedNodeMap node_map = node.getAttributes();
      for (long l = 0, len = node_map.getLength(); l < len; l++) {
         nsIDOMNode attribute = node_map.item(l);
         String name = attribute.getNodeName();
         String value = attribute.getNodeValue();
         attributes.put(name, value);
         }

      return attributes;
      }

   /**
    * Gets the value of a specific attribute.
    * @param   node   the node.
    * @param   name   the name of the attribute.
    * @return   the value of the attribute if present, {@code null} otherwise.
    */
   public static String getAttributeValue(nsIDOMNode node, String name) {
      String value = null;

      nsIDOMNamedNodeMap attributes = node.getAttributes();
      nsIDOMNode attribute = attributes.getNamedItem(name);
      if (null != attribute) {
         value = attribute.getNodeValue();
         }

      return value;
      }

   /**
    * Query the {@code nsIDOMHTMLElement} interface from the node.
    * @param   node   the node to convert.
    * @exception   XPCOMException   if the node is not an anchor.
    */
   public static nsIDOMHTMLElement asHTMLElement(nsIDOMNode node) {
      nsIDOMHTMLElement element = (nsIDOMHTMLElement) node.queryInterface(nsIDOMHTMLElement.NS_IDOMHTMLELEMENT_IID);
      return element;
      }


   /**
    * Query the {@code nsIDOMHTMLAnchorElement} interface from the node.
    * @param   node   the node to convert.
    * @exception   XPCOMException   if the node is not an anchor.
    */
   public static nsIDOMHTMLAnchorElement asAnchor(nsIDOMNode node) {
      nsIDOMHTMLAnchorElement anchor = (nsIDOMHTMLAnchorElement) node.queryInterface(nsIDOMHTMLAnchorElement.NS_IDOMHTMLANCHORELEMENT_IID);
      return anchor;
      }

   /**
    * Query the {@code nsIDOMHTMLDivElement} interface from the node.
    * @param   node   the node to convert.
    * @exception   XPCOMException   if the node is not a div.
    */
   public static nsIDOMHTMLDivElement asDiv(nsIDOMNode node) {
      nsIDOMHTMLDivElement div = (nsIDOMHTMLDivElement) node.queryInterface(nsIDOMHTMLDivElement.NS_IDOMHTMLDIVELEMENT_IID);
      return div;
      }

   /**
    * Query the {@code nsIDOMHTMLFormElement} interface from the node.
    * @param   node   the node to convert.
    * @exception   XPCOMException   if the node is not a form.
    */
   public static nsIDOMHTMLFormElement asForm(nsIDOMNode node) {
      nsIDOMHTMLFormElement form = (nsIDOMHTMLFormElement) node.queryInterface(nsIDOMHTMLFormElement.NS_IDOMHTMLFORMELEMENT_IID);
      return form;
      }

   /**
    * Query the {@code nsIDOMHTMLHeadingElement} interface ({@code <h1>} ...
    * {@code <h6>}) from the node.
    * @param   node   the node to convert.
    * @exception   XPCOMException   if the node is not a heading.
    */
   public static nsIDOMHTMLHeadingElement asHeading(nsIDOMNode node) {
      nsIDOMHTMLHeadingElement heading = (nsIDOMHTMLHeadingElement) node.queryInterface(nsIDOMHTMLHeadingElement.NS_IDOMHTMLHEADINGELEMENT_IID);
      return heading;
      }

   /**
    * Query the {@code nsIDOMHTMLInputElement} interface from the node.
    * @param   node   the node to convert.
    * @exception   XPCOMException   if the node is not an input.
    */
   public static nsIDOMHTMLInputElement asInput(nsIDOMNode node) {
      nsIDOMHTMLInputElement input = (nsIDOMHTMLInputElement) node.queryInterface(nsIDOMHTMLInputElement.NS_IDOMHTMLINPUTELEMENT_IID);
      return input;
      }

   /**
    * Query the {@code nsIDOMHTMLTableElement} interface from the node.
    * @param   node   the node to convert.
    * @exception   XPCOMException   if the node is not a table.
    */
   public static nsIDOMHTMLTableElement asTable(nsIDOMNode node) {
      nsIDOMHTMLTableElement table = (nsIDOMHTMLTableElement) node.queryInterface(nsIDOMHTMLTableElement.NS_IDOMHTMLTABLEELEMENT_IID);
      return table;
      }

   /**
    * Query the {@code nsIDOMHTMLTableCellElement} interface ({@code <th>} or
    * {@code <td>}) from the node.
    * @param   node   the node to convert.
    * @exception   XPCOMException   if the node is not a table cell.
    */
   public static nsIDOMHTMLTableCellElement asTableCell(nsIDOMNode node) {
      nsIDOMHTMLTableCellElement cell = (nsIDOMHTMLTableCellElement) node.queryInterface(nsIDOMHTMLTableCellElement.NS_IDOMHTMLTABLECELLELEMENT_IID);
      return cell;
      }

   /**
    * Query the {@code nsIDOMHTMLTableRowElement} interface from the node.
    * @param   node   the node to convert.
    * @exception   XPCOMException   if the node is not a table row.
    */
   public static nsIDOMHTMLTableRowElement asTableRow(nsIDOMNode node) {
      nsIDOMHTMLTableRowElement row = (nsIDOMHTMLTableRowElement) node.queryInterface(nsIDOMHTMLTableRowElement.NS_IDOMHTMLTABLEROWELEMENT_IID);
      return row;
      }

   /**
    * Query the {@code nsIDOMText} interface from the node. Extracting the plain
    * inner text of a node should be done with {@link #asPlainText(nsIDOMNode)}.
    * @param   node   the node to convert.
    * @exception   XPCOMException   if the node is not a text node.
    * @see   #asPlainText(nsIDOMNode)
    */
   public static nsIDOMText asTextNode(nsIDOMNode node) {
      nsIDOMText text = (nsIDOMText) node.queryInterface(nsIDOMText.NS_IDOMTEXT_IID);
      return text;
      }

   }
