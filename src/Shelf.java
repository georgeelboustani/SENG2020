import java.util.Collection;
import java.util.List;


public class Shelf {
	ProductCategory type;
	private int id;
	private List<ProductBatch>  batches;
	
	public int getId() {
		return id;
	}
	
	public void addProductBatch(ProductBatch batch) {
		batches.add(batch);
	}
	
}