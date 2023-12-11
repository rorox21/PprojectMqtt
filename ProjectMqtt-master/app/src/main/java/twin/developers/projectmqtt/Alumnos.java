package twin.developers.projectmqtt;

public class Alumnos {
    private String idMatricula;
    private String nombre;
    private String edad;

    public Alumnos()
    {
        this.idMatricula="";
        this.nombre="";
        this.edad
                ="";
    }

    public Alumnos( String idMatricula, String nombre, String edad )
    {
        this.idMatricula=idMatricula;
        this.nombre=nombre;
        this.edad=edad;
    }

    public String getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(String idMatricula) {
        this.idMatricula = idMatricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return "Matricula [" +
                "idMatricula='" + idMatricula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", edad='" + edad + '\'' +
                ']';
    }
}
