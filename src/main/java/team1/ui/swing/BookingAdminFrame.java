package team1.ui.swing;

import team1.dao.BookingDao;
import team1.domain.booking.Booking;
import team1.dto.booking.UpdateBookingStateRequestDto;
import team1.facade.BookingManagementFacade;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class BookingAdminFrame extends JFrame {
    private final BookingManagementFacade facade;
    private final BookingDao bookingDao;
    private final BookingTableModel tableModel;
    private final JTextField restaurantIdField = new JTextField();
    private final JTextField bookingIdField = new JTextField();
    private final JTextField newStateField = new JTextField();
    private final JTextField actorField = new JTextField();

    public BookingAdminFrame(BookingManagementFacade facade, BookingDao bookingDao) {
        super("Booking Admin");
        this.facade = facade;
        this.bookingDao = bookingDao;
        this.tableModel = new BookingTableModel();
        initUi();
        loadToday();
    }

    private void initUi() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLayout(new BorderLayout());

        JPanel west = new JPanel(new GridLayout(0, 1, 4, 4));
        JButton refreshBtn = new JButton("오늘 예약 조회");
        refreshBtn.addActionListener(e -> loadToday());
        west.add(new JLabel("Restaurant ID"));
        west.add(restaurantIdField);
        west.add(refreshBtn);
        add(west, BorderLayout.WEST);

        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(0, 1, 4, 4));
        right.add(new JLabel("Actor(owner)"));
        right.add(actorField);
        right.add(new JLabel("Booking ID"));
        right.add(bookingIdField);
        right.add(new JLabel("New State"));
        right.add(newStateField);
        JButton changeBtn = new JButton("변경");
        changeBtn.addActionListener(e -> changeState());
        right.add(changeBtn);
        add(right, BorderLayout.EAST);
    }

    private void loadToday() {
        try {
            String restId = restaurantIdField.getText();
            if (restId == null || restId.isBlank()) {
                JOptionPane.showMessageDialog(this, "Restaurant ID를 입력하세요.");
                return;
            }
            List<Booking> list = bookingDao.findByRestaurantAndDate(restId, LocalDate.now());
            tableModel.setData(list);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "조회 실패: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void changeState() {
        try {
            UpdateBookingStateRequestDto req = new UpdateBookingStateRequestDto();
            req.setActorUserId(actorField.getText());
            req.setBookingId(bookingIdField.getText());
            req.setNewState(newStateField.getText());
            req.setDescription("admin change");
            facade.changeBookingStateByOwner(req.getActorUserId(), req);
            JOptionPane.showMessageDialog(this, "변경 완료");
            loadToday();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "변경 실패: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static class BookingTableModel extends AbstractTableModel {
        private List<Booking> data = List.of();
        private final String[] columns = {"ID", "Restaurant", "User", "Date", "Start", "End", "Persons", "State"};

        public void setData(List<Booking> data) {
            this.data = data;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Booking b = data.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> b.getId();
                case 1 -> b.getRestaurantId();
                case 2 -> b.getUserId();
                case 3 -> b.getDate();
                case 4 -> b.getStartTime();
                case 5 -> b.getEndTime();
                case 6 -> b.getPersons();
                case 7 -> b.getState();
                default -> "";
            };
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "BookingAdminFrame는 실제 DAO/서비스 구성 후 main에서 facade/dao를 주입해 주세요.");
        });
    }
}
