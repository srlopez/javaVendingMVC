package repositorio;

import vending.hardware.subsistemas.Dispensador;
import vending.hardware.subsistemas.economico.ControladorDePagos;

public interface IRepositorio {

    public void inicializar(String id);

    void cargarProductos(Dispensador dispensador);

    void guardarProductos(Dispensador dispensador);

    void cargarCaja(ControladorDePagos ctrl);

    void guardarCaja(ControladorDePagos ctrl);

}
