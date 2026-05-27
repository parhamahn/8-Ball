package view;

import controller.GamePlayController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class GamePanel extends JPanel {

    private static final int FPS = 60;
    private static final double DT = 1.0;

    private JButton exitButton;
    private Table table;
    private Physics physics;
    private Timer gameLoop;
    private GamePlayController gamePlayController;
    private GameState gameState;
    private Rule rule;

    public GamePanel(Table table, Physics physics, GamePlayController gamePlayController, GameState gameState, Rule rule) {
        this.table = table;
        this.physics = physics;
        this.gamePlayController = gamePlayController;
        this.gameState = gameState;
        this.rule = rule;

        setBackground(new Color(30, 30, 40));
        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(100, 40));
        add(exitButton);

        gameLoop = new Timer(1000 / FPS, e -> {
            physics.updatePhysics(DT);
            rule.checkRules();
            repaint();
        });
    }

    public void startLoop() {
        if (!gameLoop.isRunning()) gameLoop.start();
    }

    public void stopLoop() {
        if (gameLoop.isRunning()) gameLoop.stop();
    }

    private Color getColor(int number) {
        Color[] ballColors = {
                new Color(255, 215, 0),
                new Color(0, 69, 0),
                new Color(220, 20, 60),
                new Color(0, 0, 139),
                new Color(255, 140, 0),
                new Color(128, 0, 128),
                new Color(139, 69, 19)
        };
        return ballColors[(number - 1) % 7];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawTable(g2d);
        drawCushions(g2d);
        drawPockets(g2d);
        drawBalls(g2d);
        drawAimingLine(g2d);
        highlightBalls(g2d);
        drawUI(g2d);
        drawChosenPocket(g2d);
        drawBallInHand(g2d);
    }

    private void drawTable(Graphics2D g2d) {
        g2d.setColor(new Color(0, 100, 0));
        int surfaceX = (int) table.getStartX();
        int surfaceY = (int) table.getStartY();
        int surfaceW = (int) table.getWidth();
        int surfaceH = (int) table.getHeight();
        g2d.fillRect(surfaceX, surfaceY, surfaceW, surfaceH);

        g2d.setColor(new Color(60, 131, 60));
        g2d.drawLine(
                (int) table.getWidth() / 4, (int) Table.getDefaultStarty(),
                (int) table.getWidth() / 4, (int) (Table.getDefaultEndy() + table.getHeight())
        );
    }

    private void drawPockets(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        for (Pocket pocket : table.getPockets()) {
            int x = (int) (pocket.getCenterX() - pocket.getRadius());
            int y = (int) (pocket.getCenterY() - pocket.getRadius());
            int diameter = (int) (pocket.getRadius() * 2);
            g2d.fillOval(x, y, diameter, diameter);
        }
    }

    private void drawCushions(Graphics2D g2d) {
        double playLeft   = table.getStartX();
        double playRight  = table.getEndX();
        double playTop    = table.getStartY();
        double playBottom = table.getEndY();

        double cThick       = table.getCushionThickness();
        double pRadius      = Pocket.getDefaultRadius();
        double centerX      = (playLeft + playRight) / 2.0;
        double cornerFactor = 1.5;
        double centerGap    = 0.6 * pRadius;
        double centerHalf   = centerGap / 2.0;

        double outerTop    = playTop    + cThick;
        double outerBottom = playBottom - cThick;
        double outerLeft   = playLeft   + cThick;
        double outerRight  = playRight  - cThick;

        g2d.setColor(new Color(139, 69, 19));

        int[] xPoints, yPoints;

        // Top left
        xPoints = new int[]{(int)(playLeft + cornerFactor*pRadius + cThick), (int)(centerX - centerHalf - cThick), (int)(centerX - centerHalf), (int)(playLeft + cornerFactor*pRadius)};
        yPoints = new int[]{(int)outerTop, (int)outerTop, (int)playTop, (int)playTop};
        g2d.fillPolygon(xPoints, yPoints, 4);

        // Top right
        xPoints = new int[]{(int)(centerX + centerHalf + cThick), (int)(playRight - cornerFactor*pRadius - cThick), (int)(playRight - cornerFactor*pRadius), (int)(centerX + centerHalf)};
        yPoints = new int[]{(int)outerTop, (int)outerTop, (int)playTop, (int)playTop};
        g2d.fillPolygon(xPoints, yPoints, 4);

        // Bottom left
        xPoints = new int[]{(int)(playLeft + cornerFactor*pRadius + cThick), (int)(centerX - centerHalf - cThick), (int)(centerX - centerHalf), (int)(playLeft + cornerFactor*pRadius)};
        yPoints = new int[]{(int)outerBottom, (int)outerBottom, (int)playBottom, (int)playBottom};
        g2d.fillPolygon(xPoints, yPoints, 4);

        // Bottom right
        xPoints = new int[]{(int)(centerX + centerHalf + cThick), (int)(playRight - cornerFactor*pRadius - cThick), (int)(playRight - cornerFactor*pRadius), (int)(centerX + centerHalf)};
        yPoints = new int[]{(int)outerBottom, (int)outerBottom, (int)playBottom, (int)playBottom};
        g2d.fillPolygon(xPoints, yPoints, 4);

        // Left side
        xPoints = new int[]{(int)outerLeft, (int)outerLeft, (int)playLeft, (int)playLeft};
        yPoints = new int[]{(int)(playTop + cornerFactor*pRadius + cThick), (int)(playBottom - cornerFactor*pRadius - cThick), (int)(playBottom - cornerFactor*pRadius), (int)(playTop + cornerFactor*pRadius)};
        g2d.fillPolygon(xPoints, yPoints, 4);

        // Right side
        xPoints = new int[]{(int)outerRight, (int)outerRight, (int)playRight, (int)playRight};
        yPoints = new int[]{(int)(playTop + cornerFactor*pRadius + cThick), (int)(playBottom - cornerFactor*pRadius - cThick), (int)(playBottom - cornerFactor*pRadius), (int)(playTop + cornerFactor*pRadius)};
        g2d.fillPolygon(xPoints, yPoints, 4);
    }

    private void drawBalls(Graphics2D g2d) {
        for (Ball ball : table.getBalls()) {
            if (ball.isPocketed()) continue;

            int drawX = (int)(ball.getX() - ball.getRadius());
            int drawY = (int)(ball.getY() - ball.getRadius());
            int diameter = (int)(ball.getRadius() * 2);
            double r = ball.getRadius();

            if (ball.getBallType() == Ball.BallType.CUE) {
                g2d.setColor(Color.WHITE);
            } else if (ball.getBallType() == Ball.BallType.EIGHT) {
                g2d.setColor(Color.BLACK);
            } else if (ball.getBallType() == Ball.BallType.SOLID) {
                g2d.setColor(getColor(ball.getNumber()));
            } else {
                g2d.setColor(getColor(ball.getNumber()));
            }
            g2d.fillOval(drawX, drawY, diameter, diameter);

            g2d.setColor(new Color(255, 255, 255, 170));
            g2d.fillOval(drawX + (int)(r * 0.15), drawY + (int)(r * 0.15),
                    (int)(r * 1.25), (int)(r * 1.25));

            if (ball.getBallType() != Ball.BallType.CUE) {

                int circleX = drawX + (int)(r * 0.32);
                int circleY = drawY + (int)(r * 0.32);
                int circleSize = (int)(r * 1.36);

                if (ball.getBallType() == Ball.BallType.STRIPE) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(circleX, circleY, circleSize, circleSize);
                }
                else if (ball.getBallType() == Ball.BallType.EIGHT) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(circleX, circleY, circleSize, circleSize);
                    g2d.setColor(Color.BLACK);
                }

            }
        }
    }

    private void drawAimingLine(Graphics2D g2d) {
        if (gamePlayController == null) return;
        if (!gameState.isPocketChosen()) return;
        if (!gamePlayController.isDragging()) return;

        Ball cueBall = table.getBall(0);
        if (cueBall == null || cueBall.isPocketed()) return;

        Point2D dragStart = gamePlayController.getDragStart();
        Point2D dragCurrent = gamePlayController.getDragCurrent();
        if (dragStart == null || dragCurrent == null) return;

        double dirX = dragStart.getX() - dragCurrent.getX();
        double dirY = dragStart.getY() - dragCurrent.getY();
        double distance = Math.hypot(dirX, dirY);
        if (distance < 0.001) return;

        dirX /= distance;
        dirY /= distance;

        double power = Math.min(distance / 10.0, 20.0);
        double lineLength = power * 10.0;
        double startX = cueBall.getX();
        double startY = cueBall.getY();
        double endX = startX + dirX * lineLength;
        double endY = startY + dirY * lineLength;

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawLine((int)startX, (int)startY, (int)endX, (int)endY);

        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawOval((int)(endX - 3), (int)(endY - 3), 6, 6);
    }

    private void highlightBalls(Graphics2D g2d){
        if (!gameState.isMoving()){
            g2d.setColor(new Color(207, 207, 207));
            g2d.setStroke(new BasicStroke(2f));
            for (Ball ball: table.getBalls()){
                if(gameState.isSidesAssigned()){
                    if(gameState.getTurn().getSide()==ball.getBallType() & !ball.isPocketed()) {
                        int drawX = (int) (ball.getX() - ball.getRadius());
                        int drawY = (int) (ball.getY() - ball.getRadius());
                        int diameter = (int) (ball.getRadius() * 2);
                        g2d.draw(new Ellipse2D.Double(drawX, drawY, diameter, diameter));
                    }
                }else if(ball.getBallType()!= Ball.BallType.CUE & ball.getBallType()!= Ball.BallType.EIGHT & !ball.isPocketed()){
                    int drawX = (int) (ball.getX() - ball.getRadius());
                    int drawY = (int) (ball.getY() - ball.getRadius());
                    int diameter = (int) (ball.getRadius() * 2);
                    g2d.draw(new Ellipse2D.Double(drawX, drawY, diameter, diameter));
                }
            }
        }
    }

    private void drawUI(Graphics2D g2) {

        String p1Name = gameState.getPlayer1().getName();
        String p2Name = gameState.getPlayer2().getName();
        int y = 30;
        int leftX = 20;
        int rightX = getWidth();
        FontMetrics fm = g2.getFontMetrics();
        String p2Text = "Player 2: " + p2Name;
        int textWidth = fm.stringWidth(p2Text);

        if (gameState.getTurn() == gameState.getPlayer1()) {
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.setColor(!gameState.isSidesAssigned()?Color.WHITE:gameState.getPlayer1().getSide()==Ball.BallType.SOLID?Color.RED:Color.YELLOW);
            g2.drawString(p1Name, leftX, y);
            g2.setFont(new Font("Arial", Font.ITALIC, 18));
            g2.setColor(!gameState.isSidesAssigned()?Color.WHITE:gameState.getPlayer2().getSide()==Ball.BallType.SOLID?Color.RED:Color.YELLOW);
            g2.drawString(p2Name, rightX - textWidth, y);
        } else {
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.setColor(!gameState.isSidesAssigned()?Color.WHITE:gameState.getPlayer2().getSide()==Ball.BallType.SOLID?Color.RED:Color.YELLOW);
            g2.drawString(p2Name, rightX - textWidth, y);
            g2.setFont(new Font("Arial", Font.ITALIC, 18));
            g2.setColor(!gameState.isSidesAssigned()?Color.WHITE:gameState.getPlayer1().getSide()==Ball.BallType.SOLID?Color.RED:Color.YELLOW);
            g2.drawString(p1Name, leftX, y);
        }
    }

    private void drawChosenPocket(Graphics2D g2d) {
        if (!gameState.isPocketChosen() || gameState.getChosenPocket() == null) return;

        Pocket p = gameState.getChosenPocket();
        g2d.setColor(new Color(255, 255, 0, 180));
        g2d.setStroke(new BasicStroke(4.0f));
        int x = (int) (p.getCenterX() - p.getRadius() * 1.3);
        int y = (int) (p.getCenterY() - p.getRadius() * 1.3);
        int size = (int) (p.getRadius() * 2.6);
        g2d.drawOval(x, y, size, size);

        g2d.setColor(new Color(255, 255, 100));
        g2d.drawOval((int)(p.getCenterX() - p.getRadius() * 0.8),
                (int)(p.getCenterY() - p.getRadius() * 0.8),
                (int)(p.getRadius() * 1.6), (int)(p.getRadius() * 1.6));
    }

    private void drawBallInHand(Graphics2D g2d) {
        if (!gameState.isBallInHand()) return;

        double headString = table.getStartX() + table.getWidth() / 4.0;

        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.fillRect(
                (int) (table.getStartX()+2*Pocket.getDefaultRadius()),
                (int) (table.getStartY()+2*Pocket.getDefaultRadius()),
                (int)(headString - table.getStartX() -2*Pocket.getDefaultRadius()),
                (int) (table.getHeight()-4*Pocket.getDefaultRadius())
        );

        g2d.setColor(new Color(255, 220, 0, 200));
        g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                10, new float[]{8, 6}, 0));
        g2d.drawLine((int) headString, (int) table.getStartY(),
                (int) headString, (int) table.getEndY());

        g2d.setStroke(new BasicStroke(1f));
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String msg = "Ball in hand — drag cue ball behind the line";
        FontMetrics fm = g2d.getFontMetrics();
        int msgX = (int)(table.getStartX() + (table.getWidth() - fm.stringWidth(msg)) / 2);
        g2d.drawString(msg, msgX, (int) table.getStartY() - 10);
    }

    public JButton getExitButton() {
        return exitButton;
    }
}