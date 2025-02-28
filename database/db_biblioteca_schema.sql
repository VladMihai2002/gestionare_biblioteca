--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-02-28 19:02:22

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2 (class 3079 OID 16453)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- TOC entry 4874 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 219 (class 1259 OID 16389)
-- Name: autori; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.autori (
    autorid integer NOT NULL,
    numeautor character varying(100) NOT NULL,
    prenumeautor character varying(100) NOT NULL,
    taraorigine character varying(100)
);


ALTER TABLE public.autori OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16388)
-- Name: autori_autorid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.autori_autorid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.autori_autorid_seq OWNER TO postgres;

--
-- TOC entry 4875 (class 0 OID 0)
-- Dependencies: 218
-- Name: autori_autorid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.autori_autorid_seq OWNED BY public.autori.autorid;


--
-- TOC entry 223 (class 1259 OID 16403)
-- Name: biblioteca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.biblioteca (
    bibliotecaid integer NOT NULL,
    denumire character varying(200) NOT NULL,
    adresa text
);


ALTER TABLE public.biblioteca OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16402)
-- Name: biblioteca_bibliotecaid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.biblioteca_bibliotecaid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.biblioteca_bibliotecaid_seq OWNER TO postgres;

--
-- TOC entry 4876 (class 0 OID 0)
-- Dependencies: 222
-- Name: biblioteca_bibliotecaid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.biblioteca_bibliotecaid_seq OWNED BY public.biblioteca.bibliotecaid;


--
-- TOC entry 221 (class 1259 OID 16396)
-- Name: carti; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.carti (
    carteid integer NOT NULL,
    denumire character varying(200) NOT NULL,
    anaparitie integer,
    editura character varying(150)
);


ALTER TABLE public.carti OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16395)
-- Name: carti_carteid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.carti_carteid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.carti_carteid_seq OWNER TO postgres;

--
-- TOC entry 4877 (class 0 OID 0)
-- Dependencies: 220
-- Name: carti_carteid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.carti_carteid_seq OWNED BY public.carti.carteid;


--
-- TOC entry 224 (class 1259 OID 16411)
-- Name: publicare; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.publicare (
    autorid integer NOT NULL,
    carteid integer NOT NULL,
    tip character varying(100)
);


ALTER TABLE public.publicare OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16426)
-- Name: tiparire; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tiparire (
    carteid integer NOT NULL,
    bibliotecaid integer NOT NULL,
    sectiune character varying(100)
);


ALTER TABLE public.tiparire OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16442)
-- Name: utilizatori; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utilizatori (
    utilizatorid integer NOT NULL,
    numeutilizator character varying(100) NOT NULL,
    parola text NOT NULL,
    rol character varying(10) NOT NULL,
    CONSTRAINT utilizatori_rol_check CHECK (((rol)::text = ANY ((ARRAY['client'::character varying, 'admin'::character varying])::text[])))
);


ALTER TABLE public.utilizatori OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16441)
-- Name: utilizatori_utilizatorid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utilizatori_utilizatorid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utilizatori_utilizatorid_seq OWNER TO postgres;

--
-- TOC entry 4878 (class 0 OID 0)
-- Dependencies: 226
-- Name: utilizatori_utilizatorid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.utilizatori_utilizatorid_seq OWNED BY public.utilizatori.utilizatorid;


--
-- TOC entry 4701 (class 2604 OID 16392)
-- Name: autori autorid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.autori ALTER COLUMN autorid SET DEFAULT nextval('public.autori_autorid_seq'::regclass);


--
-- TOC entry 4703 (class 2604 OID 16406)
-- Name: biblioteca bibliotecaid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.biblioteca ALTER COLUMN bibliotecaid SET DEFAULT nextval('public.biblioteca_bibliotecaid_seq'::regclass);


--
-- TOC entry 4702 (class 2604 OID 16399)
-- Name: carti carteid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carti ALTER COLUMN carteid SET DEFAULT nextval('public.carti_carteid_seq'::regclass);


--
-- TOC entry 4704 (class 2604 OID 16445)
-- Name: utilizatori utilizatorid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilizatori ALTER COLUMN utilizatorid SET DEFAULT nextval('public.utilizatori_utilizatorid_seq'::regclass);


--
-- TOC entry 4707 (class 2606 OID 16394)
-- Name: autori autori_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.autori
    ADD CONSTRAINT autori_pkey PRIMARY KEY (autorid);


--
-- TOC entry 4711 (class 2606 OID 16410)
-- Name: biblioteca biblioteca_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.biblioteca
    ADD CONSTRAINT biblioteca_pkey PRIMARY KEY (bibliotecaid);


--
-- TOC entry 4709 (class 2606 OID 16401)
-- Name: carti carti_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carti
    ADD CONSTRAINT carti_pkey PRIMARY KEY (carteid);


--
-- TOC entry 4713 (class 2606 OID 16415)
-- Name: publicare publicare_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publicare
    ADD CONSTRAINT publicare_pkey PRIMARY KEY (autorid, carteid);


--
-- TOC entry 4715 (class 2606 OID 16430)
-- Name: tiparire tiparire_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tiparire
    ADD CONSTRAINT tiparire_pkey PRIMARY KEY (carteid, bibliotecaid);


--
-- TOC entry 4717 (class 2606 OID 16452)
-- Name: utilizatori utilizatori_numeutilizator_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilizatori
    ADD CONSTRAINT utilizatori_numeutilizator_key UNIQUE (numeutilizator);


--
-- TOC entry 4719 (class 2606 OID 16450)
-- Name: utilizatori utilizatori_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilizatori
    ADD CONSTRAINT utilizatori_pkey PRIMARY KEY (utilizatorid);


--
-- TOC entry 4720 (class 2606 OID 16416)
-- Name: publicare publicare_autorid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publicare
    ADD CONSTRAINT publicare_autorid_fkey FOREIGN KEY (autorid) REFERENCES public.autori(autorid) ON DELETE CASCADE;


--
-- TOC entry 4721 (class 2606 OID 16421)
-- Name: publicare publicare_carteid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publicare
    ADD CONSTRAINT publicare_carteid_fkey FOREIGN KEY (carteid) REFERENCES public.carti(carteid) ON DELETE CASCADE;


--
-- TOC entry 4722 (class 2606 OID 16436)
-- Name: tiparire tiparire_bibliotecaid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tiparire
    ADD CONSTRAINT tiparire_bibliotecaid_fkey FOREIGN KEY (bibliotecaid) REFERENCES public.biblioteca(bibliotecaid) ON DELETE CASCADE;


--
-- TOC entry 4723 (class 2606 OID 16431)
-- Name: tiparire tiparire_carteid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tiparire
    ADD CONSTRAINT tiparire_carteid_fkey FOREIGN KEY (carteid) REFERENCES public.carti(carteid) ON DELETE CASCADE;


-- Completed on 2025-02-28 19:02:22

--
-- PostgreSQL database dump complete
--

