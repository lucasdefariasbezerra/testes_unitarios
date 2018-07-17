package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.internal.ReflectiveTypeFinder;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {

    private Integer diaSemanna;

    public DiaSemanaMatcher(Integer diaSemana) {
        this.diaSemanna = diaSemana;
    }

    public DiaSemanaMatcher(Class<?> expectedType) {
        super(expectedType);
    }

    public DiaSemanaMatcher(ReflectiveTypeFinder typeFinder) {
        super(typeFinder);
    }

    @Override
    public void describeTo(Description desc) {
        Calendar data = Calendar.getInstance();
        data.set(Calendar.DAY_OF_WEEK,diaSemanna);
        String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,new Locale("pt","BR"));
        desc.appendText(dataExtenso);
    }

    @Override
    protected boolean matchesSafely(Date date) {
        return DataUtils.verificarDiaSemana(date,diaSemanna);
    }
}
