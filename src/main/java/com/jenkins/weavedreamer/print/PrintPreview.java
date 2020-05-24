/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jenkins.weavedreamer.print;

/**
 * @author David
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

public class PrintPreview extends JScrollPane {

    private static final long serialVersionUID = 1L;
    private Pageable mPageable = null;
    private double mScale = 1.0;
    private Page mPages[] = null;
    private CardLayout mCardLayout = new CardLayout();
    private JPanel mMainPanel = new JPanel(mCardLayout);
    private int mCurrentPage = 1;

    private JPanel mControls = null;
    private JButton mPrevButton = null;
    private JButton mNextButton = null;
    private JComboBox<String> mPagesCombo = null;
    private JSlider mZoomSlider = null;

    public PrintPreview(Pageable pg) {
        this.mPageable = pg;
        createPreview();
    }

    public PrintPreview(final Printable pr, final PageFormat p) {
        this.mPageable = new Pageable() {
            public int getNumberOfPages() {
                Graphics g = new java.awt.image.BufferedImage(2, 2,
                        java.awt.image.BufferedImage.TYPE_INT_RGB)
                        .getGraphics();
                int n = 0;
                try {
                    while (pr.print(g, p, n) == Printable.PAGE_EXISTS)
                        n++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return n;
            }

            public PageFormat getPageFormat(int x) {
                return p;
            }

            public Printable getPrintable(int x) {
                return pr;
            }
        };
        createPreview();
    }

    private void createPreview() {
        mPages = new Page[mPageable.getNumberOfPages()];
        PageFormat pf = mPageable.getPageFormat(0);
        Dimension size = new Dimension((int) pf.getPaper().getWidth(), (int) pf
                .getPaper().getHeight());
        if (pf.getOrientation() != PageFormat.PORTRAIT)
            size = new Dimension(size.height, size.width);

        for (int i = 0; i < mPages.length; i++) {
            mPages[i] = new Page(i, size);
            mMainPanel.add(String.valueOf(i + 1), mPages[i]);
        }

        setViewportView(mMainPanel);
        mPages[mCurrentPage - 1].refreshScale();
    }

    private void setupControls() {

        mControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        mPrevButton = new JButton("<<");
        mNextButton = new JButton(">>");
        mPagesCombo = new JComboBox<String>();
        mZoomSlider = new JSlider();
        for (int i = 0; i < mPages.length; i++)
            mPagesCombo.addItem(String.valueOf(i + 1));

        mPrevButton.setEnabled(false);
        mNextButton.setEnabled(mPages.length > 1);

        mPrevButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                changePage(mCurrentPage - 1);
            }
        });

        mNextButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                changePage(mCurrentPage + 1);
            }
        });

        mPagesCombo.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                changePage(Integer.parseInt((String) mPagesCombo.getSelectedItem()));
            }
        });

        mZoomSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                double v = (double) mZoomSlider.getValue() / 10.0;
                zoom(v);
            }
        });

        mZoomSlider.setPaintTicks(true);
        //mZoomSlider.setPaintLabels(true);
        mZoomSlider.setMinimum(0);
        mZoomSlider.setMaximum(70);
        mZoomSlider.setValue(10);
        mZoomSlider.setSnapToTicks(true);
        mZoomSlider.setMinorTickSpacing(5);
        mZoomSlider.setMajorTickSpacing(10);

        mControls.add(mPrevButton);
        mControls.add(mPagesCombo);
        mControls.add(mNextButton);
        mControls.add(mZoomSlider);
    }

    /**
     * Method lazy initializes JPanel with controls (if not initialized yet) and returns it
     * @return panel with controls
     */
    public JPanel getControls() {
        if (mControls == null)
            setupControls();

        return mControls;
    }

    /**
     * Method changes current page, updates controls (if displayed) and returns true on success
     * This method is safe, it checks if passed page number exists.
     *
     * @param page number
     * @return true if page was changed
     */
    public boolean changePage(int page) {

        if (mPages.length < page || page < 1 || mCurrentPage == page)
            return false;

        mCardLayout.show(mMainPanel, String.valueOf(page));
        getCurrentPageComponent().refreshScale();
        mCurrentPage = page;
        if (mPrevButton != null && mNextButton != null) {
            mPrevButton.setEnabled(mCurrentPage != 1);
            mNextButton.setEnabled(mPages.length != page);
        }
        if (mPagesCombo != null)
            mPagesCombo.setSelectedItem(String.valueOf(page));

        validate();
        return true;
    }

    /**
     * Method checks if next page exists before processing
     *
     * @return number of pages
     */
    public int pages() {
        return mPages.length;
    }

    /**
     * Method checks if previous page exists before processing.
     *
     * @return true if page changed
     */
    public boolean previousPage() {
        return changePage(mCurrentPage - 1);
    }


    /**
     * @return true if page changed
     */
    public boolean nextPage() {
        return changePage(mCurrentPage + 1);
    }

    /**
     * Prints whole document
     */
    public void print() {
        try {
            PrinterJob pj = PrinterJob.getPrinterJob();
            pj.defaultPage(mPageable.getPageFormat(0));
            pj.setPageable(mPageable);
            if (pj.printDialog())
                pj.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(),
                    "Error in Printing", 1);
        }
    }

    /**
     * Prints currently selected page
     */
    public void printCurrentPage() {
        try {
            PrinterJob pj = PrinterJob.getPrinterJob();
            pj.defaultPage(mPageable.getPageFormat(0));
            pj.setPrintable(new PsuedoPrintable());
            javax.print.attribute.HashPrintRequestAttributeSet pra = new javax.print.attribute.HashPrintRequestAttributeSet();
            if (pj.printDialog(pra))
                pj.print(pra);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(),
                    "Error in Printing", 1);
        }
    }

    /**
     * Returns currently selected Page component
     *
     * @return Page component
     */
    public Page getCurrentPageComponent() {
        return mPages[mCurrentPage - 1];
    }

    /**
     * Return currently selected page's number
     *
     * @return current page number
     */
    public int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     *
     *
     * @param zoom double value greater than 0
     */
    public void zoom(double zoom) {
        double temp = zoom;
        if (temp == mScale)
            return;
        if (temp == 0)
            temp = 0.01;


        mScale = temp;
        getCurrentPageComponent().refreshScale();
        this.validate();
    }

    class Page extends JLabel {

        private static final long serialVersionUID = 1L;
        private final int mPageNum;
        private final PageFormat mPageFormat;
        private BufferedImage mImage = null;
        private Dimension mSize = null;

        public Page(int x, Dimension size) {
            setHorizontalAlignment(JLabel.CENTER);
            mSize = size;
            mImage = new java.awt.image.BufferedImage(size.width, size.height,
                    java.awt.image.BufferedImage.TYPE_INT_RGB);
            mPageNum = x;
            mPageFormat = mPageable.getPageFormat(mPageNum);


            Graphics g = mImage.getGraphics();
            Color c = g.getColor();
            g.setColor(Color.white);
            g.fillRect(0, 0, (int) mPageFormat.getWidth(), (int) mPageFormat.getHeight());
            g.setColor(c);
            try {
                //g.clipRect(0, 0, (int) pf.getWidth(), (int) pf.getHeight());
                mPageable.getPrintable(mPageNum).print(g, mPageFormat, mPageNum);
                g.setColor(Color.black);
                g.drawRect(0, 0, (int) mPageFormat.getWidth() - 1, (int) mPageFormat.getHeight() - 1);
            } catch (Exception ex) {
            }
            setIcon(new ImageIcon(mImage));
        }

        public void refreshScale() {

            if (mScale != 1.0) {
                int w = (int) (mSize.width * mScale);
                int h = (int) (mSize.height * mScale);
                setIcon(new ImageIcon(
                        mImage.getScaledInstance(w, h, BufferedImage.SCALE_FAST)));
            } else
                setIcon(new ImageIcon(mImage));

            validate();
        }
    }

    class PsuedoPrintable implements Printable {
        public int print(Graphics g, PageFormat fmt, int index) {
            if (index > 0)
                return Printable.NO_SUCH_PAGE;
            int n = mCurrentPage - 1;
            try {
                return mPageable.getPrintable(n).print(g, fmt, n);
            } catch (Exception ex) {
            }
            return Printable.PAGE_EXISTS;
        }
    }
}
