package Resources;

public enum APIResources
{
    LoginAPI("/api/ecom/auth/login"),
    AddProductAPI("/api/ecom/product/add-product"),
    CreateOrderAPI("/api/ecom/order/create-order"),
    ViewOrderAPI("/api/ecom/order/get-orders-details"),
    DeleteProductAPI("/api/ecom/product/delete-product/{productId}"),
    DeleteOrderAPI("/api/ecom/order/delete-order/{orderId}");

    private String path;
    APIResources(String path)
    {
        this.path=path;
    }
    public String getRequest()
    {
        return path;
    }

}
