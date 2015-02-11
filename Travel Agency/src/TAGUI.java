
import java.awt.CardLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import travel.agency.Car;
import travel.agency.Car.Interval;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author abinashmeher999
 */
public class TAGUI extends javax.swing.JFrame implements Serializable {

    private int tempID;
    private Car tempCarG;
    Date pickUPDate = null, expreturnDate = null;

    /**
     * Creates new form TAGUI
     */
    public TAGUI() {
        initComponents();
        readFile(fileName);
    }

    ArrayList<Car> cars = new ArrayList<>();
    String fileName = "Data.dat";
    float fuelCost = (float) 60.0;

    void readFile(String fileName) {
        try (FileInputStream fileIn = new FileInputStream(fileName); ObjectInputStream objectInput = new ObjectInputStream(fileIn)) {
            cars = (ArrayList<Car>) objectInput.readObject();
            DefaultTableModel model = (DefaultTableModel) allTable.getModel();
            for (Car car : cars) {
                model.addRow(new Object[]{car.getType(), car.isAC(), car.getID(), car.getState().toString()});
            }
            //allTable = (JTable) objectInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
        }
    }

    void writeFile(String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOut);
            objectOutput.writeObject(cars);
            //objectOutput.writeObject(allTable);
            fileOut.close();
            JOptionPane.showMessageDialog(null, "Successfully Saved.", "Message", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Couldn't be Saved.", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    boolean addVehicle(Car.Type type, float price, int ID, boolean isAC, ArrayList<Car> carList) {

        Car newCar = new Car(type, price, ID, isAC);
        for (Car car : carList) {
            if (car.getID() == newCar.getID()) {
                JOptionPane.showMessageDialog(null, "ID already exists", "Retry", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        }
        cars.add(newCar);
        writeFile(fileName);
        return true;
    }

    void deleteVehicle(int ID) throws IOException {
        DefaultTableModel model = (DefaultTableModel) allTable.getModel();
        int numRow = model.getRowCount();
        for (int i = 0; i < numRow; i++) {
            if (Integer.parseInt(model.getValueAt(i, 2).toString()) == ID) {
                model.removeRow(i);
                numRow -= 1;
            }
        }
        for (Car car : cars) {
            if (car.getID() == ID) {
                if (car.getState() == Car.State.AVAILABLE) {
                    cars.remove(car);

                    JOptionPane.showMessageDialog(null, "Successfully Deleted.", "Message", JOptionPane.INFORMATION_MESSAGE);
                    writeFile(fileName);
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, "Car either rented or sent for repair.", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    void searchID(Car.Type type, boolean isAC, JList jList) {
        DefaultListModel model = (DefaultListModel) jList.getModel();
        int rowCount;
        rowCount = model.getSize();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.remove(i);
        }
        for (Car car : cars) {
            if (car.getType() == type && car.isAC() == isAC) {
                model.addElement(car.getID());
            }
        }
    }

    void searchCar(Car.Type type, boolean isAC, Date rdate, Date edate, JTable jTable) {
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        int rowCount;
        rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        for (Car car : cars) {
            if (car.getType() == type && car.isAC() == isAC) {
                boolean flag = true;
                for (Car.log int0 : car.getTimes()) {
                    Car.Interval inttemp = new Car.Interval(rdate, edate);
                    if (Car.isOverlapping(inttemp, int0.interval)) {
                        flag = false;
                    }
                }
                if (flag) {
                    model.addRow(new Object[]{car.getID(), car.getPerHour(), car.getPerKM(), car.getAdvanceMoney()});
                }
            }
        }
    }

    int rentCar(int ID, Date pickUp, Date expDate, JTable jTable) {
        int pID = 0;
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        ArrayList<Integer> ids = new ArrayList<>();
        int rowNum = model.getRowCount();
        for (int i = 0; i < rowNum; i++) {
            ids.add((Integer) model.getValueAt(i, 0));
        }
        if (ids.contains(ID)) {
            for (Car car : cars) {
                if (car.getID() == ID) {
                    if (car.getState() != Car.State.REPAIR) {
                        car.setState(Car.State.RENTED);
                        pID = car.getNumRented() + 1;
                        car.setNumRented(car.getNumRented() + 1);
                        car.getTimes().add(new Car.log(pID, new Car.Interval(pickUp, expDate)));
                        JOptionPane.showMessageDialog(null, "Car is Yours!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Car Sent for repair!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        //pID = -1;
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Car Not Available.", "Message", JOptionPane.INFORMATION_MESSAGE);
            //pID = -1;
        }
        return pID;
    }

    float returnCar(int ID, int pID, int odom, Date rdate) {
        Car tempCar = null;
        boolean carfound = false;
        boolean found = false;
        float extraCost = 0;
        for (Car car : cars) {
            if (car.getID() == ID && car.getState() == Car.State.RENTED) {
                tempCar = car;
                carfound = true;
                extraCost = -tempCar.getAdvanceMoney();
                break;
            }
        }
        if (carfound == false) {
            JOptionPane.showMessageDialog(null, "Wrong ID or hasnt been rented", "Error", JOptionPane.INFORMATION_MESSAGE);
            return 0;
        }
        for (Car.log templog : tempCar.getTimes()) {
            if (templog.ID == pID) {
                found = true;
                tempCar.getTimes().remove(templog);
                Car.Interval actInter = new Car.Interval(templog.interval.start, rdate);
                for (Car.log loginter : tempCar.getTimes()) {
                    if (Car.isOverlapping(actInter, loginter.interval)) {
                        JOptionPane.showMessageDialog(null, "Session ID " + loginter.ID + " was compromised due to late return\nPay " + tempCar.getAdvanceMoney() + "as penalty.", "Message", JOptionPane.INFORMATION_MESSAGE);
                        extraCost += tempCar.getAdvanceMoney();
                        tempCar.getTimes().remove(loginter);
                    }
                }
                if (tempCar.getTimes().isEmpty()) {
                    tempCar.setState(Car.State.AVAILABLE);
                    //int nID = Integer.parseInt(cRcarID.getText());
                    for (Car car : cars) {
                        if (car.getID() == ID) {

                            //car.setState(Car.State.AVAILABLE);
                            DefaultTableModel model = (DefaultTableModel) allTable.getModel();
                            int numRow = model.getRowCount();
                            if (numRow != 0) {
                                for (int i = numRow - 1; i >= 0; i--) {
                                    if (Integer.parseInt(model.getValueAt(i, 2).toString()) == ID) {
                                        model.setValueAt(Car.State.AVAILABLE.toString(), i, 3);
                                    }
                                }
                            }
                            //car.setTotalRepairCost((float) (car.getTotalRepairCost() + 300 + Math.random() % 3500))
                        }
                    }
                    //JOptionPane.showMessageDialog(null, "Car sent for repair.", "Output", JOptionPane.INFORMATION_MESSAGE);
                }
                float hours = (rdate.getTime() - templog.interval.start.getTime());
                hours /= (1000 * 60 * 60);
                if (hours < 4) {
                    hours = 4;
                }
                tempCar.setFuelConsumed(tempCar.getFuelConsumed() + (odom - tempCar.getTotalKM()) / tempCar.getMileage());
                tempCar.setTotalfuelCost(tempCar.getTotalfuelCost() + ((odom - tempCar.getTotalKM()) / tempCar.getMileage()) * fuelCost);
                float costh = hours * (tempCar.getPerHour() + (tempCar.isAC() ? 50 : 0));
                float costk = (odom - tempCar.getTotalKM()) * tempCar.getPerKM();
                tempCar.setTotalKM(odom);
                if (costh > costk) {
                    extraCost += costh;
                } else {
                    extraCost += costk;
                }
                tempCar.setTotalearned(tempCar.getTotalearned() + extraCost + tempCar.getAdvanceMoney());
                break;
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "Invalid session ID", "Retry", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        return extraCost;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        container = new javax.swing.JPanel();
        home = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(70, 0), new java.awt.Dimension(70, 0), new java.awt.Dimension(70, 32767));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(70, 0), new java.awt.Dimension(70, 0), new java.awt.Dimension(70, 32767));
        cR = new javax.swing.JButton();
        aS = new javax.swing.JButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 60), new java.awt.Dimension(0, 60), new java.awt.Dimension(32767, 60));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 30), new java.awt.Dimension(0, 30), new java.awt.Dimension(32767, 30));
        consumer = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        rCtype = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        rCtable = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        rCcarID = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        pUD = new javax.swing.JTextField();
        pUT = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        eRD = new javax.swing.JTextField();
        eRT = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        rCsessionIDno = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        rCAC = new javax.swing.JRadioButton();
        rCnAC = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        cRcarID = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        cRRD = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        cRRT = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        cRPayment = new javax.swing.JLabel();
        cRconfirm = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        cRsessionID = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        cRodom = new javax.swing.JTextField();
        company = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        comtp = new javax.swing.JTabbedPane();
        aC = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        aCprice = new javax.swing.JTextField();
        aCtype = new javax.swing.JComboBox();
        aCcreate = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        aCID = new javax.swing.JTextField();
        aCAC = new javax.swing.JRadioButton();
        aCnAC = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        dC = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dClist = new javax.swing.JList();
        jLabel7 = new javax.swing.JLabel();
        dCID = new javax.swing.JTextField();
        dCsell = new javax.swing.JButton();
        dCtype = new javax.swing.JComboBox();
        dCnAC = new javax.swing.JRadioButton();
        dCAC = new javax.swing.JRadioButton();
        jButton4 = new javax.swing.JButton();
        mD = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        mDlist = new javax.swing.JList();
        mDget = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        mDpH = new javax.swing.JTextField();
        mDadvance = new javax.swing.JTextField();
        mDpKM = new javax.swing.JTextField();
        mDupdate = new javax.swing.JButton();
        mDtype = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        mDID = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        mDminHour = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        mDmileage = new javax.swing.JTextField();
        mDaCAC = new javax.swing.JRadioButton();
        mDaCnAC = new javax.swing.JRadioButton();
        jButton3 = new javax.swing.JButton();
        s = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        sTable = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        sID = new javax.swing.JTextField();
        sGet = new javax.swing.JButton();
        sFR = new javax.swing.JPanel();
        sFPcarID = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        sFP = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        allTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        container.setLayout(new java.awt.CardLayout());

        home.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Rehearsal Curve BRK", 1, 24)); // NOI18N
        jLabel1.setText("XYZ Car Rentals");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        home.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        home.add(filler1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        home.add(filler2, gridBagConstraints);

        cR.setText("Car Rental");
        cR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cRMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        home.add(cR, gridBagConstraints);

        aS.setText("Admin Settings");
        aS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aSMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        home.add(aS, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        home.add(filler3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        home.add(filler4, gridBagConstraints);

        container.add(home, "home");

        jButton1.setText("Back");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jLabel16.setText("Type:");

        rCtype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AMBASSADOR", "TATA_SUMO", "MARUTI_OMNI", "MARUTI_ESTEEM", "MAHINDRA_ARMADA", " " }));

        rCtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "perHour", "perKM", "Advance"
            }
        ));
        jScrollPane4.setViewportView(rCtable);

        jLabel18.setText("ID:");

        jButton8.setText("Book");
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton8MouseClicked(evt);
            }
        });
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel19.setText("PickUp Time:");

        pUD.setText("dd/mm/yyyy");

        pUT.setText("hh:mm:ss");

        jLabel12.setText("Date");

        jLabel20.setText("time");

        jButton9.setText("Check Availability");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });

        jLabel21.setText("Expected Return");

        eRD.setText("dd/mm/yyyy");

        eRT.setText("hh:mm:ss");

        jLabel29.setText("Your rental session ID is:");

        jLabel30.setText("*Required for return.");

        buttonGroup4.add(rCAC);
        rCAC.setText("AC");

        buttonGroup4.add(rCnAC);
        rCnAC.setText("Non-AC");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(48, 48, 48)
                                .addComponent(rCtype, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(rCAC)
                                .addGap(42, 42, 42)
                                .addComponent(rCnAC)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(55, 55, 55)
                                .addComponent(jLabel20)
                                .addGap(22, 22, 22))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pUD)
                                    .addComponent(eRD))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pUT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(eRT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rCcarID, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rCsessionIDno, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(33, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(rCtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pUD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pUT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rCAC)
                        .addComponent(rCnAC))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(eRD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(eRT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rCsessionIDno, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(rCcarID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton8)
                        .addComponent(jLabel29)
                        .addComponent(jLabel30)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Rent Car", jPanel5);

        jLabel22.setText("ID:");

        jLabel24.setText("Return Date:");

        cRRD.setText("dd/mm/yyyy");

        jLabel25.setText("Return Time:");

        cRRT.setText("hh:mm:ss");
        cRRT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cRRTActionPerformed(evt);
            }
        });

        jButton10.setText("Pay");
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton10MouseClicked(evt);
            }
        });

        jLabel26.setText("Remainder Amount:");

        cRPayment.setText("0 to pay.");

        cRconfirm.setText("Confirm");
        cRconfirm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cRconfirmMouseClicked(evt);
            }
        });

        jLabel31.setText("Session ID:");

        cRsessionID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cRsessionIDActionPerformed(evt);
            }
        });

        jLabel32.setText("Odometer Reading:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel26)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cRPayment)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel24)
                                    .addGap(18, 18, 18)
                                    .addComponent(cRRD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cRcarID, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel25))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                    .addGap(25, 25, 25)
                                    .addComponent(jLabel31)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cRsessionID, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cRRT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(cRconfirm)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cRodom, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(311, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(cRcarID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(cRsessionID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(cRRD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(cRRT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(cRodom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(cRconfirm)
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(cRPayment)
                    .addComponent(jButton10))
                .addContainerGap(101, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Car Return", jPanel6);

        javax.swing.GroupLayout consumerLayout = new javax.swing.GroupLayout(consumer);
        consumer.setLayout(consumerLayout);
        consumerLayout.setHorizontalGroup(
            consumerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consumerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(consumerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(consumerLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        consumerLayout.setVerticalGroup(
            consumerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, consumerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        container.add(consumer, "consumer");

        jButton2.setText("Back");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jLabel2.setText("Type:");

        jLabel4.setText("Price:");

        aCtype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AMBASSADOR", "TATA_SUMO", "MARUTI_OMNI", "MARUTI_ESTEEM", "MAHINDRA_ARMADA", " " }));

        aCcreate.setText("Create");
        aCcreate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aCcreateMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                aCcreateMouseEntered(evt);
            }
        });

        jLabel23.setText("Ref ID:");

        buttonGroup1.add(aCAC);
        aCAC.setText("AC");

        buttonGroup1.add(aCnAC);
        aCnAC.setText("Non-AC");

        jLabel3.setText("*Please go to modify to add further details");

        javax.swing.GroupLayout aCLayout = new javax.swing.GroupLayout(aC);
        aC.setLayout(aCLayout);
        aCLayout.setHorizontalGroup(
            aCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aCLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(aCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, aCLayout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                            .addComponent(aCprice, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, aCLayout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(48, 48, 48)
                            .addComponent(aCtype, 0, 1, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, aCLayout.createSequentialGroup()
                            .addGroup(aCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel23)
                                .addComponent(aCAC))
                            .addGap(42, 42, 42)
                            .addGroup(aCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(aCnAC)
                                .addComponent(aCID))))
                    .addComponent(aCcreate, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(405, Short.MAX_VALUE))
        );
        aCLayout.setVerticalGroup(
            aCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aCLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(aCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(aCtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(aCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aCAC)
                    .addComponent(aCnAC))
                .addGap(9, 9, 9)
                .addGroup(aCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(aCID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(aCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(aCprice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(aCcreate)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        comtp.addTab("Add Car", aC);

        jLabel5.setText("Type:");

        jScrollPane1.setViewportView(dClist);

        jLabel7.setText("ID:");

        dCsell.setText("Sell");
        dCsell.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dCsellMouseClicked(evt);
            }
        });

        dCtype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AMBASSADOR", "TATA_SUMO", "MARUTI_OMNI", "MARUTI_ESTEEM", "MAHINDRA_ARMADA", " " }));

        buttonGroup2.add(dCnAC);
        dCnAC.setText("Non-AC");

        buttonGroup2.add(dCAC);
        dCAC.setText("AC");

        jButton4.setText("See List");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout dCLayout = new javax.swing.GroupLayout(dC);
        dC.setLayout(dCLayout);
        dCLayout.setHorizontalGroup(
            dCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dCLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(dCLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dCID, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dCsell, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(dCLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(48, 48, 48)
                        .addComponent(dCtype, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(dCLayout.createSequentialGroup()
                        .addComponent(dCAC)
                        .addGap(42, 42, 42)
                        .addComponent(dCnAC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4)))
                .addContainerGap(393, Short.MAX_VALUE))
        );
        dCLayout.setVerticalGroup(
            dCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dCLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(dCtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dCAC)
                    .addComponent(dCnAC)
                    .addComponent(jButton4))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(dCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(dCID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dCsell))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        comtp.addTab("Delete Car", dC);

        jLabel8.setText("Type:");

        jScrollPane2.setViewportView(mDlist);

        mDget.setText("Get Details");
        mDget.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mDgetMouseClicked(evt);
            }
        });

        jLabel10.setText("perHour:");

        jLabel11.setText("Advance:");

        jLabel13.setText("perKM:");

        mDupdate.setText("Update");
        mDupdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mDupdateMouseClicked(evt);
            }
        });

        mDtype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AMBASSADOR", "TATA_SUMO", "MARUTI_OMNI", "MARUTI_ESTEEM", "MAHINDRA_ARMADA", " " }));

        jLabel14.setText("ID:");

        jLabel28.setText("minHour:");

        jLabel27.setText("Mileage:");

        buttonGroup3.add(mDaCAC);
        mDaCAC.setText("AC");

        buttonGroup3.add(mDaCnAC);
        mDaCnAC.setText("Non-AC");

        jButton3.setText("Show Cars");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mDLayout = new javax.swing.GroupLayout(mD);
        mD.setLayout(mDLayout);
        mDLayout.setHorizontalGroup(
            mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mDLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(mDupdate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mDLayout.createSequentialGroup()
                        .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mDLayout.createSequentialGroup()
                                .addComponent(mDaCAC)
                                .addGap(42, 42, 42)
                                .addComponent(mDaCnAC)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3))
                            .addGroup(mDLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(44, 44, 44)
                                .addComponent(mDtype, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mDLayout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mDID)
                                .addGap(18, 18, 18)
                                .addComponent(mDget, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(22, 22, 22)
                        .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mDLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(mDpH))
                            .addGroup(mDLayout.createSequentialGroup()
                                .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel11))
                                .addGap(18, 18, 18)
                                .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(mDadvance, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                                    .addComponent(mDpKM)))
                            .addGroup(mDLayout.createSequentialGroup()
                                .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel27))
                                .addGap(18, 18, 18)
                                .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(mDminHour, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                                    .addComponent(mDmileage))))))
                .addContainerGap())
        );
        mDLayout.setVerticalGroup(
            mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(mDpH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mDtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(mDaCAC)
                        .addComponent(mDaCnAC))
                    .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(mDpKM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mDLayout.createSequentialGroup()
                        .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(mDadvance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(mDminHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(mDmileage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(mDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mDget)
                    .addComponent(jLabel14)
                    .addComponent(mDID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(mDupdate)
                .addContainerGap())
        );

        comtp.addTab("Modify Details", mD);

        sTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Total Repair", "No. of Repair", "No. Rented", "Total earned", "Total fuel cost", "Total Distance(in km)", "Total fuel consumed"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(sTable);

        jLabel15.setText("ID:");

        sGet.setText("Get Stats");
        sGet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sGetMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout sLayout = new javax.swing.GroupLayout(s);
        s.setLayout(sLayout);
        sLayout.setHorizontalGroup(
            sLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                    .addGroup(sLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(sID, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sGet)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        sLayout.setVerticalGroup(
            sLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(sID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sGet))
                .addGap(35, 35, 35)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        comtp.addTab("Statistics", s);

        sFPcarID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sFPcarIDActionPerformed(evt);
            }
        });

        jLabel33.setText("Car ID:");

        sFP.setText("Send for Repair");
        sFP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sFPMouseClicked(evt);
            }
        });

        jButton5.setText("Return from Repair");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout sFRLayout = new javax.swing.GroupLayout(sFR);
        sFR.setLayout(sFRLayout);
        sFRLayout.setHorizontalGroup(
            sFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sFRLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(sFP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(sFRLayout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sFPcarID, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addContainerGap(404, Short.MAX_VALUE))
        );
        sFRLayout.setVerticalGroup(
            sFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sFRLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(sFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sFPcarID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sFRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sFP)
                    .addComponent(jButton5))
                .addContainerGap(197, Short.MAX_VALUE))
        );

        comtp.addTab("Send for Repair", sFR);

        allTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Type", "AC/non-AC", "ID", "State"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(allTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        comtp.addTab("All Cars", jPanel1);

        javax.swing.GroupLayout companyLayout = new javax.swing.GroupLayout(company);
        company.setLayout(companyLayout);
        companyLayout.setHorizontalGroup(
            companyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(companyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(companyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(companyLayout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(comtp))
                .addContainerGap())
        );
        companyLayout.setVerticalGroup(
            companyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, companyLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(comtp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        container.add(company, "company");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cRMouseClicked
        CardLayout card = (CardLayout) container.getLayout();
        card.show(container, "consumer");
        // TODO add your handling code here:
    }//GEN-LAST:event_cRMouseClicked

    private void aSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aSMouseClicked
        CardLayout card = (CardLayout) container.getLayout();
        card.show(container, "company");
        // TODO add your handling code here:
    }//GEN-LAST:event_aSMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        CardLayout card = (CardLayout) container.getLayout();
        card.show(container, "home");
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        CardLayout card = (CardLayout) container.getLayout();
        card.show(container, "home");
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void cRRTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cRRTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cRRTActionPerformed

    private void cRsessionIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cRsessionIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cRsessionIDActionPerformed

    private void sFPcarIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sFPcarIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sFPcarIDActionPerformed

    private void aCcreateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aCcreateMouseClicked
        int ID = Integer.parseInt(aCID.getText());
        float price = Float.parseFloat(aCprice.getText());
        Car.Type carType = Car.Type.valueOf(aCtype.getSelectedItem().toString());
        boolean isAC;
        if (aCAC.isSelected()) {
            isAC = true;
        } else {
            if (aCnAC.isSelected()) {
                isAC = false;
            } else {
                JOptionPane.showMessageDialog(null, "Please select AC/non-AC", "Retry", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        boolean isAdded = addVehicle(carType, price, ID, isAC, cars);
        DefaultTableModel model = (DefaultTableModel) allTable.getModel();
        if (isAdded) {
            model.addRow(new Object[]{carType.toString(), isAC, ID, Car.State.AVAILABLE});
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_aCcreateMouseClicked

    private void dCsellMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dCsellMouseClicked
        if ("".equals(dCID.getText())) {
            JOptionPane.showMessageDialog(null, "Enter an ID", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int ID = Integer.parseInt(dCID.getText());
        try {
            deleteVehicle(ID);
            // TODO add your handling code here:
        } catch (IOException ex) {
            Logger.getLogger(TAGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_dCsellMouseClicked

    private void sFPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sFPMouseClicked
        if ("".equals(sFPcarID.getText())) {
            JOptionPane.showMessageDialog(null, "Enter an ID", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int ID = Integer.parseInt(sFPcarID.getText());
        for (Car car : cars) {
            if (car.getID() == ID) {
                if (car.getState() == Car.State.AVAILABLE) {
                    car.setState(Car.State.REPAIR);
                    DefaultTableModel model = (DefaultTableModel) allTable.getModel();
                    int numRow = model.getRowCount();
                    if (numRow != 0) {
                        for (int i = numRow - 1; i >= 0; i--) {
                            if (Integer.parseInt(model.getValueAt(i, 2).toString()) == ID) {
                                model.setValueAt(Car.State.REPAIR.toString(), i, 3);
                            }
                        }
                    }
                    car.setTotalRepairCost((float) (car.getTotalRepairCost() + 300 + Math.random() % 3500));
                    car.setNumSentRepair(car.getNumSentRepair() + 1);
                    writeFile(fileName);
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, "Car rented or already sent for repair.", "Output", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Car not found!", "Result", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_sFPMouseClicked

    private void sGetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sGetMouseClicked
        if ("".equals(sID.getText())) {
            JOptionPane.showMessageDialog(null, "Enter an ID", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) sTable.getModel();
        int numRow = model.getRowCount();
        if (numRow != 0) {
            for (int i = numRow; i >= 0; i--) {
                model.removeRow(i);
            }
        }
        int ID = Integer.parseInt(sID.getText());
        for (Car car : cars) {
            if (car.getID() == ID) {
                model.addRow(new Object[]{car.getTotalRepairCost(), car.getNumSentRepair(), car.getNumRented(),
                    car.getTotalearned(), car.getTotalfuelCost(), car.getTotalKM(),
                    car.getTotalfuelCost()});
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Car not found!", "Result", JOptionPane.INFORMATION_MESSAGE);
        // TODO add your handling code here:
    }//GEN-LAST:event_sGetMouseClicked

    private void mDgetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mDgetMouseClicked
        if ("".equals(mDID.getText())) {
            JOptionPane.showMessageDialog(null, "Enter an ID", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int ID = Integer.parseInt(mDID.getText());
        for (Car car : cars) {
            if (car.getID() == ID) {
                tempCarG = car;
                mDpH.setText(String.valueOf(car.getPerHour()));
                mDpKM.setText(String.valueOf(car.getPerKM()));
                mDadvance.setText(String.valueOf(car.getAdvanceMoney()));
                mDminHour.setText(String.valueOf(car.getMinHour()));
                mDmileage.setText(String.valueOf(car.getMileage()));
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Car not Found!", "Result", JOptionPane.INFORMATION_MESSAGE);
        // TODO add your handling code here:
    }//GEN-LAST:event_mDgetMouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        Car.Type carType = Car.Type.valueOf(mDtype.getSelectedItem().toString());
        boolean isAC;
        if (mDaCAC.isSelected()) {
            isAC = true;
        } else {
            if (mDaCnAC.isSelected()) {
                isAC = false;
            } else {
                JOptionPane.showMessageDialog(null, "Please select AC/non-AC", "Retry", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        DefaultListModel model;
        model = new DefaultListModel();
        for (Car car : cars) {
            if (car.getType() == carType && car.isAC() == isAC) {
                model.addElement(car.getID());
            }
        }
        mDlist.setModel(model);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseClicked

    private void mDupdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mDupdateMouseClicked
        if (tempCarG == null) {
            JOptionPane.showMessageDialog(null, "First press Get Details and then try again", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        tempCarG.setPerHour(Float.parseFloat(mDpH.getText()));
        tempCarG.setPerKM(Float.parseFloat(mDpKM.getText()));
        tempCarG.setMinHour(Float.parseFloat(mDminHour.getText()));
        tempCarG.setAdvanceMoney(Float.parseFloat(mDadvance.getText()));
        tempCarG.setMileage(Float.parseFloat(mDmileage.getText()));
        mDpH.setText("");
        mDpKM.setText("");
        mDminHour.setText("");
        mDadvance.setText("");
        mDmileage.setText("");
        tempCarG = null;
        writeFile(fileName);
    }//GEN-LAST:event_mDupdateMouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        Car.Type carType = Car.Type.valueOf(dCtype.getSelectedItem().toString());
        boolean isAC;
        if (dCAC.isSelected()) {
            isAC = true;
        } else {
            if (dCnAC.isSelected()) {
                isAC = false;
            } else {
                JOptionPane.showMessageDialog(null, "Please select AC/non-AC", "Retry", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        DefaultListModel model;
        model = new DefaultListModel();
        for (Car car : cars) {
            if (car.getType() == carType && car.isAC() == isAC) {
                model.addElement(car.getID());
            }
        }
        dClist.setModel(model);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseClicked
        Car.Type carType = Car.Type.valueOf(rCtype.getSelectedItem().toString());
        boolean isAC;
        if (rCAC.isSelected()) {
            isAC = true;
        } else {
            if (rCnAC.isSelected()) {
                isAC = false;
            } else {
                JOptionPane.showMessageDialog(null, "Please select AC/non-AC", "Retry", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        try {
            pickUPDate = df.parse(pUD.getText() + " " + pUT.getText());
            // TODO add your handling code here:
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Enter valid Pickup Date(dd/mm/yyyy) and time(hh:mm:ss)", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
            //Logger.getLogger(TAGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            expreturnDate = df.parse(eRD.getText() + " " + eRT.getText());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Enter valid Return Date(dd/mm/yyyy) and time(hh:mm:ss)", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
            //Logger.getLogger(TAGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        searchCar(carType, isAC, pickUPDate, expreturnDate, rCtable);
    }//GEN-LAST:event_jButton9MouseClicked

    private void jButton8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseClicked
        rCsessionIDno.setText(String.valueOf(rentCar(Integer.parseInt(rCcarID.getText()), pickUPDate, expreturnDate, rCtable)));
        int ID = Integer.parseInt(rCcarID.getText());
        for (Car car : cars) {
            if (car.getID() == ID) {
                if (car.getState() != Car.State.REPAIR) {
                    car.setState(Car.State.RENTED);
                    DefaultTableModel model = (DefaultTableModel) allTable.getModel();
                    int numRow = model.getRowCount();
                    if (numRow != 0) {
                        for (int i = numRow - 1; i >= 0; i--) {
                            if (Integer.parseInt(model.getValueAt(i, 2).toString()) == ID) {
                                model.setValueAt(Car.State.RENTED.toString(), i, 3);
                                break;
                            }
                        }
                    }
                    //car.setTotalRepairCost((float) (car.getTotalRepairCost() + 300 + Math.random() % 3500));
                    writeFile(fileName);
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, "Car sent for repair.", "Output", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Car sent for repair.", "Output", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton8MouseClicked

    private void cRconfirmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cRconfirmMouseClicked
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date returnDate;
        try {
            returnDate = df.parse(cRRD.getText() + " " + cRRT.getText());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Enter valid Return Date(dd/mm/yyyy) and time(hh:mm:ss)", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
            //Logger.getLogger(TAGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        Float cost = returnCar(Integer.parseInt(cRcarID.getText()), Integer.parseInt(cRsessionID.getText()), Integer.parseInt(cRodom.getText()), returnDate);
        if (cost >= 0) {
            cRPayment.setText(cost + " to pay.");
        } else {
            cRPayment.setText((-cost) + " to be refunded.");
        }
        writeFile(fileName);
    }//GEN-LAST:event_cRconfirmMouseClicked

    private void aCcreateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aCcreateMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_aCcreateMouseEntered

    private void jButton10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseClicked
        JOptionPane.showMessageDialog(null, "Make the payment to Mr. Abinash :)", "Output", JOptionPane.INFORMATION_MESSAGE);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        if ("".equals(sFPcarID.getText())) {
            JOptionPane.showMessageDialog(null, "Enter an ID", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int ID = Integer.parseInt(sFPcarID.getText());
        for (Car car : cars) {
            if (car.getID() == ID) {
                if (car.getState() == Car.State.REPAIR) {
                    car.setState(Car.State.AVAILABLE);
                    DefaultTableModel model = (DefaultTableModel) allTable.getModel();
                    int numRow = model.getRowCount();
                    if (numRow != 0) {
                        for (int i = numRow - 1; i >= 0; i--) {
                            if (Integer.parseInt(model.getValueAt(i, 2).toString()) == ID) {
                                model.setValueAt(Car.State.AVAILABLE.toString(), i, 3);
                            }
                        }
                    }
                    //car.setTotalRepairCost((float) (car.getTotalRepairCost() + 300 + Math.random() % 3500));
                    writeFile(fileName);
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, "Car not sent for repair.", "Output", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Car not found!", "Result", JOptionPane.INFORMATION_MESSAGE);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TAGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TAGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TAGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TAGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TAGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel aC;
    private javax.swing.JRadioButton aCAC;
    private javax.swing.JTextField aCID;
    private javax.swing.JButton aCcreate;
    private javax.swing.JRadioButton aCnAC;
    private javax.swing.JTextField aCprice;
    private javax.swing.JComboBox aCtype;
    private javax.swing.JButton aS;
    private javax.swing.JTable allTable;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JButton cR;
    private javax.swing.JLabel cRPayment;
    private javax.swing.JTextField cRRD;
    private javax.swing.JTextField cRRT;
    private javax.swing.JTextField cRcarID;
    private javax.swing.JButton cRconfirm;
    private javax.swing.JTextField cRodom;
    private javax.swing.JTextField cRsessionID;
    private javax.swing.JPanel company;
    private javax.swing.JTabbedPane comtp;
    private javax.swing.JPanel consumer;
    private javax.swing.JPanel container;
    private javax.swing.JPanel dC;
    private javax.swing.JRadioButton dCAC;
    private javax.swing.JTextField dCID;
    private javax.swing.JList dClist;
    private javax.swing.JRadioButton dCnAC;
    private javax.swing.JButton dCsell;
    private javax.swing.JComboBox dCtype;
    private javax.swing.JTextField eRD;
    private javax.swing.JTextField eRT;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JPanel home;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel mD;
    private javax.swing.JTextField mDID;
    private javax.swing.JRadioButton mDaCAC;
    private javax.swing.JRadioButton mDaCnAC;
    private javax.swing.JTextField mDadvance;
    private javax.swing.JButton mDget;
    private javax.swing.JList mDlist;
    private javax.swing.JTextField mDmileage;
    private javax.swing.JTextField mDminHour;
    private javax.swing.JTextField mDpH;
    private javax.swing.JTextField mDpKM;
    private javax.swing.JComboBox mDtype;
    private javax.swing.JButton mDupdate;
    private javax.swing.JTextField pUD;
    private javax.swing.JTextField pUT;
    private javax.swing.JRadioButton rCAC;
    private javax.swing.JTextField rCcarID;
    private javax.swing.JRadioButton rCnAC;
    private javax.swing.JLabel rCsessionIDno;
    private javax.swing.JTable rCtable;
    private javax.swing.JComboBox rCtype;
    private javax.swing.JPanel s;
    private javax.swing.JButton sFP;
    private javax.swing.JTextField sFPcarID;
    private javax.swing.JPanel sFR;
    private javax.swing.JButton sGet;
    private javax.swing.JTextField sID;
    private javax.swing.JTable sTable;
    // End of variables declaration//GEN-END:variables
}
