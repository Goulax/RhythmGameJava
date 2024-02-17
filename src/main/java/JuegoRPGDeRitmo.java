import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class JuegoRPGDeRitmo extends JFrame implements ActionListener {
    private static final Random random = new Random();
    private static final Map<String, Integer> notasMidi = new HashMap<>();

    static {
        // Mapeo de notas a valores MIDI
        notasMidi.put("C", 60);
        notasMidi.put("D", 62);
        notasMidi.put("E", 64);
        notasMidi.put("F", 65);
        notasMidi.put("G", 67);
        notasMidi.put("A", 69);
        notasMidi.put("B", 71);
    }

    private String[] ritmo;
    private int tiempoActual = 0;
    private Synthesizer synthesizer;
    private MidiChannel midiChannel;

    private JButton botonReproducir;
    private JButton botonParar;
    private JLabel labelTiempo;
    private boolean reproduciendo = false;

    public JuegoRPGDeRitmo() {
        super("Juego RPG de Ritmo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicializar el sintetizador MIDI
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            midiChannel = synthesizer.getChannels()[0]; // Usamos el primer canal para el piano
            midiChannel.programChange(0); // Seleccionamos el instrumento (piano)
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }

        ritmo = generarRitmo();

        botonReproducir = new JButton("Reproducir");
        botonReproducir.addActionListener(this);

        botonParar = new JButton("Parar");
        botonParar.addActionListener(this);

        labelTiempo = new JLabel("Tiempo actual: 0");

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());
        panelBotones.add(botonReproducir);
        panelBotones.add(botonParar);

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new FlowLayout());
        panelInfo.add(labelTiempo);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(panelBotones);
        panel.add(panelInfo);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String[] generarRitmo() {
        int duracionTotal = random.nextInt(30) + 1; // Duración total aleatoria entre 1 y 30 tiempos
        String[] ritmo = new String[duracionTotal];
        for (int tiempo = 0; tiempo < duracionTotal; tiempo++) {
            int notaIndex = random.nextInt(notasMidi.size());
            ritmo[tiempo] = (String) notasMidi.keySet().toArray()[notaIndex];
        }
        return ritmo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonReproducir) {
            reproduciendo = true;
            reproducirNota();
        } else if (e.getSource() == botonParar) {
            reproduciendo = false;
        }
    }

    private void reproducirNota() {
        new Thread(() -> {
            while (reproduciendo) {
                String nota = ritmo[tiempoActual];
                System.out.println("Nota generada: " + nota);
                midiChannel.noteOn(notasMidi.get(nota), 100); // 100 es la velocidad (volumen) de la nota
                try {
                    Thread.sleep(500); // Duración de la nota en milisegundos (0.5 segundos)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                midiChannel.noteOff(notasMidi.get(nota));
                tiempoActual = (tiempoActual + 1) % ritmo.length;
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegoRPGDeRitmo());
    }
}
