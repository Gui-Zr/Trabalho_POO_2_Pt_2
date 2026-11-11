package br.unicentro.sistema_concessionaria.strategy;

public class VendaFinanciadaStrategy implements CalculoVendaStrategy {
    @Override
    public double calcularValorFinal(double precoBase) {
        return precoBase * 1.03; // 3% de acréscimo
    }
}