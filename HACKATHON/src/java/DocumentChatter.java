import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class DocumentChatter extends JFrame {
    private JTextField filePath;
    private JTextField question;
    private JButton executeButton;
    private static JTextArea executionOutput;

    public DocumentChatter() {
        super("Document Chatter");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        filePath = new JTextField(20);
        question = new JTextField(20);
        executeButton = new JButton("Answer");
        executionOutput = new JTextArea(10, 30);
        executionOutput.setEditable(false);

        setupLayout();

        setupActionListeners();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setupLayout() {
        this.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Input"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("File Location:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(filePath, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Question:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(question, gbc);

        executeButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.CENTER;
        formPanel.add(executeButton, gbc);

        this.add(formPanel, BorderLayout.NORTH);

        executionOutput.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(executionOutput);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                scrollPane.getBorder()));

        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void setupActionListeners() {
        filePath.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int option = fileChooser.showOpenDialog(null);

                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = fileChooser.getSelectedFile();
                    filePath.setText(selectedFolder.getAbsolutePath());
                }
            }
        });

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = filePath.getText();
                String password = new String(question.getText());
                executePythonScript(location, password);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DocumentChatter::new);
    }

    private static String findPythonPath() {
        String[] commands = {"/bin/bash", "-c", "which python || which python3"};
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line != null && !line.isEmpty()) {
                    return line;
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error finding Python path");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "python";
    }

    private static void executePythonScript(String location, String question) {
        try {
            String scriptName = "DocumentChatterScript.py";
            File tempScript = extractScriptFromJar(scriptName);
            tempScript.setExecutable(true);

            String pythonPath = findPythonPath();

            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, tempScript.getAbsolutePath(), location, question);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                executionOutput.append(line + "\n");
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                executionOutput.append("Script execution failed with exit code: " + exitCode + "\n");
            }
        } catch (InterruptedException | IOException e) {
            executionOutput.append(e.getMessage());
        }
    }

    private static File extractScriptFromJar(String scriptName) throws IOException {
        InputStream inputStream = DocumentChatter.class.getResourceAsStream("/" + scriptName);
        if (inputStream == null) {
            throw new IOException("Script file not found in JAR: " + scriptName);
        }

        File tempFile = File.createTempFile("DocumentChatterScript", ".py");
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }

}