package vending.persistencia;

import vending.hardware.subsistemas.pagos.ControladorDePagos;
import vending.hardware.subsistemas.productos.Dispensador;

public interface IRepositorio {

    public void inicializar(String id);

    void cargarProductos(Dispensador dispensador);

    void guardarProductos(Dispensador dispensador);

    void cargarCaja(ControladorDePagos ctrl);

    void guardarCaja(ControladorDePagos ctrl);

}
