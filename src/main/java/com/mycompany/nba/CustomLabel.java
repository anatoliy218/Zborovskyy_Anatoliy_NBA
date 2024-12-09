
package com.mycompany.nba;


import javax.swing.*;
import java.awt.*;

public class CustomLabel extends JLabel {

    // Constructor sin parámetros
    public CustomLabel() {
        super("Texto predeterminado"); // Texto predeterminado
        configurarEstilo();
    }

    // Constructor con texto personalizado
    public CustomLabel(String texto) {
        super(texto);
        configurarEstilo();
    }

    private void configurarEstilo() {
        // Fondo degradado personalizado
        setForeground(new Color(255, 255, 255)); // Texto blanco
        setBackground(new Color(85, 130, 243)); // Azul suave
        setOpaque(true); // Hacer visible el fondo

        // Centrando y aplicando fuente personalizada
        setHorizontalAlignment(SwingConstants.CENTER); // Centrar texto
        setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 18)); // Fuente elegante
        setBorder(BorderFactory.createLineBorder(new Color(60, 90, 200), 3)); // Borde colorido
    }
    
    // Método para cambiar el tamaño de la fuente
    public void changeSize(int size) {
        switch (size) {
            case 1 -> this.setFont(new Font("Times New Roman", Font.PLAIN, 12)); // Tamaño pequeño
            case 2 -> this.setFont(new Font("Times New Roman", Font.PLAIN, 18)); // Tamaño mediano
            case 3 -> this.setFont(new Font("Times New Roman", Font.PLAIN, 22)); // Tamaño grande
            default -> this.setFont(new Font("Times New Roman", Font.PLAIN, 18)); // Por defecto mediano
        }
    }
}
