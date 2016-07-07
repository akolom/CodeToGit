package business;


public class Catalog {
	private int id;
	private String name;
	public Catalog() {
		//do nothing
	}
	public Catalog(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String n) {
		name = n;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean equals(Object ob) {
		if(ob == null) return false;
		if(this == ob) return true;
		if(getClass() != ob.getClass()) return false;
		Catalog c = (Catalog)ob;
		return name.equals(c.name);
	}
	
	public int hashCode() {
		int result = 17;
		result += 31 * result + name.hashCode();
		return result;
	}
}
