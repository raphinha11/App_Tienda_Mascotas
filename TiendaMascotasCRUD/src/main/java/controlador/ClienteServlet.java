package controlador;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import modelo.Cliente;
import modelo.ClienteDAO;
import util.EmailUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/* Es un servlet encargdo de gestionar las operaciones CRUD sobre objetos de tipo cliente,
 * manejar las solicitudes HTTP (POST y GET) relacionadas a los clientes, enviar notificaciones
 * por correo electronico tras cada operacion y generar reportes en PDF utilizando la biblioteca 
 * Apache PDFBox*/

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
	ClienteDAO dao = new ClienteDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String accion = request.getParameter("accion");

		switch (accion) {
		case "Guardar":
			// Crear y llenar objeto Cliente con datos del formulario
			Cliente c = new Cliente();
			c.setNumeroIdentidad(Integer.parseInt(request.getParameter("numero_identidad")));
			c.setNombres(request.getParameter("nombres"));
			c.setApellidos(request.getParameter("apellidos"));
			c.setDireccion(request.getParameter("direccion"));
			c.setTelefono(request.getParameter("telefono"));
			dao.agregar(c);

			// Notificacion por correo
			EmailUtil.enviarCorreo("Nuevo cliente registrado", "Se registró un nuevo cliente:\n" + "Nombre: "
					+ c.getNombres() + " " + c.getApellidos() + "\nTeléfono: " + c.getTelefono());
			break;

		case "Actualizar":
			// Crear objeto Cliente con ID y nuevos datos
			Cliente c2 = new Cliente();
			c2.setIdCliente(Integer.parseInt(request.getParameter("id_cliente")));
			c2.setNumeroIdentidad(Integer.parseInt(request.getParameter("numero_identidad")));
			c2.setNombres(request.getParameter("nombres"));
			c2.setApellidos(request.getParameter("apellidos"));
			c2.setDireccion(request.getParameter("direccion"));
			c2.setTelefono(request.getParameter("telefono"));
			dao.actualizar(c2);

			// Notificacion por correo
			EmailUtil.enviarCorreo("Cliente actualizado", "Se actualizó el cliente:\n" + "ID: " + c2.getIdCliente()
					+ "\nNombre: " + c2.getNombres() + " " + c2.getApellidos());
			break;

		case "Eliminar":
			// Obtener ID del cliente a eliminar
			int idEliminar = Integer.parseInt(request.getParameter("id_cliente"));
			Cliente eliminado = dao.buscar(idEliminar); // Buscar antes de eliminar
			dao.eliminar(idEliminar);

			// Enviar correo solo si el cliente existia
			if (eliminado != null) {
				EmailUtil.enviarCorreo("Cliente eliminado",
						"Se eliminó el cliente:\n" + "ID: " + eliminado.getIdCliente() + "\nNombre: "
								+ eliminado.getNombres() + " " + eliminado.getApellidos());
			}
			break;

		case "Consultar":
			// Buscar cliente por ID
			int idBuscar = Integer.parseInt(request.getParameter("id_cliente"));
			Cliente clienteEncontrado = dao.buscar(idBuscar);

			// Enviar datos al formulario o mostrar mensaje
			if (clienteEncontrado != null) {
				request.setAttribute("cliente", clienteEncontrado);
			} else {
				request.setAttribute("mensaje", "Cliente no encontrado");
			}

			RequestDispatcher dispatcher1 = request.getRequestDispatcher("formulario.jsp");
			dispatcher1.forward(request, response);
			return;

		case "Reporte":
			// Generar PDF con los datos de un cliente
			int idReporte = Integer.parseInt(request.getParameter("id_cliente"));
			Cliente clienteReporte = dao.buscar(idReporte);

			if (clienteReporte != null) {
				generarReporte(clienteReporte, response);
			} else {
				request.setAttribute("mensaje", "Cliente no encontrado para generar el reporte");
				RequestDispatcher dispatcher11 = request.getRequestDispatcher("formulario.jsp");
				dispatcher11.forward(request, response);
			}
			return;

		case "ReporteGeneral":
			// Generar PDF con todos los clientes
			generarReporteGeneral(response);
			return;
		}

		response.sendRedirect("ClienteServlet");
	}

	// Método que responde a solicitudes GET (listar clientes)
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    int page = 1;
	    int recordsPerPage = 10;

	    try {
	        if (request.getParameter("page") != null) {
	            page = Integer.parseInt(request.getParameter("page"));
	            if (page < 1) page = 1;
	        }
	    } catch (NumberFormatException e) {
	        page = 1;
	    }

	    List<Cliente> todosClientes = dao.listar();
	    int totalClientes = todosClientes.size();
	    int totalPages = (int) Math.ceil((double) totalClientes / recordsPerPage);

	    if (page > totalPages && totalPages > 0) {
	        page = totalPages;
	    }

	    int start = (page - 1) * recordsPerPage;
	    int end = Math.min(start + recordsPerPage, totalClientes);

	    List<Cliente> clientesPaginados = new ArrayList<>();
	    if (start < totalClientes) {
	        clientesPaginados = todosClientes.subList(start, end);
	    }

	    request.setAttribute("clientes", clientesPaginados);
	    request.setAttribute("currentPage", page);
	    request.setAttribute("totalPages", totalPages);

	    RequestDispatcher dispatcher = request.getRequestDispatcher("listar.jsp");
	    dispatcher.forward(request, response);
	}
	

	// Genera un certificado PDF para un solo cliente
	private void generarReporte(Cliente cliente, HttpServletResponse response) {
		try {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition",
					"attachment; filename=certificado_cliente_" + cliente.getIdCliente() + ".pdf");

			// Crear documento y pagina
			PDDocument document = new PDDocument();
			PDPage page = new PDPage(PDRectangle.LETTER);
			document.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			// Titulo del certificado
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
			contentStream.newLineAtOffset(100, 700);
			contentStream.showText("CERTIFICADO DE CLIENTE");
			contentStream.endText();

			// Informacion del cliente
			float y = 660;
			float leading = 20;
			float fontSize = 12;

			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA, fontSize);
			contentStream.newLineAtOffset(100, y);

			String[] lineas = { "ID Cliente: " + cliente.getIdCliente(),
					"Número de Identidad: " + cliente.getNumeroIdentidad(),
					"Nombre: " + cliente.getNombres() + " " + cliente.getApellidos(),
					"Dirección: " + cliente.getDireccion(), "Teléfono: " + cliente.getTelefono() };

			for (String linea : lineas) {
				contentStream.showText(linea);
				contentStream.newLineAtOffset(0, -leading);
			}

			contentStream.endText();
			contentStream.close();

			document.save(response.getOutputStream());
			document.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Genera un reporte general en PDF con todos los clientes
	private void generarReporteGeneral(HttpServletResponse response) {
		try {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=reporte_general_clientes.pdf");

			PDDocument document = new PDDocument();
			PDPage page = new PDPage(PDRectangle.LETTER);
			document.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			PDType1Font fontRegular = PDType1Font.HELVETICA;
			PDType1Font fontBold = PDType1Font.HELVETICA_BOLD;
			float fontSize = 9f;

			float margin = 50;
			float yStart = 730;
			float yPosition = yStart;
			float rowHeight = 15;
			float tableWidth = page.getMediaBox().getWidth() - 2 * margin;

			// Definir columnas y encabezados
			float[] colWidths = { 40, 80, 100, 100, 100, 80 }; // ID, Identidad, Nombre, Apellidos, Dirección, Teléfono
			String[] headers = { "ID", "Identidad", "Nombre", "Apellidos", "Dirección", "Teléfono" };

			// Titulo
			contentStream.beginText();
			contentStream.setFont(fontBold, 14);
			contentStream.newLineAtOffset(margin, yPosition);
			contentStream.showText("Reporte General de Clientes");
			contentStream.endText();

			yPosition -= 25;

			// Dibujar encabezados de tabla
			float textY = yPosition - 10;
			float nextX = margin;

			contentStream.setFont(fontBold, fontSize);
			for (int i = 0; i < headers.length; i++) {
				contentStream.beginText();
				contentStream.newLineAtOffset(nextX, textY);
				contentStream.showText(headers[i]);
				contentStream.endText();
				nextX += colWidths[i];
			}

			yPosition -= rowHeight;

			// Obtener datos
			List<Cliente> clientes = dao.listar();

			contentStream.setFont(fontRegular, fontSize);

			for (Cliente cliente : clientes) {
				// Crear nueva pagina si se agota el espacio
				if (yPosition < margin + rowHeight) {
					contentStream.close();
					page = new PDPage(PDRectangle.LETTER);
					document.addPage(page);
					contentStream = new PDPageContentStream(document, page);

					contentStream.setFont(fontRegular, fontSize);

					yPosition = yStart;
				}

				float xText = margin;
				textY = yPosition - 10;

				// Datos del cliente por columna
				String[] data = { String.valueOf(cliente.getIdCliente()), String.valueOf(cliente.getNumeroIdentidad()),
						cliente.getNombres(), cliente.getApellidos(), cliente.getDireccion(), cliente.getTelefono() };

				for (int i = 0; i < data.length; i++) {
					contentStream.beginText();
					contentStream.newLineAtOffset(xText, textY);
					contentStream.showText(truncateText(data[i], colWidths[i], fontRegular, fontSize));
					contentStream.endText();
					xText += colWidths[i];
				}

				yPosition -= rowHeight;
			}

			contentStream.close();
			document.save(response.getOutputStream());
			document.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Método para truncar texto si excede el ancho de columna
	private String truncateText(String text, float maxWidth, PDType1Font font, float fontSize) throws IOException {
		String result = text;
		while (font.getStringWidth(result) / 1000 * fontSize > maxWidth && result.length() > 0) {
			result = result.substring(0, result.length() - 1);
		}
		if (!result.equals(text)) {
			result += "...";
		}
		return result;
	}

	// Divide un texto largo en múltiples líneas dependiendo del ancho
	private List<String> breakLines(String text, float maxWidth, PDType1Font font, float fontSize) throws IOException {
		List<String> lines = new ArrayList<>();

		String[] words = text.split(" ");
		StringBuilder line = new StringBuilder();

		for (String word : words) {
			String temp = line.length() == 0 ? word : line + " " + word;
			float size = font.getStringWidth(temp) / 1000 * fontSize;
			if (size > maxWidth) {
				if (line.length() > 0)
					lines.add(line.toString());
				line = new StringBuilder(word);
			} else {
				line = new StringBuilder(temp);
			}
		}

		if (line.length() > 0)
			lines.add(line.toString());
		return lines;
	}

	// Devuelve la cantidad de líneas necesarias para envolver un texto
	private int wrapText(String text, float maxWidth, float fontSize) {
		try {
			return breakLines(text, maxWidth, PDType1Font.HELVETICA, fontSize).size();
		} catch (IOException e) {
			return 1;
		}
	}
}
