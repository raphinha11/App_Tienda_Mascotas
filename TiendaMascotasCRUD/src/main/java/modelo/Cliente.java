package modelo;

public class Cliente {
    private int idCliente;
    private int numeroIdentidad;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;

    // Getters y setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getNumeroIdentidad() { return numeroIdentidad; }
    public void setNumeroIdentidad(int numeroIdentidad) { this.numeroIdentidad = numeroIdentidad; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
