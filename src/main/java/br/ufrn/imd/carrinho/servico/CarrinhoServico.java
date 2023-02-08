package br.ufrn.imd.carrinho.servico;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import br.ufrn.imd.carrinho.dominio.Item;
import br.ufrn.imd.carrinho.dominio.Pedido;
import br.ufrn.imd.carrinho.dominio.Usuario;

public class CarrinhoServico {

	public Pedido finalizar(Usuario usuario, List<Item> itens, LocalDate checkoutDate) {
		
		
		double valorProdutos = 0;
		double pesoPedido = 0;
		double valorFrete = 0;
		double valorTotal = 0;
		int count = 0;
		double valorItem = 0;
		
		for (Item item : itens) {
			valorItem = item.getPreco();
			
			valorProdutos += valorItem;
		}
		
		if(valorProdutos > 1000.0) {
			valorProdutos -= valorProdutos*0.20;
		}
		else if(valorProdutos > 500.0) {
			valorProdutos -= valorProdutos*0.10;
		}
		else {
			valorProdutos = 0;
			for (Item item : itens) {
				valorItem = item.getPreco();
				int cnt = 0;
				for (Item it : itens) {
					if(item.getTipo() == it.getTipo()) {
						cnt++;
					}
					if(cnt > 1) {
						valorItem -= valorItem*0.05;
						break;
					}
				}
				valorProdutos += valorItem;
			}
			
		}
		
		for (Item item : itens) {
			pesoPedido += item.getPeso();
		}
		
		
		if(40 < pesoPedido && pesoPedido <= 100.0) {
			valorFrete = pesoPedido * 0.75;
		}
		else if(10 < pesoPedido && pesoPedido <= 40.0) {
			valorFrete = pesoPedido * 0.50;
		}
		else if(pesoPedido > 100.0) {
			valorFrete = pesoPedido;
		}
		
		
		
		switch (usuario.getEndereco().getRegiao()) {
			case NORTE:
			case SUL:
			case CENTROOESTE:
				valorFrete += valorFrete*0.10;
				break;
			default:
				break;
		}
		

		valorTotal = valorProdutos + valorFrete;
		
		return new Pedido(usuario, 
						itens, 
						Math.floor(valorTotal * 100) / 100, 
						Math.floor(valorProdutos * 100) / 100, 
						Math.floor(valorFrete * 100) / 100
						, checkoutDate);
	}
}
