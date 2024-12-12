package atm2Buton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATM_GUI extends JFrame {
    static QueryIMPL queryIMPL = new QueryIMPL();
    static String accountPassword;
    static int balance;
    static JTextField inputField;
    static JTextArea displayArea;

    public ATM_GUI() {
        // Frame özellikleri
        setTitle("ATM Uygulaması");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Display Area (bakiye, bilgiler yazılacak alan)
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Input alanı (şifre girilmesi için)
        inputField = new JTextField();
        add(inputField, BorderLayout.NORTH);

        // Buton Paneli
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 2)); // 4 satır, 2 sütun

        // Butonlar
        JButton balanceButton = new JButton("Bakiye Sorgulama");
        JButton depositButton = new JButton("Para Yatırma");
        JButton withdrawButton = new JButton("Para Çekme");
        JButton updatePhoneButton = new JButton("Telefon Numarası Güncelleme");
        JButton updatePasswordButton = new JButton("Şifre Güncelleme");
        JButton removeAccountButton = new JButton("Hesap Sil");
        JButton exitButton = new JButton("Çıkış");

        // Butonlara action listener ekleme
        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                balance = queryIMPL.costumerInfoBalance(accountPassword);
                displayArea.append("Mevcut bakiye: " + balance + "\n");
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int deposit = Integer.parseInt(JOptionPane.showInputDialog("Yatırılacak miktarı girin:"));
                    if (deposit < 0) {
                        JOptionPane.showMessageDialog(null, "Negatif bir değer giremezsiniz!");
                    } else {
                        balance += deposit;
                        queryIMPL.updateBalance(accountPassword, balance);
                        displayArea.append("Yeni bakiye: " + balance + "\n");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Geçersiz miktar!");
                }
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int withdraw = Integer.parseInt(JOptionPane.showInputDialog("Çekilecek miktarı girin:"));
                    if (withdraw < 0) {
                        JOptionPane.showMessageDialog(null, "Negatif bir değer giremezsiniz!");
                    } else if (balance >= withdraw) {
                        balance -= withdraw;
                        queryIMPL.updateBalance(accountPassword, balance);
                        displayArea.append("Yeni bakiye: " + balance + "\n");
                    } else {
                        JOptionPane.showMessageDialog(null, "Yetersiz bakiye!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Geçersiz miktar!");
                }
            }
        });

        updatePhoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPhone = JOptionPane.showInputDialog("Yeni telefon numaranızı girin:");
                queryIMPL.updatePhoneNumber(accountPassword, newPhone);
                displayArea.append("Telefon numarası güncellendi: " + newPhone + "\n");
            }
        });

        updatePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = JOptionPane.showInputDialog("Yeni şifrenizi girin:");
                queryIMPL.updateAccountPassword(accountPassword, newPassword);
                accountPassword = newPassword;  // Şifreyi güncelle
                displayArea.append("Şifre güncellendi.\n");
            }
        });

        removeAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String confirmationPassword = JOptionPane.showInputDialog("Hesap silme için şifrenizi tekrar girin:");
                if (confirmationPassword.equals(accountPassword)) {
                    int response = JOptionPane.showConfirmDialog(null, "Hesabınızı silmek istediğinizden emin misiniz?");
                    if (response == JOptionPane.YES_OPTION) {
                        queryIMPL.removeCostumer(accountPassword);
                        displayArea.append("Hesabınız silindi.\n");
                    } else {
                        displayArea.append("Hesap silme işlemi iptal edildi.\n");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Yanlış şifre!");
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryIMPL.updateBalance(accountPassword, balance);
                queryIMPL.close();
                System.exit(0);

            }
        });

        // Butonları ekleme
        buttonPanel.add(balanceButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(updatePhoneButton);
        buttonPanel.add(updatePasswordButton);
        buttonPanel.add(removeAccountButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        queryIMPL.connection();
        accountPassword = JOptionPane.showInputDialog("Lütfen hesap şifrenizi girin:");

        // GUI'yi başlat
        ATM_GUI gui = new ATM_GUI();
        gui.setVisible(true);
    }
}
