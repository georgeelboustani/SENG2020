import java.util.Collection;
import java.util.List;


public abstract class Storage {
	private List<Shelf> shelves;
	private int capacity;
	
	public void addItem(int shelfId, int Product) {
		for (Shelf currentShelf: shelves){
			if (currentShelf.getId() == shelfId) {
				currentShelf.addProductBatch();
			}
		}
	}
}
