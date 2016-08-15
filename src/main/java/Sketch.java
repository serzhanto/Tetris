import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;


/**
 * Created by Марс on 18.05.2016.
 */
public class Sketch extends PApplet {
    Figure figure;
    Field field;
    PFont font;
    int score = 0;
    boolean isGameOver = false;
    PImage blueBrick;
    PImage reset;
    PImage orangeBrick;
    PImage greenBrick;

    public static final int[][][] BRICKS = {{
            {2, 2, 0},
            {0, 2, 2},
            {0, 0, 0}}, {

            {3, 3, 0},
            {0, 3, 0},
            {0, 3, 0}}, {

            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}}, {

            {1, 1, 0},
            {1, 1, 0},
            {0, 0, 0}}, {

            {0, 0, 0},
            {1, 1, 1},
            {0, 1, 0}}, {

            {0, 2, 2},
            {2, 2, 0},
            {0, 0, 0}}, {


            {0, 3, 3},
            {0, 3, 0},
            {0, 3, 0}}
    };

    public void settings() {
        size(300, 600);
    }

    public void setup() {
        smooth();
        noStroke();
        int[][] a = {{0, 1, 0,}
                , {0, 1, 0}
                , {0, 1, 0}};
        figure = new Figure(120, 0, a);
        field = new Field(10, 20);
        font = createFont("04B_19__.TTF", 20.0f, true);
        textFont(font);
        reset = loadImage("RestartBtn.png");
        blueBrick = loadImage("blueBrick.png");
        orangeBrick = loadImage("orangeBrick.png");
        greenBrick = loadImage("greenBrick.png");

    }

    public void draw() {
        if (isGameOver) {
            imageMode(CENTER);
            image(reset, width / 2, height / 3);
            imageMode(CORNER);
            field.matrix = new int[width][height];
            if (keyPressed) {
                score = 0;
                isGameOver = false;
            }
        } else {
            background(200, 191, 231);

            figure.down();
            if (!figure.isCurrentPositionAviable()) {
                if (figure.y <= 10) isGameOver = true;
                figure.landed();
            }
            field.removeFullLine();
            text("Score: " + score, width - 295, 20);
            field.print();
        }
    }

    public class Figure {
        private int x;
        private int y;
        int[][] matrix;
        int speed;


        public Figure(int x, int y, int[][] a) {
            this.x = x;
            this.y = y;
            this.matrix = a;
            speed = 1;

        }

        public void createRandomFigure() {
            int index = (int) (Math.random() * 7);
            figure.x = 120;
            figure.y = 0;
            figure.matrix = BRICKS[index];
        }


        public void landed() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (this.matrix[i][j] == 1) field.matrix[x / 30 + j][y / 30 + i] = 1;
                    if (this.matrix[i][j] == 2) field.matrix[x / 30 + j][y / 30 + i] = 2;
                    if (this.matrix[i][j] == 3) field.matrix[x / 30 + j][y / 30 + i] = 3;
                }
            }
            createRandomFigure();
        }

        public void down() {
            y = y + speed;

        }


        public void right() {
            x = x + 30;
            if (!isCurrentPositionAviable()) x = x - 30;
        }

        public void left() {
            x = x - 30;
            if (!isCurrentPositionAviable()) x = x + 30;
        }

        public void downMaximum() {

            while (isCurrentPositionAviable()) {

                figure.y++;
            }
            figure.y--;
        }


        public void print() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (matrix[i][j] == 1) image(blueBrick, x + j * 30, y + i * 30);
                    if (matrix[i][j] == 2) image(orangeBrick, x + j * 30, y + i * 30);
                    if (matrix[i][j] == 3) image(greenBrick, x + j * 30, y + i * 30);
                }
            }
        }

        public boolean isCurrentPositionAviable() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (figure.matrix[i][j] == 1 || figure.matrix[i][j] == 2 || figure.matrix[i][j] == 3) {
                        if ((figure.x + j * 30 > 299 || figure.x + j * 30 < 0) || (figure.y + i * 30 >= 570) || (figure.y * 30 < 0))
                            return false;
                        if (field.matrix[(figure.x / 30) + j][((figure.y / 30) + i) + 1] == 1) return false;
                        if (field.matrix[(figure.x / 30) + j][((figure.y / 30) + i) + 1] == 2) return false;
                        if (field.matrix[(figure.x / 30) + j][((figure.y / 30) + i) + 1] == 3) return false;
                    }
                }
            }
            return true;
        }

        public void rotate() {

            if (figure.x >= 0 && figure.x < 240 && figure.y < 540) {
                int[][] tmpMatrix = new int[3][3];

                tmpMatrix[2][0] = matrix[0][0];
                tmpMatrix[2][1] = matrix[1][0];
                tmpMatrix[2][2] = matrix[2][0];

                tmpMatrix[1][0] = matrix[0][1];
                tmpMatrix[1][1] = matrix[1][1];
                tmpMatrix[1][2] = matrix[2][1];

                tmpMatrix[0][0] = matrix[0][2];
                tmpMatrix[0][1] = matrix[1][2];
                tmpMatrix[0][2] = matrix[2][2];

                matrix = tmpMatrix;

            }
        }

    }

    public class Field {
        int width;
        int heigth;
        int matrix[][];

        public Field(int width, int heigth) {
            this.width = width;
            this.heigth = heigth;
            this.matrix = new int[width][heigth];
        }

        public void print() {

            figure.print();
            for (int i = 0; i < field.width; i++) {
                for (int j = 0; j < field.heigth; j++) {
                    if (field.matrix[i][j] == 1) image(blueBrick, i * 30, j * 30);
                    if (field.matrix[i][j] == 2) image(orangeBrick, i * 30, j * 30);
                    if (field.matrix[i][j] == 3) image(greenBrick, i * 30, j * 30);

                }
            }
        }

        public void removeFullLine() {
            int[][] matrix2 = new int[10][20];
            int c2 = 0;

            for (int i = 19; i >= 0; i--) {
                int count = 0;
                for (int j = 0; j < 10; j++) {
                    if (field.matrix[j][i] > 0) count++;
                }

                if (count == 10) {
                    c2++;
                    i--;
                }

                for (int j = 0; j < 10; j++) {
                    matrix2[j][i + c2] = field.matrix[j][i];

                }
            }
            score += ((100 * c2) * c2);
            if (score >= 1500 && score < 3500) figure.speed = 2;
            if (score >= 3500 && score < 6000) figure.speed = 3;
            if (score >= 6000 && score < 9000) figure.speed = 4;
            if (score >= 9000 && score < 12500) figure.speed = 5;
            if (score >= 12500) figure.speed = 6;
            field.matrix = matrix2;
        }
    }

    public void keyPressed() {
        if (keyCode == 37) figure.left();
        if (keyCode == 38) figure.rotate();
        if (keyCode == 39) figure.right();
        if (keyCode == 40) figure.downMaximum();
        if (keyCode == 32) figure.downMaximum();

    }

}