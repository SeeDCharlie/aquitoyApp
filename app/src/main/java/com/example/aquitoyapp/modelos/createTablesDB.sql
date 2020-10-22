create table if not exists  sesiones(
    email             text,
    nombre_usuario    text,
    contraseña        text,
    activo            integer default 0
)WITHOUT ROWID;