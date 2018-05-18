/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 *
 * @author Alejandro Luna Gómez
 */
public class VentanaBuscaminas extends javax.swing.JFrame {

    // Variables de instancia
    int filas = 15;
    int columnas = 20;
    int casillasTotales = filas * columnas;
    int numMinas = 30;
    int minasDesactivadas = 0;
    int casillasPresionadas = 0;

    long tiempoInicio = 0;
    long tiempoFin = 0;

    boolean gameOver = false;
    boolean firstClick = true;

    Boton[][] arrayBotones = new Boton[filas][columnas];

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * Creates new form VentanaBuscaminas
     */
    public VentanaBuscaminas() {
        initComponents();
        jDialog1.setSize(325, 155);
        jDialog1.setLocation(dim.width / 2 - jDialog1.getSize().width / 2, dim.height / 2 - jDialog1.getSize().height / 2);
        jDialog2.setSize(220, 215);
        jDialog2.setLocation(dim.width / 2 - jDialog2.getSize().width / 2, dim.height / 2 - jDialog2.getSize().height / 2);
        nuevaPartidaAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        mNuevaPartida.setAction(nuevaPartidaAction);
        opcionesAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        mOpciones.setAction(opcionesAction);
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        mSalir.setAction(exitAction);
        try {
            this.setIconImage(new ImageIcon(ImageIO.read(getClass().getResource("/img/logo.png"))).getImage());
            jDialog1.setIconImage(new ImageIcon(ImageIO.read(getClass().getResource("/img/logo.png"))).getImage());
            jDialog2.setIconImage(new ImageIcon(ImageIO.read(getClass().getResource("/img/logo.png"))).getImage());
            this.setTitle("Buscaminas - Alejandro Luna Gómez");
            jDialog1.setTitle("");
            jDialog2.setTitle("Configuración");
        } catch (Exception e) {
            this.setIconImage(null);
        }
        ejecutaJuego();
    }

    /**
     * Ejecuta el juego.
     */
    private void ejecutaJuego() {
        setSize(17 * columnas, 17 * filas + 45);
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        getContentPane().setLayout(new GridLayout(filas, columnas));
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Boton boton = new Boton(i, j);
                getContentPane().add(boton);
                arrayBotones[i][j] = boton;
                arrayBotones[i][j].setIcon(new ImageIcon(getClass().getResource("/img/celda.png")));
                boton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent evt) {
                        botonPulsado(evt);
                    }
                });
            }
        }
    }

    /**
     * Reinicia la partida.
     */
    private void limpiaJuego() {
        this.setVisible(false);
        gameOver = false;
        firstClick = true;
        minasDesactivadas = 0;
        casillasPresionadas = 0;
        tiempoInicio = 0;
        tiempoFin = 0;
        casillasTotales = filas * columnas;
        arrayBotones = new Boton[filas][columnas];
        getContentPane().removeAll();
        ejecutaJuego();
        this.setVisible(true);
    }

    /**
     * Realiza una serie de operaciones dependiendo del botón del ratón
     * presionado.
     *
     * @param e - MouseEvent
     */
    private void botonPulsado(MouseEvent e) {
        Boton miBoton = (Boton) e.getComponent();
        if (firstClick) {
            tiempoInicio = System.currentTimeMillis() / 1000;
            firstClick = false;
            ponMinas(numMinas, miBoton);
            cuentaMinas();
        }
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (miBoton.isNoAccionado()) {
                    if (!miBoton.isAbanderado()) {
                        miBoton.setIcon(new ImageIcon(getClass().getResource("/img/bandera.png")));
                        miBoton.setAbanderado(true);
                    } else if (miBoton.isAbanderado()) {
                        miBoton.setIcon(new ImageIcon(getClass().getResource("/img/celda.png")));
                        miBoton.setAbanderado(false);
                    }
                    if (miBoton.getMina() == 1) {
                        minasDesactivadas++;
                    }
                }
            } else if (e.getButton() == MouseEvent.BUTTON1) {
                // TODO: Algoritmo inundacion.
                limpiaCasillas(miBoton.getI(), miBoton.getJ());
                if (casillasTotales - numMinas == casillasPresionadas) {
                    gameOver(true);
                }
            }
        } else if (gameOver) {
        }
        System.out.println(casillasPresionadas);
    }

    /**
     * Genera las minas en el tablero, sin colocar minas donde ya hay o en la
     * casilla presionada por primera vez.
     *
     * @param _numeroMinas - int
     * @param _boton - Boton(Objeto)
     */
    private void ponMinas(int _numeroMinas, Boton _boton) {
        Random r = new Random();
        for (int i = 0; i < _numeroMinas; i++) {
            int f = r.nextInt(filas);
            int c = r.nextInt(columnas);
            // TODO: Chequea si ya hay una mina en la casilla y buscar otra.
            if (arrayBotones[f][c] != _boton) {
                if (arrayBotones[f][c].getMina() == 0) {
                    arrayBotones[f][c].setMina(1);
                } else {
                    i--;
                }
            } else {
                i--;
            }
        }
    }

    /**
     * Cuenta el número de minas colindantes a cada casilla y almacena el valor
     * en una variable en cada objeto.
     */
    private void cuentaMinas() {
        int minas = 0;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                /*
                     * Mapa de Minas:
                     *      1 2 3
                     *      8 X 4
                     *      7 6 5
                 */
                try {
                    minas += arrayBotones[i - 1][j - 1].getMina();  // 1
                } catch (Exception e) {
                }
                try {
                    minas += arrayBotones[i - 1][j].getMina();      // 2
                } catch (Exception e) {
                }
                try {
                    minas += arrayBotones[i - 1][j + 1].getMina();  // 3
                } catch (Exception e) {
                }
                try {
                    minas += arrayBotones[i][j + 1].getMina();      // 4
                } catch (Exception e) {
                }
                try {
                    minas += arrayBotones[i + 1][j + 1].getMina();  // 5
                } catch (Exception e) {
                }
                try {
                    minas += arrayBotones[i + 1][j].getMina();      // 6
                } catch (Exception e) {
                }
                try {
                    minas += arrayBotones[i + 1][j - 1].getMina();  // 7
                } catch (Exception e) {
                }
                try {
                    minas += arrayBotones[i][j - 1].getMina();      // 8
                } catch (Exception e) {
                }

                arrayBotones[i][j].setNumeroMinasAlrededor(minas);

                //TODO: No mostrar los números al iniciar.
                if (arrayBotones[i][j].getMina() == 0 && minas != 0) {
                    //arrayBotones[i][j].setText(String.valueOf(minas));
                }
                minas = 0;
            }
        }
    }

    /**
     * Muestra el contenido de la casilla presionada y las colindantes en caso
     * de poder.
     *
     * @param i - int
     * @param j - int
     */
    private void limpiaCasillas(int i, int j) {

        boolean pegadaAMina = false;

        for (int auxI = -1; auxI <= 1; auxI++) {
            for (int auxJ = -1; auxJ <= 1; auxJ++) {

                try {
                    if (arrayBotones[i][j].getMina() != 0 && !gameOver) {
                        arrayBotones[i][j].setNoAccionado(false);
                        arrayBotones[i][j].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/img/bomba.png"))));
                        gameOver = true;
                        gameOver(false);
                    } else if (!gameOver) {
                        if (arrayBotones[i][j].getNumeroMinasAlrededor() == 0) {
                            if (arrayBotones[i + auxI][j + auxJ].isNoAccionado()) {
                                if (arrayBotones[i + auxI][j + auxJ].getMina() == 0) {
                                    if (arrayBotones[i + auxI][j + auxJ].getNumeroMinasAlrededor() == 0 && arrayBotones[i + auxI][j + auxJ].getMina() == 0) {
                                        if (arrayBotones[i + auxI][j + auxJ].isNoAccionado()) {
                                            casillasPresionadas++;
                                        }
                                        arrayBotones[i + auxI][j + auxJ].setNoAccionado(false);
                                        if (!pegadaAMina) {
                                            limpiaCasillas(i + auxI, j + auxJ);
                                            arrayBotones[i + auxI][j + auxJ].setImagen(arrayBotones[i + auxI][j + auxJ].getNumeroMinasAlrededor());
                                        }
                                    } else if (arrayBotones[i][j].getNumeroMinasAlrededor() != 0) {
                                        pegadaAMina = true;
                                    }
                                    if (arrayBotones[i + auxI][j + auxJ].getNumeroMinasAlrededor() != 0) {

                                        if (arrayBotones[i + auxI][j + auxJ].isNoAccionado()) {
                                            casillasPresionadas++;
                                        }
                                        arrayBotones[i + auxI][j + auxJ].setNoAccionado(false);
                                        arrayBotones[i + auxI][j + auxJ].setImagen(arrayBotones[i + auxI][j + auxJ].getNumeroMinasAlrededor());
                                    }
                                }
                            }
                        } else if (arrayBotones[i][j].getNumeroMinasAlrededor() != 0) {
                            if (arrayBotones[i][j].isNoAccionado()) {
                                casillasPresionadas++;
                                arrayBotones[i][j].setNoAccionado(false);
                                arrayBotones[i][j].setImagen(arrayBotones[i][j].getNumeroMinasAlrededor());
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Finaliza la partida y muetra la ventana de victoria/derrota.
     *
     * @param haGanado - boolean
     */
    private void gameOver(boolean haGanado) {
        //this.setVisible(false);
        if (haGanado) {
            tiempoFin = System.currentTimeMillis() / 1000;
            labelWinLose.setForeground(Color.decode("#77B24D"));
            labelWinLose.setText("¡Has ganado!");
            labelTime.setText("Tu partida ha durado: " + (tiempoFin - tiempoInicio) + " segundos");
            jDialog1.setTitle("¡Has ganado! ¿Otra partida?");
            jDialog1.setVisible(true);
        } else if (!haGanado) {
            tiempoFin = System.currentTimeMillis() / 1000;
            labelWinLose.setForeground(Color.decode("#C54D4D"));
            labelWinLose.setText("¡Has perdido!");
            labelTime.setText("Tu partida ha durado: " + (tiempoFin - tiempoInicio) + " segundos");
            jDialog1.setTitle("¡Has perdido! ¿Otra partida?");
            jDialog1.setVisible(true);
        }
    }

    /**
     * Guarda los valores de la ventana Configuración y crea una nueva partida
     * con los nuevos parámetros.
     */
    private void guardarConfig() {
        try {
            spinnerAlto.commitEdit();
            spinnerAncho.commitEdit();
            spinnerNumMinas.commitEdit();
        } catch (Exception e) {
        }
        filas = (Integer) spinnerAlto.getValue();
        columnas = (Integer) spinnerAncho.getValue();
        numMinas = (Integer) spinnerNumMinas.getValue();
        jDialog2.setVisible(false);
        this.setVisible(false);
        limpiaJuego();
        this.setVisible(true);
    }

    /**
     * Acción nuevaPartidaAction - Crea una nueva partida.
     */
    Action nuevaPartidaAction = new AbstractAction("Partida Nueva") {
        @Override
        public void actionPerformed(ActionEvent e) {
            limpiaJuego();
        }
    };

    /**
     * Acción opcionesAction - Muestra la ventana Opciones.
     */
    Action opcionesAction = new AbstractAction("Opciones") {
        @Override
        public void actionPerformed(ActionEvent e) {
            jDialog2.setVisible(true);
        }
    };

    /**
     * Acción exitAction - Cierra la aplicación.
     */
    Action exitAction = new AbstractAction("Salir") {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        buttonNewGame = new javax.swing.JButton();
        buttonClose = new javax.swing.JButton();
        labelWinLose = new javax.swing.JLabel();
        labelTime = new javax.swing.JLabel();
        jDialog2 = new javax.swing.JDialog();
        textAncho = new javax.swing.JLabel();
        textAlto = new javax.swing.JLabel();
        textNumMinas = new javax.swing.JLabel();
        spinnerAncho = new javax.swing.JSpinner();
        spinnerAlto = new javax.swing.JSpinner();
        spinnerNumMinas = new javax.swing.JSpinner();
        buttonGuardar = new javax.swing.JButton();
        buttonCerrar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        Archivo = new javax.swing.JMenu();
        mNuevaPartida = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mOpciones = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mSalir = new javax.swing.JMenuItem();

        jDialog1.setPreferredSize(new java.awt.Dimension(375, 155));
        jDialog1.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                jDialog1WindowClosing(evt);
            }
        });

        buttonNewGame.setText("Nueva Partida");
        buttonNewGame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                buttonNewGameMousePressed(evt);
            }
        });

        buttonClose.setText("Cerrar");
        buttonClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                buttonCloseMousePressed(evt);
            }
        });

        labelWinLose.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        labelWinLose.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelWinLose.setText("TEXTO HAS GANADO O PERDIDO");

        labelTime.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        labelTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTime.setText("Tu partida ha durado: 00000 Segundos");

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(buttonClose)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonNewGame))
                    .addComponent(labelWinLose, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                    .addComponent(labelTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(labelWinLose)
                .addGap(18, 18, 18)
                .addComponent(labelTime)
                .addGap(18, 18, 18)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonNewGame)
                    .addComponent(buttonClose))
                .addContainerGap())
        );

        jDialog2.setResizable(false);

        textAncho.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        textAncho.setText("Casillas Ancho:");
        textAncho.setPreferredSize(new java.awt.Dimension(125, 16));

        textAlto.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        textAlto.setText("Casillas Alto:");
        textAlto.setPreferredSize(new java.awt.Dimension(125, 16));

        textNumMinas.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        textNumMinas.setText("Numero de Minas:");
        textNumMinas.setPreferredSize(new java.awt.Dimension(125, 16));

        spinnerAncho.setModel(new javax.swing.SpinnerNumberModel(20, 10, 50, 1));
        spinnerAncho.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        spinnerAncho.setName(""); // NOI18N
        spinnerAncho.setValue(columnas);
        spinnerAncho.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                spinnerAnchoKeyPressed(evt);
            }
        });

        spinnerAlto.setModel(new javax.swing.SpinnerNumberModel(15, 10, 40, 1));
        spinnerAlto.setValue(filas);
        spinnerAlto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                spinnerAltoKeyPressed(evt);
            }
        });

        spinnerNumMinas.setModel(new javax.swing.SpinnerNumberModel(30, 5, 99, 1));
        spinnerNumMinas.setValue(numMinas);
        spinnerNumMinas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                spinnerNumMinasKeyPressed(evt);
            }
        });

        buttonGuardar.setText("Guardar");
        buttonGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                buttonGuardarMousePressed(evt);
            }
        });

        buttonCerrar.setText("Cerrar");
        buttonCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                buttonCerrarMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog2Layout.createSequentialGroup()
                        .addComponent(buttonCerrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonGuardar))
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textAncho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textAlto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textNumMinas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spinnerAlto, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(spinnerAncho, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(spinnerNumMinas, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textAncho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerAncho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textAlto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerAlto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textNumMinas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerNumMinas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonGuardar)
                    .addComponent(buttonCerrar))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Archivo.setText("Archivo");

        mNuevaPartida.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mNuevaPartida.setText("Nueva Partida");
        mNuevaPartida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mNuevaPartidaMousePressed(evt);
            }
        });
        Archivo.add(mNuevaPartida);
        Archivo.add(jSeparator1);

        mOpciones.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mOpciones.setText("Opciones");
        mOpciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mOpcionesMousePressed(evt);
            }
        });
        Archivo.add(mOpciones);
        Archivo.add(jSeparator2);

        mSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        mSalir.setText("Salir");
        mSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mSalirMousePressed(evt);
            }
        });
        Archivo.add(mSalir);

        jMenuBar1.add(Archivo);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCloseMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonCloseMousePressed
        System.exit(0);
    }//GEN-LAST:event_buttonCloseMousePressed

    private void buttonNewGameMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonNewGameMousePressed
        limpiaJuego();
        jDialog1.setVisible(false);
    }//GEN-LAST:event_buttonNewGameMousePressed

    private void jDialog1WindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialog1WindowClosing
        System.exit(0);
    }//GEN-LAST:event_jDialog1WindowClosing

    private void mSalirMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mSalirMousePressed
        System.exit(0);
    }//GEN-LAST:event_mSalirMousePressed

    private void mOpcionesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mOpcionesMousePressed
        jDialog2.setVisible(true);
    }//GEN-LAST:event_mOpcionesMousePressed

    private void buttonCerrarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonCerrarMousePressed
        jDialog2.setVisible(false);
    }//GEN-LAST:event_buttonCerrarMousePressed

    private void buttonGuardarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonGuardarMousePressed
        guardarConfig();
    }//GEN-LAST:event_buttonGuardarMousePressed

    private void mNuevaPartidaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mNuevaPartidaMousePressed
        limpiaJuego();
    }//GEN-LAST:event_mNuevaPartidaMousePressed

    private void spinnerNumMinasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spinnerNumMinasKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            guardarConfig();
        }
    }//GEN-LAST:event_spinnerNumMinasKeyPressed

    private void spinnerAltoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spinnerAltoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            guardarConfig();
        }
    }//GEN-LAST:event_spinnerAltoKeyPressed

    private void spinnerAnchoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spinnerAnchoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            guardarConfig();
        }
    }//GEN-LAST:event_spinnerAnchoKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscaminas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscaminas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscaminas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscaminas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaBuscaminas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Archivo;
    private javax.swing.JButton buttonCerrar;
    private javax.swing.JButton buttonClose;
    private javax.swing.JButton buttonGuardar;
    private javax.swing.JButton buttonNewGame;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JLabel labelTime;
    private javax.swing.JLabel labelWinLose;
    private javax.swing.JMenuItem mNuevaPartida;
    private javax.swing.JMenuItem mOpciones;
    private javax.swing.JMenuItem mSalir;
    private javax.swing.JSpinner spinnerAlto;
    private javax.swing.JSpinner spinnerAncho;
    private javax.swing.JSpinner spinnerNumMinas;
    private javax.swing.JLabel textAlto;
    private javax.swing.JLabel textAncho;
    private javax.swing.JLabel textNumMinas;
    // End of variables declaration//GEN-END:variables
}
