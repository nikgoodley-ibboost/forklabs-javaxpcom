package ca.forklabs.javaxpcom.select.filter;

import java.util.Iterator;
import java.util.List;
import org.mozilla.interfaces.nsIDOMNode;
import ca.forklabs.javaxpcom.select.Selector;

/**
 * Class {@code AndFilter} defines a composite filter that will evaluate
 * favourably a node if and only if it evaluates favourably for all the filters
 * in the composition.
 *
 * @author   <a href="mailto:forklabs at gmail.com?subject=ca.forklabs.javaxpcom.select.filter.AndFilter">Daniel Léonard</a>
 * @version $Revision$
 */
public class AndFilter extends CompositeFilter {

//---------------------------
// Constructors
//---------------------------

   /**
    * Constructor.
    * @param   filters   the list of filters.
    */
   public AndFilter(Selector.Filter... filters) {
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
      boolean evaluation = true;

      List<Selector.Filter> filters = this.getFilters();
      for (Iterator<Selector.Filter> iter = filters.iterator(); evaluation && iter.hasNext(); ) {
         Selector.Filter filter = iter.next();
         evaluation &= filter.eval(node);
         }

      return evaluation;
      }

   }
