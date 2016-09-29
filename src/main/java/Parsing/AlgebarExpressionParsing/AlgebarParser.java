package Parsing.AlgebarExpressionParsing;

import java.util.Set;
import java.util.TreeSet;

import Parsing.Parser;
import Parsing.ParsingException;
import Parsing.AlgebarExpressionParsing.Expressions.AndExpression;
import Parsing.AlgebarExpressionParsing.Expressions.ConstantExpression;
import Parsing.AlgebarExpressionParsing.Expressions.Expression;
import Parsing.AlgebarExpressionParsing.Expressions.NotExpression;
import Parsing.AlgebarExpressionParsing.Expressions.OperandExpression;
import Parsing.AlgebarExpressionParsing.Expressions.OrExpression;
import Parsing.AlgebarExpressionParsing.Expressions.VarExpression;

public class AlgebarParser implements Parser{
    
    private AlgebarLexer lexer;
    private Token token;
    private Expression root;
    
    private Set<String> variableNames;
    
    private int[] function;

    public AlgebarParser(String input) {
        try{
            lexer = new AlgebarLexer(input.replaceAll("\'\'", ""));
        }catch(LexerException e){
            throw new ParsingException(e.getMessage());
        }
        
        variableNames = new TreeSet<>();
    }
    
    public int[] parse(){
        
        try{
            root = getExpression(TokenType.EOF);
        }catch(LexerException e){
            throw new ParsingException(e.getMessage());
        }
        
        
        int numOfVars = variableNames.size();
        if(numOfVars < 0 || numOfVars >4){
            throw new ParsingException("Broj varijabli mora biti između 1 i 4, a zadano je " + numOfVars +".");
        }        
        
        return evaluate();
        
        
    }    
    
    private Expression getExpression(TokenType endOfExpression) {        
        token = lexer.nextToken();
        
        if(token.getType() == endOfExpression) 
            throw new ParsingException("Pogrešan završetak izraza ili zagrada zatvorena na pogrešnom mjestu.");
        
        if(token.getType() == TokenType.VARIABLE){
            Expression exp = new VarExpression(0, token.getName());
            variableNames.add(token.getName());
            
            return getExpression(exp, endOfExpression);
            
        }else if(token.getType() == TokenType.CONSTANT){
            Expression exp = new ConstantExpression(token.getName());
            
            return getExpression(exp, endOfExpression);            
            
        }else if(token.getType() == TokenType.OPENED_BRACKETS){
            Expression exp = getExpression(TokenType.CLOSED_BRACKETS);
            return getExpression(exp, endOfExpression);
            
        }else{
            throw new ParsingException("Znak " + token.getName() + " ne može stajati na početku izraza.");
        }
        
        
    }

    private Expression getExpression(Expression previous, TokenType endOfExpression) {
        
        token = lexer.nextToken();
        
        if(token.getType() == endOfExpression){
            if(previous instanceof OrExpression && ((OrExpression) previous).getSecondOperand()==null){
                throw new ParsingException("Operator ILI mora imati dva operanda.");
            }
            return previous;
            
        }else if(token.getType() == TokenType.VARIABLE){
            Expression secondOperand = new VarExpression(0, token.getName());
            variableNames.add(token.getName());
            
            if(lexer.peek().getType() == TokenType.NOT_OPERATOR){
                token = lexer.nextToken();
                secondOperand = new NotExpression(secondOperand, null);
            }
            
            return getExpression(new AndExpression(previous, secondOperand), endOfExpression);
            
        }else if(token.getType() == TokenType.CONSTANT){
            Expression secondOperand = new ConstantExpression(token.getName());
            
            if(lexer.peek().getType() == TokenType.NOT_OPERATOR){
                token = lexer.nextToken();
                secondOperand = new NotExpression(secondOperand, null);
            }
            
            return getExpression(new AndExpression(previous, secondOperand), endOfExpression);
            
        }else if (token.getType() == TokenType.NOT_OPERATOR){
            return getExpression(new NotExpression(previous, null), endOfExpression);
            
        }else if(token.getType() == TokenType.OR_OPERATOR){
            
            Expression secondOperand = getExpression(endOfExpression);            
            return new OrExpression(previous, secondOperand);
            
        }else if(token.getType() == TokenType.OPENED_BRACKETS){
            
            Expression secondOperand = getExpression(TokenType.CLOSED_BRACKETS);
            
            if(lexer.peek().getType() == TokenType.NOT_OPERATOR){
                token = lexer.nextToken();
                secondOperand = new NotExpression(secondOperand, null);
            }
            
            return getExpression(new AndExpression(previous, secondOperand), endOfExpression);
            
            
        }else{
            throw new ParsingException("Znak " + token.getName() + " na pogrešnom mjestu u izrazu.");
        }
    }

    private int[] evaluate() {
        String[] variables = new String[variableNames.size()];
        variableNames.toArray(variables);
        
//        System.out.println(Arrays.toString(variables));
        
        int size = (int) Math.pow(2, variables.length);
        function = new int[size];
        
        int[][] table = new int[size][variables.length];
        
        for(int i=0; i<size; i++){
            for (int j=variables.length-1; j>=0; j--) {
                table[i][variables.length - 1 - j]=(i/(int) Math.pow(2, j))%2;
            }
        }
        
        for(int i=0; i<size; i++){
            setVariables(root, variables, table[i]);
            function[i]=root.execute();
            
//            System.out.println(Arrays.toString(table[i])+ " -> " + function[i]);
        }
        
        return function;
    }

    private void setVariables(Expression root, String[] variables, int[] values) {
        if(root instanceof VarExpression){
            for(int i=0; i<variables.length; i++){
                if(((VarExpression) root).getName().equals(variables[i])){
                    ((VarExpression) root).setValue(values[i]);
                }
            }
        }else if(root instanceof ConstantExpression){
            return;
        }else{
            OperandExpression opRoot = (OperandExpression) root;
            if(opRoot.getFirstOperand()!=null){
                setVariables(opRoot.getFirstOperand(), variables, values);
            }
            
            if(opRoot.getSecondOperand()!=null){
                setVariables(opRoot.getSecondOperand(), variables, values);
            }
        }
    }

    @Override
    public String[] getVariableNames() {
        
        String[] varNames = new String[variableNames.size()];
        
        int index=0;
        for(String var : variableNames){
            varNames[index] = var;
            index++;
        }
        
        return varNames;
    }
}
