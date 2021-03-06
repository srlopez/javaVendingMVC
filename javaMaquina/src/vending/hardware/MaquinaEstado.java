package vending.hardware;

import vending.repositorio.IRepositorio;
import vending.hardware.subsistemas.pagos.ControladorDePagos;
import vending.hardware.subsistemas.productos.Dispensador;
import vending.hardware.subsistemas.seguridad.ISeguridad;
/*
  Hemos extenido la funcionalidad de la Maquina Original
  añadiendole "Estado" que se establece cuando se instancia
  y se guarda cuando se "Apaga".
*/
public class MaquinaEstado extends Maquina {

    private IRepositorio repositorio;

    public MaquinaEstado(
        String empresa, 
        Dispensador dispensador, 
        ISeguridad seguridad, 
        ControladorDePagos ctrl, 
        IRepositorio repositorio) {
        super(empresa,dispensador,seguridad,ctrl);
        this.repositorio = repositorio;
        
        repositorio.inicializar(empresa);
        repositorio.cargarProductos(this.dispensador);
        repositorio.cargarCaja(this.ctrl);
    }

    @Override
    public void apagar(){
        repositorio.guardarProductos(dispensador);
        repositorio.guardarCaja(ctrl);
        super.apagar();
    }

}
