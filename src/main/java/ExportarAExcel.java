import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportarAExcel {

    public void exportar(JTable tabla, String textoExtra) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como");
        fileChooser.setSelectedFile(new File("reporte.xlsx"));

        int seleccion = fileChooser.showSaveDialog(null);
        if (seleccion != JFileChooser.APPROVE_OPTION) {
            System.out.println("Exportación cancelada.");
            return;
        }

        File archivo = fileChooser.getSelectedFile();
        if (!archivo.getName().toLowerCase().endsWith(".xlsx")) {
            archivo = new File(archivo.getAbsolutePath() + ".xlsx");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Datos");

            // Escribir el texto adicional primero (en la primera fila)
            Row filaTexto = sheet.createRow(0);
            Cell celdaTexto = filaTexto.createCell(0);
            celdaTexto.setCellValue(textoExtra);

            // Escribir los encabezados de la tabla (una fila después del texto)
            TableModel model = tabla.getModel();
            Row header = sheet.createRow(2);
            for (int i = 0; i < model.getColumnCount(); i++) {
                Cell celda = header.createCell(i);
                celda.setCellValue(model.getColumnName(i));
            }

            // Escribir las filas de datos (comenzando después del header)
            for (int i = 0; i < model.getRowCount(); i++) {
                Row fila = sheet.createRow(i + 3);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Cell celda = fila.createCell(j);
                    Object valor = model.getValueAt(i, j);
                    celda.setCellValue(valor != null ? valor.toString() : "");
                }
            }

            // Autosize de columnas
            for (int i = 0; i < model.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(archivo)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente en:\n" + archivo.getAbsolutePath());
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



