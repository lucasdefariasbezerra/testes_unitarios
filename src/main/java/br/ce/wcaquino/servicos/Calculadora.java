package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.ZeroDivisionException;

public class Calculadora {


    public int somar(int a, int b) {
        System.out.println("execucao da soma");
        return a + b;
    }

    public int subtrair(int a, int b) {
        return  a - b;
    }

    public int dividir(int a, int b) throws ZeroDivisionException {
        if(b == 0 ){
            throw new ZeroDivisionException();
        }
        return  a/b;
    }

    public int dividir(String a,String b){
        return Integer.valueOf(a)/Integer.valueOf(b);
    }

    public void imprime(){
        System.out.println("Passei aqui");
    }
}
