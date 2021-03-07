package vending.persistencia;

import java.io.File;
import java.sql.*;

import vending.hardware.subsistemas.pagos.ControladorDePagos;
import vending.hardware.subsistemas.pagos.Efectivo;
import vending.hardware.subsistemas.productos.Dispensador;
import vending.producto.Golosina;
import vending.producto.Producto;
import vending.producto.ProductoFactory;

public class RepositorioLite implements IRepositorio {

   String dbname = "data/vending.db";
   String empresa;

   // https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.34.0/sqlite-jdbc-3.34.0.jar
   public static void main(String[] args) throws Exception {
      RepositorioLite lite = new RepositorioLite();
      lite.inicializar("prueba");
      Golosina[][] m = {
            { new Golosina("KitKat", 1.10), new Golosina("Chicles de fresa", .80), new Golosina("Lacasitos", 1.50),
                  new Golosina("Palotes", .90f) },
            { new Golosina("Kinder Bueno", 1.80), new Golosina("Bolsa Haribo", 1.0), new Golosina("Chetoos", 1.20),
                  new Golosina("Twix", 1.0f) },
            { new Golosina("Maiz", 0.7), new Golosina("M&M’S", 1.30), new Golosina("Papa Delta", 1.20),
                  new Golosina("Chicles de menta", .80f) },
            { new Golosina("Gusanitos", 1.50), new Golosina("Crunch", 1.10), new Golosina("Milkybar", 1.10),
                  new Golosina("Patatas fritas", 1.10f) }, };

      Dispensador dis = new Dispensador(m);
      lite.guardarProductos(dis);
      lite.cargarProductos(dis);

      ControladorDePagos c = new ControladorDePagos(new Efectivo(new int[]{3,3,3,3,3}));
      lite.guardarCaja(c);
      lite.cargarCaja(c);
   }

   @Override
   public void inicializar(String id) {
      this.empresa = id;
      // String dir = System.getProperty("user.dir");
      File tempFile = new File(dbname);
      if (!tempFile.exists()) {
         // CREAMOS LA BD Y LAS TABLAS
         try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE DISPENSADOR (" + " EMP      TEXT NOT NULL, " + " FILA     INT  NOT NULL, "
                  + " COL      INT  NOT NULL, " + " TIPO     TEXT, " + " PRODUCTO TEXT, " + " PRECIO   REAL, "
                  + " ZERO     BOOLEAN CHECK (ZERO IN (0,1)), " + " CANTIDAD INT,  " + " PRIMARY KEY (EMP, FILA, COL) "
                  + " )";
            // System.out.println(sql);
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE GENERAL (" + " EMP      TEXT NOT NULL, " + " FILAS    INT, " + " COLUMNAS INT, "
                  + " CAJON    REAL, " + " M20      INT, " + " M10      INT, " + " M05      INT, " + " M02      INT, "
                  + " M01      INT, " + " PRIMARY KEY (EMP) " + ")";
            // System.out.println(sql);
            stmt.executeUpdate(sql);

            sql = "INSERT INTO GENERAL (EMP) VALUES ('" + empresa + "');";
            // System.out.println(sql);
            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
         } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
         }
      } else {
         // NADA
         // Se invocan los métodos cargar/guardar desde donde corresponda
      }

   }

   @Override
   public void cargarProductos(Dispensador dispensador) {
      try {
         Class.forName("org.sqlite.JDBC");
         Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
         Statement stmt = conn.createStatement();

         String sql = "SELECT * FROM GENERAL WHERE EMP=='" + empresa + "'";
         ResultSet rs = stmt.executeQuery(sql);
         rs.next();
         int lx = rs.getInt("FILAS");
         int ly = rs.getInt("COLUMNAS");
         dispensador.matriz = new Producto[lx][ly];
         dispensador.cantidad = new int[lx][ly];
         rs.close();

         sql = "SELECT * FROM DISPENSADOR WHERE EMP = '" + empresa + "'";
         rs = stmt.executeQuery(sql);
         while (rs.next()) {
            int f = rs.getInt("FILA");
            int c = rs.getInt("COL");
            dispensador.matriz[f][c] = ProductoFactory.getProducto(rs.getString("TIPO"), rs.getString("PRODUCTO"),
                  Math.round(rs.getDouble("PRECIO")), rs.getInt("ZERO") == 0 ? false : true);
            dispensador.cantidad[f][c] = rs.getInt("CANTIDAD");
         }
         rs.close();
         stmt.close();
      } catch (Exception e) {
         System.out.println(e.toString());
      }

   }

   @Override
   public void guardarProductos(Dispensador dispensador) {
      try {
         Class.forName("org.sqlite.JDBC");
         Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
         Statement stmt = conn.createStatement();

         String sql = "UPDATE GENERAL SET" + " FILAS = " + dispensador.matriz.length + " , COLUMNAS = "
               + dispensador.matriz[0].length + " WHERE EMP = '" + empresa + "';";
         // System.out.println(sql);
         stmt.executeUpdate(sql);
         stmt.close();

         sql = "DELETE FROM DISPENSADOR WHERE EMP ='" + empresa + "';";
         // System.out.println(sql);
         stmt.executeUpdate(sql);

         for (int f = 0; f < dispensador.matriz.length; f++) {
            for (int c = 0; c < dispensador.matriz[0].length; c++) {
               Producto prod = dispensador.matriz[f][c];
               String tipo = prod.getClass().getSimpleName();
               int cant = dispensador.cantidad[f][c];
               sql = "INSERT INTO DISPENSADOR " + "(EMP,FILA,COL,TIPO,PRODUCTO,PRECIO,ZERO,CANTIDAD) "
                     + "VALUES (?,?,?,?,?,?,?,?)";
               PreparedStatement pstmt = conn.prepareStatement(sql);
               pstmt.setString(1, empresa);
               pstmt.setInt(2, f);
               pstmt.setInt(3, c);
               pstmt.setString(4, tipo);
               pstmt.setString(5, prod.nombre);
               pstmt.setDouble(6, prod.precio);
               pstmt.setInt(7, prod.esCeroCal() ? 1 : 0);
               pstmt.setInt(8, cant);
               pstmt.executeUpdate();
               pstmt.close();
            }
         }
         conn.close();
      } catch (Exception e) {
         System.out.println(e.toString());
      }
   }

   @Override
   public void cargarCaja(ControladorDePagos ctrl) {
      try {
         Class.forName("org.sqlite.JDBC");
         Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
         load(ctrl, conn, empresa);
      } catch (Exception e) {
         System.out.println(e.toString());
      }
   }

   @Override
   public void guardarCaja(ControladorDePagos ctrl) {
      try {
         Class.forName("org.sqlite.JDBC");
         Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
         save(ctrl, conn, empresa);
      } catch (Exception e) {
         System.out.println(e.toString());
      }
   }

   private static void save(ControladorDePagos ctrl, Connection conn, String empresa) {
      try {
          Statement stmt = conn.createStatement();
          String sql = "UPDATE GENERAL SET"
          + " CAJON = " +ctrl.cajon 
          + ", M20 = " + ctrl.caja.cantidad[0] 
          + ", M10 = " + ctrl.caja.cantidad[1]  
          + ", M05 = " + ctrl.caja.cantidad[2]  
          + ", M02 = " + ctrl.caja.cantidad[3] 
          + ", M01 = " + ctrl.caja.cantidad[4] 
          + " WHERE EMP = '" + empresa + "';";
       //System.out.println(sql);
       stmt.executeUpdate(sql);
       stmt.close();
      } catch (Exception e) {
          System.out.println(e.toString());
      }
  };

  private static void load(ControladorDePagos ctrl, Connection conn, String empresa) {
      try {
          Statement stmt = conn.createStatement();
          String sql = "SELECT * FROM GENERAL WHERE EMP ='"+empresa+"'";
          ResultSet rs = stmt.executeQuery(sql);
          rs.next();
          ctrl.cajon = rs.getDouble("CAJON");
          ctrl.caja.cantidad[0]= rs.getInt("M20");
          ctrl.caja.cantidad[1]= rs.getInt("M10");
          ctrl.caja.cantidad[2]= rs.getInt("M05");
          ctrl.caja.cantidad[3]= rs.getInt("M02");
          ctrl.caja.cantidad[4]= rs.getInt("M01");
          rs.close();
          stmt.close();
      } catch (Exception e) {
          System.out.println(e.toString());
      }
  };
}
