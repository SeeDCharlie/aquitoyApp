create table if not exists  sesiones(
    id                integer primary key,
    email             text,
    nombre_usuario    text,
    contraseña        text,
    fecha_creacion    text,
    activo            integer default 0
)WITHOUT ROWID;