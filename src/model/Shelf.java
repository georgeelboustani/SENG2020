package model;
import java.util.Collection;
import java.util.List;


public class Shelf {
	ProductCategory type;
	private int id;
	private List<ProductBatch>  batches;
	private int maxProducts;
	private int currentAmount;
	
	public Shelf(ProductCategory type, int id, List<ProductBatch> batches,
			int maxProducts, int currentAmount) {
		super();
		this.type = type;
		this.id = id;
		this.batches = batches;
		this.maxProducts = maxProducts;
		this.currentAmount = currentAmount;
	}

	public int getId() {
		return id;
	}
	
	public void addProductBatch(ProductBatch batch) {
		batches.add(batch);
	}
	
	public int getMaxProducts() {
		return maxProducts;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}
	
	public int getNumProductsOfType(ProductType type) {
		int amount = 0;
		
		for (ProductBatch batch: batches){
			if (batch.getType().equals(type)) {
				amount += batch.getAmount();
			}
		}
		
		return amount;
	}
	
	
}