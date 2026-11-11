package br.unicentro.sistema_concessionaria.model;

public class Carro {
    private String placa;
    private String modelo;
    private String marca;
    private int ano;
    private double preco;
    private String imagem;

    public Carro() {
    }

    public Carro(String placa, String modelo, String marca, int ano, double preco, String imagem) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.preco = preco;
        this.imagem = imagem;
    }

    private Carro(CarroBuilder builder) {
        this.placa = builder.placa;
        this.modelo = builder.modelo;
        this.marca = builder.marca;
        this.ano = builder.ano;
        this.preco = builder.preco;
        this.imagem = builder.imagem;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }


    // parte do builder
    public static class CarroBuilder {
        private String placa;
        private String modelo;
        private String marca;
        private int ano;
        private double preco;
        private String imagem;

        public CarroBuilder comPlaca(String placa) {
            this.placa = placa;
            return this;
        }

        public CarroBuilder comModelo(String modelo) {
            this.modelo = modelo;
            return this;
        }

        public CarroBuilder comMarca(String marca) {
            this.marca = marca;
            return this;
        }

        public CarroBuilder comAno(int ano) {
            this.ano = ano;
            return this;
        }

        public CarroBuilder comPreco(double preco) {
            this.preco = preco;
            return this;
        }

        public CarroBuilder comImagem(String imagem) {
            this.imagem = imagem;
            return this;
        }

        public Carro build() {
            return new Carro(this);
        }
    }
}