package br.unicentro.sistema_concessionaria.strategy;

public class DescontoAVistaStrategy implements CalculoVendaStrategy {
    @Override
    public double calcularValorFinal(double precoBase) {
        return precoBase * 0.95; // 5% de desconto
    }
}