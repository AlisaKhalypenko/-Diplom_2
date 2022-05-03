import java.util.List;

public class Order {
    public List<String> ingredients;

    public Order(){

    }

    public Order (List<String> ingredients){
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String>  setIngredients (List<String> ingredients){
        this.ingredients = ingredients;
        return ingredients;
    }
}
