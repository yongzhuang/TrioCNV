package cn.edu.hit.triocnv.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yongzhuang Liu
 */
public class TransitionProbability {

    private double a;
    private double p;
    private double b;
    private double e=0.0001;

    public TransitionProbability(double p, double a, double b) {
        this.a = a;
        this.p = p;
        this.b = b;
    }

    public double[][][][][][] getTransitionMatrix() {
        double[][][] matrix1 = getSingleMarkerMatrix();
        double[][][][][][] matrix2 = getTwoMarkersInheritance();
        double[][] matrix3 = getTransition();
        double[][][][][][] transition = new double[5][5][5][5][5][5];
        double [][][] matrix4= getAutoMatrix();
        for (int f1 = 0; f1 <= 4; f1++) {
            for (int f2 = 0; f2 <= 4; f2++) {
                for (int m1 = 0; m1 <= 4; m1++) {
                    for (int m2 = 0; m2 <= 4; m2++) {
                        for (int o1 = 0; o1 <= 4; o1++) {
                            for (int o2 = 0; o2 <= 4; o2++) {
                                if (isMendelian(f1, m1, o1) && isMendelian(f2, m2, o2) && matrix2[f1][f2][m1][m2][o1][o2] != 0) {
                                    transition[f1][m1][o1][f2][m2][o2] = Math.log(matrix3[f1][f2] * matrix3[m1][m2] * matrix2[f1][f2][m1][m2][o1][o2]);
                                } else if ((!isMendelian(f1, m1, o1) && !isMendelian(f2, m2, o2))||matrix2[f1][f2][m1][m2][o1][o2] == 0) {
                                    transition[f1][m1][o1][f2][m2][o2] = Math.log(matrix3[f1][f2] * matrix3[m1][m2] * matrix3[o1][o2]);
                                } else {
                                    transition[f1][m1][o1][f2][m2][o2] = Math.log(matrix3[f1][f2] * matrix3[m1][m2] * matrix4[f1][m1][o1] * matrix4[f2][m2][o2]);
                                }
                            }
                        }
                    }
                }
            }
        }
        return transition;
    }
    
    public boolean isMendelian(int fState, int mState, int oState) {
        double[][][] matrix = getSingleMarkerMatrix();
        if (matrix[fState][mState][oState] == 0) {
            return false;
        } else {
            return true;
        }
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

       return matrix;
    }

    public double[][] getTransition() {

        double[][] transition = new double[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == j) {
                    transition[i][j] = 1 - 4 * p;
                } else {
                    transition[i][j] = p;
                }
            }
        }

//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 5; j++) {
//                if (i != 2 && j == 2) {
//                    transition[i][j] = p;
//                } else if (i != 2 && i == j) {
//                    transition[i][j] = 1 - p;
//                } else if (i == 2 && j != 2) {
//                    transition[i][j] = p;
//                } else if (i == 2 && j == 2) {
//                    transition[i][j] = 1 - 4 * p;
//                } else {
//                    transition[i][j] = 0;
//                }
//            }
//        }
//        
        return transition;
    }

    public double[][][] getSingleMarkerMatrix() {
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
        return matrix;
    }

    public double[][][][][][] getTwoMarkersInheritance() {
        int[][] CN = new int[48][6];
        CN[0] = new int[]{0, 0, 0, 0, 0, 0};
        CN[1] = new int[]{0, 1, 0, 0, 0, 1};
        CN[2] = new int[]{0, 2, 0, 0, 0, 2};
        CN[3] = new int[]{0, 2, 0, 1, 0, 1};
        CN[4] = new int[]{0, 3, 0, 0, 0, 3};
        CN[5] = new int[]{0, 3, 0, 1, 0, 2};
        CN[6] = new int[]{0, 4, 0, 1, 0, 3};
        CN[7] = new int[]{0, 4, 0, 2, 0, 2};
        CN[8] = new int[]{1, 1, 0, 0, 1, 1};
        CN[9] = new int[]{1, 2, 0, 0, 1, 2};
        CN[10] = new int[]{1, 2, 0, 1, 1, 1};
        CN[11] = new int[]{1, 3, 0, 0, 1, 3};
        CN[12] = new int[]{1, 3, 0, 2, 1, 1};
        CN[13] = new int[]{1, 4, 0, 3, 1, 1};
        CN[14] = new int[]{1, 4, 0, 2, 1, 2};
        CN[15] = new int[]{2, 2, 1, 1, 1, 1};
        CN[16] = new int[]{2, 2, 0, 0, 2, 2};
        CN[17] = new int[]{2, 3, 1, 1, 1, 2};
        CN[18] = new int[]{2, 3, 0, 0, 2, 3};
        CN[19] = new int[]{2, 4, 1, 1, 1, 3};
        CN[20] = new int[]{2, 4, 1, 2, 1, 2};
        CN[21] = new int[]{2, 4, 0, 2, 2, 2};
        CN[22] = new int[]{3, 3, 0, 0, 3, 3};
        CN[23] = new int[]{3, 3, 1, 1, 2, 2};
        CN[24] = new int[]{3, 4, 1, 1, 2, 3};
        CN[25] = new int[]{3, 4, 1, 2, 2, 2};
        CN[26] = new int[]{4, 4, 2, 2, 2, 2};
        CN[27] = new int[]{4, 4, 1, 1, 3, 3};
        CN[28] = new int[]{1, 0, 0, 0, 1, 0};
        CN[29] = new int[]{2, 0, 0, 0, 2, 0};
        CN[30] = new int[]{2, 0, 1, 0, 1, 0};
        CN[31] = new int[]{2, 1, 0, 0, 2, 1};
        CN[32] = new int[]{2, 1, 1, 0, 1, 1};
        CN[33] = new int[]{3, 0, 0, 0, 3, 0};
        CN[34] = new int[]{3, 0, 1, 0, 2, 0};
        CN[35] = new int[]{3, 1, 0, 0, 3, 1};
        CN[36] = new int[]{3, 1, 2, 0, 1, 1};
        CN[37] = new int[]{3, 2, 1, 1, 2, 1};
        CN[38] = new int[]{3, 2, 0, 0, 3, 2};
        CN[39] = new int[]{4, 0, 1, 0, 3, 0};
        CN[40] = new int[]{4, 0, 2, 0, 2, 0};
        CN[41] = new int[]{4, 1, 3, 0, 1, 1};
        CN[42] = new int[]{4, 1, 2, 0, 2, 1};
        CN[43] = new int[]{4, 2, 1, 1, 3, 1};
        CN[44] = new int[]{4, 2, 2, 1, 2, 1};
        CN[45] = new int[]{4, 2, 2, 0, 2, 2};
        CN[46] = new int[]{4, 3, 1, 1, 3, 2};
        CN[47] = new int[]{4, 3, 2, 1, 2, 2};
        double[] value = new double[48];
        value[0] = 1;
        value[1] = 1;
        value[2] = b;
        value[3] = 1 - b;
        value[4] = b;
        value[5] = 1 - b;
        value[6] = 0.5;
        value[7] = 0.5;
        value[8] = 1;
        value[9] = b;
        value[10] = 1 - b;
        value[11] = b;
        value[12] = 1 - b;
        value[13] = 0.5;
        value[14] = 0.5;
        value[15] = 1 - b;
        value[16] = b;
        value[17] = 1 - b;
        value[18] = b;
        value[19] = 0.5 - 0.5 * b;
        value[20] = 0.5 - 0.5 * b;
        value[21] = b;
        value[22] = b;
        value[23] = 1 - b;
        value[24] = 0.5;
        value[25] = 0.5;
        value[26] = 0.5;
        value[27] = 0.5;
        value[28] = 1;
        value[29] = b;
        value[30] = 1 - b;
        value[31] = b;
        value[32] = 1 - b;
        value[33] = b;
        value[34] = 1 - b;
        value[35] = b;
        value[36] = 1 - b;
        value[37] = 1 - b;
        value[38] = b;
        value[39] = 0.5;
        value[40] = 0.5;
        value[41] = 0.5;
        value[42] = 0.5;
        value[43] = 0.5 - 0.5 * b;
        value[44] = 0.5 - 0.5 * b;
        value[45] = b;
        value[46] = 0.5;
        value[47] = 0.5;
        double[][][][][][] M = new double[5][5][5][5][5][5];
        for (int i = 0; i < 48; i++) {
            for (int j = 0; j < 48; j++) {
                int[] item1 = CN[i];
                int[] item2 = CN[j];
                M[item1[0]][item1[1]][item2[0]][item2[1]][Math.min(4, item1[2] + item2[2])][Math.min(4, item1[3] + item2[3])] += value[i] * value[j] * 0.25;
                M[item1[0]][item1[1]][item2[0]][item2[1]][Math.min(4, item1[2] + item2[4])][Math.min(4, item1[3] + item2[5])] += value[i] * value[j] * 0.25;
                M[item1[0]][item1[1]][item2[0]][item2[1]][Math.min(4, item1[4] + item2[2])][Math.min(4, item1[5] + item2[3])] += value[i] * value[j] * 0.25;
                M[item1[0]][item1[1]][item2[0]][item2[1]][Math.min(4, item1[4] + item2[4])][Math.min(4, item1[5] + item2[5])] += value[i] * value[j] * 0.25;
            }
        }
        return M;
    }

    public static void main(String[] args) {
        
        TransitionProbability t=new TransitionProbability(0.00001, 0.0009, 0.0009);
        double[][][][][][] m=t.getTransitionMatrix();
        
        double[][] m3=t.getTransition();
       
        System.out.println(m[0][0][1][0][0][1]);
        System.out.println(t.isMendelian(0,0,1));
        System.out.println(m3[1][1]);
//        double[][][][][][] n=t.getTwoMarkersInheritance();
//        System.out.println(n[2][2][2][1][2][1]);
      
         

//        for (int f1=0; f1<=4; f1++) {
//		for (int f2=0; f2<=4; f2++) {
//			for (int m1=0; m1<=4; m1++) {
//				for (int m2=0; m2<=4; m2++) {
//					for (int o1=0; o1<=4; o1++) {
//						for (int o2=0; o2<=4; o2++) {
//							if(M[f1][f2][m1][m2][o1][o2] > 0){
//                                                            count++;
//                                                        }
//						}
//					}
//				}
//			}
//		}
//	}



    }
}
