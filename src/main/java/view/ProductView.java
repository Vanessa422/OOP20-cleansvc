package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import controller.Company;
import controller.CompanyImpl;
import model.Products;
import model.ProductsImpl;
import model.users.Clients;
import model.users.ClientsImpl;

public class ProductView extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 3438738368807932420L;
    private static final String TITLE = "CLEAN SERVICE MANAGER";
    
    private final JButton btnHome;
    private JTextField txtSearch;
    private final JButton btnSearch;
    private JTextField txtCode;
    private JTextField txtStep;
    private JTextField txtName;
    private JTextField txtDescr;
    private JTextField txtPrice;
    private JTextField txtUsage;
    private final JButton btnSubmit;
    private final JButton btnChange;
    private final JButton btnRemove;
    private Company company = CompanyImpl.getInstance();
    private PopUp popUp = new PopUp();
    private final String[] cols = new String[] {"Codice", "Nome", "Descrizione", "Prezzo/Litro", "Utilizzo L/500mq", "Fase sanificazione"};
    private Object[][] data = new Object[company.getProducts().size()][cols.length];
    private DefaultTableModel model = new DefaultTableModel(data,cols);
    private JTable table = new JTable(model);
    
    public ProductView() {
        
        setTitle(ProductView.TITLE);
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
        
        JLabel lblTitle = new JLabel("Elenco prodotti");
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
         * test
         *
         * productsList.add(new ProductsImpl("a", "b", 3.3, 22));
         * productsList.add(new ProductsImpl("b", "b", 5.5, 22));
         * final String[] cols = new String[] {"Nome", "Descrizione", "Prezzo/Litro", "Utilizzo L/500mq"};
         * Object[][] data = new Object[productsList.size()][cols.length];
         *
         * for (int i = 0; i < productsList.size(); i++) {
         *    data[i][0] = productsList.get(i).getName();
         *    data[i][1] = productsList.get(i).getDescription();
         *    data[i][2] = String.valueOf(productsList.get(i).getPricePerLitre());
         *    data[i][3] = String.valueOf(productsList.get(i).getLitersPer500Mq());
         * }
         */

        Products p;
        for (int i = 0; i < company.getProducts().size(); i++) {
            p = company.getProducts().get(i);
            model.insertRow(i, new Object[] {p.getCode(), p.getName(), p.getDescription(), p.getPricePerLitre(), p.getLitersPer500Mq(), p.getStepType()});
        }
        
        table.setPreferredScrollableViewportSize(new Dimension(1000, 200));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true); //sort by the column header clicked
        panelTable.add(table,BorderLayout.CENTER);
        panelTable.add(new JScrollPane(table));

        final JPanel pnlSearch = new JPanel();
        pnlSearch.setBorder(new TitledBorder(null, "Recupera dati prodotto", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaption));
        pnlSearch.setBackground(SystemColor.window);
        pnlSearch.setPreferredSize(new Dimension(1000, 50));
        pnlSearch.setMinimumSize(new Dimension(1000, 50));
        
        JLabel lblsearch = new JLabel("Codice:"); //TODO list box of existing step
        lblsearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlSearch.add(lblsearch);

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
                Optional<Products> p = company.searchProduct(getSearching());
                if (p.isEmpty()) {
                    popUp.popUpWarning("Prodotto non trovato!");
                } else {
                    writeField(p.get());
                    txtSearch.setText("");
                }
            }
        });
        pnlSearch.add(btnSearch);

        final JPanel pnlSubmit = new JPanel();
        pnlSubmit.setBorder(new TitledBorder(null, "Dati prodotto", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaption));
        pnlSubmit.setBackground(SystemColor.window);
        pnlSubmit.setPreferredSize(new Dimension(900, 120));
        pnlSubmit.setMinimumSize(new Dimension(900, 120));
        pnlSubmit.setLayout(new BorderLayout(0,0));
        
        final JPanel pnlData = new JPanel();
        pnlData.setBorder(null);
        pnlData.setBackground(SystemColor.window);
        pnlData.setPreferredSize(new Dimension(900, 60));
        pnlData.setMinimumSize(new Dimension(900, 60));
        pnlData.setLayout(new GridLayout(3,2,20,5));
        
        JLabel labelCode = new JLabel("Codice:");
        labelCode.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelCode);
        
        txtCode = new JTextField(15);
        txtCode.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtCode);
        
        JLabel labelStep = new JLabel("Step:");
        labelStep.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelStep);
        
        txtStep = new JTextField(15);
        txtStep.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtStep);
        
        JLabel labelName = new JLabel("Nome:");
        labelName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelName);
        
        txtName = new JTextField(15);
        txtName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtName);

        JLabel labelDescr = new JLabel("Descrizione:");
        labelDescr.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelDescr);
        
        txtDescr = new JTextField(15);
        txtDescr.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtDescr);
        
        JLabel labelPrice = new JLabel("Prezzo al litro:");
        labelPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelPrice);
        
        txtPrice = new JTextField(15);
        txtPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtPrice);
        
        JLabel labelUsage = new JLabel("Utilizzo L/500mq:");
        labelUsage.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(labelUsage);
        
        txtUsage = new JTextField(15);
        txtUsage.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pnlData.add(txtUsage);
        pnlSubmit.add(pnlData, BorderLayout.CENTER);
        
        final JPanel pnlButtons = new JPanel();
        pnlButtons.setBackground(SystemColor.window);
        pnlButtons.setBorder(null);
        pnlButtons.setPreferredSize(new Dimension(900, 30));
        pnlButtons.setMinimumSize(new Dimension(900, 30));
        pnlButtons.setLayout(new GridLayout(1,2,20,5));
        
        btnSubmit = new JButton("Inserisci nuovo");
        btnSubmit.setForeground(SystemColor.textText);
        btnSubmit.setBackground(SystemColor.activeCaption);
        btnSubmit.setPreferredSize(new Dimension(120,20));
        btnSubmit.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        btnSubmit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!missingField()) {
                    Products p = new ProductsImpl(getCode(), getStep(), getName(), getDescription(), getPrice(), getUsage());
                    if (company.searchProduct(p.getCode()).isEmpty()) {
                        popUp.popUpInfo("Prodotto inserito con successo.");
                        company.addProduct(p);
                        addProductToTable(company.getProducts().get(company.getProducts().size()-1));
                        clearInsertField();
                    } else {
                        popUp.popUpError("Prodotto già esistente!");
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
        btnChange.setPreferredSize(new Dimension(200,20));
        btnChange.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        btnChange.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Products changed = new ProductsImpl(getCode(), getStep(), getName(), getDescription(), getPrice(), getUsage());
                if (!missingField()) {
                    Optional<Products> toModify = company.searchProduct(changed.getCode());
                    if (toModify.isEmpty()) {
                        popUp.popUpWarning("Codice inesistente tra i prodotti.");
                    } else {
                        popUp.popUpInfo("Prodotto modificato con successo.");
                        company.removeProduct(toModify.get());
                        removeProductToTable(toModify.get());
                        company.addProduct(changed);
                        addProductToTable(changed);
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
        btnRemove.setPreferredSize(new Dimension(200,20));
        btnRemove.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (getCode().isEmpty()) {
                    popUp.popUpWarning("Nessun codice prodotto specificato");
                } else {
                    Optional<Products> productToRemove = company.searchProduct(getCode());
                    if (productToRemove.isEmpty()) {
                        popUp.popUpWarning("Prodotto non trovato!");
                    } else {
                        Boolean confirmed = popUp.popUpConfirm("Vuoi eliminare il prodotto " + productToRemove.get().getName() + "?");
                        if (confirmed) {
                            popUp.popUpInfo("Prodotto eliminato con successo.");
                            company.removeProduct(productToRemove.get());
                            removeProductToTable(productToRemove.get());
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

    /**
     * method the returns text.
     * @return the text.
     */
    public String getSearching() {
        return txtSearch.getText();
    }
    /**
     * 
     * @param p
     */
    public  void writeField(Products p) {
        txtCode.setText(p.getCode());
        txtStep.setText(p.getStepType());
        txtName.setText(p.getName());
        txtDescr.setText(p.getDescription());
        txtPrice.setText(String.valueOf(p.getPricePerLitre()));
        txtUsage.setText(String.valueOf(p.getLitersPer500Mq()));
    }
    /**
     * 
     */
    public void clearInsertField() {
        txtCode.setText("");
        txtStep.setText("");
        txtName.setText("");
        txtDescr.setText("");
        txtPrice.setText("");
        txtUsage.setText("");
    }
    /**
     * 
     * @return
     */
    public Boolean missingField() {
        return (getCode().isEmpty() || getStep().isEmpty() || getName().isEmpty() || getDescription().isEmpty() || String.valueOf(getPrice()).isEmpty() || String.valueOf(getUsage()).isEmpty());
    }
    /**
     * 
     * @param p
     */
    public void addProductToTable(Products p) {
        model.insertRow(company.getProducts().size()-1, new Object[] {p.getCode(), p.getName(), p.getDescription(), p.getPricePerLitre(), p.getLitersPer500Mq(), p.getStepType()});
    }
    /**
     * 
     * @param p
     */
    public void removeProductToTable(final Products p) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getDataVector().elementAt(i).elementAt(0).equals(p.getCode())) {
                model.removeRow(i);
            }
        }
    }
    /**
     * 
     * @return
     */
    public String getCode() {
        return txtCode.getText();
    }
    /**
     * 
     * @return
     */
    public String getStep() {
        return txtStep.getText();
    }
    /**
     * 
     */
    public String getName() {
        return txtName.getText();
    }
    /**
     * 
     * @return
     */
    public String getDescription() {
        return txtDescr.getText();
    }
    
    /**
     * 
     * @return
     */
    public double getPrice() {
        return Double.parseDouble(txtPrice.getText());
    }
    
    /**
     * 
     * @return
     */
    public double getUsage() {
        return Double.parseDouble(txtUsage.getText());
    }
    
    /**
     * 
     */
    public void display() {
        setVisible(true);
        setResizable(true);
    }
    
}