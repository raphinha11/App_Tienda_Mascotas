<%@ page import="java.util.List"%>
<%@ page import="modelo.Cliente"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Lista de Clientes</title>

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
</head>
<body class="bg-light">

	<div class="container mt-5">
		<h2 class="text-center text-primary mb-4">Clientes Registrados</h2>

		<div class="table-responsive">
			<table class="table table-striped table-bordered align-middle">
				<thead class="table-primary text-center">
					<tr>
						<th>ID</th>
						<th>Número Identidad</th>
						<th>Nombres</th>
						<th>Apellidos</th>
						<th>Dirección</th>
						<th>Teléfono</th>
					</tr>
				</thead>
				<tbody>
					<%
                if (clientes != null) {
                    for (Cliente c : clientes) {
            %>
					<tr class="text-center">
						<td><%= c.getIdCliente() %></td>
						<td><%= c.getNumeroIdentidad() %></td>
						<td><%= c.getNombres() %></td>
						<td><%= c.getApellidos() %></td>
						<td><%= c.getDireccion() %></td>
						<td><%= c.getTelefono() %></td>
					</tr>
					<%
                    }
                }
            %>
				</tbody>
			</table>
		</div>

		<div class="text-center mt-4">
			<a href="formulario.jsp" class="btn btn-success">Agregar Nuevo
				Cliente</a>
		</div>
	</div>

	<!-- Bootstrap JS -->
	<script
		src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>
