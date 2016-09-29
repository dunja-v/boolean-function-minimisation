package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import GUI.LogicCircuit.JAndOperator;
import GUI.LogicCircuit.JConstant;
import GUI.LogicCircuit.JNotOperator;
import GUI.LogicCircuit.JOperand;
import GUI.LogicCircuit.JOperator;
import GUI.LogicCircuit.JOrOperator;
import GUI.LogicCircuit.JVariable;
import Minimisation.Implicant;

public class LogicCircuitPanel extends JPanel {

    private static final long serialVersionUID = 148332162784446079L;

    private static final int DIMENSION = JOperand.DIMENSION;
    /** Horizontal distance between constants 0 and 1. */
    private static final int CONST_DISTANCE = 2 * DIMENSION;
    /** Horizontal distance between variables. */
    private static final int VAR_DISTANCE = 4 * DIMENSION;
    /** Distance from variable to not operator. */
    private static final int NOT_DISTANCE = DIMENSION;
    /** Vertical distance between circuits. */
    private static final int CIRCUIT_DISTANCE = 3 * DIMENSION;
    /** Horizontal distance between circuits and final operator. */
    private static final int FINAL_OP_DIST = 8 * DIMENSION;
    /** Length of the line coming out of an operator. */
    private static final int OP_LINE_LENGTH = 2 * DIMENSION;
    /** Length of a line coming out of the final operator. */
    private static final int FINAL_OP_LINE_LENGTH = 4 * DIMENSION;
    /**
     * Distance between variables and logical operators (both vertical
     * and horizontal.
     */
    private static final int VAR_CIRCUIT_DISTANCE = 4 * DIMENSION;
    /**
     * Horizontal distance between logical circuit and truth table.
     */
    private static final int CIRCUIT_TABLE_DISTANCE = 2 * DIMENSION;
    /** Vertical distance between truth table and K-Map. */
    private static final int TABLE_MAP_DISTANCE = 3 * DIMENSION;
    /** Radius of the circle marking line connections. */
    private static final int RADIUS = 4;

    // for zooming and floatable
    private double zoom = 1;
    private double x = 0;
    private double y = 0;

    private KMap map;
    private TruthTable table;

    private int notDelay;
    private int andDelay;
    private int orDelay;

    private int numOfVars;
    private boolean isSum;
    private int[] function;
    private List<JOperand> implicants;
    private JOperator finalOperator;

    private List<JVariable> variables;
    private List<JNotOperator> negatedVariables;

    JButton btnSimulate;

    private int paddingTop;
    private int paddingLeft;

    /**
     * Horizontal position of the line from the first constant (from
     * 0)
     */
    private int firstConstantX;
    /**
     * Horizontal position of the line from the second constant (from
     * 1)
     */
    private int secondConstantX;

    private int afterVariablesX;

    private int varPaddingLeft;

    private int varWidth;
    private int notHeight;
    private int sidelineLength;
    private int varCentre;

    public LogicCircuitPanel() {

        setLayout(null);
        setBackground(Color.WHITE);

        Insets insets = getInsets();
        paddingTop = insets.top + DIMENSION;
        paddingLeft = insets.left + DIMENSION;
        varPaddingLeft = paddingLeft + 2 * CONST_DISTANCE;

    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getZoom() {
        return zoom;
    }

    public void increment(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void initLogicCircuit(int numOfVars, List<JVariable> variables, int[] function,
            boolean isSum) {
        this.numOfVars = numOfVars;
        this.variables = variables;
        this.isSum = isSum;
        this.function = function;
        negatedVariables = new ArrayList<>();

        for (int i = 0; i < numOfVars; i++) {
            JVariable var = variables.get(i);

            List<JOperand> notOperands = new ArrayList<>();
            notOperands.add(var);
            JNotOperator notOperator = new JNotOperator(notOperands, var.getIndex());
            negatedVariables.add(notOperator);
        }
    }

    public void initKMap(String[] varNames, int[] function, int positiveValue) {

        if (map != null) {
            this.remove(map);
        }

        map = new KMap();
        map.initKMap(varNames, function, positiveValue);

        repaint();
    }

    public void initTable(String[] varNames, int[] function, int positiveValue) {

        if (table != null) {
            this.remove(table);
        }

        table = new TruthTable();
        table.initTable(varNames, function, positiveValue);

        repaint();
    }

    public void setCurrentFunction(int[] function, List<Implicant> selectedImplicants) {
        map.setSelectedFunction(function, selectedImplicants);
        table.setGivenFunction(function);
    }

    public void setSelectedImplicants(List<Implicant> selectedImplicants) {
        
        int numOfImpl = selectedImplicants.size();
        implicants = new ArrayList<>();

        if (selectedImplicants != null && numOfImpl != 0) {

            for (Implicant impl : selectedImplicants) {

                List<JOperand> implicantOperands = new ArrayList<>();
                int[] varValues = impl.getVarValues();

                for (int i = 0; i < varValues.length; i++) {
                    if ((varValues[i] == 1 && isSum) || (varValues[i]==0 && !isSum)) {
                        implicantOperands.add(variables.get(i));
                    } else if ((varValues[i] == 0 && isSum) || (varValues[i]==1 && !isSum)) {
                        implicantOperands.add(negatedVariables.get(i));
                    }
                }

                JOperand op;
                if (implicantOperands.size() == 0) {
                    int value = isSum ? 1 : 0;
                    op = new JConstant(value);

                } else if (implicantOperands.size() == 1) {
                    op = implicantOperands.get(0);

                } else {
                    if (isSum)
                        op = new JAndOperator(implicantOperands);
                    else
                        op = new JOrOperator(implicantOperands);
                }
                implicants.add(op);
            }

            if (implicants.size() > 1) {
                if (isSum) {
                    finalOperator = new JOrOperator(implicants);
                } else {
                    finalOperator = new JAndOperator(implicants);
                }
            } else {
                finalOperator = null;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        removeAll();
        
        g.setColor(Color.BLACK);

        Graphics2D graphics = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.scale(zoom, zoom);
        graphics.transform(at);

        if (variables != null) {

            drawVariables(g);
            drawLogicCircuit(g);

        }

        int distance = afterVariablesX + VAR_CIRCUIT_DISTANCE + JAndOperator.WIDTH * 2
                + CIRCUIT_DISTANCE + +FINAL_OP_DIST + CIRCUIT_TABLE_DISTANCE;

        Dimension tableSize;
        if (table != null) {
            tableSize = table.getPreferredSize();
            table.setBounds(new Rectangle(distance, paddingTop, tableSize.width, tableSize.height));
            add(table);
        } else {
            tableSize = new Dimension(0, 0);
        }

        if (map != null) {
            Dimension mapSize = map.getPreferredSize();
            map.setBounds(distance, paddingTop + tableSize.height + TABLE_MAP_DISTANCE,
                    mapSize.width, mapSize.height);
            add(map);
        }

    }

    private void drawLogicCircuit(Graphics g) {

        afterVariablesX = varPaddingLeft + numOfVars * varWidth + VAR_CIRCUIT_DISTANCE;

        if (implicants == null || implicants.size() == 0) {
            int beginX;
            String constant = null;

            if (variables.size() == 0) {
                constant = String.valueOf(function[0]);
                if (function[0] == 0)
                    beginX = firstConstantX;
                else
                    beginX = secondConstantX;

            } else {
                if (isSum) {
                    beginX = firstConstantX;
                    constant = "0";
                } else {
                    beginX = secondConstantX;
                    constant = "1";
                }
            }

            int endX = 2 * CIRCUIT_DISTANCE + 2 * JAndOperator.WIDTH + afterVariablesX;
            int beginY = notHeight + VAR_CIRCUIT_DISTANCE
                    + (int) Math.round(JAndOperator.HEIGHT / 2.);
            g.drawLine(beginX, beginY, endX, beginY);
            g.fillOval(beginX - RADIUS, beginY - RADIUS, 2 * RADIUS, 2 * RADIUS);
            g.drawString(constant, endX - DIMENSION, beginY - DIMENSION);

        } else if (implicants.size() == 1) {
            int x = afterVariablesX;
            int y = notHeight + VAR_CIRCUIT_DISTANCE;
            JOperand op = implicants.get(0);

            drawImplicant(x, y, op, g);

            int beginX = x + JAndOperator.WIDTH;
            int endX = beginX + CIRCUIT_DISTANCE + JAndOperator.WIDTH;
            int beginY = y + (int) Math.round(JAndOperator.HEIGHT / 2.);
            g.drawLine(beginX, beginY, endX, beginY);

            String value = op.getValue() != -1 ? String.valueOf(op.getValue()) : "X";
            g.drawString(value, endX - DIMENSION, beginY - DIMENSION);

        } else {
            int finalOpX = afterVariablesX + 2 * CIRCUIT_DISTANCE + JAndOperator.WIDTH;
            int finalOpY = (int) Math.round(notHeight + VAR_CIRCUIT_DISTANCE
                    + (implicants.size() / 2.) * (CIRCUIT_DISTANCE + JAndOperator.HEIGHT));
            int finalOpLinesDistY = (int) Math
                    .round(JAndOperator.HEIGHT / (implicants.size() + 1.));
            int finalOpLinesDistX = (int) Math
                    .round((FINAL_OP_DIST - 2 * OP_LINE_LENGTH) / (implicants.size()));

            int implicantsCount = 0;
            for (JOperand impl : implicants) {
                int y = notHeight + implicantsCount * (CIRCUIT_DISTANCE + JAndOperator.HEIGHT)
                        + VAR_CIRCUIT_DISTANCE;
                int x = afterVariablesX;

                drawImplicant(x, y, impl, g);

                int horDist;
                if (implicantsCount < (implicants.size() / 2)) {
                    horDist = (implicants.size() / 2 - implicantsCount) * finalOpLinesDistX;
                } else {
                    horDist = (implicantsCount - implicants.size() / 2) * finalOpLinesDistX;
                }

                int beginX = x + JAndOperator.WIDTH;
                int beginY = y + (int) Math.round(JAndOperator.HEIGHT / 2.);
                int endX = beginX + OP_LINE_LENGTH + horDist;

                // horizontal line from operator
                g.drawLine(beginX, beginY, endX, beginY);

                String value = impl.getValue() != -1 ? String.valueOf(impl.getValue()) : "X";
                g.drawString(value, endX - DIMENSION - horDist, beginY - DIMENSION);

                int endY = finalOpY + (implicantsCount + 1) * finalOpLinesDistY;
                // vertical line between operator and final operator
                g.drawLine(endX, beginY, endX, endY);
                // horizontal line to final operator
                g.drawLine(endX, endY, beginX + FINAL_OP_DIST - OP_LINE_LENGTH, endY);

                implicantsCount++;
            }

            drawFinalOperator(finalOpX, finalOpY, g);
        }

    }

    private void drawImplicant(int x, int y, JOperand op, Graphics g) {
        if (op instanceof JConstant) {
            int value = op.getValue();
            int beginX;
            if (value == 0)
                beginX = firstConstantX;
            else
                beginX = secondConstantX;

            int beginY = y + (int) Math.round(JAndOperator.HEIGHT / 2.);
            int endX = afterVariablesX + JAndOperator.WIDTH;
            g.drawLine(beginX, beginY, endX, beginY);
            g.fillOval(beginX - RADIUS, beginY - RADIUS, 2 * RADIUS, 2 * RADIUS);

        } else if (op instanceof JVariable || op instanceof JNotOperator) {
            int beginX = getBeginX(op);

            int beginY = y + (int) Math.round(JAndOperator.HEIGHT / 2.);
            int endX = afterVariablesX + JAndOperator.WIDTH;
            g.drawLine(beginX, beginY, endX, beginY);
            g.fillOval(beginX - RADIUS, beginY - RADIUS, 2 * RADIUS, 2 * RADIUS);

        } else {
            JOperator operator = (JOperator) op;
            List<JOperand> operands = operator.getOperands();
            int numOfOperands = operands.size();
            int lineDistance = JAndOperator.HEIGHT / (numOfOperands + 1);

            for (int i = 0; i < numOfOperands; i++) {
                JOperand operand = operands.get(i);
                int beginX = getBeginX(operand);
                int endX = afterVariablesX;
                int beginY = y + lineDistance * (i + 1);

                g.drawLine(beginX, beginY, endX, beginY);
                g.fillOval(beginX - RADIUS, beginY - RADIUS, 2 * RADIUS, 2 * RADIUS);
            }

            Dimension operatorSize = operator.getPreferredSize();
            operator.setBounds(x, y, operatorSize.width, operatorSize.height);
            add(operator);
        }

    }

    private int getBeginX(JOperand op) {
        if (op instanceof JVariable) {
            return varPaddingLeft + ((JVariable) op).getIndex() * varWidth + varCentre;
        } else if (op instanceof JNotOperator) {
            return varPaddingLeft + ((JNotOperator) op).getVarIndex() * varWidth + varCentre
                    + sidelineLength;
        }
        return -1;
    }

    private void drawFinalOperator(int x, int y, Graphics g) {
        Dimension operatorSize = finalOperator.getPreferredSize();
        finalOperator.setBounds(x, y, operatorSize.width, operatorSize.height);
        add(finalOperator);

        int beginX = x + operatorSize.width;
        int beginY = y + (int) Math.round(operatorSize.height / 2.);
        int endX = x + operatorSize.width + FINAL_OP_LINE_LENGTH;
        g.drawLine(beginX, beginY, endX, beginY);

        int value = finalOperator.getValue();
        String opValue = value == -1 ? "X" : String.valueOf(value);
        g.drawString("f' = " + opValue, endX - 2*DIMENSION, beginY - DIMENSION);

    }

    private void drawVariables(Graphics g) {

        int numOfPossibleCircuits = (int) Math.pow(2, 2 * numOfVars);
        int lineLength = (JAndOperator.HEIGHT + CIRCUIT_DISTANCE) * numOfPossibleCircuits
                + JNotOperator.HEIGHT + NOT_DISTANCE;
        Dimension varSize = new Dimension(DIMENSION, DIMENSION);

        for (int i = 0; i < numOfVars; i++) {

            JVariable var = variables.get(i);
            add(var);
            var.revalidate();

            varSize = var.getPreferredSize();
            varWidth = VAR_DISTANCE + varSize.width;
            int variableX = i * varWidth + varPaddingLeft;
            var.setBounds(variableX, paddingTop, varSize.width, varSize.height);

            varCentre = (int) Math.round(0.5 * varSize.width);
            int lineX = variableX + varCentre;

            JNotOperator not = negatedVariables.get(i);
            add(not);

            Dimension notSize = not.getPreferredSize();
            int notX = lineX + NOT_DISTANCE;
            int notY = paddingTop + varSize.height + 2 * NOT_DISTANCE;
            not.setBounds(notX, notY, notX + notSize.width, notY + notSize.height);

            notHeight = notY + notSize.height;

            sidelineLength = (int) Math.round(0.5 * notSize.width) + NOT_DISTANCE;

            int value = not.getValue();
            String stringValue = value == -1 ? "X" : String.valueOf(value);
            g.drawString(stringValue, lineX + sidelineLength + DIMENSION,
                    notY + notSize.height + DIMENSION);

            // horizontal line to not operator
            g.drawLine(lineX, notY - NOT_DISTANCE, lineX + sidelineLength, notY - NOT_DISTANCE);
            // vertical line to not operator
            g.drawLine(lineX + sidelineLength, notY - NOT_DISTANCE, lineX + sidelineLength, notY);
            // vertical line from not operator
            g.drawLine(lineX + sidelineLength, notY + notSize.height, lineX + sidelineLength,
                    lineLength);
            // vertical line from variable
            g.drawLine(lineX, paddingTop + varSize.height, lineX, lineLength);

        }

        int constantHeight = paddingTop + (int) Math.round(varSize.height / 2.);
        int constLineHeight = paddingTop + varSize.height;
        g.drawString("0", paddingLeft, constantHeight);
        firstConstantX = paddingLeft + 3;
        g.drawLine(firstConstantX, constLineHeight, paddingLeft + 3, lineLength);
        secondConstantX = paddingLeft + CONST_DISTANCE + 3;
        g.drawString("1", secondConstantX - 3, constantHeight);
        g.drawLine(secondConstantX, constLineHeight, secondConstantX, lineLength);

    }

    public void setDelays(int notDelay, int orDelay, int andDelay) {

        if (notDelay < 0 || orDelay < 0 || andDelay < 0) {
            throw new IllegalArgumentException("Kašnjenje ne može biti negativan broj!");
        }

        this.notDelay = notDelay;
        this.orDelay = orDelay;
        this.andDelay = andDelay;

        if (negatedVariables != null) {
            for (JOperator not : negatedVariables) {
                not.setDelay(notDelay);
            }
        }

        if (implicants != null) {
            for (JOperand op : implicants) {
                if (op instanceof JOperator) {
                    JOperator operator = (JOperator) op;
                    if (operator instanceof JAndOperator) {
                        operator.setDelay(andDelay);
                    } else if (operator instanceof JOrOperator) {
                        operator.setDelay(orDelay);
                    }
                }

            }
        }

    }

    public void calculateValues() {

        if (btnSimulate != null) {
            btnSimulate.setEnabled(false);
        }

        SwingWorker<Void, Void> notWorker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                if (notDelay > 0) {
                    try {
                        Thread.sleep(notDelay);
                    } catch (InterruptedException e) {
                    }
                }
                for (JOperator op : negatedVariables) {
                    op.calculateValue();
                }
                repaint();
                revalidate();

                if (implicants != null) {
                    int delay;
                    int finalDelay;

                    if (!isSum) {
                        delay = orDelay;
                        finalDelay = andDelay;
                    } else {
                        delay = andDelay;
                        finalDelay = orDelay;
                    }

                    if (delay > 0) {
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                        }
                    }
                    for (JOperand op : implicants) {
                        op.calculateValue();
                    }
                    repaint();
                    revalidate();

                    if (finalDelay > 0 && finalOperator != null) {
                        try {
                            Thread.sleep(finalDelay);
                        } catch (InterruptedException e) {
                        }
                    }
                    finalOperator.calculateValue();
                    repaint();
                    revalidate();
                }
                return null;
            }

            @Override
            protected void done() {
                if (btnSimulate != null) {
                    btnSimulate.setEnabled(true);
                }
            }

        };

        SwingWorker<Void, Void> operandsWorker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                if (implicants != null) {
                    int delay;
                    int finalDelay;

                    if (!isSum) {
                        delay = orDelay;
                        finalDelay = andDelay;
                    } else {
                        delay = andDelay;
                        finalDelay = orDelay;
                    }

                    if (delay > 0) {
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                        }
                    }
                    for (JOperand op : implicants) {
                        op.calculateValue();
                    }
                    repaint();
                    revalidate();

                    if (finalDelay > 0 && finalOperator != null) {
                        try {
                            Thread.sleep(finalDelay);
                        } catch (InterruptedException e) {
                        }
                    }
                    finalOperator.calculateValue();
                    repaint();
                    revalidate();
                }
                return null;
            }
        };

        SwingWorker<Void, Void> finalWorker = new SwingWorker<Void, Void>() {

            @Override
            public Void doInBackground() {
                int finalDelay;
                if (!isSum) {
                    finalDelay = andDelay;
                } else {
                    finalDelay = orDelay;
                }
                if (finalDelay > 0 && finalOperator != null) {
                    try {
                        Thread.sleep(finalDelay);
                    } catch (InterruptedException e) {
                    }

                    finalOperator.calculateValue();
                    repaint();
                    revalidate();
                }
                return null;
            }
        };

        notWorker.execute();
        operandsWorker.execute();
        finalWorker.execute();
    }

    public void addSimulationButton(JButton btnSimulate) {
        this.btnSimulate = btnSimulate;

    }

	public void setSelectedRow(int selectedRow) {
		table.setSelectedRow(selectedRow);
		map.setSelectedCell(selectedRow);
		
	}

}
