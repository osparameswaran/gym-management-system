import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class GymGUI extends JFrame {
    @SuppressWarnings("FieldMayBeFinal")
    private MemberDAO memberDAO;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    
    private JTextField txtName, txtEmail, txtPhone, txtSearch;
    private JComboBox<String> cbMembershipType, cbStatus;
    private JSpinner spinnerStartDate, spinnerEndDate;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JLabel lblMemberId;
    
    public GymGUI() {
        memberDAO = new MemberDAO();
        initializeUI();
        loadMemberData();
    }
    
    private void initializeUI() {
        setTitle("Gym Membership Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(400, 0));
        
        JPanel formPanel = createFormPanel();
        leftPanel.add(formPanel, BorderLayout.NORTH);
        
        JPanel searchPanel = createSearchPanel();
        JPanel tablePanel = createTablePanel();
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel);
        addEventListeners();
        clearForm();
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Member Details"));
        formPanel.setPreferredSize(new Dimension(380, 500));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        lblMemberId = new JLabel("New Member");
        formPanel.add(lblMemberId, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Name *:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtName = new JTextField(20);
        formPanel.add(txtName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email *:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Membership Type *:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        cbMembershipType = new JComboBox<>(new String[]{"Basic", "Premium", "Gold", "Platinum"});
        formPanel.add(cbMembershipType, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Start Date *:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        spinnerStartDate = createDateSpinner();
        formPanel.add(spinnerStartDate, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("End Date *:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        spinnerEndDate = createDateSpinner();
        formPanel.add(spinnerEndDate, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Status *:"), gbc);
        gbc.gridx = 1; gbc.gridy = 7;
        cbStatus = new JComboBox<>(new String[]{"Active", "Expired", "Suspended"});
        formPanel.add(cbStatus, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        JPanel buttonPanel = createButtonPanel();
        formPanel.add(buttonPanel, gbc);
        
        return formPanel;
    }
    
    private JSpinner createDateSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);
        return spinner;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        
        btnAdd = new JButton("Add Member");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnCheckExpiry = new JButton("Check Expiry");
        
        btnAdd.setBackground(new Color(76, 175, 80));
        btnAdd.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(33, 150, 243));
        btnUpdate.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(244, 67, 54));
        btnDelete.setForeground(Color.WHITE);
        btnClear.setBackground(new Color(255, 152, 0));
        btnClear.setForeground(Color.WHITE);
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnCheckExpiry);
        
        btnRefresh.addActionListener(e -> loadMemberData());
        btnCheckExpiry.addActionListener(e -> checkMembershipExpiry());
        
        return buttonPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        searchPanel.add(new JLabel("Search by Name:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        
        btnSearch = new JButton("Search");
        searchPanel.add(btnSearch);
        
        JButton btnClearSearch = new JButton("Clear Search");
        searchPanel.add(btnClearSearch);
        
        btnClearSearch.addActionListener(e -> {
            txtSearch.setText("");
            loadMemberData();
        });
        
        return searchPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Members List"));
        
        String[] columnNames = {"ID", "Name", "Email", "Phone", "Membership Type", "Start Date", "End Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        memberTable = new JTable(tableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.getTableHeader().setReorderingAllowed(false);
        
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        memberTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        memberTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        memberTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        memberTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        memberTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        memberTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        memberTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        
        JScrollPane tableScrollPane = new JScrollPane(memberTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        JLabel statusLabel = new JLabel(" Total Members: 0");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tablePanel.add(statusLabel, BorderLayout.SOUTH);
        
        return tablePanel;
    }
    
    private void addEventListeners() {
        btnAdd.addActionListener(e -> addMember());
        btnUpdate.addActionListener(e -> updateMember());
        btnDelete.addActionListener(e -> deleteMember());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> searchMembers());
        
        txtSearch.addActionListener(e -> searchMembers());
        
        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && memberTable.getSelectedRow() >= 0) {
                displayMemberDetails(memberTable.getSelectedRow());
            }
        });
        
        memberTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = memberTable.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        displayMemberDetails(row);
                    }
                }
            }
        });
    }
    
    private void addMember() {
        try {
            Member member = getMemberFromForm();
            if (member != null) {
                if (memberDAO.addMember(member)) {
                    JOptionPane.showMessageDialog(this, "Member added successfully!");
                    clearForm();
                    loadMemberData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add member. Email might already exist or database error.");
                }
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void updateMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a member to update.");
            return;
        }
        
        try {
            Member member = getMemberFromForm();
            if (member != null) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                member.setId(id);
                
                if (memberDAO.updateMember(member)) {
                    JOptionPane.showMessageDialog(this, "Member updated successfully!");
                    clearForm();
                    loadMemberData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update member.");
                }
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void deleteMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a member to delete.");
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete member: " + name + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (memberDAO.deleteMember(id)) {
                JOptionPane.showMessageDialog(this, "Member deleted successfully!");
                clearForm();
                loadMemberData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete member.");
            }
        }
    }
    
    private void searchMembers() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            loadMemberData();
            return;
        }
        
        List<Member> members = memberDAO.searchMembersByName(searchText);
        updateTable(members);
        JOptionPane.showMessageDialog(this, "Found " + members.size() + " members matching: " + searchText);
    }
    
    private void checkMembershipExpiry() {
        List<Member> expiringMembers = memberDAO.getExpiringMembers(7);
        
        if (expiringMembers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No memberships expiring in the next 7 days!");
        } else {
            StringBuilder message = new StringBuilder("Memberships expiring soon:\n\n");
            for (Member member : expiringMembers) {
                message.append("• ").append(member.getName())
                      .append(" - Expires: ").append(member.getEndDate())
                      .append(" (").append(member.getEmail()).append(")\n");
            }
            JOptionPane.showMessageDialog(this, message.toString());
        }
    }
    
    private void displayMemberDetails(int row) {
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            String name = (String) tableModel.getValueAt(row, 1);
            String email = (String) tableModel.getValueAt(row, 2);
            String phone = (String) tableModel.getValueAt(row, 3);
            String membershipType = (String) tableModel.getValueAt(row, 4);
            LocalDate startDate = LocalDate.parse(tableModel.getValueAt(row, 5).toString());
            LocalDate endDate = LocalDate.parse(tableModel.getValueAt(row, 6).toString());
            String status = (String) tableModel.getValueAt(row, 7);
            
            lblMemberId.setText(String.valueOf(id));
            txtName.setText(name);
            txtEmail.setText(email);
            txtPhone.setText(phone);
            cbMembershipType.setSelectedItem(membershipType);
            
            Date startUtilDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endUtilDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            spinnerStartDate.setValue(startUtilDate);
            spinnerEndDate.setValue(endUtilDate);
            
            cbStatus.setSelectedItem(status);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error displaying member details: " + e.getMessage());
        }
    }
    
    private Member getMemberFromForm() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        
        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Email are required fields.");
            return null;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return null;
        }
        
        String membershipType = (String) cbMembershipType.getSelectedItem();
        Date startUtilDate = (Date) spinnerStartDate.getValue();
        Date endUtilDate = (Date) spinnerEndDate.getValue();
        
        LocalDate startDate = startUtilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = endUtilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        String status = (String) cbStatus.getSelectedItem();
        
        if (endDate.isBefore(startDate)) {
            JOptionPane.showMessageDialog(this, "End date must be after start date.");
            return null;
        }
        
        if (endDate.isBefore(LocalDate.now())) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "End date is in the past. Do you want to set status to 'Expired'?", 
                "Past End Date", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                status = "Expired";
                cbStatus.setSelectedItem("Expired");
            }
        }
        
        return new Member(name, email, phone, membershipType, startDate, endDate, status);
    }
    
    private void loadMemberData() {
        List<Member> members = memberDAO.getAllMembers();
        updateTable(members);
    }
    
    private void updateTable(List<Member> members) {
        tableModel.setRowCount(0);
        for (Member member : members) {
            Object[] rowData = {
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getMembershipType(),
                member.getStartDate().toString(),
                member.getEndDate().toString(),
                member.getStatus()
            };
            tableModel.addRow(rowData);
        }
        
        // Update member count
        JPanel tablePanel = (JPanel) memberTable.getParent().getParent().getParent();
        Component[] components = tablePanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel label) {
                if (label.getText().startsWith(" Total Members:")) {
                    label.setText(" Total Members: " + members.size());
                    break;
                }
            }
        }
    }
    
    private void clearForm() {
        lblMemberId.setText("New Member");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        cbMembershipType.setSelectedIndex(0);
        
        Date currentDate = new Date();
        spinnerStartDate.setValue(currentDate);
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(java.util.Calendar.DATE, 30);
        spinnerEndDate.setValue(cal.getTime());
        
        cbStatus.setSelectedIndex(0);
        memberTable.clearSelection();
        txtName.requestFocus();
    }
}