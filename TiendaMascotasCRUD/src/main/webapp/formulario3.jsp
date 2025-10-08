<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
    modelo.Producto producto = (modelo.Producto) request.getAttribute("producto");
    if (producto == null) {
    	producto = new modelo.Producto();
    }
    String mensaje = (String) request.getAttribute("mensaje");
    String tipoMensaje = (String) request.getAttribute("tipoMensaje"); // "success" o "danger"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Formulario Producto</title>

<!-- Bootstrap CSS -->
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">

<style>
    body {
        background-color: #f8f9fa;
    }
    .navbar {
        margin-bottom: 30px;
    }
    .card {
        max-width: 700px;
        margin: auto;
    }
</style>
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="InicioServlet">🐾 Tienda de Mascotas</a>
            <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
            <li class="nav-item"><a class="nav-link text-danger" href="logout.jsp">Cerrar sesión</a></li>
             </ul>
            </div>
        </div>
    </nav>

    <!-- Mensajes -->
    <div class="container">
        <% if (mensaje != null) { %>
            <div class="alert alert-<%= (tipoMensaje != null) ? tipoMensaje : "danger" %> text-center" role="alert">
                <%= mensaje %>
            </div>
        <% } %>
    </div>

    <!-- Formulario -->
    <div class="container">
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white text-center fw-bold">
                📋 Formulario de Productos
            </div>
            <div class="card-body">
                <form action="ProductoServlet" method="post">

                    <div class="mb-3">
                        <label for="codigo_barras" class="form-label">Codigo de Barras</label>
                        <input type="number" class="form-control" id="codigo_barras"
                            name="codigo_barras" value="<%= producto.getCodigo_barras() %>">
                    </div>

                    <div class="mb-3">
                        <label for="nombre_producto" class="form-label">Nombre de Producto</label>
                        <input type="text" class="form-control" id="nombre_producto"
                            name="nombre_producto" value="<%= producto.getNombre_producto() != null ? producto.getNombre_producto() : "" %>">
                    </div>

                    <div class="mb-3">
                        <label for="marca" class="form-label">Marca</label>
                        <input type="text" class="form-control" id="marca"
                            name="marca"
                            value="<%= producto.getMarca() != null ? producto.getMarca() : "" %>">
                    </div>

                    <div class="mb-3">
                        <label for="precio" class="form-label">Precio</label>
                        <input type="text" class="form-control" id="precio"
                            name="precio"
                            value="<%= producto.getPrecio() %>">
                    </div>


                    <div class="d-flex flex-wrap gap-2 mb-3">
                        <button type="submit" name="accion" value="Guardar" class="btn btn-primary flex-fill">🗃️ Guardar</button>
                        <button type="submit" name="accion" value="Actualizar" class="btn btn-warning flex-fill">🔁 Actualizar</button>
                        <button type="submit" name="accion" value="Eliminar" class="btn btn-danger flex-fill">✖️ Eliminar</button>
                        <button type="submit" name="accion" value="Consultar" class="btn btn-info flex-fill">🔎 Consultar</button>
                    </div>

                    <div class="d-flex flex-wrap gap-2 mb-3">
                        <button type="submit" name="accion" value="ReporteGeneral" class="btn btn-success flex-fill">🗳️ Reporte General</button>
                    </div>

                    <div class="mt-3">
                        <a href="ProductoServlet" class="btn btn-outline-dark w-100">⬅️ Volver a la lista</a><br><br>
                         <a href="${pageContext.request.contextPath}/InicioServlet" class="btn btn-outline-dark w-100">🏠 Inicio</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-white text-center py-3 mt-5">
        <p class="mb-0">&copy; 2025 Tienda de Mascotas | Módulo de Productos</p>
    </footer>

    <!-- Bootstrap JS -->
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>
