--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4 (Debian 16.4-1.pgdg120+2)
-- Dumped by pg_dump version 16.4

-- Started on 2024-12-13 03:07:17 UTC

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
-- TOC entry 3395 (class 0 OID 16420)
-- Dependencies: 220
-- Data for Name: administrador; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.administrador VALUES (1, 'Garcia', 'Admin@123', '2024-12-13 02:57:00.591781+00', 'Alberto', '2024-12-13 02:57:00.591781+00', 'Alberto');
INSERT INTO public.administrador VALUES (2, 'Martinez', 'Pass#2023', '2024-12-13 02:57:00.591781+00', 'Juana', '2024-12-13 02:57:00.591781+00', 'Juana');
INSERT INTO public.administrador VALUES (3, 'Lopez', 'Secure*456', '2024-12-13 02:57:00.591781+00', 'Mario', '2024-12-13 02:57:00.591781+00', 'Mario');
INSERT INTO public.administrador VALUES (4, 'Perez', 'Admin!456', '2024-12-13 02:57:00.591781+00', 'Lucas', '2024-12-13 02:57:00.591781+00', 'Lucas');
INSERT INTO public.administrador VALUES (5, 'Sanchez', 'Pass@789', '2024-12-13 02:57:00.591781+00', 'Camila', '2024-12-13 02:57:00.591781+00', 'Camila');
INSERT INTO public.administrador VALUES (6, 'Diaz', 'Secure#101', '2024-12-13 02:57:00.591781+00', 'Sofia', '2024-12-13 02:57:00.591781+00', 'Sofia');


--
-- TOC entry 3396 (class 0 OID 16425)
-- Dependencies: 221
-- Data for Name: aula; Type: TABLE DATA; Schema: public; Owner: postgres
--

--tipo|numero|capacidad|NN|NN|nombre|piso|tieneAire|pizarron|cantPC'S|tieneCanion|tienePC|tieneTelevisor|tieneVentilador

INSERT INTO public.aula VALUES (0, 101, 40, '2024-12-13 02:57:00.60103+00', '2024-12-13 02:57:00.60103+00', 'Aula 1', 1, true, 1, 0, false, false, false, true);
INSERT INTO public.aula VALUES (0, 104, 45, '2024-12-13 02:57:00.60103+00', '2024-12-13 02:57:00.60103+00', 'Aula 4', 1, true, 1, 0, false, false, false, false);


INSERT INTO public.aula VALUES (1, 102, 30, '2024-12-13 02:57:00.60103+00', '2024-12-13 02:57:00.60103+00', 'Aula 2', 1, true, 0, 25, true, true, false, false);
INSERT INTO public.aula VALUES (1, 105, 35, '2024-12-13 02:57:00.60103+00', '2024-12-13 02:57:00.60103+00', 'Aula 5', 2, false, 0, 10, true, true, false, true);

INSERT INTO public.aula VALUES (2, 103, 50, '2024-12-13 02:57:00.60103+00', '2024-12-13 02:57:00.60103+00', 'Aula 3', 2, false, 1, 0, true, false, true, true);
INSERT INTO public.aula VALUES (2, 106, 60, '2024-12-13 02:57:00.60103+00', '2024-12-13 02:57:00.60103+00', 'Aula 6', 3, true, 1, 1, true, true, false, false);


--
-- TOC entry 3390 (class 0 OID 16389)
-- Dependencies: 215
-- Data for Name: bedel; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.bedel VALUES (1, 'Fernandez', 'Bedel@123', '2024-12-13 02:57:00.604224+00', 'Clara', '2024-12-13 02:57:00.604224+00', 'Clara', false, 0);
INSERT INTO public.bedel VALUES (2, 'Rodriguez', 'Clave#789', '2024-12-13 02:57:00.604224+00', 'Sergio', '2024-12-13 02:57:00.604224+00', 'Sergio', false, 1);
INSERT INTO public.bedel VALUES (3, 'Hernandez', 'Turno*456', '2024-12-13 02:57:00.604224+00', 'Ana', '2024-12-13 02:57:00.604224+00', 'Ana', true, 2);
INSERT INTO public.bedel VALUES (4, 'Gutierrez', 'Bedel#202', '2024-12-13 02:57:00.604224+00', 'Ramon', '2024-12-13 02:57:00.604224+00', 'Ramon', false, 0);
INSERT INTO public.bedel VALUES (5, 'Mendez', 'Clave@303', '2024-12-13 02:57:00.604224+00', 'Luz', '2024-12-13 02:57:00.604224+00', 'Luz', false, 1);
INSERT INTO public.bedel VALUES (6, 'Salas', 'Turno#404', '2024-12-13 02:57:00.604224+00', 'Pedro', '2024-12-13 02:57:00.604224+00', 'Pedro', true, 2);


--
-- TOC entry 3391 (class 0 OID 16395)
-- Dependencies: 216
-- Data for Name: dia_reservado; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.dia_reservado VALUES (1, '2024-12-13 02:57:00.617037+00', 60, '2024-12-15', '08:00:00', '2024-12-13 02:57:00.617037+00', 101, 4);
INSERT INTO public.dia_reservado VALUES (2, '2024-12-13 02:57:00.617037+00', 120, '2024-01-16', '10:00:00', '2024-12-13 02:57:00.617037+00', 102, 2);
INSERT INTO public.dia_reservado VALUES (3, '2024-12-13 02:57:00.617037+00', 90, '2024-07-17', '14:00:00', '2024-12-13 02:57:00.617037+00', 103, 3);
INSERT INTO public.dia_reservado VALUES (4, '2024-12-13 02:57:00.617037+00', 60, '2024-12-18', '09:00:00', '2024-12-13 02:57:00.617037+00', 101, 1);
INSERT INTO public.dia_reservado VALUES (5, '2024-12-13 02:57:00.617037+00', 60, '2024-02-19', '11:30:00', '2024-12-13 02:57:00.617037+00', 102, 2);
INSERT INTO public.dia_reservado VALUES (6, '2024-12-13 02:57:00.617037+00', 120, '2024-08-20', '13:00:00', '2024-12-13 02:57:00.617037+00', 103, 3);
INSERT INTO public.dia_reservado VALUES (7, '2024-12-13 02:57:00.617037+00', 60, '2024-12-21', '08:30:00', '2024-12-13 02:57:00.617037+00', 101, 1);
INSERT INTO public.dia_reservado VALUES (8, '2024-12-13 02:57:00.617037+00', 90, '2024-03-22', '12:00:00', '2024-12-13 02:57:00.617037+00', 102, 6);
INSERT INTO public.dia_reservado VALUES (9, '2024-12-13 02:57:00.617037+00', 60, '2024-09-23', '14:00:00', '2024-12-13 02:57:00.617037+00', 103, 3);
INSERT INTO public.dia_reservado VALUES (10, '2024-12-13 02:57:00.617037+00', 90, '2024-12-24', '16:00:00', '2024-12-13 02:57:00.617037+00', 101, 1);
INSERT INTO public.dia_reservado VALUES (11, '2024-12-13 02:57:00.617037+00', 120, '2024-04-25', '18:00:00', '2024-12-13 02:57:00.617037+00', 102, 2);
INSERT INTO public.dia_reservado VALUES (12, '2024-12-13 02:57:00.617037+00', 60, '2024-10-26', '20:00:00', '2024-12-13 02:57:00.617037+00', 103, 6);
INSERT INTO public.dia_reservado VALUES (13, '2024-12-13 02:57:00.617037+00', 90, '2024-12-27', '10:00:00', '2024-12-13 02:57:00.617037+00', 101, 1);
INSERT INTO public.dia_reservado VALUES (14, '2024-12-13 02:57:00.617037+00', 60, '2024-05-28', '08:30:00', '2024-12-13 02:57:00.617037+00', 102, 4);
INSERT INTO public.dia_reservado VALUES (15, '2024-12-13 02:57:00.617037+00', 120, '2024-11-29', '12:30:00', '2024-12-13 02:57:00.617037+00', 103, 3);
INSERT INTO public.dia_reservado VALUES (16, '2024-12-13 02:57:00.617037+00', 60, '2024-12-30', '15:00:00', '2024-12-13 02:57:00.617037+00', 101, 1);
INSERT INTO public.dia_reservado VALUES (17, '2024-12-13 02:57:00.617037+00', 90, '2024-06-20', '17:30:00', '2024-12-13 02:57:00.617037+00', 102, 2);
INSERT INTO public.dia_reservado VALUES (18, '2024-12-13 02:57:00.617037+00', 120, '2025-01-01', '19:30:00', '2024-12-13 02:57:00.617037+00', 103, 5);
INSERT INTO public.dia_reservado VALUES (19, '2024-12-13 02:57:00.617037+00', 90, '2025-01-03', '10:00:00', '2024-12-13 02:57:00.617037+00', 104, 4);
INSERT INTO public.dia_reservado VALUES (20, '2024-12-13 02:57:00.617037+00', 60, '2025-12-10', '08:00:00', '2024-12-13 02:57:00.617037+00', 105, 5);
INSERT INTO public.dia_reservado VALUES (21, '2024-12-13 02:57:00.617037+00', 120, '2025-05-03', '12:00:00', '2024-12-13 02:57:00.617037+00', 106, 6);
INSERT INTO public.dia_reservado VALUES (22, '2024-12-13 02:57:00.617037+00', 60, '2025-01-04', '09:00:00', '2024-12-13 02:57:00.617037+00', 104, 7);
INSERT INTO public.dia_reservado VALUES (23, '2024-12-13 02:57:00.617037+00', 150, '2024-12-05', '11:30:00', '2024-12-13 02:57:00.617037+00', 105, 8);
INSERT INTO public.dia_reservado VALUES (24, '2024-12-13 02:57:00.617037+00', 60, '2025-01-06', '15:00:00', '2024-12-13 02:57:00.617037+00', 106, 9);


--
-- TOC entry 3392 (class 0 OID 16400)
-- Dependencies: 217
-- Data for Name: periodo; Type: TABLE DATA; Schema: public; Owner: postgres
--


INSERT INTO public.periodo VALUES (2, '2024-12-13 02:57:00.606907+00', '2024-06-30', '2024-01-01', '2024-12-13 02:57:00.606907+00', 0);
INSERT INTO public.periodo VALUES (3, '2024-12-13 02:57:00.606907+00', '2024-12-31', '2024-07-01', '2024-12-13 02:57:00.606907+00', 1);
INSERT INTO public.periodo VALUES (1, '2024-12-13 02:57:00.606907+00', '2024-12-31', '2024-01-01', '2024-12-13 02:57:00.606907+00', 2);
INSERT INTO public.periodo VALUES (4, '2024-12-13 02:57:00.606907+00', '2025-06-30', '2025-01-01', '2024-12-13 02:57:00.606907+00', 0);
INSERT INTO public.periodo VALUES (5, '2024-12-13 02:57:00.606907+00', '2025-12-31', '2025-07-01', '2024-12-13 02:57:00.606907+00', 1);
INSERT INTO public.periodo VALUES (6, '2024-12-13 02:57:00.606907+00', '2025-12-31', '2025-01-01', '2024-12-13 02:57:00.606907+00', 2);


--
-- TOC entry 3393 (class 0 OID 16406)
-- Dependencies: 218
-- Data for Name: reserva_esporadica; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.reserva_esporadica VALUES (12, 'López', 40, 'carloslopezk@hotmail.com', '2024-12-13 02:57:00.612485+00', 3, 3, '2024-12-13 02:57:00.612485+00', 'Probabilidad y Estadística', 'Carlos', 2, 6);
INSERT INTO public.reserva_esporadica VALUES (11, 'Gómez', 30, 'gomezmaria88@outlook.com', '2024-12-13 02:57:00.612485+00', 2, 2, '2024-12-13 02:57:00.612485+00', 'Física II', 'María', 1, 5);
INSERT INTO public.reserva_esporadica VALUES (10, 'Pérez', 15, 'jperez@gmail.com', '2024-12-13 02:57:00.612485+00', 1, 1, '2024-12-13 02:57:00.612485+00', 'Análisis Numérico', 'Juan', 0, 4);
INSERT INTO public.reserva_esporadica VALUES (9, 'Lopez', 45, 'carloslopezk@hotmail.com', '2024-12-13 02:57:00.612485+00', 3, 3, '2024-12-13 02:57:00.612485+00', 'Probabilidad y Estadistica', 'Carlos', 2, 3);
INSERT INTO public.reserva_esporadica VALUES (8, 'Gomez', 30, 'gomezmaria88@outlook.com', '2024-12-13 02:57:00.612485+00', 2, 2, '2024-12-13 02:57:00.612485+00', 'Fisica II', 'Maria', 1, 2);
INSERT INTO public.reserva_esporadica VALUES (7, 'Perez', 25, 'jperez@gmail.com', '2024-12-13 02:57:00.612485+00', 1, 1, '2024-12-13 02:57:00.612485+00', 'Analisis Numerico', 'Juan', 0, 1);


--
-- TOC entry 3394 (class 0 OID 16412)
-- Dependencies: 219
-- Data for Name: reserva_periodica; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.reserva_periodica VALUES (1, 'Perez', 35, 'jperez@gmail.com', '2024-12-13 02:57:00.608919+00', 1, 1, '2024-12-13 02:57:00.608919+00', 'Analisis Numerico', 'Juan', 0, 1, 1);
INSERT INTO public.reserva_periodica VALUES (2, 'Gomez', 40, 'gomezmaria88@outlook.com', '2024-12-13 02:57:00.608919+00', 2, 2, '2024-12-13 02:57:00.608919+00', 'Fisica II', 'Maria', 1, 2, 2);
INSERT INTO public.reserva_periodica VALUES (3, 'Lopez', 50, 'carloslopezk@hotmail.com', '2024-12-13 02:57:00.608919+00', 3, 3, '2024-12-13 02:57:00.608919+00', 'Probabilidad y Estadistica', 'Carlos', 2, 3, 3);
INSERT INTO public.reserva_periodica VALUES (4, 'Pérez', 20, 'jperez@gmail.com', '2024-12-13 02:57:00.608919+00', 1, 1, '2024-12-13 02:57:00.608919+00', 'Análisis Numérico', 'Juan', 0, 4, 4);
INSERT INTO public.reserva_periodica VALUES (5, 'Gómez', 45, 'gomezmaria88@outlook.com', '2024-12-13 02:57:00.608919+00', 2, 2, '2024-12-13 02:57:00.608919+00', 'Física II', 'María', 1, 5, 5);
INSERT INTO public.reserva_periodica VALUES (6, 'López', 50, 'carloslopezk@hotmail.com', '2024-12-13 02:57:00.608919+00', 3, 3, '2024-12-13 02:57:00.608919+00', 'Probabilidad y Estadística', 'Carlos', 2, 6, 6);


--
-- TOC entry 3403 (class 0 OID 0)
-- Dependencies: 222
-- Name: primary_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.primary_sequence', 10000, false);


-- Completed on 2024-12-13 03:07:17 UTC

--
-- PostgreSQL database dump complete
--