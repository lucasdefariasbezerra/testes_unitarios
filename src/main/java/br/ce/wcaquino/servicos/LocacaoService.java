package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Test;

public class LocacaoService {

    LocacaoDAO dao;
    private SPCService spcService;
    private EmailService emailService;

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
        if(usuario == null) {
            throw new LocadoraException("Usuario Vazio");
        }

        if(filmes == null || filmes.isEmpty()) {
            throw new LocadoraException("Filme Vazio");
        }

        for(Filme filme: filmes) {
            if(filme.getEstoque() == 0) {
                throw new FilmeSemEstoqueException();
            }
        }

        boolean negativado;
        try {
            negativado = spcService.possuiNegativacao(usuario);
        } catch (Exception e) {
            throw new LocadoraException("Problemas com SPC, tente novamente");
        }


            if(negativado){
                throw new LocadoraException("Usuario negativado");
            }


        Locacao locacao = new Locacao();
        locacao.setFilme(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(obterData());
        locacao.setValor(calcularValorLocacao(filmes));
        //Entrega no dia seguinte
        Date dataEntrega = obterData();
        dataEntrega = adicionarDias(dataEntrega, 1);
        if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
            dataEntrega = adicionarDias(dataEntrega, 1);
        }
        locacao.setDataRetorno(dataEntrega);

        //Salvando a locacao...
        dao.salvar(locacao);

        return locacao;
    }


    public void notificarAtrasos(){
        List<Locacao> locacaoes = dao.obterLocacoesPendentes();
        for (Locacao l : locacaoes){
            if(l.getDataRetorno().before(new Date())){
                emailService.notificarAtraso(l.getUsuario());
            }
        }
    }

    private double calcularValorLocacao(List<Filme> filmes){
        //System.out.println("valor calculado");
        double total = 0d;
        for(int i = 0; i < filmes.size(); i++) {
            Filme filme = filmes.get(i);
            double valorFilme = filme.getPrecoLocacao();
            switch (i) {
                case 2:{
                    valorFilme = valorFilme * 0.75;
                    total += valorFilme;
                    break;
                }
                case 3:{
                    valorFilme = valorFilme * 0.5;
                    total += valorFilme;
                    break;
                }
                case 4:{
                    valorFilme = valorFilme * 0.25;
                    total += valorFilme;
                    break;
                }
                case 5:{
                    valorFilme = 0d;
                    total += valorFilme;
                    break;
                }
                default:{
                    valorFilme = filme.getPrecoLocacao();
                    total += valorFilme;
                    break;
                }
            }

        }
        return total;
    }


    public void prorrogarLocacao(Locacao locacao,int dias){
        Locacao novaLocacao = new Locacao();
        novaLocacao.setUsuario(locacao.getUsuario());
        novaLocacao.setFilme(locacao.getFilme());
        novaLocacao.setDataLocacao(new Date());
        novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
        novaLocacao.setValor(locacao.getValor() * dias);
        dao.salvar(novaLocacao);
    }

    protected Date obterData(){
        return new Date();
    }
}