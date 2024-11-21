package com.mycompany.nba;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Anatoliy
 */
public class Principal extends javax.swing.JFrame {

    public Principal() {
        initComponents();
        this.setLocationRelativeTo(null);
        botonCalcular.addActionListener(evt -> generarExcel()); 
    }
 private void generarExcel() {
        String nombre = textoNombre.getText();
        int tirosRealizados = (Integer) contadorTirosRealizados.getValue();
        int tirosDobles = (Integer) contadorDobles.getValue();
        int tirosTriples = (Integer) contadorTriples.getValue();

        if (tirosRealizados <= 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Los tiros realizados deben ser mayores a 0");
            return; // Termina el método
        }

        if (tirosDobles + tirosTriples > tirosRealizados) {
            javax.swing.JOptionPane.showMessageDialog(this, "La suma de dobles y triples no puede ser mayor a los tiros realizados.");
            return;
        }

        double FG = (double) (tirosDobles + tirosTriples) / tirosRealizados * 100;
        double EFG = (double) (tirosDobles + 1.5 * tirosTriples) / tirosRealizados * 100;

        javax.swing.JOptionPane.showMessageDialog(this,
                """
                Resultados: 
                FG: """ + String.format("%.2f", FG) + "%\n" +
                        "EFG: " + String.format("%.2f", EFG) + "%");

        try {
            crearArchivoExcel("D:\\GSDAM 2º\\Desarrollo de interfaces (DI)\\NBA\\Resultados_NBA.xlsx", nombre, tirosRealizados, tirosDobles, tirosTriples, FG, EFG);
            javax.swing.JOptionPane.showMessageDialog(this, "Archivo actualizado");
        } catch (IOException e) {
        }
    }

private void crearArchivoExcel(String archivos, String jugador, int tirosRealizados, int tirosDobles, int tirosTriples, double FG, double EFG)throws IOException {
    
        Workbook excel ;
        Sheet pagina ;
        
        java.io.File archivoExcel = new java.io.File(archivos);
        if(archivoExcel.exists()){
            try (FileInputStream file = new FileInputStream(archivoExcel)){
                excel = new XSSFWorkbook(file);
                pagina = excel.getSheet("Estadisticas"); // Si existe carga la pagina de estadisticas creada
                if(pagina == null) { 
                    pagina = excel.createSheet("Estadisticas"); // Si es nulo crea una nueva pagina de excel
                }
            }
        }else{
            excel = new  XSSFWorkbook(); // Si no existe el documento excel lo crea
            pagina = excel.createSheet("Estadisticas"); // Crea una pagina nueva de estadisticas
        }
        
        
        int revisarFilas = pagina.getLastRowNum(); // Busca la proxima fila disponible
       
    
        Row fila;
        
        Row encabezado = pagina.getRow(0);
        if (encabezado == null){
        
        fila = pagina.createRow(0);
        fila.createCell(0).setCellValue("Nombre del jugador");
        fila.createCell(1).setCellValue("Tiros realizados");
        fila.createCell(2).setCellValue("Dobles encestados");
        fila.createCell(3).setCellValue("Triples encestados");
        fila.createCell(4).setCellValue("Calculo FG%");
        fila.createCell(5).setCellValue("Calculo eFG%");
        revisarFilas ++ ; 
        }
        
        fila = pagina.createRow(revisarFilas + 1);
        fila.createCell(0).setCellValue(jugador);
        fila.createCell(1).setCellValue(tirosRealizados);
        fila.createCell(2).setCellValue(tirosDobles);
        fila.createCell(3).setCellValue(tirosTriples);
        fila.createCell(4).setCellValue(FG);
        fila.createCell(5).setCellValue(EFG);
        
        for (int i = 0; i <= 5; i++){
            pagina.autoSizeColumn(i);
        }
        
        try (FileOutputStream archivo = new FileOutputStream(archivos)) {
            excel.write(archivo);
        }

} 
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nombre = new javax.swing.JLabel();
        textoNombre = new javax.swing.JTextField();
        tirosRealizados = new javax.swing.JLabel();
        contadorTirosRealizados = new javax.swing.JSpinner();
        dobles = new javax.swing.JLabel();
        contadorDobles = new javax.swing.JSpinner();
        triples = new javax.swing.JLabel();
        contadorTriples = new javax.swing.JSpinner();
        botonCalcular = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(java.awt.Color.white);

        nombre.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        nombre.setText("Nombre del jugador");

        tirosRealizados.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosRealizados.setText("Tiros realizados");

        contadorTirosRealizados.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        dobles.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        dobles.setText("Tiros metidos de 2");

        contadorDobles.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        triples.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        triples.setText("Tiros metidos de 3");

        contadorTriples.setModel(new javax.swing.SpinnerNumberModel());

        botonCalcular.setText("Calcular");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dobles, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(triples, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tirosRealizados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(contadorDobles, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                        .addComponent(contadorTriples)
                        .addComponent(contadorTirosRealizados))
                    .addComponent(textoNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                .addGap(29, 29, 29))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(159, 159, 159))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(contadorTirosRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nombre)
                            .addComponent(textoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(tirosRealizados)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dobles)
                    .addComponent(contadorDobles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(triples)
                    .addComponent(contadorTriples, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addComponent(botonCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Principal().setVisible(true);
        });
    }
    
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCalcular;
    private javax.swing.JSpinner contadorDobles;
    private javax.swing.JSpinner contadorTirosRealizados;
    private javax.swing.JSpinner contadorTriples;
    private javax.swing.JLabel dobles;
    private javax.swing.JLabel nombre;
    private javax.swing.JTextField textoNombre;
    private javax.swing.JLabel tirosRealizados;
    private javax.swing.JLabel triples;
    // End of variables declaration//GEN-END:variables
}
