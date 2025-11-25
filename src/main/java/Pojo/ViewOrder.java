package Pojo;

public class ViewOrder
{
   private ViewOrderDetails data;
   private String message;

    public ViewOrderDetails getData() {
        return data;
    }

    public void setData(ViewOrderDetails data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
