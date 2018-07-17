package br.ce.wcaquino.matchers;


import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataLocacaoMatcher extends TypeSafeMatcher<Date> {

    private Integer qdtDias;

    public DataLocacaoMatcher(Integer qtDias) {
        this.qdtDias = qtDias;
    }



    @Override
    public void describeTo(Description description) {
        Date dataEsperada = DataUtils.obterDataComDiferencaDias(qdtDias);
        DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
        description.appendText(format.format(dataEsperada));
    }

    @Override
    protected boolean matchesSafely(Date date) {
        return DataUtils.isMesmaData(date,DataUtils.obterDataComDiferencaDias(qdtDias));
    }


}
