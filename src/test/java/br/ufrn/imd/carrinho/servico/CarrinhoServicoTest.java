package br.ufrn.imd.carrinho.servico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import br.ufrn.imd.carrinho.dominio.Endereco;
import br.ufrn.imd.carrinho.dominio.Estado;
import br.ufrn.imd.carrinho.dominio.Item;
import br.ufrn.imd.carrinho.dominio.ItemTipo;
import br.ufrn.imd.carrinho.dominio.Pedido;
import br.ufrn.imd.carrinho.dominio.Regiao;
import br.ufrn.imd.carrinho.dominio.Usuario;

public class CarrinhoServicoTest {

	
	public CarrinhoServico service;
	public Usuario usuario;
	
	@BeforeEach
	void setUp(){
		service = new CarrinhoServico();
		
		usuario = new Usuario("login", "email", "usuario", new Endereco("Rua", 13, "59123-59", "Bairro", "Cidade", Estado.RN));
		
	}
	
	@ParameterizedTest
	@CsvSource( value = {
			"5.20, 0.0, MG", 				//Regiao SUDESTE
			"10.00, 0.0, PI", 				//Regiao NORDESTE
			"10.01, 5.00, ES", 			//Regiao SUDESTE
			"40.00, 20.0, RN", 				//Regiao NORDESTE
			"40.01, 30.00, RJ", 			//Regiao SUDESTE
			"100.00, 75.00, PB", 			//Regiao NORDESTE
			"100.01, 100.01, SP", 			//Regiao SUDESTE
			"200.00, 200.00, PE", 			//Regiao NORDESTE
			
			"5.20, 0.0, PR", 				//Regiao SUL
			"10.00, 0.0, TO", 				//Regiao NORTE
			"10.01, 5.50, MT", 			//Regiao CENTROOESTE
			"40.00, 22.0, SC", 				//Regiao SUL
			"40.01, 33.00, AC", 			//Regiao NORTE
			"100.00, 82.50, GO", 			//Regiao CENTROOESTE
			"100.01, 110.01, RS", 			//Regiao SUL
			"200.00, 220.00, RR", 			//Regiao NORTE
			
	})
	void testCalcularFrete(Double peso, Double freteEsperado, Estado estado){
		
		usuario.getEndereco().setEstado(estado);
		
		Item item = new Item("Item 1", "Descrição Item 1", 50.20, peso, ItemTipo.COZINHA);		
		
		List<Item> lista = new ArrayList<Item>();
		lista.add(item);
		
		Pedido pedidoFinalizado = service.finalizar(usuario, lista, LocalDate.now());
		
		Assertions.assertEquals(freteEsperado, pedidoFinalizado.getPrecoFrete());
		
	}
	
	@ParameterizedTest
	@CsvSource( value = {
			"100.00, COZINHA, 100.00, COZINHA, 190.00", //Desconto 5%
			"250.00, COZINHA, 250.00, COZINHA, 475.00",	//Desconto 5%
			"300.00, COZINHA, 300.00, COZINHA, 540.00",	//Desconto 10%
			"500.00, COZINHA, 500.00, COZINHA, 900.00",	//Desconto 10%
			"500.01, COZINHA, 500.00, COZINHA, 800.00",//Desconto 20%
			"100.00, COZINHA, 100.00, CASA, 200",		//Sem Desconto
	})
	void testFinalizarPedidoDoisItensIguais(Double precoItem1, ItemTipo tipo1, Double precoItem2, ItemTipo tipo2, Double valorEsperado) {
		Item item1 = new Item("Item 1", "Descrição Item 1", precoItem1, 50.20, tipo1);
		Item item2 = new Item("Item 2", "Descrição Item 2", precoItem2, 50.20, tipo2);
		
		List<Item> lista = new ArrayList<Item>();
		lista.add(item1);
		lista.add(item2);
		
		Pedido pedidoFinalizado = service.finalizar(usuario, lista, LocalDate.now());
		
		Assertions.assertEquals(valorEsperado, pedidoFinalizado.getPrecoItens());
	}
	
	@ParameterizedTest
	@CsvSource( value = {
			"100.00, 10.0, 100.00", 		//Desconto 0% Frete 0
			"250.00, 40.0, 270.00",			//Desconto 0% Frete 0,50*kg
			"300.00, 100.0, 375.00",		//Desconto 0% Frete 0,75*kg
			"500.00, 100.01, 600.01",		//Desconto 0% Frete 1*kg
			"500.01, 40.01, 480.01",		//Desconto 10% Frete 0,75*kg
			"1000.00, 10.1, 905.05",		//Desconto 10% Frete 0,50*kg
			"1000.01, 150.0, 950.00",		//Desconto 20% Frete 1*kg
	})
	void testFinalizarPedidoUmItem(Double precoItem1, Double pesoItem1, Double valorTotalEsperado) {
		Item item1 = new Item("Item 1", "Descrição Item 1", precoItem1, pesoItem1, ItemTipo.COZINHA);
		
		List<Item> lista = new ArrayList<Item>();
		lista.add(item1);
		
		Pedido pedidoFinalizado = service.finalizar(usuario, lista, LocalDate.now());
		
		Assertions.assertEquals(valorTotalEsperado, pedidoFinalizado.getPrecoTotal());
	}

}
