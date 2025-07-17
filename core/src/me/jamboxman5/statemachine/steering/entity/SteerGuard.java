package me.jamboxman5.statemachine.steering.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import me.jamboxman5.statemachine.steering.SteeringGameScreen;

import java.util.ArrayList;

public class SteerGuard extends SteerEntity {

    private Rectangle catchCollision;
    public GuardState state;
    double angle = 270;
    public Circle sightRadius;
    public Polygon arc;
    public PathFinder finder = new PathFinder();
    public Vector2 target;
    float maxForce;
    Vector2 startPosition;

    public SteerGuard(SteeringGameScreen screen) {
        super(new Rectangle(screen.building.x - 40, screen.building.y - 40, 40, 40), screen);
        catchCollision = new Rectangle( bounds.x + 10, bounds.y + 10, 20, 20);
        sightRadius = new Circle(bounds.x + bounds.width/2, bounds.y + bounds.height/2, 200);
        arc = new Polygon();
        state = GuardState.LOOKINGLEFT;
        speed = 4;
        velocity = new Vector2(0,0);
        maxForce = .1f;
        startPosition = new Vector2(screen.building.x - 40, screen.building.y - 40);
    }

    public void pursue() {
        target = new Vector2(screen.player.bounds.x, screen.player.bounds.y);
        Vector2 prediction = screen.player.velocity.cpy();
        prediction.scl(10);
        target.add(prediction);

        seek(target);

//        if (!pathFind((int) target.x, (int) target.y)) pathFind((int) screen.player.bounds.x, (int) screen.player.bounds.y);
    }

    public void seek(Vector2 target) {
        Vector2 force = target.sub(new Vector2(bounds.x, bounds.y));
        force.setLength(speed);
        Vector2 steer = force.sub(velocity);
        steer.limit(maxForce);

        acceleration.add(steer);
        acceleration.limit(speed);

        System.out.println(acceleration);

//        if (!deadSpace.contains(new Vector2(bounds.x + steer.x, bounds.y))) bounds.x += steer.x;
//        else bounds.x += steer.y;
//        if (!deadSpace.contains(new Vector2(bounds.x, bounds.y + steer.y))) bounds.y += steer.y;
//        else bounds.y += steer.x;
    }

    public void arrive(Vector2 target) {
        Vector2 force = target.sub(new Vector2(bounds.x, bounds.y));

        int radius = 200;
        float distance = force.len();
        if (distance < radius) {
            float m = map(distance, 0, radius, 0, speed);
            force.setLength(m);
        }
        else {
            force.setLength(speed);
        }

        force.sub(velocity);
        force.limit(maxForce);



        acceleration.add(force);
        acceleration.limit(speed);
    }

    public static float map(float val, float oldmax, float max, float newMin, float newMax)
    {
        val = (val - oldmax)/(max - oldmax);
        return newMin + val * (newMax - newMin);
    }

    private class Node {
        Node parent;
        int x;
        int y;
        int gCost;
        int hCost;
        int fCost;
        boolean solid;
        boolean open;
        boolean checked;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class PathFinder {
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> pathList = new ArrayList<>();
        Node[][] nodeMatrix;
        Node startNode, goalNode, currentNode;
        boolean goalReached = false;
        int step = 0;

        public PathFinder() {
            instantiateNodes();
        }

        public void instantiateNodes() {
            nodeMatrix = new Node[Gdx.graphics.getWidth()][Gdx.graphics.getHeight()];

            int x = 0;
            int y = 0;

            while (x < Gdx.graphics.getWidth() && y < Gdx.graphics.getHeight()) {
                nodeMatrix[x][y] = new Node(x, y);
                x++;
                if (x == Gdx.graphics.getWidth()) {
                    x = 0;
                    y++;
                }
            }
        }

        public void resetNodes() {
            int x = 0;
            int y = 0;

            while (x < Gdx.graphics.getWidth() && y < Gdx.graphics.getHeight()) {
                nodeMatrix[x][y].open = false;
                nodeMatrix[x][y].checked = false;
                nodeMatrix[x][y].solid = false;
                x++;
                if (x == Gdx.graphics.getWidth()) {
                    x = 0;
                    y++;
                }
            }

            openList.clear();
            pathList.clear();
            goalReached = false;
            step = 0;
        }

        public void setNodes(int startX, int startY, int targetX, int targetY) {
            resetNodes();

            if (targetX < 0) targetX += Gdx.graphics.getWidth();
            if (targetY < 0) targetY += Gdx.graphics.getHeight();
            if (startX < 0) startX += Gdx.graphics.getWidth();
            if (startY < 0) startY += Gdx.graphics.getHeight();

            if (targetX >= 1280) targetX -= Gdx.graphics.getWidth();
            if (targetY >= 720) targetY -= Gdx.graphics.getHeight();
            if (startX >= 1280) startX -= Gdx.graphics.getWidth();
            if (startY >= 720) startY -= Gdx.graphics.getHeight();

            startNode = nodeMatrix[startX][startY];
            currentNode = startNode;
            goalNode = nodeMatrix[targetX][targetY];
            openList.add(currentNode);

            int x = 0;
            int y = 0;

            while (x < Gdx.graphics.getWidth() && y < Gdx.graphics.getHeight()) {

                if (deadSpace.contains(x, y)) {
                    nodeMatrix[x][y].solid = true;
                }

                getCost(nodeMatrix[x][y]);

                x++;
                if (x == Gdx.graphics.getWidth()) {
                    x = 0;
                    y++;
                }
            }
        }

        public void getCost(Node node) {
            int xDist = Math.abs(node.x - startNode.x);
            int yDist = Math.abs(node.y - startNode.y);
            node.gCost = xDist + yDist;

            xDist = Math.abs(node.x - goalNode.x);
            yDist = Math.abs(node.y - goalNode.y);
            node.hCost = xDist + yDist;

            node.fCost = node.hCost + node.gCost;

        }

        public boolean search() {
            while(!goalReached && step < 50000) {
                int x = currentNode.x;
                int y = currentNode.y;

                currentNode.checked = true;
                openList.remove(currentNode);


                int xDist = Math.abs(goalNode.x - startNode.x);
                int yDist = Math.abs(goalNode.y - startNode.y);

                if (xDist > yDist) {
                    if (x+1 < Gdx.graphics.getWidth()) {
                        openNode(nodeMatrix[x+1][y]);
                    }
                    if (x-1 >= 0) {
                        openNode(nodeMatrix[x-1][y]);
                    }
                    if (y-1 >= 0) {
                        openNode(nodeMatrix[x][y-1]);
                    }
                    if (y+1 >= 0) {
                        openNode(nodeMatrix[x][y+1]);
                    }

                } else {
                    if (y-1 >= 0) {
                        openNode(nodeMatrix[x][y-1]);
                    }
                    if (y+1 >= 0) {
                        openNode(nodeMatrix[x][y+1]);
                    }
                    if (x+1 < Gdx.graphics.getWidth()) {
                        openNode(nodeMatrix[x+1][y]);
                    }
                    if (x-1 >= 0) {
                        openNode(nodeMatrix[x-1][y]);
                    }
                }


                int bestNodeIndex = 0;
                int bestNodefCost = 999;

                for (int i = 0; i < openList.size(); i++) {
                    if (openList.get(i).fCost < bestNodefCost) {
                        bestNodeIndex = i;
                        bestNodefCost = openList.get(i).fCost;
                    } else if (openList.get(i).fCost == bestNodefCost) {
                        if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) bestNodeIndex = i;
                    }
                }

                if (openList.isEmpty()) break;

                currentNode = openList.get(bestNodeIndex);
                if (currentNode == goalNode) {

                    goalReached = true;
                    trackThePath();
                }
                step++;
            }

            return goalReached;
        }

        public void trackThePath() {

            Node current = goalNode;

            while (current != startNode) {
                pathList.add(0, current);
                current = current.parent;
            }

        }

        public void openNode(Node node) {
            if (!node.open && !node.checked && !node.solid) {
                node.open = true;
                node.parent = currentNode;
                openList.add(node);
            }
        }

        private void setNodeCosts() {
            int x = 0;
            int y = 0;

            while (x < 1280 && y < 720) {
                getCost(nodeMatrix[x][y]);
                x++;
                if (x == 1280) {
                    x = 0;
                    y++;
                }
            }
        }

    }

    public void fillPolygonWithArc(Polygon polygon, float x, float y, float radius, float start, float degrees, int segments) {
        float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / segments;
        float cos = MathUtils.cos(theta);
        float sin = MathUtils.sin(theta);
        float cx = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
        float cy = radius * MathUtils.sin(start * MathUtils.degreesToRadians);

        ArrayList<Vector2> vertices = new ArrayList<>();

        vertices.add(new Vector2(x, y));
        vertices.add(new Vector2(x + cx, y + cy));
        for (int i = 0; i < segments; i++) {
            vertices.add(new Vector2(x + cx, y + cy));
            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;
            vertices.add(new Vector2(x + cx, y + cy));
        }
        vertices.add(new Vector2(x + cx, y + cy));

        cx = 0;
        cy = 0;
        vertices.add(new Vector2(x + cx, y + cy));

        polygon.setVertices(new float[vertices.size() * 2 + 4]);

        for (int i = 0; i < vertices.size(); i++) {
            polygon.setVertex(i, vertices.get(i).x, vertices.get(i).y);
        }
    }


    @Override
    public void update() {
        super.update();

        updateCollision();
        fillPolygonWithArc(arc, bounds.x + bounds.width/2, bounds.y + bounds.height/2, 250F, (float) (angle - 60f), 120, 20);

        if (catchCollision.overlaps(screen.player.bounds)) {
            screen.gameOver();
        }

        if (arc.contains(screen.player.bounds.x + screen.player.bounds.width/2, screen.player.bounds.y + screen.player.bounds.height/2)) state = GuardState.CHASING;

        System.out.println(state);
        switch (state) {

            case LOOKINGLEFT:
                angle -= .5;
                if (angle == 180) state = GuardState.LOOKINGRIGHT;
                break;
            case LOOKINGRIGHT:
                angle += .5;
                if (angle == 270) state = GuardState.LOOKINGLEFT;
                break;
            case CHASING:
                if (!arc.contains(screen.player.bounds.x, screen.player.bounds.y)) {
                    state = GuardState.ARRIVING;
                    acceleration.setLength(0);
                    velocity.setLength(0);

                } else {
                    angle = getDrawingAngle(true);

                    pursue();

//                    int targetX = (int) screen.player.bounds.x+5;
//                    int targetY = (int) screen.player.bounds.y+5;
//
//                    pathFind(targetX, targetY);

                }
                break;
            case ARRIVING:
                if (arc.contains(screen.player.bounds.x, screen.player.bounds.y)) {
                    acceleration.setLength(0);
                    velocity.setLength(0);
                    state = GuardState.CHASING;
                }
                else if (Math.abs(bounds.x - deadSpace.x) < 20 && Math.abs(bounds.y - deadSpace.y) < 20) {
                    acceleration.setLength(0);
                    velocity.setLength(0);
                    state = GuardState.LOOKINGRIGHT;

                    angle = 180;
                } else {

                    angle = getDrawingAngle(false);

                    int targetX = (int) deadSpace.x-10;
                    int targetY = (int) deadSpace.y-10;

                    arrive(new Vector2(targetX, targetY));

                }
                break;
        }



    }

    public boolean pathFind(int targetX, int targetY) {
        int startX = (int) bounds.x;
        int startY = (int) bounds.y;

        finder.setNodes(startX, startY, targetX, targetY);

        if (finder.search()) {
            int nextX = 0;
            int nextY = 0;


            if (finder.pathList.size() < speed) {
                nextX = finder.pathList.get(0).x;
                nextY = finder.pathList.get(0).y;
            } else {
                nextX = finder.pathList.get(speed).x;
                nextY = finder.pathList.get(speed).y;
            }



            if (!screen.building.contains(nextX, bounds.y)) bounds.x = nextX;
            if (!screen.building.contains(bounds.x, nextY)) bounds.y = nextY;

            return true;
        }
        return false;

    }

    public float getDrawingAngle(boolean toPlayer) {

        if (toPlayer) {
            float degrees = (float) (Math.atan2(
                    screen.player.bounds.getY() + screen.player.velocity.scl(10).y - bounds.getY(),
                    screen.player.bounds.getX() + screen.player.velocity.scl(10).x - bounds.getX()
            ) * 180.0d / Math.PI);

            return degrees;
        } else {
            float degrees = (float) (Math.atan2(
                    screen.building.getY() - bounds.getY(),
                    screen.building.getY() - bounds.getX()
            ) * 180.0d / Math.PI);

            return degrees;
        }



    }

    private void updateCollision() {
        catchCollision = new Rectangle( bounds.x + 15, bounds.y + 15, 10, 10);
        sightRadius = new Circle(bounds.x + bounds.width/2, bounds.y + bounds.height/2, 200);

    }

    public void draw(ShapeRenderer renderer) {
        super.draw(renderer, Color.RED);

        renderer.set(ShapeRenderer.ShapeType.Point);
        renderer.setColor(Color.MAGENTA);

        for (int i = 0; i < finder.pathList.size(); i++) {
            renderer.rect(finder.pathList.get(i).x, finder.pathList.get(i).y, 2, 2);
        }

        if (target != null) {
//            System.out.println(target.x + ", " + target.y);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(Color.WHITE);
            renderer.circle(target.x, target.y, 5);
        }

        renderer.set(ShapeRenderer.ShapeType.Filled);

    }

    public void drawVisionArc(ShapeRenderer renderer) {
        renderer.setColor(.8f, .8f, 0f, .4f);
        renderer.polygon(arc.getVertices());
        renderer.setColor(Color.WHITE);
        renderer.set(ShapeRenderer.ShapeType.Line);
//        renderer.circle(sightRadius.x, sightRadius.y, sightRadius.radius);
        renderer.set(ShapeRenderer.ShapeType.Filled);

    }

    public enum GuardState {
        LOOKINGLEFT, LOOKINGRIGHT, CHASING, ARRIVING
    }

}
