<%@page import="modelo.Mascota"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%
    List<Mascota> mascotas = (List<Mascota>) request.getAttribute("mascotas");
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Mascotas - Tienda de Mascotas</title>

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
    .table thead {
        background-color: #0d6efd;
        color: white;
    }
    .form-section {
        background: white;
        border-radius: 10px;
        padding: 20px;
        margin-top: 40px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
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

    <!-- Título -->
    <div class="container">
        <h2 class="text-center text-primary fw-bold mb-4">🐕‍🦺 Mascotas Registradas</h2>

        <!-- Botones de acción -->
        <div class="text-center mb-4">
            <a href="formulario2.jsp" class="btn btn-success me-2">➕ Agregar Mascota</a>
            <a href="verificarConexion.jsp" class="btn btn-secondary">🛜 Verificar Conexión</a>
        </div>

        <!-- Tabla -->
        <div class="table-responsive shadow-sm">
            <table class="table table-striped table-bordered align-middle text-center">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Tipo</th>
                        <th>Género</th>
                        <th>Raza</th>
                        <th>Cédula Cliente</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (mascotas != null && !mascotas.isEmpty()) {
                            for (Mascota m : mascotas) {
                    %>
                    <tr>
                        <td><%= m.getId_mascota() %></td>
                        <td><%= m.getNombre() %></td>
                        <td><%= m.getTipo() %></td>
                        <td><%= m.getGenero() %></td>
                        <td><%= m.getRaza() %></td>
                        <td><%= m.getCedula_cliente() %></td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="6" class="text-muted">No hay mascotas registradas.</td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>

        <!-- Paginación -->
        <% if (totalPages != null && totalPages > 1) { %>
        <nav aria-label="Paginación">
            <ul class="pagination justify-content-center">
                <li class="page-item <%= (currentPage <= 1) ? "disabled" : "" %>">
                    <a class="page-link" href="MascotaServlet?page=<%= currentPage - 1 %>">Anterior</a>
                </li>
                <% for (int i = 1; i <= totalPages; i++) { %>
                    <li class="page-item <%= (i == currentPage) ? "active" : "" %>">
                        <a class="page-link" href="MascotaServlet?page=<%= i %>"><%= i %></a>
                    </li>
                <% } %>
                <li class="page-item <%= (currentPage >= totalPages) ? "disabled" : "" %>">
                    <a class="page-link" href="MascotaServlet?page=<%= currentPage + 1 %>">Siguiente</a>
                </li>
            </ul>
        </nav>
        <% } %>

    </div>

    <!-- Footer -->
    <footer class="bg-dark text-white text-center py-3 mt-5">
        <p class="mb-0">&copy; 2025 Tienda de Mascotas | Módulo de Mascotas</p>
    </footer>

    <!-- JS -->
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script>
    document.querySelector('input[name="archivo"]').addEventListener('change', function () {
        if (this.files.length > 0) {
            alert("Archivo cargado: " + this.files[0].name);
        }
    });
    </script>
</body>
</html>
