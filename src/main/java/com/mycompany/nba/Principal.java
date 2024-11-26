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
        int tirosLibresRealizados = (Integer) contadorTirosLibresRealizados.getValue();
        int tirosLibresMetidos = (Integer) contadorTirosLibresMetidos.getValue();
        int tirosDoblesRealizados = (Integer) contadorTirosDoblesRealizados.getValue();
        int tirosDoblesMetidos = (Integer) contadorTirosDoblesMetidos.getValue();
        int tirosTriplesRealizados = (Integer) contadorTriplesRealizados.getValue();
        int tirosTriplesMetidos = (Integer) contadorTriplesMetidos.getValue();
        
        
        if (tirosDoblesRealizados + tirosTriplesRealizados <= 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "El total de tiros dobles y triples no puede ser 0");
            return; 
        }
        
        //Restricción para que los tiros encestados no sean mayor que los realizados
        if (tirosLibresMetidos > tirosLibresRealizados || tirosDoblesMetidos > tirosDoblesRealizados || tirosTriplesMetidos > tirosTriplesRealizados) {
            javax.swing.JOptionPane.showMessageDialog(this, "Los tiros metidos no pueden ser mas que los realizados.");
            return;
        }
        
        //Calculo de las estadisticas FG, eFG Y TS
        double FG = (double) (tirosDoblesMetidos + tirosTriplesMetidos) / (tirosDoblesRealizados + tirosTriplesRealizados) * 100;
        double EFG = (double) (tirosDoblesMetidos + 1.5 * tirosTriplesMetidos) / (tirosDoblesRealizados + tirosTriplesRealizados) * 100;
        
        //Calcular los tiros totales realizados
        int puntosTotales = tirosLibresMetidos + (2 * tirosDoblesMetidos) + (3 * tirosTriplesMetidos);
        double TS = (double) puntosTotales / (2 * (tirosDoblesRealizados + tirosTriplesRealizados + 0.44 * tirosLibresRealizados)) * 100;
        
        //Panel emergente con los datos de FG,eFG y TS
        javax.swing.JOptionPane.showMessageDialog(this,
                """
                Resultados: 
                FG: """ + String.format("%.2f", FG) + "%\n" +
                        "EFG: " + String.format("%.2f", EFG) + "%" +
                        "TS: " + String.format("%.2f", TS) + "%");

        try {
            crearArchivoExcel("D:\\GSDAM 2º\\Desarrollo de interfaces (DI)\\NBA\\Resultados_NBA.xlsx", nombre, tirosLibresRealizados, tirosLibresMetidos, tirosDoblesRealizados,tirosDoblesMetidos,tirosTriplesRealizados,tirosTriplesMetidos, FG, EFG, TS);
            javax.swing.JOptionPane.showMessageDialog(this, "Archivo actualizado");
        } catch (IOException e) {
        }
    }

private void crearArchivoExcel(String archivos, String jugador, int tirosLibresRealizados, int tirosLibresMetidos, int tirosDoblesRealizados,int tirosDoblesMetidos,int tirosTriplesRealizados,int tirosTriplesMetidos, double FG, double EFG,double TS)throws IOException {
    
        Workbook excel ;
        Sheet pagina ;
        
        //Comprobar si el excel existe para registrar datos nuevos sin sobreescribirlo
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
        
        
        // Eliminar la fila de media para escribirla con los nuevos partidos
        for (int i = 1; i <= pagina.getLastRowNum(); i++) { 
            Row filaActual = pagina.getRow(i);
            if (filaActual != null) {
                Cell primeraCelda = filaActual.getCell(0);
                if (primeraCelda != null && primeraCelda.getCellType() == CellType.STRING) {
                    if ("Media:".equals(primeraCelda.getStringCellValue())) {
                        pagina.removeRow(filaActual);
                    }
                }
            }
}
        
        
        
        // Busca la proxima fila disponible
        int ultimaFila = pagina.getLastRowNum(); 
        Row fila;
        
        
        //Creamos los encabezados de las columnas
        Row encabezado = pagina.getRow(0);
        if (encabezado == null){
        fila = pagina.createRow(0);
        fila.createCell(0).setCellValue("Nombre del jugador");
        fila.createCell(1).setCellValue("Tiros libres realizados");
        fila.createCell(2).setCellValue("Tiros libres metidos");
        fila.createCell(3).setCellValue("Dobles realizados");
        fila.createCell(4).setCellValue("Dobles metidos");
        fila.createCell(5).setCellValue("Triples realizados");
        fila.createCell(6).setCellValue("Triples metidos");
        fila.createCell(7).setCellValue("FG%");
        fila.createCell(8).setCellValue("eFG%");
        fila.createCell(9).setCellValue("TS%");
        ultimaFila ++ ; 
        }
        
        //Filas con los datos recogidos
        fila = pagina.createRow(ultimaFila + 1);
        fila.createCell(0).setCellValue(jugador);
        fila.createCell(1).setCellValue(tirosLibresRealizados);
        fila.createCell(2).setCellValue(tirosLibresMetidos);
        fila.createCell(3).setCellValue(tirosDoblesRealizados);
        fila.createCell(4).setCellValue(tirosDoblesMetidos);
        fila.createCell(5).setCellValue(tirosTriplesRealizados);
        fila.createCell(6).setCellValue(tirosTriplesMetidos);
        fila.createCell(7).setCellValue(FG);
        fila.createCell(8).setCellValue(EFG);
        fila.createCell(9).setCellValue(TS);
        
        
        //Esctibir la media en la ultima fila del excel
        Row filaMedias = pagina.createRow(ultimaFila + 2);
        filaMedias.createCell(0).setCellValue("Media:");
        
        for(int columnas = 1;columnas <= 9; columnas++){
            double suma = 0.0;
            int filasDatos = 0;
            
            for (int i = 1; i <= ultimaFila; i++) { 
                Row filaActual = pagina.getRow(i);
                if (filaActual != null) {
                     Cell celda = filaActual.getCell(columnas); 
                    if (celda != null && celda.getCellType() == CellType.NUMERIC) {
                        suma += celda.getNumericCellValue();
                        filasDatos++;
                    }
                }
            }
            double media;
            
            //Respetar que valores tiene que escribir
            if (filasDatos > 0){
                media = suma / filasDatos;
            }else{
                media = 0.0;
            }
            
            Cell celdaMedia = filaMedias.createCell(columnas);
            celdaMedia.setCellValue(media);
            
        }
        
        //Ajusta el tamaño de las celdas
        for (int i = 0; i <= 9; i++){
            pagina.autoSizeColumn(i);
        }
        
        //Escribir el documento excel
        try (FileOutputStream archivo = new FileOutputStream(archivos)) {
            excel.write(archivo);
        }

} 
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nombre = new javax.swing.JLabel();
        textoNombre = new javax.swing.JTextField();
        tirosRealizados = new javax.swing.JLabel();
        contadorTirosLibresRealizados = new javax.swing.JSpinner();
        tirosLibresMetidos = new javax.swing.JLabel();
        contadorTirosLibresMetidos = new javax.swing.JSpinner();
        TirosDoblesRealizados = new javax.swing.JLabel();
        contadorTirosDoblesRealizados = new javax.swing.JSpinner();
        tirosDoblesMetidos = new javax.swing.JLabel();
        contadorTirosDoblesMetidos = new javax.swing.JSpinner();
        tirosTriplesRealizados = new javax.swing.JLabel();
        contadorTriplesRealizados = new javax.swing.JSpinner();
        tirosTriplesMetidos = new javax.swing.JLabel();
        contadorTriplesMetidos = new javax.swing.JSpinner();
        botonCalcular = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(java.awt.Color.white);

        nombre.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        nombre.setText("Nombre del jugador");

        tirosRealizados.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosRealizados.setText("Tiros libres realizados");

        contadorTirosLibresRealizados.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        tirosLibresMetidos.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosLibresMetidos.setText("Tiros libres metidos");

        contadorTirosLibresMetidos.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        TirosDoblesRealizados.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        TirosDoblesRealizados.setText("Tiros de 2 realizados");

        contadorTirosDoblesRealizados.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        tirosDoblesMetidos.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosDoblesMetidos.setText("Tiros metidos de 2");

        contadorTirosDoblesMetidos.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        tirosTriplesRealizados.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosTriplesRealizados.setText("Tiros de 3 realizados");

        contadorTriplesRealizados.setModel(new javax.swing.SpinnerNumberModel());

        tirosTriplesMetidos.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosTriplesMetidos.setText("Tiros metidos de 3");

        contadorTriplesMetidos.setModel(new javax.swing.SpinnerNumberModel());

        botonCalcular.setText("Calcular");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoNombre))
                    .addComponent(tirosTriplesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TirosDoblesRealizados, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tirosDoblesMetidos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tirosTriplesRealizados))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(contadorTirosDoblesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(contadorTriplesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(contadorTirosDoblesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(contadorTriplesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botonCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tirosRealizados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tirosLibresMetidos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(contadorTirosLibresMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(contadorTirosLibresRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombre)
                    .addComponent(textoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contadorTirosLibresRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tirosRealizados, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tirosLibresMetidos)
                    .addComponent(contadorTirosLibresMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TirosDoblesRealizados)
                    .addComponent(contadorTirosDoblesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contadorTirosDoblesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tirosDoblesMetidos, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contadorTriplesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tirosTriplesRealizados, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tirosTriplesMetidos)
                    .addComponent(contadorTriplesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(botonCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Principal().setVisible(true);
        });
    }
    
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TirosDoblesRealizados;
    private javax.swing.JButton botonCalcular;
    private javax.swing.JSpinner contadorTirosDoblesMetidos;
    private javax.swing.JSpinner contadorTirosDoblesRealizados;
    private javax.swing.JSpinner contadorTirosLibresMetidos;
    private javax.swing.JSpinner contadorTirosLibresRealizados;
    private javax.swing.JSpinner contadorTriplesMetidos;
    private javax.swing.JSpinner contadorTriplesRealizados;
    private javax.swing.JLabel nombre;
    private javax.swing.JTextField textoNombre;
    private javax.swing.JLabel tirosDoblesMetidos;
    private javax.swing.JLabel tirosLibresMetidos;
    private javax.swing.JLabel tirosRealizados;
    private javax.swing.JLabel tirosTriplesMetidos;
    private javax.swing.JLabel tirosTriplesRealizados;
    // End of variables declaration//GEN-END:variables
}
