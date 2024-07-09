package com.yourpackage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Objects;


public class RoomsPage {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private JPanel roomsPanel;
    private JFrame frame;

    public void createAndShowGUI(String username) {
        frame = new JFrame();
        frame.setTitle("Hokm Rooms page");
        frame.setSize(600, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(255, 255, 255));

        frame.getContentPane().setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridwidth = 1;
        gbc.gridy = 0;
        gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Username : " + username);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        headerPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        int matchWon = 0;
        JLabel matchWonLabel = new JLabel("Match won : " + matchWon);
        matchWonLabel.setFont(new Font("Arial", Font.BOLD, 15));
        headerPanel.add(matchWonLabel, gbc);

        gbc.gridx = 2;
        int matchLoses = 0;
        JLabel matchLosesLabel = new JLabel("Match loses : " + matchLoses);
        matchLosesLabel.setFont(new Font("Arial", Font.BOLD, 15));
        headerPanel.add(matchLosesLabel, gbc);

        gbc.gridx = 3;
        JButton newGameButton = new JButton();
        configureButton(newGameButton, "New Game");
        headerPanel.add(newGameButton, gbc);

        newGameButton.addActionListener(e -> showPlayerOptions(username));

        frame.add(headerPanel, BorderLayout.NORTH);

        roomsPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(roomsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
        fetchAndDisplayRooms(username);

        Timer timer = new Timer(2000, e -> fetchAndDisplayRooms(username));
        timer.start();
    }

    private void configureButton(JButton button, String text) {
        button.setBorderPainted(false);
        button.setText(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBackground(Color.red);
        button.setForeground(Color.white);
    }

    private void fetchAndDisplayRooms(String username) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("GET_ROOMS");
            out.flush();

            List<Room> rooms = (List<Room>) in.readObject();

            roomsPanel.removeAll();


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            for (Room room : rooms) {
                if (room.getCreator() != null) {
                    JPanel roomPanel = createRoomPanel(room, username);
                    roomsPanel.add(roomPanel, gbc);
                    gbc.gridy++;
                }
            }


            for (int i = rooms.size(); i < 3; i++) {
                JPanel emptyRoomPanel = createEmptyRoomPanel();
                roomsPanel.add(emptyRoomPanel, gbc);
                gbc.gridy++;
            }

            roomsPanel.revalidate();
            roomsPanel.repaint();

        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error fetching rooms: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showPlayerOptions(String username) {
        JFrame optionFrame = new JFrame("Options");
        optionFrame.setSize(400,430);
        optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        optionFrame.setLocationRelativeTo(null);
        optionFrame.setResizable(false);
        optionFrame.setLayout(null);
        optionFrame.getContentPane().setBackground(Color.white);

        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        panel1.setBounds(49,52,303,50);
        panel1.setBackground(Color.decode("#f0f0f0"));

        JLabel roundTextLabel = new JLabel();
        roundTextLabel.setText("Round : ");
        roundTextLabel.setFont(new Font("Arial", Font.BOLD, 14 ));
        roundTextLabel.setBounds(17,20,60,15);
        panel1.add(roundTextLabel);

        JRadioButton round_3 = new JRadioButton();
        round_3.setText("3");
        round_3.setBounds(96,20,50,15);
        panel1.add(round_3);

        JRadioButton round_5 = new JRadioButton();
        round_5.setText("5");
        round_5.setBounds(96+74,20,50,15);
        panel1.add(round_5);

        JRadioButton round_7 = new JRadioButton();
        round_7.setText("7");
        round_7.setBounds(96+2*(74),20,50,15);
        panel1.add(round_7);

        ButtonGroup group = new ButtonGroup();
        group.add(round_3);
        group.add(round_5);
        group.add(round_7);

        optionFrame.add(panel1);


        JPanel panel2 = new JPanel();
        panel2.setLayout(null);
        panel2.setBounds(49,116,303,50);
        panel2.setBackground(Color.decode("#f0f0f0"));

        JLabel playersTextLabel = new JLabel();
        playersTextLabel.setText("Players : ");
        playersTextLabel.setFont(new Font("Arial", Font.BOLD, 14 ));
        playersTextLabel.setBounds(17,20,100,15);
        panel2.add(playersTextLabel);

        JRadioButton player_2 = new JRadioButton();
        player_2.setText("2_player");
        player_2.setBounds(106,20,100,15);
        panel2.add(player_2);

        JRadioButton player_4 = new JRadioButton();
        player_4.setText("4_player");
        player_4.setBounds(204,20,100,15);
        panel2.add(player_4);

        ButtonGroup group2 = new ButtonGroup();
        group2.add(player_2);
        group2.add(player_4);

        optionFrame.add(panel2);

        JPanel panel3 = new JPanel();
        panel3.setLayout(null);
        panel3.setBounds(49,178,303,50);
        panel3.setBackground(Color.decode("#f0f0f0"));

        JLabel chatTextLabel = new JLabel();
        chatTextLabel.setText("Chat : ");
        chatTextLabel.setFont(new Font("Arial", Font.BOLD, 14 ));
        chatTextLabel.setBounds(17,20,100,15);
        panel3.add(chatTextLabel);

        JRadioButton ON = new JRadioButton();
        ON.setText("ON");
        ON.setBounds(106 ,20,100,15);
        panel3.add(ON);

        JRadioButton OFF = new JRadioButton();
        OFF.setText("OFF");
        OFF.setBounds(204,20,100,15);
        panel3.add(OFF);

        ButtonGroup group3 = new ButtonGroup();
        group3.add(ON);
        group3.add(OFF);

        optionFrame.add(panel3);

        JPanel panel4 = new JPanel();
        panel4.setLayout(null);
        panel4.setBounds(49,242,303,50);
        panel4.setBackground(Color.decode("#f0f0f0"));

        JLabel RoomPasswordLabel = new JLabel();
        RoomPasswordLabel.setText("Room Password : ");
        RoomPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14 ));
        RoomPasswordLabel.setBounds(17,20,150,15);
        panel4.add(RoomPasswordLabel);

        JTextField roomPasswordTextField = new JTextField();
        roomPasswordTextField.setBounds(163,12 ,112,26);
        roomPasswordTextField.setColumns(10);
        panel4.add(roomPasswordTextField);

        optionFrame.add(panel4);

        JButton cancelButton = new JButton();
        cancelButton.setText("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14 ));
        cancelButton.setBackground(Color.black);
        cancelButton.setBounds(49,320,144,30);
        cancelButton.setForeground(Color.white);
        cancelButton.setBorder(null);

        optionFrame.add(cancelButton);

        JButton OKButton = new JButton();
        OKButton.setText("OK");
        OKButton.setFont(new Font("Arial", Font.BOLD, 14 ));
        OKButton.setBackground(Color.red);
        OKButton.setBounds(206,320,144,30);
        OKButton.setForeground(Color.white);
        OKButton.setBorder(null);

        optionFrame.add(OKButton);



        OKButton.addActionListener(e -> {
            if (player_2.isSelected())
            {
                optionFrame.dispose();
                createRoom(username, frame, 2);
            }
            else if (player_4.isSelected())
            {
                optionFrame.dispose();
                createRoom(username, frame, 4);
            }
        });

        optionFrame.setVisible(true);
    }

    private void createRoom(String username, JFrame frame, int maxPlayers) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("CREATE_ROOM:" + username + ":" + maxPlayers);
            out.flush();

            String response = (String) in.readObject();
            if (response.startsWith("ROOM_CREATED")) {
                JOptionPane.showMessageDialog(null, "Room created successfully!");
                frame.dispose();
                new RoomView(username, username,maxPlayers);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to create room! Response: " + response);
            }

            fetchAndDisplayRooms(username);

        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error creating room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createRoomPanel(Room room, String username) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 200));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel roomCreatorLabel = new JLabel();
        roomCreatorLabel.setText("Room creator : " + (room.getCreator() != null ? room.getCreator() : "Unknown"));
        roomCreatorLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(roomCreatorLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JButton joinRoomButton = new JButton();
        configureButton(joinRoomButton, "Join room");
        panel.add(joinRoomButton, gbc);

        if (room.getCreator() == null || room.isFull()) {
            joinRoomButton.setEnabled(false);
        } else {
            joinRoomButton.addActionListener(e -> {

                frame.dispose();
                new RoomView(username, room.getCreator(),room.getMaxPlayers());
            });
        }

        List<String> players = room.getPlayers();
        for (int i = 0; i < room.getMaxPlayers(); i++) {
            gbc.gridy = 1;
            gbc.gridx = i;
            JLabel playerImageLabel = new JLabel();
            ImageIcon playerIcon = (i < players.size() && room.getCreator() != null)
                    ? new ImageIcon(Objects.requireNonNull(getClass().getResource("/data/boy.png")))
                    : new ImageIcon(Objects.requireNonNull(getClass().getResource("/data/nullPlayer.png")));

            if (playerIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
            }
            playerImageLabel.setIcon(playerIcon);
            panel.add(playerImageLabel, gbc);

            gbc.gridy = 2;
            JLabel playerLabel = new JLabel();
            playerLabel.setText(i < players.size() ? players.get(i) : "Empty Slot");
            panel.add(playerLabel, gbc);
        }

        return panel;
    }

    private JPanel createEmptyRoomPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 200));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel roomCreatorLabel = new JLabel("Room creator: Empty");
        roomCreatorLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(roomCreatorLabel, gbc);

        for (int i = 0; i < 4; i++) {
            gbc.gridy = 1;
            gbc.gridx = i;
            JLabel playerImageLabel = new JLabel();
            ImageIcon playerIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/data/nullPlayer.png")));

            if (playerIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
            }
            playerImageLabel.setIcon(playerIcon);
            panel.add(playerImageLabel, gbc);

            gbc.gridy = 2;
            JLabel playerLabel = new JLabel("Empty Slot");
            panel.add(playerLabel, gbc);
        }

        return panel;
    }
}
