package br.ce.wcaquino.matchers;


import java.util.Calendar;


public class MatchersPropios {

    public static DiaSemanaMatcher caiEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiNumaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }


    public static DataLocacaoMatcher ehHoje(){
        return new DataLocacaoMatcher(0);
    }

    public static DataLocacaoMatcher ehHojeComDiferencaDeDias(Integer qtdDias){
        return new DataLocacaoMatcher(qtdDias);
    }


}
