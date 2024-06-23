package com.ns;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Map;

class ProductTableModel extends AbstractTableModel {

    private final List<ProductModel> products;
    private final Map<String, String> brandMap;
    private final String[] columnNames = {"is_free","pro_id","pro_name","pro_brand","pro_model","unit_price","unit","total_price","discount"};
    private final Boolean[] checked; // Boolean array to hold checkbox values

    public ProductTableModel(List<ProductModel> products, Map<String, String> brandMap) {
        this.products = products;
        this.brandMap = brandMap;
        this.checked = new Boolean[products.size()];
        for (int i = 0; i < checked.length; i++) {
            checked[i] = Boolean.FALSE; // Initialize all checkboxes to false
        }
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class; // First column is Boolean for checkboxes
        }
        return String.class; // Other columns are String or other types
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0; // Only the first column (checkbox) is editable
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductModel product = products.get(rowIndex);

        switch (columnIndex) {
            case 0: return checked[rowIndex];
            case 1: return product.getPro_id();
            case 2: return product.getPro_name();
            case 3: return brandMap.get(product.getBrand_id());
            case 4: return product.getPro_model();
            case 5: return product.getPro_buyprice();
            default: return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            checked[rowIndex] = (Boolean) aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
}
