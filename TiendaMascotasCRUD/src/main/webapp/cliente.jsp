<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    modelo.Usuario usuario = (modelo.Usuario) session.getAttribute("usuario");
    if (usuario == null || !"cliente".equals(usuario.getRol())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Bienvenido Cliente</title>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light px-3">
        <a class="navbar-brand" href="#">🐾 Tienda de Mascotas</a>
        <div class="ms-auto">
            <a href="logout.jsp" class="text-danger">Cerrar sesión</a>
        </div>
    </nav>

    <div class="container mt-5 text-center">
        <h1>Bienvenido, <%= usuario.getUsuario() %> 🐶</h1>
        <p>Puedes explorar y comprar productos o mascotas.</p>

        <div class="mt-4">
            <a href="producto.jsp" class="btn btn-primary me-2">Ver Productos</a>
            <a href="mascotas.jsp" class="btn btn-success">Ver Mascotas</a>
        </div>
    </div>
</body>
</html>
