package gui;

import java.awt.*;
import java.awt.event.*;

import flights.Flight;
import timeHandling.Timer;

public class Scene extends Frame {
	private static final long serialVersionUID = 1L;
	private DrawingCanvas drawingCanvas;
    private Timer timer;
    private SimulationController simulationController;
    private boolean isSimulationRunning = false;
    private ShowData dataDisplay;

    public Scene() {
        timer = new Timer();
        simulationController = new SimulationController();

        timer.setListener((minutes, seconds) -> {
            if (seconds == 55) showShutdownWarning();
            else if (minutes == 1) System.exit(0);
        });

        timer.start();
        timer.go();

        drawingCanvas = new DrawingCanvas(timer, null);
        dataDisplay = new ShowData(drawingCanvas);
        MouseController mouseController = new MouseController(timer, drawingCanvas);
        drawingCanvas.addMouseListener(mouseController);
        drawingCanvas.addMouseMotionListener(mouseController);

        setLayout(new BorderLayout());
        add(drawingCanvas, BorderLayout.CENTER);

        setMenuBar(createMenuBar());
        setTitle("Coordinate Canvas");
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.end();
                dispose();
                System.exit(0);
            }
        });
    }

    private void showShutdownWarning() {
        EventQueue.invokeLater(() -> {
            Dialog warningDialog = new Dialog(this, "WARNING", true);
            Panel content = new Panel(new BorderLayout());
            content.setPreferredSize(new Dimension(300, 150));
            
            Label msgLabel1 = new Label("Application will close in 5 seconds", Label.CENTER);
            Label msgLabel2 = new Label("Do you want to continue working?", Label.CENTER);
            
            Panel textPanel = new Panel(new GridLayout(2, 1));
            textPanel.add(msgLabel1);
            textPanel.add(msgLabel2);
            
            Panel buttonPanel = new Panel(new FlowLayout());
            Button okButton = new Button("OK");
            Button noButton = new Button("NO");
            
            okButton.addActionListener(e -> { 
                warningDialog.dispose(); 
                timer.reset(); 
            });
            noButton.addActionListener(e -> System.exit(0));
            
            buttonPanel.add(okButton);
            buttonPanel.add(noButton);
            
            content.add(textPanel, BorderLayout.CENTER);
            content.add(buttonPanel, BorderLayout.SOUTH);
            
            warningDialog.add(content);
            warningDialog.pack();
            warningDialog.setLocationRelativeTo(this);
            warningDialog.setVisible(true);
        });
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = createFileMenu();
        Menu simMenu = createSimMenu();
        menuBar.add(fileMenu);
        menuBar.add(simMenu);
        return menuBar;
    }

    private Menu createFileMenu() {
        Menu menu = new Menu("File");

        MenuItem importFiles = new MenuItem("Import files");
        importFiles.addActionListener(e -> {
            new FileInput(new Frame());
            drawingCanvas.repaint();
            dataDisplay.showAirports(timer);
            timer.reset();
        });

        MenuItem saveFile = new MenuItem("Save data to file");
        saveFile.addActionListener(e -> {
            new CreateFile(new Frame());
            timer.reset();
        });

        MenuItem addAirport = new MenuItem("Add airport");
        addAirport.addActionListener(e -> {
            new AirportDataInput(new Frame());
            drawingCanvas.repaint();
            dataDisplay.refreshAirports(timer);
            timer.reset();
        });

        MenuItem addFlight = new MenuItem("Add flight");
        addFlight.addActionListener(e -> {
            new FlightDataInput(new Frame());
            drawingCanvas.repaint();
            timer.reset();
        });

        MenuItem showAirports = new MenuItem("Show all airports");
        showAirports.addActionListener(e -> {
            dataDisplay.showAirports(timer);
            timer.reset();
        });

        MenuItem showFlights = new MenuItem("Show all flights");
        showFlights.addActionListener(e -> {
        	new ShowData(drawingCanvas).showFlights(timer);
            timer.reset();
        });

        menu.add(importFiles);
        menu.add(saveFile);
        menu.add(addAirport);
        menu.add(addFlight);
        menu.addSeparator(); // Dodata linija razdvajanja radi preglednosti
        menu.add(showAirports);
        menu.add(showFlights);

        return menu;
    }

    private Menu createSimMenu() {
        Menu menu = new Menu("Simulation");

        MenuItem startSim = new MenuItem("Start simulation");
        startSim.addActionListener(e -> {
            if (Flight.allFlights.isEmpty()) {
                PopupMsg.showMsg("No imported flights data. Import them before starting simulation.");
                return;
            }
            if (isSimulationRunning) {
                PopupMsg.showMsg("Simulation already running!");
                return;
            }

            simulationController.start(drawingCanvas, drawingCanvas::repaint);
            timer.pause();
            isSimulationRunning = true;
        });

        MenuItem pauseSim = new MenuItem("Stop simulation");
        pauseSim.addActionListener(e -> {
            if (!isSimulationRunning) {
                PopupMsg.showMsg("Start simulation first.");
                return;
            }
            simulationController.pause();
            timer.go();
            isSimulationRunning = false;
        });

        MenuItem resumeSim = new MenuItem("Continue simulation");
        resumeSim.addActionListener(e -> {
            simulationController.resume();
            isSimulationRunning = true;
            timer.pause();
        });

        MenuItem restartSim = new MenuItem("Restart simulation");
        restartSim.addActionListener(e -> {
            simulationController.restart(drawingCanvas, drawingCanvas::repaint);
            timer.go();
        });

        MenuItem stopSim = new MenuItem("End simulation");
        stopSim.addActionListener(e -> {
            simulationController.stop();
            timer.go();
            isSimulationRunning = false;
            drawingCanvas.repaint();
            PopupMsg.showMsg("Simulation ended successfully.");
        });

        menu.add(startSim);
        menu.add(pauseSim);
        menu.add(resumeSim);
        menu.add(restartSim);
        menu.add(stopSim);

        return menu;
    }

    public static void main(String[] args) {
        new Scene().setVisible(true);
    }
}