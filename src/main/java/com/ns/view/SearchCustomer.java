package com.ns.view;

import com.ns.CustomerModel;
import com.ns.CustomerService;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class SearchCustomer extends JDialog {

    private CustomerService customerService;
    @Getter
    private CustomerModel selectedCustomer;
    private List<CustomerModel> searchedCustomer;

    private JPanel contentPane;
    private JPanel panelSearch;
    private JTextField tfCustomerSearch;
    private JButton btnShowAll;
    private JTable tbCustomer;

    private String[] columnNames = {"cust_id","cust_name","cust_address","cust_phone","cust_fax","cust_email"};
    private DefaultTableModel tableModel;

    public SearchCustomer(JFrame parent,CustomerService customerService) {
        super(parent, "Search Customer", true);
        this.customerService = customerService;
        setContentPane(contentPane);
        setModal(true);
        pack();
        tableModel = new DefaultTableModel(columnNames,0);
        tbCustomer.setModel(tableModel);

        setWidthAsPercentages(tbCustomer, 0.06, 0.3, 0.3, 0.05,0.05,0.05);


        tfCustomerSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                searchCustomers();
            }
        });

        tbCustomer.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tbCustomer.getSelectedRow();
                if (selectedRow != -1) {
                    selectedCustomer = searchedCustomer.get(selectedRow);
                    returnSelectedCustomer();
                }
            }
        });

    }

    private void searchCustomers(){
        String name = tfCustomerSearch.getText().trim();
        if (!name.isEmpty()) {
           searchedCustomer = customerService.searchCustomerByName(name);
            displaySearchResults(searchedCustomer);
        }
    }

    private void displaySearchResults(List<CustomerModel> customers) {
        DefaultTableModel tableModel = (DefaultTableModel) tbCustomer.getModel();
        tableModel.setRowCount(0); // Clear previous rows

        for (CustomerModel customer : customers) {
            Object[] row = {
                    customer.getCust_id(),
                    customer.getCust_name(),
                    customer.getCust_address(),
                    customer.getCust_phone(),
                    customer.getCust_fax(),
                    customer.getCust_email()
            };
            tableModel.addRow(row);
        }
    }

    private void returnSelectedCustomer() {
        if (selectedCustomer != null) {
            dispose(); // Close dialog
        }
    }

    private static void setWidthAsPercentages(JTable table,
                                              double... percentages) {
        final double factor = 10000;

        TableColumnModel model = table.getColumnModel();
        for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
            TableColumn column = model.getColumn(columnIndex);
            column.setPreferredWidth((int) (percentages[columnIndex] * factor));
        }
    }

}
