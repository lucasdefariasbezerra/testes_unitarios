package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.ZeroDivisionException;
import br.ce.wcaquino.runners.ParallelRun;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.JUnit4;


public class CalculadoraTest {

    private Calculadora calculadora;

    @Before
    public void setup(){
       calculadora = new Calculadora();
       System.out.println("Iniciando... ");
    }

    @After
    public void tearDown(){
        System.out.println("Finalizando... ");
    }


    @Test
    public void somarValores(){
        //cenario
        int a = 5;
        int b = 3;

        //acao
        int resultado = calculadora.somar(a,b);

        //verificacao
        Assert.assertEquals(8,resultado);
    }

    @Test
    public void subtrairValores(){
        //cenario
        int a = 5;
        int b = 3;

        //acao
        int resultado = calculadora.subtrair(a,b);

        //verificacao
        Assert.assertEquals(2,resultado);
    }

    @Test
    public void dividirValores() throws ZeroDivisionException {
        //cenario
        int a = 5;
        int b = 3;

        //acao
        int resultado = calculadora.dividir(a,b);

        //verificacao
        Assert.assertEquals(1,resultado);
    }

    @Test(expected = ZeroDivisionException.class)
    public void dividirPorZero() throws ZeroDivisionException {
        //cenario
        int a = 10;
        int b = 0;

        //acao
        int resultado = calculadora.dividir(a,0);
    }

    @Test
    public void deveDividr(){
        String a = "6";
        String b = "3";
        int resultado = calculadora.dividir(a,b);
        Assert.assertEquals(2,resultado);
    }
}
