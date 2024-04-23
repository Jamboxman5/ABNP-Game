package me.jamboxman5.pathfinder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class PathfinderScreen implements Screen {

    final PathfinderGame game;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;
    boolean found = false;
    PriorityQueue<Node> queue;
    double cameraDefaultX, cameraDefaultY;

    private class Node implements Comparable<Node> {
        int weight;
        int x,y;
        Node parent;
        int teleportNo;
        boolean selected;
        boolean traversed;
        boolean highLighted;
        String nodeRaw;
        Rectangle bounds;
        public Node(String rawNode, int x, int y) {
            nodeRaw = rawNode;
            this.x = x;
            this.y = y;
            if (nodeRaw.contains("F")) {
                weight = 10;
            }
            else if (nodeRaw.contains("T")) {
                weight = 0;
                weight = Integer.parseInt(nodeRaw.substring(1));
            }
            else {
                try {
                    weight = Integer.parseInt(nodeRaw);
                } catch (NumberFormatException e) {}
            }
            bounds = new Rectangle();
        }
        public Color getColor() {
            if (weight == 10) return Color.RED;
            if (weight == 0) return Color.GREEN;
            return new Color((weight/10.0f),(weight/10.0f),(weight/10.0f),1f);
        }
        public int compareTo(Node other) {
            return Integer.compare(weight, other.weight);
        }
    }

    private Node[][] matrix;
    private Node[] selectedNodes = new Node[2];
    boolean searching = false;

    public PathfinderScreen(PathfinderGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280,720);
        shapeRenderer = new ShapeRenderer();
        cameraDefaultX = camera.position.x;
        cameraDefaultY = camera.position.y;
    }
    @Override
    public void show() {
//        FileHandle inputFile = Gdx.files.internal("demos/pathfinder/teleportinput.txt");
        FileHandle inputFile = Gdx.files.internal("demos/pathfinder/input.txt");
        String fullText = inputFile.readString();
        String[] rows = fullText.split("\\n");
        for(int i = 0; i < rows.length; i++) {
            String[] nodes = rows[i].split(" ");
            if (matrix == null) matrix = new Node[rows.length][nodes.length];
            for (int j = 0; j < nodes.length; j++) {
                matrix[i][j] = new Node(nodes[j], j, i);
            }
        }
    }

    //x = j
    //y = i

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Node active = matrix[i][j];
                shapeRenderer.setColor(Color.BLUE);
                if (active.traversed) {
                    shapeRenderer.setColor(Color.ORANGE);
                } if (active.highLighted){
                    shapeRenderer.setColor(Color.GREEN);
                } if (active.selected) {
                    shapeRenderer.setColor(Color.YELLOW);
                }

                active.bounds.x = 32*j;
                active.bounds.y = 32*i;
                active.bounds.width = 32;
                active.bounds.height = 32;
                shapeRenderer.rect(active.bounds.x, 720-32-active.bounds.y, active.bounds.width, active.bounds.height);
                shapeRenderer.setColor(active.getColor());
                shapeRenderer.rect(active.bounds.x + 2, 720-32-active.bounds.y+2, 28, 28);
            }
        }

        shapeRenderer.end();
        game.batch.begin();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Node active = matrix[i][j];
                int x = 32*j;
                int y = 32*i;
                game.font.draw(game.batch, active.nodeRaw, x + 4, 720-32-y + 16);
            }
        }
        //RENDER TEXTURES HERE
        //RENDER FONTS HERE
        game.batch.end();

        if (!searching) {
            boolean found = false;
            if (Gdx.input.isTouched()) {
                for (Node[] nodes : matrix) {
                    if (found) break;
                    for (Node node : nodes) {
                        if (found) break;
                        if (node.bounds.intersects(Gdx.input.getX() + (camera.position.x - cameraDefaultX), Gdx.input.getY() - (camera.position.y - cameraDefaultY), 1, 1)) {
                            if (node.selected) {

                            } else {
                                if (selectedNodes[0] == null) {
                                    selectedNodes[0] = node;
                                    node.selected = true;
                                }
                                else if (selectedNodes[1] == null) {
                                    selectedNodes[1] = node;
                                    node.selected = true;
                                } else {
                                    for (Node n : selectedNodes) {
                                        n.selected = false;
                                    }
                                    selectedNodes = new Node[2];
                                    selectedNodes[0] = node;
                                    node.selected = true;
                                }
                            }

//                            System.out.println(node.nodeRaw);
                            found = true;
                        }
                    }
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                if (selectedNodes[0] != null && selectedNodes[1] != null) {
                    searching = true;
                    System.out.println("Starting pathfinder...");
                }

            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

                camera.translate(-delta*500, 0);
                shapeRenderer.translate(delta*500, 0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                camera.translate(delta*500, 0);
                shapeRenderer.translate(-delta*500, 0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                camera.translate(0, -delta*500);
                shapeRenderer.translate(0, delta*500, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                camera.translate(0, delta*750);
                shapeRenderer.translate(0, -delta*750, 0);
            }



        } else {

            unTraverse();
            dijkstra(selectedNodes[0], selectedNodes[1]);

            if (found) {
                System.out.println("Path found!");
                findPath(selectedNodes[1], selectedNodes[0]);
            } else {
                System.out.println("No path found!");
            }

            selectedNodes[0].selected = false;
            selectedNodes[1].selected = false;
            selectedNodes = new Node[2];
            searching = false;

        }

    }

    void unTraverse() {
        found = false;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Node active = matrix[i][j];
                active.parent = null;
                active.traversed = false;
                active.highLighted = false;
            }
        }
    }

    void dijkstra(Node start, Node end) {
        if (end.traversed) return;
        if (start.traversed) return;
        if (start.weight == 10) return;
        start.traversed = true;

        PriorityQueue<Node> operatingSet = new PriorityQueue<>();
        Set<Node> nextSet = new HashSet<>();
        operatingSet.addAll(getNeighbors(start));

        while (!end.traversed) {
            while (operatingSet.peek() != null) {
                Node current = operatingSet.poll();
                current.traversed = true;
                Set<Node> neighbors = getNeighbors(current);
                nextSet.addAll(neighbors);
                for (Node n : neighbors) if (n.weight == 10) nextSet.remove(n);
                for (Node n : neighbors) {
                    if (n.parent == null)
                        n.parent = current;
                }


            }
            if (nextSet.isEmpty()) break;
            operatingSet.clear();
            operatingSet.addAll(nextSet);
            nextSet.clear();
        }

        if (end.traversed) found = true;

    }

    void findPath(Node goal, Node start) {
        Node ptr = goal;
        goal.highLighted = true;
        start.highLighted = true;

        while (ptr.parent != null) {
            ptr = ptr.parent;
            ptr.highLighted = true;
        }

    }

    private Set<Node> getNeighbors(Node current) {
        if (current == null) return new HashSet<>();
        Set<Node> neighbors = new HashSet<>();
        if (current.x - 1 >= 0) neighbors.add(matrix[current.y][current.x-1]);
        if (current.x + 1 < matrix[current.y].length) neighbors.add(matrix[current.y][current.x+1]);
        if (current.y - 1 >= 0) neighbors.add(matrix[current.y-1][current.x]);
        if (current.y + 1 < matrix.length ) neighbors.add(matrix[current.y+1][current.x]);

        Set<Node> toRemove = new HashSet<>();
        Set<Node> toAdd = new HashSet<>();

        for (Node n : neighbors) {
            if (isTeleporter(n) && !n.traversed) {
                n.traversed = true;
                toAdd.addAll(getNeighbors(getOtherTeleport(n)));
            }
        }
        neighbors.addAll(toAdd);

        for (Node n : neighbors) {
            if (n.traversed) toRemove.add(n);
            if (n.weight == 10) toRemove.add(n);
        }
        neighbors.removeAll(toRemove);

        return neighbors;
    }

    private Node getOtherTeleport(Node n) {
        if (!isTeleporter(n)) return null;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Node active = matrix[i][j];
                if (!active.traversed && active.nodeRaw.equalsIgnoreCase(n.nodeRaw)) return active;
            }
        }
        return null;
    }
    private boolean isTeleporter(Node n) {
        return n.nodeRaw.contains("T");
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
