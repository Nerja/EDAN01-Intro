import java.util.Arrays;

import org.jacop.constraints.Alldifferent;
import org.jacop.constraints.LinearInt;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;

public class Balance {

	public static void main(String[] args) {
		Store store = new Store();

		// FDV:s weights
		IntVar[] weights = new IntVar[9];
		for (int i = 0; i < 9; i++)
			weights[i] = new IntVar(store, "w_" + ((char) ('a' + i)), 1, 9);

		store.impose(new Alldifferent(weights));

		int[] balanceCoeffs = { 8, 5, 4, -2, -3, -5, -4, -5, -9 };
		store.impose(new LinearInt(store, weights, balanceCoeffs, "==", 0));

		Search<IntVar> search = new DepthFirstSearch<IntVar>();

		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(weights, null, new IndomainMin<IntVar>());

		boolean result = search.labeling(store, select);

		if (result) {
			System.out.println("\n*** Yes");
			System.out.println("Solution: " + Arrays.asList(weights));
		} else {
			System.out.println("\n*** No");
		}
	}

}
