package modelo;

public class Mascota {
	private int id_mascota;
	private String nombre;
	private String tipo;
	private String genero;
	private String raza;
	private int cedula_cliente;
	
	public int getId_mascota() {
		return id_mascota;
	}
	public void setId_mascota(int id_mascota) {
		this.id_mascota = id_mascota;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getRaza() {
		return raza;
	}
	public void setRaza(String raza) {
		this.raza = raza;
	}
	public int getCedula_cliente() {
		return cedula_cliente;
	}
	public void setCedula_cliente(int cedula_cliente) {
		this.cedula_cliente = cedula_cliente;
	}
	

}

