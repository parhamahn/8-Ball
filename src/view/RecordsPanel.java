package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RecordsPanel extends JPanel {
    private JTable recordsTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    public RecordsPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Game Records", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Winner", "Loser"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recordsTable = new JTable(tableModel);
        recordsTable.setFont(new Font("Arial", Font.PLAIN, 16));
        recordsTable.setRowHeight(30);
        recordsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(recordsTable);
        add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void displayRecords(List<model.Record> records) {
        tableModel.setRowCount(0);
        for (model.Record record : records) {
            tableModel.addRow(new Object[]{record.getWinner(), record.getLoser()});
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
