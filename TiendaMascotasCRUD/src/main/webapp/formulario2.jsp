<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
    modelo.Mascota mascota = (modelo.Mascota) request.getAttribute("mascota");
    if (mascota == null) {
        mascota = new modelo.Mascota();
    }
    String mensaje = (String) request.getAttribute("mensaje");
    String tipoMensaje = (String) request.getAttribute("tipoMensaje"); // "success" o "danger"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Formulario Mascota</title>

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
            <div class="card-header bg-success text-white text-center fw-bold">
                🐕 Formulario de Mascota
            </div>
            <div class="card-body">
                <form action="MascotaServlet" method="post">

                    <div class="mb-3">
                        <label for="id_mascota" class="form-label">ID Mascota</label>
                        <input type="number" class="form-control" id="id_mascota"
                            name="id_mascota" value="<%= mascota.getId_mascota() %>">
                    </div>

                    <div class="mb-3">
                        <label for="nombre" class="form-label">Nombre</label>
                        <input type="text" class="form-control" id="nombre"
                            name="nombre" value="<%= mascota.getNombre() != null ? mascota.getNombre() : "" %>">
                    </div>

                    <div class="mb-3">
                        <label for="tipo" class="form-label">Tipo</label>
                        <select class="form-select" id="tipo" name="tipo">
                            <option value="">-- Seleccione --</option>
                            <option value="Perro" <%= "Perro".equals(mascota.getTipo()) ? "selected" : "" %>>Perro</option>
                            <option value="Gato" <%= "Gato".equals(mascota.getTipo()) ? "selected" : "" %>>Gato</option>
                            <option value="Ave" <%= "Ave".equals(mascota.getTipo()) ? "selected" : "" %>>Ave</option>
                            <option value="Otro" <%= "Otro".equals(mascota.getTipo()) ? "selected" : "" %>>Otro</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="genero" class="form-label">Género</label>
                        <select class="form-select" id="genero" name="genero">
                            <option value="">-- Seleccione --</option>
                            <option value="Macho" <%= "Macho".equals(mascota.getGenero()) ? "selected" : "" %>>Macho</option>
                            <option value="Hembra" <%= "Hembra".equals(mascota.getGenero()) ? "selected" : "" %>>Hembra</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="raza" class="form-label">Raza</label>
                        <input type="text" class="form-control" id="raza"
                            name="raza" value="<%= mascota.getRaza() != null ? mascota.getRaza() : "" %>">
                    </div>

                    <div class="mb-3">
                        <label for="cedula_cliente" class="form-label">Cédula Cliente</label>
                        <input type="number" class="form-control" id="cedula_cliente"
                            name="cedula_cliente" value="<%= mascota.getCedula_cliente() %>">
                    </div>

                    <div class="d-flex flex-wrap gap-2 mb-3">
                        <button type="submit" name="accion" value="Guardar" class="btn btn-primary flex-fill">🗃️ Guardar</button>
                        <button type="submit" name="accion" value="Actualizar" class="btn btn-warning flex-fill">🔁 Actualizar</button>
                        <button type="submit" name="accion" value="Eliminar" class="btn btn-danger flex-fill">✖️ Eliminar</button>
                        <button type="submit" name="accion" value="Consultar" class="btn btn-info flex-fill">🔎 Consultar</button>
                        <button type="submit" name="accion" value="ReporteGeneral" class="btn btn-primary flex-fill">📄 Generar Reporte</button>
                        <button type="submit" name="accion" value="Reporte" class="btn btn-info flex-fill">🗳️ Certificado</button>
                    </div>

                    <div class="mt-3">
                        <a href="MascotaServlet" class="btn btn-outline-dark w-100">⬅️ Volver a la lista</a><br><br>
                        <a href="${pageContext.request.contextPath}/InicioServlet" class="btn btn-outline-dark w-100">🏠 Inicio</a>

                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-white text-center py-3 mt-5">
        <p class="mb-0">&copy; 2025 Tienda de Mascotas | Módulo de Mascotas</p>
    </footer>

    <!-- Bootstrap JS -->
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>
