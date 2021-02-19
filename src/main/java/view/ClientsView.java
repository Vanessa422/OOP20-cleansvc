package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import controller.Company;
import controller.CompanyImpl;
import controller.backupFile.SaveAndLoadClients;
import model.users.Clients;
import model.users.ClientsImpl;

public class ClientsView extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 3375687914483476432L;
    private static final String TITLE ="CLEAN SERVICE MANAGER";
    
    private JTextField txtCFPIVA;
    private JTextField txtName;
    private JTextField txtAddress;
    private JTextField txtCity;
    private JTextField txtCAP;
    private JTextField txtMq;
    private JTextField txtTel;
    private JTextField txtEmail;
    private JTextField txtSearch;
    private final JButton btnSearch;
    private final JButton btnSubmit;
    private final JButton btnChange;
    private final JButton btnRemove;
    private final JButton btnHome;
    private Company company = CompanyImpl.getInstance();
    /*
     * testing:
     */
    //List<Clients> clientsList = new ArrayList<>();
    private final String[] cols = new String[] {"Nome", "Indirizzo", "Città", "CAP", "Struttura_mq", "Telefono", "Email", "CF_PIVA"};
    private Object[][] data = new Object[company.getClients().size()][cols.length];
    private DefaultTableModel model = new DefaultTableModel(data,cols);
    private JTable table = new JTable(model);
    
    private PopUp popUp = new PopUp();

    public ClientsView() {
        
        setTitle(ClientsView.TITLE);
        setMinimumSize(new Dimension(1200, 500));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel panelTable = new JPanel();
        panelTable.setMinimumSize(new Dimension(1000, 200));
        panelTable.setBackground(SystemColor.activeCaption);
        panelTable.setLayout(new BorderLayout(0, 0));
        
        JPanel panelTitle = new JPanel();
        panelTitle.setMinimumSize(new Dimension(1000, 60));
        panelTitle.setBackground(SystemColor.activeCaption);
        panelTitle.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Elenco clienti");
        lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
        lblTitle.setForeground(SystemColor.textText);
        lblTitle.setFont(new Font("Trebuchet MS", Font.CENTER_BASELINE,20));
        panelTitle.add(lblTitle, BorderLayout.WEST);
        
        btnHome = new JButton("BACK HOME");
        btnHome.setForeground(SystemColor.textText);
        btnHome.setBackground(SystemColor.activeCaption);
        btnHome.setPreferredSize(new Dimension(120,20));
        btnHome.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        btnHome.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                HomeView cv = new HomeView();
                cv.display();
                dispose();
            }
            
        });
        panelTitle.add(btnHome, BorderLayout.EAST);
        panelTable.add(panelTitle, BorderLayout.NORTH);
        
        /*
         * testing:
         *
         *
         * clientsList.add(new ClientsImpl("a", "b", "c", "d", "f", "f", "f", 22));
         * clientsList.add(new ClientsImpl("b", "b", "b", "b", "f", "f", "f", 22));
         */
       
        Clients cc;
         for (int i = 0; i < company.getClients().size(); i++) {
             cc = company.getClients().get(i);
             model.insertRow(i, new Object[] {cc.getName(),cc.getAddress(),cc.getCity(),cc.getCAP(),cc.getMqStructure(),cc.getTel(),cc.getEmail(),cc.getCFPIVA()});
         }
         
        table.setPreferredScrollableViewportSize(new Dimension(1000, 200));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true); //sort by the column header clicked
        panelTable.add(table,BorderLayout.CENTER);
        panelTable.add(new JScrollPane(table));

        final JPanel pnlSearch = new JPanel();
        pnlSearch.setBorder(new TitledBorder(null, "Recupera dati clienti", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaption));
        pnlSearch.setBackground(SystemColor.window);
        pnlSearch.setPreferredSize(new Dimension(1000, 40));
        pnlSearch.setMinimumSize(new Dimension(1000, 40));
        
        JLabel lblsearchCFPIVA = new JLabel("CF/P.IVA:");
        lblsearchCFPIVA.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlSearch.add(lblsearchCFPIVA);

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlSearch.add(txtSearch);
        
        btnSearch = new JButton("Estrai dati");
        btnSearch.setForeground(SystemColor.textText);
        btnSearch.setBackground(SystemColor.activeCaption);
        btnSearch.setPreferredSize(new Dimension(120,20));
        btnSearch.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        btnSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Optional<Clients> c = company.searchClient(getSearchingCFPIVA());
                if (c.isEmpty()) {
                    popUp.popUpWarning("Cliente non trovato!");
                } else {
                    writeField(c.get());
                    txtSearch.setText("");
                }
            }
        });
        pnlSearch.add(btnSearch);
        
        
        final JPanel pnlSubmit = new JPanel();
        pnlSubmit.setBorder(new TitledBorder(null, "Dati nuovo cliente", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaption));
        pnlSubmit.setBackground(SystemColor.window);
        pnlSubmit.setPreferredSize(new Dimension(900, 140));
        pnlSubmit.setMinimumSize(new Dimension(900, 140));
        pnlSubmit.setLayout(new BorderLayout(0,0));
        
        final JPanel pnlData = new JPanel();
        pnlData.setBorder(null);
        pnlData.setBackground(SystemColor.window);
        pnlData.setPreferredSize(new Dimension(900, 60));
        pnlData.setMinimumSize(new Dimension(900, 60));
        pnlData.setLayout(new GridLayout(4,4,20,2));
        
        JLabel labelCFPIVA = new JLabel("CF/P.IVA:");
        labelCFPIVA.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelCFPIVA);
        
        txtCFPIVA = new JTextField(20);
        txtCFPIVA.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtCFPIVA);
        
        JLabel labelName = new JLabel("Nome:");
        labelName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelName);
        
        txtName = new JTextField(10);
        txtName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtName);
        
        JLabel labelAddress = new JLabel("Indirizzo:");
        labelAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelAddress);
        
        txtAddress = new JTextField(20);
        txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtAddress);
        
        JLabel labelCity = new JLabel("Città:");
        labelCity.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelCity);
        
        txtCity = new JTextField(10);
        txtCity.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtCity);
        
        JLabel labelCAP = new JLabel("CAP:");
        labelCAP.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelCAP);
        
        txtCAP = new JTextField(5);
        txtCAP.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtCAP);
        
        JLabel labelmq = new JLabel("Struttura (mq):");
        labelmq.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelmq);
        
        txtMq = new JTextField(5);
        txtMq.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtMq);
        
        JLabel labeltel = new JLabel("Telefono:");
        labeltel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labeltel);
        
        txtTel = new JTextField(10);
        txtTel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtTel);
        
        JLabel labelemail = new JLabel("Email:");
        labelemail.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelemail);
        
        txtEmail = new JTextField(15);
        txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtEmail);
        pnlSubmit.add(pnlData, BorderLayout.CENTER);
        
        final JPanel pnlButtons = new JPanel();
        pnlButtons.setBackground(SystemColor.window);
        pnlButtons.setBorder(null);
        pnlButtons.setPreferredSize(new Dimension(900, 30));
        pnlButtons.setMinimumSize(new Dimension(900, 30));
        pnlButtons.setLayout(new GridLayout(1,3,20,20));
        
        btnSubmit = new JButton("Inserisci nuovo");
        btnSubmit.setForeground(SystemColor.textText);
        btnSubmit.setBackground(SystemColor.activeCaption);
        btnSubmit.setPreferredSize(new Dimension(120,20));
        btnSubmit.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        btnSubmit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!missingField()){
                    Clients c = new ClientsImpl(getCFPIVA(), getName(), getAddress(), getCity(), getCAP(), getTel(), getEmail(),  getMq());
                    if (company.searchClient(c.getCFPIVA()).isEmpty()) {
                        popUp.popUpInfo("Cliente inserito con successo.");
                        company.addClient(c);
                        addClientToTable(company.getClients().get(company.getClients().size()-1));
                        clearInsertField();
                    } else {
                        popUp.popUpError("Cliente già esistente!");
                    }
                } else {
                    popUp.popUpWarning("Ci sono dati mancanti o errati.");
                }
            }
        });
        pnlButtons.add(btnSubmit);
        
        btnChange = new JButton("Modifica esistente");
        btnChange.setForeground(SystemColor.textText);
        btnChange.setBackground(SystemColor.activeCaption);
        btnChange.setPreferredSize(new Dimension(120,20));
        btnChange.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        btnChange.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Clients changed = new ClientsImpl(getCFPIVA(), getName(), getAddress(), getCity(), getCAP(), getTel(), getEmail(),  getMq());
                if (!missingField()) {
                    Optional<Clients> toModify = company.searchClient(changed.getCFPIVA());
                    if (toModify.isEmpty()) {
                        popUp.popUpWarning("Codice Fiscale o Partita IVA inesistente tra i clienti!");
                    } else {
                        popUp.popUpInfo("Cliente modificato con successo.");
                        company.removeClient(toModify.get());
                        removeClientToTable(toModify.get());
                        company.addClient(changed);
                        addClientToTable(changed);
                        clearInsertField();
                    }
                } else {
                    popUp.popUpWarning("Ci sono dati mancanti o errati!");
                }
            }
        });
        pnlButtons.add(btnChange);
        
        btnRemove = new JButton("Elimina");
        btnRemove.setForeground(SystemColor.textText);
        btnRemove.setBackground(SystemColor.activeCaption);
        btnRemove.setPreferredSize(new Dimension(120,20));
        btnRemove.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (getCFPIVA().isEmpty()) {
                    popUp.popUpWarning("Nessun cliente specificato");
                } else {
                    Optional<Clients> clientToRemove = company.searchClient(getCFPIVA());
                    if (clientToRemove.isEmpty()) {
                        popUp.popUpWarning("Cliente non trovato");
                    } else {
                        Boolean confirm = popUp.popUpConfirm("Vuoi eliminare il cliente " + clientToRemove.get().getName() + "?");
                        if (confirm) {
                            popUp.popUpInfo("Cliente eliminato con successo.");
                            company.removeClient(clientToRemove.get());
                            removeClientToTable(clientToRemove.get());
                            clearInsertField();
                        } else {
                            popUp.popUpInfo("Eliminazione annullata.");
                        }
                    }
                }
            }
        });
        pnlButtons.add(btnRemove);
        pnlSubmit.add(pnlButtons, BorderLayout.SOUTH);

        
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(0)
                .addComponent(panelTable)
                .addGap(0)
                .addComponent(pnlSearch)
                .addGap(0)
                .addComponent(pnlSubmit)
                .addGap(0));
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(0)
                .addGroup(layout.createParallelGroup(Alignment.CENTER)
                        .addComponent(panelTable)
                        .addComponent(pnlSearch)
                        .addComponent(pnlSubmit))
                .addGap(0));
        
    }

    public void clearInsertField() {
        txtCFPIVA.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtCAP.setText("");
        txtMq.setText("");
        txtTel.setText("");
        txtEmail.setText("");
    }
    
    public void writeField(Clients c) {
        txtCFPIVA.setText(c.getCFPIVA());
        txtName.setText(c.getName());
        txtAddress.setText(c.getAddress());
        txtCity.setText(c.getCity());
        txtCAP.setText(c.getCAP());
        txtMq.setText(String.valueOf(c.getMqStructure()));
        txtTel.setText(c.getTel());
        txtEmail.setText(c.getEmail());
    }
    
    public Boolean missingField() {
        return (getCFPIVA().isEmpty() || getName().isEmpty() || getAddress().isEmpty() || getCity().isEmpty() || getCAP().isEmpty() || String.valueOf(getMq()).isEmpty());
    }
    
    public void addClientToTable(Clients c) {
        model.insertRow(company.getClients().size()-1, new Object[] {c.getName(), c.getAddress(), c.getCity(), c.getCAP(), c.getMqStructure(), c.getTel(), c.getEmail(), c.getCFPIVA()});
    }
    
    public void removeClientToTable(Clients c) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getDataVector().elementAt(i).elementAt(7).equals(c.getCFPIVA())) {
                model.removeRow(i);
            }
        }
    }
    
    public String getSearchingCFPIVA() {
        return txtSearch.getText();
    }
    
    public String getCFPIVA() {
        return txtCFPIVA.getText();
    }
    
    public String getName() {
        return txtName.getText();
    }
    
    public String getAddress() {
        return txtAddress.getText();
    }
    
    public String getCity() {
        return txtCity.getText();
    }
    
    public String getCAP() {
        return txtCAP.getText();
    }
    
    public int getMq() {
        return Integer.parseInt(txtMq.getText());
    }
    
    public String getTel() {
        return txtTel.getText();
    }
    
    public String getEmail() {
        return txtEmail.getText();
    }

    public void display() {
        setVisible(true);
        setResizable(true);
    }
}
