--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4 (Debian 16.4-1.pgdg120+2)
-- Dumped by pg_dump version 16.4

-- Started on 2024-12-13 03:18:22 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3400 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 16420)
-- Name: administrador; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.administrador (
    id integer NOT NULL,
    apellido character varying(100),
    contrasena character varying(100),
    date_created timestamp(6) with time zone NOT NULL,
    id_registro character varying(20),
    last_updated timestamp(6) with time zone NOT NULL,
    nombre character varying(100)
);


ALTER TABLE public.administrador OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16425)
-- Name: aula; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.aula (
    tipo_aula integer NOT NULL,
    numero integer NOT NULL,
    capacidad integer,
    date_created timestamp(6) with time zone NOT NULL,
    last_updated timestamp(6) with time zone NOT NULL,
    nombre character varying(100),
    piso integer,
    tiene_aire_acondicionado boolean,
    tipo_pizarron smallint,
    cantidadpcs integer,
    tiene_canon boolean,
    tiene_computadora boolean,
    tiene_televisor boolean,
    tiene_ventiladores boolean,
    CONSTRAINT aula_tipo_pizarron_check CHECK (((tipo_pizarron >= 0) AND (tipo_pizarron <= 1)))
);


ALTER TABLE public.aula OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16389)
-- Name: bedel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bedel (
    id integer NOT NULL,
    apellido character varying(100),
    contrasena character varying(100),
    date_created timestamp(6) with time zone NOT NULL,
    id_registro character varying(20),
    last_updated timestamp(6) with time zone NOT NULL,
    nombre character varying(100),
    eliminado boolean,
    tipo_turno smallint,
    CONSTRAINT bedel_tipo_turno_check CHECK (((tipo_turno >= 0) AND (tipo_turno <= 2)))
);


ALTER TABLE public.bedel OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16395)
-- Name: dia_reservado; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dia_reservado (
    id integer NOT NULL,
    date_created timestamp(6) with time zone NOT NULL,
    duracion integer NOT NULL,
    fecha_reserva date NOT NULL,
    hora_inicio time(6) without time zone NOT NULL,
    last_updated timestamp(6) with time zone NOT NULL,
    id_aula integer,
    id_reserva integer
);


ALTER TABLE public.dia_reservado OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16400)
-- Name: periodo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.periodo (
    id integer NOT NULL,
    date_created timestamp(6) with time zone NOT NULL,
    fecha_fin date,
    fecha_inicio date,
    last_updated timestamp(6) with time zone NOT NULL,
    tipo_periodo smallint,
    CONSTRAINT periodo_tipo_periodo_check CHECK (((tipo_periodo >= 0) AND (tipo_periodo <= 2)))
);


ALTER TABLE public.periodo OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16431)
-- Name: primary_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.primary_sequence
    START WITH 10000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.primary_sequence OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16406)
-- Name: reserva_esporadica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reserva_esporadica (
    id integer NOT NULL,
    apellido_docente character varying(100),
    cant_alumnos integer,
    correo_docente character varying(100),
    date_created timestamp(6) with time zone NOT NULL,
    id_catedra integer,
    id_docente integer,
    last_updated timestamp(6) with time zone NOT NULL,
    nombre_catedra character varying(100),
    nombre_docente character varying(100),
    tipo_aula smallint,
    id_bedel integer,
    CONSTRAINT reserva_esporadica_tipo_aula_check CHECK (((tipo_aula >= 0) AND (tipo_aula <= 2)))
);


ALTER TABLE public.reserva_esporadica OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16412)
-- Name: reserva_periodica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reserva_periodica (
    id integer NOT NULL,
    apellido_docente character varying(100),
    cant_alumnos integer,
    correo_docente character varying(100),
    date_created timestamp(6) with time zone NOT NULL,
    id_catedra integer,
    id_docente integer,
    last_updated timestamp(6) with time zone NOT NULL,
    nombre_catedra character varying(100),
    nombre_docente character varying(100),
    tipo_aula smallint,
    id_bedel integer,
    id_periodo integer,
    CONSTRAINT reserva_periodica_tipo_aula_check CHECK (((tipo_aula >= 0) AND (tipo_aula <= 2)))
);


ALTER TABLE public.reserva_periodica OWNER TO postgres;

--
-- TOC entry 3245 (class 2606 OID 16424)
-- Name: administrador administrador_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrador
    ADD CONSTRAINT administrador_pkey PRIMARY KEY (id);


--
-- TOC entry 3247 (class 2606 OID 16430)
-- Name: aula aula_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aula
    ADD CONSTRAINT aula_pkey PRIMARY KEY (numero);


--
-- TOC entry 3233 (class 2606 OID 16394)
-- Name: bedel bedel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bedel
    ADD CONSTRAINT bedel_pkey PRIMARY KEY (id);


--
-- TOC entry 3237 (class 2606 OID 16399)
-- Name: dia_reservado dia_reservado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dia_reservado
    ADD CONSTRAINT dia_reservado_pkey PRIMARY KEY (id);


--
-- TOC entry 3239 (class 2606 OID 16405)
-- Name: periodo periodo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.periodo
    ADD CONSTRAINT periodo_pkey PRIMARY KEY (id);


--
-- TOC entry 3241 (class 2606 OID 16411)
-- Name: reserva_esporadica reserva_esporadica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva_esporadica
    ADD CONSTRAINT reserva_esporadica_pkey PRIMARY KEY (id);


--
-- TOC entry 3243 (class 2606 OID 16417)
-- Name: reserva_periodica reserva_periodica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva_periodica
    ADD CONSTRAINT reserva_periodica_pkey PRIMARY KEY (id);


--
-- TOC entry 3235 (class 2606 OID 16419)
-- Name: bedel ukvo6k3ooiuuxdwg1193oaamv2; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bedel
    ADD CONSTRAINT ukvo6k3ooiuuxdwg1193oaamv2 UNIQUE (id_registro);


--
-- TOC entry 3250 (class 2606 OID 16442)
-- Name: reserva_periodica fk589ljgm8tmhmfm4d613q9sl7n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva_periodica
    ADD CONSTRAINT fk589ljgm8tmhmfm4d613q9sl7n FOREIGN KEY (id_periodo) REFERENCES public.periodo(id);


--
-- TOC entry 3249 (class 2606 OID 16437)
-- Name: reserva_esporadica fkcf9avup6m76rmk31njxq10ufb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva_esporadica
    ADD CONSTRAINT fkcf9avup6m76rmk31njxq10ufb FOREIGN KEY (id_bedel) REFERENCES public.bedel(id);


--
-- TOC entry 3251 (class 2606 OID 16447)
-- Name: reserva_periodica fkcf9avup6m76rmk31njxq10ufb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva_periodica
    ADD CONSTRAINT fkcf9avup6m76rmk31njxq10ufb FOREIGN KEY (id_bedel) REFERENCES public.bedel(id);


--
-- TOC entry 3248 (class 2606 OID 16432)
-- Name: dia_reservado fkmwmscceocso88xtkdf7g4mrcc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dia_reservado
    ADD CONSTRAINT fkmwmscceocso88xtkdf7g4mrcc FOREIGN KEY (id_aula) REFERENCES public.aula(numero);


-- Completed on 2024-12-13 03:18:22 UTC

--
-- PostgreSQL database dump complete
--

