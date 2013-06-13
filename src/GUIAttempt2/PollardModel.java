/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIAttempt2;

import ECGroups.PollardRho;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author benbenbenultra
 */
public class PollardModel extends ECGModel {

    private PollardRho solver;
    private ArrayList<ECViewPoint> points;
    private ECGModel mod;
    private String[][] logs;

    public PollardModel(ECGModel m, PollardRho pr) {//, ECViewPoint[] p){
        super(m.getECG());
        solver = pr;
        mod = m;
        m.setDLPLogSeen(true);
        super.setA(m.getA());
        super.setB(m.getB());
        super.setPFromPrime(m.getP());
        super.setECG(m.getECG());
        
        this.setPoints(mod.getGroupGeneratedBy(mod.convertECPointToECViewPoint(pr.getPointP())));
        super.clearSelectedPoints();
        this.setLogs(pr);

    }

    private void setLogs(PollardRho pr) {
        int k = 0, i = 0;
        for (String s : pr.getInfo()) {
//            System.out.println("k= " + k + "|" + s);
            if (s.startsWith("reset")) {
                i++;
//                System.err.println("i++ ="+ i);
            }
            k++;
        }
        //using that, we can figure out how big to make logs...
        // pr.getInfo.size -1 will be the last index we want to access... i will be the ones we skip, so the offest..
        
        logs = new String[(pr.getInfo().size() - i) / 10][10];
//        System.err.println("PollardRho reset " + i + " times");
        for (int j = 0; j < logs.length; j++) {
            logs[j] = new String[10];
            for (k = i; k < (i + 10); k++) {
                int x = (j * 10) + k;
                if (pr.getInfo().get(x).startsWith("PollardRho took")) {
                    logs[j][k-i] = pr.getInfo().get(x); // i is offset, j*10 jumps us forward, k is index we want
                    break;
                }
                logs[j][k-i] = pr.getInfo().get(x); // i is offset, j*10 jumps us forward, k is index we want
            }
//            System.err.print("log[" + j + "]={");
//            for (int d = 0; d < 10; d++) {
////                System.err.print(logs[j][d] + ",");
//            }
//            System.err.println("}");
        }
    }

    @Override
    public List<ECViewPoint> getOrderedPoints() {
        return points;
    }

    @Override
    int getGroupSize() {
        return points.size();
    }

    public ECViewPoint getPointP() {
//        System.err.println(solver.getPointP());
//        System.out.println("benben"+ super.convertECPointToECViewPoint(solver.getP()));
        return mod.convertECPointToECViewPoint(solver.getPointP());
    }

    public ECViewPoint getPointQ() {
        return mod.convertECPointToECViewPoint(solver.getPointQ());
    }

    private void setPoints(ECViewPoint[] pts) {
        points = new ArrayList<ECViewPoint>();
        for (int i = 0; i < pts.length; i++) {
//            System.out.println(pts[i]);
            points.add(new ECViewPoint(pts[i].toString(), super.getP_prime()));
        }
        // debug: print the points
        System.out.println("PollardModel has the following points (total of  "+points.size()+" points): ");
        for (ECViewPoint p : points) {
            System.out.println(p.toString()+" , ");
        }
    }

    String[] getLogsAt(int i) {
        return logs[i];
    }

    int getLogsLength() {
        return logs.length;
    }
}
