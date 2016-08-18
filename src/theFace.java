import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class theFace {

    JFrame frame;
    JButton chooseButton;
    JButton convertButton;
    File chosenFile;
    int startPos=0;
    int lmostPicX;
    int lmostPicY;
    JLabel img;
    int yMin, yMax;
    Color tempC;
    BufferedImage readImage;
    BufferedImage vecImage;
    BufferedImage cImg;
    BufferedImage compImg;
    File compImgFile;
    File output;
    int lmostx=0, lmosty=0;
    int check;
    int rMostX, lMostX, tMostY;
    BufferedImage bim;
    int startX, startY;
    Color r1, rT, rB, rL, rR, rTL, rTR, rBL, rBR;
    int endHeight = 10;
    int prevEndHeight;
    int checkFori=0;
    JTextArea textArea;
    double addX,addY, countX,countY, First, Last,prevCase;

    String[] alphas = {"A-", "B-", "C-", "C2", "D-", "D2" , "E-" ,"E2", "F-", "F2", "G-","G2", "H-","H2", "I-","I2", "J-", "J2", "K-","K2", "L-","L2", "M-","M2", "N-","N2", "O-","O2", "P-","P2", "Q-","Q2", "R-","R2", "S-","S2", "T-","T2", "U-","U2", "V-","V2", "W-","W2", "X-","X2", "Y-","Y2", "Z-","Z2",
            "a", "b", "c", "d" , "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    ArrayList<Integer> xCoords = new ArrayList<Integer>();      //array list for storing all the boundary x coordinates
    ArrayList<Integer> yCoords = new ArrayList<Integer>();      //array list for storing all the boundary y coordinates


    public static void main(String[] args) {
        theFace window = new theFace();
        window.method();
    }

    void method() {
        frame = new JFrame();
        frame.setSize(1000, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel borderPanel = new JPanel();
        borderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        JPanel subPanel2 = new JPanel();
        masterPanel.add(panel1);
        masterPanel.add(panel2);
        JLabel imageLabel = new JLabel("Image: ");
        img = new JLabel("");
        panel1.add(imageLabel);
        panel1.add(img);

        chooseButton = new JButton("Choose Image");
        chooseButton.addActionListener(new chooseListener());
        convertButton = new JButton("Convert");
        convertButton.addActionListener(new convertListener());
        convertButton.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), new convertListener());
        subPanel2.add(chooseButton);
        subPanel2.add(convertButton);
        panel2.add(subPanel2, BorderLayout.SOUTH);
        textArea = new JTextArea(20, 35);
        borderPanel.add(masterPanel);
        borderPanel.add(textArea);
        textArea.setLineWrap(true);
        frame.getContentPane().add(borderPanel);
        frame.setVisible(true);
    }

    class chooseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            xCoords.clear();
            yCoords.clear();
            textArea.setText("");
            FileDialog openFile = new FileDialog(new JFrame(), "Select Image");
            openFile.setFile("*.jpg;*png;*jpeg;*bmp");
            openFile.setVisible(true);
            openFile.getFile();
            File f = new File(openFile.getDirectory() + openFile.getFile());

            ImageIcon chosenImage = new ImageIcon(f.getAbsolutePath());
            int width = chosenImage.getIconWidth();
            int height = chosenImage.getIconHeight();
            //scaling start
            while (true) {
                if (width >= 600 || height >= 600) {
                    width = width / 2;
                    height = height / 2;
                }
                if (height < 600 && width < 600) {
                    System.out.println("Scaled width = " + width + "scaled height = " + height);
                    break;
                }
            }
            //scaling end
            Image scaledImage = chosenImage.getImage();
            scaledImage = scaledImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(scaledImage);
            img.setIcon(newIcon);
            chosenFile = f;
            System.out.println(chosenFile.getAbsolutePath());
        }
    }

    class convertListener implements ActionListener {
        processImage pi = new processImage();

        @Override
        public void actionPerformed(ActionEvent event) {
            xCoords.clear();
            yCoords.clear();
            startX = 0;
            startY = 0;
            //prevEndHeight = 0;
            //endHeight = 0;
            System.out.println("\n call from actionPerformed");
            pi.toBinary();
            pi.convert();
        }
    }

    class processImage extends JLabel {
        int lMostXOfLetter = 0, lMostYOfLetter = 0, bottomMost = 0;
        void toBinary() {

            File chosenFile1;
            chosenFile1 = new File(chosenFile.getAbsolutePath());
            File toConvert = new File(chosenFile1.getAbsolutePath());
            Color c, c1;
            BufferedImage bwImage;
            try {
                BufferedImage image1 = ImageIO.read(toConvert);
                bwImage = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
                bwImage.createGraphics().drawImage(image1, 0, 0, Color.WHITE, null);
                /*for(int i=0;i<image1.getWidth();i++){
                    for(int j=0;j<image1.getHeight();j++){
                        c = new Color(image1.getRGB(i,j));
                        if(c.getRed()>230 && c.getGreen()>230 && c.getBlue()>230){

                            c1 = new Color(255,255,255);
                            bwImage.setRGB(i, j, c1.getRGB());
                        }
                        else{
                            c1 = new Color(0,0,0);
                            bwImage.setRGB(i,j,c1.getRGB());
                        }
                    }
                }*/
                File output = new File(chosenFile.getParent() + "\\" + "b&w.png");
                ImageIO.write(bwImage, "png", output);
                //chosenFile1.deleteOnExit();
            } catch (Exception ex) {}
        }

        void convert() {
            File bnw = new File(chosenFile.getParent() + "\\" + "b&w.png");
            try {
                readImage = ImageIO.read(bnw);
            }catch(IOException io){}
            endHeight = readImage.getHeight();
            prevEndHeight = 20;
            System.out.println("Image width = " +readImage.getWidth() + " Image height = " +readImage.getHeight());



                lMost();

                mapPixel();
                draw();
                recog();


        }

        void setColor(int x, int y) {
            r1 = new Color(readImage.getRGB(x, y));
            rL = new Color(readImage.getRGB(x - 1, y));
            rR = new Color(readImage.getRGB(x + 1, y));
            rT = new Color(readImage.getRGB(x, y - 1));
            rTR = new Color(readImage.getRGB(x + 1, y - 1));
            rTL = new Color(readImage.getRGB(x - 1, y - 1));
            rB = new Color(readImage.getRGB(x, y + 1));
            rBR = new Color(readImage.getRGB(x + 1, y + 1));
            rBL = new Color(readImage.getRGB(x - 1, y + 1));
            //System.out.println(rL.getRed() + " " + rR.getRed() +" " + rT.getRed() + " " + rB.getRed());
        }

        void lMost() {
            if(lmosty + endHeight > readImage.getHeight()) {
                while (lmosty + endHeight > readImage.getHeight()) {
                    endHeight--;
                }
            }
            Color pixelColor;
            boolean foundPixel = false, endOfImage = false;
            int spaceChecker = 0;
            for(int i = lmostx; i < readImage.getWidth() - 1; i++){
                spaceChecker++;
                for(int j = lmosty; j < endHeight - 1; j++){
                    pixelColor = new Color(readImage.getRGB(i, j));
                    if(pixelColor.getRed() == 0){
                        lmostx = i;
                        lmosty = j;
                        setColor(i, j);
                        foundPixel = true;
                        break;
                    }
                    if(i == readImage.getWidth() - 1 && j != readImage.getHeight() - 2){    //  end of the line but not the end of image
                        i = 0;
                        lmosty = endHeight + 1;
                        endHeight = endHeight + 11;
                    }
                    if(i == readImage.getWidth() - 2 && j == readImage.getHeight() - 2){    //  end of image
                        System.out.println("END OF IMAGE");
                        endOfImage = true;
                        break;
                    }
                }
                if(foundPixel)
                    break;
                if(spaceChecker >= 5 && lmostx != 0){
                    textArea.append(" ");
                }
                if(endOfImage)
                    break;
            }

        }

        void mapPixel() {
            System.out.println(lmostx + " mapME! " + lmosty);
            if(prevEndHeight==readImage.getHeight())
                prevEndHeight=0;
            int x = lmostx, y = lmosty;
            lMostX = x;
            int traversalChecker = 0;       //  to check if the whole letter's boudary has been traversed
            int left, right, up, down;
            left = right = up = down = 0;
            int xCount = 0, yCount = 0, xSum = 0, ySum = 0;
            Color white = new Color(255, 255, 255);
            lMostXOfLetter = x;
            lMostYOfLetter = y;
            while(true) {
                setColor(x, y);

                if(rR.getRed() == 0 && ((rT.getRed() == 255) || (rTR.getRed() == 255 && up == 0))){
                    right = 1;                                      //  right = 1 for normal case for traversing right
                    if(left == 1 && yCount < 6 && yCount > 0 && down == 1){                               //  to detect 180 deg x axis direction change within 6 pixels of down traversal
                        xCoords.add(x);
                        yCoords.add(ySum / yCount);
                        System.out.println("Wrote x = " + x +" y = " + ySum / yCount);
                    }

                    if(rB.getRed() == 255 && rT.getRed() == 255){   //  to eliminate one pixel horizontal lines styling in fonts
                        System.out.println("removal");              //  like Times New Roman
                        while(rB.getRed() == 255 && rT.getRed() == 255){
                            readImage.setRGB(x, y, white.getRGB());
                            x++;
                            setColor(x, y);
                        }
                        if(traversalChecker == 0) {
                            lMostXOfLetter = x;
                            lMostYOfLetter = y;
                        }
                        System.out.println("lMostXOfLetter = " +lMostXOfLetter + " lMostYOfLetter = " +lMostYOfLetter);
                    }

                    xSum = xCount = 0;
                    System.out.println("right");


                    left = 0;
                    while(rR.getRed() == 0 && (rT.getRed() == 255 || rTR.getRed() == 255)){  //  loop for traversing right until a black upper pixel
                        xSum += x;                                                           //  or a white right pixel is encountered
                        traversalChecker = 1;
                        x++;
                        rMostX = x;
                        xCount++;
                        setColor(x, y);
                        System.out.println("x : " +x + "y: " +y);
                        if(rB.getRed() == 255 && rBR.getRed() == 255 && rTR.getRed() == 255){  // to eliminate one pixel horizontal line styling
                            int tmpx = x-1;                               //  to preserve last valid x coordinate
                            x = tmpx + 1;
                            right = 2;
                            while(rB.getRed() == 255 && rBR.getRed() == 255 && rTR.getRed() == 255 && r1.getRed() == 0) {
                                readImage.setRGB(x, y, white.getRGB());
                                System.out.println("x : " +x + "y: " +y);
                                x++;
                                setColor(x, y);
                            }
                            rMostX = x;
                            x = tmpx;                                   //  to restore last valid x coordinate
                            System.out.println("tmpx = " +tmpx);
                        }

                    }
                    if(xCount >= 6){                                //  storing the avg x coordinates incase the number of pixels
                        xCoords.add(xSum / xCount);                 //  in straight horizontal line are >= 6
                        yCoords.add(y);

                        System.out.println("Wrote x = " + xSum/xCount +" y = " +y);
                        System.out.println("xSum = " + xSum + " xCount = " +xCount + " ySum = " +ySum +" yCount = " +yCount);
                        System.out.println("\ncoordinates:\n");
                        for (int w : xCoords) {
                            System.out.print(w + " ");
                        }
                        System.out.println("\n");
                        for (int e : yCoords) {
                            System.out.print(e + " ");
                        }
                    }

                }

                else if(rT.getRed() == 0 && (rL.getRed() == 255 || (rTL.getRed() == 255 && left == 0 && rBR.getRed() == 0) || (rTL.getRed() == 255 && right == 1 && rBR.getRed() == 255))){
                    up = 1;                                         //  up = 1 for normal case for traversing up
                    ySum = yCount = 0;
                    ySum += y;
                    yCount++;
                    System.out.println("up");
                    /*if(left == 1 && xCount < 6){
                        xCoords.add(xSum / xCount);
                        yCoords.add(y);
                        System.out.println("special case in up");
                    }*/
                    xSum =  xCount = 0;

                                                                    //  following is the loop for traversing up until a white upper pixel or
                                                                    //  a black left pixel is encountered
                    while((right == 1 && rT.getRed() == 0 && (rL.getRed() == 255 || rTL.getRed() == 255 )) || (left == 1 && rT.getRed() == 0 && rL.getRed() == 255)){
                        if(down == 2){                              // to remove black pixels when down traversal of one pixel wide vertical line has completed
                            System.out.println("down = 2 case");
                            down = 0;
                            while(rL.getRed() == 255 && rR.getRed() == 255){
                                System.out.println("x : " +x + "y: " +y);
                                readImage.setRGB(x, y, white.getRGB());
                                y--;
                                setColor(x, y);
                            }

                        }
                        y--;
                        ySum += y;
                        yCount++;
                        traversalChecker = 1;
                        System.out.println("x : " +x + "y: " +y);
                        setColor(x, y);

                        if(rL.getRed() == 255 && rR.getRed() == 255 && rT.getRed() == 0){  // to eliminate one pixel vertical line styling
                            int tmpy = y+1;                               //  to preserve last valid y coordinate
                            y = tmpy - 1;
                            up = 2;
                            while(rL.getRed() == 255 && rR.getRed() == 255 && r1.getRed() == 0) {
                                readImage.setRGB(x, y, white.getRGB());
                                System.out.println("x : " +x + "y: " +y);
                                y--;
                                setColor(x, y);
                            }
                            yMax = y;
                            y = tmpy;                                   //  to restore last valid y coordinate
                            System.out.println("tmpy = " +tmpy);
                        }
                        if(rT.getRed() == 255 && rR.getRed() == 255 && rL.getRed() == 255){         //for a single stranded pixel at the top, not a whole one pixel wide vertical line
                            xCoords.add(x);
                            yCoords.add(y);
                            System.out.println("Wrote x = " + x +" y = " +y);
                            System.out.println("xSum = " + xSum + " xCount = " +xCount + " ySum = " +ySum +" yCount = " +yCount);
                            System.out.println("\ncoordinates:\n");
                            for (int w : xCoords) {
                                System.out.print(w + " ");
                            }
                            System.out.println("\n");
                            for (int e : yCoords) {
                                System.out.print(e + " ");
                            }
                            readImage.setRGB(x, y, white.getRGB());
                            y++;
                        }

                    }
                    if(yCount >= 6){                                //  storing the avg y coordinates incase the number of pixels
                        xCoords.add(x);                             //  in straight line are >= 6
                        yCoords.add(ySum / yCount);
                        System.out.println("Wrote x = " + x +" y = " +y);
                        System.out.println("xSum = " + xSum + " xCount = " +xCount + " ySum = " +ySum +" yCount = " +yCount);
                        System.out.println("\ncoordinates:\n");
                        for (int w : xCoords) {
                            System.out.print(w + " ");
                        }
                        System.out.println("\n");
                        for (int e : yCoords) {
                            System.out.print(e + " ");
                        }

                    }

                }
                else if(rB.getRed() == 0 && (rR.getRed() == 255 || (rBR.getRed() == 255 && right == 0))){
                    down = 1;                                       //  down = 1 for normal case for traversing down

                    System.out.println("down");
                    if(right == 1 && xCount < 6 && xCount > 0 && up == 1){        //  to detect 180 deg y axis direction change within 6 pixels of right traversal
                        System.out.println("special addition to xCoords");
                        xCoords.add(xSum / xCount);
                        yCoords.add(y);
                        System.out.println("Wrote x = " + xSum / xCount +" y = " +y);
                        System.out.println("xSum = " + xSum + " xCount = " +xCount + " ySum = " +ySum +" yCount = " +yCount);
                        System.out.println("\ncoordinates:\n");
                        for (int w : xCoords) {
                            System.out.print(w + " ");
                        }
                        System.out.println("\n");
                        for (int e : yCoords) {
                            System.out.print(e + " ");
                        }
                    }
                    xCount = xSum = 0;
                    yCount = ySum = 0;
                    up = 0;
                    while(rB.getRed() == 0 && (rR.getRed() == 255 || rBR.getRed() == 255)){

                        y++;
                        if(y > endHeight)
                            endHeight = y;
                        traversalChecker = 1;
                        bottomMost = y;
                        ySum += y;
                        yCount++;
                        System.out.println("x : " +x + "y: " +y);
                        setColor(x, y);
                        if(rB.getRed() == 255){
                            down = 2;
                        }
                    }
                    if(yCount >= 6){                                //  storing the avg y coordinates incase the number of pixels
                        xCoords.add(x);                             //  in straight line are >= 6
                        yCoords.add(ySum / yCount);
                        System.out.println("Wrote x = " + x +" y = " +ySum / yCount);
                        System.out.println("xSum = " + xSum + " xCount = " +xCount + " ySum = " +ySum +" yCount = " +yCount);
                        System.out.println("\ncoordinates:\n");
                        for (int w : xCoords) {
                            System.out.print(w + " ");
                        }
                        System.out.println("\n");
                        for (int e : yCoords) {
                            System.out.print(e + " ");
                        }

                    }

                }
                else if(rL.getRed() == 0 && (rB.getRed() == 255 || rBL.getRed() == 255)){
                    left = 1;
                    if(right == 1 && yCount < 6 && down == 1){                                          //  to detect 180 deg x axis direction change within 6 pixels of down traversal
                        xCoords.add(x);
                        yCoords.add(ySum / yCount);
                        System.out.println("Wrote x = " + x +" y = " + ySum / yCount);
                    }
                    right = 0;
                    xSum =  xCount = 0;
                    System.out.println("left");
                    xSum += x;
                    xCount++;
                    System.out.println("x : " +x + "y: " +y);
                    while(rL.getRed() == 0 && (rB.getRed() == 255 || rBL.getRed() == 255)){
                        x--;
                        xSum += x;
                        xCount++;
                        setColor(x, y);
                        if(rT.getRed() == 255 && rB.getRed() == 255){
                            left = 2;
                            System.out.println("special left case");
                        }
                    }
                    if(xCount >= 6){                                //  storing the avg x coordinates incase the number of pixels
                        xCoords.add(xSum / xCount);                 //  in straight horizontal line are >= 6
                        yCoords.add(y);
                        System.out.println("Wrote x = " + xSum/xCount +" y = " +y);
                        System.out.println("xSum = " + xSum + " xCount = " +xCount + " ySum = " +ySum +" yCount = " +yCount);
                        System.out.println("\ncoordinates:\n");
                        for (int w : xCoords) {
                            System.out.print(w + " ");
                        }
                        System.out.println("\n");
                        for (int e : yCoords) {
                            System.out.print(e + " ");
                        }

                    }

                }
                if(x == lMostXOfLetter && y == lMostYOfLetter && traversalChecker == 1){
                    System.out.println("Traversal of letter complete");
                    lmostx = rMostX + 1;
                    lMost();
                    break;
                }
                try{
                    Thread.sleep(100);
                }
                catch(InterruptedException e){}
            }
            System.out.println("\ncoordinates:\n");
            for (int w : xCoords) {
                System.out.print(w + " ");
            }
            System.out.println("\n");
            for (int e : yCoords) {
                System.out.print(e + " ");
            }
            lmostx=startX;
            System.out.println("\nprevEndHeight = " + prevEndHeight);


            System.out.println("\nRightmost pixel of the letter  = " + rMostX + " leftmost pixel of the letter = " +lMostX + " topmost pixel = " + tMostY);
        }
        int startPos2 = 0;
        void draw(){


            vecImage = new BufferedImage(readImage.getWidth(),readImage.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = vecImage.createGraphics();
            output = new File(chosenFile.getParent() + "\\vectastic.bmp");
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0,0,readImage.getWidth(), readImage.getHeight());
            g2d.setColor(Color.BLACK);

            for(int k=startPos;k<xCoords.size()-1;k++){
                if(xCoords.get(k) != -1)
                    if(xCoords.get(k+1)!=-1)
                        g2d.drawLine(xCoords.get(k),yCoords.get(k),xCoords.get(k+1),yCoords.get(k+1));
                    else if(xCoords.get(k) == -1)
                        startPos = k+1;
            }
            try {
                ImageIO.write(vecImage, "bmp", output);
            }catch(IOException io){}
            System.out.println("\nrightMost x: " + startX + " endheight= " + endHeight);
        }

        void recog(){

            File tmpcImgFile = new File(chosenFile.getParent() + "\\tmpcImg.png");

            System.out.println("\nyMin = " + yMin + " yMax = " + yMax +" xMin = "+ lMostX + "xMax = " + rMostX);
            float pixelRatio;
            float totalBlackPixels, totalPixels;

            for(int i=0;i<alphas.length;i++){
                totalBlackPixels=0;
                pixelRatio=0;
                totalPixels=0;
                compImgFile = new File("J:\\OCR pics\\Alphabets\\" + alphas[i] +".png");
                System.out.println("J:\\OCR pics\\Alphabets\\" + alphas[i] +".png");
                try{
                    cImg = ImageIO.read(compImgFile);
                }
                catch(IOException ie){
                    System.out.println("IOException");
                }
                /*
                int width = rMostX - lMostX;
                //rMostX = 0;
                scaledWidth = cImg.getWidth();
                scaledHeight = yMax - yMin;
                //adjusting the width of the cImg
                try {
                    bim=ImageIO.read(compImgFile);
                }
                catch(IOException io){}
                Image resizedImg = bim.getScaledInstance(width+3, scaledHeight+1, Image.SCALE_SMOOTH);

                //making a new buffered image to store the scaled image
                BufferedImage resizedImage = new BufferedImage(width+3, scaledHeight+1, BufferedImage.TYPE_BYTE_BINARY);
                Graphics2D g2d = resizedImage.createGraphics();
                g2d.drawImage(resizedImg, 0, 0, null);
                g2d.dispose();
                try{
                    ImageIO.write(resizedImage, "png", tmpcImgFile);
                }
                catch(IOException ie){}
                */
                leftmostofthepic();
                Color colorPic;
                int tempX, tempY;
                for(int q = startPos2;q<xCoords.size();q++){
                    pixelRatio = totalBlackPixels/totalPixels;
                    if(xCoords.get(q)==-1) {
                        if ((pixelRatio) > 0.97) {

                            startPos2 = q + 1;
                        }
                    }
                    else {
                        tempX = xCoords.get(q) - xCoords.get(startPos2);
                        tempY = yCoords.get(q) - yCoords.get(startPos2) + lmostPicY;
                        totalPixels++;
                        if (tempX < cImg.getWidth() && tempX >= 0 && tempY >= 0 && tempY < cImg.getHeight()) {
                            colorPic = new Color(cImg.getRGB(tempX, tempY));
                            if (colorPic.getRed() == 0) {
                                totalBlackPixels++;
                            }
                        }
                    }
                }
                System.out.println(" \npixel ratio = " + pixelRatio);
                if((pixelRatio) > 0.97) {
                    //if(i==2 && xCoords.size()>40)

                    textArea.append(alphas[i].substring(0, 1));
                    break;
                }
                else{
                    System.out.println("not this alpha");
                }

                //tmpcImgFile.delete();
            }
        }

        void leftmostofthepic() {
            System.out.println("\ntmpcimg width = " + cImg.getWidth() +" tmpcimg height = " +cImg.getHeight());
            check = 0;
            //if(endHeight!=)
            for (int x1 = 0; x1 < cImg.getWidth(); x1++) {

                for (int y1 = 0; y1 < cImg.getHeight(); y1++) {

                    Color c = new Color(cImg.getRGB(x1, y1));
                    if (c.getRed() == 0) {
                        Color c1 = new Color(cImg.getRGB(x1, y1));
                        if (c1 != c) {
                            lmostPicY = y1;
                            check = 1;
                            break;
                        }
                    }
                }
                if(check==1)
                    break;
            }
        }
    }
}
