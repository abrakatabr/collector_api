ALTER SYSTEM SET max_connections = 200;

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'Collector DB') THEN
        CREATE DATABASE "Collector DB";
    END IF;
END
$$;

CREATE USER "user" WITH ENCRYPTED PASSWORD 'password';
ALTER DATABASE "Collector DB" OWNER TO "user";

GRANT ALL PRIVILEGES ON DATABASE "Collector DB" TO "user";
GRANT ALL PRIVILEGES ON SCHEMA collector TO "user";

