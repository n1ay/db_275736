package pl.edu.agh.tkk17.sample;

import java.util.Iterator;

public class Parser
{
    private Iterator<Token> tokens;
    private Token ctoken;

    public Parser(Iterable<Token> tokens)
    {
        this.tokens = tokens.iterator();
        this.forward();
    }

    private void forward()
    {
        this.ctoken = this.tokens.next();
    }

    private String value()
    {
        return this.ctoken.getValue();
    }

    private boolean check(TokenType type)
    {
        return this.ctoken.getType() == type;
    }

    private void expect(TokenType type)
    {
        if (!this.check(type)) {
            throw new UnexpectedTokenException(this.ctoken, type);
        }
    }

    private Node parseNumber()
    {
        this.expect(TokenType.NUM);
        String value = this.value();
        this.forward();
        while(check(TokenType.NUM)) {
            value += this.value();
            this.forward();
        }

        return new NodeNumber(value);
    }

    private Node parseTerm()
    {
        if(check(TokenType.LBR))
            return parseBrackets();
        else {
            Node left = this.parseNumber();
            if (this.check(TokenType.MUL)) {
                this.forward();
                Node right = this.parseTerm();
                return new NodeMul(left, right);
            } else if (this.check(TokenType.DIV)) {
                this.forward();
                Node right = this.parseTerm();
                return new NodeDiv(left, right);
            } else {
                return left;
            }
        }
    }

    private Node parseExpression()
    {
        if(check(TokenType.LBR))
            return parseBrackets();
        else {
            Node left = this.parseTerm();
            if (this.check(TokenType.ADD)) {
                this.forward();
                Node right = this.parseExpression();
                return new NodeAdd(left, right);
            } else if (this.check(TokenType.SUB)) {
                this.forward();
                Node right = this.parseExpression();
                return new NodeSub(left, right);
            } else {
                return left;
            }
        }
    }

    private Node parseBrackets() {
        forward();
        Node expression = parseExpression();
        if(check(TokenType.RBR)) {
            forward();
            Node nodeBr = new NodeBr(expression);
            if(check(TokenType.ADD)) {
                forward();
                Node right = parseExpression();
                return new NodeAdd(nodeBr, right);
            }
            else if(check(TokenType.MUL)) {
                forward();
                Node right = parseExpression();
                return new NodeMul(nodeBr, right);
            }
            return nodeBr;
        } else
            throw new UnexpectedTokenException(this.ctoken, ctoken.getType());
    }

    private Node parseProgram()
    {
        Node root = this.parseExpression();
        this.expect(TokenType.END);
        return root;
    }

    public static Node parse(Iterable<Token> tokens)
    {
        Parser parser = new Parser(tokens);
        Node root = parser.parseProgram();
        return root;
    }
}
