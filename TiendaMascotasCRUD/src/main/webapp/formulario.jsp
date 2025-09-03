<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
    modelo.Cliente cliente = (modelo.Cliente) request.getAttribute("cliente");
    if (cliente == null) {
        cliente = new modelo.Cliente();
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Formulario Cliente</title>

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
</head>
<body class="bg-light">

	<% String mensaje = (String) request.getAttribute("mensaje"); %>
	<% if (mensaje != null) { %>
	<div class="alert alert-danger text-center" role="alert">
		<%= mensaje %>
	</div>
	<% } %>

	<div class="container mt-5">
		<h2 class="text-center mb-4">Formulario de Cliente</h2>
		<form action="ClienteServlet" method="post"
			class="border p-4 bg-white rounded shadow-sm">

			<div class="mb-3">
				<label for="id_cliente" class="form-label">ID Cliente</label> 
				<input
					type="number" class="form-control" id="id_cliente"
					name="id_cliente" value="<%= cliente.getIdCliente() %>">
			</div>

			<div class="mb-3">
				<label for="numero_identidad" class="form-label">Número de
					Identidad</label> 
				<input type="number" class="form-control"
					id="numero_identidad" name="numero_identidad"
					value="<%= cliente.getNumeroIdentidad() %>" required>
			</div>

			<div class="mb-3">
				<label for="nombres" class="form-label">Nombres</label> 
				<input
					type="text" class="form-control" id="nombres" name="nombres"
					value="<%= cliente.getNombres() != null ? cliente.getNombres() : "" %>">
			</div>

			<div class="mb-3">
				<label for="apellidos" class="form-label">Apellidos</label> 
				<input
					type="text" class="form-control" id="apellidos" name="apellidos"
					value="<%= cliente.getApellidos() != null ? cliente.getApellidos() : "" %>">
			</div>

			<div class="mb-3">
				<label for="direccion" class="form-label">Dirección</label> 
				<input
					type="text" class="form-control" id="direccion" name="direccion"
					value="<%= cliente.getDireccion() != null ? cliente.getDireccion() : "" %>">
			</div>

			<div class="mb-3">
				<label for="telefono" class="form-label">Teléfono</label> 
				<input
					type="text" class="form-control" id="telefono" name="telefono"
					value="<%= cliente.getTelefono() != null ? cliente.getTelefono() : "" %>">
			</div>

			<div class="d-flex flex-wrap gap-2">
				<button type="submit" name="accion" value="Guardar"
					class="btn btn-primary">🗃️ Guardar</button>
				<button type="submit" name="accion" value="Actualizar"
					class="btn btn-warning">🔁 Actualizar</button>
				<button type="submit" name="accion" value="Eliminar"
					class="btn btn-danger">✖️ Eliminar</button>
				<button type="submit" name="accion" value="Consultar"
					class="btn btn-info">🔎 Consultar</button>
			    <button type="submit" name="accion" value="Reporte"
	                class="btn btn-success">📄 Generar Certificado</button>
	            <button type="submit" name="accion" value="ReporteGeneral"
	                class="btn btn-success">🗳️ Reporte General</button>			
			</div>

			<div class="mt-3">
				<a href="ClienteServlet" class="btn btn-outline-dark w-100">Volver
					a la lista</a>
					
			</div>
		</form>
		
	</div>


	<!-- Bootstrap JS -->
	<script
		src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>
