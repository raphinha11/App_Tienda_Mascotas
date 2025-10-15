package modelo;

public class Producto {
    private int codigo_barras;
    private String nombre_producto;
    private String marca;
    private double precio;
    private int cantidad;
    private double subtotal;

    // 🔹 Constructores
    public Producto() {}

    public Producto(int codigo_barras, String nombre_producto, String marca, double precio) {
        this.codigo_barras = codigo_barras;
        this.nombre_producto = nombre_producto;
        this.marca = marca;
        this.precio = precio;
        this.cantidad = 1;
        this.subtotal = precio;
    }

    // 🔹 Getters y Setters
    public int getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(int codigo_barras) {
        this.codigo_barras = codigo_barras;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    // 🔹 Método para calcular el subtotal
    public void calcularSubtotal() {
        this.subtotal = this.precio * this.cantidad;
    }
}
