package model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Shelf {

	ProductCategory type;
	private int id;
	private List<ProductBatch> batches;
	private int maxProducts;
	private int currentAmount;
	private List<ProductCategory> categories;

	public Shelf(int id, int maxProducts) {
		super();
		this.categories = new ArrayList<ProductCategory>();
		this.id = id;
		this.batches = new ArrayList<ProductBatch>();
		this.maxProducts = maxProducts;
		this.currentAmount = 0;
	}
	
	// TODO - make sure the equals still works even when saved and laoded
	//        from database
	public void assignCategory(ProductCategory category) {
		if (!categories.contains(category)) {
			categories.add(category);
		}
	}
	
	public void removeCategory(ProductCategory category) {
		categories.remove(category);
	}

	public ProductCategory getCategory(){
		return type;
	}
	
	public void setCategory(ProductCategory type){
		this.type = type;
	}
	
	
	public List getProducts(){
		return batches;
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
			if (batch.getProductType().equals(type)) {
				amount += batch.getAmount();
			}
		}
		
		return amount;
	}
	
	
}