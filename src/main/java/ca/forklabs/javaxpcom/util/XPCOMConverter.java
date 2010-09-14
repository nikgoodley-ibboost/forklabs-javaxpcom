/*
 * @(#) $Header$
 *
 * Copyright (C) 2010  Daniel Léonard
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

import org.mozilla.interfaces.nsIDOMHTMLAnchorElement;
import org.mozilla.interfaces.nsIDOMHTMLDivElement;
import org.mozilla.interfaces.nsIDOMHTMLFormElement;
import org.mozilla.interfaces.nsIDOMHTMLHeadingElement;
import org.mozilla.interfaces.nsIDOMHTMLInputElement;
import org.mozilla.interfaces.nsIDOMHTMLTableCellElement;
import org.mozilla.interfaces.nsIDOMHTMLTableElement;
import org.mozilla.interfaces.nsIDOMHTMLTableRowElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.xpcom.XPCOMException;


public class XPCOMConverter {

//---------------------------
// Constructors
//---------------------------

   protected XPCOMConverter() {
   // nothing
      }


//---------------------------
// Conversion class methods
//---------------------------

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
    * Query the {@code nsIDOMHTMLHeadingElement} interface ({@code <h1>) ...
    * {@code <h6} from the node.
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

   }
