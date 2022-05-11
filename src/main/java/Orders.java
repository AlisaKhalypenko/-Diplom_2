import java.util.List;

public class Orders {
    private List<String> ingredients;
    private String _id;
    private String status;
    private int number;
    private String createdAt;
    private String updatedAt;

    public Orders(){
    }

    public Orders (List<String> ingredients, String _id, String status, int number, String createdAt, String updatedAt){
        this.ingredients = ingredients;
        this.set_id(_id);
        this.setStatus(status);
        this.setNumber(number);
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String>  setIngredients (List<String> ingredients){
        this.ingredients = ingredients;
        return ingredients;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
