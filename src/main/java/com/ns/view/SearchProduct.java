package com.ns.view;

import com.ns.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class SearchProduct extends JDialog {

    private ProductService productService;
    private BrandService brandService;

    @Getter @Setter
    private ProductModel selectedProduct;
    @Getter
    private List<BrandModel> allBrand;
    @Getter @Setter
    private BrandModel selectedBrand;
    private List<ProductModel> searchedProduct;
    private Map<String,String> brandMap;

    private String[] columnNames = {"pro_id","pro_name","pro_brand","pro_model","pro_buyprice"};
    private DefaultTableModel tableModel;

    private JPanel contentPane;
    private JPanel panelSearch;
    private JComboBox cbProductType;
    private JTextField tfProductSearch;
    private JTable tbProduct;

    public SearchProduct(JFrame parent, ProductService productService,BrandService brandService) {
        super(parent, "Search Product", true);

        this.productService = productService;
        this.brandService= brandService;

        setContentPane(contentPane);
        setModal(true);
        pack();

        tableModel = new DefaultTableModel(columnNames,0);
        tbProduct.setModel(tableModel);
        setWidthAsPercentages(tbProduct, 0.06, 0.15, 0.3, 0.05,0.05);
        allBrand = brandService.getAllBrand();
        brandMap = brandService.getAsMap(allBrand);
        fillCombobox();

        tbProduct.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tbProduct.getSelectedRow();
                if (selectedRow != -1) {
                    selectedProduct = searchedProduct.get(selectedRow);
                    returnSelectedProduct();
                }
            }
        });

        tfProductSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                searchProduct();
            }
        });


        cbProductType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedBrand = allBrand.get(cbProductType.getSelectedIndex());
                searchedProduct = productService.searchProductByBrandId(selectedBrand.getBrand_id());
                displaySearchResults(searchedProduct);
            }
        });
    }



    private void fillCombobox(){
        for(BrandModel brandModel:allBrand){
            cbProductType.addItem(brandModel.getBrand_name());
        }
    }

    private void searchProduct(){
        String name = tfProductSearch.getText().trim();
        if (!name.isEmpty()) {
            searchedProduct = productService.searchProductByName(name);
            displaySearchResults(searchedProduct);
        }
    }

    private void returnSelectedProduct() {
        if (selectedProduct != null) {
            dispose(); // Close dialog
        }
    }

    private void displaySearchResults(List<ProductModel> productModels) {
        DefaultTableModel tableModel = (DefaultTableModel) tbProduct.getModel();
        tableModel.setRowCount(0); // Clear previous rows

        for (ProductModel productModel : productModels) {
            Object[] row = {
                    productModel.getPro_id(),
                    productModel.getPro_name(),
                    brandMap.get(productModel.getBrand_id()),
                    productModel.getPro_model(),
                    productModel.getPro_buyprice()
            };
            tableModel.addRow(row);
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
