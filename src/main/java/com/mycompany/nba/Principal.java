package com.mycompany.nba;

import java.io.File;
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
        seleccionarEquipos.addActionListener(evt -> elegirEquipo());
        botonCalcular.addActionListener(evt -> generarExcel()); 
        
    }
    
    
    //Version 3.0 Crear desplegables para los equipos y jugadores
    private String[] jugadoresChicago = {"Michael Jordan", "Scottie Pippen", "Dennis Rodman", "Steve Kerr", "Toni Kukoc"};
    private String[] jugadoresLakers = {"Kobe Bryant", "Shaquille O-Neal", "Magic Johnson", "Kareem Abdul-Jabbar", "James Worthy"};
    
    private void elegirEquipo(){
        
        String seleccionarEquipo = (String) seleccionarEquipos.getSelectedItem();
        
        // Borramos las anteriores opciones marcadas para evitar duplicar los jugadores
        seleccionarJugadores.removeAllItems();
        
        if ("Chicago Bulls".equals(seleccionarEquipo)){
            for (String jugador : jugadoresChicago){
                seleccionarJugadores.addItem(jugador);
            }
        }else if ("Los Angeles Lakers".equals(seleccionarEquipo)) {
            for (String jugador : jugadoresLakers){
                seleccionarJugadores.addItem(jugador);
            }
        }
    }
    
    private void generarExcel() {
        
        // Recuperar el equipo y jugador seleccionados
        String equipoSeleccionado = (String) seleccionarEquipos.getSelectedItem();
        String jugadorSeleccionado = (String) seleccionarJugadores.getSelectedItem();
        
        
       
        
        //Primera ventana con los valores de la version 1.5
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
            if("Chicago Bulls".equals(equipoSeleccionado)){
                crearArchivoExce("D:\\GSDAM 2º\\Desarrollo de interfaces (DI)\\NBA\\Chicago Bulls.xlsx", jugadorSeleccionado,tirosLibresRealizados, tirosLibresMetidos, tirosDoblesRealizados,tirosDoblesMetidos,tirosTriplesRealizados,tirosTriplesMetidos, FG, EFG, TS, puntos, rebotes , asistencias, robos, perdidas, taponesRealizados, taponesRecibidos, faltasRealizadas, faltasRecibidas,eficiencia);
                calcularMediasPorEquipo("D:\\GSDAM 2º\\Desarrollo de interfaces (DI)\\NBA\\Chicago Bulls.xlsx");
                javax.swing.JOptionPane.showMessageDialog(this, "Archivo actualizado");
            }else if("Los Angeles Lakers".equals(equipoSeleccionado)){
                crearArchivoExce("D:\\GSDAM 2º\\Desarrollo de interfaces (DI)\\NBA\\Los Angeles Lakers.xlsx", jugadorSeleccionado,tirosLibresRealizados, tirosLibresMetidos, tirosDoblesRealizados,tirosDoblesMetidos,tirosTriplesRealizados,tirosTriplesMetidos, FG, EFG, TS, puntos, rebotes , asistencias, robos, perdidas, taponesRealizados, taponesRecibidos, faltasRealizadas, faltasRecibidas,eficiencia);
                calcularMediasPorEquipo("D:\\GSDAM 2º\\Desarrollo de interfaces (DI)\\NBA\\Los Angeles Lakers.xlsx");
                javax.swing.JOptionPane.showMessageDialog(this, "Archivo actualizado");
            }
        } catch (IOException e) {
        }
    }
    
    private void crearArchivoExce(String rutaArchivo, String jugador, int tirosLibresRealizados, int tirosLibresMetidos, int tirosDoblesRealizados,int tirosDoblesMetidos,int tirosTriplesRealizados,int tirosTriplesMetidos, double FG, double EFG,double TS,int puntos,int rebotes, int asistencias, int robos, int perdidas, int taponesRealizados, int taponesRecibidos, int faltasRealizadas, int faltasRecibidas, double eficiencia )throws IOException {
        
        Workbook excel;
        Sheet hojaJugador;

        // Verificar si el archivo ya existe
        File archivo = new File(rutaArchivo);
        if (archivo.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(archivo)) {
                excel = new XSSFWorkbook(fileInputStream);
            }
        } else {
            excel = new XSSFWorkbook(); // Crear nuevo archivo si no existe
        }

            // Crea una hoja nueva o usa la existente
            hojaJugador = excel.getSheet(jugador);
            if (hojaJugador == null) {
                hojaJugador = excel.createSheet(jugador);
            }

            // Añade estadísticas a la hoja del jugador
            estadisticasJugador(hojaJugador, tirosLibresRealizados, tirosLibresMetidos, tirosDoblesRealizados,
                                tirosDoblesMetidos, tirosTriplesRealizados, tirosTriplesMetidos, FG, EFG, TS,
                                puntos, rebotes, asistencias, robos, perdidas, taponesRealizados, taponesRecibidos,
                                faltasRealizadas, faltasRecibidas, eficiencia);

            // Escribe el archivo actualizado
            try (FileOutputStream fileOutputStream = new FileOutputStream(archivo)) {
                excel.write(fileOutputStream);
            }
        }

        private void estadisticasJugador(Sheet hojaJugador, int tirosLibresRealizados, int tirosLibresMetidos, int tirosDoblesRealizados,int tirosDoblesMetidos, int tirosTriplesRealizados, int tirosTriplesMetidos, double FG, double EFG,double TS, int puntos, int rebotes, int asistencias, int robos, int perdidas, int taponesRealizados,int taponesRecibidos, int faltasRealizadas, int faltasRecibidas, double eficiencia){

            Row encabezado = hojaJugador.getRow(0);
            if (encabezado == null) {
                encabezado = hojaJugador.createRow(0);
                encabezado.createCell(0).setCellValue("Tiros libres realizados");
                encabezado.createCell(1).setCellValue("Tiros libres metidos");
                encabezado.createCell(2).setCellValue("Dobles realizados");
                encabezado.createCell(3).setCellValue("Dobles metidos");
                encabezado.createCell(4).setCellValue("Triples realizados");
                encabezado.createCell(5).setCellValue("Triples metidos");
                encabezado.createCell(6).setCellValue("FG%");
                encabezado.createCell(7).setCellValue("eFG%");
                encabezado.createCell(8).setCellValue("TS%");
                encabezado.createCell(9).setCellValue("Puntos");
                encabezado.createCell(10).setCellValue("Rebotes");
                encabezado.createCell(11).setCellValue("Asistencias");
                encabezado.createCell(12).setCellValue("Robos");
                encabezado.createCell(13).setCellValue("Pérdidas");
                encabezado.createCell(14).setCellValue("Tapones Realizados");
                encabezado.createCell(15).setCellValue("Tapones Recibidos");
                encabezado.createCell(16).setCellValue("Faltas Realizadas");
                encabezado.createCell(17).setCellValue("Faltas Recibidas");
                encabezado.createCell(18).setCellValue("Eficiencia");
            }

            // Determinar la siguiente fila disponible
            int ultimaFila = hojaJugador.getLastRowNum() + 1;

            // Crear una nueva fila para los datos
            Row datos = hojaJugador.createRow(ultimaFila);
            datos.createCell(0).setCellValue(tirosLibresRealizados);
            datos.createCell(1).setCellValue(tirosLibresMetidos);
            datos.createCell(2).setCellValue(tirosDoblesRealizados);
            datos.createCell(3).setCellValue(tirosDoblesMetidos);
            datos.createCell(4).setCellValue(tirosTriplesRealizados);
            datos.createCell(5).setCellValue(tirosTriplesMetidos);
            datos.createCell(6).setCellValue(FG);
            datos.createCell(7).setCellValue(EFG);
            datos.createCell(8).setCellValue(TS);
            datos.createCell(9).setCellValue(puntos);
            datos.createCell(10).setCellValue(rebotes);
            datos.createCell(11).setCellValue(asistencias);
            datos.createCell(12).setCellValue(robos);
            datos.createCell(13).setCellValue(perdidas);
            datos.createCell(14).setCellValue(taponesRealizados);
            datos.createCell(15).setCellValue(taponesRecibidos);
            datos.createCell(16).setCellValue(faltasRealizadas);
            datos.createCell(17).setCellValue(faltasRecibidas);
            datos.createCell(18).setCellValue(eficiencia);

            // Ajustar automáticamente el tamaño de las columnas
            for (int i = 0; i <= 18; i++) {
                hojaJugador.autoSizeColumn(i);
            }

        }

        private void calcularMediasPorEquipo(String rutaArchivo) throws IOException {
            
            File archivo = new File(rutaArchivo);
            
            Workbook excel;
            try (FileInputStream fileInputStream = new FileInputStream(archivo)) {
                excel = new XSSFWorkbook(fileInputStream);
            }
            
            int borrarHojaMedias = excel.getSheetIndex("Medias por jugador");
            
            if (borrarHojaMedias != -1){
                excel.removeSheetAt(borrarHojaMedias);
            }
            
            Sheet hojaMedias = excel.createSheet("Medias por jugador");
            
            // Encabezados para la hoja de medias
            Row encabezado = hojaMedias.createRow(0);
            encabezado.createCell(0).setCellValue("Jugador");
            encabezado.createCell(1).setCellValue("Tiros libres Realizados");
            encabezado.createCell(2).setCellValue("Tiros libres metidos");
            encabezado.createCell(3).setCellValue("Dobles realizados");
            encabezado.createCell(4).setCellValue("Dobles metidos");
            encabezado.createCell(5).setCellValue("Triples realizados");
            encabezado.createCell(6).setCellValue("Triples metidos");
            encabezado.createCell(7).setCellValue("FG%");
            encabezado.createCell(8).setCellValue("eFG%");
            encabezado.createCell(9).setCellValue("TS%");
            encabezado.createCell(10).setCellValue("Puntos");
            encabezado.createCell(11).setCellValue("Rebotes");
            encabezado.createCell(12).setCellValue("Asistencias");
            encabezado.createCell(13).setCellValue("Robos");
            encabezado.createCell(14).setCellValue("Pérdidas");
            encabezado.createCell(15).setCellValue("Tapones Realizados");
            encabezado.createCell(16).setCellValue("Tapones recibidos");
            encabezado.createCell(17).setCellValue("Faltas realizadas");
            encabezado.createCell(18).setCellValue("Faltas recibidas");
            encabezado.createCell(19).setCellValue("Eficiencia");
            
            
            int filaMedia = 1;
            for (int i = 0; i < excel.getNumberOfSheets();i++){
                Sheet hojaJugador =excel.getSheetAt(i);
                String nombreJugador = excel.getSheetName(i);
                
                if("Medias por jugador".equals(nombreJugador)){
                    continue;
                }
                Row filaMedias = hojaMedias.createRow(filaMedia++);
                filaMedias.createCell(0).setCellValue(nombreJugador);
                
                int ultimafila = hojaJugador.getLastRowNum();
                
                for(int columna = 0; columna < 19; columna++){
                    double suma = 0.0;
                    int filasDatos = 0;
                    
                    for(int filas = 1; filas <= ultimafila; filas++){
                        Row fila = hojaJugador.getRow(filas);

                        Cell celda = fila.getCell(columna);
                        if (celda != null && celda.getCellType() == CellType.NUMERIC){
                            suma += celda.getNumericCellValue();
                            filasDatos++;
                        }

                        double media ;

                        if (filasDatos > 0) {
                            media = suma / filasDatos; 
                        } else {
                            media = 0.0; 
                        }

                        filaMedias.createCell(columna + 1).setCellValue(media);

                    }

                     for (int a = 0; a <= 19; a++){
                        hojaMedias.autoSizeColumn(a);
                    }
        
                }
                
                try (FileOutputStream fileOutputStream = new FileOutputStream(archivo)) {
                   excel.write(fileOutputStream);
                }
                
            }
        }
        
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PestañaResultados = new javax.swing.JTabbedPane();
        Opcion_1 = new javax.swing.JPanel();
        Ventana_1 = new javax.swing.JPanel();
        equipo = new javax.swing.JLabel();
        seleccionarEquipos = new javax.swing.JComboBox<>();
        jugador = new javax.swing.JLabel();
        seleccionarJugadores = new javax.swing.JComboBox<>();
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
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Ventana_1.setBackground(new java.awt.Color(0, 0, 0));

        equipo.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        equipo.setForeground(new java.awt.Color(255, 255, 255));
        equipo.setText("Equipo");

        seleccionarEquipos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Chicago Bulls", "Los Angeles Lakers" }));

        jugador.setFont(new java.awt.Font("Bookman Old Style", 3, 18)); // NOI18N
        jugador.setForeground(new java.awt.Color(255, 255, 255));
        jugador.setText("Jugador");

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
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jugador, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(equipos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Ventana_1Layout.createSequentialGroup()
                            .addComponent(tirosLibresMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(contadorTirosLibresMetidos))
                        .addGroup(Ventana_1Layout.createSequentialGroup()
                            .addComponent(tirosTriplesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                            .addComponent(contadorTriplesMetidos, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(Ventana_1Layout.createSequentialGroup()
                            .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tirosDoblesMetidos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tirosTriplesRealizados)
                                .addComponent(TirosDoblesRealizados, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(Ventana_1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(contadorTirosDoblesRealizados, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                                .addGroup(Ventana_1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(contadorTriplesRealizados, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(contadorTirosDoblesMetidos)))))
                        .addGroup(Ventana_1Layout.createSequentialGroup()
                            .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tirosRealizados)
                                .addComponent(equipo, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(seleccionarEquipos, 0, 169, Short.MAX_VALUE)
                                .addComponent(seleccionarJugadores, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(contadorTirosLibresRealizados)))))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        Ventana_1Layout.setVerticalGroup(
            Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Ventana_1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(equipos, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(equipo)
                    .addComponent(seleccionarEquipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Ventana_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jugador)
                    .addComponent(seleccionarJugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
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
                    .addComponent(img, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
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

        getContentPane().add(PestañaResultados, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 0, -1, -1));

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
    private javax.swing.JLabel equipo;
    private javax.swing.JLabel equipos;
    private javax.swing.JLabel faltasRealizadas;
    private javax.swing.JLabel faltasRecibidas;
    private javax.swing.JLabel img;
    private javax.swing.JLabel jugador;
    private javax.swing.JLabel perdidas;
    private javax.swing.JLabel puntos;
    private javax.swing.JLabel rebotes;
    private javax.swing.JComboBox<String> seleccionarEquipos;
    private javax.swing.JComboBox<String> seleccionarJugadores;
    private javax.swing.JLabel taponesRealizados;
    private javax.swing.JLabel taponesRecibidos;
    private javax.swing.JLabel tirosDoblesMetidos;
    private javax.swing.JLabel tirosLibresMetidos;
    private javax.swing.JLabel tirosRealizados;
    private javax.swing.JLabel tirosTriplesMetidos;
    private javax.swing.JLabel tirosTriplesRealizados;
    // End of variables declaration//GEN-END:variables
}
