package cn.edu.hit.triocnv.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yongzhuang Liu
 */
public class InheritanceMatrix {

    private double e;
    private double a;

    public InheritanceMatrix(double e, double a) {
        this.e = e;
        this.a = a;
    }
    
    public double[][][] getMaleXMatrix() {
        double[][][] matrix = new double[5][5][5];
        return matrix;
    }

    public double[][][] getFemaleXMatrix() {

        double[][][] matrix = new double[5][5][5];

        matrix[0][0][0] = 1 - e;
        matrix[0][0][1] = 0.25 * e;
        matrix[0][0][2] = 0.25 * e;
        matrix[0][0][3] = 0.25 * e;
        matrix[0][0][4] = 0.25 * e;
        
        matrix[1][0][0] = 0.25 * e;
        matrix[1][0][1] = 1-e;
        matrix[1][0][2] = 0.25 * e;
        matrix[1][0][3] = 0.25 * e;
        matrix[1][0][4] = 0.25 * e;
        
        matrix[2][0][0] = 0.25 * e;
        matrix[2][0][1] = 0.25 * e;
        matrix[2][0][2] = 1 - e;
        matrix[2][0][3] = 0.25 * e;
        matrix[2][0][4] = 0.25 * e;
        
        matrix[3][0][0] = 0.25 * e;
        matrix[3][0][1] = 0.25 * e;
        matrix[3][0][2] = 0.25 * e;
        matrix[3][0][3] = 1 - e;
        matrix[3][0][4] = 0.25 * e;
        
        matrix[4][0][0] = 0.25 * e;
        matrix[4][0][1] = 0.25 * e;
        matrix[4][0][2] = 0.25 * e;
        matrix[4][0][3] = 0.25 * e;
        matrix[4][0][4] = 1 - e;
        
        matrix[0][1][0] = 0.5 * (1 - e);
        matrix[0][1][1] = 0.5 * (1 - e);
        matrix[0][1][2] = 0.3333 * e;
        matrix[0][1][3] = 0.3333 * e;
        matrix[0][1][4] = 0.3333 * e;
        
        matrix[1][1][0] = 0.3333 * e;
        matrix[1][1][1] = 0.5 * (1 - e);
        matrix[1][1][2] = 0.5 * (1 - e);
        matrix[1][1][3] = 0.3333 * e;
        matrix[1][1][4] = 0.3333 * e;
        
        matrix[2][1][0] = 0.3333 * e;
        matrix[2][1][1] = 0.3333 * e;
        matrix[2][1][2] = 0.5 * (1 - e);
        matrix[2][1][3] = 0.5 * (1 - e);
        matrix[2][1][4] = 0.3333 * e;
        
        matrix[3][1][0] = 0.3333 * e;
        matrix[3][1][1] = 0.3333 * e;
        matrix[3][1][2] = 0.3333 * e;
        matrix[3][1][3] = 0.5 * (1 - e);
        matrix[3][1][4] = 0.5 * (1 - e);
        
        matrix[4][1][0] = 0.25 * e;
        matrix[4][1][1] = 0.25 * e;
        matrix[4][1][2] = 0.25 * e;
        matrix[4][1][3] = 0.25 * e;
        matrix[4][1][4] = 1 - e;
        
        matrix[0][2][0] = 0.5 * a * (1 - e) + 0.25 * e;
        matrix[0][2][1] = (1 - a) * (1 - e);
        matrix[0][2][2] = 0.5 * a * (1 - e) + 0.25 * e;
        matrix[0][2][3] = 0.25 * e;
        matrix[0][2][4] = 0.25 * e;
                
        matrix[1][2][0] = 0.25 * e;
        matrix[1][2][1] = 0.5 * a * (1 - e) + 0.25 * e;
        matrix[1][2][2] = (1 - a) * (1 - e);
        matrix[1][2][3] = 0.5 * a * (1 - e) + 0.25 * e;
        matrix[1][2][4] = 0.25 * e;

        matrix[2][2][0] = 0.25 * e;
        matrix[2][2][1] = 0.25 * e;
        matrix[2][2][2] = 0.5 * a * (1 - e) + 0.25 * e;
        matrix[2][2][3] = (1 - a) * (1 - e);
        matrix[2][2][4] = 0.5 * a * (1 - e) + 0.25 * e;

        matrix[3][2][0] = 0.25 * e;
        matrix[3][2][1] = 0.25 * e;
        matrix[3][2][2] = 0.25 * e;
        matrix[3][2][3] = 0.5 * a * (1 - e) + 0.25 * e;
        matrix[3][2][4] = (1 - 0.5 * a) * (1 - e);

        matrix[4][2][0] = 0.25 * e;
        matrix[4][2][1] = 0.25 * e;
        matrix[4][2][2] = 0.25 * e;
        matrix[4][2][3] = 0.25 * e;
        matrix[4][2][4] = 1 - e;
        
        matrix[0][3][0] = 0.5 * a * (1 - e) + 0.3333 * e;
        matrix[0][3][1] = 0.5 * (1 - a) * (1 - e);
        matrix[0][3][2] = 0.5 * (1 - a) * (1 - e);
        matrix[0][3][3] = 0.5 * a * (1 - e) + 0.3333 * e;
        matrix[0][3][4] = 0.3333 * e;
        
        matrix[1][3][0] = 0.3333 * e;
        matrix[1][3][1] = 0.5 * a * (1 - e) + 0.3333 * e;
        matrix[1][3][2] = 0.5 * (1 - a) * (1 - e);
        matrix[1][3][3] = 0.5 * (1 - a) * (1 - e);
        matrix[1][3][4] = 0.5 * a * (1 - e) + 0.3333 * e;
        
        matrix[2][3][0] = 0.3333 * e;
        matrix[2][3][1] = 0.3333 * e;
        matrix[2][3][2] = 0.5 * a * (1 - e) + 0.3333 * e;
        matrix[2][3][3] = 0.5 * (1 - a) * (1 - e);
        matrix[2][3][4] = 0.5 * (1 - e);
        
        matrix[3][3][0] = 0.25 * e;
        matrix[3][3][1] = 0.25 * e;
        matrix[3][3][2] = 0.25 * e;
        matrix[3][3][3] = 0.5 * a * (1 - e) + 0.25 * e;
        matrix[3][3][4] = (1 - 0.5 * a) * (1 - e);
        
        matrix[4][3][0] = 0.25 * e;
        matrix[4][3][1] = 0.25 * e;
        matrix[4][3][2] = 0.25 * e;
        matrix[4][3][3] = 0.25 * e;
        matrix[4][3][4] = 1 - e;
        
        matrix[0][4][0] = 0.5 * e;
        matrix[0][4][1] = 0.25 * (1 - e);
        matrix[0][4][2] = 0.5 * (1 - e);
        matrix[0][4][3] = 0.25 * (1 - e);
        matrix[0][4][4] = 0.5 * e;
        
        matrix[1][4][0] = 0.5 * e;
        matrix[1][4][1] = 0.5 * e;
        matrix[1][4][2] = 0.25 * (1 - e);
        matrix[1][4][3] = 0.5 * (1 - e);
        matrix[1][4][4] = 0.25 * (1 - e);
        
        matrix[2][4][0] = 0.3333 * e;
        matrix[2][4][1] = 0.3333 * e;
        matrix[2][4][2] = 0.3333 * e;
        matrix[2][4][3] = 0.25 * (1 - e);
        matrix[2][4][4] = 0.75 * (1 - e);
        
        matrix[3][4][0] = 0.25 * e;
        matrix[3][4][1] = 0.25 * e;
        matrix[3][4][2] = 0.25 * e;
        matrix[3][4][3] = 0.25 * e;
        matrix[3][4][4] = 1 - e;
        
        matrix[4][4][0] = 0.25 * e;
        matrix[4][4][1] = 0.25 * e;
        matrix[4][4][2] = 0.25 * e;
        matrix[4][4][3] = 0.25 * e;
        matrix[4][4][4] = 1 - e;
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    matrix[i][j][k] = Math.log(matrix[i][j][k]);
                }
            }
        }
        
        return matrix;
    }

    public double[][][] getAutoMatrix() {

        double[][][] matrix = new double[5][5][5];
        
        matrix[0][0][0] = 1.0 - e;
        matrix[0][0][1] = 0.25 * e;
        matrix[0][0][2] = 0.25 * e;
        matrix[0][0][3] = 0.25 * e;
        matrix[0][0][4] = 0.25 * e;

        matrix[0][1][0] = 0.5 * (1.0 - e);
        matrix[0][1][1] = 0.5 * (1.0 - e);
        matrix[0][1][2] = 0.333 * e;
        matrix[0][1][3] = 0.333 * e;
        matrix[0][1][4] = 0.333 * e;

        matrix[1][1][0] = 0.25 * (1.0 - e);
        matrix[1][1][1] = 0.5 * (1.0 - e);
        matrix[1][1][2] = 0.25 * (1.0 - e);
        matrix[1][1][3] = 0.5 * e;
        matrix[1][1][4] = 0.5 * e;

        matrix[0][2][0] = 0.5 * a * (1.0 - e) + 0.25 * e;
        matrix[0][2][1] = (1.0 - a) * (1.0 - e);
        matrix[0][2][2] = 0.5 * a * (1.0 - e) + 0.25 * e;
        matrix[0][2][3] = 0.25 * e;
        matrix[0][2][4] = 0.25 * e;

        matrix[1][2][0] = 0.25 * a * (1.0 - e) + 0.3333 * e;
        matrix[1][2][1] = (0.5 - 0.25 * a) * (1.0 - e);
        matrix[1][2][2] = (0.5 - 0.25 * a) * (1.0 - e);
        matrix[1][2][3] = 0.25 * a * (1.0 - e) + 0.3333 * e;
        matrix[1][2][4] = 0.333 * e;

        matrix[2][2][0] = 0.25 * a * a * (1.0 - e) + 0.25 * e;
        matrix[2][2][1] = a * (1.0 - a) * (1.0 - e) + 0.25 * e;
        matrix[2][2][2] = ((1.0 - a) * (1.0 - a) + 0.5 * a * a) * (1.0 - e);
        matrix[2][2][3] = a * (1.0 - a) * (1.0 - e) + 0.25 * e;
        matrix[2][2][4] = 0.25 * a * a * (1.0 - e) + 0.25 * e;

        matrix[0][3][0] = 0.5 * a * (1 - e) + 0.3333 * e;
        matrix[0][3][1] = 0.5 * (1 - a) * (1 - e);
        matrix[0][3][2] = 0.5 * (1 - a) * (1 - e);
        matrix[0][3][3] = 0.5 * a * (1 - e) + 0.3333 * e;
        matrix[0][3][4] = 0.333 * e;

        matrix[1][3][0] = 0.25 * a * (1 - e) + 0.5 * e;
        matrix[1][3][1] = 0.25 * (1 - e);
        matrix[1][3][2] = 0.5 * (1 - a) * (1 - e);
        matrix[1][3][3] = 0.25 * (1 - e);
        matrix[1][3][4] = 0.25 * a * (1 - e) + 0.5 * e;

        matrix[2][3][0] = 0.25 * a * a * (1 - e) + 0.3333 * e;
        matrix[2][3][1] = 0.75 * a * (1 - a) * (1 - e) + 0.3333 * e;
        matrix[2][3][2] = (0.5 * (1 - a) * (1 - a) + 0.25 * a * (1 - a) + 0.25 * a * a) * (1 - e);
        matrix[2][3][3] = (0.5 * (1 - a) * (1 - a) + 0.25 * a * (1 - a) + 0.25 * a * a) * (1 - e);
        matrix[2][3][4] = (0.75 * a * (1 - a) + 0.25 * a * a) * (1 - e) + 0.3333 * e;

        matrix[3][3][0] = 0.25 * a * a * (1 - e) + 0.5 * e;
        matrix[3][3][1] = 0.5 * a * (1 - a) * (1 - e) + 0.5 * e;
        matrix[3][3][2] = (0.5 * a * (1 - a) + 0.25 * (1 - a) * (1 - a)) * (1 - e);
        matrix[3][3][3] = (0.5 * (1 - a) * (1 - a) + 0.5 * a * a) * (1 - e);
        matrix[3][3][4] = (0.25 * (1 - a) * (1 - a) + a * (1 - a) + 0.25 * a * a) * (1 - e);

        matrix[3][3][0] = 0.25 * a * a * (1 - e) + 0.5 * e;
        matrix[3][3][1] = 0.5 * a * (1 - a) * (1 - e) + 0.5 * e;
        matrix[3][3][2] = (0.5 * a * (1 - a) + 0.25 * (1 - a) * (1 - a)) * (1 - e);
        matrix[3][3][3] = (0.5 * (1 - a) * (1 - a) + 0.5 * a * a) * (1 - e);
        matrix[3][3][4] = (0.25 * (1 - a) * (1 - a) + a * (1 - a) + 0.25 * a * a) * (1 - e);

        matrix[0][4][0] = 0.5 * e;
        matrix[0][4][1] = 0.25 * (1 - e);
        matrix[0][4][2] = 0.5 * (1 - e);
        matrix[0][4][3] = 0.25 * (1 - e);
        matrix[0][4][4] = 0.5 * e;

        matrix[1][4][0] = e;
        matrix[1][4][1] = 0.125 * (1 - e);
        matrix[1][4][2] = 0.375 * (1 - e);
        matrix[1][4][3] = 0.375 * (1 - e);
        matrix[1][4][4] = 0.125 * (1 - e);

        matrix[2][4][0] = 0.5 * e;
        matrix[2][4][1] = 0.125 * a * (1 - e) + 0.5 * e;
        matrix[2][4][2] = 0.25 * (1 - e);
        matrix[2][4][3] = (0.5 - 0.25 * a) * (1 - e);
        matrix[2][4][4] = (0.25 + 0.125 * a) * (1 - e);

        matrix[3][4][0] = 0.5 * e;
        matrix[3][4][1] = 0.125 * a * (1 - e) + 0.5 * e;
        matrix[3][4][2] = 0.125 * (1 + a) * (1 - e);
        matrix[3][4][3] = 0.125 * (3 - 2 * a) * (1 - e);
        matrix[3][4][4] = 0.5 * (1 - e);

        matrix[4][4][0] = 0.5 * e;
        matrix[4][4][1] = 0.5 * e;
        matrix[4][4][2] = 0.0625 * (1 - e);
        matrix[4][4][3] = 0.25 * (1 - e);
        matrix[4][4][4] = 0.6875 * (1 - e);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < i; j++) {
                for (int k = 0; k < 5; k++) {
                    matrix[i][j][k] = matrix[j][i][k];
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    matrix[i][j][k] = Math.log(matrix[i][j][k]);
                }
            }
        }

        return matrix;
    }

    public double[][][] getAutoMendelianMatrix() {
        double[][][] matrix = new double[5][5][5];
        matrix[0][0][0] = 1.0;
        matrix[0][1][0] = 0.5;
        matrix[1][1][0] = 0.25;
        matrix[0][1][1] = 0.5;
        matrix[1][1][1] = 0.5;
        matrix[1][1][2] = 0.5;
        matrix[0][2][0] = 0.5 * a;
        matrix[1][2][0] = 0.25 * a;
        matrix[2][2][0] = 0.25 * a * a;
        matrix[0][2][1] = 1 - a;
        matrix[1][2][1] = 0.5 - 0.25 * a;
        matrix[2][2][1] = a * (1 - a);
        matrix[0][2][2] = 0.5 * a;
        matrix[1][2][2] = 0.5 - 0.25 * a;
        matrix[2][2][2] = (1 - a) * (1 - a) + 0.5 * a * a;
        matrix[1][2][3] = 0.25 * a;
        matrix[2][2][3] = a * (1 - a);
        matrix[2][2][4] = 0.25 * a * a;
        matrix[0][3][0] = 0.5 * a;
        matrix[1][3][0] = 0.25 * a;
        matrix[2][3][0] = 0.25 * a * a;
        matrix[3][3][0] = 0.25 * a * a;
        matrix[0][3][1] = 0.5 * (1 - a);
        matrix[1][3][1] = 0.25;
        matrix[2][3][1] = 0.75 * a * (1 - a);
        matrix[3][3][1] = 0.5 * a * (1 - a);
        matrix[0][3][2] = 0.5 * a;
        matrix[1][3][2] = 0.25;
        matrix[2][3][2] = 0.5 * (1 - a) * (1 - a) + 0.25 * a * (1 - a) + 0.25 * a * a;
        matrix[3][3][2] = 0.5 * a * (1 - a) + 0.25 * (1 - a) * (1 - a);
        matrix[0][3][3] = 0.5 * a;
        matrix[1][3][3] = 0.25;
        matrix[2][3][3] = 0.5 * (1 - a) * (1 - a) + 0.25 * a * (1 - a) + 0.25 * a * a;
        matrix[3][3][3] = 0.5 * (1 - a) * (1 - a) + 0.5 * a * a;
        matrix[0][3][4] = 0;
        matrix[1][3][4] = 0.25 * a;
        matrix[2][3][4] = 0.75 * a * (1 - a) + 0.25 * a * a;
        matrix[3][3][4] = 0.25 * (1 - a) * (1 - a) + a * (1 - a) + 0.25 * a * a;
        matrix[0][4][1] = 0.25;
        matrix[1][4][1] = 0.125;
        matrix[2][4][1] = 0.125 * a;
        matrix[3][4][1] = 0.125 * a;
        matrix[0][4][2] = 0.5;
        matrix[1][4][2] = 0.375;
        matrix[2][4][2] = 0.25;
        matrix[3][4][2] = 0.125 * (1 + a);
        matrix[4][4][2] = 0.0625;
        matrix[0][4][3] = 0.25;
        matrix[1][4][3] = 0.375;
        matrix[2][4][3] = 0.5 - 0.25 * a;
        matrix[3][4][3] = 0.125 * (3 - 2 * a);
        matrix[4][4][3] = 0.25;
        matrix[1][4][4] = 0.125;
        matrix[2][4][4] = 0.25 + 0.125 * a;
        matrix[3][4][4] = 0.5;
        matrix[4][4][4] = 0.6875;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < i; j++) {
                for (int k = 0; k < 5; k++) {
                    matrix[i][j][k] = matrix[j][i][k];
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    matrix[i][j][k] = Math.log(matrix[i][j][k]);
                }
            }
        }
        return matrix;
    }

//    public static void main(String[] args) {
//        Inheritance inheritance = new Inheritance();
//        double[][][] matrix = inheritance.getAutoMendelianMatrix();
//        System.out.println(matrix[2][4][2]);
//        System.out.println(matrix[4][2][2]);
//    }
}
