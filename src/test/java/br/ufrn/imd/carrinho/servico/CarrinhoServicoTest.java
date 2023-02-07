package br.ufrn.imd.carrinho.servico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.provider.CsvSource;

import br.ufrn.imd.carrinho.dominio.Endereco;
import br.ufrn.imd.carrinho.dominio.Item;
import br.ufrn.imd.carrinho.dominio.ItemTipo;
import br.ufrn.imd.carrinho.dominio.Pedido;
import br.ufrn.imd.carrinho.dominio.Usuario;

public class CarrinhoServicoTest {

	
	public CarrinhoServico service;
	
	@Test
	public void test() {
		Assertions.fail("Teste ainda não foi implementado.");
	}
	
	@Test
	@CsvSource( value = {
			"5.20, 0",
			"10.00, 0",
			"10.01, 5,005",
			"40.00, 20",
			"40.01, 30.0075",
			"100.00, 75.00",
			"100.01, 100.01",
			"200.00, 200,00",
	})
	void testCalcularFrete(Double peso, Double freteEsperado){
		
		Item item = new Item("Item 1", "Descrição Item 1", 50.20, peso, ItemTipo.COZINHA);
		Usuario usuario = new Usuario("login", "email", "usuario", new Endereco(null, null, null, null, null, null));
		
		List<Item> lista = new ArrayList();
		lista.add(item);
		
		Pedido pedidoFinalizado = service.finalizar(usuario, lista, LocalDate.now());
		
		Assertions.assertEquals(freteEsperado, pedidoFinalizado.getPrecoFrete());
		
	}
	
	@Test
	void testCalcularDescontoMaisDeDoisItensIguais(Double valorEsperado) {
		Item item = new Item("Item 1", "Descrição Item 1", 50.20, 50.2, ItemTipo.COZINHA);
		Usuario usuario = new Usuario("login", "email", "usuario", new Endereco(null, null, null, null, null, null));
		
		List<Item> lista = new ArrayList();
		lista.add(item);
		
		Pedido pedidoFinalizado = service.finalizar(usuario, lista, LocalDate.now());
		
		Assertions.assertEquals(valorEsperado, pedidoFinalizado.getPrecoFrete());
	}
}
