package interfaz.Panel.subPaneles;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;

class linkArchivo extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
        );

        if (value instanceof String ruta) {
            File f = new File(ruta);
            if (f.exists()) {
                setText("<html><u>" + ruta + "</u></html>");
                setForeground(Color.BLUE);
            } else {
                setText(ruta);
                setForeground(Color.BLACK);
            }
        }

        return this;
    }
}
