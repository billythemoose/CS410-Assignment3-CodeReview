import sun.nio.cs.StandardCharsets;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;

public class SimpleNotePad extends JFrame{
    JMenuBar uiMenuBar = new JMenuBar();
    JMenu uiMenuFile = new JMenu("File");
    JMenu uiMenuEdit = new JMenu("Edit");
    JMenu uiMenuRecent = new JMenu("Recent");
    JTextPane uiTextPane = new JTextPane();
    JMenuItem uiMenuNew = new JMenuItem("New File");
    JMenuItem uiMenuSave = new JMenuItem("Save File");
    JMenuItem uiMenuPrint = new JMenuItem("Print File");
    JMenuItem uiMenuCopy = new JMenuItem("Copy");
    JMenuItem uiMenuPaste = new JMenuItem("Paste");
    JMenuItem uiMenuOpen = new JMenuItem("Open");
    JMenuItem uiMenuReplace = new JMenuItem("Replace");

    Properties appProperties;

    // Notepad Initialization
    public SimpleNotePad(Properties configuration) {
        this.setTitle("A Simple Notepad Tool");

        appProperties = configuration;
        String config = appProperties.getProperty("recent");
        // Load recent files from config
        if (config != null && config.length() > 0) {

            String[] settings = config.split(",");
            for (String newItem : settings) {
                JMenuItem recentItem = new JMenuItem(newItem);
                recentItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            JMenuItem sourceItem = (JMenuItem)e.getSource();
                            FileReader file = new FileReader(sourceItem.getText());
                            String inputText = "";
                            int i;
                            while ((i=file.read()) != -1) {
                                inputText += (char)i;
                            }
                            uiTextPane.setText(inputText);
                            JOptionPane.showMessageDialog(null, "File loading complete");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Load error " + ex, "Load error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                uiMenuRecent.add(recentItem);
            }
        }

        uiMenuFile.add(uiMenuOpen);
        uiMenuFile.addSeparator();
        uiMenuFile.add(uiMenuNew);
        uiMenuFile.addSeparator();
        uiMenuFile.add(uiMenuSave);
        uiMenuFile.addSeparator();
        uiMenuFile.add(uiMenuPrint);
        uiMenuFile.addSeparator();
        uiMenuFile.add(uiMenuRecent);

        uiMenuEdit.add(uiMenuPaste);
        uiMenuEdit.addSeparator();
        uiMenuEdit.add(uiMenuCopy);
        uiMenuEdit.addSeparator();
        uiMenuEdit.add(uiMenuReplace);

        uiMenuOpen.addActionListener(this.actionMenuOpen());
        uiMenuOpen.setActionCommand("open");
        uiMenuNew.addActionListener(this.actionMenuNew());
        uiMenuNew.setActionCommand("new");
        uiMenuSave.addActionListener(this.actionMenuSave());
        uiMenuSave.setActionCommand("save");
        uiMenuPrint.addActionListener(this.actionMenuPrint());
        uiMenuPrint.setActionCommand("print");
        uiMenuCopy.addActionListener(this.actionMenuCopy());
        uiMenuCopy.setActionCommand("copy");
        uiMenuPaste.addActionListener(this.actionMenuPaste());
        uiMenuPaste.setActionCommand("paste");
        uiMenuReplace.addActionListener(this.actionMenuReplace());
        uiMenuReplace.setActionCommand("replace");

        uiMenuBar.add(uiMenuFile);
        uiMenuBar.add(uiMenuEdit);
        setJMenuBar(uiMenuBar);
        add(new JScrollPane(uiTextPane));
        setPreferredSize(new Dimension(600,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    // Main entry point for the application
    public static void main(String[] args) {
        // load config file
        Properties properties = new Properties();
        try {
            File configFile = new File("config.properties");
            FileReader reader = new FileReader(configFile);
            properties.load(reader);
        }
        catch (Exception e) {
            // create config file
            try {
                properties.setProperty("recent", "");
                File config = new File("config.properties");
                FileWriter write = new FileWriter(config);
                properties.store(write, "settings");
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Config load error " + ex, "Config load error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }


        SimpleNotePad app = new SimpleNotePad(properties);
    }

    private void UpdateRecents(String newRecent) {
        try {
            String propsList = appProperties.getProperty("recent");
            if (propsList.length() > 0) {
                String[] propsSplit = propsList.split(",");
                if (!newRecent.equals(propsSplit[0])) {
                    if (propsSplit.length > 4) {
                        String[] newProps = new String[5];
                        newProps[0] = newRecent;
                        newProps[1] = propsSplit[0];
                        newProps[2] = propsSplit[1];
                        newProps[3] = propsSplit[2];
                        newProps[4] = propsSplit[3];
                    } else {
                        String[] newPropsSplit = new String[propsSplit.length + 1];
                        for (int i = propsSplit.length; i > 0; i--) {
                            if (propsSplit[i-1].length() > 0) {
                                newPropsSplit[i] = propsSplit[i-1];
                            }
                        }

                        newPropsSplit[0] = newRecent;
                        propsSplit = newPropsSplit;
                    }

                    StringBuilder builder = new StringBuilder();

                    for (String s : propsSplit) {
                        builder.append(s).append(",");
                    }

                    propsList = builder.deleteCharAt(builder.length() - 1).toString();
                }
            }
            else {
                propsList = newRecent;
            }

            appProperties.setProperty("recent", propsList);
            File config = new File("config.properties");
            FileWriter write = new FileWriter(config);
            appProperties.store(write, "settings");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Config save error " + e, "Config save error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Replace action listener
    private ActionListener actionMenuReplace() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String replace = JOptionPane.showInputDialog("Replace or insert with:");
                uiTextPane.replaceSelection(replace);
            }
        };

        return action;
    }

    // Open action listener
    private ActionListener actionMenuOpen() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File fileToOpen = null;
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION)
                    fileToOpen = fc.getSelectedFile();
                try {
                    FileReader file = new FileReader(fileToOpen);
                    String inputText = "";
                    int i;
                    while ((i=file.read()) != -1) {
                        inputText += (char)i;
                    }
                    uiTextPane.setText(inputText);
                    UpdateRecents(fileToOpen.getPath());
                    JOptionPane.showMessageDialog(null, "File loading complete");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Load error " + ex, "Load error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        return action;
    }

    private ActionListener actionMenuRecent() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        };

        return action;
    }

    // New item action listener
    private ActionListener actionMenuNew() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uiTextPane.setText("");
            }
        };

        return action;
    }

    // Save action listener
    private ActionListener actionMenuSave() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File fileToWrite = null;
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION)
                    fileToWrite = fc.getSelectedFile();
                try {
                    PrintWriter out = new PrintWriter(new FileWriter(fileToWrite));
                    out.println(uiTextPane.getText());
                    JOptionPane.showMessageDialog(null, "File is saved successfully...");
                    out.close();
                    UpdateRecents(fileToWrite.getName());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Save error " + ex, "Save error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        return action;
    }

    // Print action listener
    private ActionListener actionMenuPrint() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    PrinterJob pjob = PrinterJob.getPrinterJob();
                    pjob.setJobName("Sample Command Pattern");
                    pjob.setCopies(1);
                    pjob.setPrintable(new Printable() {
                        public int print(Graphics pg, PageFormat pf, int pageNum) {
                            if (pageNum>0)
                                return Printable.NO_SUCH_PAGE;
                            pg.drawString(uiTextPane.getText(), 500, 500);
                            paint(pg);
                            return Printable.PAGE_EXISTS;
                        }
                    });

                    if (pjob.printDialog() == false)
                        return;
                    pjob.print();
                } catch (PrinterException pe) {
                    JOptionPane.showMessageDialog(null,
                            "Printer error " + pe, "Printing error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        return action;
    }

    // Copy action listener
    private ActionListener actionMenuCopy() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uiTextPane.copy();
            }
        };

        return action;
    }

    // Paste action listener
    private ActionListener actionMenuPaste() {
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StyledDocument doc = uiTextPane.getStyledDocument();
                Position position = doc.getEndPosition();
                System.out.println("offset"+position.getOffset());
                uiTextPane.paste();
            }
        };

        return action;
    }
}