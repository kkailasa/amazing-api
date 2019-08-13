-- Table: public.organizationaldata

-- DROP TABLE public.organizationaldata;

CREATE TABLE public.organizationaldata
(
    id bigserial,
    identifier character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
	ancestor_id bigint,
	created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    CONSTRAINT organizationaldata_pkey PRIMARY KEY (id),
    CONSTRAINT organizationaldata_identifier_unq UNIQUE (identifier),
    CONSTRAINT fk26bf9boid5g04de3c9j9hgiqf FOREIGN KEY (ancestor_id)
        REFERENCES public.organizationaldata (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.organizationaldata
    OWNER to docker;
    
    
-- Table: public.organizationaldatahierarchy

-- DROP TABLE public.organizationaldatahierarchy;

CREATE TABLE public.organizationaldatahierarchy
(
    id bigserial,
    ancestor_id bigint NOT NULL,
    descendant_id bigint NOT NULL,
	depth integer,
	created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    CONSTRAINT organizationaldatahierarchy_pkey PRIMARY KEY (id),
    CONSTRAINT fk1pgyf4r05kvg5hb1jt1niox30 FOREIGN KEY (ancestor_id)
        REFERENCES public.organizationaldata (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fknivxihtr1leg66tlqnj02feei FOREIGN KEY (descendant_id)
        REFERENCES public.organizationaldata (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.organizationaldatahierarchy
    OWNER to docker;
    
CREATE INDEX organizationdata_identifier_idx
    ON public.organizationaldata USING btree
    (identifier ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX organizationaldata_ancestor_idx
    ON public.organizationaldata USING btree
    (ancestor_id ASC NULLS LAST)
    TABLESPACE pg_default;
    
CREATE INDEX organizationaldatahierarchy_ancestor_idx
    ON public.organizationaldatahierarchy USING btree
    (ancestor_id ASC NULLS LAST)
    TABLESPACE pg_default;
    
CREATE INDEX organizationaldatahierarchy_descendant_idx
    ON public.organizationaldatahierarchy USING btree
    (descendant_id ASC NULLS LAST)
    TABLESPACE pg_default;    
   

CREATE OR REPLACE FUNCTION after_change_organizationaldata() 
RETURNS TRIGGER LANGUAGE PLPGSQL AS $$
BEGIN
  IF (TG_OP = 'INSERT') THEN
    INSERT INTO organizationaldatahierarchy(ancestor_id,descendant_id,depth, created_at, updated_at)
    SELECT ancestor_id, NEW.id, depth + 1, current_timestamp, current_timestamp
    FROM organizationaldatahierarchy WHERE descendant_id = NEW.ancestor_id
    UNION ALL 
	SELECT NEW.id, NEW.id, 0, current_timestamp, current_timestamp;
  ELSIF (TG_OP = 'UPDATE') THEN
    -- Move Subtree
    IF OLD.ancestor_id != NEW.ancestor_id THEN
      -- Step 1: Disconnect from current ancestors
	  
      DELETE FROM organizationaldatahierarchy
      WHERE descendant_id IN (SELECT descendant_id FROM organizationaldatahierarchy WHERE ancestor_id = OLD.id)
          AND ancestor_id IN (SELECT ancestor_id   FROM organizationaldatahierarchy WHERE descendant_id = OLD.id AND ancestor_id != descendant_id);

      -- Add new paths
      INSERT INTO organizationaldatahierarchy (ancestor_id, descendant_id, depth, created_at, updated_at)
        SELECT supertree.ancestor_id, subtree.descendant_id, supertree.depth + subtree.depth + 1, current_timestamp, current_timestamp
        FROM organizationaldatahierarchy AS supertree
        CROSS JOIN organizationaldatahierarchy AS subtree
        WHERE subtree.ancestor_id = OLD.id
        AND supertree.descendant_id = NEW.ancestor_id ;

    END IF;
  END IF;
  RETURN NULL;
END;
$$;


CREATE TRIGGER after_change_organizationaldata AFTER INSERT OR UPDATE
  ON organizationaldata FOR EACH ROW EXECUTE PROCEDURE after_change_organizationaldata();
  
  
  
