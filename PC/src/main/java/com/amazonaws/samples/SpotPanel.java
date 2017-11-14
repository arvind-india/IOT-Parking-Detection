package com.amazonaws.samples;// Created by Austin Patel
// 10/21/2017

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SpotPanel extends JComponent {

    private int startX, startY, curX, curY;
    private ArrayList<ParkingSpot> spots = new ArrayList<>();
    private Image parkingImage;
    private boolean valid;
    private SpotMakerInterface frame;
    private int selectedIndex = -1;

    public SpotPanel(SpotMakerInterface frame) {
        this.frame = frame;

        setLayout(new BorderLayout());

//        try {
////            parkingImage =  new ImageIcon(ImageIO.read(new File("res/top parking.jpg"))).getImage();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        parkingImage = S3Sample.getImage();


        initMouseListeners();
    }

    public void deleteSpot(int index) {
        spots.remove(index);
        frame.refresh();
        repaint();
    }

    private void initMouseListeners() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                valid = true;

                frame.resetSelection();
                selectedIndex = -1;

            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if (e.getX() <= getWidth() && e.getY() <= getHeight() &&
                        e.getX() >= 0 && e.getY() >= 0) {
                    spots.add(new ParkingSpot(startX, startY, e.getX(), e.getY()));
                    frame.refresh();
                }

                valid = false;
                repaint();

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                repaint();
                curX = e.getX();
                curY = e.getY();
            }
        });
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        repaint();
    }

    public int getWidth() {
        return parkingImage.getWidth(null);
    }

    public int getHeight() {
        return parkingImage.getHeight(null);
    }

    public ArrayList<ParkingSpot> getSpots() {
        return spots;
    }

    @Override
    public void paintComponent(Graphics graphics){
        Graphics2D g = (Graphics2D) graphics;

        g.drawImage(parkingImage, 0, 0, null);

        g.setStroke(new BasicStroke(4));

        ParkingSpot curSpot = new ParkingSpot(startX, startY, curX, curY);
        if (valid) {
            g.setColor(Color.GREEN);
            g.drawRect(curSpot.getX(), curSpot.getY(), curSpot.getW(), curSpot.getH());
        }

        for (int i = 0; i < spots.size(); i++) {
            ParkingSpot spot = spots.get(i);

            if (i == selectedIndex)
                g.setColor(Color.BLUE);
            else
                g.setColor(Color.RED);

            g.drawRect(spot.getX(), spot.getY(), spot.getW(), spot.getH());
        }
    }
}
