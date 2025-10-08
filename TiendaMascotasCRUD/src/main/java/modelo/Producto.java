package modelo;

public class Producto {
	private int codigo_barras;
	private String nombre_producto;
	private String marca;
	private double precio;
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
	
}
