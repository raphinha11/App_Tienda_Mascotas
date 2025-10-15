<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, modelo.Producto" %>
<%
    List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
    if (carrito == null) carrito = new ArrayList<>();

    double total = 0;
    for (Producto p : carrito) {
        total += p.getSubtotal();
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>🛒 Carrito de Compras</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body { background-color: #f9f9f9; }
        .navbar { background-color: #ff6f61; }
        .navbar-brand, .nav-link { color: white !important; }
        .btn-primary { background-color: #ff6f61; border: none; }
        .btn-primary:hover { background-color: #e35b50; }
        .table { background: white; border-radius: 10px; }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg">
    <div class="container">
        <a class="navbar-brand fw-bold" href="ControladorProducto">🐾 Tienda Mascotas</a>
        <a href="ControladorProducto" class="btn btn-light ms-auto">Volver</a>
    </div>
</nav>

<div class="container mt-4">
    <h3 class="text-center text-secondary mb-4">🛒 Carrito de Compras</h3>

    <%
        String mensaje = (String) request.getAttribute("mensaje");
        if (mensaje != null) {
    %>
        <div class="alert alert-success text-center"><%= mensaje %></div>
    <%
        }
    %>

    <%
        if (carrito.isEmpty()) {
    %>
        <div class="alert alert-warning text-center">Tu carrito está vacío 😿</div>
    <%
        } else {
    %>
        <table class="table table-striped text-center">
            <thead class="table-danger">
                <tr>
                    <th>Código</th>
                    <th>Producto</th>
                    <th>Marca</th>
                    <th>Cantidad</th>
                    <th>Precio</th>
                    <th>Subtotal</th>
                    <th>Acción</th>
                </tr>
            </thead>
            <tbody>
                <% for (Producto p : carrito) { %>
                <tr>
                    <td><%= p.getCodigo_barras() %></td>
                    <td><%= p.getNombre_producto() %></td>
                    <td><%= p.getMarca() %></td>
                    <td><%= p.getCantidad() %></td>
                    <td>$<%= String.format("%.2f", p.getPrecio()) %></td>
                    <td>$<%= String.format("%.2f", p.getSubtotal()) %></td>
                    <td>
                        <form action="ControladorCarrito" method="POST" style="display:inline;">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="codigo_barras" value="<%= p.getCodigo_barras() %>">
                            <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <div class="text-end mb-4">
            <h4>Total: $<%= String.format("%.2f", total) %></h4>
        </div>

        <div class="d-flex justify-content-between">
            <form action="ControladorCarrito" method="POST">
                <input type="hidden" name="accion" value="vaciar">
                <button type="submit" class="btn btn-outline-danger">Vaciar carrito</button>
            </form>

             <form action="ControladorCarrito" method="post">
    <input type="hidden" name="accion" value="comprar">

    <label for="cedula_cliente">Ingrese su cédula:</label>
    <input type="number" name="cedula_cliente" id="cedula_cliente" required class="form-control" style="width:250px;">

    <button type="submit" class="btn btn-success mt-2">Comprar 🧾</button>
    </form>
        </div>
        
    <%
        }
    %>
</div>

</body>
</html>
