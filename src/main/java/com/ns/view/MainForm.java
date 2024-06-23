package com.ns.view;

import com.ns.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainForm extends JFrame{
    private CustomerService customerService;
    private ProductService productService;
    private BrandService brandService;
    private SearchCustomer searchCustomer;
    private SearchProduct searchProduct;
    private final String[] columnNames = {"is_free","pro_id","pro_name","pro_brand","pro_model","unit_price","unit","total_price","discount"};
    private DefaultTableModel tableModel;
    private List<SaleProductModel> saleProductModels;
    private SaleCalculationModel saleCalculationModel;
    NumberFormat numberFormat = NumberFormat.getInstance();

    private JPanel mainPanel;
    private JTextField tfSaleNumber;
    private JTextField tfDate;
    private JTextField tfCustomerId;
    private JButton btnCustomerSelect;
    private JTextField tfCustomerName;
    private JButton btnSave;
    private JButton btnClear;
    private JTable tbProductSale;
    private JTextField tfProductId;
    private JButton btnProductSelect;
    private JTextField tfProductName;
    private JTextField tfProductType;
    private JTextField tfProductDetail;
    private JTextField tfProductUnitPrice;
    private JTextField tfProductUnit;
    private JButton btnAdd;
    private JCheckBox cbxIsFreeProduct;
    private JRadioButton radioCash;
    private JRadioButton radioDebtor;
    private JRadioButton radioBankTransfer;
    private JRadioButton radioCredit;
    private JTextField tfProductTotalPrice;
    private JTextField tfProductTotalDiscount;
    private JTextField tfProductTotalPriceBeforeTax;
    private JCheckBox includeTax7CheckBox;
    private JTextField tfTax;
    private JTextField tfTotalPrice;
    private JCheckBox cbxRoundNumber;
    private JPanel panelCustomer;
    private JPanel panelPaymentTerm;
    private JScrollPane scrollPanelTable;

    public MainForm() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();

        tableModel = new DefaultTableModel(columnNames,0){
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // First column is Boolean for checkboxes
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 0 || columnIndex == 5 || columnIndex == 6 || columnIndex == 8; // Only the first column (checkbox) is editable
            }
        };

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        tbProductSale.setModel(tableModel);


        tbProductSale.getColumnModel().getColumn(0).setCellRenderer(tbProductSale.getDefaultRenderer(Boolean.class));

        setWidthAsPercentages(tbProductSale,0.03,0.15,0.3,0.1,0.1,0.1,0.1,0.1,0.1);

        tbProductSale.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);
        tbProductSale.getColumnModel().getColumn(6).setCellRenderer(leftRenderer);
        tbProductSale.getColumnModel().getColumn(7).setCellRenderer(leftRenderer);
        tbProductSale.getColumnModel().getColumn(8).setCellRenderer(leftRenderer);

        JTextField textField = new JTextField();
        TableCellEditor cellEditor = new DefaultCellEditor(textField) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
                if (c instanceof JTextField) {
                    SwingUtilities.invokeLater(((JTextField) c)::selectAll);
                }
                return c;
            }
        };
        tbProductSale.setDefaultEditor(Object.class, cellEditor);


        btnCustomerSelect.addActionListener(e -> {
            searchCustomer.setVisible(true);
            displaySelectedCustomer();
        });

        initCustomerServiceInBackground();
        btnProductSelect.addActionListener(e -> {
            searchProduct.setVisible(true);
            ProductModel productModel = searchProduct.getSelectedProduct();
            if(productModel != null){
                displaySelectedProduct();
                tfProductUnit.requestFocus();
                tfProductUnit.selectAll();
            }


        });
        btnAdd.addActionListener(e -> {
            if(addProductToSale()){
                tfProductId.setText("");
                tfProductName.setText("");
                tfProductType.setText("");
                tfProductDetail.setText("");
                tfProductUnitPrice.setText("");
                tfProductUnit.setText("");
                searchProduct.setSelectedProduct(null);
            }

        });

        tbProductSale.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Point point = e.getPoint();
                int row = tbProductSale.rowAtPoint(point);
                int col = tbProductSale.columnAtPoint(point);
                if (e.getClickCount() == 2 && tbProductSale.getSelectedRow() != -1 && col != 0 && col !=5 && col !=6&& col !=7&& col !=8) {
                    // your valueChanged overridden method
                    int dialogResult = JOptionPane.showConfirmDialog (null, "Remove?","Warning",JOptionPane.YES_NO_OPTION);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        // Saving code here
                        saleProductModels.remove(row);
                        tableModel.removeRow(row);
                    }
                }


            }
        });

        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();

                    // Temporarily remove the listener to prevent infinite loop
                    tableModel.removeTableModelListener(this);

                    Object isFreeObj = tableModel.getValueAt(row, 0);
                    Object unitPriceObj = tableModel.getValueAt(row, 5);
                    Object unitObj = tableModel.getValueAt(row, 6);
                    Object discountObj = tableModel.getValueAt(row, 8);

                    boolean isFree = Boolean.parseBoolean(String.valueOf(isFreeObj));
                    double unitPrice = Double.parseDouble(String.valueOf(unitPriceObj));
                    double unit = Double.parseDouble(String.valueOf(unitObj));
                    double discount = Double.parseDouble(String.valueOf(discountObj));
                    changeSaleProductValue(row, isFree, unitPrice, unit, discount);
                    updateCellValueSaleProductTotalPrice(row);
                    updateCellValueSaleProductUnitPrice(row);

                    calculateSaleTotalSum();

                    // Re-add the listener after the update
                    tableModel.addTableModelListener(this);
                }
            }
        });

    }

    private void updateCellValueSaleProductUnitPrice(int row){
        tableModel.setValueAt(saleProductModels.get(row).getUnitPrice(),row,5);
    }

    private void updateCellValueSaleProductTotalPrice(int row){
        tableModel.setValueAt(saleProductModels.get(row).getTotalPrice(),row,7);
    }

    private void changeSaleProductValue(int row,boolean isFree,double unitPrice,double unit,double discount){

        if(isFree && !saleProductModels.get(row).isFree()){
            unitPrice = 0.0;
        }else if(!isFree && saleProductModels.get(row).isFree()){
            unitPrice = saleProductModels.get(row).getProductModel().getPro_buyprice();
        }
        saleProductModels.get(row).setFree(isFree);
        saleProductModels.get(row).setUnitPrice(unitPrice);
        saleProductModels.get(row).setUnit(unit);
        saleProductModels.get(row).setTotalPrice(
                round(saleProductModels.get(row).getUnit()*saleProductModels.get(row).getUnitPrice(),2)
        );
        saleProductModels.get(row).setDiscount(discount);
        saleProductModels.get(row).setVatPrice(
                round(Calculation.calculateVatFromPriceIncludeVat(saleProductModels.get(row).getTotalPrice()-saleProductModels.get(row).getDiscount()),2)
        );
        saleProductModels.get(row).setPriceBeforeVat(
                round(Calculation.calculatePriceNotIncludeVat(saleProductModels.get(row).getTotalPrice()-saleProductModels.get(row).getDiscount(),saleProductModels.get(row).getVatPrice()),2)
        );

    }

    private SaleProductModel createSaleProductModel(){
        if(searchProduct.getSelectedProduct()==null || searchProduct.getSelectedBrand()==null){
            return null;
        }

        if(tfProductUnitPrice.getText().isEmpty() || tfProductUnit.getText().isEmpty()){
            return null;
        }

        if(!isNumeric(tfProductUnitPrice.getText()) || !isNumeric(tfProductUnit.getText())){
            return null;
        }

        double unitPrice = Double.parseDouble(tfProductUnitPrice.getText());
        double unit = Double.parseDouble(tfProductUnit.getText());
        boolean isFree = cbxIsFreeProduct.isSelected();
        if(isFree){
            unitPrice = 0.0;
        }

        SaleProductModel saleProductModel = new SaleProductModel();
        saleProductModel.setProductModel(searchProduct.getSelectedProduct());
        saleProductModel.setBrandModel(searchProduct.getSelectedBrand());

        saleProductModel.setUnitPrice(unitPrice);
        saleProductModel.setUnit(unit);
        saleProductModel.setFree(isFree);
        saleProductModel.setDiscount(0.0);
        saleProductModel.setTotalPrice(round(unit*unitPrice,2));
        saleProductModel.setVatPrice(
                round(Calculation.calculateVatFromPriceIncludeVat(saleProductModel.getTotalPrice()),2)
        );
        saleProductModel.setPriceBeforeVat(
                round(Calculation.calculatePriceNotIncludeVat(saleProductModel.getTotalPrice(),saleProductModel.getVatPrice()),2)
        );

        return saleProductModel;
    }

    private boolean addProductToSale(){
        SaleProductModel saleProductModel = createSaleProductModel();
        if(saleProductModel==null){
            return false;
        }

        Object[] row = {
                saleProductModel.isFree(),
                saleProductModel.getProductModel().getPro_id(),
                saleProductModel.getProductModel().getPro_name(),
                saleProductModel.getBrandModel().getBrand_name(),
                saleProductModel.getProductModel().getPro_model(),
                saleProductModel.getUnitPrice(),
                saleProductModel.getUnit(),
                saleProductModel.getTotalPrice(),
                saleProductModel.getDiscount()
        };
        if(saleProductModels == null){
            saleProductModels = new ArrayList<>();
        }
        saleProductModels.add(saleProductModel);
        tableModel.addRow(row);

        calculateSaleTotalSum();

        return true;
    }

    private void calculateSaleTotalSum(){
        saleCalculationModel = new SaleCalculationModel();

        for(SaleProductModel saleProductModel:saleProductModels){
            saleCalculationModel.setTotalProductPrice(
                    round(saleCalculationModel.getTotalProductPrice()+saleProductModel.getTotalPrice(),2)
            );

            saleCalculationModel.setTotalDiscount(
                    round(saleCalculationModel.getTotalDiscount()+saleProductModel.getDiscount(),2)
            );

            saleCalculationModel.setTotalProductPriceBeforeVat(
                    round(saleCalculationModel.getTotalProductPriceBeforeVat()+saleProductModel.getPriceBeforeVat(),2)
            );

            saleCalculationModel.setTotalVat(
                    round(saleCalculationModel.getTotalVat()+saleProductModel.getVatPrice(),2)
            );
        }
        saleCalculationModel.setTotalPrice(
                round(saleCalculationModel.getTotalProductPrice()-saleCalculationModel.getTotalDiscount(),2)
        );
        displaySaleCalculation();

    }

    private void displaySaleCalculation(){
        tfProductTotalPrice.setText(numberFormat.format(saleCalculationModel.getTotalProductPrice()));
        tfProductTotalDiscount.setText(numberFormat.format(saleCalculationModel.getTotalDiscount()));
        tfProductTotalPriceBeforeTax.setText(numberFormat.format(saleCalculationModel.getTotalProductPriceBeforeVat()));
        tfTax.setText(numberFormat.format(saleCalculationModel.getTotalVat()));
        tfTotalPrice.setText(numberFormat.format(saleCalculationModel.getTotalPrice()));
    }

    private void displaySelectedCustomer(){
        if(searchCustomer.getSelectedCustomer() != null){
            tfCustomerId.setText(searchCustomer.getSelectedCustomer().getCust_id());
            tfCustomerName.setText(searchCustomer.getSelectedCustomer().getCust_name());
        }
    }

    private void displaySelectedProduct(){
        if(searchProduct.getSelectedProduct() != null){
            tfProductId.setText(searchProduct.getSelectedProduct().getPro_id());
            tfProductName.setText(searchProduct.getSelectedProduct().getPro_name());
            tfProductType.setText(searchProduct.getSelectedBrand().getBrand_name());
            tfProductDetail.setText(searchProduct.getSelectedProduct().getPro_model());
            tfProductUnitPrice.setText(String.valueOf(searchProduct.getSelectedProduct().getPro_buyprice()));
            tfProductUnit.setText(String.valueOf(1));
        }
    }

    private void initCustomerServiceInBackground() {
        // Show initializing dialog
        ProgressDialog progressDialog = new ProgressDialog(this, "Initializing, please wait...");
        progressDialog.setLocationRelativeTo(this);

        SwingWorker<CustomerService, Void> worker = new SwingWorker<>() {
            @Override
            protected CustomerService doInBackground() {
                // Simulate initialization process (replace with actual initialization)

                // Initialize customer service
                customerService = new CustomerService();
                productService = new ProductService();
                brandService = new BrandService();

                return customerService;
            }

            @Override
            protected void done() {
                // Close initializing dialog
                progressDialog.dispose();
                searchCustomer = new SearchCustomer(MainForm.this, customerService);
                searchProduct = new SearchProduct(MainForm.this, productService, brandService);
                // Enable search button after initialization completes
                getContentPane().getComponent(0).setEnabled(true); // Enable search button
                revalidate(); // Refresh the frame to reflect changes
            }
        };

        worker.execute(); // Execute the SwingWorker

        // Show progress dialog
        progressDialog.setVisible(true);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
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
