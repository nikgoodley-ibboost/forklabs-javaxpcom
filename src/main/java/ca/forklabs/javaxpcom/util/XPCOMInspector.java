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

import org.mozilla.interfaces.nsIDOMAttr;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNamedNodeMap;
import org.mozilla.interfaces.nsIDOMNodeList;

/**
 * Class {@code XPCOMInspector} is a rudimentary set of methods to inspect
 * {@link nsIDOMNode}s.
 *
 * The main inspection entry point is {@link #inspect(nsIDOMNode)} for a
 * non-recursive inspection and its two-parameter sibling to control the
 * recursion into child nodes.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.util.XPCOMInspector">Daniel Léonard</a>
 * @version $Revision$
 */
@SuppressWarnings({ "unchecked", "nls" })
public class XPCOMInspector {

//---------------------------
// Constructor
//---------------------------

   /**
    * Constructor.
    */
   protected XPCOMInspector() {
   // nothing
      }


//---------------------------
// Inspection class methods
//---------------------------

   /**
    * Inspects the node declaration, that is its Java type as well as the
    * interfaces it implements.
    * @param   node   the node to inspect.
    */
   public static void inspectNodeDeclaration(nsIDOMNode node) {
      Class clazz = node.getClass();
      Class[] classes = clazz.getInterfaces();

      System.out.print("The node is of class " + clazz.getName() + " and implements interfaces: ");
      for (Class c : classes) {
         System.out.print(c.getName() + ", ");
         }
      System.out.println();
      }

   /**
    * Inspects the type of node.
    * @param   node   the node to inspect.
    */
   public static void inspectNodeType(nsIDOMNode node) {
      String type;

      int node_type = node.getNodeType();
      switch (node_type) {
         case nsIDOMNode.ELEMENT_NODE:
            type = "ELEMENT_NODE"; break;
         case nsIDOMNode.ATTRIBUTE_NODE:
            type = "ATTRIBUTE_NODE"; break;
         case nsIDOMNode.TEXT_NODE:
            type = "TEXT_NODE"; break;
         case nsIDOMNode.CDATA_SECTION_NODE:
            type = "CDATA_SECTION_NODE"; break;
         case nsIDOMNode.ENTITY_REFERENCE_NODE:
            type = "ENTITY_REFERENCE_NODE"; break;
         case nsIDOMNode.ENTITY_NODE:
            type = "ENTITY_NODE"; break;
         case nsIDOMNode.PROCESSING_INSTRUCTION_NODE:
            type = "PROCESSING_INSTRUCTION_NODE"; break;
         case nsIDOMNode.COMMENT_NODE:
            type = "COMMENT_NODE"; break;
         case nsIDOMNode.DOCUMENT_NODE:
            type = "DOCUMENT_NODE"; break;
         case nsIDOMNode.DOCUMENT_TYPE_NODE:
            type = "DOCUMENT_TYPE_NODE"; break;
         case nsIDOMNode.DOCUMENT_FRAGMENT_NODE:
            type = "DOCUMENT_FRAGMENT_NODE"; break;
         case nsIDOMNode.NOTATION_NODE:
            type = "NOTATION_NODE"; break;
         default:
            type = "<unknown node>"; break;
         }

      System.out.println("Node type is " + node_type + " which is " + type + " and its class is " + node.getClass());
      }

   /**
    * Inspects the different names of the node.
    * @param   node   the node to inspect.
    */
   public static void inspectNodeNames(nsIDOMNode node) {
      System.out.println("namespaceURI=" + node.getNamespaceURI());
      System.out.println("prefix=" + node.getPrefix());
      System.out.println("nodeName=" + node.getNodeName());
      System.out.println("localName=" + node.getLocalName());
      System.out.println("nodeValue=" + node.getNodeValue());
      }

   /**
    * Inspects the different attributes of the node.
    * @param   node   the node to inspect.
    */
   public static void inspectAttributes(nsIDOMNode node) {
      boolean has_attributes = node.hasAttributes();
      if (has_attributes) {
         nsIDOMNamedNodeMap attributes = node.getAttributes();
         long len = attributes.getLength();
         System.out.println("The node has " + len + " attributes");
         for (long i = 0; i < len; i++) {
            nsIDOMAttr attribute = (nsIDOMAttr) attributes.item(i).queryInterface(nsIDOMAttr.NS_IDOMATTR_IID);
            System.out.println("Attribute #" + i + " -> " + attribute.getName() + "=" + attribute.getValue());
            }
         }
      else {
         System.out.println("The node does not have attributes");
         }
      }

   /**
    * Inspects the different children of the node.
    * @param   node   the node to inspect.
    * @param   recursive   {@code true} to also inspect the children, then their
    *                      children, depth-first; {@code false} otherwise.
    */
   public static void inspectChildren(nsIDOMNode node, boolean recursive) {
      boolean has_children = node.hasChildNodes();
      if (has_children) {
         nsIDOMNodeList children = node.getChildNodes();
         long len = children.getLength();

         System.out.print("The node has " + len + " children: ");
         for (long i = 0; i < len; i++) {
            String name = children.item(i).getNodeName();
            System.out.print(name + ",");
            }
         System.out.println();

         for (long i = 0; recursive && i < len; i++) {
            nsIDOMNode child = children.item(i);
            System.out.println("Inspecting children #" + i);
            inspect(child, true);
            }
         }
      else {
         System.out.println("The node does not have children");
         }
      }

   /**
    * Inspect only the node. It calls, in order,
    * {@link #inspectNodeType(nsIDOMNode)},
    * {@link #inspectNodeNames(nsIDOMNode)},
    * {@link #inspectAttributes(nsIDOMNode)} and
    * {@link #inspectChildren(nsIDOMNode, boolean)}.
    * @param   node   the node to inspect.
    */
   public static void inspect(nsIDOMNode node) {
      inspect(node, false);
      }

   /**
    * Inspect the node. It calls, in order,
    * {@link #inspectNodeType(nsIDOMNode)},
    * {@link #inspectNodeNames(nsIDOMNode)},
    * {@link #inspectAttributes(nsIDOMNode)} and
    * {@link #inspectChildren(nsIDOMNode, boolean)}.
    * @param   node   the node to inspect.
    * @param   recursive   {@code true} to also inspect the children, then their
    *                      children, depth-first; {@code false} otherwise.
    */
   public static void inspect(nsIDOMNode node, boolean recursive) {
      if (null == node) {
         System.out.println("The node is <null>");
         return;
         }

      //inspectNodeDeclaration(node);
      inspectNodeType(node);
      inspectNodeNames(node);
      inspectAttributes(node);
      inspectChildren(node, recursive);
      }

   }
