package interfaz.Panel;

import javax.swing.*;
import java.awt.*;

public abstract class Panel extends JPanel {

    public Panel(String NombrePanel) {
        setLayout(new BorderLayout());
    }
}
