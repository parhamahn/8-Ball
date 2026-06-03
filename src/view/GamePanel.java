package view;

import controller.GamePlayController;
import game.Main;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import javax.swing.*;
import model.*;

public class GamePanel extends JPanel {

    private static final int FPS = 60;
    private static final double DT = 1.0;
    private static final int TURN_TIME = 30;

    private JButton exitButton;
    private JButton debugButton;
    private Table table;
    private Physics physics;
    private Timer gameLoop;
    private GamePlayController gamePlayController;
    private GameState gameState;
    private Rule rule;
    private Main mainApp;
    private Timer turnTimer;

    public GamePanel(Table table, Physics physics, GamePlayController gamePlayController, GameState gameState, Rule rule, Main mainApp) {
        this.table = table;
        this.physics = physics;
        this.gamePlayController = gamePlayController;
        this.gameState = gameState;
        this.rule = rule;
        this.mainApp = mainApp;

        setBackground(new Color(30, 30, 40));

        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(100, 25));
        add(exitButton);

        debugButton = new JButton("Debug");
        /*debugButton.setPreferredSize(new Dimension(100, 40));
        add(debugButton);*/

        gameLoop = new Timer(1000 / FPS, e -> {
            physics.updatePhysics(DT);
            rule.checkRules();
            if (gameState.isEnded()) {
                stopLoop();
                mainApp.showWin(gameState.getWinner());
            }
            repaint();
        });

        turnTimer = new Timer(1000, e -> {
            if (gameState.isTimerRunning()) {
                if (gameState.getTurnTimer() <= 0) {
                    gameState.setShouldChangeTurn(true);
                    gameState.switchTurns();
                    gameState.resetTimer();
                } else if (!gameState.isMoving()) {
                    gameState.passOneSecond();
                }
                repaint();
            }
        });
    }

    public void startLoop() {
        if (!gameLoop.isRunning()) gameLoop.start();
        startTurnTimer();
    }

    public void stopLoop() {
        if (gameLoop.isRunning()) gameLoop.stop();
        stopTurnTimer();
    }

    public void startTurnTimer() {
        gameState.setTimerRunning(true);
        if (!turnTimer.isRunning()) turnTimer.start();
    }

    public void stopTurnTimer() {
        gameState.setTimerRunning(false);
        if (turnTimer.isRunning()) turnTimer.stop();
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
        drawSpinSelector(g2d);
        drawFoul(g2d);
        drawTimer(g2d);
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

        AimCollision collision = findAimCollision(cueBall, dirX, dirY);
        if (collision == null) return;

        Stroke previousStroke = g2d.getStroke();
        Composite previousComposite = g2d.getComposite();

        g2d.setComposite(AlphaComposite.SrcOver.derive(0.45f));
        g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(Color.WHITE);
        g2d.drawLine((int)cueBall.getX(), (int)cueBall.getY(), (int)collision.x, (int)collision.y);

        if (collision.ball != null) {
            double targetDirX = collision.ball.getX() - collision.x;
            double targetDirY = collision.ball.getY() - collision.y;
            double targetDistance = Math.hypot(targetDirX, targetDirY);
            if (targetDistance > 0.001) {
                targetDirX /= targetDistance;
                targetDirY /= targetDistance;
                double targetLineLength = Math.min(180.0, Math.max(80.0, distance * 0.6));
                g2d.setColor(new Color(255, 230, 70));
                g2d.drawLine(
                        (int)collision.ball.getX(),
                        (int)collision.ball.getY(),
                        (int)(collision.ball.getX() + targetDirX * targetLineLength),
                        (int)(collision.ball.getY() + targetDirY * targetLineLength)
                );
            }

            double cueAfterDirX = dirX;
            double cueAfterDirY = dirY;
            if (targetDistance > 0.001) {
                double cueVelocityX = dirX;
                double cueVelocityY = dirY;
                double targetVelocityX = 0.0;
                double targetVelocityY = 0.0;

                double normalX = collision.x - collision.ball.getX();
                double normalY = collision.y - collision.ball.getY();
                double normalLength = Math.hypot(normalX, normalY);
                if (normalLength > 0.001) {
                    normalX /= normalLength;
                    normalY /= normalLength;

                    double relativeVelocityX = cueVelocityX - targetVelocityX;
                    double relativeVelocityY = cueVelocityY - targetVelocityY;
                    double velocityAlongNormal = relativeVelocityX * normalX + relativeVelocityY * normalY;

                    if (velocityAlongNormal < 0) {
                        double impulse = -velocityAlongNormal;
                        cueVelocityX += impulse * normalX;
                        cueVelocityY += impulse * normalY;

                        double forwardX = dirX;
                        double forwardY = dirY;
                        double tangentX = -forwardY;
                        double tangentY = forwardX;
                        double spinFactor = 0.4;
                        Ball.Spin spin = gameState.getSelectedSpin();

                        if (spin == Ball.Spin.TOP) {
                            cueVelocityX += spinFactor * forwardX;
                            cueVelocityY += spinFactor * forwardY;
                        } else if (spin == Ball.Spin.BACK) {
                            cueVelocityX -= spinFactor * forwardX;
                            cueVelocityY -= spinFactor * forwardY;
                        } else if (spin == Ball.Spin.LEFT) {
                            cueVelocityX += spinFactor * tangentX;
                            cueVelocityY += spinFactor * tangentY;
                        } else if (spin == Ball.Spin.RIGHT) {
                            cueVelocityX -= spinFactor * tangentX;
                            cueVelocityY -= spinFactor * tangentY;
                        }

                        double cueAfterLength = Math.hypot(cueVelocityX, cueVelocityY);
                        if (cueAfterLength > 0.001) {
                            cueAfterDirX = cueVelocityX / cueAfterLength;
                            cueAfterDirY = cueVelocityY / cueAfterLength;
                            double cueLineLength = Math.min(180.0, Math.max(70.0, distance * 0.55));
                            g2d.setColor(new Color(190, 120, 255));
                            g2d.drawLine(
                                    (int)collision.x,
                                    (int)collision.y,
                                    (int)(collision.x + cueAfterDirX * cueLineLength),
                                    (int)(collision.y + cueAfterDirY * cueLineLength)
                            );
                        }
                    }
                }
            }
        } else {
            double bounceLineLength = Math.min(220.0, Math.max(100.0, distance * 0.8));
            g2d.setColor(new Color(90, 210, 255));
            g2d.drawLine(
                    (int)collision.x,
                    (int)collision.y,
                    (int)(collision.x + collision.reflectedDirX * bounceLineLength),
                    (int)(collision.y + collision.reflectedDirY * bounceLineLength)
            );
        }

        g2d.setColor(new Color(255, 255, 255, 160));
        g2d.fillOval((int)(collision.x - 4), (int)(collision.y - 4), 8, 8);
        g2d.setComposite(previousComposite);
        g2d.setStroke(previousStroke);
    }

    private AimCollision findAimCollision(Ball cueBall, double dirX, double dirY) {
        AimCollision nearest = null;

        for (Ball ball : table.getBalls()) {
            if (ball == cueBall || ball.isPocketed()) continue;

            double combinedRadius = cueBall.getRadius() + ball.getRadius();
            double toBallX = ball.getX() - cueBall.getX();
            double toBallY = ball.getY() - cueBall.getY();
            double projection = toBallX * dirX + toBallY * dirY;
            if (projection <= 0) continue;

            double closestDistanceSq = toBallX * toBallX + toBallY * toBallY - projection * projection;
            double radiusSq = combinedRadius * combinedRadius;
            if (closestDistanceSq > radiusSq) continue;

            double offset = Math.sqrt(radiusSq - closestDistanceSq);
            double collisionDistance = projection - offset;
            if (collisionDistance <= 0) continue;

            AimCollision candidate = new AimCollision(
                    cueBall.getX() + dirX * collisionDistance,
                    cueBall.getY() + dirY * collisionDistance,
                    collisionDistance
            );
            candidate.ball = ball;
            nearest = closerAimCollision(nearest, candidate);
        }

        for (int[][] polygon : table.getCushionPolygons()) {
            int[] xs = polygon[0];
            int[] ys = polygon[1];
            for (int i = 0; i < xs.length; i++) {
                int j = (i + 1) % xs.length;
                AimCollision candidate = findSegmentAimCollision(
                        cueBall.getX(), cueBall.getY(), dirX, dirY, cueBall.getRadius(),
                        xs[i], ys[i], xs[j], ys[j]
                );
                nearest = closerAimCollision(nearest, candidate);
            }
        }

        return nearest;
    }

    private AimCollision closerAimCollision(AimCollision current, AimCollision candidate) {
        if (candidate == null) return current;
        if (current == null || candidate.distance < current.distance) return candidate;
        return current;
    }

    private AimCollision findSegmentAimCollision(double startX, double startY, double dirX, double dirY,
                                                 double radius, double ax, double ay, double bx, double by) {
        AimCollision nearest = null;

        double edgeX = bx - ax;
        double edgeY = by - ay;
        double edgeLengthSq = edgeX * edgeX + edgeY * edgeY;
        if (edgeLengthSq == 0) return null;

        double edgeLength = Math.sqrt(edgeLengthSq);
        double normalX = -edgeY / edgeLength;
        double normalY = edgeX / edgeLength;

        nearest = closerAimCollision(nearest, findOffsetLineAimCollision(
                startX, startY, dirX, dirY, radius, ax, ay, bx, by, edgeX, edgeY, edgeLengthSq, normalX, normalY
        ));
        nearest = closerAimCollision(nearest, findOffsetLineAimCollision(
                startX, startY, dirX, dirY, radius, ax, ay, bx, by, edgeX, edgeY, edgeLengthSq, -normalX, -normalY
        ));
        nearest = closerAimCollision(nearest, findEndpointAimCollision(startX, startY, dirX, dirY, radius, ax, ay));
        nearest = closerAimCollision(nearest, findEndpointAimCollision(startX, startY, dirX, dirY, radius, bx, by));

        return nearest;
    }

    private AimCollision findOffsetLineAimCollision(double startX, double startY, double dirX, double dirY,
                                                    double radius, double ax, double ay, double bx, double by,
                                                    double edgeX, double edgeY, double edgeLengthSq,
                                                    double normalX, double normalY) {
        double offsetAx = ax + normalX * radius;
        double offsetAy = ay + normalY * radius;
        double denominator = cross(dirX, dirY, edgeX, edgeY);
        if (Math.abs(denominator) < 0.0001) return null;

        double deltaX = offsetAx - startX;
        double deltaY = offsetAy - startY;
        double distance = cross(deltaX, deltaY, edgeX, edgeY) / denominator;
        double segmentPosition = cross(deltaX, deltaY, dirX, dirY) / denominator;
        if (distance <= 0 || segmentPosition < 0 || segmentPosition > 1) return null;

        double x = startX + dirX * distance;
        double y = startY + dirY * distance;
        double closestT = ((x - ax) * edgeX + (y - ay) * edgeY) / edgeLengthSq;
        closestT = Math.max(0.0, Math.min(1.0, closestT));
        double closestX = ax + closestT * edgeX;
        double closestY = ay + closestT * edgeY;
        double collisionNormalX = x - closestX;
        double collisionNormalY = y - closestY;
        double normalLength = Math.hypot(collisionNormalX, collisionNormalY);
        if (normalLength < 0.001) return null;

        collisionNormalX /= normalLength;
        collisionNormalY /= normalLength;
        double velocityDotNormal = dirX * collisionNormalX + dirY * collisionNormalY;
        if (velocityDotNormal >= 0) return null;

        return createCushionAimCollision(x, y, distance, dirX, dirY, collisionNormalX, collisionNormalY);
    }

    private AimCollision findEndpointAimCollision(double startX, double startY, double dirX, double dirY,
                                                  double radius, double pointX, double pointY) {
        double toPointX = pointX - startX;
        double toPointY = pointY - startY;
        double projection = toPointX * dirX + toPointY * dirY;
        if (projection <= 0) return null;

        double closestDistanceSq = toPointX * toPointX + toPointY * toPointY - projection * projection;
        double radiusSq = radius * radius;
        if (closestDistanceSq > radiusSq) return null;

        double distance = projection - Math.sqrt(radiusSq - closestDistanceSq);
        if (distance <= 0) return null;

        double x = startX + dirX * distance;
        double y = startY + dirY * distance;
        double normalX = x - pointX;
        double normalY = y - pointY;
        double normalLength = Math.hypot(normalX, normalY);
        if (normalLength < 0.001) return null;

        normalX /= normalLength;
        normalY /= normalLength;
        double velocityDotNormal = dirX * normalX + dirY * normalY;
        if (velocityDotNormal >= 0) return null;

        return createCushionAimCollision(x, y, distance, dirX, dirY, normalX, normalY);
    }

    private AimCollision createCushionAimCollision(double x, double y, double distance,
                                                   double dirX, double dirY, double normalX, double normalY) {
        double velocityDotNormal = dirX * normalX + dirY * normalY;
        AimCollision collision = new AimCollision(x, y, distance);
        collision.reflectedDirX = dirX - 2 * velocityDotNormal * normalX;
        collision.reflectedDirY = dirY - 2 * velocityDotNormal * normalY;
        double reflectedLength = Math.hypot(collision.reflectedDirX, collision.reflectedDirY);
        if (reflectedLength > 0.001) {
            collision.reflectedDirX /= reflectedLength;
            collision.reflectedDirY /= reflectedLength;
        }
        return collision;
    }

    private double cross(double ax, double ay, double bx, double by) {
        return ax * by - ay * bx;
    }

    private static class AimCollision {
        private final double x;
        private final double y;
        private final double distance;
        private Ball ball;
        private double reflectedDirX;
        private double reflectedDirY;

        private AimCollision(double x, double y, double distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
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
                    } else if (table.allTypesPocketed(gameState.getTurn().getSide()) && !table.getBall(8).isPocketed() && ball.getBallType()== Ball.BallType.EIGHT) {
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
        int namegroupspace = 40;
        FontMetrics fm = g2.getFontMetrics();
        String p2Text = "Player 2: " + p2Name;
        int textWidth = fm.stringWidth(p2Text);

        String groupText = gameState.getPlayer2().getSide() == Ball.BallType.STRIPE ? "STRIPE" : "SOLID";
        int groupWidth = 90;

        if (gameState.getTurn() == gameState.getPlayer1()) {
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.setColor(!gameState.isSidesAssigned()?Color.WHITE:gameState.getPlayer1().getSide()==Ball.BallType.SOLID?Color.RED:Color.YELLOW);
            g2.drawString(p1Name, leftX, y);
            if (gameState.isSidesAssigned()){g2.drawString(gameState.getPlayer1().getSide()==Ball.BallType.STRIPE?"STRIPE":"SOLID", leftX, y+namegroupspace);}
            g2.drawString(String.valueOf(gameState.getPlayer1().getPoints()), leftX, y+(2*namegroupspace));
            g2.setFont(new Font("Arial", Font.ITALIC, 18));
            g2.setColor(!gameState.isSidesAssigned()?Color.WHITE:gameState.getPlayer2().getSide()==Ball.BallType.SOLID?Color.RED:Color.YELLOW);
            g2.drawString(p2Name, rightX - textWidth, y);
            if (gameState.isSidesAssigned()){g2.drawString(groupText, rightX - groupWidth, y + namegroupspace);}
            g2.drawString(String.valueOf(gameState.getPlayer2().getPoints()), rightX-50, y+(2*namegroupspace));
        } else {
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.setColor(!gameState.isSidesAssigned()?Color.WHITE:gameState.getPlayer2().getSide()==Ball.BallType.SOLID?Color.RED:Color.YELLOW);
            g2.drawString(p2Name, rightX - textWidth, y);
            if (gameState.isSidesAssigned())g2.drawString(groupText, rightX - groupWidth, y+namegroupspace);
            g2.drawString(String.valueOf(gameState.getPlayer2().getPoints()), rightX-50, y+(2*namegroupspace));
            g2.setFont(new Font("Arial", Font.ITALIC, 18));
            g2.setColor(!gameState.isSidesAssigned()?Color.WHITE:gameState.getPlayer1().getSide()==Ball.BallType.SOLID?Color.RED:Color.YELLOW);
            g2.drawString(p1Name, leftX, y);
            if (gameState.isSidesAssigned())g2.drawString(gameState.getPlayer1().getSide()==Ball.BallType.STRIPE?"STRIPE":"SOLID", leftX, y+namegroupspace);
            g2.drawString(String.valueOf(gameState.getPlayer1().getPoints()), leftX, y+(2*namegroupspace));
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

    private void drawSpinSelector(Graphics2D g2d) {
        int selectorRadius = 50;
        int selectorX = (int) table.getWidth() / 2;
        int selectorY = 120;

        boolean canSelectSpin = !gameState.isMoving() && gameState.isPocketChosen();
        int alpha = canSelectSpin ? 255 : 100;

        g2d.setColor(new Color(255, 255, 255, alpha));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.fillOval(selectorX - selectorRadius, selectorY - selectorRadius, selectorRadius * 2, selectorRadius * 2);

        int pointRadius = 8;
        int innerOffset = (int)(selectorRadius * 0.6);

        int[][] dotCenters = {
                {selectorX, selectorY},
                {selectorX, selectorY - innerOffset},
                {selectorX, selectorY + innerOffset},
                {selectorX - innerOffset, selectorY},
                {selectorX + innerOffset, selectorY}
        };

        Ball.Spin selectedSpin = gameState.getSelectedSpin();

        for (int i = 0; i < dotCenters.length; i++) {
            int cx = dotCenters[i][0];
            int cy = dotCenters[i][1];
            boolean isSelected = selectedSpin != null && selectedSpin.ordinal() == i;

            if (isSelected) {
                g2d.setColor(new Color(200, 0, 0, alpha));
                g2d.fillOval(cx - pointRadius, cy - pointRadius, pointRadius * 2, pointRadius * 2);
            } else {
                g2d.setColor(new Color(200, 0, 0, alpha));
                g2d.drawOval(cx - pointRadius, cy - pointRadius, pointRadius * 2, pointRadius * 2);
            }
        }
    }

    private void drawFoul(Graphics2D g2d){
        if (!gameState.isFoulHappened()) return;
        
        g2d.setStroke(new BasicStroke(1f));
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        g2d.setColor(Color.RED);
        String msg = "FOUL";
        FontMetrics fm = g2d.getFontMetrics();
        int msgX = (int)(table.getStartX() + (table.getWidth() - fm.stringWidth(msg)) / 2);
        int msgY = 60;
        g2d.drawString(msg, msgX, msgY);
    }

    private void drawTimer(Graphics2D g2){
        int timer = gameState.getTurnTimer();
        g2.setColor(timer <= 10 ? Color.RED : Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String timerText = "Time: " + timer + "s";
        FontMetrics fm = g2.getFontMetrics();
        int timerX;
        if (gameState.getTurn()==gameState.getPlayer1()){
            timerX = getWidth()/2 - 2*fm.stringWidth(timerText);
        }else{
            timerX = getWidth()/2 + fm.stringWidth(timerText);
        }
        g2.drawString(timerText, timerX, 20);
    }

    public JButton getExitButton() {
        return exitButton;
    }
    public JButton getDebugButton() {return debugButton;}
}
