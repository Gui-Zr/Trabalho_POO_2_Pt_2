package br.unicentro.sistema_concessionaria.strategy;

public class VendaNormalStrategy implements CalculoVendaStrategy {
    @Override
    public double calcularValorFinal(double precoBase) {
        return precoBase;
    }
}