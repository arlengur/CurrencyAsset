package ru.arlen;

import org.apache.log4j.Logger;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import ru.arlen.utils.SpringUtilities;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static ru.arlen.utils.UserUtils.calculateProfit;

class UserGUI extends JFrame {
    private static final Logger logger = Logger.getLogger(UserGUI.class);
    private static final int WIDTH = 300;
    private static final int HEIGHT = 140;
    private JDatePickerImpl datePicker;
    private JFormattedTextField amountField;
    private JTextField calcField = new JTextField();
    private JButton calcButton = new JButton("Recalculate");

    UserGUI() {
        // Init Title
        super("Calculation of profit");
        // Setup components
        setUp();
    }

    private void setUp() {
        // Init window
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.setBounds(dimension.width / 2 - WIDTH / 2, dimension.height / 2 - HEIGHT / 2, WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Init amount field
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        amountField = new JFormattedTextField(formatter);

        // Init date field
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

        // Init recalculate field
        calcField.setEditable(false);

        // Init recalculate button
        calcButton.addActionListener(e -> {
            if (isFieldsEmpty()) {
                JOptionPane.showMessageDialog(null, "Text field or date is empty!");
                logger.warn("Text field or date is empty!");
                return;
            }
            Double profit = calculateProfit(getSelectedDate(), amountField.getText(), logger);
            calcField.setText(profit.toString());
            logger.info("Profit = " + profit);
        });

        // Add components on window
        Container container = this.getContentPane();
        container.setLayout(new SpringLayout());
        container.add(new JLabel("Date:"));
        container.add(datePicker);
        container.add(new JLabel("Amount USD:"));
        container.add(amountField);
        container.add(calcButton);
        container.add(calcField);

        SpringUtilities.makeGrid(container, 3, 2, 5, 5, 5, 5);
    }

    private boolean isFieldsEmpty() {
        return amountField.getText().isEmpty() || getDate().isEmpty();
    }

    private String getSelectedDate() {
        LocalDate localDate = LocalDate.parse(getDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String getDate() {
        return datePicker.getJFormattedTextField().getText();
    }

}
