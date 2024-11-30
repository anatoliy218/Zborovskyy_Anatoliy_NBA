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
        //Primera ventana con los valores de la version 1.5
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
        
        
        //Version 2.0 nuevos datos
        int puntos = (Integer)contadorPuntos.getValue();
        int rebotes = (Integer)contadorRebotes.getValue();
        int asistencias = (Integer)contadorAsistencias.getValue();
        int robos = (Integer)contadorRobos.getValue();
        int perdidas = (Integer)contadorPerdidas.getValue();
        int taponesRealizados = (Integer)contadorTaponesRealizados.getValue();
        int taponesRecibidos = (Integer)contadorTaponesRecibidos.getValue();
        int faltasRealizadas = (Integer)contadorFaltasRealizadas.getValue();
        int faltasRecibidas = (Integer)contadorFaltasRecibidas.getValue();
        
        int tirosTotales = tirosLibresRealizados + tirosDoblesRealizados + tirosTriplesRealizados;
        int tirosMetidos = tirosLibresMetidos + tirosDoblesMetidos + tirosTriplesMetidos;
        int tirosDeCampoFallados = tirosTotales - tirosMetidos;
        
        //Calcular la eficiencia de los valores 2.0
        double eficiencia  = (puntos + rebotes + asistencias + robos + taponesRealizados + faltasRecibidas) - (tirosDeCampoFallados + perdidas + taponesRecibidos + faltasRealizadas);
        
        //Panel emergente con los datos de FG,eFG y TS
        javax.swing.JOptionPane.showMessageDialog(this,"FG: " + FG + "\n" +
                                                    "EFG: " + EFG + "\n" +
                                                    "TS: " + TS + "\n" + 
                                                    "Eficiencia del jugador: " + eficiencia);
        
        try {
            crearArchivoExcel("D:\\GSDAM 2º\\Desarrollo de interfaces (DI)\\NBA\\Resultados_NBA.xlsx", nombre, tirosLibresRealizados, tirosLibresMetidos, tirosDoblesRealizados,tirosDoblesMetidos,tirosTriplesRealizados,tirosTriplesMetidos, FG, EFG, TS, puntos, rebotes , asistencias, robos, perdidas, taponesRealizados, taponesRecibidos, faltasRealizadas, faltasRecibidas,eficiencia);
            javax.swing.JOptionPane.showMessageDialog(this, "Archivo actualizado");
        } catch (IOException e) {
        }
    }

    private void crearArchivoExcel(String archivos, String jugador, int tirosLibresRealizados, int tirosLibresMetidos, int tirosDoblesRealizados,int tirosDoblesMetidos,int tirosTriplesRealizados,int tirosTriplesMetidos, double FG, double EFG,double TS,int puntos,int rebotes, int asistencias, int robos, int perdidas, int taponesRealizados, int taponesRecibidos, int faltasRealizadas, int faltasRecibidas, double eficiencia )throws IOException {
    
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
        fila.createCell(10).setCellValue("Puntos");
        fila.createCell(11).setCellValue("Rebotes");
        fila.createCell(12).setCellValue("Asistencias");
        fila.createCell(13).setCellValue("Robos");
        fila.createCell(14).setCellValue("Pérdidas");
        fila.createCell(15).setCellValue("Tapones Realizados");
        fila.createCell(16).setCellValue("Tapones recibidos");
        fila.createCell(17).setCellValue("Faltas realizadas");
        fila.createCell(18).setCellValue("Faltas recibidas");
        fila.createCell(19).setCellValue("Eficiencia");
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
        fila.createCell(10).setCellValue(puntos);
        fila.createCell(11).setCellValue(rebotes);
        fila.createCell(12).setCellValue(asistencias);
        fila.createCell(13).setCellValue(robos);
        fila.createCell(14).setCellValue(perdidas);
        fila.createCell(15).setCellValue(taponesRealizados);
        fila.createCell(16).setCellValue(taponesRecibidos);
        fila.createCell(17).setCellValue(faltasRealizadas);
        fila.createCell(18).setCellValue(faltasRecibidas);
        fila.createCell(19).setCellValue(eficiencia);
        
        //Actualizar la ultima fila para que realice el calculo correcto de las medias
        ultimaFila = pagina.getLastRowNum();
        
        //Asignamos un fondo para la celda de medias
        CellStyle estilo = excel.createCellStyle();
        estilo.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        //Esctibir la media en la ultima fila del excel
        Row filaMedias = pagina.createRow(ultimaFila + 2);
        
        //Cambiamos el estilo de la celda "Media"
        Cell celdaMediaTexto = filaMedias.createCell(0);
        celdaMediaTexto.setCellValue("Media:");
        celdaMediaTexto.setCellStyle(estilo);

       
        for (int columna = 1; columna <= 19; columna++) {
            double suma = 0.0;
            int filasDatos = 0;

            // Recorremos las filas para la columna actual
            for (int i = 1; i <= ultimaFila; i++) {
                Row filaActual = pagina.getRow(i);
                if (filaActual != null) {
                    Cell celda = filaActual.getCell(columna);
                    if (celda != null && celda.getCellType() == CellType.NUMERIC) {
                        System.out.println("Columna: " + columna + ", Fila: " + i + ", Valor: " + celda.getNumericCellValue());
                        suma += celda.getNumericCellValue();
                        filasDatos++;
                    }
                }
            }
            System.out.println("Columna: " + columna + ", Suma: " + suma + ", Filas válidas: " + filasDatos);
            
            
            double media;

            if (filasDatos > 0) {
                media = suma / filasDatos; 
            } else {
                media = 0.0; 
            }
            
            // Aplicar el estilo a la celda del resultado "Media"
            Cell fondoCeldaMedia = filaMedias.createCell(columna);
            fondoCeldaMedia.setCellValue(media);
            fondoCeldaMedia.setCellStyle(estilo);
        }
        
        //Ajusta el tamaño de las celdas
        for (int i = 0; i <= 19; i++){
            pagina.autoSizeColumn(i);
        }
        
        //Escribir el documento excel
        try (FileOutputStream archivo = new FileOutputStream(archivos)) {
            excel.write(archivo);
        }
} 
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PestañaResultados = new javax.swing.JTabbedPane();
        Opcion_1 = new javax.swing.JPanel();
        Ventana_1 = new javax.swing.JPanel();
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
        equipos = new javax.swing.JLabel();
        Opcion_2 = new javax.swing.JPanel();
        Ventana_2 = new javax.swing.JPanel();
        puntos = new javax.swing.JLabel();
        contadorPuntos = new javax.swing.JSpinner();
        rebotes = new javax.swing.JLabel();
        contadorRebotes = new javax.swing.JSpinner();
        asistencias = new javax.swing.JLabel();
        contadorAsistencias = new javax.swing.JSpinner();
        Robos = new javax.swing.JLabel();
        contadorRobos = new javax.swing.JSpinner();
        perdidas = new javax.swing.JLabel();
        contadorPerdidas = new javax.swing.JSpinner();
        taponesRealizados = new javax.swing.JLabel();
        contadorTaponesRealizados = new javax.swing.JSpinner();
        taponesRecibidos = new javax.swing.JLabel();
        contadorTaponesRecibidos = new javax.swing.JSpinner();
        faltasRealizadas = new javax.swing.JLabel();
        contadorFaltasRealizadas = new javax.swing.JSpinner();
        faltasRecibidas = new javax.swing.JLabel();
        contadorFaltasRecibidas = new javax.swing.JSpinner();
        img = new javax.swing.JLabel();
        botonCalcular = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(java.awt.Color.white);

        Ventana_1.setBackground(new java.awt.Color(0, 0, 0));

        nombre.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        nombre.setForeground(new java.awt.Color(255, 255, 255));
        nombre.setText("Nombre del jugador");

        tirosRealizados.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosRealizados.setForeground(new java.awt.Color(255, 255, 255));
        tirosRealizados.setText("Tiros libres realizados");

        contadorTirosLibresRealizados.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        tirosLibresMetidos.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosLibresMetidos.setForeground(new java.awt.Color(255, 255, 255));
        tirosLibresMetidos.setText("Tiros libres metidos");

        contadorTirosLibresMetidos.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        TirosDoblesRealizados.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        TirosDoblesRealizados.setForeground(new java.awt.Color(255, 255, 255));
        TirosDoblesRealizados.setText("Tiros de 2 realizados");

        contadorTirosDoblesRealizados.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        tirosDoblesMetidos.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosDoblesMetidos.setForeground(new java.awt.Color(255, 255, 255));
        tirosDoblesMetidos.setText("Tiros metidos de 2");

        contadorTirosDoblesMetidos.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        tirosTriplesRealizados.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosTriplesRealizados.setForeground(new java.awt.Color(255, 255, 255));
        tirosTriplesRealizados.setText("Tiros de 3 realizados");

        contadorTriplesRealizados.setModel(new javax.swing.SpinnerNumberModel());

        tirosTriplesMetidos.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        tirosTriplesMetidos.setForeground(new java.awt.Color(255, 255, 255));
        tirosTriplesMetidos.setText("Tiros metidos de 3");

        contadorTriplesMetidos.setModel(new javax.swing.SpinnerNumberModel());

        equipos.setFont(new java.awt.Font("MingLiU-ExtB", 2, 48)); // NOI18N
        equipos.setForeground(new java.awt.Color(255, 255, 255));
        equipos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        equipos.setText("Equipos");
        equipos.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout Ventana_1Layout = new javax.swing.GroupLayout(Ventana_1);
        Ventana_1.setLayout(Ventana_1Layout);
        Ventana_1Layout.setHorizontalGroup(
            Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Ventana_1Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(equipos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Ventana_1Layout.createSequentialGroup()
                        .addComponent(tirosLibresMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(contadorTirosLibresMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(Ventana_1Layout.createSequentialGroup()
                            .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(textoNombre))
                        .addGroup(Ventana_1Layout.createSequentialGroup()
                            .addComponent(tirosTriplesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(contadorTriplesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Ventana_1Layout.createSequentialGroup()
                            .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(TirosDoblesRealizados, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tirosDoblesMetidos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tirosTriplesRealizados))
                            .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(Ventana_1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(contadorTirosDoblesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(Ventana_1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(contadorTirosDoblesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Ventana_1Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(contadorTriplesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(Ventana_1Layout.createSequentialGroup()
                            .addComponent(tirosRealizados)
                            .addGap(18, 18, 18)
                            .addComponent(contadorTirosLibresRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(130, Short.MAX_VALUE))
        );
        Ventana_1Layout.setVerticalGroup(
            Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Ventana_1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(equipos, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombre)
                    .addComponent(textoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contadorTirosLibresRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tirosRealizados))
                .addGap(29, 29, 29)
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tirosLibresMetidos)
                    .addComponent(contadorTirosLibresMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contadorTirosDoblesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TirosDoblesRealizados))
                .addGap(28, 28, 28)
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tirosDoblesMetidos)
                    .addComponent(contadorTirosDoblesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tirosTriplesRealizados)
                    .addComponent(contadorTriplesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tirosTriplesMetidos)
                    .addComponent(contadorTriplesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(81, 81, 81))
        );

        javax.swing.GroupLayout Opcion_1Layout = new javax.swing.GroupLayout(Opcion_1);
        Opcion_1.setLayout(Opcion_1Layout);
        Opcion_1Layout.setHorizontalGroup(
            Opcion_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Ventana_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Opcion_1Layout.setVerticalGroup(
            Opcion_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Ventana_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        PestañaResultados.addTab("Jugador y Tiros", Opcion_1);

        Ventana_2.setBackground(new java.awt.Color(0, 0, 0));

        puntos.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        puntos.setForeground(new java.awt.Color(255, 255, 255));
        puntos.setText("Puntos");

        contadorPuntos.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        rebotes.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        rebotes.setForeground(new java.awt.Color(255, 255, 255));
        rebotes.setText("Rebotes");

        contadorRebotes.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        asistencias.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        asistencias.setForeground(new java.awt.Color(255, 255, 255));
        asistencias.setText("Asistencias");

        contadorAsistencias.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        Robos.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        Robos.setForeground(new java.awt.Color(255, 255, 255));
        Robos.setText("Robos");

        contadorRobos.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        perdidas.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        perdidas.setForeground(new java.awt.Color(255, 255, 255));
        perdidas.setText("Pérdidas");

        contadorPerdidas.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        taponesRealizados.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        taponesRealizados.setForeground(new java.awt.Color(255, 255, 255));
        taponesRealizados.setText("Tapones realizados");

        contadorTaponesRealizados.setModel(new javax.swing.SpinnerNumberModel());

        taponesRecibidos.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        taponesRecibidos.setForeground(new java.awt.Color(255, 255, 255));
        taponesRecibidos.setText("Tapones recibidos");

        contadorTaponesRecibidos.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        faltasRealizadas.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        faltasRealizadas.setForeground(new java.awt.Color(255, 255, 255));
        faltasRealizadas.setText("Faltas realizadas");

        contadorFaltasRealizadas.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        faltasRecibidas.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        faltasRecibidas.setForeground(new java.awt.Color(255, 255, 255));
        faltasRecibidas.setText("Faltas recibidas");

        contadorFaltasRecibidas.setModel(new javax.swing.SpinnerNumberModel());

        img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nba.png"))); // NOI18N

        botonCalcular.setText("Calcular");
        botonCalcular.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonCalcular.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout Ventana_2Layout = new javax.swing.GroupLayout(Ventana_2);
        Ventana_2.setLayout(Ventana_2Layout);
        Ventana_2Layout.setHorizontalGroup(
            Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Ventana_2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Ventana_2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Ventana_2Layout.createSequentialGroup()
                        .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(taponesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(perdidas, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Robos, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(asistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rebotes, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(puntos, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(faltasRealizadas, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(contadorTaponesRealizados)
                            .addComponent(contadorRobos)
                            .addComponent(contadorAsistencias)
                            .addComponent(contadorPerdidas)
                            .addComponent(contadorPuntos, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                            .addComponent(contadorFaltasRealizadas)
                            .addComponent(contadorRebotes))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(Ventana_2Layout.createSequentialGroup()
                                    .addComponent(faltasRecibidas, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(contadorFaltasRecibidas, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(Ventana_2Layout.createSequentialGroup()
                                    .addComponent(taponesRecibidos, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(contadorTaponesRecibidos, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(img, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(42, 42, 42))
        );
        Ventana_2Layout.setVerticalGroup(
            Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Ventana_2Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Ventana_2Layout.createSequentialGroup()
                        .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(puntos)
                            .addComponent(contadorPuntos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rebotes)
                            .addComponent(contadorRebotes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(asistencias)
                            .addComponent(contadorAsistencias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Robos)
                            .addComponent(contadorRobos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(perdidas)
                            .addComponent(contadorPerdidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(img, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(61, 61, 61)
                .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(taponesRealizados)
                    .addComponent(contadorTaponesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taponesRecibidos)
                    .addComponent(contadorTaponesRecibidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(Ventana_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(faltasRecibidas)
                    .addComponent(contadorFaltasRealizadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(faltasRealizadas)
                    .addComponent(contadorFaltasRecibidas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(botonCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout Opcion_2Layout = new javax.swing.GroupLayout(Opcion_2);
        Opcion_2.setLayout(Opcion_2Layout);
        Opcion_2Layout.setHorizontalGroup(
            Opcion_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Ventana_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        Opcion_2Layout.setVerticalGroup(
            Opcion_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Ventana_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        PestañaResultados.addTab("Estadisticas del Jugador", Opcion_2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PestañaResultados, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PestañaResultados, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Principal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Opcion_1;
    private javax.swing.JPanel Opcion_2;
    private javax.swing.JTabbedPane PestañaResultados;
    private javax.swing.JLabel Robos;
    private javax.swing.JLabel TirosDoblesRealizados;
    private javax.swing.JPanel Ventana_1;
    private javax.swing.JPanel Ventana_2;
    private javax.swing.JLabel asistencias;
    private javax.swing.JButton botonCalcular;
    private javax.swing.JSpinner contadorAsistencias;
    private javax.swing.JSpinner contadorFaltasRealizadas;
    private javax.swing.JSpinner contadorFaltasRecibidas;
    private javax.swing.JSpinner contadorPerdidas;
    private javax.swing.JSpinner contadorPuntos;
    private javax.swing.JSpinner contadorRebotes;
    private javax.swing.JSpinner contadorRobos;
    private javax.swing.JSpinner contadorTaponesRealizados;
    private javax.swing.JSpinner contadorTaponesRecibidos;
    private javax.swing.JSpinner contadorTirosDoblesMetidos;
    private javax.swing.JSpinner contadorTirosDoblesRealizados;
    private javax.swing.JSpinner contadorTirosLibresMetidos;
    private javax.swing.JSpinner contadorTirosLibresRealizados;
    private javax.swing.JSpinner contadorTriplesMetidos;
    private javax.swing.JSpinner contadorTriplesRealizados;
    private javax.swing.JLabel equipos;
    private javax.swing.JLabel faltasRealizadas;
    private javax.swing.JLabel faltasRecibidas;
    private javax.swing.JLabel img;
    private javax.swing.JLabel nombre;
    private javax.swing.JLabel perdidas;
    private javax.swing.JLabel puntos;
    private javax.swing.JLabel rebotes;
    private javax.swing.JLabel taponesRealizados;
    private javax.swing.JLabel taponesRecibidos;
    private javax.swing.JTextField textoNombre;
    private javax.swing.JLabel tirosDoblesMetidos;
    private javax.swing.JLabel tirosLibresMetidos;
    private javax.swing.JLabel tirosRealizados;
    private javax.swing.JLabel tirosTriplesMetidos;
    private javax.swing.JLabel tirosTriplesRealizados;
    // End of variables declaration//GEN-END:variables
}
