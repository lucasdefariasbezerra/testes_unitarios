package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import org.mockito.*;


import java.lang.reflect.Method;
import java.util.*;

import static br.ce.wcaquino.matchers.MatchersPropios.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

public class LocacaoServiceTest {

    @InjectMocks @Spy
    private LocacaoService service;

    @Mock
    private SPCService spcService;

    @Mock
    private LocacaoDAO locacaoDAO;

    @Mock
    private EmailService emailService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        System.out.println("Iniciando2...");
    }

    @After
    public void finalizando(){
        System.out.println("Finalizando2...");
    }


    @Test
    public void testLocacao() throws Exception {
        // cenario
        //Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(),Calendar.SATURDAY));
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5d).agora());

        Mockito.doReturn(DataUtils.obterData(28,4,2017)).when(service).obterData();

        // acao
        Locacao locacao = service.alugarFilme(usuario,filmes);

        // verificacao
        // Fluent Interface
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(),DataUtils.obterData(28,4,2017)),is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(),DataUtils.obterData(29,4,2017)),is(true));

    }

    /*
     * ways to treat exception in unit test
     *
     * */

    // Elegant way
    @Test(expected = FilmeSemEstoqueException.class)
    public void testeLocacaoFilmeSemEstoque() throws Exception{
        // cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeSemEstoque().agora());

        // acao
        service.alugarFilme(usuario,filmes);
    }


    // Robust way
    @Test
    public void testeLocacaoUsuarioVazio() throws FilmeSemEstoqueException {
        // cenario
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        //acao
        try {
            service.alugarFilme(null,filmes);
            Assert.fail("Exception deveria ocorrer");
        } catch (LocadoraException e) {

            Assert.assertThat(e.getMessage(),is("Usuario Vazio"));
        }
    }

    // New Way
    @Test
    public void testeLocacaoFilmeVazio() throws FilmeSemEstoqueException,LocadoraException {
        // cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = new ArrayList<>();

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme Vazio");

        //acao
        service.alugarFilme(usuario,filmes);
    }

    // New Way
    @Test
    public void testeLocacaoFilmeNulo() throws FilmeSemEstoqueException,LocadoraException {
        // cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme Vazio");

        //acao
        service.alugarFilme(usuario,null);
    }


    @Test
    public void testeDesconto6filmes() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme1 = new Filme("Filme 1",2,4.0);
        Filme filme2 = new Filme("Filme 2",3,4.0);
        Filme filme3 = new Filme("Filme 3",2,4.0);
        Filme filme4 = new Filme("Filme 4",2,4.0);
        Filme filme5 = new Filme("Filme 5",1,4.0);
        Filme filme6 = new Filme("Filme 6",5,4.0);

        List<Filme> filmes = Arrays.asList(filme1,filme2,filme3,filme4,filme5,filme6);

        //acao
        //service.aplicarDesconto(filmes);
        Locacao locacao = service.alugarFilme(usuario,filmes);

        // verificacao
        error.checkThat(locacao.getValor(),is(14.0));

    }


    @Test
    public void testeDesconto3filmes() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme1 = new Filme("Filme 1",2,4.0);
        Filme filme2 = new Filme("Filme 2",3,4.0);
        Filme filme3 = new Filme("Filme 3",2,4.0);

        List<Filme> filmes = Arrays.asList(filme1,filme2,filme3);

        //acao
        //service.aplicarDesconto(filmes);
        Locacao locacao = service.alugarFilme(usuario,filmes);

        // verificacao
        error.checkThat(locacao.getValor(),is(11.0));

    }

    @Test
    public void testeDesconto4filmes() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme1 = new Filme("Filme 1",2,4.0);
        Filme filme2 = new Filme("Filme 2",3,4.0);
        Filme filme3 = new Filme("Filme 3",2,4.0);
        Filme filme4 = new Filme("Filme 4",2,4.0);

        List<Filme> filmes = Arrays.asList(filme1,filme2,filme3,filme4);

        //acao
        //service.aplicarDesconto(filmes);
        Locacao locacao = service.alugarFilme(usuario,filmes);

        // verificacao
        error.checkThat(locacao.getValor(),is(13.0));
    }


    @Test
    public void testeDesconto5filmes() throws FilmeSemEstoqueException, LocadoraException {
        // cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Filme filme1 = new Filme("Filme 1",2,4.0);
        Filme filme2 = new Filme("Filme 2",3,4.0);
        Filme filme3 = new Filme("Filme 3",2,4.0);
        Filme filme4 = new Filme("Filme 4",2,4.0);
        Filme filme5 = new Filme("Filme 5",2,4.0);

        List<Filme> filmes = Arrays.asList(filme1,filme2,filme3,filme4,filme5);

        //acao
        //service.aplicarDesconto(filmes);
        Locacao locacao = service.alugarFilme(usuario,filmes);

        // verificacao
        error.checkThat(locacao.getValor(),is(14.0));
    }


    @Test
    public void  testeDiaDevolucao() throws Exception {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        Mockito.doReturn(DataUtils.obterData(29,4,2017)).when(service).obterData();

        Locacao retorno = service.alugarFilme(usuario,filmes);

        assertThat(retorno.getDataRetorno(),caiNumaSegunda());
    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

        try {
            service.alugarFilme(usuario,filmes);
            //verificacao
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(),is("Usuario negativado"));
        }

        Mockito.verify(spcService).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas(){
        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("locacao em dia").agora();
        Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("outra locacao atrasada").agora();
        Locacao locacao1 = LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario).agora();
        Locacao locacao2 = LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario3).agora();
        Locacao locacao4 = LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario3).agora();
        Locacao locacao3 = LocacaoBuilder.umLocacao().comUsuario(usuario2).agora();


        List<Locacao> locacoes = Arrays.asList(locacao3,locacao1,locacao2,locacao4);
        when(locacaoDAO.obterLocacoesPendentes()).thenReturn(locacoes);

        // acao
        service.notificarAtrasos();

        // verificacao
        // verify in the email mock that 2 execution regarding the email sending will be executed for any Usuario instance
        Mockito.verify(emailService,Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
        Mockito.verify(emailService).notificarAtraso(usuario);
        Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
        Mockito.verify(emailService, never()).notificarAtraso(usuario2);
        Mockito.verifyNoMoreInteractions(emailService);

    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {
        //cenario
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Problemas com SPC, tente novamente"));

        // verificação
        exception.expect(LocadoraException.class);
        exception.expectMessage("Problemas com SPC, tente novamente");

        //acao
        service.alugarFilme(usuario,filmes);
    }

    @Test
    public void deveProrrogarUmaLocacao(){
        //cenario
        Locacao locacao = LocacaoBuilder.umLocacao().agora();

        //acao
        service.prorrogarLocacao(locacao,3);

        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(locacaoDAO).salvar(argCapt.capture());
        Locacao locacaoRetornada = argCapt.getValue();

        error.checkThat(locacaoRetornada.getValor(),is(12d));
        error.checkThat(locacaoRetornada.getDataLocacao(),ehHoje());
        error.checkThat(locacaoRetornada.getDataRetorno(),ehHojeComDiferencaDeDias(3));
    }



    @Test
    public void devecalcularValorLocacao() throws Exception {
        //cenario
        List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

        //acao
        Class<LocacaoService> clazz = LocacaoService.class;
        Method method = clazz.getDeclaredMethod("calcularValorLocacao",List.class);
        method.setAccessible(true);
        Double valor = (Double) method.invoke(service,filmes);

        //verificacao
        Assert.assertThat(valor, is(4d));
        
    }


}