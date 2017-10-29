import org.jacop.constraints.LinearInt;
import org.jacop.constraints.XeqY;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;

public class Burger {

	public static void main(String[] args) {
		String[] itemNames = { "Beef Patty", "Bun", "Cheese", "Onions", "Pickles", "Lettuce", "Ketchup", "Tomato" };
		int[] sodiumLevels = { 50, 330, 310, 1, 260, 3, 160, 3 };
		int[] fatLevels = { 17, 9, 6, 2, 0, 0, 0, 0 };
		int[] calories = { 220, 260, 70, 10, 5, 4, 20, 9 };
		int[] costs = { -25, -15, -10, -9, -3, -4, -2, -4 };
		
		solve(itemNames, sodiumLevels, fatLevels, calories, costs);
	}

	private static void solve(String[] itemNames, int[] sodiumLevels, int[] fatLevels, int[] calories, int[] costs) {
		int nbrItems = itemNames.length;
		
		// Create a store
		Store store = new Store();
		
		// Counts of each item
		IntVar[] itemCounts = new IntVar[nbrItems];
		
		// At least one of each item and no more than five(controlled by domain)
		for (int i = 0; i < nbrItems; i++)
			itemCounts[i] = new IntVar(store, 1, 5);
		
		// Set health constraints
		store.impose(new LinearInt(store, itemCounts, sodiumLevels, "<", 3000));
		store.impose(new LinearInt(store, itemCounts, fatLevels, "<", 150));
		store.impose(new LinearInt(store, itemCounts, calories, "<", 3000));
		
		// Taste constraints
		store.impose(new XeqY(itemCounts[5], itemCounts[6])); // Servings of lettuce == servings of ketchup
		store.impose(new XeqY(itemCounts[4], itemCounts[7])); // Servings of pickles == servings of tomatoes
		
		// Cost of burger
		IntVar cost = new IntVar(store, -10000, 0);
		store.impose(new LinearInt(store, itemCounts, costs, "==", cost));
		
		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(itemCounts, null, new IndomainMin<IntVar>());
		boolean result = search.labeling(store, select, cost);
		
		if(result) {
			System.out.println("Items:");
			for(int i = 0; i < nbrItems; i++)
				System.out.println(itemNames[i] + " : " + itemCounts[i].value());
			System.out.println("Cost: " + (-1)*cost.value() + " cents ");
		} else 
			System.out.println("ARGH!!!");
	}

}
